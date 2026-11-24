package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Pure candidate-selection policy shared by Apothic, portable tables and
 * Eidolon. Deliberate enchantment application MUST NOT call this filter.
 */
public final class PersonalEnchantFilterPolicy {
    public static final int BOOK_SLOTS = 6;

    private PersonalEnchantFilterPolicy() {}

    /** Empty books and duplicate IDs have no additive effect. */
    public static <T> Set<T> excluded(boolean unlocked, List<T> bookEnchantments) {
        if (!unlocked || bookEnchantments == null) return Set.of();
        if (bookEnchantments.size() != BOOK_SLOTS)
            throw new IllegalArgumentException("Expected exactly six filter-book positions");
        LinkedHashSet<T> result = new LinkedHashSet<>();
        for (T id : bookEnchantments) {
            if (id != null) result.add(id);
        }
        return Set.copyOf(result);
    }

    /** Preserve upstream ordering, compatibility filtering and random seed. */
    public static <T> List<T> candidates(List<T> nativeCandidates, Set<T> excluded) {
        Objects.requireNonNull(nativeCandidates);
        Objects.requireNonNull(excluded);
        if (excluded.isEmpty()) return List.copyOf(nativeCandidates);
        List<T> result = new ArrayList<>(nativeCandidates.size());
        for (T candidate : nativeCandidates) {
            if (!excluded.contains(candidate)) result.add(candidate);
        }
        return List.copyOf(result);
    }

    /** All excluded -> empty result; caller must not charge XP or materials. */
    public static <T> boolean mayRoll(T enchantment, Set<T> excluded) {
        return !excluded.contains(enchantment);
    }
}
