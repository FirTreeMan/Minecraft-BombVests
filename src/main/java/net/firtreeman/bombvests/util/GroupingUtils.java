package net.firtreeman.bombvests.util;

import net.firtreeman.bombvests.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Map;

public class GroupingUtils {
    public static Map<Item, Item> ARMOR_TO_BOMB = Map.ofEntries(
            Map.entry(Items.IRON_CHESTPLATE, ModItems.IRON_BOMB_VEST.get()),
            Map.entry(Items.GOLDEN_CHESTPLATE, ModItems.GOLD_BOMB_VEST.get()),
            Map.entry(Items.DIAMOND_CHESTPLATE, ModItems.DIAMOND_BOMB_VEST.get())
    );
}
