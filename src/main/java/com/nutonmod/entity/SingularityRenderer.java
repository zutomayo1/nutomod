package com.nutonmod.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class SingularityRenderer extends GeoEntityRenderer<SingularityEntity> {
    public SingularityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SingularityModel());
        this.shadowRadius = 1.85F;
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
