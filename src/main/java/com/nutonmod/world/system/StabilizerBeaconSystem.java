package com.nutonmod.world.system;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.custom.StabilizerBeaconBlock;
import com.nutonmod.world.dimension.ModDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class StabilizerBeaconSystem {
    private static final int PROTECTION_RADIUS = 16;
    private static final Map<RegistryKey<World>, Map<BlockPos, Long>> ACTIVE_BEACONS = new HashMap<>();

    private StabilizerBeaconSystem() {
    }

    public static void activate(ServerWorld world, BlockPos pos, long durationTicks) {
        ACTIVE_BEACONS.computeIfAbsent(world.getRegistryKey(), key -> new HashMap<>())
                .put(pos.toImmutable(), world.getTime() + Math.max(20L, durationTicks));
    }

    public static void deactivate(ServerWorld world, BlockPos pos) {
        Map<BlockPos, Long> worldMap = ACTIVE_BEACONS.get(world.getRegistryKey());
        if (worldMap == null) {
            return;
        }
        worldMap.remove(pos);
        if (worldMap.isEmpty()) {
            ACTIVE_BEACONS.remove(world.getRegistryKey());
        }
    }

    public static void tick(ServerWorld world) {
        if (!world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return;
        }

        Map<BlockPos, Long> worldMap = ACTIVE_BEACONS.get(world.getRegistryKey());
        if (worldMap == null || worldMap.isEmpty()) {
            return;
        }

        long now = world.getTime();
        Iterator<Map.Entry<BlockPos, Long>> iterator = worldMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Long> entry = iterator.next();
            BlockPos pos = entry.getKey();
            long endTime = entry.getValue();

            BlockState state = world.getBlockState(pos);
            if (!state.isOf(ModBlocks.STABILIZER_BEACON)
                    || !state.contains(StabilizerBeaconBlock.ACTIVE)
                    || !state.get(StabilizerBeaconBlock.ACTIVE)) {
                iterator.remove();
                continue;
            }

            if (now < endTime) {
                continue;
            }

            world.setBlockState(pos, state.with(StabilizerBeaconBlock.ACTIVE, false), Block.NOTIFY_ALL);
            iterator.remove();
        }

        if (worldMap.isEmpty()) {
            ACTIVE_BEACONS.remove(world.getRegistryKey());
        }
    }

    public static boolean isProtected(PlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld serverWorld)) {
            return false;
        }
        if (!serverWorld.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return false;
        }

        Map<BlockPos, Long> worldMap = ACTIVE_BEACONS.get(serverWorld.getRegistryKey());
        if (worldMap == null || worldMap.isEmpty()) {
            return false;
        }

        long now = serverWorld.getTime();
        BlockPos playerPos = player.getBlockPos();
        int radiusSq = PROTECTION_RADIUS * PROTECTION_RADIUS;
        for (Map.Entry<BlockPos, Long> entry : worldMap.entrySet()) {
            if (entry.getValue() < now) {
                continue;
            }

            BlockPos beaconPos = entry.getKey();
            if (beaconPos.getSquaredDistance(playerPos) > radiusSq) {
                continue;
            }

            BlockState state = serverWorld.getBlockState(beaconPos);
            if (!state.isOf(ModBlocks.STABILIZER_BEACON)
                    || !state.contains(StabilizerBeaconBlock.ACTIVE)
                    || !state.get(StabilizerBeaconBlock.ACTIVE)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static int getProtectionRadius() {
        return PROTECTION_RADIUS;
    }
}
