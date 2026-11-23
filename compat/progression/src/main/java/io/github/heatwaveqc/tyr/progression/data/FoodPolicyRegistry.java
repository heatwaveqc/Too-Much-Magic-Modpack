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
                         long generation) {
        private State {
            sourceEvidence = Map.copyOf(sourceEvidence);
            recipeEvidence = Map.copyOf(recipeEvidence);
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
     * Complete evidence-driven lookup. Never assumes a food is one whole source:
     * recipes may cut, combine or heat input units, and ambiguous sources must
     * not receive a fabricated default nutrition value.
     */
    public static Optional<FoodPolicyRules.Nutrition> evaluateSource(
            String profile, ItemStack stack) {
        State state = active;
        if (state == null || stack.isEmpty()) return Optional.empty();
        String item = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        // Every output item has one result per dietary profile, independently
        // of how a particular stack was crafted. Resolve the lower envelope
        // of every verified alternative; incomplete evidence keeps native.
        return new FoodRecipeEvaluator(state.rules(), state.sourceEvidence(),
                state.recipeEvidence(), input -> {
                    if (input.equals(item)) return tags(stack);
                    ResourceLocation id = ResourceLocation.parse(input);
                    if (!BuiltInRegistries.ITEM.containsKey(id)) return Set.of();
                    return tags(BuiltInRegistries.ITEM.get(id).getDefaultInstance());
                }).evaluate(profile, item);
    }

    /**
     * Server-owned, snapshot-only client preview. Derive food values from the
     * same immutable datapack evidence and rules as the consumption hook.
     * Never transmit raw rules, recipe graphs, client-side entitlements or
     * mutable item components. Unverified foods remain native on the client.
     */
    public static Map<String, FoodPreviewWire.Entry> clientPreview(String profile) {
        State state = active;
        if (state == null || profile == null
                || !state.rules().profiles().containsKey(profile)) return Map.of();

        var evaluator = new FoodRecipeEvaluator(state.rules(), state.sourceEvidence(),
            state.recipeEvidence(), input -> {
                ResourceLocation id = ResourceLocation.tryParse(input);
                if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) return Set.of();
                return tags(BuiltInRegistries.ITEM.get(id).getDefaultInstance());
            });
        Map<String, FoodPreviewWire.Entry> projected = new HashMap<>();
        Set<String> evidenced = new HashSet<>(state.sourceEvidence().keySet());
        evidenced.addAll(state.recipeEvidence().keySet());
        for (String item : evidenced) {
            ResourceLocation id = ResourceLocation.tryParse(item);
            if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) continue;
            ItemStack stack = BuiltInRegistries.ITEM.get(id).getDefaultInstance();
            if (stack.get(DataComponents.FOOD) == null) continue;
            FoodSourceEvidence root = state.sourceEvidence().get(item);
            if (state.rules().universallyPermitted(effectiveTags(stack, root))) {
                projected.put(item, FoodPreviewWire.Entry.permitNative());
                continue;
            }
            evaluator.evaluate(profile, item).ifPresent(value ->
                projected.put(item, FoodPreviewWire.Entry.nourishment(
                    value.hunger(), value.saturationModifier())));
        }
        // A magical food may have no source/route record. As on the server,
        // the exemption is permission-only: preserve its actual native food.
        for (ResourceLocation id : BuiltInRegistries.ITEM.keySet()) {
            ItemStack stack = BuiltInRegistries.ITEM.get(id).getDefaultInstance();
            if (stack.get(DataComponents.FOOD) == null) continue;
            if (state.rules().universallyPermitted(effectiveTags(
                    stack, state.sourceEvidence().get(id.toString()))))
                projected.put(id.toString(), FoodPreviewWire.Entry.permitNative());
        }
        return Map.copyOf(projected);
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
        // One volatile publish; an invalid route or root record never exposes
        // a partially updated diet policy on /reload.
        active = new State(rules, sources, recipes, NEXT_GENERATION.incrementAndGet());
        ProgressionMod.LOGGER.info(
            "Loaded shared food policy schema {}: {} profiles, {} source roots, {} recipe outputs",
            rules.schema(), rules.profiles().size(), sources.size(), recipes.size());
    }
}
