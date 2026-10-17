package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.blocks.CarapaceBlock;
import net.atired.creaturefeature.client.CFClientProxy;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.LightningDelayEntity;
import net.atired.creaturefeature.init.CFBlockInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Unique;

public class LightningDelayEntityRenderer extends EntityRenderer<LightningDelayEntity> {

    @Unique
    private static final ResourceLocation BLANK_LOCATION = CreatureFeature.getId("textures/entity/evil.png");
    public LightningDelayEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public boolean shouldRender(LightningDelayEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(LightningDelayEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        float modulo =Math.max(0,Mth.sin((entity.tickCount+partialTicks)/24.0f*3.14f));
        if(CFRenderTypes.WRAITH_SHADER_INSTANCE!=null){
            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("GameTime").set(entity.tickCount+entity.getId()+partialTicks);
            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("Fade").set(modulo);
            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("Flipped").set(1.0f);

            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("OffYPosition")
                    .set((float)(entity.getPosition(partialTicks).y- Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y));


        }
        float sinused =  (float)Math.pow((26-entity.tickCount-partialTicks),1.5)/24.0f;
        poseStack.pushPose();
        VertexConsumer consumer = CFClientProxy.getFendSource().getBuffer(CFRenderTypes.entityWraithCutout(BLANK_LOCATION));
        PoseStack.Pose pose = poseStack.last();
        for (int i = 0; i < 3; i++) {
            Vec3 vec3_1 = new Vec3(1.2,0,0).yRot(-(float)Math.PI/3.0f+(float)Math.PI*i/1.5f+sinused);
            Vec3 vec3_2 = new Vec3(1.2,0,0).yRot((float)Math.PI/3.0f+(float)Math.PI*i/1.5f+sinused);
            vertex2(pose,consumer,vec3_1.x,1.0f, vec3_1.z, i/3.0f,0,0,0,1,packedLight,1);
            vertex2(pose,consumer,vec3_2.x,1.0f, vec3_2.z, (i+1)/3.0f,0,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_2.x,0.0f, vec3_2.z, (i+1)/3.0f,1,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_1.x,0.0f, vec3_1.z, i/3.0f,1,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_1.x,0.0f, vec3_1.z, i/3.0f,0,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_2.x,0.0f, vec3_2.z, (i+1)/3.0f,0,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_2.x,-6.0f, vec3_2.z, (i+1)/3.0f,6,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_1.x,-6.0f, vec3_1.z, i/3.0f,6,0,0,1,packedLight,1);

        }
        poseStack.popPose();
        CFClientProxy.getFendSource().endBatch();
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1,1,1,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal((float)normalX, (float)normalZ, (float)normalY);
    }
    public void vertex2(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1,1,1,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal((float)normalX, (float)normalZ, (float)normalY);
    }

    public ResourceLocation getTextureLocation(LightningDelayEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
