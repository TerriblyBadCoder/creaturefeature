package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.atired.creaturefeature.init.CFItemInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.networking.payloads.RabiesPayload;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class VertigoEntity extends Monster {
    private int rabiesCD = 20;
    private static final EntityDataAccessor<Float> RABIES= SynchedEntityData.defineId(VertigoEntity.class, EntityDataSerializers.FLOAT);

    public VertigoEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));

        super.registerGoals();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        Entity var5 = damageSource.getEntity();
        if (var5 instanceof Player creeper&&Math.random()>0.4) {
            this.spawnAtLocation(createHorn());
        }
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
    }

    public ItemStack createHorn() {
        RandomSource randomsource = RandomSource.create((long)this.getUUID().hashCode());
        TagKey<Instrument> tagkey = Math.random()>0.5 ? InstrumentTags.SCREAMING_GOAT_HORNS : InstrumentTags.REGULAR_GOAT_HORNS;
        HolderSet<Instrument> holderset = BuiltInRegistries.INSTRUMENT.getOrCreateTag(tagkey);
        return InstrumentItem.create(CFItemInit.VERTIGO_HORN.get(), (Holder)holderset.getRandomElement(randomsource).get());
    }

    public void setRabies(float spin) {
        entityData.set(RABIES,Math.clamp(spin,0.0f,2.1f));
    }
    public float getRabies() {
        return entityData.get(RABIES);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(RABIES,0.0f);
        super.defineSynchedData(builder);
    }

//    @Override
//    public boolean doHurtTarget(Entity entity) {
//        boolean doTheHurt =super.doHurtTarget(entity);
//        if(doTheHurt){
//            entity.addDeltaMovement(new Vec3(0,0.2,0));
//        }
//        return doTheHurt;
//    }

    @Override
    public void tick() {
        if(level() instanceof ServerLevel serverLevel){
            if(this.rabiesCD<30) {
                if (Math.random() > 0.9) {
                    serverLevel.sendParticles(CFParticleInit.RABIES_PARTICLE.get(),
                            getX() + getViewVector(1).x * 0.7, getEyeY(), getZ() + getViewVector(1).z * 0.7,
                            1, 0, 0, 0, 0);
                }
            }
            if(getTarget()!=null&&getTarget().position().distanceTo(position())<7&&this.getSensing().hasLineOfSight(getTarget())){
                this.rabiesCD-=1;
                LivingEntity i = getTarget();
                if(this.rabiesCD<0){

                    if(getRabies()>0.0f||getTarget().position().distanceTo(position())>3){
                        if(getRabies()==0.0f){
                            this.navigation.stop();
                            this.moveControl.strafe(0.3f,Math.random()>0.5?-1.5f:1.5f);
                        }
                        if(Math.random()>0.4)
                            playSound(SoundEvents.PANDA_PRE_SNEEZE,0.4f,0.3f);
                        setRabies(getRabies()+0.04f);

                        if(getRabies()>=1.0f){
                            Vec3 dir = this.calculateViewVector(this.getXRot(), this.getYHeadRot());
                            Vec3 targetOffset = getTarget().getPosition(1).subtract(getPosition(1)).normalize();
                            if(targetOffset.dot(dir)>0.75&&getTarget().position().distanceTo(position())<7){
                                i.hurt(damageSources().mobAttack(this),4.0f);
                                i.addDeltaMovement(new Vec3(dir.x*2.0,dir.y,dir.z*2.0));
                                if(i instanceof ServerPlayer player){
                                    VelSyncPayload payload = new VelSyncPayload(player.getId(),dir.x*2.0,dir.y,dir.z*2.0);
                                    PacketDistributor.sendToPlayer(player,payload);
                                    RabiesPayload payload2 = new RabiesPayload(player.getId());
                                    PacketDistributor.sendToPlayer(player,payload2);
                                    if(player instanceof PlayerBrainrotAccessor accessor){
                                        accessor.setRabies(1.0f);
                                    }
                                }
                            }
                            playSound(SoundEvents.PANDA_SNEEZE,0.5f,0.1f);
                            playSound(SoundEvents.SHIELD_BLOCK,1.1f,0.4f);
                            playSound(SoundEvents.GOAT_SCREAMING_HURT,1.1f,0.8f);
                            playSound(SoundEvents.GOAT_SCREAMING_DEATH,0.7f,0.4f);
                            setRabies(2.1f);
                            this.navigation.stop();
                            this.moveControl.strafe(0.0f,0.0f);
                            addDeltaMovement(dir.multiply(-1.0,0.2,-1.0).normalize().add(0,0.4,0));
                            this.rabiesCD=100;
                        }
                    }

                }
                else{
                    setRabies(getRabies()-0.3f);
                }

            }
            else{
                if(getRabies()>0.0f){
                    this.navigation.stop();
                    this.moveControl.strafe(0.0f,0.0f);
                    setRabies(getRabies()-0.3f);

                }
            }
        }
        else if(getRabies()>0.0f){
            level().addParticle(CFParticleInit.RABIES_PARTICLE.get(),
                    getX()+getViewVector(1).x*0.7,getEyeY(),getZ()+getViewVector(1).z*0.7
                    ,0,0,0);
        }
        float oldYaw = getYHeadRot();
        float oldPitch = getXRot();
        super.tick();
        if(getRabies()>0.5f){
            setYHeadRot(oldYaw);
            yHeadRotO=oldYaw;
            setXRot(oldPitch);
        }
    }

    @Override
    public float getSpeed() {
        if(this.rabiesCD>20&&getRabies()<=0.0f){
            return super.getSpeed()*2.0f;
        }
        return super.getSpeed()*(1.0f-(float)Math.pow(getRabies(),0.5f));
    }

    public static AttributeSupplier.Builder createVertigoAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 25.0).add(Attributes.KNOCKBACK_RESISTANCE,0.1).add(Attributes.MOVEMENT_SPEED, 0.23);
    }
}
