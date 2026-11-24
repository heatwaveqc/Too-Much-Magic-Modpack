package io.github.heatwaveqc.tyr.progression.mxp.core;

/** Validate only measured accepted payment; native units can differ across shared ledgers. */
public final class PaymentReceiptPolicy {
    private PaymentReceiptPolicy() {}
    public static boolean positive(double x) { return Double.isFinite(x) && x > 0; }
    public static boolean bounded(double actual,double quoted) {
        return positive(actual) && positive(quoted) && actual <= quoted + 1e-6;
    }
    public static boolean fullyFunded(double actual,double quoted) {
        return bounded(actual,quoted) && Math.abs(actual-quoted) <= 1e-6;
    }
    // ANS native attempt UUID, rather than fresh per callback identity.
    public static String ansTransaction(java.util.UUID id) {
        return "ans:payment:"+java.util.Objects.requireNonNull(id);
    }
}
