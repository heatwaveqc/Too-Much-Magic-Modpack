package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.Map;

/**
 * First-class, mutually exclusive player baseline Paths.
 *
 * Newbie and Normie have no private XP currency or purchased ranks.
 * Core is universal for graduates and is never a specialist Path.
 * This is a pure player-ledger reconciliation, not an enrollment shortcut:
 * only the trusted Newbie graduation/admin gateway chooses the stage.
 */
public final class BaselinePaths {
    public static final String NEWBIE = "tyr_progression:newbie";
    public static final String NORMIE = "tyr_progression:normie";
    public static final String CORE = "tyr_progression:core";

    private BaselinePaths() {}

    public static boolean isBaseline(String id) {
        return NEWBIE.equals(id) || NORMIE.equals(id);
    }
    public static boolean isCore(String id) {
        return CORE.equals(id);
    }
    public static boolean isSpecialist(String id) {
        return !isBaseline(id) && !isCore(id);
    }

    public static boolean hasSpecialist(Ledger ledger) {
        return ledger.paths().entrySet().stream().anyMatch(row ->
                isSpecialist(row.getKey()) && row.getValue().joined());
    }

    /**
     * Baseline projection changes no XP, Core purchase, milestone, achievement
     * or specialist investment. Normie is the graduated fallback only when no
     * specialist Path is active; it is never concurrently joined with one.
     * Reconciliation is idempotent and does not count as a punitive Path change.
     */
    public static Ledger reconcile(Ledger source, boolean newbie) {
        Ledger next = source;
        boolean normie = !newbie && !hasSpecialist(source);
        if (source.get(NEWBIE).joined() != newbie)
            next = next.with(NEWBIE, baseline(source.get(NEWBIE), newbie));
        if (source.get(NORMIE).joined() != normie)
            next = next.with(NORMIE, baseline(source.get(NORMIE), normie));
        return next;
    }

    private static Track baseline(Track previous, boolean joined) {
        return new Track(joined, 0L, joined ? 1 : 0, 0, Map.of(),
                previous.milestones(), previous.counters());
    }
}
