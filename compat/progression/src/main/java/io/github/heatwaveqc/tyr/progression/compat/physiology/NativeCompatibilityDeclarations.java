package io.github.heatwaveqc.tyr.progression.compat.physiology;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Form;
import java.util.EnumSet;
import java.util.Set;

/**
 * Optional, per-Path coexistence declarations. No wildcard or implicit pairing:
 * absent field means a specialist Path permits no native form.
 *
 * This class parses declarative permissions; it never grants a native form,
 * alters a native cure, or changes the native-to-native combination rules.
 */
public final class NativeCompatibilityDeclarations {
    private NativeCompatibilityDeclarations() {}

    public static Set<Form> read(JsonObject path) {
        if (!path.has("native_compatible_forms")) return Set.of();
        JsonElement raw = path.get("native_compatible_forms");
        if (!raw.isJsonArray())
            throw new IllegalArgumentException("native_compatible_forms must be an array");
        EnumSet<Form> forms = EnumSet.noneOf(Form.class);
        for (JsonElement entry : raw.getAsJsonArray()) {
            if (!entry.isJsonPrimitive() || !entry.getAsJsonPrimitive().isString())
                throw new IllegalArgumentException("native_compatible_forms entries must be IDs");
            String name = entry.getAsString();
            Form form = switch (name) {
                case "jawr_vampire" -> Form.JAWR_VAMPIRE;
                case "jawr_werewolf" -> Form.JAWR_WEREWOLF;
                case "goety_lich" -> Form.GOETY_LICH;
                case "forbidden_magic_lich" -> Form.FORBIDDEN_MAGIC_LICH;
                case "cave_dragon" -> Form.CAVE_DRAGON;
                case "forest_dragon" -> Form.FOREST_DRAGON;
                case "sea_dragon" -> Form.SEA_DRAGON;
                default -> throw new IllegalArgumentException(
                    "Unknown native_compatible_forms provider: " + name);
            };
            if (!forms.add(form))
                throw new IllegalArgumentException("Duplicate native_compatible_forms provider: " + name);
        }
        return Set.copyOf(forms);
    }
}
