package com.nutonmod.world.tree;

import com.nutonmod.NutonMod;
import com.nutonmod.world.ModConfiguredFeatures;
import net.minecraft.block.SaplingGenerator;

import java.util.Optional;

public class ModTreeGenerator {
    public static final SaplingGenerator ENERGY_TREE = new SaplingGenerator(
            NutonMod.MOD_ID + ":energy_tree",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.ENERGY_TREE_KEY),
            Optional.empty()
    );
}
