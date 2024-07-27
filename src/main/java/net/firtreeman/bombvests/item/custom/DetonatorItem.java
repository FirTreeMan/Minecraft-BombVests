package net.firtreeman.bombvests.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DetonatorItem extends AbstractDetonatorItem{
    public DetonatorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    boolean canDetonate(ItemStack bombVest, ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        tryDetonate(pPlayer, stack);

        return InteractionResultHolder.success(stack);
    }
}
