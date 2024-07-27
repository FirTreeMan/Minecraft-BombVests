package net.firtreeman.bombvests.util;

import net.firtreeman.bombvests.item.custom.NormalBombVestItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class BombVestUtils {
    public static String TAG_DYNAMITES = "Dynamites";
    public static String TAG_TIMER = "Timer";
    public static String TAG_TIMER_MAX = "TimerMax";
    public static String TAG_SOUND_TIMING = "TimerSoundTracker";

    public static int[] DESIRED_BEEP_TIMES = Arrays.stream(new int[]{
//            0, 80, 195, 241, 283, 316, 343, 366, 385, 403, 418, 432, 445, 457, 467, 478, 487, 496, 504, 512, 520, 527, 534, 540, 547, 553, 558, 564, 569,
            0, 80, 173, 228, 267, 298, 322, 343, 361, 377, 392, 405, 416, 427, 437, 447, 455, 464, 471, 479, 486, 492, 498, 504, 510, 516, 521, 526, 531, 536, 541, 545, 549, 553, 557, 561, 565, 569, 573,
    }).map(s -> s * 2).toArray();

    public static float getExplosionRadius(ItemStack stack) {
        DYNAMITE_TYPES[] dynamites = getDynamites(stack);
        if (dynamites.length == 0) return 0.0F;

        return (float) (5 * Math.log10(dynamites.length) + 2);
    }

    public static DYNAMITE_TYPES[] getDynamites(ItemStack stack) {
        if (!stack.hasTag()) return new DYNAMITE_TYPES[]{};

        CompoundTag tag = stack.getTag();
        return Arrays.stream(tag.getIntArray(TAG_DYNAMITES)).mapToObj(s -> DYNAMITE_TYPES.values()[s]).toList().toArray(new DYNAMITE_TYPES[0]);
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

    public static void clearDynamite(ItemStack stack) {
        if (!stack.hasTag()) return;

        stack.getTag().remove(TAG_DYNAMITES);
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
