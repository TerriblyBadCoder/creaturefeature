package net.atired.creaturefeature.mixin;

import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.atired.creaturefeature.init.CFItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxPlayable.class)
public class JukeBoxPlayableMixin {
    @Inject(method = "tryInsertIntoJukebox",at= @At(value = "INVOKE",shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/ItemInteractionResult;sidedSuccess(Z)Lnet/minecraft/world/ItemInteractionResult;"))
    private static void awesome(Level level, BlockPos pos, ItemStack stack, Player player, CallbackInfoReturnable<ItemInteractionResult> cir){
        if(stack.getItem()== CFItemInit.NEW_AGE_NEVERMORE_DISC.asItem()){
            if(level.isClientSide()){
                CreatureFeatureClient.PROXY.nan_title=1.0f;
                player.displayClientMessage(Component.empty(),true);
            }
        }
    }
}
