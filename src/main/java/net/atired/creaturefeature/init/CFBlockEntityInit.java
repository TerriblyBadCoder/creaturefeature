package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.blocks.blockentities.DoohickeyBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CFBlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreatureFeature.MODID);

    public static final Supplier<BlockEntityType<DoohickeyBlockEntity>> DOOHICKEY = BLOCK_ENTITY_TYPES.register(
            "doohickey",
            () -> BlockEntityType.Builder.of(DoohickeyBlockEntity::new, CFBlockInit.DOOHICKEY.get()).build(null)
    );
}
