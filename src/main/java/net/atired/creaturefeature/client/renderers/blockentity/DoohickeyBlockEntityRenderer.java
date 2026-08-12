package net.atired.creaturefeature.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.blocks.blockentities.DoohickeyBlockEntity;
import net.atired.creaturefeature.client.CFClientProxy;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.atired.creaturefeature.init.CFItemInit;
import net.atired.creaturefeature.misc.IcoSphere;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class DoohickeyBlockEntityRenderer implements BlockEntityRenderer<DoohickeyBlockEntity> {
    private static final ResourceLocation BLANK_LOCATION = CreatureFeature.getId("textures/entity/blank.png");
    private final ItemRenderer itemRenderer;

    public DoohickeyBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }
    @Override
    public void render(DoohickeyBlockEntity doohickeyBlockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int i2, int i1) {
        float ages = 0;
        if(doohickeyBlockEntity.placedItem!=null&&!doohickeyBlockEntity.placedItem.isEmpty()){
            poseStack.pushPose();
            int seed = (int)doohickeyBlockEntity.getBlockPos().asLong();
            ages= (doohickeyBlockEntity.aged+tickDelta)/12.0f+seed%100;
            poseStack.translate(0,1.3+ Mth.sin(ages)/8.0f,0);
            poseStack.translate(0.5,0,0.5);
            poseStack.mulPose(new Quaternionf().rotationXYZ(0,ages,0));
            this.itemRenderer.renderStatic(doohickeyBlockEntity.placedItem, ItemDisplayContext.GROUND,i2,i1,poseStack,multiBufferSource,doohickeyBlockEntity.getLevel(),seed);
            poseStack.popPose();
            if(Minecraft.getInstance().getCameraEntity()!=null&&Minecraft.getInstance().gameRenderer.getMainCamera().getPosition().distanceTo(doohickeyBlockEntity.getBlockPos().getCenter())<8.6){
                if(doohickeyBlockEntity.placedItem.getItem()== CFItemInit.SLEEPING_POWDER.asItem()){
                    CreatureFeatureClient.PROXY.eebyDeebyNess+=0.05f;
                    if(CreatureFeatureClient.PROXY.eebyDeebyNess>0.98f){
                        CreatureFeatureClient.PROXY.eebyDeebyNess=1.1f;
                    }
                }
                else if(doohickeyBlockEntity.placedItem.getItem()== CFItemInit.MINEDFLAYER_GOOP.asItem()){
                    CreatureFeatureClient.PROXY.flayed2+=0.05f;
                }
                else if(doohickeyBlockEntity.placedItem.getItem()== CFItemInit.VERTIGO_HORN.asItem()){
                    CreatureFeatureClient.PROXY.rabies2+=0.05f;
                }
                else if(doohickeyBlockEntity.placedItem.getItem()== CFItemInit.BACTERIUM_BALL.asItem()){
                    CreatureFeatureClient.PROXY.bacterial+=0.05f;
                    if(CreatureFeatureClient.PROXY.bacterial>0.94f){
                        CreatureFeatureClient.PROXY.bacterial=1.1f;
                    }
                }
            }

        }
        if(doohickeyBlockEntity.ranged>0){
            if(ages==0){
                int seed = (int)doohickeyBlockEntity.getBlockPos().asLong();
                ages= (doohickeyBlockEntity.aged+tickDelta)/12.0f+seed%100;
            }
            IcoSphere sphere = CreatureFeature.ICO;
            Vector2f divisor = new Vector2f(3.0f,3.0f);
            VertexConsumer consumer = multiBufferSource.getBuffer(CFRenderTypes.entityOutlinedCutout(BLANK_LOCATION));
            float scaled = doohickeyBlockEntity.ranged;
            Vector3f col = doohickeyBlockEntity.color;
            for (int i = 0; i < sphere.index.size(); i+=3) {
                poseStack.pushPose();
                poseStack.translate(0,1.5,0);
                poseStack.translate(0.5,0,0.5);
                Vec3 one = sphere.pos.get(sphere.index.get(i)).scale(scaled*8.6).yRot(-ages/8.0f);
                Vec3 two = sphere.pos.get(sphere.index.get(i+1)).scale(scaled*8.6).yRot(-ages/8.0f);
                Vec3 three = sphere.pos.get(sphere.index.get(i+2)).scale(scaled*8.6).yRot(-ages/8.0f);
                one=one.scale(1.0f+Mth.sin((float)Mth.atan2(one.x,one.z)*16.0f-ages/32.0f)/24.0f);
                two=two.scale(1.0f+Mth.sin((float)Mth.atan2(two.x,two.z)*16.0f-ages/32.0f)/24.0f);
                three=three.scale(1.0f+Mth.sin((float)Mth.atan2(three.x,three.z)*16.0f-ages/32.0f)/24.0f);
                float[] v = new float[]{(float) sphere.uvs.get(sphere.index.get(i)).x/divisor.x, (float) sphere.uvs.get(sphere.index.get(i+1)).x/divisor.x, (float) sphere.uvs.get(sphere.index.get(i+2)).x/divisor.x, sphere.uvs.get(sphere.index.get(i)).x/divisor.x};
                float[] u = new float[]{(float) sphere.uvs.get(sphere.index.get(i)).y/divisor.y, (float) sphere.uvs.get(sphere.index.get(i+1)).y/divisor.y, (float) sphere.uvs.get(sphere.index.get(i+2)).y/divisor.y,(float) sphere.uvs.get(sphere.index.get(i)).y/divisor.y};


                PoseStack.Pose pose = poseStack.last();
                vertex(pose,consumer,one.x,one.y,one.z,u[0],v[0],0,0,1,255,1,col);
                vertex(pose,consumer,two.x,two.y,two.z,u[1],v[1],0,0,1,255,1,col);
                vertex(pose,consumer,three.x,three.y,three.z,u[2],v[2],0,0,1,255,1,col);
                vertex(pose,consumer,one.x,one.y,one.z,u[3],v[3],0,0,1,255,1,col);
                poseStack.popPose();
            }
        }
    }

    @Override
    public AABB getRenderBoundingBox(DoohickeyBlockEntity blockEntity) {
        return BlockEntityRenderer.super.getRenderBoundingBox(blockEntity).inflate(9);
    }

    @Override
    public boolean shouldRenderOffScreen(DoohickeyBlockEntity blockEntity) {
        return true;
    }

    @Override
    public boolean shouldRender(DoohickeyBlockEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha, Vector3f color) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(color.x,color.y,color.z,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal((float)normalX, (float)normalZ, (float)normalY);
    }
}
