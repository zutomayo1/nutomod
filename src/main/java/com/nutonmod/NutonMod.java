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
import com.nutonmod.world.dimension.ModDimensions;
import com.nutonmod.world.dimension.ModPortals;
import com.nutonmod.world.feature.ModFeatures;
import com.nutonmod.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vazkii.patchouli.api.PatchouliAPI;

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
        ModFeatures.register();
        ModRecipeTypes.registerRecipeTypes();
        // Worldgen features are already declared in biome JSON/datagen.
        // Disable runtime biome injections to avoid feature order cycles.
        // ModWorldGeneration.generateModWorldGen();
        ModPortals.registerPortals();

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
                ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);
                if (chestplate.getItem() instanceof EnergyChestplateItem energyChestplateItem) {
                    energyChestplateItem.clientTick(chestplate, player);
                }
                applyEnergyRealmPressure(player);
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerEntity player = handler.getPlayer();
            if (player == null || player.getWorld().isClient) {
                return;
            }

            final String tag = "nutonmod_received_patchouli_guide";
            if (player.getCommandTags().contains(tag)) {
                return;
            }

            ItemStack guide = PatchouliAPI.get().getBookStack(Identifier.of(MOD_ID, "energy_realm_guide"));
            if (!guide.isEmpty()) {
                player.giveItemStack(guide);
                player.addCommandTag(tag);
            }
        });

        LOGGER.info("Hello Fabric world!");
    }

    private static void applyEnergyRealmPressure(PlayerEntity player) {
        if (!player.getWorld().getRegistryKey().equals(ModDimensions.ENERGY_REALM_WORLD_KEY)) {
            return;
        }
        if (player.age % 40 != 0) {
            return;
        }

        boolean hasStabilizer = player.getMainHandStack().isOf(ModItems.CORE_STABILIZER)
                || player.getOffHandStack().isOf(ModItems.CORE_STABILIZER);
        boolean fullEnergyArmor = player.getEquippedStack(EquipmentSlot.HEAD).isOf(ModItems.ENERGY_HELMET)
                && player.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.ENERGY_CHESTPLATE)
                && player.getEquippedStack(EquipmentSlot.LEGS).isOf(ModItems.ENERGY_LEGGINGS)
                && player.getEquippedStack(EquipmentSlot.FEET).isOf(ModItems.ENERGY_BOOTS);

        if (hasStabilizer || fullEnergyArmor) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 120, 0, true, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 120, 0, true, false, true));
            return;
        }

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 120, 0, true, true, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 120, 0, true, true, true));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 80, 0, true, true, true));
    }
}
