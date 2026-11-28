package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.foundation.GarmentPalette;
import io.github.heatwaveqc.tyr.progression.foundation.MartialGarmentStyle;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MartialGarmentTest {
    @Test void all4096PalettesAreUniqueAndRoundTrip() {
        var seen = new HashSet<Integer>();
        for (int a=0;a<16;a++) for (int b=0;b<16;b++) for (int c=0;c<16;c++) {
            var p = GarmentPalette.of(a,b,c);
            assertTrue(seen.add(p.packed()));
            var q = new GarmentPalette(p.packed());
            assertEquals(a,q.dye(0)); assertEquals(b,q.dye(1)); assertEquals(c,q.dye(2));
        }
        assertEquals(4096,seen.size());
        assertThrows(IllegalArgumentException.class,()->new GarmentPalette(-1));
        assertThrows(IllegalArgumentException.class,()->new GarmentPalette(4096));
        assertThrows(IllegalArgumentException.class,()->GarmentPalette.of(0,16,0));
        assertThrows(IllegalArgumentException.class,()->GarmentPalette.of(0,0,0).dye(3));
    }

    @Test void allTwelveRecipesHaveEqualSlotCostsDistinctShapesAndStyleDefaults() throws Exception {
        assertEquals(6,MartialGarmentStyle.values().length);
        var seen = new HashSet<String>();
        for (var style : MartialGarmentStyle.values()) {
            for (String id : new String[]{style.upperId(),style.lowerId()}) {
                try (var stream = getClass().getResourceAsStream("/data/tyr_progression/recipe/"+id+".json")) {
                    assertNotNull(stream,id);
                    var recipe = JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
                    var pattern = recipe.getAsJsonArray("pattern");
                    var normal = new StringBuilder();
                    var mirror = new StringBuilder();
                    for (var line : pattern) {
                        String row = line.getAsString();
                        normal.append(row); mirror.append(new StringBuilder(row).reverse());
                    }
                    String raw = normal.toString(), reflected = mirror.toString();
                    assertTrue(seen.add(raw.compareTo(reflected)<0?raw:reflected),id);
                    assertEquals(id.endsWith("_upper")?5:4,raw.chars().filter(c->c=='W').count(),id);
                    assertEquals(2,raw.chars().filter(c->c=='S').count(),id);
                    var result = recipe.getAsJsonObject("result");
                    assertEquals("tyr_progression:"+id,result.get("id").getAsString());
                    assertEquals(style.defaults().packed(),result.getAsJsonObject("components").get("tyr_progression:garment_palette").getAsInt());
                }
            }
        }
        assertEquals(12,seen.size());
    }
}
