package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.TrainingXpSpec;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.CombatProof;
import io.github.heatwaveqc.tyr.progression.state.PathDiscoveries;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Pure rate, source classification and persistent state tests (not game QA). */
class PhysicalTrainingXpTest {
    private static TrainingXpSpec policy(){
        var j=JsonParser.parseString("""
          {"training_mxp":{"system":"tyr_progression:mxp",
           "hardness":35,"sprint":1,"ordinary_kill_per_hp":100,
           "strict_fist_kill_per_hp":500,"strict_window_ticks":6000,
           "first_consumable":100,"meditative_food_fixed":5,
           "meditative_drink_fixed":2}}
          """).getAsJsonObject();
        return TrainingXpSpec.parse(j,Map.of("tyr_progression:mxp",XpChannel.mxp(1.02)));
    }
    @Test void exactFractionalMiningAndMaxHpAwardCoefficients(){
        var p=policy();
        assertEquals(new BigDecimal("52.5"),p.mining(1.5));
        assertEquals(new BigDecimal("70.0"),p.mining(2));
        assertEquals(new BigDecimal("1750.0"),p.mining(50));
        assertEquals(BigDecimal.ZERO,p.mining(-1));
        assertEquals(BigDecimal.ZERO,p.mining(0));
        assertEquals(new BigDecimal("2000.0"),p.kill(20,false));
        assertEquals(new BigDecimal("10000.0"),p.kill(20,true));
        assertEquals(new BigDecimal("150000.0"),p.kill(300,true));
        assertEquals(6000,p.strictWindow());
        assertEquals(new BigDecimal("100"),p.discovery());
        assertEquals(new BigDecimal("5"),p.meditativeFood());
        assertEquals(new BigDecimal("2"),p.meditativeDrink());
        assertFalse(p.discoveryReplay());
    }
    @Test void immutableStrictProofNeverRearmsAfterInvalidHit(){
        String monk=UUID.randomUUID().toString(),other=UUID.randomUUID().toString();
        var current=CombatProof.empty().hit(true,true,monk,100,300,6000);
        assertTrue(current.strict(monk,100,6000));
        assertTrue(current.hit(false,true,monk,6099,300,6000).strict(monk,6099,6000));
        assertFalse(current.strict(monk,6101,6000));
        var interrupted=current.hit(false,false,other,101,300,6000);
        assertFalse(interrupted.qualified());
        var healed=interrupted.hit(true,true,monk,102,300,6000);
        assertFalse(healed.qualified(),"Healing cannot restart invalidated challenge");
        assertFalse(healed.strict(monk,102,6000));
        assertFalse(healed.finish().strict(monk,103,6000));
        assertFalse(CombatProof.empty().hit(false,true,monk,100,20,6000).qualified());
    }
    @Test void PathLocalFirstTasteResetsWithoutMutatingOtherPaths(){
        var a=PathDiscoveries.empty().discover("tyr_progression:monk","minecraft:apple")
            .discover("tyr_progression:hunter","minecraft:apple");
        assertSame(a,a.discover("tyr_progression:monk","minecraft:apple"));
        var reset=a.clear("tyr_progression:monk");
        assertFalse(reset.contains("tyr_progression:monk","minecraft:apple"));
        assertTrue(reset.contains("tyr_progression:hunter","minecraft:apple"));
    }
    @Test void malformedTrainingConfigurationsFailClosed(){
        var sys=Map.of("tyr_progression:mxp",XpChannel.mxp(1.02));
        var j=JsonParser.parseString("""
          {"training_mxp":{"system":"tyr_progression:mxp","hardness":35,
          "ordinary_kill_per_hp":100,"strict_fist_kill_per_hp":500}}
          """).getAsJsonObject();
        assertNotNull(TrainingXpSpec.parse(j,sys));
        j.getAsJsonObject("training_mxp").addProperty("hardness",-1);
        assertThrows(IllegalArgumentException.class,()->TrainingXpSpec.parse(j,sys));
        j.getAsJsonObject("training_mxp").addProperty("hardness",35);
        j.getAsJsonObject("training_mxp").addProperty("strange",1);
        assertThrows(IllegalArgumentException.class,()->TrainingXpSpec.parse(j,sys));
        j.getAsJsonObject("training_mxp").remove("strange");
        j.getAsJsonObject("training_mxp").addProperty("drink_tag","tyr_progression:drinks");
        assertThrows(IllegalArgumentException.class,()->TrainingXpSpec.parse(j,sys),
            "Custom drink tag is retired in favor of #c:drinks and DRINK animation");
    }
}
