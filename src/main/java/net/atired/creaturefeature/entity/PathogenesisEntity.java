package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.networking.payloads.PathogenPayload;
import net.atired.creaturefeature.networking.payloads.ToadstoolRenderPayload;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.EnumSet;
import java.util.function.Predicate;

public class PathogenesisEntity extends Monster {
    private static final EntityDataAccessor<Float> FADING = SynchedEntityData.defineId(PathogenesisEntity.class, EntityDataSerializers.FLOAT);

    public PathogenesisEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new PathogenMoveControl(this);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if(entity instanceof Monster monster){
            if(getFading()==0.0f&&monster instanceof LivingEntityGoopAccessor accessor && accessor.getDelay()<=0){
                if(level() instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(CFParticleInit.SPARKLE_PARTICLE.get(),monster.getX(),monster.getY()+monster.getBbHeight()/2.0f,monster.getZ(),4,0.3,0.3,0.3,0.1);
                    serverLevel.sendParticles(CFParticleInit.SPARKLE_PARTICLE.get(),getX(),getY()+0.4f,getZ(),4,0.3,0.3,0.3,0.1);
                }
                monster.heal(7.0f);
                if(getHealth()>1){
                    accessor.setBacterial(getHealth());
                }
                setFading(0.1f);
            }
            return false;
        }
        return super.doHurtTarget(entity);
    }

    @Override
    public void tick() {

        if((isInWall())&&getTarget()==null){
            this.noPhysics = true;
            setNoGravity(true);
            addDeltaMovement(new Vec3(0,0.3,0));
        } else if(getTarget()==null){
            setNoGravity(false);
            this.noPhysics=false;
        }
        if(getTarget()!=null){
            this.noPhysics=true;
            setNoGravity(true);
        }
        if(getFading()>0.05f&&level() instanceof ServerLevel serverLevel){
            float gotten = getFading();
            setFading(gotten+0.2f);
            if(gotten>1.0f-0.06f){
                discard();
            }
        }
        if(isInLiquid()){
            addDeltaMovement(new Vec3(0,0.1,0));
        }
        setDeltaMovement(getDeltaMovement().scale(0.97));
        super.tick();
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Monster.class,10, false, false,liver->{return !(liver instanceof PathogenesisEntity)&&tickCount>40&&(liver instanceof Monster monster && monster.getHealth()<monster.getMaxHealth());}));
        super.registerGoals();
    }
    public void setFading(float spin) {
        entityData.set(FADING,Math.clamp(spin,0.0f,1.0f));
    }
    public float getFading() {
        return entityData.get(FADING);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(FADING,0.0f);
        super.defineSynchedData(builder);
    }
    public static AttributeSupplier.Builder createPathogenAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 60.0).add(Attributes.KNOCKBACK_RESISTANCE,-1.0).add(Attributes.ATTACK_DAMAGE,2.0).add(Attributes.MOVEMENT_SPEED, 0.18).add(Attributes.MAX_HEALTH,10.0);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.01;
    }
    static class RandomFloatAroundGoal extends Goal {
        private final PathogenesisEntity patho;

        public RandomFloatAroundGoal(PathogenesisEntity ghast) {
            this.patho = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.patho.getMoveControl();
            if (!movecontrol.hasWanted()||this.patho.getTarget()!=null) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.patho.getX();
                double d1 = movecontrol.getWantedY() - this.patho.getY();
                double d2 = movecontrol.getWantedZ() - this.patho.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.patho.getRandom();
            if(this.patho.getTarget()!=null){
                this.patho.moveControl.setWantedPosition(this.patho.getTarget().getX(),this.patho.getTarget().getY()+this.patho.getTarget().getBbHeight()/2.0f,this.patho.getTarget().getZ(),1.5f);
            }else{

                double d0 = this.patho.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d1 = this.patho.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.1F) * 12.0F);
                if(d1>this.patho.getY()){
                    d1=d1*0.5+this.patho.getY()*0.5;
                }
                double d2 = this.patho.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.patho.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            }
                
        }
    }

    @Override
    public float getSpeed() {
        return super.getSpeed()+0.2f;
    }

    static class PathogenMoveControl extends MoveControl {
        private final PathogenesisEntity patho;
        private int collisionCheckCooldown;

        public PathogenMoveControl(PathogenesisEntity mosqo) {
            super(mosqo);
            this.patho = mosqo;
        }

        public void tick() {

            MoveControl moveControl = this.patho.getMoveControl();

            if (this.operation == Operation.MOVE_TO) {
                this.patho.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                this.patho.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                if(this.patho.getTarget()!=null&&this.patho.isWithinMeleeAttackRange(this.patho.getTarget())){
                    this.patho.doHurtTarget(this.patho.getTarget());
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.patho.getRandom().nextInt(5) + 2;
                    Vec3 Vec3 = new Vec3(this.wantedX - this.patho.getX(), this.wantedY - this.patho.getY(), this.wantedZ - this.patho.getZ());
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();
                    if (this.willCollide(Vec3, Mth.ceil(d))) {

                        this.patho.setDeltaMovement(this.patho.getDeltaMovement().scale(0.6).add(Vec3.scale(this.patho.getSpeed()).xRot((float)(Mth.cos(this.patho.tickCount*0.2f))*0.4f).yRot((float)(Mth.sin(this.patho.tickCount*0.2f))*0.4f)));
                    } else {
                        this.operation = Operation.WAIT;
                    }
                }

            }
        }

        private boolean willCollide(Vec3 direction, int steps) {
            AABB box = this.patho.getBoundingBox();
            for(int i = 1; i < steps; ++i) {
                box = box.move(direction);
                if (!this.patho.level().noCollision(this.patho, box)) {
                    return false;
                }
            }

            return true;
        }
    }
}
