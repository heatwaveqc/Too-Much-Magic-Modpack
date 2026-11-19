package io.github.heatwaveqc.tmm.economy;

import java.math.BigInteger;

/** Shared exact valuation; fractional resource costs are represented by
 * scaling the entire recipe, never by rounding each individual resource. */
public final class ResourceAccounting {
    private ResourceAccounting() {}
    public enum Rate {
        MANA(1,20), FE(1,200), BLOOD_MB(1,5), RAW_SPIRITUS(1,1),
        ASPECTED_SPIRITUS(2,1), SOUL(32,1), XP_POINT(5,1),
        XP_MB(1,4), EMBER(1,2), PSI(1,10), SOURCE(3,4),
        GOETY_SOUL_ENERGY(2,1), PRIMAL_CENTIMANA(1,50);
        final int numerator, denominator;
        Rate(int numerator, int denominator) {
            this.numerator = numerator; this.denominator = denominator;
        }
        public Cost of(long units) {
            if (units < 0) throw new IllegalArgumentException("Negative resource debit");
            return new Cost(BigInteger.valueOf(units).multiply(BigInteger.valueOf(numerator)),
                    BigInteger.valueOf(denominator));
        }
    }
    public record Cost(BigInteger numerator, BigInteger denominator) {
        public static final Cost ZERO = new Cost(BigInteger.ZERO, BigInteger.ONE);
        public Cost {
            if (numerator.signum() < 0 || denominator.signum() <= 0)
                throw new IllegalArgumentException("Invalid resource cost");
            BigInteger gcd = numerator.gcd(denominator);
            numerator = numerator.divide(gcd); denominator = denominator.divide(gcd);
        }
        public Cost plus(Cost other) {
            return new Cost(numerator.multiply(other.denominator).add(other.numerator.multiply(denominator)),
                    denominator.multiply(other.denominator));
        }
        public Cost times(long n) {
            if (n < 0) throw new IllegalArgumentException("Negative multiplier");
            return new Cost(numerator.multiply(BigInteger.valueOf(n)), denominator);
        }
        public int batches() { return denominator.intValueExact(); }
        public int emcUnits() { return numerator.intValueExact(); }
        public int scaleCount(int count) { return Math.multiplyExact(count, batches()); }
    }
}
