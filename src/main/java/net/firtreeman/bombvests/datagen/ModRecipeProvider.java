package net.firtreeman.bombvests.datagen;

import net.firtreeman.bombvests.BombVests;
import net.firtreeman.bombvests.item.ModItems;
import net.firtreeman.bombvests.item.custom.BombVestItem;
import net.firtreeman.bombvests.util.ModTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

//    public static final BombVestItem[] BOMB_VEST_ITEMS = Arrays.stream(new Item[]{
//            ModItems.BOMB_VEST.get(),
//            ModItems.IRON_BOMB_VEST.get(),
//            ModItems.GOLD_BOMB_VEST.get(),
//            ModItems.DIAMOND_BOMB_VEST.get(),
//    }).map(BombVestItem.class::cast).toList().toArray(new BombVestItem[0]);

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
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
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.DYNAMITE.get())
                .requires(Items.BAMBOO)
                .requires(Items.GUNPOWDER)
                .requires(Items.REDSTONE)
                .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                .save(pWriter, getFormattedRecipeName("dynamite_from_bamboo"));

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

    private String getFormattedRecipeName(String recipeName) {
        return BombVests.MOD_ID + ":" + recipeName;
    }
}
