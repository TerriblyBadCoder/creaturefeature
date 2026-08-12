package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFClientProxy;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.DetritusEntityModel;
import net.atired.creaturefeature.entity.DetritusEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class DetritusEntityRenderer extends MobRenderer<DetritusEntity, DetritusEntityModel<DetritusEntity>> {
    private static final ResourceLocation DETRITUS_LOCATION = CreatureFeature.getId("textures/entity/detritus.png");

    public DetritusEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new DetritusEntityModel<>(context.bakeLayer(DetritusEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(DetritusEntity machinationEntity) {
        return DETRITUS_LOCATION;
    }

    @Override
    public void render(DetritusEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(CFRenderTypes.MONOCHROME_SHADER_INSTANCE!=null){
            CFRenderTypes.MONOCHROME_SHADER_INSTANCE.safeGetUniform("DeSaturation").set(entity.getCopy()*(1.0f-Math.min(1.0f,entity.getUnburying()*4.0f)));
        }
        super.render(entity, entityYaw, partialTicks, poseStack, CFClientProxy.getDetritusSource(), packedLight);
        CFClientProxy.getDetritusSource().endBatch();
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
