package com.nutonmod.world.dimension;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import com.nutonmod.item.ModItems;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public final class ModPortals {
    private ModPortals() {
    }

    public static void registerPortals() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(ModBlocks.ENERGY_BLOCK)
                .lightWithItem(ModItems.RAW_ENERGY)
                .destDimID(ModDimensions.ENERGY_REALM_ID)
                .returnDim(World.OVERWORLD.getValue(), true)
                .tintColor(50, 220, 255)
                .registerPortal();

        NutonMod.LOGGER.info("Registered custom portal to {}", Identifier.of(NutonMod.MOD_ID, "energy_realm"));
    }
}
