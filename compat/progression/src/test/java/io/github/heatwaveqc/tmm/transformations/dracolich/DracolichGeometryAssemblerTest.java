package io.github.heatwaveqc.tmm.transformations.dracolich;

import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

class DracolichGeometryAssemblerTest {
    private JsonObject nativeFixture() {
        // Synthetic interface fixture; no extracted Dragon Survival assets in tests.
        return JsonParser.parseString("""
            {"format_version":"1.12.0","minecraft:geometry":[{"description":{},"bones":[
              {"name":"Root","pivot":[0,0,0],"cubes":[{"sentinel":"must disappear"}]},
              {"name":"Torso","parent":"Root","pivot":[1,2,3],"rotation":[0,15,0]},
              {"name":"MountingBone","parent":"Torso","pivot":[1,4,3]}]}]}
            """).getAsJsonObject();
    }
    private JsonObject meshFixture() {
        return JsonParser.parseString("""
            {"texture_size":[1024,1024],"attachments":{"Torso":[{"origin":[0,0,0],"size":[1,1,1],"uv":[0,0]}]}}
            """).getAsJsonObject();
    }
    private com.google.gson.JsonArray bones(JsonObject model) {
        return model.getAsJsonArray("minecraft:geometry").get(0).getAsJsonObject().getAsJsonArray("bones");
    }

    @Test void preservesRigAndOriginalInputButReplacesAllNativeCubes() {
        JsonObject source = nativeFixture(), before = source.deepCopy();
        String hash = DracolichGeometryAssembler.fingerprint(bones(source));
        JsonObject assembled = DracolichGeometryAssembler.assemble(source, meshFixture(), hash);
        assertEquals(before, source);
        assertEquals(hash, DracolichGeometryAssembler.fingerprint(bones(assembled)));
        assertFalse(bones(assembled).get(0).getAsJsonObject().has("cubes"));
        assertEquals(meshFixture().getAsJsonObject("attachments").get("Torso"),
                bones(assembled).get(1).getAsJsonObject().get("cubes"));
        assertEquals(bones(source).get(2), bones(assembled).get(2));
    }

    @Test void changedRigOrUnknownAttachmentFailsClosed() {
        JsonObject source = nativeFixture();
        String hash = DracolichGeometryAssembler.fingerprint(bones(source));
        bones(source).get(1).getAsJsonObject().addProperty("parent", "MountingBone");
        assertThrows(IllegalArgumentException.class, () -> DracolichGeometryAssembler.assemble(source, meshFixture(), hash));
        JsonObject mesh = meshFixture();
        mesh.getAsJsonObject("attachments").add("UnknownJoint", new com.google.gson.JsonArray());
        assertThrows(IllegalArgumentException.class, () -> DracolichGeometryAssembler.assemble(nativeFixture(), mesh, hash));
    }

    @Test void numericSpellingDoesNotInvalidateAnIdenticalRig() {
        JsonObject source = nativeFixture();
        String hash = DracolichGeometryAssembler.fingerprint(bones(source));
        bones(source).get(0).getAsJsonObject().add("pivot", JsonParser.parseString("[0.0,0e0,-0.000]"));
        assertEquals(hash, DracolichGeometryAssembler.fingerprint(bones(source)));
    }
}
