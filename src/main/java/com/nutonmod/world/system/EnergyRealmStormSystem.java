package com.nutonmod.world.system;

import com.nutonmod.block.EnergyCoreBlock;
import com.nutonmod.block.ModBlocks;
import com.nutonmod.entity.EnergyBeing;
import com.nutonmod.entity.ModEntities;
import com.nutonmod.entity.RiftStalker;
import com.nutonmod.item.ModItems;
import com.nutonmod.world.dimension.ModDimensions;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EnergyRealmStormSystem {
    private static final long STORM_CYCLE_TICKS = 20L * 60L * 6L;
    private static final long STORM_ACTIVE_TICKS = 20L * 60L * 2L;
    private static final String STORM_ACTIVE_TAG = "nutonmod_energy_storm_active";
    private static final int OBELISK_SCAN_RADIUS = 24;
    private static final long ENRAGE_INTERVAL = 40L;
    private static final long STORMBORN_SPAWN_INTERVAL = 100L;
    private static final long RIFT_STALKER_SPAWN_INTERVAL = 140L;
    private static final float STORMBORN_SPAWN_CHANCE = 0.18F;
    private static final float RIFT_STALKER_STORM_SPAWN_CHANCE = 0.20F;
    private static final int MAX_STORMBORN = 6;
    private static final int MAX_STORM_RIFT_STALKERS = 5;
    private static final String STORMBORN_TAG = "nutonmod_stormborn";
    private static final Map<RegistryKey<World>, Boolean> LAST_STORM_STATE = new HashMap<>();

    private EnergyRealmStormSystem() {
    }

    public static void applyPerTick(PlayerEntity player) {
        if (!player.getWorld().getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            player.removeCommandTag(STORM_ACTIVE_TAG);
            return;
        }

        boolean stormActive = isEnergyStormActive(player.getWorld().getTimeOfDay());
        boolean tagged = player.getCommandTags().contains(STORM_ACTIVE_TAG);

        if (stormActive && !tagged) {
            player.addCommandTag(STORM_ACTIVE_TAG);
            player.sendMessage(Text.translatable("message.nutonmod.storm.rising"), true);
        } else if (!stormActive && tagged) {
            player.removeCommandTag(STORM_ACTIVE_TAG);
            player.sendMessage(Text.translatable("message.nutonmod.storm.cleared"), true);
        }

        if (!stormActive || player.age % 40 != 0) {
            return;
        }

        if (StabilizerBeaconSystem.isProtected(player)) {
            player.removeStatusEffect(StatusEffects.BLINDNESS);
            player.removeStatusEffect(StatusEffects.SLOWNESS);
            player.removeStatusEffect(StatusEffects.WEAKNESS);
            player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 80, 0, true, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 80, 0, true, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 120, 0, true, false, true));
            return;
        }

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0, true, true, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 0, true, true, true));

        if (isNearStormObelisk(player)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 80, 1, true, true, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 80, 1, true, true, true));
        }
    }

    public static boolean isEnergyStormActive(long worldTime) {
        long cycleTime = Math.floorMod(worldTime, STORM_CYCLE_TICKS);
        return cycleTime < STORM_ACTIVE_TICKS;
    }

    public static long getStormCycleTicks() {
        return STORM_CYCLE_TICKS;
    }

    public static long getStormActiveTicks() {
        return STORM_ACTIVE_TICKS;
    }

    public static long getTicksUntilNextStorm(long worldTime) {
        long cycleTime = Math.floorMod(worldTime, STORM_CYCLE_TICKS);
        if (cycleTime < STORM_ACTIVE_TICKS) {
            return 0L;
        }
        return STORM_CYCLE_TICKS - cycleTime;
    }

    public static boolean isEnergyStormActive(ServerWorld world) {
        return world != null
                && world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)
                && isEnergyStormActive(world.getTimeOfDay());
    }

    public static void tick(ServerWorld world) {
        if (world == null || !world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return;
        }
        boolean stormActive = isEnergyStormActive(world);
        Boolean last = LAST_STORM_STATE.put(world.getRegistryKey(), stormActive);
        if (Boolean.TRUE.equals(last) && !stormActive) {
            spawnStormResidue(world);
        }
        if (!stormActive) {
            return;
        }
        long time = world.getTime();
        if (time % ENRAGE_INTERVAL == 0L) {
            enrageNearbyEnergyBeings(world);
        }
        if (time % STORMBORN_SPAWN_INTERVAL == 0L) {
            trySpawnStormborn(world);
        }
        if (time % RIFT_STALKER_SPAWN_INTERVAL == 0L) {
            trySpawnStormRiftStalker(world);
        }
    }

    public static boolean isNearStormObelisk(PlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld serverWorld)) {
            return false;
        }
        if (!serverWorld.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return false;
        }

        BlockPos center = player.getBlockPos();
        for (int dx = -OBELISK_SCAN_RADIUS; dx <= OBELISK_SCAN_RADIUS; dx++) {
            for (int dz = -OBELISK_SCAN_RADIUS; dz <= OBELISK_SCAN_RADIUS; dz++) {
                for (int dy = -8; dy <= 12; dy++) {
                    BlockPos corePos = center.add(dx, dy, dz);
                    if (!serverWorld.getBlockState(corePos).isOf(ModBlocks.ENERGY_CORE)) {
                        continue;
                    }
                    if (!serverWorld.getBlockState(corePos).get(EnergyCoreBlock.ACTIVATED)) {
                        continue;
                    }
                    if (isStormObeliskCore(serverWorld, corePos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isStormObeliskCore(ServerWorld world, BlockPos corePos) {
        BlockPos base = corePos.down(10);
        for (int y = 0; y < 10; y++) {
            if (!world.getBlockState(base.up(y)).isOf(ModBlocks.ENERGY_BLOCK)) {
                return false;
            }
        }
        return world.getBlockState(base.add(1, 0, 0)).isOf(ModBlocks.ENERGY_BLOCK)
                && world.getBlockState(base.add(-1, 0, 0)).isOf(ModBlocks.ENERGY_BLOCK)
                && world.getBlockState(base.add(0, 0, 1)).isOf(ModBlocks.ENERGY_BLOCK)
                && world.getBlockState(base.add(0, 0, -1)).isOf(ModBlocks.ENERGY_BLOCK);
    }

    public static boolean isStormborn(EnergyBeing being) {
        return being != null && being.getCommandTags().contains(STORMBORN_TAG);
    }

    private static void enrageNearbyEnergyBeings(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers(player -> !player.isSpectator());
        for (ServerPlayerEntity player : players) {
            Box scanBox = new Box(player.getBlockPos()).expand(36.0D, 16.0D, 36.0D);
            List<LivingEntity> beings = world.getEntitiesByClass(
                    LivingEntity.class,
                    scanBox,
                    entity -> entity.isAlive() && (entity instanceof EnergyBeing || entity instanceof RiftStalker)
            );
            for (LivingEntity being : beings) {
                being.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 120, 1, true, false, true));
                being.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 120, 1, true, false, true));
                being.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 120, 0, true, false, true));
            }
        }
    }

    private static void trySpawnStormborn(ServerWorld world) {
        if (countStormborn(world) >= MAX_STORMBORN) {
            return;
        }
        if (world.random.nextFloat() > STORMBORN_SPAWN_CHANCE) {
            return;
        }
        List<ServerPlayerEntity> players = world.getPlayers(player -> !player.isSpectator());
        if (players.isEmpty()) {
            return;
        }

        ServerPlayerEntity anchor = players.get(world.random.nextInt(players.size()));
        BlockPos spawnPos = findSpawnPosNear(world, anchor.getBlockPos());
        if (spawnPos == null) {
            return;
        }

        EnergyBeing stormborn = ModEntities.ENERGY_BEING.create(world);
        if (stormborn == null) {
            return;
        }

        stormborn.refreshPositionAndAngles(
                Vec3d.ofCenter(spawnPos).x,
                spawnPos.getY(),
                Vec3d.ofCenter(spawnPos).z,
                world.random.nextFloat() * 360.0F,
                0.0F
        );
        stormborn.addCommandTag(STORMBORN_TAG);
        stormborn.setCustomName(Text.translatable("entity.nutonmod.energy_being.stormborn"));
        stormborn.setCustomNameVisible(true);
        stormborn.setHealth(stormborn.getMaxHealth());
        stormborn.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 90, 1, true, false, true));
        stormborn.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 90, 1, true, false, true));
        stormborn.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 90, 1, true, false, true));
        stormborn.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 90, 0, true, false, true));
        world.spawnEntity(stormborn);
    }

    private static void trySpawnStormRiftStalker(ServerWorld world) {
        if (countNearbyRiftStalkers(world) >= MAX_STORM_RIFT_STALKERS) {
            return;
        }
        if (world.random.nextFloat() > RIFT_STALKER_STORM_SPAWN_CHANCE) {
            return;
        }
        List<ServerPlayerEntity> players = world.getPlayers(player -> !player.isSpectator());
        if (players.isEmpty()) {
            return;
        }
        ServerPlayerEntity anchor = players.get(world.random.nextInt(players.size()));
        BlockPos spawnPos = findSpawnPosNear(world, anchor.getBlockPos());
        if (spawnPos == null) {
            return;
        }
        RiftStalker stalker = ModEntities.RIFT_STALKER.create(world);
        if (stalker == null) {
            return;
        }
        stalker.refreshPositionAndAngles(
                Vec3d.ofCenter(spawnPos).x,
                spawnPos.getY(),
                Vec3d.ofCenter(spawnPos).z,
                world.random.nextFloat() * 360.0F,
                0.0F
        );
        stalker.setCustomName(Text.translatable("entity.nutonmod.rift_stalker.storm"));
        stalker.setCustomNameVisible(true);
        stalker.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 60, 1, true, false, true));
        stalker.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20 * 60, 0, true, false, true));
        world.spawnEntity(stalker);
    }

    private static int countStormborn(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers(player -> !player.isSpectator());
        int count = 0;
        for (ServerPlayerEntity player : players) {
            Box box = new Box(player.getBlockPos()).expand(64.0D, 24.0D, 64.0D);
            count += world.getEntitiesByClass(EnergyBeing.class, box, EnergyRealmStormSystem::isStormborn).size();
            if (count >= MAX_STORMBORN) {
                return count;
            }
        }
        return count;
    }

    private static int countNearbyRiftStalkers(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers(player -> !player.isSpectator());
        int count = 0;
        for (ServerPlayerEntity player : players) {
            Box box = new Box(player.getBlockPos()).expand(64.0D, 24.0D, 64.0D);
            count += world.getEntitiesByClass(RiftStalker.class, box, entity -> entity.isAlive()).size();
            if (count >= MAX_STORM_RIFT_STALKERS) {
                return count;
            }
        }
        return count;
    }

    private static BlockPos findSpawnPosNear(ServerWorld world, BlockPos center) {
        for (int i = 0; i < 10; i++) {
            int x = center.getX() + world.random.nextInt(41) - 20;
            int z = center.getZ() + world.random.nextInt(41) - 20;
            int y = world.getTopY() - 1;
            BlockPos.Mutable cursor = new BlockPos.Mutable(x, y, z);
            while (cursor.getY() > world.getBottomY() + 1 && world.getBlockState(cursor).isAir()) {
                cursor.move(0, -1, 0);
            }
            BlockPos ground = cursor.toImmutable();
            BlockPos spawn = ground.up();
            if (!world.getBlockState(ground).isAir() && world.getBlockState(spawn).isAir() && world.getBlockState(spawn.up()).isAir()) {
                return spawn;
            }
        }
        return null;
    }

    private static void spawnStormResidue(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers(player -> !player.isSpectator());
        if (players.isEmpty()) {
            return;
        }

        for (ServerPlayerEntity player : players) {
            int drops = world.random.nextInt(3) + 2;
            for (int i = 0; i < drops; i++) {
                BlockPos pos = findSpawnPosNear(world, player.getBlockPos());
                if (pos == null) {
                    continue;
                }
                ItemEntity residue = new ItemEntity(
                        world,
                        pos.getX() + 0.5D,
                        pos.getY() + 0.2D,
                        pos.getZ() + 0.5D,
                        new ItemStack(ModItems.CRYSTAL_MATRIX, 1)
                );
                world.spawnEntity(residue);
            }
        }
    }
}
