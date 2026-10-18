package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class WraithEntity extends Monster {
    private static final EntityDataAccessor<Float> DRIFT= SynchedEntityData.defineId(WraithEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SWING= SynchedEntityData.defineId(WraithEntity.class, EntityDataSerializers.FLOAT);
    public Vec3 storedMovement = new Vec3(0,0,0);
    private int attackCount = 1;
    public WraithEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {

        if(level() instanceof ServerLevel serverLevel){
            setSwing(getSwing()*0.8f);
            if(this.tickCount%53>35){
                this.getNavigation().stop();
                if(this.tickCount%53==36){
                    if(getTarget()!=null&&getTarget().getPosition(1).distanceTo(getPosition(1))>1.2&&Math.random()>0.1&&this.attackCount<=0){
                        this.attackCount=2;
                        LightningDelayEntity entity = new LightningDelayEntity(CFEntityInit.DELAY.get(), serverLevel);
                        setSwing(1.0f);
                        Vec3 pos = getTarget().getPosition(1);
                        BlockHitResult result1 = level().clip(new ClipContext(
                                pos,
                                pos.add(0,-12,0),
                                ClipContext.Block.COLLIDER,
                                ClipContext.Fluid.NONE,
                                this));
                        if(result1!=null&&result1.getLocation()!=null){
                           pos=result1.getLocation();
                        }
                        entity.setPos(getPosition(1).add(0,0.04,0));
                        entity.setDeltaMovement(getPosition(1).subtract(pos).scale(-0.2));
                        serverLevel.addFreshEntity(entity);
                        this.setDeltaMovement(getDeltaMovement().scale(0.2));
                        addDeltaMovement(getViewVector(1).multiply(1,0,1).normalize().scale(-1.2).yRot((float)(Math.random()-0.5)*1.0f*1.57f).add(0,0.1,0));
                    }else{
                        this.attackCount-=1;
                        this.setDeltaMovement(getDeltaMovement().scale(0.2));
                        addDeltaMovement(getViewVector(1).multiply(1,0,1).normalize().scale(-0.8).yRot((float)(Math.random()-0.5)*2.0f*1.57f).add(0,0.1,0));
                    }

                }
                serverLevel.sendParticles(ParticleTypes.FALLING_WATER,getX(),getY(0.4),getZ(),1,0.3,0.8,0.3,0.0);
                this.entityData.set(DRIFT,Mth.clamp((float)Mth.lerp(0.23f,getDrift(),getSwing()*3.6f), 0.0f,1.0f));

            }
            else{
                this.storedMovement=this.storedMovement.lerp(getDeltaMovement(),0.5);
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.05,1.01,1.05).add(this.storedMovement.scale(0.21).multiply(1,0.01,1)));

                if(this.tickCount%4==0&&getTarget()!=null){
                    this.getNavigation().moveTo(getTarget(),1.0f);
                    this.addDeltaMovement(getViewVector(1).multiply(1,0,1).scale(0.2).yRot((float)(Math.random()-0.5)*1.0f*1.57f));
                }
                this.entityData.set(DRIFT,Mth.clamp((float)Mth.lerp(0.23f,getDrift(),Math.min(2.0f,this.getDeltaMovement().multiply(1,0,1).length()*20.4f-0.3)),0.0f,1.0f));

            }

        }
        super.tick();
    }
    public float getDrift(){
        return this.entityData.get(DRIFT);
    }
    public float getSwing(){
        return this.entityData.get(SWING);
    }
    public void setSwing(float sw){
        this.entityData.set(SWING,sw);
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{WraithEntity.class})));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
        super.registerGoals();
    }
    public static AttributeSupplier.Builder createWraithAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.ATTACK_DAMAGE,4.0).add(Attributes.MOVEMENT_SPEED, 0.16).add(Attributes.MAX_HEALTH,25.0);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DRIFT,0.0f);
        builder.define(SWING,0.0f);

        super.defineSynchedData(builder);
    }

    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public float getSpeed() {
        return super.getSpeed()*1.0f;
    }
}
