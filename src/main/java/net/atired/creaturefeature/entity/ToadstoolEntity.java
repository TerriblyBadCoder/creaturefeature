package net.atired.creaturefeature.entity;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.SableCommonEvents;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.physics.PhysicsPipeline;
import dev.ryanhcode.sable.api.physics.collider.SableCollisionContext;
import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.network.packets.tcp.ServerboundPunchSubLevelPacket;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.entity_collision.SubLevelEntityCollision;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import dev.ryanhcode.sable.util.LevelAccelerator;
import net.atired.creaturefeature.init.CFBlockInit;
import net.atired.creaturefeature.networking.payloads.ToadstoolRenderPayload;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.List;

public class ToadstoolEntity extends Monster {
    public ServerSubLevel myCube = null;
    public ClientSubLevel myClientCube = null;
    public boolean hasMadeCube=false;
    public int freshTickCount = 0;
    public float lerpedonFours=1f;
    public int toadstoolDelay=-1;
    public int throwCD = 0;
    public int tempXChunk=-19971234;
    public int tempZChunk=-19971234;
    public ToadstoolEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
        this.goalSelector.addGoal(1, new AvoidEntityGoal(this, Player.class, 12.0F, 1.5, 2.1,(p)->{return this.toadstoolDelay>-1;}));

