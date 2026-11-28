package io.github.heatwaveqc.tyr.progression.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Protect the reviewed four-node Core candidate against obsolete namespaced imports. */
class CoreMagicPilotTest {
    @Test void sourceBackedCoreFundamentalsArePlayableAndCostOnePerRank() throws Exception {
        String path = "/data/tyr_progression/tyr_progression/paths/core.json";
        try (var raw = CoreMagicPilotTest.class.getResourceAsStream(path)) {
            assertNotNull(raw, "Core datapack must be bundled with the Progression jar");
            var parsed = JsonParser.parseReader(
                    new InputStreamReader(raw, StandardCharsets.UTF_8)).getAsJsonObject();
            assertEquals("automatic", parsed.get("enrollment").getAsString());
            assertEquals("tyr_progression:mxp", parsed.get("xp_channel").getAsString());
            var skills = parsed.getAsJsonArray("skills");
            assertEquals(4, skills.size(), "Remaining 23 Core nodes are not yet reviewed live");
            var ids = new HashSet<String>();
            for (var el : skills) {
                JsonObject skill = el.getAsJsonObject();
                String id = skill.get("id").getAsString();
                assertTrue(id.startsWith("tyr_progression:"), id);
                assertTrue(ids.add(id), "Duplicate Core skill: " + id);
                assertEquals(5, skill.get("max_rank").getAsInt());
                assertEquals(1, skill.get("required_rank").getAsInt());
                assertEquals("tyr_progression:mxp", skill.get("xp_channel").getAsString());
                var costs = skill.getAsJsonArray("rank_costs");
                assertEquals(5, costs.size());
                for (var cost : costs) assertEquals(1, cost.getAsInt());
                assertFalse(skill.getAsJsonArray("effects").isEmpty());
                for (var fxEl : skill.getAsJsonArray("effects")) {
                    var fx = fxEl.getAsJsonObject();
                    assertTrue(java.util.Set.of("attribute","magic_potency").contains(fx.get("type").getAsString()));
                    assertEquals(5, fx.getAsJsonArray("rank_values").size());
                }
            }
            assertTrue(ids.contains("tyr_progression:tmm_magic_reservoir_1"));
            assertTrue(ids.contains("tyr_progression:tmm_magic_wellspring_1"));
            assertTrue(ids.contains("tyr_progression:tmm_magic_thrift_1"));
            assertTrue(ids.contains("tyr_progression:tmm_magic_potency_1"));
            var expected=java.util.Map.ofEntries(
                java.util.Map.entry("ars_nouveau:ars_nouveau.perk.max_mana",100d),
                java.util.Map.entry("irons_spellbooks:max_mana",100d),
                java.util.Map.entry("psi:total_psi",5000d),
                java.util.Map.entry("ars_nouveau:ars_nouveau.perk.mana_regen",2.5d),
                java.util.Map.entry("irons_spellbooks:mana_regen",1d),
                java.util.Map.entry("psi:regen",25d),
                java.util.Map.entry("forbidden_arcanus:generic.aureal_regeneration",2.5d),
                java.util.Map.entry("goety:soul_discount",.3d),
                java.util.Map.entry("neovitae:sigil_cost_reduction",30d),
                java.util.Map.entry("ars_nouveau:ars_nouveau.perk.spell_damage",5d),
                java.util.Map.entry("irons_spellbooks:spell_power",1d),
                java.util.Map.entry("goety:spell_potency",100d),
                java.util.Map.entry("eidolon_repraised:frost",1d));
            var seen=new HashSet<String>();
            for(var el:skills)for(var fxEl:el.getAsJsonObject().getAsJsonArray("effects")){
                var fx=fxEl.getAsJsonObject();
                String binding=fx.has("attribute")?fx.get("attribute").getAsString():fx.get("target").getAsString();
                assertTrue(seen.add(binding),binding);
                assertNotNull(expected.get(binding),binding);
                for(int n=1;n<=5;n++)
                    assertEquals(expected.get(binding)*n/5,fx.getAsJsonArray("rank_values").get(n-1).getAsDouble(),1e-9,binding);
            }
            assertEquals(expected.keySet(),seen);
        }
    }
}
