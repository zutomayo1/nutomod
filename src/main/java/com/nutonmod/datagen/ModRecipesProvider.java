package com.nutonmod.datagen;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import com.nutonmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends FabricRecipeProvider {
    public ModRecipesProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        offerReversibleCompactingRecipes(
                recipeExporter,
                RecipeCategory.MISC,
                ModBlocks.ENERGY_BLOCK,
                RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.ENERGY_CORE
        );

        offerReversibleCompactingRecipes(
                recipeExporter,
                RecipeCategory.MISC,
                ModItems.ANTHRACITE,
                RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.ANTHRACITE_BLOCK
        );

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.STABILIZER_BEACON)
                .pattern(" A ")
                .pattern("SCS")
                .pattern("BMB")
                .input('A', ModItems.ALTAR_SHARD)
                .input('S', ModItems.STORM_ALLOY)
                .input('C', ModItems.CORE_STABILIZER)
                .input('B', ModBlocks.ENERGY_BLOCK)
                .input('M', ModItems.CRYSTAL_MATRIX)
                .criterion(hasItem(ModItems.CORE_STABILIZER), conditionsFromItem(ModItems.CORE_STABILIZER))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "stabilizer_beacon"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ANNIHILATION_EYE)
                .pattern(" S ")
                .pattern("ECE")
                .pattern(" S ")
                .input('S', ModItems.STORM_ALLOY)
                .input('E', ModItems.ENERGY_INGOT)
                .input('C', ModItems.CORE_HEART)
                .criterion(hasItem(ModItems.CORE_HEART), conditionsFromItem(ModItems.CORE_HEART))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "annihilation_eye"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.DIMENSIONAL_TUNER)
                .pattern("CAC")
                .pattern("BEB")
                .pattern("CDC")
                .input('A', ModItems.CRYSTAL_MATRIX)
                .input('B', ModItems.STORM_ALLOY)
                .input('C', ModItems.ENERGY_INGOT)
                .input('D', ModItems.VOID_FRAGMENT)
                .input('E', ModBlocks.POLISHING_MACHINE)
                .criterion(hasItem(ModItems.CRYSTAL_MATRIX), conditionsFromItem(ModItems.CRYSTAL_MATRIX))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "dimensional_tuner"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.ENERGY_DISINTEGRATOR)
                .pattern("SVS")
                .pattern("VCV")
                .pattern("SIS")
                .input('S', ModItems.STORM_ALLOY)
                .input('V', ModItems.VOID_FRAGMENT)
                .input('C', ModItems.CORE_HEART)
                .input('I', Items.NETHERITE_INGOT)
                .criterion(hasItem(ModItems.CORE_HEART), conditionsFromItem(ModItems.CORE_HEART))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_disintegrator"));

        // Energy wood line
        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENERGY_PLANKS, 4)
                .input(ModBlocks.ENERGY_LOG)
                .criterion(hasItem(ModBlocks.ENERGY_LOG), conditionsFromItem(ModBlocks.ENERGY_LOG))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENERGY_PLANKS, 4)
                .input(ModBlocks.ENERGY_WOOD)
                .criterion(hasItem(ModBlocks.ENERGY_WOOD), conditionsFromItem(ModBlocks.ENERGY_WOOD))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_planks_from_energy_wood"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENERGY_PLANKS, 4)
                .input(ModBlocks.STRIPPED_ENERGY_LOG)
                .criterion(hasItem(ModBlocks.STRIPPED_ENERGY_LOG), conditionsFromItem(ModBlocks.STRIPPED_ENERGY_LOG))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_planks_from_stripped_energy_log"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENERGY_PLANKS, 4)
                .input(ModBlocks.STRIPPED_ENERGY_WOOD)
                .criterion(hasItem(ModBlocks.STRIPPED_ENERGY_WOOD), conditionsFromItem(ModBlocks.STRIPPED_ENERGY_WOOD))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_planks_from_stripped_energy_wood"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENERGY_WOOD, 3)
                .pattern("##")
                .pattern("##")
                .input('#', ModBlocks.ENERGY_LOG)
                .criterion(hasItem(ModBlocks.ENERGY_LOG), conditionsFromItem(ModBlocks.ENERGY_LOG))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_ENERGY_WOOD, 3)
                .pattern("##")
                .pattern("##")
                .input('#', ModBlocks.STRIPPED_ENERGY_LOG)
                .criterion(hasItem(ModBlocks.STRIPPED_ENERGY_LOG), conditionsFromItem(ModBlocks.STRIPPED_ENERGY_LOG))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENERGY_STAIRS, 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .input('#', ModBlocks.ENERGY_PLANKS)
                .criterion(hasItem(ModBlocks.ENERGY_PLANKS), conditionsFromItem(ModBlocks.ENERGY_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ENERGY_SLAB, 6)
                .pattern("###")
                .input('#', ModBlocks.ENERGY_PLANKS)
                .criterion(hasItem(ModBlocks.ENERGY_PLANKS), conditionsFromItem(ModBlocks.ENERGY_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.ENERGY_FENCE, 3)
                .pattern("#S#")
                .pattern("#S#")
                .input('#', ModBlocks.ENERGY_PLANKS)
                .input('S', Items.STICK)
                .criterion(hasItem(ModBlocks.ENERGY_PLANKS), conditionsFromItem(ModBlocks.ENERGY_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.ENERGY_FENCE_GATE)
                .pattern("S#S")
                .pattern("S#S")
                .input('#', ModBlocks.ENERGY_PLANKS)
                .input('S', Items.STICK)
                .criterion(hasItem(ModBlocks.ENERGY_PLANKS), conditionsFromItem(ModBlocks.ENERGY_PLANKS))
                .offerTo(recipeExporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.ENERGY_BUTTON)
                .input(ModBlocks.ENERGY_PLANKS)
                .criterion(hasItem(ModBlocks.ENERGY_PLANKS), conditionsFromItem(ModBlocks.ENERGY_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.ENERGY_PRESSURE_PLATE)
                .pattern("##")
                .input('#', ModBlocks.ENERGY_PLANKS)
                .criterion(hasItem(ModBlocks.ENERGY_PLANKS), conditionsFromItem(ModBlocks.ENERGY_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.ENERGY_DOOR, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .input('#', ModBlocks.ENERGY_PLANKS)
                .criterion(hasItem(ModBlocks.ENERGY_PLANKS), conditionsFromItem(ModBlocks.ENERGY_PLANKS))
                .offerTo(recipeExporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.ENERGY_TRAPDOOR, 2)
                .pattern("###")
                .pattern("###")
                .input('#', ModBlocks.ENERGY_PLANKS)
                .criterion(hasItem(ModBlocks.ENERGY_PLANKS), conditionsFromItem(ModBlocks.ENERGY_PLANKS))
                .offerTo(recipeExporter);

        CookingRecipeJsonBuilder.createSmelting(
                        Ingredient.ofItems(ModBlocks.ENERGY_LOG),
                        RecipeCategory.MISC,
                        Items.CHARCOAL,
                        0.1f,
                        200)
                .criterion(hasItem(ModBlocks.ENERGY_LOG), conditionsFromItem(ModBlocks.ENERGY_LOG))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "charcoal_from_smelting_energy_log"));

        CookingRecipeJsonBuilder.createSmelting(
                        Ingredient.ofItems(ModBlocks.STRIPPED_ENERGY_LOG),
                        RecipeCategory.MISC,
                        Items.CHARCOAL,
                        0.1f,
                        200)
                .criterion(hasItem(ModBlocks.STRIPPED_ENERGY_LOG), conditionsFromItem(ModBlocks.STRIPPED_ENERGY_LOG))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "charcoal_from_smelting_stripped_energy_log"));

        CookingRecipeJsonBuilder.createSmelting(
                        Ingredient.ofItems(ModBlocks.ENERGY_WOOD),
                        RecipeCategory.MISC,
                        Items.CHARCOAL,
                        0.1f,
                        200)
                .criterion(hasItem(ModBlocks.ENERGY_WOOD), conditionsFromItem(ModBlocks.ENERGY_WOOD))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "charcoal_from_smelting_energy_wood"));

        CookingRecipeJsonBuilder.createSmelting(
                        Ingredient.ofItems(ModBlocks.STRIPPED_ENERGY_WOOD),
                        RecipeCategory.MISC,
                        Items.CHARCOAL,
                        0.1f,
                        200)
                .criterion(hasItem(ModBlocks.STRIPPED_ENERGY_WOOD), conditionsFromItem(ModBlocks.STRIPPED_ENERGY_WOOD))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "charcoal_from_smelting_stripped_energy_wood"));

        CookingRecipeJsonBuilder.createSmelting(
                        Ingredient.ofItems(ModBlocks.ENERGY_ORE),
                        RecipeCategory.MISC,
                        ModItems.ENERGY_INGOT,
                        0.7f,
                        200)
                .criterion(hasItem(ModBlocks.ENERGY_ORE), conditionsFromItem(ModBlocks.ENERGY_ORE))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_ingot_from_smelting_energy_ore"));

        CookingRecipeJsonBuilder.createSmelting(
                        Ingredient.ofItems(ModBlocks.DEEPSLATE_ENERGY_ORE),
                        RecipeCategory.MISC,
                        ModItems.ENERGY_INGOT,
                        0.7f,
                        200)
                .criterion(hasItem(ModBlocks.DEEPSLATE_ENERGY_ORE), conditionsFromItem(ModBlocks.DEEPSLATE_ENERGY_ORE))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_ingot_from_smelting_deepslate_energy_ore"));

        CookingRecipeJsonBuilder.createSmelting(
                        Ingredient.ofItems(ModItems.RAW_ENERGY),
                        RecipeCategory.MISC,
                        ModItems.ENERGY_INGOT,
                        0.7f,
                        200)
                .criterion(hasItem(ModItems.RAW_ENERGY), conditionsFromItem(ModItems.RAW_ENERGY))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_ingot_from_smelting_raw_energy"));

        CookingRecipeJsonBuilder.createBlasting(
                        Ingredient.ofItems(ModBlocks.ENERGY_ORE),
                        RecipeCategory.MISC,
                        ModItems.ENERGY_INGOT,
                        0.7f,
                        100)
                .criterion(hasItem(ModBlocks.ENERGY_ORE), conditionsFromItem(ModBlocks.ENERGY_ORE))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_ingot_from_blasting_energy_ore"));

        CookingRecipeJsonBuilder.createBlasting(
                        Ingredient.ofItems(ModBlocks.DEEPSLATE_ENERGY_ORE),
                        RecipeCategory.MISC,
                        ModItems.ENERGY_INGOT,
                        0.7f,
                        100)
                .criterion(hasItem(ModBlocks.DEEPSLATE_ENERGY_ORE), conditionsFromItem(ModBlocks.DEEPSLATE_ENERGY_ORE))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_ingot_from_blasting_deepslate_energy_ore"));

        CookingRecipeJsonBuilder.createBlasting(
                        Ingredient.ofItems(ModItems.RAW_ENERGY),
                        RecipeCategory.MISC,
                        ModItems.ENERGY_INGOT,
                        0.7f,
                        100)
                .criterion(hasItem(ModItems.RAW_ENERGY), conditionsFromItem(ModItems.RAW_ENERGY))
                .offerTo(recipeExporter, Identifier.of(NutonMod.MOD_ID, "energy_ingot_from_blasting_raw_energy"));
    }
}
