package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFMobEffectInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class PrimedEepEntity extends PrimedTnt {
    public PrimedEepEntity(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(CFEntityInit.EEP.get(), level);
    }
    public PrimedEepEntity(Level level, double x, double y, double z) {
        this(CFEntityInit.EEP.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * 6.2831854820251465;
        this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.20000000298023224+Math.random()/5.0f, -Math.cos(d0) * 0.02);
        this.setYRot((float)(Math.random()-0.5f)*20.0f);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public void tick() {
        super.tick();
        this.yRotO=this.getYRot();
        this.setYRot(this.getYRot()-(float)Math.abs(this.getDeltaMovement().y)*15.0f);
    }

    @Override
    protected void explode() {
        if(level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(CFParticleInit.SLEEPY_PARTICLE.get(), this.getX(), this.getY(), this.getZ(),40,2,1.8,2,0.1);
            serverLevel.sendParticles(CFParticleInit.SLEEPY_PARTICLE.get(), this.getX(), this.getY(), this.getZ(),5,0.4,0.4,0.4,0.1);
            serverLevel.sendParticles(CFParticleInit.SLEEPY_EXPLODE_PARTICLE.get(), this.getX(), this.getY(), this.getZ(),70,2.3,1.8,2.3,0.1);
            for(LivingEntity i : serverLevel.getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(5))){
                if(this.distanceTo(i)<5){
                    Vec3 dir = i.getDeltaMovement().add(i.getPosition(1).subtract(getPosition(1)).multiply(1,0.2,1).normalize().multiply(2,2,2).add(0,0.6,0));
                    i.setDeltaMovement(dir);
                    i.hurt(damageSources().explosion(this,this),6.0f);
                    i.addEffect(new MobEffectInstance(CFMobEffectInit.SLEEPY,120,0));
                    if(i instanceof ServerPlayer player){
                        VelSyncPayload payload = new VelSyncPayload(player.getId(),dir.x,dir.y,dir.z);
                        PacketDistributor.sendToPlayer(player,payload);
                    }
                }
            }
        }
    }
}
