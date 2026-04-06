package com.nutonmod.entity;

import com.nutonmod.NutonMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
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
    public static final EntityType<RiftStalker> RIFT_STALKER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "rift_stalker"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RiftStalker::new)
                    .dimensions(EntityDimensions.changing(0.95F, 2.55F))
                    .trackRangeBlocks(10)
                    .trackedUpdateRate(2)
                    .build()
    );

    public static void register() {
        NutonMod.LOGGER.info("Registering entities for {}", NutonMod.MOD_ID);

        FabricDefaultAttributeRegistry.register(ENERGY_BEING, EnergyBeing.createMobAttributes());
        FabricDefaultAttributeRegistry.register(RIFT_STALKER, RiftStalker.createMobAttributes());
    }
}
