package net.atired.creaturefeature.misc;

import net.atired.creaturefeature.blocks.RunicStoneBricksBlock;
import net.atired.creaturefeature.entity.MockingBirdEntity;
import net.atired.creaturefeature.entity.ToadstoolEntity;
import net.atired.creaturefeature.init.CFBlockInit;
import net.atired.creaturefeature.init.CFEntityInit;
import net.atired.creaturefeature.init.CFItemInit;
import net.atired.creaturefeature.networking.payloads.ToadstoolRenderPayload;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class SableCarrier {
    public static void initBlock(){
        CFBlockInit.RUNIC_STONE_BRICKS = CFBlockInit.registerBlock("runic_stone_bricks",
                () -> new RunicStoneBricksBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    }
    public static void initspawn(RegisterSpawnPlacementsEvent event){
        event.register(CFEntityInit.TOADSTOOL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ToadstoolEntity::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

    }
    public static void initEvent(PayloadRegistrar registrar){
        registrar.playToClient(
                ToadstoolRenderPayload.TYPE,
                ToadstoolRenderPayload.STREAM_CODEC,
                ToadstoolRenderPayload::handleData
        );
    }
    public static void initEgg(){
        CFItemInit.TOADSTOOL_EGG= CFItemInit.ITEMS.register("toadstool_spawn_egg", ()->new
                SpawnEggItem(CFEntityInit.TOADSTOOL.get(),0xabe4a9,0xad6cad,new  Item.Properties()));
    }
    public static void initEntity(){
        CFEntityInit.TOADSTOOL = CFEntityInit.ENTITIES.register("toadstool", () -> EntityType.Builder.of(ToadstoolEntity::new, MobCategory.MONSTER)
                .sized(1.2f, 0.9f).eyeHeight(0.78F).passengerAttachments(2.0125F).ridingOffset(-0.7F).clientTrackingRange(12)
                .build("toadstool"));
    }
}
