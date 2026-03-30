package com.nutonmod.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class HatArmorRenderer implements ArmorRenderer {
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity,
                       EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        if (slot != EquipmentSlot.HEAD) {
            return;
        }

        matrices.push();
        contextModel.getHead().rotate(matrices);
        matrices.translate(0.0F, -0.75F, 0.0F);
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                entity,
                stack,
                ModelTransformationMode.HEAD,
                false,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                light,
                OverlayTexture.DEFAULT_UV,
                entity.getId()
        );
        matrices.pop();
    }
}
