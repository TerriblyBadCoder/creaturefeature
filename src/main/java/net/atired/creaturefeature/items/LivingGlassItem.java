package net.atired.creaturefeature.items;

import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class LivingGlassItem extends Item {
    public LivingGlassItem(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        MutableComponent mutablecomponent = Component.translatable("desc.creaturefeature.living_glass");
        tooltipComponents.add(mutablecomponent.withStyle(ChatFormatting.GRAY));
    }
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.setCount(stack.getCount()-1);
        target.invulnerableTime=0;
        if(!target.getActiveEffects().isEmpty()){
            int indexed = target.getRandom().nextInt(target.getActiveEffects().size());
            MobEffectInstance instance = target.getActiveEffects().stream().toList().get(indexed);
            target.removeEffect(instance.getEffect());
        }
        if(target.level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(CFParticleInit.GLASS_SHARD_SINISTER_PARTICLE.get(),target.getX(),target.getY(0.5),target.getZ(),12,target.getBbWidth()*0.8,target.getBbHeight()*0.4,target.getBbWidth()*0.8,0.2);
            serverLevel.sendParticles(CFParticleInit.GLASS_SHARD_PARTICLE.get(),target.getX(),target.getY(0.5),target.getZ(),4,target.getBbWidth()*0.8,target.getBbHeight()*0.4,target.getBbWidth()*0.8,0.23);
        }
        return super.hurtEnemy(stack, target, attacker);
    }

}
