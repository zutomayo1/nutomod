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
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("item.nutonmod.energy_core", "能量核心");
        translationBuilder.add("block.nutonmod.energy_core", "能量核心");
        translationBuilder.add("item.nutonmod.energy_helmet", "能量头盔");
        translationBuilder.add("item.nutonmod.energy_leggings", "能量护腿");
        translationBuilder.add("item.nutonmod.energy_boots", "能量靴子");
        translationBuilder.add("block.nutonmod.energy_block", "能量方块");
        translationBuilder.add("item.nutonmod.energy_chestplate", "能量胸甲");
        translationBuilder.add("item.nutonmod.energy_being_spawn_egg", "能量生物刷怪蛋");
        translationBuilder.add("item.nutonmod.holy_helmet", "神圣头盔");
        translationBuilder.add("item.nutonmod.holy_chestplate", "神圣胸甲");
        translationBuilder.add("item.nutonmod.holy_leggings", "神圣护腿");
        translationBuilder.add("item.nutonmod.holy_boots", "神圣靴子");
        translationBuilder.add("item.nutonmod.energy_apple", "能量苹果");
        translationBuilder.add("item.nutonmod.energy_potato", "能量马铃薯");
        translationBuilder.add("item.nutonmod.anthracite", "无烟煤");
        translationBuilder.add("item.nutonmod.anthracite_block", "无烟煤块");
        translationBuilder.add("block.nutonmod.anthracite_block", "无烟煤块");
        translationBuilder.add("item.nutonmod.prospector", "探矿器");
        translationBuilder.add("item.nutonmod.test_music_disc", "测试音乐唱片");

        translationBuilder.add("block.nutonmod.energy_stairs", "能量楼梯");
        translationBuilder.add("block.nutonmod.energy_slab", "能量台阶");
        translationBuilder.add("block.nutonmod.energy_button", "能量按钮");
        translationBuilder.add("block.nutonmod.energy_pressure_plate", "能量压力板");
        translationBuilder.add("block.nutonmod.energy_fence", "能量栅栏");
        translationBuilder.add("block.nutonmod.energy_fence_gate", "能量栅栏门");
        translationBuilder.add("block.nutonmod.energy_wall", "能量墙");
        translationBuilder.add("block.nutonmod.energy_door", "能量门");
        translationBuilder.add("block.nutonmod.energy_trapdoor", "能量活板门");

        translationBuilder.add("item.nutonmod.energy_sword", "能量剑");
        translationBuilder.add("item.nutonmod.energy_pickaxe", "能量镐");
        translationBuilder.add("item.nutonmod.energy_axe", "能量斧");
        translationBuilder.add("item.nutonmod.energy_shovel", "能量锹");
        translationBuilder.add("item.nutonmod.energy_hoe", "能量锄");
        translationBuilder.add("item.nutonmod.energy_ingot", "能量锭");
        translationBuilder.add("item.nutonmod.raw_energy", "粗能量");
        translationBuilder.add("block.nutonmod.energy_ore", "能量矿石");
        translationBuilder.add("block.nutonmod.deepslate_energy_ore", "深板岩能量矿石");

        translationBuilder.add("item.nutonmod.hat", "帽子");
        translationBuilder.add("entity.minecraft.villager.energy_master", "能量大师");
        translationBuilder.add("entity.minecraft.villager.nutonmod.energy_master", "能量大师");

        translationBuilder.add("block.nutonmod.energy_potato_crop", "能量马铃薯作物");
        translationBuilder.add("item.nutonmod.corn_seeds", "玉米种子");
        translationBuilder.add("item.nutonmod.corn", "玉米");
        translationBuilder.add("sounds.nutonmod.prospector_found_ore", "探矿器发现矿石");
        translationBuilder.add("sounds.nutonmod.energy_block_break", "能量方块破坏");
        translationBuilder.add("sounds.nutonmod.energy_block_place", "能量方块放置");
        translationBuilder.add("sounds.nutonmod.energy_block_hit", "能量方块击中");
        translationBuilder.add("sounds.nutonmod.energy_block_step", "能量方块踩踏");
        translationBuilder.add("sounds.nutonmod.energy_block_fall", "能量方块落地");

        translationBuilder.add("jukebox_song.nutonmod.test", "测试");
        translationBuilder.add("item.nutonmod.energy_bucket", "能量桶");
        translationBuilder.add("item.nutonmod.energy_horse_armor", "能量马铠");

        translationBuilder.add("block.nutonmod.box", "箱子");
        translationBuilder.add("item.nutonmod.box", "箱子");
        translationBuilder.add("container.box", "箱子");

        translationBuilder.add("block.nutonmod.polishing_machine", "抛光机");
        translationBuilder.add("item.nutonmod.polishing_machine", "抛光机");
        translationBuilder.add("container.polishing_machine", "抛光机");
        translationBuilder.add("gui.nutonmod.polishing_machine.status.ready", "可加工");
        translationBuilder.add("gui.nutonmod.polishing_machine.status.missing_material", "缺材料");
        translationBuilder.add("gui.nutonmod.polishing_machine.status.output_full", "输出满");

        translationBuilder.add("block.nutonmod.energy_log", "能量原木");
        translationBuilder.add("block.nutonmod.energy_wood", "能量木");
        translationBuilder.add("block.nutonmod.stripped_energy_log", "去皮能量原木");
        translationBuilder.add("block.nutonmod.stripped_energy_wood", "去皮能量木");
        translationBuilder.add("block.nutonmod.energy_planks", "能量木板");
        translationBuilder.add("block.nutonmod.energy_leaves", "能量树叶");
        translationBuilder.add("block.nutonmod.energy_sapling", "能量树苗");
        translationBuilder.add("block.nutonmod.energy_flower", "能量花");
        translationBuilder.add("block.nutonmod.potted_energy_flower", "盆栽能量花");

        translationBuilder.add("itemGroup.nutonmod.nuton_group", "Noton 模组");
    }
}
