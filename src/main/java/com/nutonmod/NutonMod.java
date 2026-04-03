package com.nutonmod;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.ModFluids;
import com.nutonmod.block.entity.ModBlockEntities;
import com.nutonmod.entity.ModEntities;
import com.nutonmod.item.EnergyChestplateItem;
import com.nutonmod.item.ModItemGroups;
import com.nutonmod.item.ModItems;
import com.nutonmod.recipe.ModRecipeTypes;
import com.nutonmod.sound.ModSoundEvents;
import com.nutonmod.util.ModCustomTrades;
import com.nutonmod.util.ModLootTableModifiers;
import com.nutonmod.villager.Modvillagers;
import com.nutonmod.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NutonMod implements ModInitializer {
    public static final String MOD_ID = "nutonmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModBlockEntities.registerModBlockEntities();
        ModItemGroups.registerItemGroups();
        ModEntities.register();
        ModLootTableModifiers.modifyLootTable();
        ModCustomTrades.registerModCustomTrades();
        Modvillagers.registerVillagers();
        ModSoundEvents.registerModSoundEvents();
        ModFluids.registerModFluids();
        ModRecipeTypes.registerRecipeTypes();
        ModWorldGeneration.generateModWorldGen();

        StrippableBlockRegistry.register(ModBlocks.ENERGY_LOG, ModBlocks.STRIPPED_ENERGY_LOG);
        StrippableBlockRegistry.register(ModBlocks.ENERGY_WOOD, ModBlocks.STRIPPED_ENERGY_WOOD);

        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.ENERGY_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.ENERGY_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.ENERGY_PLANKS, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.ENERGY_STAIRS, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.ENERGY_SLAB, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.ENERGY_LEAVES, 30, 60);

        FuelRegistry.INSTANCE.add(ModItems.ANTHRACITE, 1600);
        FuelRegistry.INSTANCE.add(ModBlocks.ANTHRACITE_BLOCK.asItem(), 14400);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player == null || player.getWorld().isClient) {
                    continue;
                }
                ItemStack chestplate = player.getEquippedStack(net.minecraft.entity.EquipmentSlot.CHEST);
                if (chestplate.getItem() instanceof EnergyChestplateItem energyChestplateItem) {
                    energyChestplateItem.clientTick(chestplate, player);
                }
            }
        });

        LOGGER.info("Hello Fabric world!");
    }
}
