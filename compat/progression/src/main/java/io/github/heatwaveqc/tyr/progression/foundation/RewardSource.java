/*
 * Adapted from HaooooZhang/Epiphany, revision 1cbffb31ef4474fec73d66f1a888b4db27c4a71d.
 * content/reward/RewardSource.java
 * Original license: MIT; see third_party_licenses/Epiphany.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.Objects;

/** Stable effect ownership key; keep kind, owner and slot separate to avoid collisions. */
public record RewardSource(String ownerKind, String ownerId, String rewardSlot) {
    public RewardSource {
        Objects.requireNonNull(ownerKind);
        Objects.requireNonNull(ownerId);
        Objects.requireNonNull(rewardSlot);
        if (!ownerKind.matches("[a-z0-9_.-]+") ||
            !ownerId.matches("[a-z0-9_.-]+:[a-z0-9_./-]+") ||
            !rewardSlot.matches("[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid reward source");
    }
}
