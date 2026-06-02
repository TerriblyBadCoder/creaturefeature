package net.atired.creaturefeature.statuseffects;

import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SleepyStatusEffect extends MobEffect {
    public SleepyStatusEffect(MobEffectCategory category) {
        super(category, 0x93c077);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        livingEntity.setXRot(Math.min(90.0f,livingEntity.getViewXRot(1)+1));
        if(Math.random()>0.9)
        livingEntity.level().addParticle(CFParticleInit.SLEEPY_PARTICLE.get(),livingEntity.getX(-0.5f+Math.random()),livingEntity.getEyeY(),livingEntity.getZ(-0.5f+Math.random()),0,-0.1,0);
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
