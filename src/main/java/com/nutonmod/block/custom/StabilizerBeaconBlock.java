package com.nutonmod.block.custom;

import com.nutonmod.item.ModItems;
import com.nutonmod.world.dimension.ModDimensions;
import com.nutonmod.world.system.EnergyRealmStormSystem;
import com.nutonmod.world.system.StabilizerBeaconSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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

public class StabilizerBeaconBlock extends Block {
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

        boolean isActive = state.get(ACTIVE);
        if (isActive) {
            world.setBlockState(pos, state.with(ACTIVE, false), Block.NOTIFY_ALL);
            StabilizerBeaconSystem.deactivate(serverWorld, pos);
            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            player.sendMessage(Text.translatable("message.nutonmod.stabilizer_beacon.offline"), true);
            return ActionResult.SUCCESS;
        }

        boolean stormActive = EnergyRealmStormSystem.isEnergyStormActive(serverWorld);
        int fuelCost = stormActive ? 2 : 1;
        if (!consumeFuel(player, fuelCost)) {
            player.sendMessage(Text.translatable("message.nutonmod.stabilizer_beacon.need_fuel", fuelCost), true);
            return ActionResult.SUCCESS;
        }

        long duration = stormActive ? BASE_DURATION_TICKS / 2L : BASE_DURATION_TICKS;
        world.setBlockState(pos, state.with(ACTIVE, true), Block.NOTIFY_ALL);
        StabilizerBeaconSystem.activate(serverWorld, pos, duration);
        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0, true, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 200, 0, true, false, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 0, true, false, true));

        int seconds = (int) (duration / 20L);
        int radius = StabilizerBeaconSystem.getProtectionRadius();
        player.sendMessage(Text.translatable("message.nutonmod.stabilizer_beacon.online", seconds, radius), true);
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
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private static boolean consumeFuel(PlayerEntity player, int amount) {
        if (player.isCreative()) {
            return hasFuelInEitherHand(player, amount);
        }
        if (consumeFromHand(player, Hand.MAIN_HAND, amount)) {
            return true;
        }
        return consumeFromHand(player, Hand.OFF_HAND, amount);
    }

    private static boolean hasFuelInEitherHand(PlayerEntity player, int amount) {
        return hasFuelInHand(player, Hand.MAIN_HAND, amount) || hasFuelInHand(player, Hand.OFF_HAND, amount);
    }

    private static boolean hasFuelInHand(PlayerEntity player, Hand hand, int amount) {
        ItemStack stack = player.getStackInHand(hand);
        return stack.isOf(ModItems.ALTAR_SHARD) && stack.getCount() >= amount;
    }

    private static boolean consumeFromHand(PlayerEntity player, Hand hand, int amount) {
        ItemStack stack = player.getStackInHand(hand);
        if (!stack.isOf(ModItems.ALTAR_SHARD) || stack.getCount() < amount) {
            return false;
        }
        stack.decrement(amount);
        return true;
    }

}
