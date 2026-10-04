package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.PowderSkullEntityModel;
import net.atired.creaturefeature.entity.PowderSkullEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class PowderSkullEntityRenderer extends MobRenderer<PowderSkullEntity, PowderSkullEntityModel<PowderSkullEntity>> {
    private static final ResourceLocation POWDERSKULL_LOCATION = CreatureFeature.getId("textures/entity/powder_skull.png");
    private static final ResourceLocation RED_POWDER_BG_LOCATION = CreatureFeature.getId("textures/entity/red_powder_bg.png");

    public PowderSkullEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new PowderSkullEntityModel<>(context.bakeLayer(PowderSkullEntityModel.LAYER_LOCATION)), 1.5f);
    }

    @Override
    public void render(PowderSkullEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.tickCount>0){
            poseStack.pushPose();

            poseStack.mulPose(new Quaternionf()

                    .rotationY(-entityYaw/180.0f*3.14f)
                    .rotationX(entity.getViewXRot(partialTicks)/180.0f*3.14f)
            );
            poseStack.translate(0,0.8,-1.2);
            PoseStack.Pose pose = poseStack.last();

            VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityRedCull(RED_POWDER_BG_LOCATION));
            vertex(pose,consumer,-1.5,1.5,0,0,0,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,1.5,1.5,0,1,0,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,0,0,1.5,0.5f,0.5f,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,0,0,1.5,0.5f,0.5f,0,0,1,packedLight,1.0f);

            vertex(pose,consumer,-1.5,1.5,0,0,0,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,0,0,1.5,0.5f,0.5f,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,0,0,1.5,0.5f,0.5f,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,-1.5,-1.5,0,0,1,0,0,1,packedLight,1.0f);

            vertex(pose,consumer,0,0,1.5,0.5f,0.5f,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,0,0,1.5,0.5f,0.5f,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,1.5,-1.5,0,1,1,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,-1.5,-1.5,0,0,1,0,0,1,packedLight,1.0f);

            vertex(pose,consumer,0,0,1.5,0.5f,0.5f,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,1.5,1.5,0,1,0,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,1.5,-1.5,0,1,1,0,0,1,packedLight,1.0f);
            vertex(pose,consumer,0,0,1.5,0.5f,0.5f,0,0,1,packedLight,1.0f);
            poseStack.popPose();
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PowderSkullEntity machinationEntity) {
        return POWDERSKULL_LOCATION;
    }


    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight,float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
