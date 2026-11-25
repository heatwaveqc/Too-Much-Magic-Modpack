package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.Set;

/**
 * Declarative food restriction for an independent, server-authorized
 * physiology/path profile. A matching allow tag lifts this policy's own veto;
 * it never overrides another profile, a native use callback or a resource cost.
 *
 * Universal magical nourishment is resolved by FoodDecisionResolver before
 * strict profiles; this policy does not reclassify foods as magical.
 */
public record StrictDietPolicy(String profile, DefaultDecision fallback,
                               Set<String> allowAny, Set<String> denyAny, JsonObject when) {
    public enum DefaultDecision { ALLOW, DENY }

    public StrictDietPolicy {
        allowAny = Set.copyOf(allowAny);
        denyAny = Set.copyOf(denyAny);
        when = when == null ? null : when.deepCopy();
    }

    /** Existing source tests and manually activated policies remain valid. */
    public StrictDietPolicy(String profile, DefaultDecision fallback,
            Set<String> allowAny, Set<String> denyAny) {
        this(profile, fallback, allowAny, denyAny, null);
    }

    public static StrictDietPolicy parse(JsonObject obj) {
        if (obj == null || !Set.of(
                "schema", "profile", "type", "default", "allow_any", "deny_any")
                .stream().allMatch(obj::has)
                || obj.keySet().stream().anyMatch(k -> !Set.of(
                    "schema", "profile", "type", "default", "allow_any", "deny_any", "when")
                    .contains(k)))
            throw new IllegalArgumentException("Invalid strict food policy fields");
        if (obj.get("schema").getAsInt() != 1
                || !obj.get("type").getAsString().equals("strict"))
            throw new IllegalArgumentException("Unsupported strict food policy schema/type");
        String profile = obj.get("profile").getAsString();
        if (!profile.matches("[a-z][a-z0-9_]*"))
            throw new IllegalArgumentException("Invalid strict food profile " + profile);
        DefaultDecision fallback;
        try {
            fallback = DefaultDecision.valueOf(
                obj.get("default").getAsString().toUpperCase(java.util.Locale.ROOT));
        } catch (RuntimeException bad) {
            throw new IllegalArgumentException("Invalid food policy default", bad);
        }
        Set<String> allow = tags(obj.getAsJsonArray("allow_any"));
        Set<String> deny = tags(obj.getAsJsonArray("deny_any"));
        for (String tag : allow) if (deny.contains(tag))
            throw new IllegalArgumentException("Conflicting food allow/deny tag " + tag);
        // No 'when' means dormant: registering a diet never enrolls everyone.
        JsonObject when = obj.has("when") ? obj.getAsJsonObject("when") : null;
        if (when != null && !when.has("type"))
            throw new IllegalArgumentException("Strict food policy needs typed activation");
        return new StrictDietPolicy(profile, fallback, allow, deny, when);
    }

    private static Set<String> tags(JsonArray array) {
        if (array == null) throw new IllegalArgumentException("Missing strict policy tag array");
        Set<String> tags = new HashSet<>();
        for (var element : array) {
            String tag = element.getAsString();
            if (!tag.matches("#[a-z0-9_.-]+:[a-z0-9_./-]+") || !tags.add(tag))
                throw new IllegalArgumentException("Invalid/duplicate strict food tag " + tag);
        }
        return Set.copyOf(tags);
    }

    private static boolean any(Set<String> selectors, Set<String> observed) {
        for (String tag : selectors) if (observed.contains(tag)) return true;
        return false;
    }

    /** Deny tags beat ordinary allow tags; unrelated policy vetoes still apply. */
    public boolean denies(Set<String> tags) {
        if (any(denyAny, tags)) return true;
        if (any(allowAny, tags)) return false;
        return fallback == DefaultDecision.DENY;
    }
}
