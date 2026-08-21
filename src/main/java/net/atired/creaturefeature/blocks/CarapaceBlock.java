package net.atired.creaturefeature.blocks;

import com.mojang.serialization.MapCodec;
import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.atired.creaturefeature.networking.payloads.PathogenPayload;
import net.atired.creaturefeature.networking.payloads.SquashedPayload;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class CarapaceBlock extends ColoredFallingBlock {

    public CarapaceBlock(ColorRGBA dustColor, Properties properties) {
        super(dustColor, properties);
    }



    @Override
    public void onLand(Level level, BlockPos pos, BlockState stated, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        if(level instanceof ServerLevel serverLevel){
            for (int x = -2;x < 2; x++) {
                for (int z = -2;z < 2; z++) {
                    if(!level.getBlockState(pos).isAir()){
                        BlockState state = level.getBlockState(pos);
                        Vec3 center = pos.getCenter().add(0,0.5,0);
                        serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,state),center.x,center.y,center.z,2,0.3,0,0.3,0.3);
                        serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.DUST_PILLAR,state),center.x,center.y,center.z,2,0.3,0,0.3,0.3);
                    }
                }
            }
            for(LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class,new AABB(pos).inflate(2.8))){
                if(living.position().subtract(pos.getCenter()).horizontalDistance()<3.2){
                    living.hurt(fallingBlock.damageSources().fallingBlock(fallingBlock),5.5f+fallingBlock.fallDistance);
                    if(living instanceof LivingEntityGoopAccessor accessor){
                        SquashedPayload payload = new SquashedPayload(living.getId());
                        PacketDistributor.sendToPlayersTrackingChunk(serverLevel,level.getChunkAt(pos).getPos(),payload);
                        accessor.setSquashed(0.8f);
                    }
                }
            }
        }
        super.onLand(level, pos, stated, replaceableState, fallingBlock);
    }

    @Override
    protected int getDelayAfterPlace() {
        return 10;
    }
}
