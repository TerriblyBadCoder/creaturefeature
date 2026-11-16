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
    
    public static final TagKey<EntityType<?>> DREAMWEAVER_SUMMONS = TagKey.create(
            Registries.ENTITY_TYPE,
            CreatureFeature.getId("dreamweaver_summons")
    );
    public static final TagKey<EntityType<?>> DREAMWEAVER_END_SUMMONS = TagKey.create(
            Registries.ENTITY_TYPE,
            CreatureFeature.getId("dreamweaver_end_summons")
    );
    public static final TagKey<EntityType<?>> DREAMWEAVER_LIMITED = TagKey.create(
            Registries.ENTITY_TYPE,
            CreatureFeature.getId("dreamweaver_summon_one")
    );
    public static final TagKey<EntityType<?>> DREAMWEAVER_UNLIMITED = TagKey.create(
            Registries.ENTITY_TYPE,
            CreatureFeature.getId("dreamweaver_summon_many")
    );
}
