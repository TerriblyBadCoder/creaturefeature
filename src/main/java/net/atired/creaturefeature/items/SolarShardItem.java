package net.atired.creaturefeature.items;

import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.atired.creaturefeature.entity.MurkyPearlEntity;
import net.atired.creaturefeature.entity.StarProjEntity;
import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class SolarShardItem extends Item implements ProjectileItem {
    public SolarShardItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            for (int i = 0; i < 4; i++) {
                StarProjEntity snowball = new StarProjEntity(level, player);
                snowball.setItem(itemstack);
                snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 35.0F);
                level.addFreshEntity(snowball);
            }
            for (int i = 0; i < 4; i++) {
                StarProjEntity snowball = new StarProjEntity(level, player);
                snowball.setItem(itemstack);
                snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 12.0F);
                level.addFreshEntity(snowball);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        itemstack.consume(1, player);
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        MutableComponent mutablecomponent = Component.translatable("desc.creaturefeature.solar");
        tooltipComponents.add(mutablecomponent.withStyle(ChatFormatting.GRAY));
    }
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        StarProjEntity snowball = new StarProjEntity(level, pos.x(), pos.y(), pos.z());
        snowball.setItem(stack);
        return snowball;
    }
}
