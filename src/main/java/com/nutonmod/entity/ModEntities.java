package com.nutonmod.entity;

import com.nutonmod.NutonMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

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
    public static final EntityType<StarPrisonBoltEntity> STAR_PRISON_BOLT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "star_prison_bolt"),
            FabricEntityTypeBuilder.<StarPrisonBoltEntity>create(SpawnGroup.MISC, StarPrisonBoltEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(8)
                    .trackedUpdateRate(10)
                    .build()
    );
    public static final EntityType<VoidRiftSpikeEntity> VOID_RIFT_SPIKE_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "void_rift_spike_projectile"),
            FabricEntityTypeBuilder.<VoidRiftSpikeEntity>create(SpawnGroup.MISC, VoidRiftSpikeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(8)
                    .trackedUpdateRate(10)
                    .build()
    );
    public static final EntityType<EnergySpriteEntity> ENERGY_SPRITE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "energy_sprite"),
            FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT, EnergySpriteEntity::new)
                    .dimensions(EntityDimensions.changing(0.45F, 0.6F))
                    .trackRangeBlocks(10)
                    .trackedUpdateRate(2)
                    .build()
    );
    public static final EntityType<CrystalSnailEntity> CRYSTAL_SNAIL = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "crystal_snail"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CrystalSnailEntity::new)
                    .dimensions(EntityDimensions.changing(0.8F, 0.65F))
                    .trackRangeBlocks(10)
                    .trackedUpdateRate(2)
                    .build()
    );
    public static final EntityType<StormFinchEntity> STORM_FINCH = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "storm_finch"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, StormFinchEntity::new)
                    .dimensions(EntityDimensions.changing(0.55F, 0.75F))
                    .trackRangeBlocks(10)
                    .trackedUpdateRate(2)
                    .build()
    );
    public static final EntityType<EnergyWardenEntity> ENERGY_WARDEN = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "energy_warden"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EnergyWardenEntity::new)
                    .dimensions(EntityDimensions.changing(1.4F, 2.7F))
                    .trackRangeBlocks(12)
                    .trackedUpdateRate(2)
                    .build()
    );
    public static final EntityType<VoidCrawlerEntity> VOID_CRAWLER = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "void_crawler"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, VoidCrawlerEntity::new)
                    .dimensions(EntityDimensions.changing(1.0F, 0.7F))
                    .trackRangeBlocks(10)
                    .trackedUpdateRate(2)
                    .build()
    );
    public static final EntityType<StormElementalEntity> STORM_ELEMENTAL = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(NutonMod.MOD_ID, "storm_elemental"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, StormElementalEntity::new)
                    .dimensions(EntityDimensions.changing(0.9F, 1.8F))
                    .trackRangeBlocks(12)
                    .trackedUpdateRate(2)
                    .build()
    );

    public static void register() {
        NutonMod.LOGGER.info("Registering entities for {}", NutonMod.MOD_ID);

        FabricDefaultAttributeRegistry.register(ENERGY_BEING, EnergyBeing.createMobAttributes());
        FabricDefaultAttributeRegistry.register(RIFT_STALKER, RiftStalker.createMobAttributes());
        FabricDefaultAttributeRegistry.register(SINGULARITY, SingularityEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(STORM_ARBITER, StormArbiterEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(VOID_ARCHON, VoidArchonEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(ENERGY_SPRITE, EnergySpriteEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(CRYSTAL_SNAIL, CrystalSnailEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(STORM_FINCH, StormFinchEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(ENERGY_WARDEN, EnergyWardenEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(VOID_CRAWLER, VoidCrawlerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(STORM_ELEMENTAL, StormElementalEntity.createMobAttributes());

        SpawnRestriction.register(STORM_ELEMENTAL, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StormElementalEntity::canSpawn);
    }
}
