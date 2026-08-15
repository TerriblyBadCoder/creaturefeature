package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.BlossomEntityModel;
import net.atired.creaturefeature.entity.BlossomEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BlossomEntityRenderer extends HumanoidMobRenderer<BlossomEntity, BlossomEntityModel<BlossomEntity>> {
    private static final ResourceLocation BLOSSOM_LEAF_LOCATION = CreatureFeature.getId("textures/entity/blossom_leaf.png");
    private static final ResourceLocation BLOSSOM_LOCATION = CreatureFeature.getId("textures/entity/blossom.png");
    private static final ResourceLocation SPORES_LOCATION = CreatureFeature.getId("textures/entity/spores.png");
    public BlossomEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BlossomEntityModel<>(context.bakeLayer(BlossomEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public void render(BlossomEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        float pied = (float)Math.PI;
        float opening = entity.getOpening();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(BLOSSOM_LEAF_LOCATION));
        for (int i = 0; i < 6; i++) {
            poseStack.pushPose();
            float zRot = 0f;
            if (entity.deathTime > 0) {
                float f = ((float)entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
                f = Mth.sqrt(f);
                if (f > 1.0F) {
                    f = 1.0F;
                }
                zRot=f * this.getFlipDegrees(entity)/90f*3.14f;
                setupRotations(entity,poseStack,0,entityYaw,partialTicks,1f);
            }
            poseStack.translate(0.0f,1.6f,0);
            poseStack.mulPose(new Quaternionf().rotationZYX(0,-entity.getYHeadRot()/180.0f*3.14f,0.2f+entity.getViewXRot(partialTicks)/180.0f/4.0f*3.14f));
            poseStack.translate(0.0f,0,0.1f);
            poseStack.mulPose(new Quaternionf().rotationXYZ(0,3.14f/3.0f*i,0));
            PoseStack.Pose pose= poseStack.last();
            Vec3 vec1 = new Vec3(0.21,0,0).yRot(-3.14f/4.0f);
            Vec3 vec2 = new Vec3(0.21,0,0).yRot(3.14f/4.0f);
            Vector3f vector3f = new Vector3f(1.0f,0,0).rotateY(0);
            int overlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
            float sinused = Mth.sin(entity.getId()+entity.tickCount/10.0f+i*1.57f)*0.9f*(0.4f+opening*0.6f)+0.7f+(opening-1.0f)*2.7f;
            for (int j = 0; j < 6; j++) {
                Vec3 vec3 = vec1.add(new Vec3(0.1,0.1,0).zRot(sinused*j/6.0f).yRot(-3.14f/8.0f));
                Vec3 vec4 = vec2.add(new Vec3(0.1,0.1,0).zRot(sinused*j/6.0f).yRot(3.14f/8.0f));

                vertex(pose,consumer,vec3.x,vec3.y,vec3.z,0,1.0f-(j+1)/6.0f,1,0.0f,1,packedLight,1.0f,overlay);
                vertex(pose,consumer,vec4.x,vec4.y,vec4.z,1,1.0f-(j+1)/6.0f,1,0.0f,1,packedLight,1.0f,overlay);
                vertex(pose,consumer,vec2.x,vec2.y,vec2.z,1,1.0f-(j)/6.0f,1,0.0f,1,packedLight,1.0f,overlay);
                vertex(pose,consumer,vec1.x,vec1.y,vec1.z,0,1.0f-(j)/6.0f,1,0.0f,1,packedLight,1.0f,overlay);
                vec1=vec3;
                vec2=vec4;
            }
            poseStack.popPose();
        }
        if(entity.isDeadOrDying())return;

        consumer = buffer.getBuffer(CFRenderTypes.entityBlossomCull(SPORES_LOCATION));
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.translate(0.0f,1.6f,0);
            poseStack.mulPose(new Quaternionf().rotationZYX(0,-entity.getYHeadRot()/180.0f*3.14f,(0.25f-opening*0.25f)+entity.getViewXRot(partialTicks)/180.0f/4.0f*3.14f));
            poseStack.translate(0.0f,0,0.05f);
            poseStack.mulPose(new Quaternionf().rotationXYZ(0,3.14f/2.0f*i,0));
            Vec3 vec1 = new Vec3(0.2,0,0).yRot(-3.14f/4.0f);
            Vec3 vec2 = new Vec3(0.2,0,0).yRot(3.14f/4.0f);
            Vec3 vec3 = new Vec3(0.25,0.2,0).yRot(-3.14f/4.0f);
            Vec3 vec4 = new Vec3(0.25,0.2,0).yRot(3.14f/4.0f);
            PoseStack.Pose pose= poseStack.last();

            vertex(pose,consumer,vec3.x,vec3.y,vec3.z,i/4.0f,0.2f,1,0.0f,1,packedLight,0.5f,OverlayTexture.NO_OVERLAY);
            vertex(pose,consumer,vec4.x,vec4.y,vec4.z,(i+1)/4.0f,0.2f,1,0.0f,1,packedLight,0.5f,OverlayTexture.NO_OVERLAY);
            vertex(pose,consumer,vec2.x,vec2.y,vec2.z,(i+1)/4.0f,0,1,0.0f,1,packedLight,1.0f,OverlayTexture.NO_OVERLAY);
            vertex(pose,consumer,vec1.x,vec1.y,vec1.z,i/4.0f,0,1,0.0f,1,packedLight,1.0f,OverlayTexture.NO_OVERLAY);

            vertex(pose,consumer,vec3.x,vec3.y+0.8f,vec3.z,i/4.0f,1.0f,1,0.0f,1,packedLight,0.0f,OverlayTexture.NO_OVERLAY);
            vertex(pose,consumer,vec4.x,vec4.y+0.8f,vec4.z,(i+1)/4.0f,1.0f,1,0.0f,1,packedLight,0.0f,OverlayTexture.NO_OVERLAY);
            vertex(pose,consumer,vec4.x,vec4.y,vec4.z,(i+1)/4.0f,0.2f,1,0.0f,1,packedLight,0.5f,OverlayTexture.NO_OVERLAY);
            vertex(pose,consumer,vec3.x,vec3.y,vec3.z,i/4.0f,0.2f,1,0.0f,1,packedLight,0.5f,OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }

    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, float normalX, float normalY, float normalZ, int packedLight,float alpha,int overlaid) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(overlaid).setLight(packedLight).setNormal((float)normalX, (float)normalZ, (float)normalY);
    }
    @Override
    public ResourceLocation getTextureLocation(BlossomEntity blossomEntity) {
        return BLOSSOM_LOCATION;
    }

}
