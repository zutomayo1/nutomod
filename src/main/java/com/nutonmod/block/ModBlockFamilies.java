package com.nutonmod.block;

import com.google.common.collect.Maps;

import com.nutonmod.NutonMod;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.stream.Stream;

public class ModBlockFamilies {
    private static final Map<Block, BlockFamily.Builder> BASE_BLOCKS_TO_BUILDERS = Maps.newHashMap();
    public static final Map<Block, BlockFamily> BASE_BLOCKS_TO_FAMILIES = Maps.newHashMap();

    public static final BlockFamily ENERGY = register(ModBlocks.ENERGY_BLOCK)
            .stairs(ModBlocks.ENERGY_STAIRS)
            .slab(ModBlocks.ENERGY_SLAB)
            .button(ModBlocks.ENERGY_BUTTON)
            .pressurePlate(ModBlocks.ENERGY_PRESSURE_PLATE)
            .fence(ModBlocks.ENERGY_FENCE)
            .fenceGate(ModBlocks.ENERGY_FENCE_GATE)
            .wall(ModBlocks.ENERGY_WALL)
            .door(ModBlocks.ENERGY_DOOR)
            .trapdoor(ModBlocks.ENERGY_TRAPDOOR)
            .unlockCriterionName("has_energy_block")
            .build();
    
    static {
        // 在静态块中将构建好的 BlockFamily 注册到 Map 中
        BASE_BLOCKS_TO_FAMILIES.put(ModBlocks.ENERGY_BLOCK, ENERGY);
    }

    public static BlockFamily.Builder register(Block baseBlock) {
        if (BASE_BLOCKS_TO_BUILDERS.containsKey(baseBlock)) {
            throw new IllegalStateException("Duplicate block family registration for block: " + Registries.BLOCK.getId(baseBlock));
        }
        BlockFamily.Builder builder = new BlockFamily.Builder(baseBlock);
        BASE_BLOCKS_TO_BUILDERS.put(baseBlock, builder);
        return builder;
    }
    
    public static Stream<BlockFamily> getBlockFamilies() {
        return BASE_BLOCKS_TO_FAMILIES.values().stream();
    }

    public static class ModFluids {
        private static <T extends Fluid> T register(String id, T value) {
            return Registry.register(Registries.FLUID, Identifier.of(NutonMod.MOD_ID, id), value);
        }

        static {
            for (Fluid fluid : Registries.FLUID) {
                for (FluidState fluidState : fluid.getStateManager().getStates()) {
                    Fluid.STATE_IDS.add(fluidState);
                }
            }
        }
    }
}
