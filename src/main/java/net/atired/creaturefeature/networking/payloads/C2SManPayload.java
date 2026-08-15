package net.atired.creaturefeature.networking.payloads;

import io.netty.buffer.ByteBuf;
import net.atired.creaturefeature.CreatureFeature;
import net.atired.creaturefeature.accessors.PlayerBrainrotAccessor;
import net.atired.creaturefeature.init.CFAchievements;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record C2SManPayload(int playerID) implements CustomPacketPayload {

    public static final Type<C2SManPayload> TYPE = new Type<>(CreatureFeature.getId("c2sman"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, C2SManPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            C2SManPayload::playerID,
            C2SManPayload::new
    );
    public void handleData(final IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                Level test = context.player().level();
                if(test!=null&&test.getEntity(playerID) instanceof ServerPlayer serverPlayer){
                    if(serverPlayer instanceof PlayerBrainrotAccessor accessor){
                        accessor.setManCan(true);
                    }
                    CFAchievements.NOBODY.get().trigger(serverPlayer);
                }
            });
        }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}