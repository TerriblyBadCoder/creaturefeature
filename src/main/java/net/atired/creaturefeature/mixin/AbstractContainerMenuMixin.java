package net.atired.creaturefeature.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.atired.creaturefeature.accessors.NoBlockContainerLevelAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @ModifyReturnValue(method = "Lnet/minecraft/world/inventory/AbstractContainerMenu;stillValid(Lnet/minecraft/world/inventory/ContainerLevelAccess;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/Block;)Z",at=@At("RETURN"))
    private static boolean yeah(boolean original, ContainerLevelAccess access, Player player, Block targetBlock){
        if(access instanceof NoBlockContainerLevelAccess){
            return true;
        }
        return original;
    }
}
