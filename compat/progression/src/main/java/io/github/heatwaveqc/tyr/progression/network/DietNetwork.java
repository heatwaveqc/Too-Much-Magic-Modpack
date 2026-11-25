package io.github.heatwaveqc.tyr.progression.network;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.ClientDiet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/** Server-to-client previews only; never a C2S dietary permission channel. */
public final class DietNetwork {
    private DietNetwork() {}
    private static final AtomicInteger NEXT_TRANSFER = new AtomicInteger();

    /** Existing short-lived beverage-selector preview lease. */
    public record State(String selectors) implements CustomPacketPayload {
        public static final Type<State> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID, "diet_state"));
        public static final StreamCodec<RegistryFriendlyByteBuf, State> CODEC =
            StreamCodec.composite(ByteBufCodecs.stringUtf8(8192),
                State::selectors, State::new);
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    /**
     * A coherent per-profile snapshot divided into small, bounded packets.
     * Nothing is applied on the client until all pieces of the transaction arrive.
     */
    public record PreviewChunk(int transfer, int index, int total, String rows)
            implements CustomPacketPayload {
        public static final Type<PreviewChunk> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID, "food_preview"));
        public static final StreamCodec<RegistryFriendlyByteBuf, PreviewChunk> CODEC =
            StreamCodec.composite(
                ByteBufCodecs.VAR_INT, PreviewChunk::transfer,
                ByteBufCodecs.VAR_INT, PreviewChunk::index,
                ByteBufCodecs.VAR_INT, PreviewChunk::total,
                ByteBufCodecs.stringUtf8(4096), PreviewChunk::rows,
                PreviewChunk::new);
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToClient(State.TYPE, State.CODEC, (payload, context) ->
            context.enqueueWork(() -> ClientDiet.accept(payload.selectors())));
        registrar.playToClient(PreviewChunk.TYPE, PreviewChunk.CODEC, (payload, context) ->
            context.enqueueWork(() -> ClientDiet.acceptPreviewChunk(
                payload.transfer(), payload.index(), payload.total(), payload.rows())));
    }

    public static void send(ServerPlayer player, String selectors) {
        PacketDistributor.sendToPlayer(player, new State(selectors));
    }

    public static void sendPreview(ServerPlayer player,
            Map<String, FoodPreviewWire.Entry> values) {
        var chunks = FoodPreviewWire.chunks(values);
        int transfer = NEXT_TRANSFER.incrementAndGet();
        for (int index = 0; index < chunks.size(); index++)
            PacketDistributor.sendToPlayer(player,
                new PreviewChunk(transfer, index, chunks.size(), chunks.get(index)));
    }
}
