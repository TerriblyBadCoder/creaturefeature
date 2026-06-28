package net.atired.creaturefeature.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockMixin {
    @Shadow
    public abstract Block getBlock();

    @ModifyReturnValue(method = "isValidSpawn",at = @At("RETURN"))
    private boolean shouldAllowin(boolean original, BlockGetter level, BlockPos pos, EntityType<?> entityType){
        if(entityType == CFEntityInit.FRIEND.get() && getBlock() == Blocks.BEDROCK)
        {
            return true;
        }
        return original;
    }
}
