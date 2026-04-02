package com.nutonmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nutonmod.NutonMod;
import com.nutonmod.block.entity.PolishingMachineBlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PolishingMachineScreen extends HandledScreen<PolishingMachineScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(NutonMod.MOD_ID, "textures/gui/polishing_machine_gui.png");
    public PolishingMachineScreen(PolishingMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        renderProgressArrow(context, x, y);
    }

    private void renderProgressArrow(DrawContext context, int x, int y) {
        if (handler.isCrafting()){
            context.drawTexture(TEXTURE, x + 85, y + 30, 176, 0, 8, handler.getScaledProgress());
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        Text status = switch (handler.getMachineStatus()) {
            case PolishingMachineBlockEntity.STATUS_CAN_PROCESS ->
                    Text.translatable("gui.nutonmod.polishing_machine.status.ready");
            case PolishingMachineBlockEntity.STATUS_OUTPUT_FULL ->
                    Text.translatable("gui.nutonmod.polishing_machine.status.output_full");
            default ->
                    Text.translatable("gui.nutonmod.polishing_machine.status.missing_material");
        };
        context.drawText(this.textRenderer, status, 8, 58, 0x404040, false);
    }
}
