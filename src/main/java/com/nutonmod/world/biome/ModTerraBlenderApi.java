package com.nutonmod.world.biome;

import com.nutonmod.NutonMod;
import terrablender.api.TerraBlenderApi;
import terrablender.api.Regions;

public class ModTerraBlenderApi implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new EnergyOverworldRegion());
        NutonMod.LOGGER.info("Initializing TerraBlender for {}", NutonMod.MOD_ID);
    }
}
