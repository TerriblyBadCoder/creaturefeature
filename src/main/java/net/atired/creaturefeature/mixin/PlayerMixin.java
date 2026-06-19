package net.atired.creaturefeature.mixin;

import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.atired.creaturefeature.networking.payloads.GasLeakPayload;
import net.atired.creaturefeature.networking.payloads.RabiesPayload;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerBrainrotAccessor{
    @Shadow public abstract boolean hurt(DamageSource source, float amount);
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

    @Inject(method = "Lnet/minecraft/world/entity/player/Player;tick()V",at=@At("HEAD"))
    private void iDontTrustYerEventsPally(CallbackInfo ci){
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
                        Mth.sin(this.tickCount/12.0f)*1.5f-Mth.sin((this.tickCount-1)/12.0f)*1.5f,0,
                        -Mth.cos(this.tickCount/12.0f)*1.5f+Mth.cos((this.tickCount-1)/12.0f)*1.5f));
            setBrainrot(getBrainrot()-0.02f);
        }
    }
    @Override
    public float getBrainrot() {
        return this.brainRot;
    }

    @Override
    public float getRabies() {
        return rabies;
    }

    @Override
    public void setRabies(float rabies) {
        this.rabies=Math.clamp(rabies,0.0f,1.0f);
    }
}
