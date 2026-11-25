package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.state.HunterMutationState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminMutationTest{
    @Test void ranksNormalizeProgressAndPreserveOtherEffects(){
        var old=HunterMutationState.empty().credit("minecraft:speed",2,100,100)
            .credit("minecraft:haste",0,100,100);
        var lowered=old.adminRank("minecraft:speed",1,3,100);
        assertEquals(0,lowered.unlocked().get("minecraft:speed"));
        assertEquals(100,lowered.progress("minecraft:speed",0));
        assertEquals(0,lowered.progress("minecraft:speed",2));
        assertEquals(0,lowered.unlocked().get("minecraft:haste"));
        var removed=lowered.adminRank("minecraft:speed",0,3,100);
        assertFalse(removed.unlocked().containsKey("minecraft:speed"));
        assertEquals(100,removed.progress("minecraft:haste",0));
        assertThrows(IllegalArgumentException.class,()->old.adminRank("minecraft:speed",5,3,100));
    }
}
