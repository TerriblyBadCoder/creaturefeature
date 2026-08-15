package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.Config;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.FeatherEntity;
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
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class FeatherEntityRenderer extends EntityRenderer<FeatherEntity> {
    private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
    private static final ResourceLocation BLANK_LOCATION = CreatureFeature.getId("textures/entity/feather_trail.png");
    private static final ResourceLocation BLANK_LOCATION_DB = CreatureFeature.getId("textures/entity/feather_trail_dt.png");
    private static final ResourceLocation FEATHER_LOCATION = CreatureFeature.getId("textures/entity/feather.png");
    private static final ResourceLocation FEATHER1_LOCATION = CreatureFeature.getId("textures/entity/feather_hue0.png");
    private static final ResourceLocation FEATHER2_LOCATION = CreatureFeature.getId("textures/entity/feather_hue1.png");
    private static final ResourceLocation[] FEATHERS = {FEATHER1_LOCATION,FEATHER_LOCATION,FEATHER2_LOCATION};
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;

    public FeatherEntityRenderer(EntityRendererProvider.Context context, float scale, boolean fullBright) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.scale = scale;
        this.fullBright = fullBright;
    }

    protected int getBlockLightLevel(FeatherEntity entity, BlockPos pos) {
        return this.fullBright ? 15 : super.getBlockLightLevel(entity, pos);
    }

    public void render(FeatherEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(!Config.SPEC.isLoaded()){
            return;
        }
        if (entity.tickCount >2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25)) {
            poseStack.pushPose();
            float scaled = 1.0f-Mth.clamp(20.0f-entity.tickCount/10.0f,0,1);
            poseStack.scale(this.scale, this.scale, this.scale);
            poseStack.mulPose(new Quaternionf().rotationZYX(0,entity.lastYaw,entity.lastPitch));
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(Config.REMODEL_MB.get()?FEATHER_LOCATION:FEATHERS[(entity.getId()%3)]));
            PoseStack.Pose posed = poseStack.last();
            vertex(posed,consumer,0.5,0,0.5,0,0,0,0,1,packedLight,1);
            vertex(posed,consumer,0.5,0,-0.5,0,1,0,0,1,packedLight,1);
            vertex(posed,consumer,-0.5,0,-0.5,1,1,0,0,1,packedLight,1);
            vertex(posed,consumer,-0.5,0,0.5,1,0,0,0,1,packedLight,1);
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
        if(CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness")!=null)
            CFRenderTypes.AMBUSH_SHADER_INSTANCE.getUniform("Revealness").set(1.0f);
        VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityAmbushCutout(Config.REMODEL_MB.get()?BLANK_LOCATION_DB:BLANK_LOCATION));
        float ud = 0.0f;
        float evilTickCount = Math.clamp((entity.tickCount+partialTicks-1)/8.0f,0.0f,1.0f);
        for (int j = 0; j < 2; j++) {
            for(int i = entity.posTracker-1;i>0;i--){
                Vec3 first = entity.positions[i];
                float scaled = 1f;
                float scaled2 = 1f;
                Vec3 second =  entity.positions[i-1];
                if(i==entity.posTracker-1){
                    first=entity.getPosition(partialTicks);
                }
                Vec3 to = first.subtract(second).multiply(1,0,1).normalize();
                float a = (i/((float)entity.posTracker))*1.5f;
                a=Math.min(1.0f,a)*evilTickCount;
                float a2 = ((i-1)/((float)entity.posTracker))*1.5f;
                a2=Math.min(1.0f,a2)*evilTickCount;
                float ud2=ud+(float)first.distanceTo(second)*1.0f;
                if(j==0){
                    vertex(posed,consumer, first.x,first.y+0.2f*scaled,first.z,ud,0.0f,0,-1,0,packedLight,a);
                    vertex(posed,consumer, second.x,second.y+0.2f*scaled2,second.z,ud2,0.0f,0,-1,0,packedLight,a2);
                    vertex(posed,consumer, second.x,second.y-0.2f*scaled2,second.z,ud2,1.0f,0,-1,0,packedLight,a2);
                    vertex(posed,consumer, first.x,first.y-0.2f*scaled,first.z,ud,1.0f,0,-1,0,packedLight,a);
                }else{
                    float angle = (float)Math.atan2(to.x,to.z);
                    Vec3 otherAdd = new Vec3(0.1,0,0.0).yRot(angle);
                    float angle2 = entity.getYRot();
                    if(i>1){
                        to = second.subtract(entity.positions[i-2]).multiply(1,0,1).normalize();
                        angle2 = (float)Math.atan2(to.x,to.z);
                    }
                    Vec3 otherAdd2 = new Vec3(0.1,0,0.0).yRot(angle2);
                    vertex(posed,consumer, first.x+otherAdd.x,first.y,first.z+otherAdd.z,ud,0.0f,0,-1,0,packedLight,a);
                    vertex(posed,consumer, second.x+otherAdd2.x,second.y,second.z+otherAdd2.z,ud2,0.0f,0,-1,0,packedLight,a2);
                    vertex(posed,consumer, second.x-otherAdd2.x,second.y,second.z-otherAdd2.z,ud2,1.0f,0,-1,0,packedLight,a2);
                    vertex(posed,consumer, first.x-otherAdd.x,first.y,first.z-otherAdd.z,ud,1.0f,0,-1,0,packedLight,a);
                }
                ud=ud2;
            }
        }

        poseStack.popPose();

    }

    @Override
    public boolean shouldRender(FeatherEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
    public ResourceLocation getTextureLocation(FeatherEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
