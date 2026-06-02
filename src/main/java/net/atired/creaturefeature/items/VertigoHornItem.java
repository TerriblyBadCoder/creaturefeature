package net.atired.creaturefeature.items;

import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.networking.payloads.C2SVelSyncPayload;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class VertigoHornItem extends Item {
    private final TagKey<Instrument> instruments;

    public VertigoHornItem(Item.Properties properties, TagKey<Instrument> instruments) {
        super(properties);
        this.instruments = instruments;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        Optional<ResourceKey<Instrument>> optional = this.getInstrument(stack).flatMap(Holder::unwrapKey);
        if (optional.isPresent()) {
            MutableComponent mutablecomponent = Component.translatable(Util.makeDescriptionId("instrument", ((ResourceKey)optional.get()).location())).append("?");
            tooltipComponents.add(mutablecomponent.withStyle(ChatFormatting.GRAY));
        }

    }

    public static ItemStack create(Item item, Holder<Instrument> instrument) {
        ItemStack itemstack = new ItemStack(item);
        itemstack.set(DataComponents.INSTRUMENT, instrument);
        return itemstack;
    }

    public static void setRandom(ItemStack stack, TagKey<Instrument> instrumentTag, RandomSource random) {
        Optional<Holder<Instrument>> optional = BuiltInRegistries.INSTRUMENT.getRandomElementOf(instrumentTag, random);
        optional.ifPresent((p_330088_) -> {
            stack.set(DataComponents.INSTRUMENT, p_330088_);
        });
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(level instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(CFParticleInit.RABIES_PARTICLE.get(),livingEntity.getX(),livingEntity.getY(0.8),livingEntity.getZ(),4,0.2,0.2,0.2,0.2);
        }
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        Optional<? extends Holder<Instrument>> optional = this.getInstrument(itemstack);
        if (optional.isPresent()) {
            Instrument instrument = (Instrument)((Holder)optional.get()).value();
            player.startUsingItem(usedHand);
            play(level, player, instrument);
            player.getCooldowns().addCooldown(this, instrument.useDuration());
            player.addDeltaMovement(new Vec3(0,0.4,0));
            if(player instanceof LocalPlayer abstractClientPlayer){
                Vec3 dir = new Vec3(abstractClientPlayer.input.leftImpulse,0,abstractClientPlayer.input.forwardImpulse).normalize().scale(1.66).yRot(-abstractClientPlayer.getYHeadRot()/180.0f*3.14f);
                abstractClientPlayer.addDeltaMovement(dir);
                C2SVelSyncPayload payload = new C2SVelSyncPayload(abstractClientPlayer.getId(),dir.x,dir.y,dir.z);
                PacketDistributor.sendToServer(payload);
            }
            if(level instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(CFParticleInit.TOOT_PARTICLE.get(),player.getX(),player.getY(0.5),player.getZ(),1,0,0,0,0);
                serverLevel.sendParticles(CFParticleInit.RABIES_PARTICLE.get(),player.getX(),player.getY(0.5),player.getZ(),32,0.6,0.2,0.6,0.2);
            }
            if(player instanceof PlayerBrainrotAccessor accessor){
                accessor.setRabies(0.5f);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        Optional<Holder<Instrument>> optional = this.getInstrument(stack);
        return (Integer)optional.map((p_248418_) -> {
            return ((Instrument)p_248418_.value()).useDuration();
        }).orElse(0);
    }

    private Optional<Holder<Instrument>> getInstrument(ItemStack stack) {
        Holder<Instrument> holder = (Holder)stack.get(DataComponents.INSTRUMENT);
        if (holder != null) {
            return Optional.of(holder);
        } else {
            Iterator<Holder<Instrument>> iterator = BuiltInRegistries.INSTRUMENT.getTagOrEmpty(this.instruments).iterator();
            return iterator.hasNext() ? Optional.of((Holder)iterator.next()) : Optional.empty();
        }
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    private static void play(Level level, Player player, Instrument instrument) {
        SoundEvent soundevent = (SoundEvent)instrument.soundEvent().value();
        float f = instrument.range() / 16.0F;
        level.playSound(player, player, soundevent, SoundSource.RECORDS, f, 0.66F);
        level.playSound(player, player, soundevent, SoundSource.RECORDS, f*0.5f, 1.66F);
        level.playSound(player, player, soundevent, SoundSource.RECORDS, f*0.5f, 0.33F);
        level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of(player));
    }
}
