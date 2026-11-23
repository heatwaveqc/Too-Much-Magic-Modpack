package io.github.heatwaveqc.tmm.economy.storage;

/**
 * Pure, destination-aware policy for portable inventory safety.
 *
 * This is not a prohibition on bags as a gameplay feature: only an
 * item-embedded destination and a known unsafe embedded source are denied.
 * A native refusal is always preserved, even for otherwise-safe sources.
 */
public final class StorageNestingPolicy {
    public enum Source {
        ORDINARY,
        EMBEDDED_RISK,
        EXTERNAL_REFERENCE
    }

    public enum Destination {
        ORDINARY,
        ITEM_EMBEDDED,
        EXTERNAL_REFERENCE
    }

    private StorageNestingPolicy() { }

    public static boolean rejects(Source source, Destination destination, boolean nativeRejects) {
        if (nativeRejects) return true;
        return destination == Destination.ITEM_EMBEDDED && source == Source.EMBEDDED_RISK;
    }
}
