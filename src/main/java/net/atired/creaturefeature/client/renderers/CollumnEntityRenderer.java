package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.CollumnEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

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

        poseStack.pushPose();
        poseStack.translate(0,sin*5.0f-1.0f,0);
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(OBELISK_OUTER_LOCATION));
        for (int i = 0; i < 5; i++) {
            Vec3 vec3_1 = new Vec3(0.5,i,0).yRot(i/4.0f);
            Vec3 vec3_3 = new Vec3(0.5,i+1,0).yRot((i+1)/4.0f);

            for (int j = 1; j <= 4; j++) {
                Vec3 vec3_2 = new Vec3(0.5,i,0).yRot(j/2.0f*3.14f+i/4.0f);
                Vec3 vec3_4 = new Vec3(0.5,i+1,0).yRot(j/2.0f*3.14f+(i+1)/4.0f);
                vertex(pose,consumer,vec3_3.x,vec3_3.y,vec3_3.z,0,0,0,0, -1,packedLight,1.0f);
                vertex(pose,consumer,vec3_4.x,vec3_4.y,vec3_4.z,1,0,0,0, -1,packedLight,1.0f);
                vertex(pose,consumer,vec3_2.x,vec3_2.y,vec3_2.z,1,1,0,0, -1,packedLight,1.0f);
                vertex(pose,consumer,vec3_1.x,vec3_1.y,vec3_1.z,0,1,0,0, -1,packedLight,1.0f);
                vec3_1=vec3_2;
                vec3_3=vec3_4;
            }
        }
        consumer = bufferSource.getBuffer(RenderType.entityTranslucent(OBELISK_LOCATION));
        for (int i = 0; i < 5; i++) {
            Vec3 vec3_1 = new Vec3(0.495,i,0).yRot(i/4.0f);
            Vec3 vec3_3 = new Vec3(0.495,i+1,0).yRot((i+1)/4.0f);

            for (int j = 1; j <= 4; j++) {
                Vec3 vec3_2 = new Vec3(0.495,i,0).yRot(j/2.0f*3.14f+i/4.0f);
                Vec3 vec3_4 = new Vec3(0.495,i+1,0).yRot(j/2.0f*3.14f+(i+1)/4.0f);
                vertex(pose,consumer,vec3_3.x,vec3_3.y,vec3_3.z,0,0,0,0, -1,packedLight,1.0f);
                vertex(pose,consumer,vec3_4.x,vec3_4.y,vec3_4.z,1,0,0,0, -1,packedLight,1.0f);
                vertex(pose,consumer,vec3_2.x,vec3_2.y,vec3_2.z,1,1,0,0, -1,packedLight,1.0f);
                vertex(pose,consumer,vec3_1.x,vec3_1.y,vec3_1.z,0,1,0,0, -1,packedLight,1.0f);
                vec3_1=vec3_2;
                vec3_3=vec3_4;
            }
        }
        poseStack.popPose();
        super.render(collumn, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
}
