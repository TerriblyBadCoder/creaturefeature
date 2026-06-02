package net.atired.creaturefeature.networking.payloads;

import io.netty.buffer.ByteBuf;
import net.atired.creaturefeature.CreatureFeature;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record VelSyncPayload( int playerID,double x,double y,double z) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<VelSyncPayload> TYPE = new CustomPacketPayload.Type<>(CreatureFeature.getId("velsync"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, VelSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            VelSyncPayload::playerID,
            ByteBufCodecs.DOUBLE,
            VelSyncPayload::x,
            ByteBufCodecs.DOUBLE,
            VelSyncPayload::y,
            ByteBufCodecs.DOUBLE,
            VelSyncPayload::z,
            VelSyncPayload::new
    );
    public void handleData(final IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> {
                Level test = context.player().level();
                Entity entity =test.getEntity(playerID);
                entity.addDeltaMovement(new Vec3(x,y,z));
            });
        }
    }
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}