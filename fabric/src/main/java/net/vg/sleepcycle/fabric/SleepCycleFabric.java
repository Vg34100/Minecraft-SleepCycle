package net.vg.sleepcycle.fabric;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.vg.sleepcycle.SleepCycle;
import net.vg.sleepcycle.fabric.util.DaySleep;

public final class SleepCycleFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        SleepCycle.init();





        DaySleep.init();
    }
}
