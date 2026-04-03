package com.nutonmod.world;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;

public class ModPlacedFeatures {
    public static final RegistryKey<PlacedFeature> ENERGY_TREE_PLACED_KEY = of("energy_tree_placed");
    public static void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
        RegistryEntryLookup<ConfiguredFeature<?,?>> registerEntryLookup = featureRegisterable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        PlacedFeatures.register(featureRegisterable, ENERGY_TREE_PLACED_KEY, registerEntryLookup.getOrThrow(ModConfiguredFeatures.ENERGY_TREE_KEY),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        PlacedFeatures.createCountExtraModifier(2, 0.1f, 2),
                        ModBlocks.ENERGY_SAPLING));
    }
    public static RegistryKey<PlacedFeature> of(String id){
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(NutonMod.MOD_ID, id));
    }
}

