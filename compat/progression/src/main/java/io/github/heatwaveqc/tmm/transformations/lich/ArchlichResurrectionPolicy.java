package io.github.heatwaveqc.tmm.transformations.lich;

/**
 * Pure preflight for the target Archlich death priority.
 *
 * Only a genuine dual-lich player with a level-2+ Forbidden Magic soul
 * should run JAWR's existing native save before Goety's normal listener.
 * Never create or debit souls here; both providers retain their own mechanics.
 */
public final class ArchlichResurrectionPolicy {
    private ArchlichResurrectionPolicy() { }

    public static boolean prioritizePhylactery(boolean goetyLich,
                                                 boolean forbiddenMagicLich,
                                                 int forbiddenMagicLevel,
                                                 int storedSouls,
                                                 boolean alreadyCanceled) {
        return !alreadyCanceled
                && goetyLich
                && forbiddenMagicLich
                && forbiddenMagicLevel >= 2
                && storedSouls > 0;
    }
}
