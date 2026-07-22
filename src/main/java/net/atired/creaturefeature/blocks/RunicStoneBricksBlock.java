package net.atired.creaturefeature.blocks;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.block.BlockWithSubLevelCollisionCallback;
import dev.ryanhcode.sable.api.physics.callback.BlockSubLevelCollisionCallback;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.atired.creaturefeature.entity.ToadstoolEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.List;



public class RunicStoneBricksBlock extends Block implements BlockWithSubLevelCollisionCallback {
    public RunicStoneBricksBlock(Properties properties) {
        super(properties);
    }
//    @Override
//    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
//        super.onPlace(state, level, pos, oldState, movedByPiston);
//        if (oldState.getBlock() instanceof RunicStoneBricksBlock) {
//            return;
//        }
//        if (level instanceof ServerLevel serverLevel) {
//            SubLevel subLevel = Sable.HELPER.getContaining(serverLevel, pos);
//            if (subLevel != null && isSingleBlock(subLevel)) {
//                return;
//            }
//
//            BoundingBox3i bounds = BoundingBox3i.from(List.of(pos, pos.offset(1, 1, 1)));
//            assert bounds != null;
//
//            ServerSubLevel serverSubLevel = SubLevelAssemblyHelper.assembleBlocks(
//                    serverLevel,
//                    pos,
//                    List.of(pos),
//                    bounds
//            );
//            level.updateNeighborsAt(pos, oldState.getBlock());
//        }
//    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(!(entity instanceof ToadstoolEntity)){
            entity.hurt(entity.damageSources().flyIntoWall(),4);
        }
        super.entityInside(state, level, pos, entity);
    }

    private static boolean isSingleBlock(SubLevel subLevel) {
        BoundingBox3ic bounds = subLevel.getPlot().getBoundingBox();
        return bounds != null && bounds.minX() == bounds.maxX() && bounds.minY() == bounds.maxY() && bounds.minZ() == bounds.maxZ();
    }
    @Override
    public BlockSubLevelCollisionCallback sable$getCallback() {
        return RunicStoneBrickCallback.INSTANCE;
    }
    public static class RunicStoneBrickCallback implements BlockSubLevelCollisionCallback {

        public static final RunicStoneBrickCallback INSTANCE = new RunicStoneBrickCallback();

        @Override
        public CollisionResult sable$onCollision(BlockPos hitBlockPos, @Nullable BlockPos otherHitBlockPos, Vector3d impactPosition, double impactVelocity) {
            Vec3 dir = new Vec3(impactPosition.x,impactPosition.y,impactPosition.z).subtract(hitBlockPos.getCenter()).normalize().scale(impactVelocity).scale(-1);
            return new CollisionResult(new Vector3d(dir.x,dir.y,dir.z), false);
        }
    }
}
