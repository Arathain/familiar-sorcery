package net.arathain.familiarsorcery;

import com.google.common.reflect.Reflection;
import net.arathain.familiarsorcery.block.FamiliarSorceryBlocks;
import net.arathain.familiarsorcery.enchantment.FamiliarSorceryEnchants;
import net.arathain.familiarsorcery.entity.FamiliarEntities;
import net.arathain.familiarsorcery.item.FamiliarSorceryItems;
import net.arathain.familiarsorcery.worldgen.FamiliarBiomes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;


public class FamiliarSorcery implements ModInitializer {
	public static final String MODID = "familiarsorcery";

	@Override
	public void onInitialize() {
		FamiliarSorceryItems.init();
		FamiliarSorceryEnchants.init();
		FamiliarSorceryBlocks.init();
		FamiliarSorceryBlocks.registerCutouts();
		FamiliarEntities.init();
		Reflection.initialize(FamiliarBiomes.class);
		TheEndBiomes.addHighlandsBiome(FamiliarBiomes.CRYSTAL_GROVE, 0.8);
	}

}
