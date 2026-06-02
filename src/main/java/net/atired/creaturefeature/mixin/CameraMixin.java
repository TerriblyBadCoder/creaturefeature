package net.atired.creaturefeature.mixin;

import net.atired.creaturefeature.init.CFMobEffectInit;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private boolean detached;

    @ModifyVariable(method = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",
            at= @At(value = "HEAD"),ordinal = 0)
    private boolean detatchAmirite(boolean value,BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick){
        if(entity instanceof Player player && player.hasEffect(CFMobEffectInit.SLEEPY)){
            return true;
        }
        return value;
    }
    @ModifyVariable(method = "Lnet/minecraft/client/Camera;setRotation(FFF)V",at=@At("HEAD"),ordinal = 1)
    private float setEvilFuckingRotation(float xRot){
        if(Minecraft.getInstance().player!=null&&Minecraft.getInstance().player.hasEffect(CFMobEffectInit.SLEEPY)){
            return Mth.lerp(0.66f,xRot,90.0f);
        }
        return xRot;
    }
}
