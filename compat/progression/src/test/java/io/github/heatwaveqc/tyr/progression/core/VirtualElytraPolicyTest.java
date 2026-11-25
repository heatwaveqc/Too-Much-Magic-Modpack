package io.github.heatwaveqc.tyr.progression.core;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VirtualElytraPolicyTest {
    private static Set<String> sources(boolean alive,boolean creative,boolean spectator,
            boolean newbie,boolean monkUnlocked,boolean monkModeOn) {
        return VirtualElytraPolicy.sources(alive,creative,spectator,newbie,monkUnlocked,monkModeOn);
    }

    @Test void newbieHasElytraWithoutMonkAndGraduationImmediatelyRevokes() {
        assertEquals(Set.of(VirtualElytraPolicy.NEWBIE),
                     sources(true,false,false,true,false,false));
        assertTrue(sources(true,false,false,false,false,false).isEmpty());
    }

    @Test void monkOnlyWhenSkillUnlockedAndActuallyToggledOn() {
        assertTrue(sources(true,false,false,false,true,false).isEmpty());
        assertTrue(sources(true,false,false,false,false,true).isEmpty());
        assertEquals(Set.of(VirtualElytraPolicy.MONK),
                     sources(true,false,false,false,true,true));
        assertTrue(sources(true,false,false,false,true,false).isEmpty());
        assertTrue(sources(true,false,false,false,false,true).isEmpty()); // respec
    }

    @Test void privilegedAndDeadPlayersNeverInheritPassivePermission() {
        for (boolean newbie : new boolean[] {true,false}) {
            assertTrue(sources(false,false,false,newbie,true,true).isEmpty());
            assertTrue(sources(true,true,false,newbie,true,true).isEmpty());
            assertTrue(sources(true,false,true,newbie,true,true).isEmpty());
        }
    }

    @Test void baselineNewbieOverridesSpecialistWhenOldSaveContainsBoth() {
        assertEquals(Set.of(VirtualElytraPolicy.NEWBIE),
                     sources(true,false,false,true,true,true));
    }
}
