package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.TrichaelEntityModel;
import net.atired.creaturefeature.entity.TrichaelEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class TrichaelLayer<T extends TrichaelEntity, M extends TrichaelEntityModel<T>> extends EyesLayer<T, M> {
    private static final ResourceLocation TRICHAEL =CreatureFeature.getId("textures/entity/trichael_overlay.png");
    private static final RenderType SPIDER_EYES = CFRenderTypes.entityCritCull(TRICHAEL);

    private final TrichaelEntityModel<T> model;

    public TrichaelLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new TrichaelEntityModel<>(modelSet.bakeLayer(TrichaelEntityModel.INNER_LAYER_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.isInvisible()) {

            float danceSin = Math.clamp(Mth.sin(livingEntity.getDancing()*3.14f)*1.5f,0.15f,1.0f);
            this.getParentModel().copyPropertiesTo(model);
            if(CFRenderTypes.SILK_SHADER_INSTANCE!=null){
                CFRenderTypes.SILK_SHADER_INSTANCE.safeGetUniform("Time").set(ageInTicks/70.0f);
            }
            model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
            model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexconsumer =buffer.getBuffer(CFRenderTypes.entityTrichaelCull(TRICHAEL));
            model.renderToBuffer(poseStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F), new Color(255,255,255,(int)(255*danceSin)).getRGB());

        }
    }
    public RenderType renderType() {
        return SPIDER_EYES;
    }
}

