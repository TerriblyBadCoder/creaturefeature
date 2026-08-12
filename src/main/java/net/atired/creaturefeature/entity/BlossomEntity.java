package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class BlossomEntity extends Zombie {
    public  int spewDelay = 0;
    private static final EntityDataAccessor<Float> OPENING= SynchedEntityData.defineId(BlossomEntity.class, EntityDataSerializers.FLOAT);
    public BlossomEntity(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if(level() instanceof ServerLevel serverLevel){
            if(getTarget()!=null){
                spewDelay=Math.max(0,spewDelay-1);
            }else{
                setOpening(getOpening()*0.6f);
            }
            if(getTarget()!=null&&distanceTo(getTarget())<5&&(spewDelay>15||spewDelay<=1)){
                if(getOpening()>0.97f&&spewDelay<=0){
                    playSound(SoundEvents.DROWNED_HURT,1.6f,1.2f);
                    playSound(SoundEvents.DROWNED_HURT,1.6f,0.8f);
                    playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,2.2f,0.2f);
                    playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,2.2f,0.3f);
                    playSound(SoundEvents.BUBBLE_COLUMN_BUBBLE_POP,2.2f,0.4f);
                    SpatBlockEntity entity = new SpatBlockEntity(CFEntityInit.SPAT_BLOCK.get(), serverLevel);
                    entity.setPos(getEyePosition(1).add(0,0.2,0));
                    Vec3 dir = getViewVector(1).multiply(1,0,1).normalize().scale(0.4).scale(0.4+Math.pow(getPosition(1).distanceTo(getTarget().getPosition(1)),0.5)).add(0,0.45+(getTarget().getY()-getY())/3.0f,0);
                    dir=dir.multiply(0.5f,1,0.5f);
                    entity.setDeltaMovement(dir.x,dir.y,dir.z);
                    entity.state= Blocks.AIR.defaultBlockState();
                    entity.setOwner(this);
                    spewDelay=50;
                    serverLevel.addFreshEntity(entity);
                }
                setOpening(Mth.lerp(0.4f,getOpening()+0.1f,1.0f));
            }else if(spewDelay<15){
                setOpening(getOpening()*0.6f);
            } 
        }

        super.tick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OPENING,0.0f);
        super.defineSynchedData(builder);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    public float getOpening(){
        return entityData.get(OPENING);
    }
    public void setOpening(float wing){
        entityData.set(OPENING,wing);
    }

    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        if(slot.isArmor())return;
        super.setItemSlot(slot, stack);
    }
}
