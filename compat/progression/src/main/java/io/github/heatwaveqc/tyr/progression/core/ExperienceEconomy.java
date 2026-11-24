package io.github.heatwaveqc.tyr.progression.core;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Provider-independent, immutable foundation for configuration-defined XP ledgers.
 *
 * Current XP is authoritative. Levels and available points are derived, never
 * mutable state. The runtime adapter owns server-side award verification, effect
 * eligibility, Path membership, storage codecs and client synchronization.
 *
 * This class deliberately has no notion of a special "magic" currency.
 */
public final class ExperienceEconomy {
    private ExperienceEconomy() {}

    private static final MathContext PRECISION = MathContext.DECIMAL128;
    public static final double MIN_GROWTH = 0.001;
    public static final double MAX_GROWTH = 100.0;
    /** Hard computational guards, not balance presets or growth-factor clamps. */
    public static final long HARD_MAX_LEVEL = 10_000_000L;
    public static final int MAX_LEVEL_CROSSINGS_PER_AWARD = 10_000;
    public static final int MAX_PAID_RANK_RECEIPTS = 1_000_000;
    public static final int MAX_RECENT_TRANSACTIONS = 1024;
    public static final int MAX_FIRST_COMPLETIONS = 65536;
    /** Storage/work limits, not a gameplay growth-factor clamp. */
    public static final int MAX_XP_SCALE = 128;
    public static final int MAX_XP_INTEGER_DIGITS = 128;
    private static final BigDecimal MIN_XP_UNIT = BigDecimal.ONE.scaleByPowerOfTen(-MAX_XP_SCALE);
    private static final String DEFAULT_SOURCE = "tyr_progression:default";

    public record SystemSpec(String id, BigDecimal xpPerPoint, long maxLevel, BigDecimal maxBaseAward) {
        public SystemSpec {
            requireId(id);
            Objects.requireNonNull(xpPerPoint, "xpPerPoint");
            Objects.requireNonNull(maxBaseAward, "maxBaseAward");
            requireBoundedDecimal(xpPerPoint);
            requireBoundedDecimal(maxBaseAward);
            if (xpPerPoint.signum() <= 0 || maxBaseAward.signum() <= 0
                    || maxLevel < 1 || maxLevel > HARD_MAX_LEVEL)
                throw new IllegalArgumentException("Invalid XP system limits");
        }

        /** Initial TMM values; other systems may use independent thresholds. */
        public static SystemSpec mxp() {
            return new SystemSpec("tyr_progression:mxp", new BigDecimal("1000"),
                    HARD_MAX_LEVEL, new BigDecimal("1e12"));
        }
    }

    /** Each receipt is ONE purchased rank, charged to exactly one XP system. */
    public record PaidRank(String path, String skill, String system, int pointCost) {
        public PaidRank {
            requireId(path);
            requireId(skill);
            requireId(system);
            if (pointCost < 1) throw new IllegalArgumentException("Paid rank must cost points");
        }
    }

    public enum DeparturePolicy { RETAIN, REFUND, FORFEIT }
    public record PathSystem(String path, String system) {
        public PathSystem { requireId(path); requireId(system); }
    }

    /**
     * The accomplishment ledgers are independent of Path membership and skill
     * purchases. First-time reward receipts must survive deliberate XP resets.
     */
    public record Ledger(Map<String, BigDecimal> currentXp, List<PaidRank> paidRanks,
                         Set<String> achievements, Set<String> rewardReceipts,
                         Set<String> recentTransactions) {
        public Ledger {
            currentXp = Map.copyOf(currentXp);
            paidRanks = List.copyOf(paidRanks);
            achievements = Set.copyOf(achievements);
            rewardReceipts = Set.copyOf(rewardReceipts);
            // Preserve insertion order for deterministic oldest-transaction eviction.
            recentTransactions = Collections.unmodifiableSet(new LinkedHashSet<>(recentTransactions));
            if (recentTransactions.size() > MAX_RECENT_TRANSACTIONS
                    || rewardReceipts.size() > MAX_FIRST_COMPLETIONS)
                throw new IllegalArgumentException("Too many XP reward receipts");
            currentXp.forEach((id, xp) -> {
                requireId(id);
                if (xp == null || xp.signum() < 0) throw new IllegalArgumentException("Negative or missing XP");
                requireBoundedDecimal(xp);
            });
            if (paidRanks.size() > MAX_PAID_RANK_RECEIPTS)
                throw new IllegalArgumentException("Too many skill purchase receipts");
        }

        public static Ledger empty() {
            return new Ledger(Map.of(), List.of(), Set.of(), Set.of(), Set.of());
        }

        public Ledger withAchievement(String achievement) {
            requireId(achievement);
            if (achievements.contains(achievement)) return this;
            var next = new HashSet<>(achievements);
            next.add(achievement);
            return new Ledger(currentXp, paidRanks, next, rewardReceipts, recentTransactions);
        }
    }

