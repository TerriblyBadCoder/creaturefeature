package net.atired.creaturefeature.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class MockingBirdEntity extends Monster {
    public float extendedWings = 0.0f;
    public int fallDelay =60;
    public int landDelay = 0;
    public int lungeDelay = 40;
    public boolean shouldChase=true;
    private static final EntityDataAccessor<Float> FLAPPING= SynchedEntityData.defineId(MockingBirdEntity.class, EntityDataSerializers.FLOAT);

    public MockingBirdEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        super.registerGoals();
    }
    public float getWingspan(){
        return this.extendedWings;
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FLAPPING,0.0f);
        super.defineSynchedData(builder);
    }
    public float getFlapping(){
        return entityData.get(FLAPPING);
    }
    public void setFlapping(float wing){
        entityData.set(FLAPPING,wing);
    }
    public static AttributeSupplier.Builder createMockingBirdAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.KNOCKBACK_RESISTANCE,-1.0).add(Attributes.ATTACK_DAMAGE,5.0).add(Attributes.MOVEMENT_SPEED, 0.24).add(Attributes.MAX_HEALTH,25.0);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurted = super.hurt(source,amount);
        if(hurted){
            if(getTarget()!=null){
                lookAt(getTarget(),360,360);
                setYBodyRot(getYRot());
            }
            this.landDelay=2;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        super.knockback(strength*4.0, x, z);
    }

    @Override
    public void tick() {
        if(shouldChase&&level()!=null&&!level().isClientSide){
            setFlapping(Mth.lerp(0.3f,getFlapping(),0.0f));
        }
        if(getTarget()!=null){
            Vec3 dir = getViewVector(1).multiply(1,0,1).normalize().scale(0.12);
            if(onGround()){
                lungeDelay-=1;
                landDelay-=1;
                fallDelay=0;
                if(landDelay<=0){
                    shouldChase=true;
                }
                if(landDelay<=-10&&lungeDelay<0&&level() instanceof ServerLevel serverLevel){
                    Vec3 dir2 = getTarget().getPosition(1).subtract(position()).multiply(1,0,1).normalize().scale(0.2);
                    addDeltaMovement(new Vec3(-dir2.x*5.0,0.7,-dir2.z*5.0));
                    lookAt(getTarget(),360,360);
                    lungeDelay=140;
                    playSound(SoundEvents.CHICKEN_HURT,1.7f,0.5f);
                    playSound(SoundEvents.CHICKEN_HURT,1.7f,1.2f);
                    playSound(SoundEvents.EVOKER_CELEBRATE,1.4f,0.8f);
                    landDelay=2;
                    for (int i = 0; i < 7; i++) {
                        FeatherEntity snowball = new FeatherEntity(level(), this);
                        snowball.shootFromRotation(this, getXRot(), getYRot(), 0.0F, 0.7F, 45.0F);
                        serverLevel.addFreshEntity(snowball);
                    }
                    shouldChase=false;
                }
            }
            if(fallDelay>20){
                landDelay-=3;
                fallDelay=0;
            }
            if(getDeltaMovement().y<0.0&&!onGround()&&landDelay>0){
                getNavigation().stop();
                lookAt(getTarget(),1,3);
                setYBodyRot(getYRot());
                fallDelay+=1;
                setDeltaMovement(getDeltaMovement().multiply(1,0.6,1));
                if(!shouldChase){
                    if(fallDelay>10&&level()instanceof ServerLevel serverLevel){
                        playSound(SoundEvents.DISPENSER_LAUNCH,0.7f,0.7f);
                        playSound(SoundEvents.CHICKEN_AMBIENT,1.0f,0.7f);
                        FeatherEntity snowball = new FeatherEntity(level(), this);
                        snowball.shootFromRotation(this, getXRot(), getYRot(), 0.0F, 1.5F, 14.0F);
                        serverLevel.addFreshEntity(snowball);
                    }
                    setFlapping(Mth.lerp(0.3f,getFlapping(),1.0f));
                    dir=dir.scale(-0.1);
                    dir.add(0,0.06,0);
                }
                addDeltaMovement(new Vec3(dir.x,0.01+dir.y,dir.z));
            }

        }

        if(onGround()){
            this.extendedWings=Math.max(0.0f,this.extendedWings-0.1f);
        }else{
            if(this.getFlapping()>0.1){
                this.extendedWings= Mth.lerp(0.4f,this.extendedWings,1.0f);
            }
            this.extendedWings= Mth.lerp(0.1f,this.extendedWings,1.0f);
        }
        super.tick();

    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch()*0.7f;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }
}
