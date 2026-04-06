package com.nutonmod.entity.goal;

import com.nutonmod.world.system.EnergyRealmPressureSystem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class RiftBoltGoal extends Goal {
    private final HostileEntity mob;
    private final double minDistanceSq;
    private final double maxDistanceSq;
    private int cooldownTicks;
    private int chargeTicks;

    public RiftBoltGoal(HostileEntity mob, double minDistance, double maxDistance) {
        this.mob = mob;
        this.minDistanceSq = minDistance * minDistance;
        this.maxDistanceSq = maxDistance * maxDistance;
    }

    @Override
    public boolean canStart() {
        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
            return false;
        }
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return false;
        }
        if (!this.mob.canSee(target)) {
            return false;
        }
        double distSq = this.mob.squaredDistanceTo(target);
        return distSq >= this.minDistanceSq && distSq <= this.maxDistanceSq;
    }

    @Override
    public boolean shouldContinue() {
        return this.chargeTicks > 0;
    }

    @Override
    public void start() {
        this.chargeTicks = 14;
        this.mob.getNavigation().stop();
        this.mob.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT, 0.55F, 1.4F);
    }

    @Override
    public void stop() {
        this.chargeTicks = 0;
        int base = 80 + this.mob.getRandom().nextInt(50);
        this.cooldownTicks = base;
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return;
        }
        this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);
        this.mob.getNavigation().stop();

        if (this.chargeTicks > 0 && this.mob.getWorld() instanceof ServerWorld serverWorld) {
            Vec3d castFrom = this.mob.getPos().add(0.0D, this.mob.getHeight() * 0.72D, 0.0D);
            serverWorld.spawnParticles(
                    ParticleTypes.ENCHANT,
                    castFrom.x,
                    castFrom.y,
                    castFrom.z,
                    8,
                    0.2D,
                    0.2D,
                    0.2D,
                    0.02D
            );
        }

        this.chargeTicks--;
        if (this.chargeTicks == 0) {
            fireBolt();
        }
    }

    private void fireBolt() {
        if (!(this.mob.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        LivingEntity target = this.mob.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        Vec3d start = this.mob.getPos().add(0.0D, this.mob.getHeight() * 0.72D, 0.0D);
        Vec3d intendedEnd = target.getPos().add(0.0D, target.getHeight() * 0.55D, 0.0D);
        BlockHitResult blockHit = this.mob.getWorld().raycast(new RaycastContext(
                start,
                intendedEnd,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                this.mob
        ));

        Vec3d hitPos = blockHit.getType() == HitResult.Type.BLOCK ? blockHit.getPos() : intendedEnd;
        Vec3d delta = hitPos.subtract(start);
        double length = Math.max(0.001D, delta.length());
        Vec3d dir = delta.multiply(1.0D / length);
        int steps = Math.max(8, (int) (length * 5.0D));

        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3d p = start.add(dir.multiply(length * t));
            serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK, p.x, p.y, p.z, 1, 0.02D, 0.02D, 0.02D, 0.0D);
            if ((i & 1) == 0) {
                serverWorld.spawnParticles(ParticleTypes.END_ROD, p.x, p.y, p.z, 1, 0.03D, 0.03D, 0.03D, 0.0D);
            }
        }

        serverWorld.spawnParticles(ParticleTypes.EXPLOSION, hitPos.x, hitPos.y, hitPos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK, hitPos.x, hitPos.y, hitPos.z, 15, 0.25D, 0.25D, 0.25D, 0.06D);
        this.mob.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, 0.55F, 1.55F);

        if (blockHit.getType() != HitResult.Type.MISS && blockHit.getType() == HitResult.Type.BLOCK) {
            return;
        }
        if (!this.mob.canSee(target)) {
            return;
        }
        if (this.mob.squaredDistanceTo(target) < this.minDistanceSq || this.mob.squaredDistanceTo(target) > this.maxDistanceSq) {
            return;
        }

        target.damage(this.mob.getDamageSources().mobAttack(this.mob), 5.0F);
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0, true, true, true));
        if (target instanceof PlayerEntity player) {
            EnergyRealmPressureSystem.addPressure(player, 4);
        }
    }
}
