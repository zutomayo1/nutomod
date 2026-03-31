package com.nutonmod.item;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import com.nutonmod.item.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    
    public static final ItemGroup NUTON_GROUP = Registry.register(
        Registries.ITEM_GROUP,
        Identifier.of(NutonMod.MOD_ID, "nuton_group"),
        FabricItemGroup.builder()
            .displayName(Text.literal("\u00a7bNoton \u6a21\u7ec4"))
            .icon(() -> ModItems.ENERGY_CORE.getDefaultStack())
            .entries((context, entries) -> {
                entries.add(ModItems.ENERGY_CORE);
                entries.add(ModItems.ENERGY_CHESTPLATE);
                entries.add(ModItems.ENERGY_HELMET);
                entries.add(ModItems.ENERGY_LEGGINGS);
                entries.add(ModItems.ENERGY_BOOTS);
                entries.add(ModBlocks.ENERGY_BLOCK.asItem());
                entries.add(ModItems.HOLY_HELMET);
                entries.add(ModItems.HOLY_CHESTPLATE);
                entries.add(ModItems.HOLY_LEGGINGS);
                entries.add(ModItems.HOLY_BOOTS);
                entries.add(ModItems.ENERGY_APPLE);
                entries.add(ModItems.ENERGY_POTATO);
                entries.add(ModItems.ANTHRACITE);
                entries.add(ModBlocks.ANTHRACITE_BLOCK);
                entries.add(ModItems.PROSPECTOR);

                entries.add(ModBlocks.ENERGY_STAIRS);
                entries.add(ModBlocks.ENERGY_SLAB);
                entries.add(ModBlocks.ENERGY_BUTTON);
                entries.add(ModBlocks.ENERGY_PRESSURE_PLATE);
                entries.add(ModBlocks.ENERGY_FENCE);
                entries.add(ModBlocks.ENERGY_FENCE_GATE);
                entries.add(ModBlocks.ENERGY_WALL);
                entries.add(ModBlocks.ENERGY_DOOR);
                entries.add(ModBlocks.ENERGY_TRAPDOOR);

                entries.add(ModItems.ENERGY_INGOT);
                entries.add(ModItems.ENERGY_SWORD);
                entries.add(ModItems.ENERGY_PICKAXE);
                entries.add(ModItems.ENERGY_AXE);
                entries.add(ModItems.ENERGY_SHOVEL);
                entries.add(ModItems.ENERGY_HOE);

                entries.add(ModItems.HAT);

                entries.add(ModItems.CORN_SEEDS);
                entries.add(ModItems.CORN);
                entries.add(ModItems.TEST_MUSIC_DISC);


            })
            .build()
    );

    public static void registerItemGroups() {
        NutonMod.LOGGER.info("Registering Item Groups for " + NutonMod.MOD_ID);
    }
}
