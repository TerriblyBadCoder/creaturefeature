package net.atired.creaturefeature.init;

import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.items.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CFItemInit {
    public static final DeferredRegister.Items ITEMS =DeferredRegister.createItems(CreatureFeature.MODID);
    public static final DeferredItem<Item> MINDS_EGG = ITEMS.register("minds_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.MINDS.get(),0x6a2a51,0xd17989,new  Item.Properties()));
    public static final DeferredItem<Item> SINISTER_EGG = ITEMS.register("sinister_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.SINISTER.get(),0xffffff,0xf1e49f,new  Item.Properties()));
    public static final DeferredItem<Item> MACHINATION_EGG = ITEMS.register("machination_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.MACHINATION.get(),0xf1e49f,0x6f5adb,new  Item.Properties()));
    public static final DeferredItem<Item> BEAUTY_EGG = ITEMS.register("beauty_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.BEAUTY.get(),0x629062,0x91344d,new  Item.Properties()));


    public static final DeferredItem<Item> EEPER_EGG = ITEMS.register("eeper_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.EEPER.get(),0xd1e198,0x5d9b98,new  Item.Properties()));
    public static final DeferredItem<Item> BLITZ_EGG = ITEMS.register("blitz_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.BLITZ.get(),0xeec1db,0xd442dd,new  Item.Properties()));
    public static final DeferredItem<Item> MINEDFLAYER_EGG = ITEMS.register("minedflayer_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.MINEDFLAYER.get(),0xe7b9d1,0x75a6cd,new  Item.Properties()));
    public static final DeferredItem<Item> NOTHING_EGG = ITEMS.register("nothing_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.NOTHING.get(),0xdfce9b,0xdfce9b,new  Item.Properties()));


    public static final DeferredItem<Item> FIEND_EGG = ITEMS.register("fiend_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.FIEND.get(),0xf58dfc,0xdf52f2,new  Item.Properties()));



    public static final DeferredItem<Item> CANARY_EGG = ITEMS.register("canary_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.CANARY.get(),0xaab5b3,0x99e550,new  Item.Properties()));
    public static final DeferredItem<Item> VERTIGO_EGG = ITEMS.register("vertigo_spawn_egg", ()->new
            SpawnEggItem(CFEntityInit.VERTIGO.get(),0xa97eb6,0x736967,new  Item.Properties()));


    public static ResourceKey<JukeboxSong> NOTHING = ResourceKey.create(Registries.JUKEBOX_SONG, CreatureFeature.getId("nothing"));
    public static final DeferredItem<Item> NOTHING_DISC = ITEMS.register(
            "music_disc_nothing",
            ()->new Item((new Item.Properties()).stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(NOTHING))
    );
    public static final DeferredItem<Item> BOUQUET = ITEMS.register(
            "bouquet",
            ()->new BouquetItem(new Item.Properties().durability(8).stacksTo(1))
    );
    public static final FoodProperties BLIGHTED_BRAIN_FOOD = (new FoodProperties.Builder()).nutrition(11).saturationModifier(0.3F)
            .effect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.8F)
            .effect(new MobEffectInstance(MobEffects.INFESTED, 400, 0), 0.8F)
            .effect(new MobEffectInstance(MobEffects.OOZING, 200, 0), 0.8F).build();

    public static final DeferredItem<Item> BLIGHTED_BRAIN = ITEMS.register(
            "blighted_brain",
            ()->new Item(new Item.Properties().food(BLIGHTED_BRAIN_FOOD))
    );
    public static final DeferredItem<Item> SLEEPING_POWDER = ITEMS.register(
            "sleeping_powder",
            ()->new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> BLITZ_ROD = ITEMS.register(
            "blitz_rod",
            ()->new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> MURKY_PEARL = ITEMS.register(
            "murky_pearl",
            ()->new MurkyPearlItem(new Item.Properties())
    );
    public static final DeferredItem<Item> OPEN_MIND = ITEMS.register(
            "open_mind",
            ()->new OpenMindItem(new Item.Properties().stacksTo(1))
    );
    public static final DeferredItem<Item> VERTIGO_HORN = ITEMS.register(
            "vertigo_horn",
            ()->new VertigoHornItem((new Item.Properties()).stacksTo(1), InstrumentTags.GOAT_HORNS)
    );
    public static final DeferredItem<Item> MINEDFLAYER_GOOP = ITEMS.register(
            "minedflayer_goop",
            ()->new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> HEAVY_AXE = ITEMS.register(
            "heavy_axe",
            ()->new HeavyAxeItem(Tiers.DIAMOND, (new Item.Properties()).attributes(AxeItem.createAttributes(Tiers.DIAMOND, 5.0F, -3.6F)).rarity(Rarity.EPIC))
    );

}
