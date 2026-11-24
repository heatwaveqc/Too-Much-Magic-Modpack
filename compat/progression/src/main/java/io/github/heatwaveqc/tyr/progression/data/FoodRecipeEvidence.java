package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * One item ID, all validated alternative routes. No NBT, persistent provenance
 * on ItemStacks, new food IDs, or global FoodProperties modifications.
 *
 * A route receipt refers to the verified native/pack recipe identity, while
 * output count, material inputs and processing efficiency are evidence, not
 * global nutritional ratings.
 */
public record FoodRecipeEvidence(String item, Identity identity, List<Route> routes) {
    public enum Method { NONTHERMAL, HEAT, CHAR }
    public enum Identity { ORDINARY, UNFAVORED_ALL }
    public record Ingredient(String item, int count, boolean neutral) {}
    public record Route(String receipt, Method method, int resultCount, double efficiency,
                        List<Ingredient> ingredients) {
        public Route { ingredients = List.copyOf(ingredients); }
    }

    public FoodRecipeEvidence { routes = List.copyOf(routes); }

    private static boolean id(String value) {
        return value.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");
    }

    private static void keys(JsonObject raw, Set<String> required, Set<String> optional) {
        if (!raw.keySet().containsAll(required)
                || !union(required, optional).containsAll(raw.keySet()))
            throw new IllegalArgumentException("Invalid food recipe evidence keys " + raw.keySet());
    }

    private static Set<String> union(Set<String> a, Set<String> b) {
        var all = new HashSet<>(a);
        all.addAll(b);
        return all;
    }

    private static int portion(int value) {
        if (value < 1 || value > 64) throw new IllegalArgumentException("Food route count outside 1..64");
        return value;
    }

    public static FoodRecipeEvidence parse(JsonObject raw) {
        if (raw == null) throw new IllegalArgumentException("Null food recipe evidence");
        keys(raw, Set.of("schema", "item", "identity", "complete", "routes"), Set.of());
        if (raw.get("schema").getAsInt() != 1 || !raw.get("complete").getAsBoolean())
            throw new IllegalArgumentException("Only complete schema-1 food route sets are accepted");
        String item = raw.get("item").getAsString();
        if (!id(item)) throw new IllegalArgumentException("Invalid food recipe item " + item);
        Identity identity;
        try { identity = Identity.valueOf(raw.get("identity").getAsString().toUpperCase(java.util.Locale.ROOT)); }
        catch (RuntimeException bad) { throw new IllegalArgumentException("Invalid finished food identity", bad); }

        List<Route> routes = new ArrayList<>();
        Set<String> receipts = new HashSet<>();
        for (var value : raw.getAsJsonArray("routes")) {
            JsonObject route = value.getAsJsonObject();
            keys(route, Set.of("receipt", "method", "result_count", "ingredients"),
                Set.of("efficiency"));
            String receipt = route.get("receipt").getAsString();
            if (!id(receipt))
                throw new IllegalArgumentException("Invalid food route receipt " + receipt);
            Method method;
            try { method = Method.valueOf(route.get("method").getAsString().toUpperCase(java.util.Locale.ROOT)); }
            catch (RuntimeException bad) { throw new IllegalArgumentException("Invalid food processing method", bad); }
            int outputCount = portion(route.get("result_count").getAsInt());
            double efficiency = route.has("efficiency") ? route.get("efficiency").getAsDouble() : 1.0;
            if (!Double.isFinite(efficiency) || efficiency < 1 || efficiency > 4
                    || (method != Method.NONTHERMAL && efficiency != 1))
                throw new IllegalArgumentException("Unapproved food processing efficiency " + receipt);
            List<Ingredient> ingredients = new ArrayList<>();
            boolean edibleInput = false;
            for (var part : route.getAsJsonArray("ingredients")) {
                JsonObject ingredient = part.getAsJsonObject();
                keys(ingredient, Set.of("item", "count"), Set.of("role"));
                String input = ingredient.get("item").getAsString();
                if (!id(input)) throw new IllegalArgumentException("Invalid ingredient " + input);
                int count = portion(ingredient.get("count").getAsInt());
                String role = ingredient.has("role") ? ingredient.get("role").getAsString() : "food";
                if (!role.equals("food") && !role.equals("neutral"))
                    throw new IllegalArgumentException("Invalid ingredient dietary role " + role);
                boolean neutral = role.equals("neutral");
                edibleInput |= !neutral;
                ingredients.add(new Ingredient(input, count, neutral));
                if (ingredients.size() > 32)
                    throw new IllegalArgumentException("Too many food recipe ingredients");
            }
            if (!edibleInput) throw new IllegalArgumentException("No nutritive input for " + receipt);
            // One vanilla/FD/Pam recipe may accept an item tag with multiple
            // eligible ingredients. Every concrete expansion must share its
            // verified recipe receipt. Reject duplicate expanded *routes*,
            // not repeat receipts with distinct ingredients.
            String signature = receipt + "|" + method + "|" + outputCount + "|"
                + efficiency + "|" + ingredients;
            if (!receipts.add(signature))
                throw new IllegalArgumentException("Duplicate concrete food route " + receipt);
            routes.add(new Route(receipt, method, outputCount, efficiency, ingredients));
            if (routes.size() > 64)
                throw new IllegalArgumentException("Too many alternate food routes for " + item);
        }
        if (routes.isEmpty()) throw new IllegalArgumentException("No food route for " + item);
        return new FoodRecipeEvidence(item, identity, routes);
    }
}
