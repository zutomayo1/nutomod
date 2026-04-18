package com.nutonmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class StabilizerBeaconScreen extends HandledScreen<StabilizerBeaconScreenHandler> {
    public StabilizerBeaconScreen(StabilizerBeaconScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.backgroundWidth = 176;
        this.backgroundHeight = 130;
        this.titleX = 10;
        this.titleY = 8;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.fill(x, y, x + backgroundWidth, y + backgroundHeight, 0xFF111827);
        context.fill(x + 2, y + 2, x + backgroundWidth - 2, y + backgroundHeight - 2, 0xFF1B2636);
        context.drawBorder(x + 6, y + 20, 164, 100, 0xFF6AA8FF);
        context.drawText(this.textRenderer, Text.translatable("gui.nutonmod.stabilizer_beacon.status." + handler.getStatus()), x + 10, y + 30, 0xD5E6FF, false);
        context.drawText(this.textRenderer, Text.translatable("gui.nutonmod.stabilizer_beacon.remaining", handler.getRemainingSeconds()), x + 10, y + 48, 0xBCE9D8, false);
        context.drawText(this.textRenderer, Text.translatable("gui.nutonmod.stabilizer_beacon.radius", handler.getProtectionRadius()), x + 10, y + 64, 0xB4C2D7, false);
        context.drawText(this.textRenderer, Text.translatable("gui.nutonmod.stabilizer_beacon.storm", handler.getStormMultiplier()), x + 10, y + 80, 0xFFD59A, false);
        context.drawText(this.textRenderer, Text.translatable("gui.nutonmod.stabilizer_beacon.hint." + handler.getActivationHint()), x + 10, y + 100, 0x9FB5D1, false);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY + 15, 0xFFFFFF, false);
    }
}
