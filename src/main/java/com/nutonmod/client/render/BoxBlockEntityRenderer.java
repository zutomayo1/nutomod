package com.nutonmod.client.render;

import com.nutonmod.NutonMod;
import com.nutonmod.block.entity.BoxBlockEntity;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BoxBlockEntityRenderer implements BlockEntityRenderer<BoxBlockEntity> {
    private static final Identifier TEXTURE = Identifier.of(NutonMod.MOD_ID, "textures/block/box.png");
    private final ModelPart lid;
    private final ModelPart base;

    public BoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = getTexturedModelData().createModel();
        this.base = modelPart.getChild("bottom");
        this.lid = modelPart.getChild("lid");
    }

    @Override
    public void render(BoxBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        float progress = entity.getAnimationProgress(tickDelta);
        progress = 1.0f - progress;
        progress = 1.0f - progress * progress * progress;

        this.lid.pitch = -(progress * ((float) Math.PI / 2.0f));
        var vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE));

        matrices.push();
        this.base.render(matrices, vertexConsumer, light, overlay);
        this.lid.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
    }

    @Override
    public int getRenderDistance() {
        return 64;
    }

    private static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild("bottom", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(0.0f, 0.0f, 0.0f, 2.0f, 15.0f, 16.0f)
                        .uv(4, 0)
                        .cuboid(14.0f, 0.0f, 0.0f, 2.0f, 15.0f, 16.0f)
                        .uv(0, 8)
                        .cuboid(2.0f, 0.0f, 1.0f, 12.0f, 14.0f, 14.0f),
                ModelTransform.NONE);
        root.addChild("lid", ModelPartBuilder.create()
                        .uv(0, 4)
                        .cuboid(0.0f, 0.0f, -1.0f, 2.0f, 1.0f, 16.0f)
                        .uv(4, 4)
                        .cuboid(14.0f, 0.0f, -1.0f, 2.0f, 1.0f, 16.0f)
                        .uv(8, 0)
                        .cuboid(2.0f, -1.0f, 0.0f, 12.0f, 1.0f, 14.0f),
                ModelTransform.pivot(0.0f, 15.0f, 1.0f));
        return TexturedModelData.of(modelData, 64, 64);
    }
}
