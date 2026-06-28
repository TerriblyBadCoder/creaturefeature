package net.atired.creaturefeature;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.BooleanValue REMODEL_FRIEND;
    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        REMODEL_FRIEND = builder
                .comment("Shove FRIEND into a woodchipper.")
                .define("remodelfriend", false);

        SPEC = builder.build();
    }
}
