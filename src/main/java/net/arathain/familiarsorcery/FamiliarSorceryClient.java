package net.arathain.familiarsorcery;

import net.arathain.familiarsorcery.entity.MagikBeamEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class FamiliarSorceryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.INSTANCE.register(FamiliarSorcery.BEAM, (dispatcher, context) -> {
            return new MagikBeamEntityRenderer(dispatcher);
        });
    }
}