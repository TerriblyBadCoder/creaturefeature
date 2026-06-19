package net.atired.creaturefeature.init;

import com.mojang.serialization.Codec;
import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CFDataComponentTypeInit {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CreatureFeature.MODID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> OPENED =
            DATA_COMPONENT_TYPES.register("opened_mind", () ->
                    DataComponentType.<Integer>builder()
                            .persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
                            .build()
            );
}
