package com.nutonmod.block.custom;

import com.nutonmod.block.CustomFluid;
import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.ModFluids;
import com.nutonmod.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class EnergyFluid extends CustomFluid {
    @Override
    public Fluid getStill() {
        return ModFluids.STILL_ENERGY;
    }

    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_ENERGY;
    }

    @Override
    public Item getBucketItem() {
        return ModItems.ENERGY_BUCKET;
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return ModBlocks.STILL_ENERGY.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
    }

    @Override
    protected void flow(WorldAccess world, BlockPos pos, BlockState state, Direction direction, FluidState fluidState) {
        if (direction == Direction.DOWN) {
            FluidState targetFluidState = world.getFluidState(pos);
            if (targetFluidState.isIn(FluidTags.WATER) || targetFluidState.isIn(FluidTags.LAVA)) {
                world.setBlockState(pos, ModBlocks.ENERGY_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
                return;
            }
        }

        super.flow(world, pos, state, direction, fluidState);
    }

    public static class Flowing extends EnergyFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public boolean isStill(FluidState state) {
            return false;
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }
    }
    public static class Still extends EnergyFluid {
        @Override
        public boolean isStill(FluidState state) {
            return true;
        }

        @Override
        public int getLevel(FluidState state) {
            return 8;
        }
    }
}
