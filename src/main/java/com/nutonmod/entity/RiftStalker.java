package com.nutonmod.entity;

import com.nutonmod.entity.goal.RiftDashGoal;
import com.nutonmod.entity.goal.RiftBoltGoal;
import com.nutonmod.item.ModItems;
import com.nutonmod.world.system.EnergyRealmPressureSystem;
import com.nutonmod.world.system.EnergyRealmStormSystem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RiftStalker extends HostileEntity implements GeoEntity {
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.rift_stalker.idle");
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("animation.rift_stalker.walk");
    private static final RawAnimation ATTACK_ANIMATION = RawAnimation.begin().thenPlay("animation.rift_stalker.attack");
    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public RiftStalker(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 12;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new RiftDashGoal(this, 1.35D, 4.0D, 16.0D));
        this.goalSelector.add(2, new RiftBoltGoal(this, 7.0D, 22.0D));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.15D, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 28.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 28.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.5D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient && this.random.nextFloat() < 0.15F) {
            this.getWorld().addParticle(
                    ParticleTypes.ELECTRIC_SPARK,
                    this.getX() + (this.random.nextDouble() - 0.5D) * this.getWidth(),
                    this.getBodyY(0.5D),
                    this.getZ() + (this.random.nextDouble() - 0.5D) * this.getWidth(),
                    0.0D,
                    0.02D,
                    0.0D
            );
        }
        if (!this.getWorld().isClient
                && this.age % 40 == 0
                && this.getWorld() instanceof ServerWorld serverWorld
                && EnergyRealmStormSystem.isEnergyStormActive(serverWorld)) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 100, 0, true, false, true));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 100, 0, true, false, true));
            if (this.random.nextFloat() < 0.12F) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 100, 1, true, false, true));
            }
        }
    }

    @Override
    public boolean tryAttack(net.minecraft.entity.Entity target) {
        boolean hit = super.tryAttack(target);
        if (!hit || !(target instanceof PlayerEntity player)) {
            return hit;
        }
        EnergyRealmPressureSystem.addPressure(player, 6);
        int pressure = EnergyRealmPressureSystem.getPressure(player);
        if (pressure >= 70) {
            player.damage(this.getDamageSources().mobAttack(this), 2.0F);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0, true, true, true));
        }
        return true;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.getWorld().isClient || !(this.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        this.dropStack(new ItemStack(ModItems.STORM_FRAGMENT, this.random.nextFloat() < 0.35F ? 2 : 1));
        if (EnergyRealmStormSystem.isEnergyStormActive(serverWorld) && this.random.nextFloat() < 0.08F) {
            this.dropStack(new ItemStack(ModItems.CRYSTAL_MATRIX, 1));
        }
        if (this.random.nextFloat() < 0.20F) {
            this.dropStack(new ItemStack(ModItems.ALTAR_SHARD, 1));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ENDERMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ENDERMITE_DEATH;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 5, this::animationPredicate));
    }

    private PlayState animationPredicate(AnimationState<RiftStalker> state) {
        if (this.handSwinging) {
            state.setAnimation(ATTACK_ANIMATION);
            return PlayState.CONTINUE;
        }
        if (state.isMoving()) {
            state.setAnimation(WALK_ANIMATION);
            return PlayState.CONTINUE;
        }
        state.setAnimation(IDLE_ANIMATION);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }
}
