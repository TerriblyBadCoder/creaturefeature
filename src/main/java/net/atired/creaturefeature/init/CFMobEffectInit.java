package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.statuseffects.FiendishEffect;
import net.atired.creaturefeature.statuseffects.SleepyStatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CFMobEffectInit {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, CreatureFeature.MODID);
    public static final Holder<MobEffect> FIENDISH = MOB_EFFECTS.register("fiendish", () -> new FiendishEffect(
            MobEffectCategory.BENEFICIAL).addAttributeModifier(Attributes.MOVEMENT_SPEED,CreatureFeature.getId("effect.speedup"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    );
    public static final Holder<MobEffect> SLEEPY = MOB_EFFECTS.register("sleepy", () -> new SleepyStatusEffect(
                    MobEffectCategory.HARMFUL).addAttributeModifier(Attributes.MOVEMENT_SPEED,CreatureFeature.getId("effect.slowdown"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .addAttributeModifier(Attributes.JUMP_STRENGTH,CreatureFeature.getId("effect.jumpy"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            );
}
