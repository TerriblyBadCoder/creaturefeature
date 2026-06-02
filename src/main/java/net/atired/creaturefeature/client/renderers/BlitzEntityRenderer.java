package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.BlitzEntity;
import net.atired.creaturefeature.client.renderers.models.BlitzEntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class BlitzEntityRenderer extends MobRenderer<BlitzEntity, BlitzEntityModel<BlitzEntity>> {
    private static final ResourceLocation BLITZ_LOCATION = CreatureFeature.getId("textures/entity/blitz.png");
   
    public BlitzEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BlitzEntityModel<>(context.bakeLayer(BlitzEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(BlitzEntity machinationEntity) {
        return BLITZ_LOCATION;
    }

    @Override
    public void render(BlitzEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        if(entity.posTracker>=5&&!entity.isInvisible()&&entity.getHealth()>0.1){
            for (int i = 0; i < 5; i++) {
                Vec3 translate = entity.getPosition(partialTicks).multiply(-1,-1,-1).add(entity.positions[i]);
                if(translate.length()<0.2){
                    continue;
                }
                poseStack.pushPose();
                poseStack.translate(
                        translate.x
                                +Mth.sin(i/4.0f/4.0f*3.14f+(entity.tickCount+partialTicks)/4.0f)/16.0f*(4-i+1),
                        translate.y +1.501F,
                        translate.z
                                -Mth.cos(i/4.0f/4.0f*3.14f+(entity.tickCount+partialTicks)/4.0f)/16.0f*((4-i)*2.0+1));
                RenderType rendertype = RenderType.entityTranslucent(BLITZ_LOCATION);
                float scalemult = 1.0f-(4-i)/12.0f;
                poseStack.scale(-1*scalemult,-1*scalemult,-1*scalemult);
                this.setupRotations(entity, poseStack, 0, Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot)+180.0f, partialTicks, 1.0f);
                this.model.renderToBuffer(poseStack,buffer.getBuffer(rendertype),255,OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int)(250.0f/((4.0f-i)/2.0f+3.0f)*Math.min(1.0f,translate.length()*3.0)),255,255,255));
                poseStack.popPose();
            }
        }
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
