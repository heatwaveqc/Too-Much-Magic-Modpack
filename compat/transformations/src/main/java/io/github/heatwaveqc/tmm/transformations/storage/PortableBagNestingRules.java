package io.github.heatwaveqc.tmm.transformations.storage;

/** Pure release-blocker #16 decision: no destructive migration or mutations. */
public final class PortableBagNestingRules {
    private PortableBagNestingRules() { }

    public static boolean rejects(boolean empty, boolean taggedBag, boolean shulkerBox) {
        return !empty && (taggedBag || shulkerBox);
    }
}
