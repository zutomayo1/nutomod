package com.nutonmod;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.ModFluids;
import com.nutonmod.block.entity.ModBlockEntities;
import com.nutonmod.client.EnergyHudClientSystem;
import com.nutonmod.client.StormWarningClientSystem;
import com.nutonmod.client.DimensionalTuneTooltipClient;
import com.nutonmod.client.render.BoxBlockEntityRenderer;
import com.nutonmod.client.render.EndJudicatorSlashRenderer;
import com.nutonmod.client.render.HatArmorRenderer;
import com.nutonmod.client.render.ThunderSpearProjectileRenderer;
import com.nutonmod.client.render.VoidWingsFeatureRenderer;
import com.nutonmod.entity.EnergyBeingRenderer;
import com.nutonmod.entity.EnergySpriteRenderer;
import com.nutonmod.entity.EnergyWardenRenderer;
import com.nutonmod.entity.ModEntities;
import com.nutonmod.entity.RiftStalkerRenderer;
import com.nutonmod.entity.SingularityRenderer;
import com.nutonmod.entity.CrystalSnailRenderer;
import com.nutonmod.entity.StormArbiterRenderer;
import com.nutonmod.entity.StormElementalRenderer;
import com.nutonmod.entity.StormFinchRenderer;
import com.nutonmod.entity.VoidCrawlerRenderer;
import com.nutonmod.entity.VoidArchonRenderer;
import com.nutonmod.item.ModItems;
import com.nutonmod.network.EnergySyncPayload;
import com.nutonmod.screen.ModScreenHandlers;
import com.nutonmod.screen.DimensionalTunerScreen;
import com.nutonmod.screen.PolishingMachineScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class NutonModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(EnergySyncPayload.ID, (payload, context) ->
                context.client().execute(() ->
                        EnergyHudClientSystem.updateFromServer(payload.energy(), payload.maxEnergy(), payload.overloaded())));

        EntityRendererRegistry.register(ModEntities.ENERGY_BEING, EnergyBeingRenderer::new);
        EntityRendererRegistry.register(ModEntities.RIFT_STALKER, RiftStalkerRenderer::new);
        EntityRendererRegistry.register(ModEntities.SINGULARITY, SingularityRenderer::new);
        EntityRendererRegistry.register(ModEntities.STORM_ARBITER, StormArbiterRenderer::new);
        EntityRendererRegistry.register(ModEntities.VOID_ARCHON, VoidArchonRenderer::new);
        EntityRendererRegistry.register(ModEntities.VOID_PIERCING_ARROW, ArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.THUNDER_SPEAR_PROJECTILE, ThunderSpearProjectileRenderer::new);
        EntityRendererRegistry.register(ModEntities.END_JUDICATOR_SLASH, EndJudicatorSlashRenderer::new);
        EntityRendererRegistry.register(ModEntities.ENERGY_SPRITE, EnergySpriteRenderer::new);
        EntityRendererRegistry.register(ModEntities.CRYSTAL_SNAIL, CrystalSnailRenderer::new);
        EntityRendererRegistry.register(ModEntities.STORM_FINCH, StormFinchRenderer::new);
        EntityRendererRegistry.register(ModEntities.ENERGY_WARDEN, EnergyWardenRenderer::new);
        EntityRendererRegistry.register(ModEntities.VOID_CRAWLER, VoidCrawlerRenderer::new);
        EntityRendererRegistry.register(ModEntities.STORM_ELEMENTAL, StormElementalRenderer::new);
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityRenderer instanceof PlayerEntityRenderer playerRenderer) {
                registrationHelper.register(new VoidWingsFeatureRenderer<>(playerRenderer, context.getModelLoader()));
            }
        });

        BlockEntityRendererFactories.register(ModBlockEntities.BOX, BoxBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORN_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENERGY_POTATO_CROP, RenderLayer.getCutout());
        ArmorRenderer.register(new HatArmorRenderer(), ModItems.HAT);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENERGY_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ENERGY_FLOWER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POTTED_ENERGY_FLOWER, RenderLayer.getCutout());

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_ENERGY, ModFluids.FLOWING_ENERGY,
                new SimpleFluidRenderHandler(
                        Identifier.of(NutonMod.MOD_ID, "block/energy_fluid_still"),
                        Identifier.of(NutonMod.MOD_ID, "block/energy_fluid_flow"),
                        0xFFFFFFFF
                ));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ModFluids.STILL_ENERGY, ModFluids.FLOWING_ENERGY);
        HandledScreens.register(ModScreenHandlers.POLISHING_MACHINE_SCREEN_HANDLER, PolishingMachineScreen::new);
        HandledScreens.register(ModScreenHandlers.DIMENSIONAL_TUNER_SCREEN_HANDLER, DimensionalTunerScreen::new);
        DimensionalTuneTooltipClient.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            StormWarningClientSystem.tick(client);
            EnergyHudClientSystem.tick(client);
        });
        HudRenderCallback.EVENT.register((drawContext, renderTickCounter) -> {
            StormWarningClientSystem.render(drawContext, 0.0F);
            EnergyHudClientSystem.render(drawContext, 0.0F);
        });
    }
}
