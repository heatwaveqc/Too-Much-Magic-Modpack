package io.github.heatwaveqc.tyr.progression.runtime.external;

import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import java.util.Objects;

/**
 * External rewards spend the SAME global XP economy used by Core and specialist
 * ranks. Their permanent receipts use a synthetic, unjoinable path; changing
 * Paths and ordinary respec never refund a granted native-mod reward.
 *
 * Price units are XP *points* (currently 1 MXP point = 1000 MXP); 0 base price
 * explicitly means disabled until the owner selects a balance value.
 */
public final class ExternalRewardPolicy {
    private ExternalRewardPolicy() {}

    public static final String EXTERNAL_PATH = "tyr_progression:external";
    public static final int HARD_MAX_PURCHASES = 1_000_000;

    public record Offer(String id, String kind, String currency,
                        int basePoints, int stepPoints, int maxPurchases) {
        public Offer {
            if (id == null || !id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")
                    || currency == null || !currency.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")
                    || !"astral_perk_token".equals(kind)
                    || basePoints < 0 || stepPoints < 0
                    || maxPurchases < 0 || maxPurchases > HARD_MAX_PURCHASES)
                throw new IllegalArgumentException("Invalid external reward configuration");
        }
        public int cap() { return maxPurchases == 0 ? HARD_MAX_PURCHASES : maxPurchases; }
    }

    public static int owned(ExperienceEconomy.Ledger state, Offer offer) {
        return ExperienceEconomy.contributedRanks(state, EXTERNAL_PATH, offer.id(), offer.currency());
    }

    /** -1 is disabled, overflow, capped or otherwise unquotable. */
    public static int price(Offer offer, int owned) {
        if (offer.basePoints() == 0 || owned < 0 || owned >= offer.cap()) return -1;
        try {
            int amount = Math.addExact(offer.basePoints(), Math.multiplyExact(owned, offer.stepPoints()));
            return amount > 0 ? amount : -1;
        } catch (ArithmeticException overflow) { return -1; }
    }

    public record Plan(ExperienceEconomy.Ledger charged, String tokenId,
                       int pricePoints, int purchaseIndex) {}

    /**
     * Pure preparation: validates the real shared available-point balance and
     * creates exactly one paid rank, never a separate wallet or Path ownership.
     * Caller commits this proposed ledger BEFORE invoking the native adapter;
     * if the native operation fails without a side effect, caller rolls back.
     */
    public static Plan prepare(ExperienceEconomy.Ledger before,
                               ExperienceEconomy.SystemSpec system, Offer offer) {
        Objects.requireNonNull(before);
        if (system == null || offer == null || !system.id().equals(offer.currency())) return null;
        int count = owned(before, offer);
        int amount = price(offer, count);
        if (amount < 1 || ExperienceEconomy.available(before, system) < amount) return null;
        ExperienceEconomy.Ledger paid = ExperienceEconomy.buy(
                before, system, EXTERNAL_PATH, offer.id(), amount, offer.cap(), offer.cap());
        if (paid == before) return null;
        return new Plan(paid, tokenId(offer.kind(), count + 1), amount, count + 1);
    }

    /** Token names are stable, player-local in native Astral progress, bounded. */
    public static String tokenId(String kind, int purchaseIndex) {
        if (!"astral_perk_token".equals(kind) || purchaseIndex < 1
                || purchaseIndex > HARD_MAX_PURCHASES)
            throw new IllegalArgumentException("Unknown external token kind/index");
        return "tyr_progression:astral_purchase_" + String.format(java.util.Locale.ROOT, "%06d", purchaseIndex);
    }
}
