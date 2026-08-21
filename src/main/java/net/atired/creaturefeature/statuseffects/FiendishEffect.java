package net.atired.creaturefeature.statuseffects;

import net.atired.creaturefeature.init.CFAchievements;
import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FiendishEffect extends MobEffect {
    public FiendishEffect(MobEffectCategory category) {
        super(category, 0xff57f9);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
//        if(Math.random()>0.9)
//        livingEntity.level().addParticle(CFParticleInit.CRIT_VER_PARTICLE.get(),livingEntity.getX(-0.5f+Math.random()),livingEntity.getY(Math.random()),livingEntity.getZ(-0.5f+Math.random()),0,-0.1,0);
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public void onMobHurt(LivingEntity livingEntity, int amplifier, DamageSource damageSource, float amount) {
        super.onMobHurt(livingEntity, amplifier, damageSource, amount);
    }
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
