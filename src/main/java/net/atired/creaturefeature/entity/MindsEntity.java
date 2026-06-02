package net.atired.creaturefeature.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;

public class MindsEntity extends Zombie {
    public MindsEntityPart[] parts = new MindsEntityPart[1];
    private static final EntityDataAccessor<Float> SPEED_UP= SynchedEntityData.defineId(MindsEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> NO_MIND= SynchedEntityData.defineId(MindsEntity.class, EntityDataSerializers.BOOLEAN);
    public int test = 0;
    public MindsEntityPart mind;
    public MindsEntity(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
        this.mind = new MindsEntityPart(this, "body", 0.3F, 0.25F);
        this.parts[0]=this.mind;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SPEED_UP, 0.0f);
        builder.define(NO_MIND, false);
        super.defineSynchedData(builder);
    }
    public void setSpeedUp(float speedup) {
        entityData.set(SPEED_UP,speedup);
    }
    public float getSpeedUp() {
        return entityData.get(SPEED_UP);
    }
    public boolean hasNoMind() {
        return entityData.get(NO_MIND);
    }
    public void setNoMind(boolean mi) {
         entityData.set(NO_MIND,mi);
    }

    public static AttributeSupplier.Builder createMindsAttributes() {
        return Zombie.createAttributes().add(Attributes.FOLLOW_RANGE, 25.0).add(Attributes.KNOCKBACK_RESISTANCE,1.1f).add(Attributes.MOVEMENT_SPEED, 0.13);
    }

    @Override
    public void load(CompoundTag compound) {
        setNoMind(compound.getBoolean("cf_no_mind"));
        super.load(compound);
    }

    @Override
    public boolean save(CompoundTag compound) {
        compound.putBoolean("cf_no_mind",hasNoMind());
        return super.save(compound);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void aiStep() {
        if(this.mind!=null&&this.mind.isAlive()&&!hasNoMind()){
            this.mind.setPos(getPosition(1).add(0,1.69,0));
            this.mind.xOld = this.mind.getX();
            this.mind.yOld = this.mind.getY();
            this.mind.zOld = this.mind.getZ();
            this.mind.xo = this.mind.getX();
            this.mind.yo = this.mind.getY();
            this.mind.zo = this.mind.getZ();
        }
        if(hasNoMind()&&this.mind!=null&&this.mind.isAlive()){
            this.mind.setPos(0,-70,0);
        }
        super.aiStep();
    }
    @Override
    public void tick() {

        if(hasNoMind()&&isAlive()&&deathTime<=0){
            level().addParticle(ParticleTypes.WITCH,getX(Math.random()*1.0f-0.5f),getY(0.9f+Math.random()/5.0f),getZ(Math.random()*1.0f-0.5f),0,0.2,0);
        }
        if(getSpeedUp()>0.0001f){
            setSpeedUp(getSpeedUp()*(hasNoMind()?0.94f:0.83f));
        }
        else{
            setSpeedUp(0);
        }
        super.tick();
    }

    @Override
    public @Nullable PartEntity<?>[] getParts() {
        return hasNoMind()?null:this.parts;
    }

    @Override
    public boolean isMultipartEntity() {
        return !hasNoMind();
    }
    @Override
    public float getSpeed() {
        return super.getSpeed()*(1.0f+(float)Math.min(Math.pow(getSpeedUp()*5.2f,0.5f)*1.5f,1.0f)*9.0f);
    }
    public boolean hurt(MindsEntityPart part, DamageSource source, float damage) {
        if(level().isClientSide){
            return false;
        }
        this.mind.discard();
        this.mind=null;
        if(hasNoMind()){
            return false;
        }
        entityData.set(NO_MIND,true);
        setSpeedUp(1.0f);
        boolean hurted = hurt(source,damage);
        return hurted;
    }

        @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.getDirectEntity()!=null&&source.getDirectEntity() instanceof Projectile &&source.getSourcePosition()!=null){

            if(this.mind!=null&&this.mind.getEyePosition().distanceTo(source.getDirectEntity().position())<0.8){
                entityData.set(NO_MIND,true);
                setSpeedUp(1.0f);
            }
        }
        if(getSpeedUp()<0.3f&&!hasNoMind()){
            if(source.is(DamageTypes.FALLING_ANVIL)&&!hasNoMind()){
                hurt(this.mind,source,amount*1.5f);
                entityData.set(NO_MIND,true);
                return false;
            }
            setSpeedUp(0.3f);
        }


        return super.hurt(source, amount);
    }
}
