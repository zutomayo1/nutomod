package com.nutonmod.block.custom;

import com.nutonmod.block.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EnergyFluidBlock extends FluidBlock {
	public EnergyFluidBlock(FlowableFluid fluid, AbstractBlock.Settings settings) {
		super(fluid, settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		// Vanilla swimming/buoyancy is tied to FluidTags.WATER, which also makes the entity "wet" and extinguishes it.
		// For this energy fluid we keep it NOT-water, and implement simple viscous drag + buoyancy ourselves.
		if (!world.isClient) {
			Vec3d velocity = entity.getVelocity();

			// Thick fluid: damp horizontal movement hard, clamp downward speed a bit.
			double newY = Math.max(velocity.y * 0.5, -0.08);
			entity.setVelocity(velocity.x * 0.35, newY, velocity.z * 0.35);
			entity.fallDistance = 0.0f;

			// Gentle buoyancy when below the local surface.
			double surfaceY = pos.getY() + state.getFluidState().getHeight(world, pos);
			if (entity.getY() < surfaceY - 0.2) {
				entity.addVelocity(0.0, 0.02, 0.0);
			}
		}

		super.onEntityCollision(state, world, pos, entity);
	}

	@Override
	protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (this.crystallizeIfTouchingWaterOrLava(world, pos)) {
			return;
		}

		super.onBlockAdded(state, world, pos, oldState, notify);
	}

	@Override
	protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		if (this.crystallizeIfTouchingWaterOrLava(world, pos)) {
			return;
		}

		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
	}

	private boolean crystallizeIfTouchingWaterOrLava(World world, BlockPos pos) {
		// Match vanilla-style lava mixing:
		// - Touching water/lava from the sides or above crystallizes immediately.
		// - Water below is handled by the fluid's flow hook to avoid instantly solidifying when placed above water.
		if (this.isReactiveFluid(world.getFluidState(pos.up())) || world.getFluidState(pos.down()).isIn(FluidTags.LAVA)) {
			world.setBlockState(pos, ModBlocks.ENERGY_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
			return true;
		}

		for (Direction direction : Direction.Type.HORIZONTAL) {
			if (this.isReactiveFluid(world.getFluidState(pos.offset(direction)))) {
				world.setBlockState(pos, ModBlocks.ENERGY_BLOCK.getDefaultState(), Block.NOTIFY_ALL);
				return true;
			}
		}

		return false;
	}

	private boolean isReactiveFluid(FluidState fluidState) {
		return fluidState.isIn(FluidTags.WATER) || fluidState.isIn(FluidTags.LAVA);
	}
}
