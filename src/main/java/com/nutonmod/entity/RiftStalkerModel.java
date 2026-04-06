package com.nutonmod.entity;

import com.nutonmod.NutonMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class RiftStalkerModel extends GeoModel<RiftStalker> {
    @Override
    public Identifier getModelResource(RiftStalker animatable) {
        return Identifier.of(NutonMod.MOD_ID, "geo/rift_stalker.geo.json");
    }

    @Override
    public Identifier getTextureResource(RiftStalker animatable) {
        int variant = Math.floorMod(animatable.getUuid().hashCode(), 3);
        return switch (variant) {
            case 1 -> Identifier.of(NutonMod.MOD_ID, "textures/entity/rift_stalker_crimson.png");
            case 2 -> Identifier.of(NutonMod.MOD_ID, "textures/entity/rift_stalker_amber.png");
            default -> Identifier.of(NutonMod.MOD_ID, "textures/entity/rift_stalker_azure.png");
        };
    }

    @Override
    public Identifier getAnimationResource(RiftStalker animatable) {
        return Identifier.of(NutonMod.MOD_ID, "animations/rift_stalker.animation.json");
    }
}
