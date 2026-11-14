package net.atired.creaturefeature.worldgen;

import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class CFPlacedFeatures {
    public static final ResourceKey<PlacedFeature> OBELISK_PLACED = registerKey("obelisk_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, OBELISK_PLACED, configuredFeatures.getOrThrow(CFConfiguredFeatures.OBELISK_KEY),
                commonPlacement(56, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(50), VerticalAnchor.belowTop(27))));

    }
        public static List<PlacementModifier> typicalPlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonPlacement(int pCount, PlacementModifier pHeightRange) {
        return typicalPlacement(CountPlacement.of(pCount), pHeightRange);
    }

    public static List<PlacementModifier> rarePlacement(int pChance, PlacementModifier pHeightRange) {
        return typicalPlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }
    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, CreatureFeature.getId(name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
