package com.nutonmod.world.system;

import com.nutonmod.entity.ModEntities;
import com.nutonmod.entity.SingularityEntity;
import com.nutonmod.item.ModItems;
import com.nutonmod.world.dimension.ModDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class SingularitySummonSystem {
    private static final RegistryKey<net.minecraft.world.biome.Biome> CORE_WASTES =
            RegistryKey.of(RegistryKeys.BIOME, net.minecraft.util.Identifier.of("nutonmod", "core_wastes"));

    private SingularitySummonSystem() {
    }

    public static boolean trySummon(ServerWorld world, BlockPos altarPos, PlayerEntity player) {
        if (!world.getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return false;
        }
        ItemStack hand = player.getMainHandStack();
        if (!hand.isOf(ModItems.ANNIHILATION_EYE)) {
            return false;
        }
        var biomeKey = world.getBiome(altarPos).getKey();
        if (biomeKey.isEmpty() || !biomeKey.get().equals(CORE_WASTES)) {
            player.sendMessage(Text.translatable("boss.nutonmod.singularity.need_core_wastes"), true);
            return true;
        }
        boolean nearbyBoss = !world.getEntitiesByType(
                ModEntities.SINGULARITY,
                entity -> entity.isAlive() && entity.squaredDistanceTo(Vec3d.ofCenter(altarPos)) <= 96 * 96
        ).isEmpty();
        if (nearbyBoss) {
            player.sendMessage(Text.translatable("boss.nutonmod.singularity.already_active"), true);
            return true;
        }

        if (!player.isCreative()) {
            hand.decrement(1);
        }

        BlockPos center = altarPos.up(2);
        SingularityEntity boss = ModEntities.SINGULARITY.create(world);
        if (boss == null) {
            return true;
        }
        int playersNearby = Math.max(1, world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(Vec3d.ofCenter(altarPos)) <= 48 * 48).size());
        boss.configureForSummon(center, playersNearby);
        boss.refreshPositionAndAngles(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, world.random.nextFloat() * 360.0F, 0.0F);
        boss.setCustomName(Text.translatable("entity.nutonmod.singularity"));
        boss.setCustomNameVisible(true);

        SingularityArenaSystem.createArena(world, boss.getUuid(), center);
        world.spawnEntity(boss);
        world.playSound(null, center, SoundEvents.ENTITY_WITHER_SPAWN, SoundCategory.HOSTILE, 1.4F, 0.8F);
        world.spawnParticles(net.minecraft.particle.ParticleTypes.END_ROD,
                center.getX() + 0.5D, center.getY() + 1.0D, center.getZ() + 0.5D,
                140, 1.2D, 1.2D, 1.2D, 0.08D);

        for (PlayerEntity p : world.getPlayers(pl -> pl.squaredDistanceTo(Vec3d.ofCenter(center)) <= 64 * 64)) {
            p.sendMessage(Text.translatable("boss.nutonmod.singularity.awakened"), false);
        }
        return true;
    }
}
