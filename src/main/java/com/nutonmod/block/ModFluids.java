package com.nutonmod.block;

import com.nutonmod.NutonMod;
import com.nutonmod.block.custom.EnergyFluid;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModFluids {
    public static final FlowableFluid FLOWING_ENERGY = register("flowing_energy", new EnergyFluid.Flowing());
    public static final FlowableFluid STILL_ENERGY = register("still_energy", new EnergyFluid.Still());
    private static <T extends Fluid> T register(String id, T value) {
        return Registry.register(Registries.FLUID, Identifier.of(NutonMod.MOD_ID, id), value);
    }
    public static void registerModFluids() {
        NutonMod.LOGGER.info("Registering ModFluids for " + NutonMod.MOD_ID);
    }
}
