package net.atired.creaturefeature.items;

import net.atired.creaturefeature.entity.BulletEntity;
import net.atired.creaturefeature.entity.StarProjEntity;
import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFItemInit;
import net.atired.creaturefeature.init.CFParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FlintlockItem extends Item {
    public FlintlockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.2f, 0.74F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        player.addDeltaMovement(player.getViewVector(1).scale(player.isShiftKeyDown()?-0.1:-0.5));
        if (level instanceof ServerLevel serverLevel) {
                BulletEntity bullet = new BulletEntity(CFEntityInit.BULLET.get(),serverLevel);
            bullet.ownerUsual=player;
            bullet.setPos(player.getEyePosition().add(0,-0.7,0).add(player.getViewVector(1).multiply(1,0,1).yRot(usedHand==InteractionHand.MAIN_HAND?-3.14f/2.4f:3.14f/2.4f).scale(0.4)));
            bullet.setYRot(player.getYRot());
            bullet.setXRot(player.getXRot());
            serverLevel.sendParticles(CFParticleInit.CASING_PARTICLE.get(),bullet.getX(),bullet.getY(),bullet.getZ(),1,0,0,0,0.2);
            serverLevel.sendParticles(ParticleTypes.SMOKE,bullet.getX(),bullet.getY(),bullet.getZ(),7,0,0,0,0.04);

            level.addFreshEntity(bullet);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        player.swing(usedHand,true);
        itemstack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, Consumer<Item> onBroken) {
        return super.damageItem(stack, amount, entity, onBroken);
    }
}
