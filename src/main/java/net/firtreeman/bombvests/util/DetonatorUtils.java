package net.firtreeman.bombvests.util;

import net.minecraft.world.item.ItemStack;

public class DetonatorUtils {
    public static String TAG_PRIMED = "Primed";


    public static boolean isPrimed(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(TAG_PRIMED);
    }

    public static void setPrimed(ItemStack stack, boolean primed) {
        stack.getOrCreateTag().putBoolean(TAG_PRIMED, primed);
    }
}
