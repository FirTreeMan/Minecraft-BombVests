package net.firtreeman.bombvests.item.custom;

import net.firtreeman.bombvests.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static net.firtreeman.bombvests.util.DetonatorUtils.isPrimed;
import static net.firtreeman.bombvests.util.DetonatorUtils.setPrimed;

public class ToggleDetonatorItem extends AbstractDetonatorItem{
    public ToggleDetonatorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    boolean canDetonate(ItemStack bombVest, ItemStack stack) {
        return isPrimed(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (getFilledBombVest(pPlayer) != null) {
            setPrimed(stack, !isPrimed(stack));

            pLevel.playSeededSound(null, pPlayer.getX(), pPlayer.getY() + 1.0F, pPlayer.getZ(),
                    ModSounds.SOUND_SHORT_BEEP.get(), SoundSource.PLAYERS, 1.0F, 1.0F, 0);
        }

        return InteractionResultHolder.success(stack);
    }
}
