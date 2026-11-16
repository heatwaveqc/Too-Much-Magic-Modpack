package io.github.heatwaveqc.tmm.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tmm.progression.core.ProgressionMath;
import io.github.heatwaveqc.tmm.progression.core.Signal;
import io.github.heatwaveqc.tmm.progression.data.PathJson;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.state.Track;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Source-checked training XP experiment, not multiplayer or client gameplay QA. */
final class MonkClassicXpTrialTest {
    private static JsonObject json() throws Exception {
        String resource = "data/tmm_progression/tmm_progression/paths/monk.json";
        try (InputStream input = MonkClassicXpTrialTest.class.getClassLoader()
                .getResourceAsStream(resource)) {
            assertNotNull(input,"Standalone Monk datapack missing");
            return JsonParser.parseReader(
                    new InputStreamReader(input,StandardCharsets.UTF_8)).getAsJsonObject();
        }
    }

    private static Definition.Path path() throws Exception {
        return PathJson.read("tmm_progression:monk",json());
    }

    private static JsonObject original() throws Exception {
        return JsonParser.parseString(Files.readString(
                Path.of("design","monk-original-21-gate-inventory-v1.json"))).getAsJsonObject();
    }

    private static Definition.Activity activity(Definition.Path path,String event,String selector) {
        return path.activities().stream().filter(x->
                x.event().equals(event) && x.selector().equals(selector))
                .findFirst().orElseThrow(()->new AssertionError(
                        "Missing configured training activity "+event+"/"+selector));
    }

    private static Definition.Milestone milestone(Definition.Path path,String id) {
        return path.milestones().stream().filter(m->m.id().equals("tmm_progression:"+id))
                .findFirst().orElseThrow(()->new AssertionError(
                        "Missing configured milestone "+id));
    }

    @Test void originalTwentyOneGateInventoryHasNoInventedHardRequirements() throws Exception {
        JsonObject audit = original();
        assertEquals(21,audit.get("original_numbered_stages").getAsInt());
        var stages = audit.getAsJsonArray("stages");
        assertEquals(21,stages.size());
        Definition.Path spec = path();
        Set<String> ids = new HashSet<>();
        int[] categories = new int[4]; // implemented, pending, replaced, partial
        for (int i=0;i<stages.size();i++) {
            JsonObject gate = stages.get(i).getAsJsonObject();
            assertEquals(i,gate.get("classic_stage").getAsInt());
            JsonObject xp = gate.getAsJsonObject("xp_design");
            String status = xp.get("status").getAsString();
            String milestone = xp.get("milestone_id").getAsString();
            assertTrue(ids.add(milestone),"Gate mapping must be distinct");
            assertFalse(xp.get("enforced_as_rank_gate").getAsBoolean());
            assertFalse(gate.get("verified_original_behavior").getAsString().isBlank());
            if (status.startsWith("live")) categories[0]++;
            else if (status.startsWith("pending")) categories[1]++;
            else if (status.startsWith("replaced")) categories[2]++;
            else if (status.startsWith("partial")) categories[3]++;
            else fail("Unclassified original stage "+i+": "+status);
            // Only verified server sources or intentional substitutes belong
            // in the loadable runtime. Proposed future deeds stay design-only.
            if (!status.startsWith("pending"))
                assertNotNull(milestone(spec,milestone.substring("tmm_progression:".length())));
            else
                assertTrue(spec.milestones().stream().noneMatch(m->m.id().equals(milestone)),
                        "No false-progress placeholder for unwired classic trial: "+milestone);
        }
        assertArrayEquals(new int[]{8,9,2,2},categories);
        assertEquals("replaced_by_first_sunset_initiation",
                stages.get(0).getAsJsonObject().getAsJsonObject("xp_design")
                        .get("status").getAsString());
        assertEquals("replaced_by_verified_dragon_momentum_glide",
                stages.get(20).getAsJsonObject().getAsJsonObject("xp_design")
                        .get("status").getAsString());
    }

    @Test void actualServerSignalTypesAreDistinctFromGenericToolAndMagicEvents() throws Exception {
        var bareLog = new Signal("barehand_block_break","minecraft:oak_log",
                Set.of("minecraft:logs","minecraft:mineable/axe"));
        var ordinaryToolLog = new Signal("block_break","minecraft:oak_log",
                Set.of("minecraft:logs","minecraft:mineable/axe"));
        var fistZombie = new Signal("unarmed_kill","minecraft:zombie",
                Set.of("tmm_progression:undead","tmm_progression:monk_training_hostiles"));
        var spellZombie = new Signal("kill","minecraft:zombie",
                Set.of("tmm_progression:undead","tmm_progression:monk_training_hostiles"));

        assertTrue(bareLog.matches("barehand_block_break","#minecraft:logs"));
        assertFalse(ordinaryToolLog.matches("barehand_block_break","#minecraft:logs"));
        assertTrue(fistZombie.matches("unarmed_kill","#tmm_progression:undead"));
        assertFalse(spellZombie.matches("unarmed_kill","#tmm_progression:undead"));
        assertTrue(spellZombie.matches("kill","#tmm_progression:monk_training_hostiles"));
        assertEquals(2,activity(path(),"barehand_block_break","#minecraft:logs").xp());
        assertEquals(8,activity(path(),"kill","#tmm_progression:monk_training_hostiles").xp());
        assertEquals(4,activity(path(),"unarmed_kill","#tmm_progression:monk_training_hostiles").xp());
        String invalid = json().toString().replace("\"unarmed_kill\"","\"not_a_trusted_event\"");
        assertThrows(IllegalArgumentException.class, ()->
                PathJson.read("tmm_progression:monk",JsonParser.parseString(invalid).getAsJsonObject()));
    }

