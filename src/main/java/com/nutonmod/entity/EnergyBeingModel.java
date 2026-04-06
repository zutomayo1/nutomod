package com.nutonmod.entity;

import com.nutonmod.NutonMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class EnergyBeingModel extends GeoModel<EnergyBeing> {
    @Override
    public Identifier getModelResource(EnergyBeing animatable) {
        return Identifier.of(NutonMod.MOD_ID, "geo/energy_being.geo.json");
    }

    @Override
    public Identifier getTextureResource(EnergyBeing animatable) {
        return Identifier.of(NutonMod.MOD_ID, "textures/entity/energy_being.png");
    }

    @Override
    public Identifier getAnimationResource(EnergyBeing animatable) {
        return Identifier.of(NutonMod.MOD_ID, "animations/energy_being.animation.json");
    }
}
