package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BeautyEntity extends Monster {
    private static final EntityDataAccessor<Float> SPIN= SynchedEntityData.defineId(BeautyEntity.class, EntityDataSerializers.FLOAT);

    public BeautyEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if(getSpin()>0.0f&&!level().isClientSide()){
            setSpin(Math.max(getSpin()-0.05f,0.0f));
        }
        if(getSpin()>0.2f&&level().isClientSide()){
            level().addParticle(CFParticleInit.LEAF_PARTICLE.get(),getX(Math.random()-0.5),getY(Math.random()/1.5f+0.4),getZ(Math.random()-0.5),0,0.1,0);
        }
        super.tick();
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

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!level().isClientSide()){
            if(source.is(DamageTypeTags.IS_PROJECTILE)){
                setSpin(1.5f);
                this.playSound(SoundEvents.BREEZE_WHIRL, 1.0F, 1.1F);
                this.playSound(SoundEvents.GRASS_BREAK);
                addDeltaMovement(getLookAngle().multiply(-0.5,0.0,-0.5).add(0,0.1,0));
                if(source.getDirectEntity()!=null){
                    source.getDirectEntity().setDeltaMovement(source.getDirectEntity().getDeltaMovement().multiply(3,1,3).add(0,0.3,0));
                }
                return false;
            }else{
                if(getSpin()>0.1f){
                    return super.hurt(source,amount);
                }
                setSpin(1.5f);
                this.playSound(SoundEvents.BREEZE_WHIRL, 1.0F, 1.1F);
                this.playSound(SoundEvents.GRASS_BREAK);
                addDeltaMovement(getLookAngle().multiply(-0.5,0.0,-0.5).add(0,0.1,0));
                return false;
            }
        }
        if(getSpin()<=0.1f){
            return false;
        }
        return super.hurt(source,amount);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean shallHurt = super.doHurtTarget(entity);
        if(getSpin()<1.0&&shallHurt){
            entity.addDeltaMovement(new Vec3(0,0.1,0));
            setSpin(1.0f);
        }
        return shallHurt;
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected double getDefaultGravity() {
        return super.getDefaultGravity()*0.7f*(1.0f-getSpin());
    }
    public static AttributeSupplier.Builder createBeautyAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.ATTACK_DAMAGE,4.0).add(Attributes.MOVEMENT_SPEED, 0.31).add(Attributes.MAX_HEALTH,10.0).add(Attributes.ATTACK_KNOCKBACK,-1.0);
    }
    public void setSpin(float spin) {
        entityData.set(SPIN,spin);
    }
    public float getSpin() {
        return entityData.get(SPIN);
    }

    @Override
    public float getSpeed() {
        return super.getSpeed()*(1.0f-getSpin());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SPIN,0.0f);
        super.defineSynchedData(builder);
    }



}
