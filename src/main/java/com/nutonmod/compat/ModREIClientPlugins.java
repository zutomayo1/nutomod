package com.nutonmod.compat;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.recipe.PolishingMachineRecipe;
import com.nutonmod.recipe.VoidResonanceRecipe;
import com.nutonmod.screen.DimensionalTunerScreen;
import com.nutonmod.screen.EnergyDisintegratorScreen;
import com.nutonmod.screen.PolishingMachineScreen;
import com.nutonmod.screen.VoidResonanceBoxScreen;
import com.nutonmod.world.system.EnergyDisintegratorBossRefiningData;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;

public class ModREIClientPlugins implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new PolishingMachineCategory());
        registry.add(new DimensionalTunerCategory());
        registry.add(new EnergyDisintegratorCategory());
        registry.add(new VoidResonanceBoxCategory());

        registry.addWorkstations(PolishingMachineCategory.POLISHING_MACHINE, EntryStacks.of(ModBlocks.POLISHING_MACHINE));
        registry.addWorkstations(DimensionalTunerCategory.DIMENSIONAL_TUNER, EntryStacks.of(ModBlocks.DIMENSIONAL_TUNER));
        registry.addWorkstations(EnergyDisintegratorCategory.ENERGY_DISINTEGRATOR, EntryStacks.of(ModBlocks.ENERGY_DISINTEGRATOR));
        registry.addWorkstations(VoidResonanceBoxCategory.VOID_RESONANCE_BOX, EntryStacks.of(ModBlocks.VOID_RESONANCE_BOX));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(PolishingMachineRecipe.class, PolishingMachineRecipe.Type.INSTANCE, PolishingMachineDisplay::new);
        registry.registerRecipeFiller(VoidResonanceRecipe.class, VoidResonanceRecipe.Type.INSTANCE, VoidResonanceBoxDisplay::new);

        for (DimensionalTunerDisplay display : DimensionalTunerDisplay.all()) {
            registry.add(display);
        }

        // Existing bridge for crafting-based energy disintegrator displays.
        registry.registerRecipesFiller(CraftingRecipe.class, RecipeType.CRAFTING, EnergyDisintegratorDisplay::fromCraftingRecipe);

        var resourceManager = MinecraftClient.getInstance() != null ? MinecraftClient.getInstance().getResourceManager() : null;
        for (var entry : EnergyDisintegratorBossRefiningData.getAllProfiles(resourceManager).entrySet()) {
            registry.add(EnergyDisintegratorDisplay.fromBossProfile(entry.getKey(), entry.getValue()));
        }
        registry.add(EnergyDisintegratorDisplay.essence());
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(75, 30, 20, 30), PolishingMachineScreen.class, PolishingMachineCategory.POLISHING_MACHINE);
        registry.registerClickArea(screen -> new Rectangle(48, 45, 24, 12), DimensionalTunerScreen.class, DimensionalTunerCategory.DIMENSIONAL_TUNER);
        registry.registerClickArea(screen -> new Rectangle(48, 35, 24, 12), EnergyDisintegratorScreen.class, EnergyDisintegratorCategory.ENERGY_DISINTEGRATOR);
        registry.registerClickArea(screen -> new Rectangle(18, 18, 84, 62), VoidResonanceBoxScreen.class, VoidResonanceBoxCategory.VOID_RESONANCE_BOX);
    }
}
