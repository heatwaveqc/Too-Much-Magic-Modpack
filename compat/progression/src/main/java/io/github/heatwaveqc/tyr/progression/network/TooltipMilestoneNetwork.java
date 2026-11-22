package io.github.heatwaveqc.tyr.progression.network;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.ClientTooltipMilestones;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/** Only the server may publish persistent personal valuation unlocks. */
public final class TooltipMilestoneNetwork {
    private TooltipMilestoneNetwork() {}

    public record State(boolean projecte, boolean replication) implements CustomPacketPayload {
        public static final Type<State> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID, "tooltip_milestones"));
        public static final StreamCodec<RegistryFriendlyByteBuf, State> CODEC =
            StreamCodec.composite(ByteBufCodecs.BOOL, State::projecte,
                                  ByteBufCodecs.BOOL, State::replication, State::new);

        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(State.TYPE, State.CODEC, (state, context) ->
            context.enqueueWork(() ->
                ClientTooltipMilestones.accept(state.projecte(), state.replication())));
    }

    public static void send(ServerPlayer player, boolean stone, boolean ingot) {
        PacketDistributor.sendToPlayer(player, new State(stone, ingot));
    }
}
