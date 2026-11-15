package net.atired.creaturefeature.blocks;

import net.atired.creaturefeature.entity.CollumnEntity;
import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;

public class MonolithBlock  extends HugeMushroomBlock {
    public MonolithBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        for (Direction dir : Direction.values()){
            if(!level.getBlockState(pos.offset(dir.getNormal())).isEmpty()){
                state=state.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(dir), false);
            }
            level.setBlock(pos,state,2);
        }
        super.tick(state, level, pos, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(level.getBlockState(pos.below()).getBlock()!=this&&Math.random()>0.6){
            Vec3 posCenter = pos.getCenter();
            Player player = level.getNearestPlayer(posCenter.x,posCenter.y,posCenter.z,60,false);
            CollumnEntity collumnEntity = new CollumnEntity(CFEntityInit.COLLUMN.get(),level);
            collumnEntity.setPos(posCenter);
            if(player!=null){
                collumnEntity.dir=player.getPosition(1).subtract(posCenter).multiply(1,0,1).normalize();
            }
            level.addFreshEntity(collumnEntity);
        }
        super.randomTick(state, level, pos, random);
    }
}
