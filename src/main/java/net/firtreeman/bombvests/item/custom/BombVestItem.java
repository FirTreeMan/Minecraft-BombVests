package net.firtreeman.bombvests.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.firtreeman.bombvests.util.BombVestUtils.clearDynamite;
import static net.firtreeman.bombvests.util.BombVestUtils.getExplosionRadius;

public class BombVestItem extends ArmorItem {
    public BombVestItem(ArmorMaterial pMaterial, Properties pProperties) {
        super(pMaterial, Type.CHESTPLATE, pProperties);
    }

    public void explode(ItemStack stack, LivingEntity livingEntity) {
        livingEntity.level().explode(null, livingEntity.getX(), livingEntity.getY() + 1.0F, livingEntity.getZ(), getExplosionRadius(stack), Level.ExplosionInteraction.MOB);
        clearDynamite(stack);
        stack.shrink(1);
    }
}