    public record Award(Ledger ledger, BigDecimal effectiveXp, boolean accepted) {}

    public static Ledger initialize(Ledger state, SystemSpec system) {
        if (state.currentXp().containsKey(system.id())) return state;
        var xp = new HashMap<>(state.currentXp());
        xp.put(system.id(), BigDecimal.ZERO);
        return new Ledger(xp, state.paidRanks(), state.achievements(),
                state.rewardReceipts(), state.recentTransactions());
    }

    /** Integer display only; neither the level nor point balance is saved. */
    public static long level(Ledger state, SystemSpec system) {
        var xp = state.currentXp().getOrDefault(system.id(), BigDecimal.ZERO);
        long result = xp.divide(system.xpPerPoint(), 0, RoundingMode.DOWN).longValueExact();
        if (result > system.maxLevel())
            throw new IllegalArgumentException("Derived XP level exceeds configured safety limit");
        return result;
    }

    public static long committed(Ledger state, String systemId) {
        return state.paidRanks().stream().filter(r -> r.system().equals(systemId))
                .mapToLong(PaidRank::pointCost).sum();
    }

    public static long available(Ledger state, SystemSpec system) {
        long free = level(state, system) - committed(state, system.id());
        if (free < 0) throw new IllegalStateException("Spent more points than current XP permits");
        return free;
    }

    public static int contributedRanks(Ledger state, String path, String skill, String system) {
        return (int) state.paidRanks().stream().filter(r -> r.path().equals(path)
                && r.skill().equals(skill) && r.system().equals(system)).count();
    }

    public static int effectiveRank(Ledger state, String path, String skill) {
        return (int) state.paidRanks().stream().filter(r -> r.path().equals(path)
                && r.skill().equals(skill)).count();
    }

    /**
     * Purchase one rank from a chosen currency. The caller must independently
     * validate active Path, prerequisites and eligible player on the server.
     * The overall cap prevents accepting purchases which add no actual rank.
     */
    public static Ledger buy(Ledger state, SystemSpec system, String path, String skill,
                             int pointCost, int systemRankCap, int totalRankCap) {
        requireId(path);
        requireId(skill);
        if (pointCost < 1 || systemRankCap < 1 || totalRankCap < 1)
            throw new IllegalArgumentException("Invalid skill purchase config");
        if (contributedRanks(state, path, skill, system.id()) >= systemRankCap
                || effectiveRank(state, path, skill) >= totalRankCap
                || available(state, system) < pointCost
                || state.paidRanks().size() >= MAX_PAID_RANK_RECEIPTS)
            return state;
        var purchases = new ArrayList<>(state.paidRanks());
        purchases.add(new PaidRank(path, skill, system.id(), pointCost));
        return new Ledger(state.currentXp(), purchases, state.achievements(),
                state.rewardReceipts(), state.recentTransactions());
    }

    public static double validateGrowth(double factor) {
        if (!Double.isFinite(factor) || factor < MIN_GROWTH || factor > MAX_GROWTH)
            throw new IllegalArgumentException("Growth factor must be finite and in [0.001, 100]");
        return factor;
    }

    /**
     * Server-verified earning. The configured factor is resolved by the caller
     * for EACH transaction, allowing runtime modifications without rewriting
     * stored XP. Fixed sources skip growth. All numbers stay finite and exact
     * decimal XP accumulation is retained, including fractional ordinary awards.
     *
     * A growth factor below 1 can imply an infinite number of levels for finite
     * base XP; fail the WHOLE award rather than committing partial/overflow XP.
     */
    public static Award earn(Ledger source, SystemSpec system, String transaction,
                             String firstCompletion, BigDecimal base,
                             boolean fixed, double effectiveGrowth) {
        return earn(source, system, DEFAULT_SOURCE, transaction,
                firstCompletion, base, fixed, effectiveGrowth);
    }

