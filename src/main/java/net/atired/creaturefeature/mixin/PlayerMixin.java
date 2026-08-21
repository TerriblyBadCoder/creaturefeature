package net.atired.creaturefeature.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.atired.creaturefeature.init.CFAchievements;
import net.atired.creaturefeature.networking.payloads.C2SManPayload;
import net.atired.creaturefeature.networking.payloads.GasLeakPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerBrainrotAccessor{
    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    private float tempDelayDamage = 0.0f;
    private float brainRot=0.0f;
    private float rabies=0.0f;
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void setBrainrot(float brainrot) {
        this.brainRot=Math.clamp(brainrot,0.0f,1.0f);
    }

    @Override
    public float getDelayedDamage() {
        return this.tempDelayDamage;
    }

    @Override
    public void setDelayedDamage(float damage) {
        this.tempDelayDamage=damage;
    }
    @Unique
    private int creaturefeature$counter =0;
    @Unique
    private int creaturefeature$aged =0;
    @Unique
    private double creaturefeature$oldX =0;
    @Unique
    private double creaturefeature$oldZ =0;

    @Inject(method = "Lnet/minecraft/world/entity/player/Player;tick()V",at=@At("HEAD"))
    private void iDontTrustYerEventsPally(CallbackInfo ci){
        this.creaturefeature$aged +=1;
        if(getY()>127.8&&level()!=null&&(getYRot()<-89&&getYRot()>-91)&&level().isClientSide()&&CreatureFeatureClient.PROXY.searchingForHim&&level().dimensionType().respawnAnchorWorks()&&this.creaturefeature$aged%50==(48-creaturefeature$counter *2)){
            if(Math.abs(this.creaturefeature$oldX -this.getX())>0.02&&Math.abs(this.creaturefeature$oldZ -this.getZ())<0.02){
                creaturefeature$counter +=1;
                this.creaturefeature$aged =0;
                playSound(SoundEvents.BELL_RESONATE, creaturefeature$counter /5f, creaturefeature$counter /10f);
                playSound(SoundEvents.ARROW_HIT_PLAYER, creaturefeature$counter /5f, creaturefeature$counter /10f);
                CreatureFeatureClient.PROXY.manShader= creaturefeature$counter /24f;
                if(creaturefeature$counter >23){
                    playSound(SoundEvents.WARDEN_SONIC_BOOM, creaturefeature$counter /5f, creaturefeature$counter /10f);
                    creaturefeature$counter =0;
                    CreatureFeatureClient.PROXY.searchingForHim=false;
                    CreatureFeatureClient.PROXY.manShader=0;
                    C2SManPayload payload = new C2SManPayload(getId());
                    PacketDistributor.sendToServer(payload);
                    //CFAchievements.NOBODY.get().trigger();
                }
            }
        }
        else if(getY()<127.8||!(getYRot()<-89&&getYRot()>-91)||level()==null||!level().dimensionType().respawnAnchorWorks()){
            creaturefeature$counter =0;
        }
        creaturefeature$oldX =getX();
        creaturefeature$oldZ =getZ();
        if(getDelayedDamage()>0){
            int dam=Math.min((int)Math.floor((getDelayedDamage()+0.5f)/2)+1,5);
            float prevDelayed = getDelayedDamage();
            setDelayedDamage(getDelayedDamage()-0.01f);
            if((LivingEntity)(this) instanceof ServerPlayer player){
                if(prevDelayed>0.0f&&getDelayedDamage()<=0.0f){
                    GasLeakPayload payload2 = new GasLeakPayload(player.getId(),
                            Math.max(getDelayedDamage(),0.0f)-0.5f);
                    PacketDistributor.sendToPlayer(player,payload2);
                }
                if(getDelayedDamage()>0.5f&&this.tickCount%30==0){
                    boolean hurting = hurt(damageSources().magic(),dam);
                    if(hurting){
                        CFAchievements.GYAS.get().trigger(player);
                        setDelayedDamage(Math.max(0.0f,getDelayedDamage()-dam));
                        GasLeakPayload payload2 = new GasLeakPayload(player.getId(),getDelayedDamage());
                        PacketDistributor.sendToPlayer(player,payload2);
                    }
                }
            }
        }

        if(getRabies()>0){
            setRabies(getRabies()-0.01f);

            if(this.tickCount%10==0&&getRabies()>0.33){
                if((LivingEntity)(this) instanceof Player player){
                    player.causeFoodExhaustion(0.2f);
                }
                if(this.tickCount%20==0)
                    this.hurt(damageSources().dryOut(),1);
            }
        }
        if(getBrainrot()>0){
            yRotO=getYRot();
            setYRot(getYRot()+ Mth.sin(this.tickCount/4.0f)*5.0f*getBrainrot());
            if(onGround())
                move(MoverType.PLAYER,new Vec3(
                        Mth.sin(this.tickCount/12.0f)*0.1f-Mth.sin((this.tickCount-1)/12.0f)*0.1f,0,
                        -Mth.cos(this.tickCount/12.0f)*0.1f+Mth.cos((this.tickCount-1)/12.0f)*0.1f));
            setBrainrot(getBrainrot()-0.02f);
        }
    }
    @Override
    public float getBrainrot() {
        return this.brainRot;
    }
    private boolean can = false;
    @Override
    public float getRabies() {
        return rabies;
    }

    @Override
    public boolean getManCan() {
        return this.can;
    }

    @Override
    public void setManCan(boolean can) {
        this.can=can;
    }

    @Override
    public void setRabies(float rabies) {
        this.rabies=Math.clamp(rabies,0.0f,1.0f);
    }
}
