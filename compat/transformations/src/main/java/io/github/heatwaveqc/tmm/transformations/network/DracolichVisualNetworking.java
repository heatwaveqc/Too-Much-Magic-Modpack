package io.github.heatwaveqc.tmm.transformations.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class DracolichVisualNetworking {
    private DracolichVisualNetworking() { }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(DracolichVisualPayload.TYPE, DracolichVisualPayload.CODEC,
                (payload, context) -> context.enqueueWork(() ->
                        DracolichVisualState.apply(payload.playerId(), payload.active())));
    }
}
