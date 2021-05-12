package net.arathain.familiarsorcery.worldgen;

import com.google.common.collect.ImmutableList;
import net.arathain.familiarsorcery.FamiliarSorcery;
import net.arathain.familiarsorcery.block.FamiliarSorceryBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.OptionalInt;

public class FamiliarConfiguredFeatures {
    public static final ConfiguredFeature<TreeFeatureConfig, ?> LIVINGTREE = register(
            "livingwood_trees",
            Feature.TREE.configure( new TreeFeatureConfig.Builder
                    (
                            new SimpleBlockStateProvider(BlockStates.LIVINGWOOD_LOG),
                            new SimpleBlockStateProvider(BlockStates.LIVINGWOOD_LEAVES),
                            new LargeOakFoliagePlacer(UniformIntDistribution.of(2), UniformIntDistribution.of(4), 4),
                            new LargeOakTrunkPlacer(3, 11, 0),
                            new TwoLayersFeatureSize(6, 0, 6))
                    .ignoreVines()
                    .build()));

    public static final ConfiguredFeature<?, ?> LIVINGTREES = register(
            "trees_livingtree",
            Feature.RANDOM_SELECTOR.configure(
                    new RandomFeatureConfig(
                            ImmutableList.of(LIVINGTREE.withChance(0.0F)),
                            LIVINGTREE
                    )
            )
                    .decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP)
                    .decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(20, 0.1F, 1)))
    );

    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(FamiliarSorcery.MODID, id), configuredFeature);
    }


    private static class BlockStates {
        private static final BlockState LIVINGWOOD_LOG = FamiliarSorceryBlocks.LIVINGWOOD_LOG.getDefaultState();
        private static final BlockState LIVINGWOOD_LEAVES = FamiliarSorceryBlocks.LIVINGLEAF.getDefaultState();
    }

}
