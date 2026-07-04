package net.atired.creaturefeature.items;

import net.atired.creaturefeature.init.CFAchievements;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ThePillItem extends Item {
    public ThePillItem(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        MutableComponent mutablecomponent = Component.translatable("desc.creaturefeature.thepill");
        tooltipComponents.add(mutablecomponent.withStyle(ChatFormatting.GRAY));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof ServerPlayer player){
            CFAchievements.PILL.get().trigger(player);
            player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
