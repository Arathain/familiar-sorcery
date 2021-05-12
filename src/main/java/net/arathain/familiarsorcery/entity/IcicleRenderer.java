package net.arathain.familiarsorcery.entity;

import net.arathain.familiarsorcery.FamiliarSorcery;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class IcicleRenderer extends ProjectileEntityRenderer<IcicleProjectile> {

    public static final Identifier TEXTURE = new Identifier(FamiliarSorcery.MODID, "textures/entity/projectile/icicle.png");

    public IcicleRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    public Identifier getTexture(IcicleProjectile icicleProjectile) {
            return TEXTURE;
    }
}

