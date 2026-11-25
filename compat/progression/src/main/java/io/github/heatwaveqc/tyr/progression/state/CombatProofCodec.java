package io.github.heatwaveqc.tyr.progression.state;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
public final class CombatProofCodec {
    private CombatProofCodec(){}
    public static final Codec<CombatProof> CODEC=RecordCodecBuilder.create(i->i.group(
        Codec.BOOL.fieldOf("seen").forGetter(CombatProof::seen),
        Codec.BOOL.fieldOf("qualified").forGetter(CombatProof::qualified),
        Codec.STRING.fieldOf("owner").forGetter(CombatProof::owner),
        Codec.LONG.fieldOf("first_tick").forGetter(CombatProof::firstTick),
        Codec.DOUBLE.fieldOf("baseline_max_hp").forGetter(CombatProof::baselineMaxHp),
        Codec.BOOL.fieldOf("last_fist").forGetter(CombatProof::lastFist),
        Codec.BOOL.fieldOf("settled").forGetter(CombatProof::settled)
    ).apply(i,CombatProof::new));
}
