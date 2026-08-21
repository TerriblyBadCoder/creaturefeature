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
import net.minecraft.world.item.ShearsItem;
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
    public static final DeferredBlock<Block> CARAPACE_BRICKS = registerBlock("carapace_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> CARAPACE_BRICK_STAIRS = registerBlock("carapace_brick_stairs",
            () -> new StairBlock(CARAPACE_BRICKS.get().defaultBlockState(),BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> DOWN_FEATHERS = registerBlock("down_feathers",
            () -> new FeathersBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_WOOL)));
    public static final DeferredBlock<Block> DOWN_FEATHERS_CARPET = registerBlock("down_feathers_carpet",
            () -> new FeathersCarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_WOOL)));
    public static final DeferredBlock<Block> MOSAIC_DOWN_FEATHERS = registerBlock("mosaic_down_feathers",
            () -> new FeathersBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_WOOL)));
    public static final DeferredBlock<Block> MOSAIC_DOWN_FEATHERS_CARPET = registerBlock("mosaic_down_feathers_carpet",
            () -> new FeathersCarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_WOOL)));
    public static final DeferredBlock<Block> FIENDISH_TILES = registerBlock("fiendish_tiles",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));
    public static final DeferredBlock<Block> BLIND_FIENDISH_TILES = registerBlock("blind_fiendish_tiles",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)));
    public static final DeferredBlock<Block> MURKY_PEARL_TILES = registerBlock("murky_pearl_tiles",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<Block> MURKY_PEARL_TILE_STAIRS = registerBlock("murky_pearl_tile_stairs",
            () -> new StairBlock(MURKY_PEARL_TILES.get().defaultBlockState(),BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<Block> MURKY_PEARL_BLOCK = registerBlock("murky_pearl_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<Block> DREAM_SILK_SPOOL = registerBlock("dream_silk_spool",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_WOOL)));
    public static final DeferredBlock<Block> WALLPAPER = registerBlock("wallpaper",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(1.0F, 3.0F)));
    public static final DeferredBlock<Block> SOLAR_BLOCK = registerBlock("solar_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SHROOMLIGHT)));
    public static final DeferredBlock<Block> SOLAR_BRICKS = registerBlock("solar_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SHROOMLIGHT)));
    public static DeferredBlock<Block> RUNIC_STONE_BRICKS;
    static {
        ShearsItem
        RUNIC_STONE_BRICKS=null;
        if(ModList.get().isLoaded("sable")){
            SableCarrier.initBlock();
        }
    }
    public static final DeferredBlock<Block> DOOHICKEY = registerDoohickeyBlock("doohickey",
            () -> new DoohickeyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()));

}
