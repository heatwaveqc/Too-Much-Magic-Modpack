package io.github.heatwaveqc.tyr.progression.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.ProgressionScreen;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Track;
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
                StreamCodec.composite(ByteBufCodecs.stringUtf8(262144), Snapshot::json, Snapshot::new);
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
                            case "activate" -> validId(payload.skill()) && io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.request(player,payload.path(),payload.skill());
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
        if(pathId.isEmpty()||!PathRegistry.all().containsKey(pathId)){
            pathId=PathRegistry.all().keySet().stream().sorted()
                .filter(id->ProgressionApi.track(player,id).joined()&&PathRegistry.eligible(player,id)).findFirst()
                .orElseGet(()->PathRegistry.all().keySet().stream().sorted().findFirst().orElse(""));
        }
        if (!validId(pathId)) return;
        Definition.Path path = PathRegistry.get(pathId);
        if (path == null) return;
        Track state = ProgressionApi.track(player, pathId);
        JsonObject json = new JsonObject();
        json.addProperty("path", pathId);
        json.addProperty("title",PathRegistry.presentation(pathId,"").title());
        var channel=PathRegistry.channel(pathId);
        json.addProperty("xp_channel",channel==null?"":channel.id());
        json.add("paths",strings(PathRegistry.all().keySet().stream().sorted().toList()));
        json.addProperty("joined", state.joined());
        json.addProperty("eligible",PathRegistry.eligible(player,pathId));
        json.addProperty("rank", state.rank());
        json.addProperty("max_rank", channel.maxRank());
        json.addProperty("xp", state.xp());
        json.addProperty("points", state.points());
        var curve=channel.magic()?java.util.List.of(0L):channel.source().rankXp();
        long threshold=state.rank()<curve.size()?curve.get(state.rank()):curve.getLast();
        if(channel!=null && channel.magic()) {
            var wallet=player.getData(io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments.LEDGER.get()).wallet(channel.id());
            long start=wallet==null?0:wallet.xp()-(long)wallet.magicProgress();
            threshold=start>Long.MAX_VALUE-1000?Long.MAX_VALUE:start+1000;
            json.addProperty("magic_channel",true);
        }
        json.addProperty("next_xp", threshold);
        long currentStart = state.rank() > 0
                ? curve.get(Math.min(state.rank()-1,curve.size()-1))
                : 0;
        if(channel!=null && channel.magic()) currentStart=Math.max(0,threshold-1000);
        json.addProperty("rank_start_xp", currentStart);
        var ledger=player.getData(io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments.LEDGER.get());
        JsonArray wallets=new JsonArray();
        for(var system:PathRegistry.economies().values().stream().sorted(java.util.Comparator.comparing(io.github.heatwaveqc.tyr.progression.model.XpChannel::id)).toList()){
            var w=ledger.wallet(system.id());JsonObject row=new JsonObject();
            row.addProperty("id",system.id());row.addProperty("name",system.name());row.addProperty("point_name",system.pointName());
            row.addProperty("level",w==null?0:w.rank());row.addProperty("points",w==null?0:w.points());
            row.addProperty("xp",w==null?0:w.xp());wallets.add(row);
        }
        json.add("wallets",wallets);
        json.add("completed_milestones", strings(state.milestones()));
        JsonArray skills = new JsonArray();
        for (Definition.Skill skill : path.skills()) {
            JsonObject item = new JsonObject();
            int owned = state.skillRank(skill.id());
            item.addProperty("id", skill.id());
            var present=PathRegistry.presentation(pathId,skill.id());
            item.addProperty("title",present.title());item.addProperty("description",present.description());item.addProperty("lane",present.lane());
            item.addProperty("owned", owned);
            item.addProperty("maximum", skill.maxRank());
            item.addProperty("required_rank", skill.nextRequiredRank(owned));
            item.addProperty("eligible",PathRegistry.skillEligible(player,pathId,skill.id()));
            item.addProperty("auto", !skill.autoRanks().isEmpty());
            var binding=PathRegistry.config().skills().get(new io.github.heatwaveqc.tyr.progression.data.ChannelConfig.SkillKey(pathId,skill.id()));
            item.addProperty("currency",binding.currency().id());
            item.addProperty("permanent",binding.permanent());
            item.addProperty("point_name",binding.currency().pointName());
            item.addProperty("level_system",binding.levelSystem().name());
            item.addProperty("price", skill.price(owned));
            var activation=PathRegistry.activation(pathId,skill.id());
            if(activation!=null){
                item.addProperty("activation_mode",activation.mode());
                item.addProperty("activation_active",io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.active(player,pathId,skill.id()));
                item.addProperty("activation_ready",io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.canRequest(player,pathId,skill.id()));
                item.addProperty("activation_remaining",io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.remaining(player,pathId,skill.id()));
            }
            item.addProperty("choice_group", skill.choiceGroup());
            item.addProperty("buyable", ProgressionApi.canBuy(player,pathId,skill.id()));
            item.add("requires", strings(skill.requiresSkills()));
            item.add("requires_any", strings(skill.requiresAnySkills()));
            item.add("milestones", strings(skill.requiresMilestones()));
            JsonArray effects = new JsonArray();
            for (Definition.Effect fx : skill.effects()) effects.add(fx.type());
            item.add("effects", effects);
            skills.add(item);
        }
        json.add("skills", skills);
        String payload=json.toString();
        if(payload.length()>262144){ProgressionMod.LOGGER.warn("Tree snapshot too large: {}",pathId);return;}
        PacketDistributor.sendToPlayer(player, new Snapshot(payload));
    }

    public static void forget(ServerPlayer player) {
        LAST_MUTATION.remove(player.getUUID());
    }
}
