package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.runtime.enchanting.EnchantPreviewStamp;
import io.github.heatwaveqc.tyr.progression.runtime.enchanting.PersonalEnchantFilter;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

final class PersonalEnchantFilterTest {
    @Test void lockedFilterCannotChangeRandomOrDirectedResults() {
        PersonalEnchantFilter filter = PersonalEnchantFilter.locked();
        assertTrue(filter.permits(PersonalEnchantFilter.Selection.RANDOM, "minecraft:fortune"));
        assertTrue(filter.permits(PersonalEnchantFilter.Selection.PLAYER_DIRECTED, "minecraft:fortune"));
        assertThrows(IllegalStateException.class,
            () -> filter.replace(List.of("minecraft:fortune")));
    }

    @Test void unlockedFilterAffectsOnlyRandomCandidateSelection() {
        PersonalEnchantFilter filter = PersonalEnchantFilter.locked().unlock()
            .replace(List.of("minecraft:fortune", "minecraft:smite"));
        assertFalse(filter.permits(PersonalEnchantFilter.Selection.RANDOM, "minecraft:fortune"));
        assertTrue(filter.permits(PersonalEnchantFilter.Selection.RANDOM, "minecraft:unbreaking"));
        assertTrue(filter.permits(PersonalEnchantFilter.Selection.PLAYER_DIRECTED, "minecraft:fortune"));
    }

    @Test void sixUniqueNamespacedEnchantmentsAreTheHardLimit() {
        assertDoesNotThrow(() -> new PersonalEnchantFilter(true, 4, List.of(
            "a:one", "a:two", "a:three", "a:four", "a:five", "a:six")));
        assertThrows(IllegalArgumentException.class, () -> new PersonalEnchantFilter(true, 4, List.of(
            "a:one", "a:two", "a:three", "a:four", "a:five", "a:six", "a:seven")));
        assertThrows(IllegalArgumentException.class,
            () -> new PersonalEnchantFilter(true, 4, List.of("minecraft:fortune", "minecraft:fortune")));
        assertThrows(IllegalArgumentException.class,
            () -> new PersonalEnchantFilter(true, 4, List.of("Not A Registry ID")));
    }

    @Test void committedChangesAdvanceRevisionAndNoOpsDoNot() {
        PersonalEnchantFilter locked = PersonalEnchantFilter.locked();
        PersonalEnchantFilter unlocked = locked.unlock();
        PersonalEnchantFilter one = unlocked.replace(List.of("minecraft:fortune"));
        assertEquals(1, unlocked.revision());
        assertEquals(2, one.revision());
        assertSame(one, one.replace(List.of("minecraft:fortune")));
        assertEquals(3, one.replace(List.of("minecraft:smite")).revision());
    }

    @Test void previewBecomesStaleOnFilterOrDatapackRevisionChange() {
        PersonalEnchantFilter filter = PersonalEnchantFilter.locked().unlock();
        EnchantPreviewStamp preview = EnchantPreviewStamp.capture(filter, 12);
        assertTrue(preview.current(filter, 12));
        assertFalse(preview.current(filter.replace(List.of("minecraft:fortune")), 12));
        assertFalse(preview.current(filter, 13));
    }
}
