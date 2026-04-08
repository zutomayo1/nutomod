package com.nutonmod.entity;

import com.nutonmod.sound.ModSoundEvents;
import com.nutonmod.world.system.SingularityArenaSystem;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SingularityEntity extends HostileEntity implements GeoEntity {
    private static final RawAnimation IDLE_PHASE_1_ANIMATION = RawAnimation.begin().thenLoop("animation.singularity.idle_phase_1");
    private static final RawAnimation IDLE_PHASE_2_ANIMATION = RawAnimation.begin().thenLoop("animation.singularity.idle_phase_2");
    private static final RawAnimation IDLE_PHASE_3_ANIMATION = RawAnimation.begin().thenLoop("animation.singularity.idle_phase_3");
    private static final RawAnimation CAST_ANIMATION = RawAnimation.begin().thenLoop("animation.singularity.cast");

    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    private Phase phase = Phase.PHASE_1;
    private AttackMode attackMode = AttackMode.IDLE;
    private int attackCooldown = 80;
    private int stateTicks = 0;
    private int waveTick = 0;
    private BlockPos arenaCenter = BlockPos.ORIGIN;
    private boolean arenaBound = false;

    private final Set<UUID> ringHit = new HashSet<>();
    private final Set<UUID> rampageHit = new HashSet<>();
    private final List<GravityWell> activeWells = new ArrayList<>();
    private final List<Vec3d> novaShieldAnchors = new ArrayList<>();
    private Vec3d blackHoleCenter = null;
    private Vec3d rampageDirection = Vec3d.ZERO;
    private int rampageTicksLeft = 0;
    private int rampageChargesDone = 0;
    private int nextRampageTick = 0;
    private final List<UUID> novaShieldCloudIds = new ArrayList<>();
    private boolean decoy = false;
    private int decoyLifeTicks = 0;
    private final Set<UUID> damagedPlayers = new HashSet<>();
    private final Set<UUID> participatingPlayers = new HashSet<>();
    private float orbitAngle = 0.0F;
    private int targetSwitchCooldown = 0;
    private int tacticalBlinkCooldown = 0;
    private AttackMode lastAttack = AttackMode.IDLE;
    private int sameAttackChain = 0;
    private int phase3MajorLockTicks = 0;
    private final ServerBossBar bossBar = new ServerBossBar(
            Text.translatable("boss.nutonmod.singularity.bar.phase_1"),
            BossBar.Color.BLUE,
            BossBar.Style.NOTCHED_10
    );

    public SingularityEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 100;
        this.setNoGravity(true);
        this.setPersistent();
        this.bossBar.setDarkenSky(true);
        this.bossBar.setThickenFog(true);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SingularityCombatGoal(this));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 24.0F));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 600.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0D)
                .add(EntityAttributes.GENERIC_ARMOR, 12.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D);
    }

    public void configureForSummon(BlockPos center, int playerCount) {
        this.arenaCenter = center.toImmutable();
        this.arenaBound = true;
        var maxHealthAttr = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealthAttr != null) {
            double scaledMax = 600.0D * Math.max(1, playerCount);
            maxHealthAttr.setBaseValue(scaledMax);
            this.setHealth((float) scaledMax);
        }
    }

    public void configureAsDecoy(SingularityEntity owner) {
        this.decoy = true;
        this.phase = Phase.PHASE_2;
        this.attackMode = AttackMode.IDLE;
        this.attackCooldown = 15;
        this.stateTicks = 0;
        this.waveTick = 0;
        this.arenaCenter = owner.arenaCenter;
        this.arenaBound = owner.arenaBound;
        this.experiencePoints = 0;
        this.decoyLifeTicks = 20 * 20;
        var maxHealthAttr = this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (maxHealthAttr != null) {
            maxHealthAttr.setBaseValue(180.0D);
            this.setHealth(180.0F);
        } else {
            this.setHealth(Math.min(this.getHealth(), 180.0F));
        }
        this.setCustomName(Text.translatable("boss.nutonmod.singularity.decoy_name"));
        this.setCustomNameVisible(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.arenaBound && this.arenaCenter == BlockPos.ORIGIN && this.age <= 5) {
            this.arenaCenter = this.getBlockPos().toImmutable();
        }
        hoverMovement();
        if (this.getWorld().isClient) {
            clientParticles();
            return;
        }
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        if (this.targetSwitchCooldown > 0) {
            this.targetSwitchCooldown--;
        }
        if (this.tacticalBlinkCooldown > 0) {
            this.tacticalBlinkCooldown--;
        }
        if (this.phase3MajorLockTicks > 0) {
            this.phase3MajorLockTicks--;
        }

        if (this.decoy) {
            tickDecoy(serverWorld);
            return;
        }
        updatePhase(serverWorld);
        updateBossBar();
        tickPersistentHazards(serverWorld);
        acquireTarget(serverWorld);
    }

    private void updateBossBar() {
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        switch (this.phase) {
            case PHASE_1 -> {
                this.bossBar.setColor(BossBar.Color.BLUE);
                this.bossBar.setName(Text.translatable("boss.nutonmod.singularity.bar.phase_1"));
            }
            case PHASE_2 -> {
                this.bossBar.setColor(BossBar.Color.YELLOW);
                this.bossBar.setName(Text.translatable("boss.nutonmod.singularity.bar.phase_2"));
            }
            case PHASE_3 -> {
                this.bossBar.setColor(BossBar.Color.RED);
                this.bossBar.setName(Text.translatable("boss.nutonmod.singularity.bar.phase_3"));
            }
        }
    }

    private void tickDecoy(ServerWorld world) {
        if (this.decoyLifeTicks-- <= 0) {
            this.discard();
            return;
        }
        acquireTarget(world);
        if (!(this.getTarget() instanceof PlayerEntity player) || !player.isAlive()) {
            return;
        }
        if (this.age % 24 != 0) {
            return;
        }
        fireBoltLine(world, player, 6.0F, StatusEffects.WEAKNESS, 50);
        world.spawnParticles(ParticleTypes.CLOUD, this.getX(), this.getBodyY(0.4), this.getZ(), 8, 0.3, 0.3, 0.3, 0.01);
    }

    private void acquireTarget(ServerWorld serverWorld) {
        for (PlayerEntity p : serverWorld.getPlayers(pl -> pl.isAlive() && pl.squaredDistanceTo(this) <= 56 * 56)) {
            this.participatingPlayers.add(p.getUuid());
        }

        LivingEntity current = this.getTarget();
        boolean currentValid = current != null
                && current.isAlive()
                && this.squaredDistanceTo(current) <= 62 * 62;

        if (currentValid && this.targetSwitchCooldown > 0) {
            return;
        }

        PlayerEntity best = chooseBestTarget(serverWorld);
        if (best != null && best != current) {
            this.setTarget(best);
            this.targetSwitchCooldown = 24;
            return;
        }

        if (!currentValid) {
            LivingEntity nearest = serverWorld.getClosestPlayer(this, 40.0D);
            if (nearest instanceof PlayerEntity player) {
                this.setTarget(player);
                this.targetSwitchCooldown = 14;
            }
        }
    }

    private PlayerEntity chooseBestTarget(ServerWorld world) {
        List<? extends PlayerEntity> candidates = world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this) <= 42 * 42);
        if (candidates.isEmpty()) {
            return null;
        }
        Vec3d arena = Vec3d.ofCenter(this.arenaCenter);
        PlayerEntity best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (PlayerEntity player : candidates) {
            double distance = Math.sqrt(player.squaredDistanceTo(this));
            double arenaDistance = Math.sqrt(player.getPos().squaredDistanceTo(arena));
            double healthRatio = player.getHealth() / Math.max(player.getMaxHealth(), 1.0F);

            double score = 0.0D;
            score += (22.0D - Math.min(distance, 22.0D)) * 1.15D;
            score += (1.0D - healthRatio) * 10.0D;
            score += this.canSee(player) ? 2.8D : -1.4D;
            if (arenaDistance > 13.0D) {
                score += 6.0D;
            }
            if (player == this.getTarget()) {
                score += 2.0D;
            }
            score += this.random.nextDouble() * 1.2D;

            if (score > bestScore) {
                bestScore = score;
                best = player;
            }
        }
        return best;
    }

    private void hoverMovement() {
        double baseY;
        if (this.arenaBound) {
            baseY = this.arenaCenter.getY() + 2.5D;
        } else if (this.getTarget() != null && this.getTarget().isAlive()) {
            baseY = this.getTarget().getY() + 2.2D;
        } else {
            baseY = this.getY();
        }
        double osc = Math.sin((this.age % 120) / 120.0D * Math.PI * 2.0D) * 0.18D;
        double targetY = baseY + osc;
        double yMotion = (targetY - this.getY()) * 0.12D;
        Vec3d horizontal = computeHorizontalMotion();
        this.setVelocity(this.getVelocity().multiply(0.75D, 0.72D, 0.75D).add(horizontal.x, yMotion, horizontal.z));
        this.velocityModified = true;
    }

    private Vec3d computeHorizontalMotion() {
        double phaseSpeed = switch (this.phase) {
            case PHASE_1 -> 0.065D;
            case PHASE_2 -> 0.085D;
            case PHASE_3 -> 0.115D;
        };
        this.orbitAngle += (float) phaseSpeed;
        double orbitRadius = this.decoy ? 4.0D : (this.phase == Phase.PHASE_3 ? 7.5D : 9.0D);
        if (this.attackMode == AttackMode.LASER || this.attackMode == AttackMode.GRAVITY_WELL) {
            orbitRadius = Math.max(5.3D, orbitRadius - 2.2D);
        } else if (this.attackMode == AttackMode.RAMPAGE) {
            orbitRadius = 4.4D;
        } else if (this.attackMode == AttackMode.BLACK_HOLE) {
            orbitRadius = 6.0D;
        }
        Vec3d orbitCenter;
        if (this.getTarget() != null && this.getTarget().isAlive()) {
            orbitCenter = this.getTarget().getPos();
        } else if (this.arenaBound) {
            orbitCenter = Vec3d.ofCenter(this.arenaCenter);
        } else {
            orbitCenter = this.getPos();
        }
        double ox = orbitCenter.x + Math.cos(this.orbitAngle) * orbitRadius;
        double oz = orbitCenter.z + Math.sin(this.orbitAngle) * orbitRadius;
        Vec3d desired = new Vec3d(ox - this.getX(), 0.0D, oz - this.getZ());
        if (desired.lengthSquared() < 1.0E-4D) {
            return Vec3d.ZERO;
        }

        double speed = phaseSpeed * 1.9D;
        if (this.getTarget() != null) {
            double targetDist = Math.sqrt(this.getTarget().squaredDistanceTo(this));
            if (targetDist > 16.0D) {
                speed *= 1.35D;
            } else if (targetDist < 4.0D && this.attackMode == AttackMode.IDLE) {
                speed *= 1.15D;
            }
        }
        return desired.normalize().multiply(speed);
    }

    private void updatePhase(ServerWorld world) {
        float pct = this.getHealth() / this.getMaxHealth();
        Phase next = pct <= 0.30F ? Phase.PHASE_3 : (pct <= 0.60F ? Phase.PHASE_2 : Phase.PHASE_1);
        if (next == this.phase) {
            return;
        }
        this.phase = next;
        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getBodyY(0.5), this.getZ(), 2, 0.3, 0.3, 0.3, 0.0);
        world.spawnParticles(ParticleTypes.END_ROD, this.getX(), this.getBodyY(0.5), this.getZ(), 50, 0.9, 0.9, 0.9, 0.06);
        this.playSound(ModSoundEvents.SINGULARITY_PHASE_SHIFT, 1.2F, 0.7F + this.phase.ordinal() * 0.15F);
        Text phaseMsg = switch (this.phase) {
            case PHASE_2 -> Text.translatable("boss.nutonmod.singularity.phase_2");
            case PHASE_3 -> Text.translatable("boss.nutonmod.singularity.phase_3");
            default -> null;
        };
        if (phaseMsg != null) {
            broadcastAround(world, phaseMsg);
        }
        this.attackCooldown = 25;
        this.attackMode = AttackMode.IDLE;
        this.lastAttack = AttackMode.IDLE;
        this.sameAttackChain = 0;
        this.stateTicks = 0;
        this.waveTick = 0;
    }

    private void runAttackState(ServerWorld world) {
        if (this.attackMode == AttackMode.IDLE) {
            maybeTacticalBlink(world);
            if (this.attackCooldown > 0) {
                this.attackCooldown--;
                return;
            }
            chooseNextAttack(world);
            this.waveTick = 0;
            this.ringHit.clear();
            return;
        }

        this.stateTicks--;
        this.waveTick++;
        switch (this.attackMode) {
            case BARRAGE -> tickBarrage(world);
            case PULL -> tickPull(world);
            case RING -> tickRing(world);
            case SUMMON -> tickSummonMinions(world);
            case LASER -> tickLaser(world);
            case GRAVITY_WELL -> tickGravityWellCast(world);
            case MIRROR -> tickMirrorPhantoms(world);
            case BLACK_HOLE -> tickBlackHole(world);
            case RAMPAGE -> tickRampage(world);
            case NOVA -> tickAnnihilationNova(world);
            default -> {
            }
        }

        if (this.stateTicks <= 0) {
            this.attackMode = AttackMode.IDLE;
            int baseCooldown = switch (this.phase) {
                case PHASE_1 -> 40 + this.random.nextInt(25);
                case PHASE_2 -> 30 + this.random.nextInt(20);
                case PHASE_3 -> 22 + this.random.nextInt(15);
            };
            int players = getNearbyPlayerCount(52.0D);
            int phase3Extra = this.phase == Phase.PHASE_3 ? Math.min(14, players * 3) : 0;
            this.attackCooldown = baseCooldown + phase3Extra;
            this.novaShieldAnchors.clear();
            this.blackHoleCenter = null;
            this.rampageTicksLeft = 0;
            this.rampageChargesDone = 0;
            this.nextRampageTick = 0;
            this.rampageHit.clear();
            clearShieldClouds(world);
        }
    }

    private void chooseNextAttack(ServerWorld world) {
        List<? extends PlayerEntity> players = world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this) <= 40 * 40);
        LivingEntity target = this.getTarget();
        double targetDistance = target == null ? 99.0D : Math.sqrt(target.squaredDistanceTo(this));
        boolean targetVisible = target != null && this.canSee(target);

        int closePlayers = 0;
        int farPlayers = 0;
        int clusteredPlayers = 0;
        for (PlayerEntity player : players) {
            double dist = Math.sqrt(player.squaredDistanceTo(this));
            if (dist < 6.5D) {
                closePlayers++;
            } else if (dist > 16.0D) {
                farPlayers++;
            }
            if (isPlayerClustered(player, players, 6.0D)) {
                clusteredPlayers++;
            }
        }

        AttackMode planned;
        switch (this.phase) {
            case PHASE_1 -> {
                if (closePlayers >= 2 || targetDistance < 5.5D) {
                    planned = this.random.nextBoolean() ? AttackMode.RING : AttackMode.PULL;
                } else if (targetDistance > 13.0D || farPlayers > 0) {
                    planned = this.random.nextFloat() < 0.72F ? AttackMode.BARRAGE : AttackMode.SUMMON;
                } else {
                    int roll = this.random.nextInt(100);
                    planned = roll < 34 ? AttackMode.BARRAGE
                            : roll < 58 ? AttackMode.PULL
                            : roll < 80 ? AttackMode.RING
                            : AttackMode.SUMMON;
                }
            }
            case PHASE_2 -> {
                if (targetVisible && targetDistance > 12.0D) {
                    planned = AttackMode.LASER;
                } else if (clusteredPlayers >= 2) {
                    planned = AttackMode.GRAVITY_WELL;
                } else if (this.random.nextFloat() < 0.20F) {
                    planned = AttackMode.MIRROR;
                } else {
                    int roll = this.random.nextInt(100);
                    planned = roll < 20 ? AttackMode.BARRAGE
                            : roll < 40 ? AttackMode.LASER
                            : roll < 58 ? AttackMode.GRAVITY_WELL
                            : roll < 75 ? AttackMode.PULL
                            : roll < 88 ? AttackMode.RING
                            : roll < 95 ? AttackMode.MIRROR
                            : AttackMode.SUMMON;
                }
            }
            case PHASE_3 -> {
                boolean majorLocked = this.phase3MajorLockTicks > 0;
                if (!majorLocked && clusteredPlayers >= 2 && this.random.nextFloat() < 0.50F) {
                    planned = AttackMode.NOVA;
                } else if (!majorLocked && ((!targetVisible && targetDistance > 10.0D) || farPlayers >= 1)) {
                    planned = this.random.nextFloat() < 0.58F ? AttackMode.BLACK_HOLE : AttackMode.LASER;
                } else if (!majorLocked && targetDistance < 7.5D) {
                    planned = AttackMode.RAMPAGE;
                } else {
                    int roll = this.random.nextInt(100);
                    if (!majorLocked) {
                        planned = roll < 20 ? AttackMode.NOVA
                                : roll < 38 ? AttackMode.BLACK_HOLE
                                : roll < 58 ? AttackMode.RAMPAGE
                                : roll < 76 ? AttackMode.LASER
                                : roll < 90 ? AttackMode.GRAVITY_WELL
                                : AttackMode.BARRAGE;
                    } else {
                        planned = roll < 45 ? AttackMode.LASER
                                : roll < 72 ? AttackMode.GRAVITY_WELL
                                : AttackMode.BARRAGE;
                    }
                }
            }
            default -> planned = AttackMode.BARRAGE;
        }

        if (planned == this.lastAttack && this.sameAttackChain >= 1) {
            planned = chooseFallbackAttack();
        }
        if (planned == this.lastAttack) {
            this.sameAttackChain++;
        } else {
            this.sameAttackChain = 0;
        }
        this.lastAttack = planned;
        this.attackMode = planned;
        this.stateTicks = attackDuration(planned);
        if (this.phase == Phase.PHASE_3 && isMajorPhase3Attack(planned)) {
            int nearbyPlayers = getNearbyPlayerCount(52.0D);
            this.phase3MajorLockTicks = 40 + nearbyPlayers * 8;
        }
    }

    private AttackMode chooseFallbackAttack() {
        return switch (this.phase) {
            case PHASE_1 -> this.random.nextBoolean() ? AttackMode.PULL : AttackMode.RING;
            case PHASE_2 -> {
                int roll = this.random.nextInt(4);
                yield roll == 0 ? AttackMode.LASER
                        : roll == 1 ? AttackMode.GRAVITY_WELL
                        : roll == 2 ? AttackMode.PULL
                        : AttackMode.MIRROR;
            }
            case PHASE_3 -> {
                int roll = this.random.nextInt(4);
                yield roll == 0 ? AttackMode.RAMPAGE
                        : roll == 1 ? AttackMode.BLACK_HOLE
                        : roll == 2 ? AttackMode.LASER
                        : AttackMode.GRAVITY_WELL;
            }
        };
    }

    private int attackDuration(AttackMode mode) {
        return switch (mode) {
            case BARRAGE -> this.phase == Phase.PHASE_1 ? 42 : (this.phase == Phase.PHASE_2 ? 48 : 50);
            case PULL -> this.phase == Phase.PHASE_1 ? 55 : 60;
            case RING -> this.phase == Phase.PHASE_1 ? 30 : 32;
            case SUMMON -> this.phase == Phase.PHASE_1 ? 36 : 30;
            case LASER -> this.phase == Phase.PHASE_2 ? 56 : 58;
            case GRAVITY_WELL -> this.phase == Phase.PHASE_2 ? 66 : 64;
            case MIRROR -> 60;
            case BLACK_HOLE -> 160;
            case RAMPAGE -> 96;
            case NOVA -> 110;
            default -> 40;
        };
    }

    private boolean isPlayerClustered(PlayerEntity player, List<? extends PlayerEntity> allPlayers, double range) {
        double rangeSq = range * range;
        for (PlayerEntity other : allPlayers) {
            if (other == player) {
                continue;
            }
            if (player.squaredDistanceTo(other) <= rangeSq) {
                return true;
            }
        }
        return false;
    }

    private void maybeTacticalBlink(ServerWorld world) {
        if (this.tacticalBlinkCooldown > 0) {
            return;
        }
        if (!(this.getTarget() instanceof PlayerEntity target) || !target.isAlive()) {
            return;
        }
        double distSq = target.squaredDistanceTo(this);
        if (this.arenaBound) {
            double arenaDistSq = target.squaredDistanceTo(Vec3d.ofCenter(this.arenaCenter));
            if (distSq < 24.0D * 24.0D && arenaDistSq < 13.5D * 13.5D) {
                return;
            }
        } else if (distSq < 24.0D * 24.0D) {
            return;
        }

        Vec3d toTarget = target.getPos().subtract(this.getPos());
        Vec3d flat = new Vec3d(toTarget.x, 0.0D, toTarget.z);
        Vec3d dir = flat.lengthSquared() < 1.0E-5D
                ? Vec3d.fromPolar(0.0F, this.random.nextFloat() * 360.0F)
                : flat.normalize();
        Vec3d side = new Vec3d(-dir.z, 0.0D, dir.x).multiply(this.random.nextBoolean() ? 3.5D : -3.5D);
        Vec3d blinkPos = this.arenaBound ? clampToArena(target.getPos().add(side), 13.0D) : target.getPos().add(side);
        double y = Math.max(this.arenaCenter.getY() + 2.0D, blinkPos.y);

        world.spawnParticles(ParticleTypes.PORTAL, this.getX(), this.getBodyY(0.4D), this.getZ(), 24, 0.45, 0.35, 0.45, 0.05);
        this.refreshPositionAndAngles(blinkPos.x, y, blinkPos.z, this.random.nextFloat() * 360.0F, 0.0F);
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getBodyY(0.4D), this.getZ(), 30, 0.45, 0.35, 0.45, 0.05);
        this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.6F);
        this.tacticalBlinkCooldown = 80;
        this.attackCooldown = Math.max(4, this.attackCooldown - 10);
    }

    private Vec3d clampToArena(Vec3d pos, double radius) {
        Vec3d center = Vec3d.ofCenter(this.arenaCenter);
        Vec3d offset = pos.subtract(center);
        Vec3d flat = new Vec3d(offset.x, 0.0D, offset.z);
        double len = flat.length();
        if (len <= radius || len < 1.0E-6D) {
            return pos;
        }
        Vec3d limited = flat.normalize().multiply(radius);
        return new Vec3d(center.x + limited.x, pos.y, center.z + limited.z);
    }

    private void tickMirrorPhantoms(ServerWorld world) {
        if (this.waveTick == 1) {
            broadcastAround(world, Text.translatable("boss.nutonmod.singularity.mirror_warning"));
            if (this.getTarget() instanceof PlayerEntity player) {
                Vec3d toTarget = player.getPos().subtract(this.getPos());
                Vec3d flat = new Vec3d(toTarget.x, 0.0D, toTarget.z);
                Vec3d dir = flat.lengthSquared() < 1.0E-6D ? new Vec3d(1, 0, 0) : flat.normalize();
                Vec3d side = new Vec3d(-dir.z, 0.0D, dir.x);
                Vec3d tp = this.getPos().add(side.multiply((this.random.nextBoolean() ? 1 : -1) * 6.0D));
                this.refreshPositionAndAngles(tp.x, this.getY(), tp.z, this.random.nextFloat() * 360.0F, 0.0F);
            }
            world.spawnParticles(ParticleTypes.CLOUD, this.getX(), this.getBodyY(0.45), this.getZ(), 24, 0.6, 0.25, 0.6, 0.04);
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.95F);
        }
        if (this.waveTick != 12) {
            return;
        }

        for (int i = 0; i < 2; i++) {
            double angle = this.random.nextDouble() * Math.PI * 2.0D;
            Vec3d spawnPos = this.getPos().add(Math.cos(angle) * 4.5D, 0.0D, Math.sin(angle) * 4.5D);
            SingularityEntity phantom = ModEntities.SINGULARITY.create(world);
            if (phantom == null) {
                continue;
            }
            phantom.refreshPositionAndAngles(spawnPos.x, this.getY(), spawnPos.z, this.random.nextFloat() * 360.0F, 0.0F);
            phantom.configureAsDecoy(this);
            if (this.getTarget() instanceof PlayerEntity player) {
                phantom.setTarget(player);
            }
            world.spawnEntity(phantom);
            world.spawnParticles(ParticleTypes.LARGE_SMOKE, spawnPos.x, spawnPos.y + 0.8D, spawnPos.z, 16, 0.25, 0.3, 0.25, 0.03);
        }
    }

    private void tickSummonMinions(ServerWorld world) {
        if (this.waveTick == 1) {
            this.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.0F, 0.75F);
            world.spawnParticles(ParticleTypes.PORTAL, this.getX(), this.getBodyY(0.45), this.getZ(), 38, 0.8, 0.4, 0.8, 0.08);
        }
        if (this.waveTick != 20) {
            return;
        }
        int spawnCount = this.phase == Phase.PHASE_1 ? 2 : 3;
        for (int i = 0; i < spawnCount; i++) {
            double angle = (Math.PI * 2.0D / spawnCount) * i + (this.random.nextDouble() - 0.5D) * 0.4D;
            Vec3d spawnPos = this.getPos().add(Math.cos(angle) * 4.0D, 0.2D, Math.sin(angle) * 4.0D);
            HostileEntity minion = this.phase == Phase.PHASE_1 || this.random.nextFloat() < 0.65F
                    ? ModEntities.ENERGY_BEING.create(world)
                    : ModEntities.RIFT_STALKER.create(world);
            if (minion == null) {
                continue;
            }
            minion.refreshPositionAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, this.random.nextFloat() * 360.0F, 0.0F);
            if (this.getTarget() instanceof PlayerEntity player) {
                minion.setTarget(player);
            }
            world.spawnEntity(minion);
            world.spawnParticles(ParticleTypes.END_ROD, spawnPos.x, spawnPos.y + 0.7D, spawnPos.z, 24, 0.35, 0.35, 0.35, 0.03);
        }
        this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.85F);
    }

    private void tickBarrage(ServerWorld world) {
        if (this.waveTick % 10 != 0) {
            return;
        }
        List<PlayerEntity> players = new ArrayList<>(world.getPlayers(player -> player.isAlive() && player.squaredDistanceTo(this) <= 36 * 36));
        if (players.isEmpty()) {
            return;
        }
        players.sort(Comparator
                .comparingDouble((PlayerEntity p) -> p.getHealth() / Math.max(p.getMaxHealth(), 1.0F))
                .thenComparingDouble(p -> -p.squaredDistanceTo(this)));
        int shots = Math.min(3, players.size());
        for (int i = 0; i < shots; i++) {
            PlayerEntity target = players.get(i);
            fireBoltLine(world, target, 8.0F, StatusEffects.SLOWNESS, 40);
        }
        this.playSound(ModSoundEvents.SINGULARITY_BARRAGE, 0.8F, 0.75F);
    }

    private void tickPull(ServerWorld world) {
        for (PlayerEntity player : world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this) <= 22 * 22)) {
            Vec3d toBoss = this.getPos().subtract(player.getPos());
            Vec3d n = toBoss.lengthSquared() < 0.0001D ? Vec3d.ZERO : toBoss.normalize();
            player.setVelocity(player.getVelocity().add(n.multiply(0.12D)));
            player.velocityModified = true;
            if (this.waveTick % 18 == 0) {
                dealBossDamage(player, 2.0F);
            }
            if (this.waveTick % 4 == 0) {
                world.spawnParticles(ParticleTypes.PORTAL, player.getX(), player.getBodyY(0.5), player.getZ(), 7, 0.25, 0.35, 0.25, 0.02);
            }
        }
        if (this.waveTick % 20 == 0) {
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.55F, 0.5F);
        }
    }

    private void tickRing(ServerWorld world) {
        double radius = 2.0D + this.waveTick * 0.58D;
        for (int i = 0; i < 44; i++) {
            double a = (Math.PI * 2.0D) * (i / 44.0D);
            double x = this.getX() + Math.cos(a) * radius;
            double z = this.getZ() + Math.sin(a) * radius;
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, x, this.getBodyY(0.35), z, 1, 0.03, 0.03, 0.03, 0.0);
        }
        for (PlayerEntity player : world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this) <= 28 * 28)) {
            if (this.ringHit.contains(player.getUuid())) {
                continue;
            }
            double dist = Math.sqrt(player.squaredDistanceTo(this));
            if (Math.abs(dist - radius) <= 1.1D) {
                this.ringHit.add(player.getUuid());
                dealBossDamage(player, 6.0F);
                Vec3d out = player.getPos().subtract(this.getPos()).normalize();
                player.takeKnockback(1.2F, -out.x, -out.z);
            }
        }
    }

    private void tickLaser(ServerWorld world) {
        PlayerEntity player = this.getTarget() instanceof PlayerEntity p ? p : null;
        if (player == null || !player.isAlive()) {
            return;
        }
        if ((!this.canSee(player) || player.squaredDistanceTo(this) > 34 * 34) && this.waveTick % 8 == 0) {
            PlayerEntity alternative = chooseBestTarget(world);
            if (alternative != null) {
                this.setTarget(alternative);
                player = alternative;
            }
        }
        if (this.waveTick % 2 == 0) {
            drawLine(world, this.getPos().add(0.0D, this.getHeight() * 0.68D, 0.0D), player.getPos().add(0.0D, 1.0D, 0.0D), ParticleTypes.DRAGON_BREATH);
        }
        if (this.waveTick > 16 && this.waveTick % 10 == 0 && this.canSee(player)) {
            dealBossDamage(player, 10.0F);
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 60, 0, true, true, true));
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getBodyY(0.5), player.getZ(), 18, 0.3, 0.4, 0.3, 0.04);
        }
    }

    private void tickGravityWellCast(ServerWorld world) {
        if (this.waveTick != 1) {
            return;
        }
        List<PlayerEntity> players = new ArrayList<>(world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this) <= 34 * 34));
        players.sort(Comparator.comparingDouble((PlayerEntity p) -> p.squaredDistanceTo(this)).reversed());
        int placeCount = Math.min(3, players.size());
        for (int i = 0; i < placeCount; i++) {
            PlayerEntity target = players.get(i);
            Vec3d predicted = target.getPos().add(target.getVelocity().multiply(10.0D));
            Vec3d clamped = clampToArena(predicted, 13.5D).add((this.random.nextDouble() - 0.5D) * 0.6D, 0.05D, (this.random.nextDouble() - 0.5D) * 0.6D);
            this.activeWells.add(new GravityWell(clamped, 100));
        }
        if (placeCount > 0) {
            this.playSound(ModSoundEvents.SINGULARITY_GRAVITY_WELL, 1.0F, 0.7F);
        }
    }

    private void tickBlackHole(ServerWorld world) {
        if (this.blackHoleCenter == null) {
            this.blackHoleCenter = Vec3d.ofCenter(this.arenaCenter).add(0.0D, 0.2D, 0.0D);
            broadcastAround(world, Text.translatable("boss.nutonmod.singularity.black_hole_warning"));
            this.playSound(ModSoundEvents.SINGULARITY_BLACK_HOLE, 1.2F, 0.55F);
        }
        if (this.waveTick <= 60) {
            if (this.waveTick % 4 == 0) {
                world.spawnParticles(ParticleTypes.REVERSE_PORTAL, this.blackHoleCenter.x, this.blackHoleCenter.y, this.blackHoleCenter.z, 20, 1.5, 0.3, 1.5, 0.02);
            }
            if (this.waveTick % 8 == 0) {
                spawnGroundRing(world, this.blackHoleCenter, 3.0D + this.waveTick * 0.055D, 56, ParticleTypes.ELECTRIC_SPARK);
            }
            return;
        }

        if (this.waveTick % 2 == 0) {
            world.spawnParticles(ParticleTypes.PORTAL, this.blackHoleCenter.x, this.blackHoleCenter.y, this.blackHoleCenter.z, 16, 0.9, 0.2, 0.9, 0.01);
            world.spawnParticles(ParticleTypes.REVERSE_PORTAL, this.blackHoleCenter.x, this.blackHoleCenter.y, this.blackHoleCenter.z, 10, 0.5, 0.2, 0.5, 0.01);
            if (this.waveTick % 6 == 0) {
                spawnGroundRing(world, this.blackHoleCenter, 5.8D, 68, ParticleTypes.DRAGON_BREATH);
            }
        }
        for (PlayerEntity player : world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this.blackHoleCenter) <= 28 * 28)) {
            Vec3d pull = this.blackHoleCenter.subtract(player.getPos());
            Vec3d direction = pull.lengthSquared() < 0.0001D ? Vec3d.ZERO : pull.normalize();
            player.setVelocity(player.getVelocity().add(direction.multiply(0.15D)));
            player.velocityModified = true;
            if (this.waveTick % 20 == 0) {
                dealBossDamage(player, scaledPhase3Damage(world, 5.0F));
            }
        }
    }

    private void tickAnnihilationNova(ServerWorld world) {
        if (this.waveTick == 1) {
            prepareNovaShields(world);
            broadcastAround(world, Text.translatable("boss.nutonmod.singularity.nova_warning"));
        }
        if (this.waveTick % 5 == 0) {
            world.spawnParticles(ParticleTypes.FLASH, this.getX(), this.getBodyY(0.45), this.getZ(), 2, 0.1, 0.1, 0.1, 0.0);
            for (Vec3d shield : this.novaShieldAnchors) {
                world.spawnParticles(ParticleTypes.END_ROD, shield.x, shield.y + 0.7D, shield.z, 12, 0.35, 0.35, 0.35, 0.02);
                world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, shield.x, shield.y + 0.3D, shield.z, 10, 0.45, 0.25, 0.45, 0.02);
                drawLine(world, this.getPos().add(0.0D, this.getHeight() * 0.64D, 0.0D), shield.add(0.0D, 0.8D, 0.0D), ParticleTypes.END_ROD);
            }
        }
        if (this.stateTicks == 1) {
            detonateNova(world);
            this.novaShieldAnchors.clear();
            clearShieldClouds(world);
        }
    }

    private void tickRampage(ServerWorld world) {
        if (this.waveTick == 1) {
            broadcastAround(world, Text.translatable("boss.nutonmod.singularity.rampage_warning"));
            this.nextRampageTick = 12;
            this.rampageChargesDone = 0;
        }

        if (this.rampageTicksLeft > 0) {
            this.setVelocity(this.rampageDirection.multiply(1.25D).add(0.0D, 0.04D, 0.0D));
            this.velocityModified = true;

            if (this.waveTick % 2 == 0) {
                world.spawnParticles(ParticleTypes.FLAME, this.getX(), this.getBodyY(0.35), this.getZ(), 8, 0.3, 0.2, 0.3, 0.01);
                world.spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getBodyY(0.35), this.getZ(), 5, 0.2, 0.2, 0.2, 0.01);
                world.spawnParticles(ParticleTypes.REVERSE_PORTAL, this.getX(), this.getBodyY(0.35), this.getZ(), 4, 0.2, 0.12, 0.2, 0.01);
            }
            BlockPos firePos = this.getBlockPos();
            if (world.getBlockState(firePos).isAir() && world.getBlockState(firePos.down()).isOpaqueFullCube(world, firePos.down())) {
                world.setBlockState(firePos, net.minecraft.block.Blocks.FIRE.getDefaultState(), 3);
            }
            for (PlayerEntity player : world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this) <= 2.2D * 2.2D)) {
                if (this.rampageHit.contains(player.getUuid())) {
                    continue;
                }
                this.rampageHit.add(player.getUuid());
                dealBossDamage(player, scaledPhase3Damage(world, 10.0F));
                player.setOnFireFor(2);
            }

            this.rampageTicksLeft--;
            if (this.rampageTicksLeft <= 0) {
                this.rampageHit.clear();
            }
            return;
        }

        if (this.rampageChargesDone >= 3 || this.waveTick < this.nextRampageTick) {
            return;
        }

        LivingEntity chargeTarget = this.getTarget();
        if (chargeTarget == null || !chargeTarget.isAlive()) {
            chargeTarget = world.getClosestPlayer(this, 36.0D);
        }
        Vec3d dir;
        if (chargeTarget != null) {
            Vec3d predicted = chargeTarget.getPos().add(chargeTarget.getVelocity().multiply(6.0D));
            Vec3d delta = predicted.subtract(this.getPos());
            dir = new Vec3d(delta.x, 0.0D, delta.z);
        } else {
            double angle = this.random.nextDouble() * Math.PI * 2.0D;
            dir = new Vec3d(Math.cos(angle), 0.0D, Math.sin(angle));
        }
        if (dir.lengthSquared() < 1.0E-5D) {
            dir = new Vec3d(1.0D, 0.0D, 0.0D);
        }
        this.rampageDirection = dir.normalize();
        this.rampageTicksLeft = 8;
        this.rampageChargesDone++;
        this.nextRampageTick = this.waveTick + 22;
        spawnGroundRing(world, this.getPos(), 2.6D, 36, ParticleTypes.FLAME);
        this.playSound(ModSoundEvents.SINGULARITY_RAMPAGE, 1.1F, 0.9F + this.random.nextFloat() * 0.15F);
    }

    private void prepareNovaShields(ServerWorld world) {
        clearShieldClouds(world);
        this.novaShieldAnchors.clear();
        double radius = 9.0D;
        for (int i = 0; i < 3; i++) {
            double angle = (Math.PI * 2.0D / 3.0D) * i + (this.random.nextDouble() * 0.5D);
            double x = this.arenaCenter.getX() + 0.5D + Math.cos(angle) * radius;
            double z = this.arenaCenter.getZ() + 0.5D + Math.sin(angle) * radius;
            Vec3d anchor = new Vec3d(x, this.arenaCenter.getY() + 1.0D, z);
            this.novaShieldAnchors.add(anchor);

            AreaEffectCloudEntity cloud = EntityType.AREA_EFFECT_CLOUD.create(world);
            if (cloud == null) {
                continue;
            }
            cloud.setPosition(anchor.x, anchor.y, anchor.z);
            cloud.setRadius(1.8F);
            cloud.setDuration(20 * 6);
            cloud.setWaitTime(0);
            cloud.setParticleType(ParticleTypes.END_ROD);
            world.spawnEntity(cloud);
            this.novaShieldCloudIds.add(cloud.getUuid());
        }
    }

    private void clearShieldClouds(ServerWorld world) {
        if (this.novaShieldCloudIds.isEmpty()) {
            return;
        }
        for (UUID id : this.novaShieldCloudIds) {
            var entity = world.getEntity(id);
            if (entity != null) {
                entity.discard();
            }
        }
        this.novaShieldCloudIds.clear();
    }

    private void detonateNova(ServerWorld world) {
        Set<UUID> protectedPlayers = new HashSet<>();
        Set<UUID> alreadyTaken = new HashSet<>();

        for (Vec3d shield : this.novaShieldAnchors) {
            PlayerEntity best = null;
            double bestDistance = Double.MAX_VALUE;
            for (PlayerEntity player : world.getPlayers(p -> p.isAlive() && !alreadyTaken.contains(p.getUuid()))) {
                double distSq = player.squaredDistanceTo(shield);
                if (distSq > 2.8D * 2.8D || distSq >= bestDistance) {
                    continue;
                }
                best = player;
                bestDistance = distSq;
            }
            if (best != null) {
                alreadyTaken.add(best.getUuid());
                protectedPlayers.add(best.getUuid());
            }
        }

        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getBodyY(0.35), this.getZ(), 8, 0.4, 0.3, 0.4, 0.03);
        world.spawnParticles(ParticleTypes.FLASH, this.getX(), this.getBodyY(0.35), this.getZ(), 2, 0.1, 0.1, 0.1, 0.0);
        spawnGroundRing(world, this.getPos(), 4.0D, 54, ParticleTypes.FLASH);
        spawnGroundRing(world, this.getPos(), 8.0D, 72, ParticleTypes.ELECTRIC_SPARK);

        for (PlayerEntity player : world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this) <= 40 * 40)) {
            if (protectedPlayers.contains(player.getUuid())) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 50, 4, true, true, true));
                world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getBodyY(0.5), player.getZ(), 8, 0.2, 0.2, 0.2, 0.02);
            } else {
                dealBossDamage(player, scaledPhase3Damage(world, 20.0F));
            }
        }
        this.playSound(ModSoundEvents.SINGULARITY_NOVA, 1.2F, 0.75F);
    }

    private void tickPersistentHazards(ServerWorld world) {
        if (this.activeWells.isEmpty()) {
            return;
        }
        for (int i = this.activeWells.size() - 1; i >= 0; i--) {
            GravityWell well = this.activeWells.get(i);
            well.ticksLeft--;
            well.age++;
            if (well.ticksLeft <= 0) {
                this.activeWells.remove(i);
                continue;
            }

            if (well.age % 4 == 0) {
                world.spawnParticles(ParticleTypes.REVERSE_PORTAL, well.center.x, well.center.y + 0.1D, well.center.z, 10, 0.55, 0.05, 0.55, 0.01);
                if (well.age % 8 == 0) {
                    spawnGroundRing(world, well.center, 1.1D, 26, ParticleTypes.PORTAL);
                }
            }
            for (PlayerEntity player : world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(well.center) <= 2.2D * 2.2D)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 30, 1, true, true, true));
                if (well.age % 20 == 0) {
                    dealBossDamage(player, 4.0F);
                }
            }
        }
    }

    private void fireBoltLine(ServerWorld world, PlayerEntity target, float damage,
                              net.minecraft.registry.entry.RegistryEntry<net.minecraft.entity.effect.StatusEffect> effect,
                              int duration) {
        Vec3d from = this.getPos().add(0.0D, this.getHeight() * 0.68D, 0.0D);
        Vec3d lead = target.getPos().add(target.getVelocity().multiply(6.0D));
        if (lead.squaredDistanceTo(this.getPos()) > 42.0D * 42.0D) {
            lead = target.getPos();
        }
        Vec3d to = lead.add(0.0D, target.getHeight() * 0.55D, 0.0D);
        drawLine(world, from, to, ParticleTypes.END_ROD);
        if (this.canSee(target)) {
            dealBossDamage(target, damage);
            target.addStatusEffect(new StatusEffectInstance(effect, duration, 0, true, true, true));
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getBodyY(0.5), target.getZ(), 12, 0.25, 0.25, 0.25, 0.03);
        }
    }

    private void dealBossDamage(PlayerEntity player, float amount) {
        this.participatingPlayers.add(player.getUuid());
        if (player.damage(this.getDamageSources().mobAttack(this), amount)) {
            this.damagedPlayers.add(player.getUuid());
        }
    }

    private void drawLine(ServerWorld world, Vec3d from, Vec3d to, net.minecraft.particle.ParticleEffect particle) {
        Vec3d d = to.subtract(from);
        int n = Math.max(10, (int) (d.length() * 4.0D));
        for (int i = 0; i <= n; i++) {
            Vec3d p = from.add(d.multiply(i / (double) n));
            world.spawnParticles(particle, p.x, p.y, p.z, 1, 0.02, 0.02, 0.02, 0.0D);
        }
    }

    private void clientParticles() {
        if (this.age % 2 != 0) {
            return;
        }
        double px = this.getX() + (this.random.nextDouble() - 0.5D) * this.getWidth() * 1.45D;
        double py = this.getBodyY(this.random.nextDouble());
        double pz = this.getZ() + (this.random.nextDouble() - 0.5D) * this.getWidth() * 1.45D;
        switch (this.phase) {
            case PHASE_1 -> this.getWorld().addParticle(ParticleTypes.END_ROD, px, py, pz, 0.0D, 0.02D, 0.0D);
            case PHASE_2 -> {
                this.getWorld().addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 0.0D, 0.01D, 0.0D);
                if (this.random.nextFloat() < 0.45F) {
                    this.getWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, px, py - 0.1D, pz, 0.0D, 0.005D, 0.0D);
                }
            }
            case PHASE_3 -> {
                this.getWorld().addParticle(ParticleTypes.FLAME, px, py, pz, 0.0D, 0.01D, 0.0D);
                if (this.random.nextFloat() < 0.45F) {
                    this.getWorld().addParticle(ParticleTypes.REVERSE_PORTAL, px, py, pz, 0.0D, 0.01D, 0.0D);
                }
            }
        }
        if (this.attackMode == AttackMode.BLACK_HOLE || this.attackMode == AttackMode.NOVA) {
            this.getWorld().addParticle(ParticleTypes.FLASH, this.getX(), this.getBodyY(0.45D), this.getZ(), 0.0D, 0.0D, 0.0D);
        } else if (this.attackMode == AttackMode.RAMPAGE) {
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX(), this.getBodyY(0.2D), this.getZ(), 0.0D, 0.02D, 0.0D);
        }
    }

    private int getNearbyPlayerCount(double range) {
        if (!(this.getWorld() instanceof ServerWorld world)) {
            return 1;
        }
        return Math.max(1, world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this) <= range * range).size());
    }

    private float scaledPhase3Damage(ServerWorld world, float baseDamage) {
        if (this.phase != Phase.PHASE_3) {
            return baseDamage;
        }
        int players = Math.max(1, world.getPlayers(p -> p.isAlive() && p.squaredDistanceTo(this) <= 52 * 52).size());
        float multiplier = players <= 1 ? 1.0F : (players == 2 ? 0.92F : players == 3 ? 0.84F : 0.76F);
        return Math.max(2.0F, baseDamage * multiplier);
    }

    private static boolean isMajorPhase3Attack(AttackMode mode) {
        return mode == AttackMode.NOVA || mode == AttackMode.BLACK_HOLE || mode == AttackMode.RAMPAGE;
    }

    private void spawnGroundRing(ServerWorld world, Vec3d center, double radius, int points, net.minecraft.particle.ParticleEffect particle) {
        double y = center.y + 0.08D;
        for (int i = 0; i < points; i++) {
            double a = (Math.PI * 2.0D) * (i / (double) points);
            double x = center.x + Math.cos(a) * radius;
            double z = center.z + Math.sin(a) * radius;
            world.spawnParticles(particle, x, y, z, 1, 0.02D, 0.01D, 0.02D, 0.0D);
        }
    }

    private void broadcastAround(ServerWorld world, Text msg) {
        for (PlayerEntity player : world.getPlayers(p -> p.squaredDistanceTo(this) <= 64 * 64)) {
            player.sendMessage(msg, true);
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean isDisallowedInPeaceful() {
        return false;
    }

    @Override
    protected void pushAway(net.minecraft.entity.Entity entity) {
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        this.playSound(ModSoundEvents.SINGULARITY_DEATH, 1.25F, 0.9F);
        this.bossBar.clearPlayers();
        if (this.decoy) {
            return;
        }
        rewardBossAdvancements(serverWorld, source);
        clearShieldClouds(serverWorld);
        SingularityArenaSystem.markBossDefeated(this.getUuid());
    }

    private void rewardBossAdvancements(ServerWorld world, DamageSource source) {
        if (!(source.getAttacker() instanceof ServerPlayerEntity killer)) {
            return;
        }
        this.participatingPlayers.add(killer.getUuid());
        incrementKillCount(killer);
        if (!this.damagedPlayers.contains(killer.getUuid())) {
            grantAdvancement(killer, "energy_realm/no_damage");
        }
    }

    private void incrementKillCount(ServerPlayerEntity killer) {
        Scoreboard scoreboard = killer.getScoreboard();
        ScoreboardObjective objective = scoreboard.getNullableObjective("nutonmod_singularity_kills");
        if (objective == null) {
            objective = scoreboard.addObjective(
                    "nutonmod_singularity_kills",
                    ScoreboardCriterion.DUMMY,
                    Text.literal("Singularity Kills"),
                    ScoreboardCriterion.RenderType.INTEGER,
                    true,
                    null
            );
        }
        ScoreAccess score = scoreboard.getOrCreateScore(killer, objective);
        int kills = score.getScore() + 1;
        score.setScore(kills);
        if (kills >= 5) {
            grantAdvancement(killer, "energy_realm/singularity_master");
        }
    }

    private void grantAdvancement(ServerPlayerEntity player, String path) {
        Identifier id = Identifier.of("nutonmod", path);
        AdvancementEntry advancement = player.getServer().getAdvancementLoader().get(id);
        if (advancement == null) {
            return;
        }
        var progress = player.getAdvancementTracker().getProgress(advancement);
        for (String criterion : progress.getUnobtainedCriteria()) {
            player.getAdvancementTracker().grantCriterion(advancement, criterion);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean damaged = super.damage(source, amount);
        if (!damaged || !(this.getWorld() instanceof ServerWorld world)) {
            return damaged;
        }
        if (this.decoy) {
            world.spawnParticles(ParticleTypes.CLOUD, this.getX(), this.getBodyY(0.45), this.getZ(), 12, 0.35, 0.25, 0.35, 0.03);
            world.spawnParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getBodyY(0.45), this.getZ(), 8, 0.25, 0.2, 0.25, 0.02);
        } else {
            world.spawnParticles(ParticleTypes.END_ROD, this.getX(), this.getBodyY(0.45), this.getZ(), 16, 0.28, 0.28, 0.28, 0.03);
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getBodyY(0.45), this.getZ(), 12, 0.22, 0.22, 0.22, 0.02);
        }
        return true;
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.participatingPlayers.add(player.getUuid());
        if (!this.decoy) {
            this.bossBar.addPlayer(player);
        }
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 2, this::animationPredicate));
    }

    private PlayState animationPredicate(AnimationState<SingularityEntity> state) {
        if (this.attackMode == AttackMode.IDLE) {
            switch (this.phase) {
                case PHASE_1 -> state.setAnimation(IDLE_PHASE_1_ANIMATION);
                case PHASE_2 -> state.setAnimation(IDLE_PHASE_2_ANIMATION);
                case PHASE_3 -> state.setAnimation(IDLE_PHASE_3_ANIMATION);
            }
        } else {
            state.setAnimation(CAST_ANIMATION);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animatableInstanceCache;
    }

    public Phase getPhase() {
        return this.phase;
    }

    public AttackMode getAttackMode() {
        return this.attackMode;
    }

    public int debugGetCooldown() {
        return this.attackCooldown;
    }

    public int debugGetStateTicks() {
        return this.stateTicks;
    }

    public int debugGetWaveTick() {
        return this.waveTick;
    }

    public LivingEntity getCurrentTarget() {
        return this.getTarget();
    }

    public boolean isSkillBusy() {
        return this.attackMode != AttackMode.IDLE;
    }

    public void debugSetCooldown(int ticks) {
        this.attackCooldown = Math.max(0, ticks);
    }

    public void debugStopSkill() {
        this.attackMode = AttackMode.IDLE;
        this.stateTicks = 0;
        this.waveTick = 0;
        this.ringHit.clear();
        this.rampageHit.clear();
        this.blackHoleCenter = null;
        this.rampageTicksLeft = 0;
        this.rampageChargesDone = 0;
        this.nextRampageTick = 0;
        this.novaShieldAnchors.clear();
        this.lastAttack = AttackMode.IDLE;
        this.sameAttackChain = 0;
        if (this.getWorld() instanceof ServerWorld world) {
            clearShieldClouds(world);
        }
    }

    public void debugForcePhase(Phase phase) {
        float max = Math.max(this.getMaxHealth(), 1.0F);
        switch (phase) {
            case PHASE_1 -> this.setHealth(max * 0.95F);
            case PHASE_2 -> this.setHealth(max * 0.55F);
            case PHASE_3 -> this.setHealth(max * 0.25F);
        }
    }

    public boolean debugStartSkill(AttackMode mode) {
        if (mode == null || mode == AttackMode.IDLE || this.isSkillBusy()) {
            return false;
        }
        this.attackMode = mode;
        this.stateTicks = attackDuration(mode);
        this.waveTick = 0;
        this.attackCooldown = Math.max(this.attackCooldown, 14);
        this.ringHit.clear();
        this.rampageHit.clear();
        return true;
    }

    public enum Phase {
        PHASE_1,
        PHASE_2,
        PHASE_3
    }

    public enum AttackMode {
        IDLE,
        BARRAGE,
        PULL,
        RING,
        SUMMON,
        LASER,
        GRAVITY_WELL,
        MIRROR,
        BLACK_HOLE,
        RAMPAGE,
        NOVA
    }

    private static final class GravityWell {
        private final Vec3d center;
        private int ticksLeft;
        private int age;

        private GravityWell(Vec3d center, int ticksLeft) {
            this.center = center;
            this.ticksLeft = ticksLeft;
            this.age = 0;
        }
    }

    private static final class SingularityCombatGoal extends Goal {
        private final SingularityEntity boss;

        private SingularityCombatGoal(SingularityEntity boss) {
            this.boss = boss;
        }

        @Override
        public boolean canStart() {
            return true;
        }

        @Override
        public boolean shouldContinue() {
            return true;
        }

        @Override
        public void tick() {
            if (!(this.boss.getWorld() instanceof ServerWorld serverWorld)) {
                return;
            }
            this.boss.runAttackState(serverWorld);
        }
    }
}
