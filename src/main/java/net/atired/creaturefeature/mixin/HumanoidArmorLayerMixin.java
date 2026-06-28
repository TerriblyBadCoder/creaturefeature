package net.atired.creaturefeature.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.atired.creaturefeature.client.CFSheets;
import net.atired.creaturefeature.init.CFTrimMaterials;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @WrapOperation(method = "renderTrim(Lnet/minecraft/core/Holder;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/armortrim/ArmorTrim;Lnet/minecraft/client/model/Model;Z)V",
            at= @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer layerOfEvil(MultiBufferSource instance, RenderType renderType, Operation<VertexConsumer> original, Holder<ArmorMaterial> holder, PoseStack p_289687_, MultiBufferSource p_289643_, int p_289683_, ArmorTrim trim, Model p_289663_, boolean p_289651_){
        int typed = CFTrimMaterials.test(trim.material().getKey());
        if(typed!=-1){
            return original.call(instance, CFSheets.armorTrimsSheet(true,typed));
        }
        return original.call(instance,renderType);
    }
}
