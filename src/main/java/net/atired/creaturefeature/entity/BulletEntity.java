package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFSoundInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BulletEntity extends Entity {
    public LivingEntity ownerUsual = null;
    public BulletEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if(level() instanceof ServerLevel serverLevel){
            if(tickCount==1){
                playSound(CFSoundInit.SHOT.value(),0.8f,0.6f+(float)Math.random()/2f);
                float customPitch = -getXRot();
                float customYaw = -getYRot();
                Vec3 godir = new Vec3(0,0,1).xRot(customPitch/180*3.14f).yRot(customYaw/180.0f*3.14f).scale(12);

                float yawCos = (float) Math.cos(customYaw/180*3.14f);
                float yawSin = (float) Math.sin(customYaw/180*3.14f);
                float pitchCos = (float) Math.cos(customPitch/180*3.14f);
                float pitchSin = (float) Math.sin(customPitch/180*3.14f);
                for(Entity other : serverLevel.getEntitiesOfClass(Entity.class,getBoundingBox().inflate(24),Entity::isAlive)){
                    if(other==this||this.ownerUsual==other){
                        continue;
                    }
                    double translatedX = other.getX() - (this.getX()+godir.x());
                    double translatedY = other.getY()+other.getBbHeight()/2 - (this.getY()+0.1+godir.y());
                    double translatedZ = other.getZ() - (this.getZ()+godir.z());
                    double x1 = translatedX * yawCos - translatedZ * yawSin;
                    double z1 = translatedX * yawSin + translatedZ * yawCos;
                    double y1 = translatedY;
                    double y2 = y1 * pitchCos - z1 * pitchSin;
                    double z2 = y1 * pitchSin + z1 * pitchCos;
                    double x2 = x1;
                    if((Math.abs(x2) <=0.6) && (Math.abs(y2) <= 0.1+other.getBbHeight()/2) && (Math.abs(z2) <= 12)){
                        other.hurt(damageSources().mobProjectile(this,ownerUsual),2.5f);
                        if(other instanceof LivingEntity i&&this.hasLineOfSight(i)){
                            i.hurtTime=0;
                            i.invulnerableTime=0;
                            i.hurtMarked=false;
                            i.hurtDuration=0;
                        }
                    }
                }
            }
        }
        if(tickCount>10){
            discard();
        }
    }
    public boolean hasLineOfSight(Entity entity) {
        if (entity.level() != this.level()) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
            Vec3 vec31 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
            return vec31.distanceTo(vec3) > 128.0 ? false : this.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
        }
    }
    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
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
