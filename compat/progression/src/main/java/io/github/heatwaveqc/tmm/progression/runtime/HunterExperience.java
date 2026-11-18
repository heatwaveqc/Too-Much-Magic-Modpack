package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.HunterXpMath;
import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.data.HunterXpPolicy;
import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
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
    public static final String HUNTER = "tmm_progression:hunter";
    private static final Map<String,Long> awardedKills=new HashMap<>();
    private static final List<BiPredicate<ServerPlayer,LivingEntity>> KILL_GUARDS = new CopyOnWriteArrayList<>();

    /** Trusted pack adapters may reject summoned, player-owned or automation-created victims. */
    public static void registerKillGuard(BiPredicate<ServerPlayer,LivingEntity> guard) {
        KILL_GUARDS.add(java.util.Objects.requireNonNull(guard));
    }

    private static boolean eligibleKill(ServerPlayer hunter,LivingEntity target,HunterXpPolicy policy) {
        if (hunter.isCreative() || hunter.isSpectator() || target instanceof Player || target == hunter)
            return false;
        if (target instanceof TamableAnimal animal && animal.isTame()) return false;
        if(!policy.excludeTag().isEmpty()&&target.getType().is(TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse(policy.excludeTag()))))return false;
        boolean included=!policy.includeTag().isEmpty()&&target.getType().is(TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse(policy.includeTag())));
        if(target.getType().getCategory()!=MobCategory.MONSTER&&!included)return false;
        for (BiPredicate<ServerPlayer,LivingEntity> rule : KILL_GUARDS)
            if (!rule.test(hunter,target)) return false;
        return true;
    }

    public static boolean eligibleKill(ServerPlayer player,LivingEntity target){
        return PathRegistry.computedXp().values().stream().anyMatch(policy->eligibleKill(player,target,policy));
    }
    private static boolean permitted(ServerPlayer player,String system){
        var spec=PathRegistry.economy(system);
        return spec!=null&&(spec.earningPath().isEmpty()||(ProgressionApi.track(player,spec.earningPath()).joined()
            &&PathRegistry.eligible(player,spec.earningPath())));
    }
    private static boolean chosenSpecialtyMatches(ServerPlayer player,LivingEntity target,HunterXpPolicy policy) {
        Definition.Path path=PathRegistry.get(policy.specialtyPath());
        if(path==null||!PathRegistry.eligible(player,path.id()))return false;
        var state=ProgressionApi.track(player,path.id());
        for (Definition.Skill skill : path.skills()) {
            if (state.skillRank(skill.id()) <= 0
                || !policy.specialtyGroup().equals(skill.choiceGroup())) continue;
            for(int i=0;i<skill.effects().size();i++){
                Definition.Effect effect=skill.effects().get(i);
                if (PathRegistry.effectEligible(player,path.id(),skill.id(),i) && "hunter_specialty".equals(effect.type()) && Selectors.entity(effect.selector(),target))
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
            .filter(skill -> "tmm_progression:hunter_favored_enemy".equals(skill.choiceGroup())
                && skill.id().startsWith("tmm_progression:specialty_"))
            .map(skill -> skill.id().substring("tmm_progression:specialty_".length()))
            .sorted().toList();
    }

    public static String selectedSpecialty(ServerPlayer player) {
        Definition.Path path = PathRegistry.get(HUNTER);
        if (path == null) return "none";
        var state = ProgressionApi.track(player,HUNTER);
        return path.skills().stream()
            .filter(skill -> "tmm_progression:hunter_favored_enemy".equals(skill.choiceGroup())
                && state.skillRank(skill.id()) > 0)
            .map(skill -> skill.id().replace("tmm_progression:specialty_",""))
            .findFirst().orElse("none");
    }

    /** Returns qualifying configured enrollment paths, for milestone provenance filtering. */
    public static java.util.Set<String> kill(ServerPlayer player,LivingEntity target){
        var credited=new java.util.HashSet<String>();
        long now=player.server.overworld().getGameTime();
        if(awardedKills.size()>8192)awardedKills.entrySet().removeIf(entry->entry.getValue()>now||now-entry.getValue()>24000);
        for(var entry:PathRegistry.computedXp().entrySet()){
            String system=entry.getKey();var policy=entry.getValue();
            if(!permitted(player,system)||!eligibleKill(player,target,policy))continue;
            String victim=system+"|"+target.getUUID();
            if(awardedKills.containsKey(victim))continue;
            long xp=HunterXpMath.killXp(target.getMaxHealth(),policy.healthDivisor(),policy.specialtyMultiplier(),
               policy.minKillXp(),chosenSpecialtyMatches(player,target,policy));
            if(xp<=0||!ProgressionApi.awardExperience(player,system,xp))continue;
            awardedKills.put(victim,now);
            String path=PathRegistry.economy(system).earningPath();if(!path.isEmpty())credited.add(path);
        }
        return java.util.Set.copyOf(credited);
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
        for(var entry:PathRegistry.computedXp().entrySet())
            if(permitted(player,entry.getKey()))potionFinished(player,consumed,entry.getKey(),entry.getValue());
    }
    private static void potionFinished(ServerPlayer player,ItemStack consumed,String system,HunterXpPolicy policy){
        if (!"minecraft:potion".equals(BuiltInRegistries.ITEM.getKey(consumed.getItem()).toString())) return;
        PotionContents contents = consumed.get(DataComponents.POTION_CONTENTS);
        if (contents == null || !contents.customEffects().isEmpty()) return;
        String potionId = contents.potion()
            .flatMap(holder -> holder.unwrapKey())
            .map(key -> key.location().toString()).orElse("");
        int difficulty = policy.difficulty(potionId);
        if (difficulty < 1) return; // water/awkward/mundane/unknown cannot create XP
        int highestAmplifier = 0, longestDuration = 0;
        boolean hasEffect = false;
        for (var effect : contents.getAllEffects()) {
            hasEffect = true;
            highestAmplifier = Math.max(highestAmplifier,effect.getAmplifier());
            longestDuration = Math.max(longestDuration,effect.getDuration());
        }
        if (!hasEffect) return;
        String claim = "tmm_progression:potion_claim/" + system.replace(':','/')+"/"+potionId.replace(':','/');
        // Refuse namespaced recipes whose claim cannot be persisted: no uncapped repeat XP.
        if (claim.length() > 128 || !claim.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) return;
        String stored = ProgressionApi.mode(player,claim,"");
        boolean discovery = stored.isEmpty();
        long now = player.server.overworld().getGameTime();
        if (!discovery) {
            long last;
            try { last = Long.parseLong(stored); }
            catch (NumberFormatException bad) { return; } // invalid state fails closed
            long cooldown = Math.max(policy.potionRepeatCooldownTicks(),longestDuration);
            if (now < last || now - last < cooldown) return;
        }
        long xp = HunterXpMath.potionXp(policy.potionBaseXp(),difficulty,highestAmplifier,
            longestDuration,policy.potionDiscoveryMultiplier(),discovery);
        if (ProgressionApi.awardExperience(player,system,xp))
            ProgressionApi.setMode(player,claim,Long.toString(now));
    }

    public static void forgetTransient(UUID hunter) {
        // Victim UUIDs are global to the running server, so a player's logout must
        // not erase another player's anti-double-award state.
    }
}
