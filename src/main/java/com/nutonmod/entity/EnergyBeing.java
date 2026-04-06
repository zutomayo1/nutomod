package com.nutonmod.entity;

import com.nutonmod.item.ModItems;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class EnergyBeing extends HostileEntity implements GeoEntity {
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.energy_being.idle");
    private static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenLoop("animation.energy_being.walk");
    private static final RawAnimation ATTACK_ANIMATION = RawAnimation.begin().thenPlay("animation.energy_being.attack");

    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    public EnergyBeing(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 8;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.15D, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 28.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.30)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.4);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient && this.random.nextFloat() < 0.2F) {
            this.getWorld().addParticle(
                    ParticleTypes.END_ROD,
                    this.getX() + (this.random.nextDouble() - 0.5D) * this.getWidth(),
                    this.getBodyY(0.6D),
                    this.getZ() + (this.random.nextDouble() - 0.5D) * this.getWidth(),
                    0.0D,
                    0.02D,
                    0.0D
            );
        }

        if (!this.getWorld().isClient
                && this.age % 40 == 0
                && this.getWorld() instanceof net.minecraft.server.world.ServerWorld serverWorld
                && EnergyRealmStormSystem.isEnergyStormActive(serverWorld)) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 120, 0, true, false, true));
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 120, 0, true, false, true));
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient && this.getWorld() instanceof net.minecraft.server.world.ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    this.getX(),
                    this.getBodyY(0.5D),
                    this.getZ(),
                    10,
                    0.35D,
                    0.35D,
                    0.35D,
                    0.0D
            );
        }
        return super.damage(source, amount);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLOCK_RESPAWN_ANCHOR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.getWorld().isClient || !(this.getWorld() instanceof net.minecraft.server.world.ServerWorld serverWorld)) {
            return;
        }
        if (!(damageSource.getAttacker() instanceof PlayerEntity player)) {
            return;
        }
        if (!EnergyRealmStormSystem.isEnergyStormActive(serverWorld)) {
            return;
        }

        int bonusCount = EnergyRealmStormSystem.isNearStormObelisk(player) ? 2 : 1;
        this.dropStack(new ItemStack(ModItems.STORM_FRAGMENT, bonusCount));
        if (EnergyRealmStormSystem.isStormborn(this)) {
            this.dropStack(new ItemStack(ModItems.STORM_FRAGMENT, 2));
            if (this.random.nextFloat() < 0.35F) {
                this.dropStack(new ItemStack(ModItems.STORM_ALLOY, 1));
            }
        }
    }

    @Override
    protected void playStepSound(net.minecraft.util.math.BlockPos pos, net.minecraft.block.BlockState state) {
        this.playSound(SoundEvents.BLOCK_AMETHYST_BLOCK_STEP, 0.15F, 1.1F);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 5, this::animationPredicate));
    }

    private PlayState animationPredicate(AnimationState<EnergyBeing> state) {
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
