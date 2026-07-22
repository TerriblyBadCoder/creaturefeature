package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.entity.SpatBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Iterator;

public class SpatBlockEntityRenderer extends EntityRenderer<SpatBlockEntity> {
    private final BlockRenderDispatcher dispatcher;

    private static final ResourceLocation CRIT_LOCATION = CreatureFeature.getId("textures/entity/spores_spit.png");

    public SpatBlockEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(SpatBlockEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(entity.state!=null){
            BlockState blockstate = entity.state;
            poseStack.pushPose();
            float aged = (entity.tickCount+partialTick)/4.0f;
            float scaleAge = Math.min(1.0f,aged/1.0f);
            BlockPos blockpos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
            poseStack.scale(scaleAge*0.8f,scaleAge*0.8f,scaleAge*0.8f);
            //
            scaleAge = Math.min(1.0f,aged/4.0f);
            poseStack.translate(0, 0.5, 0);
            poseStack.mulPose(new Quaternionf().rotationZYX(0,aged,aged));
            poseStack.translate(0, -0.5, 0);
            //
            poseStack.translate(-0.5, 0.0, -0.5);
            BakedModel model = this.dispatcher.getBlockModel(blockstate);
            Iterator var11 = model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(BlockPos.ZERO)), ModelData.EMPTY).iterator();

            while(var11.hasNext()) {
                RenderType renderType = (RenderType)var11.next();
                this.dispatcher.getModelRenderer().tesselateBlock(entity.level(), this.dispatcher.getBlockModel(blockstate), blockstate, blockpos, poseStack, bufferSource.getBuffer(RenderTypeHelper.getMovingBlockRenderType(renderType)), false, RandomSource.create(), blockstate.getSeed(BlockPos.ZERO), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
            }
            for (int j = 0; j < 2; j++) {
                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.scale(0.8f,0.8f,0.8f);
                poseStack.mulPose(new Quaternionf().rotationXYZ(j*1.57f/2.0f,j*1.57f/2.0f,0));
                PoseStack.Pose pose = poseStack.last();
                for (int i = 0; i < 4; i++) {
                    Vec3 dir1=new Vec3(1,0.77,0).yRot((float)Math.PI/4.0f+(float)Math.PI*i/2.0f);
                    Vec3 dir2=new Vec3(1,0.77,0).yRot((float)Math.PI/4.0f+(float)Math.PI*(i+1)/2.0f);
                    Vec3 dir3=new Vec3(1,-0.77,0).yRot((float)Math.PI/4.0f+(float)Math.PI*(i+1)/2.0f);
                    Vec3 dir4=new Vec3(1.,-0.77,0).yRot((float)Math.PI/4.0f+(float)Math.PI*i/2.0f);
                    Vector3f vector3f = new Vector3f(0,0,0);
                    VertexConsumer consumer = bufferSource.getBuffer(CFRenderTypes.entityBlossomCull(CRIT_LOCATION));

                    vertex(pose,consumer,dir1.x,dir1.y,dir1.z,0,0,vector3f.x,-1,vector3f.z,255,scaleAge);
                    vertex(pose,consumer,dir2.x,dir2.y,dir2.z,1,0,vector3f.x,-1,vector3f.z,255,scaleAge);
                    vertex(pose,consumer,dir3.x,dir3.y,dir3.z,1,1,vector3f.x,-1,vector3f.z,255,scaleAge);
                    vertex(pose,consumer,dir4.x,dir4.y,dir4.z,0,1,vector3f.x,-1,vector3f.z,255,scaleAge);

                }
                VertexConsumer consumer = bufferSource.getBuffer(CFRenderTypes.entityBlossomCull(CRIT_LOCATION));

                float y =-1.0f;
                Vec3 dir1=new Vec3(1,y*0.77,0).yRot((float)Math.PI/4.0f);
                Vec3 dir2=new Vec3(1,y*0.77,0).yRot((float)Math.PI/4.0f+(float)Math.PI*.5f);
                Vec3 dir3=new Vec3(1,y*0.77,0).yRot((float)Math.PI/4.0f+(float)Math.PI);
                Vec3 dir4=new Vec3(1,y*0.77,0).yRot((float)Math.PI/4.0f+(float)Math.PI*1.5f);

                vertex(pose,consumer,dir1.x,dir1.y,dir1.z,0,0,0,y,0,255,scaleAge);
                vertex(pose,consumer,dir2.x,dir2.y,dir2.z,1,0,0,y,0,255,scaleAge);
                vertex(pose,consumer,dir3.x,dir3.y,dir3.z,1,1,0,y,0,255,scaleAge);
                vertex(pose,consumer,dir4.x,dir4.y,dir4.z,0,1,0,y,0,255,scaleAge);

                vertex(pose,consumer,dir1.x,-dir1.y,dir1.z,0,0,0,y,0,255,scaleAge);
                vertex(pose,consumer,dir4.x,-dir4.y,dir4.z,0,1,0,y,0,255,scaleAge);
                vertex(pose,consumer,dir3.x,-dir3.y,dir3.z,1,1,0,y,0,255,scaleAge);
                vertex(pose,consumer,dir2.x,-dir2.y,dir2.z,1,0,0,y,0,255,scaleAge);
                poseStack.popPose();
            }
            poseStack.popPose();
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, float normalX, float normalY, float normalZ, int packedLight,float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(1.0f,1.0f,1.0f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, (float)normalX, (float)normalZ, (float)normalY);
    }
    @Override
    public ResourceLocation getTextureLocation(SpatBlockEntity entity) {
        return CRIT_LOCATION;
    }
}
