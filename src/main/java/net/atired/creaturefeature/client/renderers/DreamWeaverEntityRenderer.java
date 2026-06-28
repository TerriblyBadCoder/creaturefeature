package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.DreamWeaverEntityModel;
import net.atired.creaturefeature.entity.DreamWeaverEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class DreamWeaverEntityRenderer extends MobRenderer<DreamWeaverEntity, DreamWeaverEntityModel<DreamWeaverEntity>> {
    private static final ResourceLocation DREAMWEAVER_LOCATION = CreatureFeature.getId("textures/entity/dreamweaver.png");

    private final EntityRenderDispatcher entityRenderer;

    public DreamWeaverEntityRenderer(EntityRendererProvider.Context context) {

        super(context, new DreamWeaverEntityModel<>(context.bakeLayer(DreamWeaverEntityModel.LAYER_LOCATION)), 0.8f);
        this.entityRenderer = context.getEntityRenderDispatcher();
        this.addLayer(new DreamWeaverSilkLayer<>(this,context.getModelSet()));
        this.addLayer(new DreamWeaverSilk2Layer<>(this,context.getModelSet()));
        this.addLayer(new DreamWeaverEyesLayer<>(this));


    }

    @Override
    public ResourceLocation getTextureLocation(DreamWeaverEntity machinationEntity) {
        return DREAMWEAVER_LOCATION;
    }

    @Override
    public void render(DreamWeaverEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Level level = entity.level();

        if (level != null&&(entity.getRupturing()<-0.9f||entity.getRupturing()>0.05f)&&!entity.isDeadOrDying()&&entity.theSummonee!=null) {
            Entity entity2 = entity.theSummonee;
            if (entity2 != null) {
                renderEntityInSpawner(partialTicks, poseStack, buffer, packedLight, entity2, this.entityRenderer, 0, 0,entity);
            }
        }

        float scaled= Math.min(1.0f,(entity.tickCount+partialTicks)/8.0f);
        if(entity.getRupturing()==-1.0f){
            scaled=1.0f;
        }
        if(entity.tickCount>0){
            CFRenderTypes.SILK_SHADER_INSTANCE.safeGetUniform("OffYPosition")
                    .set((float)(entity.getPosition(partialTicks).y- Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y));
        }
        poseStack.pushPose();
        poseStack.scale(scaled,scaled,scaled);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
    public static void renderEntityInSpawner(float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, Entity entity, EntityRenderDispatcher entityRenderer, double oSpin, double spin,DreamWeaverEntity dreamWeaverEntity) {
        poseStack.pushPose();
        float scaled = 1.0f;
        if(dreamWeaverEntity.getRupturing()>-0.6f){
            scaled*=dreamWeaverEntity.getRupturing()*(1.0f+Mth.sin(dreamWeaverEntity.getRupturing()/1.4f*3.14f)*2.0f);
        }
        float f = 0.5125F*scaled;
        float f1 = Math.max(entity.getBbWidth()*1.6f, entity.getBbHeight()*1.2f);
        if ((double)f1 > 1.0) {
            f /= f1;
        }
        CFRenderTypes.SILK_SHADER_INSTANCE.safeGetUniform("OffYPosition")
                .set(-300.5f);

        float timed = (dreamWeaverEntity.tickCount+partialTick)*10.0f;
        Vector3f dir = new Vector3f(0,1.4f,-0.65f).rotateY(-Mth.rotLerp(partialTick, dreamWeaverEntity.yBodyRotO, dreamWeaverEntity.yBodyRot)/180.0f*3.14f).mul(scaled);
        poseStack.translate(dir.x, dir.y, dir.z);
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(timed/40.0f)*20.0f));
        poseStack.mulPose(Axis.YP.rotationDegrees(timed));
        poseStack.translate(0,-entity.getBbHeight()/2.0f*f,0);
        poseStack.scale(f, f, f);
        entityRenderer.render(entity, 0.0, 0.0, 0.0, 0.0F, partialTick, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
