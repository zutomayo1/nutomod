package com.nutonmod.world.gen;

public class ModWorldGeneration {
    public static void generateModWorldGen() {
        ModTreeGeneration.addBiomeFeatures();
        ModFlowerGeneration.addBiomeFeatures();
        ModOreGeneration.addBiomeFeatures();
    }
}
