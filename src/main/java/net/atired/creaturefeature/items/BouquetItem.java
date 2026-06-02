package net.atired.creaturefeature.items;

import net.atired.creaturefeature.init.CFItemInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BouquetItem extends Item {
    public BouquetItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        return super.use(level, player, usedHand);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        MutableComponent mutablecomponent = Component.translatable("desc.creaturefeature.bouquet");
        tooltipComponents.add(mutablecomponent.withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        if(context.getLevel().getBlockState(context.getClickedPos()).getBlock().asItem().getDefaultInstance().is(ItemTags.FLOWERS)){
            Vec3 pos = context.getClickedPos().getCenter().add(0,0.3,0);
            if(context.getLevel() instanceof ServerLevel serverLevel){
                serverLevel.playSound(null,context.getClickedPos(), SoundEvents.BONE_MEAL_USE, SoundSource.PLAYERS,2.0f,0.8f);
                serverLevel.playSound(null,context.getClickedPos(), SoundEvents.FLOWERING_AZALEA_BREAK, SoundSource.PLAYERS,2.0f,0.8f);

                ItemEntity item = new ItemEntity(serverLevel,pos.x,pos.y,pos.z,context.getLevel().getBlockState(context.getClickedPos()).getBlock().asItem().getDefaultInstance());
                serverLevel.addFreshEntity(item);
            }else{
                for (int i = 0; i < 5; i++) {
                    context.getLevel().addParticle(CFParticleInit.FLOWER_PARTICLE.get(),pos.x+(Math.random()-0.5)/2.0,pos.y-0.3,pos.z+(Math.random()-0.5)/2.0,(Math.random()-0.5)/6.0,0.2+(Math.random()-0.5)/8.0,(Math.random()-0.5)/6.0);
                    context.getLevel().addParticle(CFParticleInit.LEAF_PARTICLE.get(),pos.x+(Math.random()-0.5)/1.2,pos.y-0.3,pos.z+(Math.random()-0.5)/1.2,(Math.random()-0.5)/4.0,0.7,(Math.random()-0.5)/4.0);
                }
            }
            context.getItemInHand().hurtAndBreak(1,context.getPlayer(), EquipmentSlot.MAINHAND);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        int damage =0;
        itemStack=itemStack.copyWithCount(1);
        if(itemStack.has(DataComponents.DAMAGE)){
            damage = itemStack.get(DataComponents.DAMAGE);
        }
        itemStack.set(DataComponents.DAMAGE,damage+1);
        if(damage>=8){
            return ItemStack.EMPTY;
        }
        return itemStack;
    }
}
