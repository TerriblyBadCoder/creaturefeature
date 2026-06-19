package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.CanaryEntityModel;
import net.atired.creaturefeature.entity.CanaryEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class CanaryEntityRenderer extends MobRenderer<CanaryEntity, CanaryEntityModel<CanaryEntity>> {
    private static final ResourceLocation CANARY_LOCATION = CreatureFeature.getId("textures/entity/canary.png");
    private static final ResourceLocation CANARY_SMOG_LOCATION = CreatureFeature.getId("textures/entity/canary_smog.png");

    public CanaryEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CanaryEntityModel<>(context.bakeLayer(CanaryEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(CanaryEntity canaryEntity) {
        return CANARY_LOCATION;
    }

    @Override
    public boolean shouldRender(CanaryEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(CanaryEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.pushPose();
        Vec3 pos = entity.getPosition(partialTicks);
        poseStack.translate(-pos.x(),-pos.y()+0.5,-pos.z());
        PoseStack.Pose posed = poseStack.last();
        if(CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness")!=null)
            CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness").set(1.0f);
        VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityAmbushCutout(CANARY_SMOG_LOCATION));

        float attackAnim = entity.attackAnim;
        float sinused = Mth.sin(attackAnim*3.14f)+1.0f;
        float ud = 0.0f;
        for(int i = entity.posTracker;i>0;i--){
            Vec3 first = new Vec3(0,0,0);
            Vec3 second =  entity.positions[i-1];
            if(i<entity.posTracker){
                first = entity.positions[i];
            }else{
                first=entity.getPosition(partialTicks);
            }
            float a = (i/((float)entity.posTracker))*0.5f;
            vertex(posed,consumer, first.x,first.y+0.6f*sinused,first.z,ud,0.0f,0,-1,0,packedLight,a);
            vertex(posed,consumer, first.x,first.y-0.6f*sinused,first.z,ud,1.0f,0,-1,0,packedLight,a);

            a = ((i-1)/((float)entity.posTracker))*0.5f;
            ud+=(float)first.distanceTo(second)/2.0f;
            vertex(posed,consumer, second.x,second.y-0.6f*sinused,second.z,ud,1.0f,0,-1,0,packedLight,a);
            vertex(posed,consumer, second.x,second.y+0.6f*sinused,second.z,ud,0.0f,0,-1,0,packedLight,a);
        }
        poseStack.popPose();
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
