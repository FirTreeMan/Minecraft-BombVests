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

    public static void forceDetonate(LivingEntity livingEntity, ItemStack bombVest) {
        ((BombVestItem) bombVest.getItem()).explode(bombVest, livingEntity);
    }

    abstract boolean canDetonate(ItemStack bombVest, ItemStack stack);

    public boolean tryDetonate(LivingEntity livingEntity, ItemStack stack) {
        return tryDetonate(livingEntity, getFilledBombVest(livingEntity), stack);
    }

    protected boolean tryDetonate(LivingEntity livingEntity, ItemStack bombVest, ItemStack stack) {
        if (bombVest == null) return false;
        if (canDetonate(bombVest, stack)) {
            detonate(livingEntity, bombVest);
            return true;
        }
        return false;
    }

    private void detonate(LivingEntity livingEntity, ItemStack bombVest) {
        ((BombVestItem) bombVest.getItem()).explode(bombVest, livingEntity);
    }

    protected ItemStack getBombVest(LivingEntity livingEntity) {
        ItemStack armorPiece = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (armorPiece.getItem() instanceof BombVestItem)
            return armorPiece;

        return null;
    }

    protected ItemStack getFilledBombVest(LivingEntity livingEntity) {
        ItemStack bombVest = getBombVest(livingEntity);
        if (bombVest != null && BombVestUtils.getDynamites(bombVest).length > 0)
            return bombVest;

        return null;
    }
}
