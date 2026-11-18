package io.github.heatwaveqc.tmm.progression.core;

import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.state.Ledger;
import io.github.heatwaveqc.tmm.progression.state.Discoveries;
import io.github.heatwaveqc.tmm.progression.state.Modes;
import io.github.heatwaveqc.tmm.progression.runtime.Selectors;
import io.github.heatwaveqc.tmm.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tmm.progression.runtime.TyzMonkBridge;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import io.github.heatwaveqc.tmm.progression.state.ProgressionAttachments;
import io.github.heatwaveqc.tmm.progression.state.Track;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import net.neoforged.fml.ModList;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiPredicate;
import java.util.UUID;

/**
 * Stable entry points for standalone Monk/Hunter initiation, KubeJS bridges and future mods.
 * All methods must run on the logical server thread; no client-supplied XP amounts.
 */
public final class ProgressionApi {
    private ProgressionApi() {}
    private static final Map<UUID,Map<String,Long>> cooldowns = new HashMap<>();
    /** Registration-time join vetoes, including optional supernatural-species compatibility. */
    private static final List<BiPredicate<ServerPlayer,String>> JOIN_VETOES = new CopyOnWriteArrayList<>();

    /** Optional trusted adapter: return false to reject joining the given path. */
    public static void registerJoinVeto(BiPredicate<ServerPlayer,String> policy) {
        JOIN_VETOES.add(java.util.Objects.requireNonNull(policy));
    }

    public static Track track(ServerPlayer player, String pathId) {
        var path=PathRegistry.get(pathId);
        var channel=PathRegistry.channel(pathId);
        return path==null || channel==null ? Track.empty() :
            ChannelEngine.view(player.getData(ProgressionAttachments.LEDGER.get()),path,PathRegistry.config());
    }

    /** Any joined path, including future Gilded or data-defined paths. */
    public static boolean anyJoined(ServerPlayer player) {
        return hasJoinedPaths(player.getData(ProgressionAttachments.LEDGER.get()));
    }

    public static boolean hasJoinedPaths(Ledger ledger) {
        return ledger.paths().values().stream().anyMatch(Track::joined);
    }

    public static boolean join(ServerPlayer player, String pathId) {
        Definition.Path path = PathRegistry.get(pathId);
        var channel=PathRegistry.channel(pathId);
        if (path == null || channel==null || !PathRegistry.eligible(player,pathId)) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        if (ledger.get(pathId).joined()) return true;
        if (NewbieMode.isNewbie(player)) return false;
        for (BiPredicate<ServerPlayer,String> veto : JOIN_VETOES)
            if (!veto.test(player,pathId)) return false;
        if (!path.exclusiveGroup().isBlank()) {
            for (Definition.Path other : PathRegistry.all().values()) {
                if (!pathId.equals(other.id()) && path.exclusiveGroup().equals(other.exclusiveGroup())
                    && ledger.get(other.id()).joined()) return false;
            }
        }
        player.setData(ProgressionAttachments.LEDGER.get(),
            ChannelEngine.join(ledger,path,channel));
        return true;
    }

    public static boolean canBuy(ServerPlayer player,String pathId,String skillId) {
        var path=PathRegistry.get(pathId);
        if(path==null || !PathRegistry.skillEligible(player,pathId,skillId)) return false;
        Ledger before=player.getData(ProgressionAttachments.LEDGER.get());
        return ChannelEngine.buy(before,path,PathRegistry.config(),skillId)!=before;
    }

    /** Fail-closed if the path is missing, gated, full, or short on skill points. */
    public static boolean buy(ServerPlayer player, String pathId, String skillId) {
        Definition.Path path = PathRegistry.get(pathId);
        var channel=PathRegistry.channel(pathId);
        if (path == null || channel==null || !canBuy(player,pathId,skillId)) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next=ChannelEngine.buy(ledger,path,PathRegistry.config(),skillId);
        if(next==ledger)return false;
        player.setData(ProgressionAttachments.LEDGER.get(),next);
        return true;
    }

    public static boolean respec(ServerPlayer player, String pathId) {
        Definition.Path path = PathRegistry.get(pathId);
        var channel=PathRegistry.channel(pathId);
        if (path == null || channel==null || !track(player,pathId).joined()) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        player.setData(ProgressionAttachments.LEDGER.get(),
            ChannelEngine.refund(ledger,path));
        return true;
    }

