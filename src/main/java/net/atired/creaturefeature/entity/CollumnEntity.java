package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CollumnEntity extends Entity {
    public Vec3 dir = new Vec3(1,0,0);
    public int count = 5;
    public CollumnEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.dir=new Vec3(1,0,0).yRot((float)(Math.random()*3.14f*4.0f));
    }

    @Override
    public void tick() {
        if(level() instanceof ServerLevel serverLevel&&this.tickCount==6&&this.count>0){
            CollumnEntity collumnEntity = new CollumnEntity(CFEntityInit.COLLUMN.get(), serverLevel);
            collumnEntity.count=this.count-1;
            collumnEntity.setPos(getPosition(1).add(this.dir.scale(1.9f).yRot((float)(Math.random()-0.5f))));
            collumnEntity.dir=this.dir;
            serverLevel.addFreshEntity(collumnEntity);
        }
        if(this.tickCount==84){
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
