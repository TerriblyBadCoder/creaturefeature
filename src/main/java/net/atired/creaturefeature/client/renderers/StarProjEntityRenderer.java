
package net.atired.creaturefeature.client.renderers;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.Config;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFClientProxy;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.StarProjEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class StarProjEntityRenderer extends EntityRenderer<StarProjEntity> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
    private static final ResourceLocation BLANK_LOCATION = CreatureFeature.getId("textures/entity/suntrail.png");
    public static final ResourceLocation STARPROJ_LOCATION = CreatureFeature.getId("textures/entity/sun_proj.png");
    private static final ResourceLocation STARPROJ_FRONT_LOCATION = CreatureFeature.getId("textures/entity/sun_proj_front.png");

    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;

    public StarProjEntityRenderer(EntityRendererProvider.Context context, float scale, boolean fullBright) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = scale;
        this.fullBright = fullBright;
    }

    protected int getBlockLightLevel(StarProjEntity entity, BlockPos pos) {
        return this.fullBright ? 15 : super.getBlockLightLevel(entity, pos);
    }

    public void render(StarProjEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        float aged = (entity.tickCount+partialTicks)/5.0f+entity.getId();
        if (entity.tickCount >2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25)) {
            poseStack.pushPose();
            float scaled = 1.0f;
            scaled*=this.scale*Mth.clamp(12.0f-(entity.tickCount+partialTicks)/5.0f,0.0f,1.0f);
            poseStack.scale(scaled, scaled, scaled);
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(STARPROJ_FRONT_LOCATION));
            PoseStack.Pose posed = poseStack.last();
            vertex(posed,consumer,0.2,0.2,0.1,0,0,0,0,1,packedLight,1);
            vertex(posed,consumer,0.2,-0.2,0.1,0,1,0,0,1,packedLight,1);
            vertex(posed,consumer,-0.2,-0.2,0.1,1,1,0,0,1,packedLight,1);
            vertex(posed,consumer,-0.2,0.2,0.1,1,0,0,0,1,packedLight,1);

            consumer = (Config.FIX_SAS.isTrue()?buffer:CFClientProxy.getSunSource()).getBuffer(RenderType.entityTranslucent(STARPROJ_LOCATION));
            poseStack.popPose();
            poseStack.pushPose();
            poseStack.scale(scaled, scaled, scaled);
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            posed = poseStack.last();
            Vec3 vec3_1=new Vec3(0.5,0.5,0).zRot(aged);
            Vec3 vec3_2=new Vec3(0.5,-0.5,0).zRot(aged);
            Vec3 vec3_3=new Vec3(-0.5,-0.5,0).zRot(aged);
            Vec3 vec3_4=new Vec3(-0.5,0.5,0).zRot(aged);
            vertex(posed,consumer,vec3_1.x,vec3_1.y,vec3_1.z,0,0,1,1,1,255,1);
            vertex(posed,consumer,vec3_2.x,vec3_2.y,vec3_2.z,0,1,1,1,1,255,1);
            vertex(posed,consumer,vec3_3.x,vec3_3.y,vec3_3.z,1,1,1,1,1,255,1);
            vertex(posed,consumer,vec3_4.x,vec3_4.y,vec3_4.z,1,0,1,1,1,255,1);
            poseStack.popPose();
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
        poseStack.pushPose();
        double d0 = Mth.lerp((double)partialTicks, entity.xOld, entity.getX());
        double d1 = Mth.lerp((double)partialTicks, entity.yOld, entity.getY());
        double d2 = Mth.lerp((double)partialTicks, entity.zOld, entity.getZ());
        Vec3 pos = new Vec3(d0,d1,d2);
        poseStack.translate(-pos.x(),-pos.y(),-pos.z());
        PoseStack.Pose posed = poseStack.last();

        if(CFRenderTypes.STAR_SHADER_INSTANCE.getUniform("Revealness")!=null)
            CFRenderTypes.STAR_SHADER_INSTANCE.getUniform("Revealness").set(entity.getId()*40.6f);
        VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityStarCull(BLANK_LOCATION));
        float ud = partialTicks*0.125f;
        float a =1-partialTicks/7f;

        float evilTickCount = Math.clamp((entity.tickCount+partialTicks-1)/8.0f,0.0f,1.0f);
            for(int i = entity.posTracker-1;i>1;i--){
                Vec3 first = entity.positions[i];
                float scaled = Mth.clamp(10.0f-(entity.tickCount+partialTicks)/5.0f,0.0f,1.0f);
                float scaled2 =Mth.clamp(10.0f-(entity.tickCount+partialTicks)/5.0f,0.0f,1.0f);
                Vec3 second =  entity.positions[i-1];
                if(i==entity.posTracker-1){
                    first=pos;
                }
                Vec3 to = first.subtract(second).multiply(1,0,1).normalize();
                float a2 = Math.clamp(((i-1)/((float)(entity.posTracker-1))),0f,1f)-partialTicks/7f;
                float ud2=ud+0.125f;
                vertex(posed, consumer, first.x, first.y + 0.2f * scaled, first.z, ud, 0.0f, 0, 0, 1, 255, a);
                vertex(posed,consumer, second.x,second.y+0.2f*scaled2,second.z,ud2,0.0f,0,0,1,255,a2);
                vertex(posed,consumer, second.x,second.y-0.2f*scaled2,second.z,ud2,1.0f,0,0,1,255,a2);
                vertex(posed,consumer, first.x,first.y-0.2f*scaled,first.z,ud,1.0f,0,0,1,255,a);
                ud=ud2;
                a =a2;

            }

        poseStack.popPose();

    }

    @Override
    public boolean shouldRender(StarProjEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,alpha,alpha,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
    public ResourceLocation getTextureLocation(StarProjEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
