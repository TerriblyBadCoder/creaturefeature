package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.DreamWeaverEntityModel;
import net.atired.creaturefeature.entity.DreamWeaverEntity;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DreamWeaverEyesLayer<T extends DreamWeaverEntity, M extends DreamWeaverEntityModel<T>> extends EyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = CFRenderTypes.entitySilkCull(CreatureFeature.getId("textures/entity/dreamweaver_eyes.png"));

    public DreamWeaverEyesLayer(RenderLayerParent<T, M> p_117507_) {
        super(p_117507_);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        super.render(poseStack, buffer, 255, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    public RenderType renderType() {
        return SPIDER_EYES;
    }
}

