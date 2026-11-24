package io.github.heatwaveqc.tyr.progression.state;

/**
 * Two independent, permanent account-level discoveries. Neither is a Path,
 * player mode, world gamerule, skill level, or globally configured client flag.
 */
public record TooltipMilestones(boolean projecte, boolean replication) {
    public static TooltipMilestones empty() {
        return new TooltipMilestones(false, false);
    }

    /** Unlocks are monotonic, independent, and idempotent even after item loss. */
    public TooltipMilestones discover(boolean hasStone, boolean hasReplicaIngot) {
        boolean stone = projecte || hasStone;
        boolean replica = replication || hasReplicaIngot;
        return stone == projecte && replica == replication ? this : new TooltipMilestones(stone, replica);
    }
}
