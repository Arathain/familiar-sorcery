package net.arathain.familiarsorcery.worldgen;

import net.arathain.familiarsorcery.FamiliarSorcery;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

import java.util.HashMap;
import java.util.Map;

public class FamiliarBiomes {

    public static final RegistryKey<Biome> CRYSTAL_GROVE = create("crystal_grove", createCrystalGrove());

    protected static Biome createCrystalGrove() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addEndMobs(spawnSettings);

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder().surfaceBuilder(ConfiguredSurfaceBuilders.END);
        generationSettings.structureFeature(ConfiguredStructureFeatures.END_CITY);
        DefaultBiomeFeatures.addDefaultUndergroundStructures(generationSettings);
        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addDefaultDisks(generationSettings);
        FamiliarBiomeFeatures.addLivingwoodTrees(generationSettings);


        return new Biome.Builder().precipitation(Biome.Precipitation.NONE).category(Biome.Category.THEEND).depth(0.1F).scale(0.1F).temperature(0.6F).downfall(0.6F).effects(new BiomeEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(12638463).skyColor(getSkyColor(0.0F)).moodSound(BiomeMoodSound.CAVE).build()).spawnSettings(spawnSettings.build()).generationSettings(generationSettings.build()).build();
    }
    private static int getSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = MathHelper.clamp(f, -1.0F, 1.0F);
        return MathHelper.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }

    private static RegistryKey<Biome> create(String id, Biome biome) {
        Identifier identifier = new Identifier(FamiliarSorcery.MODID, id);
        BuiltinRegistries.add(BuiltinRegistries.BIOME, identifier, biome);

        return FamiliarBiomes.getKey(identifier);
    }
    private static RegistryKey<Biome> getKey(Identifier identifier) {
        return RegistryKey.of(Registry.BIOME_KEY, identifier);
    }
}
