package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;

public class SpatBlockEntity extends Projectile {
    public BlockState state = Blocks.STONE.defaultBlockState();
    private static final EntityDataAccessor<Boolean> CRITICAL= SynchedEntityData.defineId(SpatBlockEntity.class, EntityDataSerializers.BOOLEAN);

    public SpatBlockEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

    }
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity, Block.getId(this.state));
    }

    @Override
    protected double getDefaultGravity() {
        return super.getDefaultGravity()*0.5f;
    }

    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.state = Block.stateById(packet.getData());
        this.blocksBuilding = true;
        double d0 = packet.getX();
        double d1 = packet.getY();
        double d2 = packet.getZ();
        this.setPos(d0, d1, d2);
    }
    public void setCritical(boolean critical) {
        entityData.set(CRITICAL,critical);
    }
    public boolean getCritical() {
        return entityData.get(CRITICAL);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(this.CRITICAL,false);
    }
    @Override
    public void tick() {
        if(level()!=null){
            for (int i = 0; i < 3; i++) {
                level().addParticle(CFParticleInit.SPORE_PARTICLE.get(),getX((Math.random()-0.5)*1.5),getY()+0.3,getZ((Math.random()-0.5)*1.5),0,0,0);
            }
        }
        this.move(MoverType.SELF,getDeltaMovement());
        setDeltaMovement(getDeltaMovement().scale(0.96));
        addDeltaMovement(new Vec3(0,getGravity()-0.08,0));
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if(level() instanceof ServerLevel serverLevel){
            playSound(SoundEvents.SLIME_SQUISH,1.5f,2.0f);
            playSound(SoundEvents.HONEY_DRINK,1.5f,2.0f);
            for(LivingEntity entity : serverLevel.getEntitiesOfClass(LivingEntity.class,getBoundingBox().inflate(4))){
                if(entity.distanceTo(this)<3){
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,40,2,true,false));
                }
            }
            Vec3 pos = result.getBlockPos().getCenter().add(0,0.5,0);
            serverLevel.sendParticles(CFParticleInit.SPIT_PARTICLE.get(),pos.x,pos.y+0.01,pos.z,1,0.1,0,0.1,0);
            serverLevel.sendParticles(CFParticleInit.SPORE_PARTICLE.get(),pos.x,pos.y+0.21,pos.z,16,0.3,0.2,0.3,0);
            for (int i = 0; i < 3; i++) {
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,this.state),getX(),getY()+0.3,getZ(),1,0.2,0,0.2,1);
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.DUST_PILLAR,this.state),getX(),getY()+0.3,getZ(),1,0.2,0,0.2,1);
            }

        }
        discard();
    }

    
}
