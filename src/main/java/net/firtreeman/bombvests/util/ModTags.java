package net.firtreeman.bombvests.util;

import net.firtreeman.bombvests.BombVests;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> DETONATORS = tag("detonators");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(BombVests.MOD_ID, name));
        }
    }
}
