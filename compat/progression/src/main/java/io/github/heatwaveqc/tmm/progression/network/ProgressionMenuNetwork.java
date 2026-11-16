package io.github.heatwaveqc.tmm.progression.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import io.github.heatwaveqc.tmm.progression.client.ProgressionScreen;
import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.core.ProgressionMath;
import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.state.Track;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Server-authoritative graphical progression screen. The client receives a
 * read-only, bounded projection of the REAL loaded server path + player ledger;
 * it may only request a validated purchase or refund via the existing API.
 * Neither XP, skill ownership, costs nor ranks arrive from the client.
 */
public final class ProgressionMenuNetwork {
    private ProgressionMenuNetwork() {}

    public record Request(String path) implements CustomPacketPayload {
        public static final Type<Request> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID, "menu_request"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Request> CODEC =
                StreamCodec.composite(ByteBufCodecs.STRING_UTF8, Request::path, Request::new);
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record Mutate(String path, String action, String skill) implements CustomPacketPayload {
        public static final Type<Mutate> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID, "menu_mutate"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Mutate> CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8, Mutate::path,
                        ByteBufCodecs.STRING_UTF8, Mutate::action,
                        ByteBufCodecs.STRING_UTF8, Mutate::skill,
                        Mutate::new);
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record Snapshot(String json) implements CustomPacketPayload {
        public static final Type<Snapshot> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID, "menu_snapshot"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Snapshot> CODEC =
                StreamCodec.composite(ByteBufCodecs.STRING_UTF8, Snapshot::json, Snapshot::new);
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    private static final Map<UUID, Long> LAST_MUTATION = new HashMap<>();

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToServer(Request.TYPE, Request.CODEC, (payload, context) -> {
                    if (!(context.player() instanceof ServerPlayer player)) return;
                    context.enqueueWork(() -> send(player, payload.path()));
                })
                .playToServer(Mutate.TYPE, Mutate.CODEC, (payload, context) -> {
                    if (!(context.player() instanceof ServerPlayer player)) return;
                    context.enqueueWork(() -> {
                        if (player.isSpectator() || !validId(payload.path())
                                || payload.skill().length() > 128) return;
                        long tick = player.serverLevel().getGameTime();
                        Long old = LAST_MUTATION.get(player.getUUID());
                        if (old != null && tick >= old && tick - old < 2) return;
                        LAST_MUTATION.put(player.getUUID(), tick);
                        boolean changed = switch (payload.action()) {
                            case "buy" -> validId(payload.skill()) &&
                                    ProgressionApi.buy(player, payload.path(), payload.skill());
                            case "respec" -> ProgressionApi.respec(player, payload.path());
                            default -> false;
                        };
                        // A rejected mutation also receives a fresh projection
                        // to explain the real server-side prerequisite.
                        send(player, payload.path());
                    });
                })
                .playToClient(Snapshot.TYPE, Snapshot.CODEC, (payload, context) ->
                        context.enqueueWork(() -> ProgressionScreen.receive(payload.json())));
    }

    private static boolean validId(String value) {
        return value != null && value.length() <= 128 &&
                value.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");
    }

    private static JsonArray strings(Iterable<String> values) {
        JsonArray array = new JsonArray();
        for (String value : values) array.add(value);
        return array;
    }

    private static void send(ServerPlayer player, String pathId) {
        if (!validId(pathId)) return;
        Definition.Path path = PathRegistry.get(pathId);
        if (path == null) return;
        Track state = ProgressionApi.track(player, pathId);
        JsonObject json = new JsonObject();
        json.addProperty("path", pathId);
        json.addProperty("joined", state.joined());
        json.addProperty("rank", state.rank());
        json.addProperty("max_rank", path.maxRank());
        json.addProperty("xp", state.xp());
        json.addProperty("points", state.points());
        long threshold = state.rank() < path.rankXp().size()
                ? path.rankXp().get(state.rank()) : path.rankXp().getLast();
        json.addProperty("next_xp", threshold);
        json.add("completed_milestones", strings(state.milestones()));
        JsonArray skills = new JsonArray();
        for (Definition.Skill skill : path.skills()) {
            JsonObject item = new JsonObject();
            int owned = state.skillRank(skill.id());
            item.addProperty("id", skill.id());
            item.addProperty("owned", owned);
            item.addProperty("maximum", skill.maxRank());
            item.addProperty("required_rank", skill.requiredRank());
            item.addProperty("auto", !skill.autoRanks().isEmpty());
            item.addProperty("price", skill.price(owned));
            item.addProperty("choice_group", skill.choiceGroup());
            item.addProperty("buyable", ProgressionMath.buy(path, state, skill.id()) != state);
            item.add("requires", strings(skill.requiresSkills()));
            item.add("requires_any", strings(skill.requiresAnySkills()));
            item.add("milestones", strings(skill.requiresMilestones()));
            JsonArray effects = new JsonArray();
            for (Definition.Effect fx : skill.effects()) effects.add(fx.type());
            item.add("effects", effects);
            skills.add(item);
        }
        json.add("skills", skills);
        PacketDistributor.sendToPlayer(player, new Snapshot(json.toString()));
    }

    public static void forget(ServerPlayer player) {
        LAST_MUTATION.remove(player.getUUID());
    }
}
