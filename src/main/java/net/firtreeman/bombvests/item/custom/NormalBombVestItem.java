package net.firtreeman.bombvests.item.custom;

import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

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
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        if (pAction == ClickAction.SECONDARY && pSlot.allowModification(pPlayer))
            if (!pOther.isEmpty() && pOther.getItem() instanceof DynamiteItem dynamiteItem)
                if (addDynamite(pStack, dynamiteItem.getDynamiteType())) {
                    pOther.shrink(1);
                    return true;
                }

        return false;
    }
}
