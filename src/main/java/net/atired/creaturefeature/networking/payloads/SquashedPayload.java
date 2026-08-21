package net.atired.creaturefeature.networking.payloads;

import io.netty.buffer.ByteBuf;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SquashedPayload(int playerID) implements CustomPacketPayload {

    public static final Type<SquashedPayload> TYPE = new Type<>(CreatureFeature.getId("squashed"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, SquashedPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SquashedPayload::playerID,
            SquashedPayload::new
    );
    public void handleData(final IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                Level test = context.player().level();
                if(test.getEntity(playerID) instanceof LivingEntityGoopAccessor accessor){
                    accessor.setSquashed(0.8f);
                }
            });
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}