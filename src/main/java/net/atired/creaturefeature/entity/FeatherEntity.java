package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFItemInit;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FeatherEntity extends ThrowableItemProjectile {
    public Vec3[] positions = new Vec3[8];
    public float lastYaw;
    public float lastPitch;
    public int posTracker = 0;
    public boolean hitBlock = false;
    public FeatherEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    public FeatherEntity(Level level, LivingEntity shooter) {
        super(CFEntityInit.FEATHER.get(), shooter, level);
    }

    public FeatherEntity(Level level, double x, double y, double z) {
        super(CFEntityInit.FEATHER.get(), x, y, z, level);
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
        if(this.tickCount>200){
            discard();
        }
        if(!isInWall()&&!onGround()&&!this.hitBlock){
            Vec3 dir = getDeltaMovement().normalize();
            float yaw = (float) Mth.atan2((float)dir.x,(float)dir.z);
            float pitch = -(float)Math.asin(dir.y);
            lastYaw=yaw;
            lastPitch=pitch;
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
        if(onGround()||this.hitBlock)return;
        super.applyGravity();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(result.getEntity() instanceof LivingEntity living){
            living.hurt(damageSources().mobProjectile(this,(getOwner() instanceof LivingEntity ? (LivingEntity)getOwner() : null)),4);
        }
        discard();
        super.onHitEntity(result);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if(result.getType()== HitResult.Type.BLOCK&&result instanceof BlockHitResult result1&&!this.hitBlock){
            move(MoverType.SELF,getDeltaMovement().scale(0.3));
            this.hitBlock=true;
            setDeltaMovement(getDeltaMovement().scale(0.00001));
        }
    }

    @Override
    public void move(MoverType type, Vec3 pos) {

        super.move(type, pos);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.FEATHER.asItem();
    }
}
