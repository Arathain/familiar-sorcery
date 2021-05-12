package net.arathain.familiarsorcery;

import net.arathain.familiarsorcery.entity.FamiliarEntities;
import net.arathain.familiarsorcery.entity.IcicleRenderer;
import net.arathain.familiarsorcery.entity.MagikBeamEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class FamiliarSorceryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.INSTANCE.register(FamiliarEntities.BEAM, (dispatcher, context) -> new MagikBeamEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(FamiliarEntities.ICICLE, (dispatcher, context) -> new IcicleRenderer(dispatcher));

    }
}