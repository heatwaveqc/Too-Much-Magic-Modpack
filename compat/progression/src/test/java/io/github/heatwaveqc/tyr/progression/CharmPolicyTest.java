package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.runtime.charm.CharmPolicy;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class CharmPolicyTest {
    private static Definition.Effect effect(String target,Map<String,String> params){
        return new Definition.Effect("charm","","add_value",target,"","",0,0,false,false,params);
    }
    @Test void durationAndCooldownAreIndependent(){
        long expiry=CharmPolicy.until(100,CharmPolicy.DURATION);
        assertEquals(700,expiry);
        assertTrue(CharmPolicy.active(100,expiry));
        assertTrue(CharmPolicy.active(699,expiry));
        assertFalse(CharmPolicy.active(700,expiry));
        assertFalse(CharmPolicy.active(99,expiry));
        assertFalse(CharmPolicy.cooldownReady(139,140));
        assertTrue(CharmPolicy.cooldownReady(140,140));
        assertThrows(IllegalArgumentException.class,()->CharmPolicy.until(Long.MAX_VALUE,600));
    }
    @Test void defaultsAndExplicitCost(){
        var free=CharmPolicy.validate(effect("minecraft:pillager",Map.of()));
        assertEquals(40,free.cooldown());assertEquals(0,free.cost());assertFalse(free.sneaking());
        var paid=CharmPolicy.validate(effect("#example:humanoids",Map.of("resource","example:fury","resource_cost","25","requires_sneaking","true")));
        assertEquals(25,paid.cost());assertTrue(paid.sneaking());
        assertThrows(IllegalArgumentException.class,()->CharmPolicy.validate(effect("*",Map.of())));
        assertThrows(IllegalArgumentException.class,()->CharmPolicy.validate(effect("minecraft:pillager",Map.of("resource_cost","1"))));
        assertThrows(IllegalArgumentException.class,()->CharmPolicy.validate(effect("minecraft:pillager",Map.of("resource","example:mana"))));
        assertThrows(IllegalArgumentException.class,()->CharmPolicy.validate(effect("minecraft:pillager",Map.of("resource","example:mana","resource_cost","1.5"))));
        assertThrows(IllegalArgumentException.class,()->CharmPolicy.validate(effect("minecraft:pillager",Map.of("duration_ticks","1200"))));
    }
    @Test void boundedSupportAndCommittedDamage(){
        assertTrue(CharmPolicy.supported("minecraft:pillager"));
        assertTrue(CharmPolicy.supported("minecraft:vindicator"));
        assertFalse(CharmPolicy.supported("minecraft:evoker"));
        assertFalse(CharmPolicy.supported("minecraft:player"));
        assertFalse(CharmPolicy.supported("example:elemental"));
        assertTrue(CharmPolicy.breaksOnDamage(0.01f));
        assertFalse(CharmPolicy.breaksOnDamage(0));
        assertFalse(CharmPolicy.breaksOnDamage(Float.NaN));
    }
}
