package com.nutonmod.screen;

import com.nutonmod.NutonMod;
import com.nutonmod.data.DimensionalTunerData;
import com.nutonmod.data.PolishingMachineData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;


public class ModScreenHandlers {
    public static final ScreenHandlerType<PolishingMachineScreenHandler> POLISHING_MACHINE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(NutonMod.MOD_ID, "polishing_machine"),
                    new ExtendedScreenHandlerType<>(PolishingMachineScreenHandler::new, PolishingMachineData.CODEC));
    public static final ScreenHandlerType<DimensionalTunerScreenHandler> DIMENSIONAL_TUNER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(NutonMod.MOD_ID, "dimensional_tuner"),
                    new ExtendedScreenHandlerType<>(DimensionalTunerScreenHandler::new, DimensionalTunerData.CODEC));
    public static void registerScreenHandlers() {

    }
}
