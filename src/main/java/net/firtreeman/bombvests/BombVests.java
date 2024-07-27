package net.firtreeman.bombvests;

import com.mojang.logging.LogUtils;
import net.firtreeman.bombvests.item.ModItems;
import net.firtreeman.bombvests.item.custom.*;
import net.firtreeman.bombvests.sound.ModSounds;
import net.firtreeman.bombvests.util.BombVestUtils;
import net.firtreeman.bombvests.util.DetonatorUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import static net.firtreeman.bombvests.util.GroupingUtils.ARMOR_TO_BOMB;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BombVests.MOD_ID)
public class BombVests {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "bombvests";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public BombVests() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.DEAD_MANS_SWITCH.get(),
                    new ResourceLocation(BombVests.MOD_ID, "primed"),
                        (stack, level, living, id) -> DetonatorUtils.isPrimed(stack) ? 1.0F : 0.0F);
            ItemProperties.register(ModItems.FAILSAFE.get(),
                    new ResourceLocation(BombVests.MOD_ID, "primed"),
                        (stack, level, living, id) -> DetonatorUtils.isPrimed(stack) ? 1.0F : 0.0F);
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static class BombVestListener {
        public static HashSet<Player> beepingPlayers = new HashSet<>();

        @SubscribeEvent
        public static void onAnvilUpdate(AnvilUpdateEvent event) {
            ItemStack bombVest = event.getLeft();
            ItemStack chestplate = event.getRight();

            if (!(bombVest.getItem() instanceof NormalBombVestItem)) return;
            if (!ARMOR_TO_BOMB.containsKey(chestplate.getItem())) return;

            ItemStack output = new ItemStack(ARMOR_TO_BOMB.get(chestplate.getItem()));
            BombVestUtils.setDynamites(output, BombVestUtils.getDynamites(bombVest));
            output.setDamageValue((int) (chestplate.getDamageValue() * 0.9));

            event.setOutput(output);
            event.setCost(1);
        }

        @SubscribeEvent
        public static void onLivingDeath(LivingDeathEvent event) {
            if (event.getEntity() instanceof Player player) {
                beepingPlayers.remove(player);
                if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof BombVestItem)
                    for (ItemStack itemStack : player.getInventory().items)
                        if (itemStack.getItem() instanceof FailSafeItem failSafeItem)
                            failSafeItem.tryDetonate(player, itemStack);
            }
        }

        @SubscribeEvent
        public static void onChangeEquipment(LivingEquipmentChangeEvent event) {
            if (event.getEntity() instanceof Player player) {
                if (event.getSlot() == EquipmentSlot.CHEST) {
                    if (event.getFrom().getItem() instanceof BombVestItem && BombVestUtils.hasTimer(event.getFrom()))
                        beepingPlayers.remove(player);
                    if (event.getTo().getItem() instanceof BombVestItem && BombVestUtils.hasTimer(event.getTo()))
                        beepingPlayers.add(player);
                }
            }
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            for (Player player: beepingPlayers) {
                switch (BombVestUtils.tickTimer(player.getItemBySlot(EquipmentSlot.CHEST))) {
                    case SHORT_BEEP -> player.level().playSeededSound(null, player.getX(), player.getY() + 1.0F, player.getZ(),
                            ModSounds.SOUND_SHORT_BEEP.get(), SoundSource.PLAYERS, 1.5F, 1.0F, 0);
                    case LONG_BEEP -> player.level().playSeededSound(null, player.getX(), player.getY() + 1.0F, player.getZ(),
                            ModSounds.SOUND_LONG_BEEP.get(), SoundSource.PLAYERS, 1.5F, 1.0F, 0);
                    case EXPLODE -> AbstractDetonatorItem.forceDetonate(player, player.getItemBySlot(EquipmentSlot.CHEST));
                }
            }
        }
    }
}