package net.atired.creaturefeature.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;

@Mixin(AdvancementTab.class)

public class AdvancementTabMixin {
    @Shadow private double scrollX;
    @Shadow private double scrollY;
    private float test = 0.0f;
    private float testMul=1f;
    @WrapOperation(method = "drawContents",at= @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"))
    private void drawawesomeTexture(GuiGraphics instance, ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight, Operation<Void> original,
                                    @Local(ordinal = 4) int k, @Local(ordinal = 5) int l, @Local(ordinal = 6) int m, @Local(ordinal = 7) int n){
        if(atlasLocation.getNamespace().equals("creaturefeature")){
            if(m==-1&&n==-1){
                Level level = Minecraft.getInstance().level;
                double yPos = Minecraft.getInstance().player!=null?Minecraft.getInstance().player.getY():0;
                if(CreatureFeatureClient.PROXY.searchingForHim&&(yPos>127.8&&level!=null&&level.dimensionType().respawnAnchorWorks())){
                    testMul*=0.94f;
                }else if(testMul<0.99f){
                    testMul=Math.min(testMul+0.05f,1f);
                }
                test-=1.0f/10.0f*testMul;
                test%=64;
            }
            int i = Mth.floor(this.scrollX);
            int j = Mth.floor(this.scrollY);
            k = i % 64;l = j % 64;
            instance.pose().translate(test,test,0);
            instance.blit(atlasLocation,k+64*m,l+64*n,0.0f,0.0f,64,64,64,64);
            instance.pose().translate(-test,-test,0);
            if(m==15&&n==8){
                RenderSystem.setShaderColor(1.0f,testMul,testMul,0.66f);
                RenderSystem.enableBlend();
                for (m=-1;m<=15;m++){
                    for(n = -1; n <= 8; ++n) {
                        instance.pose().pushPose();
                        instance.pose().translate(test*2.0f,(float)Math.sin(test/4.0f*3.14f),0);
                        instance.blit(atlasLocation,k+64*(m),l+64*(n),0.0f,0.0f,64,64,64,64);
                        instance.pose().translate(-test*2.0f,(float)-Math.sin(test/4.0f*3.14f),0);
                        instance.pose().popPose();
                    }
                }
                RenderSystem.setShaderColor(1.0f,1.0f,1.0f,1.0f);
            }
        }
        else{
            original.call(instance,atlasLocation,x,y,uOffset,vOffset,width,height,textureWidth,textureHeight);
        }
    }
}
