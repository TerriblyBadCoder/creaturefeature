package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CanaryPart extends Monster {
    private static final EntityDataAccessor<Float> PART= SynchedEntityData.defineId(CanaryPart.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> PARENT_ID= SynchedEntityData.defineId(CanaryPart.class, EntityDataSerializers.INT);

    public CanaryPart(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createCanaryAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.ATTACK_DAMAGE,6.0).add(Attributes.MOVEMENT_SPEED, 0.31).add(Attributes.MAX_HEALTH,30.0);
    }

    @Override
    public boolean shouldBeSaved() {
        return true;
    }

    @Override
    public void tick() {
        if((getParent()==null)||!getParent().isAlive()){
            this.discard();
            return;
        }

        for(Player player : level().getEntitiesOfClass(Player.class,getBoundingBox().inflate(0.3))){
            if(level() instanceof ServerLevel)
            {
                player.hurt(this.getParent().damageSources().mobAttack(this.getParent()),1.5f);
                if(player instanceof PlayerBrainrotAccessor accessor&&!player.isCreative()){
                    accessor.setDelayedDamage(accessor.getDelayedDamage()+0.02f);
                }
            }

            Vec3 other = this.getParent().getPosition(1).scale(-1).add(player.getPosition(1)).multiply(1,0,1).normalize();
            player.knockback(0.02f,other.x,other.z);
        }
        super.tick();

    }

    public void setPart(float par) {
        entityData.set(PART,par);
    }
    public void setParent(int par) {
        entityData.set(PARENT_ID,par);
    }
    public float getPart() {
        return entityData.get(PART);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(PART,0.0f);
        builder.define(PARENT_ID,-1);
        super.defineSynchedData(builder);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(getParent()==null){
            return super.hurt(source,amount);
        }
        super.hurt(source,0.01f);
        return getParent().hurt(source,amount);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0f;
    }

    public CanaryEntity getParent(){
        if(level().getEntity(entityData.get(PARENT_ID)) instanceof CanaryEntity){
            return (CanaryEntity)level().getEntity(entityData.get(PARENT_ID));
        }
        return null;
    }
}
