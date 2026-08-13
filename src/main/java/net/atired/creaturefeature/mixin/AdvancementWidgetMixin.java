package net.atired.creaturefeature.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.atired.creaturefeature.init.CFAchievements;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AdvancementWidget.class)
public class AdvancementWidgetMixin {
    @Shadow @Final private AdvancementNode advancementNode;

    @Mutable
    @Shadow @Final private int x;

    @Mutable
    @Shadow @Final private int y;

    @Shadow @Final private DisplayInfo display;
    private float xMult=0.5f;
    @WrapMethod(method = "draw(Lnet/minecraft/client/gui/GuiGraphics;II)V")
    private void wrapMethodDraw(GuiGraphics guiGraphics, int x, int y, Operation<Void> original){
        if(this.xMult>-0.1f&&this.advancementNode.advancement().requirements().requirements().get(0).get(0).equals("man_test_criteria_name")){
            this.x+=(int)(3*this.xMult);
            this.xMult*=1.1f;
            if(this.xMult>20f){
                this.xMult=-1f;
            }
            x+=7;
        }
        original.call(guiGraphics,x,y);

    }
}
