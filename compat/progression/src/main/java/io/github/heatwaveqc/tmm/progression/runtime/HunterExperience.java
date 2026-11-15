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
    private static final TagKey<net.minecraft.world.entity.EntityType<?>> EXTRA_PREY =
        TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse("tmm_progression:hunt_eligible"));
    private static final TagKey<net.minecraft.world.entity.EntityType<?>> NO_PREY =
        TagKey.create(Registries.ENTITY_TYPE,ResourceLocation.parse("tmm_progression:hunt_excluded"));
    private static final Map<UUID,Long> awardedKills = new HashMap<>();
    private static final List<BiPredicate<ServerPlayer,LivingEntity>> KILL_GUARDS = new CopyOnWriteArrayList<>();

    /** Trusted pack adapters may reject summoned, player-owned or automation-created victims. */
    public static void registerKillGuard(BiPredicate<ServerPlayer,LivingEntity> guard) {
        KILL_GUARDS.add(java.util.Objects.requireNonNull(guard));
    }

    public static boolean eligibleKill(ServerPlayer hunter, LivingEntity target) {
        if (hunter.isCreative() || hunter.isSpectator() || target instanceof Player || target == hunter)
            return false;
        if (target instanceof TamableAnimal animal && animal.isTame()) return false;
        if (target.getType().is(NO_PREY)) return false;
        if (target.getType().getCategory() != MobCategory.MONSTER && !target.getType().is(EXTRA_PREY))
            return false;
        for (BiPredicate<ServerPlayer,LivingEntity> rule : KILL_GUARDS)
            if (!rule.test(hunter,target)) return false;
        return true;
    }

    private static boolean chosenSpecialtyMatches(ServerPlayer player, LivingEntity target) {
        Definition.Path path = PathRegistry.get(HUNTER);
        if (path == null) return false;
        var state = ProgressionApi.track(player,HUNTER);
        for (Definition.Skill skill : path.skills()) {
            if (state.skillRank(skill.id()) <= 0
                || !"tmm_progression:hunter_favored_enemy".equals(skill.choiceGroup())) continue;
            for (Definition.Effect effect : skill.effects())
                if ("hunter_specialty".equals(effect.type()) && Selectors.entity(effect.selector(),target))
                    return true; // double at most once, even for a creature with multiple lineage tags
        }
        return false;
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

    /** Returns true only when a qualified creature can count toward Hunter milestones. */
    public static boolean kill(ServerPlayer player, LivingEntity target) {
        HunterXpPolicy policy = PathRegistry.hunterXp(HUNTER);
        if (policy == null || !ProgressionApi.track(player,HUNTER).joined()
            || !eligibleKill(player,target)) return false;
        long now = player.server.overworld().getGameTime();
        if (awardedKills.size() > 8192)
            awardedKills.entrySet().removeIf(entry -> entry.getValue() > now || now - entry.getValue() > 24000);
        UUID victim = target.getUUID();
        if (awardedKills.containsKey(victim)) return false;
        long xp = HunterXpMath.killXp(target.getMaxHealth(),policy.healthDivisor(),
            policy.specialtyMultiplier(),policy.minKillXp(),chosenSpecialtyMatches(player,target));
        if (xp <= 0 || !ProgressionApi.awardServerXp(player,HUNTER,xp)) return false;
        awardedKills.put(victim,now);
        return true;
    }

    /**
     * Only completed drinking of a curated registered base potion grants XP.
     * The player cannot change a PotionContents custom component to mint extra XP.
     * Saved claim times are overworld game ticks, not volatile session-only timers.
     */
    public static void potionFinished(ServerPlayer player, ItemStack consumed) {
        HunterXpPolicy policy = PathRegistry.hunterXp(HUNTER);
        if (policy == null || !ProgressionApi.track(player,HUNTER).joined()
            || player.isCreative() || player.isSpectator() || consumed.isEmpty()) return;
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
        String claim = "tmm_progression:potion_claim/" + potionId.replace(':','/');
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
        if (ProgressionApi.awardServerXp(player,HUNTER,xp))
            ProgressionApi.setMode(player,claim,Long.toString(now));
    }

    public static void forgetTransient(UUID hunter) {
        // Victim UUIDs are global to the running server, so a player's logout must
        // not erase another player's anti-double-award state.
    }
}
