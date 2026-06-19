package net.atired.creaturefeature.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class CFGuiMixin {
    @Inject(method= "renderItemHotbar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",at= @At(value = "INVOKE",ordinal = 0,shift = At.Shift.AFTER, target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"))
    private void headHotbarMixin(GuiGraphics p_316628_, DeltaTracker deltaTracker, CallbackInfo ci){
        if(CreatureFeatureClient.PROXY.gasLeak>0.0f&&Minecraft.getInstance().level!=null){
            float timed = Minecraft.getInstance().level.getGameTime()+deltaTracker.getGameTimeDeltaPartialTick(true);
            timed/=10.0f;
            p_316628_.pose().translate(Mth.sin(timed)*CreatureFeatureClient.PROXY.gasLeak*0.8,0.0f,0.0f);
            float[] col = RenderSystem.getShaderColor();
            col[0]*= Mth.lerp(CreatureFeatureClient.PROXY.gasLeak,1.0f,0.6f);
            col[2]*= Mth.lerp(CreatureFeatureClient.PROXY.gasLeak,1.0f,0.5f);
        }
    }

    @Inject(method= "renderItemHotbar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",at= @At(value = "INVOKE",ordinal = 0, target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V"))
    private void tailHotbarMixin(GuiGraphics p_316628_, DeltaTracker p_348543_, CallbackInfo ci){
        if(CreatureFeatureClient.PROXY.gasLeak>0.0f&&Minecraft.getInstance().level!=null){
            float[] col = RenderSystem.getShaderColor();
            col[0]/= Mth.lerp(CreatureFeatureClient.PROXY.gasLeak,1.0f,0.6f);
            col[2]/= Mth.lerp(CreatureFeatureClient.PROXY.gasLeak,1.0f,0.5f);
        }
    }

}
