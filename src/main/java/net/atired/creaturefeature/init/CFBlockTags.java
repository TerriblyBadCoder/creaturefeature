package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CFBlockTags {
    public static final TagKey<Block> SHEARABLE = TagKey.create(
            Registries.BLOCK,
            CreatureFeature.getId("shearable")
    );
    public static final TagKey<Item> GHOSTLY = TagKey.create(
            Registries.ITEM,
            CreatureFeature.getId("ghostly")
    );
}
