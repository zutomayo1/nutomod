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

public class SanctumGateFeature extends Feature<DefaultFeatureConfig> {
    public SanctumGateFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos top = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, context.getOrigin());
        BlockPos base = top.down();

        BlockState block = ModBlocks.ENERGY_BLOCK.getDefaultState();
        BlockState gate = ModBlocks.SANCTUM_GATE.getDefaultState();

        for (int y = 0; y < 4; y++) {
            world.setBlockState(base.up(y), block, 3);
            world.setBlockState(base.add(2, y, 0), block, 3);
        }
        world.setBlockState(base.up(4), block, 3);
        world.setBlockState(base.add(2, 4, 0), block, 3);
        world.setBlockState(base.add(1, 1, 0), gate, 3);
        world.setBlockState(base.add(1, 2, 0), gate, 3);
        world.setBlockState(base.add(1, 0, 1), block, 3);
        world.setBlockState(base.add(1, 0, -1), block, 3);
        return true;
    }
}
