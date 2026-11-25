package io.github.heatwaveqc.tyr.progression.mxp.hooks;
import io.github.heatwaveqc.tyr.progression.mxp.core.GoetyRitualProof;

/** A native storm entity accepts original caster attribution only after its source altar clears. */
public interface GoetyStormReceipt {
    void tmm$armStorm(GoetyRitualProof proof);
}
