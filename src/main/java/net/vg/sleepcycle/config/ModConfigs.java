package net.vg.sleepcycle.config;

import com.mojang.datafixers.util.Pair;
import net.vg.sleepcycle.SleepCycle;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static boolean ALLOW_DAY_SLEEPING;
    public static boolean CHANGE_TICK_SPEED;
    public static boolean CHANGE_CAMERA_POS;
    public static double DAY_SKIP_SPEED;
    public static double SLEEP_TICK_SPEED;


    public static boolean GRANT_BUFFS;
    public static boolean DO_REGEN;
    public static int SLEEP_BUTTON_HEIGHT;

    public static int WELL_RESTED_WAIT;
    public static int TIRED_WAIT;
    public static int WELL_RESTED_LENGTH;
    public static int TIRED_LENGTH;


    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(SleepCycle.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("sleep.allow.day.sleep", true), "bool");
        configs.addKeyValuePair(new Pair<>("sleep.day.skip.speed", 25.0d), "float/double");
        configs.addKeyValuePair(new Pair<>("sleep.sleep.tick.speed", 100.0d), "float/double");
        configs.addKeyValuePair(new Pair<>("sleep.well.rested.wait", 500), "int");
        configs.addKeyValuePair(new Pair<>("sleep.tired.wait", 800), "int");
        configs.addKeyValuePair(new Pair<>("sleep.well.rested.length", 2400), "int");
        configs.addKeyValuePair(new Pair<>("sleep.tired.length", 600), "int");
        configs.addKeyValuePair(new Pair<>("sleep.grant.buffs", true), "bool");
        configs.addKeyValuePair(new Pair<>("sleep.do.regeneration", true), "bool");
        configs.addKeyValuePair(new Pair<>("sleep.change.tick.speed", true), "bool");
        configs.addKeyValuePair(new Pair<>("sleep.sleep.button.height", 30), "int");
        configs.addKeyValuePair(new Pair<>("sleep.change.camera.pos", true), "bool");
    }

    private static void assignConfigs() {
        ALLOW_DAY_SLEEPING = CONFIG.getOrDefault("sleep.allow.day.sleep", true);
        DAY_SKIP_SPEED = CONFIG.getOrDefault("sleep.day.skip.speed", 25.0d);
        SLEEP_TICK_SPEED = CONFIG.getOrDefault("sleep.sleep.tick.speed", 100.0d);
        WELL_RESTED_WAIT = CONFIG.getOrDefault("sleep.well.rested.wait", 500);
        TIRED_WAIT = CONFIG.getOrDefault("sleep.tired.wait", 800);
        WELL_RESTED_LENGTH = CONFIG.getOrDefault("sleep.well.rested.length", 2400);
        TIRED_LENGTH = CONFIG.getOrDefault("sleep.tired.length", 600);
        GRANT_BUFFS = CONFIG.getOrDefault("sleep.grant.buffs", true);
        DO_REGEN = CONFIG.getOrDefault("sleep.do.regeneration", true);
        CHANGE_TICK_SPEED = CONFIG.getOrDefault("sleep.change.tick.speed", true);
        SLEEP_BUTTON_HEIGHT = CONFIG.getOrDefault("sleep.sleep.button.height", 30);
        CHANGE_CAMERA_POS = CONFIG.getOrDefault("sleep.change.camera.pos", true);

        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }

    public static void saveConfigs() {
        CONFIG.save();
    }
}