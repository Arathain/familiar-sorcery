package net.arathain.familiarsorcery.entity;

import net.arathain.familiarsorcery.FamiliarSorcery;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class FamiliarEntities {
    public static EntityType<IcicleProjectile> ICICLE;
    public static EntityType<MagikBeamEntity> BEAM;
    public static void init() {
        ICICLE = register("icicle", FabricEntityTypeBuilder.<IcicleProjectile>create(SpawnGroup.MISC, IcicleProjectile::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).trackRangeBlocks(4).trackedUpdateRate(20).build());
        BEAM = register("beam", FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, MagikBeamEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());
    }

    private static <T extends Entity> EntityType<T> register(String s, EntityType<T> entityType) {
        return Registry.register(Registry.ENTITY_TYPE, FamiliarSorcery.MODID + ":" + s, entityType);
    }
}
