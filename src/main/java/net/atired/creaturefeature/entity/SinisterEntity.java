package net.atired.creaturefeature.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class SinisterEntity extends Monster {
    private int internalCd=0;
    private static final EntityDataAccessor<Float> MIRROR= SynchedEntityData.defineId(SinisterEntity.class, EntityDataSerializers.FLOAT);

    public SinisterEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if(getMirrorBounce()>0.0f){
            setMirrorBounce(Math.max(0.0f,getMirrorBounce()-0.2f));
        }
        if(internalCd>0){
            internalCd-=1;
        }
    }
    public static boolean checkSinisterSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return  checkMonsterSpawnRules(type, level, spawnType, pos, random)&&pos.getY()<-1;
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(MIRROR, 0.0f);
        super.defineSynchedData(builder);
    }
    public void setMirrorBounce(float mirrorBounce) {
        entityData.set(MIRROR,mirrorBounce);
    }
    public float getMirrorBounce() {
        return entityData.get(MIRROR);
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));

        super.registerGoals();
    }
    public static AttributeSupplier.Builder createSinisterAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.MOVEMENT_SPEED, 0.16).add(Attributes.MAX_HEALTH,35.0);
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void push(Entity entity) {

    }

    @Override
    public float getSpeed() {
        if(getTarget()!=null){
            return super.getSpeed()*Mth.clamp(distanceTo(getTarget())/5.0f-0.4f,0.0f,1.0f);
        }
        return super.getSpeed();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.getSourcePosition()!=null){
            Vec3 vec = source.getSourcePosition().subtract(getPosition(1)).multiply(1,0,1).normalize().yRot(getYRot()/180.0f*3.14f);
            double dot = Mth.atan2(vec.x,vec.z)/3.14f;
            if(Math.abs(dot)<0.35){
                if(internalCd==0){
                    double d0 = source.getSourcePosition().x() - this.getX();
                    double d1 = source.getSourcePosition().z() - this.getZ();
                    internalCd=10;
                    knockback(0.1f,d0,d1);
                }
                this.playSound(SoundEvents.GLASS_BREAK, 0.6F, 0.5F);
                this.playSound(SoundEvents.SHIELD_BLOCK);
                if(source.getEntity() instanceof LivingEntity living && living.distanceTo(this)<4){
                    living.hurt(damageSources().mobAttack(this),amount*0.5f-0.4f);
                }
                if(getMirrorBounce()<0.2f)
                    setMirrorBounce(1.0f);
                return false;
            }
        }
        return super.hurt(source, amount);
    }
}
