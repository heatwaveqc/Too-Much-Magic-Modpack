package io.github.heatwaveqc.tmm.transformations.core;

/** An advisory preflight decision until independently verified upstream hooks exist. */
public record TransitionDecision(boolean allowed, EffectiveForm before, EffectiveForm after, String reason) {
    public static TransitionDecision allow(EffectiveForm before, EffectiveForm after, String reason) {
        return new TransitionDecision(true, before, after, reason);
    }

    public static TransitionDecision deny(EffectiveForm before, EffectiveForm after, String reason) {
        return new TransitionDecision(false, before, after, reason);
    }
}
