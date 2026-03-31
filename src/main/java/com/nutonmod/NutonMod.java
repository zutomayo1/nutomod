package com.nutonmod;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.entity.ModEntities;
import com.nutonmod.item.EnergyChestplateItem;
import com.nutonmod.item.ModItems;
import com.nutonmod.item.ModItemGroups;
import com.nutonmod.sound.ModSoundEvents;
import com.nutonmod.util.ModCustomTrades;
import com.nutonmod.util.ModLootTableModifiers;
import com.nutonmod.villager.Modvillagers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NutonMod implements ModInitializer {

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.

	public static final String MOD_ID = "nutonmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModBlocks.registerModBlocks(); // 注册方块
		ModItems.registerModItems();   // 注册物品
		ModItemGroups.registerItemGroups(); // 注册物品组
		ModEntities.register();        // 注册实体
		ModLootTableModifiers.modifyLootTable();
		ModCustomTrades.registerModCustomTrades();
		Modvillagers.registerVillagers(); // 注册村民及其职业
		ModSoundEvents.registerModSoundEvents();
		
		// 使用 Fabric API 添加熔炉燃料
		FuelRegistry.INSTANCE.add(ModItems.ANTHRACITE, 1600); // 无烟煤：80 秒
		FuelRegistry.INSTANCE.add(ModBlocks.ANTHRACITE_BLOCK.asItem(), 14400); // 无烟煤块：720 秒（9 倍）

		// 注册服务器 tick 事件，用于处理能量胸甲的效果
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
				if (player != null && !player.getWorld().isClient) {
					// 检查玩家是否穿着能量胸甲
					ItemStack chestplate = player.getEquippedStack(net.minecraft.entity.EquipmentSlot.CHEST);
					if (chestplate.getItem() instanceof EnergyChestplateItem) {
						((EnergyChestplateItem) chestplate.getItem()).clientTick(chestplate, player);
					}
				}
			}
		});
		
		LOGGER.info("Hello Fabric world!");
	}
}