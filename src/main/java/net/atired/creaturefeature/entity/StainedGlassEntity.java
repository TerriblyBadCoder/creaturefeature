package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.init.CFSoundInit;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.EnumSet;

public class StainedGlassEntity extends Monster {
    public Vec3[] positions = new Vec3[8];
    public int posTracker = 0;

    public int chargeCD=0;
    private static final EntityDataAccessor<Float> RETURNING= SynchedEntityData.defineId(StainedGlassEntity.class, EntityDataSerializers.FLOAT);
    
    public int floatingUp=-1;
    public StainedGlassEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new StainedGlassMoveControl(this);

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Player.class, 6.0F, 1.0, 1.2,(player)->{return (player instanceof Player realPlayer && this.getReturning()>0.01);}));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(RETURNING,0.0f);
        super.defineSynchedData(builder);
    }
    public void setReturning(float returning) {
        if(returning>=0.999f){
            playSound(CFSoundInit.SG_RETREAT.value(),0.6f,0.7f+(float)Math.random()/10f);
        }
        entityData.set(RETURNING,returning);
    }
    public float getReturning() {
        return entityData.get(RETURNING);
    }
    @Override
    public void tick() {
        if(onGround()){
            addDeltaMovement(new Vec3(0,0.33,0));
            this.navigation.stop();
        }
        if(this.posTracker<8){
            for(int i =0;i<8;i++){
                this.positions[i]=position();
                this.posTracker+=1;
            }
        }
        else{
            for (int i = 1; i < 8; i++) {
                this.positions[i-1]=this.positions[i];
            }
            this.positions[7]=position();
        }
        if(level()instanceof ServerLevel serverLevel&&getReturning()>=0.01){
            setReturning(getReturning()-0.05f);
            serverLevel.sendParticles(CFParticleInit.GLASS_SHARD_PARTICLE.get(),getX(),getY()+0.3,getZ(),1,0.1,0.1,0.1,0.1);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.TINTED_GLASS.defaultBlockState()),getX(),getY()+0.3,getZ(),3,0.1,0.1,0.1,0.1);
            setDeltaMovement(getDeltaMovement().scale(1.01));
        }
        if(this.chargeCD>0){
            this.chargeCD-=1;
        }
        super.tick();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.01;
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {

        return 0;
    }

    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }
    public static AttributeSupplier.Builder createStainedGlassAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ATTACK_DAMAGE,8.0f).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.MAX_HEALTH,10.0);
    }
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }
    static class RandomFloatAroundGoal extends Goal {
        private final StainedGlassEntity glass;

        public RandomFloatAroundGoal(StainedGlassEntity ghast) {
            this.glass = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.glass.getMoveControl();
            if (!movecontrol.hasWanted()||(this.glass.getTarget()!=null&&this.glass.floatingUp==-1&&this.glass.distanceTo(this.glass.getTarget())>5)) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.glass.getX();
                double d1 = movecontrol.getWantedY() - this.glass.getY();
                double d2 = movecontrol.getWantedZ() - this.glass.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 5.0 || d3 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.glass.getRandom();
            if(this.glass.getTarget()!=null&&this.glass.floatingUp<0&&this.glass.getTarget().getY()-this.glass.getY()>-1.2f&&this.glass.distanceTo(this.glass.getTarget())>5&&this.glass.chargeCD<=0){
                this.glass.floatingUp=-1;
                this.glass.moveControl.setWantedPosition(this.glass.getX(),this.glass.getY()+1.6,this.glass.getZ(),0.3f);
            }else if(this.glass.getTarget()!=null&&this.glass.chargeCD<=0&&this.glass.floatingUp<0&&this.glass.getTarget().getY()-this.glass.getY()<=-1.2f){
                this.glass.floatingUp=0;
                this.glass.chargeCD=20;

                this.glass.moveControl.setWantedPosition(
                        this.glass.getTarget().getX(),this.glass.getTarget().getY()+0.8,this.glass.getTarget().getZ(),0.7f);

            }
            else{

                this.glass.floatingUp=-2;
                double d0 = this.glass.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d1 = this.glass.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.1F) * 12.0F);
                double d2 = this.glass.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.glass.getMoveControl().setWantedPosition(d0, d1, d2, 0.3);
            }
        }
    }

    @Override
    protected AABB getAttackBoundingBox() {
        if(this.getDeltaMovement().length()<0.12){
            return super.getAttackBoundingBox();
        }
        return super.getAttackBoundingBox().inflate(.3).inflate(getDeltaMovement().x,getDeltaMovement().y,getDeltaMovement().z);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurted= super.hurt(source, amount);
        if(hurted&&source.getEntity()!=null&&this.getDeltaMovement().multiply(1,0,1).length()>0.2){
            setDeltaMovement(getDeltaMovement().scale(0.6).add(0,-0.3,0));
            this.setReturning(1.0f);
        }
        return hurted;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CFSoundInit.SG_DIE.value();
    }

    static class StainedGlassMoveControl extends MoveControl {
        private final StainedGlassEntity glass;
        private int collisionCheckCooldown;

        public StainedGlassMoveControl(StainedGlassEntity mosqo) {
            super(mosqo);
            this.glass = mosqo;
        }

        public void tick() {
            if(this.glass.getReturning()>0.01)return;
            MoveControl moveControl = this.glass.getMoveControl();
            LivingEntity i = this.glass.getTarget();
            if (this.operation == Operation.MOVE_TO) {
                if(i!=null||(
                        this.glass.getDeltaMovement().length()>0.1&&this.glass.getPosition(1).distanceTo(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()))>1.2)){
                    this.glass.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                    this.glass.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += 2;
                    Vec3 vec3 = new Vec3(this.wantedX - this.glass.getX(), this.wantedY - this.glass.getY(), this.wantedZ - this.glass.getZ());
                    double d = vec3.length();
                    vec3 = vec3.normalize();
                    if(i!=null&&this.glass.floatingUp==0&&this.glass.getReturning()<0.01){
                        this.glass.addDeltaMovement(new Vec3(0,-0.01,0));
                        Vec3 dir = new Vec3(this.wantedX,this.wantedY,this.wantedZ).lerp(i.position().add(0,1,0).subtract(glass.position()).normalize().scale(11).add(this.glass.position()),0.2);
                        this.glass.moveControl.setWantedPosition(
                                dir.x,dir.y,dir.z,0.7f);
                        if(this.glass.level() instanceof ServerLevel serverLevel){
                            serverLevel.sendParticles(CFParticleInit.GLASS_SHARD_PARTICLE.get(),this.glass.getX(),this.glass.getY()+0.3,this.glass.getZ(),1,0.1,0.1,0.1,0.01);
                        }
                    }
                    if (this.willCollide(vec3, Mth.ceil(d))) {
                        bounce(i);
                        if(this.glass.chargeCD<15){
                            this.glass.setDeltaMovement(this.glass.getDeltaMovement().scale(0.6).add(vec3.scale(this.speedModifier)));
                        }
                    } else {

                        if(!bounce(i)){
                            this.operation = Operation.WAIT;
                            this.glass.addDeltaMovement(new Vec3(0,-0.1,0));
                        }
                    }
                }

            }
        }
        private boolean bounce(LivingEntity i){
            if(i!=null&&this.glass.getReturning()<0.01&&i.hurtTime<=0&&this.glass.isWithinMeleeAttackRange(i)){
                Vec3 dir = this.glass.getLookAngle().multiply(1,0,1).normalize().add(0,0.4,0).scale(0.5);
                i.addDeltaMovement(dir);
                i.hurtDuration=0;
                i.hurt(this.glass.damageSources().mobProjectile(this.glass,this.glass),3);
                this.glass.playSound(SoundEvents.GLASS_HIT,1.6f,0.8f);
                this.glass.playSound(SoundEvents.GLASS_PLACE,1.6f,0.8f);
                this.glass.playSound(SoundEvents.DECORATED_POT_SHATTER,0.6f,0.8f);
                this.glass.playSound(SoundEvents.CHICKEN_HURT,0.1f,1.2f);
                i.hurtTime=0;
                i.invulnerableTime=0;
                i.hurtMarked=false;
                i.hurtDuration=0;
                if(i instanceof ServerPlayer player){
                    VelSyncPayload payload = new VelSyncPayload(player.getId(),dir.x,dir.y,dir.z);
                    PacketDistributor.sendToPlayer(player,payload);
                }
                this.operation = Operation.WAIT;
                this.wantedX=this.glass.getX()*3-this.wantedX*2;
                this.wantedY=this.glass.getY()*3-this.wantedY*2+1;
                this.wantedZ=this.glass.getZ()*3-this.wantedZ*2;
                this.glass.setDeltaMovement(this.glass.getLookAngle().multiply(1,0.0,1).normalize().scale(-2).add(0,0.3,0));
                this.glass.floatingUp=-2;
                this.glass.setReturning(1.0f);
                return true;
            }
            return false;
        }
        private boolean willCollide(Vec3 direction, int steps) {
            AABB box = this.glass.getBoundingBox();
            for(int i = 1; i < steps; ++i) {
                box = box.move(direction);
                if (!this.glass.level().noCollision(this.glass, box)) {
                    return false;
                }
            }

            return true;
        }
    }
}
