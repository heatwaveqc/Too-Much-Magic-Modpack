package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.data.ChallengeHudSpec;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChallengeHudSpecTest {
    private static final String JSON="""
      {"schema":1,"rank_xp":[0],
       "challenge_hud":[{"id":"journey","title":"Journey","title_key":"test.hud.journey"},{"id":"tools","title":"Tools"}],
       "milestones":[
        {"id":"test:first","strict_order":true,"criteria":[{"event":"external","target":"test:one","count":2}],
         "hud":{"group":"journey","objectives":[{"text":"First deed","key":"test.hud.first"}]}},
        {"id":"test:second","strict_order":true,"after":["test:first"],"criteria":[{"event":"external","target":"test:two"}],
         "hud":{"group":"journey","objectives":[{"text":"Second deed"}]}},
        {"id":"test:tool","criteria":[{"event":"barehand_block_break","target":"minecraft:dirt","count":20}],
         "hud":{"group":"tools","objectives":[{"text":"Break dirt barehanded"}]}},
        {"id":"test:hidden","criteria":[{"event":"external","target":"test:hidden"}]}
       ]}
      """;
    @Test void currentObjectiveAndParallelLaneAdvanceAndDisappear(){
        var raw=JsonParser.parseString(JSON).getAsJsonObject();
        var path=TestPaths.read("test:path",raw);
        var lanes=ChallengeHudSpec.parse(raw,path);
        var track=Track.empty().joinedAtRankOne(0);
        assertTrue(ChallengeHudSpec.select(path,Track.empty(),lanes).isEmpty());
        var rows=ChallengeHudSpec.select(path,track,lanes);
        assertEquals(2,rows.size());assertEquals("First deed",rows.getFirst().objective().text());
        assertEquals("test.hud.first",rows.getFirst().objective().key());
        assertEquals(20,rows.getLast().total());
        track=track.withCounter(ProgressionMath.counterKey("test:first",0),1);
        assertEquals(1,ChallengeHudSpec.select(path,track,lanes).getFirst().current());
        track=track.complete("test:first");
        assertEquals("Second deed",ChallengeHudSpec.select(path,track,lanes).getFirst().objective().text());
        track=track.complete("test:second");
        assertEquals("Tools",ChallengeHudSpec.select(path,track,lanes).getFirst().title().text());
        assertTrue(ChallengeHudSpec.select(path,track.complete("test:tool"),lanes).isEmpty());
    }
    @Test void badGroupOrObjectiveCountsFailClosed(){
        for(String bad:List.of(JSON.replace("\"group\":\"journey\"","\"group\":\"unknown\""),
            JSON.replace("[{\"text\":\"Second deed\"}]","[]"))){
            var raw=JsonParser.parseString(bad).getAsJsonObject();
            var path=TestPaths.read("test:path",raw);
            assertThrows(IllegalArgumentException.class,()->ChallengeHudSpec.parse(raw,path));
        }
    }
    @Test void noHudConfigurationRemainsDormant(){
        var raw=JsonParser.parseString("{\"schema\":1,\"rank_xp\":[0]}").getAsJsonObject();
        assertTrue(ChallengeHudSpec.parse(raw,TestPaths.read("test:path",raw)).isEmpty());
    }
}
