package com.nutonmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nutonmod.block.entity.VoidResonanceBoxBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class VoidResonanceBoxScreen extends HandledScreen<VoidResonanceBoxScreenHandler> {
    public VoidResonanceBoxScreen(VoidResonanceBoxScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.backgroundWidth = 196;
        this.backgroundHeight = 186;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.fill(x, y, x + backgroundWidth, y + backgroundHeight, 0xFF0E1521);
        context.fill(x + 2, y + 2, x + backgroundWidth - 2, y + backgroundHeight - 2, 0xFF172235);

        context.fill(x + 6, y + 18, x + 120, y + 90, 0xFF101827);
        context.fill(x + 124, y + 18, x + 190, y + 112, 0xFF101827);
        context.fill(x + 6, y + 94, x + 190, y + 112, 0xFF101827);
        context.fill(x + 6, y + 114, x + 190, y + 180, 0xFF141E2D);

        drawSlotFrame(context, x + 28, y + 32);
        drawSlotFrame(context, x + 28, y + 54);
        drawSlotFrame(context, x + 82, y + 44);

        int invX = x + 8;
        int invY = y + 102;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                drawSlotFrame(context, invX + col * 18 + 5, invY + row * 18);
            }
        }
        int hotbarY = y + 160;
        for (int col = 0; col < 9; col++) {
            drawSlotFrame(context, invX + col * 18 + 5, hotbarY);
        }

        int progress = handler.getScaledProgress();
        int ax = x + 48;
        int ay = y + 45;
        context.fill(ax, ay + 3, ax + 24, ay + 9, 0xFF2A3448);
        if (progress > 0) {
            context.fill(ax, ay + 3, ax + progress, ay + 9, 0xFF6AA8FF);
        }
        context.fill(ax + 20, ay + 1, ax + 24, ay + 11, 0xFF2A3448);
        if (progress >= 20) {
            context.fill(ax + 20, ay + 1, ax + 24, ay + 11, 0xFF6AA8FF);
        }
    }

    private void drawSlotFrame(DrawContext context, int x, int y) {
        context.fill(x, y, x + 16, y + 16, 0xFF0C121B);
        context.drawBorder(x - 1, y - 1, 18, 18, 0xFF6E88BB);
        context.drawBorder(x, y, 16, 16, 0xFF2A3448);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        drawTrimmedText(context, this.title, 2, -2, 116, 0xD5E6FF);

        Text status = switch (handler.getStatus()) {
            case VoidResonanceBoxBlockEntity.STATUS_CAN_PROCESS -> Text.translatable("gui.nutonmod.void_resonance_box.status.ready");
            case VoidResonanceBoxBlockEntity.STATUS_OUTPUT_FULL -> Text.translatable("gui.nutonmod.void_resonance_box.status.output_full");
            case VoidResonanceBoxBlockEntity.STATUS_MISSING_CATALYST -> Text.translatable("gui.nutonmod.void_resonance_box.status.missing_catalyst");
            default -> Text.translatable("gui.nutonmod.void_resonance_box.status.missing_material");
        };

        drawTrimmedText(context, Text.translatable("gui.nutonmod.void_resonance_box.current_status", status), 115, 15, 74, 0xBCE9D8);
        drawTrimmedText(context, Text.translatable("gui.nutonmod.void_resonance_box.recommended", getRecommendedText()), 115, 70, 188, 0xB4C2D7);
    }

    private Text getRecommendedText() {
        return switch (handler.getStatus()) {
            case VoidResonanceBoxBlockEntity.STATUS_CAN_PROCESS -> Text.translatable("gui.nutonmod.void_resonance_box.recommended.ready");
            case VoidResonanceBoxBlockEntity.STATUS_OUTPUT_FULL -> Text.translatable("gui.nutonmod.void_resonance_box.recommended.output_full");
            case VoidResonanceBoxBlockEntity.STATUS_MISSING_CATALYST -> Text.translatable("gui.nutonmod.void_resonance_box.recommended.missing_catalyst");
            default -> Text.translatable("gui.nutonmod.void_resonance_box.recommended.missing_material");
        };
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private void drawTrimmedText(DrawContext context, Text text, int x, int y, int maxWidth, int color) {
        Text trimmed = Text.literal(this.textRenderer.trimToWidth(text, maxWidth).getString());
        context.drawText(this.textRenderer, trimmed, x, y, color, false);
    }
}