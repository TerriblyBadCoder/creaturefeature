package net.atired.creaturefeature.blocks;

import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class MinedFlayerJellyBlock extends Block {
    public MinedFlayerJellyBlock(Properties properties) {
        super(properties);
    }
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(level, entity);
        } else {
            Vec3 vec3 = entity.getDeltaMovement();
            if(vec3.y<-0.0){

            }
            if (vec3.y < -0.16) {
                double d0 = entity instanceof LivingEntity ? 0.7 : 0.5;
                if(level instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,entity.getBlockStateOn()),entity.getX(),entity.getY(),entity.getZ(),32,0.4,0,0.4,0.2);
                }
                if(entity instanceof LivingEntityGoopAccessor accessor){
                    accessor.setGoop(1.0f);
                }
                double add = 1.0f+Math.abs(vec3.y);
                entity.setDeltaMovement(vec3.x*add, -vec3.y * d0, vec3.z*add);
            }else{
                entity.setDeltaMovement(vec3.x, vec3.y *0.4, vec3.z);
            }
        }

    }
}
