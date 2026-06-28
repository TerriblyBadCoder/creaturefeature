package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.MachinationEntity;
import net.atired.creaturefeature.client.renderers.models.MachinationEntityModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class MachinationEntityRenderer extends MobRenderer<MachinationEntity, MachinationEntityModel<MachinationEntity>> {
    private static final ResourceLocation MACHINATION_LOCATION = CreatureFeature.getId("textures/entity/machination.png");
    private static final ResourceLocation SIGNAL_LOCATION = CreatureFeature.getId("textures/entity/signal.png");

    public MachinationEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new MachinationEntityModel<>(context.bakeLayer(MachinationEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(MachinationEntity machinationEntity) {
        return MACHINATION_LOCATION;
    }

    @Override
    public void render(MachinationEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        if(!entity.isLeaping()&&entity.getCharge()>0.5&&!entity.isDeadOrDying()){
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(SIGNAL_LOCATION));

            poseStack.pushPose();
            Quaternionf cam= Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
            Vec3 dir = entity.getLookAngle().multiply(1,0,1).normalize().scale(0.2);
            poseStack.translate(dir.x,1.5,dir.z);
            poseStack.mulPose(cam);
            float timed = (entity.tickCount+partialTicks)/3.0f+entity.getId();
            float scaled = 1.0f+Math.max(0.0f,(entity.getCharge()-0.85f)*8.0f);
            PoseStack.Pose pose = poseStack.last();
            float charged = Math.min((entity.getCharge()-0.5f)*2.0f,(10.0f-entity.getCharge()*10.0f));
            charged=Math.min(1.0f,charged);
            Vec3 vec3_1 = new Vec3(-0.3,0.3,0.0).zRot(timed).scale(scaled);
            Vec3 vec3_2 = new Vec3(-0.3,-0.3,0.0).zRot(timed).scale(scaled);
            Vec3 vec3_3 = new Vec3(0.3,-0.3,0.0).zRot(timed).scale(scaled);
            Vec3 vec3_4 = new Vec3(0.3,0.3,0.0).zRot(timed).scale(scaled);
            vertex(pose,consumer,vec3_1.x,vec3_1.y,vec3_1.z,0f,0f,0,0,1,255,charged);
            vertex(pose,consumer,vec3_2.x,vec3_2.y,vec3_2.z,0f,1f,0,0,1,255,charged);
            vertex(pose,consumer,vec3_3.x,vec3_3.y,vec3_3.z,1f,1f,0,0,1,255,charged);
            vertex(pose,consumer,vec3_4.x,vec3_4.y,vec3_4.z,1f,0f,0,0,1,255,charged);
            poseStack.popPose();
        }
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
