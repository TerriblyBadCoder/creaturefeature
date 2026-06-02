package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.BeautyEntity;
import net.atired.creaturefeature.client.renderers.models.BeautyEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class BeautyEntityRenderer extends MobRenderer<BeautyEntity, BeautyEntityModel<BeautyEntity>> {
    private static final ResourceLocation BEAUTY_LOCATION = CreatureFeature.getId("textures/entity/beauty.png");
    private static final ResourceLocation BEAUTY_OUTLINE_LOCATION = CreatureFeature.getId("textures/entity/beauty_outline.png");

    public BeautyEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BeautyEntityModel<>(context.bakeLayer(BeautyEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(BeautyEntity machinationEntity) {
        return BEAUTY_LOCATION;
    }

    @Override
    public void render(BeautyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        if(entity.getSpin()<=0.0f||entity.getHealth()<=0){
            return;
        }
        float spun = Math.clamp(entity.getSpin(),0.0f,2.5f);
        float rot = (float) (Math.pow(spun/1.5f*2.0f,2.0f)*3.14f);
         spun = Math.clamp(spun,0.0f,1.0f);
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(BEAUTY_OUTLINE_LOCATION));
        float offset = entity.tickCount+partialTicks;
        offset/=10.0f;
        for (int j = 3; j < 5; j++) {
            float jDiv = j/4.0f;
            for (int i = 0; i < 5; i++) {
                Vec3 dir1 = new Vec3(1,1,0).yRot(i/5.0f*2.0f*3.14f-rot*jDiv).multiply(0.7*jDiv,0.7*jDiv,0.7*jDiv);
                Vec3 dir2 = new Vec3(1,1,0).yRot((i+1)/5.0f*2.0f*3.14f-rot*jDiv).multiply(0.7*jDiv,0.7*jDiv,0.7*jDiv);
                Vec3 dir3 = new Vec3(1,-1,0).yRot((i+1)/5.0f*2.0f*3.14f-rot*jDiv).multiply(0.5*jDiv,0.5*jDiv,0.5*jDiv);
                Vec3 dir4 = new Vec3(1,-1,0).yRot(i/5.0f*2.0f*3.14f-rot*jDiv).multiply(0.5*jDiv,0.5*jDiv,0.5*jDiv);
                vertex(pose,consumer,dir1.x,dir1.y+(j/2.0f*1.7f-1.7f),dir1.z,i/2.5f+offset,0.0f,0,-1,0,packedLight,spun);
                vertex(pose,consumer,dir2.x,dir2.y+(j/2.0f*1.7f-1.7f),dir2.z,(i+1)/2.5f+offset,0.0f,0,-1,0,packedLight,spun);
                vertex(pose,consumer,dir3.x,dir3.y+(j/2.0f*1.7f-1.7f),dir3.z,(i+1)/2.5f+offset,1.0f,0,-1,0,packedLight,0);
                vertex(pose,consumer,dir4.x,dir4.y+(j/2.0f*1.7f-1.7f),dir4.z,i/2.5f+offset,1.0f,0,-1,0,packedLight,0);

            }
        }

        poseStack.popPose();

    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
