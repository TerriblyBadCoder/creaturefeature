package net.atired.creaturefeature.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.atired.creaturefeature.init.CFAchievements;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(AdvancementWidget.class)
public abstract class AdvancementWidgetMixin {
    @Shadow @Final private AdvancementNode advancementNode;

    @Mutable
    @Shadow @Final private int x;

    @Mutable
    @Shadow @Final private int y;

    @Mutable
    @Shadow @Final private DisplayInfo display;

    @Shadow public abstract boolean isMouseOver(int x, int y, int mouseX, int mouseY);

    @Shadow @Nullable private AdvancementProgress progress;
    @Mutable
    @Shadow @Final private List<FormattedCharSequence> description;

    @Shadow protected abstract List<FormattedText> findOptimalLines(Component component, int maxWidth);

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private FormattedCharSequence title;

    @Shadow protected abstract int getMaxProgressWidth();

    private float xMult=0.1f;
    @Inject(method = "Lnet/minecraft/client/gui/screens/advancements/AdvancementWidget;isMouseOver(IIII)Z",at=@At("RETURN"))
    private void getStuff(int x, int y, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> cir){
        boolean hasStuff = cir.getReturnValue();

        float f = this.progress == null ? 0.0F : this.progress.getPercent();
        if(hasStuff&&this.advancementNode.advancement().requirements().requirements().get(0).get(0).equals("man_test_criteria_name")&&f<1.0f&&!CreatureFeatureClient.PROXY.searchingForHim){
            CreatureFeatureClient.PROXY.searchingForHim=true;
            Minecraft.getInstance().player.playSound(SoundEvents.BELL_RESONATE,0.2f,2f);
        }
    }

    @WrapMethod(method = "draw(Lnet/minecraft/client/gui/GuiGraphics;II)V")
    private void wrapMethodDraw(GuiGraphics guiGraphics, int x, int y, Operation<Void> original){
        float f = this.progress == null ? 0.0F : this.progress.getPercent();
        if(this.xMult>-0.1&&this.advancementNode.advancement().requirements().requirements().get(0).get(0).equals("man_test_criteria_name")){
            if(f<1.0f){
                this.x+=(int)(3*this.xMult);
                this.xMult*=1.1f;
                if(this.xMult>20f){
                    this.xMult=-1f;
                }
                x+=7;
                Level level = Minecraft.getInstance().level;
                double yPos = Minecraft.getInstance().player!=null?Minecraft.getInstance().player.getY():0;
                if(CreatureFeatureClient.PROXY.searchingForHim&&(yPos>127.8&&level!=null&&level.dimensionType().respawnAnchorWorks())&&!this.description.get(0).equals('I')){
                    int j = 29 + minecraft.font.width(this.title) +  this.getMaxProgressWidth();
                    this.description = Language.getInstance().getVisualOrder(this.findOptimalLines(ComponentUtils.mergeStyles(Component.literal("I WILL SHOW YOU A GREAT VESTIGE TO THE EAST."), Style.EMPTY.withColor(display.getType().getChatColor())), j));

                }
            }else if(!this.description.get(0).equals('W')){
                int j = 29 + minecraft.font.width(this.title) +  this.getMaxProgressWidth();
                this.description = Language.getInstance().getVisualOrder(this.findOptimalLines(ComponentUtils.mergeStyles(Component.literal("Well, the man thanked you, then he bowed, and left."), Style.EMPTY.withColor(display.getType().getChatColor())), j));

            }
        }
        original.call(guiGraphics,x,y);

    }
}
