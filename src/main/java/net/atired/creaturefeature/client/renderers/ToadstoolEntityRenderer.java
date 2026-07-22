package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.ryanhcode.sable.network.packets.tcp.ServerboundPunchSubLevelPacket;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.ToadstoolEntityModel;
import net.atired.creaturefeature.entity.ToadstoolEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;

public class ToadstoolEntityRenderer extends MobRenderer<ToadstoolEntity, ToadstoolEntityModel<ToadstoolEntity>> {
    private static final ResourceLocation TOADSTOOL_LOCATION = CreatureFeature.getId("textures/entity/toadstool.png");
    private static final ResourceLocation TOADSTOOL_NOISE_LOCATION = CreatureFeature.getId("textures/entity/toadstool_noise.png");

    public ToadstoolEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new ToadstoolEntityModel<>(context.bakeLayer(ToadstoolEntityModel.LAYER_LOCATION)), 0.65f);
    }

    @Override
    public ResourceLocation getTextureLocation(ToadstoolEntity machinationEntity) {
        return TOADSTOOL_LOCATION;
    }

    @Override
    public void render(ToadstoolEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.myClientCube!=null&&!entity.myClientCube.isRemoved()){
            VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityAmbushCutout(TOADSTOOL_NOISE_LOCATION));
            float offset = entity.tickCount+partialTicks;
            offset/=10.0f;
            if(CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness")!=null)
                CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness").set(1.0f);
            Vector3dc position = entity.myClientCube.renderPose(partialTicks).position();
            Vector3d dir = entity.myClientCube.renderPose(partialTicks).orientation().getEulerAnglesYXZ(new Vector3d());
            dir=entity.myClientCube.renderPose(partialTicks).transformNormal(new Vector3d(0,1,0)).mul(entity.myClientCube.boundingBox().height()/2.0f-0.8f);
            Vector3d offSet = new Vector3d(dir.x,0,dir.z).normalize().mul(0.3).rotateY(3.14f/2.0f);
            Vec3 pos = new Vec3(
                    position.x()+dir.x(),
                    position.y()+dir.y(),
                    position.z()+dir.z()).subtract(entity.getPosition(partialTicks).add(Mth.sin(offset)/7.0f,Mth.cos(offset/1.5f)/16,Mth.cos(offset)/7.0f));
            Vec3 off = new Vec3(0.56,0.6,0).yRot(-entity.yBodyRot/180.0f*3.14f);
            poseStack.pushPose();
            PoseStack.Pose pose = poseStack.last();
            vertex(pose,consumer,pos.x-offSet.x,pos.y,pos.z-offSet.z,offset,0.0f,0,-1,0,255,1);
            vertex(pose,consumer,pos.x+offSet.x,pos.y,pos.z+offSet.z,0.25f+offset,0.0f,0,-1,0,255,1);
            vertex(pose,consumer,off.x,off.y,off.z,0.25f+offset,1.0f,0,-1,0,packedLight,0.5f);
            vertex(pose,consumer,off.x,off.y,off.z,offset,1.0f,0,-1,0,packedLight,0.5f);
            off = new Vec3(0.56,0.6,0).yRot(-entity.yBodyRot/180.0f*3.14f+3.14f);

            Vec3 pos2 = new Vec3(
                    position.x()-dir.x(),
                    position.y()-dir.y(),
                    position.z()-dir.z()).subtract(entity.getPosition(partialTicks).add(-Mth.sin(offset)/7.0f,Mth.cos(offset/1.5f)/16,-Mth.cos(offset)/7.0f));
            vertex(pose,consumer,pos2.x-offSet.x,pos2.y,pos2.z-offSet.z,0.25f+offset,0.0f,0,-1,0,255,1);
            vertex(pose,consumer,pos2.x+offSet.x,pos2.y,pos2.z+offSet.z,0.5f+offset,0.0f,0,-1,0,255,1);
            vertex(pose,consumer,off.x,off.y,off.z,0.5f+offset,1.0f,0,-1,0,packedLight,0.5f);
            vertex(pose,consumer,off.x,off.y,off.z,0.25f+offset,1.0f,0,-1,0,packedLight,0.5f);

            poseStack.popPose();
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
