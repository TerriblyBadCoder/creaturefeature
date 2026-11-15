package net.atired.creaturefeature.items;

import net.atired.creaturefeature.entity.LightningDelayEntity;
import net.atired.creaturefeature.init.CFEntityInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class WeatheringRodItem extends Item {
    public WeatheringRodItem(Properties properties) {
        super(properties);
    }


    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                if(i>14&&level instanceof ServerLevel serverLevel){
                    LightningDelayEntity delayEntity = new LightningDelayEntity(CFEntityInit.DELAY.get(), serverLevel);
                    delayEntity.setPos(entityLiving.getPosition(1));
                    double len = Math.pow(i-14,0.5)/14.0f+1.0f;
                    delayEntity.setDeltaMovement(entityLiving.getViewVector(1).multiply(1,0.01,1).normalize().scale(len*0.8));
                    serverLevel.addFreshEntity(delayEntity);
                    delayEntity.playSound(SoundEvents.CAMPFIRE_CRACKLE,2.0f,0.6f+(float)Math.random()/5.0f);
                    stack.hurtAndBreak(1,entityLiving, EquipmentSlot.MAINHAND);
                    delayEntity.playSound(SoundEvents.CAMPFIRE_CRACKLE,5.0f,0.6f+(float)Math.random()/5.0f);
                    delayEntity.playSound(SoundEvents.WIND_CHARGE_THROW,0.8f,0.6f);
                    delayEntity.playSound(SoundEvents.WIND_CHARGE_THROW,0.4f,0.1f);
                }
        }
    }


    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);

        player.startUsingItem(usedHand);

        return InteractionResultHolder.consume(itemstack);
    }
}
