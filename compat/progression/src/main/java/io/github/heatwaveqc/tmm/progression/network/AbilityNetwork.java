package io.github.heatwaveqc.tmm.progression.network;

import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import io.github.heatwaveqc.tmm.progression.runtime.ClassAbilities;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** No XP, skill, position, rank, or arbitrary effect data is ever accepted from the client. */
public final class AbilityNetwork {
    private AbilityNetwork() {}
    private static final Map<UUID,Long> LAST = new HashMap<>();
    public record Action(String action) implements CustomPacketPayload {
        public static final Type<Action> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID,"ability_action"));
        public static final StreamCodec<RegistryFriendlyByteBuf,Action> CODEC =
            StreamCodec.composite(ByteBufCodecs.STRING_UTF8,Action::action,Action::new);
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(Action.TYPE,Action.CODEC,(payload,context) -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            context.enqueueWork(() -> {
                if (player.isSpectator() || player.isCreative()) return;
                long now = player.serverLevel().getGameTime();
                long old = LAST.getOrDefault(player.getUUID(),Long.MIN_VALUE);
                if (old != Long.MIN_VALUE && now >= old && now - old < 2) return;
                LAST.put(player.getUUID(),now);
                String action = payload.action();
                boolean ok = switch (action) {
                    case "blink" -> ClassAbilities.blink(player);
                    case "armor" -> ClassAbilities.toggle(player,"armor");
                    case "treasure" -> ClassAbilities.toggle(player,"treasure");
                    case "glide" -> ClassAbilities.toggle(player,"glide");
                    case "ordeal" -> ClassAbilities.toggle(player,"ordeal");
                    case "enhanced_night_vision","lava_vision" -> io.github.heatwaveqc.tmm.progression.runtime.VisionRuntime.toggle(player,action);
                    default -> false;
                };
                if (ok && !action.equals("blink"))
                    player.displayClientMessage(Component.literal(action + ": " +
                        (action.equals("enhanced_night_vision")||action.equals("lava_vision")?
                        io.github.heatwaveqc.tmm.progression.runtime.VisionRuntime.mode(player,action):ClassAbilities.mode(player,action))),true);
            });
        });
    }
    public static void forget(ServerPlayer p) { LAST.remove(p.getUUID()); }
}
