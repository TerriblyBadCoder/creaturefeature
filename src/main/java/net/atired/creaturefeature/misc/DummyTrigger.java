package net.atired.creaturefeature.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

public class DummyTrigger extends SimpleCriterionTrigger<DummyTrigger.TriggerInstance> {
    @Override
    public Codec<DummyTrigger.TriggerInstance> codec() {
        return DummyTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (a)->{return true;});
    }

    public static record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
        public static final Codec<DummyTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
            p_337403_ -> p_337403_.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(DummyTrigger.TriggerInstance::player)
                    )
                    .apply(p_337403_, DummyTrigger.TriggerInstance::new)
        );


        public boolean matches(Player item) {
            return this.player().isPresent()&&item.level()!=null;
        }
    }
}
