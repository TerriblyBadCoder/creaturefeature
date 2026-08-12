package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFAchievements;
import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.entity.ai.control.BodyRotationControl;
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
import net.neoforged.neoforge.network.PacketDistributor;

public class SinisterEntity extends Monster {
    private int internalCd=0;
    private static final EntityDataAccessor<Float> MIRROR= SynchedEntityData.defineId(SinisterEntity.class, EntityDataSerializers.FLOAT);

    public SinisterEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getTarget()!=null&&this.getBoundingBox().inflate(0.2).move(getViewVector(1).multiply(1,0,1).scale(0.6)).intersects(this.getTarget().getBoundingBox())){
            this.push(this.getTarget());
        }
        if(getMirrorBounce()>0.0f){
            if(getMirrorBounce()>00.79f&&getMirrorBounce()<1f&&level()!=null){
                for (int i = 0; i < 4; i++) {
                    Vec3 dir = getViewVector(1);
                    dir=new Vec3(0,0,0.28+Math.random()/2f).scale(0.2).xRot((float)(Math.random()-0.5f)/10f).yRot((float)(Math.random()-0.5f)/10f).yRot((float)Mth.atan2(dir.x,dir.z));
                    Vec3 pos = getPosition(1).add((Math.random()-0.5)/2f,0.2+Math.random()*1.4,(Math.random()-0.5)/2f);
                    level().addParticle(CFParticleInit.GLASS_SHARD_SINISTER_PARTICLE.get(),pos.x,pos.y,pos.z,dir.x,dir.y,dir.z);
                }
            }
            setMirrorBounce(Math.max(0.0f,getMirrorBounce()-0.2f));
        }
        if(internalCd>0){
            internalCd-=1;
        }
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SinisterEntity.SinisterRotationControl(this);
    }
    @Override
    public void setYRot(float yRot) {
        super.setYRot(Mth.rotLerp(0.13f,this.getYRot(),yRot));
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
        entityData.set(MIRROR,Math.max(0f,mirrorBounce));
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
        return false;
    }

    @Override
    public void push(Entity entity) {

        if(this.getMirrorBounce()<0.2f&&entity==getTarget()){
            this.setMirrorBounce(0.78f);

            Vec3 dir = getViewVector(1).multiply(1,0,1).normalize().scale(0.7).add(0,0.1,0);
            entity.addDeltaMovement(dir);
            if(entity instanceof ServerPlayer player){
                VelSyncPayload payload = new VelSyncPayload(player.getId(),dir.x,dir.y,dir.z);
                PacketDistributor.sendToPlayersTrackingEntity((Entity)this,payload);
            }
            this.playSound(SoundEvents.GLASS_BREAK, 0.4F, 0.5F);
        }
    }

    @Override
    public float getSpeed() {
        if(getTarget()!=null){
            return super.getSpeed()*Mth.clamp(distanceTo(getTarget())/3.0f-0.4f,0.3f,2.0f);
        }
        return super.getSpeed();
    }

    @Override
    public int getHeadRotSpeed() {
        return 1;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.getSourcePosition()!=null){
            double dotted = source.getSourcePosition().subtract(getPosition(1)).multiply(1,0,1).normalize().dot(new Vec3(0,0,1).yRot(-getYRot()/180.0f*3.14f));
            double dot = dotted;
            if(dot>0.7){
                if(internalCd==0){
                    double d0 = source.getSourcePosition().x() - this.getX();
                    double d1 = source.getSourcePosition().z() - this.getZ();
                    internalCd=10;
                    knockback(0.4f,d0,d1);
                }
                this.playSound(SoundEvents.GLASS_BREAK, 0.6F, 0.5F);
                this.playSound(SoundEvents.SHIELD_BLOCK);
                if(source.getEntity() instanceof LivingEntity living && living.distanceTo(this)<4){
                    living.hurt(damageSources().mobAttack(this),amount*0.5f-0.4f);
                }
                if(getMirrorBounce()<0.2f)
                    setMirrorBounce(1.0f);
                return false;
            }else{
                if(source.getEntity()!=null&&source.getEntity() instanceof ServerPlayer serverPlayer){

                    CFAchievements.SINISTER.get().trigger(serverPlayer);
                }
            }
        }
        return super.hurt(source, amount);
    }

    public static class SinisterRotationControl extends BodyRotationControl{
        private SinisterEntity sinister = null;
        public SinisterRotationControl(SinisterEntity mob) {
            super(mob);
            this.sinister=mob;
        }

        @Override
        public void clientTick() {

            float olRot = this.sinister.yBodyRot;
            super.clientTick();
        }

    }
}
