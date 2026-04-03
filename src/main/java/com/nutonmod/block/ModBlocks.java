package com.nutonmod.block;

import com.nutonmod.NutonMod;
import com.nutonmod.block.custom.*;
import com.nutonmod.sound.ModSoundEvents;
import com.nutonmod.world.tree.ModTreeGenerator;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    
    // 能量核心方块 - 可以放置，右键切换激活状态，激活时发光等级 15
    public static final Block ENERGY_CORE = register("energy_core", 
        new EnergyCoreBlock(Block.Settings.create().requiresTool()
            .strength(3.0f, 6.0f)  // 硬度 3.0，爆炸抗性 6.0（类似石头）
            .luminance(state -> state.get(EnergyCoreBlock.ACTIVATED) ? 15 : 0))); // 激活时发光等级 15

    // 能量方块
    public static final Block ENERGY_BLOCK = register("energy_block", new Block(AbstractBlock.Settings.create()
            .requiresTool()
            .strength(3.0f, 6.0f)
            .sounds(ModSoundEvents.ENERGY_BLOCK_SOUND_GROUP)));
    public static final Block ENERGY_ORE = register("energy_ore", new ExperienceDroppingBlock(
            net.minecraft.util.math.intprovider.UniformIntProvider.create(2, 5),
            AbstractBlock.Settings.copy(Blocks.IRON_ORE).requiresTool()));
    public static final Block DEEPSLATE_ENERGY_ORE = register("deepslate_energy_ore", new ExperienceDroppingBlock(
            net.minecraft.util.math.intprovider.UniformIntProvider.create(3, 7),
            AbstractBlock.Settings.copy(Blocks.DEEPSLATE_IRON_ORE).requiresTool()));

    // 无烟煤块 - 由 9 个无烟煤合成，可反向分解
    public static final Block ANTHRACITE_BLOCK = register("anthracite_block", new Block(AbstractBlock.Settings.create()
            .requiresTool()
            .strength(5.0f, 6.0f))); // 比石头更硬

    // 能量楼梯
    public static final Block ENERGY_STAIRS = register("energy_stairs",
            new StairsBlock(ENERGY_BLOCK.getDefaultState(), AbstractBlock.Settings.copy(ENERGY_BLOCK)));
    // 能量半砖
    public static final Block ENERGY_SLAB = register("energy_slab",
            new SlabBlock(AbstractBlock.Settings.copy(ENERGY_BLOCK)));
    //能量按钮
    public static final Block ENERGY_BUTTON = register("energy_button",
            new ButtonBlock(BlockSetType.OAK,10, AbstractBlock.Settings.copy(ENERGY_BLOCK)));
    //能量压力板
    public static final Block ENERGY_PRESSURE_PLATE = register("energy_pressure_plate",
            new PressurePlateBlock(BlockSetType.OAK, AbstractBlock.Settings.copy(ENERGY_BLOCK)));
    // 能量栅栏
    public static final Block ENERGY_FENCE = register("energy_fence",
            new FenceBlock(AbstractBlock.Settings.copy(ENERGY_BLOCK)));
    // 能量栅栏门
    public static final Block ENERGY_FENCE_GATE = register("energy_fence_gate",
            new FenceGateBlock(WoodType.OAK,AbstractBlock.Settings.copy(ENERGY_BLOCK)));
    //能量墙
    public static final Block ENERGY_WALL = register("energy_wall",
            new WallBlock(AbstractBlock.Settings.copy(ENERGY_BLOCK)));
    // 能量门
    public static final Block ENERGY_DOOR = register("energy_door",
            new DoorBlock(BlockSetType.OAK,AbstractBlock.Settings.copy(ENERGY_BLOCK)));
    // 能量活板门
    public static final Block ENERGY_TRAPDOOR = register("energy_trapdoor",
            new TrapdoorBlock(BlockSetType.OAK,AbstractBlock.Settings.copy(ENERGY_BLOCK)));

    //能量土豆作物
    public static final Block ENERGY_POTATO_CROP = Registry.register(
            Registries.BLOCK,
            Identifier.of(NutonMod.MOD_ID, "energy_potato_crop"),
            new EnergyPotatoCropBlock(AbstractBlock.Settings.copy(Blocks.POTATOES))
    );

    //玉米作物
    public static final Block CORN_CROP = Registry.register(Registries.BLOCK,Identifier.of(NutonMod.MOD_ID, "corn_crop"),
            new CornCropBlock(AbstractBlock.Settings.copy(Blocks.WHEAT)));

    public static final Block STILL_ENERGY = Registry.register(
            Registries.BLOCK,
            Identifier.of(NutonMod.MOD_ID, "still_energy"),
            new EnergyFluidBlock(ModFluids.STILL_ENERGY, AbstractBlock.Settings.copy(Blocks.WATER).luminance(state -> 10))
    );

    public static final Block BOX = register("box", new BoxBlock(AbstractBlock.Settings.copy(Blocks.CHEST)));
    public static final Block POLISHING_MACHINE = register("polishing_machine",
            new PolishingMachine(AbstractBlock.Settings.copy(Blocks.STONE)
                    .luminance(state -> state.get(PolishingMachine.WORKING) ? 10 : 0))
    );

    public static final Block ENERGY_LOG = register("energy_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_LOG)));
    public static final Block ENERGY_WOOD = register("energy_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.OAK_WOOD)));
    public static final Block STRIPPED_ENERGY_LOG = register("stripped_energy_log",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final Block STRIPPED_ENERGY_WOOD = register("stripped_energy_wood",
            new PillarBlock(AbstractBlock.Settings.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final Block ENERGY_PLANKS = register("energy_planks",
            new Block(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)));
    public static final Block ENERGY_LEAVES = register("energy_leaves",
            new LeavesBlock(AbstractBlock.Settings.copy(Blocks.OAK_LEAVES)));

    public static final Block ENERGY_SAPLING = register("energy_sapling",
            new SaplingBlock(ModTreeGenerator.ENERGY_TREE, AbstractBlock.Settings.copy(Blocks.OAK_SAPLING)));
    public static final Block ENERGY_FLOWER = register("energy_flower",
            new FlowerBlock(StatusEffects.GLOWING, 8,
                    AbstractBlock.Settings.copy(Blocks.DANDELION).nonOpaque().noCollision().breakInstantly()));
    public static final Block POTTED_ENERGY_FLOWER = registerBlockOnly("potted_energy_flower",
            new FlowerPotBlock(ENERGY_FLOWER, AbstractBlock.Settings.copy(Blocks.POTTED_DANDELION).nonOpaque()));

    private static <T extends Block> T register(String id, T block) {
        Identifier identifier = Identifier.of(NutonMod.MOD_ID, id);
        Registry.register(Registries.BLOCK, identifier, block);
        Registry.register(Registries.ITEM, identifier, new BlockItem(block, new Item.Settings()));
        return block;
    }

    private static <T extends Block> T registerBlockOnly(String id, T block) {
        Identifier identifier = Identifier.of(NutonMod.MOD_ID, id);
        Registry.register(Registries.BLOCK, identifier, block);
        return block;
    }
    
    public static void registerModBlocks() {
        NutonMod.LOGGER.info("Registering Mod Blocks for " + NutonMod.MOD_ID);
        NutonMod.LOGGER.info("Registered: energy_core (可激活方块), energy_block, anthracite_block");
    }
}
