package net.atired.creaturefeature.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CFRenderTypes;
import net.atired.creaturefeature.client.renderers.models.FiendEntityModel;
import net.atired.creaturefeature.entity.FiendEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class FiendCritLayer<T extends FiendEntity, M extends FiendEntityModel<T>> extends EyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = CFRenderTypes.entityFriendCutout(CreatureFeature.getId("textures/entity/silk.png"));
    private static final ResourceLocation SILK =CreatureFeature.getId("textures/entity/silk_cube.png");
    private static final ResourceLocation FIEND =CreatureFeature.getId("textures/entity/fiend_white.png");

    private final FiendEntityModel<T> model;

    public FiendCritLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new FiendEntityModel<>(modelSet.bakeLayer(FiendEntityModel.INNER_LAYER_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.isInvisible()&&(livingEntity.getKick()>0||livingEntity.getCrit()>0)) {
            float critted = Math.min(1f,(livingEntity.getCrit()-0.35f)*10f);

            float maxVal = Math.max(critted, livingEntity.getKick());
            this.getParentModel().copyPropertiesTo(model);
            if(CFRenderTypes.SILK_SHADER_INSTANCE!=null){
                CFRenderTypes.SILK_SHADER_INSTANCE.safeGetUniform("Time").set(ageInTicks/70.0f);
            }
            model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
            model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexconsumer =buffer.getBuffer(CFRenderTypes.entityCritCull(FIEND));
            model.renderToBuffer(poseStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F), new Color(255,255,255,(int)(255*maxVal)).getRGB());

        }
    }
    public RenderType renderType() {
        return SPIDER_EYES;
    }
}

