package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.runtime.RecentBlockCredits;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

final class TreeChopCreditDedupTest {
    @Test void normalDropsAndFellCallbacksCannotDoubleAwardOneLog() {
        UUID monk = UUID.randomUUID();
        assertTrue(RecentBlockCredits.first(monk, "minecraft:overworld", 101L, 500L));
        assertFalse(RecentBlockCredits.first(monk, "minecraft:overworld", 101L, 500L));
        assertTrue(RecentBlockCredits.first(monk, "minecraft:overworld", 102L, 500L),
                "Each genuinely removed log has its own identity");
        assertTrue(RecentBlockCredits.first(monk, "minecraft:the_nether", 101L, 500L));
        assertTrue(RecentBlockCredits.first(UUID.randomUUID(), "minecraft:overworld", 101L, 500L));
        assertTrue(RecentBlockCredits.first(monk, "minecraft:overworld", 101L, 501L),
                "A distinct committed later-tick break isn't suppressed");
        RecentBlockCredits.forget(monk);
        assertTrue(RecentBlockCredits.first(monk, "minecraft:overworld", 101L, 501L));
    }

    @Test void timesResetAndCacheDoesNotLoseIndividualPositions() {
        UUID monk = UUID.randomUUID();
        assertTrue(RecentBlockCredits.first(monk, "minecraft:overworld", 44L, 800L));
        assertTrue(RecentBlockCredits.first(monk, "minecraft:overworld", 44L, 4L));
        for (int i = 0; i < 120; i++) {
            assertTrue(RecentBlockCredits.first(monk, "minecraft:overworld",
                    1000 + i, 4L));
        }
        assertFalse(RecentBlockCredits.first(monk, "minecraft:overworld", 1000L, 4L));
        RecentBlockCredits.forget(monk);
    }
}
