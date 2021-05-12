package net.arathain.familiarsorcery;

import net.arathain.familiarsorcery.entity.FamiliarEntities;
import net.arathain.familiarsorcery.entity.IcicleRenderer;
import net.arathain.familiarsorcery.entity.MagikBeamEntityRenderer;
import net.arathain.familiarsorcery.pain.EntityDispatcher;
import net.arathain.familiarsorcery.pain.UnfamiliarPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

@Environment(EnvType.CLIENT)
public class FamiliarSorceryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(UnfamiliarPackets.SPAWN, EntityDispatcher::spawnFrom);

        EntityRendererRegistry.INSTANCE.register(FamiliarEntities.BEAM, (manager, context) -> new MagikBeamEntityRenderer(manager));
        EntityRendererRegistry.INSTANCE.register(FamiliarEntities.ICICLE, (manager, context) -> new IcicleRenderer(manager));

    }
}