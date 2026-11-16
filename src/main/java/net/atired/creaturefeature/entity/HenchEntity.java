package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HenchEntity extends Monster {
     public HenchEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.dir=new Vec3(1,0,0).yRot((float)Math.random()*4.0f*3.14f);
    }
    public Vec3 dir = new Vec3(0,0,0);
    @Override
    public void tick() {
        if(level() instanceof ServerLevel serverLevel && this.tickCount>80&&this.tickCount%40==0){
            this.hurt(damageSources().inWall(),10);
        }
        if(this.dir.length()>0.01&&this.tickCount<=24){
            this.setTarget(null);
            this.moveControl.setWantedPosition(getX()+dir.x,getY()+dir.y,getZ()+dir.z,1.8);
            if(this.tickCount==24||horizontalCollision){
                this.setDeltaMovement(this.dir.scale(-0.1).add(0,0.3,0));
                this.tickCount=24;
            }
        }else{
            if (getTarget() != null && (getTarget().getDeltaMovement().multiply(1, 0, 1).length() < 0.02&&getTarget().walkDist-getTarget().walkDistO<0.02)&&this.tickCount%4==0) {
                this.stopInPlace();
            }
        }
        super.tick();
    }

    @Override
    protected int getBaseExperienceReward() {
        return super.getBaseExperienceReward()/3;
    }

    @Override
    public float getSpeed() {
        return super.getSpeed()*(2.0f-Math.abs(Mth.sin(getId()/16.0f)));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        float oldHP=this.getHealth();
        boolean hurting = super.hurt(source, amount);
        return hurting;
    }
    public static boolean checkHenchSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return  checkMonsterSpawnRules(type, level, spawnType, pos, random);
    }


    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));

        super.registerGoals();
    }
    public static AttributeSupplier.Builder createHenchAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ATTACK_DAMAGE,2.0f).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.MAX_HEALTH,4.0);
    }
}
