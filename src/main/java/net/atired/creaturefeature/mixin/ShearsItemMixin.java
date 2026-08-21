package net.atired.creaturefeature.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.atired.creaturefeature.init.CFBlockInit;
import net.atired.creaturefeature.init.CFBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {

    @WrapOperation(method = "createToolProperties",at= @At(value = "INVOKE", target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;"))
    private static List<Object> myEvilList(Object e1,Object e2, Object e3, Object e4, Operation<List<Object>> original){
        ArrayList<Object> listed = new ArrayList<>(original.call(e1,e2,e3,e4));
        listed.add(Tool.Rule.overrideSpeed(CFBlockTags.SHEARABLE, 8.0F));
        return listed;
    }
    @ModifyReturnValue(method = "mineBlock(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z",at=@At("RETURN"))
    private boolean mineMyBlock(boolean original, ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving){
        if(state.is(CFBlockInit.DREAM_SILK_SPOOL)||
                state.is(CFBlockInit.DOWN_FEATHERS)||state.is(CFBlockInit.DOWN_FEATHERS_CARPET)||
                state.is(CFBlockInit.MOSAIC_DOWN_FEATHERS)||state.is(CFBlockInit.MOSAIC_DOWN_FEATHERS_CARPET)){
            return true;
        }
        return original;
    }
}
