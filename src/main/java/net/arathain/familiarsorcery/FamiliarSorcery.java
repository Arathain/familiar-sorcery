package net.arathain.familiarsorcery;

import net.arathain.familiarsorcery.enchantment.FamiliarSorceryEnchants;
import net.arathain.familiarsorcery.entity.FamiliarEntities;
import net.arathain.familiarsorcery.item.FamiliarSorceryItems;
import net.fabricmc.api.ModInitializer;


public class FamiliarSorcery implements ModInitializer {
	public static final String MODID = "familiarsorcery";

	@Override
	public void onInitialize() {
		FamiliarSorceryItems.init();
		FamiliarSorceryEnchants.init();
		FamiliarEntities.init();
	}

}
