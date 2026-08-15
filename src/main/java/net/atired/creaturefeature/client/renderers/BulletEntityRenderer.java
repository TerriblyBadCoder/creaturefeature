package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.BulletEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class BulletEntityRenderer extends EntityRenderer<BulletEntity> {
    private static final ResourceLocation BULLET_LOCATION = CreatureFeature.getId("textures/entity/bullet.png");

    public BulletEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BulletEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        if(p_entity.tickCount<2)return;

        float len = p_entity.length+0.4f;
        poseStack.pushPose();
        poseStack.translate(0,0.1,0);
        Vec3 dir = new Vec3(0,0,len).xRot(-p_entity.getXRot()/180f*3.14f).yRot(-entityYaw/180.f*3.14f);
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(BULLET_LOCATION));
        Vec3 scaleY=new Vec3(0,Math.clamp(Mth.sin((p_entity.tickCount+partialTick)/10.0f*3.14f)*1f,0f,1f),0).xRot(-p_entity.getXRot()/180f*3.14f).yRot(-entityYaw/180.f*3.14f);
        float alpha = Math.min(1f,(float)Math.pow(scaleY.y,2f)*1.2f);
        float aged = -(p_entity.tickCount+partialTick)*1.8f;
        scaleY=scaleY.scale(0.5*0.2);
        vertex(pose,consumer,scaleY.x,scaleY.y,scaleY.z,aged,0,0,0,1,255,alpha);
        vertex(pose,consumer,-scaleY.x,-scaleY.y,-scaleY.z,aged,1,0,0,1,255,alpha);
        vertex(pose,consumer,dir.x-scaleY.x,dir.y-scaleY.y,dir.z-scaleY.z,len+aged,1,0,0,1,255,alpha);
        vertex(pose,consumer,dir.x+scaleY.x,dir.y+scaleY.y,dir.z+scaleY.z,len+aged,0,0,0,1,255,alpha);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(BulletEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity entity) {
        return BULLET_LOCATION;
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(alpha,alpha,alpha,1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
}
