package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.misc.DummyTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CFAchievements {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, CreatureFeature.MODID);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> SLEEPY=  TRIGGER_TYPES.register("sleepy",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> FRIENDLESS=  TRIGGER_TYPES.register("friendless",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> PUNCH_EVERYONE=  TRIGGER_TYPES.register("punch_everyone",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> GYAS =  TRIGGER_TYPES.register("gyas",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> SINISTER =  TRIGGER_TYPES.register("sinister",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> PILL=  TRIGGER_TYPES.register("pill",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> RENOVATION=  TRIGGER_TYPES.register("renovation",DummyTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, DummyTrigger> NOBODY =  TRIGGER_TYPES.register("nobody",DummyTrigger::new);

}
