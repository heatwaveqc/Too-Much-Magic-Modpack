package io.github.heatwaveqc.tyr.progression.runtime.blood;

/**
 * Exact ordinary-fluid identities authorized for import into a Hexerei Mixing
 * Cauldron. This is intentionally NOT a broad #c:blood tag lookup: Neo Vitae
 * Essentia Vitae and JAWR's component-backed special bottles are separate
 * resources. Hexerei's native blood is already valid and needs no conversion.
 */
public final class HexereiBloodInputPolicy {
    private HexereiBloodInputPolicy() {}

    public static boolean needsInternalConversion(String fluidId) {
        return "evilcraft:blood".equals(fluidId)
                || "irons_spellbooks:blood".equals(fluidId);
    }

    /** Exact amount that may be moved without exceeding either tank or source. */
    public static int availableTransfer(int sourceAmount, int currentAmount, int tankCapacity) {
        if (sourceAmount <= 0 || currentAmount < 0 || tankCapacity <= 0
                || currentAmount > tankCapacity) return 0;
        return Math.min(sourceAmount, tankCapacity - currentAmount);
    }

    /**
     * The cauldron is allowed to accept a newly imported ordinary blood stack
     * only when empty or already full of the native Hexerei blood identity.
     * Other Hexerei potion/water/particular-fluids must never be replaced.
     */
    public static boolean mayMix(String existingFluidId, int existingAmount) {
        return existingAmount == 0 || "hexerei:blood_fluid".equals(existingFluidId);
    }
}
