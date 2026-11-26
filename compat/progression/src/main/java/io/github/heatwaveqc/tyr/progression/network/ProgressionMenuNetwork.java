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

    public record Mutate(String path, String action, String skill, String currency) implements CustomPacketPayload {
        public static final Type<Mutate> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID, "menu_mutate"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Mutate> CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8, Mutate::path,
                        ByteBufCodecs.STRING_UTF8, Mutate::action,
                        ByteBufCodecs.STRING_UTF8, Mutate::skill,
                        ByteBufCodecs.STRING_UTF8, Mutate::currency,
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

    private static final Map<UUID,String> LAST_VIEW = new HashMap<>();
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
                                || payload.skill().length() > 128
                                || payload.currency().length() > 128) return;
                        long tick = player.serverLevel().getGameTime();
                        Long old = LAST_MUTATION.get(player.getUUID());
                        if (old != null && tick >= old && tick - old < 2) return;
                        LAST_MUTATION.put(player.getUUID(), tick);
                        boolean changed = switch (payload.action()) {
                            case "buy" -> validId(payload.skill()) && validId(payload.currency())
                                    && ProgressionApi.buy(player, payload.path(), payload.skill(),payload.currency());
                            case "external_buy" -> "tyr_progression:core".equals(payload.path())
                                    && validId(payload.skill())
                                    && io.github.heatwaveqc.tyr.progression.runtime.external.ExternalRewardService.purchase(player,payload.skill());
                            case "respec" -> ProgressionApi.respec(player, payload.path());
                            case "activate","activate_key" -> validId(payload.skill()) && io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.request(player,payload.path(),payload.skill());
                            default -> false;
                        };
                        if (changed && ("buy".equals(payload.action()) || "respec".equals(payload.action()))) {
                            io.github.heatwaveqc.tyr.progression.runtime.EffectRuntime.reconcile(player);
                            io.github.heatwaveqc.tyr.progression.runtime.VirtualElytra.reconcile(player,true);
                        }
                        if("activate_key".equals(payload.action())){
                            if(changed)player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                                PathRegistry.presentation(payload.path(),payload.skill()).title()+": "+
                                (io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.active(player,payload.path(),payload.skill())?"on":"off")),true);
                            return; // hotkey does not reopen or request a large menu snapshot
                        }
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
        LAST_VIEW.put(player.getUUID(),pathId);
        Track state = ProgressionApi.track(player, pathId);
        JsonObject json = new JsonObject();
        json.addProperty("path", pathId);
        json.addProperty("title",PathRegistry.presentation(pathId,"").title());
        var channel=PathRegistry.channel(pathId);
        json.addProperty("xp_channel",channel==null?"":channel.id());
        json.add("paths",strings(PathRegistry.all().keySet().stream().sorted().toList()));
        json.addProperty("joined", state.joined());
        json.addProperty("eligible",PathRegistry.eligible(player,pathId));
        var ledger=player.getData(io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments.LEDGER.get());
        var catalog=PathRegistry.economyCatalog();
        var spec=channel==null?null:catalog.systems().get(channel.id());
        long level=spec==null?state.rank():
            io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.level(ledger.economy(),spec);
        json.addProperty("rank",level);
        if(PathRegistry.config().masteryStages().containsKey(pathId))
            json.addProperty("mastery_stage",state.rank());
        json.addProperty("max_rank",spec==null?channel.maxRank():spec.maxLevel());
        if(PathRegistry.config().masteryStages().containsKey(pathId))
            json.addProperty("max_mastery_stage",
                PathRegistry.config().masteryStages().get(pathId).maxStage());
        json.addProperty("xp",spec==null?java.math.BigDecimal.valueOf(state.xp()):
                ledger.economy().currentXp().getOrDefault(spec.id(),java.math.BigDecimal.ZERO));
        json.addProperty("points",spec==null?state.points():
                io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.available(ledger.economy(),spec));
        json.addProperty("debt",spec==null?0L:io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.debt(ledger.economy(),spec));
        long threshold;
        long currentStart;
        if(spec==null){
            var curve=channel.source().rankXp();
            threshold=state.rank()<curve.size()?curve.get(state.rank()):curve.getLast();
            currentStart=state.rank()>0?curve.get(Math.min(state.rank()-1,curve.size()-1)):0;
        } else {
            // Level is a display projection of current XP, not persisted rank.
            var start=spec.xpPerPoint().multiply(java.math.BigDecimal.valueOf(level));
            var next=spec.xpPerPoint().multiply(java.math.BigDecimal.valueOf(level+1L));
            var max=java.math.BigDecimal.valueOf(Long.MAX_VALUE);
            currentStart=start.compareTo(max)>=0?Long.MAX_VALUE:start.longValue();
            threshold=next.compareTo(max)>=0?Long.MAX_VALUE:next.longValue();
            json.addProperty("magic_channel",true);
        }
        json.addProperty("next_xp",threshold);
        json.addProperty("rank_start_xp",currentStart);
        JsonArray wallets=new JsonArray();
        for(var entry:catalog.systems().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()){
            String id=entry.getKey();
            var systemSpec=entry.getValue();
            var w=ledger.wallet(id);
            JsonObject row=new JsonObject();
            row.addProperty("id",id);
            var channelName=PathRegistry.economy(id);
            row.addProperty("name",channelName==null?id:channelName.name());
            row.addProperty("point_name",channelName==null?"Points":channelName.pointName());
            row.addProperty("level",io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy
                .level(ledger.economy(),systemSpec));
            row.addProperty("debt",io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.debt(ledger.economy(),systemSpec));
            row.addProperty("points",io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy
                .available(ledger.economy(),systemSpec));
            row.addProperty("xp",ledger.economy().currentXp()
                .getOrDefault(id,java.math.BigDecimal.ZERO));wallets.add(row);
        }
        json.add("wallets",wallets);
        json.add("completed_milestones", strings(state.milestones()));
        var upcoming=state.joined()
            ? io.github.heatwaveqc.tyr.progression.core.MilestoneFeedback.next(path,state) : null;
        if(upcoming!=null){
            JsonObject challenge=new JsonObject();
            challenge.addProperty("id",upcoming.milestoneId());
            challenge.addProperty("title",upcoming.title());
            challenge.addProperty("progress",upcoming.current());
            challenge.addProperty("goal",upcoming.total());
            json.add("next_challenge",challenge);
        }
        long invested=io.github.heatwaveqc.tyr.progression.core.ChannelEngine.investedRanks(ledger,pathId,PathRegistry.config());
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
            if(owned<skill.maxRank()&&skill.nextInvestedRequirement(owned)>0){
                item.addProperty("required_invested_ranks",skill.nextInvestedRequirement(owned));
                item.addProperty("invested_ranks",invested);
            }
            item.addProperty("eligible",PathRegistry.skillEligible(player,pathId,skill.id()));
            item.addProperty("auto", !skill.autoRanks().isEmpty());
            var binding=PathRegistry.config().skills().get(new io.github.heatwaveqc.tyr.progression.data.ChannelConfig.SkillKey(pathId,skill.id()));
            item.addProperty("currency",binding.currency().id());
            item.addProperty("permanent",binding.permanent());
            item.addProperty("point_name",binding.currency().pointName());
            item.addProperty("level_system",binding.levelSystem().name());
            item.addProperty("price", binding.currency().magic()?1:skill.price(owned));
            var activation=PathRegistry.activation(pathId,skill.id());
            if(activation!=null){
                item.addProperty("activation_mode",activation.mode());
                item.addProperty("activation_active",io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.active(player,pathId,skill.id()));
                item.addProperty("activation_ready",io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.canRequest(player,pathId,skill.id()));
                item.addProperty("activation_remaining",io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.remaining(player,pathId,skill.id()));
            }
            item.addProperty("choice_group", skill.choiceGroup());
            item.addProperty("buyable", ProgressionApi.canBuy(player,pathId,skill.id()));
            JsonArray purchaseSources=new JsonArray();
            var rule=catalog.skills().get(
                new io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig.SkillKey(pathId,skill.id()));
            if(rule!=null){
                for(var source:rule.sources()){
                    JsonObject choice=new JsonObject();
                    choice.addProperty("system",source.system());
                    choice.addProperty("max_ranks",source.maxRanks());
                    choice.addProperty("owned",
                        io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.contributedRanks(
                            ledger.economy(),pathId,skill.id(),source.system()));
                    choice.addProperty("price",source.pointsPerRank());
                    choice.addProperty("buyable",ProgressionApi.canBuy(
                        player,pathId,skill.id(),source.system()));
                    purchaseSources.add(choice);
                }
            } else {
                JsonObject choice=new JsonObject();
                choice.addProperty("system",binding.currency().id());
                choice.addProperty("max_ranks",skill.maxRank());
                choice.addProperty("owned",owned);
                choice.addProperty("price",binding.currency().magic()?1:skill.price(owned));
                choice.addProperty("buyable",ProgressionApi.canBuy(player,pathId,skill.id()));
                purchaseSources.add(choice);
            }
            item.add("purchase_sources",purchaseSources);
            item.add("requires", strings(skill.requiresSkills()));
            item.add("requires_any", strings(skill.requiresAnySkills()));
            item.add("milestones", strings(skill.requiresMilestones()));
            JsonArray effects = new JsonArray();
            for (Definition.Effect fx : skill.effects()) effects.add(fx.type());
            item.add("effects", effects);
            skills.add(item);
        }
        // External native rewards are ordinary graph nodes in Core's server
        // projection, not extra Path ranks or an alternate payment ledger.
        if ("tyr_progression:core".equals(pathId)) {
            for (var reward : io.github.heatwaveqc.tyr.progression.runtime.external.ExternalRewardConfig.current().values()) {
                if (path.skills().stream().anyMatch(skill -> skill.id().equals(reward.id()))
                        || !io.github.heatwaveqc.tyr.progression.runtime.external.ExternalRewardService.visible(player,reward.id()))
                    continue;
                int owned=io.github.heatwaveqc.tyr.progression.runtime.external.ExternalRewardService.owned(player,reward.id());
                int price=io.github.heatwaveqc.tyr.progression.runtime.external.ExternalRewardService.nextPrice(player,reward.id());
                boolean buyable=io.github.heatwaveqc.tyr.progression.runtime.external.ExternalRewardService.canBuy(player,reward.id());
                skills.add(io.github.heatwaveqc.tyr.progression.runtime.external.ExternalRewardNode.project(reward,owned,price,buyable));
            }
        }
        json.add("skills", skills);
        String payload=json.toString();
        if(payload.length()>262144){ProgressionMod.LOGGER.warn("Tree snapshot too large: {}",pathId);return;}
        PacketDistributor.sendToPlayer(player, new Snapshot(payload));
    }

    public static void refresh(ServerPlayer player) {
        var path=LAST_VIEW.get(player.getUUID());
        if(path!=null)send(player,path);
    }

    public static void forget(ServerPlayer player) {
        LAST_VIEW.remove(player.getUUID());
        LAST_MUTATION.remove(player.getUUID());
    }
}
