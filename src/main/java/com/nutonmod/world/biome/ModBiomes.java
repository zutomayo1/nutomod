package com.nutonmod.world.biome;

import com.nutonmod.NutonMod;
import com.nutonmod.entity.ModEntities;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public final class ModBiomes {
    public static final RegistryKey<Biome> ENERGY_BIOME = RegistryKey.of(
            RegistryKeys.BIOME,
            Identifier.of(NutonMod.MOD_ID, "energy_biome")
    );

    private ModBiomes() {
    }

    public static void bootstrap(Registerable<Biome> context) {
        RegistryEntryLookup<PlacedFeature> placedFeatures = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        RegistryEntryLookup<ConfiguredCarver<?>> configuredCarvers = context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);
        context.register(ENERGY_BIOME, createEnergyBiome(placedFeatures, configuredCarvers));
    }

    private static Biome createEnergyBiome(
            RegistryEntryLookup<PlacedFeature> placedFeatures,
            RegistryEntryLookup<ConfiguredCarver<?>> configuredCarvers
    ) {
        SpawnSettings.Builder spawns = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addFarmAnimals(spawns);
        DefaultBiomeFeatures.addBatsAndMonsters(spawns);
        spawns.spawn(
                SpawnGroup.MONSTER,
                new SpawnSettings.SpawnEntry(ModEntities.ENERGY_BEING, 40, 1, 3)
        );

        GenerationSettings.LookupBackedBuilder generation = new GenerationSettings.LookupBackedBuilder(
                placedFeatures,
                configuredCarvers
        );
        DefaultBiomeFeatures.addLandCarvers(generation);
        DefaultBiomeFeatures.addDefaultOres(generation);
        DefaultBiomeFeatures.addDefaultDisks(generation);
        DefaultBiomeFeatures.addDefaultFlowers(generation);
        DefaultBiomeFeatures.addDefaultGrass(generation);
        DefaultBiomeFeatures.addDefaultMushrooms(generation);
        DefaultBiomeFeatures.addDefaultVegetation(generation);
        DefaultBiomeFeatures.addSprings(generation);
        DefaultBiomeFeatures.addFrozenTopLayer(generation);
        BiomeEffects effects = new BiomeEffects.Builder()
                .waterColor(0x35C8F2)
                .waterFogColor(0x1376A0)
                .fogColor(0xB3F5FF)
                .skyColor(0x75D9FF)
                .grassColor(0x5BEA92)
                .foliageColor(0x4CD77D)
                .moodSound(BiomeMoodSound.CAVE)
                .build();

        return new Biome.Builder()
                .precipitation(true)
                .temperature(0.7F)
                .downfall(0.8F)
                .effects(effects)
                .spawnSettings(spawns.build())
                .generationSettings(generation.build())
                .build();
    }
}
