package net.atired.creaturefeature.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.phys.Vec3;

public class TrichaelEntity extends Monster {
    private int danceDelay = 200;
    private int danceAmount = 3;
    public int flipped = 0;
    private Vec3 danceAway = new Vec3(0,0,0);
    private static final EntityDataAccessor<Float> DANCING = SynchedEntityData.defineId(TrichaelEntity.class, EntityDataSerializers.FLOAT);
    public TrichaelEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {

        if(getTarget()!=null){
            this.danceDelay-=1;
            if(this.danceDelay<=0){
                setDancing(1.0f);
                this.danceAmount-=1;
                this.danceDelay=15;
                this.danceAway=getPosition(1).subtract(getTarget().getPosition(1)).multiply(1,0,1).normalize().scale(-1).yRot(((float)Math.random()-0.5f)*2.0f);
                if(this.getPosition(1).distanceTo(getTarget().getPosition(1))<4){
                    this.danceAway=this.danceAway.scale(-0.7);
                }
                if(this.danceAmount<=0){
                    this.danceDelay=50;
                    this.danceAmount=5;
                }
            }
        }
        if(level() instanceof ServerLevel serverLevel&&this.getDancing()>0){
            this.getNavigation().stop();
            this.addDeltaMovement(this.danceAway.scale(0.15));
            this.setDancing(Math.max(this.getDancing()-0.1f,0.0f));
            if(this.getDancing()==0.0f&&getTarget()!=null){
                this.getNavigation().moveTo(getTarget(),1.0);
                this.danceAway=this.danceAway.scale(0);
            }
        }
        if(getDancing()==0.9f){
            this.flipped+=1;
        }
        super.tick();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));


        super.registerGoals();
    }
    public static AttributeSupplier.Builder createTrichaelAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.ATTACK_DAMAGE,0.2f).add(Attributes.MOVEMENT_SPEED, 0.13).add(Attributes.MAX_HEALTH,25.0);
    }

    @Override
    public float getSpeed() {
        return super.getSpeed()*0.7f;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DANCING,0.0f);
        super.defineSynchedData(builder);
    }
    public void setDancing(float mirrorBounce) {
        entityData.set(DANCING,mirrorBounce);
    }
    public float getDancing() {
        return entityData.get(DANCING);
    }
}
