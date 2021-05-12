package net.arathain.familiarsorcery.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MagikBeamEntityRenderer extends EntityRenderer<MagikBeamEntity> {
    public static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");
    public static final DyeColor BEAM_COLOR = DyeColor.RED;

    public MagikBeamEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);

    } 

    @Override
    public Identifier getTexture(MagikBeamEntity entity) {
        return null;
    }

    @Override
    public void render(MagikBeamEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        long time = entity.getWorld().getTime();
        BeaconBlockEntityRenderer.renderLightBeam(matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0f, time, 0, 256, BEAM_COLOR.getColorComponents(), 0.25F, 0.35F);
        BeaconBlockEntityRenderer.renderLightBeam(matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0f, time, 0, -256, BEAM_COLOR.getColorComponents(), 0.25F, 0.35F);

    }


}
