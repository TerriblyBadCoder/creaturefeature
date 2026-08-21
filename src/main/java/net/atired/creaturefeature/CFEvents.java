package net.atired.creaturefeature;

import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.atired.creaturefeature.entity.*;
import net.atired.creaturefeature.init.CFBlockInit;
import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFItemInit;
import net.atired.creaturefeature.misc.SableCarrier;
import net.atired.creaturefeature.networking.payloads.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = CreatureFeature.MODID)
public class CFEvents {

    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(CFItemInit.SOLAR_SHARD.get())) {
            event.setBurnTime(1600*8);
        }
    }
    @SubscribeEvent // on the mod event bus
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(CFItemInit.MINDS_EGG.get());
            event.accept(CFItemInit.BEAUTY_EGG.get());
            event.accept(CFItemInit.SINISTER_EGG.get());
            event.accept(CFItemInit.MACHINATION_EGG.get());

            event.accept(CFItemInit.MINEDFLAYER_EGG.get());
            event.accept(CFItemInit.EEPER_EGG.get());
            event.accept(CFItemInit.BLITZ_EGG.get());
            event.accept(CFItemInit.NOTHING_EGG.get());

            event.accept(CFItemInit.CANARY_EGG.get());
            event.accept(CFItemInit.VERTIGO_EGG.get());
            event.accept(CFItemInit.DREAMWEAVER_EGG.get());
            event.accept(CFItemInit.RUNAWAY_EGG.get());

            event.accept(CFItemInit.FIEND_EGG.get());
            event.accept(CFItemInit.FEND_EGG.get());
            event.accept(CFItemInit.FRIEND_EGG.get());

            if(ModList.get().isLoaded("sable"))
            event.accept(CFItemInit.TOADSTOOL_EGG.get());
            event.accept(CFItemInit.MOCKINGBIRD_EGG.get());
            event.accept(CFItemInit.PATHOGEN_EGG.get());
            event.accept(CFItemInit.BLOSSOM_EGG.get());
            event.accept(CFItemInit.SAINT_SOLIS_EGG.get());

            event.accept(CFItemInit.DETRITUS_EGG.get());
            event.accept(CFItemInit.STAINED_GLASS_EGG.get());
            event.accept(CFItemInit.COAT_OF_ARMS_EGG.get());

        }
        if(event.getTabKey()==CreativeModeTabs.FOOD_AND_DRINKS){
            event.insertAfter(Items.MILK_BUCKET.getDefaultInstance(),CFItemInit.THE_PILL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);;
        }
        if(event.getTabKey()==CreativeModeTabs.COMBAT){
            event.insertAfter(Items.ARROW.getDefaultInstance(),CFItemInit.VITRIC_ARROW.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);;
            event.insertBefore(Items.BOW.getDefaultInstance(),CFItemInit.FLINTLOCK.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);;
            event.insertBefore(Items.MACE.getDefaultInstance(),CFItemInit.HEAVY_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);;
        }
        if(event.getTabKey()==CreativeModeTabs.INGREDIENTS){
            event.insertBefore(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(),CFItemInit.BI_SCROLL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(),CFItemInit.PRIDE_SCROLL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(),CFItemInit.TRANS_SCROLL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE.getDefaultInstance(),CFItemInit.PAN_SCROLL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);


            event.insertBefore(Items.SLIME_BALL.getDefaultInstance(),CFItemInit.MINEDFLAYER_GOOP.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.BREEZE_ROD.getDefaultInstance(),CFItemInit.BLITZ_ROD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.NAUTILUS_SHELL.getDefaultInstance(),CFItemInit.CARAPACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.POPPED_CHORUS_FRUIT.getDefaultInstance(),CFItemInit.DREAM_SILK.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.HONEYCOMB.getDefaultInstance(),CFItemInit.DOWN_FEATHER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.HONEYCOMB.getDefaultInstance(),CFItemInit.ECTOPLASM.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GHAST_TEAR.getDefaultInstance(),CFItemInit.THINGAMABOB.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GHAST_TEAR.getDefaultInstance(),CFItemInit.FIENDISH_ESSENCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.LEATHER.getDefaultInstance(),CFItemInit.COAT_SCRAPS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.BONE.getDefaultInstance(),CFItemInit.LIVING_GLASS_SHARDS.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GUNPOWDER.getDefaultInstance(),CFItemInit.SLEEPING_POWDER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if(event.getTabKey()==CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.insertBefore(InstrumentItem.create(Items.GOAT_HORN,  BuiltInRegistries.INSTRUMENT.getTag(InstrumentTags.GOAT_HORNS).get().get(0)),CFItemInit.VERTIGO_HORN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.MUSIC_DISC_13.getDefaultInstance(),CFItemInit.NOTHING_DISC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.MUSIC_DISC_MALL.getDefaultInstance(),CFItemInit.NEW_AGE_NEVERMORE_DISC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.BRUSH.getDefaultInstance(),CFItemInit.OPEN_MIND.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.SADDLE.getDefaultInstance(),CFItemInit.DREAM_CATCHER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.SADDLE.getDefaultInstance(),CFItemInit.BACTERIUM_BALL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertBefore(Items.ENDER_PEARL.getDefaultInstance(),CFItemInit.BOUQUET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.ENDER_PEARL.getDefaultInstance(),CFItemInit.SOLAR_SHARD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.ENDER_PEARL.getDefaultInstance(),CFItemInit.MURKY_PEARL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }if(event.getTabKey()==CreativeModeTabs.BUILDING_BLOCKS) {
            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.MURKY_PEARL_BLOCK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.MURKY_PEARL_TILES.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.MURKY_PEARL_TILE_STAIRS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.DOWN_FEATHERS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.DOWN_FEATHERS_CARPET.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.MOSAIC_DOWN_FEATHERS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.MOSAIC_DOWN_FEATHERS_CARPET.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.FIENDISH_TILES.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.BLIND_FIENDISH_TILES.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.CARAPACE_BLOCK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.CARAPACE_BRICKS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertBefore(Items.GOLD_BLOCK.getDefaultInstance(), CFBlockInit.CARAPACE_BRICK_STAIRS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.PURPUR_BLOCK.getDefaultInstance(), CFBlockInit.DREAM_SILK_SPOOL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.PURPUR_BLOCK.getDefaultInstance(), CFBlockInit.SOLAR_BLOCK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.PURPUR_BLOCK.getDefaultInstance(), CFBlockInit.SOLAR_BRICKS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.PACKED_MUD.getDefaultInstance(), CFBlockInit.WALLPAPER.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

        }
        if(event.getTabKey()==CreativeModeTabs.FUNCTIONAL_BLOCKS){
            event.insertAfter(Items.JUKEBOX.getDefaultInstance(), CFBlockInit.DOOHICKEY.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.SOUL_CAMPFIRE.getDefaultInstance(), CFBlockInit.CARAPACE_BLOCK.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.LODESTONE.getDefaultInstance(), CFBlockInit.MINEDFLAYER_JELLY.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if(event.getTabKey()==CreativeModeTabs.FOOD_AND_DRINKS){
            event.insertBefore(Items.CAKE.getDefaultInstance(), CFItemInit.FIENDISH_SODA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertBefore(Items.SPIDER_EYE.getDefaultInstance(), CFItemInit.VERTIGO_CHUNK.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.ROTTEN_FLESH.getDefaultInstance(), CFItemInit.BLIGHTED_BRAIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if(event.getTabKey()==CreativeModeTabs.REDSTONE_BLOCKS){
            event.insertBefore(Items.TNT.getDefaultInstance(), CFBlockInit.EEP.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
    @SubscribeEvent // on the mod event bus
    public static void register(RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                VelSyncPayload.TYPE,
                VelSyncPayload.STREAM_CODEC,
                VelSyncPayload::handleData
        );
        registrar.playToClient(
                DeAmpPayload.TYPE,
                DeAmpPayload.STREAM_CODEC,
                DeAmpPayload::handleData
        );
        registrar.playToServer(
                C2SVelSyncPayload.TYPE,
                C2SVelSyncPayload.STREAM_CODEC,
                C2SVelSyncPayload::handleData
        );
        registrar.playToServer(
                C2SManPayload.TYPE,
                C2SManPayload.STREAM_CODEC,
                C2SManPayload::handleData
        );
        registrar.playToClient(
                RabiesPayload.TYPE,
                RabiesPayload.STREAM_CODEC,
                RabiesPayload::handleData
        );

        if(ModList.get().isLoaded("sable"))
            SableCarrier.initEvent(registrar);
        registrar.playToClient(
                PathogenPayload.TYPE,
                PathogenPayload.STREAM_CODEC,
                PathogenPayload::handleData
        );
        registrar.playToClient(
                SquashedPayload.TYPE,
                SquashedPayload.STREAM_CODEC,
                SquashedPayload::handleData
        );
        registrar.playToClient(
                GasLeakPayload.TYPE,
                GasLeakPayload.STREAM_CODEC,
                GasLeakPayload::handleData
        );
    }

    @SubscribeEvent // on the mod event bus
    public static void hurtMinds(AttackEntityEvent event) {
        if(event.getTarget() instanceof MindsEntityPart part){
            event.setCanceled(true);
            event.getEntity().attack(part.getParent());
        }
    }
    @SubscribeEvent
    public static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(CFEntityInit.EEPER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.NOTHING.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.BLITZ.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.MINEDFLAYER.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ((entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) -> {return blockPos.getY()>50;}), RegisterSpawnPlacementsEvent.Operation.AND);

        event.register(CFEntityInit.SINISTER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SinisterEntity::checkSinisterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.BEAUTY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BeautyEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.MINDS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MindsEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.MACHINATION.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MachinationEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

        event.register(CFEntityInit.VERTIGO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, VertigoEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.CANARY.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CanaryEntity::checkCanarySpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.CANNONBALL_CRAB.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CannonballCrabEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.DREAMWEAVER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DreamWeaverEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

        event.register(CFEntityInit.FIEND.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FiendEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.FRIEND.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FriendEntity::checkFriendSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.FEND.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FendEntity::checkFendSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

        if(ModList.get().isLoaded("sable")){
            SableCarrier.initspawn(event);
        }
        event.register(CFEntityInit.BLOSSOM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlossomEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.DETRITUS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DetritusEntity::checkDetritusSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.SAINT_SOLIS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SaintSolisEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.COATOFARMS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CoatOfArmsEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.STAINED_GLASS.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ((entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) -> {return Monster.isDarkEnoughToSpawn(serverLevelAccessor, blockPos, randomSource)&&blockPos.getY()>0&&Math.random()>0.4&&serverLevelAccessor.getHeight(Heightmap.Types.MOTION_BLOCKING,blockPos.getX(),blockPos.getZ())+6>blockPos.getY();}), RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(CFEntityInit.MOCKINGBIRD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MockingBirdEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

    }
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
    }
}
