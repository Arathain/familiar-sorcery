// Made with Blockbench 3.8.4
	// Exported for Minecraft version 1.15
	// Paste this class into your mod and generate all required imports

package net.arathain.familiarsorcery.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class PartOfYouModel<T extends PartOfYou> extends EntityModel<T> {
	private final ModelPart bone;
	private final ModelPart arm_r1;
	private final ModelPart arm_r2;
	public PartOfYouModel() {
		textureWidth = 32;
		textureHeight = 32;
		bone = new ModelPart(this);
		bone.setPivot(-4.0F, 21.0F, 0.0F);
		bone.setTextureOffset(0, 0).addCuboid(0.0F, -5.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		arm_r1 = new ModelPart(this);
		arm_r1.setPivot(8.0F, 0.0F, 0.0F);
		bone.addChild(arm_r1);
		setRotationAngle(arm_r1, 0.0F, 0.0F, -0.1745F);
		arm_r1.setTextureOffset(0, 16).addCuboid(0.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		arm_r2 = new ModelPart(this);
		arm_r2.setPivot(0.0F, 0.0F, 0.0F);
		bone.addChild(arm_r2);
		setRotationAngle(arm_r2, 0.0F, 0.0F, 0.1745F);
		arm_r2.setTextureOffset(0, 16).addCuboid(-1.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
}
	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		//bazinga
	}
@Override
public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		
		bone.render(matrixStack, buffer, packedLight, packedOverlay);
}
public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
}

}