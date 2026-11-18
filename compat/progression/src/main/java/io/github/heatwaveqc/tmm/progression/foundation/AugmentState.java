/*
 * Adapted from Rearth/Oritech, revision 225b182d0f8d2f9eb56a61220569dc32c4461f8e.
 * common/src/main/java/rearth/oritech/block/entity/augmenter/api/Augment.java
 * Original license: CC0-1.0; see third_party_licenses/Oritech.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tmm.progression.foundation;

/** Installation is distinct from activation; no mutation of a player ledger here. */
public enum AugmentState {
    ENABLED, DISABLED, NOT_INSTALLED;

    public boolean installed() { return this != NOT_INSTALLED; }
    public boolean enabled() { return this == ENABLED; }

    public AugmentState toggle(boolean toggleable) {
        if (!toggleable) return this;
        return switch (this) {
            case ENABLED -> DISABLED;
            case DISABLED -> ENABLED;
            case NOT_INSTALLED -> NOT_INSTALLED;
        };
    }
}
