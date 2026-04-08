package com.nutonmod.item;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.PatchouliAPI;

public class ModItemGroups {
    
    public static final ItemGroup NUTON_GROUP = Registry.register(
        Registries.ITEM_GROUP,
        Identifier.of(NutonMod.MOD_ID, "nuton_group"),
        FabricItemGroup.builder()
            .displayName(Text.literal("\u00a7bNuton \u6a21\u7ec4"))
            .icon(() -> ModItems.ENERGY_CORE != null
                ? ModItems.ENERGY_CORE.getDefaultStack()
                : new ItemStack(Items.DIAMOND))
            .entries((context, entries) -> {
                // Guide
                if (FabricLoader.getInstance().isModLoaded("patchouli")) {
                    ItemStack guideBook = PatchouliAPI.get().getBookStack(Identifier.of(NutonMod.MOD_ID, "energy_realm_guide"));
                    if (!guideBook.isEmpty()) {
                        entries.add(guideBook);
                    }
                }

                // Core progression materials
                entries.add(ModItems.ENERGY_CORE);
                entries.add(ModItems.ENERGY_INGOT);
                entries.add(ModItems.RAW_ENERGY);
                entries.add(ModItems.ENERGY_ESSENCE);
                entries.add(ModItems.ALTAR_SHARD);
                entries.add(ModItems.CORE_STABILIZER);
                entries.add(ModItems.CRYSTAL_MATRIX);
                entries.add(ModItems.SANCTUM_KEY);
                entries.add(ModItems.CORE_HEART);
                entries.add(ModItems.STORM_FRAGMENT);
                entries.add(ModItems.STORM_ALLOY);
                entries.add(ModItems.STORM_CHARM);
                entries.add(ModItems.ANNIHILATION_EYE);
                entries.add(ModItems.ANNIHILATION_CORE);
                entries.add(ModItems.SINGULARITY_SHARD);
                entries.add(ModItems.ARBITER_SIGIL);
                entries.add(ModItems.THUNDERFORGED_CORE);
                entries.add(ModItems.LIGHTNING_ESSENCE);
                entries.add(ModItems.ARCHON_MARK);
                entries.add(ModItems.END_HEART);
                entries.add(ModItems.VOID_FRAGMENT);
                entries.add(ModItems.END_TOME);
                entries.add(ModItems.VOID_WINGS);

                // Weapons and combat tools
                entries.add(ModItems.ANNIHILATION_BLADE);
                entries.add(ModItems.THUNDER_SPEAR);
                entries.add(ModItems.VOID_CALAMITY);
                entries.add(ModItems.STORM_SCEPTER);
                entries.add(ModItems.END_JUDICATOR);
                entries.add(ModItems.ENERGY_SWORD);
                entries.add(ModItems.ENERGY_PICKAXE);
                entries.add(ModItems.ENERGY_AXE);
                entries.add(ModItems.ENERGY_SHOVEL);
                entries.add(ModItems.ENERGY_HOE);
                entries.add(ModItems.PROSPECTOR);

                // Armor and wearable gear
                entries.add(ModItems.HOLY_HELMET);
                entries.add(ModItems.HOLY_CHESTPLATE);
                entries.add(ModItems.HOLY_LEGGINGS);
                entries.add(ModItems.HOLY_BOOTS);
                entries.add(ModItems.ENERGY_HORSE_ARMOR);
                entries.add(ModItems.HAT);

                // Consumables and utility items
                entries.add(ModItems.ENERGY_APPLE);
                entries.add(ModItems.ENERGY_POTATO);
                entries.add(ModItems.CORN_SEEDS);
                entries.add(ModItems.CORN);
                entries.add(ModItems.ENERGY_BUCKET);
                entries.add(ModItems.TEST_MUSIC_DISC);

                // Spawn eggs
                entries.add(ModItems.ENERGY_BEING_SPAWN_EGG);
                entries.add(ModItems.RIFT_STALKER_SPAWN_EGG);
                entries.add(ModItems.SINGULARITY_SPAWN_EGG);
                entries.add(ModItems.STORM_ARBITER_SPAWN_EGG);
                entries.add(ModItems.VOID_ARCHON_SPAWN_EGG);

                // Functional blocks and machines
                entries.add(ModBlocks.STABILIZER_BEACON);
                entries.add(ModBlocks.SANCTUM_GATE);
                entries.add(ModBlocks.POLISHING_MACHINE);
                entries.add(Items.LIGHTNING_ROD);
                entries.add(ModBlocks.BOX.asItem());

                // Ores, storage and building blocks
                entries.add(ModBlocks.ENERGY_ORE);
                entries.add(ModBlocks.DEEPSLATE_ENERGY_ORE);
                entries.add(ModBlocks.ENERGY_BLOCK.asItem());
                entries.add(ModItems.ANTHRACITE);
                entries.add(ModBlocks.ANTHRACITE_BLOCK);
                entries.add(ModBlocks.ENERGY_PLANKS);
                entries.add(ModBlocks.ENERGY_STAIRS);
                entries.add(ModBlocks.ENERGY_SLAB);
                entries.add(ModBlocks.ENERGY_BUTTON);
                entries.add(ModBlocks.ENERGY_PRESSURE_PLATE);
                entries.add(ModBlocks.ENERGY_FENCE);
                entries.add(ModBlocks.ENERGY_FENCE_GATE);
                entries.add(ModBlocks.ENERGY_WALL);
                entries.add(ModBlocks.ENERGY_DOOR);
                entries.add(ModBlocks.ENERGY_TRAPDOOR);

                // Nature blocks
                entries.add(ModBlocks.ENERGY_LOG);
                entries.add(ModBlocks.ENERGY_WOOD);
                entries.add(ModBlocks.STRIPPED_ENERGY_LOG);
                entries.add(ModBlocks.STRIPPED_ENERGY_WOOD);
                entries.add(ModBlocks.ENERGY_LEAVES);
                entries.add(ModBlocks.ENERGY_SAPLING);
                entries.add(ModBlocks.ENERGY_FLOWER);
            })
            .build()
    );

    public static void registerItemGroups() {
        NutonMod.LOGGER.info("Registering Item Groups for " + NutonMod.MOD_ID);
    }
}
