package io.github.heatwaveqc.tyr.progression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import io.github.heatwaveqc.tyr.progression.runtime.LootBonusMath;
import org.junit.jupiter.api.Test;

class LootBonusMathTest {
    @Test void monksOnlyGetFortuneOnRealEmptyHandInFortuneMode() {
        assertEquals(1, LootBonusMath.monkFortune(1, 0, true, true));
        assertEquals(4, LootBonusMath.monkFortune(1, 3, true, true));
        assertEquals(4, LootBonusMath.monkFortune(1, 100, true, true));
        assertEquals(0, LootBonusMath.monkFortune(0, 3, true, true));
        assertEquals(0, LootBonusMath.monkFortune(1, 3, false, true)); // Silk
        assertEquals(0, LootBonusMath.monkFortune(1, 3, true, false)); // tool
    }

    @Test void huntersOnlyGetBonusForJoinedSpecialtyAndValidKill() {
        assertEquals(1, LootBonusMath.favoredLooting(true, true, true));
        assertEquals(0, LootBonusMath.favoredLooting(false, true, true));
        assertEquals(0, LootBonusMath.favoredLooting(true, false, true));
        assertEquals(0, LootBonusMath.favoredLooting(true, true, false));
    }
}
