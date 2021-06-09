package net.arathain.familiarsorcery.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.arathain.familiarsorcery.FamiliarSorcery.MODID;

public class FamiliarEntities {
    public static EntityType<IcicleProjectile> ICICLE;
    public static EntityType<MagikBeamEntity> BEAM;
    public static EntityType<PartOfYou> PART_OF_YOU;

    public static void init() {
        ICICLE = register("icicle", FabricEntityTypeBuilder.<IcicleProjectile>create(SpawnGroup.MISC, IcicleProjectile::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).trackRangeBlocks(4).trackedUpdateRate(20).fireImmune().build());
        BEAM = register("beam", FabricEntityTypeBuilder.create(SpawnGroup.MISC, MagikBeamEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());
        PART_OF_YOU = createEntity("partofyou", PartOfYou.createAttributes(), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, PartOfYou::new).dimensions(EntityDimensions.fixed(0.6f, 0.6f)).build());

    }

    private static <T extends Entity> EntityType<T> register(String s, EntityType<T> entityType) {
        return Registry.register(Registry.ENTITY_TYPE, MODID + ":" + s, entityType);
    }
    private static <T extends LivingEntity> EntityType<T> createEntity(String name, DefaultAttributeContainer.Builder attributes, EntityType<T> type) {
        FabricDefaultAttributeRegistry.register(type, attributes);

        return Registry.register(Registry.ENTITY_TYPE, new Identifier(MODID, name), type);
    }
}
