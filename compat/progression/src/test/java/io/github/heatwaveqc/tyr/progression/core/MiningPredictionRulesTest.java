package io.github.heatwaveqc.tyr.progression.core;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import io.github.heatwaveqc.tyr.progression.core.MiningPredictionRules.Rule;

class MiningPredictionRulesTest {
    @Test void rankScaledPredictionMatchesServerAdditiveFormula(){
        String family="#tyr_progression:classic_mineable";
        for(int rank=0;rank<=9;rank++){
            var rules=List.of(new Rule("speed",family,"",1,true,true),
                new Rule("speed",family,"",5.222222222222222*rank,true,true));
            var roundtrip=MiningPredictionRules.decode(MiningPredictionRules.encode(rules));
            assertEquals(2+5.222222222222222*rank,MiningPredictionRules.scale(roundtrip,family::equals,true,true),1e-9);
            assertEquals(1,MiningPredictionRules.scale(roundtrip,s->false,true,true));
            assertEquals(1,MiningPredictionRules.scale(roundtrip,family::equals,false,true));
            assertEquals(1,MiningPredictionRules.scale(roundtrip,family::equals,true,false));
        }
    }
    @Test void permissionsAreSeparateAndMalformedPacketsFailClosed(){
        var rule=new Rule("native","#minecraft:mineable/pickaxe","minecraft:wooden_pickaxe",0,true,true);
        assertEquals(List.of(rule),MiningPredictionRules.decode(MiningPredictionRules.encode(List.of(rule))));
        assertEquals(1,MiningPredictionRules.scale(List.of(rule),s->true,true,true));
        assertThrows(IllegalArgumentException.class,()->MiningPredictionRules.decode("speed\t*\t\tNaN\t1\t1"));
        assertThrows(IllegalArgumentException.class,()->MiningPredictionRules.decode("speed\t*\t\t1\t2\t1"));
        assertThrows(IllegalArgumentException.class,()->MiningPredictionRules.encode(java.util.Collections.nCopies(129,rule)));
        assertTrue(MiningPredictionRules.decode("").isEmpty());
    }
}
