package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.data.XpSourceRouting;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Discoveries;
import io.github.heatwaveqc.tyr.progression.state.Modes;
import io.github.heatwaveqc.tyr.progression.runtime.Selectors;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tyr.progression.runtime.ExperienceSourceRouter;
import io.github.heatwaveqc.tyr.progression.runtime.ExperienceModifiers;
import net.neoforged.neoforge.common.util.FakePlayer;
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

    /** Read-only operator-facing diagnosis, shared with the actual join gate. */
    public static String joinBlocker(ServerPlayer player,String pathId){
        if(BaselinePaths.isBaseline(pathId))return "Baseline requires graduation/stage gateway";
        var path=PathRegistry.get(pathId);var channel=PathRegistry.channel(pathId);
        if(path==null||channel==null)return "Unknown path or missing channel";
        if(!PathRegistry.eligible(player,pathId))return "Configured eligibility failed";
        var ledger=player.getData(ProgressionAttachments.LEDGER.get());
        if(ledger.get(pathId).joined())return "";
        if(NewbieMode.isNewbie(player))return "Newbie stage blocks specialist enrollment";
        if(!BaselinePaths.isCore(pathId)){
            for(var veto:JOIN_VETOES)if(!veto.test(player,pathId))return "Registered join veto rejected";
            for(var other:PathRegistry.all().values())
                if(!pathId.equals(other.id())&&ledger.get(other.id()).joined()
                        &&!PathRegistry.canCoexist(pathId,other.id()))return "Incompatible joined path: "+other.id();
        }
        return "";
    }
    public static String leaveBlocker(ServerPlayer player,String pathId){
        if(player==null||player.getServer()==null||!player.getServer().isSameThread())return "Server thread required";
        if(PathRegistry.get(pathId)==null)return "Unknown path";
        if(BaselinePaths.isBaseline(pathId)||BaselinePaths.isCore(pathId)||"automatic".equals(PathRegistry.enrollment(pathId)))
            return "Baseline, Core or automatic path cannot be manually removed";
        if(!player.getData(ProgressionAttachments.LEDGER.get()).get(pathId).joined())return "Path not joined";
        return "";
    }
    public static boolean join(ServerPlayer player, String pathId) {
        if(!joinBlocker(player,pathId).isEmpty())return false;
        Definition.Path path=PathRegistry.get(pathId);
        var channel=PathRegistry.channel(pathId);
        Ledger ledger=player.getData(ProgressionAttachments.LEDGER.get());
        if(ledger.get(pathId).joined())return true;
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
        Ledger changed=ChannelEngine.depart(old,path,PathRegistry.economyCatalog());
        changed=BaselinePaths.reconcile(changed,false);
        player.setData(ProgressionAttachments.LEDGER.get(),changed);
        clearPathDiscoveries(player,pathId);
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
        for(String id:departed){
            clearPathDiscoveries(player,id);
            io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.stopPath(player,id);
        }
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
        clearPathDiscoveries(player,pathId);
        io.github.heatwaveqc.tyr.progression.runtime.SkillActivation.stopPath(player,pathId);
        return true;
    }

    /**
     * Trusted logical-server grant for reward sources with computed outcomes, such as
     * quarry maximum-health XP and potion potency. Never expose over a client packet.
     */
    public static boolean awardServerXp(ServerPlayer player, String pathId, long xp) {
        if (!eligibleEarner(player) || xp <= 0 || xp > 1_000_000L) return false;
        Definition.Path path = PathRegistry.get(pathId);
        var channel=PathRegistry.channel(pathId);
        if (path == null || channel==null || !PathRegistry.eligible(player,pathId)
                || !PathRegistry.economyEligible(player,channel.id())) return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        if(!ledger.get(pathId).joined())return false;
        if(channel.magic())
            return awardExplicitConfiguredXp(player,channel.id(),"tyr_progression:admin_grant",
                    "admin:"+UUID.randomUUID(),null,BigDecimal.valueOf(xp),false);
        if(!channel.earnsFrom(pathId))return false;
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

    /** Reset local first-taste progress, not permanent paid XP receipts. */
    private static void clearPathDiscoveries(ServerPlayer player,String pathId){
        io.github.heatwaveqc.tyr.progression.runtime.ElixirRuntime.resetPath(player,pathId);
        var previous=player.getData(ProgressionAttachments.PATH_DISCOVERIES.get());
        var cleared=previous.clear(pathId);
        if(cleared!=previous)player.setData(ProgressionAttachments.PATH_DISCOVERIES.get(),cleared);
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

    /** Trusted server hit detector resets only an active, unfinished arrow-dodge trial. */
    public static void resetArrowTrial(ServerPlayer player,String pathId,String milestoneId){
        if(!eligibleEarner(player)||!PathRegistry.eligible(player,pathId))return;
        var path=PathRegistry.get(pathId);
        if(path==null||PathRegistry.channel(pathId)==null)return;
        var milestone=path.milestones().stream().filter(m->m.id().equals(milestoneId)).findFirst().orElse(null);
        if(milestone==null||milestone.criteria().size()!=1
                ||!milestone.criteria().getFirst().event().equals("arrow_dodged"))return;
        Ledger ledger=player.getData(ProgressionAttachments.LEDGER.get());
        Track initial=ChannelEngine.view(ledger,path,PathRegistry.config());
        Track next=ProgressionMath.resetCriterion(initial,milestone,0);
        if(!next.equals(initial))player.setData(ProgressionAttachments.LEDGER.get(),
            ChannelEngine.ownership(ledger,path,next,PathRegistry.config()));
    }

    /** Standardized activity events: one authoritative signal, all joined paths. */
    public static void record(ServerPlayer player, Signal signal) {
        recordFiltered(player,signal,path -> true);
    }

    /** Trusted server-side event provenance filter: reject rewards for a specific path. */
    public static void recordFiltered(ServerPlayer player, Signal signal,
                                      java.util.function.Predicate<Definition.Path> eligiblePath) {
        if (!eligibleEarner(player)){
            io.github.heatwaveqc.tyr.progression.admin.XpTrace.note(player,()->"signal rejected at earning gate: "+signal.target());
            return;
        }
        io.github.heatwaveqc.tyr.progression.admin.XpTrace.note(player,()->"signal received: "+signal.target());
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next = ledger;
        MilestoneFeedback.Notice progressNotice = null;
        for(var system:PathRegistry.economies().values()){
            if(system.magic()||!PathRegistry.economyEligible(player,system.id()))continue;
            if(!PathRegistry.config().usesNative(system.id(),id->{
                var bound=PathRegistry.get(id);
                return bound!=null&&ledger.get(id).joined()&&PathRegistry.eligible(player,id)
                        &&eligiblePath.test(bound);
            }))continue;
            String required=system.earningPath();
            if(!required.isEmpty()){
                var gate=PathRegistry.get(required);
                if(gate==null||!next.get(required).joined()||!PathRegistry.eligible(player,required)||!eligiblePath.test(gate))continue;
            }
            int index=0;
            for(var source:system.sources()){
                int sourceIndex=index++;
                String cooldownKey=system.id()+"/"+sourceIndex;
                if(!signal.matches(source.event(),source.selector()))continue;
                if(!PathRegistry.sourceEligible(player,system.id(),sourceIndex)){
                    io.github.heatwaveqc.tyr.progression.admin.XpTrace.note(player,()->cooldownKey+" rejected: source eligibility");continue;
                }
                long now=player.level().getGameTime();
                var recent=cooldowns.computeIfAbsent(player.getUUID(),ignored->new HashMap<>());
                long last=recent.getOrDefault(cooldownKey,Long.MIN_VALUE);
                if(last!=Long.MIN_VALUE&&now>=last&&now-last<source.cooldownTicks()){
                    io.github.heatwaveqc.tyr.progression.admin.XpTrace.note(player,()->cooldownKey+" rejected: source cooldown");continue;
                }
                recent.put(cooldownKey,now);
                long xp=io.github.heatwaveqc.tyr.progression.runtime.ExperienceBonuses.apply(player,system.id(),source.xp());
                next=ChannelEngine.grant(next,system,xp,0);
                io.github.heatwaveqc.tyr.progression.admin.XpTrace.note(player,()->cooldownKey+" native grant nominal="+xp);
            }
        }
        // A shared source category is one authenticated accomplishment,
        // not one award per joined Path. Collect measurements before routing.
        var activityAwards=new java.util.ArrayList<XpSourceRouting.Candidate>();
        for (Definition.Path path : PathRegistry.all().values()) {
            if(!eligiblePath.test(path)||!PathRegistry.eligible(player,path.id())
                    ||!next.get(path.id()).joined())continue;
            for(var award:PathRegistry.config().unifiedSources().getOrDefault(path.id(),List.of())){
                if(!signal.matches(award.event(),award.selector()))continue;
                String first=award.oncePerTarget()
                        ?award.firstCompletionKey(path.id(),signal.target()):null;
                activityAwards.add(new XpSourceRouting.Candidate(award.systemId(),
                        award.category(),first,award.nominal(),false));
            }
        }
        if(activityAwards.isEmpty())io.github.heatwaveqc.tyr.progression.admin.XpTrace.note(player,()->"no eligible configured unified activity matched signal "+signal.target());
        next=ExperienceSourceRouter.batch(player,next,"activity:"+UUID.randomUUID(),activityAwards);
        for (Definition.Path path : PathRegistry.all().values()) {
            if (!eligiblePath.test(path)) continue;
            var channel=PathRegistry.channel(path.id());
            if(channel==null || !PathRegistry.eligible(player,path.id())) continue;
            Track initial=ChannelEngine.view(next,path,PathRegistry.config());
            Track state=initial;
            if (!state.joined()) continue;

            // Use the state at signal start: a single action cannot advance two strict steps.
            state = ProgressionMath.collectMilestoneCriteria(path,initial,signal);
            // A single event may satisfy a chain of previously completed prerequisite counters.
            boolean changed;
            do {
                changed = false;
                for (Definition.Milestone milestone : path.milestones()) {
                    if (state.completed(milestone.id()) || !state.milestones().containsAll(milestone.after())
                            || !ProgressionMath.canCollectMilestone(milestone,initial))
                        continue;
                    boolean ready = true;
                    for (int i = 0; i < milestone.criteria().size(); i++) {
                        if (state.criterionCount(ProgressionMath.counterKey(milestone.id(),i))
                                < milestone.criteria().get(i).count()) {
                            ready = false; break;
                        }
                    }
                    if (ready) {
                        state=ProgressionMath.completeMilestone(state,milestone);
                        // This is a permanent achievement, independent of
                        // current Path membership or purchased skill ranks.
                        next=next.withEconomy(next.economy().withAchievement(milestone.id()));
                        for(String alias:milestone.completionAliases())
                            next=next.withEconomy(next.economy().withAchievement(alias));
                        if(milestone.xp()>0||milestone.points()>0)
                            next=ChannelEngine.grant(next,channel,milestone.xp(),milestone.points());
                        // One permanent receipt per distinct Path trial, independent
                        // of the ordinary event's independent scaled earning.
                        if(milestone.fixedRewardXp()>0) {
                            next=ExperienceSourceRouter.award(player,next,
                                milestone.fixedRewardSystem(),"tyr_progression:path_trial",
                                "trial:"+path.id()+"/"+milestone.id(),
                                path.id()+"/"+milestone.id(),
                                BigDecimal.valueOf(milestone.fixedRewardXp()),true);
                        }
                        changed = true;
                    }
                }
            } while(changed);
            if (!state.equals(initial)) {
                var candidate=MilestoneFeedback.changed(path,initial,state);
                if(candidate!=null && (progressNotice==null || candidate.complete() && !progressNotice.complete()))
                    progressNotice=candidate;
                next=ChannelEngine.ownership(next,path,state,PathRegistry.config());
            }
        }
        if (next != ledger) player.setData(ProgressionAttachments.LEDGER.get(),next);
        // Notifications follow committed server-side state; they never issue XP.
        if(progressNotice!=null)
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(progressNotice.message()),true);
    }

    /**
     * Permanent, non-refundable Tyz-to-native advancement conversion.
     * Each marker can grant exactly one native Monk point over a character's
     * lifetime, even after relog, skill reload, admin grants or later respec.
     * This deliberately does NOT mint Tyz SP or global MXP.
     */
    public static boolean awardMonkTyzPointOnce(ServerPlayer player, String marker) {
        if (!eligibleEarner(player) || marker == null || !marker.matches("tyr_progression:tyz_focused_advancement_[1-5]"))
            return false;
        Ledger ledger = player.getData(ProgressionAttachments.LEDGER.get());
        var path=PathRegistry.get("tyr_progression:monk");
        var channel=PathRegistry.channel("tyr_progression:monk");
        // Legacy native bonus points must never mint a second spendable
        // Monk wallet after the data-defined shared-MXP cutover.
        if(path==null || channel==null || channel.magic()) return false;
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
        if(!eligibleEarner(player)) return false;
        XpChannel channel=PathRegistry.economy(channelId);
        if(channel==null || !channel.magic()) return false;
        if(!Double.isFinite(repeatBase)||repeatBase<0||
                first!=null&&(!Double.isFinite(firstBase)||firstBase<0)) return false;
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        Ledger next=first==null
                ?ExperienceSourceRouter.award(player,prior,channelId,sourceCategory,tx,null,
                        BigDecimal.valueOf(repeatBase),repeatFixed)
                :ExperienceSourceRouter.completion(player,prior,channelId,sourceCategory,tx,first,
                        BigDecimal.valueOf(firstBase),BigDecimal.valueOf(repeatBase),firstFixed,repeatFixed);
        return publishAward(player,prior,next);
    }

    private static boolean eligibleEarner(ServerPlayer player){
        return player!=null&&player.getServer()!=null&&player.getServer().isSameThread()
                &&!(player instanceof FakePlayer)&&!player.isCreative()&&!player.isSpectator()
                &&!NewbieMode.isNewbie(player)
                &&player.getServer().getPlayerList().getPlayer(player.getUUID())==player;
    }

    private static boolean publishAward(ServerPlayer player,Ledger prior,Ledger next){
        if(next==prior)return false;
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
        if(!eligibleEarner(player))return false;
        var catalog=PathRegistry.economyCatalog();
        if(!catalog.systems().containsKey(systemId))return false;
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        return publishAward(player,prior,ExperienceSourceRouter.award(player,prior,systemId,
                sourceCategory,transaction,completionId,nominalXp,fixed));
    }

    /** Explicit destination grant, not a gameplay-source reroute. */
    public static boolean awardExplicitConfiguredXp(ServerPlayer player,String systemId,
            String sourceCategory,String transaction,String completionId,
            BigDecimal nominalXp,boolean fixed) {
        if(!eligibleEarner(player))return false;
        var catalog=PathRegistry.economyCatalog();
        var spec=catalog.systems().get(systemId);
        if(spec==null || PathRegistry.economy(systemId)!=null
                && !PathRegistry.economyEligible(player,systemId))return false;
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        var result=ExperienceModifiers.earn(player,prior,spec,sourceCategory,transaction,
                completionId,nominalXp,fixed,catalog.growth(systemId));
        return result.accepted()&&publishAward(player,prior,prior.withEconomy(result.ledger()));
    }

    /** One authenticated event, potentially several independently configured categories. */
    public static boolean awardSourceBatch(ServerPlayer player,String transaction,
            List<XpSourceRouting.Candidate> candidates) {
        if(!eligibleEarner(player))return false;
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        return publishAward(player,prior,ExperienceSourceRouter.batch(player,prior,transaction,candidates));
    }

    /** Existing trusted server consumers may grant a native-style source award. */
    public static boolean awardExperience(ServerPlayer player,String id,long xp) {
        return awardExperience(player,id,"tyr_progression:server_award",
                "award:"+UUID.randomUUID(),xp);
    }
    /** Source-aware overload ensures repeat callbacks can share one receipt. */
    public static boolean awardExperience(ServerPlayer player,String id,
            String sourceCategory,String transaction,long xp) {
        if(!eligibleEarner(player)||xp<=0||xp>1_000_000L)return false;
        var system=PathRegistry.economy(id);
        if(system==null||!PathRegistry.economyEligible(player,id))return false;
        Ledger prior=player.getData(ProgressionAttachments.LEDGER.get());
        String gate=system.earningPath();
        if(!gate.isEmpty()&&(!prior.get(gate).joined()||!PathRegistry.eligible(player,gate)))return false;
        if(PathRegistry.economyCatalog().systems().containsKey(id))
            return awardConfiguredXp(player,id,sourceCategory,transaction,null,
                    BigDecimal.valueOf(xp),false);
        if(system.magic()||!PathRegistry.config().usesNative(id,path->
                prior.get(path).joined()&&PathRegistry.eligible(player,path)))return false;
        long nominal=io.github.heatwaveqc.tyr.progression.runtime.ExperienceBonuses.apply(player,id,xp);
        player.setData(ProgressionAttachments.LEDGER.get(),
                ChannelEngine.grant(prior,system,nominal,0));
        return true;
    }
    /** Existing legacy source cooldowns; separate from persisted skill activation. */
    public static java.util.Set<String> adminSourceCooldownKeys(ServerPlayer p){
        var keys=new java.util.TreeSet<String>();
        for(var system:PathRegistry.economies().values()){
            if(system.magic())continue;
            for(int i=0;i<system.sources().size();i++)
                if(system.sources().get(i).cooldownTicks()>0)keys.add(system.id()+"/"+i);
        }
        keys.addAll(cooldowns.getOrDefault(p.getUUID(),Map.of()).keySet());
        return java.util.Set.copyOf(keys);
    }
    public static Long adminSourceCooldown(ServerPlayer p,String key){
        return cooldowns.getOrDefault(p.getUUID(),Map.of()).get(key);
    }
    public static void adminResetSourceCooldown(ServerPlayer p,String key,Long expected){
        if(p.getServer()==null||!p.getServer().isSameThread())throw new IllegalStateException("Server thread required");
        if(!java.util.Objects.equals(adminSourceCooldown(p,key),expected))throw new IllegalStateException("Cooldown changed");
        var rows=cooldowns.get(p.getUUID());
        if(rows!=null){rows.remove(key);if(rows.isEmpty())cooldowns.remove(p.getUUID());}
    }
    public static void forgetTransient(UUID playerId) {
        cooldowns.remove(playerId);
        io.github.heatwaveqc.tyr.progression.admin.XpTrace.forget(playerId);
        io.github.heatwaveqc.tyr.progression.runtime.forms.ProviderFlags.forget(playerId);
    }
}