        super.registerGoals();
    }

    public void push(double x, double y, double z) {
        this.setDeltaMovement(this.getDeltaMovement().add(new Vec3(x, y, z).scale(0.2)));
        this.hasImpulse = true;
    }

    @Override
    public void die(DamageSource damageSource) {
        if(myCube!=null){
            ServerLevel other = myCube.getLevel();
            BlockPos pos = myCube.getPlot().getCenterBlock();
            for (int z = - 7; z < 8; z++) {
                for (int y = -13; y < 14; y++) {
                    for (int x = -7; x < 8; x++) {
                        if(!other.getBlockState(pos.offset(x,y,z)).isEmpty()){
                            other.destroyBlock(pos.offset(x,y,z),true);
                        }
                    }
                }
            }
        }
        super.die(damageSource);
    }

    @Override
    public void remove(RemovalReason reason) {
        if(myCube!=null&&!myCube.isRemoved()){
            myCube.markRemoved();
            myCube=null;
        }
        super.remove(reason);
    }

    @Override
    public void tick() {
        if(myClientCube!=null&&!myClientCube.isRemoved()&&tickCount>2&&level() instanceof ClientLevel clientLevel){
            Level other = myClientCube.getLevel();
            BlockPos pos = myClientCube.getPlot().getCenterBlock();
            for (int z = -3; z < 4; z++) {
                for (int y = -5; y < 8; y++) {
                    for (int x = -3; x < 4; x++) {
                        if(!other.getBlockState(pos.offset(x,y,z)).isEmpty()){
                            Vec3 center = pos.offset(x,y,z).getCenter();
                                clientLevel.addParticle(ParticleTypes.WITCH,center.x+(Math.random()-0.5)*1.1,center.y+(Math.random()-0.5)*1.1,center.z+(Math.random()-0.5)*1.1,0,0.9,0);


                        }
                    }
                }
            }
            this.lerpedonFours*=0.7f;

        }
        else{

            this.lerpedonFours= Mth.lerp(0.3f,this.lerpedonFours,1f);
        }
        if(myClientCube!=null&&myClientCube.isRemoved())myClientCube=null;
        if(getTarget()!=null){
            this.throwCD-=1;
            if(this.throwCD<=0){
                this.throwCD=100;
            }
        }
        if(this.toadstoolDelay>0){
            this.toadstoolDelay-=1;
            if(this.toadstoolDelay==0){
                this.throwCD=70;
                this.toadstoolDelay=-1;
                this.hasMadeCube=false;
                if(level()instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(ParticleTypes.WITCH,getX(),getY()+1.0,getZ(),12,0.5,0.5,0.5,0.1);
                }
            }
        }
        if(myCube!=null&&myCube.isRemoved()&&this.toadstoolDelay==-1){
            this.toadstoolDelay=70;
            this.myCube=null;
        }
        if(myCube!=null&&!myCube.isRemoved()&&this.freshTickCount>2&&level() instanceof ServerLevel serverLevel){
            ServerSubLevelContainer container = SubLevelContainer.getContainer(serverLevel);
            boolean falsified=true;
            falsified=falsified&&myCube.getLevel().getBlockState(myCube.getPlot().getCenterBlock().above()).getBlock()==Blocks.CHISELED_STONE_BRICKS;
            falsified=falsified&&myCube.getLevel().getBlockState(myCube.getPlot().getCenterBlock().above().east()).getBlock()==Blocks.AIR;
            falsified=falsified&&myCube.getLevel().getBlockState(myCube.getPlot().getCenterBlock().above().west()).getBlock()==Blocks.AIR;
            falsified=falsified&&myCube.getLevel().getBlockState(myCube.getPlot().getCenterBlock().above().north()).getBlock()==Blocks.AIR;
            falsified=falsified&&myCube.getLevel().getBlockState(myCube.getPlot().getCenterBlock().above().south()).getBlock()==Blocks.AIR;
            falsified=falsified&&myCube.getLevel().getBlockState(myCube.getPlot().getCenterBlock().above().above()).getBlock()==CFBlockInit.RUNIC_STONE_BRICKS.get();
            falsified=falsified&&myCube.getLevel().getBlockState(myCube.getPlot().getCenterBlock()).getBlock()==CFBlockInit.RUNIC_STONE_BRICKS.get();

            if(this.freshTickCount==3){
                BlockPos pos = myCube.getPlot().getCenterBlock();

            }
            if(this.freshTickCount%5==4){
                PacketDistributor.sendToPlayersTrackingEntity(this,new ToadstoolRenderPayload(getId(),myCube.getPlot().getCenterChunk().x,myCube.getPlot().getCenterChunk().z));
            }

            //myCube.getUniqueId()
             Pose3d pose = myCube.logicalPose();
             Vector3d localPosition = pose.transformPositionInverse(JOMLConversion.toJOML(position()));
             Vector3d vector3d = position().add(0,2.4+Math.max(Math.max(myCube.boundingBox().height(),myCube.boundingBox().width()),myCube.boundingBox().length())/2.4f,0).toVector3f().get(new Vector3d(0,0,0)).sub(myCube.logicalPose().position()).sub(0.0,0.5,0.0);
             vector3d=vector3d.mul(Math.min(1.0/vector3d.length(),1.0)).mul(1.6).add(getDeltaMovement().toVector3f().mul(2));
             if(getTarget()!=null&&this.throwCD<18){
                 vector3d = getTarget().position().toVector3f().get(new Vector3d(0,0,0)).sub(myCube.logicalPose().position()).sub(0.0,0.5,0.0).add(0,getTarget().getEyeHeight(),0);
                 vector3d=vector3d.mul(Math.min(14.0/vector3d.length(),1.0)).mul(1.4);
             }
             if(vector3d.y<0){
                 vector3d.y*=0.2;
             }
            if(vector3d.y>0){
                vector3d.y+=0.24;
            }
            assert container != null;
            PhysicsPipeline pipeline = container.physicsSystem().getPipeline();
            Vector3d dir = pose.orientation().getEulerAnglesYXZ(new Vector3d());
            pipeline.addLinearAndAngularVelocity(myCube, pipeline.getLinearVelocity(myCube, new Vector3d()).negate().mul(0.4), pipeline.getAngularVelocity(myCube, new Vector3d()).negate().mul(0.1));
            pipeline.addLinearAndAngularVelocity(myCube,vector3d,new Vector3d(dir.x*0.1+0.001,0.3,dir.z*0.1));
            if(!falsified){
                ServerLevel other = myCube.getLevel();
                BlockPos pos = myCube.getPlot().getCenterBlock();
                for (int z = - 7; z < 8; z++) {
                    for (int y = -13; y < 14; y++) {
                        for (int x = -7; x < 8; x++) {
                            if(!other.getBlockState(pos.offset(x,y,z)).isEmpty()){
                                other.destroyBlock(pos.offset(x,y,z),false);
                            }
                        }
                    }
                }
                myCube.markRemoved();
                myCube=null;
                this.toadstoolDelay=40;
            }
        }
        super.tick();
        if(myCube==null&&this.freshTickCount>0&&position().y>-61&&level() instanceof ServerLevel serverLevel&&!hasMadeCube){
            if(tempXChunk!=-19971234&&this.freshTickCount<70){
                myCube=(ServerSubLevel)Sable.HELPER.getContaining(serverLevel,new ChunkPos(tempXChunk,tempZChunk));
                if(myCube!=null)hasMadeCube=true;
            }
            else{

                BlockPos pos = getOnPos().offset(0,3,0);
                serverLevel.setBlock(pos,CFBlockInit.RUNIC_STONE_BRICKS.get().defaultBlockState(),2);

                BoundingBox3i bounds = BoundingBox3i.from(List.of(pos, pos.offset(1, 2, 1)));
                assert bounds != null;
                ServerSubLevelContainer container = SubLevelContainer.getContainer(serverLevel);
                ServerSubLevel serverSubLevel = SubLevelAssemblyHelper.assembleBlocks(
                        serverLevel,
                        pos,
                        List.of(pos),
                        bounds
                );

                myCube=serverSubLevel;
                hasMadeCube=true;
                final LevelAccelerator resultingAccelerator = new LevelAccelerator(serverLevel);
                final SubLevelAssemblyHelper.AssemblyTransform transform = new SubLevelAssemblyHelper.AssemblyTransform(getOnPos(), myCube.getPlot().getCenterBlock(), 0, Rotation.NONE, serverLevel);
                BlockPos newPos = transform.apply(getOnPos().above(1));
                LevelChunk chunk = resultingAccelerator.getChunk(SectionPos.blockToSectionCoord(newPos.getX()), SectionPos.blockToSectionCoord(newPos.getZ()));
                chunk.setBlockState(newPos, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), true);
                newPos = transform.apply(getOnPos().above(2));
                chunk = resultingAccelerator.getChunk(SectionPos.blockToSectionCoord(newPos.getX()), SectionPos.blockToSectionCoord(newPos.getZ()));
                chunk.setBlockState(newPos, CFBlockInit.RUNIC_STONE_BRICKS.get().defaultBlockState(), true);
            }
        }
        if(level() instanceof ServerLevel serverLevel){
            this.freshTickCount+=1;
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (level instanceof ServerLevel serverLevel) {

        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurting = super.hurt(source, amount);
        if(hurting){

        }
        return hurting;
    }

    @Override
    public void load(CompoundTag compound) {
        hasMadeCube=compound.getBoolean("hasMadeCube");

        if(compound.getInt("xChunkCube")!=-19971234&&hasMadeCube&&level() instanceof ServerLevel serverLevel){
            tempXChunk=compound.getInt("xChunkCube");
            tempZChunk=compound.getInt("zChunkCube");
            myCube=(ServerSubLevel)Sable.HELPER.getContaining(serverLevel,new ChunkPos(compound.getInt("xChunkCube"),compound.getInt("zChunkCube")));
        }
        super.load(compound);
    }
    public static AttributeSupplier.Builder createToadstoolAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.14).add(Attributes.ATTACK_DAMAGE,6.0).add(Attributes.MAX_HEALTH,25.0);
    }
    @Override
    public boolean save(CompoundTag compound) {
        if(myCube!=null){
            compound.putInt("xChunkCube",myCube.getPlot().getCenterChunk().x);
            compound.putInt("zChunkCube",myCube.getPlot().getCenterChunk().z);
            compound.putBoolean("hasMadeCube",hasMadeCube);

        }else{
            compound.putBoolean("hasMadeCube",false);

        }
        return super.save(compound);
    }
}
