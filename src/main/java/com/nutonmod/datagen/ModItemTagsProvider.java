package com.nutonmod.datagen;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagProvider.ItemTagProvider {
	public ModItemTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture, null);
	}

	@Override
	public void configure(RegistryWrapper.WrapperLookup arg) {
		// 剑标签
		getOrCreateTagBuilder(ItemTags.SWORDS)
				.add(ModItems.ENERGY_SWORD);
		
		// 护甲标签
		getOrCreateTagBuilder(ItemTags.CHEST_ARMOR)
				.add(ModItems.ENERGY_CHESTPLATE);
				
		getOrCreateTagBuilder(ItemTags.HEAD_ARMOR)
				.add(ModItems.ENERGY_HELMET);
				
		getOrCreateTagBuilder(ItemTags.LEG_ARMOR)
				.add(ModItems.ENERGY_LEGGINGS);
				
		getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
				.add(ModItems.ENERGY_BOOTS);

		getOrCreateTagBuilder(ItemTags.HEAD_ARMOR)
				.add(ModItems.HOLY_HELMET);
		getOrCreateTagBuilder(ItemTags.CHEST_ARMOR)
				.add(ModItems.ENERGY_CHESTPLATE);
		getOrCreateTagBuilder(ItemTags.LEG_ARMOR)
				.add(ModItems.ENERGY_LEGGINGS);
		getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
				.add(ModItems.ENERGY_BOOTS);
				
		// 可锻造的护甲（可以使用锻造模板）
		getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
				.add(ModItems.ENERGY_CHESTPLATE)
				.add(ModItems.ENERGY_HELMET)
				.add(ModItems.ENERGY_LEGGINGS)
				.add(ModItems.ENERGY_BOOTS)
				.add(ModItems.HOLY_HELMET)
				.add(ModItems.HOLY_CHESTPLATE)
				.add(ModItems.HOLY_LEGGINGS)
				.add(ModItems.HOLY_BOOTS);

		getOrCreateTagBuilder(ItemTags.CREEPER_DROP_MUSIC_DISCS)
				.add(ModItems.TEST_MUSIC_DISC);

		getOrCreateTagBuilder(ItemTags.PLANKS)
				.add(ModBlocks.ENERGY_PLANKS.asItem());

		getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
				.add(ModBlocks.ENERGY_LOG.asItem())
				.add(ModBlocks.ENERGY_WOOD.asItem())
				.add(ModBlocks.STRIPPED_ENERGY_LOG.asItem())
				.add(ModBlocks.STRIPPED_ENERGY_WOOD.asItem());

		getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("minecraft", "raw_materials")))
				.add(ModItems.RAW_ENERGY);


	}
}
