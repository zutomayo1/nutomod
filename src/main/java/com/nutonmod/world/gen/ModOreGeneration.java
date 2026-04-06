package com.nutonmod.world.gen;

import com.nutonmod.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public final class ModOreGeneration {
    private ModOreGeneration() {
    }

    public static void addBiomeFeatures() {
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ENERGY_ORE_PLACED_KEY
        );
    }
}
