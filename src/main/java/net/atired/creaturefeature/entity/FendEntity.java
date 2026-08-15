package net.atired.creaturefeature.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FendEntity extends Monster implements RangedAttackMob {
    public int chargeCd = 80;
    private static final EntityDataAccessor<Float> LUNGE= SynchedEntityData.defineId(FendEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DEEP= SynchedEntityData.defineId(FendEntity.class, EntityDataSerializers.BOOLEAN);

    public FendEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2,new RangedBowAttackGoal(this, 0.4, 15, 15.0F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{FendEntity.class})));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
    }
    public boolean isDeep(){
        return entityData.get(DEEP);
    }
    public static boolean checkFendSpawnRules(EntityType<FendEntity> fend, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkMonsterSpawnRules(fend, level, spawnType, pos, random) && (MobSpawnType.isSpawner(spawnType) || level.canSeeSky(pos.above()));
    }
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (this.random.nextFloat()>0.7 && this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        }
        if(getY()<20){
            this.entityData.set(DEEP,true);
        }
        if (this.random.nextFloat()>0.7 && this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        }
        if(this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
        }

        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
        }
        if (this.random.nextFloat()>0.6 && this.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        }
        if (this.random.nextFloat()>0.3 && this.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    public static AttributeSupplier.Builder createFendAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 30.0).add(Attributes.ATTACK_DAMAGE,4.0).add(Attributes.MOVEMENT_SPEED, 0.24).add(Attributes.MAX_HEALTH,20.0);
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DEEP,false);
        builder.define(LUNGE,0.0f);
    }
    public void setLunge(float lunge){
        entityData.set(LUNGE,lunge);
    }

    public float getLunge() {
        return entityData.get(LUNGE);
    }

    @Override
    public void jumpFromGround() {
        if(this.noPhysics){
            return;
        }
        super.jumpFromGround();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return super.doHurtTarget(entity);
    }

    @Override
    public void tick() {
        if(getLunge()>0.0f&&!level().isClientSide())
            setLunge(getLunge()-0.05f);
        boolean shouldHover = getItemInHand(InteractionHand.MAIN_HAND).getItem()instanceof ProjectileWeaponItem;
        boolean shouldMeleeSword = getItemInHand(InteractionHand.MAIN_HAND).getItem()instanceof SwordItem;
        if(shouldHover){
            BlockHitResult result1 = level().clip(new ClipContext(
                    getPosition(1),
                    getPosition(1).add(new Vec3(0.0,-2.1,0)),
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    this));
            if(result1.getType()!= HitResult.Type.MISS&&getTarget()!=null&&distanceTo(getTarget())>7&&distanceTo(getTarget())<30){
                addDeltaMovement(new Vec3(0.0,getGravity()+0.01,0.0));
            }
        }
        else if(shouldMeleeSword){

            if(getTarget()!=null){
                if(distanceTo(getTarget())>4&&distanceTo(getTarget())<10){
                    this.chargeCd-=1;
                    if(this.chargeCd<10){
                        this.navigation.stop();
                    }
                    if(this.chargeCd<0){
                        addDeltaMovement(getViewVector(1).multiply(1,0,1).normalize().scale(Math.pow(distanceTo(getTarget()),0.5)/2.0f+0.4).add(0,0.3,0));
                        this.chargeCd=50;
                        setLunge(1.0f);
                    }
                }
                else if(this.chargeCd>35){
                    this.chargeCd-=1;
                }

                if(this.chargeCd<50&&this.chargeCd>40&&this.chargeCd%2==0){
                    if(isWithinMeleeAttackRange(getTarget())){
                        doHurtTarget(getTarget());
                        this.chargeCd=50;
                        setDeltaMovement(0,0.1,0);
                    }
                    this.navigation.recomputePath();
                }

            }

        }
        else{
            boolean collided = this.level().noBlockCollision(this, getBoundingBox());
            if(getTarget()!=null&&(distanceTo(getTarget())>5||(this.noPhysics&&!collided))){
                this.noPhysics=true;
                setDeltaMovement(getTarget().getPosition(1).subtract(getPosition(1)).normalize().scale(0.5).add(0,0.1,0).yRot(Mth.sin(getId()+this.tickCount/16.0f)/8.0f));
                this.navigation.stop();
            }else if(collided&&this.noPhysics&&(getTarget()==null||distanceTo(getTarget())<3)){
                this.noPhysics=false;
                this.setDeltaMovement(new Vec3(0,0,0));
                this.navigation.recomputePath();
            }
            if(getTarget()==null&&this.noPhysics){
                addDeltaMovement(new Vec3(0,getGravity()+0.1,0));
            }
        }

        super.tick();
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        ItemStack weapon = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> {
            return item instanceof BowItem;
        }));
        ItemStack itemstack1 = this.getProjectile(weapon);
        AbstractArrow abstractarrow = this.getArrow(itemstack1, v, weapon);
        Item var7 = weapon.getItem();
        if (var7 instanceof ProjectileWeaponItem weaponItem) {
            abstractarrow = weaponItem.customArrow(abstractarrow, itemstack1, weapon);
        }

        double d0 = livingEntity.getX() - this.getX();
        double d1 =  livingEntity.getY(0.3333333333333333) - abstractarrow.getY();
        double d2 =  livingEntity.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * 0.20000000298023224, d2, 1.8F, (float)(15 - this.level().getDifficulty().getId() * 5));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        addDeltaMovement(getLookAngle().scale(-0.4));
        this.level().addFreshEntity(abstractarrow);
    }
    protected AbstractArrow getArrow(ItemStack arrow, float velocity, @javax.annotation.Nullable ItemStack weapon) {
        return ProjectileUtil.getMobArrow(this, arrow, velocity, weapon);
    }

    @Override
    public void load(CompoundTag compound) {
        this.entityData.set(DEEP,compound.getBoolean("is_cf_deep"));
        super.load(compound);
    }

    @Override
    public boolean save(CompoundTag compound) {
        compound.putBoolean("is_cf_deep",isDeep());
        return super.save(compound);
    }

    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeapon) {
        return projectileWeapon == Items.BOW;
    }

}
