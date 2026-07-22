package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.renderers.models.SaintSolisEntityModel;
import net.atired.creaturefeature.entity.SaintSolisEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class SaintSolisEntityRenderer extends MobRenderer<SaintSolisEntity, SaintSolisEntityModel<SaintSolisEntity>> {
    private static final ResourceLocation SAINT_SOLIS_LOCATION = CreatureFeature.getId("textures/entity/saint_solis.png");

    public SaintSolisEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SaintSolisEntityModel<>(context.bakeLayer(SaintSolisEntityModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(SaintSolisEntity machinationEntity) {
        return SAINT_SOLIS_LOCATION;
    }

    @Override
    public void render(SaintSolisEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
