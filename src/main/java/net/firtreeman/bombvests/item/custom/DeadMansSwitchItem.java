package net.firtreeman.bombvests.item.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DeadMansSwitchItem extends ToggleDetonatorItem {
    public DeadMansSwitchItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        tryDetonate(player, item);

        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof LivingEntity livingEntity) {
            if (livingEntity.getOffhandItem() != pStack && !pIsSelected)
                tryDetonate(livingEntity, pStack);
        }

        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }
}
