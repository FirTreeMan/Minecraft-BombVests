package net.firtreeman.bombvests.util;

import net.firtreeman.bombvests.config.ServerConfig;
import net.firtreeman.bombvests.item.ModItems;
import net.firtreeman.bombvests.item.custom.ArmorBombVestItem;
import net.firtreeman.bombvests.item.custom.NormalBombVestItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BombVestUtils {
    public static String TAG_DYNAMITES = "Dynamites";
    public static String TAG_TIMER = "Timer";
    public static String TAG_TIMER_MAX = "TimerMax";
    public static String TAG_SOUND_TIMING = "TimerSoundTracker";

    public static int[] DESIRED_BEEP_TIMES = Arrays.stream(new int[]{
//            0, 80, 195, 241, 283, 316, 343, 366, 385, 403, 418, 432, 445, 457, 467, 478, 487, 496, 504, 512, 520, 527, 534, 540, 547, 553, 558, 564, 569,
            0, 80, 173, 228, 267, 298, 322, 343, 361, 377, 392, 405, 416, 427, 437, 447, 455, 464, 471, 479, 486, 492, 498, 504, 510, 516, 521, 526, 531, 536, 541, 545, 549, 553, 557, 561, 565, 569, 573,
    }).map(s -> s * 2).toArray();
    public static Map<DYNAMITE_TYPES, Item> DYNAMITE_MAP = Map.ofEntries(
            Map.entry(DYNAMITE_TYPES.DYNAMITE, ModItems.DYNAMITE.get()),
            Map.entry(DYNAMITE_TYPES.VOLATILE_DYNAMITE, ModItems.VOLATILE_DYNAMITE.get()),
            Map.entry(DYNAMITE_TYPES.HIGH_EXPLOSIVE, ModItems.HIGH_EXPLOSIVE.get()),
            Map.entry(DYNAMITE_TYPES.SHRAPNEL, ModItems.SHRAPNEL.get()),
            Map.entry(DYNAMITE_TYPES.PACKED, ModItems.PACKED.get())
    );

    public static HashMap<DYNAMITE_TYPES, Integer> getDynamiteCounts(ItemStack stack) {
        HashMap<DYNAMITE_TYPES, Integer> map = new HashMap<>();
        DYNAMITE_TYPES[] dynamites = getDynamites(stack);

        for (DYNAMITE_TYPES dynamite: dynamites)
            map.put(dynamite, map.getOrDefault(dynamite, 0) + 1);

        return map;
    }

    public static float getExplosionRadius(ItemStack stack) {
        float[] powers = getExplosionPower(stack);
        float logInput = powers[0];
        float flatRadius = powers[1];
        if (logInput <= 0.0F && flatRadius <= 0.0F) return 0.0F;

        double logValue = Math.log(logInput / 10 + 1) / Math.log(ServerConfig.EXPLOSION_RADIUS_LOG_BASE.get());

        return (float) (ServerConfig.EXPLOSION_RADIUS_LOG_SCALAR.get() * logValue + flatRadius);
//        return (float) (5 * Math.log10(logInput) + 2);
    }

    public static float[] getExplosionPower(ItemStack stack) {
        DYNAMITE_TYPES[] dynamites = getDynamites(stack);
        if (dynamites.length == 0) return new float[]{0.0F, 0.0F};

        float power = 0.0F;
        float flat_power = 0.0F;
        for (DYNAMITE_TYPES dynamite: dynamites) {
            power += switch (dynamite) {
                case DYNAMITE -> ServerConfig.DYNAMITE_EXPLOSION_VALUE.get().floatValue();
                case VOLATILE_DYNAMITE -> ServerConfig.VOLATILE_DYNAMITE_EXPLOSION_VALUE.get().floatValue();
                case HIGH_EXPLOSIVE -> ServerConfig.HIGH_EXPLOSIVE_EXPLOSION_VALUE.get().floatValue();
                case SHRAPNEL -> ServerConfig.SHRAPNEL_EXPLOSION_VALUE.get().floatValue();
                default -> 0.0F;
            };
            flat_power += switch (dynamite) {
                case PACKED -> ServerConfig.PACKED_EXPLOSION_VALUE.get().floatValue();
                default -> 0.0F;
            };
        }

        return new float[]{power, flat_power};
    }

    public static float getDamageExplosionChance(ItemStack stack, boolean isDead) {
        HashMap<DYNAMITE_TYPES, Integer> dynamiteCounts = getDynamiteCounts(stack);
        int volatiles = dynamiteCounts.getOrDefault(DYNAMITE_TYPES.VOLATILE_DYNAMITE, 0);
        int packeds = dynamiteCounts.getOrDefault(DYNAMITE_TYPES.PACKED, 0);
        int maxDynamite = getMaxDynamiteCount(stack) > 0 ? getMaxDynamiteCount(stack) : 10;

        if (isDead && volatiles > 0)
            return 1.0F;
        if (stack.getItem() instanceof ArmorBombVestItem && stack.getMaxDamage() - stack.getDamageValue() <= 2)
            return 1.0F;

        return (float) Math.pow((float) (volatiles + packeds) / maxDynamite, 1.6F);
    }

    public static float getShrapnelChance(ItemStack stack) {
        int shrapnel = getDynamiteCounts(stack).getOrDefault(DYNAMITE_TYPES.SHRAPNEL, 0);
        int maxDynamite = getMaxDynamiteCount(stack) > 0 ? getMaxDynamiteCount(stack) : 10;

        // max 50%
        return (float) shrapnel / (maxDynamite * 2);
    }

    public static DYNAMITE_TYPES[] getDynamites(ItemStack stack) {
        if (!stack.hasTag()) return new DYNAMITE_TYPES[]{};

        CompoundTag tag = stack.getTag();
        return Arrays.stream(tag.getIntArray(TAG_DYNAMITES)).mapToObj(s -> DYNAMITE_TYPES.values()[s]).toArray(DYNAMITE_TYPES[]::new);
    }

    public static void setDynamites(ItemStack stack, DYNAMITE_TYPES[] dynamites) {
        int[] newDynamites = Arrays.stream(dynamites).mapToInt(DYNAMITE_TYPES::ordinal).toArray();
        stack.getOrCreateTag().putIntArray(TAG_DYNAMITES, newDynamites);
    }

    public static boolean addDynamite(ItemStack stack, DYNAMITE_TYPES dynamiteType) {
        if (getDynamites(stack).length >= getMaxDynamiteCount(stack)) return false;

        CompoundTag tag = stack.getOrCreateTag();

        int[] currentDynamites = tag.getIntArray(TAG_DYNAMITES);
        int[] newDynamites = new int[getCurrentDynamiteCount(stack) + 1];
        System.arraycopy(currentDynamites, 0, newDynamites, 0, currentDynamites.length);
        newDynamites[newDynamites.length - 1] = dynamiteType.ordinal();

        tag.putIntArray(TAG_DYNAMITES, newDynamites);

        return true;
    }

    public static ItemStack removeDynamite(ItemStack stack) {
        if (getDynamites(stack).length > 0)
            return removeDynamite(stack, getDynamites(stack)[0], -1);
        return new ItemStack(ModItems.DYNAMITE.get(), 0);
    }

    public static ItemStack removeDynamite(ItemStack stack, DYNAMITE_TYPES dynamiteToRemove, int maxRemoved) {
        DYNAMITE_TYPES[] dynamites = getDynamites(stack);
        DYNAMITE_TYPES[] filteredDynamites = Arrays.stream(dynamites).filter(s -> s != dynamiteToRemove).toArray(DYNAMITE_TYPES[]::new);
        int outAmt = dynamites.length - filteredDynamites.length;

        // if unable to extract all dynamite to the target slot
        if (maxRemoved > -1 && outAmt > maxRemoved) {
            int addAmt = outAmt - maxRemoved;
            outAmt -= maxRemoved;

            DYNAMITE_TYPES[] newFilteredDynamites = new DYNAMITE_TYPES[filteredDynamites.length + addAmt];
            System.arraycopy(filteredDynamites, 0, newFilteredDynamites, 0, filteredDynamites.length);
            for (int i = filteredDynamites.length; i < newFilteredDynamites.length; i++)
                newFilteredDynamites[i] = dynamiteToRemove;
            filteredDynamites = newFilteredDynamites;
        }

        if (filteredDynamites.length == 0)
            clearTimer(stack);

        setDynamites(stack, filteredDynamites);
        return new ItemStack(DYNAMITE_MAP.get(dynamiteToRemove), outAmt);
    }

    public static void clearDynamite(ItemStack stack) {
        if (!stack.hasTag()) return;

        stack.getTag().remove(TAG_DYNAMITES);
        clearTimer(stack);
    }

    public static int getMaxDynamiteCount(ItemStack stack) {
        if (stack.getItem() instanceof NormalBombVestItem bombVestItem)
            return bombVestItem.getMaxDynamite();
        return 0;
    }

    public static int getCurrentDynamiteCount(ItemStack stack) {
        return getDynamites(stack).length;
    }

    public static void setTimer(ItemStack stack) {
        stack.getOrCreateTag().putInt(TAG_TIMER, 0);
        stack.getOrCreateTag().putInt(TAG_TIMER_MAX, 600 * 2);
        stack.getOrCreateTag().putInt(TAG_SOUND_TIMING, 0);
    }

    public static void clearTimer(ItemStack stack) {
        if (hasTimer(stack)) {
            stack.getTag().remove(TAG_TIMER);
            stack.getTag().remove(TAG_TIMER_MAX);
            stack.getTag().remove(TAG_SOUND_TIMING);
        }
    }

    public static boolean hasTimer(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getInt(TAG_TIMER_MAX) != 0;
    }

    public static BEEP_TYPES tickTimer(ItemStack stack) {
        if (!stack.hasTag()) return BEEP_TYPES.NO_BEEP;

        CompoundTag tag = stack.getTag();

        int soundIndex = tag.getInt(TAG_SOUND_TIMING);
        int time = tag.getInt(TAG_TIMER);
        BEEP_TYPES beepType = BEEP_TYPES.NO_BEEP;

        if (soundIndex < DESIRED_BEEP_TIMES.length && DESIRED_BEEP_TIMES[soundIndex] == time) {
            tag.putInt(TAG_SOUND_TIMING, soundIndex + 1);
            beepType = BEEP_TYPES.SHORT_BEEP;
        }

        if (time == 575 * 2)
            beepType = BEEP_TYPES.LONG_BEEP;
        if (time == 600 * 2)
            beepType = BEEP_TYPES.EXPLODE;

        tag.putInt(TAG_TIMER, time + 1);

        return beepType;
    }

    public enum BEEP_TYPES {
        NO_BEEP,
        SHORT_BEEP,
        LONG_BEEP,
        EXPLODE,
    }

}
