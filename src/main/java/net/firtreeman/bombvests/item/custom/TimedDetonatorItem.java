package net.firtreeman.bombvests.item.custom;

import net.firtreeman.bombvests.util.BombVestUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.firtreeman.bombvests.util.BombVestUtils.setTimer;

public class TimedDetonatorItem extends AbstractDetonatorItem {
    public TimedDetonatorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    boolean canDetonate(ItemStack bombVest, ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack bombVest = getFilledBombVest(pPlayer);
        if (bombVest != null)
            setTimer(bombVest);

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
