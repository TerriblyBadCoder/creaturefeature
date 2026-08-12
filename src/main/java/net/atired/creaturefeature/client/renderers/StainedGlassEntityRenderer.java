package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.StainedGlassEntityModel;
import net.atired.creaturefeature.entity.StainedGlassEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class StainedGlassEntityRenderer extends MobRenderer<StainedGlassEntity, StainedGlassEntityModel<StainedGlassEntity>> {
    private static final ResourceLocation STAINEDGLASS_LOCATION = CreatureFeature.getId("textures/entity/stained_glass.png");
    private static final ResourceLocation BLANK_LOCATION = CreatureFeature.getId("textures/entity/stained_glass_trail.png");

    public StainedGlassEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new StainedGlassEntityModel<>(context.bakeLayer(StainedGlassEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(StainedGlassEntity machinationEntity) {
        return STAINEDGLASS_LOCATION;
    }

    @Override
    public void render(StainedGlassEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.pushPose();
        double d0 = Mth.lerp((double)partialTicks, entity.xOld, entity.getX());
        double d1 = Mth.lerp((double)partialTicks, entity.yOld, entity.getY());
        double d2 = Mth.lerp((double)partialTicks, entity.zOld, entity.getZ());
        Vec3 pos = new Vec3(d0,d1,d2);
        poseStack.translate(-pos.x(),-pos.y()+0.34,-pos.z());
        PoseStack.Pose posed = poseStack.last();
        if(CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness")!=null)
            CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness").set(1.0f);
        VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityAmbushCutout(BLANK_LOCATION));
        float ud = 1.0f;
        float alphad = 1.0f-Math.min(1.0f,entity.getReturning()*3.0f);
        float evilTickCount = Math.clamp((entity.tickCount+partialTicks-1)/8.0f,0.0f,1.0f);
            for(int i = entity.posTracker-1;i>1;i--){
                Vec3 first = entity.positions[i];
                float scaled = 1f;
                float scaled2 = 1f;
                Vec3 second =  entity.positions[i-1];
                if(i==entity.posTracker-1){
                    first=entity.getPosition(partialTicks);
                }
                Vec3 to = first.subtract(second).multiply(1,0,1).normalize();
                float a = (i/((float)entity.posTracker))*1.5f;
                a=Math.min(1.0f,a)*evilTickCount;
                float a2 = ((i-1)/((float)entity.posTracker))*1.5f;
                a2=Math.min(1.0f,a2)*evilTickCount;
                float ud2= (float) i /(float)entity.posTracker;

                    float angle = (float)Math.atan2(to.x,to.z);
                    Vec3 otherAdd = new Vec3(0.4,0,0.0).yRot(angle);
                    float angle2 = entity.getYRot();
                    if(i>1){
                        to = second.subtract(entity.positions[i-2]).multiply(1,0,1).normalize();
                        angle2 = (float)Math.atan2(to.x,to.z);
                    }
                    Vec3 otherAdd2 = new Vec3(0.4,0,0.0).yRot(angle2);
                    vertex(posed,consumer, first.x+otherAdd.x,first.y,first.z+otherAdd.z,0.0f,ud,0,-1,0,packedLight,alphad);
                    vertex(posed,consumer, second.x+otherAdd2.x,second.y,second.z+otherAdd2.z,0.0f,ud2,0,-1,0,packedLight,alphad);
                    vertex(posed,consumer, second.x-otherAdd2.x,second.y,second.z-otherAdd2.z,1.0f,ud2,0,-1,0,packedLight,alphad);
                    vertex(posed,consumer, first.x-otherAdd.x,first.y,first.z-otherAdd.z,1.0f,ud,0,-1,0,packedLight,alphad);
                ud=ud2;
            }

        poseStack.popPose();
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
