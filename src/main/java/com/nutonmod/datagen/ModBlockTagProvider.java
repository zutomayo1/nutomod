package com.nutonmod.datagen;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.tags.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        // 注册所有可探测的矿石到 prospector_ores 标签
        getOrCreateTagBuilder(ModBlockTags.PROSPECTOR_ORES)
                .add(Blocks.DIAMOND_ORE)
                .add(Blocks.IRON_ORE)
                .add(Blocks.GOLD_ORE)
                .add(Blocks.COAL_ORE)
                .add(Blocks.REDSTONE_ORE)
                .add(Blocks.LAPIS_ORE)
                .add(Blocks.EMERALD_ORE)
                .add(Blocks.COPPER_ORE)
            
            // 深层矿石也添加进去
                .add(Blocks.DEEPSLATE_DIAMOND_ORE)
                .add(Blocks.DEEPSLATE_IRON_ORE)
                .add(Blocks.DEEPSLATE_GOLD_ORE)
                .add(Blocks.DEEPSLATE_COAL_ORE)
                .add(Blocks.DEEPSLATE_REDSTONE_ORE)
                .add(Blocks.DEEPSLATE_LAPIS_ORE)
                .add(Blocks.DEEPSLATE_EMERALD_ORE)
                .add(Blocks.DEEPSLATE_COPPER_ORE);

        getOrCreateTagBuilder(BlockTags.FENCES)
                .add(ModBlocks.ENERGY_FENCE);
        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                .add(ModBlocks.ENERGY_FENCE_GATE);
        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.ENERGY_WALL);
        getOrCreateTagBuilder(BlockTags.DOORS)
                .add(ModBlocks.ENERGY_DOOR);
        getOrCreateTagBuilder(BlockTags.TRAPDOORS)
                .add(ModBlocks.ENERGY_TRAPDOOR);
        getOrCreateTagBuilder(BlockTags.STAIRS)
                .add(ModBlocks.ENERGY_STAIRS);
        getOrCreateTagBuilder(BlockTags.SLABS)
                .add(ModBlocks.ENERGY_SLAB);
        getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(ModBlocks.ENERGY_PLANKS);
        getOrCreateTagBuilder(BlockTags.BUTTONS)
                .add(ModBlocks.ENERGY_BUTTON);
        getOrCreateTagBuilder(BlockTags.PRESSURE_PLATES)
                .add(ModBlocks.ENERGY_PRESSURE_PLATE);

        getOrCreateTagBuilder(BlockTags.CROPS)
                .add(ModBlocks.ENERGY_POTATO_CROP);
        getOrCreateTagBuilder(BlockTags.FLOWERS)
                .add(ModBlocks.ENERGY_FLOWER);
        getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS)
                .add(ModBlocks.ENERGY_FLOWER);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.ENERGY_LOG)
                .add(ModBlocks.ENERGY_WOOD)
                .add(ModBlocks.STRIPPED_ENERGY_LOG)
                .add(ModBlocks.STRIPPED_ENERGY_WOOD);


    }
}
