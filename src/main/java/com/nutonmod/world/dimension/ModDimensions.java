package com.nutonmod.world.dimension;

import com.nutonmod.NutonMod;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public final class ModDimensions {
    public static final Identifier ENERGY_REALM_ID = Identifier.of(NutonMod.MOD_ID, "energy_realm");
    public static final RegistryKey<World> ENERGY_REALM_WORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, ENERGY_REALM_ID);
    public static final RegistryKey<DimensionType> ENERGY_REALM_DIM_TYPE_KEY =
            RegistryKey.of(RegistryKeys.DIMENSION_TYPE, ENERGY_REALM_ID);
    public static final RegistryKey<DimensionOptions> ENERGY_REALM_DIM_KEY =
            RegistryKey.of(RegistryKeys.DIMENSION, ENERGY_REALM_ID);

    private ModDimensions() {
    }
}
