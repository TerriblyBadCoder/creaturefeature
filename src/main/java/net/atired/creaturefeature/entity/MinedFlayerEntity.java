package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MinedFlayerEntity extends Monster {
    public float selfRot = 0.0f;
    public MinedFlayerEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new MinedFlayerMoveControl(this);
    }
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    @Override
    public void tick() {
        for(Player player : level().getEntitiesOfClass(Player.class,getBoundingBox().inflate(4.2))){
            if(player instanceof PlayerBrainrotAccessor accessor&&!player.isCreative()){
                accessor.setBrainrot(accessor.getBrainrot()+0.1f);
                selfRot=(float)Math.clamp(selfRot+.05f,0.0,1.0f);
            }
        }
        if(selfRot>0.0f){
            selfRot-=0.025f;
        }
        super.tick();
    }

    @Override
    public float getSpeed() {
        if(getTarget()!=null){
            return 0.2f*Math.clamp(((float)getPosition(1).distanceTo(getTarget().position())-4.0f)/4.0f,-0.2f,1.0f);
        }
        return 0.2f;
    }

    public static AttributeSupplier.Builder createFlayerAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.26).add(Attributes.MAX_HEALTH,15.0);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0f;
    }

    static class RandomFloatAroundGoal extends Goal {
        private final MinedFlayerEntity minedflayer;

        public RandomFloatAroundGoal(MinedFlayerEntity ghast) {
            this.minedflayer = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.minedflayer.getMoveControl();
            if (!movecontrol.hasWanted()||(this.minedflayer.getTarget()!=null)) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.minedflayer.getX();
                double d1 = movecontrol.getWantedY() - this.minedflayer.getY();
                double d2 = movecontrol.getWantedZ() - this.minedflayer.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.minedflayer.getRandom();
            if(this.minedflayer.getTarget()!=null){
                this.minedflayer.moveControl.setWantedPosition(this.minedflayer.getTarget().getX(),this.minedflayer.getTarget().getY()+1.6,this.minedflayer.getTarget().getZ(),1.5f);
            }else{

                double d0 = this.minedflayer.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d1 = this.minedflayer.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.1F) * 12.0F);
                double d2 = this.minedflayer.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.minedflayer.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            }
        }
    }
    static class MinedFlayerMoveControl extends MoveControl {
        private final MinedFlayerEntity flayer;
        private int collisionCheckCooldown;

        public MinedFlayerMoveControl(MinedFlayerEntity mosqo) {
            super(mosqo);
            this.flayer = mosqo;
        }

        public void tick() {

            MoveControl moveControl = this.flayer.getMoveControl();

            if (this.operation == Operation.MOVE_TO) {
                if(this.flayer.getTarget()!=null||(
                        this.flayer.getDeltaMovement().length()>0.1&&this.flayer.getPosition(1).distanceTo(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()))>1.2)){
                    this.flayer.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                    this.flayer.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.flayer.getRandom().nextInt(5) + 2;
                    Vec3 Vec3 = new Vec3(this.wantedX - this.flayer.getX(), this.wantedY - this.flayer.getY(), this.wantedZ - this.flayer.getZ());
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();
                    if (this.willCollide(Vec3, Mth.ceil(d))) {

                        this.flayer.setDeltaMovement(this.flayer.getDeltaMovement().scale(0.6).add(Vec3.scale(this.flayer.getSpeed()).xRot((float)(Mth.cos(this.flayer.tickCount*0.2f))*0.4f).yRot((float)(Mth.sin(this.flayer.tickCount*0.2f))*0.4f)));
                    } else {
                        this.operation = Operation.WAIT;
                    }
                }

            }
        }

        private boolean willCollide(Vec3 direction, int steps) {
            AABB box = this.flayer.getBoundingBox();
            for(int i = 1; i < steps; ++i) {
                box = box.move(direction);
                if (!this.flayer.level().noCollision(this.flayer, box)) {
                    return false;
                }
            }

            return true;
        }
    }
}
