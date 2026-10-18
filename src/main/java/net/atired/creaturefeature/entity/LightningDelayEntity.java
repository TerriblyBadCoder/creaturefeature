package net.atired.creaturefeature.entity;

import net.minecraft.core.particles.ParticleTypes;
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
        if(this.tickCount<40){
            for (int i = 0; i < 3; i++) {
                level().addParticle(ParticleTypes.RAIN,getX((Math.random()-0.5)*20.0f),getY()+0.04f+Math.random()*1.4f,getZ((Math.random()-0.5)*20.0f),0,0.01,0);
            }
        }
        if(level() instanceof ServerLevel serverLevel&&this.tickCount>42){
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
