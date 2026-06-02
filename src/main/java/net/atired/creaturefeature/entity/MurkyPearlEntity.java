package net.atired.creaturefeature.entity;

import com.mojang.math.Axis;
import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFItemInit;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class MurkyPearlEntity  extends ThrowableItemProjectile {
    public Vec3[] positions = new Vec3[8];
    public int posTracker = 0;
    public MurkyPearlEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    public MurkyPearlEntity(Level level, LivingEntity shooter) {
        super(CFEntityInit.MURKY_PEARL.get(), shooter, level);
    }

    public MurkyPearlEntity(Level level, double x, double y, double z) {
        super(EntityType.SNOWBALL, x, y, z, level);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
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
    protected void onHit(HitResult result) {
        super.onHit(result);

        if(result.getType()== HitResult.Type.BLOCK&&result instanceof BlockHitResult result1){
            Direction.Axis axis = result1.getDirection().getAxis();
            if(axis.isVertical()){
                setDeltaMovement(getDeltaMovement().multiply(1,-0.95,1));

            }
            if(axis== Direction.Axis.X){
                setDeltaMovement(getDeltaMovement().multiply(-1.01,1,1));

            }
            if(axis== Direction.Axis.Z){
                setDeltaMovement(getDeltaMovement().multiply(1,1,-1.01));
            }
            if (getDeltaMovement().multiply(1,0.05,1).length()<0.02&&this.tickCount>100&&level() instanceof ServerLevel serverLevel){
                ItemEntity item = new ItemEntity(serverLevel,getX(),getY(),getZ(),getDefaultItem().getDefaultInstance());
                serverLevel.addFreshEntity(item);
                discard();
            }
            move(MoverType.SELF,getDeltaMovement().scale(0.2));
        }
    }

    @Override
    protected Item getDefaultItem() {
        return CFItemInit.MURKY_PEARL.asItem();
    }
}
