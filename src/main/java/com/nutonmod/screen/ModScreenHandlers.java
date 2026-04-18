package com.nutonmod.screen;

import com.nutonmod.NutonMod;
import com.nutonmod.data.DimensionalTunerData;
import com.nutonmod.data.EnergyDisintegratorData;
import com.nutonmod.data.PolishingMachineData;
import com.nutonmod.data.StabilizerBeaconData;
import com.nutonmod.data.VoidResonanceBoxData;
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
    public static final ScreenHandlerType<EnergyDisintegratorScreenHandler> ENERGY_DISINTEGRATOR_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(NutonMod.MOD_ID, "energy_disintegrator"),
                    new ExtendedScreenHandlerType<>(EnergyDisintegratorScreenHandler::new, EnergyDisintegratorData.CODEC));
    public static final ScreenHandlerType<VoidResonanceBoxScreenHandler> VOID_RESONANCE_BOX_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(NutonMod.MOD_ID, "void_resonance_box"),
                    new ExtendedScreenHandlerType<>(VoidResonanceBoxScreenHandler::new, VoidResonanceBoxData.CODEC));
    public static final ScreenHandlerType<StabilizerBeaconScreenHandler> STABILIZER_BEACON_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(NutonMod.MOD_ID, "stabilizer_beacon"),
                    new ExtendedScreenHandlerType<>(StabilizerBeaconScreenHandler::new, StabilizerBeaconData.CODEC));
    public static void registerScreenHandlers() {

    }
}
