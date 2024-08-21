package net.vg.sleepcycle.util;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.chunk.LevelChunk;
import net.vg.sleepcycle.Constants;
import net.vg.sleepcycle.advancement.ModCriteria;
import net.vg.sleepcycle.config.ModConfigs;
import net.vg.sleepcycle.effect.ModEffects;
import net.vg.sleepcycle.sounds.ModSounds;
import net.vg.sleepcycle.stats.ModStats;

import java.util.*;

public class TimeProgressionHandler {
    private static final Map<ServerLevel, Integer> worldSleepTicks = new HashMap<>();
    private static final Map<ServerPlayer, Integer> playerSleepTicks = new HashMap<>();
    private static final Set<ServerPlayer> sleepingPlayers = new HashSet<>();
    private static Integer originalTickSpeed = null;
    private static final int SLEEP_ADVANCEMENT_DURATION = 6000; // 5 minutes in ticks (20 ticks * 60 seconds * 5 minutes) = 6000

    public static void register() {
        TickEvent.SERVER_LEVEL_POST.register(TimeProgressionHandler::onWorldTick);
        PlayerEvent.PLAYER_QUIT.register(TimeProgressionHandler::onPlayerDisconnect);
//        LifecycleEvent.SERVER_LEVEL_LOAD.register(TimeProgressionHandler::onWorldLoad);
    }

    public static void addWorld(ServerLevel world) {
        Constants.LOGGER.info("World being Added");
        if (!worldSleepTicks.containsKey(world)) {
            originalTickSpeed = world.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        }
        worldSleepTicks.putIfAbsent(world, 0);
    }

    public static void removeWorld(ServerLevel world) {
        worldSleepTicks.remove(world);
        // Reset the tick speed
        if (originalTickSpeed != null) {
            world.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).set(originalTickSpeed, world.getServer());
        }
    }

    private static void onWorldTick(ServerLevel world) {
        if (worldSleepTicks.containsKey(world)) {
            List<ServerPlayer> players = world.players();
            int playerCount = players.size();

            double playersRequiredToSleepRatio = world.getServer().getGameRules().getInt(GameRules.RULE_PLAYERS_SLEEPING_PERCENTAGE) / 100d;
            int playersRequiredToSleep = (int) Math.ceil(playersRequiredToSleepRatio * playerCount);
            long sleepingPlayerCount = players.stream().filter(ServerPlayer::isSleeping).count();

            for (ServerPlayer player : players) {
                if (player.isSleeping()) {
                    sleepingPlayers.add(player);
                    playerSleepTicks.putIfAbsent(player, 0);
                    if (ModConfigs.DO_REGEN) {
                        if (!player.hasEffect(MobEffects.REGENERATION)) {
                            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0)); // 2 minutes
                            if (player.getHealth() != player.getMaxHealth()) {
                                player.awardStat(ModStats.HEALTH_REGAINED.value(), 1);
                            }
                        }
                    }

                    int playerTicksAsleep = playerSleepTicks.get(player);
                    if (ModConfigs.GRANT_BUFFS) {
                        if (!player.hasEffect(ModEffects.getWellRestedHolder()) && playerTicksAsleep >= ModConfigs.WELL_RESTED_WAIT && playerTicksAsleep < ModConfigs.TIRED_WAIT) {
                            player.addEffect(new MobEffectInstance(ModEffects.getWellRestedHolder(), ModConfigs.WELL_RESTED_LENGTH, 0), player);
                            playSound(player, ModSounds.WELL_RESTED_SOUND.get());
                        } else if (!player.hasEffect(ModEffects.getTiredHolder()) && playerTicksAsleep >= ModConfigs.TIRED_WAIT) {
                            player.addEffect(new MobEffectInstance(ModEffects.getTiredHolder(), ModConfigs.TIRED_LENGTH, 0), player);
                            playSound(player, ModSounds.TIRED_SOUND.get());
                        }



                    }

                    // Check for advancement condition
                    if (playerTicksAsleep >= SLEEP_ADVANCEMENT_DURATION) {
                        ModCriteria.getSleepCriterion().trigger(player); // Trigger the custom criterion
                    }

                    playerSleepTicks.put(player, playerTicksAsleep + 1); // Increment player sleep ticks
                    player.awardStat(ModStats.TIME_SLEPT.value(), 1);

                } else {
                    // Reset player's sleep ticks if they are not sleeping
                    if (player.hasEffect(ModEffects.getWellRestedHolder())) {
                        player.awardStat(ModStats.WELL_RESTED_SLEEPS.value(), 1);
                    }
                    if (player.hasEffect(ModEffects.getTiredHolder())) {
                        player.awardStat(ModStats.TIRED_SLEEPS.value(), 1);
                    }
                    playerSleepTicks.remove(player);
                    sleepingPlayers.remove(player);
                }
            }

            if (sleepingPlayerCount >= playersRequiredToSleep) {
                if (ModConfigs.CHANGE_TICK_SPEED && sleepingPlayerCount > 0) {
                    if (originalTickSpeed != null && world.getGameRules().getInt(GameRules.RULE_RANDOMTICKING) == originalTickSpeed) {
                        world.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).set((int) (originalTickSpeed * ModConfigs.DAY_SKIP_SPEED * ModConfigs.SLEEP_TICK_MULTIPLIER), world.getServer());
                        System.out.println("Increasing world tick speed");
                    }
                }

                int ticksAsleep = worldSleepTicks.get(world);
                long timeIncrement = calculateTimeIncrement(ticksAsleep);

                world.setDayTime(world.getDayTime() + timeIncrement); // Adjust time
                for (int i = 0; i < timeIncrement; i++) {
                    Minecraft.getInstance().levelRenderer.tick();
                }

                if (ticksAsleep % 20 == 0) { // Tick chunks less frequently
                    tickChunks(world);
                }

                worldSleepTicks.put(world, ticksAsleep + 1); // Increment sleep ticks
            } else {
                if (originalTickSpeed != null) {
                    world.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).set(originalTickSpeed, world.getServer());
                }
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

    private static void playSound(ServerPlayer player, SoundEvent soundId) {
        player.level().playSound(
                null, // Player that hears the sound
                player.blockPosition(), // Position of the sound
                soundId,
                SoundSource.NEUTRAL, // This determines which slider affects this sound
                1.0F, // Volume multiplier, 1 is normal, 0.5 is half volume, etc
                1.0F // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
        );
    }

    private static void tickChunks(ServerLevel world) {
        ServerChunkCache chunkManager = world.getChunkSource();
        chunkManager.tick(() -> true, true);
    }

    private static void onPlayerDisconnect(ServerPlayer player) {
        System.out.println("SleepCycle: on player disconnect");

        for (ServerLevel world : player.server.getAllLevels()) {
            List<ServerPlayer> players = world.players();
            long sleepingPlayerCount = players.stream().filter(ServerPlayer::isSleeping).count();

            if (sleepingPlayers.contains(player)) {
                playerSleepTicks.remove(player);
                sleepingPlayers.remove(player);

                removeWorld(world);
                System.out.println("Removing the World");
            }

            if (originalTickSpeed != null) {
                world.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).set(originalTickSpeed, player.server);
                System.out.println("Resetting world tick speed");
            }
        }
    }

}
