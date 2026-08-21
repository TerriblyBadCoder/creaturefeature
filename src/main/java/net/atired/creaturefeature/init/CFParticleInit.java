package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CFParticleInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE,  CreatureFeature.MODID);

    public static final Supplier<SimpleParticleType> SLEEPY_PARTICLE = PARTICLE_TYPES.register(
            "sleepy",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> CRIT_TEXT_PARTICLE = PARTICLE_TYPES.register(
            "crit_text",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> LEAF_PARTICLE = PARTICLE_TYPES.register(
            "leaf",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> CASING_PARTICLE = PARTICLE_TYPES.register(
            "casing",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> GLASS_SHARD_PARTICLE = PARTICLE_TYPES.register(
            "glass_shard",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> GLASS_SHARD_SINISTER_PARTICLE = PARTICLE_TYPES.register(
            "glass_shard_sinister",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> FLOWER_PARTICLE = PARTICLE_TYPES.register(
            "flower",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> CRIT_PARTICLE = PARTICLE_TYPES.register(
            "crit",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> CRIT_VER_PARTICLE = PARTICLE_TYPES.register(
            "crit_ver",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> SPARKLE_PARTICLE = PARTICLE_TYPES.register(
            "sparkle",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> SLEEPY_EXPLODE_PARTICLE = PARTICLE_TYPES.register(
            "sleepy_explode",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> RABIES_PARTICLE = PARTICLE_TYPES.register(
            "rabies",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> TOOT_PARTICLE = PARTICLE_TYPES.register(
            "toot",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> CLEAVE_PARTICLE = PARTICLE_TYPES.register(
            "cleave",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> SPORE_PARTICLE = PARTICLE_TYPES.register(
            "spore",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> STAR_PARTICLE = PARTICLE_TYPES.register(
            "star",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> SPIT_PARTICLE = PARTICLE_TYPES.register(
            "spores_spit",
            () -> new SimpleParticleType(false)
    );
}
