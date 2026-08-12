package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.renderers.models.FiendEntityModel;
import net.atired.creaturefeature.entity.FiendEntity;
import net.atired.creaturefeature.init.CFItemInit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class FiendEntityRenderer extends MobRenderer<FiendEntity, FiendEntityModel<FiendEntity>> {
    private static final ResourceLocation FIEND_LOCATION = CreatureFeature.getId("textures/entity/fiend.png");
    private final ItemRenderer itemRenderer;


    public FiendEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new FiendEntityModel<>(context.bakeLayer(FiendEntityModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new FiendCritLayer<>(this,context.getModelSet()));
        this.itemRenderer=context.getItemRenderer();
    }

    @Override
    public void render(FiendEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.getCrit()>0.1f){
            float critted = Math.min(1f,(entity.getCrit()-0.04f)*20f);
            float f4 = entity.walkAnimation.speed(partialTicks);

            if (f4 > 1.0F) {
                f4 = 1.0F;
            }
            float yawEd = -entity.getPreciseBodyRotation(partialTicks)/180.0f*3.14f;
            float aged = (entity.tickCount+partialTicks)/3.0f;

            poseStack.pushPose();
            poseStack.mulPose(new Quaternionf().rotationXYZ(0f,yawEd+3.14f,0f));
            poseStack.translate(Mth.sin(aged)/20f,1.8-f4/10f,-0.5-f4/6f+Mth.cos(aged)/20f);
            poseStack.scale(critted,1,1);

            this.itemRenderer.renderStatic(CFItemInit.FLINTLOCK.toStack(), ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
            poseStack.popPose();
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FiendEntity machinationEntity) {
        return FIEND_LOCATION;
    }


    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight,float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
