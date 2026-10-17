package net.atired.creaturefeature.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class WraithEntity extends Monster {
    protected WraithEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
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
    public float getSpeed() {
        return super.getSpeed();
    }
}
