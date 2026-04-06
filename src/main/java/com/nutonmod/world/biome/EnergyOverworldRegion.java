package com.nutonmod.world.biome;

import com.mojang.datafixers.util.Pair;
import com.nutonmod.NutonMod;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class EnergyOverworldRegion extends Region {
    public EnergyOverworldRegion() {
        // Slightly higher weight so the biome appears often enough in survival worlds.
        super(Identifier.of(NutonMod.MOD_ID, "overworld"), RegionType.OVERWORLD, 4);
    }

    @Override
    public void addBiomes(
            Registry<Biome> registry,
            Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> mapper
    ) {
        MultiNoiseUtil.NoiseHypercube temperateLowlands = new MultiNoiseUtil.NoiseHypercube(
                MultiNoiseUtil.ParameterRange.of(-0.2F, 0.3F),
                MultiNoiseUtil.ParameterRange.of(-0.2F, 0.3F),
                MultiNoiseUtil.ParameterRange.of(-0.15F, 0.2F),
                MultiNoiseUtil.ParameterRange.of(-0.5F, 0.0F),
                MultiNoiseUtil.ParameterRange.of(0.0F, 0.4F),
                MultiNoiseUtil.ParameterRange.of(-0.2F, 0.2F),
                0L
        );

        MultiNoiseUtil.NoiseHypercube warmMidlands = new MultiNoiseUtil.NoiseHypercube(
                MultiNoiseUtil.ParameterRange.of(0.0F, 0.55F),
                MultiNoiseUtil.ParameterRange.of(-0.15F, 0.45F),
                MultiNoiseUtil.ParameterRange.of(-0.1F, 0.35F),
                MultiNoiseUtil.ParameterRange.of(-0.35F, 0.2F),
                MultiNoiseUtil.ParameterRange.of(0.0F, 0.6F),
                MultiNoiseUtil.ParameterRange.of(-0.2F, 0.2F),
                0L
        );

        this.addBiome(mapper, temperateLowlands, ModBiomes.ENERGY_BIOME);
        this.addBiome(mapper, warmMidlands, ModBiomes.ENERGY_BIOME);
    }
}
