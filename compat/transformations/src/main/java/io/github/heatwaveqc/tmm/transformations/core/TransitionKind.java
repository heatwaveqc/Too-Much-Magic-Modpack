package io.github.heatwaveqc.tmm.transformations.core;

/** A transition's intent. Classification does not perform the transformation. */
public enum TransitionKind {
    NORMAL,
    CONTROLLED_CURE,
    VOLUNTARY_HUNTER_REPLACEMENT,
    ABOMINATION_RITUAL
}
