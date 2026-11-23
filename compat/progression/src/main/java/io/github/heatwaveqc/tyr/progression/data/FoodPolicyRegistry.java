package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import io.github.heatwaveqc.tyr.progression.network.FoodPreviewWire;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Atomic server reload of both tag-first dietary policy and generated source
 * evidence. No global food component mutation and no hardcoded item diet list.
 */
public final class FoodPolicyRegistry extends SimpleJsonResourceReloadListener {
    private record State(FoodPolicyRules rules,
                         Map<String, FoodSourceEvidence> sourceEvidence,
                         Map<String, FoodRecipeEvidence> recipeEvidence,
                         Map<String, StrictDietPolicy> strictPolicies,
                         long generation) {
        private State {
            sourceEvidence = Map.copyOf(sourceEvidence);
            recipeEvidence = Map.copyOf(recipeEvidence);
            strictPolicies = Map.copyOf(strictPolicies);
        }
    }
    private static volatile State active;
    private static final AtomicLong NEXT_GENERATION = new AtomicLong();

    /** Reload generation; does not depend on an operator-invoked food census. */
    public static long generation() {
        State state = active;
        return state == null ? 0 : state.generation();
    }

    public FoodPolicyRegistry() { super(new Gson(), "tyr_progression/food_policies"); }

    public static Optional<FoodPolicyRules> current() {
        State state = active;
        return state == null ? Optional.empty() : Optional.of(state.rules());
    }

    /** Live item tags: pack-provided tags remain authoritative after /reload. */
    public static Set<String> tags(ItemStack stack) {
        return stack.getTags().map(tag -> "#" + tag.location())
            .collect(Collectors.toUnmodifiableSet());
    }

    private static Set<String> effectiveTags(ItemStack stack, FoodSourceEvidence source) {
        Set<String> tags = new HashSet<>(tags(stack));
        if (source != null) tags.addAll(source.inferredTags());
        return Set.copyOf(tags);
    }

    /** A generated provenance entry may add virtual tags without rewriting registry tags. */
    public static boolean universallyPermitted(ItemStack stack) {
        State state = active;
        if (state == null || stack.isEmpty()) return false;
        FoodSourceEvidence source = state.sourceEvidence().get(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        return state.rules().universallyPermitted(effectiveTags(stack, source));
    }

    /** Explicit source units from a trusted recipe graph, for integration adapters. */
    public static Optional<FoodPolicyRules.Nutrition> evaluate(
            String profile, ItemStack stack, double rawSourceUnits, Double preferredShare) {
        State state = active;
        if (state == null || stack.isEmpty()) return Optional.empty();
        FoodSourceEvidence source =
            state.sourceEvidence().get(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        return state.rules().evaluate(profile, effectiveTags(stack, source),
            rawSourceUnits, preferredShare);
    }

    /**
     * Shared policy decision for an authorized set of native/form/Path profiles.
     * Only component-backed food is interpreted; custom-use items are still
     * owned by their native mod and remain outside this nutrition projection.
     */
    public static FoodDecisionResolver.Decision decide(Set<String> profiles, ItemStack stack) {
        State state = active;
        if (state == null || stack.isEmpty() || stack.get(DataComponents.FOOD) == null)
            return FoodDecisionResolver.Decision.nativeResult();
        String item = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        return resolver(state, input -> {
            if (input.equals(item)) return tags(stack);
            ResourceLocation id = ResourceLocation.tryParse(input);
            if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) return Set.of();
            return tags(BuiltInRegistries.ITEM.get(id).getDefaultInstance());
        }).decide(profiles, item);
    }

    private static FoodDecisionResolver resolver(State state,
            java.util.function.Function<String, Set<String>> observedTags) {
        return new FoodDecisionResolver(state.rules(), state.strictPolicies(),
            state.sourceEvidence(), state.recipeEvidence(), observedTags);
    }

    /** Preserve the previously published single-profile nutrition adapter. */
    public static Optional<FoodPolicyRules.Nutrition> evaluateSource(
            String profile, ItemStack stack) {
        if (profile == null) return Optional.empty();
        FoodDecisionResolver.Decision decision = decide(Set.of(profile), stack);
        return decision.outcome() == FoodDecisionResolver.Outcome.NUTRITION
            ? Optional.of(decision.nutrition()) : Optional.empty();
    }

    /**
     * Build the exact same server-derived decision for every native FOOD item
     * from a single atomically loaded policy snapshot. When strict profiles
     * apply, explicit denies are transmitted too, even without source recipes.
     * The client never runs its own food policy or imports provenance.
     */
    public static Map<String, FoodPreviewWire.Entry> clientPreview(Set<String> profiles) {
        State state = active;
        if (state == null || profiles == null || profiles.isEmpty()) return Map.of();
        var evaluator = resolver(state, input -> {
            ResourceLocation id = ResourceLocation.tryParse(input);
            if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) return Set.of();
            return tags(BuiltInRegistries.ITEM.get(id).getDefaultInstance());
        });
        Map<String, FoodPreviewWire.Entry> projected = new HashMap<>();
        for (ResourceLocation id : BuiltInRegistries.ITEM.keySet()) {
            ItemStack stack = BuiltInRegistries.ITEM.get(id).getDefaultInstance();
            if (stack.get(DataComponents.FOOD) == null) continue;
            var decision = evaluator.decide(profiles, id.toString());
            switch (decision.outcome()) {
                case NATIVE -> { }
                case DENY -> projected.put(id.toString(), FoodPreviewWire.Entry.deny());
                case PERMIT_NATIVE -> projected.put(id.toString(), FoodPreviewWire.Entry.permitNative());
                case NUTRITION -> projected.put(id.toString(), FoodPreviewWire.Entry.nourishment(
                    decision.nutrition().hunger(), decision.nutrition().saturationModifier()));
            }
        }
        return Map.copyOf(projected);
    }

