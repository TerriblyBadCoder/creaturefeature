package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.DreamWeaverEntityModel;
import net.atired.creaturefeature.entity.DreamWeaverEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DreamWeaverSilk2Layer<T extends DreamWeaverEntity, M extends DreamWeaverEntityModel<T>> extends EyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = CFRenderTypes.entityFriendCutout(CreatureFeature.getId("textures/entity/silk.png"));
    private static final ResourceLocation SILK =CreatureFeature.getId("textures/entity/dreamweaver_silk.png");

    private final DreamWeaverEntityModel<T> model;

    public DreamWeaverSilk2Layer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new DreamWeaverEntityModel<>(modelSet.bakeLayer(DreamWeaverEntityModel.INNERER_LAYER_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.isInvisible()) {
            this.getParentModel().copyPropertiesTo(model);
            if(CFRenderTypes.SILK_SHADER_INSTANCE!=null){
                CFRenderTypes.SILK_SHADER_INSTANCE.safeGetUniform("Time").set(ageInTicks/70.0f);
            }
            model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
            model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            model.setupSilk(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexconsumer =buffer.getBuffer(CFRenderTypes.entitySilkCull(SILK));
            model.renderToBuffer(poseStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F), -1);

        }
    }
    public RenderType renderType() {
        return SPIDER_EYES;
    }
}

