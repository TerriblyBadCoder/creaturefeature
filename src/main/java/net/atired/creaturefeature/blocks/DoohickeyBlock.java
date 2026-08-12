package net.atired.creaturefeature.blocks;

import net.atired.creaturefeature.blocks.blockentities.DoohickeyBlockEntity;
import net.atired.creaturefeature.init.CFBlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class DoohickeyBlock extends Block implements EntityBlock {
    public DoohickeyBlock(Properties properties) {
        super(properties);
    }

    protected static final VoxelShape Y_AXIS_AABB = Shapes.join(
            Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0),
            Block.box(0.0, 15.0, 0.0, 16.0, 18.0, 16.0),
            BooleanOp.OR
    );
    private static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker
    ) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DoohickeyBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos)!=null&&level.getBlockEntity(pos) instanceof DoohickeyBlockEntity doohickey) {
            if(doohickey.placedItem!=null&&!doohickey.placedItem.isEmpty()&&doohickey.ranged>0.5f){
                player.addItem(doohickey.placedItem);
                doohickey.placedItem=null;
                player.swing(InteractionHand.MAIN_HAND);
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(level.getBlockEntity(pos)!=null&&level.getBlockEntity(pos) instanceof DoohickeyBlockEntity doohickey) {
            if(doohickey.placedItem!=null&&!doohickey.placedItem.isEmpty()&&doohickey.ranged>0.5f){
                if(level instanceof ServerLevel serverLevel){
                    Vec3 centered = pos.getCenter().add(0,0.8,0);
                    ItemEntity itementity = new ItemEntity(level, centered.x,centered.y,centered.z, doohickey.placedItem);
                    float f = 0.05F;
                    itementity.setDeltaMovement(level.random.triangle(0.0, 0.11485000171139836), level.random.triangle(0.2, 0.11485000171139836), level.random.triangle(0.0, 0.11485000171139836));
                    level.addFreshEntity(itementity);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos)!=null&&level.getBlockEntity(pos) instanceof DoohickeyBlockEntity doohickey){
            if((doohickey.placedItem==null||doohickey.placedItem.isEmpty())&&DoohickeyBlockEntity.PLACEABLE.contains(stack.getItem())){
                doohickey.placedItem=stack.copy();
                stack.setCount(0);
                player.swing(hand);
                DoohickeyBlockEntity.placedItem(doohickey);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, CFBlockEntityInit.DOOHICKEY.get(), DoohickeyBlockEntity::tick);
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Y_AXIS_AABB;
    }
}
