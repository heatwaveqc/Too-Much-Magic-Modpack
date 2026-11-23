package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Discoveries;
import io.github.heatwaveqc.tyr.progression.state.Modes;
import io.github.heatwaveqc.tyr.progression.runtime.Selectors;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import io.github.heatwaveqc.tyr.progression.state.Track;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiPredicate;
import java.util.UUID;
import java.math.BigDecimal;

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
        return BaselinePaths.hasSpecialist(ledger);
    }

    public static boolean join(ServerPlayer player, String pathId) {
        // Only the personal graduation/admin gateway may change baseline Paths.
        if(BaselinePaths.isBaseline(pathId))return false;
        Definition.Path path = PathRegistry.get(pathId);
        var channel=PathRegistry.channel(pathId);
        if (path == null || channel==null || !PathRegistry.eligible(player,pathId)) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        if (ledger.get(pathId).joined()) return true;
        if (NewbieMode.isNewbie(player)) return false;
        // Core is a universal graduate tree, not a specialist enrollment.
        if (!BaselinePaths.isCore(pathId))
            for (BiPredicate<ServerPlayer,String> veto : JOIN_VETOES)
                if (!veto.test(player,pathId)) return false;
        if (!BaselinePaths.isCore(pathId) && !path.exclusiveGroup().isBlank()) {
            for (Definition.Path other : PathRegistry.all().values()) {
                if (!pathId.equals(other.id()) && path.exclusiveGroup().equals(other.exclusiveGroup())
                    && ledger.get(other.id()).joined()) return false;
            }
        }
        // Join is a real Path change: TMM's configured default sacrifices
        // unallocated MXP, but never already purchased Core/retained Path ranks.
        Ledger next=ChannelEngine.join(ledger,path,channel);
        if(!BaselinePaths.isCore(pathId)) {
            // Actual specialist enrollment forfeits any unallocated MXP.
            next=ChannelEngine.pathChange(next,java.util.Set.of());
            // Normie is only the graduated, unspecialized fallback.
            next=BaselinePaths.reconcile(next,false);
        }
        player.setData(ProgressionAttachments.LEDGER.get(),next);
        return true;
    }

    /**
     * Validated, punitive Path departure for both gameplay and operator commands.
     * Achievements/counters persist; all purchased ranks of the lost Path are
     * destroyed and current MXP rebases from surviving purchased ranks.
     */
    public static boolean leave(ServerPlayer player,String pathId) {
        if(player==null || player.getServer()==null || !player.getServer().isSameThread())return false;
        Definition.Path path=PathRegistry.get(pathId);
        if(path==null || BaselinePaths.isBaseline(pathId)
            || BaselinePaths.isCore(pathId)
            || "automatic".equals(PathRegistry.enrollment(pathId)))return false;
        Ledger old=player.getData(ProgressionAttachments.LEDGER.get());
        Track track=old.get(pathId);
        if(!track.joined())return false;
        Track dormant=new Track(false,0,0,0,Map.of(),track.milestones(),track.counters());
        Ledger changed=old.with(pathId,dormant).clearAllReceipts(pathId);
        changed=ChannelEngine.pathChange(changed,java.util.Set.of(pathId));
        changed=BaselinePaths.reconcile(changed,false);
        player.setData(ProgressionAttachments.LEDGER.get(),changed);
        io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.stopPath(player,pathId);
        return true;
    }

    /**
     * Operator-only Newbie restoration is a destructive stage transition:
     * no specialist or automatic Core investment may remain active during
     * the Newbie stage, but personal milestone and one-time receipt history stays.
     * Call only from the trusted stage setter, never from client packets.
     */
    public static void forfeitAllPathsForNewbie(ServerPlayer player) {
        if(player==null || player.getServer()==null || !player.getServer().isSameThread())
            throw new IllegalStateException("Newbie rebase requires the logical server thread");
        Ledger old=player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next=old;
        var departed=new java.util.HashSet<String>();
        for(var row:old.paths().entrySet()) {
            Track track=row.getValue();
            if(!track.joined())continue;
            departed.add(row.getKey());
            Track removed=new Track(false,0,0,0,Map.of(),track.milestones(),track.counters());
            next=next.with(row.getKey(),removed).clearAllReceipts(row.getKey());
        }
        next=ChannelEngine.pathChange(next,departed);
        player.setData(ProgressionAttachments.LEDGER.get(),next);
        for(String id:departed)
            io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.stopPath(player,id);
    }

    private static String defaultCurrency(String pathId,String skillId){
        var rules=PathRegistry.economyCatalog().skills().get(
            new io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig.SkillKey(pathId,skillId));
        if(rules!=null)return rules.sources().getFirst().system();
        var binding=PathRegistry.config().skills().get(
            new io.github.heatwaveqc.tyr.progression.data.ChannelConfig.SkillKey(pathId,skillId));
        return binding==null?"":binding.currency().id();
    }

    public static boolean canBuy(ServerPlayer player,String pathId,String skillId) {
        return canBuy(player,pathId,skillId,defaultCurrency(pathId,skillId));
    }

    /** The client may CHOOSE a declared currency, never its price or rank. */
    public static boolean canBuy(ServerPlayer player,String pathId,String skillId,String systemId) {
        var path=PathRegistry.get(pathId);
        if(path==null || player.isCreative() || player.isSpectator()
            || NewbieMode.isNewbie(player)
            || !PathRegistry.purchaseEligible(player,pathId,skillId))return false;
        Ledger before=player.getData(ProgressionAttachments.LEDGER.get());
        return ChannelEngine.buy(before,path,PathRegistry.config(),skillId,systemId)!=before;
    }

    /** Fail closed on unknown currencies, prerequisites, caps, or insufficient points. */
    public static boolean buy(ServerPlayer player,String pathId,String skillId) {
        return buy(player,pathId,skillId,defaultCurrency(pathId,skillId));
    }

    public static boolean buy(ServerPlayer player,String pathId,String skillId,String systemId) {
        Definition.Path path=PathRegistry.get(pathId);
        if(path==null || PathRegistry.channel(pathId)==null
            || !canBuy(player,pathId,skillId,systemId))return false;
        Ledger before=player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next=ChannelEngine.buy(before,path,PathRegistry.config(),skillId,systemId);
        if(next==before)return false;
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
        io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.stopPath(player,pathId);
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
        if (path == null || channel==null || !channel.earnsFrom(pathId) || !PathRegistry.eligible(player,pathId) || !PathRegistry.economyEligible(player,channel.id()) || NewbieMode.isNewbie(player)) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        if(!ledger.get(pathId).joined())return false;
        player.setData(ProgressionAttachments.LEDGER.get(),ChannelEngine.grant(ledger,channel,io.github.heatwaveqc.tyr.progression.runtime.ExperienceBonuses.apply(player,channel.id(),xp),0));
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
        if (player.isCreative() || player.isSpectator() || NewbieMode.isNewbie(player)) return;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next = ledger;
        for(var system:PathRegistry.economies().values()){
            if(system.magic()||!PathRegistry.economyEligible(player,system.id()))continue;
            String required=system.earningPath();
            if(!required.isEmpty()){
                var gate=PathRegistry.get(required);
                if(gate==null||!next.get(required).joined()||!PathRegistry.eligible(player,required)||!eligiblePath.test(gate))continue;
            }
            int index=0;
            for(var source:system.sources()){
                int sourceIndex=index++;
                String cooldownKey=system.id()+"/"+sourceIndex;
                if(!signal.matches(source.event(),source.selector())||!PathRegistry.sourceEligible(player,system.id(),sourceIndex))continue;
                long now=player.level().getGameTime();
                var recent=cooldowns.computeIfAbsent(player.getUUID(),ignored->new HashMap<>());
                long last=recent.getOrDefault(cooldownKey,Long.MIN_VALUE);
                if(last!=Long.MIN_VALUE&&now>=last&&now-last<source.cooldownTicks())continue;
                recent.put(cooldownKey,now);
                long xp=io.github.heatwaveqc.tyr.progression.runtime.ExperienceBonuses.apply(player,system.id(),source.xp());
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
                        // This is a permanent achievement, independent of
                        // current Path membership or purchased skill ranks.
                        next=next.withEconomy(next.economy().withAchievement(milestone.id()));
                        if(milestone.xp()>0||milestone.points()>0)
                            next=ChannelEngine.grant(next,channel,milestone.xp(),milestone.points());
                        changed = true;
                    }
                }
            } while(changed);
            if (!state.equals(initial)) next=ChannelEngine.ownership(next,path,state,PathRegistry.config());
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
        if (marker == null || !marker.matches("tyr_progression:tyz_focused_advancement_[1-5]"))
            return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        var path=PathRegistry.get("tyr_progression:monk");
        var channel=PathRegistry.channel("tyr_progression:monk");
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
        return awardMagic(player,channelId,"tyr_progression:verified_resource",tx,null,1,
                MxpMath.normalizedBase(nominalCost,referencePool),false,false);
    }
    /** Trusted verified ordinary award, already normalized; no fictional debit. */
    public static boolean awardMagicOrdinary(ServerPlayer player,String channelId,String tx,double base) {
        return awardMagicOrdinary(player,channelId,"tyr_progression:default",tx,base);
    }
    public static boolean awardMagicOrdinary(ServerPlayer player,String channelId,
                                             String sourceCategory,String tx,double base) {
        if(!Double.isFinite(base)||base<=0)return false;
        return awardMagic(player,channelId,sourceCategory,tx,null,1,base,false,false);
    }

    /** Trusted completion: first receipt scoped by XP system and source category. */
    public static boolean awardMagicCompletion(ServerPlayer player,String channelId,String tx,
            String completionId,double firstBase,double repeatBase,boolean firstFixed,boolean repeatFixed) {
        return awardMagicCompletion(player,channelId,"tyr_progression:default",
                tx,completionId,firstBase,repeatBase,firstFixed,repeatFixed);
    }
    public static boolean awardMagicCompletion(ServerPlayer player,String channelId,
            String sourceCategory,String tx,String completionId,
            double firstBase,double repeatBase,boolean firstFixed,boolean repeatFixed) {
        return awardMagic(player,channelId,sourceCategory,tx,
            java.util.Objects.requireNonNull(completionId),
            firstBase,repeatBase,firstFixed,repeatFixed);
    }
    private static boolean awardMagic(ServerPlayer player,String channelId,
            String sourceCategory,String tx,String first,
            double firstBase,double repeatBase,boolean firstFixed,boolean repeatFixed) {
        if(player==null || player.getServer()==null || !player.getServer().isSameThread()
            || player.isCreative() || player.isSpectator() || NewbieMode.isNewbie(player)) return false;
        XpChannel channel=PathRegistry.economy(channelId);
        if(channel==null || !channel.magic()) return false;
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        double configured=PathRegistry.economyCatalog().growth(channelId);
        double growth=io.github.heatwaveqc.tyr.progression.runtime.ExperienceGrowth
            .effective(player,configured,channelId);
        Ledger next=ChannelEngine.magic(prior,
            XpChannel.generic(channelId,growth,channel.name(),channel.pointName()),
            sourceCategory,tx,first,firstBase,repeatBase,firstFixed,repeatFixed);
        if(next==prior) return false;
        player.setData(ProgressionAttachments.LEDGER.get(),next);
        return true;
    }

    /**
     * Authenticated server-side award for ANY configured XP system. The caller
     * supplies a transaction identity from a verified committed action; never
     * expose this amount or identity in a client payload.
     */
    public static boolean awardConfiguredXp(ServerPlayer player,String systemId,
            String sourceCategory,String transaction,String completionId,
            BigDecimal nominalXp,boolean fixed) {
        if(player==null||player.getServer()==null||!player.getServer().isSameThread()
            ||player.isCreative()||player.isSpectator()||NewbieMode.isNewbie(player))
            return false;
        var catalog=PathRegistry.economyCatalog();
        if(!catalog.systems().containsKey(systemId))return false;
        var nativeChannel=PathRegistry.economy(systemId);
        if(nativeChannel!=null&&!PathRegistry.economyEligible(player,systemId))return false;
        double growth=io.github.heatwaveqc.tyr.progression.runtime.ExperienceGrowth
                .effective(player,catalog.growth(systemId),systemId);
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next=ChannelEngine.awardConfigured(prior,catalog,systemId,
                sourceCategory,transaction,completionId,nominalXp,fixed,growth);
        if(next==prior)return false;
        player.setData(ProgressionAttachments.LEDGER.get(),next);
        return true;
    }

    /** Direct grant to a configured native system; independent of any tree. */
    public static boolean awardExperience(ServerPlayer player,String id,long xp) {
        if(player==null||player.getServer()==null||!player.getServer().isSameThread()||player.isCreative()
            ||player.isSpectator()||NewbieMode.isNewbie(player)||xp<=0||xp>1_000_000L)return false;
        var system=PathRegistry.economy(id);if(system==null||system.magic()||!PathRegistry.economyEligible(player,id))return false;
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        String gate=system.earningPath();
        if(!gate.isEmpty()&&(!prior.get(gate).joined()||!PathRegistry.eligible(player,gate)))return false;
        player.setData(ProgressionAttachments.LEDGER.get(),ChannelEngine.grant(prior,system,io.github.heatwaveqc.tyr.progression.runtime.ExperienceBonuses.apply(player,id,xp),0));return true;
    }
    public static void forgetTransient(UUID playerId) {
        cooldowns.remove(playerId);
        io.github.heatwaveqc.tyr.progression.runtime.forms.ProviderFlags.forget(playerId);
    }
}
