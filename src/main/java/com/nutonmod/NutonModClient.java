package com.nutonmod;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.block.ModFluids;
import com.nutonmod.block.entity.ModBlockEntities;
import com.nutonmod.client.StormWarningClientSystem;
import com.nutonmod.client.render.BoxBlockEntityRenderer;
import com.nutonmod.client.render.HatArmorRenderer;
import com.nutonmod.entity.EnergyBeingRenderer;
import com.nutonmod.entity.ModEntities;
import com.nutonmod.entity.RiftStalkerRenderer;
import com.nutonmod.entity.SingularityRenderer;
import com.nutonmod.entity.StormArbiterRenderer;
import com.nutonmod.item.ModItems;
import com.nutonmod.screen.ModScreenHandlers;
import com.nutonmod.screen.PolishingMachineScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class NutonModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.ENERGY_BEING, EnergyBeingRenderer::new);
        EntityRendererRegistry.register(ModEntities.RIFT_STALKER, RiftStalkerRenderer::new);
        EntityRendererRegistry.register(ModEntities.SINGULARITY, SingularityRenderer::new);
        EntityRendererRegistry.register(ModEntities.STORM_ARBITER, StormArbiterRenderer::new);

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
        ClientTickEvents.END_CLIENT_TICK.register(StormWarningClientSystem::tick);
        HudRenderCallback.EVENT.register((drawContext, renderTickCounter) ->
                StormWarningClientSystem.render(drawContext, 0.0F));
    }
}
