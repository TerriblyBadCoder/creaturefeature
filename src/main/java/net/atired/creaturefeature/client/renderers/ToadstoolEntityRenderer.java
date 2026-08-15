package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.ryanhcode.sable.network.packets.tcp.ServerboundPunchSubLevelPacket;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.ryanhcode.sable.sublevel.render.SubLevelRenderer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.ToadstoolEntityModel;
import net.atired.creaturefeature.entity.ToadstoolEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
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
    public static void renderBoxes(PoseStack poseStack,MultiBufferSource buffer){
        for (int i = 0; i < 4; i++) {
            Vec3 dir = new Vec3(0.7,0,0).yRot(i/2.0f*3.14f+3.14f/4.0f);
            DebugRenderer.renderFilledBox(poseStack, buffer, new AABB(
                    -0.02f+dir.x,-1.51,-0.02+dir.z,
                    0.02+dir.x,1.51,0.02+dir.z),0.509f*0.81f, 0.0f*0.81f,0.487f*0.81f, 1F);
        }
        for (int i = 0; i < 2; i++) {
            Vec3 dir = new Vec3(0,1.5,0).scale(1-((i)%2)*2);
            for (int j = 0; j < 2; j++) {
                Vec3 dir2 = new Vec3(0.5,0,0).scale(1-((j)%2)*2);
                DebugRenderer.renderFilledBox(poseStack, buffer, new AABB(
                        -0.02f+dir2.x,-0.02+dir.y,-1.02/2,
                        0.02+dir2.x,0.02+dir.y,1.02/2),0.509f*0.81f, 0.0f*0.81f,0.487f*0.81f, 1F);
            }
            for (int j = 0; j < 2; j++) {
                Vec3 dir2 = new Vec3(0.0,0,0.5).scale(1-((j)%2)*2);
                DebugRenderer.renderFilledBox(poseStack, buffer, new AABB(
                        -1.02/2,-0.02+dir.y,-0.02+dir2.z,
                        1.02/2,0.02+dir.y,0.02+dir2.z),0.509f*0.81f, 0.0f*0.81f,0.487f*0.81f, 1F);
            }

        }
    }
    @Override
    public void render(ToadstoolEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.myClientCube!=null&&!entity.myClientCube.isRemoved()&&entity.myClientCube instanceof ClientSubLevel clientCube){
            poseStack.pushPose();
            Vector3dc posCube = clientCube.renderPose(partialTicks).position();
            Vec3 transpos = entity.getPosition(partialTicks).scale(-1).add(posCube.x(),posCube.y(),posCube.z());
            Vector3d dirCube = clientCube.renderPose(partialTicks).orientation().getEulerAnglesYXZ(new Vector3d());
            poseStack.translate(transpos.x,transpos.y,transpos.z);

            poseStack.mulPose(new Quaternionf().rotationYXZ((float) dirCube.y(),(float)dirCube.x(),(float)dirCube.z()));
            renderBoxes(poseStack,buffer);
            Vector3dc sizedCube = clientCube.renderPose(partialTicks).scale().mul(clientCube.boundingBox().size(),new Vector3d(1,1,1));

            poseStack.popPose();
            for (int i = 0; i < 2; i++) {
                float alpha=1f-entity.lerpedonFours;
                float zRot = Mth.cos((entity.tickCount+partialTicks)/8.0f)/5.0f+1f;
                VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityAmbushCutout(TOADSTOOL_NOISE_LOCATION));
                float offset = entity.tickCount+partialTicks;
                offset/=80.0f;
                if(CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness")!=null)
                    CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness").set(1.0f);
                Vector3dc position = clientCube.renderPose(partialTicks).position();
                Vector3d dir = clientCube.renderPose(partialTicks).orientation().getEulerAnglesYXZ(new Vector3d());
                dir=clientCube.renderPose(partialTicks).transformNormal(new Vector3d(0,1,0)).mul(clientCube.boundingBox().height()/2.0f-0.8f);
                Vector3d offSet = new Vector3d(dir.x,0,dir.z).normalize().mul(0.3).rotateY(3.14f/2.0f+i*3.14f/2f);
                Vec3 pos = new Vec3(
                        position.x()+dir.x(),
                        position.y()+dir.y(),
                        position.z()+dir.z()).subtract(entity.getPosition(partialTicks).add(Mth.sin(offset)/7.0f,Mth.cos(offset/1.5f)/16,Mth.cos(offset)/7.0f));
                Vec3 off = new Vec3(0.56*zRot,0.75,0).yRot(-entity.yBodyRot/180.0f*3.14f);
                poseStack.pushPose();
                PoseStack.Pose pose = poseStack.last();
                vertex(pose,consumer,pos.x-offSet.x*1.5f,pos.y,pos.z-offSet.z*1.5f,offset,0.0f,0,-1,0,255,alpha);
                vertex(pose,consumer,pos.x+offSet.x*1.5f,pos.y,pos.z+offSet.z*1.5f,0.25f+offset,0.0f,0,-1,0,255,alpha);
                vertex(pose,consumer,off.x+offSet.x,off.y,off.z+offSet.z,0.25f+offset,1.0f,0,-1,0,packedLight,0.0f*alpha);
                vertex(pose,consumer,off.x-offSet.x,off.y,off.z-offSet.z,offset,1.0f,0,-1,0,packedLight,0.0f*alpha);
                off = new Vec3(0.56*zRot,0.75,0).yRot(-entity.yBodyRot/180.0f*3.14f+3.14f);

                Vec3 pos2 = new Vec3(
                        position.x()-dir.x(),
                        position.y()-dir.y(),
                        position.z()-dir.z()).subtract(entity.getPosition(partialTicks).add(-Mth.sin(offset)/7.0f,Mth.cos(offset/1.5f)/16,-Mth.cos(offset)/7.0f));
                vertex(pose,consumer,pos2.x-offSet.x*1.5f,pos2.y,pos2.z-offSet.z*1.5f,0.25f+offset,0.0f,0,-1,0,255,alpha);
                vertex(pose,consumer,pos2.x+offSet.x*1.5f,pos2.y,pos2.z+offSet.z*1.5f,0.5f+offset,0.0f,0,-1,0,255,alpha);
                vertex(pose,consumer,off.x+offSet.x,off.y,off.z+offSet.z,0.5f+offset,1.0f,0,-1,0,packedLight,0.0f*alpha);
                vertex(pose,consumer,off.x-offSet.x,off.y,off.z-offSet.z,0.25f+offset,1.0f,0,-1,0,packedLight,0.0f*alpha);

                poseStack.popPose();
            }
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public boolean shouldRender(ToadstoolEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
