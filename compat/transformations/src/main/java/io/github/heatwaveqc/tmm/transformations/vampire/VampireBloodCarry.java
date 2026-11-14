package io.github.heatwaveqc.tmm.transformations.vampire;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Fractional native blood earned from JAWR transfers, preserved across relog.
 * Never a second blood pool: less than one unit of canonical Vampirism blood.
 */
public record VampireBloodCarry(int denominator, int numerator) {
    public static final Codec<VampireBloodCarry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("denominator").forGetter(VampireBloodCarry::denominator),
                    Codec.INT.fieldOf("numerator").forGetter(VampireBloodCarry::numerator)
            ).apply(instance, VampireBloodCarry::new));

    public static VampireBloodCarry empty() { return new VampireBloodCarry(1, 0); }

    public VampireBloodCarry {
        if (denominator <= 0 || numerator < 0 || numerator >= denominator) {
            throw new IllegalArgumentException("Invalid fractional blood: " + numerator + "/" + denominator);
        }
    }

    public record Gain(int wholeUnits, VampireBloodCarry remainder) { }

    /** Conserves fractional native blood across repeated JAWR blood micro-gains. */
    public Gain gain(int jawrAmount, int jawrMax, int nativeMax) {
        if (jawrAmount < 0 || jawrMax <= 0 || nativeMax <= 0) {
            throw new IllegalArgumentException("Blood gain and capacities must be valid");
        }
        // Fractions represent shares of one native unit. All int*int products fit long.
        long rescaled = ((long) numerator * jawrMax) / denominator;
        long total = ((long) jawrAmount * nativeMax) + rescaled;
        long units = Math.min((long) nativeMax, total / jawrMax);
        int remainder = (int) (total % jawrMax);
        return new Gain((int) units, new VampireBloodCarry(jawrMax, remainder));
    }
}
