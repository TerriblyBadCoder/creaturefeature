package net.atired.creaturefeature.networking.payloads;

import io.netty.buffer.ByteBuf;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GasLeakPayload(int playerID,float damageAmp) implements CustomPacketPayload {

    public static final Type<GasLeakPayload> TYPE = new Type<>(CreatureFeature.getId("gasdamageamp"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, GasLeakPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            GasLeakPayload::playerID,
            ByteBufCodecs.FLOAT,
            GasLeakPayload::damageAmp,
            GasLeakPayload::new
    );
    public void handleData(final IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                Level test = context.player().level();
                if(context.player() instanceof PlayerBrainrotAccessor accessor){
                    accessor.setDelayedDamage(damageAmp+0.1f);
                }
            });
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}