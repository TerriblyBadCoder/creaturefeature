package net.atired.creaturefeature.worldgen;

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.init.CFFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CFConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OBELISK_KEY = registerKey("obelisk_key");


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context,OBELISK_KEY, CFFeatures.OBELISK_FEATURE.get(), new NoneFeatureConfiguration());
    }
    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,  CreatureFeature.getId(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
