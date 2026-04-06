package com.nutonmod.world.gen;

import com.nutonmod.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public final class ModFlowerGeneration {
    private ModFlowerGeneration() {
    }

    public static void addBiomeFeatures() {
        BiomeModifications.addFeature(
                context -> BiomeSelectors.includeByKey(
                        BiomeKeys.PLAINS,
                        BiomeKeys.SUNFLOWER_PLAINS,
                        BiomeKeys.MEADOW,
                        BiomeKeys.FOREST,
                        BiomeKeys.FLOWER_FOREST,
                        BiomeKeys.BIRCH_FOREST,
                        BiomeKeys.CHERRY_GROVE
                ).test(context),
                GenerationStep.Feature.VEGETAL_DECORATION,
                ModPlacedFeatures.ENERGY_FLOWER_PLACED_KEY
        );
    }
}
