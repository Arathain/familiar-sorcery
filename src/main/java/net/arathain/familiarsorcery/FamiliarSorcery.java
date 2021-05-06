package net.arathain.familiarsorcery;

import net.arathain.familiarsorcery.block.FamiliarSorceryBlocks;
import net.arathain.familiarsorcery.enchantment.FamiliarSorceryEnchants;
import net.arathain.familiarsorcery.entity.MagikBeamEntity;
import net.arathain.familiarsorcery.item.FamiliarSorceryItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class FamiliarSorcery implements ModInitializer {
	public static final String MODID = "familiarsorcery";
	public static final EntityType<MagikBeamEntity> BEAM = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier(MODID, "magic_beam"),
			FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, MagikBeamEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
	);
	@Override
	public void onInitialize() {
		FamiliarSorceryItems.init();
		FamiliarSorceryEnchants.init();
		FamiliarSorceryBlocks.init();
		FamiliarSorceryBlocks.registerCutouts();
	}

}
