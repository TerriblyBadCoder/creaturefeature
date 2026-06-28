package net.atired.creaturefeature.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.blocks.CarapaceBlock;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockRenderer.class)
public class FallingBlockEntityRendererMixin {
    @Unique
    private static final ResourceLocation BLANK_LOCATION = CreatureFeature.getId("textures/entity/blank.png");

    @Inject(method = "render(Lnet/minecraft/world/entity/item/FallingBlockEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at=@At("TAIL"))
    private void injectAtEvilSquare(FallingBlockEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci){
        if(entity.getBlockState().getBlock() instanceof CarapaceBlock block){
            float modulo = Math.min(1.0f,(entity.tickCount+partialTicks)/4.0f);
            float sinused = (entity.tickCount+partialTicks)/8.0f;
            poseStack.pushPose();
            VertexConsumer consumer = buffer.getBuffer(CFRenderTypes.entityOutlinedCutout(BLANK_LOCATION));
            PoseStack.Pose pose = poseStack.last();
            for (int i = 0; i < 5; i++) {
                Vec3 vec3_1 = new Vec3(3.6,0,0).yRot(-(float)Math.PI/5.0f+(float)Math.PI*i/2.5f+sinused);
                Vec3 vec3_2 = new Vec3(3.6,0,0).yRot((float)Math.PI/5.0f+(float)Math.PI*i/2.5f+sinused);
                vertex(pose,consumer,vec3_1.x,1.5f, vec3_1.z, 0,0,0,0,1,255,0);
                vertex(pose,consumer,vec3_2.x,1.5f, vec3_2.z, 1,0,0,0,1,255,0);
                vertex(pose,consumer,vec3_2.x,0.0f, vec3_2.z, 1,1,0,0,1,255,modulo);
                vertex(pose,consumer,vec3_1.x,0.0f, vec3_1.z, 0,1,0,0,1,255,modulo);
                vertex(pose,consumer,vec3_1.x,0.0f, vec3_1.z, 0,0,0,0,1,255,modulo);
                vertex(pose,consumer,vec3_2.x,0.0f, vec3_2.z, 1,0,0,0,1,255,modulo);
                vertex(pose,consumer,vec3_2.x,-6.0f, vec3_2.z, 1,1,0,0,1,255,modulo);
                vertex(pose,consumer,vec3_1.x,-6.0f, vec3_1.z, 0,1,0,0,1,255,modulo);

            }
            poseStack.popPose();
        }

    }
    public void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, float u, float v, int normalX, int normalY, int normalZ, int packedLight, float alpha) {
        consumer.addVertex(pose, (float)x, (float)y, (float)z).setColor(0.46f,0.61f,0.48f,alpha).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal((float)normalX, (float)normalZ, (float)normalY);
    }
}
