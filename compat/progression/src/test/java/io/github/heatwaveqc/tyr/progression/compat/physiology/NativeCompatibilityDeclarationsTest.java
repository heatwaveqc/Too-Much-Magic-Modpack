package io.github.heatwaveqc.tyr.progression.compat.physiology;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Form;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NativeCompatibilityDeclarationsTest {
    private static Set<Form> parse(String json) {
        return NativeCompatibilityDeclarations.read(JsonParser.parseString(json).getAsJsonObject());
    }

    @Test void absentMeansNoAutomaticHybridPermissions() {
        assertEquals(Set.of(), parse("{}"));
        assertEquals(Set.of(), parse("{\"native_compatible_forms\":[]}"));
    }

    @Test void eachApprovedDragonPairCanBeDeclaredExactly() {
        assertEquals(Set.of(Form.CAVE_DRAGON), parse("{\"native_compatible_forms\":[\"cave_dragon\"]}"));
        assertEquals(Set.of(Form.FOREST_DRAGON), parse("{\"native_compatible_forms\":[\"forest_dragon\"]}"));
        assertEquals(Set.of(Form.SEA_DRAGON), parse("{\"native_compatible_forms\":[\"sea_dragon\"]}"));
    }

    @Test void supportsExplicitNonDragonProvidersWithoutImplicitPairs() {
        assertEquals(Set.of(Form.JAWR_VAMPIRE, Form.GOETY_LICH),
            parse("{\"native_compatible_forms\":[\"jawr_vampire\",\"goety_lich\"]}"));
    }

    @Test void rejectsBroadDragonWildcardAndTypos() {
        assertThrows(IllegalArgumentException.class, () -> parse("{\"native_compatible_forms\":[\"dragon\"]}"));
        assertThrows(IllegalArgumentException.class, () -> parse("{\"native_compatible_forms\":[\"cav_dragon\"]}"));
    }

    @Test void malformedAndRepeatedEntriesFailClosed() {
        for (String raw : Set.of(
                "{\"native_compatible_forms\":null}",
                "{\"native_compatible_forms\":\"cave_dragon\"}",
                "{\"native_compatible_forms\":[3]}",
                "{\"native_compatible_forms\":[\"sea_dragon\",\"sea_dragon\"]}")) {
            assertThrows(IllegalArgumentException.class, () -> parse(raw), raw);
        }
    }
}
