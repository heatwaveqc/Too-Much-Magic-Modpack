package io.github.heatwaveqc.tyr.progression.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Pure, conservative comparison of reviewed recipe receipts against the
 * effective server RecipeManager snapshot. "complete":true is NOT evidence
 * that the installed recipes still have the same outputs and alternatives.
 *
 * All active recipe IDs producing a known output must be accounted for. Every
 * declared receipt must still exist. A changed result count, processing method,
 * input multiset or ingredient-tag alternative blocks that entire output.
 * One invalid child invalidates callers via FoodRecipeEvaluator's blocked set.
 */
public final class FoodLiveRecipeValidator {
    private FoodLiveRecipeValidator() {}

    /** A server recipe expands into zero or more concrete input combinations. */
    public record Observed(String receipt, String output, int count,
                           FoodRecipeEvidence.Method method,
                           List<List<FoodRecipeEvidence.Ingredient>> variants,
                           boolean reliable) {
        public Observed {
            variants = variants.stream().map(List::copyOf).toList();
        }
        public static Observed unsupported(String receipt, String output) {
            return new Observed(receipt, output, 0, null, List.of(), false);
        }
    }

    public record Validation(Set<String> approved, Set<String> blocked,
                             Map<String, String> reasons) {
        public Validation {
            approved = Set.copyOf(approved);
            blocked = Set.copyOf(blocked);
            reasons = Map.copyOf(reasons);
        }
    }

    private record Signature(String receipt, FoodRecipeEvidence.Method method,
                             int count, Map<String, Integer> ingredients) {
        private Signature { ingredients = Map.copyOf(ingredients); }
    }

    private static Signature key(String receipt, FoodRecipeEvidence.Method method,
                                 int count, List<FoodRecipeEvidence.Ingredient> ingredients) {
        Map<String, Integer> parts = new HashMap<>();
        for (var ingredient : ingredients) {
            if (ingredient.neutral()) continue;
            parts.merge(ingredient.item(), ingredient.count(), Integer::sum);
        }
        return new Signature(receipt, method, count, parts);
    }

    /**
     * An unregistered active output cannot be declared complete. Unrecognized
     * recipes with unknown outputs are retained in the snapshot by receipt
     * so changes to formerly verified recipes also invalidate the output.
     */
    public static Validation validate(Map<String, FoodRecipeEvidence> declared,
                                      List<Observed> activeRecipes) {
        Map<String, List<Observed>> byOutput = new HashMap<>();
        Map<String, List<Observed>> byReceipt = new HashMap<>();
        for (Observed observed : activeRecipes) {
            if (observed.output() != null)
                byOutput.computeIfAbsent(observed.output(), k -> new ArrayList<>()).add(observed);
            byReceipt.computeIfAbsent(observed.receipt(), k -> new ArrayList<>()).add(observed);
        }
        Set<String> approved = new HashSet<>(), blocked = new HashSet<>();
        Map<String, String> reasons = new HashMap<>();
        for (var entry : declared.entrySet()) {
            String item = entry.getKey();
            FoodRecipeEvidence evidence = entry.getValue();
            Set<Signature> expected = new HashSet<>();
            Set<String> receipts = new HashSet<>();
            for (var route : evidence.routes()) {
                expected.add(key(route.receipt(), route.method(),
                    route.resultCount(), route.ingredients()));
                receipts.add(route.receipt());
            }
            List<Observed> all = byOutput.getOrDefault(item, List.of());
            Set<Signature> found = new HashSet<>();
            boolean reliable = !all.isEmpty();
            for (Observed observed : all) {
                if (!observed.reliable() || observed.variants().isEmpty()) {
                    reliable = false;
                    break;
                }
                for (var variant : observed.variants())
                    found.add(key(observed.receipt(), observed.method(), observed.count(), variant));
            }
            // A receipt removed, replaced by a different output, or hidden by
            // a serializer with an empty preview must not be accepted.
            for (String receipt : receipts) {
                List<Observed> sameReceipt = byReceipt.get(receipt);
                if (sameReceipt == null || sameReceipt.size() != 1
                        || !item.equals(sameReceipt.getFirst().output()))
                    reliable = false;
            }
            String reason = !reliable ? "missing_or_uninspectable_active_recipe"
                : !found.equals(expected) ? "active_routes_differ_from_evidence" : null;
            if (reason == null) approved.add(item);
            else { blocked.add(item); reasons.put(item, reason); }
        }
        return new Validation(approved, blocked, reasons);
    }
}
