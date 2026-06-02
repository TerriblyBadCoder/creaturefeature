package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.atired.creaturefeature.entity.PrimedEepEntity;
import net.atired.creaturefeature.init.CFBlockInit;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class EepEntityRenderer extends EntityRenderer<PrimedEepEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public EepEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    public void render(PrimedEepEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.5F, 0.0F);
        int i = entity.getFuse();
        if ((float)i - partialTicks + 1.0F < 20.0F) {
            float par = (float)i - partialTicks + 1.0F;
            float f = Mth.sin(par/5.0f*3.14f)*(float)Math.pow((20.0f-par)/20.0f,2.0f);
            float f1 = 1.0F + f * 0.3F;
            poseStack.mulPose(new Quaternionf().rotationY(f/4.0f));
            poseStack.scale(f1, f1, f1);
        }
        poseStack.mulPose(new Quaternionf().rotationY(entityYaw/180.0f*3.14f));

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, CFBlockInit.EEP.get().defaultBlockState(), poseStack, buffer, packedLight, i / 5 % 2 == 0);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public ResourceLocation getTextureLocation(PrimedEepEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
