package net.vg.sleepcycle.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.vg.sleepcycle.SleepCycle;

public class ModEffects {

    public static final RegistryEntry<StatusEffect> WELL_RESTED;
    public static final RegistryEntry<StatusEffect> TIRED;

    static {
        WELL_RESTED = register("well_rested", new WellRestedEffect(StatusEffectCategory.BENEFICIAL, 13458603));
        TIRED = register("tired", new TiredEffect(StatusEffectCategory.HARMFUL, 2039587));
    }

    private static RegistryEntry<StatusEffect> register(String id, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(SleepCycle.MOD_ID, id), effect);
    }

    public static void registerEffects() {
        // This method ensures the static block runs and registers the effects
        SleepCycle.LOGGER.info("Registering Effects for " + SleepCycle.MOD_ID);
    }
}
