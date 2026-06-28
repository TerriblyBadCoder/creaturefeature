package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CFSoundInit {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, CreatureFeature.MODID);
    public static final Holder<SoundEvent> NOTHING = SOUND_EVENTS.register(
            "music_disc.nothing",
            // Takes in the registry name
            SoundEvent::createVariableRangeEvent
    );
    public static final Holder<SoundEvent> FRIEND = SOUND_EVENTS.register(
            "entity.friend",
            // Takes in the registry name
            SoundEvent::createVariableRangeEvent
    );
    public static final Holder<SoundEvent> FRIEND_HURT = SOUND_EVENTS.register(
            "entity.friend_hurt",
            // Takes in the registry name
            SoundEvent::createVariableRangeEvent
    );
}
