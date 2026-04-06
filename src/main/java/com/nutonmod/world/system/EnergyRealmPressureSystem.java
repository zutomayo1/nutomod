package com.nutonmod.world.system;

import com.nutonmod.item.ModItems;
import com.nutonmod.world.dimension.ModDimensions;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class EnergyRealmPressureSystem {
    private static final int MAX_PRESSURE = 100;
    private static final int HIGH_PRESSURE_THRESHOLD = 70;
    private static final Map<UUID, Integer> PRESSURE = new HashMap<>();
    private static final RegistryKey<net.minecraft.world.biome.Biome> STORM_FIELDS_BIOME =
            RegistryKey.of(net.minecraft.registry.RegistryKeys.BIOME, Identifier.of("nutonmod", "storm_fields"));
    private static final RegistryKey<net.minecraft.world.biome.Biome> FRACTURE_CANYONS_BIOME =
            RegistryKey.of(net.minecraft.registry.RegistryKeys.BIOME, Identifier.of("nutonmod", "fracture_canyons"));
    private static final RegistryKey<net.minecraft.world.biome.Biome> CORE_WASTES_BIOME =
            RegistryKey.of(net.minecraft.registry.RegistryKeys.BIOME, Identifier.of("nutonmod", "core_wastes"));

    private EnergyRealmPressureSystem() {
    }

    public static void applyPerTick(PlayerEntity player) {
        if (!player.getWorld().getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            PRESSURE.remove(player.getUuid());
            return;
        }
        if (player.age % 40 != 0) {
            return;
        }

        boolean hasStabilizer = player.getMainHandStack().isOf(ModItems.CORE_STABILIZER)
                || player.getOffHandStack().isOf(ModItems.CORE_STABILIZER);
        boolean fullEnergyArmor = player.getEquippedStack(EquipmentSlot.HEAD).isOf(ModItems.ENERGY_HELMET)
                && player.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.ENERGY_CHESTPLATE)
                && player.getEquippedStack(EquipmentSlot.LEGS).isOf(ModItems.ENERGY_LEGGINGS)
                && player.getEquippedStack(EquipmentSlot.FEET).isOf(ModItems.ENERGY_BOOTS);
        boolean beaconProtected = StabilizerBeaconSystem.isProtected(player);
        boolean stormActive = player.getWorld() instanceof net.minecraft.server.world.ServerWorld serverWorld
                && EnergyRealmStormSystem.isEnergyStormActive(serverWorld);

        int pressureDelta = getBiomePressureDelta(player);
        if (stormActive) {
            pressureDelta += 1;
        }

        if (hasStabilizer || fullEnergyArmor) {
            addPressure(player, -4);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 120, 0, true, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 120, 0, true, false, true));
            return;
        }
        if (beaconProtected) {
            addPressure(player, -6);
            player.removeStatusEffect(StatusEffects.WEAKNESS);
            player.removeStatusEffect(StatusEffects.MINING_FATIGUE);
            player.removeStatusEffect(StatusEffects.NAUSEA);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 0, true, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 100, 0, true, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 0, true, false, true));
            return;
        }

        addPressure(player, pressureDelta);
        int pressure = getPressure(player);
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 120, 0, true, true, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 120, 0, true, true, true));
        if (pressure >= 45) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 80, 0, true, true, true));
        }
        if (pressure >= HIGH_PRESSURE_THRESHOLD) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 0, true, true, true));
        }
    }

    public static void addPressure(PlayerEntity player, int delta) {
        UUID id = player.getUuid();
        int current = PRESSURE.getOrDefault(id, 0);
        int next = Math.max(0, Math.min(MAX_PRESSURE, current + delta));
        PRESSURE.put(id, next);
    }

    public static int getPressure(PlayerEntity player) {
        return PRESSURE.getOrDefault(player.getUuid(), 0);
    }

    public static boolean isHighPressure(PlayerEntity player) {
        return getPressure(player) >= HIGH_PRESSURE_THRESHOLD;
    }

    private static int getBiomePressureDelta(PlayerEntity player) {
        var biomeKey = player.getWorld().getBiome(player.getBlockPos()).getKey();
        if (biomeKey.isEmpty()) {
            return 2;
        }
        RegistryKey<net.minecraft.world.biome.Biome> key = biomeKey.get();
        if (key.equals(FRACTURE_CANYONS_BIOME)) {
            return 4;
        }
        if (key.equals(STORM_FIELDS_BIOME) || key.equals(CORE_WASTES_BIOME)) {
            return 3;
        }
        return 2;
    }

    public static void onPlayerLeaveWorld(ServerPlayerEntity player, World world) {
        if (!world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            PRESSURE.remove(player.getUuid());
        }
    }
}
