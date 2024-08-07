package net.firtreeman.bombvests.datagen;

import net.firtreeman.bombvests.BombVests;
import net.firtreeman.bombvests.item.ModItems;
import net.firtreeman.bombvests.item.custom.BombVestItem;
import net.firtreeman.bombvests.util.ModTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public static final Map<Item, Item> DETONATOR_ITEMS = Map.ofEntries(
            Map.entry(ModItems.DETONATOR.get(), Items.REDSTONE),
            Map.entry(ModItems.DEAD_MANS_SWITCH.get(), Items.LAPIS_LAZULI),
            Map.entry(ModItems.FAILSAFE.get(), Items.EMERALD),
            Map.entry(ModItems.TIMED_DETONATOR.get(), Items.HONEYCOMB)
    );

    public static final Map<Item, Item> BOMB_ANVIL_RECIPES = Map.ofEntries(
            Map.entry(Items.IRON_CHESTPLATE, ModItems.IRON_BOMB_VEST.get()),
            Map.entry(Items.GOLDEN_CHESTPLATE, ModItems.GOLD_BOMB_VEST.get()),
            Map.entry(Items.DIAMOND_CHESTPLATE, ModItems.DIAMOND_BOMB_VEST.get())
    );

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    public static String getJeiRecipeName(ItemLike out) {
        return "jei_plugin" + getSimpleRecipeName(out);
    }

    public static String getJeiRecipeName(ItemLike out, ItemLike in) {
        return "jei_plugin" + getConversionRecipeName(out, in);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BOMB_VEST.get())
                .pattern("L L")
                .pattern("LSL")
                .pattern("GGG")
                .define('L', Items.LEATHER)
                .define('S', Items.STRING)
                .define('G', Items.GUNPOWDER)
                .unlockedBy(getHasName(Items.LEATHER), has(Items.LEATHER))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.DYNAMITE.get(), 6)
                .requires(Items.TNT)
                .unlockedBy(getHasName(Items.TNT), has(Items.TNT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DYNAMITE.get())
                .pattern(" G ")
                .pattern(" B ")
                .pattern(" R ")
                .define('G', Items.GUNPOWDER)
                .define('B', Items.BAMBOO)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                .save(pWriter, getFormattedConversionRecipeName(ModItems.DYNAMITE.get(), Items.BAMBOO));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.VOLATILE_DYNAMITE.get(), 2)
                .pattern(" G ")
                .pattern("RBR")
                .pattern("   ")
                .define('G', Items.GUNPOWDER)
                .define('B', Items.BAMBOO)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.HIGH_EXPLOSIVE.get())
                .pattern(" I ")
                .pattern("GBG")
                .pattern(" R ")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GUNPOWDER)
                .define('B', Items.BAMBOO)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.HIGH_EXPLOSIVE.get())
                .requires(ModItems.DYNAMITE.get())
                .requires(Items.GUNPOWDER)
                .requires(Items.IRON_INGOT)
                .unlockedBy(getHasName(ModItems.DYNAMITE.get()), has(ModItems.DYNAMITE.get()))
                .save(pWriter, getFormattedConversionRecipeName(ModItems.HIGH_EXPLOSIVE.get(), ModItems.DYNAMITE.get()));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SHRAPNEL.get())
                .pattern("III")
                .pattern("IDI")
                .pattern("III")
                .define('I', Items.IRON_NUGGET)
                .define('D', ModItems.DYNAMITE.get())
                .unlockedBy(getHasName(ModItems.DYNAMITE.get()), has(ModItems.DYNAMITE.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.PACKED.get())
                .pattern("SHS")
                .pattern("HHH")
                .pattern("SHS")
                .define('S', Items.SANDSTONE)
                .define('H', ModItems.HIGH_EXPLOSIVE.get())
                .unlockedBy(getHasName(ModItems.HIGH_EXPLOSIVE.get()), has(ModItems.HIGH_EXPLOSIVE.get()))
                .save(pWriter);

        DETONATOR_ITEMS.forEach((detonator, determiner) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, detonator)
                .pattern(" D ")
                .pattern(" # ")
                .pattern(" # ")
                .define('D', determiner)
                .define('#', Items.IRON_INGOT)
                .unlockedBy(getHasName(determiner), has(determiner))
                .save(pWriter);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, detonator)
                .requires(ModTags.Items.DETONATORS)
                .requires(determiner)
                .unlockedBy(getHasName(determiner), has(determiner))
                .save(pWriter, getFormattedRecipeName(getSimpleRecipeName(detonator) + "_from_detonators"));
        });
    }

    private String getFormattedConversionRecipeName(ItemLike out, ItemLike in) {
        return getFormattedRecipeName(getConversionRecipeName(out, in));
    }

    private String getFormattedRecipeName(String recipeName) {
        return BombVests.MOD_ID + ":" + recipeName;
    }
}
