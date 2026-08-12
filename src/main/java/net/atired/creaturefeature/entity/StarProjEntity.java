package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StarProjEntity extends ThrowableItemProjectile {
    public Vec3[] positions = new Vec3[8];
    public float lastYaw;
    public float lastPitch;
    public int posTracker = 0;
    private static final EntityDataAccessor<Boolean> AGED= SynchedEntityData.defineId(StarProjEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean hitStuff = false;
    public LivingEntity toHurt = null;
    public StarProjEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    public StarProjEntity(Level level, LivingEntity shooter) {
        super(CFEntityInit.STAR.get(), shooter, level);
    }

    public StarProjEntity(Level level, double x, double y, double z) {
        super(CFEntityInit.STAR.get(), x, y, z, level);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public boolean save(CompoundTag compound) {
        return super.save(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
    }

    @Override
    public void tick() {
        super.tick();
        if(level() instanceof ServerLevel serverLevel && toHurt==null){
            LivingEntity otherLiving=null;
            for(LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(7))){
                if(living!=getOwner()){
                    if(otherLiving==null){
                        otherLiving=living;
                    }else if(distanceTo(otherLiving)>distanceTo(living)){
                        otherLiving=living;
                    }
                }
            }
            if(otherLiving!=null){
                toHurt=otherLiving;
            }
        }
        if(tickCount<30&&toHurt!=null){
            this.setDeltaMovement(getDeltaMovement().lerp(this.toHurt.getPosition(1).add(0,1.6,0).subtract(this.getPosition(1)).normalize().scale(1.9),0.065f));
        }
        if(this.isAged())this.tickCount=Math.max(47,this.tickCount);
        if(this.tickCount>53){
            if(level() instanceof ServerLevel serverLevel){
                this.setDeltaMovement(getDeltaMovement().scale(0.6));

            }
            if(this.tickCount>60){
                discard();
            }
        }
        if(this.posTracker<8){
            for(int i =0;i<8;i++){
                this.positions[i]=position();
                this.posTracker+=1;
            }
        }
        else{
            for (int i = 1; i < 8; i++) {
                this.positions[i-1]=this.positions[i];
            }
            this.positions[7]=position();
        }
    }

    @Override
    protected void applyGravity() {
        return;
    }
    public void setHitStuff(){
        this.hitStuff=true;
        entityData.set(AGED,true);
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(this.hitStuff)return;
        if(result.getEntity() instanceof LivingEntity living&&(living!=getOwner()||this.tickCount>20)&&level()instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(CFParticleInit.STAR_PARTICLE.get(),getX(),getY(0.5),getZ(),1,0.02,0.02,0.02,0);

            living.setRemainingFireTicks(Math.max(0,living.getRemainingFireTicks())+15);
            living.hurt(damageSources().mobProjectile(this,(getOwner() instanceof LivingEntity ? (LivingEntity)getOwner() : null)),4);
        }

        if(level() instanceof ServerLevel serverLevel){
            setHitStuff();
        }
        super.onHitEntity(result);
    }
    public boolean isAged(){
        return this.entityData.get(AGED);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AGED,false);
        
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if(this.hitStuff)return;
        if(result.getType()!= HitResult.Type.ENTITY||((EntityHitResult)result).getEntity()!=getOwner()){
            if(level() instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(CFParticleInit.STAR_PARTICLE.get(),getX(),getY(0.5),getZ(),1,0.02,0.02,0.02,0);
            }
            if(level() instanceof ServerLevel serverLevel){
                setHitStuff();
            }
        }else{
            if(result instanceof EntityHitResult hitResult && hitResult.getEntity()==getOwner()&&this.tickCount>20){
                if(level() instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(CFParticleInit.STAR_PARTICLE.get(),getX(),getY(0.5),getZ(),1,0.02,0.02,0.02,0);
                }
                if(level() instanceof ServerLevel serverLevel){
                    setHitStuff();
                }
            }
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        super.move(type, pos);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.MAGMA_CREAM.asItem();
    }
}
