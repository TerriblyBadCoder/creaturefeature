package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class NoThingEntity extends Monster {
    private static final EntityDataAccessor<Float> REVEALNESS= SynchedEntityData.defineId(NoThingEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> LUNGE= SynchedEntityData.defineId(NoThingEntity.class, EntityDataSerializers.FLOAT);
    private int delay = 40;
    public NoThingEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
    @Override
    public void tick() {
        if(level() instanceof ServerLevel serverLevel){
            if(getTarget()!=null){
                delay-=1;
            }
            if(this.getRevealness()>0.0f){
                this.setRevealness(getRevealness()-0.075f);
            }
            if(this.getLunge()>0.0f){
                this.setLunge(getLunge()-0.1f);
            }
        }
        super.tick();
    }
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        Entity entity = damageSource.getEntity();
        if (entity != this && entity instanceof Skeleton skeleton) {
            this.spawnAtLocation(CFItemInit.NOTHING_DISC.get());
        }

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean supah= super.hurt(source, amount);
        if(supah){
            this.setRevealness(1.4f);
        }
        return supah;
    }

    public static AttributeSupplier.Builder createNothingAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.3)
                .add(Attributes.MOVEMENT_SPEED, 0.35)
                .add(Attributes.ATTACK_DAMAGE,7.0)
                .add(Attributes.MAX_HEALTH,40.0);
    }

    @Override
    public float getSpeed() {
        return super.getSpeed()*Math.max(0.0f,1.0f-getRevealness()*0.6f);
    }

    public float getLunge(){
        return this.entityData.get(LUNGE);
    }
    public void setLunge(float lunge){
        this.entityData.set(LUNGE,Math.clamp(lunge,-1.0f,1.0f));
    }
    public float getRevealness(){
        return this.entityData.get(REVEALNESS);
    }
    public void setRevealness(float revealness){
        this.entityData.set(REVEALNESS,Math.clamp(revealness,0.0f,2.5f));
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new NoThingChargeGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Endermite.class, true));

        super.registerGoals();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(REVEALNESS,0.0f);
        builder.define(LUNGE,0.0f);
    }
    public class NoThingChargeGoal extends Goal {
        public NoThingEntity mob;
        public int delay =0;
        public int beforeStopping = -1;
        public boolean STOPUSINGNOW = false;
        public NoThingChargeGoal(NoThingEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public void start() {
            STOPUSINGNOW=false;
            this.beforeStopping=40;

            this.mob.delay=70+this.mob.random.nextInt(0,30);
            this.delay=30+this.mob.random.nextInt(0,15);
            this.mob.setRevealness(1.4f);
            this.mob.setLunge(1.0f);
            this.mob.playSound(SoundEvents.VEX_CHARGE,1.0f,0.5f);
            this.mob.playSound(SoundEvents.VEX_AMBIENT,0.6f,0.5f);
            this.mob.playSound(SoundEvents.ENDERMAN_TELEPORT,0.7f,0.8f);
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
            STOPUSINGNOW=true;
        }

        @Override
        public void tick() {
            if(this.mob.getTarget()==null){
                this.beforeStopping=0;
                stop();
                return;
            }else{
                this.mob.lookAt(this.mob.getTarget(),500,500);
            }
            if(this.delay>0){
                this.delay-=1;
            }else{
                if(this.beforeStopping%5==0){
                    this.mob.navigation.moveTo(this.mob.getTarget(),1.4);
                }
                if(this.mob.distanceTo(this.mob.getTarget())<1.0){
                    if(this.mob.isWithinMeleeAttackRange(this.mob.getTarget())){
                        this.mob.swing(InteractionHand.MAIN_HAND);
                        this.mob.doHurtTarget(this.mob.getTarget());
                        this.mob.setRevealness(0.0f);
                        this.mob.navigation.stop();
                        Vec3 to = this.mob.getTarget().getPosition(1).subtract(this.mob.getPosition(1)).multiply(1,0,1).normalize();
                        float angle = (float)Math.atan2(to.x,to.z);
                        this.mob.setDeltaMovement(new Vec3(0,0.4,1).yRot(angle).multiply(-1.0,1.0,-1.0));

                        this.beforeStopping=0;
                        stop();
                        return;
                    }
                }

                this.beforeStopping-=1;
                if(this.beforeStopping<=0){
                    stop();
                    return;
                }
            }
            super.tick();
        }

        @Override
        public boolean canContinueToUse() {
            return (this.beforeStopping==-1||this.beforeStopping>0)&&!STOPUSINGNOW;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
        @Override
        public boolean canUse() {
            return this.mob!=null&&this.mob.delay<=0&&this.mob.onGround()&&this.mob.getTarget()!=null;
        }
    }
}
