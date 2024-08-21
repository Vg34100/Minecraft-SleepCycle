package net.vg.sleepcycle.stats;

import com.mojang.datafixers.kinds.Const;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.SleepCycle;


public class ModStats {



//    public static final ResourceLocation TIME_SLEPT = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "time_slept"); // Counts the amount of time the user sleeps
//    public static final ResourceLocation WELL_RESTED_SLEEPS = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "well_rested_sleeps"); // Counts the amount of times the user has gotten a well rested sleep
//    public static final ResourceLocation TIRED_SLEEPS = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "tired_sleeps"); // Counts the amount of times the user has gotten a tired sleep
//    public static final ResourceLocation HEALTH_REGAINED = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "health_regained"); // Counts the amount of health the user has regained while sleeping

    private static final DeferredRegister<ResourceLocation> CUSTOM_STATS = DeferredRegister.create(Constants.MOD_ID, Registries.CUSTOM_STAT);
    public static final RegistrySupplier<ResourceLocation> EXAMPLE_ITEM = CUSTOM_STATS.register("well_rested_sleeps", () -> ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "well_rested_sleeps"));


    public static final RegistrySupplier<ResourceLocation> TIME_SLEPT = CUSTOM_STATS.register("time_slept",
            () -> makeCustomStat("time_slept", StatFormatter.TIME));
    public static final RegistrySupplier<ResourceLocation> WELL_RESTED_SLEEPS = CUSTOM_STATS.register("well_rested_sleeps",
            () -> makeCustomStat("well_rested_sleeps", StatFormatter.DEFAULT));
    public static final RegistrySupplier<ResourceLocation> TIRED_SLEEPS = CUSTOM_STATS.register("tired_sleeps",
            () -> makeCustomStat("tired_sleeps", StatFormatter.DEFAULT));
    public static final RegistrySupplier<ResourceLocation> HEALTH_REGAINED = CUSTOM_STATS.register("health_regained",
            () -> makeCustomStat("health_regained", StatFormatter.DEFAULT));

    private static ResourceLocation makeCustomStat(String name, StatFormatter formatter) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        Stats.CUSTOM.get(id, formatter);
        return id;
    }

    private static void registerCustomStat(ResourceLocation id, StatFormatter statFormatter) {
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        Stats.CUSTOM.get(id, statFormatter);
    }

    public static void register() {
        CUSTOM_STATS.register();
    }
}
