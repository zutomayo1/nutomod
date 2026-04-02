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
        translationBuilder.add("block.nutonmod.energy_block", "能量方块");
        translationBuilder.add("item.nutonmod.energy_chestplate", "能量胸甲");
        translationBuilder.add("item.nutonmod.energy_helmet", "能量头盔");
        translationBuilder.add("item.nutonmod.energy_leggings", "能量护腿");
        translationBuilder.add("item.nutonmod.energy_boots", "能量靴子");
        translationBuilder.add("itemGroup.nutonmod.nuton_group", "Noton 模组");
        translationBuilder.add("item.nutonmod.holy_helmet", "光明头盔");
        translationBuilder.add("item.nutonmod.holy_chestplate", "光明胸甲");
        translationBuilder.add("item.nutonmod.holy_leggings", "光明护腿");
        translationBuilder.add("item.nutonmod.holy_boots", "光明靴子");
        translationBuilder.add("item.nutonmod.energy_apple", "能量苹果");
        translationBuilder.add("item.nutonmod.energy_potato", "能量马铃薯");
        translationBuilder.add("item.nutonmod.anthracite", "无烟煤");
        translationBuilder.add("block.nutonmod.anthracite_block", "无烟煤块");
        translationBuilder.add("item.nutonmod.prospector", "探矿器");
        translationBuilder.add("item.nutonmod.test_music_disc", "测试音乐唱片");

        translationBuilder.add("block.nutonmod.energy_stairs", "能量楼梯");
        translationBuilder.add("block.nutonmod.energy_slab", "能量半砖");
        translationBuilder.add("block.nutonmod.energy_button", "能量按钮");
        translationBuilder.add("block.nutonmod.energy_pressure_plate", "能量压力板");
        translationBuilder.add("block.nutonmod.energy_fence", "能量栅栏");
        translationBuilder.add("block.nutonmod.energy_fence_gate", "能量栅栏门");
        translationBuilder.add("block.nutonmod.energy_wall", "能量墙");
        translationBuilder.add("block.nutonmod.energy_door", "能量门");
        translationBuilder.add("block.nutonmod.energy_trapdoor", "能量活板门");

        translationBuilder.add("item.nutonmod.energy_pickaxe", "能量镐");
        translationBuilder.add("item.nutonmod.energy_axe", "能量斧");
        translationBuilder.add("item.nutonmod.energy_shovel", "能量铲");
        translationBuilder.add("item.nutonmod.energy_hoe", "能量锄");
        translationBuilder.add("item.nutonmod.energy_sword", "能量剑");
        translationBuilder.add("item.nutonmod.energy_ingot", "能量锭");

        translationBuilder.add("item.nutonmod.hat", "帽子");
        translationBuilder.add("entity.minecraft.villager.energy_master", "能量大师");
        translationBuilder.add("entity.minecraft.villager.nutonmod.energy_master", "能量大师");

        translationBuilder.add("block.nutonmod.energy_potato_crop", "能量马铃薯作物");
        translationBuilder.add("item.nutonmod.corn_seeds", "玉米种子");
        translationBuilder.add("item.nutonmod.corn", "玉米");

        translationBuilder.add("sounds.nutonmod.prospector_found_ore", "探矿器发现矿石");
        translationBuilder.add("sounds.nutonmod.energy_block_break", "能量方块被破坏");
        translationBuilder.add("sounds.nutonmod.energy_block_place", "能量方块被放置");
        translationBuilder.add("sounds.nutonmod.energy_block_hit", "能量方块被击打");
        translationBuilder.add("sounds.nutonmod.energy_block_step", "能量方块被踩");
        translationBuilder.add("sounds.nutonmod.energy_block_fall", "能量方块被落下");

        translationBuilder.add("jukebox_song.nutonmod.test", "测试");
        translationBuilder.add("item.nutonmod.energy_bucket", "能量桶");
        translationBuilder.add("item.nutonmod.energy_horse_armor", "能量马铠");
        translationBuilder.add("item.nutonmod.box", "箱子");
        translationBuilder.add("container.box", "箱子");

        translationBuilder.add("item.nutonmod.polishing_machine", "抛光机");
        translationBuilder.add("container.polishing_machine", "抛光机");
    }
}
