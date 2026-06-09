package net.atired.creaturefeature;

import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.atired.creaturefeature.entity.*;
import net.atired.creaturefeature.init.CFBlockInit;
import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFItemInit;
import net.atired.creaturefeature.networking.payloads.C2SVelSyncPayload;
import net.atired.creaturefeature.networking.payloads.DeAmpPayload;
import net.atired.creaturefeature.networking.payloads.RabiesPayload;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = CreatureFeature.MODID)
public class CFEvents {
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
            //cannonball crab egg
            //Yet I egg
            event.accept(CFItemInit.FIEND_EGG.get());

        }
        if(event.getTabKey()==CreativeModeTabs.COMBAT){
            event.insertBefore(Items.MACE.getDefaultInstance(),CFItemInit.HEAVY_AXE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);;
        }
        if(event.getTabKey()==CreativeModeTabs.INGREDIENTS){
            event.insertBefore(Items.SLIME_BALL.getDefaultInstance(),CFItemInit.MINEDFLAYER_GOOP.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.BREEZE_ROD.getDefaultInstance(),CFItemInit.BLITZ_ROD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(Items.GUNPOWDER.getDefaultInstance(),CFItemInit.SLEEPING_POWDER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if(event.getTabKey()==CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.insertBefore(InstrumentItem.create(Items.GOAT_HORN,  BuiltInRegistries.INSTRUMENT.getTag(InstrumentTags.GOAT_HORNS).get().get(0)),CFItemInit.VERTIGO_HORN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.MUSIC_DISC_13.getDefaultInstance(),CFItemInit.NOTHING_DISC.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertBefore(Items.ENDER_PEARL.getDefaultInstance(),CFItemInit.BOUQUET.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.ENDER_PEARL.getDefaultInstance(),CFItemInit.MURKY_PEARL.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if(event.getTabKey()==CreativeModeTabs.FUNCTIONAL_BLOCKS){
            event.insertBefore(Items.LODESTONE.getDefaultInstance(), CFBlockInit.MINEDFLAYER_JELLY.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
        if(event.getTabKey()==CreativeModeTabs.FOOD_AND_DRINKS){
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
        registrar.playToClient(
                RabiesPayload.TYPE,
                RabiesPayload.STREAM_CODEC,
                RabiesPayload::handleData
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

        event.register(CFEntityInit.FIEND.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FiendEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

    }
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
    }
}
