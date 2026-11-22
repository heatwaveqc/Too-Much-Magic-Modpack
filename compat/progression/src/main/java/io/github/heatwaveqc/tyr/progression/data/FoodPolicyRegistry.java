package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.minecraft.core.registries.BuiltInRegistries;
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
import java.util.stream.Collectors;

/**
 * Atomic server reload of both tag-first dietary policy and generated source
 * evidence. No global food component mutation and no hardcoded item diet list.
 */
public final class FoodPolicyRegistry extends SimpleJsonResourceReloadListener {
    private record State(FoodPolicyRules rules,
                         Map<ResourceLocation, FoodSourceEvidence> sourceEvidence) {
        private State {
            sourceEvidence = Map.copyOf(sourceEvidence);
        }
    }
    private static volatile State active;

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
        FoodSourceEvidence source = state.sourceEvidence().get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        return state.rules().universallyPermitted(effectiveTags(stack, source));
    }

    /** Explicit source units from a trusted recipe graph, for integration adapters. */
    public static Optional<FoodPolicyRules.Nutrition> evaluate(
            String profile, ItemStack stack, double rawSourceUnits, Double preferredShare) {
        State state = active;
        return state == null || stack.isEmpty() ? Optional.empty()
            : state.rules().evaluate(profile, tags(stack), rawSourceUnits, preferredShare);
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
        FoodSourceEvidence source =
            state.sourceEvidence().get(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        if (source == null) return Optional.empty();
        return state.rules().evaluate(profile, effectiveTags(stack, source),
            source.sourceUnits(), source.preferredShares().get(profile));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data,
                         ResourceManager manager, ProfilerFiller profiler) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("tyr_progression", "default");
        JsonElement raw = data.get(id);
        if (raw == null) throw new IllegalArgumentException("Missing bundled food policy " + id);
        FoodPolicyRules rules = FoodPolicyRules.parse(raw.getAsJsonObject());

        Map<ResourceLocation, FoodSourceEvidence> sources = new HashMap<>();
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

        // One volatile publish; invalid policy or duplicate/unverified source
        // entries leave the last accepted snapshot intact.
        active = new State(rules, sources);
        ProgressionMod.LOGGER.info(
            "Loaded shared food policy schema {} with {} profiles and {} sourced foods",
            rules.schema(), rules.profiles().size(), sources.size());
    }
}
