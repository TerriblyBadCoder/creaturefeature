package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.renderers.models.TrichaelEntityModel;
import net.atired.creaturefeature.entity.TrichaelEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class TrichaelEntityRenderer extends MobRenderer<TrichaelEntity, TrichaelEntityModel<TrichaelEntity>> {
    private static final ResourceLocation TRICHAEL_LOCATION = CreatureFeature.getId("textures/entity/trichael.png");

    public TrichaelEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new TrichaelEntityModel<>(context.bakeLayer(TrichaelEntityModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new TrichaelLayer<>(this,context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(TrichaelEntity trichaelEntity) {
        return TRICHAEL_LOCATION;
    }

    @Override
    public void render(TrichaelEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
