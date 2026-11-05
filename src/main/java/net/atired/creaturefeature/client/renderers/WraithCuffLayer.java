package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.DreamWeaverEntityModel;
import net.atired.creaturefeature.client.renderers.models.WraithEntityModel;
import net.atired.creaturefeature.entity.WraithEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WraithCuffLayer<T extends WraithEntity> extends EyesLayer<T, WraithEntityModel<T>> {
    private static final RenderType WRAITH_CUFF = RenderType.entityTranslucent(CreatureFeature.getId("textures/entity/wraith_cuff.png"));

    public WraithCuffLayer(RenderLayerParent<T, WraithEntityModel<T>> p_116964_) {
        super(p_116964_);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer vertexconsumer = buffer.getBuffer(this.renderType());
        this.getParentModel().renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
    }

    @Override
    public RenderType renderType() {
        return WRAITH_CUFF;
    }
}
