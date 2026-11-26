package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.core.Signal;
import io.github.heatwaveqc.tyr.progression.state.Track;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class SubmergedHarvestTrialTest {
    private static final String JSON="""
       {"schema":1,"rank_xp":[0,10],"milestones":[
       {"id":"test:breath","criteria":[{"event":"external","target":"test:breath","count":1}]},
       {"id":"test:aqua","after":["test:breath"],"strict_order":true,
        "criteria":[{"event":"submerged_barehand_harvest","target":"#test:blocks","count":5}]}],
       "skills":[{"id":"test:affinity","base_cost":0,"auto_achievements":["test:aqua"],
       "effects":[{"type":"attribute","attribute":"minecraft:player.submerged_mining_speed","operation":"add_value","per_rank":0.8,"empty_hand":true,"unarmored":true}]}]}
       """;
    @Test void strictSubmergedHarvestIsScopedAndCannotPrecount(){
        var path=TestPaths.read("test:path",JsonParser.parseString(JSON).getAsJsonObject());
        var initial=ProgressionMath.join(path,Track.empty());
        var valid=new Signal("submerged_barehand_harvest","minecraft:dirt",Set.of("test:blocks"));
        assertEquals(initial,ProgressionMath.collectMilestoneCriteria(path,initial,valid));
        var ready=initial.complete("test:breath");
        assertEquals(ready,ProgressionMath.collectMilestoneCriteria(path,ready,new Signal("barehand_block_break","minecraft:dirt",Set.of("test:blocks"))));
        assertEquals(ready,ProgressionMath.collectMilestoneCriteria(path,ready,new Signal("submerged_barehand_harvest","minecraft:stone",Set.of())));
        var progress=ready;
        for(int i=0;i<5;i++)progress=ProgressionMath.collectMilestoneCriteria(path,progress,valid);
        assertEquals(5,progress.criterionCount(ProgressionMath.counterKey("test:aqua",0)));
        assertEquals(progress,ProgressionMath.collectMilestoneCriteria(path,progress,valid));
    }
    @Test void nonStrictTrialRejected(){
        assertThrows(IllegalArgumentException.class,()->TestPaths.read("test:path",JsonParser.parseString(JSON.replace("\"strict_order\":true","\"strict_order\":false")).getAsJsonObject()));
    }
}
