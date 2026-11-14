package io.github.heatwaveqc.tmm.transformations.vampire;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** Minecraft-only persisted attachment codec. Pure carry math has no game dependency. */
public final class VampireBloodCarryCodec {
    public static final Codec<VampireBloodCarry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("denominator").forGetter(VampireBloodCarry::denominator),
                    Codec.INT.fieldOf("numerator").forGetter(VampireBloodCarry::numerator)
            ).apply(instance, VampireBloodCarry::new));

    private VampireBloodCarryCodec() { }
}
