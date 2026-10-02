package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.renderers.models.FishEyeEntityModel;
import net.atired.creaturefeature.entity.FishEyeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class FishEyeEntityRenderer extends MobRenderer<FishEyeEntity, FishEyeEntityModel<FishEyeEntity>> {
    private static final ResourceLocation FISHEYE_LOCATION = CreatureFeature.getId("textures/entity/fisheye.png");

    public FishEyeEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new FishEyeEntityModel<>(context.bakeLayer(FishEyeEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(FishEyeEntity machinationEntity) {
        return FISHEYE_LOCATION;
    }


    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight,float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
