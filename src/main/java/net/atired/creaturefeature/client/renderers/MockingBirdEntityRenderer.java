package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.Config;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.MockingBirdEntityModel;
import net.atired.creaturefeature.entity.MockingBirdEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MockingBirdEntityRenderer extends MobRenderer<MockingBirdEntity, MockingBirdEntityModel<MockingBirdEntity>> {
    private static final ResourceLocation MOCKINGBIRD_LOCATION = CreatureFeature.getId("textures/entity/mockingbird.png");
    private static final ResourceLocation MOCKINGBIRD_WINGS_LOCATION = CreatureFeature.getId("textures/entity/mockingbird_wings.png");

    private static final ResourceLocation MOCKINGBIRD_DT_LOCATION = CreatureFeature.getId("textures/entity/mockingbird_dt.png");
    private static final ResourceLocation MOCKINGBIRD_WINGS_DT_LOCATION = CreatureFeature.getId("textures/entity/mockingbird_wings_dt.png");

    public MockingBirdEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new MockingBirdEntityModel<>(context.bakeLayer(MockingBirdEntityModel.LAYER_LOCATION)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(MockingBirdEntity machinationEntity) {
        if(!Config.SPEC.isLoaded()){
            return MOCKINGBIRD_LOCATION;
        }
        return Config.REMODEL_MB.get()?MOCKINGBIRD_DT_LOCATION:MOCKINGBIRD_LOCATION;
    }

    @Override
    public void render(MockingBirdEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        if(!Config.SPEC.isLoaded()){
            return;
        }
        float yawEd = -entity.getPreciseBodyRotation(partialTicks)/180.0f*3.14f;
        Vec3 translated = new Vec3(0,0,-0.4).scale(1.0-entity.getWingspan()).yRot(yawEd);
        float mul = 1.0f+ Mth.sin((entity.tickCount+partialTicks)/10.0f)*3.0f*(1.0f+entity.getWingspan()/1.5f)+(entity.getWingspan()-1.0f)*16.0f;
        float scaled = 0.8f+entity.getWingspan()/3.0f;
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
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(Config.REMODEL_MB.get()?MOCKINGBIRD_WINGS_DT_LOCATION:MOCKINGBIRD_WINGS_LOCATION));
        for (int i = 0; i < 2; i++) {
            Vec3 old1 = new Vec3(0.1,1,0).yRot(yawEd);
            Vec3 old2 = new Vec3(0.1,-1,0).yRot(yawEd);
            for (int j = 1; j <= 8; j++) {
                float sinused = Mth.sin((entity.tickCount+partialTicks)/(4.0f)+j/2.0f)/5.0f*(1.7f+entity.getWingspan()+entity.getFlapping()*1.0f);
                Vec3 old3 = new Vec3(0.4f*scaled,0,0).yRot(j/23.0f*mul).zRot(sinused).yRot(yawEd).add(old1);
                Vec3 old4 = new Vec3(0.4f*scaled,0,0).yRot(j/23.0f*mul).zRot(sinused).yRot(yawEd).add(old2);
                vertex(posed,consumer,old3.x,old3.y,old3.z,(1-(j)/8.0f),0,0,1,0,packedLight,1,overlay);
                vertex(posed,consumer,old1.x,old1.y,old1.z,(1-(j-1)/8.0f),0,0,1,0,packedLight,1,overlay);
                vertex(posed,consumer,old2.x,old2.y,old2.z,(1-(j-1)/8.0f),1,0,1,0,packedLight,1,overlay);
                vertex(posed,consumer,old4.x,old4.y,old4.z,(1-(j)/8.0f),1,0,1,0,packedLight,1,overlay);
                old1=old3;
                old2=old4;
            }
            yawEd+=3.14f;
            mul*=-1.0f;
        }
        poseStack.popPose();
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha,int over) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(over).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }

}
