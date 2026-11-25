package io.github.heatwaveqc.tmm.transformations.dracolich;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Pure in-memory assembly. The installed native rig is never written or distributed. */
public final class DracolichGeometryAssembler {
    private DracolichGeometryAssembler() { }

    public static JsonObject assemble(JsonObject nativeModel, JsonObject mesh, String expectedHash) {
        JsonArray nativeBones = nativeModel.getAsJsonArray("minecraft:geometry").get(0)
                .getAsJsonObject().getAsJsonArray("bones");
        if (!fingerprint(nativeBones).equals(expectedHash)) {
            throw new IllegalArgumentException("Unsupported Dragon Survival rig fingerprint");
        }
        JsonObject attachments = mesh.getAsJsonObject("attachments");
        Set<String> names = new HashSet<>();
        for (JsonElement entry : nativeBones) {
            if (!names.add(entry.getAsJsonObject().get("name").getAsString())) {
                throw new IllegalArgumentException("Duplicate native bone");
            }
        }
        if (!names.containsAll(attachments.keySet())) throw new IllegalArgumentException("Unknown mesh attachment");
        JsonObject result = nativeModel.deepCopy();
        JsonObject geometry = result.getAsJsonArray("minecraft:geometry").get(0).getAsJsonObject();
        JsonObject description = new JsonObject();
        description.addProperty("identifier", "geometry.tmm_dracolich_cave_center");
        description.addProperty("texture_width", mesh.getAsJsonArray("texture_size").get(0).getAsInt());
        description.addProperty("texture_height", mesh.getAsJsonArray("texture_size").get(1).getAsInt());
        description.addProperty("visible_bounds_width", 24);
        description.addProperty("visible_bounds_height", 24);
        JsonArray offset = new JsonArray(); offset.add(0); offset.add(2); offset.add(0);
        description.add("visible_bounds_offset", offset);
        geometry.add("description", description);
        for (JsonElement entry : geometry.getAsJsonArray("bones")) {
            JsonObject bone = entry.getAsJsonObject();
            bone.remove("cubes");
            String name = bone.get("name").getAsString();
            if (attachments.has(name)) bone.add("cubes", attachments.get(name).deepCopy());
        }
        return result;
    }

    public static String fingerprint(JsonArray bones) {
        JsonArray signature = new JsonArray();
        for (JsonElement entry : bones) {
            JsonObject bone = entry.getAsJsonObject().deepCopy();
            bone.remove("cubes"); signature.add(bone);
        }
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(canonical(signature).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException impossible) { throw new IllegalStateException(impossible); }
    }

    /** Numeric normalization makes 1, 1.0 and exponent spelling equivalent. */
    private static String canonical(JsonElement value) {
        if (value.isJsonObject()) {
            StringBuilder out = new StringBuilder("{");
            for (Map.Entry<String, JsonElement> entry : new TreeMap<>(value.getAsJsonObject().asMap()).entrySet()) {
                if (out.length() > 1) out.append(',');
                out.append(new com.google.gson.JsonPrimitive(entry.getKey())).append(':').append(canonical(entry.getValue()));
            }
            return out.append('}').toString();
        }
        if (value.isJsonArray()) {
            StringBuilder out = new StringBuilder("[");
            for (JsonElement element : value.getAsJsonArray()) {
                if (out.length() > 1) out.append(',');
                out.append(canonical(element));
            }
            return out.append(']').toString();
        }
        if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
            BigDecimal number = value.getAsBigDecimal().stripTrailingZeros();
            return number.signum() == 0 ? "0" : number.toPlainString();
        }
        return value.toString();
    }
}
