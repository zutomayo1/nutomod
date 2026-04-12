package com.nutonmod;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.ModFluids;
import com.nutonmod.block.entity.ModBlockEntities;
import com.nutonmod.command.StormArbiterDebugCommands;
import com.nutonmod.config.ModConfig;
import com.nutonmod.entity.ModEntities;
import com.nutonmod.item.ModItemGroups;
import com.nutonmod.item.ModItems;
import com.nutonmod.network.EnergySyncPayload;
import com.nutonmod.recipe.ModRecipeTypes;
import com.nutonmod.sound.ModSoundEvents;
import com.nutonmod.util.ModCustomTrades;
import com.nutonmod.util.ModLootTableModifiers;
import com.nutonmod.villager.Modvillagers;
import com.nutonmod.world.dimension.ModDimensions;
import com.nutonmod.world.dimension.ModPortals;
import com.nutonmod.world.feature.ModFeatures;
import com.nutonmod.world.system.EnergyRealmPressureSystem;
import com.nutonmod.world.system.EnergyRealmStormSystem;
import com.nutonmod.world.system.EnergyWardenAggroSystem;
import com.nutonmod.world.system.DimensionalTuneSystem;
import com.nutonmod.world.system.MobSpawnSystem;
import com.nutonmod.world.system.PlayerEnergySystem;
import com.nutonmod.world.system.SingularityArenaSystem;
import com.nutonmod.world.system.StormArbiterArenaSystem;
import com.nutonmod.world.system.StabilizerBeaconSystem;
import com.nutonmod.world.system.StormObeliskEventSystem;
import com.nutonmod.world.system.VoidArchonArenaSystem;
import com.nutonmod.world.system.VoidWingsSystem;
import com.nutonmod.world.system.WeaponEffectSystem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vazkii.patchouli.api.PatchouliAPI;

public class NutonMod implements ModInitializer {
    public static final String MOD_ID = "nutonmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final String GUIDE_TAG = "nutonmod_received_patchouli_guide";
    private static final String[] MANUAL_UNLOCK_ADVANCEMENTS = {
            "energy_realm/root",
            "energy_realm/enter_realm",
            "energy_realm/first_shard",
            "energy_realm/alloy_smith",
            "energy_realm/beacon_online",
            "energy_realm/stabilized",
            "energy_realm/core_heart",
            "energy_realm/sanctum_access",
            "energy_realm/sanctum_hunter",
            "energy_realm/storm_surge",
            "energy_realm/summon_singularity",
            "energy_realm/defeat_singularity",
            "energy_realm/defeat_storm_arbiter",
            "energy_realm/defeat_void_archon",
            "energy_realm/dimension_master",
            "energy_realm/storm_master",
            "energy_realm/singularity_master",
            "energy_realm/no_damage",
            "energy_realm/no_lightning_hit",
            "energy_realm/perfect_archon",
            "energy_realm/archon_slayer",
            "energy_realm/encounter_storm_arbiter",
            "energy_realm/encounter_void_archon",
            "energy_realm/warden_down"
    };
    private static ModConfig CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = ModConfig.load();
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
        PayloadTypeRegistry.playS2C().register(EnergySyncPayload.ID, EnergySyncPayload.CODEC);
        // Worldgen features are already declared in biome JSON/datagen.
        // Disable runtime biome injections to avoid feature order cycles.
        // ModWorldGeneration.generateModWorldGen();
        ModPortals.registerPortals();
        VoidWingsSystem.register();
        DimensionalTuneSystem.register();
        MobSpawnSystem.register();

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
            SingularityArenaSystem.tick(server);
            StormArbiterArenaSystem.tick(server);
            VoidArchonArenaSystem.tick(server);
            var energyRealmWorld = server.getWorld(ModDimensions.ENERGY_REALM_WORLD_KEY);
            if (energyRealmWorld != null) {
                StormObeliskEventSystem.tick(energyRealmWorld);
                StabilizerBeaconSystem.tick(energyRealmWorld);
                EnergyRealmStormSystem.tick(energyRealmWorld);
            }
            for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player == null || player.getWorld().isClient) {
                    continue;
                }
                EnergyRealmPressureSystem.applyPerTick(player);
                EnergyRealmStormSystem.applyPerTick(player);
            }
            PlayerEnergySystem.tick(server);
            WeaponEffectSystem.tick(server);
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            if (player == null || player.getWorld().isClient) {
                return;
            }

            if (!player.getCommandTags().contains(GUIDE_TAG)) {
                ItemStack guide = PatchouliAPI.get().getBookStack(Identifier.of(MOD_ID, "energy_realm_guide"));
                if (!guide.isEmpty()) {
                    player.giveItemStack(guide);
                    player.addCommandTag(GUIDE_TAG);
                }
            }

            if (CONFIG != null && CONFIG.manual_unlock_all) {
                unlockManualAdvancements(player);
            }
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient || !(world instanceof ServerWorld serverWorld)) {
                return ActionResult.PASS;
            }
            if (!world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.LIGHTNING_ROD)) {
                return ActionResult.PASS;
            }
            if (StormArbiterArenaSystem.tryUseLightningRod(serverWorld, hitResult.getBlockPos(), player)) {
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient) {
                return;
            }
            if (state.isOf(ModBlocks.ENERGY_ORE) || state.isOf(ModBlocks.DEEPSLATE_ENERGY_ORE)) {
                EnergyWardenAggroSystem.markPlayer(player);
            }
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient) {
                EnergyWardenAggroSystem.markPlayer(player);
            }
            return ActionResult.PASS;
        });

        StormArbiterDebugCommands.register();

        LOGGER.info("Hello Fabric world!");
    }

    private static void unlockManualAdvancements(ServerPlayerEntity player) {
        for (String path : MANUAL_UNLOCK_ADVANCEMENTS) {
            AdvancementEntry advancement = player.getServer().getAdvancementLoader().get(Identifier.of(MOD_ID, path));
            if (advancement == null) {
                continue;
            }
            var progress = player.getAdvancementTracker().getProgress(advancement);
            for (String criterion : progress.getUnobtainedCriteria()) {
                player.getAdvancementTracker().grantCriterion(advancement, criterion);
            }
        }
    }

    public static ModConfig getConfig() {
        if (CONFIG == null) {
            CONFIG = ModConfig.load();
        }
        return CONFIG;
    }
}
