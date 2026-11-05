package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFClientProxy;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.WraithEntityModel;
import net.atired.creaturefeature.entity.WraithEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;
import org.joml.Vector3f;

public class WraithEntityRenderer extends MobRenderer<WraithEntity, WraithEntityModel<WraithEntity>> {
    private static final ResourceLocation WRAITH_LOCATION = CreatureFeature.getId("textures/entity/wraith.png");

    public WraithEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new WraithEntityModel(context.bakeLayer(WraithEntityModel.LAYER_LOCATION)), 0.0f);
        this.addLayer(new WraithCuffLayer(this));
    }

    @Override
    public void render(WraithEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        if(CFRenderTypes.WRAITH_SHADER_INSTANCE!=null){
            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("GameTime").set(entity.tickCount+entity.getId()+partialTicks);
            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("Fade").set(entity.getDrift());
            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("Flipped").set(0.0f);

            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("OffYPosition")
                    .set((float)(entity.getPosition(partialTicks).y- Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y));


        }

        super.render(entity, entityYaw, partialTicks, poseStack, CFClientProxy.getFendSource(), packedLight);
        CFClientProxy.getFendSource().endBatch();
    }

    @Override
    public ResourceLocation getTextureLocation(WraithEntity fendEntity) {
        
        return WRAITH_LOCATION;
    }


    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight,float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
