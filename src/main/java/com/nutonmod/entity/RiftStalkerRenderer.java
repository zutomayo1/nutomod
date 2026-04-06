package com.nutonmod.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RiftStalkerRenderer extends GeoEntityRenderer<RiftStalker> {
    public RiftStalkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new RiftStalkerModel());
        this.shadowRadius = 0.95F;
    }
}
