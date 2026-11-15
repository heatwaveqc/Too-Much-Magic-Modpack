package io.github.heatwaveqc.tmm.transformations.network;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Server-to-client only; no client may request or grant lichdom using this. */
public record DracolichVisualPayload(UUID playerId, boolean active) implements CustomPacketPayload {
    public static final Type<DracolichVisualPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("tmm_transformations", "dracolich_visual"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DracolichVisualPayload> CODEC =
            StreamCodec.of((buffer, value) -> {
                buffer.writeUUID(value.playerId());
                buffer.writeBoolean(value.active());
            }, buffer -> new DracolichVisualPayload(buffer.readUUID(), buffer.readBoolean()));

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