    /**
     * Trusted logical-server grant for reward sources with computed outcomes, such as
     * quarry maximum-health XP and potion potency. Never expose over a client packet.
     */
    public static boolean awardServerXp(ServerPlayer player, String pathId, long xp) {
        if (player.isCreative() || player.isSpectator() || xp <= 0 || xp > 1_000_000L) return false;
        Definition.Path path = PathRegistry.get(pathId);
        var channel=PathRegistry.channel(pathId);
        if (path == null || channel==null || !channel.earnsFrom(pathId) || !PathRegistry.eligible(player,pathId)) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        if(!ledger.get(pathId).joined())return false;
        player.setData(ProgressionAttachments.LEDGER.get(),ChannelEngine.grant(ledger,channel,xp,0));
        return true;
    }

    /** For verified external milestones and compatible mods; cannot mint arbitrary XP. */
    public static void recordExternal(ServerPlayer player, String namespacedEventId) {
        record(player,Signal.external(namespacedEventId));
    }

    /** Server-only independent mode storage, for Fortune/Silk and Monk Armor Lock toggles. */
    public static String mode(ServerPlayer player, String key, String fallback) {
        return player.getData(ProgressionAttachments.PLAYER_MODES.get()).get(key,fallback);
    }
    public static boolean setMode(ServerPlayer player, String key, String value) {
        if (!key.matches("[a-z0-9_.-]+:[a-z0-9_./-]+") || key.length() > 128
                || value == null || value.length() > 80 || !value.matches("[a-z0-9_./-]+")) return false;
        Modes before = player.getData(ProgressionAttachments.PLAYER_MODES.get());
        Modes after = before.with(key,value);
        if (before != after) player.setData(ProgressionAttachments.PLAYER_MODES.get(),after);
        return true;
    }

    /** First-consumption reward is global per player, not per NBT variant, saved across death/relog. */
    public static void recordConsumedFood(ServerPlayer player, ItemStack food) {
        if (player.isCreative() || player.isSpectator() || food.isEmpty()) return;
        record(player,Selectors.itemAction("food_consumed",food));
        String id = BuiltInRegistries.ITEM.getKey(food.getItem()).toString();
        Discoveries before = player.getData(ProgressionAttachments.FOOD_HISTORY.get());
        Discoveries after = before.discover(id);
        if (before != after) {
            player.setData(ProgressionAttachments.FOOD_HISTORY.get(),after);
            record(player,Selectors.itemAction("food_discovered",food));
        }
    }

    /** Standardized activity events: one authoritative signal, all joined paths. */
    public static void record(ServerPlayer player, Signal signal) {
        recordFiltered(player,signal,path -> true);
    }

    /** Trusted server-side event provenance filter: reject rewards for a specific path. */
    public static void recordFiltered(ServerPlayer player, Signal signal,
                                      java.util.function.Predicate<Definition.Path> eligiblePath) {
        if (player.isCreative() || player.isSpectator()) return;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next = ledger;
        for(var system:PathRegistry.economies().values()){
            if(system.magic())continue;
            String required=system.earningPath();
            if(!required.isEmpty()){
                var gate=PathRegistry.get(required);
                if(gate==null||!next.get(required).joined()||!PathRegistry.eligible(player,required)||!eligiblePath.test(gate))continue;
            }
            int index=0;
            for(var source:system.sources()){
                String cooldownKey=system.id()+"/"+index++;
                if(!signal.matches(source.event(),source.selector()))continue;
                long now=player.level().getGameTime();
                var recent=cooldowns.computeIfAbsent(player.getUUID(),ignored->new HashMap<>());
                long last=recent.getOrDefault(cooldownKey,Long.MIN_VALUE);
                if(last!=Long.MIN_VALUE&&now>=last&&now-last<source.cooldownTicks())continue;
                recent.put(cooldownKey,now);
                long xp=TyzMonkBridge.bonusMonkXp(player,required,source.xp());
                next=ChannelEngine.grant(next,system,xp,0);
            }
        }
        for (Definition.Path path : PathRegistry.all().values()) {
            if (!eligiblePath.test(path)) continue;
            var channel=PathRegistry.channel(path.id());
            if(channel==null || !PathRegistry.eligible(player,path.id())) continue;
            Track initial=ChannelEngine.view(next,path,PathRegistry.config());
            Track state=initial;
            if (!state.joined()) continue;

            for (Definition.Milestone milestone : path.milestones()) {
                if (state.completed(milestone.id())) continue;
                int index = 0;
                for (Definition.Criterion criterion : milestone.criteria()) {
                    if (signal.matches(criterion.event(),criterion.selector())) {
                        String key = ProgressionMath.counterKey(milestone.id(),index);
                        if (state.criterionCount(key) < criterion.count())
                            state = ProgressionMath.recordCriterion(state,key,1);
                    }
                    index++;
                }
            }
            // A single event may satisfy a chain of previously completed prerequisite counters.
            boolean changed;
            do {
                changed = false;
                for (Definition.Milestone milestone : path.milestones()) {
                    if (state.completed(milestone.id()) || !state.milestones().containsAll(milestone.after()))
                        continue;
                    boolean ready = true;
                    for (int i = 0; i < milestone.criteria().size(); i++) {
                        if (state.criterionCount(ProgressionMath.counterKey(milestone.id(),i))
                                < milestone.criteria().get(i).count()) {
                            ready = false; break;
                        }
                    }
                    if (ready) {
                        state=state.complete(milestone.id());
                        if(milestone.xp()>0||milestone.points()>0)
                            next=ChannelEngine.grant(next,channel,milestone.xp(),milestone.points());
                        changed = true;
                    }
                }
            } while(changed);
            if (!state.equals(initial)) next=ChannelEngine.ownership(next,path,state);
        }
        if (next != ledger) player.setData(ProgressionAttachments.LEDGER.get(),next);
    }

