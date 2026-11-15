package io.github.heatwaveqc.tmm.mxp.core;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Per-player server-authoritative MXP record.
 * Mutable only on the server thread; MxpService stages copies transactionally.
 * Does NOT store spendable Tyz SP or duplicate Tyz's player XP wallet.
 */
public final class MxpAccount {
    public static final int RECENT_TRANSACTION_LIMIT = 1024;

    private MxpMath.Counter counter = new MxpMath.Counter(0, 0);
    private double pendingDelivery = 0;
    private final Set<String> rituals = new HashSet<>();
    private final Set<String> bosses = new HashSet<>();
    private final Set<String> crafts = new HashSet<>();
    private final LinkedHashSet<String> recentTransactions = new LinkedHashSet<>();

    public record Receipt(double effectiveXP, long lifetimeLevels, double progress,
                          boolean firstCompletion, boolean duplicate) {
        public static Receipt duplicate(MxpAccount account) {
            return new Receipt(0, account.counter.lifetimeLevels(),
                    account.counter.progress(), false, true);
        }
    }

    public MxpAccount copy() {
        MxpAccount other = new MxpAccount();
        other.counter = counter;
        other.pendingDelivery = pendingDelivery;
        other.rituals.addAll(rituals);
        other.bosses.addAll(bosses);
        other.crafts.addAll(crafts);
        other.recentTransactions.addAll(recentTransactions);
        return other;
    }

    public MxpMath.Counter counter() { return counter; }
    public double pendingDelivery() { return pendingDelivery; }
    public Set<String> firstRituals() { return Collections.unmodifiableSet(rituals); }
    public Set<String> firstBosses() { return Collections.unmodifiableSet(bosses); }
    public Set<String> firstCrafts() { return Collections.unmodifiableSet(crafts); }
    public Set<String> recentTransactions() {
        return Collections.unmodifiableSet(recentTransactions);
    }

    public void restore(MxpMath.Counter counter, double pending,
                        Iterable<String> rituals, Iterable<String> bosses,
                        Iterable<String> crafts, Iterable<String> transactions) {
        if (counter == null || !Double.isFinite(pending) || pending < 0 || pending >= 1.0e9)
            throw new IllegalArgumentException("Invalid persisted MXP record");
        this.counter = counter;
        this.pendingDelivery = pending;
        this.rituals.clear();
        this.bosses.clear();
        this.crafts.clear();
        this.recentTransactions.clear();
        rituals.forEach(this.rituals::add);
        bosses.forEach(this.bosses::add);
        crafts.forEach(this.crafts::add);
        transactions.forEach(this::remember);
    }

    public Receipt resource(String source, String transactionId,
                            double nominalCost, double firstAccessiblePool,
                            double actualDebit) {
        if (!Double.isFinite(actualDebit) || actualDebit <= 0)
            return new Receipt(0, counter.lifetimeLevels(), counter.progress(), false, false);
        return apply(source, transactionId,
                MxpMath.normalizedBase(nominalCost, firstAccessiblePool), false, false);
    }

    public Receipt ritual(String ritualId, String transactionId, double first, double repeat) {
        return discovery("ritual", ritualId, transactionId, first, repeat, false, rituals);
    }

    public Receipt boss(String bossId, String transactionId,
                        double first, double repeat, boolean fixedRepeat) {
        return discovery("boss", bossId, transactionId, first, repeat, fixedRepeat, bosses);
    }

    public Receipt craft(String craftId, String transactionId, double first, double repeat) {
        // Even FIRST-CRAFT rewards remain DIVIDED (unlike first ritual/boss).
        return discovery("craft", craftId, transactionId, first, repeat, false, crafts, true);
    }

    private Receipt discovery(String type, String id, String tx, double first,
                              double repeat, boolean fixedRepeat, Set<String> discovered) {
        return discovery(type, id, tx, first, repeat, fixedRepeat, discovered, false);
    }

    private Receipt discovery(String type, String id, String tx, double first,
                              double repeat, boolean fixedRepeat, Set<String> discovered,
                              boolean divideFirst) {
        validateIdentity(id);
        validateReward(first);
        validateReward(repeat);
        if (recentTransactions.contains(tx)) return Receipt.duplicate(this);
        boolean isFirst = !discovered.contains(id);
        double base = isFirst ? first : repeat;
        Receipt outcome = apply(type + ":" + id, tx, base,
                isFirst ? !divideFirst : fixedRepeat, isFirst);
        if (!outcome.duplicate && isFirst) discovered.add(id);
        return outcome;
    }

    private Receipt apply(String source, String tx, double base,
                          boolean fixed, boolean isFirst) {
        validateIdentity(source);
        validateIdentity(tx);
        if (recentTransactions.contains(tx)) return Receipt.duplicate(this);
        MxpMath.Award award = MxpMath.apply(counter, base, fixed);
        counter = award.state();
        pendingDelivery += award.effectiveXP();
        if (!Double.isFinite(pendingDelivery))
            throw new IllegalArgumentException("MXP pending float overflow");
        remember(tx);
        return new Receipt(award.effectiveXP(), counter.lifetimeLevels(),
                counter.progress(), isFirst, false);
    }

    public float deliverableNow() {
        // Do not lose sub-centi-MXP awards to Tyz's float-based XP store.
        // Safe upper batch keeps float precision near Tyz's <1000 current XP.
        double batch = Math.min(1_000_000, Math.floor((pendingDelivery + 1e-9) * 100) / 100);
        return batch >= 0.01 ? (float) batch : 0f;
    }

    public void confirmedDelivery(float delivered) {
        if (!Float.isFinite(delivered) || delivered <= 0 || delivered > pendingDelivery + 0.011)
            throw new IllegalArgumentException("Invalid confirmed MXP delivery");
        pendingDelivery = Math.max(0, pendingDelivery - delivered);
    }

    private void remember(String tx) {
        validateIdentity(tx);
        recentTransactions.remove(tx);
        recentTransactions.add(tx);
        while (recentTransactions.size() > RECENT_TRANSACTION_LIMIT) {
            String oldest = recentTransactions.iterator().next();
            recentTransactions.remove(oldest);
        }
    }

    private static void validateReward(double award) {
        if (!Double.isFinite(award) || award <= 0 || award > MxpMath.MAX_EVENT_BASE)
            throw new IllegalArgumentException("MXP event reward must be positive and finite");
    }

    private static void validateIdentity(String key) {
        if (key == null || key.isBlank() || key.length() > 240
                || !key.matches("[a-zA-Z0-9_.:/#@-]+"))
            throw new IllegalArgumentException("Invalid MXP source or transaction identity");
    }
}
