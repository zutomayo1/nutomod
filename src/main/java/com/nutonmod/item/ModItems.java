package com.nutonmod.item;

import com.nutonmod.ModArmorMaterials;
import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.ModFluids;
import com.nutonmod.item.custom.*;
import com.nutonmod.sound.ModJukeBoxSongs;
import com.nutonmod.util.RegistryNamingRules;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import com.nutonmod.entity.ModEntities;

public class ModItems {

    //能量锭
    public static final Item ENERGY_INGOT = registerItem("energy_ingot", new Item(new Item.Settings()));
    public static final Item RAW_ENERGY = registerItem("raw_energy", new Item(new Item.Settings()));
    public static final Item ALTAR_SHARD = registerItem("altar_shard", new Item(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item CORE_STABILIZER = registerItem("core_stabilizer", new Item(new Item.Settings().maxCount(16).rarity(Rarity.RARE)));
    public static final Item STORM_FRAGMENT = registerItem("storm_fragment", new Item(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item STORM_ALLOY = registerItem("storm_alloy", new Item(new Item.Settings().maxCount(64).rarity(Rarity.RARE)));
    public static final Item CRYSTAL_MATRIX = registerItem("crystal_matrix", new Item(new Item.Settings().maxCount(32).rarity(Rarity.RARE)));
    public static final Item SANCTUM_KEY = registerItem("sanctum_key", new Item(new Item.Settings().maxCount(16).rarity(Rarity.RARE)));
    public static final Item CORE_HEART = registerItem("core_heart", new Item(new Item.Settings().maxCount(16).rarity(Rarity.EPIC)));
    public static final Item ANNIHILATION_EYE = registerItem("annihilation_eye", new Item(new Item.Settings().maxCount(16).rarity(Rarity.EPIC)));
    public static final Item ANNIHILATION_CORE = registerItem("annihilation_core", new Item(new Item.Settings().maxCount(16).rarity(Rarity.EPIC)));
    public static final Item SINGULARITY_SHARD = registerItem("singularity_shard", new Item(new Item.Settings().maxCount(64).rarity(Rarity.RARE)));
    public static final Item ENERGY_ESSENCE = registerItem("energy_essence", new Item(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item ENERGY_CRYSTAL = registerItem("energy_crystal", new Item(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item TUNING_ESSENCE = registerItem("tuning_essence", new Item(new Item.Settings().maxCount(64).rarity(Rarity.RARE)));
    public static final Item SPRITE_DUST = registerItem("sprite_dust", new SpriteDustItem(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item CRYSTAL_SHARD = registerItem("crystal_shard", new Item(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item SNAIL_SHELL = registerItem("snail_shell", new Item(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item CRYSTAL_SNAIL_CAPSULE = registerItem("crystal_snail_capsule", new CrystalSnailCapsuleItem(new Item.Settings().maxCount(16).rarity(Rarity.UNCOMMON)));
    public static final Item STORM_FEATHER = registerItem("storm_feather", new Item(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item STORM_SEED = registerItem("storm_seed", new StormSeedItem(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item WARDEN_CORE = registerItem("warden_core", new WardenCoreItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE)));
    public static final Item VOID_SILK = registerItem("void_silk", new VoidSilkItem(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item VOID_SLIME_BALL = registerItem("void_slime_ball", new VoidSlimeBallItem(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item STORM_ESSENCE = registerItem("storm_essence", new StormEssenceItem(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item STORM_CORE = registerItem("storm_core", new StormCoreItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE)));
    public static final Item STORM_CHARM = registerItem("storm_charm", new StormCharmItem(new Item.Settings().maxCount(16).rarity(Rarity.RARE)));
    public static final Item ANNIHILATION_BLADE = registerItem("annihilation_blade", new AnnihilationBladeItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().fireproof().rarity(Rarity.EPIC)
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 8, -2.3F))));
    public static final Item GROUND_SHAKER = registerItem("ground_shaker", new GroundShakerItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().maxCount(1).maxDamage(1800).fireproof().rarity(Rarity.EPIC)
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 12, -3.1F))));

    public static final Item ARBITER_SIGIL = registerItem("arbiter_sigil", new Item(new Item.Settings().maxCount(16).rarity(Rarity.EPIC)));
    public static final Item THUNDERFORGED_CORE = registerItem("thunderforged_core", new Item(new Item.Settings().maxCount(16).rarity(Rarity.EPIC)));
    public static final Item LIGHTNING_ESSENCE = registerItem("lightning_essence", new Item(new Item.Settings().maxCount(64).rarity(Rarity.UNCOMMON)));
    public static final Item ARBITER_TROPHY = registerItem("arbiter_trophy", new Item(new Item.Settings().maxCount(16).rarity(Rarity.EPIC)));
    public static final Item THUNDER_SPEAR = registerItem("thunder_spear", new ThunderSpearItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().maxCount(1).maxDamage(600).fireproof().rarity(Rarity.EPIC)
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 10, -2.2F))));
    public static final Item THUNDER_RIPPER = registerItem("thunder_ripper", new ThunderRipperItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().maxCount(1).maxDamage(1200).fireproof().rarity(Rarity.EPIC)
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 13, -2.2F))));
    public static final Item VOID_CALAMITY = registerItem("void_calamity", new VoidCalamityBowItem(
            new Item.Settings().maxCount(1).maxDamage(1200).fireproof().rarity(Rarity.EPIC)));
    public static final Item STORM_SCEPTER = registerItem("storm_scepter", new StormScepterItem(
            new Item.Settings().maxCount(1).maxDamage(800).fireproof().rarity(Rarity.EPIC)));
    public static final Item ARCHON_MARK = registerItem("archon_mark", new Item(new Item.Settings().maxCount(16).rarity(Rarity.EPIC)));
    public static final Item END_HEART = registerItem("end_heart", new Item(new Item.Settings().maxCount(8).rarity(Rarity.EPIC)));
    public static final Item VOID_FRAGMENT = registerItem("void_fragment", new Item(new Item.Settings().maxCount(64).rarity(Rarity.RARE)));
    public static final Item END_TOME = registerItem("end_tome", new EndTomeItem(new Item.Settings().maxCount(16).rarity(Rarity.EPIC)));
    public static final Item VOID_WINGS = registerItem("void_wings", new VoidWingsItem(new Item.Settings().maxDamage(432).maxCount(1).rarity(Rarity.EPIC).fireproof()));
    public static final Item END_JUDICATOR = registerItem("end_judicator", new EndJudicatorItem(ModToolMaterials.ENERGY_INGOT,
            new Item.Settings().maxCount(1).maxDamage(1600).fireproof().rarity(Rarity.EPIC)
                    .attributeModifiers(SwordItem.createAttributeModifiers(ModToolMaterials.ENERGY_INGOT, 15, -2.3F))));


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
    // 能量头盔（需要激活才能发挥全部效果）
    // 能量裤子
    // 能量鞋子

    // 光明套装 - 神圣能量
    public static final Item HOLY_HELMET = registerItem("holy_helmet",
            new HolyHelmetItem());
    public static final Item HOLY_CHESTPLATE = registerItem("holy_chestplate",
            new HolyChestplateItem());
    public static final Item HOLY_LEGGINGS = registerItem("holy_leggings",
            new HolyLeggingsItem());
    public static final Item HOLY_BOOTS = registerItem("holy_boots",
            new HolyBootsItem());
    public static final Item CRYSTAL_HELMET = registerItem("crystal_helmet",
            new CrystalHelmetItem(ModArmorMaterials.ENERGY_INGOT, ArmorItem.Type.HELMET, new Item.Settings().rarity(Rarity.RARE)));

    //食物物品
    public static final Item ENERGY_APPLE = registerItem("energy_apple",
            new EnergyAppleItem());
    public static final Item ENERGY_POTATO = registerItem("energy_potato",
            new EnergyPotatoItem());


    public static final Item ANTHRACITE = registerItem("anthracite",
            new Item(new Item.Settings()));

    public static final Item ENERGY_BEING_SPAWN_EGG = registerItem("energy_being_spawn_egg", 
        new SpawnEggItem(
            ModEntities.ENERGY_BEING,
            0x00FFFF,  // 主颜色 - 青色
            0xFFFFFF,  // 斑点颜色 - 白色
            new Item.Settings()
        )
    );

    public static final Item RIFT_STALKER_SPAWN_EGG = registerItem("rift_stalker_spawn_egg",
            new SpawnEggItem(
                    ModEntities.RIFT_STALKER,
                    0x2D4C56,
                    0x89F0FF,
                    new Item.Settings()
            )
    );
    public static final Item SINGULARITY_SPAWN_EGG = registerItem("singularity_spawn_egg",
            new SpawnEggItem(
                    ModEntities.SINGULARITY,
                    0x17151B,
                    0xF08C48,
                    new Item.Settings().rarity(Rarity.EPIC)
            )
    );
    public static final Item STORM_ARBITER_SPAWN_EGG = registerItem("storm_arbiter_spawn_egg",
            new SpawnEggItem(
                    ModEntities.STORM_ARBITER,
                    0x0E2E6B,
                    0xB5D5FF,
                    new Item.Settings().rarity(Rarity.EPIC)
            )
    );
    public static final Item VOID_ARCHON_SPAWN_EGG = registerItem("void_archon_spawn_egg",
            new SpawnEggItem(
                    ModEntities.VOID_ARCHON,
                    0x160A1C,
                    0xF2F2F2,
                    new Item.Settings().rarity(Rarity.EPIC)
            )
    );
    public static final Item ENERGY_SPRITE_SPAWN_EGG = registerItem("energy_sprite_spawn_egg",
            new SpawnEggItem(
                    ModEntities.ENERGY_SPRITE,
                    0x4DBAFF,
                    0xD9F3FF,
                    new Item.Settings().rarity(Rarity.UNCOMMON)
            )
    );
    public static final Item CRYSTAL_SNAIL_SPAWN_EGG = registerItem("crystal_snail_spawn_egg",
            new SpawnEggItem(
                    ModEntities.CRYSTAL_SNAIL,
                    0x5E3B87,
                    0xCBA5FF,
                    new Item.Settings().rarity(Rarity.UNCOMMON)
            )
    );
    public static final Item STORM_FINCH_SPAWN_EGG = registerItem("storm_finch_spawn_egg",
            new SpawnEggItem(
                    ModEntities.STORM_FINCH,
                    0x2D7BFF,
                    0x8FD4FF,
                    new Item.Settings().rarity(Rarity.UNCOMMON)
            )
    );
    public static final Item ENERGY_WARDEN_SPAWN_EGG = registerItem("energy_warden_spawn_egg",
            new SpawnEggItem(
                    ModEntities.ENERGY_WARDEN,
                    0x2E86FF,
                    0xBFD9FF,
                    new Item.Settings().rarity(Rarity.RARE)
            )
    );
    public static final Item VOID_CRAWLER_SPAWN_EGG = registerItem("void_crawler_spawn_egg",
            new SpawnEggItem(
                    ModEntities.VOID_CRAWLER,
                    0x10091F,
                    0xEFEFFF,
                    new Item.Settings().rarity(Rarity.UNCOMMON)
            )
    );
    public static final Item STORM_ELEMENTAL_SPAWN_EGG = registerItem("storm_elemental_spawn_egg",
            new SpawnEggItem(
                    ModEntities.STORM_ELEMENTAL,
                    0x1B5EE0,
                    0xA9D9FF,
                    new Item.Settings().rarity(Rarity.RARE)
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
        RegistryNamingRules.validateItemId(id);
        return Registry.register(Registries.ITEM, Identifier.of(NutonMod.MOD_ID, id), item);
    }
    
    public static void registerModItems() {
         NutonMod.LOGGER.info("Registering Mod Items for " + NutonMod.MOD_ID);
         NutonMod.LOGGER.info("Registered pure items (block items are registered in ModBlocks)");
    }
}
