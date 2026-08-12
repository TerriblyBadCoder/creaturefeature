package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFClientProxy;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.SaintSolisEntityModel;
import net.atired.creaturefeature.entity.SaintSolisEntity;
import net.atired.creaturefeature.misc.IcoSphere;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class SaintSolisEntityRenderer extends MobRenderer<SaintSolisEntity, SaintSolisEntityModel<SaintSolisEntity>> {
    public static final ResourceLocation SAINT_SOLIS_LOCATION = CreatureFeature.getId("textures/entity/saint_solis.png");
    public static final ResourceLocation SUN_LOCATION = CreatureFeature.getId("textures/entity/sun.png");

    public SaintSolisEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SaintSolisEntityModel<>(context.bakeLayer(SaintSolisEntityModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(SaintSolisEntity machinationEntity) {
        return SAINT_SOLIS_LOCATION;
    }



    @Override
    public void render(SaintSolisEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.pushPose();
        Vec3 dir = new Vec3(0,0,-0.1).yRot(-entity.getPreciseBodyRotation(partialTicks)/180.0f*3.14f);
        poseStack.translate(dir.x,1.25,dir.z);
        Vector2f divisor = new Vector2f(3.0f,3.0f);
        VertexConsumer consumer = CFClientProxy.getSunSource().getBuffer(RenderType.entityTranslucent(SUN_LOCATION));
        IcoSphere sphere = CreatureFeature.ICO;
        float gotten = entity.getOpening();
        float timed = (entity.tickCount+partialTicks+entity.getId()*5.0f)/20.0f;
        Vector3f sunDir = entity.getDir();
        for (int i = 0; i < sphere.index.size(); i+=3) {
            poseStack.pushPose();
            Vec3 one = sphere.pos.get(sphere.index.get(i)).scale(0.6).xRot(timed);
            Vec3 two = sphere.pos.get(sphere.index.get(i+1)).scale(0.6).xRot(timed);
            Vec3 three = sphere.pos.get(sphere.index.get(i+2)).scale(0.6).xRot(timed);
            double oneAlpha=Math.max(1.0,Math.max(0.0,one.normalize().dot(new Vec3(sunDir))*5.0-4.0)*2.5*gotten+1.0);
            one=one.scale(oneAlpha);
            double twoAlpha=Math.max(1.0,Math.max(0.0,two.normalize().dot(new Vec3(sunDir))*5.0-4.0)*2.5*gotten+1.0);
            two=two.scale(twoAlpha);
            double threeAlpha=Math.max(1.0,Math.max(0.0,three.normalize().dot(new Vec3(sunDir))*5.0-4.0)*2.5*gotten+1.0);
            three=three.scale(threeAlpha);
            double mul1=Math.sin(timed*-6.0+one.y*3.0*3.14)/8.0;
            one=one.multiply(mul1+1.0,1,mul1+1.0);
            double mul2=Math.sin(timed*-6.0+two.y*3.0*3.14)/8.0;
            two=two.multiply(mul2+1.0,1,mul2+1.0);
            double mul3=Math.sin(timed*-6.0+three.y*3.0*3.14)/8.0;
            three=three.multiply(mul3+1.0,1,mul3+1.0);
            Vector2f vec2f = sphere.uvs.get(sphere.index.get(i+2)).add(sphere.uvs.get(sphere.index.get(i+1)).mul(-1, new Vector2f()),new Vector2f()).mul(-0.1f,new Vector2f());
            float[] v = new float[]{(float) sphere.uvs.get(sphere.index.get(i)).x/divisor.x, (float) sphere.uvs.get(sphere.index.get(i+1)).x/divisor.x, (float) sphere.uvs.get(sphere.index.get(i+2)).x/divisor.x, sphere.uvs.get(sphere.index.get(i)).x/divisor.x};
            float[] u = new float[]{(float) sphere.uvs.get(sphere.index.get(i)).y/divisor.y, (float) sphere.uvs.get(sphere.index.get(i+1)).y/divisor.y, (float) sphere.uvs.get(sphere.index.get(i+2)).y/divisor.y,(float) sphere.uvs.get(sphere.index.get(i)).y/divisor.y};
            mul1=mul1*1.0f+7.0/8.0f;
            mul2=mul2*1.0f+7.0/8.0f;
            mul3=mul3*1.0f+7.0/8.0f;

            PoseStack.Pose pose = poseStack.last();
            vertex(pose,consumer,one.x,one.y,one.z,u[0],v[0],0,0,1,255,Math.max(0.0f,2.0f-(float)oneAlpha),0.5f+(float)mul1*0.5f,(float)mul1,(float)mul1);
            vertex(pose,consumer,two.x,two.y,two.z,u[1],v[1],0,0,1,255,Math.max(0.0f,2.0f-(float)twoAlpha),0.5f+(float)mul2*0.5f,(float)mul2,(float)mul2);
            vertex(pose,consumer,three.x,three.y,three.z,u[2],v[2],0,0,1,255,Math.max(0.0f,2.0f-(float)threeAlpha),0.5f+(float)mul3*0.5f,(float)mul3,(float)mul3);
            vertex(pose,consumer,one.x,one.y,one.z,u[3],v[3],0,0,1,255,Math.max(0.0f,2.0f-(float)oneAlpha),0.5f+(float)mul1*0.5f,(float)mul1,(float)mul1);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha,float r,float g,float b) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(r,g,b,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
