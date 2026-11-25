package io.github.heatwaveqc.tmm.economy;

import java.util.function.BooleanSupplier;

/** ProjectE mapping only: an exhausted Roots totem has no return value. */
public final class GoetyRemainderPolicy {
    private GoetyRemainderPolicy() {}

    public static boolean hasActualRemainder(boolean advertised, boolean rootsTotem,
                                             BooleanSupplier remainderIsEmpty) {
        return advertised && (!rootsTotem || !remainderIsEmpty.getAsBoolean());
    }
}
