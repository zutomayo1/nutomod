package com.nutonmod.world.feature;

import com.nutonmod.NutonMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public final class ModFeatures {
    public static final Feature<DefaultFeatureConfig> ENERGY_ALTAR = Registry.register(
            Registries.FEATURE,
            Identifier.of(NutonMod.MOD_ID, "energy_altar"),
            new EnergyAltarFeature(DefaultFeatureConfig.CODEC)
    );
    public static final Feature<DefaultFeatureConfig> RELAY_TOWER = Registry.register(
            Registries.FEATURE,
            Identifier.of(NutonMod.MOD_ID, "relay_tower"),
            new RelayTowerFeature(DefaultFeatureConfig.CODEC)
    );
    public static final Feature<DefaultFeatureConfig> STORM_OBELISK = Registry.register(
            Registries.FEATURE,
            Identifier.of(NutonMod.MOD_ID, "storm_obelisk"),
            new StormObeliskFeature(DefaultFeatureConfig.CODEC)
    );
    public static final Feature<DefaultFeatureConfig> SANCTUM_GATE = Registry.register(
            Registries.FEATURE,
            Identifier.of(NutonMod.MOD_ID, "sanctum_gate"),
            new SanctumGateFeature(DefaultFeatureConfig.CODEC)
    );

    private ModFeatures() {
    }

    public static void register() {
        NutonMod.LOGGER.info("Registering features for {}", NutonMod.MOD_ID);
    }
}
