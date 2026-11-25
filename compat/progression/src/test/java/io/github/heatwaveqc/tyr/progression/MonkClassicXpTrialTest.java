package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.core.Signal;
import io.github.heatwaveqc.tyr.progression.data.PathJson;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Track;
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
        String resource = "data/tyr_progression/tyr_progression/paths/monk.json";
        try (InputStream input = MonkClassicXpTrialTest.class.getClassLoader()
                .getResourceAsStream(resource)) {
            assertNotNull(input,"Standalone Monk datapack missing");
            return TestPaths.project(JsonParser.parseReader(
                    new InputStreamReader(input,StandardCharsets.UTF_8)).getAsJsonObject());
        }
    }

    private static Definition.Path path() throws Exception {
        return TestPaths.read("tyr_progression:monk",json());
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
        return path.milestones().stream().filter(m->m.id().equals("tyr_progression:"+id))
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
                assertNotNull(milestone(spec,milestone.substring("tyr_progression:".length())));
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
                Set.of("tyr_progression:undead","tyr_progression:monk_training_hostiles"));
        var spellZombie = new Signal("kill","minecraft:zombie",
                Set.of("tyr_progression:undead","tyr_progression:monk_training_hostiles"));

        assertTrue(bareLog.matches("barehand_block_break","#minecraft:logs"));
        assertFalse(ordinaryToolLog.matches("barehand_block_break","#minecraft:logs"));
        assertTrue(fistZombie.matches("unarmed_kill","#tyr_progression:undead"));
        assertFalse(spellZombie.matches("unarmed_kill","#tyr_progression:undead"));
        assertTrue(spellZombie.matches("kill","#tyr_progression:monk_training_hostiles"));
        if(!path().activities().isEmpty()){
            assertEquals(2,activity(path(),"barehand_block_break","#minecraft:logs").xp());
            assertEquals(8,activity(path(),"kill","#tyr_progression:monk_training_hostiles").xp());
            assertEquals(4,activity(path(),"unarmed_kill","#tyr_progression:monk_training_hostiles").xp());
        }else assertTrue(path().activities().isEmpty(),"Unified Monk has no duplicate native activity XP");
        String invalid = json().toString().replace("\"unarmed_kill\"","\"not_a_trusted_event\"");
        assertThrows(IllegalArgumentException.class, ()->
                TestPaths.read("tyr_progression:monk",JsonParser.parseString(invalid).getAsJsonObject()));
    }

    @Test void trainingRewardsFollowConfiguredUnifiedOrLegacyModel() throws Exception {
        Definition.Path spec=path();
        if(spec.activities().isEmpty()){
            assertEquals(200,spec.maxRank());
            assertEquals(16,spec.milestones().size());
            assertTrue(spec.milestones().stream().allMatch(m->m.xp()==0&&m.points()==0));
            assertEquals(1000,milestone(spec,"trial_ten_logs").fixedRewardXp());
            assertEquals(0,milestone(spec,"dragon_defeated").fixedRewardXp());
            assertEquals("tyr_progression:mxp",
                milestone(spec,"trial_bedrock_sleep").fixedRewardSystem());
            assertEquals("external",milestone(spec,"trial_wither_victory").criteria().getFirst().event());
            assertEquals("tyr_progression:strict_barehand_wither_victory",
                milestone(spec,"trial_wither_victory").criteria().getFirst().selector());
        }else{
            assertEquals(20,spec.maxRank());
            assertEquals(20L,spec.rankXp().get(1).longValue());
            assertEquals(14300L,spec.rankXp().getLast().longValue());
            assertEquals(7,spec.activities().size());
            assertEquals(16,spec.milestones().size());
            assertEquals(10,activity(spec,"food_discovered","*").xp());
            assertEquals(25,activity(spec,"advancement","*").xp());
            Track initial=ProgressionMath.join(spec,Track.empty());
            Track late=ProgressionMath.grantXp(spec,initial,15340);
            assertEquals(20,late.rank());
            assertEquals(8,late.skillRank("tyr_progression:iron_palm"));
            assertEquals(1,late.skillRank("tyr_progression:momentum_glide"));
        }
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
        assertEquals("tyr_progression:bedrock_rest_completed",
                milestone(spec,"trial_bedrock_sleep").criteria().getFirst().selector());
        if(spec.activities().isEmpty()){
            assertEquals(0,milestone(spec,"trial_bedrock_sleep").xp());
            assertEquals(1000,milestone(spec,"trial_bedrock_sleep").fixedRewardXp());
        }else assertEquals(230,milestone(spec,"trial_bedrock_sleep").xp());
        assertEquals(5,milestone(spec,"blind_ordeal_victory").criteria().getFirst().count(),
                "Historic Blindness ordeal must count five hostile kills");
        assertEquals("external",
                milestone(spec,"trial_creeper_kiss").criteria().getFirst().event());
        assertEquals("tyr_progression:creeper_kissed",
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
        var glide = spec.skill("tyr_progression:momentum_glide");
        assertEquals(spec.activities().isEmpty()
                ?"tyr_progression:trial_wither_victory":"tyr_progression:dragon_defeated",
                glide.effects().getFirst().params().get("requires_milestone"));
    }

    @Test void trainingMobsTagIsExplicitVanillaPilotAndIncludesBothOriginalEnemyGroups()
            throws Exception {
        String tag = "data/tyr_progression/tags/entity_type/monk_training_hostiles.json";
        try (InputStream input = MonkClassicXpTrialTest.class.getClassLoader()
                .getResourceAsStream(tag)) {
            assertNotNull(input,"Training target datapack tag absent");
            JsonObject source = JsonParser.parseReader(
                    new InputStreamReader(input,StandardCharsets.UTF_8)).getAsJsonObject();
            assertFalse(source.get("replace").getAsBoolean());
            var ids = new HashSet<String>();
            source.getAsJsonArray("values").forEach(e->ids.add(e.getAsString()));
            assertTrue(ids.contains("#tyr_progression:undead"));
            assertTrue(ids.contains("#tyr_progression:raiders"));
            assertTrue(ids.contains("minecraft:creeper"));
            assertTrue(ids.contains("minecraft:ender_dragon"));
            assertFalse(ids.contains("minecraft:cow"),
                    "No generic passive livestock kill farm");
        }
    }
}
