package net.atired.creaturefeature.entity;

import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DetritusEntity extends Monster {
    public int howMuch = 3;
    private static final EntityDataAccessor<Float> MONOCHROMATIC= SynchedEntityData.defineId(DetritusEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> UNBURYING= SynchedEntityData.defineId(DetritusEntity.class, EntityDataSerializers.FLOAT);
    public DetritusEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        if(getUnburying()>0.01){
            navigation.stop();
            setDeltaMovement(getDeltaMovement().scale(0.7));

            if(!level().isClientSide()&&level()instanceof ServerLevel serverLevel){
                setUnburying(getUnburying()*0.8f);
                BlockPos pos = this.getOnPos();
                if(!this.level().getBlockState(pos).isAir()){
                    BlockState state = this.level().getBlockState(pos);
                    Vec3 center = getPosition(1);
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,state),center.x,center.y,center.z,2,0.2,0,0.2,0.3);
                    if(Math.random()>0.5)
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.DUST_PILLAR,state),center.x,center.y,center.z,1,0.2,0,0.2,0.3);
                }
            }
        }
        super.tick();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(UNBURYING,1.0f);
        builder.define(MONOCHROMATIC,0.0f);

        super.defineSynchedData(builder);
    }

    @Override
    public void load(CompoundTag compound) {
        setCopy(compound.getFloat("copiedMonochrome"));
        super.load(compound);
    }

    @Override
    public boolean save(CompoundTag compound) {
        compound.putFloat("copiedMonochrome",getCopy());
        return super.save(compound);
    }

    public void aiStep() {
        if(getTarget()!=null&&this.tickCount%(int)(80*(1.0f+getCopy()*4.0f))==79&&getCopy()<0.5&&level()instanceof ServerLevel serverLevel){
            if(this.howMuch>0){
                this.howMuch-=1;
                birth(serverLevel);
            }
        }
        if (this.isAlive()) {
            boolean flag = this.isSunBurnTick();
            if (flag) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        Item item = itemstack.getItem();
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.onEquippedItemBroken(item, EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.igniteForSeconds(3.0F);
                }
            }
        }

        super.aiStep();
    }

    @Override
    protected int getBaseExperienceReward() {
        return super.getBaseExperienceReward()/4;
    }

    public void birth(ServerLevel serverLevel){
        DetritusEntity detritusEntity = new DetritusEntity(CFEntityInit.DETRITUS.get(),serverLevel);
        int i = Mth.floor(this.getX());
        int j = Mth.floor(this.getY());
        int k = Mth.floor(this.getZ());
        if(getTarget()!=null){
            Vec3 horTo=getTarget().getPosition(1).subtract(getPosition(1)).multiply(1,0,1).normalize().scale(4).add(getTarget().getPosition(1));
             i = Mth.floor(horTo.x());
             j = Mth.floor(horTo.y());
             k = Mth.floor(horTo.z());
        }
        for(int l = 0; l <= 50; ++l) {
            int i1 = i + Mth.nextInt(this.random, 4, 8) * Mth.nextInt(this.random, -1, 1);
            int j1 = j + Mth.nextInt(this.random, -4, 4) * Mth.nextInt(this.random, -1, 1);
            int k1 = k + Mth.nextInt(this.random, 4, 8) * Mth.nextInt(this.random, -1, 1);
            BlockPos blockpos = new BlockPos(i1, j1, k1);
            EntityType<?> entitytype = detritusEntity.getType();
            if (SpawnPlacements.isSpawnPositionOk(entitytype, this.level(), blockpos)) {
                detritusEntity.setPos((double)i1, (double)j1, (double)k1);
                break;
            }
            if(l==50){
                detritusEntity.setPos(getPosition(1));
            }
        }
        detritusEntity.setTarget(getTarget());
        detritusEntity.finalizeSpawn(serverLevel, this.level().getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null);
        detritusEntity.setCopy((float) Math.min(getCopy()+0.33f+Math.random()/16.0f,1.0f));
        detritusEntity.howMuch=1;
        detritusEntity.setHealth(Math.max(0.5f,detritusEntity.getMaxHealth()*(1.0f-detritusEntity.getCopy()*1.25f)));
        detritusEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(detritusEntity.getHealth());
        detritusEntity.setUnburying(1.0f);
        serverLevel.addFreshEntity(detritusEntity);
        detritusEntity.lookAt(getTarget(),360.0f,360.0f);
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        float oldHP=this.getHealth();
        boolean hurting = super.hurt(source, amount);
        if(hurting&&(getTarget()!=null||source.getDirectEntity() instanceof Player)&&oldHP/this.getMaxHealth()>getCopy()+0.24&&level() instanceof ServerLevel serverLevel){
            while (oldHP>this.getHealth()&&oldHP/this.getMaxHealth()>getCopy()+0.24&&oldHP>0){
                oldHP-=1.8f;
                birth(serverLevel);
            }
        }
        return hurting;
    }
    public static boolean checkDetritusSpawnRules(EntityType<? extends Monster> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random){
        return  checkMonsterSpawnRules(type, level, spawnType, pos, random)&&pos.getY()<6;
    }
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        setCopy((float)Math.random()/8.0f);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
    public void setUnburying(float mirrorBounce) {
        entityData.set(UNBURYING,mirrorBounce);
    }
    public float getUnburying() {
        return entityData.get(UNBURYING);
    }
    public void setCopy(float mirrorBounce) {
        entityData.set(MONOCHROMATIC,mirrorBounce);
    }
    public float getCopy() {
        return entityData.get(MONOCHROMATIC);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.HUSK_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0])));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));

        super.registerGoals();
    }
    public static AttributeSupplier.Builder createDetritusAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ATTACK_DAMAGE,2.0f).add(Attributes.MOVEMENT_SPEED, 0.23).add(Attributes.MAX_HEALTH,12.0);
    }
}
