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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownEgg;
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

public class KickedBlockEntity extends Projectile {
    public BlockState state = Blocks.STONE.defaultBlockState();
    private static final EntityDataAccessor<Boolean> CRITICAL= SynchedEntityData.defineId(KickedBlockEntity.class, EntityDataSerializers.BOOLEAN);
    
    public KickedBlockEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if(level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(CFParticleInit.CRIT_PARTICLE.get(),getX(),getY()+0.3,getZ(),1,0.1,0,0.1,0);
            serverLevel.sendParticles(CFParticleInit.CRIT_VER_PARTICLE.get(),getX(),getY(0.5),getZ(),10,0.2,0.2,0.2,0.1);

            Vec3 dir = getDeltaMovement().multiply(1,0,1).normalize().scale(0.6).add(0,0.5,0);
            result.getEntity().addDeltaMovement(dir);

            result.getEntity().hurt(damageSources().mobProjectile(this,(getOwner() instanceof LivingEntity living)?living:null),5.0f);
            if(result.getEntity() instanceof ServerPlayer player){
                VelSyncPayload payload = new VelSyncPayload(player.getId(),dir.x,dir.y,dir.z);
                PacketDistributor.sendToPlayer(player,payload);
            }
            discard();
        }
    }
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        return new ClientboundAddEntityPacket(this, entity, Block.getId(this.state));
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
            serverLevel.sendParticles(CFParticleInit.CRIT_PARTICLE.get(),getX(),getY()+0.3,getZ(),1,0.1,0,0.1,0);
            for (int i = 0; i < 32; i++) {
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,this.state),getX(),getY()+0.3,getZ(),1,0.2,0,0.2,1);
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.DUST_PILLAR,this.state),getX(),getY()+0.3,getZ(),1,0.2,0,0.2,1);
            }

        }
        discard();
    }

    
}
