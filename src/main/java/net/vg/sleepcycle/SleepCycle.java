package net.vg.sleepcycle;

import net.fabricmc.api.ModInitializer;

import net.minecraft.advancement.criterion.Criteria;
import net.vg.sleepcycle.advancement.ModCriteria;
import net.vg.sleepcycle.config.ModConfigs;
import net.vg.sleepcycle.effect.ModEffects;
import net.vg.sleepcycle.sounds.ModSounds;
import net.vg.sleepcycle.stats.ModStats;
import net.vg.sleepcycle.util.DaySleep;
import net.vg.sleepcycle.util.TimeProgressionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SleepCycle implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "sleepcycle";
	public static final String MOD_NAME = "Sleep Cycle";

	public static final Logger LOGGER = LoggerFactory.getLogger("sleepcycle");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModConfigs.registerConfigs();
		ModEffects.registerEffects();
		ModSounds.registerSounds();
		ModCriteria.register();

		TimeProgressionHandler.register();

		ModStats.register();
		DaySleep.init();


		LOGGER.info("Initializing Mod: " + MOD_NAME);

	}
}