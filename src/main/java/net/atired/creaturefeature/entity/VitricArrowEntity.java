package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFItemInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.init.CFSoundInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class VitricArrowEntity extends AbstractArrow {
    public Vec3[] positions = new Vec3[12];
    public int posTracker = 0;

    public VitricArrowEntity(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(CFEntityInit.VITRIC_ARROW.get(),  x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public VitricArrowEntity(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(CFEntityInit.VITRIC_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    public VitricArrowEntity(EntityType<VitricArrowEntity> vitricArrowEntityEntityType, Level level) {
        super(vitricArrowEntityEntityType,level);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.posTracker<12){
            for(int i =0;i<12;i++){
                this.positions[i]=position();
                this.posTracker+=1;
            }
        }
        else{
            for (int i = 1; i < 12; i++) {
                this.positions[i-1]=this.positions[i];
            }
            this.positions[11]=position();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        double olLength = getDeltaMovement().multiply(1,0,1).length();
        super.onHitBlock(result);
        if(olLength>0.33&&level() instanceof ServerLevel level) {
            Vec3 dir = getDeltaMovement().scale(-0.2).add(getPosition(0));
            level.sendParticles(CFParticleInit.GLASS_SHARD_PARTICLE.get(),getX(),getY(),getZ(),10,0.1,0.1,0.1,0.2);
            float yaw = (float) -(Mth.atan2(getDeltaMovement().x,getDeltaMovement().z)/3.14f*180.0f)+180.0f;
            for (int i = 0; i < 8; i++) {
                FeatherEntity snowball = new FeatherEntity(level, dir.x, dir.y, dir.z);
                snowball.setItem(CFItemInit.DOWN_FEATHER.toStack());
                snowball.shootFromRotation(this, getXRot(), yaw, 0.0F, 2.5F, 10.0F);
                snowball.setOwner(getOwner());
                level.addFreshEntity(snowball);
            }
            playSound(CFSoundInit.SG_DIE.value(),1.0f,1.4f);
            discard();
        }
    }

    @Override
    protected double getDefaultGravity() {
        return Math.max(0.0f,0.1f-getDeltaMovement().multiply(1,0,1).length());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return CFItemInit.VITRIC_ARROW.toStack();
    }
}
