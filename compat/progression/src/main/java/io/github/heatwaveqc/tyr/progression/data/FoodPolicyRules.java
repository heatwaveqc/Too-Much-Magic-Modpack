package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Source-independent, tag-first food policy. Item IDs and recipe outputs are not
 * listed here: a provenance graph supplies tags, preparation and source units.
 * Native food effects and use handlers are deliberately outside this class.
 */
public record FoodPolicyRules(
    int schema, int preferredRawNutrition, double preferredRawSaturationModifier,
    Map<Stage, Double> cookingMultipliers, double offDietContribution,
    Map<String, Set<String>> familyTags, Map<String, Set<String>> profiles,
    Map<Stage, Set<String>> preparationTags, Set<String> magicalTags
) {
    public enum Stage { RAW, COOKED, CHARRED }

    /** Saturation points, not the saturation modifier, follow the cooking ratio. */
    public record Nutrition(int hunger, float saturationModifier, double theoreticalSaturationPoints) {}

    public FoodPolicyRules {
        cookingMultipliers = Map.copyOf(cookingMultipliers);
        familyTags = Map.copyOf(familyTags);
        profiles = Map.copyOf(profiles);
        preparationTags = Map.copyOf(preparationTags);
        magicalTags = Set.copyOf(magicalTags);
    }

    public static FoodPolicyRules parse(JsonObject raw) {
        keys(raw, Set.of("schema", "preferred_raw", "stage_multipliers", "mixed",
            "family_tags", "profiles", "preparation_tags", "magical_tags"));
        int schema = raw.get("schema").getAsInt();
        if (schema != 1) throw new IllegalArgumentException("Unsupported food policy schema " + schema);
        JsonObject base = raw.getAsJsonObject("preferred_raw");
        keys(base, Set.of("nutrition", "saturation_modifier"));
        int nutrition = base.get("nutrition").getAsInt();
        double saturation = finite(base.get("saturation_modifier"), 0, 4, "raw saturation");
        if (nutrition < 1 || nutrition > 40) throw new IllegalArgumentException("Raw nutrition must be 1..40");
        Map<Stage, Double> multipliers = stages(raw.getAsJsonObject("stage_multipliers"));
        if (multipliers.get(Stage.RAW) != 1.0
            || multipliers.get(Stage.COOKED) > 1.0
            || multipliers.get(Stage.CHARRED) > multipliers.get(Stage.COOKED)
            || multipliers.get(Stage.CHARRED) <= 0)
            throw new IllegalArgumentException("Require raw=1, raw >= cooked >= charred > 0");
        JsonObject mixed = raw.getAsJsonObject("mixed");
        keys(mixed, Set.of("off_diet_contribution"));
        double off = finite(mixed.get("off_diet_contribution"), 0, 1, "off-diet contribution");
        Map<String, Set<String>> families = new HashMap<>();
        JsonObject familyJson = raw.getAsJsonObject("family_tags");
        if (familyJson.size() == 0) throw new IllegalArgumentException("No food families");
        for (var entry : familyJson.entrySet()) {
            if (!entry.getKey().matches("[a-z][a-z0-9_]*"))
                throw new IllegalArgumentException("Invalid food family " + entry.getKey());
            families.put(entry.getKey(), tags(entry.getValue().getAsJsonArray()));
        }
        Map<String, Set<String>> profiles = new HashMap<>();
        JsonObject profileJson = raw.getAsJsonObject("profiles");
        if (profileJson.size() == 0) throw new IllegalArgumentException("No dietary profiles");
        for (var entry : profileJson.entrySet()) {
            if (!entry.getKey().matches("[a-z][a-z0-9_]*"))
                throw new IllegalArgumentException("Invalid dietary profile " + entry.getKey());
            Set<String> allowed = new HashSet<>();
            for (var value : entry.getValue().getAsJsonArray()) {
                String family = value.getAsString();
                if (!families.containsKey(family) || !allowed.add(family))
                    throw new IllegalArgumentException("Unknown/duplicate family " + family);
            }
            if (allowed.isEmpty()) throw new IllegalArgumentException("Empty profile " + entry.getKey());
            profiles.put(entry.getKey(), Set.copyOf(allowed));
        }
        Map<Stage, Set<String>> preparation = new EnumMap<>(Stage.class);
        JsonObject stages = raw.getAsJsonObject("preparation_tags");
        if (stages.size() != 3) throw new IllegalArgumentException("Exactly three preparation stages required");
        for (Stage s : Stage.values()) preparation.put(s, tags(stages.getAsJsonArray(key(s))));
        keys(stages, Set.of("raw", "cooked", "charred"));
        return new FoodPolicyRules(schema, nutrition, saturation, multipliers, off,
            families, profiles, preparation, tags(raw.getAsJsonArray("magical_tags")));
    }

    private static Map<Stage, Double> stages(JsonObject raw) {
        keys(raw, Set.of("raw", "cooked", "charred"));
        Map<Stage, Double> result = new EnumMap<>(Stage.class);
        for (Stage stage : Stage.values())
            result.put(stage, finite(raw.get(key(stage)), 0, 1, key(stage) + " multiplier"));
        return result;
    }

    private static String key(Stage stage) { return stage.name().toLowerCase(java.util.Locale.ROOT); }

    private static double finite(JsonElement element, double min, double max, String key) {
        if (element == null) throw new IllegalArgumentException("Missing " + key);
        double value = element.getAsDouble();
        if (!Double.isFinite(value) || value < min || value > max)
            throw new IllegalArgumentException("Invalid " + key);
        return value;
    }

    private static void keys(JsonObject object, Set<String> allowed) {
        if (object == null || !object.keySet().equals(allowed))
            throw new IllegalArgumentException("Invalid food policy keys; expected " + allowed);
    }

    private static Set<String> tags(JsonArray array) {
        if (array == null) throw new IllegalArgumentException("Missing tag list");
        Set<String> result = new HashSet<>();
        for (JsonElement el : array) {
            String tag = el.getAsString();
            if (!tag.matches("#[a-z0-9_.-]+:[a-z0-9_./-]+") || !result.add(tag))
                throw new IllegalArgumentException("Invalid or duplicate food tag " + tag);
        }
        if (result.isEmpty()) throw new IllegalArgumentException("Empty food tag list");
        return Set.copyOf(result);
    }

    private static boolean matches(Set<String> expected, Set<String> observed) {
        for (String tag : expected) if (observed.contains(tag)) return true;
        return false;
    }

    public boolean universallyPermitted(Set<String> itemTags) {
        return matches(magicalTags, itemTags);
    }

    /** Stage tags are resolved at consumption time; stronger heat treatment wins. */
    public Optional<Stage> preparation(Set<String> itemTags) {
        for (Stage stage : new Stage[]{Stage.CHARRED, Stage.COOKED, Stage.RAW})
            if (matches(preparationTags.get(stage), itemTags)) return Optional.of(stage);
        return Optional.empty();
    }

    /** No item matching, no default-to-human, and no guessing mixed provenance. */
    public Optional<Double> inferredPreferredShare(String profile, Set<String> itemTags) {
        Set<String> favored = profiles.get(profile);
        if (favored == null) return Optional.empty();
        boolean preferred = false, other = false;
        for (var family : familyTags.entrySet()) {
            if (!matches(family.getValue(), itemTags)) continue;
            if (favored.contains(family.getKey())) preferred = true;
            else other = true;
        }
        if (preferred == other) return Optional.empty(); // untagged or mixed/ambiguous
        return Optional.of(preferred ? 1.0 : 0.0);
    }

    /**
     * Raw source units come from the recipe graph, not a hand-authored item
     * matrix: a half cod slice has 0.5; 2 oranges in one juice have 2.0.
     * The graph may explicitly supply the preferred material fraction for
     * mixed dishes; absent evidence uses tags only for unambiguous foods.
     *
     * Empty means keep the native provider result (including magical special
     * use, unknown foods, unresolved provenance, and missing stage tags).
     */
    public Optional<Nutrition> evaluate(String profile, Set<String> itemTags,
                                         double rawSourceUnits, Double preferredShare) {
        if (!profiles.containsKey(profile) || universallyPermitted(itemTags)
                || !Double.isFinite(rawSourceUnits) || rawSourceUnits <= 0 || rawSourceUnits > 64)
            return Optional.empty();
        Optional<Stage> stage = preparation(itemTags);
        if (stage.isEmpty()) return Optional.empty();
        Optional<Double> inferred = inferredPreferredShare(profile, itemTags);
        if (preferredShare == null && inferred.isEmpty()) return Optional.empty();
        double fraction = preferredShare == null ? inferred.orElseThrow() : preferredShare;
        if (!Double.isFinite(fraction) || fraction < 0 || fraction > 1) return Optional.empty();
        // Even explicit fractions must come from source-classified family tags.
        if (familyTags.values().stream().noneMatch(tags -> matches(tags, itemTags)))
            return Optional.empty();
        double weight = fraction + (1 - fraction) * offDietContribution;
        double factor = cookingMultipliers.get(stage.orElseThrow());
        double exactHunger = preferredRawNutrition * rawSourceUnits * weight * factor;
        double theoretical = 2 * preferredRawNutrition * preferredRawSaturationModifier
            * rawSourceUnits * weight * factor;
        int roundedHunger = (int) Math.round(exactHunger);
        if (theoretical > 0 && roundedHunger == 0) roundedHunger = 1;
        float modifier = roundedHunger > 0 ? (float) (theoretical / (2 * roundedHunger)) : 0f;
        // Integer hunger rounds to the closest unit; recompute the saturation
        // modifier to preserve the intended *points* rather than quartering
        // saturation a second time due to both values being multiplied.
        return Optional.of(new Nutrition(roundedHunger, modifier, theoretical));
    }
}
