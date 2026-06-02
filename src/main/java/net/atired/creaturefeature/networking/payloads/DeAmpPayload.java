package net.atired.creaturefeature.networking.payloads;

import io.netty.buffer.ByteBuf;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.accessors.LivingEntityGoopAccessor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DeAmpPayload(int playerID,boolean should) implements CustomPacketPayload {

    public static final Type<DeAmpPayload> TYPE = new Type<>(CreatureFeature.getId("deamp"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, DeAmpPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            DeAmpPayload::playerID,
            ByteBufCodecs.BOOL,
            DeAmpPayload::should,
            DeAmpPayload::new
    );
    public void handleData(final IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                Level test = context.player().level();
                Entity entity =test.getEntity(playerID);
                if(entity instanceof LivingEntityGoopAccessor accessor){
                    accessor.setFallDamageAmped(should);
                }
            });
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}