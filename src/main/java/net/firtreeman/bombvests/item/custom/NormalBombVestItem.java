package net.firtreeman.bombvests.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.firtreeman.bombvests.util.BombVestUtils.*;

public class NormalBombVestItem extends BombVestItem {
    private final int maxDynamite;

    public NormalBombVestItem(ArmorMaterial pMaterial, Properties pProperties) {
        this(pMaterial, pProperties, 10);
    }

    public NormalBombVestItem(ArmorMaterial pMaterial, Properties pProperties, int maxDynamite) {
        super(pMaterial, pProperties);
        this.maxDynamite = maxDynamite;
    }

    public int getMaxDynamite() {
        return maxDynamite;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return (int) Math.min(0 + 13.0F * getCurrentDynamiteCount(pStack) / getMaxDynamiteCount(pStack), 13);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer)) {
            if (!pSlot.hasItem())
                pSlot.set(removeDynamite(pStack));
            else if (pSlot.getItem().getItem() instanceof DynamiteItem dynamiteItem) {
                int maxIncrementable = pSlot.getMaxStackSize() - pSlot.getItem().getCount();
                if (maxIncrementable == 0) return false;

                pSlot.getItem().grow(removeDynamite(pStack, dynamiteItem.getDynamiteType(), maxIncrementable).getCount());
            }
            else return false;

            return true;
        }

        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer))
            if (!pOther.isEmpty() && pOther.getItem() instanceof DynamiteItem dynamiteItem)
                if (addDynamite(pStack, dynamiteItem.getDynamiteType())) {
                    pOther.shrink(1);
                    return true;
                }

        return false;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal(String.format("%d/%d", getDynamites(pStack).length, getMaxDynamite())));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
