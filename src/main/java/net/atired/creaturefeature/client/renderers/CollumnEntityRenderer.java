package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
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
        float pi = (float)Math.PI;
        float sin = Math.min(Mth.sin((collumn.tickCount+partialTick)/80.0f*pi)*1.01f,1.0f);
        sin=(float)Math.pow(sin,0.5f);
        sin-=1.1f;
        float sin2 = Math.clamp(Mth.sin(Math.max(40.0f,(collumn.tickCount+partialTick))/80.0f*pi*1.4f),0.0f,1.0f);

        poseStack.pushPose();
        poseStack.scale(Math.min(sin+1.0f,0.5f)*2.0f,1.0f,Math.min(sin+1.0f,0.5f)*2.0f);
        poseStack.translate(0,sin*5.0f-8.0f,0);
        sin+=1.1f;
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = bufferSource.getBuffer(CFRenderTypes.entityCollumnCutout(OBELISK_OUTER_LOCATION));
        float a = 1.0f;
        float aged = (collumn.tickCount+partialTick)/18.0f;
        for (int i = 0; i < 12; i++) {
            Vec3 vec3_1 = new Vec3(0.7,i,0).yRot(i/4.0f*(1.0f+sin*4.0f)+entityYaw);
            float yaw = (i + 1) / 4.0f * (1.0f + sin * 4.0f) + entityYaw;
            Vec3 vec3_3 = new Vec3(0.7,i+1,0).yRot(yaw);
            float a2 = Mth.lerp(1.0f-(i+1)/12.0f,sin2,1.0f);
            for (int j = 1; j <= 4; j++) {

                Vec3 vec3_2 = new Vec3(0.7,i,0).yRot(j/2.0f*pi+i/4.0f*(1.0f+sin*4.0f)+entityYaw);
                Vec3 vec3_4 = new Vec3(0.7,i+1,0).yRot(j/2.0f*pi+(i+1)/4.0f*(1.0f+sin*4.0f)+entityYaw);
                vertex(pose,consumer,vec3_3.x,vec3_3.y,vec3_3.z,j,i,0,1,0,packedLight+i*3-4-12,a2);
                vertex(pose,consumer,vec3_4.x,vec3_4.y,vec3_4.z,j+1,i,0,1,0,packedLight+i*3-4-12,a2);
                vertex(pose,consumer,vec3_2.x,vec3_2.y,vec3_2.z,j+1,i+1,0,1,0,packedLight+i*3-4-12,a);
                vertex(pose,consumer,vec3_1.x,vec3_1.y,vec3_1.z,j,i+1,0,1,0,packedLight+i*3-4-12,a);
                vec3_1=vec3_2;
                vec3_3=vec3_4;
            }
            a=a2;
            if(i==11) {
                Vec3 vec3_top1 = new Vec3(0.7, i + 1, 0).yRot(yaw);
                Vec3 vec3_top2 = new Vec3(0.7, i + 1, 0).yRot(pi * 0.5f + yaw);
                Vec3 vec3_top3 = new Vec3(0.7, i + 1, 0).yRot(pi * 1.0f + yaw);
                Vec3 vec3_top4 = new Vec3(0.7, i + 1, 0).yRot(pi * 1.5f + yaw);
                vertex(pose, consumer, vec3_top1.x, vec3_top1.y, vec3_top1.z, 0, 0, 0,0,1, packedLight+i*3-4-12, a2);
                vertex(pose, consumer, vec3_top2.x, vec3_top2.y, vec3_top2.z, 1, 0, 0,0,1, packedLight+i*3-4-12, a2);
                vertex(pose, consumer, vec3_top3.x, vec3_top3.y, vec3_top3.z, 1, 1, 0,0,1, packedLight+i*3-4-12, a);
                vertex(pose, consumer, vec3_top4.x, vec3_top4.y, vec3_top4.z, 0, 1, 0,0,1, packedLight+i*3-4-12, a);
            }
        }
        consumer = bufferSource.getBuffer(RenderType.entityTranslucent(OBELISK_LOCATION));
        for (int i = 0; i < 12; i++) {
            Vec3 vec3_1 = new Vec3(0.695,i,0).yRot(i/4.0f*(1.0f+sin*4.0f)+entityYaw);
            float yaw = (i + 1) / 4.0f * (1.0f + sin * 4.0f) + entityYaw;
            Vec3 vec3_3 = new Vec3(0.695,i+1,0).yRot(yaw);

            for (int j = 1; j <= 4; j++) {
                Vec3 vec3_2 = new Vec3(0.695,i,0).yRot(j/2.0f*pi+i/4.0f*(1.0f+sin*4.0f)+entityYaw);
                Vec3 vec3_4 = new Vec3(0.695,i+1,0).yRot(j/2.0f*pi+(i+1)/4.0f*(1.0f+sin*4.0f)+entityYaw);
                vertex(pose,consumer,vec3_3.x,vec3_3.y,vec3_3.z,0,aged,0,0, 1,packedLight+i*3-4-12,1.0f);
                vertex(pose,consumer,vec3_4.x,vec3_4.y,vec3_4.z,1,aged,0,0, 1,packedLight+i*3-4-12,1.0f);
                vertex(pose,consumer,vec3_2.x,vec3_2.y,vec3_2.z,1,1+aged,0,0, 1,packedLight+i*3-4-12,1.0f);
                vertex(pose,consumer,vec3_1.x,vec3_1.y,vec3_1.z,0,1+aged,0,0, 1,packedLight+i*3-4-12,1.0f);
                vec3_1=vec3_2;
                vec3_3=vec3_4;
            }
            if(i==11){
                Vec3 vec3_top1 = new Vec3(0.7,i+0.997,0).yRot(yaw);
                Vec3 vec3_top2 = new Vec3(0.7,i+0.997,0).yRot(pi*0.5f+yaw);
                Vec3 vec3_top3 = new Vec3(0.7,i+0.997,0).yRot(pi*1.0f+yaw);
                Vec3 vec3_top4 = new Vec3(0.7,i+0.997,0).yRot(pi*1.5f+yaw);
                vertex(pose,consumer,vec3_top1.x,vec3_top1.y,vec3_top1.z,0,0,0,0, 1,packedLight+i*3-4-12,1);
                vertex(pose,consumer,vec3_top2.x,vec3_top2.y,vec3_top2.z,1,0,0,0, 1,packedLight+i*3-4-12,1);
                vertex(pose,consumer,vec3_top3.x,vec3_top3.y,vec3_top3.z,1,1,0,0, 1,packedLight+i*3-4-12,1);
                vertex(pose,consumer,vec3_top4.x,vec3_top4.y,vec3_top4.z,0,0,1,0, 1,packedLight+i*3-4-12,1);
            }
        }
        poseStack.popPose();
        super.render(collumn, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
}