    /**
     * Source-category identity is explicit: one event can pay multiple systems
     * and multiple independent source categories without accidental dedup.
     * Repeated callbacks in the SAME source/system/action are still rejected.
     */
    public static Award earn(Ledger source, SystemSpec system, String sourceCategory,
                             String transaction, String firstCompletion, BigDecimal base,
                             boolean fixed, double effectiveGrowth) {
        requireId(sourceCategory);
        requireReceiptKey(transaction);
        if (firstCompletion != null) requireReceiptKey(firstCompletion);
        String txReceipt = system.id() + "|" + sourceCategory + "|" + transaction;
        String firstReceipt = firstCompletion == null ? null
                : system.id() + "|" + sourceCategory + "|" + firstCompletion;
        Objects.requireNonNull(base, "base");
        requireBoundedDecimal(base);
        validateGrowth(effectiveGrowth);
        if (base.signum() <= 0 || base.compareTo(system.maxBaseAward()) > 0)
            throw new IllegalArgumentException("Award exceeds system source limits");
        if (source.recentTransactions().contains(txReceipt)
                || firstReceipt != null && source.rewardReceipts().contains(firstReceipt))
            return new Award(source, BigDecimal.ZERO, false);

        Ledger current = initialize(source, system);
        BigDecimal oldXp = current.currentXp().get(system.id());
        BigDecimal nextXp;
        try {
            nextXp = fixed ? oldXp.add(base) : ordinary(oldXp, system, base, effectiveGrowth);
            requireBoundedDecimal(nextXp);
            BigDecimal exclusiveLimit = system.xpPerPoint().multiply(
                    BigDecimal.valueOf(system.maxLevel() + 1L));
            if (nextXp.compareTo(exclusiveLimit) >= 0)
                return new Award(source, BigDecimal.ZERO, false);
        } catch (AwardLimitException limit) {
            // A legitimate event at an extreme, but valid, configured growth
            // factor must not throw through a server event callback.
            return new Award(source, BigDecimal.ZERO, false);
        } catch (IllegalArgumentException storageLimit) {
            // Input/spec validation happened above. This guard applies only
            // to the computed result and leaves all receipts untouched.
            return new Award(source, BigDecimal.ZERO, false);
        }

        var xp = new HashMap<>(current.currentXp());
        xp.put(system.id(), nextXp);
        var tx = new LinkedHashSet<>(current.recentTransactions());
        tx.add(txReceipt);
        while (tx.size() > MAX_RECENT_TRANSACTIONS) {
            var oldest = tx.iterator();
            oldest.next();
            oldest.remove();
        }
        var completed = new HashSet<>(current.rewardReceipts());
        if (firstReceipt != null) completed.add(firstReceipt);
        var result = new Ledger(xp, current.paidRanks(), current.achievements(), completed, tx);
        return new Award(result, nextXp.subtract(oldXp), true);
    }

    private static BigDecimal ordinary(BigDecimal xp, SystemSpec system,
                                       BigDecimal base, double growth) {
        if (growth == 1.0) return xp.add(base);
        BigDecimal factor = BigDecimal.valueOf(growth);
        BigDecimal remaining = base;
        BigDecimal nextXp = xp;
        int crossings = 0;

        while (remaining.signum() > 0) {
            long level = nextXp.divide(system.xpPerPoint(), 0, RoundingMode.DOWN).longValueExact();
            if (level >= system.maxLevel())
                throw new AwardLimitException("XP level safety ceiling reached");
            BigDecimal divisor = factor.pow(Math.toIntExact(level), PRECISION);
            BigDecimal progress = nextXp.subtract(system.xpPerPoint().multiply(BigDecimal.valueOf(level)));
            BigDecimal toNext = system.xpPerPoint().subtract(progress);
            if (toNext.signum() <= 0)
                throw new IllegalStateException("Invalid XP level fraction");
            BigDecimal baseToNext = toNext.multiply(divisor, PRECISION);
            if (remaining.compareTo(baseToNext) >= 0) {
                if (++crossings > MAX_LEVEL_CROSSINGS_PER_AWARD)
                    throw new AwardLimitException("Unsafe number of levels in one XP award");
                // A microscopic crossing cost can have a scale of millions at
                // valid g < 1. Never expand that decimal in an exact subtraction.
                if (baseToNext.compareTo(MIN_XP_UNIT) < 0)
                    throw new AwardLimitException("XP crossing cost below safe decimal resolution");
                remaining = remaining.subtract(baseToNext);
                nextXp = system.xpPerPoint().multiply(BigDecimal.valueOf(level + 1L));
            } else {
                BigDecimal effective = remaining.divide(divisor, PRECISION);
                // Reject the whole award before creating any receipt when its
                // gain cannot be represented within the storage safety budget.
                if (effective.compareTo(MIN_XP_UNIT) < 0)
                    throw new AwardLimitException("XP award below safe decimal resolution");
                if (effective.scale() > MAX_XP_SCALE)
                    effective = effective.setScale(MAX_XP_SCALE, RoundingMode.DOWN);
                nextXp = nextXp.add(effective);
                remaining = BigDecimal.ZERO;
            }
        }
        return nextXp;
    }

