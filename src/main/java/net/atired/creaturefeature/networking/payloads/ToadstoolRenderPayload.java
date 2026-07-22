package net.atired.creaturefeature.networking.payloads;

import dev.ryanhcode.sable.Sable;
import io.netty.buffer.ByteBuf;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.entity.ToadstoolEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record ToadstoolRenderPayload(int playerID,int x,int z) implements CustomPacketPayload {

    public static final Type<ToadstoolRenderPayload> TYPE = new Type<>(CreatureFeature.getId("toadstooled"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, ToadstoolRenderPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ToadstoolRenderPayload::playerID,
            ByteBufCodecs.INT,
            ToadstoolRenderPayload::x,
            ByteBufCodecs.INT,
            ToadstoolRenderPayload::z,
            ToadstoolRenderPayload::new
    );
    public void handleData(final IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                Level test = context.player().level();
                Entity entity =test.getEntity(playerID);
                if(entity instanceof ToadstoolEntity toadstoolEntity&&toadstoolEntity.myClientCube==null){
                    toadstoolEntity.myClientCube= Sable.HELPER.getContainingClient(new ChunkPos(x,z));
                    System.out.println(toadstoolEntity.myClientCube);
                }
            });
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}