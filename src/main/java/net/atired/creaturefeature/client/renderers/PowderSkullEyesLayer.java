package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.PowderSkullEntityModel;
import net.atired.creaturefeature.entity.PowderSkullEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PowderSkullEyesLayer<T extends PowderSkullEntity, M extends PowderSkullEntityModel<T>> extends EyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = RenderType.entityTranslucentEmissive(CreatureFeature.getId("textures/entity/powder_skull_eyes.png"));

    public PowderSkullEyesLayer(RenderLayerParent<T, M> p_117507_) {
        super(p_117507_);

    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.scale(1.01f,1.0f,1.01f);
        super.render(poseStack, buffer, 15728880, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        poseStack.popPose();
    }

    public RenderType renderType() {
        return SPIDER_EYES;
    }
}

