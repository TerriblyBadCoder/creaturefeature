package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.CoatOfArmsEntityModel;
import net.atired.creaturefeature.entity.CoatOfArmsEntity;
import net.atired.creaturefeature.init.CFItemInit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class CoatOfArmsEntityRenderer extends MobRenderer<CoatOfArmsEntity, CoatOfArmsEntityModel<CoatOfArmsEntity>> {
    private static final ResourceLocation COATOFARMS_LOCATION = CreatureFeature.getId("textures/entity/coatofarms.png");
    private static final ResourceLocation COAT_LOCATION = CreatureFeature.getId("textures/entity/coat.png");
    private static final ResourceLocation COAT2_LOCATION = CreatureFeature.getId("textures/entity/coat2.png");
    private final ItemRenderer itemRenderer;


    public CoatOfArmsEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CoatOfArmsEntityModel<>(context.bakeLayer(CoatOfArmsEntityModel.LAYER_LOCATION)), 0.6f);
        this.itemRenderer = context.getItemRenderer();

    }

    @Override
    public ResourceLocation getTextureLocation(CoatOfArmsEntity machinationEntity) {
        return COATOFARMS_LOCATION;
    }

    @Override
    public void render(CoatOfArmsEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        float yawEd = -entity.getPreciseBodyRotation(partialTicks)/180.0f*3.14f;
        Vec3 translated = new Vec3(0,0,-0.00).yRot(yawEd);
        float mulTheOther=entity.getOpening()*1.2f;
        float mul = -3.63f+mulTheOther*0.3f;
        float scaled = 1f;
        float aged = (entity.tickCount+partialTicks)/9.0f;
        int overlay = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));
        poseStack.pushPose();
        float zRot = 0f;
        if (entity.deathTime > 0) {
            float f = ((float)entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            zRot=f * this.getFlipDegrees(entity)/90f*3.14f;
            setupRotations(entity,poseStack,0,entityYaw,partialTicks,1f);
            yawEd=3.14f;
        }
        poseStack.translate((float)translated.x ,1.4f,(float)translated.z);
        Vec3 pos = entity.getPosition(partialTicks);
        PoseStack.Pose posed = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(COAT_LOCATION));
        float mulZ=1.0f;
        Vec3 horDir = entity.lerpedDelta.multiply(1,0,1).scale(-1.5);
        if(horDir.length()>horDir.normalize().scale(-1.5).length()){
            horDir=horDir.normalize().scale(-1.5);
        }
        if(mulTheOther>0.1f){
            for (int i = 0; i < 10; i++) {
                float sinused = Mth.sin(aged+i*1.256f/2f);
                float sc2=(1.0f-(sinused/2.0f+0.5f))*mulTheOther;
                sc2=Math.min(1f,sc2*1.2f);
                sc2= (float) Math.pow(sc2,3.0);
                if(sc2>0.2){
                    poseStack.pushPose();
                    poseStack.mulPose(new Quaternionf().rotationXYZ(0f,yawEd+3.14f,0));
                    poseStack.translate(Mth.sin(aged*2.0f+i*1.256f*4.f)/8.0f,Mth.cos(aged+i*1.256f/2f)/1.6f+0.1f,-sc2/2.0f);
                    poseStack.scale(sc2,1,1);

                    this.itemRenderer.renderStatic(CFItemInit.FLINTLOCK.toStack(), ItemDisplayContext.NONE, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
                    poseStack.popPose();
                }

            }

        }
        aged*=2f;
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 2; i++) {
                Vec3 old1 = new Vec3(0.0,0.001,mulZ*-0.34).yRot(yawEd);
                Vec3 old2 = new Vec3(0.0,-1.2,mulZ*-0.6).yRot(yawEd);
                for (int j = 1; j <= 8; j++) {
                    float sinAged = Mth.sin(aged*1.5f+j/1.0f)/8.0f+1.0f;
                    float sinAged2 = Mth.sin(aged*1.5f+(j+1)/1.0f)/8.0f+1.0f;

                    float sinused = 0;
                    float addAfter5=0.0f;
                    if(j>6){
                        if(k>0){
                            break;
                        }
                        addAfter5=-1.4f*(1-(i%2)*2)*mulTheOther;
                    }
                    Vec3 old3 = new Vec3(0.17f*scaled,0,0).yRot((j-0.6f)/8.0f*mul-addAfter5).zRot(sinused).yRot(yawEd).add(old1);
                    Vec3 old4 = new Vec3(0.3f*scaled,0,0).yRot((j-0.6f)/8.0f*mul-addAfter5).zRot(sinused).yRot(yawEd).add(old2);
                    vertex(posed,consumer,old3.x,old3.y,old3.z,(1-(j)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                    vertex(posed,consumer,old1.x,old1.y,old1.z,(1-(j-1)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                    vertex(posed,consumer,old2.x+horDir.x,old2.y*sinAged,old2.z+horDir.z,(1-(j-1)/8.0f),1f,0,1,0,packedLight,1,overlay);
                    vertex(posed,consumer,old4.x+horDir.x,old4.y*sinAged2,old4.z+horDir.z,(1-(j)/8.0f),1f,0,1,0,packedLight,1,overlay);
                    if(k>0){
                        vertex(posed,consumer,0,old3.y,0,(1-(j)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                        vertex(posed,consumer,0,old1.y,0,(1-(j-1)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                        vertex(posed,consumer,old2.x+horDir.x,old2.y*sinAged,old2.z+horDir.z,(1-(j-1)/8.0f),1f,0,1,0,packedLight,1,overlay);
                        vertex(posed,consumer,old4.x+horDir.x,old4.y*sinAged2,old4.z+horDir.z,(1-(j)/8.0f),1f,0,1,0,packedLight,1,overlay);
                        if(j==6){
                            vertex(posed,consumer,old3.x,old3.y,old3.z,(1-(j)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                            vertex(posed,consumer,old4.x+horDir.x,old4.y*sinAged2,old4.z+horDir.z,(1-(j)/8.0f),1f,0,1,0,packedLight,1,overlay);
                            vertex(posed,consumer,0,old4.y*sinAged,0,(1-(j+2)/8.0f),1f,0,1,0,packedLight,1,overlay);
                            vertex(posed,consumer,0,old3.y,0,(1-(j+2)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                        }
                    }
                    old1=old3;
                    old2=old4;
                }
                yawEd+=3.14f;
                mul*=-1.0f;
                mulZ*=-1.0f;
            }
            for (int i = 0; i < 2; i++) {
                Vec3 old1 = new Vec3(0.0,1,mulZ*-0.4).yRot(yawEd);
                Vec3 old2 = new Vec3(0.0,-0.001,mulZ*-0.34).yRot(yawEd);
                for (int j = 1; j <= 8; j++) {
                    float sinAged = -Mth.sin(aged*1.5f+j/1.0f)/12.0f+1.0f;
                    float sinAged2 = -Mth.sin(aged*1.5f+(j+1)/1.0f)/12.0f+1.0f;

                    float sinused = 0;
                    float addAfter5=0.0f;
                    if(j>6){
                        if(k>0){

                            break;
                        }
                        addAfter5=-1.8f*(1-(i%2)*2)*mulTheOther;
                    }
                    Vec3 old3 = new Vec3(0.2f*scaled,0,0).yRot((j-0.6f)/8.0f*mul-addAfter5).zRot(sinused).yRot(yawEd).add(old1);
                    Vec3 old4 = new Vec3(0.17f*scaled,0,0).yRot((j-0.6f)/8.0f*mul-addAfter5).zRot(sinused).yRot(yawEd).add(old2);
                    vertex(posed,consumer,old3.x+horDir.x,old3.y*sinAged2,old3.z+horDir.z,(1-(j)/8.0f),0,0,1,0,packedLight,1,overlay);
                    vertex(posed,consumer,old1.x+horDir.x,old1.y*sinAged,old1.z+horDir.z,(1-(j-1)/8.0f),0,0,1,0,packedLight,1,overlay);
                    vertex(posed,consumer,old2.x,old2.y,old2.z,(1-(j-1)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                    vertex(posed,consumer,old4.x,old4.y,old4.z,(1-(j)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                    if(k>0){
                        vertex(posed,consumer,old3.x+horDir.x,old3.y*sinAged2,old3.z+horDir.z,(1-(j)/8.0f),0,0,1,0,packedLight,1,overlay);
                        vertex(posed,consumer,old1.x+horDir.x,old1.y*sinAged,old1.z+horDir.z,(1-(j-1)/8.0f),0,0,1,0,packedLight,1,overlay);
                        vertex(posed,consumer,0,old2.y,0,(1-(j-1)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                        vertex(posed,consumer,0,old4.y,0,(1-(j)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                        if(j==6){
                            vertex(posed,consumer,old3.x+horDir.x,old3.y*sinAged2,old3.z+horDir.z,(1-(j)/8.0f),0.0f,0,1,0,packedLight,1,overlay);
                            vertex(posed,consumer,old4.x,old4.y,old4.z,(1-(j)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                            vertex(posed,consumer,0,old4.y,0,(1-(j+2)/8.0f),0.5f,0,1,0,packedLight,1,overlay);
                            vertex(posed,consumer,0,old3.y*sinAged,0,(1-(j+2)/8.0f),0.0f,0,1,0,packedLight,1,overlay);
                        }
                    }
                    old1=old3;
                    old2=old4;
                }
                yawEd+=3.14f;
                mulZ*=-1.0f;
                mul*=-1.0f;
            }
            consumer = buffer.getBuffer(RenderType.entityTranslucent(COAT2_LOCATION));
            poseStack.scale(0.96f,1f,0.96f);
        }
        poseStack.popPose();

    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha,int over) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(over).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
