package net.atired.creaturefeature.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Gui.class)
public abstract class CFGuiMixin {
    private static final ResourceLocation FORTUNE_LOCATION = CreatureFeature.getId("textures/gui/fortune.png");

    @Inject(method= "renderItemHotbar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",at= @At(value = "INVOKE",ordinal = 0,shift = At.Shift.AFTER, target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"))
    private void headHotbarMixin(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        if(CreatureFeatureClient.PROXY.nan_title>0.0f&&Minecraft.getInstance().level!=null){
            int i = guiGraphics.guiWidth() / 2;
            int j = guiGraphics.guiHeight() -74;
            float scaled =  Math.min(1.0f, Mth.sin(CreatureFeatureClient.PROXY.nan_title*3.14f)*3.0f);
            float timed = Minecraft.getInstance().level.getGameTime()%24000+deltaTracker.getGameTimeDeltaPartialTick(true);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(i,guiGraphics.guiHeight() -74,50);
            guiGraphics.pose().mulPose(new Quaternionf().rotationZ(Mth.cos(CreatureFeatureClient.PROXY.nan_title*3.14f)*(1.0f- Mth.sin(CreatureFeatureClient.PROXY.nan_title*3.14f))*0.05f));
            guiGraphics.pose().translate(-i,-guiGraphics.guiHeight() +74,50);
            guiGraphics.pose().translate(0,(1.0f-scaled)*80.0f,0);
            guiGraphics.blit(FORTUNE_LOCATION,i-64,j-16,0,0,0,128,32,128,32);
            Component component = Component.translatable("jukebox_song.creaturefeature.new_age_nevermore");
            float length = -Minecraft.getInstance().font.width(component)/2.0f*0.9f;
            guiGraphics.pose().popPose();
            for(char c : component.getString().toCharArray()){
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(length,Mth.sin(length/20.0f+timed/2.0f)*CreatureFeatureClient.PROXY.nan_title*3.0f,101);
                guiGraphics.pose().translate(0,(1.0f-scaled)*60.0f,0);
                guiGraphics.pose().translate(i,j-3,0);
                guiGraphics.pose().scale(0.9f,0.9f,1.0f);
                guiGraphics.drawString(Minecraft.getInstance().font,c+"",1,1,0x412b49);
                guiGraphics.drawString(Minecraft.getInstance().font,c+"",0,0, Color.getHSBColor(281.0f/360.0f+Mth.sin(length/20.0f+timed/3.0f)/40.0f,0.52f+Mth.sin(length/10.0f+timed/4.0f)/10.0f,0.98f).getRGB());
                guiGraphics.pose().popPose();
                length+=Minecraft.getInstance().font.width(c+"")*0.9f;
            }

        }
        if(CreatureFeatureClient.PROXY.gasLeak>0.0f&&Minecraft.getInstance().level!=null){
            float timed = Minecraft.getInstance().level.getGameTime()+deltaTracker.getGameTimeDeltaPartialTick(true);
            timed/=10.0f;
            guiGraphics.pose().translate(Mth.sin(timed)*CreatureFeatureClient.PROXY.gasLeak*0.8,0.0f,0.0f);
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
