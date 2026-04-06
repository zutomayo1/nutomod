package com.nutonmod.entity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Set;

public class RiftDashGoal extends Goal {
    private final HostileEntity mob;
    private final double dashSpeed;
    private final double minDistanceSq;
    private final double maxDistanceSq;
    private int cooldownTicks;
    private int windupTicks;
    private int dashTicks;
    private final Set<Integer> hitEntities = new HashSet<>();

    public RiftDashGoal(HostileEntity mob, double dashSpeed, double minDistance, double maxDistance) {
        this.mob = mob;
        this.dashSpeed = dashSpeed;
        this.minDistanceSq = minDistance * minDistance;
        this.maxDistanceSq = maxDistance * maxDistance;
    }

    @Override
    public boolean canStart() {
        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
            return false;
        }
        if (!this.mob.isOnGround()) {
            return false;
        }
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        double distSq = this.mob.squaredDistanceTo(target);
        return distSq >= this.minDistanceSq && distSq <= this.maxDistanceSq;
    }

    @Override
    public boolean shouldContinue() {
        return this.windupTicks > 0 || this.dashTicks > 0;
    }

    @Override
    public void start() {
        this.windupTicks = 10;
        this.dashTicks = 0;
        this.hitEntities.clear();
        this.mob.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.windupTicks = 0;
        this.dashTicks = 0;
        this.mob.setVelocity(this.mob.getVelocity().multiply(0.45D, 1.0D, 0.45D));
        this.cooldownTicks = 140 + this.mob.getRandom().nextInt(80);
        this.hitEntities.clear();
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return;
        }
        this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);
        this.mob.getNavigation().stop();

        if (this.windupTicks > 0) {
            this.windupTicks--;
            if (this.mob.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(
                        ParticleTypes.ELECTRIC_SPARK,
                        this.mob.getX(),
                        this.mob.getBodyY(0.5D),
                        this.mob.getZ(),
                        4,
                        0.18D,
                        0.18D,
                        0.18D,
                        0.03D
                );
            }
            if (this.windupTicks == 0) {
                Vec3d toTarget = target.getPos().subtract(this.mob.getPos());
                Vec3d horizontal = new Vec3d(toTarget.x, 0.0D, toTarget.z).normalize();
                this.mob.setVelocity(horizontal.multiply(this.dashSpeed).add(0.0D, 0.12D, 0.0D));
                this.mob.velocityModified = true;
                this.mob.playSound(SoundEvents.ENTITY_ENDERMAN_SCREAM, 0.6F, 1.2F);
                this.dashTicks = 8;
            }
            return;
        }

        if (this.dashTicks > 0) {
            this.dashTicks--;
            this.hitTargetsOnPath();
        }
    }

    private void hitTargetsOnPath() {
        for (var entity : this.mob.getWorld().getOtherEntities(
                this.mob,
                this.mob.getBoundingBox().expand(0.4D, 0.2D, 0.4D),
                e -> e instanceof LivingEntity living && living.isAlive() && e != this.mob)) {
            if (!this.hitEntities.add(entity.getId())) {
                continue;
            }
            if (entity instanceof LivingEntity living) {
                living.damage(this.mob.getDamageSources().mobAttack(this.mob), 5.0F);
                living.takeKnockback(0.85F, this.mob.getX() - living.getX(), this.mob.getZ() - living.getZ());
                if (living instanceof PlayerEntity player) {
                    player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, 0.5F, 0.8F);
                }
            }
        }
    }
}
