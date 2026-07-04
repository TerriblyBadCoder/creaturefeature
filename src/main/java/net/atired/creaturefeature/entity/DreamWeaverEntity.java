package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.Config;
import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.EnumSet;

public class DreamWeaverEntity extends Spider {
    private static final EntityDataAccessor<Float> RUPTURING= SynchedEntityData.defineId(DreamWeaverEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> SPAWNED_ID= SynchedEntityData.defineId(DreamWeaverEntity.class, EntityDataSerializers.INT);
    public LivingEntity theSummonee = null;
    public DreamWeaverEntity(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if(this.theSummonee==null){
            this.theSummonee=getSpawned(entityData.get(SPAWNED_ID));
        }
        if(getRupturing()>0.0f){
            setRupturing(getRupturing()-0.1f);
        }
        super.tick();
    }
    public LivingEntity getSpawned(int type){
        if(type==10){
            return new EnderMan(EntityType.ENDERMAN,level());
        }
        if(type==0){
            return new Illusioner(EntityType.ILLUSIONER,level());
        };
        if(type==1){
            return new Vex(EntityType.VEX,level());
        };
        if(type==2){
            return new FendEntity(CFEntityInit.FEND.get(), level());
        };
        if(type==3){
            return new NoThingEntity(CFEntityInit.NOTHING.get(), level());
        };
        if(type==4){
            return new TropicalFish(EntityType.TROPICAL_FISH, level());
        };
        if(type==6){
            return new DreamWeaverEntity(CFEntityInit.DREAMWEAVER.get(), level());
        };
        return new Zombie(level());
    }
    @Override
    public void load(CompoundTag compound) {
        entityData.set(SPAWNED_ID,compound.getInt("spawned_bonus_id"));
        super.load(compound);
    }

    @Override
    public boolean save(CompoundTag compound) {
        compound.putInt("spawned_bonus_id",entityData.get(SPAWNED_ID));
        compound.putFloat("spider_rupturing",getRupturing());

        return super.save(compound);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        int spawnIdDream = random.nextInt(0,7);
        if(Config.END_DREAMWEAVER.isTrue()){
            spawnIdDream=6;
            if(Math.random()>0.5)spawnIdDream=3;
            if(Math.random()>0.5)spawnIdDream=10;
        }
        entityData.set(SPAWNED_ID,spawnIdDream);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RUPTURING,-1.0f);
        builder.define(SPAWNED_ID,0);
    }
    public void setRupturing(float rupturing){
        entityData.set(RUPTURING,rupturing);
    }
    public float getRupturing(){
        return entityData.get(RUPTURING);
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hasHurt = super.hurt(source, amount);
        if(getRupturing()<=-1.0f&&this.theSummonee!=null&&hasHurt&&getHealth()>0.01f&&getHealth()<getMaxHealth()*0.75f){
            setRupturing(1.0f);
            int spawned = entityData.get(SPAWNED_ID);
            int count = 4;
            if(spawned==0||spawned==10){count=2;}
            if(spawned==2||spawned==5){count=3;}
            if(spawned==4){count=12;}
            if(spawned==6){count=1;}
            Vector3f dir2 = new Vector3f(0,0.6f,-0.6f).rotateY(getYRot()/180.0f*3.14f);

            for (int i = 0; i < count; i++) {
                this.theSummonee.setPos(position().add(new Vec3(dir2).multiply(3,1,3)));
                Vec3 dir = new Vec3(1,0,0).scale(0.5f+Math.random()/2.0f).yRot((float)Math.random()*3.14f*4.0f);
                this.theSummonee.setDeltaMovement(dir2.x,dir2.y+Math.random()*0.2f,dir2.z);
                this.theSummonee.addDeltaMovement(dir);
                if(level() instanceof ServerLevel serverLevel){
                    if(this.theSummonee instanceof Mob mob){
                        mob.setTarget(getTarget());
                        mob.finalizeSpawn(serverLevel,this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.SPAWNER, (SpawnGroupData)null);
                        if(mob instanceof MagmaCube magmaCube){
                            magmaCube.setSize(1,true);
                        }
                        if(mob instanceof DreamWeaverEntity entity && Math.random()>0.2){
                            entity.setRupturing(-2.0f);
                            entity.entityData.set(SPAWNED_ID,6);
                        }
                    }
                    serverLevel.addFreshEntity(this.theSummonee);
                }
                this.theSummonee=getSpawned(spawned);
            }
        }
        return hasHurt;
    }

    @Override
    public float getSpeed() {
        return super.getSpeed();
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(3, new DreamLeapAtTargetGoal(this, 0.55F));
        super.registerGoals();
        for(WrappedGoal goal : this.goalSelector.getAvailableGoals()){
            if(goal!=null&&goal.getGoal() instanceof LeapAtTargetGoal goal1){
                this.goalSelector.removeGoal(goal1);
            }
        }
    }

    public static AttributeSupplier.Builder createWeaverAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.36).add(Attributes.ATTACK_DAMAGE,3.0).add(Attributes.MAX_HEALTH,24.0);
    }
    public static class DreamLeapAtTargetGoal extends Goal {
        private final Mob mob;
        private LivingEntity target;
        private final float yd;

        public DreamLeapAtTargetGoal(Mob mob, float yd) {
            this.mob = mob;
            this.yd = yd;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            if (this.mob.hasControllingPassenger()) {
                return false;
            } else {
                if(this.mob instanceof DreamWeaverEntity dreamWeaverEntity && dreamWeaverEntity.getRupturing()<-0.5f){
                    return false;
                }
                this.target = this.mob.getTarget();
                if (this.target == null) {
                    return false;
                } else {
                    double d0 = this.mob.distanceTo(this.target);
                    if (!(d0 < 2.0) && !(d0 > 18.0)) {
                        return !this.mob.onGround() ? false : this.mob.getRandom().nextInt(reducedTickDelay(7)) == 0;
                    } else {
                        return false;
                    }
                }
            }
        }

        public boolean canContinueToUse() {
            return !this.mob.onGround();
        }

        public void start() {
            Vec3 vec3 = this.mob.getDeltaMovement();
            Vec3 vec31 = new Vec3(this.target.getX() - this.mob.getX(), 0.0, this.target.getZ() - this.mob.getZ());
            vec31 = vec31.normalize().scale(0.6).add(vec3.scale(0.2));
            vec31=vec31.yRot((float)(-Math.random()+0.5f)/7.0f).scale(1.4f);
            if(Math.random()>0.66){vec31=vec31.scale(-0.8);}
            this.mob.setDeltaMovement(vec31.x, (double)this.yd*0.7f, vec31.z);
        }
    }
}
