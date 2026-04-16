package com.nutonmod.recipe;

import com.nutonmod.NutonMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeTypes {
        public static void registerRecipeTypes() {
            Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(NutonMod.MOD_ID, PolishingMachineRecipe.Serializer.ID),
                    PolishingMachineRecipe.Serializer.INSTANCE);
            Registry.register(Registries.RECIPE_TYPE, Identifier.of(NutonMod.MOD_ID, PolishingMachineRecipe.Type.ID),
                    PolishingMachineRecipe.Type.INSTANCE);
            Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(NutonMod.MOD_ID, VoidResonanceRecipe.Serializer.ID),
                    VoidResonanceRecipe.Serializer.INSTANCE);
            Registry.register(Registries.RECIPE_TYPE, Identifier.of(NutonMod.MOD_ID, VoidResonanceRecipe.Type.ID),
                    VoidResonanceRecipe.Type.INSTANCE);

        }
}
