package net.firtreeman.bombvests.sound;

import net.firtreeman.bombvests.BombVests;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BombVests.MOD_ID);

    public static final RegistryObject<SoundEvent> SOUND_SHORT_BEEP = registerSoundEvents("sound_short_beep");
    public static final RegistryObject<SoundEvent> SOUND_LONG_BEEP = registerSoundEvents("sound_long_beep");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(BombVests.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
