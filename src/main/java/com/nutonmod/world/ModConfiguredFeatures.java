package com.nutonmod.world;

import com.nutonmod.NutonMod;
import com.nutonmod.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> ENERGY_TREE_KEY = of("energy_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ENERGY_FLOWER_KEY = of("energy_flower");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ENERGY_ORE_KEY = of("energy_ore");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
        register(featureRegisterable, ENERGY_TREE_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(ModBlocks.ENERGY_LOG),
                new StraightTrunkPlacer(4, 3, 2),
                BlockStateProvider.of(ModBlocks.ENERGY_LEAVES),
                new BlobFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(2), 2),
                new TwoLayersFeatureSize(1, 0, 2)
        ).build());

        register(
                featureRegisterable,
                ENERGY_FLOWER_KEY,
                Feature.SIMPLE_BLOCK,
                new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.ENERGY_FLOWER))
        );

        register(
                featureRegisterable,
                ENERGY_ORE_KEY,
                Feature.ORE,
                new OreFeatureConfig(
                        List.of(
                                OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES), ModBlocks.ENERGY_ORE.getDefaultState()),
                                OreFeatureConfig.createTarget(new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), ModBlocks.DEEPSLATE_ENERGY_ORE.getDefaultState())
                        ),
                        9
                )
        );
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> of(String id) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(NutonMod.MOD_ID, id));
    }

    public static <FC extends FeatureConfig, F extends Feature<FC>> void register(
            Registerable<ConfiguredFeature<?, ?>> registerable,
            RegistryKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC config
    ) {
        registerable.register(key, new ConfiguredFeature<>(feature, config));
    }
}
