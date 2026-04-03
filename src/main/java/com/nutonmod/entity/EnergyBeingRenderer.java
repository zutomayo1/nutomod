package com.nutonmod.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EnergyBeingRenderer extends GeoEntityRenderer<EnergyBeing> {
    public EnergyBeingRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new EnergyBeingModel());
        this.shadowRadius = 0.5F;
    }
}
