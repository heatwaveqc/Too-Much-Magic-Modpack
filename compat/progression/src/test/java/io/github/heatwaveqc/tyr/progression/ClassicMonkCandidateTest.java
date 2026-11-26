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
        // Eligibility loads Minecraft server classes unavailable in standalone unit-test runtime.
        // Validate the declarative condition shape and every reference below; live registry parse remains B/C.
        assertEquals("and",gate.getAsJsonObject("when").get("type").getAsString());
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
        var aqua=raw.getAsJsonArray("skills").asList().stream().map(x->x.getAsJsonObject()).filter(x->x.get("id").getAsString().equals("tyr_progression:classic/aqua_affinity")).findFirst().orElseThrow();
        assertEquals(0,aqua.get("base_cost").getAsInt());
        var aquaEffect=aqua.getAsJsonArray("effects").get(0).getAsJsonObject();
        assertEquals("minecraft:player.submerged_mining_speed",aquaEffect.get("attribute").getAsString());
        assertEquals(0.8,aquaEffect.get("per_rank").getAsDouble());
        assertTrue(aquaEffect.get("empty_hand").getAsBoolean()&&aquaEffect.get("unarmored").getAsBoolean());
        var aquaTrial=path.milestones().stream().filter(m->m.id().equals("tyr_progression:classic/aqua_trial")).findFirst().orElseThrow();
        assertTrue(aquaTrial.strictOrder());assertEquals(java.util.List.of("tyr_progression:classic/deep_breath"),aquaTrial.after());
        assertEquals("submerged_barehand_harvest",aquaTrial.criteria().getFirst().event());assertEquals(5,aquaTrial.criteria().getFirst().count());
        assertTrue(conditions.asList().stream().anyMatch(x->x.getAsJsonObject().get("id").getAsString().equals(aquaTrial.id())));
        int rankSum=0;boolean dragon=false,wither=false;
        var skillIds=new java.util.HashSet<String>();
        for(var el:conditions){
            var condition=el.getAsJsonObject();
            String type=condition.get("type").getAsString(),id=condition.get("id").getAsString();
            assertTrue(Set.of("skill_rank","milestone","advancement").contains(type));
            if(!type.equals("advancement"))assertEquals(MONK,condition.get("path").getAsString());
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
    @Test void allClassicDeedsAndPurchasesReachMasteryWithVanillaEquivalentTools() throws Exception {
        Assumptions.assumeTrue(Files.isRegularFile(CANDIDATE));
        var raw=JsonParser.parseString(Files.readString(CANDIDATE)).getAsJsonObject();
        var survey=new java.util.HashMap<>(vanillaSurvey());
        survey.put("minecraft:golden_pickaxe",survey.get("minecraft:wooden_pickaxe"));
        survey.put("minecraft:netherite_pickaxe",survey.get("minecraft:diamond_pickaxe"));
        var expanded=HarvestGeneration.expand(MONK,raw,(i,b)->survey);
        var path=TestPaths.read(MONK,expanded.path());
        String finale=raw.getAsJsonObject("mastery_completion").get("milestone").getAsString();
        var gate=raw.getAsJsonObject("mastery_completion").getAsJsonObject("when").getAsJsonArray("conditions");
        var deeds=new java.util.HashSet<String>();
        var paid=new java.util.HashSet<String>();
        for(var element:gate){
            var c=element.getAsJsonObject();String kind=c.get("type").getAsString();
            if(kind.equals("milestone"))assertTrue(deeds.add(c.get("id").getAsString()));
            if(kind.equals("skill_rank"))assertTrue(paid.add(c.get("id").getAsString()));
        }
        var reached=new java.util.HashSet<String>();
        var track=io.github.heatwaveqc.tyr.progression.core.ProgressionMath.join(path,io.github.heatwaveqc.tyr.progression.state.Track.empty());
        boolean advanced;
        do{
            advanced=false;
            for(var m:path.milestones()){
                if(m.id().equals(finale)||reached.contains(m.id())||!reached.containsAll(m.after()))continue;
                for(var skill:path.skills())for(var effect:skill.effects())
                    if(m.id().equals(effect.params().get("trial_milestone"))){
                        assertEquals(0,track.skillRank(skill.id()),"Trial access must not confer ownership");
                        assertEquals(1,io.github.heatwaveqc.tyr.progression.core.ProgressionMath.effectRank(path,track,skill,effect),"No self-locked trial "+m.id());
                    }
                reached.add(m.id());reached.addAll(m.completionAliases());
                track=io.github.heatwaveqc.tyr.progression.core.ProgressionMath.completeMilestone(track,m);
                advanced=true;
            }
        }while(advanced);
        assertTrue(reached.containsAll(deeds),"Unreachable mastery receipt");
        for(var m:path.milestones())if(!m.id().equals(finale))assertTrue(reached.contains(m.id()),"Unreachable deed "+m.id());
        for(var skill:path.skills())if(skill.baseCost()>0){
            assertTrue(paid.contains(skill.id()));
            assertTrue(skill.choiceGroup().isEmpty(),"All mastery purchases must coexist");
        }
        for(var e:raw.getAsJsonArray("skills")){
            var node=e.getAsJsonObject();
            if(node.has("auto_achievements"))for(var receipt:node.getAsJsonArray("auto_achievements"))
                assertTrue(deeds.contains(receipt.getAsString()),"Free Classic unlock omitted from mastery");
        }
        assertEquals(28,paid.size());assertEquals(24,deeds.size());
        assertTrue(path.milestones().stream().filter(m->m.id().equals(finale)).allMatch(m->reached.containsAll(m.after())));
    }

}
