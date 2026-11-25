package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.data.PathJson;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RankScheduleTest{
 @Test void cumulativeValuesAndExactPrices(){
    var path=PathJson.read("test:path",JsonParser.parseString("""
    {"schema":1,"rank_xp":[0],"skills":[{"id":"test:skill","max_rank":3,
      "rank_costs":[1,2,4],"effects":[{"type":"attribute","attribute":"minecraft:generic.max_health","rank_values":[2,5,9]}]}]}
    """).getAsJsonObject());
    var skill=path.skill("test:skill");
    assertEquals(1,skill.price(0));assertEquals(2,skill.price(1));assertEquals(4,skill.price(2));
    var fx=skill.effects().getFirst();assertEquals(5,fx.atRank(2).amountPerRank()*2);
    assertEquals(9,fx.atRank(3).amountPerRank()*3);
 }
 @Test void investedRankScheduleAndBoundary(){
    var path=PathJson.read("test:path",JsonParser.parseString("""
    {"schema":1,"rank_xp":[0],"skills":[{"id":"test:skill","max_rank":2,
      "rank_invested_requirements":[100,150],"rank_costs":[1,1]}]}
    """).getAsJsonObject());
    var skill=path.skill("test:skill");
    var state=new io.github.heatwaveqc.tyr.progression.state.Track(true,0,1,10,
        java.util.Map.of(),java.util.List.of(),java.util.Map.of());
    assertEquals(100,skill.nextInvestedRequirement(0));
    assertSame(state,io.github.heatwaveqc.tyr.progression.core.ProgressionMath.buy(path,state,skill.id(),1,99));
    var bought=io.github.heatwaveqc.tyr.progression.core.ProgressionMath.buy(path,state,skill.id(),1,100);
    assertEquals(1,bought.skillRank(skill.id()));
    assertEquals(9,bought.points());
    assertSame(bought,io.github.heatwaveqc.tyr.progression.core.ProgressionMath.buy(path,bought,skill.id(),1,149));
    assertEquals(2,io.github.heatwaveqc.tyr.progression.core.ProgressionMath.buy(path,bought,skill.id(),1,150).skillRank(skill.id()));
    assertSame(state,io.github.heatwaveqc.tyr.progression.core.ProgressionMath.buy(path,state,skill.id(),1));
 }
 @Test void rejectsConflictingPriceAndIncompleteValues(){
    for(String skill:new String[]{
        "{\"id\":\"test:skill\",\"max_rank\":2,\"base_cost\":1,\"rank_costs\":[1,2]}",
        "{\"id\":\"test:skill\",\"max_rank\":2,\"effects\":[{\"type\":\"magnet\",\"rank_values\":[2]}]}",
        "{\"id\":\"test:skill\",\"max_rank\":2,\"rank_invested_requirements\":[100]}",
        "{\"id\":\"test:skill\",\"max_rank\":2,\"rank_invested_requirements\":[150,100]}",
        "{\"id\":\"test:skill\",\"max_rank\":2,\"rank_invested_requirements\":[-1,100]}"})
        assertThrows(IllegalArgumentException.class,()->PathJson.read("test:path",JsonParser.parseString("{\"schema\":1,\"rank_xp\":[0],\"skills\":["+skill+"]}").getAsJsonObject()));
 }
}
