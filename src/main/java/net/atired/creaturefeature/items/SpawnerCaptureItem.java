package net.atired.creaturefeature.items;

import net.atired.creaturefeature.entity.BlitzEntity;
import net.atired.creaturefeature.init.CFDataComponentTypeInit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SpawnerCaptureItem extends Item {

    public SpawnerCaptureItem( Properties properties) {
        super( properties);
    }



    @Override
    public Component getName(ItemStack stack) {
        return Component.literal("Dreamcatcher");
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        MutableComponent mutablecomponent = Component.translatable("desc.creaturefeature.dreamcatcher");
        tooltipComponents.add(mutablecomponent.withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        DreamCatcherContents contents = context.getItemInHand().getOrDefault(CFDataComponentTypeInit.DREAMCATCHER_CONTENTS, DreamCatcherContents.EMPTY);
        BlockPos pos = context.getClickedPos().offset(context.getClickedFace().getNormal());
        if(contents.items().iterator().hasNext()&&context.getLevel().isEmptyBlock(pos)){
            ItemStack itemStack = contents.items().iterator().next();
            BlockItem item = (BlockItem)itemStack.getItem();
            BlockState state = item.getBlock().defaultBlockState();
            context.getLevel().setBlock(pos,state,2);
            if(context.getLevel().getBlockEntity(pos) instanceof BlockEntity spawnerBlockEntity&&itemStack.has(DataComponents.BLOCK_ENTITY_DATA)){
                spawnerBlockEntity.loadCustomOnly(itemStack.get(DataComponents.BLOCK_ENTITY_DATA).copyTag(),context.getLevel().registryAccess());

            }
            context.getPlayer().swing(context.getHand());

            context.getItemInHand().set(CFDataComponentTypeInit.DREAMCATCHER_CONTENTS, DreamCatcherContents.EMPTY);
            return InteractionResult.SUCCESS;
        }
        if(!contents.items().iterator().hasNext()&&!context.getLevel().getBlockState(context.getClickedPos()).isEmpty()&&context.getLevel() instanceof ServerLevel serverLevel){
            BlockState stated  =context.getLevel().getBlockState(context.getClickedPos());
            if(stated.getBlock().defaultDestroyTime()<0.0f){
                return InteractionResult.FAIL;
            }
            ItemStack stacked = stated.getBlock().asItem().getDefaultInstance();
            context.getLevel().playSound(null,context.getClickedPos(), stated.getBlock().getSoundType(stated,context.getLevel(),context.getClickedPos(),null).getBreakSound(), SoundSource.PLAYERS,1.2f,0.8f);
            context.getLevel().playSound(null,context.getClickedPos(), SoundEvents.SLIME_SQUISH, SoundSource.PLAYERS,1.1f,0.6f);
            if(context.getLevel().getBlockEntity(context.getClickedPos()) instanceof BlockEntity spawnerBlockEntity){
                spawnerBlockEntity.saveToItem(stacked,context.getLevel().registryAccess());
                if(spawnerBlockEntity instanceof Container){
                    if(context.getPlayer() instanceof ServerPlayer serverPlayer){
                        serverPlayer.displayClientMessage(Component.translatable("info.creaturefeature.dreamcatcher").withColor(0xAAAAAA),true);
                    }
                    return InteractionResult.FAIL;
                }
            }
            context.getItemInHand().set(CFDataComponentTypeInit.DREAMCATCHER_CONTENTS, new DreamCatcherContents(List.of(stacked)));
            Vec3 center = context.getClickedPos().getCenter();
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,stated),center.x,center.y,center.z,20,0.3,0.3,0.3,0.3);
            context.getLevel().destroyBlock(context.getClickedPos(),false);

            context.getPlayer().swing(context.getHand());
            return InteractionResult.SUCCESS;

        }
        return super.useOn(context);
    }
}
