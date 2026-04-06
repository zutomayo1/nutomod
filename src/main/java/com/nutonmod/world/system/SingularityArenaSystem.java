package com.nutonmod.world.system;

import com.nutonmod.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class SingularityArenaSystem {
    private static final int ARENA_RADIUS = 15;
    private static final int WALL_HEIGHT = 5;
    private static final int CLEANUP_DELAY_TICKS = 20 * 10;
    private static final int NODE_COOLDOWN_TICKS = 20 * 120;
    private static final Map<UUID, ActiveArena> ACTIVE = new HashMap<>();

    private SingularityArenaSystem() {
    }

    public static void createArena(ServerWorld world, UUID bossId, BlockPos center) {
        ActiveArena arena = new ActiveArena(world.getRegistryKey(), center.toImmutable());
        ACTIVE.put(bossId, arena);
        buildFloorAndWalls(world, arena);
    }

    public static void markBossDefeated(UUID bossId) {
        ActiveArena arena = ACTIVE.get(bossId);
        if (arena == null) {
            return;
        }
        arena.markForCleanup();
    }

    public static void tick(MinecraftServer server) {
        Iterator<Map.Entry<UUID, ActiveArena>> it = ACTIVE.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, ActiveArena> entry = it.next();
            UUID bossId = entry.getKey();
            ActiveArena arena = entry.getValue();
            ServerWorld world = server.getWorld(arena.worldKey);
            if (world == null) {
                it.remove();
                continue;
            }

            if (!arena.cleanupScheduled && world.getEntity(bossId) == null) {
                arena.markForCleanup();
            }

            if (!arena.cleanupScheduled) {
                continue;
            }

            arena.cleanupTimer--;
            if (arena.cleanupTimer > 0) {
                continue;
            }

            restoreArena(world, arena);
            it.remove();
        }
    }

    public static boolean tryActivateNode(ServerWorld world, BlockPos pos, PlayerEntity player) {
        for (ActiveArena arena : ACTIVE.values()) {
            if (!arena.worldKey.equals(world.getRegistryKey()) || arena.cleanupScheduled) {
                continue;
            }
            ArenaNodeType nodeType = arena.nodes.get(pos.toImmutable());
            if (nodeType == null) {
                continue;
            }

            long now = world.getTime();
            long remain = arena.nodeCooldownUntil.getOrDefault(pos.toImmutable(), 0L) - now;
            if (remain > 0L) {
                long seconds = Math.max(1L, remain / 20L);
                player.sendMessage(Text.translatable("boss.nutonmod.singularity.node_cooldown", seconds), true);
                return true;
            }

            applyNodeEffect(world, player, nodeType, pos);
            arena.nodeCooldownUntil.put(pos.toImmutable(), now + NODE_COOLDOWN_TICKS);
            return true;
        }
        return false;
    }

    private static void buildFloorAndWalls(ServerWorld world, ActiveArena arena) {
        BlockPos center = arena.center;
        int y = center.getY();

        for (int dx = -ARENA_RADIUS; dx <= ARENA_RADIUS; dx++) {
            for (int dz = -ARENA_RADIUS; dz <= ARENA_RADIUS; dz++) {
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > ARENA_RADIUS + 0.35D) {
                    continue;
                }

                BlockPos floorPos = center.add(dx, -1, dz);
                setArenaBlock(world, arena, floorPos, ModBlocks.ENERGY_BLOCK.getDefaultState());

                if (dist >= ARENA_RADIUS - 0.8D) {
                    for (int i = 0; i < WALL_HEIGHT; i++) {
                        BlockPos wallPos = new BlockPos(floorPos.getX(), y + i, floorPos.getZ());
                        setArenaBlock(world, arena, wallPos, ModBlocks.ENERGY_BLOCK.getDefaultState());
                    }
                }
            }
        }

        int[][] pillars = new int[][] {
                {ARENA_RADIUS - 1, 0}, {-ARENA_RADIUS + 1, 0}, {0, ARENA_RADIUS - 1}, {0, -ARENA_RADIUS + 1},
                {10, 10}, {10, -10}, {-10, 10}, {-10, -10}
        };
        for (int[] p : pillars) {
            BlockPos base = center.add(p[0], 0, p[1]);
            for (int i = 0; i < 8; i++) {
                setArenaBlock(world, arena, base.up(i), ModBlocks.ENERGY_BLOCK.getDefaultState());
            }
        }

        placeNode(world, arena, center.add(11, 0, 11), ArenaNodeType.RED);
        placeNode(world, arena, center.add(11, 0, -11), ArenaNodeType.BLUE);
        placeNode(world, arena, center.add(-11, 0, 11), ArenaNodeType.YELLOW);
        placeNode(world, arena, center.add(-11, 0, -11), ArenaNodeType.PURPLE);
    }

    private static void restoreArena(ServerWorld world, ActiveArena arena) {
        for (Map.Entry<BlockPos, BlockState> e : arena.replaced.entrySet()) {
            world.setBlockState(e.getKey(), e.getValue(), 3);
        }
    }

    private static void setArenaBlock(ServerWorld world, ActiveArena arena, BlockPos pos, BlockState target) {
        BlockState old = world.getBlockState(pos);
        if (old.isOf(Blocks.BEDROCK) || old.isOf(Blocks.END_PORTAL_FRAME)) {
            return;
        }
        if (!arena.replaced.containsKey(pos)) {
            arena.replaced.put(pos.toImmutable(), old);
        }
        if (old == target) {
            return;
        }
        world.setBlockState(pos, target, 3);
    }

    private static void placeNode(ServerWorld world, ActiveArena arena, BlockPos pos, ArenaNodeType type) {
        setArenaBlock(world, arena, pos.down(), ModBlocks.ENERGY_BLOCK.getDefaultState());
        setArenaBlock(world, arena, pos, ModBlocks.ENERGY_CORE.getDefaultState());
        arena.nodes.put(pos.toImmutable(), type);
    }

    private static void applyNodeEffect(ServerWorld world, PlayerEntity player, ArenaNodeType nodeType, BlockPos pos) {
        switch (nodeType) {
            case RED -> {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 15, 1, true, true, true));
                player.sendMessage(Text.translatable("boss.nutonmod.singularity.node_red"), true);
            }
            case BLUE -> {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 15, 1, true, true, true));
                player.sendMessage(Text.translatable("boss.nutonmod.singularity.node_blue"), true);
            }
            case YELLOW -> {
                player.heal(40.0F);
                player.sendMessage(Text.translatable("boss.nutonmod.singularity.node_yellow"), true);
            }
            case PURPLE -> {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20 * 15, 9, true, true, true));
                player.sendMessage(Text.translatable("boss.nutonmod.singularity.node_purple"), true);
            }
        }
        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1.0F, 1.15F);
        world.spawnParticles(ParticleTypes.END_ROD, pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, 35, 0.25, 0.45, 0.25, 0.04);
    }

    private static final class ActiveArena {
        private final net.minecraft.registry.RegistryKey<World> worldKey;
        private final BlockPos center;
        private final Map<BlockPos, BlockState> replaced = new HashMap<>();
        private final Map<BlockPos, ArenaNodeType> nodes = new HashMap<>();
        private final Map<BlockPos, Long> nodeCooldownUntil = new HashMap<>();
        private boolean cleanupScheduled = false;
        private int cleanupTimer = CLEANUP_DELAY_TICKS;

        private ActiveArena(net.minecraft.registry.RegistryKey<World> worldKey, BlockPos center) {
            this.worldKey = worldKey;
            this.center = center;
        }

        private void markForCleanup() {
            this.cleanupScheduled = true;
            this.cleanupTimer = CLEANUP_DELAY_TICKS;
        }
    }

    private enum ArenaNodeType {
        RED,
        BLUE,
        YELLOW,
        PURPLE
    }
}
