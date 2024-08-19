package net.vg.sleepcycle.neoforge.util;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;

public class DaySleep {
    @SubscribeEvent
    public void onCanSleep(CanPlayerSleepEvent event) {
        if (event.getProblem() == Player.BedSleepingProblem.NOT_POSSIBLE_NOW) {
            event.setProblem(null);
        }
    }

    @SubscribeEvent
    public void onCanContinueSleeping(CanContinueSleepingEvent event) {
        if (event.getProblem() == Player.BedSleepingProblem.NOT_POSSIBLE_NOW) {
            event.setContinueSleeping(true);
        }
    }
}
