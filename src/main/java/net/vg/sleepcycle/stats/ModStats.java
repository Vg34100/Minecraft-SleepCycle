package net.vg.sleepcycle.stats;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.vg.sleepcycle.SleepCycle;

public class ModStats {
    public static final Identifier TIME_SLEPT = Identifier.of(SleepCycle.MOD_ID, "time_slept"); // Counts the amount of time the user sleeps
    public static final Identifier WELL_RESTED_SLEEPS = Identifier.of(SleepCycle.MOD_ID, "well_rested_sleeps"); // Counts the amount of times the user has gotten a well rested sleep
    public static final Identifier TIRED_SLEEPS = Identifier.of(SleepCycle.MOD_ID, "tired_sleeps"); // Counts the amount of times the user has gotten a tired sleep
    public static final Identifier HEALTH_REGAINED = Identifier.of(SleepCycle.MOD_ID, "health_regained"); // Counts the amount of health the user has regained while sleeping

    private static void registerCustomStat(Identifier id, StatFormatter statFormatter) {
        Registry.register(Registries.CUSTOM_STAT, id, id);
        Stats.CUSTOM.getOrCreateStat(id, statFormatter);
    }

    public static void register() {
        // Register custom stats
        registerCustomStat(TIME_SLEPT, StatFormatter.TIME);
        registerCustomStat(WELL_RESTED_SLEEPS, StatFormatter.DEFAULT);
        registerCustomStat(TIRED_SLEEPS, StatFormatter.DEFAULT);
        registerCustomStat(HEALTH_REGAINED, StatFormatter.DEFAULT);
    }
}
