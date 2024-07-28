package net.firtreeman.bombvests.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.firtreeman.bombvests.BombVests;
import net.firtreeman.bombvests.datagen.ModRecipeProvider;
import net.firtreeman.bombvests.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

import static net.firtreeman.bombvests.datagen.ModRecipeProvider.BOMB_ANVIL_RECIPES;

@JeiPlugin
public class JEIBombVestsPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(BombVests.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory recipeFactory = registration.getVanillaRecipeFactory();
        List<IJeiAnvilRecipe> anvilRecipes = BOMB_ANVIL_RECIPES.keySet().stream().map(
                (input) -> recipeFactory.createAnvilRecipe(new ItemStack(ModItems.BOMB_VEST.get()),
                        List.of(new ItemStack(input)),
                        List.of(new ItemStack(BOMB_ANVIL_RECIPES.get(input))),
                        new ResourceLocation(BombVests.MOD_ID, ModRecipeProvider.getJeiRecipeName(BOMB_ANVIL_RECIPES.get(input)))
                )
        ).toList();

        registration.addRecipes(RecipeTypes.ANVIL, anvilRecipes);

        IModPlugin.super.registerRecipes(registration);
    }
}
