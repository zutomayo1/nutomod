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
    public static final EntityType<SingularityEntity> SINGULARITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "singularity"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SingularityEntity::new)
                    .dimensions(EntityDimensions.changing(3.5F, 4.5F))
                    .trackRangeBlocks(16)
                    .trackedUpdateRate(1)
                    .build()
    );
    public static final EntityType<StormArbiterEntity> STORM_ARBITER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "storm_arbiter"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, StormArbiterEntity::new)
                    .dimensions(EntityDimensions.changing(1.45F, 3.8F))
                    .trackRangeBlocks(16)
                    .trackedUpdateRate(1)
                    .build()
    );
    public static final EntityType<VoidArchonEntity> VOID_ARCHON = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "void_archon"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, VoidArchonEntity::new)
                    .dimensions(EntityDimensions.changing(2.0F, 4.0F))
                    .trackRangeBlocks(16)
                    .trackedUpdateRate(1)
                    .build()
    );
    public static final EntityType<VoidPiercingArrowEntity> VOID_PIERCING_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "void_piercing_arrow"),
            FabricEntityTypeBuilder.<VoidPiercingArrowEntity>create(SpawnGroup.MISC, VoidPiercingArrowEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(6)
                    .trackedUpdateRate(10)
                    .build()
    );
    public static final EntityType<ThunderSpearProjectileEntity> THUNDER_SPEAR_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "thunder_spear_projectile"),
            FabricEntityTypeBuilder.<ThunderSpearProjectileEntity>create(SpawnGroup.MISC, ThunderSpearProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(8)
                    .trackedUpdateRate(10)
                    .build()
    );
    public static final EntityType<EndJudicatorSlashEntity> END_JUDICATOR_SLASH = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "end_judicator_slash"),
            FabricEntityTypeBuilder.<EndJudicatorSlashEntity>create(SpawnGroup.MISC, EndJudicatorSlashEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .trackRangeBlocks(8)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static void register() {
        NutonMod.LOGGER.info("Registering entities for {}", NutonMod.MOD_ID);

        FabricDefaultAttributeRegistry.register(ENERGY_BEING, EnergyBeing.createMobAttributes());
        FabricDefaultAttributeRegistry.register(RIFT_STALKER, RiftStalker.createMobAttributes());
        FabricDefaultAttributeRegistry.register(SINGULARITY, SingularityEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(STORM_ARBITER, StormArbiterEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(VOID_ARCHON, VoidArchonEntity.createMobAttributes());
    }
}
