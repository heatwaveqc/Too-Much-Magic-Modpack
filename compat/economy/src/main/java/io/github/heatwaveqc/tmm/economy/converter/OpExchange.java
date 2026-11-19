package io.github.heatwaveqc.tmm.economy.converter;

import java.math.BigInteger;
import java.util.Objects;

/** Exact OP/EMC exchange plans. The caller must commit matched debits and
 * credits against real storage on the server thread; a plan is not a transfer.
 * All limits are available quantities, not total capacities. */
public final class OpExchange {
    private OpExchange() {}

    public static final int OP_PER_EMC_VALUE = 200;
    public static final int OP_PER_EMC_OUT = 160;
    public static final int OP_PER_EMC_IN = 250;

    public record Plan(BigInteger emc, long op) {}

    /** Bound forward transfer by source EMC, destination OP space and tier EMC budget. */
    public static Plan forward(BigInteger storedEmc, long freeOp, BigInteger emcBudget) {
        return plan(storedEmc, freeOp, emcBudget, OP_PER_EMC_OUT);
    }

    /** Bound reverse transfer by source OP, destination EMC space and tier EMC budget. */
    public static Plan reverse(long storedOp, BigInteger freeEmc, BigInteger emcBudget) {
        return plan(freeEmc, storedOp, emcBudget, OP_PER_EMC_IN);
    }

    private static Plan plan(BigInteger emcLimit, long opLimit, BigInteger budget, int opPerEmc) {
        Objects.requireNonNull(emcLimit);
        Objects.requireNonNull(budget);
        if (emcLimit.signum() < 0 || budget.signum() < 0 || opLimit < 0)
            throw new IllegalArgumentException("negative exchange limit");
        BigInteger emc = emcLimit.min(budget).min(BigInteger.valueOf(opLimit / opPerEmc));
        long op = emc.multiply(BigInteger.valueOf(opPerEmc)).longValueExact();
        return new Plan(emc, op);
    }
}
