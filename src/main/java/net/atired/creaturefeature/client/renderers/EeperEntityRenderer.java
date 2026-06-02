package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.EeperEntity;
import net.atired.creaturefeature.client.renderers.models.EeperEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EeperEntityRenderer extends MobRenderer<EeperEntity, EeperEntityModel<EeperEntity>> {
    private static final ResourceLocation EEPER_LOCATION = CreatureFeature.getId("textures/entity/eeper.png");

    public EeperEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new EeperEntityModel<>(context.bakeLayer(EeperEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(EeperEntity EeperEntity) {
        return EEPER_LOCATION;
    }
    protected float getWhiteOverlayProgress(EeperEntity livingEntity, float partialTicks) {
        float f = Math.clamp(livingEntity.getFuse()/5.f-3f,0.0f,1.0f);
        return (f * 1.0F);
    }

    @Override
    protected void scale(EeperEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        float aged = livingEntity.tickCount+partialTickTime;
        float f = Math.min(livingEntity.getFuse()/20.f,1.0f);
        if(f==0){
            super.scale(livingEntity, poseStack, partialTickTime);
            return;
        }
        f = Math.min(Math.min(livingEntity.getFuse()+partialTickTime,20.0f)/20.f,1.0f);
        poseStack.scale(1.0f+Mth.sin(aged)/5.0f*f,1.0f-Mth.sin(aged/1.5f)/5.0f*f,1.0f+Mth.cos(aged)/5.0f*f);
        super.scale(livingEntity, poseStack, partialTickTime);
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
