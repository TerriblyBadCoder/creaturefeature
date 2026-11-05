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

    private static final ResourceLocation EVIL_LOCATION = CreatureFeature.getId("textures/entity/evil.png");
    private static final ResourceLocation MARK_LOCATION = CreatureFeature.getId("textures/entity/markapplier.png");
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
        float scaled = Math.min(1.0f,(float)Math.pow(42.0f/(1.0f+(entity.tickCount+partialTicks)*1.2f),3.0));
        float modulo =Math.min((entity.tickCount+partialTicks)/5.0f,Math.min(1.0f,Mth.sin(((entity.tickCount+partialTicks)/48.0f)*3.14f)*1.4f));
        if(CFRenderTypes.WRAITH_SHADER_INSTANCE!=null){
            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("GameTime").set(entity.tickCount+entity.getId()+partialTicks);
            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("Fade").set(1.0f);
            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("Flipped").set(1.0f);

            CFRenderTypes.WRAITH_SHADER_INSTANCE.safeGetUniform("OffYPosition")
                    .set((float)(entity.getPosition(partialTicks).y- Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().y-3.8+modulo*4.0f));


        }
        float sinused =  (float)Math.pow((50-entity.tickCount-partialTicks),1.5)/96.0f;
        poseStack.pushPose();
        poseStack.scale(1.0f/scaled,scaled,1.0f/scaled);
        VertexConsumer consumer = CFClientProxy.getFendSource().getBuffer(CFRenderTypes.entityWraithCutout(MARK_LOCATION));
        PoseStack.Pose pose = poseStack.last();

        Vec3 vec3_1_mark = new Vec3(1.5,0,0).yRot((float)Math.PI*0+sinused);
        Vec3 vec3_2_mark = new Vec3(1.5,0,0).yRot((float)Math.PI/1.5f+sinused);
        Vec3 vec3_center_mark = new Vec3(1.5,0,0).yRot((float)Math.PI/1.5f*2.0f+sinused).scale(0.5);
        Vec3 vec3_3_mark = vec3_1_mark.scale(-1).add(vec3_center_mark);
        Vec3 vec3_4_mark = vec3_2_mark.scale(-1).add(vec3_center_mark);
        vec3_1_mark = vec3_1_mark.add(vec3_center_mark.scale(-0.15));
        vec3_2_mark = vec3_2_mark.add(vec3_center_mark.scale(-0.15));
        vertex(pose,consumer,vec3_1_mark.x,0.01f, vec3_1_mark.z, 0,0f,0,0,1,packedLight,1);
        vertex(pose,consumer,vec3_2_mark.x,0.01f, vec3_2_mark.z, 0,1f,0,0,1,packedLight,1);
        vertex(pose,consumer,vec3_3_mark.x,0.01f, vec3_3_mark.z, 1,1,0,0,1,packedLight,1);
        vertex(pose,consumer,vec3_4_mark.x,0.01f, vec3_4_mark.z, 1,0,0,0,1,packedLight,1);
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.scale(1.0f/scaled,1.0f,1.0f/scaled);
        consumer = CFClientProxy.getFendSource().getBuffer(CFRenderTypes.entityWraithCutout(EVIL_LOCATION));
        pose = poseStack.last();

        for (int i = 0; i < 3; i++) {
            Vec3 vec3_1 = new Vec3(1.2,0,0).yRot((float)Math.PI*i/1.5f+sinused);
            Vec3 vec3_2 = new Vec3(1.2,0,0).yRot((float)Math.PI/1.5f+(float)Math.PI*i/1.5f+sinused);
            Vec3 vec3_3 = new Vec3(1.2,0,0).yRot((float)Math.PI*i/1.5f+sinused+0.3f);
            Vec3 vec3_4 = new Vec3(1.2,0,0).yRot((float)Math.PI/1.5f+(float)Math.PI*i/1.5f+sinused+0.3f);
            Vec3 vec3_5 = new Vec3(1.2,0,0).yRot((float)Math.PI*i/1.5f+sinused+0.7f);
            Vec3 vec3_6 = new Vec3(1.2,0,0).yRot((float)Math.PI/1.5f+(float)Math.PI*i/1.5f+sinused+0.7f);
            vertex(pose,consumer,vec3_3.x,1.0f, vec3_3.z, i/1.5f,0.5f,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_4.x,1.0f, vec3_4.z, (i+1)/1.5f,0.5f,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_6.x,2.0f, vec3_6.z, (i+1)/1.5f,1f,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_5.x,2.0f, vec3_5.z, i/1.5f,1f,0,0,1,packedLight,1);


            vertex(pose,consumer,vec3_3.x,1.0f, vec3_3.z, i/1.5f,0.5f,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_4.x,1.0f, vec3_4.z, (i+1)/1.5f,0.5f,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_2.x,0.0f, vec3_2.z, (i+1)/1.5f,0,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_1.x,0.0f, vec3_1.z, i/1.5f,0,0,0,1,packedLight,1);

            vertex(pose,consumer,vec3_1.x,0.0f, vec3_1.z, i/1.5f,0,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_2.x,0.0f, vec3_2.z, (i+1)/1.5f,0,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_2.x,-6.0f, vec3_2.z, (i+1)/1.5f,3,0,0,1,packedLight,1);
            vertex(pose,consumer,vec3_1.x,-6.0f, vec3_1.z, i/1.5f,3,0,0,1,packedLight,1);

        }
        poseStack.popPose();
        CFClientProxy.getFendSource().endBatch();
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1,1,1,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal((float)normalX, (float)normalZ, (float)normalY);
    }

    public ResourceLocation getTextureLocation(LightningDelayEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
