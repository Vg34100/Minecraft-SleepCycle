package net.vg.sleepcycle.neoforge;

import net.neoforged.fml.common.Mod;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.CanContinueSleepingEvent;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.SleepCycle;
import net.vg.sleepcycle.neoforge.util.DaySleep;

@Mod(Constants.MOD_ID)
public final class SleepCycleNeoForge {
    public SleepCycleNeoForge() {
        // Run our common setup.
        SleepCycle.init();
        NeoForge.EVENT_BUS.register(new DaySleep());
    }

    private void onSleepingTimeCheck(CanContinueSleepingEvent event) {
        // Allow sleeping at any time
        event.setContinueSleeping(true);
//        event.setContinueSleeping(true);
    }
}
