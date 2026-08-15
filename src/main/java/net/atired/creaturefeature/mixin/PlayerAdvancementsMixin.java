package net.atired.creaturefeature.mixin;

import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin {
    @Shadow private ServerPlayer player;

    @Inject(method = "award",at= @At(value = "HEAD"))
    private void  unperform(AdvancementHolder advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir){
        if(advancement.value().requirements().requirements().get(0).get(0).equals("man_test_criteria_name")&&player instanceof PlayerBrainrotAccessor accessor && !accessor.getManCan()){
            player.sendSystemMessage(Component.literal("Well, there was not a            here."));
            System.out.println(criterionKey);
            cir.cancel();
        }
    }
}
