package net.arathain.familiarsorcery.worldgen;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;

import java.util.LinkedHashMap;
import java.util.Map;

public class FamiliarBiomeFeatures {
    //TODO this still doesn't work
    public static void addLivingwoodTrees(GenerationSettings.Builder builder) {
        builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, FamiliarConfiguredFeatures.LIVINGTREES);
    }
}
