package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PowderSkullEntity extends Monster {
    public PowderSkullEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new PowderSkullMoveControl(this);
    }

    @Override
    public void tick() {
        if(level()!=null){
            for (int i = 0; i < 2; i++) {
                Vec3 dir = getViewVector(1).scale(-1).multiply(1,0,1).normalize().scale(1.5);
                level().addParticle(CFParticleInit.RED_POWDER_PARTICLE.get(),dir.x*0.6f+getX((Math.random()-0.5)*0.7f),getY(Math.random()*0.7f+0.1),dir.z*0.6f+getZ((Math.random()-0.5)*0.7f),dir.x,0,dir.z);
            }
        }
        this.noPhysics=true;
        super.tick();
    }

    @Override
    public float getSpeed() {
        return 0.2f;
    }
    public static AttributeSupplier.Builder createPowderSkullAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.MOVEMENT_SPEED, 0.26).add(Attributes.MAX_HEALTH,16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1,new RandomFloatAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        super.registerGoals();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0f;
    }
    static class RandomFloatAroundGoal extends Goal {
        private final PowderSkullEntity powderskull;

        public RandomFloatAroundGoal(PowderSkullEntity ghast) {
            this.powderskull = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.powderskull.getMoveControl();
            if (!movecontrol.hasWanted()||(this.powderskull.getTarget()!=null)) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.powderskull.getX();
                double d1 = movecontrol.getWantedY() - this.powderskull.getY();
                double d2 = movecontrol.getWantedZ() - this.powderskull.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.powderskull.getRandom();
            if(this.powderskull.getTarget()!=null){
                this.powderskull.moveControl.setWantedPosition(this.powderskull.getTarget().getX(),this.powderskull.getTarget().getY()+1.6,this.powderskull.getTarget().getZ(),0.25f);
            }else{

                double d0 = this.powderskull.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d1 = this.powderskull.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.1F) * 12.0F);
                double d2 = this.powderskull.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.powderskull.getMoveControl().setWantedPosition(d0, d1, d2, 0.1);
            }
        }
    }
    static class PowderSkullMoveControl extends MoveControl {
        private final PowderSkullEntity powderskull;
        private int collisionCheckCooldown;

        public PowderSkullMoveControl(PowderSkullEntity powderskull) {
            super(powderskull);
            this.powderskull = powderskull;
        }

        public void tick() {

            MoveControl moveControl = this.powderskull.getMoveControl();

            if (this.operation == Operation.MOVE_TO) {
                if(this.powderskull.getTarget()!=null||(
                        this.powderskull.getDeltaMovement().length()>0.1&&this.powderskull.getPosition(1).distanceTo(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()))>1.2)){
                    this.powderskull.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                    this.powderskull.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.powderskull.getRandom().nextInt(5) + 2;
                    Vec3 Vec3 = new Vec3(this.wantedX - this.powderskull.getX(), this.wantedY - this.powderskull.getY(), this.wantedZ - this.powderskull.getZ());
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();
                    if (this.willCollide(Vec3, Mth.ceil(d))) {

                        this.powderskull.setDeltaMovement(this.powderskull.getDeltaMovement().scale(0.6).add(Vec3.scale(this.powderskull.getSpeed()*this.speedModifier)));
                    } else {
                        this.operation = Operation.WAIT;
                    }
                }

            }
        }

        private boolean willCollide(Vec3 direction, int steps) {
            AABB box = this.powderskull.getBoundingBox();
            for(int i = 1; i < steps; ++i) {
                box = box.move(direction);
                if (!this.powderskull.level().noCollision(this.powderskull, box)) {
                    return false;
                }
            }

            return true;
        }
    }
}
