package com.nutonmod.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AnnihilationBladeItem extends SwordItem {
    private static final int SKILL_COOLDOWN_TICKS = 80;
    private static final double SKILL_RANGE = 12.0;
    private static final float SKILL_DAMAGE = 20.0f;
    private static final float SKILL_SPLASH_DAMAGE = 8.0f;
    private static final double SKILL_SPLASH_RADIUS = 2.75;

    private static final int MAX_MARK_STACK = 5;
    private static final int MARK_TIMEOUT_TICKS = 120;
    private static final float EXECUTE_WAVE_DAMAGE = 16.0f;
    private static final float EXECUTE_THRESHOLD_RATIO = 0.28f;

    private static final float KILL_HEAL = 2.0f;
    private static final Map<UUID, Integer> MARK_STACKS = new HashMap<>();
    private static final Map<UUID, Long> LAST_MARK_TICK = new HashMap<>();

    public AnnihilationBladeItem(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.pass(stack);
        }

        user.getItemCooldownManager().set(this, SKILL_COOLDOWN_TICKS);
        user.swingHand(hand, true);

        Vec3d look = user.getRotationVec(1.0f).normalize();
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 0.95f, 1.15f);
        world.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.PLAYERS, 0.8f, 1.55f);

        if (!world.isClient) {
            applyConeSlash(world, user, look);
            spawnSlashParticles((ServerWorld) world, user, look);
        }

        return TypedActionResult.success(stack, world.isClient);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.postHit(stack, target, attacker);
        if (!(attacker instanceof PlayerEntity player)) {
            return result;
        }

        if (!target.isAlive()) {
            player.heal(KILL_HEAL);
            return result;
        }

        UUID id = player.getUuid();
        long now = player.getWorld().getTime();
        int mark = MARK_STACKS.getOrDefault(id, 0);
        long last = LAST_MARK_TICK.getOrDefault(id, -10000L);
        if (now - last > MARK_TIMEOUT_TICKS) {
            mark = 0;
        }

        mark++;
        MARK_STACKS.put(id, mark);
        LAST_MARK_TICK.put(id, now);

        if (mark >= MAX_MARK_STACK) {
            triggerExecuteWave(player, target);
            MARK_STACKS.put(id, 0);
            player.sendMessage(Text.literal("湮灭印记已满：处决波爆发"), true);
        } else {
            player.sendMessage(Text.literal("湮灭印记 " + mark + "/" + MAX_MARK_STACK), true);
        }

        return result;
    }

    private static void applyConeSlash(World world, PlayerEntity user, Vec3d look) {
        Box rangeBox = user.getBoundingBox().stretch(look.multiply(SKILL_RANGE)).expand(2.0);
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, rangeBox,
                entity -> entity.isAlive() && entity != user && !entity.isTeammate(user));

        for (LivingEntity target : targets) {
            Vec3d toTarget = target.getPos().subtract(user.getPos());
            double distance = toTarget.length();
            if (distance > SKILL_RANGE + 0.5) {
                continue;
            }

            Vec3d dir = toTarget.normalize();
            double dot = look.dotProduct(dir);
            if (dot < 0.55) {
                continue;
            }

            target.damage(world.getDamageSources().playerAttack(user), SKILL_DAMAGE);
            target.addVelocity(look.x * 1.0, 0.25, look.z * 1.0);
            applySplashExplosion(world, user, target);
        }
    }

    private static void spawnSlashParticles(ServerWorld world, PlayerEntity user, Vec3d look) {
        Vec3d start = user.getEyePos();
        for (int i = 1; i <= 30; i++) {
            double t = i / 30.0;
            Vec3d point = start.add(look.multiply(SKILL_RANGE * t));
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    point.x, point.y, point.z, 4, 0.08, 0.08, 0.08, 0.02);
            world.spawnParticles(ParticleTypes.END_ROD,
                    point.x, point.y, point.z, 2, 0.05, 0.05, 0.05, 0.01);
            world.spawnParticles(ParticleTypes.SONIC_BOOM,
                    point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    private static void applySplashExplosion(World world, PlayerEntity user, LivingEntity center) {
        Box splashBox = center.getBoundingBox().expand(SKILL_SPLASH_RADIUS);
        List<LivingEntity> splashTargets = world.getEntitiesByClass(LivingEntity.class, splashBox,
                entity -> entity.isAlive() && entity != user && !entity.isTeammate(user));

        Vec3d origin = center.getPos();
        for (LivingEntity splash : splashTargets) {
            splash.damage(world.getDamageSources().playerAttack(user), SKILL_SPLASH_DAMAGE);
            Vec3d push = splash.getPos().subtract(origin).normalize().multiply(0.8);
            splash.addVelocity(push.x, 0.2, push.z);
        }

        if (world instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                    origin.x, origin.y + 0.6, origin.z, 1, 0, 0, 0, 0);
            sw.spawnParticles(ParticleTypes.FLASH,
                    origin.x, origin.y + 0.6, origin.z, 2, 0.2, 0.2, 0.2, 0.0);
        }
    }

    private static void triggerExecuteWave(PlayerEntity player, LivingEntity primaryTarget) {
        World world = player.getWorld();
        Vec3d center = primaryTarget.getPos();
        Box waveBox = new Box(center, center).expand(4.5);
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, waveBox,
                entity -> entity.isAlive() && entity != player && !entity.isTeammate(player));

        for (LivingEntity target : targets) {
            float hp = target.getHealth();
            float maxHp = target.getMaxHealth();
            if (hp <= maxHp * EXECUTE_THRESHOLD_RATIO) {
                target.damage(world.getDamageSources().playerAttack(player), 9999.0f);
            } else {
                target.damage(world.getDamageSources().playerAttack(player), EXECUTE_WAVE_DAMAGE);
            }
            Vec3d knock = target.getPos().subtract(center).normalize().multiply(1.2);
            target.addVelocity(knock.x, 0.35, knock.z);
        }

        world.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0f, 0.8f);
        world.playSound(null, center.x, center.y, center.z,
                SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.PLAYERS, 1.0f, 0.9f);

        if (world instanceof ServerWorld sw) {
            sw.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                    center.x, center.y + 0.8, center.z, 2, 0.0, 0.0, 0.0, 0.0);
            sw.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    center.x, center.y + 0.8, center.z, 60, 1.4, 0.7, 1.4, 0.08);
            sw.spawnParticles(ParticleTypes.SONIC_BOOM,
                    center.x, center.y + 0.8, center.z, 4, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.nutonmod.annihilation_blade.line1").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("tooltip.nutonmod.annihilation_blade.line2").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.nutonmod.annihilation_blade.line3").formatted(Formatting.GRAY));
    }
}
