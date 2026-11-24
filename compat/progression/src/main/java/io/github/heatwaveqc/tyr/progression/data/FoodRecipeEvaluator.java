package io.github.heatwaveqc.tyr.progression.data;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Per-consumer food budgets from verified recipe alternatives, source ratios,
 * family affinity and preparation. Always resolves by item ID, never stack NBT.
 * An incomplete alternative or cycle refuses the override (native fallback).
 */
public final class FoodRecipeEvaluator {
    private static final int MAX_DEPTH = 32;
    private final FoodPolicyRules rules;
    private final Map<String, FoodSourceEvidence> sources;
    private final Map<String, FoodRecipeEvidence> recipes;
    private final Function<String, Set<String>> liveTags;

    private record Budget(double hunger, double saturationPoints,
                          boolean preferred, boolean offDiet) {
        FoodPolicyRules.Nutrition food() {
            return FoodPolicyRules.rounded(hunger, saturationPoints);
        }
    }

    public FoodRecipeEvaluator(FoodPolicyRules rules,
            Map<String, FoodSourceEvidence> sources,
            Map<String, FoodRecipeEvidence> recipes,
            Function<String, Set<String>> liveTags) {
        this.rules = rules;
        this.sources = Map.copyOf(sources);
        this.recipes = Map.copyOf(recipes);
        this.liveTags = liveTags;
    }

    public Optional<FoodPolicyRules.Nutrition> evaluate(String profile, String item) {
        if (!rules.profiles().containsKey(profile)) return Optional.empty();
        return resolve(profile, item, new HashSet<>(), 0).map(Budget::food);
    }

    private Optional<Budget> resolve(String profile, String item, Set<String> visiting, int depth) {
        if (depth > MAX_DEPTH || !visiting.add(item)) return Optional.empty();
        try {
            FoodSourceEvidence root = sources.get(item);
            Set<String> tags = new HashSet<>(liveTags.apply(item));
            if (root != null) tags.addAll(root.inferredTags());
            if (rules.universallyPermitted(tags)) return Optional.empty();

            FoodRecipeEvidence recipe = recipes.get(item);
            if (recipe != null) {
                Double minimumHunger = null, minimumSaturation = null;
                boolean worstPreferred = false, worstOffDiet = false;
                for (FoodRecipeEvidence.Route route : recipe.routes()) {
                    double totalHunger = 0, totalSaturation = 0;
                    double bestInputHunger = 0, bestInputSaturation = 0;
                    boolean anyPreferred = false, anyOffDiet = false;
                    for (FoodRecipeEvidence.Ingredient input : route.ingredients()) {
                        if (input.neutral()) continue; // water, utensils, salt: explicit evidence
                        Optional<Budget> ingredient = resolve(
                            profile, input.item(), visiting, depth + 1);
                        if (ingredient.isEmpty()) return Optional.empty();
                        Budget value = ingredient.orElseThrow();
                        totalHunger += value.hunger() * input.count();
                        totalSaturation += value.saturationPoints() * input.count();
                        bestInputHunger = Math.max(bestInputHunger, value.hunger());
                        bestInputSaturation = Math.max(bestInputSaturation, value.saturationPoints());
                        anyPreferred |= value.preferred();
                        anyOffDiet |= value.offDiet();
                    }
                    if (!Double.isFinite(totalHunger) || !Double.isFinite(totalSaturation))
                        return Optional.empty();

                    double processing = switch (route.method()) {
                        case NONTHERMAL -> route.efficiency();
                        case HEAT -> rules.cookingMultipliers().get(FoodPolicyRules.Stage.COOKED);
                        case CHAR -> rules.cookingMultipliers().get(FoodPolicyRules.Stage.CHARRED)
                            / rules.cookingMultipliers().get(FoodPolicyRules.Stage.COOKED);
                    };
                    double hunger = totalHunger * processing / route.resultCount();
                    double saturation = totalSaturation * processing / route.resultCount();

                    // One finished mixed dish may be no better than half the best
                    // input (both hunger and theoretical saturation). Do NOT merely
                    // halve the incompatible ingredient's portion of a larger meal.
                    // Processing purely incompatible foods and explicit finished
                    // identities (e.g. meatballs) also cannot become a diet loophole.
                    boolean mixed = anyPreferred && anyOffDiet;
                    boolean incompatible = !anyPreferred && anyOffDiet;
                    if (mixed || incompatible
                            || recipe.identity() == FoodRecipeEvidence.Identity.UNFAVORED_ALL) {
                        double cap = rules.offDietContribution();
                        hunger = Math.min(hunger, cap * bestInputHunger);
                        saturation = Math.min(saturation, cap * bestInputSaturation);
                    }

                    // Independent lower envelopes prevent any alternative route
                    // from granting superior hunger *or* saturation to this item.
                    if (minimumHunger == null) {
                        minimumHunger = hunger;
                        minimumSaturation = saturation;
                    } else {
                        minimumHunger = Math.min(minimumHunger, hunger);
                        minimumSaturation = Math.min(minimumSaturation, saturation);
                    }
                    worstPreferred |= anyPreferred;
                    worstOffDiet |= anyOffDiet;
                }
                if (minimumHunger == null || !Double.isFinite(minimumHunger)
                        || !Double.isFinite(minimumSaturation)) return Optional.empty();
                // A disfavored finished dish stays disfavored if later used in
                // another recipe, even when made from initially favored inputs.
                if (recipe.identity() == FoodRecipeEvidence.Identity.UNFAVORED_ALL) {
                    worstPreferred = false;
                    worstOffDiet = true;
                }
                return Optional.of(new Budget(minimumHunger, minimumSaturation,
                    worstPreferred, worstOffDiet));
            }

            if (root == null) return Optional.empty();
            Double share = root.preferredShares().get(profile);
            if (share == null) share = rules.inferredPreferredShare(profile, tags).orElse(null);
            if (share == null) return Optional.empty();
            Optional<FoodPolicyRules.Budget> budget = rules.evaluateBudget(
                profile, tags, root.sourceUnits(), share);
            if (budget.isEmpty()) return Optional.empty();
            var value = budget.orElseThrow();
            return Optional.of(new Budget(value.exactHunger(),
                value.theoreticalSaturationPoints(), share == 1, share == 0));
        } finally {
            visiting.remove(item);
        }
    }
}
