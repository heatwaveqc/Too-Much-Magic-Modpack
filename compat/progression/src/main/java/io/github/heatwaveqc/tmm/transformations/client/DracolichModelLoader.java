package io.github.heatwaveqc.tmm.transformations.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichGeometryAssembler;
import java.io.IOException;
import java.io.Reader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

/** Called only inside the client GeckoLib model resource reload. No file output. */
public final class DracolichModelLoader {
    public static final ResourceLocation MODEL = id("geo/dracolich_cave_center.geo.json");
    public static final ResourceLocation TEXTURE = id("textures/entity/dracolich/cave_center.png");
    public static final ResourceLocation GLOW = id("textures/entity/dracolich/cave_center_glow.png");
    private static final ResourceLocation NATIVE = ResourceLocation.fromNamespaceAndPath(
            "dragonsurvival", "geo/dragon_model.geo.json");
    private DracolichModelLoader() { }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(TransformationsMod.MOD_ID, path);
    }

    public static Object load(ResourceManager resources) {
        JsonObject marker;
        try {
            marker = read(resources, MODEL);
            JsonObject model;
            try {
                JsonObject nativeModel = read(resources, NATIVE);
                JsonObject mesh = read(resources, id("dracolich/cave_center.mesh.json"));
                String expected = read(resources, id("dracolich/cave_center.rig.json"))
                        .get("rig_sha256_normalized").getAsString();
                model = DracolichGeometryAssembler.assemble(nativeModel, mesh, expected);
                TransformationsMod.LOGGER.info("Assembled Cave/Center Dracolich geometry from the installed native rig");
            } catch (IOException | RuntimeException failure) {
                TransformationsMod.LOGGER.warn("Dracolich visual resource unsupported; keeping ordinary dragon visuals: {}",
                        failure.toString());
                model = marker; // Selectors reject this empty marker, so normal dragons remain visible.
            }
            Class<?> adapter = Class.forName("software.bernie.geckolib.loading.json.typeadapter.KeyFramesAdapter");
            Gson gson = (Gson) adapter.getField("GEO_GSON").get(null);
            return gson.fromJson(model, Class.forName("software.bernie.geckolib.loading.json.raw.Model"));
        } catch (ReflectiveOperationException | IOException failure) {
            throw new IllegalStateException("Cannot deserialize Dracolich model marker", failure);
        }
    }

    private static JsonObject read(ResourceManager resources, ResourceLocation id) throws IOException {
        try (Reader reader = resources.getResourceOrThrow(id).openAsReader()) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }
}
