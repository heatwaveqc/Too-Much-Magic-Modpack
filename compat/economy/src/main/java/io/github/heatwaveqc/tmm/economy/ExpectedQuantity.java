package io.github.heatwaveqc.tmm.economy;

import java.math.BigDecimal;
import java.math.BigInteger;

/** Exact rational nominal quantities for independently rolled yields and
 * fractional resource consumption. All material counts are scaled together. */
public record ExpectedQuantity(BigInteger numerator, BigInteger denominator) {
    public static final ExpectedQuantity ZERO = new ExpectedQuantity(BigInteger.ZERO, BigInteger.ONE);
    public ExpectedQuantity {
        if (numerator.signum() < 0 || denominator.signum() <= 0)
            throw new IllegalArgumentException("invalid expected quantity");
        BigInteger gcd = numerator.gcd(denominator);
        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);
    }
    public static ExpectedQuantity of(float amount) {
        if (!Float.isFinite(amount) || amount < 0) throw new IllegalArgumentException("invalid nominal amount");
        BigDecimal decimal = new BigDecimal(Float.toString(amount)).stripTrailingZeros();
        int scale = decimal.scale();
        BigInteger n = decimal.unscaledValue();
        BigInteger d = BigInteger.ONE;
        if (scale > 0) d = BigInteger.TEN.pow(scale);
        else if (scale < 0) n = n.multiply(BigInteger.TEN.pow(-scale));
        return new ExpectedQuantity(n, d);
    }
    public static ExpectedQuantity probability(float p) {
        if (!Float.isFinite(p) || p < 0) throw new IllegalArgumentException("invalid probability");
        return of(Math.min(p, 1F));
    }
    public ExpectedQuantity times(long multiplier) {
        if (multiplier < 0) throw new IllegalArgumentException("negative multiplier");
        return new ExpectedQuantity(numerator.multiply(BigInteger.valueOf(multiplier)), denominator);
    }
    public ExpectedQuantity plus(ExpectedQuantity other) {
        return new ExpectedQuantity(numerator.multiply(other.denominator).add(other.numerator.multiply(denominator)),
                denominator.multiply(other.denominator));
    }
    public boolean isZero() { return numerator.signum() == 0; }
    public int countAt(int batches) {
        if (batches <= 0) throw new IllegalArgumentException("invalid scale");
        var qr = numerator.multiply(BigInteger.valueOf(batches)).divideAndRemainder(denominator);
        if (qr[1].signum() != 0) throw new IllegalArgumentException("fractional graph count");
        return qr[0].intValueExact();
    }
    public static int scale(ExpectedQuantity... quantities) {
        BigInteger lcm = BigInteger.ONE;
        for (var q : quantities) {
            lcm = lcm.divide(lcm.gcd(q.denominator)).multiply(q.denominator);
            if (lcm.compareTo(BigInteger.valueOf(1_000_000)) > 0)
                throw new IllegalArgumentException("expected quantity scale exceeds graph bound");
        }
        return lcm.intValueExact();
    }
    /** Growth is processed once per second and stage progress resets. */
    public static long growthRolls(int secondsPerStage, int stages, float acceleration) {
        if (secondsPerStage <= 0 || stages < 0 || !Float.isFinite(acceleration)
                || acceleration <= 0 || acceleration > 1_000_000)
            throw new IllegalArgumentException("invalid growth progression");
        long target = Math.multiplyExact((long) secondsPerStage, 20);
        int progress = (int) (20 * acceleration);
        if (progress <= 0) throw new IllegalArgumentException("no growth progress");
        return Math.multiplyExact((target + progress - 1L) / progress, stages);
    }
}
