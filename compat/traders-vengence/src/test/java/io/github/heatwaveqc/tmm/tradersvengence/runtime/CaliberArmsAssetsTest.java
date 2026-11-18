package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CaliberArmsAssetsTest {
    private static final String ROOT = "/META-INF/traders_vengence/caliberarms/";
    @Test void allAuditedDonorFilesArePresentAndByteExact() throws Exception {
        try (var input = getClass().getResourceAsStream(ROOT + "provenance.json")) {
            assertNotNull(input);
            var manifest = JsonParser.parseReader(new InputStreamReader(input, StandardCharsets.UTF_8)).getAsJsonObject();
            assertEquals("88068f10a62af5af35b2e8256137cb5160ea5eb2cc8f42644a0fa42722931f90",
                manifest.get("jar_sha256").getAsString());
            var files = manifest.getAsJsonObject("files");
            assertEquals(15, files.size());
            assertTrue(files.has("META-INF/traders_vengence/caliberarms/LICENSE.txt"));
            assertTrue(files.has("META-INF/traders_vengence/caliberarms/CREDITS.md"));
            for (var entry : files.entrySet()) {
                try (var resource = getClass().getResourceAsStream("/" + entry.getKey())) {
                    assertNotNull(resource, entry.getKey());
                    assertEquals(entry.getValue().getAsString(), HexFormat.of().formatHex(
                        MessageDigest.getInstance("SHA-256").digest(resource.readAllBytes())), entry.getKey());
                }
            }
        }
    }
}
