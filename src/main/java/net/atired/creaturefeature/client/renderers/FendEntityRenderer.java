package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFClientProxy;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.FendEntityModel;
import net.atired.creaturefeature.entity.FendEntity;
import net.atired.creaturefeature.entity.MachinationEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;
import org.joml.Vector3f;

public class FendEntityRenderer extends HumanoidMobRenderer<FendEntity, FendEntityModel> {
    private static final ResourceLocation FEND_LOCATION = CreatureFeature.getId("textures/entity/fend.png");
    private static final ResourceLocation FEND_DEEP_LOCATION = CreatureFeature.getId("textures/entity/fend_deep.png");

    public FendEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new FendEntityModel(context.bakeLayer(FendEntityModel.LAYER_LOCATION)), 0.0f);
        this.addLayer(new HumanoidArmorLayer(this, new FendEntityModel(context.bakeLayer(FendEntityModel.LAYER_INNER_ARMOUR_LOCATION)), new FendEntityModel(context.bakeLayer(FendEntityModel.LAYER_ARMOUR_LOCATION)), context.getModelManager()));
    }

    @Override
    public void render(FendEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {


        CFRenderTypes.FEND_SHADER_INSTANCE.safeGetUniform("GameTime").set(entity.tickCount+entity.getId()+partialTicks);

        CFRenderTypes.FEND_SHADER_INSTANCE.safeGetUniform("OffYPosition")
                .set((float)(entity.getPosition(partialTicks).y- Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y));

        boolean shouldHover = entity.getItemInHand(InteractionHand.MAIN_HAND).getItem()instanceof ProjectileWeaponItem;
        boolean shouldMeleeSword = entity.getItemInHand(InteractionHand.MAIN_HAND).getItem()instanceof SwordItem;
        if(shouldHover){
            CFRenderTypes.FEND_SHADER_INSTANCE.safeGetUniform("TypeMult").set(new Vector3f(0.4f,-0.5f,1));
        }
        else if(shouldMeleeSword){
            CFRenderTypes.FEND_SHADER_INSTANCE.safeGetUniform("TypeMult").set(new Vector3f(0,1,-1.0f));
        }
        else{
            CFRenderTypes.FEND_SHADER_INSTANCE.safeGetUniform("TypeMult").set(new Vector3f(0,1,1));
        }
        super.render(entity, entityYaw, partialTicks, poseStack, CFClientProxy.getFendSource(), packedLight);
        CFClientProxy.getFendSource().endBatch();
    }

    @Override
    public ResourceLocation getTextureLocation(FendEntity fendEntity) {
        if(fendEntity.isDeep()){
            return FEND_DEEP_LOCATION;
        }
        return FEND_LOCATION;
    }


    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight,float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
