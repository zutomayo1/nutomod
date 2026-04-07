package com.nutonmod.block;

import com.nutonmod.entity.ModEntities;
import com.nutonmod.item.ModItems;
import com.nutonmod.world.dimension.ModDimensions;
import com.nutonmod.world.system.EnergyRealmStormSystem;
import com.nutonmod.world.system.StormArbiterSummonSystem;
import com.nutonmod.world.system.SingularityArenaSystem;
import com.nutonmod.world.system.SingularitySummonSystem;
import com.nutonmod.world.system.StormObeliskEventSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import java.util.HashMap;
import java.util.Map;

public class EnergyCoreBlock extends Block {
    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");
    private static final long SANCTUM_COOLDOWN_TICKS = 20L * 60L * 5L;
    private static final Map<String, Long> SANCTUM_LAST_TRIGGER = new HashMap<>();
    private static final Map<String, Integer> SANCTUM_TRIGGER_COUNT = new HashMap<>();

    public EnergyCoreBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(ACTIVATED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        if (world instanceof ServerWorld serverWorld
                && SingularityArenaSystem.tryActivateNode(serverWorld, pos, player)) {
            return ActionResult.SUCCESS;
        }
        if (world instanceof ServerWorld serverWorld
                && StormArbiterSummonSystem.trySummon(serverWorld, pos, player)) {
            return ActionResult.SUCCESS;
        }
        if (world instanceof ServerWorld serverWorld
                && SingularitySummonSystem.trySummon(serverWorld, pos, player)) {
            return ActionResult.SUCCESS;
        }
        if (world instanceof ServerWorld serverWorld
                && EnergyRealmStormSystem.isStormObeliskCore(serverWorld, pos)) {
            return StormObeliskEventSystem.handleCoreUse(serverWorld, pos, player);
        }

        boolean isActivated = state.get(ACTIVATED);
        if (!isActivated && tryActivateAltar(state, world, pos, player)) {
            return ActionResult.SUCCESS;
        }

        world.setBlockState(pos, state.with(ACTIVATED, !isActivated));
        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 0.5f, 1.0f);
        player.sendMessage(Text.literal(!isActivated ? "Energy core activated" : "Energy core disabled"), true);
        return ActionResult.SUCCESS;
    }

    private boolean tryActivateAltar(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return false;
        }
        boolean stormActive = world instanceof ServerWorld serverWorld
                && EnergyRealmStormSystem.isEnergyStormActive(serverWorld);
        if (!isAltarShape(world, pos)) {
            player.sendMessage(Text.literal("Incomplete altar structure"), true);
            return true;
        }
        String altarKey = world.getRegistryKey().getValue() + ":" + pos.toShortString();
        long currentTime = world.getTime();
        long lastTrigger = SANCTUM_LAST_TRIGGER.getOrDefault(altarKey, Long.MIN_VALUE / 4L);
        long cooldown = stormActive ? SANCTUM_COOLDOWN_TICKS / 2L : SANCTUM_COOLDOWN_TICKS;
        long remaining = cooldown - (currentTime - lastTrigger);
        if (remaining > 0) {
            long seconds = Math.max(1L, remaining / 20L);
            player.sendMessage(Text.literal("Sanctum cooling down: " + seconds + "s"), true);
            return true;
        }

        ItemStack stack = player.getMainHandStack();
        if (!stack.isOf(ModItems.RAW_ENERGY) || stack.getCount() < 4) {
            player.sendMessage(Text.literal("Need 4 raw_energy in main hand to activate"), true);
            return true;
        }

        stack.decrement(4);
        world.setBlockState(pos, state.with(ACTIVATED, true));
        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0f, 1.1f);
        int ingotReward = stormActive ? 3 : 2;
        player.giveItemStack(new ItemStack(ModItems.ENERGY_INGOT, ingotReward));
        player.giveItemStack(new ItemStack(ModItems.ALTAR_SHARD, 1));
        if (stormActive) {
            player.giveItemStack(new ItemStack(ModItems.STORM_FRAGMENT, 1));
        }
        player.addExperience(12);
        int triggerCount = SANCTUM_TRIGGER_COUNT.getOrDefault(altarKey, 0) + 1;
        SANCTUM_TRIGGER_COUNT.put(altarKey, triggerCount);
        SANCTUM_LAST_TRIGGER.put(altarKey, currentTime);
        triggerSanctumEvent(world, pos, player, triggerCount, stormActive);
        player.sendMessage(Text.literal("Altar activated: reward energy_ingot x" + ingotReward), true);
        return true;
    }

    private boolean isAltarShape(World world, BlockPos corePos) {
        BlockPos floorCenter = corePos.down();
        if (!world.getBlockState(floorCenter).isOf(ModBlocks.ENERGY_BLOCK)) {
            return false;
        }

        for (Direction direction : Direction.Type.HORIZONTAL) {
            if (!world.getBlockState(corePos.offset(direction)).isOf(ModBlocks.ENERGY_BLOCK)) {
                return false;
            }
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (!world.getBlockState(floorCenter.add(dx, 0, dz)).isOf(ModBlocks.ENERGY_BLOCK)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void triggerSanctumEvent(World world, BlockPos pos, PlayerEntity activator, int triggerCount, boolean stormActive) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return;
        }

        int tier = Math.min(4, 1 + ((triggerCount - 1) / 2) + (stormActive ? 1 : 0));
        int extraMobs = Math.min(5, (triggerCount / 2) + (stormActive ? 1 : 0));
        int totalSpawns = 4 + extraMobs;
        BlockPos[] ring = new BlockPos[] {
                pos.add(4, 1, 0),
                pos.add(-4, 1, 0),
                pos.add(0, 1, 4),
                pos.add(0, 1, -4),
                pos.add(5, 1, 5),
                pos.add(-5, 1, 5),
                pos.add(5, 1, -5),
                pos.add(-5, 1, -5)
        };
        for (int i = 0; i < totalSpawns; i++) {
            spawnMob(serverWorld, ring[i], tier);
        }

        if (tier >= 2) {
            activator.giveItemStack(new ItemStack(ModItems.CORE_STABILIZER, 1));
            activator.giveItemStack(new ItemStack(ModItems.STORM_FRAGMENT, tier + (stormActive ? 1 : 0)));
        }

        String suffix = stormActive ? " (storm surge)" : "";
        activator.sendMessage(Text.literal("Sanctum wave tier " + tier + " started" + suffix), false);
    }

    private void spawnMob(ServerWorld world, BlockPos pos, int tier) {
        boolean spawnRiftStalker = tier >= 3 && world.random.nextFloat() < 0.40F;
        var entity = spawnRiftStalker ? ModEntities.RIFT_STALKER.create(world) : ModEntities.ENERGY_BEING.create(world);
        if (entity == null) {
            return;
        }
        entity.refreshPositionAndAngles(pos, world.random.nextFloat() * 360.0F, 0.0F);
        if (entity instanceof MobEntity mob) {
            mob.setHealth(mob.getMaxHealth() * (1.2F + (tier * 0.25F)));
            mob.setCustomName(Text.literal((spawnRiftStalker ? "Rift Stalker T" : "Energy Guard T") + tier));
            mob.setCustomNameVisible(tier >= 2);
        }
        world.spawnEntity(entity);
    }
}
