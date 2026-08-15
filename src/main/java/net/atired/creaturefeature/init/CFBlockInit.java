package net.atired.creaturefeature.init;


import java.util.function.Supplier;

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.blocks.*;
import net.atired.creaturefeature.items.DoohickeyBlockItem;
import net.atired.creaturefeature.misc.SableCarrier;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CFBlockInit {
    public static final DeferredRegister.Blocks
            BLOCKS = DeferredRegister.createBlocks(CreatureFeature.MODID);
    public static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    public static <T extends Block> DeferredBlock<T> registerDoohickeyBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name,block);
        registerDoohickeyBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> DeferredItem<Item> registerDoohickeyBlockItem(String name, DeferredBlock<T> block)
    {
        return CFItemInit.ITEMS.register(name, () -> new DoohickeyBlockItem(block.get(),new Item.Properties()));
    }
    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block)
    {
        return CFItemInit.ITEMS.register(name, () -> new BlockItem(block.get(),new Item.Properties()));
    }
   public static final DeferredBlock<Block> MINEDFLAYER_JELLY = registerBlock("minedflayer_jelly",
            () -> new MinedFlayerJellyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).friction(1.04F).sound(SoundType.FUNGUS)));

    public static final DeferredBlock<Block> CARAPACE_BLOCK = registerBlock("carapace_block",
            () -> new CarapaceBlock(new ColorRGBA(0xFF7b775f),BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_MUD)));
    public static final DeferredBlock<Block> EEP = registerBlock("eep",
            () -> new EepBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));
    public static DeferredBlock<Block> RUNIC_STONE_BRICKS;
    static {

        RUNIC_STONE_BRICKS=null;
        if(ModList.get().isLoaded("sable")){
            SableCarrier.initBlock();
        }
    }
    public static final DeferredBlock<Block> DOOHICKEY = registerDoohickeyBlock("doohickey",
            () -> new DoohickeyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()));

}
