package com.nutonmod.compat;

import com.nutonmod.recipe.PolishingMachineRecipe;
import com.nutonmod.screen.PolishingMachineScreen;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;

import java.awt.image.renderable.RenderedImageFactory;
import java.time.temporal.ValueRange;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PolishingMachineDisplay extends BasicDisplay {
    public PolishingMachineDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }
    public PolishingMachineDisplay(RecipeEntry<PolishingMachineRecipe> recipe){
        super(getInputList(recipe.value()), List.of(EntryIngredient.of(EntryStacks.of(recipe.value().getResult(null)))));
    }




    private static List<EntryIngredient> getInputList(PolishingMachineRecipe value){
        if(value.getIngredients().isEmpty()){
            return Collections.emptyList();
        }
        return EntryIngredients.ofIngredients(value.getIngredients());
    }
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PolishingMachineCategory.POLISHING_MACHINE;
    }
}
