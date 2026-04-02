package com.nutonmod.item;

import com.nutonmod.ModArmorMaterials;
import com.nutonmod.NutonMod;
import com.nutonmod.armor.EnergyArmor;
import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.ModFluids;
import com.nutonmod.entity.ModEntities;
import com.nutonmod.item.custom.EnergyAppleItem;
import com.nutonmod.item.custom.EnergyPotatoItem;
import com.nutonmod.item.custom.HatItem;
import com.nutonmod.item.custom.Prospector;
import com.nutonmod.sound.ModJukeBoxSongs;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import com.nutonmod.entity.ModEntities;

public class ModItems {

    // 基础物品 - 能量核心（用于放置的方块）
    public static final Item ENERGY_CORE = registerBlockItem("energy_core", ModBlocks.ENERGY_CORE);
    
    // 基础物品 - 能量方块（用于放置的方块）
    public static final Item ENERGY_BLOCK = registerBlockItem("energy_block", ModBlocks.ENERGY_BLOCK);

    //能量锭
    public static final Item ENERGY_INGOT = registerItem("energy_ingot", new Item(new Item.Settings()));


    // 物品 - 能量工具（需要激活才能发挥威力）
    public static final Item ENERGY_SWORD = registerItem("energy_sword", new SwordItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 6, -2.4F))));
    public static final Item ENERGY_PICKAXE = registerItem("energy_pickaxe", new PickaxeItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().fireproof().attributeModifiers(PickaxeItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 1.0F, -3.0F))));
    public static final Item ENERGY_AXE = registerItem("energy_axe", new AxeItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().fireproof().attributeModifiers(AxeItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 5.0F, -2.8F))));
    public static final Item ENERGY_SHOVEL = registerItem("energy_shovel", new ShovelItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().fireproof().attributeModifiers(ShovelItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 1.5F, -2.0F))));
    public static final Item ENERGY_HOE = registerItem("energy_hoe", new HoeItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().fireproof().attributeModifiers(HoeItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 0.0F, -0.1F))));


    /**
     * 能源chestplate
     */// 能量胸甲（需要激活才能发挥全部效果）
    public static final Item ENERGY_CHESTPLATE = registerItem("energy_chestplate", EnergyArmor.createChestplate());
    // 能量头盔（需要激活才能发挥全部效果）
    public static final Item ENERGY_HELMET = registerItem("energy_helmet", EnergyArmor.createHelmet());
    // 能量裤子
    public static final Item ENERGY_LEGGINGS = registerItem("energy_leggings", EnergyArmor.createLeggings());
    // 能量鞋子
    public static final Item ENERGY_BOOTS = registerItem("energy_boots", EnergyArmor.createBoots());

    // 光明套装 - 神圣能量
    public static final Item HOLY_HELMET = registerItem("holy_helmet",
            new HolyHelmetItem());
    public static final Item HOLY_CHESTPLATE = registerItem("holy_chestplate",
            new HolyChestplateItem());
    public static final Item HOLY_LEGGINGS = registerItem("holy_leggings",
            new HolyLeggingsItem());
    public static final Item HOLY_BOOTS = registerItem("holy_boots",
            new HolyBootsItem());

    //食物物品
    public static final Item ENERGY_APPLE = registerItem("energy_apple",
            new EnergyAppleItem());
    public static final Item ENERGY_POTATO = registerItem("energy_potato",
            new EnergyPotatoItem());


    public static final Item ANTHRACITE = registerItem("anthracite",
            new Item(new Item.Settings()));
    public static final Item ANTHRACITE_BLOCK = registerBlockItem("anthracite_block",
            ModBlocks.ANTHRACITE_BLOCK);

    // 能量人生成蛋
    public static final Item ENERGY_BEING_SPAWN_EGG = registerItem("energy_being_spawn_egg", 
        new SpawnEggItem(
            ModEntities.ENERGY_BEING,
            0x00FFFF,  // 主颜色 - 青色
            0xFFFFFF,  // 斑点颜色 - 白色
            new Item.Settings()
        )
    );

    public static final Item PROSPECTOR = registerItem("prospector",
            new Prospector(new Item.Settings().maxDamage(127)));
    //帽子
    public static final Item HAT = registerItem("hat", new HatItem(HatItem.Type.HAT,
            new Item.Settings().maxDamage(1638)));
    //玉米种子
    public static final Item CORN_SEEDS = registerItem("corn_seeds",
            new AliasedBlockItem(ModBlocks.CORN_CROP, new Item.Settings()));
    public static final Item CORN = registerItem("corn",
            new Item(new Item.Settings().food(ModFoodComponents.CORN)));
    public static final Item TEST_MUSIC_DISC = registerItem("test_music_disc",
            new Item(new Item.Settings().maxCount(1).rarity(Rarity.RARE).jukeboxPlayable(ModJukeBoxSongs.TEST)));
    public static final Item ENERGY_BUCKET = registerItem("energy_bucket",
            new BucketItem(ModFluids.STILL_ENERGY, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1)));

    public static final Item ENERGY_HORSE_ARMOR = registerItem("energy_horse_armor",
            new AnimalArmorItem(ModArmorMaterials.ENERGY_INGOT, AnimalArmorItem.Type.EQUESTRIAN, false, new Item.Settings().maxCount(1)));






    private static Item registerItem(String id, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(NutonMod.MOD_ID, id), item);
    }
    
    private static Item registerBlockItem(String id, net.minecraft.block.Block block) {
        BlockItem item = new BlockItem(block, new Item.Settings());
        Registry.register(Registries.ITEM, Identifier.of(NutonMod.MOD_ID, id), item);
        return item;
    }
    
    public static void registerModItems() {
         NutonMod.LOGGER.info("Registering Mod Items for " + NutonMod.MOD_ID);
         NutonMod.LOGGER.info("Registered: energy_core (block), energy_block (block), energy_sword, energy_chestplate, energy_helmet, energy_leggings, energy_boots, holy_helmet, holy_chestplate, holy_leggings, holy_boots, energy_being_spawn_egg");
    }
}
