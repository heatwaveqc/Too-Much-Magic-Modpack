package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Strict, reusable server-side enrollment definition.
 * The trigger may ONLY be configured for a "potion" enrollment path.
 */
public record PotionEnrollmentTrigger(String item, List<String> potions, String requiredEffect,
                                     String requiredAdvancement, List<String> clearEffects,
                                     String completionEvent) {
    public PotionEnrollmentTrigger {
        potions = List.copyOf(potions);
        clearEffects = List.copyOf(clearEffects);
    }

    private static String id(String text) {
        if (!text.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid potion-enrollment ID: " + text);
        return text;
    }

    private static List<String> list(JsonObject value, String key) {
        if (!value.has(key) || !value.get(key).isJsonArray())
            throw new IllegalArgumentException("Expected enrollment array: " + key);
        List<String> result = new ArrayList<>();
        for (JsonElement element : value.getAsJsonArray(key)) result.add(id(element.getAsString()));
        if (result.size() > 32 || result.size() != Set.copyOf(result).size())
            throw new IllegalArgumentException("Duplicate or excessive enrollment entries: " + key);
        return result;
    }

    /**
     * Null when this is not a potion-enrollment path.
     * Rejects unknown trigger fields instead of silently ignoring a mistyped safety requirement.
     */
    public static PotionEnrollmentTrigger parse(JsonObject path) {
        String mode = path.has("enrollment") ? path.get("enrollment").getAsString() : "admin";
        boolean hasTrigger = path.has("enrollment_trigger");
        if (!"potion".equals(mode)) {
            if (hasTrigger) throw new IllegalArgumentException("enrollment_trigger requires enrollment=potion");
            return null;
        }
        if (!hasTrigger || !path.get("enrollment_trigger").isJsonObject())
            throw new IllegalArgumentException("Potion enrollment requires enrollment_trigger object");
        JsonObject trigger = path.getAsJsonObject("enrollment_trigger");
        Set<String> allowed = Set.of("item", "potions", "required_effect", "required_advancement",
                                     "clear_effects", "completion_event");
        for (String key : trigger.keySet())
            if (!allowed.contains(key)) throw new IllegalArgumentException("Unknown enrollment field: " + key);
        for (String required : List.of("item", "potions", "required_effect", "required_advancement",
                                      "clear_effects", "completion_event"))
            if (!trigger.has(required)) throw new IllegalArgumentException("Missing enrollment field: " + required);
        List<String> potions = list(trigger,"potions");
        List<String> clear = list(trigger,"clear_effects");
        if (potions.isEmpty()) throw new IllegalArgumentException("Potion enrollment requires at least one potion");
        return new PotionEnrollmentTrigger(
            id(trigger.get("item").getAsString()),potions,
            id(trigger.get("required_effect").getAsString()),
            id(trigger.get("required_advancement").getAsString()),
            clear,id(trigger.get("completion_event").getAsString()));
    }

    /** Pure check: the live server will supply the three trusted, independently verified facts. */
    public boolean matches(String itemId, String potionId, boolean effectActive, boolean advancementEarned) {
        return item.equals(itemId) && potions.contains(potionId) && effectActive && advancementEarned;
    }
}
