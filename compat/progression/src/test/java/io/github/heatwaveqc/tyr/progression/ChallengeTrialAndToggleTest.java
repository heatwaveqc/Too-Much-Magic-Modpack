package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.data.ActivationSpec;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Track;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChallengeTrialAndToggleTest {
    private static final String JSON = """
        {"schema":1,"rank_xp":[0],"milestones":[
          {"id":"test:first","strict_order":true,"criteria":[{"event":"external","target":"test:begin"}]},
          {"id":"test:trial","strict_order":true,"after":["test:first"],"criteria":[{"event":"external","target":"test:finish"}]}],
          "skills":[{"id":"test:power","max_rank":1,"auto_achievements":["test:trial"],"effects":[
            {"type":"bedrock_rest","params":{"trial_milestone":"test:trial"}},
            {"type":"attribute","attribute":"minecraft:generic.max_health","per_rank":2}]}]}
        """;
    private static Definition.Path path(String json){
        return TestPaths.read("test:path",JsonParser.parseString(json).getAsJsonObject());
    }
    @Test void onlyTrialEffectWorksWithoutOwnershipAfterPrerequisite(){
        var path=path(JSON);var skill=path.skill("test:power");
        var track=Track.empty().joinedAtRankOne(0);
        assertEquals(0,ProgressionMath.effectRank(path,track,skill,skill.effects().get(0)));
        track=track.complete("test:first");
        assertEquals(1,ProgressionMath.effectRank(path,track,skill,skill.effects().get(0)));
        assertEquals(0,ProgressionMath.effectRank(path,track,skill,skill.effects().get(1)));
        assertEquals(0,track.skillRank(skill.id()));
        assertFalse(track.completed("test:trial"));
        assertEquals(0,track.points());
        assertEquals(0,ProgressionMath.effectRank(path,Track.empty(),skill,skill.effects().get(0)));
        var finished=track.complete("test:trial");
        assertEquals(0,ProgressionMath.effectRank(path,finished,skill,skill.effects().get(0)));
        var projected=finished.withSkill(skill.id(),1,0);
        assertEquals(1,ProgressionMath.effectRank(path,projected,skill,skill.effects().get(0)));
        assertEquals(1,ProgressionMath.effectRank(path,projected,skill,skill.effects().get(1)));
    }
    @Test void rejectsUnbackedOrNonStrictTrial(){
        assertThrows(IllegalArgumentException.class,()->path(JSON.replace("\"auto_achievements\":[\"test:trial\"],","")));
        assertThrows(IllegalArgumentException.class,()->path(JSON.replace("\"id\":\"test:trial\",\"strict_order\":true","\"id\":\"test:trial\",\"strict_order\":false")));
        assertThrows(IllegalArgumentException.class,()->path(JSON.replace("\"trial_milestone\":\"test:trial\"","\"trial_milestone\":\"test:first\"")));
    }
    @Test void exclusiveToggleSelectionSwitchesAndTurnsOff(){
        var spec=ActivationSpec.read(JsonParser.parseString("""
            {"activation":{"mode":"toggle","exclusive_group":"test:tools"}}
            """).getAsJsonObject());
        assertEquals("test:tools",spec.exclusiveGroup());
        assertEquals("shears",ActivationSpec.toggleSelection("off","shears"));
        assertEquals("axe",ActivationSpec.toggleSelection("shears","axe"));
        assertEquals("off",ActivationSpec.toggleSelection("axe","axe"));
        assertThrows(IllegalArgumentException.class,()->new ActivationSpec("timed",10,0,0,0,"test:tools"));
        assertThrows(IllegalArgumentException.class,()->new ActivationSpec("toggle",0,10,0,0,"test:tools"));
        assertEquals("",new ActivationSpec("toggle",0,20).exclusiveGroup());
    }
}
