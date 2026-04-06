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
        b.add("item.nutonmod.energy_core", "能量核心");
        b.add("block.nutonmod.energy_core", "能量核心");
        b.add("item.nutonmod.energy_helmet", "能量头盔");
        b.add("item.nutonmod.energy_chestplate", "能量胸甲");
        b.add("item.nutonmod.energy_leggings", "能量护腿");
        b.add("item.nutonmod.energy_boots", "能量靴子");
        b.add("block.nutonmod.energy_block", "能量方块");
        b.add("item.nutonmod.energy_being_spawn_egg", "能量生物刷怪蛋");

        b.add("item.nutonmod.holy_helmet", "神圣头盔");
        b.add("item.nutonmod.holy_chestplate", "神圣胸甲");
        b.add("item.nutonmod.holy_leggings", "神圣护腿");
        b.add("item.nutonmod.holy_boots", "神圣靴子");

        b.add("item.nutonmod.energy_apple", "能量苹果");
        b.add("item.nutonmod.energy_potato", "能量土豆");
        b.add("item.nutonmod.anthracite", "无烟煤");
        b.add("item.nutonmod.anthracite_block", "无烟煤块");
        b.add("block.nutonmod.anthracite_block", "无烟煤块");
        b.add("item.nutonmod.prospector", "探矿器");
        b.add("item.nutonmod.test_music_disc", "测试音乐唱片");

        b.add("block.nutonmod.energy_stairs", "能量楼梯");
        b.add("block.nutonmod.energy_slab", "能量台阶");
        b.add("block.nutonmod.energy_button", "能量按钮");
        b.add("block.nutonmod.energy_pressure_plate", "能量压力板");
        b.add("block.nutonmod.energy_fence", "能量栅栏");
        b.add("block.nutonmod.energy_fence_gate", "能量栅栏门");
        b.add("block.nutonmod.energy_wall", "能量墙");
        b.add("block.nutonmod.energy_door", "能量门");
        b.add("block.nutonmod.energy_trapdoor", "能量活板门");
        b.add("block.nutonmod.sanctum_gate", "圣殿之门");

        b.add("item.nutonmod.energy_sword", "能量剑");
        b.add("item.nutonmod.energy_pickaxe", "能量镐");
        b.add("item.nutonmod.energy_axe", "能量斧");
        b.add("item.nutonmod.energy_shovel", "能量锹");
        b.add("item.nutonmod.energy_hoe", "能量锄");
        b.add("item.nutonmod.energy_ingot", "能量锭");
        b.add("item.nutonmod.raw_energy", "粗能量");
        b.add("item.nutonmod.altar_shard", "祭坛碎片");
        b.add("item.nutonmod.core_stabilizer", "核心稳定器");
        b.add("item.nutonmod.storm_fragment", "风暴碎片");
        b.add("item.nutonmod.sanctum_key", "圣殿钥匙");
        b.add("block.nutonmod.energy_ore", "能量矿石");
        b.add("block.nutonmod.deepslate_energy_ore", "深板岩能量矿石");

        b.add("item.nutonmod.hat", "帽子");
        b.add("entity.nutonmod.energy_being", "能量生物");
        b.add("entity.minecraft.villager.energy_master", "能量大师");
        b.add("entity.minecraft.villager.nutonmod.energy_master", "能量大师");

        b.add("block.nutonmod.energy_potato_crop", "能量土豆作物");
        b.add("item.nutonmod.corn_seeds", "玉米种子");
        b.add("item.nutonmod.corn", "玉米");
        b.add("sounds.nutonmod.prospector_found_ore", "探矿器发现矿石");
        b.add("sounds.nutonmod.energy_block_break", "能量方块破坏");
        b.add("sounds.nutonmod.energy_block_place", "能量方块放置");
        b.add("sounds.nutonmod.energy_block_hit", "能量方块击中");
        b.add("sounds.nutonmod.energy_block_step", "能量方块踩踏");
        b.add("sounds.nutonmod.energy_block_fall", "能量方块落地");

        b.add("jukebox_song.nutonmod.test", "测试");
        b.add("item.nutonmod.energy_bucket", "能量桶");
        b.add("item.nutonmod.energy_horse_armor", "能量马铠");

        b.add("block.nutonmod.box", "箱子");
        b.add("item.nutonmod.box", "箱子");
        b.add("container.box", "箱子");

        b.add("block.nutonmod.polishing_machine", "抛光机");
        b.add("item.nutonmod.polishing_machine", "抛光机");
        b.add("container.polishing_machine", "抛光机");
        b.add("gui.nutonmod.polishing_machine.status.ready", "可加工");
        b.add("gui.nutonmod.polishing_machine.status.missing_material", "缺少材料");
        b.add("gui.nutonmod.polishing_machine.status.output_full", "输出已满");

        b.add("block.nutonmod.energy_log", "能量原木");
        b.add("block.nutonmod.energy_wood", "能量木");
        b.add("block.nutonmod.stripped_energy_log", "去皮能量原木");
        b.add("block.nutonmod.stripped_energy_wood", "去皮能量木");
        b.add("block.nutonmod.energy_planks", "能量木板");
        b.add("block.nutonmod.energy_leaves", "能量树叶");
        b.add("block.nutonmod.energy_sapling", "能量树苗");
        b.add("block.nutonmod.energy_flower", "能量花");
        b.add("block.nutonmod.potted_energy_flower", "盆栽能量花");

        b.add("biome.nutonmod.energy_biome", "能量群系");
        b.add("biome.nutonmod.charged_forest", "充能森林");
        b.add("biome.nutonmod.crystal_plains", "晶簇平原");
        b.add("biome.nutonmod.storm_fields", "风暴原野");
        b.add("biome.nutonmod.fracture_canyons", "裂隙峡谷");
        b.add("biome.nutonmod.core_wastes", "核心荒原");

        b.add("itemGroup.nutonmod.nuton_group", "Nuton 模组");
    }
}
