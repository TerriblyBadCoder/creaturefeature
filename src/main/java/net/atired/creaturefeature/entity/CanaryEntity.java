package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;

public class CanaryEntity extends Monster {
    public Vec3[] positions = new Vec3[25];
    public ArrayList<CanaryPart> parts = new ArrayList<>();
    public int posTracker = 0;
    public CanaryEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new CanaryMoveControl(this);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
    public static boolean checkCanarySpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return  checkMonsterSpawnRules(type, level, spawnType, pos, random)&&pos.getY()<-15&&Math.random()>0.4;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.01f;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        super.registerGoals();
    }

    @Override
    public void tick() {
        if(onGround()&&getDeltaMovement().y<-0.03){
            this.getNavigation().stop();
            this.setDeltaMovement(getDeltaMovement().x,0.1,getDeltaMovement().z);
        }
        if(getTarget()!=null){
            if(getY()<getTarget().getY()+1){
                addDeltaMovement(new Vec3(0,0.02,0));
            }
        }
        if(true){
            if(this.posTracker<25){
                this.positions[this.posTracker]=position();
                if(level() instanceof ServerLevel level){
                    if(this.posTracker%2==0&&this.posTracker<15){
                        CanaryPart part = new CanaryPart(CFEntityInit.CANARY_PART.get(),level());
                        part.setParent(this.getId());
                        part.setPos(getPosition(1));
                        level.addFreshEntity(part);
                        part.setPart(this.posTracker/1.5f);
                        this.parts.add(part);
                    }

                }
                this.posTracker+=1;
            }
            else{
                for (int i = 1; i < 25; i++) {
                    this.positions[i-1]=this.positions[i];
                }
                this.positions[24]=position();
                if(level() instanceof ServerLevel level){
                    for(int i = 22;i>=8;i-=2){
                        CanaryPart part = this.parts.get((i-8)/2);
                        if(!part.isAlive()||part.isRemoved()){
                            part = new CanaryPart(CFEntityInit.CANARY_PART.get(),level());
                            part.setParent(this.getId());
                            part.setPos(getPosition(1));
                            level.addFreshEntity(part);
                            part.setPart(i/1.5f);
                            this.parts.set((i-8)/2,part);
                        }
                        part.setPos(this.positions[i+1]);
                        part.lookAt(EntityAnchorArgument.Anchor.EYES,this.positions[i+2]);
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }
    public static AttributeSupplier.Builder createCanaryAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.ATTACK_DAMAGE,6.0).add(Attributes.MOVEMENT_SPEED, 0.31).add(Attributes.MAX_HEALTH,30.0);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }
    static class RandomFloatAroundGoal extends Goal {
        private final CanaryEntity minedflayer;

        public RandomFloatAroundGoal(CanaryEntity ghast) {
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
            if(this.minedflayer.getTarget()!=null&&
                    (this.minedflayer.getTarget().getPosition(1).distanceTo(this.minedflayer.getPosition(1))>8||
                            false)){
                Vec3 dir = this.minedflayer.getTarget().position().add(0,1.2,0).subtract(this.minedflayer.getPosition(1)).normalize().scale(20).add(this.minedflayer.getPosition(1));
                this.minedflayer.moveControl.setWantedPosition(dir.x,dir.y,dir.z,1.5f);
            }else if(this.minedflayer.getTarget()==null){

                double d0 = this.minedflayer.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 32.0F);
                double d1 = this.minedflayer.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.1F) * 12.0F);
                double d2 = this.minedflayer.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 32.0F);
                this.minedflayer.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            }
        }
    }
    static class CanaryMoveControl extends MoveControl {
        private final CanaryEntity flayer;
        private int collisionCheckCooldown;
        private int stalloutTimer = 30;
        private Vec3 stallOutPos = new Vec3(0,0,0);

        public CanaryMoveControl(CanaryEntity mosqo) {
            super(mosqo);
            this.flayer = mosqo;
        }

        public void tick() {

            MoveControl moveControl = this.flayer.getMoveControl();

            if (this.operation == Operation.MOVE_TO) {
                if(this.flayer.getTarget()!=null&&this.flayer.isWithinMeleeAttackRange(this.flayer.getTarget())){
                    this.flayer.getTarget().hurt(this.flayer.damageSources().mobAttack(this.flayer),5.5f);
                }
                if(this.stallOutPos.distanceTo(this.flayer.getPosition(1))<0.1){
                    this.stalloutTimer-=1;
                }
                else
                    this.stalloutTimer=5;
                this.stallOutPos=this.flayer.getPosition(1);
                if(this.stalloutTimer<0){
                    this.operation = Operation.WAIT;
                    this.flayer.navigation.stop();

                }
                if(this.flayer.getTarget()!=null||(
                        this.flayer.getPosition(1).distanceTo(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()))>1.2)){
                    this.flayer.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                    this.flayer.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.flayer.getRandom().nextInt(5) + 2;
                    Vec3 Vec3 = new Vec3(this.wantedX - this.flayer.getX(), this.wantedY - this.flayer.getY(), this.wantedZ - this.flayer.getZ());
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();
                    if (this.willCollide(Vec3, Mth.ceil(d))||this.flayer.getTarget()!=null) {

                        this.flayer.setDeltaMovement(this.flayer.getDeltaMovement().scale(0.99).add(Vec3.scale(0.2f).xRot((float)(Mth.cos(this.flayer.tickCount*0.05f))*0.1f).yRot((float)(Mth.sin(this.flayer.tickCount*0.05f))*0.1f)));
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
