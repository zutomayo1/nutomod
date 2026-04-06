package com.nutonmod.world.system;

import com.nutonmod.block.EnergyCoreBlock;
import com.nutonmod.entity.ModEntities;
import com.nutonmod.entity.RiftStalker;
import com.nutonmod.item.ModItems;
import com.nutonmod.world.dimension.ModDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class StormObeliskEventSystem {
    private static final long EVENT_COOLDOWN_TICKS = 20L * 60L * 4L;
    private static final long EVENT_DURATION_TICKS = 20L * 90L;
    private static final long WAVE_INTERVAL_TICKS = 20L * 18L;
    private static final int MAX_WAVE = 4;
    private static final int START_COST = 2;
    private static final Map<String, Long> LAST_TRIGGER = new HashMap<>();
    private static final Map<String, ActiveObeliskEvent> ACTIVE_EVENTS = new HashMap<>();
    private static final BlockPos[] SPAWN_RING = new BlockPos[] {
            new BlockPos(5, 0, 0),
            new BlockPos(-5, 0, 0),
            new BlockPos(0, 0, 5),
            new BlockPos(0, 0, -5),
            new BlockPos(6, 0, 6),
            new BlockPos(-6, 0, 6),
            new BlockPos(6, 0, -6),
            new BlockPos(-6, 0, -6)
    };

    private StormObeliskEventSystem() {
    }

    public static ActionResult handleCoreUse(ServerWorld world, BlockPos corePos, PlayerEntity player) {
        if (!world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return ActionResult.SUCCESS;
        }

        String key = eventKey(world, corePos);
        long now = world.getTime();
        if (ACTIVE_EVENTS.containsKey(key)) {
            player.sendMessage(Text.literal("Storm Obelisk already active"), true);
            return ActionResult.SUCCESS;
        }

        long last = LAST_TRIGGER.getOrDefault(key, Long.MIN_VALUE / 4L);
        long remain = EVENT_COOLDOWN_TICKS - (now - last);
        if (remain > 0) {
            player.sendMessage(Text.literal("Obelisk stabilizing: " + Math.max(1L, remain / 20L) + "s"), true);
            return ActionResult.SUCCESS;
        }

        if (!consumeStormFragments(player, START_COST)) {
            player.sendMessage(Text.literal("Need storm_fragment x" + START_COST + " to start obelisk defense"), true);
            return ActionResult.SUCCESS;
        }

        BlockState state = world.getBlockState(corePos);
        if (state.contains(EnergyCoreBlock.ACTIVATED) && !state.get(EnergyCoreBlock.ACTIVATED)) {
            world.setBlockState(corePos, state.with(EnergyCoreBlock.ACTIVATED, true), 3);
        }

        ActiveObeliskEvent event = new ActiveObeliskEvent(
                key,
                corePos.toImmutable(),
                player.getUuid(),
                now + EVENT_DURATION_TICKS,
                now + WAVE_INTERVAL_TICKS,
                1
        );
        ACTIVE_EVENTS.put(key, event);
        LAST_TRIGGER.put(key, now);
        spawnWave(world, event);
        player.sendMessage(Text.literal("Storm Obelisk defense started: hold your ground for 90s"), false);
        return ActionResult.SUCCESS;
    }

    public static void tick(ServerWorld world) {
        if (!world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return;
        }

        long now = world.getTime();
        Iterator<Map.Entry<String, ActiveObeliskEvent>> it = ACTIVE_EVENTS.entrySet().iterator();
        while (it.hasNext()) {
            ActiveObeliskEvent event = it.next().getValue();
            if (!event.key.startsWith(world.getRegistryKey().getValue() + ":")) {
                continue;
            }

            if (now >= event.endTime) {
                finishEvent(world, event);
                it.remove();
                continue;
            }

            if (event.wave < MAX_WAVE && now >= event.nextWaveTime) {
                event.wave++;
                event.nextWaveTime = now + WAVE_INTERVAL_TICKS;
                spawnWave(world, event);
            }
        }
    }

    private static void finishEvent(ServerWorld world, ActiveObeliskEvent event) {
        PlayerEntity player = world.getServer().getPlayerManager().getPlayer(event.starter);
        if (player == null) {
            return;
        }

        boolean sameDimension = player.getWorld().getRegistryKey().equals(world.getRegistryKey());
        boolean inRange = player.getBlockPos().isWithinDistance(event.corePos, 28.0);
        if (player.isAlive() && sameDimension && inRange) {
            player.giveItemStack(new ItemStack(ModItems.STORM_FRAGMENT, 5));
            player.giveItemStack(new ItemStack(ModItems.STORM_ALLOY, 1));
            if (event.wave >= MAX_WAVE) {
                player.giveItemStack(new ItemStack(ModItems.CRYSTAL_MATRIX, 1));
            }
            player.giveItemStack(new ItemStack(ModItems.ALTAR_SHARD, 1));
            player.addExperience(100);
            player.sendMessage(Text.literal("Storm Obelisk defended: rewards granted"), false);
        } else {
            player.sendMessage(Text.literal("Storm Obelisk defense failed: stay nearby until completion"), true);
        }
    }

    private static void spawnWave(ServerWorld world, ActiveObeliskEvent event) {
        int spawnCount = Math.min(SPAWN_RING.length, 3 + event.wave);
        for (int i = 0; i < spawnCount; i++) {
            BlockPos spawnPos = event.corePos.add(SPAWN_RING[i].getX(), 1, SPAWN_RING[i].getZ());
            spawnMob(world, spawnPos, event.wave, i == 0 && event.wave >= 3);
        }

        PlayerEntity player = world.getServer().getPlayerManager().getPlayer(event.starter);
        if (player != null) {
            player.sendMessage(Text.literal("Obelisk wave " + event.wave + "/" + MAX_WAVE), true);
        }
    }

    private static void spawnMob(ServerWorld world, BlockPos pos, int wave, boolean elite) {
        boolean spawnRiftStalker = wave >= 3 && world.random.nextFloat() < (elite ? 0.8F : 0.35F);
        var entity = spawnRiftStalker ? ModEntities.RIFT_STALKER.create(world) : ModEntities.ENERGY_BEING.create(world);
        if (entity == null) {
            return;
        }
        entity.refreshPositionAndAngles(pos, world.random.nextFloat() * 360.0F, 0.0F);
        if (entity instanceof MobEntity mob) {
            float hpScale = elite ? 2.0F : (1.1F + wave * 0.2F);
            mob.setHealth(mob.getMaxHealth() * hpScale);
            if (elite) {
                mob.setCustomName(Text.literal("Storm Warden"));
                mob.setCustomNameVisible(true);
            } else {
                mob.setCustomName(Text.literal((spawnRiftStalker ? "Rift Stalker " : "Obelisk Guard T") + wave));
                mob.setCustomNameVisible(wave >= 2);
            }
            if (spawnRiftStalker && mob instanceof RiftStalker stalker) {
                stalker.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                        net.minecraft.entity.effect.StatusEffects.RESISTANCE, 20 * 45, elite ? 1 : 0, true, false, true));
            }
        }
        world.spawnEntity(entity);
    }

    private static boolean consumeStormFragments(PlayerEntity player, int amount) {
        ItemStack main = player.getMainHandStack();
        if (main.isOf(ModItems.STORM_FRAGMENT) && main.getCount() >= amount) {
            if (!player.isCreative()) {
                main.decrement(amount);
            }
            return true;
        }

        ItemStack off = player.getOffHandStack();
        if (off.isOf(ModItems.STORM_FRAGMENT) && off.getCount() >= amount) {
            if (!player.isCreative()) {
                off.decrement(amount);
            }
            return true;
        }
        return false;
    }

    private static String eventKey(ServerWorld world, BlockPos corePos) {
        return world.getRegistryKey().getValue() + ":" + corePos.toShortString();
    }

    private static final class ActiveObeliskEvent {
        private final String key;
        private final BlockPos corePos;
        private final UUID starter;
        private final long endTime;
        private long nextWaveTime;
        private int wave;

        private ActiveObeliskEvent(String key, BlockPos corePos, UUID starter, long endTime, long nextWaveTime, int wave) {
            this.key = key;
            this.corePos = corePos;
            this.starter = starter;
            this.endTime = endTime;
            this.nextWaveTime = nextWaveTime;
            this.wave = wave;
        }
    }
}
