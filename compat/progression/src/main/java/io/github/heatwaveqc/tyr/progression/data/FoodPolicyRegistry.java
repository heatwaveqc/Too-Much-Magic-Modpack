package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import io.github.heatwaveqc.tyr.progression.foundation.Condition;
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
                         Map<String, Condition<ServerPlayer>> strictActivations,
                         FoodProcessingCatalog processing,
                         long generation) {
        private State {
            sourceEvidence = Map.copyOf(sourceEvidence);
            recipeEvidence = Map.copyOf(recipeEvidence);
            strictPolicies = Map.copyOf(strictPolicies);
            strictActivations = Map.copyOf(strictActivations);
        }
    }
    private static volatile State active;
    private static final AtomicLong NEXT_GENERATION = new AtomicLong();

    /**
     * One immutable validated view per server/policy generation. Server tick
     * is the only builder; eating, hover/preview and owned-effect checks reuse
     * its resolver rather than copying route tables for each bite/tick.
     */
    private record RuntimeSnapshot(State state, MinecraftServer server, Object recipeManager,
                                   FoodDecisionResolver resolver,
                                   FoodLiveRecipeValidator.Validation validation) {}
    private static volatile RuntimeSnapshot runtimeSnapshot;

    public static void serverStopped(ServerStoppedEvent event) {
        if (runtimeSnapshot != null && runtimeSnapshot.server() == event.getServer())
            runtimeSnapshot = null;
    }

    private static RuntimeSnapshot snapshot(ServerPlayer player) {
        State state = active;
        if (state == null || player == null || player.getServer() == null
                || !player.getServer().isSameThread()) return null;
        MinecraftServer server = player.getServer();
        Object recipes = server.getRecipeManager();
        RuntimeSnapshot prior = runtimeSnapshot;
        if (prior != null && prior.state() == state && prior.server() == server
                && prior.recipeManager() == recipes) return prior;

        // Run after the server has finished applying its resource reload, not
        // inside the diet reload listener (whose order vs RecipeManager is not
        // guaranteed). The first per-player preview tick normally builds this.
        var observed = FoodServerRecipeSnapshot.read(server, state.processing());
        var validated = FoodLiveRecipeValidator.validate(state.recipeEvidence(), observed);
        Map<String, FoodRecipeEvidence> approved = new HashMap<>();
        for (String item : validated.approved())
            approved.put(item, state.recipeEvidence().get(item));
        var resolver = new FoodDecisionResolver(state.rules(), state.strictPolicies(),
            state.sourceEvidence(), approved, input -> {
                ResourceLocation id = ResourceLocation.tryParse(input);
                if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) return Set.of();
                return tags(BuiltInRegistries.ITEM.get(id).getDefaultInstance());
            }, validated.blocked());
        RuntimeSnapshot ready = new RuntimeSnapshot(state, server, recipes, resolver, validated);
        runtimeSnapshot = ready;
        if (!validated.blocked().isEmpty())
            ProgressionMod.LOGGER.warn(
                "Food recipe audit rejected {} of {} reviewed outputs (native fallback): {}",
                validated.blocked().size(), state.recipeEvidence().size(),
                validated.reasons());
        return ready;
    }

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

    /**
     * Read-only, server-authoritative active strict diets. An unbound policy
     * remains dormant; unknown or unready provider flags do not authorize it.
     * Path membership/eligibility and native form probes are evaluated live.
     */
    public static Set<String> activeStrictProfiles(ServerPlayer player) {
        State state = active;
        if (state == null || player == null || player.getServer() == null
                || !player.getServer().isSameThread()) return Set.of();
        Set<String> activeProfiles = new HashSet<>();
        for (var entry : state.strictActivations().entrySet()) {
            try {
                if (entry.getValue().test(player)) activeProfiles.add(entry.getKey());
            } catch (RuntimeException | LinkageError unavailable) {
                // Never activate an unverified native or Path gate.
            }
        }
        return Set.copyOf(activeProfiles);
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
     * Server-authorized shared decision. Never evaluate unverified recipe
     * sidecars, even when their JSON claims complete:true.
     */
    public static FoodDecisionResolver.Decision decide(
            ServerPlayer player, Set<String> profiles, ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.get(DataComponents.FOOD) == null)
            return FoodDecisionResolver.Decision.nativeResult();
        RuntimeSnapshot live = snapshot(player);
        if (live == null) return FoodDecisionResolver.Decision.nativeResult();
        return live.resolver().decide(profiles,
            BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
    }

    public static Optional<FoodPolicyRules.Nutrition> evaluateSource(
            ServerPlayer player, String profile, ItemStack stack) {
        if (profile == null) return Optional.empty();
        var result = decide(player, Set.of(profile), stack);
        return result.outcome() == FoodDecisionResolver.Outcome.NUTRITION
            ? Optional.of(result.nutrition()) : Optional.empty();
    }

    /**
     * This API deliberately requires server context. A profile/stack alone
     * cannot prove current recipe alternatives following KubeJS /reload.
     */
    @Deprecated
    public static Optional<FoodPolicyRules.Nutrition> evaluateSource(
            String profile, ItemStack stack) {
        return Optional.empty();
    }

    /** Client receives the exact same effective-recipe-validated decisions. */
    public static Map<String, FoodPreviewWire.Entry> clientPreview(
            ServerPlayer player, Set<String> profiles) {
        RuntimeSnapshot live = snapshot(player);
        if (live == null || profiles == null || profiles.isEmpty()) return Map.of();
        Map<String, FoodPreviewWire.Entry> projected = new HashMap<>();
        for (ResourceLocation id : BuiltInRegistries.ITEM.keySet()) {
            ItemStack stack = BuiltInRegistries.ITEM.get(id).getDefaultInstance();
            if (stack.get(DataComponents.FOOD) == null) continue;
            var decision = live.resolver().decide(profiles, id.toString());
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
        Map<String, Condition<ServerPlayer>> activations = new HashMap<>();
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
                if (rule.when() != null)
                    activations.put(rule.profile(), Eligibility.parse(rule.when()));
            } catch (IOException bad) {
                throw new IllegalArgumentException(
                    "Cannot read strict food policy " + resource.getKey(), bad);
            }
        }
        // The offline compiler and server validator share this method catalog.
        // Invalid prep config never partially publishes a dietary generation.
        ResourceLocation processingId = ResourceLocation.parse(
            "tyr_progression:tyr_progression/food_processing/default.json");
        var processingResource = manager.getResource(processingId).orElseThrow(
            () -> new IllegalArgumentException("Missing food processing catalog"));
        FoodProcessingCatalog processing;
        try (var reader = processingResource.openAsReader()) {
            processing = FoodProcessingCatalog.parse(
                JsonParser.parseReader(reader).getAsJsonObject());
        } catch (IOException bad) {
            throw new IllegalArgumentException("Cannot read food processing catalog", bad);
        }

        // Publish an immutable generation. The first SERVER tick afterwards
        // revalidates against the completely reloaded effective recipe manager.
        active = new State(rules, sources, recipes, strict, activations, processing,
            NEXT_GENERATION.incrementAndGet());
        runtimeSnapshot = null;
        ProgressionMod.LOGGER.info(
            "Loaded shared food policy schema {}: {} affinity profiles, {} strict profiles, {} roots, {} recipes",
            rules.schema(), rules.profiles().size(), strict.size(), sources.size(), recipes.size());
    }
}
