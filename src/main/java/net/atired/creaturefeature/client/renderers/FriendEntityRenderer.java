package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.Config;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFClientProxy;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.FriendEntity;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FriendEntityRenderer extends EntityRenderer<FriendEntity> {
    public static final ResourceLocation CUBE_LOCATION = CreatureFeature.getId("textures/entity/cube.png");
    public static final ResourceLocation SMILE_LOCATION = CreatureFeature.getId("textures/entity/smile.png");
    public static final ResourceLocation CUBE_FRIENDLESS_LOCATION = CreatureFeature.getId("textures/entity/cube_friendless.png");
    public static final ResourceLocation SMILE_FRIENDLESS_LOCATION = CreatureFeature.getId("textures/entity/smile_friendless.png");

    public FriendEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FriendEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        boolean friendless = Config.REMODEL_FRIEND.isTrue();
        float flattened =1.0f;
        if(p_entity.lerpedFriendFlattening>0.0f){
            flattened-=Math.min(p_entity.lerpedFriendFlattening,1.0f)*0.9f;
        }
        poseStack.pushPose();
        float scaled = p_entity.isDecaying()?0.66f:1.0f;
        if(scaled<1.0f){
            scaled-=0.5f*(Math.max(0.0f,p_entity.tickCount/20.0f-7.5f));
        }
        MultiBufferSource source = (Minecraft.getInstance().options.graphicsMode().get()==GraphicsStatus.FABULOUS?bufferSource:CFClientProxy.getFriendSource());
        scaled*=1.0f-Math.max(0.0f,1.0f-p_entity.tickCount/10.0f);
        poseStack.scale(scaled,scaled,scaled);
        poseStack.pushPose();
        float timed = (p_entity.tickCount+partialTick)/20.0f;
        VertexConsumer consumer = source.getBuffer(RenderType.entityTranslucent(friendless?SMILE_FRIENDLESS_LOCATION:SMILE_LOCATION));
        Vec3 thisToCam = Minecraft.getInstance().gameRenderer.getMainCamera().getEntity().getPosition(partialTick).subtract(p_entity.getPosition(partialTick));
        double length = thisToCam.length()/5.0d;
        thisToCam=thisToCam.normalize().scale(Math.max(length,2.5));
        length = Math.clamp(length*flattened-0.7f,0.0f,1.0f);
        poseStack.translate(thisToCam.x,1.3*flattened+thisToCam.y+Mth.sin(timed*2.0f)/8.0,thisToCam.z);
        Quaternionf cam=Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
    poseStack.mulPose(cam);

        Vec3 normal = new Vec3(new Vector3f(0,0,1));
        PoseStack.Pose pose = poseStack.last();
        Vec3 vec3_1 = new Vec3(-0.66,0.66*flattened,0.0);
        Vec3 vec3_2 = new Vec3(-0.66,-0.66*flattened,0.0);
        Vec3 vec3_3 = new Vec3(0.66,-0.66*flattened,0.0);
        Vec3 vec3_4 = new Vec3(0.66,0.66*flattened,0.0);
        if(length>0.04){
            vertex(pose,consumer,vec3_1.x,vec3_1.y,vec3_1.z,0f,0f,(float)normal.x,(float)normal.y,(float)normal.z,255,(float)length);
            vertex(pose,consumer,vec3_2.x,vec3_2.y,vec3_2.z,0f,1f,(float)normal.x,(float)normal.y,(float)normal.z,255,(float)length);
            vertex(pose,consumer,vec3_3.x,vec3_3.y,vec3_3.z,1f,1f,(float)normal.x,(float)normal.y,(float)normal.z,255,(float)length);
            vertex(pose,consumer,vec3_4.x,vec3_4.y,vec3_4.z,1f,0f,(float)normal.x,(float)normal.y,(float)normal.z,255,(float)length);
        }




        poseStack.popPose();
        poseStack.pushPose();
        float[] lowerScaled = new float[]{
                1.0f+ Mth.sin(timed*5.0f)/6.0f,
                1.0f+ Mth.sin(timed*5.0f+(float)Math.PI*0.5f)/6.0f,
                1.0f+ Mth.sin(timed*5.0f+(float)Math.PI*1.0f)/6.0f,
                1.0f+ Mth.sin(timed*5.0f+(float)Math.PI*1.5f)/6.0f};
        if(CFRenderTypes.FRIEND_SHADER_INSTANCE!=null){
            CFRenderTypes.FRIEND_SHADER_INSTANCE.safeGetUniform("Time").set(timed/2.0f);
        }
        Vec3 delta = p_entity.movementLerped.multiply(2,0,2);
         consumer = source.getBuffer(CFRenderTypes.entityFriendCutout(friendless?CUBE_FRIENDLESS_LOCATION:CUBE_LOCATION));
         pose = poseStack.last();
        for (int i = 0; i < 4; i++) {
            Vec3 vec3_11 = new Vec3(2,0,0).scale(lowerScaled[i]).yRot(timed-(float)Math.PI/4.0f+i*(float)Math.PI/2.0f);
            Vec3 vec3_21 = new Vec3(2,0,0).scale(lowerScaled[(i+1)%4]).yRot(timed+(float)Math.PI/4.0f+i*(float)Math.PI/2.0f);
            Vec3 vec3_31 = vec3_11.scale(1.0f/lowerScaled[i]).scale(0.66).yRot(0.2f).add(0,2.1*flattened,0).add(delta);
            Vec3 vec3_41 = vec3_21.scale(1.0f/lowerScaled[(i+1)%4]).scale(0.66).yRot(0.2f).add(0,2.1*flattened,0).add(delta);
            vertex(pose,consumer,vec3_41.x,vec3_41.y,vec3_41.z,1f,1f,0,-1,0,255,1.0f);
            vertex(pose,consumer,vec3_31.x,vec3_31.y,vec3_31.z,0f,1f,0,-1,0,255,1.0f);
            vertex(pose,consumer,vec3_11.x,vec3_11.y,vec3_11.z,0f,0f,0,-1,0,255,1.0f);
            vertex(pose,consumer,vec3_21.x,vec3_21.y,vec3_21.z,1f,0f,0,-1,0,255,1.0f);

        }
        vec3_1 = new Vec3(2*lowerScaled[0],0.005,0).yRot(timed-(float)Math.PI/4.0f);
        vec3_2 = new Vec3(2*lowerScaled[1],0.005,0).yRot(timed+(float)Math.PI/4.0f);
        vec3_3 = new Vec3(2*lowerScaled[2],0.005,0).yRot(timed+(float)Math.PI*0.75f);
        vec3_4 = new Vec3(2*lowerScaled[3],0.005,0).yRot(timed+(float)Math.PI*1.25f);

        for (int i = 0; i < 2; i++) {
            vertex(pose,consumer,vec3_1.x,vec3_1.y,vec3_1.z,1f,1f,0,-1,0,255,1.0f);
            vertex(pose,consumer,vec3_2.x,vec3_2.y,vec3_2.z,0f,1f,0,-1,0,255,1.0f);
            vertex(pose,consumer,vec3_3.x,vec3_3.y,vec3_3.z,0f,0f,0,-1,0,255,1.0f);
            vertex(pose,consumer,vec3_4.x,vec3_4.y,vec3_4.z,1f,0f,0,-1,0,255,1.0f);

            vec3_1 = vec3_1.subtract(0,0.005,0).scale(1.0f/lowerScaled[0]).scale(0.66).yRot(0.2f).add(delta).add(0,2.1*flattened,0);
            vec3_2 = vec3_2.subtract(0,0.005,0).scale(1.0f/lowerScaled[1]).scale(0.66).yRot(0.2f).add(delta).add(0,2.1*flattened,0);
            vec3_3 = vec3_3.subtract(0,0.005,0).scale(1.0f/lowerScaled[2]).scale(0.66).yRot(0.2f).add(delta).add(0,2.1*flattened,0);
            vec3_4 = vec3_4.subtract(0,0.005,0).scale(1.0f/lowerScaled[3]).scale(0.66).yRot(0.2f).add(delta).add(0,2.1*flattened,0);
        }
        thisToCam=thisToCam.multiply(1,0,1).normalize().yRot((float)Math.PI/2.0f);
        flattened=1.0f-flattened;
        for (int i = 0; i < 2; i++) {
            vec3_1 = thisToCam.scale(1.1).add(delta).add(0,1.2-flattened*1.2,0);
            vec3_2 = thisToCam.scale(0.4).add(delta).add(0,2.1-flattened*2.1,0);
            vec3_3 = thisToCam.scale(friendless?2.0:1.0).add(delta).add(0,(friendless?2.0:3.0)-flattened*1.2,0);
            vec3_4 = thisToCam.scale(friendless?2.0:1.0).add(delta).add(0,(friendless?2.0:3.0)-flattened*1.2,0);
            vertex(pose,consumer,vec3_1.x,vec3_1.y,vec3_1.z,0f,0f,0,-1,0,255,1.0f);
            vertex(pose,consumer,vec3_4.x,vec3_4.y,vec3_4.z,0.0f,1f,0,-1,0,255,1.0f);
            vertex(pose,consumer,vec3_3.x,vec3_3.y,vec3_3.z,1.0f,1f,0,-1,0,255,1.0f);
            vertex(pose,consumer,vec3_2.x,vec3_2.y,vec3_2.z,1f,0f,0,-1,0,255,1.0f);
            thisToCam=thisToCam.yRot(3.14f);
        }
        poseStack.popPose();


        poseStack.popPose();


        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, float normalX, float normalY, float normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal( (float)normalX, (float)normalZ, (float)normalY);
    }
    @Override
    public ResourceLocation getTextureLocation(FriendEntity friendEntity) {
        return CUBE_LOCATION;
    }
}
