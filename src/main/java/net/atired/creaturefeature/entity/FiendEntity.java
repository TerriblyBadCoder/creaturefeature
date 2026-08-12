package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFItemInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FiendEntity extends Monster {
    public int kickDelay = 80;
    public int shotDelay = 160;
    public int shotCount=0;
    private static final EntityDataAccessor<Float> CRIT = SynchedEntityData.defineId(FiendEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> KICK = SynchedEntityData.defineId(FiendEntity.class, EntityDataSerializers.FLOAT);

    public FiendEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RangedFiendAttackGoal(this, 1.0,7,7));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));

        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{Skeleton.class})));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Villager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        super.registerGoals();
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData spawnGroupData2 = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
        RandomSource randomsource = level.getRandom();
        if (randomsource.nextInt(12) == 0) {
            Skeleton skeleton = (Skeleton) EntityType.SKELETON.create(this.level());
            if (skeleton != null) {
                skeleton.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                skeleton.finalizeSpawn(level, difficulty, spawnType, (SpawnGroupData) null);
                skeleton.startRiding(this);
            }
        }
        return spawnGroupData2;
    }


    public void setKick(float spin) {
        entityData.set(KICK,Math.clamp(spin,0.0f,1.0f));
    }
    public float getKick() {
        return entityData.get(KICK);
    }
    public void setCrit(float spin) {
        entityData.set(CRIT,Math.clamp(spin,0.0f,1.0f));
    }
    public float getCrit() {
        return entityData.get(CRIT);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(KICK,0.0f);
        builder.define(CRIT,0.0f);
        super.defineSynchedData(builder);
    }

    @Override
    public void tick() {
        super.tick();
        if(level().isClientSide()&&getKick()>=0.9f&&getBlockStateOn()!=null){
            for (int i = 0; i < 32; i++) {
                Vec3 dir = getViewVector(1).multiply(1,0,1).normalize().yRot((float)Math.random()-0.5f).add(0,Math.random()/6.0f,0).multiply(500,1,500);
                level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK,getBlockStateOn()),getX(),getY()+0.3,getZ(),dir.x,dir.y,dir.z);
                level().addParticle(new BlockParticleOption(ParticleTypes.DUST_PILLAR,getBlockStateOn()),getX(),getY()+0.3,getZ(),dir.x,dir.y,dir.z);
            }

        }
        if(level() instanceof ServerLevel serverLevel&&getKick()>0.0f){
            setKick(getKick()-0.1f);

        }
        if(level() instanceof ServerLevel serverLevel&&getCrit()>0.0f){
            setCrit(getCrit()-0.01f);
            if(getCrit()<0.5f&&(int)(getCrit()*100f)%3==0&&getCrit()>0.4f){
                BulletEntity bulletEntity=new BulletEntity(CFEntityInit.BULLET.get(),serverLevel);
                bulletEntity.ownerUsual=this;
                serverLevel.addFreshEntity(bulletEntity);
                Vec3 dir = new Vec3(0,0,1).yRot(-yHeadRot/180f*3.14f).scale(0.8).add(0,1.6,0).add(getPosition(1));
                bulletEntity.setPos(dir.add((Math.random()-0.5)/16f,(Math.random()-0.5)/16f,(Math.random()-0.5)/16f));
                bulletEntity.setYRot(yHeadRot);
                bulletEntity.setXRot(getXRot());
                serverLevel.sendParticles(CFParticleInit.CASING_PARTICLE.get(),bulletEntity.getX(),bulletEntity.getY(),bulletEntity.getZ(),1,0,0,0,0.2);

            }
            if(getTarget()!=null)
                lookAt(getTarget(),30,30);
            if(getCrit()<0.35f){
                Vec3 dir = getViewVector(1).multiply(1,0,1).normalize().scale(0.8).add(0,1.45,0).add(getPosition(1));
                setCrit(0f);
                this.kickDelay=2;
                this.shotDelay=3;
                this.shotCount+=1;
                if(this.shotCount==3){
                    this.shotCount=0;
                    this.shotDelay=200;
                }

                serverLevel.sendParticles(CFParticleInit.CASING_PARTICLE.get(),dir.x,dir.y,dir.z,4,0,0,0,0.2);
                serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, CFItemInit.FLINTLOCK.toStack()),dir.x,dir.y,dir.z,14,0,0,0,0.4);
                serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, CFItemInit.FLINTLOCK.toStack()),dir.x,dir.y,dir.z,14,0,0,0,0.2);
            }
            return;
        }
        if(this.getTarget()!=null){
            if(Math.random()>0.4&&this.kickDelay>3&&this.shotDelay<0&&Math.abs(getY()-getTarget().getY())<0.1&&getCrit()<0.02f){
                setCrit(0.6f);
                this.navigation.stop();
                this.stopInPlace();
            }
            if(this.position().distanceTo(this.getTarget().getPosition(1))>1.23){
                this.kickDelay-=1;
                this.shotDelay-=1;
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.EVOKER_HURT;
    }



    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    @Override
    public float getSpeed() {
        if(getCrit()>0)return 0;
        return super.getSpeed();
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch()*0.6f;
    }

    public static AttributeSupplier.Builder createFiendAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 15.0).add(Attributes.ATTACK_DAMAGE,5.0).add(Attributes.MOVEMENT_SPEED, 0.34).add(Attributes.MAX_HEALTH,25.0);
    }
    public class RangedFiendAttackGoal extends Goal {
        private final FiendEntity mob;
        private final double speedModifier;
        private int attackIntervalMin;
        private final float attackRadiusSqr;
        private int attackTime;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime;


        public RangedFiendAttackGoal(FiendEntity p_25792_, double p_25793_, int p_25794_, float p_25795_) {
            this.attackTime = -1;
            this.strafingTime = -1;
            this.mob = p_25792_;
            this.speedModifier = p_25793_;
            this.attackIntervalMin = p_25794_;
            this.attackRadiusSqr = p_25795_ * p_25795_;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public void setMinAttackInterval(int attackCooldown) {
            this.attackIntervalMin = attackCooldown;
        }

        public boolean canUse() {
            return this.mob.getTarget() != null &&this.mob.kickDelay<=10;
        }


        public boolean canContinueToUse() {
            return (this.canUse() || !this.mob.getNavigation().isDone());
        }

        public void start() {
            super.start();
            this.mob.setAggressive(true);
        }

        public void stop() {
            super.stop();
            this.mob.setAggressive(false);
            this.seeTime = 0;
            this.attackTime = -1;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.mob.getTarget();
            if (livingentity != null) {
                double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1) {
                    this.seeTime = 0;
                }

                if (flag) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.mob.getNavigation().stop();
                    ++this.strafingTime;
                } else {
                    this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 20) {
                    if ((double)this.mob.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.mob.getRandom().nextFloat() < 0.3) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (d0 > (double)(this.attackRadiusSqr * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if (d0 < (double)(this.attackRadiusSqr * 0.25F)) {
                        this.strafingBackwards = true;
                    }

                    this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    Entity var7 = this.mob.getControlledVehicle();
                    if (var7 instanceof Mob) {
                        Mob mob = (Mob)var7;
                        mob.lookAt(livingentity, 30.0F, 30.0F);
                    }

                    this.mob.lookAt(livingentity, 30.0F, 30.0F);
                } else {
                    this.mob.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
                }

                if(this.mob.kickDelay<=0&&!this.mob.getBlockStateOn().isEmpty()&&this.mob.getPosition(1).distanceTo(livingentity.getPosition(1))>1.5){
                    this.mob.kickDelay=40;
                    this.mob.setKick(1.0f);
                    if(this.mob.level() instanceof ServerLevel serverLevel){
                        KickedBlockEntity entity = new KickedBlockEntity(CFEntityInit.KICKED_BLOCK.get(), serverLevel);
                        entity.setPos(this.mob.getPosition(1).add(0,0.2,0));
                        Vec3 dir = this.mob.getViewVector(1).multiply(1,0,1).normalize().scale(0.4).scale(0.4+Math.pow(this.mob.getPosition(1).distanceTo(livingentity.getPosition(1)),0.5)).add(0,0.45+(livingentity.getY()-this.mob.getY())/3.0f,0);
                        this.mob.addDeltaMovement(dir.scale(0.3));
                        this.mob.playSound(SoundEvents.VEX_AMBIENT,1.0f,0.5f+(float)Math.random()/5.0f);
                        this.mob.playSound(this.mob.getBlockStateOn().getSoundType().getBreakSound(),2.0f,0.5f+(float)Math.random()/5.0f);

                        entity.setDeltaMovement(dir.x,dir.y,dir.z);
                        entity.state=this.mob.getBlockStateOn();
                        entity.setOwner(this.mob);
                        serverLevel.addFreshEntity(entity);

                    }
                    this.mob.getNavigation().stop();
                    stop();
                }
            }

        }
    }
}
