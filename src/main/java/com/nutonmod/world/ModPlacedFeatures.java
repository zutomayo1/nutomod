package com.nutonmod.world;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class ModPlacedFeatures {
    public static final RegistryKey<PlacedFeature> ENERGY_TREE_PLACED_KEY = of("energy_tree_placed");
    public static final RegistryKey<PlacedFeature> ENERGY_FLOWER_PLACED_KEY = of("energy_flower_placed");

    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registerEntryLookup =
                featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        PlacedFeatures.register(
                featureRegisterable,
                ENERGY_TREE_PLACED_KEY,
                registerEntryLookup.getOrThrow(ModConfiguredFeatures.ENERGY_TREE_KEY),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(2, 0.1f, 2),
                        ModBlocks.ENERGY_SAPLING
                )
        );

        PlacedFeatures.register(
                featureRegisterable,
                ENERGY_FLOWER_PLACED_KEY,
                registerEntryLookup.getOrThrow(ModConfiguredFeatures.ENERGY_FLOWER_KEY),
                CountPlacementModifier.of(4),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
        );
    }

    public static RegistryKey<PlacedFeature> of(String id) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(NutonMod.MOD_ID, id));
    }
}