    /** Existing dragon-only client call; hybrid profiles use the set overload. */
    public static Map<String, FoodPreviewWire.Entry> clientPreview(String profile) {
        return profile == null ? Map.of() : clientPreview(Set.of(profile));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data,
                         ResourceManager manager, ProfilerFiller profiler) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("tyr_progression", "default");
        JsonElement raw = data.get(id);
        if (raw == null) throw new IllegalArgumentException("Missing bundled food policy " + id);
        FoodPolicyRules rules = FoodPolicyRules.parse(raw.getAsJsonObject());

        Map<String, FoodSourceEvidence> sources = new HashMap<>();
        final String directory = "tyr_progression/food_sources";
        for (var resource : manager.listResources(directory,
                    key -> key.getPath().endsWith(".json")).entrySet()) {
            try (var reader = resource.getValue().openAsReader()) {
                FoodSourceEvidence evidence =
                    FoodSourceEvidence.parse(JsonParser.parseReader(reader).getAsJsonObject(),
                        rules.profiles().keySet());
                FoodSourceEvidence old = sources.putIfAbsent(evidence.item(), evidence);
                if (old != null) throw new IllegalArgumentException(
                    "Ambiguous food provenance for " + evidence.item()
                        + ": " + old.provenance() + " vs " + evidence.provenance());
            } catch (IOException bad) {
                throw new IllegalArgumentException(
                    "Cannot read food provenance " + resource.getKey(), bad);
            }
        }

        Map<String, FoodRecipeEvidence> recipes = new HashMap<>();
        final String routeDirectory = "tyr_progression/food_recipe_routes";
        for (var resource : manager.listResources(routeDirectory,
                    key -> key.getPath().endsWith(".json")).entrySet()) {
            try (var reader = resource.getValue().openAsReader()) {
                FoodRecipeEvidence evidence = FoodRecipeEvidence.parse(
                    JsonParser.parseReader(reader).getAsJsonObject());
                FoodRecipeEvidence old = recipes.putIfAbsent(evidence.item(), evidence);
                if (old != null) throw new IllegalArgumentException(
                    "Multiple dietary route records for one food " + evidence.item());
            } catch (IOException bad) {
                throw new IllegalArgumentException(
                    "Cannot read food recipe routes " + resource.getKey(), bad);
            }
        }
        // Independent strict diets (Dryad, Gilded-style gates, future Paths).
        // Datapacks may supply more profiles without new Java. They cannot
        // shadow an affinity profile or each other; publication is atomic.
        Map<String, StrictDietPolicy> strict = new HashMap<>();
        final String constraintDirectory = "tyr_progression/food_constraints";
        for (var resource : manager.listResources(constraintDirectory,
                    key -> key.getPath().endsWith(".json")).entrySet()) {
            try (var reader = resource.getValue().openAsReader()) {
                StrictDietPolicy rule = StrictDietPolicy.parse(
                    JsonParser.parseReader(reader).getAsJsonObject());
                if (rules.profiles().containsKey(rule.profile())
                        || strict.putIfAbsent(rule.profile(), rule) != null)
                    throw new IllegalArgumentException(
                        "Duplicate/overlapping dietary profile " + rule.profile());
            } catch (IOException bad) {
                throw new IllegalArgumentException(
                    "Cannot read strict food policy " + resource.getKey(), bad);
            }
        }
        // One volatile publish; an invalid route or root record never exposes
        // a partially updated diet policy on /reload.
        active = new State(rules, sources, recipes, strict, NEXT_GENERATION.incrementAndGet());
        ProgressionMod.LOGGER.info(
            "Loaded shared food policy schema {}: {} affinity profiles, {} strict profiles, {} roots, {} recipes",
            rules.schema(), rules.profiles().size(), strict.size(), sources.size(), recipes.size());
    }
}
