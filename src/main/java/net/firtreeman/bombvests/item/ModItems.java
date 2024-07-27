package net.firtreeman.bombvests.item;

import net.firtreeman.bombvests.BombVests;
import net.firtreeman.bombvests.item.custom.*;
import net.firtreeman.bombvests.util.DYNAMITE_TYPES;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BombVests.MOD_ID);

    public static final RegistryObject<Item> BOMB_VEST = ITEMS.register("bomb_vest", () -> new NormalBombVestItem(ModArmorMaterials.BOMB_VEST, new Item.Properties()));
    public static final RegistryObject<Item> IRON_BOMB_VEST = ITEMS.register("iron_bomb", () -> new ArmorBombVestItem(ModArmorMaterials.IRON_BOMB, new Item.Properties()));
    public static final RegistryObject<Item> GOLD_BOMB_VEST = ITEMS.register("gold_bomb", () -> new ArmorBombVestItem(ModArmorMaterials.GOLD_BOMB, new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_BOMB_VEST = ITEMS.register("diamond_bomb", () -> new ArmorBombVestItem(ModArmorMaterials.DIAMOND_BOMB, new Item.Properties()));

    public static final RegistryObject<Item> DYNAMITE = ITEMS.register("dynamite", () -> new DynamiteItem(new Item.Properties(), DYNAMITE_TYPES.DYNAMITE));

    public static final RegistryObject<Item> DETONATOR = ITEMS.register("detonator", () -> new DetonatorItem(new Item.Properties()));
    public static final RegistryObject<Item> DEAD_MANS_SWITCH = ITEMS.register("dead_mans_switch", () -> new DeadMansSwitchItem(new Item.Properties()));
    public static final RegistryObject<Item> FAILSAFE = ITEMS.register("failsafe", () -> new FailSafeItem(new Item.Properties()));
    public static final RegistryObject<Item> TIMED_DETONATOR = ITEMS.register("timed_detonator", () -> new TimedDetonatorItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
