package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.HeightFallTrialSpec;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HeightFallTrialSpecTest {
    private static final String JSON="""
      {"schema":1,"rank_xp":[0],"milestones":[
        {"id":"test:fall","strict_order":true,
         "criteria":[{"event":"height_fall_trial","target":"test:fall"}],
         "fall_trial":{"dimension":"minecraft:overworld","start_y":319,"end_y":-59,"distance":379,
           "advancement":"minecraft:adventure/fall_from_world_height","advancement_criterion":"fall_from_world_height"}}
      ]}
      """;
    private static HeightFallTrialSpec spec(String json){
        var raw=JsonParser.parseString(json).getAsJsonObject();
        return HeightFallTrialSpec.parse(raw,TestPaths.read("test:path",raw)).getFirst();
    }
    @Test void requiresExactHeightAndActualContinuousDistance(){
        var s=spec(JSON);
        assertTrue(s.qualifies("minecraft:overworld",320,-59,375,-4));
        assertTrue(s.qualifies("minecraft:overworld",319,-60,379,0));
        assertFalse(s.qualifies("minecraft:overworld",319,-59,378,-1));
        assertFalse(s.qualifies("minecraft:overworld",318,-61,379,0));
        assertFalse(s.qualifies("minecraft:overworld",320,-58,378,0));
        assertFalse(s.qualifies("minecraft:the_nether",320,-59,379,0));
        assertFalse(s.qualifies("minecraft:overworld",320,-59,12,-4));
        assertFalse(s.qualifies("minecraft:overworld",320,-59,379,1));
        assertFalse(s.qualifies("minecraft:overworld",Double.NaN,-59,379,0));
    }
    @Test void failsClosedOnInvalidMilestoneOrBounds(){
        assertThrows(IllegalArgumentException.class,()->spec(JSON.replace("\"strict_order\":true","\"strict_order\":false")));
        assertThrows(IllegalArgumentException.class,()->spec(JSON.replace("\"target\":\"test:fall\"","\"target\":\"test:other\"")));
        assertThrows(IllegalArgumentException.class,()->spec(JSON.replace("\"distance\":379","\"distance\":0")));
        assertThrows(IllegalArgumentException.class,()->spec(JSON.replace("\"advancement_criterion\":\"fall_from_world_height\"","\"advancement_criterion\":\"\"")));
    }
}
