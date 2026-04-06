package com.nutonmod.world.feature;

import com.mojang.serialization.Codec;
import com.nutonmod.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class RelayTowerFeature extends Feature<DefaultFeatureConfig> {
    public RelayTowerFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos top = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, context.getOrigin());
        BlockPos base = top.down();

        BlockState block = ModBlocks.ENERGY_BLOCK.getDefaultState();
        BlockState core = ModBlocks.ENERGY_CORE.getDefaultState().with(com.nutonmod.block.EnergyCoreBlock.ACTIVATED, true);

        for (int y = 0; y < 7; y++) {
            world.setBlockState(base.up(y), block, 3);
        }
        world.setBlockState(base.up(7), core, 3);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (Math.abs(dx) + Math.abs(dz) <= 1) {
                    world.setBlockState(base.add(dx, 0, dz), block, 3);
                }
            }
        }
        return true;
    }
}
