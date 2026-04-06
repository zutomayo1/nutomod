package com.nutonmod.world.feature;

import com.mojang.serialization.Codec;
import com.nutonmod.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.Heightmap;

public class EnergyAltarFeature extends Feature<DefaultFeatureConfig> {
    public EnergyAltarFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos top = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, context.getOrigin());
        BlockPos center = top.down();

        if (center.getY() <= world.getBottomY() + 2) {
            return false;
        }

        BlockState base = ModBlocks.ENERGY_BLOCK.getDefaultState();
        BlockState core = ModBlocks.ENERGY_CORE.getDefaultState();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos floor = center.add(dx, 0, dz);
                if (!world.getBlockState(floor).isOf(Blocks.WATER)) {
                    world.setBlockState(floor, base, 3);
                }
            }
        }

        world.setBlockState(center.up(), core, 3);
        for (Direction direction : Direction.Type.HORIZONTAL) {
            world.setBlockState(center.offset(direction).up(), base, 3);
        }
        return true;
    }
}
