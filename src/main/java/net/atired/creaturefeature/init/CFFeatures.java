package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.worldgen.features.LivingObeliskFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CFFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, CreatureFeature.MODID);
    public static final Supplier<Feature<NoneFeatureConfiguration>> OBELISK_FEATURE =
            FEATURES.register("living_obelisk_feature", () -> new LivingObeliskFeature(NoneFeatureConfiguration.CODEC));
}
