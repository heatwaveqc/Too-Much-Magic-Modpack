package io.github.heatwaveqc.tyr.progression.data;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Single immutable, pure decision layer for all activated dietary profiles.
 *
 * A strict restriction is a veto, not a positive native-use permission.
 * An affinity profile calculates only evidenced nourishment and never denies
 * incompatible or mixed foods outright. Any active strict veto wins over all
 * affinity nutrition. A universal magical exemption retains native nutrition
 * without multiplying feeding effects, provided a recognized profile is active.
 *
 * Path/form adapters are responsible only for supplying authoritative active
 * profile IDs; source mods keep their consumption effects and special resources.
 */
public final class FoodDecisionResolver {
    public enum Outcome { NATIVE, PERMIT_NATIVE, NUTRITION, DENY }

    public record Decision(Outcome outcome, FoodPolicyRules.Nutrition nutrition) {
        public Decision {
            Objects.requireNonNull(outcome);
            if ((outcome == Outcome.NUTRITION) != (nutrition != null))
                throw new IllegalArgumentException("Diet nutrition must accompany a nutrition override only");
        }
        public static Decision nativeResult() { return new Decision(Outcome.NATIVE, null); }
        public static Decision permitNative() { return new Decision(Outcome.PERMIT_NATIVE, null); }
        public static Decision deny() { return new Decision(Outcome.DENY, null); }
        public static Decision nutrition(FoodPolicyRules.Nutrition n) {
            return new Decision(Outcome.NUTRITION, Objects.requireNonNull(n));
        }
    }

    private final FoodPolicyRules rules;
    private final Map<String, StrictDietPolicy> strict;
    private final Map<String, FoodSourceEvidence> roots;
    private final FoodRecipeEvaluator recipes;
    private final Function<String, Set<String>> liveTags;

    public FoodDecisionResolver(FoodPolicyRules rules,
            Map<String, StrictDietPolicy> strict,
            Map<String, FoodSourceEvidence> roots,
            Map<String, FoodRecipeEvidence> routes,
            Function<String, Set<String>> liveTags) {
        this(rules, strict, roots, routes, liveTags, Set.of());
    }

    public FoodDecisionResolver(FoodPolicyRules rules,
            Map<String, StrictDietPolicy> strict,
            Map<String, FoodSourceEvidence> roots,
            Map<String, FoodRecipeEvidence> routes,
            Function<String, Set<String>> liveTags,
            Set<String> blocked) {
        this.rules = Objects.requireNonNull(rules);
        this.strict = Map.copyOf(strict);
        this.roots = Map.copyOf(roots);
        this.liveTags = Objects.requireNonNull(liveTags);
        this.recipes = new FoodRecipeEvaluator(rules, roots, routes, liveTags, blocked);
        for (String key : strict.keySet()) if (rules.profiles().containsKey(key))
            throw new IllegalArgumentException("Conflicting strict and affinity diet " + key);
    }

    public Decision decide(Set<String> activeProfiles, String item) {
        if (activeProfiles == null || activeProfiles.isEmpty() || item == null)
            return Decision.nativeResult();
        boolean recognized = activeProfiles.stream().anyMatch(
            p -> strict.containsKey(p) || rules.profiles().containsKey(p));
        if (!recognized) return Decision.nativeResult();

        Set<String> observed = new HashSet<>(liveTags.apply(item));
        FoodSourceEvidence root = roots.get(item);
        if (root != null) observed.addAll(root.inferredTags());

        // Only explicitly tagged magical food is universally allowed. Never
        // fabricate its nutrition, blood, mana, durability or status effects.
        if (rules.universallyPermitted(observed)) return Decision.permitNative();

        // All strict policies must agree; a different diet cannot nullify a veto.
        for (String profile : activeProfiles) {
            StrictDietPolicy policy = strict.get(profile);
            if (policy != null && policy.denies(observed)) return Decision.deny();
        }

        // Independent affinities cannot turn one shared item into different
        // stack variants. Use the lower envelope across recognized profiles.
        Double lowestHunger = null, lowestSaturation = null;
        for (String profile : activeProfiles) {
            if (!rules.profiles().containsKey(profile)) continue;
            Optional<FoodPolicyRules.Nutrition> value = recipes.evaluate(profile, item);
            if (value.isEmpty()) continue; // Unknown provenance preserves native.
            var result = value.orElseThrow();
            if (lowestHunger == null) {
                lowestHunger = (double) result.hunger();
                lowestSaturation = result.theoreticalSaturationPoints();
            } else {
                lowestHunger = Math.min(lowestHunger, result.hunger());
                lowestSaturation = Math.min(lowestSaturation,
                    result.theoreticalSaturationPoints());
            }
        }
        if (lowestHunger == null) return Decision.nativeResult();
        return Decision.nutrition(
            FoodPolicyRules.rounded(lowestHunger, lowestSaturation));
    }
}
