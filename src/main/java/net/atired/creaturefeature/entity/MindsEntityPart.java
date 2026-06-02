package net.atired.creaturefeature.entity;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.entity.PartEntity;

import javax.annotation.Nullable;

public class MindsEntityPart extends PartEntity<MindsEntity> {

    private final EntityDimensions size;
    public MindsEntityPart(MindsEntity parentMob, String name, float width, float height) {
        super(parentMob);
        this.size = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }
    @Override
    public boolean canBeHitByProjectile() {
        if(level().isClientSide()){
            return false;
        }
        return super.canBeHitByProjectile();
    }

    @Override
    protected void markHurt() {
        super.markHurt();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return this.getParent().getPickResult();
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean hurt(DamageSource source, float amount) {

        return this.isInvulnerableTo(source) ? false : this.getParent().hurt(this, source, amount*1.5f);
    }
    /**
     * Returns {@code true} if Entity argument is equal to this Entity
     */
    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.size;
    }

    @Override
    protected AABB makeBoundingBox() {
        if(this.size==null){
           return super.makeBoundingBox();
        }
        return this.size.makeBoundingBox(this.position());
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }
}
