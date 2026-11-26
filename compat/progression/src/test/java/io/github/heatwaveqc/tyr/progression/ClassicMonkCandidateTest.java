package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import static org.junit.jupiter.api.Assertions.*;

/** The historical Monk fixtures remain separate; parse the current Classic candidate with generated harvest tiers. */
class ClassicMonkCandidateTest {
    private static final String MONK="tyr_progression:monk";
    private static final Path CANDIDATE=Path.of("design","tree-drafts","classic-monk-v1","monk.json");
    static Map<String,Set<String>> vanillaSurvey(){
        return Map.of(
            "minecraft:wooden_pickaxe",Set.of("minecraft:stone"),
            "minecraft:stone_pickaxe",Set.of("minecraft:stone","minecraft:iron_ore","minecraft:copper_ore"),
            "minecraft:iron_pickaxe",Set.of("minecraft:stone","minecraft:iron_ore","minecraft:copper_ore","minecraft:diamond_ore"),
            "minecraft:diamond_pickaxe",Set.of("minecraft:stone","minecraft:iron_ore","minecraft:copper_ore","minecraft:diamond_ore","minecraft:obsidian"));
    }
    @Test void installedCandidateParsesAndRequiresAllPaidRanksAndBothBosses() throws Exception {
        Assumptions.assumeTrue(Files.isRegularFile(CANDIDATE),"config-only candidate not present on standalone engine branch");
        var raw=JsonParser.parseString(Files.readString(CANDIDATE)).getAsJsonObject();
        var expanded=HarvestGeneration.expand(MONK,raw,(i,b)->vanillaSurvey());
        var path=TestPaths.read(MONK,expanded.path());
        ChallengeHudSpec.parse(expanded.path(),path);
        HeightFallTrialSpec.parse(expanded.path(),path);
        ObservedTrialSpec.parse(expanded.path(),path);
        TimedEnrollment.parse(expanded.path());
        assertEquals(125,path.skills().stream().filter(s->s.baseCost()>0).mapToInt(s->s.maxRank()).sum());
        assertTrue(path.skills().stream().filter(s->s.baseCost()>0).allMatch(s->s.baseCost()==1&&s.stepCost()==0));
        var gate=raw.getAsJsonObject("mastery_completion");
        var conditions=gate.getAsJsonObject("when").getAsJsonArray("conditions");
        Eligibility.parse(gate.get("when"));
        try(var core=ClassicMonkCandidateTest.class.getClassLoader().getResourceAsStream("data/tyr_progression/tyr_progression/paths/core.json")){
            assertNotNull(core);
            var coreSkills=JsonParser.parseReader(new java.io.InputStreamReader(core,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject().getAsJsonArray("skills");
            for(String family:new String[]{"reservoir","wellspring","thrift","potency"}){
                var expected=java.util.stream.StreamSupport.stream(coreSkills.spliterator(),false).map(x->x.getAsJsonObject()).filter(x->x.get("id").getAsString().equals("tyr_progression:tmm_magic_"+family+"_1")).findFirst().orElseThrow();
                var actual=java.util.stream.StreamSupport.stream(raw.getAsJsonArray("skills").spliterator(),false).map(x->x.getAsJsonObject()).filter(x->x.get("id").getAsString().equals("tyr_progression:classic/inner_"+family)).findFirst().orElseThrow();
                var effects=actual.getAsJsonArray("effects").deepCopy();
                for(var e:effects){assertTrue(e.getAsJsonObject().remove("unarmored").getAsBoolean());}
                assertEquals(expected.getAsJsonArray("effects"),effects,"Core schedule parity: "+family);
            }
        }
        int rankSum=0;boolean dragon=false,wither=false;
        var skillIds=new java.util.HashSet<String>();
        for(var el:conditions){
            var condition=el.getAsJsonObject();
            String type=condition.get("type").getAsString(),id=condition.get("id").getAsString();
            if(type.equals("skill_rank")){
                var skill=path.skill(id);assertNotNull(skill);
                assertTrue(skillIds.add(id));assertEquals(skill.maxRank(),condition.get("minimum").getAsInt());
                rankSum+=skill.maxRank();
            }
            if(type.equals("advancement")&&id.equals("minecraft:end/kill_dragon"))dragon=true;
            if(type.equals("milestone")){
                assertTrue(path.milestones().stream().anyMatch(m->m.id().equals(id)),"Missing mastery receipt "+id);
                if(id.equals("tyr_progression:classic/wither_trial"))wither=true;
            }
        }
        assertEquals(125,rankSum);assertTrue(dragon&&wither);
        assertTrue(path.milestones().stream().anyMatch(m->m.id().equals(gate.get("milestone").getAsString())&&m.points()==0&&m.xp()==0));
    }
}
