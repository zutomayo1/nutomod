package com.nutonmod.client;

import com.nutonmod.world.dimension.ModDimensions;
import com.nutonmod.world.system.EnergyRealmStormSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;

public final class StormWarningClientSystem {
    private static final long WARNING_WINDOW_TICKS = 20L * 20L;
    private static final long WARNING_SOUND_FIRST = 20L * 20L;
    private static final long WARNING_SOUND_SECOND = 20L * 10L;
    private static final long WARNING_SOUND_THIRD = 20L * 5L;

    private static long lastWorldTime = Long.MIN_VALUE;

    private StormWarningClientSystem() {
    }

    public static void tick(MinecraftClient client) {
        ClientPlayerEntity player = client.player;
        if (player == null || player.getWorld() == null) {
            lastWorldTime = Long.MIN_VALUE;
            return;
        }
        if (!player.getWorld().getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            lastWorldTime = Long.MIN_VALUE;
            return;
        }

        long worldTime = player.getWorld().getTimeOfDay();
        long until = EnergyRealmStormSystem.getTicksUntilNextStorm(worldTime);
        if (until <= 0L || until > WARNING_WINDOW_TICKS) {
            lastWorldTime = worldTime;
            return;
        }

        long previousUntil = lastWorldTime == Long.MIN_VALUE
                ? Long.MAX_VALUE
                : EnergyRealmStormSystem.getTicksUntilNextStorm(lastWorldTime);

        if (crossed(previousUntil, until, WARNING_SOUND_FIRST)
                || crossed(previousUntil, until, WARNING_SOUND_SECOND)
                || crossed(previousUntil, until, WARNING_SOUND_THIRD)) {
            player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 0.7F, 0.85F);
        }

        lastWorldTime = worldTime;
    }

    public static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || player.getWorld() == null) {
            return;
        }
        if (!player.getWorld().getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return;
        }

        long until = EnergyRealmStormSystem.getTicksUntilNextStorm(player.getWorld().getTimeOfDay());
        if (until <= 0L || until > WARNING_WINDOW_TICKS) {
            return;
        }

        float intensity = 1.0F - ((float) until / (float) WARNING_WINDOW_TICKS);
        intensity = MathHelper.clamp(intensity, 0.0F, 1.0F);

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        int edge = 8 + (int) (26.0F * intensity);
        int alpha = 20 + (int) (120.0F * intensity);
        int color = (alpha << 24) | 0xE6B24F;

        context.fill(0, 0, width, edge, color);
        context.fill(0, height - edge, width, height, color);
        context.fill(0, 0, edge, height, color);
        context.fill(width - edge, 0, width, height, color);
    }

    private static boolean crossed(long previous, long now, long threshold) {
        return previous > threshold && now <= threshold;
    }
}
