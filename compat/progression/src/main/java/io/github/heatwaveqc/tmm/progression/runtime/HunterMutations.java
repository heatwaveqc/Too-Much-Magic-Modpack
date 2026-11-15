package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.data.HunterMutationPolicy;
import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.state.HunterMutationState;
import io.github.heatwaveqc.tmm.progression.state.ProgressionAttachments;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/** Server-only mutation progress, permanent effect refresh and food exhaustion. */
public final class HunterMutations {
    private HunterMutations() {}
    private static final String HUNTER = HunterExperience.HUNTER;

    private static String effectId(MobEffectInstance effect) {
        ResourceLocation location = BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value());
        return location == null ? "" : location.toString();
    }

    private static boolean validEffect(HunterMutationPolicy policy, MobEffectInstance effect) {
        if (effect.getDuration() <= 0 || effect.getAmplifier() < 0) return false;
        String id = effectId(effect);
        return !id.isEmpty() && policy.eligible(id,effect.getEffect().value().isInstantenous());
    }

    public static boolean hasEligibleEffect(PotionContents contents) {
        HunterMutationPolicy policy = PathRegistry.hunterMutationPolicy(HUNTER);
        if (policy == null || contents == null) return false;
        for (MobEffectInstance effect : contents.getAllEffects())
            if (validEffect(policy,effect)) return true;
        return false;
    }

    /**
     * Only called by the special brew's server-side finishUsingItem override.
     * Ordinary potions retain their usual effects and separate Hunter XP rules.
     * A multi-effect potion contributes each effect's actual original duration.
     */
    public static boolean drink(ServerPlayer player, ItemStack brewed) {
        HunterMutationPolicy policy = PathRegistry.hunterMutationPolicy(HUNTER);
        if (policy == null || player.isCreative() || player.isSpectator()
            || !ProgressionApi.track(player,HUNTER).joined()
            || !brewed.is(HunterItems.MUTAGENIC_BREW.get())) return false;
        PotionContents contents = brewed.get(DataComponents.POTION_CONTENTS);
        if (!hasEligibleEffect(contents)) return false;
        HunterMutationState old = player.getData(ProgressionAttachments.HUNTER_MUTATIONS.get());
        HunterMutationState next = old;
        List<String> changed = new ArrayList<>();
        for (MobEffectInstance effect : contents.getAllEffects()) {
            if (!validEffect(policy,effect)) continue;
            String id = effectId(effect);
            int amplifier = Math.min(effect.getAmplifier(),policy.maximumAmplifier());
            boolean wasUnlocked = next.unlocked().containsKey(id);
            int oldLevel = next.unlocked().getOrDefault(id,-1);
            HunterMutationState advanced = next.credit(id,amplifier,
                (long) effect.getDuration(),policy.targetDurationTicks());
            if (advanced != next) changed.add(id);
            next = advanced;
            if ((!wasUnlocked || amplifier > oldLevel)
                && next.unlocked().getOrDefault(id,-1) > oldLevel)
                player.displayClientMessage(Component.literal(
                    "Permanent mutation unlocked: " + id + " level " + (amplifier + 1)),false);
        }
        if (next == old) return false; // already capped: do not eat potion for nothing.
        player.setData(ProgressionAttachments.HUNTER_MUTATIONS.get(),next);
        long target = policy.targetDurationTicks();
        for (String id : changed.stream().distinct().limit(3).toList()) {
            String msg = id;
            for (MobEffectInstance effect : contents.getAllEffects())
                if (id.equals(effectId(effect))) {
                    int tier = Math.min(effect.getAmplifier(),policy.maximumAmplifier());
                    msg = id + ": " + next.progress(id,tier) + "/" + target + " ticks";
                    break;
                }
            player.displayClientMessage(Component.literal("Mutation progress: " + msg),true);
        }
        return true;
    }

    public static void tick(ServerPlayer player) {
        if (player.isCreative() || player.isSpectator()
            || !ProgressionApi.track(player,HUNTER).joined()) return;
        HunterMutationPolicy policy = PathRegistry.hunterMutationPolicy(HUNTER);
        if (policy == null) return;
        HunterMutationState state = player.getData(ProgressionAttachments.HUNTER_MUTATIONS.get());
        if (state.unlocked().isEmpty()) return;

        // Count DISTINCT mutation effects, not levels or active potion instances.
        // Called once per 20 server ticks by ProgressionEvents.
        player.causeFoodExhaustion(
            policy.exhaustionPerMutationTick() * 20 * state.unlocked().size());

        // Periodic short refresh rather than a mutable infinite effect. When
        // Hunter eligibility is lost, these expire naturally within ~12 seconds.
        if (player.tickCount % 100 != 0) return;
        for (Map.Entry<String,Integer> mutation : state.unlocked().entrySet()) {
            ResourceLocation id = ResourceLocation.tryParse(mutation.getKey());
            if (id == null || !policy.eligible(mutation.getKey(),false)) continue;
            var effect = BuiltInRegistries.MOB_EFFECT.getHolder(id).orElse(null);
            if (effect == null || effect.value().isInstantenous()) continue;
            int amplifier = Math.min(mutation.getValue(),policy.maximumAmplifier());
            if (amplifier < 0) continue;
            MobEffectInstance existing = player.getEffect(effect);
            if (existing != null && (existing.getAmplifier() > amplifier ||
                (existing.getAmplifier() == amplifier && existing.getDuration() > 100))) continue;
            player.addEffect(new MobEffectInstance(effect,240,amplifier,true,false,true));
        }
    }

    public static String summary(ServerPlayer player) {
        HunterMutationState state = player.getData(ProgressionAttachments.HUNTER_MUTATIONS.get());
        HunterMutationPolicy policy = PathRegistry.hunterMutationPolicy(HUNTER);
        if (policy == null) return "Hunter mutations unavailable (policy not loaded)";
        String unlocked = state.unlocked().entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getKey))
            .limit(12)
            .map(e -> e.getKey() + " " + (e.getValue()+1))
            .reduce((a,b) -> a + ", " + b).orElse("none");
        return "Permanent mutations: " + state.unlocked().size()
            + " | target " + (policy.targetDurationTicks()/20) + " seconds per potency"
            + " | exhaustion/tick " + (policy.exhaustionPerMutationTick() * state.unlocked().size())
            + " | unlocked: " + unlocked;
    }
}
