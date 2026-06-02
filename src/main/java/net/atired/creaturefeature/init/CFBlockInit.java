package net.atired.creaturefeature.init;


import java.util.function.Supplier;

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.blocks.EepBlock;
import net.atired.creaturefeature.blocks.MinedFlayerJellyBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CFBlockInit {
    public static final DeferredRegister.Blocks
            BLOCKS = DeferredRegister.createBlocks(CreatureFeature.MODID);
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block)
    {
        return CFItemInit.ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties()));
    }
   public static final DeferredBlock<Block> MINEDFLAYER_JELLY = registerBlock("minedflayer_jelly",
            () -> new MinedFlayerJellyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).friction(1.04F).sound(SoundType.FUNGUS)));

    public static final DeferredBlock<Block> EEP = registerBlock("eep",
            () -> new EepBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));


}
