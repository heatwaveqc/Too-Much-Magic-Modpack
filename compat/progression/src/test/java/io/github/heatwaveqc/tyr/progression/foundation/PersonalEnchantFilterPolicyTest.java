package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class PersonalEnchantFilterPolicyTest {
    @Test void lockedAndEmptyFiltersAreNoOps() {
        List<String> books = Arrays.asList("minecraft:mending", null, null, null, null, null);
        assertTrue(PersonalEnchantFilterPolicy.excluded(false, books).isEmpty());
        assertTrue(PersonalEnchantFilterPolicy.excluded(true, Arrays.asList(null, null, null, null, null, null)).isEmpty());
        assertEquals(List.of("a", "b"),
            PersonalEnchantFilterPolicy.candidates(List.of("a", "b"), Set.of()));
    }

    @Test void onlyRandomCandidatesAreRemovedAndNativeOrderRemains() {
        List<String> books = Arrays.asList("minecraft:mending", "minecraft:unbreaking",
            "minecraft:mending", null, null, null);
        Set<String> excluded = PersonalEnchantFilterPolicy.excluded(true, books);
        assertEquals(Set.of("minecraft:mending", "minecraft:unbreaking"), excluded);
        List<String> original = List.of("minecraft:sharpness", "minecraft:mending",
            "minecraft:unbreaking", "minecraft:smite");
        assertEquals(List.of("minecraft:sharpness", "minecraft:smite"),
            PersonalEnchantFilterPolicy.candidates(original, excluded));
        // The source list used by native selection is untouched.
        assertEquals("minecraft:mending", original.get(1));
    }

    @Test void excludingEveryEligibleCandidateYieldsNoRoll() {
        Set<String> excluded = Set.of("a", "b");
        assertTrue(PersonalEnchantFilterPolicy.candidates(List.of("a", "b"), excluded).isEmpty());
        assertFalse(PersonalEnchantFilterPolicy.mayRoll("a", excluded));
        assertTrue(PersonalEnchantFilterPolicy.mayRoll("c", excluded));
    }

    @Test void requiresExactlySixBookPositions() {
        assertThrows(IllegalArgumentException.class, () ->
            PersonalEnchantFilterPolicy.excluded(true, List.of("a")));
        assertThrows(IllegalArgumentException.class, () ->
            PersonalEnchantFilterPolicy.excluded(true,
                List.of("a", "b", "c", "d", "e", "f", "g")));
    }
}
