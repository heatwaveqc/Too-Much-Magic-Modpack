package io.github.heatwaveqc.tmm.progression.runtime;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BountifulDecreeSealsTest {
    @Test void finiteExplicitRosterOnly() {
        assertEquals(Set.of("vampire","werewolf","lich_goety","lich_jawr",
            "archlich","abomination","hunter","monk"), BountifulDecreeSeals.supportedIds());
        for (String excluded : Set.of("dragon_cave","dragon_forest","dragon_sea",
                "dracolich","gilded","hunting","tmm_vampire","unknown")) {
            assertFalse(BountifulDecreeSeals.declared(excluded));
        }
    }
}
