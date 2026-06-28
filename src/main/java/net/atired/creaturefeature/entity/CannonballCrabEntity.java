package net.atired.creaturefeature.entity;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.EnumSet;
import java.util.Objects;

public class CannonballCrabEntity extends Monster {
    public Vec3[] legPositions = new Vec3[8];
    public Vec3[] oldLegPositions = new Vec3[8];
    public float[] legSin = new float[8];
    public float[] legOffset = new float[8];
    public Vec3 lastPos = new Vec3(0.0,0,0);
    public float floatAbove = 0.0f;
    public CannonballCrabEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl=new CannonBallMoveControl(this);
    }

    @Override
    public void tick() {
        if(legPositions[0]==null){
            for (int i = 0; i < 8; i++) {
                oldLegPositions[i]=new Vec3(getX(),getY(),getZ());
                legPositions[i]=new Vec3(getX(),getY(),getZ());
            }
        }
        if(this.level()!=null&&this.tickCount%3==0){
            this.lastPos=getPosition(1);
            this.noActionTime=0;
            int i = (this.tickCount/3)%8;
                if(legPositions[i]==null)
                    oldLegPositions[i]=new Vec3(getX(),getY(),getZ());

                legPositions[i]=new Vec3(getX(),getY(),getZ());
                boolean bigMisser = false;
                for (int j = 7; j > 0; j--) {

                    Vec3 dir = new Vec3(0.5,-0.1-j/9.0f,0).normalize().yRot(3.14f*2.0f*i/8.0f+(float)(this.lastPos.length()/8.0f)%(3.14f*4.0f));
                    Vec3 posFrom = getPosition(1).add(0,1.0,0);

                    BlockHitResult result = level().clip(new ClipContext(
                            posFrom,
                            posFrom.add(dir.scale(12)),
                            ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.NONE,
                            this));
                    if(legPositions[i].equals(new Vec3(getX(),getY(),getZ()))||
                            ((legPositions[i].subtract(getPosition(1)).length()>result.getLocation().subtract(getPosition(1)).length()|| Objects.equals(legPositions[i], new Vec3(getX(), getY(), getZ())))&&result.getType()!= HitResult.Type.MISS)) {

                            legPositions[i]=result.getLocation();
                            if(legPositions[i].distanceTo(oldLegPositions[i])>0.8||this.tickCount%240<25){
                                legSin[i]=1.0f;
                                legOffset[i]=(float)legPositions[i].distanceTo(oldLegPositions[i])/3.0f+0.3f;
                            }
                    }
                }

        }
        for (int i = 0; i < 8; i++) {
            if(legSin[i]>0){
                legSin[i]=Math.max(0.0f,legSin[i]-0.075f);
            }
        }
        if(legPositions[0]!=null){
            boolean sound = false;
            for (int i = 0; i < 8; i++) {
                if(oldLegPositions[i].distanceTo(legPositions[i])>0.02){
                    if(oldLegPositions[i].distanceTo(legPositions[i])>0.1)
                        sound=true;
                    oldLegPositions[i]=oldLegPositions[i].lerp(legPositions[i],0.2);
                }
            }
            if(tickCount%8==0&&sound){
                playSound(SoundEvents.ZOMBIE_STEP,0.8f,0.7f);
            }
        }

        if(level()!=null){
            BlockHitResult result = null;
            for (int i = 0; i < 4; i++) {
                BlockHitResult result1 = level().clip(new ClipContext(
                        getPosition(1),
                        getPosition(1).add(new Vec3(0.6,-4.1+Mth.sin(this.tickCount/10.0f)/1.0f,0).yRot(i*3.14f/2.0f)),
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        this));
                if(result==null||result1.getLocation().distanceTo(getPosition(1))<result.getLocation().distanceTo(getPosition(1))){
                    result=result1;
                }
            }
            if(result.getType()!= HitResult.Type.MISS||isInWater()){

                float to = (float) (1.0f-Math.clamp(result.getLocation().distanceTo(getPosition(1))-2.8f,0.0f,1.0f));
                if(getTarget()!=null&&getTarget().getY()>getY()){
                    to+= (float) (1.8f+Math.abs(getTarget().getY()-getY())/20.0f);
                    if(isInWater()){
                        to+=0.5f;
                    }
                }
                else if(getTarget()!=null&&getTarget().getY()<getY()&&isInWater()){
                    to-=3.0f;
                }
                else if(isInWater()){
                    to-=1.3f;
                }
                if(getTarget()!=null&& getPosition(1).multiply(1,0,1).distanceTo(getTarget().getPosition(1).multiply(1,0,1))<1){
                    to=-1.4f;
                }

                setDeltaMovement(new Vec3(getDeltaMovement().x,0.06*to+0.08+getDeltaMovement().y*0.9,getDeltaMovement().z));
            }
        }

        super.tick();
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    protected float getKnockback(Entity attacker, DamageSource damageSource) {
        return super.getKnockback(attacker, damageSource);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        LivingKnockBackEvent event = CommonHooks.onLivingKnockBack(this, (float)strength, x, z);
        if (!event.isCanceled()) {
            strength = (double)event.getStrength()*4.0;
            x = event.getRatioX();
            z = event.getRatioZ();
            strength *= 1.0 - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            if (!(strength <= 0.0)) {
                this.hasImpulse = true;

                Vec3 vec3;
                for(vec3 = this.getDeltaMovement(); x * x + z * z < 9.999999747378752E-6; z = (Math.random() - Math.random()) * 0.01) {
                    x = (Math.random() - Math.random()) * 0.01;
                }

                Vec3 vec31 = (new Vec3(x, 0.0, z)).normalize().scale(strength);
                this.setDeltaMovement(vec3.x / 2.0 - vec31.x, Math.min(0.4, vec3.y / 2.0 + strength), vec3.z / 2.0 - vec31.z);
            }

        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }
    public static AttributeSupplier.Builder createCrabAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.12).add(Attributes.ATTACK_DAMAGE,4.0).add(Attributes.ATTACK_KNOCKBACK,1.4).add(Attributes.MAX_HEALTH,30.0);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new RandomFloatAroundGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));

        super.registerGoals();
    }
    static class RandomFloatAroundGoal extends Goal {
        private final CannonballCrabEntity crab;

        public RandomFloatAroundGoal(CannonballCrabEntity ghast) {
            this.crab = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.crab.getMoveControl();
            if (!movecontrol.hasWanted()||(this.crab.getTarget()!=null)) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.crab.getX();
                double d1 = movecontrol.getWantedY() - this.crab.getY();
                double d2 = movecontrol.getWantedZ() - this.crab.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.crab.getRandom();
            if(this.crab.getTarget()!=null){
                this.crab.moveControl.setWantedPosition(this.crab.getTarget().getX(),this.crab.getTarget().getY()+1.6,this.crab.getTarget().getZ(),1.5f);
            }else{

                double d0 = this.crab.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double d1 = this.crab.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.1F) * 12.0F);
                double d2 = this.crab.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
                this.crab.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            }
        }
    }
    static class CannonBallMoveControl extends MoveControl {
        private final CannonballCrabEntity crab;
        private int collisionCheckCooldown;

        public CannonBallMoveControl(CannonballCrabEntity mosqo) {
            super(mosqo);
            this.crab = mosqo;
        }

        public void tick() {
            MoveControl moveControl = this.crab.getMoveControl();

            if (this.operation==Operation.MOVE_TO) {
                if(this.crab.getTarget()!=null||(
                        this.crab.getDeltaMovement().length()>0.5&&this.crab.getPosition(1).distanceTo(new Vec3(moveControl.getWantedX(),this.crab.getY(),moveControl.getWantedZ()))>1.2)){
                    this.crab.getLookControl().setLookAt(new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                    this.crab.lookAt(EntityAnchorArgument.Anchor.EYES,new Vec3(moveControl.getWantedX(),moveControl.getWantedY(),moveControl.getWantedZ()));
                }
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.crab.getRandom().nextInt(5) + 2;
                    this.wantedY=this.crab.getY();

                    Vec3 Vec3 = new Vec3(this.wantedX - this.crab.getX(), 0, this.wantedZ - this.crab.getZ());
                    double d = Vec3.length();
                    Vec3 = Vec3.normalize();
                    if (this.willCollide(Vec3, Mth.ceil(d))) {
                        this.crab.setDeltaMovement(this.crab.getDeltaMovement().multiply(0.3,0.9,0.3)
                                .add(Vec3.multiply(1.0,0.0,1.0).scale(0.2)));
                    } else {
                        this.crab.setTarget(null);
                    }
                }

            }
        }

        private boolean willCollide(Vec3 direction, int steps) {
            AABB box = this.crab.getBoundingBox();
            if(this.crab.getTarget()!=null&&this.crab.isWithinMeleeAttackRange(this.crab.getTarget())){
                if(this.crab.doHurtTarget(this.crab.getTarget())){
                    this.crab.swing(InteractionHand.MAIN_HAND);
                }
            }
            if(this.crab.getTarget()==null&&new Vec3(this.getWantedX(),this.getWantedY(),this.getWantedZ()).subtract(this.crab.getPosition(1)).length()<1.5){
                return false;
            }
            return true;
        }
    }
}
