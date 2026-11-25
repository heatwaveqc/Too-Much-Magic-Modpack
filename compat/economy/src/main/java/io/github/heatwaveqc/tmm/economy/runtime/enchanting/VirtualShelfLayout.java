package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import java.util.List;
import java.util.Objects;
import java.util.OptionalInt;

/** Pure placement policy: compact identical candles before opening a position. */
public final class VirtualShelfLayout {
    private VirtualShelfLayout() {}
    public static final int CAPACITY = VirtualCandlePacking.MAX_POSITIONS;

    /** Identity includes all components, not merely the candle color. */
    public record Occupied<T>(T identity, int count, boolean candle) {
        public Occupied {
            Objects.requireNonNull(identity);
            if (count < 1 || count > (candle ? VirtualCandlePacking.CANDLES_PER_POSITION : 1))
                throw new IllegalArgumentException("Invalid virtual position count");
        }
    }

    /** Null denotes empty; no mutation or allocation proportional to stack count. */
    public static <T> OptionalInt destination(List<Occupied<T>> positions, T identity, boolean candle) {
        Objects.requireNonNull(identity);
        if (positions.size() != CAPACITY) throw new IllegalArgumentException("Expected 32 positions");
        int empty = -1;
        for (int i = 0; i < CAPACITY; i++) {
            var existing = positions.get(i);
            if (existing == null) {
                if (empty < 0) empty = i;
            } else if (candle && existing.candle() && existing.identity().equals(identity)
                && existing.count() < VirtualCandlePacking.CANDLES_PER_POSITION) {
                return OptionalInt.of(i);
            }
        }
        return empty < 0 ? OptionalInt.empty() : OptionalInt.of(empty);
    }

    public static OptionalInt legacyBooks(float power) {
        // Native IS3 deposits/withdrawals change power by exactly one, capped at 15.
        if (!Float.isFinite(power) || power < 0 || power > 15 || power != (int) power)
            return OptionalInt.empty();
        return OptionalInt.of((int) power);
    }
}
