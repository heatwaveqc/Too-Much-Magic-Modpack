package io.github.heatwaveqc.tmm.progression.runtime;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BountifulDecreeSealsTest {
    @Test void finiteExplicitRosterOnly() {
        assertEquals(Set.of("vampire","werewolf","lich_goety","lich_jawr",
            "archlich","abomination","hunter","monk"), BountifulDecreeSealPolicy.supportedIds());
        for (String excluded : Set.of("dragon_cave","dragon_forest","dragon_sea",
                "dracolich","gilded","hunting","tmm_vampire","unknown")) {
            assertFalse(BountifulDecreeSealPolicy.declared(excluded));
        }
        assertEquals(Set.of("JAWR_VAMPIRE", "JAWR_LYCANTHROPE", "GOETY_LICH", "FORBIDDEN_MAGIC_LICH"), BountifulDecreeSealPolicy.nativeStates("abomination"));
        assertEquals("tmm_progression:hunter", BountifulDecreeSealPolicy.classPath("hunter"));
    }
}
