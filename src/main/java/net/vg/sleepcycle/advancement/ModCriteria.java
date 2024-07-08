package net.vg.sleepcycle.advancement;

import net.minecraft.advancement.criterion.Criteria;

public class ModCriteria {
    public static final SleepCriterion SLEEP = new SleepCriterion();

    public static void register() {
        Criteria.register("tutorialmod.sleep", SLEEP);
    }
}
