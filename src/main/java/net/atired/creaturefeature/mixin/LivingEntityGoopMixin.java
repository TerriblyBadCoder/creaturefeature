package net.atired.creaturefeature.mixin;

import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.atired.creaturefeature.init.CFBlockInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.networking.payloads.DeAmpPayload;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityGoopMixin extends Entity implements LivingEntityGoopAccessor {
    @Shadow public abstract boolean isDeadOrDying();

    private float goop = 0.0f;
    private int ampAdd = 0;
    private boolean amped=false;
    private boolean amped2=false;

    public LivingEntityGoopMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "aiStep",at=@At("HEAD"))
    private void evilAssAItickOfDoomLowk(CallbackInfo ci){
        if(getBlockStateOn().getBlock()== CFBlockInit.MINEDFLAYER_JELLY.get()){
            setGoop(getGoop()+0.1f);
        }else if(getGoop()>0.0f){
            setGoop(getGoop()-0.05f);
        }
        LivingEntity entity = (LivingEntity)(Object)this;
        if(level()!=null){
            if(this.isFallDamageAmped()){
                ampAdd+=1;
                addDeltaMovement(new Vec3(0,-0.12/200.0f*ampAdd,0));
            }
            if(this.isFallDamageAmped()&&(onGround()||ampAdd>200)){
                if(entity.level() instanceof ServerLevel serverLevel){
                    setFallDamageAmped(false);
                    this.amped2=true;
                    for (int x = -2;x < 2; x++) {
                        for (int z = -2;z < 2; z++) {
                            BlockPos pos = entity.getOnPos().offset(x,0,z);
                            if(!entity.level().getBlockState(pos).isAir()){
                                BlockState state = entity.level().getBlockState(pos);
                                Vec3 center = pos.getCenter().add(0,0.5,0);
                                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,state),center.x,center.y,center.z,2,0.3,0,0.3,0.3);
                                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.DUST_PILLAR,state),center.x,center.y,center.z,2,0.3,0,0.3,0.3);
                            }
                        }
                    }

                }
            }
            if(this.isFallDamageAmped()&&tickCount%4==0&&getDeltaMovement().length()>0.2&&!isDeadOrDying()){

                Vec3 dir = getDeltaMovement().normalize();
                level().addParticle(CFParticleInit.CLEAVE_PARTICLE.get(),entity.getX(),entity.getY(0.66),entity.getZ(),dir.x,dir.y,dir.z);

            }
            if(this.amped2&&level() instanceof ServerLevel serverLevel){
                this.amped2=false;
                Vec3 oldDelta = entity.getDeltaMovement();
                PacketDistributor.sendToPlayersTrackingEntity(entity,new VelSyncPayload(entity.getId(),-oldDelta.x,0.8-oldDelta.y,-oldDelta.z),new DeAmpPayload(entity.getId(),false));
                entity.move(MoverType.SELF,new Vec3(0,1.4,0));
                entity.setDeltaMovement(new Vec3(0,0.8,0));
            }
        }

    }
    @Inject(method = "causeFallDamage",at=@At("RETURN"))
    private void landEvent(float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir){
        if(cir.isCancelled()){
            return;
        }

    }
    @Override
    public void setGoop(float goop) {
        this.goop=Math.clamp(goop,0.0f,1.0f);
    }

    @Override
    public float getGoop() {
        return goop;
    }

    @Override
    public boolean isFallDamageAmped() {
        return this.amped;
    }

    @Override
    public void setFallDamageAmped(boolean fallDamageAmped) {
        if(!fallDamageAmped){
            this.ampAdd=0;
        }
        this.amped = fallDamageAmped;
    }
}
