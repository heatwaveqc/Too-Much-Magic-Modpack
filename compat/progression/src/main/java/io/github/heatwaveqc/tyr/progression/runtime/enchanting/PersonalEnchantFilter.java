package io.github.heatwaveqc.tyr.progression.runtime.enchanting;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Server-owned view of the player's six-book personal random-enchantment filter.
 *
 * <p>The IDs passed here must be derived by the server from validated, escrowed
 * single-enchantment books. Client packets must never be allowed to manufacture
 * this snapshot. This class deliberately owns no inventory: the later menu/state
 * layer must preserve and refund the exact ItemStacks and then publish this small
 * immutable view to random-enchantment consumers.</p>
 */
public record PersonalEnchantFilter(boolean unlocked, long revision, List<String> excluded) {
    public static final int MAX_BOOKS = 6;
    private static final Pattern ID = Pattern.compile("[a-z0-9_.-]+:[a-z0-9/._-]+");

    public enum Selection {
        RANDOM,
        PLAYER_DIRECTED
    }

    public PersonalEnchantFilter {
        if (revision < 0) throw new IllegalArgumentException("revision must be non-negative");
        Objects.requireNonNull(excluded, "excluded");
        if (excluded.size() > MAX_BOOKS)
            throw new IllegalArgumentException("personal filter accepts at most six books");
        Set<String> unique = new LinkedHashSet<>();
        for (String enchantment : excluded) {
            if (enchantment == null || !ID.matcher(enchantment).matches())
                throw new IllegalArgumentException("invalid enchantment id: " + enchantment);
            if (!unique.add(enchantment))
                throw new IllegalArgumentException("duplicate personal filter enchantment: " + enchantment);
        }
        excluded = List.copyOf(unique);
    }

    public static PersonalEnchantFilter locked() {
        return new PersonalEnchantFilter(false, 0, List.of());
    }

    public PersonalEnchantFilter unlock() {
        return unlocked ? this : new PersonalEnchantFilter(true, nextRevision(), excluded);
    }

    /** Replaces the server-derived ID view after a committed escrow transaction. */
    public PersonalEnchantFilter replace(List<String> next) {
        if (!unlocked && next != null && !next.isEmpty())
            throw new IllegalStateException("locked personal filter cannot accept books");
        PersonalEnchantFilter candidate = new PersonalEnchantFilter(unlocked, revision, next);
        return excluded.equals(candidate.excluded)
            ? this
            : new PersonalEnchantFilter(unlocked, nextRevision(), candidate.excluded);
    }

    /** Only random candidate generation is filtered; deliberate application is never vetoed. */
    public boolean permits(Selection selection, String enchantmentId) {
        Objects.requireNonNull(selection, "selection");
        Objects.requireNonNull(enchantmentId, "enchantmentId");
        return selection == Selection.PLAYER_DIRECTED || !unlocked || !excluded.contains(enchantmentId);
    }

    private long nextRevision() {
        if (revision == Long.MAX_VALUE) throw new IllegalStateException("personal filter revision exhausted");
        return revision + 1;
    }
}
