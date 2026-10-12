package net.atired.creaturefeature.mixin;

import net.minecraft.world.entity.OminousItemSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OminousItemSpawner.class)
public class OminousItemSpawnerMixin {
    @Inject(method = "Lnet/minecraft/world/entity/OminousItemSpawner;spawnItem()V",at=@At("HEAD"))
    private void evilPowderSkullStuff(CallbackInfo ci){

    }
}
