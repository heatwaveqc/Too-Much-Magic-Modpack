package io.github.heatwaveqc.tyr.progression.network;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.ClientPassivePowers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * Client receives current scoped power IDs only; it never sends grant/revoke,
 * owner UUID, skill levels or permission booleans back to the server.
 */
public final class PassivePowerNetwork {
    private PassivePowerNetwork() {}

    public record Snapshot(String dimension, String active,String mutations) implements CustomPacketPayload {
        public static final Type<Snapshot> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID, "passive_powers"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Snapshot> CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.stringUtf8(128), Snapshot::dimension,
                        ByteBufCodecs.stringUtf8(8192), Snapshot::active,
                        ByteBufCodecs.stringUtf8(io.github.heatwaveqc.tyr.progression.core.MutationPowerPolicy.MAX_ENCODED), Snapshot::mutations,
                        Snapshot::new);
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("3").playToClient(Snapshot.TYPE, Snapshot.CODEC, (payload, context) ->
                context.enqueueWork(() ->
                        ClientPassivePowers.accept(payload.dimension(), payload.active(),payload.mutations())));
    }

    public static void send(ServerPlayer player, String dimension, String active,String mutations) {
        PacketDistributor.sendToPlayer(player, new Snapshot(dimension, active,mutations));
    }
}
