package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.core.MutationPowerPolicy;
import io.github.heatwaveqc.tyr.progression.data.HunterMutationPolicy;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.state.HunterMutationState;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
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

/** Saved mutation progress, derived passive potency and original metabolic cost. */
public final class HunterMutations {
    private HunterMutations() {}
    /** A brew has no owner selector yet: refuse ambiguous eligible configurations. */
    public static List<String> eligiblePaths(ServerPlayer player){
        return PathRegistry.mutationPolicies().keySet().stream()
            .filter(id->ProgressionApi.track(player,id).joined()&&PathRegistry.eligible(player,id))
            .sorted().toList();
    }
    private static HunterMutationState state(ServerPlayer player,String path){
        return player.getData(ProgressionAttachments.MUTATIONS.get()).paths().getOrDefault(path,HunterMutationState.empty());
    }

    private static String effectId(MobEffectInstance effect) {
        ResourceLocation location = BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value());
        return location == null ? "" : location.toString();
    }

    private static boolean validEffect(HunterMutationPolicy policy, MobEffectInstance effect) {
        if (effect.getDuration() <= 0 || effect.getAmplifier() < 0) return false;
        String id = effectId(effect);
        return MutationPowerPolicy.supported(id) && policy.eligible(id,effect.getEffect().value().isInstantenous());
    }

    public static boolean hasEligibleEffect(PotionContents contents) {
        if(contents==null)return false;
        for(var policy:PathRegistry.mutationPolicies().values())
            for(MobEffectInstance effect:contents.getAllEffects())
                if(validEffect(policy,effect))return true;
        return false;
    }

    /**
     * Only called by the special brew's server-side finishUsingItem override.
     * Ordinary potions retain their usual effects and separate Hunter XP rules.
     * A multi-effect potion contributes each effect's actual original duration.
     */
    public static boolean drink(ServerPlayer player, ItemStack brewed) {
        var owners=eligiblePaths(player);
        if(owners.size()!=1||player.isCreative()||player.isSpectator()||!brewed.is(HunterItems.MUTAGENIC_BREW.get()))return false;
        String owner=owners.getFirst();
        HunterMutationPolicy policy=PathRegistry.hunterMutationPolicy(owner);
        PotionContents contents = brewed.get(DataComponents.POTION_CONTENTS);
        // Defense in depth: a mutated/command-granted brew cannot bypass the
        // brewing-stand source gate simply by carrying POTION_CONTENTS.
        if (!StandPotionEligibility.eligible(contents) || !hasEligibleEffect(contents)) return false;
        HunterMutationState old = state(player,owner);
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
        player.setData(ProgressionAttachments.MUTATIONS.get(),player.getData(ProgressionAttachments.MUTATIONS.get()).with(owner,next));
        MutationPowers.reconcile(player);
        PassivePowers.reconcile(player,true);
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

    /** Highest eligible potency across Paths; no duplicate mutation or second persistence layer. */
    public static Map<String,Integer> active(ServerPlayer player){
        if(!player.isAlive()||player.isCreative()||player.isSpectator()||NewbieMode.isNewbie(player)
            ||player instanceof net.neoforged.neoforge.common.util.FakePlayer)return Map.of();
        Map<String,Integer> result=new java.util.TreeMap<>();
        for(String owner:eligiblePaths(player)){
            HunterMutationPolicy policy=PathRegistry.hunterMutationPolicy(owner);
            for(var mutation:state(player,owner).unlocked().entrySet()){
                String key=mutation.getKey();
                if(!MutationPowerPolicy.supported(key)||!policy.eligible(key,false))continue;
                var holder=BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.parse(key)).orElse(null);
                int amplifier=Math.min(mutation.getValue(),policy.maximumAmplifier());
                if(holder!=null&&!holder.value().isInstantenous()&&amplifier>=0)
                    result.merge(key,amplifier,Math::max);
            }
        }
        return Map.copyOf(result);
    }
    public static void tick(ServerPlayer player) {
        if(player.isCreative()||player.isSpectator()||!player.isAlive()||NewbieMode.isNewbie(player))return;
        // Charge each active mutation once; overlapping policy sources do not stack.
        Map<String,Float> costs=new java.util.HashMap<>();
        var active=active(player);
        for(String owner:eligiblePaths(player)){
            var policy=PathRegistry.hunterMutationPolicy(owner);
            for(String id:state(player,owner).unlocked().keySet())
                if(active.containsKey(id)&&policy.eligible(id,false))
                    costs.merge(id,policy.exhaustionPerMutationTick(),Math::max);
        }
        float cost=0;
        for(float value:costs.values())cost+=value;
        if(cost>0)player.causeFoodExhaustion(cost*20);
    }

    public static String summary(ServerPlayer player) {
        var owners=eligiblePaths(player);
        if(owners.size()!=1)return owners.isEmpty()?"No eligible mutation policy":"Multiple eligible mutation policies; select an owner in data";
        String owner=owners.getFirst();
        HunterMutationState state=state(player,owner);
        HunterMutationPolicy policy=PathRegistry.hunterMutationPolicy(owner);
        String unlocked = state.unlocked().entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getKey))
            .limit(12)
            .map(e -> e.getKey() + " " + (e.getValue()+1))
            .reduce((a,b) -> a + ", " + b).orElse("none");
        return "Permanent mutations: " + state.unlocked().size()
            + " | target " + (policy.targetDurationTicks()/20) + " seconds per potency"
            + " | exhaustion/tick " + (policy.exhaustionPerMutationTick() * state.unlocked().size())
            + " | unsupported/dormant: " + state.unlocked().keySet().stream().filter(id->!MutationPowerPolicy.supported(id)).count()
            + " | unlocked: " + unlocked;
    }
}
