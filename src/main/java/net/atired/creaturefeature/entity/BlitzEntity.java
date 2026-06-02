package net.atired.creaturefeature.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class BlitzEntity extends Monster {
    public BlitzEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
    public float offsetMovement = 0.0f;
    public Vec3[] positions = new Vec3[5];
    public int posTracker = 0;
    public int leapDelay = 30;
    @Override
    public void tick() {
        if(getTarget()!=null&&distanceTo(getTarget())>2){
            if(this.leapDelay>0){
                this.leapDelay-=1;
            }
            if(random.nextFloat()>0.9&&this.leapDelay>10&&this.onGround()&&Math.abs(offsetMovement)<=0.02f){
                offsetMovement=1.0f;
                if(random.nextFloat()>0.5f){
                    offsetMovement=-1.0f;
                }
            }
            if(Math.abs(offsetMovement)>0.02f){
                this.offsetMovement= Mth.lerp(0.15f,this.offsetMovement,0.0f);
                Vec3 to = getTarget().getPosition(1).subtract(getPosition(1)).multiply(1,0,1).normalize();
                float angle = (float)Math.atan2(to.x,to.z);
                setYRot(angle/3.14f*180.0f);
                Vec3 target = getTarget().getPosition(1.0f);
                this.moveControl.setWantedPosition(target.x,target.y,target.z,2.0);
                this.getNavigation().recomputePath();
                addDeltaMovement(new Vec3(0.5,0,0).multiply(offsetMovement,offsetMovement,offsetMovement).yRot(-getYRot()/180.0f*3.14f));
            }
        }
        if(tickCount%3==0){
            if(this.posTracker<5){
                this.positions[this.posTracker]=position();
                this.posTracker+=1;
            }
            else{
                for (int i = 1; i < 5; i++) {
                    this.positions[i-1]=this.positions[i];
                }
                this.positions[4]=position();
            }
        }

        super.tick();
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BREEZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BREEZE_DEATH;
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(1, new BlitzChargeGoal(this));

        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));

        super.registerGoals();
    }

    public static AttributeSupplier.Builder createBlitzAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.STEP_HEIGHT,1.5).add(Attributes.MOVEMENT_SPEED, 0.34).add(Attributes.ATTACK_DAMAGE,6.0).add(Attributes.MAX_HEALTH,25.0);
    }
    public class BlitzChargeGoal extends Goal{
        public BlitzEntity mob;
        public int delay =0;
        public int beforeStopping = -1;
        public boolean STOPUSINGNOW = false;
        public BlitzChargeGoal(BlitzEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public void start() {
            STOPUSINGNOW=false;
            this.mob.offsetMovement=0.0f;
            this.beforeStopping=-1;
            Vec3 to = this.mob.getTarget().getPosition(1).subtract(this.mob.getPosition(1)).multiply(1,0,1).normalize();
            float angle = (float)Math.atan2(to.x,to.z);
            this.mob.leapDelay=40;
            this.delay=6;
            float dist =(float) Math.pow(this.mob.distanceTo(this.mob.getTarget())/1.8,0.5f)/1.2f;
            this.mob.setDeltaMovement(new Vec3(0,0.6,dist).yRot(angle));
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
                stop();
                return;
            }else{
                this.mob.lookAt(this.mob.getTarget(),500,500);
            }
            if(beforeStopping>0){
                beforeStopping-=1;
                if(beforeStopping==0){
                    stop();
                    return;
                }else{
                    return;
                }
            }



            this.delay-=1;
            LivingEntity target = this.mob.getTarget();
            if(this.delay<=0&&this.beforeStopping==-1){
                this.delay=3;
                if(this.mob.onGround()&&this.beforeStopping==-1){
                    if(this.mob.level() instanceof ServerLevel serverLevel){
                        for (int x = -2;x < 2; x++) {
                            for (int z = -2;z < 2; z++) {
                                BlockPos pos = this.mob.getOnPos().offset(x,0,z);
                                if(!this.mob.level().getBlockState(pos).isAir()){
                                    BlockState state = this.mob.level().getBlockState(pos);
                                    Vec3 center = pos.getCenter().add(0,0.5,0);
                                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,state),center.x,center.y,center.z,2,0.3,0,0.3,0.3);
                                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.DUST_PILLAR,state),center.x,center.y,center.z,2,0.3,0,0.3,0.3);
                                }
                            }
                        }
                        for(LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class,this.mob.getBoundingBox().inflate(2.5))){
                            if(living.onGround()&&living!=this.mob){
                                living.hurt(this.mob.damageSources().mobAttack(this.mob),1.5f);
                                living.addDeltaMovement(new Vec3(0,0.4,0));
                            }
                        }
                    }

                    stop();
                }
                if (this.mob.isWithinMeleeAttackRange(target) && this.mob.getSensing().hasLineOfSight(target)) {

                    Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.mob.getTarget().getPosition(1));
                    if(vec3!=null){
                        this.mob.swing(InteractionHand.MAIN_HAND);
                        if(this.mob.doHurtTarget(target)){
                            this.mob.navigation.moveTo(this.mob.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0),1.0);
                            this.beforeStopping=10;
                        }

                        return;
                    }
                    else{
                        this.delay=0;
                    }
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
            return this.mob!=null&&this.mob.leapDelay==0&&this.mob.onGround()&&this.mob.getTarget()!=null&&this.mob.distanceTo(getTarget())>3&&this.mob.distanceTo(getTarget())<15;
        }
    }
}
