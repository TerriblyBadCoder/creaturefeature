package net.atired.creaturefeature.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MachinationEntity extends Monster {
    public int leapCD = 0;
    private static final EntityDataAccessor<Float> CHARGE= SynchedEntityData.defineId(MachinationEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> LEAPING= SynchedEntityData.defineId(MachinationEntity.class, EntityDataSerializers.BOOLEAN);

    public MachinationEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new MachinationLeapGoal(this, 1.0, true));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, MachinationEntity.class, true,(target)->{
            if(target instanceof MachinationEntity machinationEntity){
                return machinationEntity.getHealth()<(machinationEntity.getMaxHealth()-0.5);
            }
            return false;
        }));

        super.registerGoals();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean hurt = super.doHurtTarget(entity);
        if(hurt&&!level().isClientSide()){
            this.navigation.recomputePath();
            this.moveControl.strafe(-1.3f,0);
            Vec3 dir = (this.position().subtract(entity.position()).multiply(1,0.1,1).normalize().multiply(0.7,0.3,0.7).add(0,0.3,0));
            if(this.isLeaping()){
                entity.addDeltaMovement(dir.multiply(-1.3,1.5,-1.3));
                addDeltaMovement(dir.multiply(-0.3,1.2,-0.3));
            }
            else{
                addDeltaMovement(dir);
            }

        }
        return hurt;
    }

    @Override
    public void tick() {
        if(this.leapCD>0){
            this.leapCD-=1;
        }
        if(getTarget()!=null){
            setCharge(getCharge()+0.03f);
            if(getCharge()<=0.02f){
                this.addDeltaMovement(getLookAngle().multiply(1,0,1).normalize().scale(0.1));
            }
            if(this.distanceTo(getTarget())<3.5f){
                this.navigation.moveTo(getTarget(),1.2f);
            }
        }else if(getCharge()>0.01&&level()!=null&&!level().isClientSide()){
            setCharge(getCharge()*0.7f);
        }

        super.tick();
    }

    public void setLeaping(boolean leap) {
         entityData.set(LEAPING,leap);
    }
    public boolean isLeaping() {
        return entityData.get(LEAPING);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(LEAPING, false);
        builder.define(CHARGE, 0.0f);
        super.defineSynchedData(builder);
    }
    public void setCharge(float charged){
        entityData.set(CHARGE,charged%1);
    }
    public float getCharge(){
        return entityData.get(CHARGE);
    }
    @Override
    public float getSpeed() {
        return super.getSpeed()*(0.33f+(float)Math.pow(Math.max(0.0f,getCharge()-0.5f)*2.0f,0.5f)*2.0f);
    }

    public static AttributeSupplier.Builder createMachinationAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 25.0).add(Attributes.KNOCKBACK_RESISTANCE,0.3).add(Attributes.ARMOR,10.0).add(Attributes.MOVEMENT_SPEED, 0.33).add(Attributes.ATTACK_DAMAGE,5.0f).add(Attributes.ATTACK_KNOCKBACK,1.6f);
    }
    public class MachinationLeapGoal extends Goal{
        private int timeleft = 30;
        protected final MachinationEntity mob;
        private LivingEntity target;
        public MachinationLeapGoal(MachinationEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public void start() {
            this.mob.setLeaping(true);
            this.timeleft=100;
            this.target = this.mob.getTarget();
            this.mob.leapCD=100;
            this.mob.moveControl.strafe(-1.3f,0);
            this.mob.lookAt(this.target,360f,360f);
            addDeltaMovement((this.mob.position().subtract(this.mob.getTarget().position()).multiply(1,0.1,1).normalize().multiply(-1.7,0.3,-1.7).add(0,Math.pow(this.mob.distanceTo(this.mob.getTarget())*0.4,0.5)/3.0f,0)));

        }

        @Override
        public void stop() {
            this.mob.setLeaping(false);
            this.timeleft=0;
            super.stop();
        }

        @Override
        public void tick() {
            super.tick();
            if(this.timeleft<=0||this.target==null){
                stop();
                return;
            }
            LivingEntity entity = this.target;
            if(this.mob.isWithinMeleeAttackRange(entity) && this.mob.getSensing().hasLineOfSight(entity)){
                if(this.mob.doHurtTarget(entity)){
                    this.mob.swing(InteractionHand.MAIN_HAND);
                    stop();
                    return;
                }
            }
            this.timeleft-=1;
        }

        @Override
        public boolean canContinueToUse() {
            return this.timeleft>0&&(this.timeleft>90||!this.mob.onGround());
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
        @Override
        public boolean canUse() {
            return this.mob.getTarget()!=null&&this.mob.leapCD==0&&Math.abs(this.mob.getY()-this.mob.getTarget().getY())<0.1f*this.mob.distanceTo(this.mob.getTarget())&&
                    this.mob.distanceTo(this.mob.getTarget())<12&&this.mob.distanceTo(this.mob.getTarget())>4&&this.mob.onGround();
        }
    }
}
