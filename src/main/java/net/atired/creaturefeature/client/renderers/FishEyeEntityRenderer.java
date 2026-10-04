package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.FishEyeEntityModel;
import net.atired.creaturefeature.entity.FishEyeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class FishEyeEntityRenderer extends MobRenderer<FishEyeEntity, FishEyeEntityModel<FishEyeEntity>> {
    private static final ResourceLocation FISHEYE_LOCATION = CreatureFeature.getId("textures/entity/fisheye.png");

    private static final ResourceLocation FISHEYE_TRAIL_LOCATION = CreatureFeature.getId("textures/entity/fisheye_trail.png");
    public FishEyeEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new FishEyeEntityModel<>(context.bakeLayer(FishEyeEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(FishEyeEntity machinationEntity) {
        return FISHEYE_LOCATION;
    }

    @Override
    protected void scale(FishEyeEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        float appeared = Math.max(0.0f,1.0f+Math.min(livingEntity.getAppear(),1.0f)*1.5f);

        poseStack.scale(appeared,appeared,1.0f);
        super.scale(livingEntity, poseStack, partialTickTime);
    }

    @Override
    public boolean shouldRender(FishEyeEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(FishEyeEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float appeared = Math.min(1.0f,entity.getAppear()*3.5f);
        if(appeared>0.1f){
            poseStack.pushPose();
            double d0 = Mth.lerp((double)partialTicks, entity.xOld, entity.getX());
            double d1 = Mth.lerp((double)partialTicks, entity.yOld, entity.getY());
            double d2 = Mth.lerp((double)partialTicks, entity.zOld, entity.getZ());
            Vec3 pos = new Vec3(d0,d1,d2);
            poseStack.translate(-pos.x(),-pos.y()+0.2,-pos.z());
            PoseStack.Pose posed = poseStack.last();

            if(CFRenderTypes.FISHEYE_SHADER_INSTANCE.getUniform("Revealness")!=null)
                CFRenderTypes.FISHEYE_SHADER_INSTANCE.getUniform("Revealness").set(entity.getId()*40.6f);
            VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityFisheyeCull(FISHEYE_TRAIL_LOCATION));
            float ud = partialTicks/15.0f;
            float a =1-partialTicks/14f;

            float evilTickCount = Math.clamp((entity.tickCount+partialTicks-1)/15.0f,0.0f,1.0f);
            for(int i = entity.posTracker-1;i>1;i--){
                Vec3 first = entity.positions[i];
                Vec3 second =  entity.positions[i-1];
                if(i==entity.posTracker-1){
                    first=pos;
                }
                Vec3 to = first.subtract(second).multiply(1,1,1).normalize();
                float yaw = (float)Math.atan2(to.x,to.z);
                float pitch = (float)Math.asin(to.y);
                float a2 = Math.clamp(((i-1)/((float)(entity.posTracker-1))),0f,1f)-partialTicks/15f;
                if(entity.storedIndex==i-1){
                    a=0.0f;
                    a2=0.0f;
                }
                if(entity.storedIndex==i-2){
                    a2=0.0f;
                    a=0.0f;
                }
                if(entity.storedIndex==i){
                    a=0.0f;
                }
                if(entity.storedIndex==i-3){
                    a2=0.0f;
                }
                float ud2=ud+1.0f/15.0f;
                poseStack.pushPose();
                poseStack.translate(first.x,first.y,first.z);
                poseStack.mulPose(new Quaternionf().rotationZYX(0,yaw,-pitch));

                posed=poseStack.last();
                vertex(posed, consumer, 0, 0.5f *appeared, 0, ud, 0.0f, 0, 0, 1, 255, a);
                vertex(posed,consumer, 0,-0.5f*appeared,0,ud,1.0f,0,0,1,255,a);
                poseStack.popPose();

                 to = entity.positions[i-1].subtract(entity.positions[i-2]).multiply(1,1,1).normalize();
                 yaw = (float)Math.atan2(to.x,to.z);
                 pitch = (float)Math.asin(to.y);
                poseStack.pushPose();
                poseStack.translate(second.x,second.y,second.z);
                poseStack.mulPose(new Quaternionf().rotationZYX(0,yaw,-pitch));

                posed=poseStack.last();
                vertex(posed,consumer,0,-0.5f*appeared,0,ud2,1.0f,0,0,1,255,a2);
                vertex(posed,consumer, 0,0.5f*appeared,0,ud2,0.0f,0,0,1,255,a2);
                poseStack.popPose();
                ud=ud2;
                a =a2;

            }

            poseStack.popPose();
        }

        if(CFRenderTypes.FISHEYE_ITSELF_SHADER_INSTANCE.getUniform("Bright")!=null)
            CFRenderTypes.FISHEYE_ITSELF_SHADER_INSTANCE.getUniform("Bright").set(entity.getAppear());
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
