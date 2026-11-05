package net.atired.creaturefeature.mixin;

import net.atired.creaturefeature.entity.PowderSkullEntity;
import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFItemInit;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.OminousItemSpawner;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OminousItemSpawner.class)
public abstract class OminousItemSpawnerMixin extends Entity {
    @Shadow public abstract ItemStack getItem();

    @Shadow protected abstract void setItem(ItemStack item);

    public OminousItemSpawnerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "spawnItem()V",at=@At("HEAD"),cancellable = true)
    private void evilPowderSkullStuff(CallbackInfo ci){
        Level level = this.level();
        ItemStack itemstack = this.getItem();
        if(!itemstack.isEmpty()&&itemstack.getItem()== CFItemInit.POWDER_SKULL_EGG.get()){
            Item var5 = itemstack.getItem();
            PowderSkullEntity entity;
            entity=new PowderSkullEntity(CFEntityInit.POWDER_SKULL.get(), level);
            level.levelEvent(3021, this.blockPosition(), 1);
            if(level instanceof ServerLevel serverLevel){
                entity.setPos(this.position());
                serverLevel.addFreshEntity(entity);
            }
            this.setItem(ItemStack.EMPTY);
            ci.cancel();
        }

    }
}
