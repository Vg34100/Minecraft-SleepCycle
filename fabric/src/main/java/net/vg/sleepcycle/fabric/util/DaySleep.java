package net.vg.sleepcycle.fabric.util;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.world.InteractionResult;
import net.vg.sleepcycle.config.ModConfigs;

public class DaySleep {

    public static void init() {
        // Allows daytime sleeping
        EntitySleepEvents.ALLOW_SLEEP_TIME.register(((player, sleepingPos, vanillaResult) -> {
            if (ModConfigs.ALLOW_DAY_SLEEPING) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }));
    }
}
