package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.HunterXpMath;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.HunterXpPolicy;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import io.github.heatwaveqc.tyr.progression.foundation.HunterPreyPolicy;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Hunter-specific rewards; no client-supplied mob HP, item metadata, or XP. */
public final class HunterExperience {
    private HunterExperience() {}
    public static final String HUNTER = "tyr_progression:hunter";
    private static final Map<String,Long> awardedKills=new HashMap<>();
    private static final List<BiPredicate<ServerPlayer,LivingEntity>> KILL_GUARDS = new CopyOnWriteArrayList<>();

    /** Trusted pack adapters may reject player-owned, allied or non-independent proxy victims. Spawn origin alone is not a rejection. */
    public static void registerKillGuard(BiPredicate<ServerPlayer,LivingEntity> guard) {
        KILL_GUARDS.add(java.util.Objects.requireNonNull(guard));
    }

    private static boolean eligibleKill(ServerPlayer hunter,LivingEntity target,HunterXpPolicy policy) {
        boolean invalidHunterOrVictim = hunter.isCreative() || hunter.isSpectator()
            || target instanceof Player || target == hunter;
        boolean excluded = !policy.excludeTag().isEmpty()
            && target.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(policy.excludeTag())));
        boolean included = !policy.includeTag().isEmpty()
            && target.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(policy.includeTag())));
        boolean tame = target instanceof TamableAnimal animal && animal.isTame();
        // A known owner UUID with no resolvable owner is not evidence of enemy allegiance.
        // This denies offline/unloaded player-owned targets without inferring from spawn origin.
        boolean playerOrUnknownOwner = false;
        if (target instanceof OwnableEntity owned && owned.getOwnerUUID() != null) {
            LivingEntity owner = owned.getOwner();
            playerOrUnknownOwner = owner == null || owner instanceof Player;
        }
        if (!HunterPreyPolicy.eligible(included, excluded, invalidHunterOrVictim, tame, playerOrUnknownOwner))
            return false;
        if (HunterNativePreyOwnership.excluded(target)) return false;
        for (BiPredicate<ServerPlayer,LivingEntity> rule : KILL_GUARDS)
            if (!rule.test(hunter,target)) return false;
        return true;
    }

    public static boolean eligibleKill(ServerPlayer player,LivingEntity target){
        return PathRegistry.computedXp().values().stream().anyMatch(policy->eligibleKill(player,target,policy));
    }
    private static boolean permitted(ServerPlayer player,String system){
        var spec=PathRegistry.economy(system);
        return player!=null&&player.getServer()!=null&&player.getServer().isSameThread()
            &&!(player instanceof net.neoforged.neoforge.common.util.FakePlayer)
            &&!player.isCreative()&&!player.isSpectator()&&!NewbieMode.isNewbie(player)
            &&player.getServer().getPlayerList().getPlayer(player.getUUID())==player
            &&spec!=null&&PathRegistry.economyEligible(player,system)&&(spec.earningPath().isEmpty()||(ProgressionApi.track(player,spec.earningPath()).joined()
            &&PathRegistry.eligible(player,spec.earningPath())));
    }
    /** The same configured policy follows its owning Path's selected currency.
     * A native binding retains its legacy scale; a shared binding uses the explicit MXP rates.
     * No simultaneous native/shared payout.
     */
    private static String destination(String system,HunterXpPolicy policy){
        var path=PathRegistry.channel(policy.specialtyPath());
        return path!=null&&path.magic()?path.id():system;
    }
    private static boolean shared(String system,HunterXpPolicy policy){
        return !destination(system,policy).equals(system);
    }
    private record Pending(String system,HunterXpPolicy policy,String legacyTransaction,
            java.math.BigDecimal amount,long legacy,String claim) {}

    /** Preserve native fallback only when no configured route replaces it.
     * One routed action is published once; each successful destination is
     * checked before updating the source's cooldown or milestone provenance. */
    private static java.util.Set<String> payBatch(ServerPlayer player,String category,
            String transaction,java.util.List<Pending> pending){
        var accepted=new java.util.HashSet<String>();
        var catalog=PathRegistry.economyCatalog();
        var routing=catalog.routing();
        var candidates=new java.util.ArrayList<io.github.heatwaveqc.tyr.progression.data.XpSourceRouting.Candidate>();
        var configured=new java.util.ArrayList<Pending>();
        for(var p:pending){
            String to=destination(p.system(),p.policy());
            if(shared(p.system(),p.policy())||catalog.systems().containsKey(to)||routing.routes().containsKey(category)){
                var amount=shared(p.system(),p.policy())?p.amount():java.math.BigDecimal.valueOf(p.legacy());
                if(amount.signum()<=0)continue;
                candidates.add(new io.github.heatwaveqc.tyr.progression.data.XpSourceRouting.Candidate(
                        to,category,null,amount,false));
                configured.add(p);
            }else if(p.legacy()>0&&ProgressionApi.awardExperience(player,p.system(),category,
                    p.legacyTransaction(),p.legacy()))accepted.add(p.system());
        }
        var attachment=io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments.LEDGER.get();
        var before=player.getData(attachment).economy().recentTransactions();
        ProgressionApi.awardSourceBatch(player,transaction,candidates);
        var after=player.getData(attachment).economy().recentTransactions();
        for(var p:configured){
            for(var d:routing.destinations(category,destination(p.system(),p.policy()))){
                String receipt=d.system()+"|"+category+"|"+transaction;
                if(!before.contains(receipt)&&after.contains(receipt)){
                    accepted.add(p.system());
                    break;
                }
            }
        }
        return java.util.Set.copyOf(accepted);
    }
    private static boolean chosenSpecialtyMatches(ServerPlayer player,LivingEntity target,HunterXpPolicy policy) {
        return chosenSpecialtyMatches(player,selector->Selectors.entity(selector,target),policy);
    }
    private static boolean chosenSpecialtyMatches(ServerPlayer player,java.util.function.Predicate<String> matches,HunterXpPolicy policy) {
        Definition.Path path=PathRegistry.get(policy.specialtyPath());
        if(path==null||!PathRegistry.eligible(player,path.id()))return false;
        var state=ProgressionApi.track(player,path.id());
        for (Definition.Skill skill : path.skills()) {
            if (state.skillRank(skill.id()) <= 0
                || !policy.specialtyGroup().equals(skill.choiceGroup())) continue;
            for(int i=0;i<skill.effects().size();i++){
                Definition.Effect effect=skill.effects().get(i);
                if (PathRegistry.effectEligible(player,path.id(),skill.id(),i) && "hunter_specialty".equals(effect.type()) && matches.test(effect.selector()))
                    return true; // double at most once, even for a creature with multiple lineage tags
            }
        }
        return false;
    }

    /**
     * Selected specialty grants effective Looting I against that creature type.
     * Same admissibility as Hunter XP: no players, friendly tames, automated
     * proxy targets or excluded mob classes. Actual killing player is checked
     * separately by NativeLootBonuses against the loot DamageSource.
     */
    public static int favoredLootingBonus(ServerPlayer player, LivingEntity victim) {
        int bonus=0;
        for(var entry:PathRegistry.computedXp().entrySet()){
            if(permitted(player,entry.getKey())&&eligibleKill(player,victim,entry.getValue())
                &&chosenSpecialtyMatches(player,victim,entry.getValue()))bonus=Math.max(bonus,LootBonusMath.favoredLooting(true,true,true));
        }
        return bonus;
    }

    /** Data-defined, not hardcoded into the command registration. */
    public static java.util.List<String> availableSpecialties() {
        Definition.Path path = PathRegistry.get(HUNTER);
        if (path == null) return java.util.List.of();
        return path.skills().stream()
            .filter(skill -> "tyr_progression:hunter_favored_enemy".equals(skill.choiceGroup())
                && skill.id().startsWith("tyr_progression:specialty_"))
            .map(skill -> skill.id().substring("tyr_progression:specialty_".length()))
            .sorted().toList();
    }

    public static String selectedSpecialty(ServerPlayer player) {
        Definition.Path path = PathRegistry.get(HUNTER);
        if (path == null) return "none";
        var state = ProgressionApi.track(player,HUNTER);
        return path.skills().stream()
            .filter(skill -> "tyr_progression:hunter_favored_enemy".equals(skill.choiceGroup())
                && state.skillRank(skill.id()) > 0)
            .map(skill -> skill.id().replace("tyr_progression:specialty_",""))
            .findFirst().orElse("none");
    }

    /** Returns qualifying configured enrollment paths, for milestone provenance filtering. */
    public static java.util.Set<String> kill(ServerPlayer player,LivingEntity target){
        var credited=new java.util.HashSet<String>();
        var pending=new java.util.ArrayList<Pending>();
        long now=player.server.overworld().getGameTime();
        if(awardedKills.size()>8192)awardedKills.entrySet().removeIf(entry->entry.getValue()>now||now-entry.getValue()>24000);
        for(var entry:PathRegistry.computedXp().entrySet()){
            String system=entry.getKey();var policy=entry.getValue();
            if(!permitted(player,system)||!eligibleKill(player,target,policy))continue;
            String victim=system+"|"+target.getUUID();
            if(awardedKills.containsKey(victim))continue;
            boolean favored=chosenSpecialtyMatches(player,target,policy);
            double hp=VerifiedCombatXp.baselineHealth(target);
            long legacy=HunterXpMath.killXp(target.getMaxHealth(),policy.healthDivisor(),policy.specialtyMultiplier(),policy.minKillXp(),favored);
            var mxp=HunterXpMath.combatMxp(hp,policy.mxpKillPerHp(),policy.specialtyMultiplier(),favored);
            pending.add(new Pending(system,policy,"hunter_kill:"+system+":"+target.getUUID(),mxp,legacy,victim));
        }
        var paid=payBatch(player,"tyr_progression:hunter_kill","hunter_kill:"+target.getUUID(),pending);
        for(var p:pending){
            if(!paid.contains(p.system()))continue;
            awardedKills.put(p.claim(),now);
            String path=PathRegistry.economy(p.system()).earningPath();if(!path.isEmpty())credited.add(path);
        }
        return java.util.Set.copyOf(credited);
    }
    /** Trusted native White arena victory receipt; exactly one aggregate reward per eligible winner. */
    public static void cosmicCrystalArenaVictory(ServerPlayer player,double aggregateMaxHealth,String tx){
        if(!Double.isFinite(aggregateMaxHealth)||aggregateMaxHealth<=0||tx==null||!tx.startsWith("boss:cosmic_crystal:")
            ||player instanceof net.neoforged.neoforge.common.util.FakePlayer||player.isCreative()||player.isSpectator()
            ||!player.server.isSameThread()||player.server.getPlayerList().getPlayer(player.getUUID())!=player)return;
        ResourceLocation id=ResourceLocation.parse("the_bumblezone:cosmic_crystal_entity");
        var type=BuiltInRegistries.ENTITY_TYPE.get(id);
        if(!id.equals(BuiltInRegistries.ENTITY_TYPE.getKey(type)))return;
        var pending=new java.util.ArrayList<Pending>();
        long now=player.server.overworld().getGameTime();
        if(awardedKills.size()>8192)awardedKills.entrySet().removeIf(entry->entry.getValue()>now||now-entry.getValue()>24000);
        for(var entry:PathRegistry.computedXp().entrySet()){
            String system=entry.getKey();var policy=entry.getValue();
            if(!HUNTER.equals(policy.specialtyPath())||!permitted(player,system))continue;
            String claim=system+"|"+tx+"|"+player.getUUID();
            if(awardedKills.containsKey(claim))continue;
            boolean specialty=chosenSpecialtyMatches(player,selector->selector.isEmpty()||selector.equals("*")||
                (selector.startsWith("#")?type.is(TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse(selector.substring(1)))):id.toString().equals(selector)),policy);
            long xp=HunterXpMath.killXp(aggregateMaxHealth,policy.healthDivisor(),policy.specialtyMultiplier(),policy.minKillXp(),specialty);
            var mxp=HunterXpMath.combatMxp(aggregateMaxHealth,policy.mxpKillPerHp(),policy.specialtyMultiplier(),specialty);
            pending.add(new Pending(system,policy,"hunter_arena:"+system+":"+tx+":"+player.getUUID(),mxp,xp,claim));
        }
        var paid=payBatch(player,"tyr_progression:hunter_kill","hunter_arena:"+tx+":"+player.getUUID(),pending);
        for(var p:pending)if(paid.contains(p.system()))awardedKills.put(p.claim(),now);
    }
    public static boolean allowsMilestone(String path,java.util.Set<String> credited){
        boolean restricted=PathRegistry.computedXp().keySet().stream().anyMatch(id->path.equals(PathRegistry.economy(id).earningPath()));
        return !restricted||credited.contains(path);
    }

    /**
     * Only completed drinking of a curated registered base potion grants XP.
     * The player cannot change a PotionContents custom component to mint extra XP.
     * Saved claim times are overworld game ticks, not volatile session-only timers.
     */
    public static void potionFinished(ServerPlayer player, ItemStack consumed) {
        if(player.isCreative()||player.isSpectator()||consumed.isEmpty())return;
        if (!"minecraft:potion".equals(BuiltInRegistries.ITEM.getKey(consumed.getItem()).toString())) return;
        settlePotion(player,consumed.get(DataComponents.POTION_CONTENTS));
    }
    /** Only called after HunterMutations.drink committed actual progress. */
    public static void mutagenFinished(ServerPlayer player,ItemStack consumed){
        if(!consumed.is(HunterItems.MUTAGENIC_BREW.get()))return;
        PotionContents contents=consumed.get(DataComponents.POTION_CONTENTS);
        if(StandPotionEligibility.eligible(contents))settlePotion(player,contents);
    }
    private static void settlePotion(ServerPlayer player,PotionContents contents){
        var pending=new java.util.ArrayList<Pending>();
        for(var entry:PathRegistry.computedXp().entrySet())
            if(permitted(player,entry.getKey())){
                var p=potionCandidate(player,contents,entry.getKey(),entry.getValue());
                if(p!=null)pending.add(p);
            }
        if(pending.isEmpty())return;
        String potionId=contents.potion().flatMap(holder->holder.unwrapKey())
                .map(key->key.location().toString()).orElse("");
        long now=player.server.overworld().getGameTime();
        String transaction="hunter_potion:"+UUID.nameUUIDFromBytes(
                (potionId+"|"+now).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        var paid=payBatch(player,"tyr_progression:hunter_potion",transaction,pending);
        for(var p:pending)if(paid.contains(p.system()))ProgressionApi.setMode(player,p.claim(),Long.toString(now));
    }
    private static Pending potionCandidate(ServerPlayer player,PotionContents contents,String system,HunterXpPolicy policy){
        if (contents == null || !contents.customEffects().isEmpty()) return null;
        String potionId = contents.potion()
            .flatMap(holder -> holder.unwrapKey())
            .map(key -> key.location().toString()).orElse("");
        int difficulty = policy.difficulty(potionId);
        if (difficulty < 1) return null; // water/awkward/mundane/unknown cannot create XP
        int highestAmplifier = 0, longestDuration = 0;
        boolean hasEffect = false;
        for (var effect : contents.getAllEffects()) {
            hasEffect = true;
            highestAmplifier = Math.max(highestAmplifier,effect.getAmplifier());
            longestDuration = Math.max(longestDuration,effect.getDuration());
        }
        if (!hasEffect) return null;
        String claim = "tyr_progression:potion_claim/" + system.replace(':','/')+"/"+potionId.replace(':','/');
        // Refuse namespaced recipes whose claim cannot be persisted: no uncapped repeat XP.
        if (claim.length() > 128 || !claim.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) return null;
        String stored = ProgressionApi.mode(player,claim,"");
        boolean discovery = stored.isEmpty();
        long now = player.server.overworld().getGameTime();
        if (!discovery) {
            long last;
            try { last = Long.parseLong(stored); }
            catch (NumberFormatException bad) { return null; } // invalid state fails closed
            long cooldown = Math.max(policy.potionRepeatCooldownTicks(),longestDuration);
            if (now < last || now - last < cooldown) return null;
        }
        long xp = HunterXpMath.potionXp(policy.potionBaseXp(),difficulty,highestAmplifier,
            longestDuration,policy.potionDiscoveryMultiplier(),discovery);
        var mxp=HunterXpMath.alchemyMxp(policy.mxpPotionBaseXp(),difficulty,highestAmplifier,
            longestDuration,policy.potionDiscoveryMultiplier(),discovery);
        String tx="hunter_potion:"+UUID.nameUUIDFromBytes((claim+"|"+now).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return new Pending(system,policy,tx,mxp,xp,claim);
    }

    public static void forgetTransient(UUID hunter) {
        // Victim UUIDs are global to the running server, so a player's logout must
        // not erase another player's anti-double-award state.
    }
}
