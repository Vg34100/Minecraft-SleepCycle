package net.vg.sleepcycle.util;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.util.ActionResult;
import net.vg.sleepcycle.config.ModConfigs;

public class DaySleep {

    public static void init() {
        // Allows daytime sleeping
        EntitySleepEvents.ALLOW_SLEEP_TIME.register(((player, sleepingPos, vanillaResult) -> {
            if (ModConfigs.ALLOW_DAY_SLEEPING) {
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }));
    }
}
