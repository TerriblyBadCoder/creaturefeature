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
    public static final Supplier<SimpleParticleType> LEAF_PARTICLE = PARTICLE_TYPES.register(
            "leaf",
            () -> new SimpleParticleType(false)
    );
    public static final Supplier<SimpleParticleType> FLOWER_PARTICLE = PARTICLE_TYPES.register(
            "flower",
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
}
