package com.nutonmod.entity;

import com.nutonmod.NutonMod;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<EnergyBeing> ENERGY_BEING = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "energy_being"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EnergyBeing::new)
                    .dimensions(EntityType.ZOMBIE.getDimensions())
                    .trackRangeBlocks(10)
                    .trackedUpdateRate(2)
                    .build()
    );

    public static void register() {
        NutonMod.LOGGER.info("Registering entities for {}", NutonMod.MOD_ID);

        FabricDefaultAttributeRegistry.register(ENERGY_BEING, EnergyBeing.createMobAttributes());
        BiomeModifications.addSpawn(
                BiomeSelectors.foundInOverworld(),
                SpawnGroup.MONSTER,
                ENERGY_BEING,
                18,
                1,
                2
        );
    }
}
