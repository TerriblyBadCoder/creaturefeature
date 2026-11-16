package net.atired.creaturefeature.items;

import net.atired.creaturefeature.init.CFParticleInit;
import net.atired.creaturefeature.init.CFSoundInit;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

public class FisheyeItem extends Item {
    public FisheyeItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        livingEntity.playSound(CFSoundInit.FISHEYE_SCREECH.value(),0.2f,1.0f+(float)Math.random()/5.0f);

        if(level instanceof ServerLevel serverLevel){
            for(LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class,livingEntity.getBoundingBox().inflate(24))){
                for(int i = 0; i < 16; ++i) {
                    double d0 = living.getX() + (living.getRandom().nextDouble() - 0.5) * 16.0;
                    double d1 = Mth.clamp(living.getY() + (double)(living.getRandom().nextInt(16) - 8), (double)level.getMinBuildHeight(), (double)(level.getMinBuildHeight() + ((ServerLevel)level).getLogicalHeight() - 1));
                    double d2 = living.getZ() + (living.getRandom().nextDouble() - 0.5) * 16.0;
                    if (living.isPassenger()) {
                        living.stopRiding();
                    }

                    Vec3 vec3 = living.position();
                    EntityTeleportEvent.ChorusFruit event = EventHooks.onChorusFruitTeleport(living, d0, d1, d2);
                    serverLevel.sendParticles(CFParticleInit.FISHEYE_PARTICLE.get(),living.getX(),living.getY(),living.getZ(),1,0,0,0,0);
                    if (living.randomTeleport(d0, d1,d2, false)) {
                        level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(living));
                        Vec3 dirTo = living.getPosition(1).subtract(vec3).multiply(1,0,1).normalize().scale(1.4);
                        serverLevel.sendParticles(CFParticleInit.FISHEYE_PARTICLE.get(),living.getX(),living.getY(),living.getZ(),1,0,0,0,0);
                        SoundSource soundsource;
                        SoundEvent soundevent;
                        BlockState state = living.getBlockStateOn();
                        for (int j = 0; j < 3; j++) {
                            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.DUST_PILLAR,state),living.getX(),living.getY(),living.getZ(),20,1.15,0,1.15,0);
                            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK,state),living.getX(),living.getY(),living.getZ(),20,1.18,0,1.18,0);
                        }
                        soundevent = SoundEvents.SHULKER_TELEPORT;
                        soundsource = SoundSource.PLAYERS;
                        living.setDeltaMovement(dirTo.x,0.55,dirTo.z);
                        level.playSound((Player)null, living.getX(), living.getY(), living.getZ(), soundevent, soundsource);
                        living.resetFallDistance();
                        break;
                    }
                }

                if (living instanceof Player) {
                    Player player = (Player)living;
                    player.resetCurrentImpulseContext();
                    player.getCooldowns().addCooldown(this, 20);
                }
            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
