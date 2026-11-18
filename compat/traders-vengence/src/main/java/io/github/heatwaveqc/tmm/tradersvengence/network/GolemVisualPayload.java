package io.github.heatwaveqc.tmm.tradersvengence.network;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/** Server-to-client visual snapshots only; never accept client combat requests. */
public record GolemVisualPayload(UUID id, float angle, float speed, boolean firing) implements CustomPacketPayload {
    public static final Type<GolemVisualPayload> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath("traders_vengence", "golem_minigun_visual"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GolemVisualPayload> CODEC =
        StreamCodec.of((buffer, value) -> {
            buffer.writeUUID(value.id());
            buffer.writeFloat(value.angle());
            buffer.writeFloat(value.speed());
            buffer.writeBoolean(value.firing());
        }, buffer -> new GolemVisualPayload(buffer.readUUID(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean()));
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("2").playToClient(TYPE, CODEC, (payload, context) ->
            context.enqueueWork(() -> GolemVisualState.apply(payload)));
    }
}
