package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.CollumnEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CollumnEntityRenderer extends EntityRenderer<CollumnEntity> {
    private static final ResourceLocation OBELISK_LOCATION = CreatureFeature.getId("textures/block/obelisk.png");
    private static final ResourceLocation OBELISK_OUTER_LOCATION = CreatureFeature.getId("textures/block/obelisk_outside.png");
    public CollumnEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CollumnEntity collumnEntity) {
        return OBELISK_OUTER_LOCATION;
    }

    @Override
    public void render(CollumnEntity collumn, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float sin = Math.min(Mth.sin((collumn.tickCount+partialTick)/30.0f*3.14f)*4.0f,1.0f);
        super.render(collumn, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
}
