package io.github.heatwaveqc.tyr.progression.state;

/**
 * Two independent permanent discoveries and a durable, one-time delivery
 * receipt for the ProjectE discovery's native Covalence Dust knowledge.
 */
public record TooltipMilestones(boolean projecte, boolean replication, boolean covalencePending) {
    // Older saves and existing callers deliberately do not receive backfill.
    public TooltipMilestones(boolean projecte, boolean replication) {
        this(projecte, replication, false);
    }

    public static TooltipMilestones empty() {
        return new TooltipMilestones(false, false, false);
    }

    /** Unlocks are monotonic, independent, and idempotent even after item loss. */
    public TooltipMilestones discover(boolean hasStone, boolean hasReplicaIngot) {
        boolean stone = projecte || hasStone;
        boolean replica = replication || hasReplicaIngot;
        boolean pending = covalencePending || (!projecte && stone);
        return stone == projecte && replica == replication && pending == covalencePending
            ? this : new TooltipMilestones(stone, replica, pending);
    }

    /** Called only after native knowledge has been verified and synced. */
    public TooltipMilestones covalenceDelivered() {
        return covalencePending ? new TooltipMilestones(projecte, replication, false) : this;
    }
}
