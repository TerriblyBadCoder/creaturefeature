package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.init.CFSoundInit;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;

public class FishEyeEntity extends Monster {
    private static final EntityDataAccessor<Float> APPEAR = SynchedEntityData.defineId(FishEyeEntity.class, EntityDataSerializers.FLOAT);
    public float risingUp = 0.0f;
    public float trailing = 0.0f;
    public int storedIndex = -1;
    public Vec3[] positions = new Vec3[15];
    public int posTracker = 0;
    public FishEyeEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new FisheyeMoveControl( this);
    }

    @Override
    public void tick() {
        boolean wasOnGround=this.onGround();

        if(level() instanceof ServerLevel serverLevel){
            setAppear(getAppear()*0.8f);
        }
        this.trailing=Math.clamp(Mth.lerp(0.1f+getAppear()*2.9f,this.trailing,getAppear()>0.03?1.0f:0.0f),0.0f,1.0f);
        if(!isNoAi()) {

            if (this.posTracker < 15) {
                this.positions[this.posTracker] = position();
                
                this.posTracker += 1;
            } else {
                for (int i = 1; i < 15; i++) {
                    this.positions[i - 1] = this.positions[i];
                }
                this.positions[14] = position();
                if(this.getAppear()>0.9){
                    this.storedIndex=14;
                }
                if(this.getAppear()>0.1&&this.storedIndex>0){
                    this.storedIndex-=1;
                    if(this.storedIndex==0){
                        this.storedIndex=-1;
                    }
                }
            }
        }
        this.risingUp*=0.95f;
        super.tick();
        if(this.getAppear()>0.9f){
            this.xOld=this.getX();
            this.yOld=this.getY();
            this.zOld=this.getZ();
        }
        if(this.onGround()&&!wasOnGround){
            playSound(SoundEvents.SALMON_FLOP,2.0f,0.7f-(float)Math.random()/12.0f);
        }
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
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
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
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        if(onGround && this.fallDistance > 0.0F){
            playSound(SoundEvents.SALMON_FLOP,2.0f,0.7f-(float)Math.random()/12.0f);
        }
        return;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return super.causeFallDamage(fallDistance, multiplier, source);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(APPEAR,0.0f);
        super.defineSynchedData(builder);
    }
    public void setAppear(float mirrorBounce) {
        entityData.set(APPEAR,mirrorBounce);
    }
    public float getAppear() {
        return entityData.get(APPEAR);
    }
    static class RandomFloatAroundGoal extends Goal {
        private final FishEyeEntity fisheye;

        public RandomFloatAroundGoal(FishEyeEntity ghast) {
            this.fisheye = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if(this.storedDelay>0)return true;
            MoveControl movecontrol = this.fisheye.getMoveControl();
            if((this.fisheye.horizontalCollision||this.fisheye.verticalCollision)){
                if (!movecontrol.hasWanted()) {
                    return true;
                } else {
                    double d0 = movecontrol.getWantedX() - this.fisheye.getX();
                    double d1 = movecontrol.getWantedY() - this.fisheye.getY();
                    double d2 = movecontrol.getWantedZ() - this.fisheye.getZ();
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    return d3 < 1.0 || d3 > 3600.0;
                }
            }

            return false;
        }

        public boolean canContinueToUse() {
            return false;
        }
        public <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, SimpleParticleType type, double posX, double posY, double posZ, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
            int i = 0;
            for(int j = 0; j < serverLevel.getPlayers((a)->{return true;}).size(); ++j) {
                ServerPlayer serverplayer = (ServerPlayer)serverLevel.getPlayers((a)->{return true;}).get(j);
                if (serverLevel.sendParticles(serverplayer,type, true, posX, posY, posZ,particleCount,xOffset,yOffset,zOffset,speed)) {
                    ++i;
                }
            }

            return i;
        }
        public Vec3 storedPos = new Vec3(0,0,0);
        public int storedDelay = -1;
        public void start() {
            Vec3 pos = getPosition();
            boolean dontDoThat=false;
            if(this.storedDelay>0){
                this.storedDelay-=1;
                if(this.storedDelay==0){
                    if(this.fisheye.level() instanceof ServerLevel serverLevel) {
                        sendParticles(serverLevel,CFParticleInit.FISHEYE_PARTICLE.get(),this.fisheye.getX(),this.fisheye.getY()+0.1,this.fisheye.getZ(),1,0,0,0,0.1);
                    }
                    this.fisheye.playSound(CFSoundInit.FISHEYE_SCREECH.value(),0.6f,1.0f+(float)Math.random()/4.0f);
                    this.fisheye.setPos(this.storedPos);
                    if(this.fisheye.level() instanceof ServerLevel serverLevel) {
                        sendParticles(serverLevel,CFParticleInit.FISHEYE_PARTICLE.get(),this.fisheye.getX(),this.fisheye.getY()+0.1,this.fisheye.getZ(),1,0,0,0,0.1);
                    }
                    this.fisheye.setDeltaMovement(0,0.1,0);
                    this.fisheye.setAppear(1.0f);

                    this.storedDelay=-1;
                    this.storedPos=null;
                    dontDoThat=true;
                }
            }
            if((pos!=null)&&!dontDoThat){
                this.fisheye.setDeltaMovement(0,0,0);
                this.fisheye.setAppear(1.0f/0.9f);
                if(this.storedDelay==-1){
                    this.storedDelay=2;
                    this.storedPos=pos;
                }
                return;
            }
            RandomSource randomsource = this.fisheye.getRandom();
            boolean goodEnuff = false;
            int counter = 5;
            if(this.fisheye.getTarget()!=null){
                Vec3 postar = this.fisheye.getTarget().getPosition(1).add(0,-20,0);
                this.fisheye.getMoveControl().setWantedPosition(postar.x, postar.y, postar.z, 1.0);
            }else{
                while (!goodEnuff&&counter>0){
                    double d0 = this.fisheye.getX() + (randomsource.nextInt(-16,16));
                    double d1 = this.fisheye.getY() -20;
                    double d2 = this.fisheye.getZ() + (randomsource.nextInt(-16,16));
                    counter-=1;
                    if(!this.fisheye.level().getBlockState(new BlockPos((int)d0,(int)d1,(int)d2)).isEmpty()){
                        goodEnuff=true;
                    }
                    this.fisheye.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);

                }
            }

            this.fisheye.risingUp=1.0f;
            this.fisheye.setDeltaMovement(0,0.2,0);
        }
        @Nullable
        protected Vec3 getPosition() {
            return DefaultRandomPos.getPos(this.fisheye, 24, 3);
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
                Vec3 normMove = this.fisheye.getDeltaMovement().normalize().scale(10).add(this.fisheye.getEyePosition());
                if(this.fisheye.getTarget()!=null||(
                        this.fisheye.getDeltaMovement().length()>0.1)){
                    this.fisheye.getLookControl().setLookAt(new Vec3(normMove.x,normMove.y,normMove.z));
                    this.fisheye.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(normMove.x,normMove.y,normMove.z));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown = 2;
                    Vec3 Vec3 = new Vec3(this.wantedX - this.fisheye.getX(), -0.1+(this.wantedY-this.fisheye.getY())/12.0f, this.wantedZ - this.fisheye.getZ());
                    double horLen = Vec3.multiply(1,0,1).length();
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();
                    if(this.fisheye.getTarget()!=null&&this.fisheye.getAttackBoundingBox().inflate(0.4).intersects(this.fisheye.getTarget().getHitbox())) {
                        boolean hurt = this.fisheye.doHurtTarget(this.fisheye.getTarget());

                    }
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
