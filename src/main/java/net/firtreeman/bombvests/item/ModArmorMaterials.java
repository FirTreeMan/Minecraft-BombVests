package net.firtreeman.bombvests.item;

import net.firtreeman.bombvests.BombVests;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    BOMB_VEST("bomb_vest_armor", new int[]{0, 9999, 0, 0}, new int[]{0, 0, 0, 0}, 1, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.05F, () -> Ingredient.of(Items.LEATHER)),
    IRON_BOMB("iron_bomb", new int[]{0, (int) (240 * 0.9), 0, 0}, new int[]{0, 5, 0, 0}, 1, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.05F, () -> Ingredient.of(Items.IRON_INGOT)),
    GOLD_BOMB("gold_bomb", new int[]{0, (int) (112 * 0.9), 0, 0}, new int[]{0, 4, 0, 0}, 1, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.05F, () -> Ingredient.of(Items.GOLD_INGOT)),
    DIAMOND_BOMB("diamond_bomb", new int[]{0, (int) (528 * 0.9), 0, 0}, new int[]{0, 7, 0, 0}, 1, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.05F, () -> Ingredient.of(Items.DIAMOND));

    private final String name;
    private final int[] durabilityAmounts;
    private final int[] protAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResist;
    private final Supplier<Ingredient> repairIngredient;

    ModArmorMaterials(String name, int[] durabilityAmounts, int[] protAmounts, int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResist, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityAmounts = durabilityAmounts;
        this.protAmounts = protAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResist = knockbackResist;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return durabilityAmounts[type.ordinal()];
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return protAmounts[type.ordinal()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public String getName() {
        return BombVests.MOD_ID + ":" + name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResist;
    }
}
