package net.atired.creaturefeature.items;

import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.Level;

public class BacteriumBallItem extends Item {
    public BacteriumBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(player instanceof LivingEntityGoopAccessor accessor){
            accessor.setBacterial(1.0f);
            player.swing(usedHand);
            for (int i = 0; i < 13; i++) {
                level.addParticle(CFParticleInit.SPARKLE_PARTICLE.get(),
                        player.getX(Math.random()-0.5),player.getY(0.5),player.getZ(Math.random()-0.5),
                        (Math.random()-0.5)/5.0f,0,(Math.random()-0.5)/5.0f);
            }
            ItemStack stack = player.getItemInHand(usedHand);
            level.playSound(null,player.getOnPos(), SoundEvents.HORSE_DEATH, SoundSource.PLAYERS,0.1f,0.8f);
            level.playSound(null,player.getOnPos(), SoundEvents.HONEY_BLOCK_PLACE, SoundSource.PLAYERS,0.8f,1.3f);
            level.playSound(null,player.getOnPos(), SoundEvents.HONEY_BLOCK_BREAK, SoundSource.PLAYERS,1.2f,1.1f);
            level.playSound(null,player.getOnPos(), SoundEvents.SLIME_DEATH_SMALL, SoundSource.PLAYERS,1.2f,1.1f);


            player.getCooldowns().addCooldown(stack.getItem(),32);
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }
        return super.use(level, player, usedHand);
    }
}
