package net.atired.creaturefeature.client.renderers;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.renderers.models.HenchEntityModel;
import net.atired.creaturefeature.entity.HenchEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class HenchEntityRenderer extends MobRenderer<HenchEntity, HenchEntityModel<HenchEntity>> {
    private static final ResourceLocation HENCH_LOCATION = CreatureFeature.getId("textures/entity/hench.png");

    public HenchEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new HenchEntityModel<>(context.bakeLayer(HenchEntityModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(HenchEntity machinationEntity) {
        return HENCH_LOCATION;
    }


    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight,float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
