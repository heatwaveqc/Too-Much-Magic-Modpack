package io.github.heatwaveqc.tmm.economy.compat;

import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class SingleItemInsertionTest {
    @Test
    void simulationAcceptsOneWithoutCommit() {
        assertEquals(1, SingleItemInsertion.accepted(0, 64, true, () -> true,
                () -> { fail("simulation must not mutate"); return false; }));
    }

    @Test
    void realInsertionCommitsExactlyOnceAndAcceptsAtMostOne() {
        AtomicInteger commits = new AtomicInteger();
        assertEquals(1, SingleItemInsertion.accepted(0, 64, false, () -> true,
                () -> commits.incrementAndGet() == 1));
        assertEquals(1, commits.get());
    }

    @Test
    void rejectedOrInvalidOfferNeverCommits() {
        for (boolean simulate : new boolean[]{false, true}) {
            assertEquals(0, SingleItemInsertion.accepted(0, 64, simulate, () -> false,
                    () -> { fail("native rejection must not mutate"); return false; }));
            assertEquals(0, SingleItemInsertion.accepted(-1, 1, simulate,
                    () -> { fail("invalid slot"); return false; }, () -> false));
            assertEquals(0, SingleItemInsertion.accepted(1, 1, simulate,
                    () -> { fail("invalid slot"); return false; }, () -> false));
            assertEquals(0, SingleItemInsertion.accepted(0, 0, simulate,
                    () -> { fail("empty offer"); return false; }, () -> false));
        }
    }

    @Test
    void failedNativeCommitConsumesNothing() {
        assertEquals(0, SingleItemInsertion.accepted(0, 1, false, () -> true, () -> false));
    }
}
