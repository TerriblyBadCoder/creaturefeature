package net.atired.creaturefeature.blocks;

import net.atired.creaturefeature.init.CFAchievements;
import net.atired.creaturefeature.init.CFBlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopperFullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.data.internal.NeoForgeItemTagsProvider;

public class FeathersBlock extends Block {
    public FeathersBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.getItem() instanceof BrushItem brush&&level instanceof ServerLevel serverLevel){
            Vec3 center = pos.getCenter();
            if(player instanceof ServerPlayer serverPlayer){
                CFAchievements.RENOVATION.get().trigger(serverPlayer);
            }
            serverLevel.sendParticles(ParticleTypes.ITEM_COBWEB,center.x,center.y,center.z,32,0.55,0.55,0.55,0.01);
            if(this== CFBlockInit.DOWN_FEATHERS.get()){
                level.setBlockAndUpdate(pos,CFBlockInit.MOSAIC_DOWN_FEATHERS.get().defaultBlockState());
            }else{
                level.setBlockAndUpdate(pos,CFBlockInit.DOWN_FEATHERS.get().defaultBlockState());

            }
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
