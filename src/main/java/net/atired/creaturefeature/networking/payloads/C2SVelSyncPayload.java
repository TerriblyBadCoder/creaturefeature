package net.atired.creaturefeature.networking.payloads;

import io.netty.buffer.ByteBuf;
import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record C2SVelSyncPayload(int playerID, double x, double y, double z) implements CustomPacketPayload {

    public static final Type<C2SVelSyncPayload> TYPE = new Type<>(CreatureFeature.getId("c2svelsync"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, C2SVelSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            C2SVelSyncPayload::playerID,
            ByteBufCodecs.DOUBLE,
            C2SVelSyncPayload::x,
            ByteBufCodecs.DOUBLE,
            C2SVelSyncPayload::y,
            ByteBufCodecs.DOUBLE,
            C2SVelSyncPayload::z,
            C2SVelSyncPayload::new
    );
    public void handleData(final IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                Level test = context.player().level();
                if(test.getEntity(playerID)==context.player()){
                    context.player().addDeltaMovement(new Vec3(x,y,z));
                }
            });
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}