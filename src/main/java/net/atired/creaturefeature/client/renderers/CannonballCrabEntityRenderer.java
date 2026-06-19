package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.renderers.models.CannonballCrabEntityModel;
import net.atired.creaturefeature.entity.CannonballCrabEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CannonballCrabEntityRenderer extends MobRenderer<CannonballCrabEntity, CannonballCrabEntityModel<CannonballCrabEntity>> {
    private static final ResourceLocation CRAB_LOCATION = CreatureFeature.getId("textures/entity/cannonball.png");
    private static final ResourceLocation CRAB_LEG_LOCATION = CreatureFeature.getId("textures/entity/crab_leg.png");

    public CannonballCrabEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CannonballCrabEntityModel<>(context.bakeLayer(CannonballCrabEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(CannonballCrabEntity machinationEntity) {
        return CRAB_LOCATION;
    }

    @Override
    public boolean shouldRender(CannonballCrabEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(CannonballCrabEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        if(entity.oldLegPositions[7]!=null&&entity.oldLegPositions[0]!=null){
            poseStack.pushPose();
            int overlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));;
            Vec3 pos = entity.getPosition(partialTicks).add(0,-0.0,0);
            poseStack.translate(-pos.x(),-pos.y(),-pos.z());
            pos=pos.add(0,0.2,0);
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(CRAB_LEG_LOCATION));
            int order = 0;
            for(Vec3 lPos : entity.oldLegPositions){
                lPos = lPos.add(0,Mth.sin(entity.legSin[order]*3.14f)*entity.legOffset[order],0);
                Vec3 dirTo = lPos.subtract(pos).multiply(1,1,1).normalize();
                float yaw = (float)Mth.atan2(dirTo.x,dirTo.z);
                float pitch = (float)Math.asin(-dirTo.y);
                boolean flip = false;
                if(pitch<0.0f){
                    pitch-=3.14f;
                    flip=true;
                }
                int light = (int) (packedLight);
                float distTo = (float)pos.subtract(lPos).length();
                PoseStack.Pose pose = poseStack.last();
                vertexes(entity,flip,lPos, dirTo, yaw, pitch, pose, consumer, pos, distTo, light, overlay);
                order+=1;
            }
            poseStack.popPose();
        }
    }

    private void vertexes(CannonballCrabEntity entity,boolean flip,Vec3 lPos, Vec3 dirTo, float yaw, float pitch, PoseStack.Pose pose, VertexConsumer consumer, Vec3 pos, float distTo, int light, int overlay) {
        //BULLSHIT START
        Vec3 offSet = dirTo.multiply(1,0,1).normalize().yRot(3.14f/2.0f).scale(0.5/4.0);
        Vec3 offSet2 = new Vec3(1,0,0).yRot(yaw +3.14f/2.0f).add(0,new Vec3(1,0,0).zRot(pitch +3.14f/2.0f).y,0).scale(0.5/4.0f);
        Vec3 normal = new Vec3(-1,0,-1);
        if(flip)normal=normal.scale(-1);
        vertex(pose, consumer, pos.x-offSet.x+offSet2.x, pos.y-offSet.y+offSet2.y, pos.z-offSet.z+offSet2.z,0,-distTo,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, pos.x+offSet.x+offSet2.x, pos.y+offSet.y+offSet2.y, pos.z+offSet.z+offSet2.z,1,-distTo,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, lPos.x+offSet.x+offSet2.x, lPos.y+offSet.y+offSet2.y, lPos.z+offSet.z+offSet2.z,1,0,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, lPos.x-offSet.x+offSet2.x, lPos.y-offSet.y+offSet2.y, lPos.z-offSet.z+offSet2.z,0,0,normal.x,normal.y,normal.z, light,1.0f, overlay);
        normal=normal.scale(-1);
        offSet2=offSet2.scale(-1);
        vertex(pose, consumer, pos.x-offSet.x+offSet2.x, pos.y-offSet.y+offSet2.y, pos.z-offSet.z+offSet2.z,0,-distTo,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, pos.x+offSet.x+offSet2.x, pos.y+offSet.y+offSet2.y, pos.z+offSet.z+offSet2.z,1,-distTo,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, lPos.x+offSet.x+offSet2.x, lPos.y+offSet.y+offSet2.y, lPos.z+offSet.z+offSet2.z,1,0,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, lPos.x-offSet.x+offSet2.x, lPos.y-offSet.y+offSet2.y, lPos.z-offSet.z+offSet2.z,0,0,normal.x,normal.y,normal.z, light,1.0f, overlay);
        offSet=offSet2;
        normal=new Vec3(0,1,0);
        offSet2= dirTo.multiply(1,0,1).normalize().yRot(3.14f/2.0f).scale(0.5/4.0);
        vertex(pose, consumer, pos.x-offSet.x+offSet2.x, pos.y-offSet.y+offSet2.y, pos.z-offSet.z+offSet2.z,0,-distTo,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, pos.x+offSet.x+offSet2.x, pos.y+offSet.y+offSet2.y, pos.z+offSet.z+offSet2.z,1,-distTo,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, lPos.x+offSet.x+offSet2.x, lPos.y+offSet.y+offSet2.y, lPos.z+offSet.z+offSet2.z,1,0,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, lPos.x-offSet.x+offSet2.x, lPos.y-offSet.y+offSet2.y, lPos.z-offSet.z+offSet2.z,0,0,normal.x,normal.y,normal.z, light,1.0f, overlay);
        offSet2=offSet2.scale(-1);
        vertex(pose, consumer, pos.x-offSet.x+offSet2.x, pos.y-offSet.y+offSet2.y, pos.z-offSet.z+offSet2.z,0,-distTo,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, pos.x+offSet.x+offSet2.x, pos.y+offSet.y+offSet2.y, pos.z+offSet.z+offSet2.z,1,-distTo,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, lPos.x+offSet.x+offSet2.x, lPos.y+offSet.y+offSet2.y, lPos.z+offSet.z+offSet2.z,1,0,normal.x,normal.y,normal.z, light,1.0f, overlay);
        vertex(pose, consumer, lPos.x-offSet.x+offSet2.x, lPos.y-offSet.y+offSet2.y, lPos.z-offSet.z+offSet2.z,0,0,normal.x,normal.y,normal.z, light,1.0f, overlay);
        //END
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, double normalX, double normalY, double normalZ, int packedLight, float alpha,int overlay) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(overlay).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
