package com.nutonmod.datagen;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.custom.CornCropBlock;
import com.nutonmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.BlockState;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {


    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.ENERGY_BLOCK);
        addDrop(ModBlocks.ENERGY_CORE);
        addDrop(ModBlocks.ENERGY_FLOWER);
        addDrop(ModBlocks.POTTED_ENERGY_FLOWER, pottedPlantDrops(ModBlocks.ENERGY_FLOWER));
        
        // 能量马铃薯作物战利品表
        addDrop(ModBlocks.ENERGY_POTATO_CROP, (block) -> 
            LootTable.builder()
                .pool(LootPool.builder()
                    .with(ItemEntry.builder(ModItems.ENERGY_POTATO))
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f)))));

        LootCondition.Builder builder = BlockStatePropertyLootCondition.builder(ModBlocks.CORN_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(CornCropBlock.AGE, 8));
        addDrop(ModBlocks.CORN_CROP, cropDrops(
                ModBlocks.CORN_CROP, ModItems.CORN, ModItems.CORN_SEEDS, builder));
    }
}
