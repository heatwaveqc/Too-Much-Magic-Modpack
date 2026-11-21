package io.github.heatwaveqc.tmm.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tmm.progression.data.PathJson;
import io.github.heatwaveqc.tmm.progression.data.PotionEnrollmentTrigger;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class PotionEnrollmentTriggerTest {
    private static final String RITUAL = """
        {"schema":1,"rank_xp":[0,20],"enrollment":"potion",
         "enrollment_trigger":{
           "item":"minecraft:potion",
           "potions":["minecraft:poison","minecraft:long_poison","minecraft:strong_poison"],
           "required_effect":"minecraft:regeneration",
           "required_advancement":"minecraft:adventure/kill_a_mob",
           "clear_effects":["minecraft:poison"],
           "completion_event":"tmm_progression:hunter_trial_of_venom"
          }}
        """;

    private static JsonObject json(String source) {
        return JsonParser.parseString(source).getAsJsonObject();
    }

    @Test void acceptsThreeOnlyVanillaDrinkablePoisonTypes() {
        var trigger = PotionEnrollmentTrigger.parse(json(RITUAL));
        assertNotNull(trigger);
        for (String potion : trigger.potions()) {
            assertTrue(trigger.matches("minecraft:potion",potion,true,true));
        }
        assertFalse(trigger.matches("minecraft:splash_potion","minecraft:poison",true,true));
        assertFalse(trigger.matches("minecraft:lingering_potion","minecraft:poison",true,true));
        assertFalse(trigger.matches("minecraft:potion","minecraft:regeneration",true,true));
        assertFalse(trigger.matches("minecraft:potion","minecraft:poison",false,true));
        assertFalse(trigger.matches("minecraft:potion","minecraft:poison",true,false));
        assertEquals("minecraft:regeneration",trigger.requiredEffect());
        assertEquals("minecraft:adventure/kill_a_mob",trigger.requiredAdvancement());
        assertEquals("tmm_progression:hunter_trial_of_venom",trigger.completionEvent());
    }

    @Test void rejectMissingOrMalformedSafetyRequirements() {
        assertThrows(IllegalArgumentException.class,() -> TestPaths.read("test:hunter",
            json(RITUAL.replace("\"required_advancement\"", "\"typo_advancement\""))));
        assertThrows(IllegalArgumentException.class,() -> TestPaths.read("test:hunter",
            json(RITUAL.replace("\"minecraft:poison\",\"minecraft:long_poison\",\"minecraft:strong_poison\"",
                               "\"minecraft:poison\",\"minecraft:poison\""))));
        assertThrows(IllegalArgumentException.class,() -> TestPaths.read("test:hunter",
            json(RITUAL.replace("\"enrollment\":\"potion\"","\"enrollment\":\"manual\""))));
        assertThrows(IllegalArgumentException.class,() -> TestPaths.read("test:hunter",
            json(RITUAL.replace("\"clear_effects\":[\"minecraft:poison\"],",""))));
        assertThrows(IllegalArgumentException.class,() -> TestPaths.read("test:hunter",
            json(RITUAL.replace("\"potions\":[\"minecraft:poison\",\"minecraft:long_poison\",\"minecraft:strong_poison\"],",
                               "\"potions\":[],\"extra_script\":\"doBadThings\","))));
    }

    @Test void actualHunterDatapackUsesRitualSchema() throws Exception {
        try (var stream = getClass().getClassLoader().getResourceAsStream(
            "data/tmm_progression/tmm_progression/paths/hunter.json")) {
            assertNotNull(stream);
            var json = JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
            var path = TestPaths.read("tmm_progression:hunter",json);
            var trigger = PotionEnrollmentTrigger.parse(json);
            assertNotNull(trigger);
            assertEquals("tmm_progression:human_disciplines",path.exclusiveGroup());
            assertEquals("potion",json.get("enrollment").getAsString());
            assertTrue(path.milestones().stream().anyMatch(m -> m.id().equals("tmm_progression:trial_of_venom")));
            assertEquals(3,trigger.potions().size());
        }
    }

    @Test void normalNonPotionPathStillValid() {
        assertNull(PotionEnrollmentTrigger.parse(json("""
            {"schema":1,"rank_xp":[0],"enrollment":"sunset"}
            """)));
    }
}
