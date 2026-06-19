package net.atired.creaturefeature.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.atired.creaturefeature.init.CFDataComponentTypeInit;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow @Final private PoseStack pose;

    @Shadow public abstract void blitSprite(ResourceLocation sprite, int x, int y, int blitOffset, int width, int height);

    @Shadow public abstract void blit(ResourceLocation atlasLocation, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight);


    @Inject(method= "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",at=@At("HEAD"))
    private void renderSlotEvil(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci){
        if(stack!=null&& CreatureFeatureClient.PROXY.wobble>0.02f&&CreatureFeatureClient.PROXY.wobblyItem.getItem()==stack.getItem()&& Minecraft.getInstance().level!=null){
            if(stack.getOrDefault(CFDataComponentTypeInit.OPENED,0)==0) return;
            float timed = Minecraft.getInstance().level.getGameTime();
            timed/=3.1f;
            pose.pushPose();
            pose.translate((float)(x + 8), (float)(y + 8),0);
            pose.scale(1.0f+Mth.sin(CreatureFeatureClient.PROXY.wobble*3.14f)/3.0f,1.0f-Mth.sin(CreatureFeatureClient.PROXY.wobble*3.14f)/4.0f,1);
            pose.translate(-(float)(x + 8), -(float)(y + 8),0);
            pose.rotateAround(new Quaternionf().rotationXYZ(0.0f,0.0f,Mth.sin(3.14f*CreatureFeatureClient.PROXY.wobble*6.0f)/12.0f),x+8,y+8,0);

        }
    }
    @Inject(method= "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",at=@At("RETURN"))
    private void renderSlotEviler(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci){
        if(stack!=null&&CreatureFeatureClient.PROXY.wobble>0.02f&&CreatureFeatureClient.PROXY.wobblyItem.getItem()==stack.getItem()&&Minecraft.getInstance().level!=null){
            if(stack.getOrDefault(CFDataComponentTypeInit.OPENED,0)==0) return;
            pose.popPose();
        }
    }
}
