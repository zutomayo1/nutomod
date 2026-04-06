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

public class StormObeliskFeature extends Feature<DefaultFeatureConfig> {
    public StormObeliskFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos top = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, context.getOrigin());
        BlockPos base = top.down();

        BlockState block = ModBlocks.ENERGY_BLOCK.getDefaultState();
        BlockState core = ModBlocks.ENERGY_CORE.getDefaultState().with(com.nutonmod.block.EnergyCoreBlock.ACTIVATED, true);

        for (int y = 0; y < 10; y++) {
            world.setBlockState(base.up(y), block, 3);
        }
        world.setBlockState(base.up(10), core, 3);

        world.setBlockState(base.add(1, 0, 0), block, 3);
        world.setBlockState(base.add(-1, 0, 0), block, 3);
        world.setBlockState(base.add(0, 0, 1), block, 3);
        world.setBlockState(base.add(0, 0, -1), block, 3);
        return true;
    }
}
