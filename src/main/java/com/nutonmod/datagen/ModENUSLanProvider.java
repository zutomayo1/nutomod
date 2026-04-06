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
        translationBuilder.add("item.nutonmod.test_music_disc", "Test Music Disc");

        translationBuilder.add("block.nutonmod.energy_stairs", "Energy Stairs");
        translationBuilder.add("block.nutonmod.energy_slab", "Energy Slab");
        translationBuilder.add("block.nutonmod.energy_button", "Energy Button");
        translationBuilder.add("block.nutonmod.energy_pressure_plate", "Energy Pressure Plate");
        translationBuilder.add("block.nutonmod.stabilizer_beacon", "Stabilizer Beacon");
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
        translationBuilder.add("item.nutonmod.raw_energy", "Raw Energy");
        translationBuilder.add("item.nutonmod.altar_shard", "Altar Shard");
        translationBuilder.add("item.nutonmod.core_stabilizer", "Core Stabilizer");
        translationBuilder.add("item.nutonmod.storm_fragment", "Storm Fragment");
        translationBuilder.add("item.nutonmod.storm_alloy", "Storm Alloy");
        translationBuilder.add("item.nutonmod.crystal_matrix", "Crystal Matrix");
        translationBuilder.add("item.nutonmod.sanctum_key", "Sanctum Key");
        translationBuilder.add("item.nutonmod.core_heart", "Core Heart");
        translationBuilder.add("item.nutonmod.annihilation_eye", "Eye of Annihilation");
        translationBuilder.add("item.nutonmod.annihilation_core", "Annihilation Core");
        translationBuilder.add("item.nutonmod.singularity_shard", "Singularity Shard");
        translationBuilder.add("item.nutonmod.energy_essence", "Energy Essence");
        translationBuilder.add("item.nutonmod.storm_charm", "Storm Charm");
        translationBuilder.add("block.nutonmod.energy_ore", "Energy Ore");
        translationBuilder.add("block.nutonmod.deepslate_energy_ore", "Deepslate Energy Ore");

        translationBuilder.add("item.nutonmod.hat", "Hat");
        translationBuilder.add("entity.nutonmod.energy_being", "Energy Being");
        translationBuilder.add("entity.nutonmod.energy_being.stormborn", "Stormborn Energy Being");
        translationBuilder.add("entity.nutonmod.rift_stalker", "Rift Stalker");
        translationBuilder.add("entity.nutonmod.rift_stalker.storm", "Storm Rift Stalker");
        translationBuilder.add("entity.nutonmod.singularity", "Singularity of Annihilation");
        translationBuilder.add("boss.nutonmod.singularity.need_core_wastes", "Need to summon in Core Wastes biome");
        translationBuilder.add("boss.nutonmod.singularity.already_active", "A Singularity is already active nearby");
        translationBuilder.add("boss.nutonmod.singularity.awakened", "Singularity of Annihilation has awakened!");
        translationBuilder.add("boss.nutonmod.singularity.phase_2", "Core collapse begins!");
        translationBuilder.add("boss.nutonmod.singularity.phase_3", "Annihilation descends!");
        translationBuilder.add("boss.nutonmod.singularity.black_hole_warning", "Space is collapsing into a black hole!");
        translationBuilder.add("boss.nutonmod.singularity.nova_warning", "Annihilation nova is charging - find a shield anchor!");
        translationBuilder.add("boss.nutonmod.singularity.rampage_warning", "The Singularity is entering rampage charge!");
        translationBuilder.add("boss.nutonmod.singularity.mirror_warning", "The core splits - find the true Singularity!");
        translationBuilder.add("boss.nutonmod.singularity.decoy_name", "Singularity Phantom");
        translationBuilder.add("boss.nutonmod.singularity.bar.phase_1", "Singularity of Annihilation - Gravity Awakening");
        translationBuilder.add("boss.nutonmod.singularity.bar.phase_2", "Singularity of Annihilation - Core Collapse");
        translationBuilder.add("boss.nutonmod.singularity.bar.phase_3", "Singularity of Annihilation - Annihilation Descent");
        translationBuilder.add("boss.nutonmod.singularity.node_cooldown", "Energy node cooling down: %s s");
        translationBuilder.add("boss.nutonmod.singularity.node_red", "Red node activated: +50% attack power");
        translationBuilder.add("boss.nutonmod.singularity.node_blue", "Blue node activated: +50% movement speed");
        translationBuilder.add("boss.nutonmod.singularity.node_yellow", "Yellow node activated: +20 hearts healed");
        translationBuilder.add("boss.nutonmod.singularity.node_purple", "Purple node activated: +20 hearts absorption");
        translationBuilder.add("entity.minecraft.villager.energy_master", "Energy Master");
        translationBuilder.add("entity.minecraft.villager.nutonmod.energy_master", "Energy Master");

        translationBuilder.add("block.nutonmod.energy_potato_crop", "Energy Potato Crop");
        translationBuilder.add("item.nutonmod.corn_seeds", "Corn Seeds");
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
        translationBuilder.add("item.nutonmod.rift_stalker_spawn_egg", "Rift Stalker Spawn Egg");
        translationBuilder.add("item.nutonmod.singularity_spawn_egg", "Singularity Spawn Egg");

        translationBuilder.add("block.nutonmod.box", "Box");
        translationBuilder.add("item.nutonmod.box", "Box");
        translationBuilder.add("container.box", "Box");

        translationBuilder.add("block.nutonmod.polishing_machine", "Polishing Machine");
        translationBuilder.add("container.polishing_machine", "Polishing Machine");
        translationBuilder.add("gui.nutonmod.polishing_machine.status.ready", "Ready");
        translationBuilder.add("gui.nutonmod.polishing_machine.status.missing_material", "Missing Material");
        translationBuilder.add("gui.nutonmod.polishing_machine.status.output_full", "Output Full");

        translationBuilder.add("block.nutonmod.energy_log", "Energy Log");
        translationBuilder.add("block.nutonmod.energy_wood", "Energy Wood");
        translationBuilder.add("block.nutonmod.stripped_energy_log", "Stripped Energy Log");
        translationBuilder.add("block.nutonmod.stripped_energy_wood", "Stripped Energy Wood");
        translationBuilder.add("block.nutonmod.energy_planks", "Energy Planks");
        translationBuilder.add("block.nutonmod.energy_leaves", "Energy Leaves");
        translationBuilder.add("block.nutonmod.energy_sapling", "Energy Sapling");
        translationBuilder.add("block.nutonmod.energy_flower", "Energy Flower");
        translationBuilder.add("block.nutonmod.potted_energy_flower", "Potted Energy Flower");
        translationBuilder.add("biome.nutonmod.energy_biome", "Energy Biome");

    }
}
