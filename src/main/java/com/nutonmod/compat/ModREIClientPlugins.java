package com.nutonmod.compat;

import com.nutonmod.block.ModBlocks;
import com.nutonmod.recipe.PolishingMachineRecipe;
import com.nutonmod.screen.PolishingMachineScreen;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class ModREIClientPlugins implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new PolishingMachineCategory());
        registry.addWorkstations(PolishingMachineCategory.POLISHING_MACHINE, EntryStacks.of(ModBlocks.POLISHING_MACHINE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(PolishingMachineRecipe.class, PolishingMachineRecipe.Type.INSTANCE, PolishingMachineDisplay::new);
    }
    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerClickArea(screen -> new Rectangle(75,30,20,30), PolishingMachineScreen.class,
                PolishingMachineCategory.POLISHING_MACHINE);
    }
}
