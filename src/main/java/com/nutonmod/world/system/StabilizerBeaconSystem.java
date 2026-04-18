package com.nutonmod.world.system;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.custom.StabilizerBeaconBlock;
import com.nutonmod.block.entity.StabilizerBeaconBlockEntity;
import com.nutonmod.world.dimension.ModDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.Map;

public final class StabilizerBeaconSystem {
    private static final int PROTECTION_RADIUS = 16;

    private StabilizerBeaconSystem() {
    }

    public static void activate(ServerWorld world, BlockPos pos, long durationTicks) {
        getState(world).put(pos, world.getTime() + Math.max(20L, durationTicks));
    }

    public static void deactivate(ServerWorld world, BlockPos pos) {
        getState(world).remove(pos);
    }

    public static int getRemainingSeconds(ServerWorld world, BlockPos pos) {
        long remainingTicks = getRemainingTicks(world, pos);
        return remainingTicks <= 0L ? 0 : (int) (remainingTicks / 20L);
    }

    public static void tick(ServerWorld world) {
        if (!world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return;
        }

        StabilizerBeaconWorldState state = getState(world);
        if (state.isEmpty()) {
            return;
        }

        long now = world.getTime();
        Iterator<Map.Entry<BlockPos, Long>> iterator = state.getActiveBeacons().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Long> entry = iterator.next();
            BlockPos pos = entry.getKey();
            long endTime = entry.getValue();

            BlockState blockState = world.getBlockState(pos);
            if (!blockState.isOf(ModBlocks.STABILIZER_BEACON)
                    || !blockState.contains(StabilizerBeaconBlock.ACTIVE)
                    || !blockState.get(StabilizerBeaconBlock.ACTIVE)) {
                iterator.remove();
                state.markDirty();
                continue;
            }

            if (now < endTime) {
                continue;
            }

            world.setBlockState(pos, blockState.with(StabilizerBeaconBlock.ACTIVE, false), Block.NOTIFY_ALL);
            syncBeaconEntity(world, pos, false, 0, false);
            iterator.remove();
            state.markDirty();
        }
    }

    public static boolean isProtected(PlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld serverWorld)) {
            return false;
        }
        if (!serverWorld.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return false;
        }

        StabilizerBeaconWorldState state = getState(serverWorld);
        if (state.isEmpty()) {
            return false;
        }

        long now = serverWorld.getTime();
        BlockPos playerPos = player.getBlockPos();
        int radiusSq = PROTECTION_RADIUS * PROTECTION_RADIUS;
        for (Map.Entry<BlockPos, Long> entry : state.getActiveBeacons().entrySet()) {
            if (entry.getValue() < now) {
                continue;
            }

            BlockPos beaconPos = entry.getKey();
            if (beaconPos.getSquaredDistance(playerPos) > radiusSq) {
                continue;
            }

            BlockState blockState = serverWorld.getBlockState(beaconPos);
            if (!blockState.isOf(ModBlocks.STABILIZER_BEACON)
                    || !blockState.contains(StabilizerBeaconBlock.ACTIVE)
                    || !blockState.get(StabilizerBeaconBlock.ACTIVE)) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static int getProtectionRadius() {
        return PROTECTION_RADIUS;
    }

    private static StabilizerBeaconWorldState getState(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(StabilizerBeaconWorldState.TYPE, com.nutonmod.NutonMod.MOD_ID + "_stabilizer_beacons");
    }

    private static long getRemainingTicks(ServerWorld world, BlockPos pos) {
        Long endTime = getState(world).getActiveBeacons().get(pos);
        if (endTime == null) {
            return 0L;
        }
        return Math.max(0L, endTime - world.getTime());
    }

    private static void syncBeaconEntity(ServerWorld world, BlockPos pos, boolean active, int seconds, boolean stormActive) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StabilizerBeaconBlockEntity beaconEntity) {
            beaconEntity.syncFromWorld(active, seconds, PROTECTION_RADIUS, stormActive);
        }
    }
}
