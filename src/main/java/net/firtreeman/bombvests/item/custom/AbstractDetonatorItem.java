package net.firtreeman.bombvests.item.custom;

import net.firtreeman.bombvests.util.BombVestUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractDetonatorItem extends Item {
    public AbstractDetonatorItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    abstract boolean canDetonate(ItemStack bombVest, ItemStack stack);

    public static void forceDetonate(LivingEntity livingEntity, ItemStack stack) {
        forceDetonate(livingEntity, getFilledBombVest(livingEntity), stack);
    }

    protected static void forceDetonate(LivingEntity livingEntity, ItemStack bombVest, ItemStack stack) {
        handleDetonate(livingEntity, bombVest, stack, true);
    }

    public static boolean tryDetonate(LivingEntity livingEntity, ItemStack stack) {
        return tryDetonate(livingEntity, getFilledBombVest(livingEntity), stack);
    }

    protected static boolean tryDetonate(LivingEntity livingEntity, ItemStack bombVest, ItemStack stack) {
        return handleDetonate(livingEntity, bombVest, stack, false);
    }

    private static boolean handleDetonate(LivingEntity livingEntity, ItemStack bombVest, ItemStack stack, boolean forced) {
        if (bombVest == null || bombVest.isEmpty() || stack != null && stack.isEmpty()) return false;
        if (forced || stack != null && ((AbstractDetonatorItem) stack.getItem()).canDetonate(bombVest, stack)) {
            if (stack != null)
                stack.setCount(0);
            detonate(livingEntity, bombVest);
            return true;
        }

        return false;
    }

    private static void detonate(LivingEntity livingEntity, ItemStack bombVest) {
        ((BombVestItem) bombVest.getItem()).explode(bombVest, livingEntity);
    }

    protected static ItemStack getBombVest(LivingEntity livingEntity) {
        ItemStack armorPiece = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (armorPiece.getItem() instanceof BombVestItem)
            return armorPiece;

        return null;
    }

    protected static ItemStack getFilledBombVest(LivingEntity livingEntity) {
        ItemStack bombVest = getBombVest(livingEntity);
        if (bombVest != null && BombVestUtils.getDynamites(bombVest).length > 0)
            return bombVest;

        return null;
    }
}
