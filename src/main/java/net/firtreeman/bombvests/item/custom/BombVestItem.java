package net.firtreeman.bombvests.item.custom;

import net.firtreeman.bombvests.config.ServerConfig;
import net.firtreeman.bombvests.util.DYNAMITE_TYPES;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

import static net.firtreeman.bombvests.util.BombVestUtils.*;
import static net.firtreeman.bombvests.util.BombVestUtils.DYNAMITE_MAP;

public class BombVestItem extends ArmorItem {
    public BombVestItem(ArmorMaterial pMaterial, Properties pProperties) {
        super(pMaterial, Type.CHESTPLATE, pProperties);
    }

    public void explode(ItemStack stack, LivingEntity livingEntity) {
        Level.ExplosionInteraction explosionInteraction;
        if (ServerConfig.EXPLODE_BLOCKS.get())
            explosionInteraction = Level.ExplosionInteraction.MOB;
        else explosionInteraction = Level.ExplosionInteraction.NONE;

        livingEntity.level().explode(livingEntity, livingEntity.getX(), livingEntity.getY() + 1.0F, livingEntity.getZ(), getExplosionRadius(stack), explosionInteraction);

        if (livingEntity instanceof Player player && player.getAbilities().instabuild)
            return;

        clearDynamite(stack);
        stack.shrink(1);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        HashMap<DYNAMITE_TYPES, Integer> dynamites = getDynamiteCounts(pStack);
        for (DYNAMITE_TYPES dynamite: dynamites.keySet()) {
            Component dynamiteName = Component.translatable(DYNAMITE_MAP.get(dynamite).getDescriptionId());
            Component tooltip = Component.literal(dynamiteName.getString() + " x " + dynamites.get(dynamite)).withStyle(ChatFormatting.YELLOW);
            pTooltipComponents.add(tooltip);
        }

        if (Screen.hasShiftDown()) {
            Component translatable = Component.translatable("tooltip.bombvests.bomb_vest_explosive_power");
            float[] explosionPowers = getExplosionPower(pStack);
            pTooltipComponents.add(Component.literal(translatable.getString() + String.format("%.1f", explosionPowers[0] + explosionPowers[1])).withStyle(ChatFormatting.AQUA));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
