package com.nutonmod.block.entity;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<BoxBlockEntity> BOX = create("box", BlockEntityType.Builder.create(BoxBlockEntity::new, ModBlocks.BOX));
    private static <T extends BlockEntity> BlockEntityType<T> create(String id, BlockEntityType.Builder<T> builder) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(NutonMod.MOD_ID, id), builder.build(null));
    }

    public static void registerModBlockEntities() {
        NutonMod.LOGGER.info("Registering Mod Block Entities for " + NutonMod.MOD_ID);
    }
}
