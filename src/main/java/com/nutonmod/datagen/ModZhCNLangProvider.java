package com.nutonmod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModZhCNLangProvider extends FabricLanguageProvider {
    public ModZhCNLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder b) {
        b.add("item.nutonmod.energy_core", "\u80fd\u91cf\u6838\u5fc3");
        b.add("item.nutonmod.energy_helmet", "\u80fd\u91cf\u5934\u76d4");
        b.add("item.nutonmod.energy_leggings", "\u80fd\u91cf\u62a4\u817f");
        b.add("item.nutonmod.energy_boots", "\u80fd\u91cf\u9774\u5b50");
        b.add("block.nutonmod.energy_block", "\u80fd\u91cf\u65b9\u5757");
        b.add("item.nutonmod.energy_chestplate", "\u80fd\u91cf\u80f8\u7532");
        b.add("item.nutonmod.energy_being_spawn_egg", "\u80fd\u91cf\u751f\u7269\u5237\u602a\u86cb");
        b.add("item.nutonmod.holy_helmet", "\u795e\u5723\u5934\u76d4");
        b.add("item.nutonmod.holy_chestplate", "\u795e\u5723\u80f8\u7532");
        b.add("item.nutonmod.holy_leggings", "\u795e\u5723\u62a4\u817f");
        b.add("item.nutonmod.holy_boots", "\u795e\u5723\u9774\u5b50");
        b.add("item.nutonmod.energy_apple", "\u80fd\u91cf\u82f9\u679c");
        b.add("item.nutonmod.energy_potato", "\u80fd\u91cf\u571f\u8c46");
        b.add("item.nutonmod.anthracite", "\u65e0\u70df\u7164");
        b.add("item.nutonmod.anthracite_block", "\u65e0\u70df\u7164\u5757");
        b.add("item.nutonmod.prospector", "\u63a2\u77ff\u5668");
        b.add("item.nutonmod.test_music_disc", "\u6d4b\u8bd5\u97f3\u4e50\u5531\u7247");

        b.add("block.nutonmod.energy_stairs", "\u80fd\u91cf\u697c\u68af");
        b.add("block.nutonmod.energy_slab", "\u80fd\u91cf\u53f0\u9636");
        b.add("block.nutonmod.energy_button", "\u80fd\u91cf\u6309\u94ae");
        b.add("block.nutonmod.energy_pressure_plate", "\u80fd\u91cf\u538b\u529b\u677f");
        b.add("block.nutonmod.stabilizer_beacon", "\u7a33\u5b9a\u4fe1\u6807");
        b.add("block.nutonmod.energy_fence", "\u80fd\u91cf\u6805\u680f");
        b.add("block.nutonmod.energy_fence_gate", "\u80fd\u91cf\u6805\u680f\u95e8");
        b.add("block.nutonmod.energy_wall", "\u80fd\u91cf\u5899");
        b.add("block.nutonmod.energy_door", "\u80fd\u91cf\u95e8");
        b.add("block.nutonmod.energy_trapdoor", "\u80fd\u91cf\u6d3b\u677f\u95e8");
        b.add("block.nutonmod.sanctum_gate", "\u5723\u57df\u4e4b\u95e8");

        b.add("item.nutonmod.energy_sword", "\u80fd\u91cf\u5251");
        b.add("item.nutonmod.energy_pickaxe", "\u80fd\u91cf\u9550");
        b.add("item.nutonmod.energy_axe", "\u80fd\u91cf\u65a7");
        b.add("item.nutonmod.energy_shovel", "\u80fd\u91cf\u94f2");
        b.add("item.nutonmod.energy_hoe", "\u80fd\u91cf\u9504");
        b.add("item.nutonmod.energy_ingot", "\u80fd\u91cf\u952d");
        b.add("item.nutonmod.raw_energy", "\u7c97\u80fd\u91cf");
        b.add("item.nutonmod.altar_shard", "\u796d\u575b\u788e\u7247");
        b.add("item.nutonmod.core_stabilizer", "\u6838\u5fc3\u7a33\u5b9a\u5668");
        b.add("item.nutonmod.storm_fragment", "\u98ce\u66b4\u788e\u7247");
        b.add("item.nutonmod.storm_alloy", "\u98ce\u66b4\u5408\u91d1");
        b.add("item.nutonmod.crystal_matrix", "\u6676\u4f53\u77e9\u9635");
        b.add("item.nutonmod.sanctum_key", "\u5723\u57df\u94a5\u5319");
        b.add("item.nutonmod.core_heart", "\u6838\u5fc3\u4e4b\u5fc3");
        b.add("item.nutonmod.storm_charm", "\u98ce\u66b4\u62a4\u7b26");
        b.add("block.nutonmod.energy_ore", "\u80fd\u91cf\u77ff\u77f3");
        b.add("block.nutonmod.deepslate_energy_ore", "\u6df1\u677f\u5ca9\u80fd\u91cf\u77ff\u77f3");

        b.add("item.nutonmod.hat", "\u5e3d\u5b50");
        b.add("entity.nutonmod.energy_being", "\u80fd\u91cf\u751f\u7269");
        b.add("entity.nutonmod.energy_being.stormborn", "\u98ce\u66b4\u5316\u80fd\u91cf\u751f\u7269");
        b.add("entity.minecraft.villager.energy_master", "\u80fd\u91cf\u5927\u5e08");
        b.add("entity.minecraft.villager.nutonmod.energy_master", "\u80fd\u91cf\u5927\u5e08");

        b.add("block.nutonmod.energy_potato_crop", "\u80fd\u91cf\u571f\u8c46\u4f5c\u7269");
        b.add("item.nutonmod.corn_seeds", "\u7389\u7c73\u79cd\u5b50");
        b.add("item.nutonmod.corn", "\u7389\u7c73");
        b.add("sounds.nutonmod.prospector_found_ore", "\u63a2\u77ff\u5668\u53d1\u73b0\u77ff\u77f3");
        b.add("sounds.nutonmod.energy_block_break", "\u80fd\u91cf\u65b9\u5757\u7834\u574f");
        b.add("sounds.nutonmod.energy_block_place", "\u80fd\u91cf\u65b9\u5757\u653e\u7f6e");
        b.add("sounds.nutonmod.energy_block_hit", "\u80fd\u91cf\u65b9\u5757\u51fb\u4e2d");
        b.add("sounds.nutonmod.energy_block_step", "\u80fd\u91cf\u65b9\u5757\u8e29\u8e0f");
        b.add("sounds.nutonmod.energy_block_fall", "\u80fd\u91cf\u65b9\u5757\u6389\u843d");

        b.add("jukebox_song.nutonmod.test", "\u6d4b\u8bd5");
        b.add("item.nutonmod.energy_bucket", "\u80fd\u91cf\u6876");
        b.add("item.nutonmod.energy_horse_armor", "\u80fd\u91cf\u9a6c\u94e0");

        b.add("block.nutonmod.box", "\u7bb1\u5b50");
        b.add("item.nutonmod.box", "\u7bb1\u5b50");
        b.add("container.box", "\u7bb1\u5b50");

        b.add("block.nutonmod.polishing_machine", "\u629b\u5149\u673a");
        b.add("container.polishing_machine", "\u629b\u5149\u673a");
        b.add("gui.nutonmod.polishing_machine.status.ready", "\u53ef\u52a0\u5de5");
        b.add("gui.nutonmod.polishing_machine.status.missing_material", "\u7f3a\u5c11\u6750\u6599");
        b.add("gui.nutonmod.polishing_machine.status.output_full", "\u8f93\u51fa\u5df2\u6ee1");

        b.add("block.nutonmod.energy_log", "\u80fd\u91cf\u539f\u6728");
        b.add("block.nutonmod.energy_wood", "\u80fd\u91cf\u6728");
        b.add("block.nutonmod.stripped_energy_log", "\u53bb\u76ae\u80fd\u91cf\u539f\u6728");
        b.add("block.nutonmod.stripped_energy_wood", "\u53bb\u76ae\u80fd\u91cf\u6728");
        b.add("block.nutonmod.energy_planks", "\u80fd\u91cf\u6728\u677f");
        b.add("block.nutonmod.energy_leaves", "\u80fd\u91cf\u6811\u53f6");
        b.add("block.nutonmod.energy_sapling", "\u80fd\u91cf\u6811\u82d7");
        b.add("block.nutonmod.energy_flower", "\u80fd\u91cf\u82b1");
        b.add("block.nutonmod.potted_energy_flower", "\u76c6\u683d\u80fd\u91cf\u82b1");
        b.add("biome.nutonmod.energy_biome", "\u80fd\u91cf\u7fa4\u7cfb");
        b.add("biome.nutonmod.charged_forest", "\u5145\u80fd\u68ee\u6797");
        b.add("biome.nutonmod.crystal_plains", "\u6676\u4f53\u5e73\u539f");
        b.add("biome.nutonmod.storm_fields", "\u98ce\u66b4\u539f\u91ce");
        b.add("biome.nutonmod.fracture_canyons", "\u88c2\u9699\u5ce1\u8c37");
        b.add("biome.nutonmod.core_wastes", "\u6838\u5fc3\u8352\u539f");
        b.add("itemGroup.nutonmod.nuton_group", "Nuton \u6a21\u7ec4");
    }
}
