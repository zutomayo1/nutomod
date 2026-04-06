package com.nutonmod.world.gen;

import com.nutonmod.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public final class ModTreeGeneration {
    private ModTreeGeneration() {
    }

    public static void addBiomeFeatures() {
        BiomeModifications.addFeature(
                context -> BiomeSelectors.includeByKey(
                        BiomeKeys.FOREST,
                        BiomeKeys.BIRCH_FOREST,
                        BiomeKeys.DARK_FOREST,
                        BiomeKeys.PLAINS,
                        BiomeKeys.MEADOW
                ).test(context),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.ENERGY_TREE_PLACED_KEY
        );
    }
}
