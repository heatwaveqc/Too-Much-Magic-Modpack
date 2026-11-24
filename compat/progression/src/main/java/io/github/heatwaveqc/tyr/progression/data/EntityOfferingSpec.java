package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonObject;
import java.util.Set;

/** Exact-identity, server-authoritative entity offering. Optional jewelry material
 * is a real Iron's Jewelry data component, never a display-name predicate. */
public record EntityOfferingSpec(String entity, String item, String jewelryMaterial, String completionEvent) {
    public static EntityOfferingSpec parse(JsonObject path) {
        boolean offering = path.has("enrollment") && "entity_offering".equals(path.get("enrollment").getAsString());
        if (!offering) {
            if (path.has("entity_offering")) throw new IllegalArgumentException("entity_offering requires enrollment=entity_offering");
            return null;
        }
        if (!path.has("entity_offering") || !path.get("entity_offering").isJsonObject())
            throw new IllegalArgumentException("Missing entity_offering object");
        var j = path.getAsJsonObject("entity_offering");
        for (String key : j.keySet())
            if (!Set.of("entity","item","jewelry_material","completion_event").contains(key))
                throw new IllegalArgumentException("Unknown offering field: " + key);
        String entity = id(j,"entity",true), item = id(j,"item",true);
        String material = id(j,"jewelry_material",false), completion = id(j,"completion_event",false);
        if (!material.isEmpty() && !Set.of("irons_jewelry:ring","irons_jewelry:necklace").contains(item))
            throw new IllegalArgumentException("jewelry_material requires an Iron's Jewelry item");
        return new EntityOfferingSpec(entity,item,material,completion);
    }

    private static String id(JsonObject j, String key, boolean required) {
        if (!j.has(key) && !required) return "";
        if (!j.has(key) || !j.get(key).isJsonPrimitive() || !j.get(key).getAsJsonPrimitive().isString())
            throw new IllegalArgumentException("Expected offering ID: " + key);
        String value = j.get(key).getAsString();
        if (!value.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid offering ID: " + key);
        return value;
    }

    public boolean matches(String entityId,String itemId,Set<String> materials) {
        return entity.equals(entityId) && item.equals(itemId)
            && (jewelryMaterial.isEmpty() || materials.contains(jewelryMaterial));
    }
}
