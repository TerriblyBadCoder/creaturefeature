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
    }

    @Override
    public void tick() {
        if(level() instanceof ServerLevel serverLevel&&this.tickCount==14&&this.count>0){
            CollumnEntity collumnEntity = new CollumnEntity(CFEntityInit.COLLUMN.get(), serverLevel);
            collumnEntity.count=this.count-1;
            collumnEntity.setPos(getPosition(1).add(this.dir));
            collumnEntity.dir=this.dir;
            serverLevel.addFreshEntity(collumnEntity);
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
