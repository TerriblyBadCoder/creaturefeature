package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class CFBiomeTags {
    public static final TagKey<Biome> HAS_PATHOGEN = TagKey.create(
            Registries.BIOME,
            CreatureFeature.getId("has_pathogen")
    );
}
