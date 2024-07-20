package net.vg.sleepcycle.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.GameRules;
import net.vg.sleepcycle.advancement.ModCriteria;
import net.vg.sleepcycle.config.ModConfigs;
import net.vg.sleepcycle.effect.ModEffects;
import net.vg.sleepcycle.sounds.ModSounds;
import net.vg.sleepcycle.stats.ModStats;

import java.util.*;

public class TimeProgressionHandler {
    private static final Map<ServerWorld, Integer> worldSleepTicks = new HashMap<>();
    private static final Map<ServerPlayerEntity, Integer> playerSleepTicks = new HashMap<>();
    private static final Set<ServerPlayerEntity> sleepingPlayers = new HashSet<>();
    private static int originalTickSpeed;
    private static final int SLEEP_ADVANCEMENT_DURATION = 6000; // 5 minutes in ticks (20 ticks * 60 seconds * 5 minutes)

    public static void register() {
        ServerTickEvents.START_WORLD_TICK.register(TimeProgressionHandler::onWorldTick);
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> onPlayerDisconnect(handler.getPlayer(), server));
    }

    public static void addWorld(ServerWorld world) {
        if (!worldSleepTicks.containsKey(world)) {
            originalTickSpeed = world.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED);
        }
        worldSleepTicks.putIfAbsent(world, 0);
    }

    public static void removeWorld(ServerWorld world) {
        worldSleepTicks.remove(world);
        // Reset the tick speed
        world.getGameRules().get(GameRules.RANDOM_TICK_SPEED).set(originalTickSpeed, world.getServer());
    }

    private static void onWorldTick(ServerWorld world) {
        if (worldSleepTicks.containsKey(world)) {
            List<ServerPlayerEntity> players = world.getPlayers();
            int playerCount = players.size();

            double playersRequiredToSleepRatio = world.getServer().getGameRules().getInt(GameRules.PLAYERS_SLEEPING_PERCENTAGE) / 100d;
            int playersRequiredToSleep = (int) Math.ceil(playersRequiredToSleepRatio * playerCount);
            long sleepingPlayerCount = players.stream().filter(ServerPlayerEntity::isSleeping).count();

            for (ServerPlayerEntity player : players) {
                if (player.isSleeping()) {
                    sleepingPlayers.add(player);
                    playerSleepTicks.putIfAbsent(player, 0);
                    if (ModConfigs.DO_REGEN) {
                        if (!player.hasStatusEffect(StatusEffects.REGENERATION)) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0)); // 2 minutes
                            if (player.getHealth() != player.getMaxHealth()) {
                                player.increaseStat(ModStats.HEALTH_REGAINED, 1);
                            }
                        }
                    }

                    int playerTicksAsleep = playerSleepTicks.get(player);
                    if (ModConfigs.GRANT_BUFFS) {
                        if (!player.hasStatusEffect(ModEffects.WELL_RESTED) && playerTicksAsleep >= ModConfigs.WELL_RESTED_WAIT && playerTicksAsleep < ModConfigs.TIRED_WAIT) {
                            player.addStatusEffect(new StatusEffectInstance(ModEffects.WELL_RESTED, ModConfigs.WELL_RESTED_LENGTH, 0));
                            playSound(player, ModSounds.WELL_RESTED_SOUND);

                        } else if (!player.hasStatusEffect(ModEffects.TIRED) && playerTicksAsleep >= ModConfigs.TIRED_WAIT) {
                            player.addStatusEffect(new StatusEffectInstance(ModEffects.TIRED, ModConfigs.TIRED_LENGTH, 0));
                            playSound(player, ModSounds.TIRED_SOUND);
                        }
                    }


                    // Check for advancement condition
                    if (playerTicksAsleep >= SLEEP_ADVANCEMENT_DURATION) {
                        ModCriteria.SLEEP.trigger(player); // Trigger the custom criterion
                    }

                    playerSleepTicks.put(player, playerTicksAsleep + 1); // Increment player sleep ticks
                    player.increaseStat(ModStats.TIME_SLEPT, 1);

                } else {
                    // Reset player's sleep ticks if they are not sleeping
                    if (player.hasStatusEffect(ModEffects.WELL_RESTED)) {
                        player.increaseStat(ModStats.WELL_RESTED_SLEEPS, 1);
                    }
                    if (player.hasStatusEffect(ModEffects.TIRED)) {
                        player.increaseStat(ModStats.TIRED_SLEEPS, 1);
                    }
                    playerSleepTicks.remove(player);
                    sleepingPlayers.remove(player);
                }
            }

            if (sleepingPlayerCount >= playersRequiredToSleep) {
                if (ModConfigs.CHANGE_TICK_SPEED) {
                    if (world.getGameRules().getInt(GameRules.RANDOM_TICK_SPEED) == originalTickSpeed) {
                        world.getGameRules().get(GameRules.RANDOM_TICK_SPEED).set((int) (originalTickSpeed * ModConfigs.DAY_SKIP_SPEED * ModConfigs.SLEEP_TICK_SPEED), world.getServer());
                    }
                }

                int ticksAsleep = worldSleepTicks.get(world);
                long timeIncrement = calculateTimeIncrement(ticksAsleep);

                world.setTimeOfDay(world.getTimeOfDay() + timeIncrement); // Adjust time
                for (int i = 0; i < timeIncrement; i++) {
                    MinecraftClient.getInstance().worldRenderer.tick();
                }

                if (ticksAsleep % 20 == 0) { // Tick chunks less frequently
                    tickChunks(world);
                }

                worldSleepTicks.put(world, ticksAsleep + 1); // Increment sleep ticks
            } else {
                world.getGameRules().get(GameRules.RANDOM_TICK_SPEED).set(originalTickSpeed, world.getServer());
                if (sleepingPlayerCount == 0) {
                    removeWorld(world);
                }
            }
        }
    }

    private static long calculateTimeIncrement(int ticksAsleep) {
        // Calculate the time increment with a wind-up effect
        return (long) (ModConfigs.DAY_SKIP_SPEED * Math.min(1, ticksAsleep / 100.0));
    }

    private static void playSound(ServerPlayerEntity player, SoundEvent soundId) {
        player.getWorld().playSound(
                null, // Player that hears the sound
                player.getBlockPos(), // Position of the sound
                soundId,
                SoundCategory.NEUTRAL, // This determines which slider affects this sound
                1.0F, // Volume multiplier, 1 is normal, 0.5 is half volume, etc
                1.0F // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
        );
    }

    private static void tickChunks(ServerWorld world) {
        ServerChunkManager chunkManager = world.getChunkManager();
        chunkManager.tick(() -> true, true);
    }

    private static void onPlayerDisconnect(ServerPlayerEntity player, MinecraftServer server) {
        System.out.println("SleepCycle: on player disconnect");

        for (ServerWorld world : server.getWorlds()) {
            List<ServerPlayerEntity> players = world.getPlayers();
            long sleepingPlayerCount = players.stream().filter(ServerPlayerEntity::isSleeping).count();

            if (sleepingPlayers.contains(player)) {
                playerSleepTicks.remove(player);
                sleepingPlayers.remove(player);

                if (sleepingPlayerCount == 0) {
                    removeWorld(world);
                }
            }
            world.getGameRules().get(GameRules.RANDOM_TICK_SPEED).set(originalTickSpeed, server);

        }
    }
}