    @Test void trainingCurveAndClassicMilestonesUseNativePathXpNotTyz() throws Exception {
        Definition.Path spec = path();
        assertEquals(20,spec.maxRank());
        assertEquals(20L,spec.rankXp().get(1).longValue());
        assertEquals(55L,spec.rankXp().get(2).longValue());
        assertEquals(105L,spec.rankXp().get(3).longValue());
        assertEquals(14300L,spec.rankXp().getLast().longValue());
        assertEquals(7,spec.activities().size());
        assertEquals(16,spec.milestones().size());
        assertEquals(10,activity(spec,"food_discovered","*").xp());
        assertEquals(25,activity(spec,"advancement","*").xp());

        // Published modeling examples are NOT event simulations; they verify
        // the monotone XP/rank arithmetic using the actual live curve.
        Track initial = ProgressionMath.join(spec,Track.empty());
        assertEquals(1,initial.rank());
        Track early = ProgressionMath.grantXp(spec,initial,259);
        assertEquals(5,early.rank(),"First-hour illustrative activity + deed budget");
        Track mid = ProgressionMath.grantXp(spec,early,1730-259);
        assertEquals(11,mid.rank(),"Mixed Monk activities + optional trials");
        Track late = ProgressionMath.grantXp(spec,mid,15340-1730);
        assertEquals(20,late.rank(),"High-activity endgame path");
        assertEquals(15340L,late.xp());
        assertEquals(8,late.skillRank("tmm_progression:iron_palm"));
        assertEquals(1,late.skillRank("tmm_progression:momentum_glide"),
                "Glide may be rank-granted but the effect still requires credited Dragon milestone");
        assertSame(late,ProgressionMath.grantXp(spec,late,0));
    }

    @Test void classicDeedBonusesAreOneTimeAndDoNotBlockRankRewards() throws Exception {
        Definition.Path spec = path();
        assertEquals(10,milestone(spec,"trial_ten_logs").criteria().getFirst().count());
        assertEquals("barehand_block_break",
                milestone(spec,"trial_ten_logs").criteria().getFirst().event());
        assertEquals(5,milestone(spec,"trial_five_stone").criteria().getFirst().count());
        assertEquals(10,milestone(spec,"trial_ten_unarmed_undead").criteria().getFirst().count());
        assertEquals("unarmed_kill",
                milestone(spec,"trial_ten_unarmed_undead").criteria().getFirst().event());
        assertEquals(1,milestone(spec,"trial_bedrock_sleep").criteria().getFirst().count());
        assertEquals("external",
                milestone(spec,"trial_bedrock_sleep").criteria().getFirst().event());
        assertEquals("tmm_progression:bedrock_rest_completed",
                milestone(spec,"trial_bedrock_sleep").criteria().getFirst().selector());
        assertEquals(230,milestone(spec,"trial_bedrock_sleep").xp());
        assertEquals(5,milestone(spec,"blind_ordeal_victory").criteria().getFirst().count(),
                "Historic Blindness ordeal must count five hostile kills");
        assertEquals("external",
                milestone(spec,"trial_creeper_kiss").criteria().getFirst().event());
        assertEquals("tmm_progression:creeper_kissed",
                milestone(spec,"trial_creeper_kiss").criteria().getFirst().selector());
        assertEquals("minecraft:end/kill_dragon",
                milestone(spec,"dragon_defeated").criteria().getFirst().selector());

        for (Definition.Skill skill: spec.skills()) {
            if (skill.autoRanks().isEmpty()) continue;
            assertTrue(skill.requiresMilestones().isEmpty(),
                    "Classic trials cannot block ordinary cultivation ranks: "+skill.id());
            assertTrue(skill.requiresSkills().isEmpty());
            assertEquals("",skill.choiceGroup());
            assertEquals(0,skill.baseCost());
        }
        var glide = spec.skill("tmm_progression:momentum_glide");
        assertEquals("tmm_progression:dragon_defeated",
                glide.effects().getFirst().params().get("requires_milestone"));
    }

    @Test void trainingMobsTagIsExplicitVanillaPilotAndIncludesBothOriginalEnemyGroups()
            throws Exception {
        String tag = "data/tmm_progression/tags/entity_type/monk_training_hostiles.json";
        try (InputStream input = MonkClassicXpTrialTest.class.getClassLoader()
                .getResourceAsStream(tag)) {
            assertNotNull(input,"Training target datapack tag absent");
            JsonObject source = JsonParser.parseReader(
                    new InputStreamReader(input,StandardCharsets.UTF_8)).getAsJsonObject();
            assertFalse(source.get("replace").getAsBoolean());
            var ids = new HashSet<String>();
            source.getAsJsonArray("values").forEach(e->ids.add(e.getAsString()));
            assertTrue(ids.contains("#tmm_progression:undead"));
            assertTrue(ids.contains("#tmm_progression:raiders"));
            assertTrue(ids.contains("minecraft:creeper"));
            assertTrue(ids.contains("minecraft:ender_dragon"));
            assertFalse(ids.contains("minecraft:cow"),
                    "No generic passive livestock kill farm");
        }
    }
}
