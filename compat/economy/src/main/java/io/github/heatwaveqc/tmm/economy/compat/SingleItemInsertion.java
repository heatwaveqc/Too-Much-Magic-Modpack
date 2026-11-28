package io.github.heatwaveqc.tmm.economy.compat;

import java.util.function.BooleanSupplier;

/** Return the accepted count; simulation must never invoke the commit operation. */
final class SingleItemInsertion {
    private SingleItemInsertion() {}

    static int accepted(int slot, int offered, boolean simulate,
                        BooleanSupplier allowed, BooleanSupplier commit) {
        if (slot != 0 || offered <= 0 || !allowed.getAsBoolean()) return 0;
        return simulate || commit.getAsBoolean() ? 1 : 0;
    }
}
