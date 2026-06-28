package net.atired.creaturefeature.init;

import com.mojang.serialization.Codec;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.items.DreamCatcherContents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class  CFDataComponentTypeInit {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CreatureFeature.MODID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> OPENED =
            DATA_COMPONENT_TYPES.register("opened_mind", () ->
                    DataComponentType.<Integer>builder()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build()
            );
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<DreamCatcherContents>> DREAMCATCHER_CONTENTS = DATA_COMPONENT_TYPES.register("dreamcatcher_contents", (p_341857_) -> {
        return DataComponentType.<DreamCatcherContents>builder().persistent(DreamCatcherContents.CODEC).networkSynchronized(DreamCatcherContents.STREAM_CODEC).build();
    });
}
