package com.nutonmod.block.custom;

import com.nutonmod.block.entity.StabilizerBeaconBlockEntity;
import com.nutonmod.item.ModItems;
import com.nutonmod.world.dimension.ModDimensions;
import com.nutonmod.world.system.EnergyRealmStormSystem;
import com.nutonmod.world.system.StabilizerBeaconSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class StabilizerBeaconBlock extends Block implements BlockEntityProvider {
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
    private static final long BASE_DURATION_TICKS = 20L * 60L * 3L;

    public StabilizerBeaconBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(ACTIVE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StabilizerBeaconBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.SUCCESS;
        }
        if (!world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            player.sendMessage(Text.translatable("message.nutonmod.stabilizer_beacon.only_in_realm"), true);
            return ActionResult.SUCCESS;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StabilizerBeaconBlockEntity beaconEntity) {
            boolean stormActive = EnergyRealmStormSystem.isEnergyStormActive(serverWorld);
            int remainingSeconds = StabilizerBeaconSystem.getRemainingSeconds(serverWorld, pos);
            beaconEntity.syncFromWorld(state.get(ACTIVE), remainingSeconds, StabilizerBeaconSystem.getProtectionRadius(), stormActive);
        }

        // Normal right-click opens GUI.
        // Sneak + right-click OR right-click while holding altar shard toggles beacon state.
        boolean holdingFuel = player.getMainHandStack().isOf(ModItems.ALTAR_SHARD)
                || player.getOffHandStack().isOf(ModItems.ALTAR_SHARD);
        boolean toggleRequested = player.shouldCancelInteraction() || player.isSneaking() || holdingFuel;
        if (!toggleRequested) {
            if (blockEntity instanceof StabilizerBeaconBlockEntity beaconEntity) {
                player.openHandledScreen((NamedScreenHandlerFactory) beaconEntity);
            }
            return ActionResult.SUCCESS;
        }

        if (state.get(ACTIVE)) {
            deactivateBeacon(world, serverWorld, state, pos, player, blockEntity);
            return ActionResult.SUCCESS;
        }

        boolean stormActive = EnergyRealmStormSystem.isEnergyStormActive(serverWorld);
        int fuelCost = stormActive ? 2 : 1;
        if (!consumeFuel(player, fuelCost)) {
            player.sendMessage(Text.translatable("message.nutonmod.stabilizer_beacon.need_fuel", fuelCost), true);
            return ActionResult.SUCCESS;
        }

        activateBeacon(world, serverWorld, state, pos, player, blockEntity, stormActive);
        return ActionResult.SUCCESS;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!(world instanceof ServerWorld serverWorld)) {
            super.onStateReplaced(state, world, pos, newState, moved);
            return;
        }
        if (!state.isOf(newState.getBlock())) {
            StabilizerBeaconSystem.deactivate(serverWorld, pos);
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof StabilizerBeaconBlockEntity beaconEntity) {
                beaconEntity.setOffline();
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private static boolean consumeFuel(PlayerEntity player, int amount) {
        if (player.isCreative()) {
            return true;
        }
        if (amount <= 0) {
            return true;
        }

        PlayerInventory inventory = player.getInventory();
        int totalFuel = 0;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(ModItems.ALTAR_SHARD)) {
                totalFuel += stack.getCount();
                if (totalFuel >= amount) {
                    break;
                }
            }
        }
        if (totalFuel < amount) {
            return false;
        }

        int remaining = amount;
        for (int i = 0; i < inventory.size() && remaining > 0; i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isOf(ModItems.ALTAR_SHARD) || stack.isEmpty()) {
                continue;
            }
            int toConsume = Math.min(remaining, stack.getCount());
            stack.decrement(toConsume);
            remaining -= toConsume;
        }
        return true;
    }

    private static void deactivateBeacon(World world, ServerWorld serverWorld, BlockState state, BlockPos pos, PlayerEntity player, BlockEntity blockEntity) {
        world.setBlockState(pos, state.with(ACTIVE, false), Block.NOTIFY_ALL);
        StabilizerBeaconSystem.deactivate(serverWorld, pos);
        if (blockEntity instanceof StabilizerBeaconBlockEntity beaconEntity) {
            beaconEntity.syncFromWorld(false, 0, StabilizerBeaconSystem.getProtectionRadius(), false);
        }
        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        player.sendMessage(Text.translatable("message.nutonmod.stabilizer_beacon.offline"), true);
    }

    private static void activateBeacon(
            World world,
            ServerWorld serverWorld,
            BlockState state,
            BlockPos pos,
            PlayerEntity player,
            BlockEntity blockEntity,
            boolean stormActive
    ) {
        long duration = stormActive ? BASE_DURATION_TICKS / 2L : BASE_DURATION_TICKS;
        int durationSeconds = (int) (duration / 20L);
        int radius = StabilizerBeaconSystem.getProtectionRadius();
        world.setBlockState(pos, state.with(ACTIVE, true), Block.NOTIFY_ALL);
        StabilizerBeaconSystem.activate(serverWorld, pos, duration);
        if (blockEntity instanceof StabilizerBeaconBlockEntity beaconEntity) {
            beaconEntity.syncFromWorld(true, durationSeconds, radius, stormActive);
        }
        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0, true, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 200, 0, true, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 0, true, false, true));
        player.sendMessage(Text.translatable("message.nutonmod.stabilizer_beacon.online", durationSeconds, radius), true);
    }
}