    /**
     * Atomic economy half of an actual Path membership change. Path admission,
     * exclusivity and effects are validated by the runtime BEFORE calling this.
     *
     * RETAIN: paid ranks stay committed (but Path effects must be gated elsewhere).
     * REFUND: paid ranks vanish; their points are released, not XP-minted.
     * FORFEIT: paid ranks vanish and their XP-equivalent is destroyed.
     *
     * With wipeUnallocated, preexisting free points/fraction vanish; explicit
     * REFUND points remain available. TMM uses FORFEIT + wipeUnallocated.
     */
    public static Ledger transition(Ledger source, Map<String, SystemSpec> systems,
                                    Set<String> departedPaths, Map<PathSystem, DeparturePolicy> overrides,
                                    DeparturePolicy fallback, boolean wipeUnallocated,
                                    boolean membershipChanged) {
        return transition(source, systems, departedPaths, overrides, fallback,
                wipeUnallocated ? Set.copyOf(systems.keySet()) : Set.of(), membershipChanged);
    }

    /** Same atomic transition, allowing each XP system to choose whether free XP is discarded. */
    public static Ledger transition(Ledger source, Map<String, SystemSpec> systems,
                                    Set<String> departedPaths, Map<PathSystem, DeparturePolicy> overrides,
                                    DeparturePolicy fallback, Set<String> wipeSystems,
                                    boolean membershipChanged) {
        Objects.requireNonNull(fallback, "fallback");
        if (!membershipChanged) return source;
        Objects.requireNonNull(departedPaths, "departedPaths");
        Objects.requireNonNull(wipeSystems, "wipeSystems");
        for (String id : wipeSystems) if (!systems.containsKey(id))
            throw new IllegalArgumentException("Unknown XP system wipe policy " + id);

        var remainingRanks = new ArrayList<PaidRank>();
        var forfeited = new HashMap<String, Long>();
        var refunded = new HashMap<String, Long>();
        for (PaidRank purchase : source.paidRanks()) {
            if (!departedPaths.contains(purchase.path())) {
                remainingRanks.add(purchase);
                continue;
            }
            DeparturePolicy policy = overrides.getOrDefault(
                    new PathSystem(purchase.path(), purchase.system()), fallback);
            switch (policy) {
                case RETAIN -> remainingRanks.add(purchase);
                case REFUND -> refunded.merge(purchase.system(), (long) purchase.pointCost(), Math::addExact);
                case FORFEIT -> forfeited.merge(purchase.system(), (long) purchase.pointCost(), Math::addExact);
            }
        }

        var xp = new HashMap<String, BigDecimal>();
        Ledger remaining = new Ledger(source.currentXp(), remainingRanks,
                source.achievements(), source.rewardReceipts(), source.recentTransactions());
        for (var row : source.currentXp().entrySet()) {
            SystemSpec system = systems.get(row.getKey());
            if (system == null) throw new IllegalArgumentException("Missing XP system " + row.getKey());
            long removed = forfeited.getOrDefault(row.getKey(), 0L);
            BigDecimal revised;
            if (wipeSystems.contains(row.getKey())) {
                long retained = Math.addExact(committed(remaining, system.id()),
                        refunded.getOrDefault(system.id(), 0L));
                revised = system.xpPerPoint().multiply(BigDecimal.valueOf(retained));
            } else {
                revised = row.getValue().subtract(
                        system.xpPerPoint().multiply(BigDecimal.valueOf(removed)));
            }
            if (revised.signum() < 0)
                throw new IllegalStateException("Invalid forfeiture beyond earned XP");
            xp.put(system.id(), revised);
        }
        Ledger result = new Ledger(xp, remainingRanks, source.achievements(),
                source.rewardReceipts(), source.recentTransactions());
        for (SystemSpec system : systems.values()) available(result, system);
        return result;
    }

    /**
     * Verified transaction keys may be composite IDs such as
     * boss:draconicevolution:draconic_guardian or advancement:namespace:path.
     * They are not registry IDs; keep them bounded and delimiter-safe.
     */
    private static void requireReceiptKey(String key) {
        if (key == null || key.isBlank() || key.length() > 240
                || !key.matches("[a-zA-Z0-9_.:/#@-]+") || key.contains("|"))
            throw new IllegalArgumentException("Invalid verified transaction key");
    }

    private static final class AwardLimitException extends RuntimeException {
        private AwardLimitException(String message) { super(message); }
    }

    private static void requireBoundedDecimal(BigDecimal value) {
        // Check metadata before any addition, division or plain-string save.
        // A short literal such as 1e-10000000 must not allocate megabytes.
        long integerDigits = (long) value.precision() - value.scale();
        if (Math.abs((long) value.scale()) > MAX_XP_SCALE
                || integerDigits > MAX_XP_INTEGER_DIGITS)
            throw new IllegalArgumentException("XP decimal exceeds storage safety limits");
    }

    private static void requireId(String id) {
        if (id == null || !id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid namespaced identifier: " + id);
    }
}
