package io.github.heatwaveqc.tyr.progression.core;

import java.math.BigDecimal;
import java.util.HashMap;

/** Exact administrative edits, separate from gameplay earning and reward receipts. */
public final class AdminExperience {
    private AdminExperience() {}

    public enum Operation { ADD, SUBTRACT, SET, RESET }

    public static BigDecimal balance(ExperienceEconomy.Ledger ledger, ExperienceEconomy.SystemSpec system) {
        return ledger.currentXp().getOrDefault(system.id(), BigDecimal.ZERO);
    }

    /** Reject rather than silently clamp or invalidate purchases. Reset preserves all receipts. */
    public static ExperienceEconomy.Ledger edit(ExperienceEconomy.Ledger before,
            ExperienceEconomy.SystemSpec system, Operation operation, long amount) {
        if (amount < 0 || (operation == Operation.RESET && amount != 0))
            throw new IllegalArgumentException("Invalid administrative XP amount");
        var old = balance(before, system);
        var delta = BigDecimal.valueOf(amount);
        var next = switch (operation) {
            case ADD -> old.add(delta);
            case SUBTRACT -> old.subtract(delta);
            case SET -> delta;
            case RESET -> BigDecimal.ZERO;
        };
        if (next.signum() < 0 || next.compareTo(system.maxXp()) > 0)
            throw new IllegalArgumentException("XP outside configured bounds");
        var floor = system.xpPerPoint().multiply(BigDecimal.valueOf(
                ExperienceEconomy.committed(before, system.id())));
        if (next.compareTo(floor) < 0)
            throw new IllegalArgumentException("XP would no longer back purchased ranks; reset those ranks first");
        var xp = new HashMap<>(before.currentXp());
        xp.put(system.id(), next);
        return new ExperienceEconomy.Ledger(xp, before.paidRanks(), before.achievements(),
                before.rewardReceipts(), before.recentTransactions());
    }
}
