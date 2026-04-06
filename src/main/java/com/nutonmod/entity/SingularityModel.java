package com.nutonmod.entity;

import com.nutonmod.NutonMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class SingularityModel extends GeoModel<SingularityEntity> {
    private static final Identifier TEXTURE_P1 = Identifier.of(NutonMod.MOD_ID, "textures/entity/singularity_p1.png");
    private static final Identifier TEXTURE_P2 = Identifier.of(NutonMod.MOD_ID, "textures/entity/singularity_p2.png");
    private static final Identifier TEXTURE_P3 = Identifier.of(NutonMod.MOD_ID, "textures/entity/singularity_p3.png");

    @Override
    public Identifier getModelResource(SingularityEntity animatable) {
        return Identifier.of(NutonMod.MOD_ID, "geo/singularity.geo.json");
    }

    @Override
    public Identifier getTextureResource(SingularityEntity animatable) {
        float healthRatio = animatable.getHealth() / Math.max(animatable.getMaxHealth(), 1.0F);
        if (healthRatio <= 0.30F) {
            return TEXTURE_P3;
        }
        if (healthRatio <= 0.60F) {
            return TEXTURE_P2;
        }
        return TEXTURE_P1;
    }

    @Override
    public Identifier getAnimationResource(SingularityEntity animatable) {
        return Identifier.of(NutonMod.MOD_ID, "animations/singularity.animation.json");
    }
}
