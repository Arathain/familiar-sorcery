package net.arathain.familiarsorcery.entity;


import net.arathain.familiarsorcery.FamiliarSorcery;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class PartOfYouRenderer extends MobEntityRenderer<PartOfYou, PartOfYouModel<PartOfYou>> {
    public static final Identifier TEXTURE = new Identifier(FamiliarSorcery.MODID, "textures/entity/partofyou.png");

    public PartOfYouRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new PartOfYouModel<>(), 0.5f);
        addFeature(new FeatureRenderer<PartOfYou, PartOfYouModel<PartOfYou>>(this) {
            @Override
            public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PartOfYou entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
                getContextModel().render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
            }
        });
    }

    public Identifier getTexture(PartOfYou partOfYou) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLight(PartOfYou entity, BlockPos blockPos) {
        return 15;
    }

}
