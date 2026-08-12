package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFItemInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class SaintSolisEntity extends Monster {
    public int fireDelay=100;
    private static final EntityDataAccessor<Float> OPENING= SynchedEntityData.defineId(SaintSolisEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Vector3f> DIR= SynchedEntityData.defineId(SaintSolisEntity.class, EntityDataSerializers.VECTOR3);
    public SaintSolisEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if(level() instanceof ServerLevel serverLevel){
            if(getTarget()!=null){
                this.fireDelay-=1;
                if(this.fireDelay<10){
                    if(getOpening()<0.2f){
                        setDir(new Vector3f(1,0,0).rotateZ(Mth.sin((float)Math.random()*3.14f*8.0f)/3.0f+0.5f).rotateY((float)Math.random()*3.14f*8.0f));
                    }
                    setOpening(Mth.lerp(0.3f,getOpening(),1.0f));
                    if(this.fireDelay<=5&&this.fireDelay>1){
                        playSound(SoundEvents.POLAR_BEAR_WARNING,1.0f,0.6f);
                        playSound(SoundEvents.CAMPFIRE_CRACKLE,0.4f,1.5f);
                        StarProjEntity star = new StarProjEntity(level(), this);
                        star.shoot(getDir().x, getDir().y,getDir().z, 0.8F, 40f);
                        star.setPos(getEyePosition());
                        star.toHurt=getTarget();
                        serverLevel.addFreshEntity(star);
                    }
                    if(this.fireDelay<=0){

                        this.fireDelay=40;
                    }
                }
                else{
                    setOpening(getOpening()*0.5f);
                }
            }
            else{
                setOpening(getOpening()*0.8f);
            }
        }

        super.tick();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));

        super.registerGoals();
    }

    @Override
    public int getHeadRotSpeed() {
        return 1;
    }
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        Entity var5 = damageSource.getDirectEntity();
        if (var5 instanceof StarProjEntity creeper) {
            this.spawnAtLocation(CFItemInit.SOLAR_SHARD.get().getDefaultInstance());
        }
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OPENING,0.0f);
        builder.define(DIR,new Vector3f());
        super.defineSynchedData(builder);
    }
    public Vector3f getDir(){
        return entityData.get(DIR);
    }
    public void setDir(Vector3f wing){
        entityData.set(DIR,wing);
    }
    public float getOpening(){
        return entityData.get(OPENING);
    }
    public void setOpening(float wing){
        entityData.set(OPENING,wing);
    }

    public static AttributeSupplier.Builder createSaintSolisAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.MOVEMENT_SPEED, 0.16).add(Attributes.MAX_HEALTH,35.0);
    }
    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }
}
