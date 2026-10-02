package net.atired.creaturefeature.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FishEyeEntity extends Monster {
    public float risingUp = 0.0f;
    public FishEyeEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new FisheyeMoveControl( this);
    }

    @Override
    public void tick() {
        this.risingUp*=0.95f;
        super.tick();
    }
    public static AttributeSupplier.Builder createFishEyeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.ATTACK_DAMAGE,8.0f).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.MAX_HEALTH,16.0);
    }
    @Override
    protected double getDefaultGravity() {
        return -(this.risingUp-0.5f)*0.64f;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1,new RandomFloatAroundGoal(this));
        super.registerGoals();
    }

    @Override
    public MoveControl getMoveControl() {
        return super.getMoveControl();
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    public float getSpeed() {
        return 0.4f;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }
    static class RandomFloatAroundGoal extends Goal {
        private final FishEyeEntity fisheye;

        public RandomFloatAroundGoal(FishEyeEntity ghast) {
            this.fisheye = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.fisheye.getMoveControl();
            if (!movecontrol.hasWanted()||(this.fisheye.getTarget()!=null)) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.fisheye.getX();
                double d1 = movecontrol.getWantedY() - this.fisheye.getY();
                double d2 = movecontrol.getWantedZ() - this.fisheye.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            Vec3 pos = getPosition();
            if(pos!=null){
                this.fisheye.setPos(pos);
            }
            RandomSource randomsource = this.fisheye.getRandom();
            double d0 = this.fisheye.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.fisheye.getY() -20;
            double d2 = this.fisheye.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.fisheye.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            this.fisheye.risingUp=1.0f;
            this.fisheye.setDeltaMovement(0,0.2,0);
        }
        @Nullable
        protected Vec3 getPosition() {
            return DefaultRandomPos.getPos(this.fisheye, 5, 3);
        }
    }
    static class FisheyeMoveControl extends MoveControl {
        private final FishEyeEntity fisheye;
        private int collisionCheckCooldown;

        public FisheyeMoveControl(FishEyeEntity mosqo) {
            super(mosqo);
            this.fisheye = mosqo;
        }

        public void tick() {

            MoveControl moveControl = this.fisheye.getMoveControl();

            if (this.operation == Operation.MOVE_TO) {
                if(this.fisheye.getTarget()!=null||(
                        this.fisheye.getDeltaMovement().length()>0.1&&this.fisheye.getPosition(1).distanceTo(new Vec3(moveControl.getWantedX(),this.fisheye.getY()+this.fisheye.getDeltaMovement().y*9.0,moveControl.getWantedZ()))>1.2)){
                    this.fisheye.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),this.fisheye.getY()+this.fisheye.getDeltaMovement().y*9.0,moveControl.getWantedZ()));
                    this.fisheye.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),this.fisheye.getY()+this.fisheye.getDeltaMovement().y*9.0,moveControl.getWantedZ()));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown = 2;
                    Vec3 Vec3 = new Vec3(this.wantedX - this.fisheye.getX(), -0.1+(this.wantedY-this.fisheye.getY())/12.0f, this.wantedZ - this.fisheye.getZ());
                    double horLen = Vec3.multiply(1,0,1).length();
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();
                    if ((this.willCollide(this.fisheye.getDeltaMovement().normalize(), Mth.ceil(d))||this.fisheye.risingUp>0.7)&&(horLen>0.1||!this.fisheye.onGround())) {
                        this.fisheye.setDeltaMovement(this.fisheye.getDeltaMovement().scale(0.6).add(Vec3.scale(this.fisheye.getSpeed())));
                    } else {
                        this.wantedY=this.fisheye.getY();
                        this.operation=Operation.WAIT;
                        this.fisheye.stopInPlace();
                    }
                }

            }
        }

        private boolean willCollide(Vec3 direction, int steps) {
            AABB box = this.fisheye.getBoundingBox();
            if(direction.y>0.1)return true;
            for(int i = 1; i < steps; ++i) {
                box = box.move(direction.scale(1.5));
                if (!this.fisheye.level().noCollision(this.fisheye, box)) {
                    return false;
                }
            }

            return true;
        }
    }
}
