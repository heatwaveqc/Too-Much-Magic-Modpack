package io.github.heatwaveqc.tmm.economy.converter;

import java.math.BigInteger;

/** Pure exact whole-batch arithmetic. No floating point or narrowing of a wallet. */
public record TransferBudget(int units, BigInteger cost, int unitsPerBatch, int emcPerBatch) {
    public static TransferBudget plan(int current, int capacity, BigInteger balance,
                                      int unitsPerBatch, int emcPerBatch) {
        if (unitsPerBatch <= 0 || emcPerBatch <= 0)
            throw new IllegalArgumentException("invalid exchange ratio");
        if (current < 0 || capacity < current || balance == null || balance.signum() <= 0)
            return new TransferBudget(0, BigInteger.ZERO, unitsPerBatch, emcPerBatch);
        int deficitBatches = (capacity - current) / unitsPerBatch;
        int batches = balance.divide(BigInteger.valueOf(emcPerBatch))
                .min(BigInteger.valueOf(deficitBatches)).intValueExact();
        return new TransferBudget(Math.multiplyExact(batches, unitsPerBatch),
                BigInteger.valueOf(batches).multiply(BigInteger.valueOf(emcPerBatch)),
                unitsPerBatch, emcPerBatch);
    }

    /** Refund only whole batches known not to have been accepted. Unknown or
     * inconsistent observations are never rewarded with an unearned refund. */
    public BigInteger refundForAccepted(long accepted) {
        if (accepted < 0 || accepted > units) return BigInteger.ZERO;
        long usedBatches = (accepted + unitsPerBatch - 1L) / unitsPerBatch;
        return cost.subtract(BigInteger.valueOf(usedBatches).multiply(BigInteger.valueOf(emcPerBatch)));
    }
}
