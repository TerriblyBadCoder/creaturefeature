package net.atired.creaturefeature;

import net.atired.creaturefeature.entity.*;
import net.atired.creaturefeature.init.*;
import net.atired.creaturefeature.networking.payloads.VelSyncPayload;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ClientPayloadHandler;
import net.neoforged.neoforge.network.handlers.ServerPayloadHandler;
import net.neoforged.neoforge.network.handling.ClientPayloadContext;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreatureFeature.MODID)
public class CreatureFeature {
    public static final String MODID = "creaturefeature";
    public CreatureFeature(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::createEntityAttributes);
        CFRecipeSerialisers.RECIPES.register(modEventBus);
        CFSoundInit.SOUND_EVENTS.register(modEventBus);
        CFParticleInit.PARTICLE_TYPES.register(modEventBus);
        CFEntityInit.ENTITIES.register(modEventBus);
        CFDataComponentTypeInit.DATA_COMPONENT_TYPES.register(modEventBus);
        CFItemInit.ITEMS.register(modEventBus);
        CFBlockInit.BLOCKS.register(modEventBus);
        CFMobEffectInit.MOB_EFFECTS.register(modEventBus);


    }
    public static ResourceLocation getId(String string){
        return ResourceLocation.fromNamespaceAndPath(MODID,string);
    }
    private void commonSetup(FMLCommonSetupEvent event) {

    }

    public void createEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(CFEntityInit.MINDS.get(), MindsEntity.createMindsAttributes().build());
        event.put(CFEntityInit.MACHINATION.get(), MachinationEntity.createMachinationAttributes().build());
        event.put(CFEntityInit.SINISTER.get(), SinisterEntity.createSinisterAttributes().build());
        event.put(CFEntityInit.BEAUTY.get(), BeautyEntity.createBeautyAttributes().build());
        event.put(CFEntityInit.BLITZ.get(), BlitzEntity.createBlitzAttributes().build());
        event.put(CFEntityInit.EEPER.get(), EeperEntity.createEeperAttributes().build());
        event.put(CFEntityInit.MINEDFLAYER.get(), MinedFlayerEntity.createFlayerAttributes().build());
        event.put(CFEntityInit.NOTHING.get(), NoThingEntity.createNothingAttributes().build());
        event.put(CFEntityInit.CANARY.get(), CanaryEntity.createCanaryAttributes().build());
        event.put(CFEntityInit.CANARY_PART.get(), CanaryPart.createCanaryAttributes().build());
        event.put(CFEntityInit.VERTIGO.get(), VertigoEntity.createVertigoAttributes().build());
        event.put(CFEntityInit.CANNONBALL_CRAB.get(), CannonballCrabEntity.createCrabAttributes().build());
        event.put(CFEntityInit.FIEND.get(), FiendEntity.createFiendAttributes().build());
        event.put(CFEntityInit.FEND.get(), FendEntity.createFendAttributes().build());
        event.put(CFEntityInit.FRIEND.get(), FriendEntity.createFriendAttributes().build());
    }

}
