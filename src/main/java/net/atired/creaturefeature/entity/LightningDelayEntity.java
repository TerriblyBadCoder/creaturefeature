package net.atired.creaturefeature.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;

public class LightningDelayEntity extends Entity {
    public int delay = 24;
    public LightningDelayEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        this.setPos(getPosition(0).add(getDeltaMovement()));
        setDeltaMovement(getDeltaMovement().scale(0.81));
        if(level() instanceof ServerLevel serverLevel&&this.tickCount>24){
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT,serverLevel);
            bolt.setPos(getPosition(1));
            serverLevel.addFreshEntity(bolt);
            discard();
        }
        super.tick();
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
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
