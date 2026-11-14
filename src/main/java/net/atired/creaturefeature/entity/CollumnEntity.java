package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.blocks.MonolithBlock;
import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class CollumnEntity extends Entity {
    public Vec3 dir = new Vec3(1,0,0);
    public int count = 12;
    public CollumnEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.dir=new Vec3(1,0,0).yRot((float)(Math.random()*3.14f*4.0f));
    }

    @Override
    public void tick() {
        if(level() instanceof ServerLevel serverLevel&&this.tickCount==4&&this.count>0){
            int spawnCount = 1;
            if(this.count%4==0&&Math.random()>0.5){
                spawnCount+=1;
                if(this.count==12){
                    spawnCount+=1;
                }
            }
            for (int i = 0; i < spawnCount; i++) {
                CollumnEntity collumnEntity = new CollumnEntity(CFEntityInit.COLLUMN.get(), serverLevel);
                collumnEntity.count=this.count-1;
                float rotOff = (spawnCount-1)*3.14f*((spawnCount-1)*0.5f-i)*0.4f;
                Vec3 fromPos = getPosition(1).add(this.dir.scale(1+spawnCount/2.0f).yRot((float)(Math.random()-0.5f)*1.2f+rotOff));
                BlockHitResult result = level().clip(new ClipContext(
                        fromPos.add(0,5,0),
                        fromPos.add(new Vec3(0,-5,0)),
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        this));
                collumnEntity.setPos(result.getLocation());
                int counter = 6;
                while (collumnEntity.getInBlockState().getBlock() instanceof MonolithBlock){
                    collumnEntity.setPos(collumnEntity.getPosition(1).add(0,-1,0));
                    counter-=1;
                    if(counter==0){
                        break;
                    }
                }
                collumnEntity.dir=this.dir.yRot(rotOff);
                serverLevel.addFreshEntity(collumnEntity);
            }
        }
        if(this.tickCount>=4&&this.tickCount<12&&level() instanceof ServerLevel serverLevel){
            for (LivingEntity livingEntity : serverLevel.getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(0.5,4,0.5).move(0.0,1.9,0.0))){
                if(livingEntity.hurt(this.damageSources().flyIntoWall(),livingEntity instanceof Monster ? 0.2f : 4.0f)){
                    Vec3 thrown = new Vec3(0,0.3,0).add(dir.yRot(Math.random()>0.5f?-1.57f:1.57f));
                    livingEntity.setDeltaMovement(thrown);
                    if(livingEntity instanceof ServerPlayer player){
                        VelSyncPayload payload = new VelSyncPayload(player.getId(),thrown.x,thrown.y,thrown.z);
                        PacketDistributor.sendToPlayer(player,payload);
                    }
                }

            }
        }
        if(this.tickCount<10){
            BlockState state = getBlockStateOn();
            for (int i = 0; i < 3; i++) {
                if(Math.random()>0.3&&this.tickCount<4){
                    level().addParticle(new BlockParticleOption(ParticleTypes.DUST_PILLAR,state),getX((Math.random()-0.5)*4.0f),getY()+Math.random()/12.0f,getZ((Math.random()-0.5)*4.0f),0,0.35+Math.random()/8.0f,0);
                }
                level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK,state),getX((Math.random()-0.5)*4.0f),getY()+Math.random()/12.0f,getZ((Math.random()-0.5)*4.0f),0,0.01+Math.random()/8.0f,0);
            }
        }
        if(this.tickCount==32){
            discard();
        }
        super.tick();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
