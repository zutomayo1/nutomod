package com.nutonmod.datagen;

import com.nutonmod.block.ModBlockFamilies;
import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.custom.CornCropBlock;
import com.nutonmod.block.custom.PolishingMachine;
import com.nutonmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.data.client.*;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;

public class ModModelsProvider extends FabricModelProvider {
    public ModModelsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        ModBlockFamilies.getBlockFamilies()
                .filter(BlockFamily::shouldGenerateModels)
                .forEach(blockFamily -> 
                    blockStateModelGenerator.registerCubeAllModelTexturePool(blockFamily.getBaseBlock())
                        .family(blockFamily));


        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(ModBlocks.ENERGY_POTATO_CROP)
                        .coordinate(BlockStateVariantMap.create(CropBlock.AGE)
                        .register(stage -> BlockStateVariant.create()
                                .put(VariantSettings.MODEL, blockStateModelGenerator.createSubModel(
                                        ModBlocks.ENERGY_POTATO_CROP, "_stage" + stage, Models.CROSS, TextureMap::cross)
                                )
                        )
            )
        );
        
        // 玉米作物模型
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(ModBlocks.CORN_CROP)
                        .coordinate(BlockStateVariantMap.create(CornCropBlock.AGE)
                        .register(stage -> BlockStateVariant.create()
                                .put(VariantSettings.MODEL, blockStateModelGenerator.createSubModel(
                                        ModBlocks.CORN_CROP, "_stage" + stage, Models.CROSS, TextureMap::cross)
                                )
                        )
            )
        );
        blockStateModelGenerator.registerSimpleState(ModBlocks.STILL_ENERGY);
        blockStateModelGenerator.registerSimpleState(ModBlocks.BOX);
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(ModBlocks.POLISHING_MACHINE)
                        .coordinate(BlockStateVariantMap.create(PolishingMachine.WORKING)
                                .register(working -> BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, Identifier.of("nutonmod", "block/polishing_machine"))))
        );

        blockStateModelGenerator.registerLog(ModBlocks.ENERGY_LOG).log(ModBlocks.ENERGY_LOG).wood(ModBlocks.ENERGY_WOOD);
        blockStateModelGenerator.registerLog(ModBlocks.STRIPPED_ENERGY_LOG).log(ModBlocks.STRIPPED_ENERGY_LOG).wood(ModBlocks.STRIPPED_ENERGY_WOOD);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ENERGY_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_ENERGY_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ENERGY_PLANKS);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ENERGY_LEAVES);
        blockStateModelGenerator.registerTintableCross(ModBlocks.ENERGY_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerFlowerPotPlant(
                ModBlocks.ENERGY_FLOWER,
                ModBlocks.POTTED_ENERGY_FLOWER,
                BlockStateModelGenerator.TintType.NOT_TINTED
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        itemModelGenerator.registerArmor((ArmorItem) ModItems.ENERGY_HELMET);
        itemModelGenerator.registerArmor((ArmorItem) ModItems.ENERGY_CHESTPLATE);
        itemModelGenerator.registerArmor((ArmorItem) ModItems.ENERGY_LEGGINGS);
        itemModelGenerator.registerArmor((ArmorItem) ModItems.ENERGY_BOOTS);


        itemModelGenerator.register(ModItems.HOLY_HELMET, Models.GENERATED);
        itemModelGenerator.register(ModItems.HOLY_CHESTPLATE, Models.GENERATED);
        itemModelGenerator.register(ModItems.HOLY_LEGGINGS, Models.GENERATED);
        itemModelGenerator.register(ModItems.HOLY_BOOTS, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENERGY_APPLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENERGY_POTATO, Models.GENERATED);
        itemModelGenerator.register(ModItems.ANTHRACITE, Models.GENERATED);
        itemModelGenerator.register(ModItems.PROSPECTOR, Models.GENERATED);

        itemModelGenerator.register(ModItems.ENERGY_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_ENERGY, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENERGY_SWORD, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENERGY_PICKAXE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENERGY_SHOVEL, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENERGY_AXE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENERGY_HOE, Models.GENERATED);
        itemModelGenerator.register(ModItems.CORN, Models.GENERATED);

        itemModelGenerator.register(ModItems.CORN_SEEDS, Models.GENERATED);

        itemModelGenerator.register(ModItems.TEST_MUSIC_DISC, Models.TEMPLATE_MUSIC_DISC);

        itemModelGenerator.register(ModItems.ENERGY_BUCKET, Models.GENERATED);

        itemModelGenerator.register(ModItems.ENERGY_HORSE_ARMOR, Models.GENERATED);



    }
}