    /**
     * Permanent, non-refundable Tyz-to-native advancement conversion.
     * Each marker can grant exactly one native Monk point over a character's
     * lifetime, even after relog, skill reload, admin grants or later respec.
     * This deliberately does NOT mint Tyz SP or global MXP.
     */
    public static boolean awardMonkTyzPointOnce(ServerPlayer player, String marker) {
        if (marker == null || !marker.matches("tmm_progression:tyz_focused_advancement_[1-5]"))
            return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        var path=PathRegistry.get("tmm_progression:monk");
        var channel=PathRegistry.channel("tmm_progression:monk");
        if(path==null || channel==null) return false;
        Track monk=ChannelEngine.view(ledger,path,PathRegistry.config());
        if (!monk.joined() || monk.rank() < 17 || monk.completed(marker)) return false;
        player.setData(ProgressionAttachments.LEDGER.get(),
                ChannelEngine.grant(ChannelEngine.ownership(ledger,path,monk.complete(marker)),channel,0,1));
        return true;
    }

    /** Trusted logical-server, committed-debit API. Standalone MXP still owns legacy cast hooks. */
    public static boolean awardMagicResource(ServerPlayer player,String channelId,String tx,
            double nominalCost,double referencePool,double actualDebit) {
        if(!Double.isFinite(actualDebit) || actualDebit<=0) return false;
        return awardMagic(player,channelId,tx,null,1,
                MxpMath.normalizedBase(nominalCost,referencePool),false,false);
    }
    /** Trusted success-only completion API; caller supplies verified transaction identity. */
    public static boolean awardMagicCompletion(ServerPlayer player,String channelId,String tx,
            String completionId,double firstBase,double repeatBase,boolean firstFixed,boolean repeatFixed) {
        return awardMagic(player,channelId,tx,java.util.Objects.requireNonNull(completionId),
                firstBase,repeatBase,firstFixed,repeatFixed);
    }
    private static boolean awardMagic(ServerPlayer player,String channelId,String tx,String first,
            double firstBase,double repeatBase,boolean firstFixed,boolean repeatFixed) {
        if(player==null || player.getServer()==null || !player.getServer().isSameThread()
            || player.isCreative() || player.isSpectator() || NewbieMode.isNewbie(player)
            || ModList.get().isLoaded("tmm_mxp")) return false;
        XpChannel channel=PathRegistry.economy(channelId);
        if(channel==null || !channel.magic()) return false;
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next=ChannelEngine.magic(prior,channel,tx,first,firstBase,repeatBase,firstFixed,repeatFixed);
        if(next==prior) return false;
        player.setData(ProgressionAttachments.LEDGER.get(),next);
        return true;
    }

    /** Direct grant to a configured native system; independent of any tree. */
    public static boolean awardExperience(ServerPlayer player,String id,long xp) {
        if(player==null||player.getServer()==null||!player.getServer().isSameThread()||player.isCreative()
            ||player.isSpectator()||NewbieMode.isNewbie(player)||xp<=0||xp>1_000_000L)return false;
        var system=PathRegistry.economy(id);if(system==null||system.magic())return false;
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        String gate=system.earningPath();
        if(!gate.isEmpty()&&(!prior.get(gate).joined()||!PathRegistry.eligible(player,gate)))return false;
        player.setData(ProgressionAttachments.LEDGER.get(),ChannelEngine.grant(prior,system,xp,0));return true;
    }
    public static void forgetTransient(UUID playerId) {
        cooldowns.remove(playerId);
        io.github.heatwaveqc.tmm.progression.runtime.forms.ProviderFlags.forget(playerId);
    }
}
