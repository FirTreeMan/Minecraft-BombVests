package net.firtreeman.bombvests.datagen;

import net.firtreeman.bombvests.BombVests;
import net.firtreeman.bombvests.item.ModItems;
import net.firtreeman.bombvests.item.custom.ToggleDetonatorItem;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BombVests.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item> item: ModItems.ITEMS.getEntries()) {
            if (item.get() instanceof ToggleDetonatorItem)
                makeToggleItem(item, "primed");
            else
                makeItem(item);
        }
    }

    private ItemModelBuilder buildItemModel(String path) {
        return withExistingParent(path,
                new ResourceLocation("item/generated"))
                    .texture("layer0",
                            new ResourceLocation(BombVests.MOD_ID, "item/" + path));
    }

    private ItemModelBuilder makeItem(RegistryObject<Item> item) {
        return buildItemModel(item.getId().getPath());
    }

    private ItemModelBuilder makeToggleItem(RegistryObject<Item> item, String property) {
        // make "on" model
        buildItemModel(item.getId().getPath() + "_on");
        // make default model
        return makeItem(item)
                .override()
                .model(new ModelFile.UncheckedModelFile(modLoc("item/" + item.getId().getPath() + "_on")))
                .predicate(new ResourceLocation(BombVests.MOD_ID, property), 1.0F)
                .end();
    }
}
