package io.github.heatwaveqc.tyr.progression.runtime.blood;

/**
 * An additive, exact-volume bridge for Hexerei blood in an EvilCraft extractor.
 * The source has Hexerei fluid identity; only the accepted amount becomes
 * canonical EvilCraft blood in the destination. No conversion applies to
 * third-party fluids, buckets, bottles or unrelated item handlers.
 */
public final class BloodCauldronTransferPolicy {
    private BloodCauldronTransferPolicy() {}

    /**
     * The cauldron's native Blood Sigil and an extractor-deposited pool have
     * distinct fluid registry keys. Only these two ordinary identities may
     * return to an EvilCraft Blood Extractor through this bridge.
     */
    public static boolean supportedSource(String registryId) {
        return "hexerei:blood_fluid".equals(registryId) || "evilcraft:blood".equals(registryId);
    }

    /**
     * Return how much of the server-observed cauldron source may safely move.
     * Negative/bogus handler reports fail closed. A partial source or partially
     * filled extractor is accepted, rather than demanding one full bucket.
     */
    public static int transferable(int sourceAvailable, int destinationSimulatedAcceptance) {
        if (sourceAvailable <= 0 || destinationSimulatedAcceptance <= 0) return 0;
        return Math.min(sourceAvailable, destinationSimulatedAcceptance);
    }
}
