package com.nutonmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModENUSLanProvider extends FabricLanguageProvider {
    public ModENUSLanProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("item.nutonmod.energy_core", "Energy Core");
        translationBuilder.add("item.nutonmod.energy_helmet", "Energy Helmet");
        translationBuilder.add("item.nutonmod.energy_leggings", "Energy Leggings");
        translationBuilder.add("item.nutonmod.energy_boots", "Energy Boots");
        translationBuilder.add("block.nutonmod.energy_block", "Energy Block");
        translationBuilder.add("item.nutonmod.energy_chestplate", "Energy Chestplate");
        translationBuilder.add("item.nutonmod.energy_being_spawn_egg", "Energy Being Spawn Egg");
        translationBuilder.add("item.nutonmod.holy_helmet", "Holy Helmet");
        translationBuilder.add("item.nutonmod.holy_chestplate", "Holy Chestplate");
        translationBuilder.add("item.nutonmod.holy_leggings", "Holy Leggings");
        translationBuilder.add("item.nutonmod.holy_boots", "Holy Boots");
        translationBuilder.add("item.nutonmod.energy_apple", "Energy Apple");
        translationBuilder.add("item.nutonmod.energy_potato", "Energy Potato");
        translationBuilder.add("item.nutonmod.anthracite", "Anthracite");
        translationBuilder.add("item.nutonmod.anthracite_block", "Anthracite Block");
        translationBuilder.add("item.nutonmod.prospector", "Prospector");
        translationBuilder.add("item.nutonmod,test_music_disc", "Test Music Disc");

        translationBuilder.add("block.nutonmod.energy_stairs", "Energy Stairs");
        translationBuilder.add("block.nutonmod.energy_slab", "Energy Slab");
        translationBuilder.add("block.nutonmod.energy_button", "Energy Button");
        translationBuilder.add("block.nutonmod.energy_pressure_plate", "Energy Pressure Plate");
        translationBuilder.add("block.nutonmod.energy_fence", "Energy Fence");
        translationBuilder.add("block.nutonmod.energy_fence_gate", "Energy Fence Gate");
        translationBuilder.add("block.nutonmod.energy_wall", "Energy Wall");
        translationBuilder.add("block.nutonmod.energy_door", "Energy Door");
        translationBuilder.add("block.nutonmod.energy_trapdoor", "Energy Trapdoor");

        translationBuilder.add("item.nutonmod.energy_sword", "Energy Sword");
        translationBuilder.add("item.nutonmod.energy_pickaxe", "Energy Pickaxe");
        translationBuilder.add("item.nutonmod.energy_axe", "Energy Axe");
        translationBuilder.add("item.nutonmod.energy_shovel", "Energy Shovel");
        translationBuilder.add("item.nutonmod.energy_hoe", "Energy Hoe");
        translationBuilder.add("item.nutonmod.energy_ingot", "Energy Ingot");

        translationBuilder.add("item.nutonmod.hat", "Hat");
        translationBuilder.add("entity.minecraft.villager.energy_master", "Energy Master");
        translationBuilder.add("entity.minecraft.villager.nutonmod.energy_master", "Energy Master");

        translationBuilder.add("block.nutonmod.energy_potato_crop", "Energy Potato Crop");
        translationBuilder.add("block.nutonmod.corn_seeds", "Corn Seeds");
        translationBuilder.add("item.nutonmod.corn", "Corn");
        translationBuilder.add("sounds.nutonmod.prospector_found_ore", "Prospector Found Ore");
        translationBuilder.add("sounds.nutonmod.energy_block_break", "Energy Block Break");
        translationBuilder.add("sounds.nutonmod.energy_block_place", "Energy Block Place");
        translationBuilder.add("sounds.nutonmod.energy_block_hit", "Energy Block Hit");
        translationBuilder.add("sounds.nutonmod.energy_block_step", "Energy Block Step");
        translationBuilder.add("sounds.nutonmod.energy_block_fall", "Energy Block Fall");

        translationBuilder.add("jukebox_song.nutonmod.test", "Test");
        translationBuilder.add("item.nutonmod.energy_bucket", "Energy Bucket");

        translationBuilder.add("item.nutonmod.energy_horse_armor", "Energy Horse Armor");

        translationBuilder.add("block.nutonmod.box", "Box");
        translationBuilder.add("item.nutonmod.box", "Box");
        translationBuilder.add("container.box", "Box");

        translationBuilder.add("block.nutonmod.polishing_machine", "Polishing Machine");
        translationBuilder.add("container.polishing_machine", "Polishing Machine");

    }
}
