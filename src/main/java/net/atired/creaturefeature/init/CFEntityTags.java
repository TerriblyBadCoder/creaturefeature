package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class CFEntityTags {
    public static final TagKey<EntityType<?>> PATHOGEN_SOURCES = TagKey.create(
            Registries.ENTITY_TYPE,
            CreatureFeature.getId("pathogen_sources")
    );
}
