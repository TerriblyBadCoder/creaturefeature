package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFAchievements;
import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFSoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;

public class FriendEntity extends Monster {
    public Vec3 movementLerped = new Vec3(0,0,0);
    public int chargeDelay = 40;
    public float lerpedFriendFlattening = 0.0f;
    private static final EntityDataAccessor<Float> FLATTENED= SynchedEntityData.defineId(FriendEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DECAYING= SynchedEntityData.defineId(FriendEntity.class, EntityDataSerializers.BOOLEAN);

    public FriendEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean shouldDropLoot() {
        return super.shouldDropLoot()&&!isDecaying();
    }

    @Override
    public void tick() {
        if(this.tickCount>400&&getTarget()==null&&!level().isClientSide()){
            discard();
        }
        if(this.tickCount>150&&isDecaying()&&!level().isClientSide()){
            kill();
            setFlattened(2.0f);
        }
        if(this.chargeDelay==17){
            this.navigation.recomputePath();
        }
        if(!level().isClientSide()&&getFlattened()<=0&&getTarget()!=null&&chargeDelay--<=0){
            this.chargeDelay=40;
            playSound(CFSoundInit.FRIEND_HURT.value(),1.1f,1.3f+(float)Math.random()/10.0f);
            this.navigation.recomputePath();
            addDeltaMovement(getLookAngle().multiply(1,0,1).normalize().yRot(Math.random()>0.5?-(float)Math.PI/2.0f:(float)Math.PI/2.0f).scale(3));
            if(Math.random()>0.5&&!isDecaying()&&level() instanceof ServerLevel serverLevel){
                FriendEntity friendEntity = new FriendEntity(CFEntityInit.FRIEND.get(),serverLevel);
                friendEntity.setPos(getPosition(0).add(getDeltaMovement().multiply(1,0,1)));
                friendEntity.entityData.set(DECAYING,true);
                friendEntity.setHealth(0.5f);
                this.chargeDelay=20;
                friendEntity.setTarget(getTarget());
                friendEntity.getMoveControl().setWantedPosition(getTarget().getX(),getTarget().getY(),getTarget().getZ(),4.0);
                friendEntity.setDeltaMovement((getTarget().position().subtract(position())).multiply(1,0,1).normalize().scale(Math.pow(getTarget().position().distanceTo(position()),0.5)+0.2));
                serverLevel.addFreshEntity(friendEntity);
                setFlattened(1.0f);
            }
        }
        if(getTarget()!=null&&!level().isClientSide()&&Math.max(0.0f,getTarget().getY()-getY()-0.5f)>1.6f){
            setFlattened(Math.max(getFlattened(),1.2f));
        }
        if(!level().isClientSide()&&getFlattened()>0.0f){
            setFlattened(Math.max(0.0f,getFlattened()-0.05f));
        }
        this.lerpedFriendFlattening=Mth.lerp(0.33f,this.lerpedFriendFlattening,getFlattened());
        this.movementLerped=this.movementLerped.lerp(getDeltaMovement(),0.33);
        super.tick();
    }
    public void setFlattened(float toSet){
        entityData.set(FLATTENED,toSet);
    }
    public boolean isDecaying(){
        return entityData.get(DECAYING);
    }
    public float getFlattened(){
        return entityData.get(FLATTENED);
    }

    @Override
    protected AABB makeBoundingBox() {
        if(isDecaying()){
            return super.makeBoundingBox().deflate(0.4,0,0.4);
        }
        return super.makeBoundingBox();
    }

    @Override
    public boolean save(CompoundTag compound) {
        compound.putBoolean("is_friend_decaying",isDecaying());
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        entityData.set(DECAYING,compound.getBoolean("is_friend_decaying"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(getFlattened()>0.0f){
            return false;
        }
        boolean hurting = super.hurt(source, amount);
        if(source.getEntity()!=null&&hurting){
            if(source.getEntity() instanceof ServerPlayer serverPlayer){
                CFAchievements.FRIENDLESS.get().trigger(serverPlayer);
            }
            this.chargeDelay-=20;
            setFlattened(2.0f);
        }
        return hurting;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FLATTENED,0.0f);
        builder.define(DECAYING,false);
        super.defineSynchedData(builder);
    }
    public static AttributeSupplier.Builder createFriendAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 45.0).add(Attributes.STEP_HEIGHT,10.0f).add(Attributes.KNOCKBACK_RESISTANCE,1.0).add(Attributes.ATTACK_DAMAGE,10.0).add(Attributes.MOVEMENT_SPEED, 0.14).add(Attributes.MAX_HEALTH,50.0);
    }

    @Override
    public float getSpeed() {
        if(getFlattened()>0.0f){
            return super.getSpeed()+getFlattened()/2.0f;
        }
        return super.getSpeed()*(1.0f+ Mth.sin(tickCount/10.0f+getId())/1.1f)*8.7f;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if(getFlattened()>0.1||(isDecaying()&&this.tickCount<10))return false;
        boolean doHurt = super.doHurtTarget(entity);
        if(doHurt){
            entity.addDeltaMovement(new Vec3(0,0.3,0));
        }
        return doHurt;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        Predicate var10007 = EntitySelector.NO_CREATIVE_OR_SPECTATOR;
        Objects.requireNonNull(var10007);
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Player.class,(a)->{return this.getFlattened()>0.0f;}, 6.0F, 1.0, 1.2, var10007::test));

        super.registerGoals();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CFSoundInit.FRIEND_HURT.value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CFSoundInit.FRIEND.value();
    }


    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return CFSoundInit.FRIEND.value();
    }

    public static boolean checkFriendSpawnRules(EntityType<FriendEntity> friendEntityEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return checkMonsterSpawnRules(friendEntityEntityType,serverLevelAccessor,mobSpawnType,pos,randomSource)&&pos.getY()>126;
    }
}
