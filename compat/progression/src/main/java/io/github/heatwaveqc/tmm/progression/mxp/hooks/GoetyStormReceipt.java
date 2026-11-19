package io.github.heatwaveqc.tmm.progression.mxp.hooks;
import io.github.heatwaveqc.tmm.progression.mxp.core.GoetyRitualProof;

/** A native storm entity accepts original caster attribution only after its source altar clears. */
public interface GoetyStormReceipt {
    void tmm$armStorm(GoetyRitualProof proof);
}
