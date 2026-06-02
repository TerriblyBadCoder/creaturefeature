package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.VertigoEntityModel;
import net.atired.creaturefeature.entity.VertigoEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VertigoEntityRenderer extends MobRenderer<VertigoEntity, VertigoEntityModel<VertigoEntity>> {
    private static final ResourceLocation VERTIGO_LOCATION = CreatureFeature.getId("textures/entity/vertigo.png");
    private static final ResourceLocation BLANK_LOCATION = CreatureFeature.getId("textures/entity/blank.png");

    public VertigoEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new VertigoEntityModel<>(context.bakeLayer(VertigoEntityModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(VertigoEntity canaryEntity) {
        return VERTIGO_LOCATION;
    }

    @Override
    public void render(VertigoEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        float rabies = entity.getRabies();
        if(rabies<0.1f){
            return;
        }
        poseStack.pushPose();
        Vector3f dir = entity.getViewVector(partialTicks).scale(0.8).toVector3f();
        poseStack.translate(dir.x,dir.y+1.0f,dir.z);
        poseStack.mulPose(new Quaternionf().rotationZYX(3.14f/2.0f+entity.getViewXRot(partialTicks)/180.0f*3.14f,0.0f,-entity.getYHeadRot()/180.0f*3.14f+3.14f/2.0f));
        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityOutlinedCutout(BLANK_LOCATION));
        for (int i = 0; i < 5; i++) {
                Vec3 dir1 = new Vec3(0.5f,1*rabies,0).yRot(i/5.0f*2.0f*3.14f+entity.tickCount/20.0f).scale(6);
                Vec3 dir2 = new Vec3(0.5f,1*rabies,0).yRot((i+1)/5.0f*2.0f*3.14f+entity.tickCount/20.0f).scale(6);
                vertex(pose,consumer,dir1.x,dir1.y,dir1.z,0,0.0f,0,-1,0,packedLight,1.0f*rabies);
                vertex(pose,consumer,dir2.x,dir2.y,dir2.z,1,0.0f,0,-1,0,packedLight,1.0f*rabies);
                vertex(pose,consumer,0.0f,0.0f,0.0f,1,1.0f,0,-1,0,packedLight,1.0f);
                vertex(pose,consumer,0.0f,0.0f,0.0f,0,1.0f,0,-1,0,packedLight,1.0f);

        }
        consumer = buffer.getBuffer(CFRenderTypes.entityAmbushCutout(BLANK_LOCATION));
        if(CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness")!=null)
            CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness").set(1.0f);

        for (int i = 0; i < 5; i++) {
            Vec3 dir1 = new Vec3(0.5f,1*rabies,0).yRot(i/5.0f*2.0f*3.14f+entity.tickCount/20.0f).scale(6);
            Vec3 dir2 = new Vec3(0.5f,1*rabies,0).yRot((i+1)/5.0f*2.0f*3.14f+entity.tickCount/20.0f).scale(6);
            vertex(pose,consumer,dir1.x,dir1.y,dir1.z,0,0.0f,0,-1,0,packedLight,0.0f);
            vertex(pose,consumer,dir2.x,dir2.y,dir2.z,1,0.0f,0,-1,0,packedLight,0.0f);
            vertex(pose,consumer,0.0f,0.0f,0.0f,1,1.0f,0,-1,0,packedLight,rabies*0.2f);
            vertex(pose,consumer,0.0f,0.0f,0.0f,0,1.0f,0,-1,0,packedLight,rabies*0.2f);

        }

        poseStack.popPose();
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,0.5f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal((float)normalX, (float)normalZ, (float)normalY);
    }

}
