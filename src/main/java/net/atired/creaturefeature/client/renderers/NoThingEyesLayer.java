package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.NoThingEntityModel;
import net.atired.creaturefeature.entity.NoThingEntity;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NoThingEyesLayer<T extends NoThingEntity> extends EyesLayer<T, NoThingEntityModel<T>> {
    private static final RenderType NOTHING_EYES = CFRenderTypes.entityUnlitAmbushCutout(CreatureFeature.getId("textures/entity/nothing_eyes.png"));

    public NoThingEyesLayer(RenderLayerParent<T, NoThingEntityModel<T>> p_116964_) {
        super(p_116964_);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        super.render(poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public RenderType renderType() {
        return NOTHING_EYES;
    }
}
