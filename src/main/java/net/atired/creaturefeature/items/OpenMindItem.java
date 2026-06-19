package net.atired.creaturefeature.items;

import net.atired.creaturefeature.accessors.NoBlockContainerLevelAccess;
import net.atired.creaturefeature.client.CreatureFeatureClient;
import net.atired.creaturefeature.init.CFDataComponentTypeInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OpenMindItem extends Item {
    public OpenMindItem(Properties properties) {
        super(properties);
    }
    private static final Component CONTAINER_TITLE = Component.translatable("container.creaturefeature.open_mind");



    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        MutableComponent mutablecomponent = Component.translatable("desc.creaturefeature.open_mind");
        tooltipComponents.add(mutablecomponent.withStyle(ChatFormatting.GRAY));
    }
    protected MenuProvider getMenuProvider(Level level, BlockPos pos,Entity entity) {
        return new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) -> {
            CraftingMenu menu = new CraftingMenu(p_52229_, p_52230_,new NoBlockContainerLevelAccess(entity));
            return menu;
        }, CONTAINER_TITLE);
    }
    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {

        item.set(CFDataComponentTypeInit.OPENED,0);
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if(entity instanceof Player player && player.containerMenu== player.inventoryMenu){
            stack.set(CFDataComponentTypeInit.OPENED,0);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        itemstack.set(CFDataComponentTypeInit.OPENED,1);
        if(player.level().isClientSide()){
            player.playSound(SoundEvents.SLIME_SQUISH,1.0f,0.9f);
            player.playSound(SoundEvents.SLIME_SQUISH,1.5f,0.7f);
            player.playSound(SoundEvents.PLAYER_BURP,0.6f,0.9f);
            CreatureFeatureClient.PROXY.wobble=1.0f;
            CreatureFeatureClient.PROXY.wobblyItem=itemstack;
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }else{
            player.openMenu(getMenuProvider(level,player.getOnPos(),player));
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
    }
}
