package com.nutonmod.compat;

import com.nutonmod.recipe.VoidResonanceRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.recipe.RecipeEntry;

import java.util.List;

public class VoidResonanceBoxDisplay extends BasicDisplay {
    public VoidResonanceBoxDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    public VoidResonanceBoxDisplay(RecipeEntry<VoidResonanceRecipe> recipe) {
        super(EntryIngredients.ofIngredients(List.of(recipe.value().input())), List.of(EntryIngredient.of(EntryStacks.of(recipe.value().getResult(null)))));
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return VoidResonanceBoxCategory.VOID_RESONANCE_BOX;
    }
}
