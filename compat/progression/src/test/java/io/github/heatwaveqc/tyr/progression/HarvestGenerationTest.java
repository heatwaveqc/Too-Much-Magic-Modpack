package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.HarvestGeneration;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HarvestGenerationTest {
    private static final String PATH="test:path", FAMILY="test:mining";
    private static final String JSON="""
        {"schema":1,"rank_xp":[0],"milestones":[{"id":"test:wood_speed",
         "criteria":[{"event":"barehand_block_break","target":"#minecraft:logs","count":5}]}],
         "harvest_generation":[{"id":"test:mining","item_selector":"#test:pickaxes",
         "block_selector":"#minecraft:mineable/pickaxe","after":["test:wood_speed"],
         "prefer_tools":["test:wood"],"count":5,"training_blocks":{"test:wood":["test:stone"]}}]}
        """;
    private static Map<String,Set<String>> survey(){
        return Map.of("test:wood",Set.of("test:stone"),"test:gold",Set.of("test:stone"),
          "test:iron",Set.of("test:stone","test:iron_ore"),
          "test:crystal",Set.of("test:stone","test:crystal_ore"),
          "test:union",Set.of("test:stone","test:iron_ore","test:crystal_ore"),
          "test:final",Set.of("test:stone","test:iron_ore","test:crystal_ore","test:obsidian"));
    }
    private static HarvestGeneration.Result expand(String json,Map<String,Set<String>> survey){
        return HarvestGeneration.expand(PATH,JsonParser.parseString(json).getAsJsonObject(),(i,b)->survey);
    }
    @Test void generatesOnlyNovelCapabilitiesAndRewiresRedundantUnion(){
        var result=expand(JSON,survey());
        assertEquals(Set.of("test:wood","test:iron","test:crystal","test:final"),result.trials().keySet());
        assertEquals(Set.of("test:stone"),result.trials().get("test:wood"));
        assertEquals(Set.of("test:obsidian"),result.trials().get("test:final"));
        var path=TestPaths.read(PATH,result.path());
        assertEquals(4,path.skills().size());assertEquals(5,path.milestones().size());
        var wood=path.milestones().stream().filter(m->m.id().equals(HarvestGeneration.receipt(PATH,FAMILY,"test:wood"))).findFirst().orElseThrow();
        assertEquals(List.of(HarvestGeneration.receipt(PATH,FAMILY,"test:gold")),wood.completionAliases());
        assertEquals(List.of("test:wood_speed"),wood.after());
        var last=path.milestones().stream().filter(m->m.id().equals(HarvestGeneration.receipt(PATH,FAMILY,"test:final"))).findFirst().orElseThrow();
        assertEquals(Set.of("test:wood_speed",HarvestGeneration.receipt(PATH,FAMILY,"test:iron"),
            HarvestGeneration.receipt(PATH,FAMILY,"test:crystal")),Set.copyOf(last.after()));
        assertTrue(path.skills().stream().allMatch(s->s.maxRank()==1&&s.effects().getFirst().type().equals("native_harvest")));
        assertFalse(result.path().has("harvest_generation"));
        assertEquals(result.path(),expand(JSON,survey()).path());
    }
    @Test void rejectsInvalidOrUnmatchedTrainingOverrides(){
        assertThrows(IllegalArgumentException.class,()->expand(JSON.replace("test:stone\"]","test:obsidian\"]"),survey()));
        assertThrows(IllegalArgumentException.class,()->expand(JSON.replace("\"test:wood\":[\"test:stone\"]","\"test:missing\":[\"test:stone\"]"),survey()));
    }
    @Test void generatedObjectivesUseTheRealTrainingDeltaAndOptInLane(){
        var raw=JsonParser.parseString(JSON).getAsJsonObject();
        raw.add("challenge_hud",JsonParser.parseString("[{\"id\":\"harvest\",\"title\":\"Harvesting\"}]"));
        raw.getAsJsonArray("harvest_generation").get(0).getAsJsonObject().addProperty("hud_group","harvest");
        var result=HarvestGeneration.expand(PATH,raw,(i,b)->survey());
        var path=TestPaths.read(PATH,result.path());
        var lanes=io.github.heatwaveqc.tyr.progression.data.ChallengeHudSpec.parse(result.path(),path);
        var track=io.github.heatwaveqc.tyr.progression.state.Track.empty().joinedAtRankOne(0);
        assertTrue(io.github.heatwaveqc.tyr.progression.data.ChallengeHudSpec.select(path,track,lanes).isEmpty());
        track=track.complete("test:wood_speed");
        var rows=io.github.heatwaveqc.tyr.progression.data.ChallengeHudSpec.select(path,track,lanes);
        assertEquals(1,rows.size());
        assertEquals(5,rows.getFirst().total());
        assertTrue(rows.getFirst().objective().text().contains("test stone"));
        track=track.complete(HarvestGeneration.receipt(PATH,FAMILY,"test:gold"));
        var next=io.github.heatwaveqc.tyr.progression.data.ChallengeHudSpec.select(path,track,lanes);
        assertFalse(next.getFirst().objective().text().contains("test wood:"));
    }
    @Test void stableIdentityDoesNotDependOnRegistryOrdering(){
        var a=HarvestGeneration.receipt(PATH,FAMILY,"test:wood");
        assertEquals(a,HarvestGeneration.receipt(PATH,FAMILY,"test:wood"));
        assertNotEquals(a,HarvestGeneration.receipt("test:other",FAMILY,"test:wood"));
        assertNotEquals(a,HarvestGeneration.receipt(PATH,FAMILY,"test:gold"));
        var shuffled=new LinkedHashMap<String,Set<String>>();
        survey().entrySet().stream().sorted(Map.Entry.<String,Set<String>>comparingByKey().reversed())
            .forEach(e->shuffled.put(e.getKey(),e.getValue()));
        assertEquals(expand(JSON,survey()).path(),expand(JSON,shuffled).path());
    }
}
