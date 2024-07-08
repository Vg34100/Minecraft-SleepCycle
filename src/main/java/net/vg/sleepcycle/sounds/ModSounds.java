package net.vg.sleepcycle.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.vg.sleepcycle.SleepCycle;

public class ModSounds extends SoundEvents {

    public static final SoundEvent WELL_RESTED_SOUND = registerSoundEvent("well_rested"); // A positive sound for getting the well rested effect
    public static final SoundEvent TIRED_SOUND = registerSoundEvent("tired"); // A negative sound for getting the tired effect


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(SleepCycle.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        SleepCycle.LOGGER.info("Registering Sounds for " + SleepCycle.MOD_ID);
    }
}
