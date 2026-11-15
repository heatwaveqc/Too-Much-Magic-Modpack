package io.github.heatwaveqc.tmm.monk.progression;

/** Small, deterministic early-rank training pilot; gameplay rewards stay server owned. */
public final class MonkMasteryRules {
    public static final int HIGHEST_PILOT_LEVEL = 4;

    private MonkMasteryRules() { }

    /** Cumulative mastery since initiation. Rank 1 has no XP prerequisite. */
    public static int totalXpForLevel(int level) {
        return switch (level) {
            case 1 -> 0;
            case 2 -> 20;
            case 3 -> 55;
            case 4 -> 105;
            default -> throw new IllegalArgumentException("Early mastery levels are 1-4");
        };
    }

    public static int eligibleLevel(int currentlyOwnedLevel, int totalXp) {
        int current = Math.max(1, Math.min(currentlyOwnedLevel, HIGHEST_PILOT_LEVEL));
        while (current < HIGHEST_PILOT_LEVEL && totalXp >= totalXpForLevel(current + 1)) {
            current++;
        }
        return current;
    }

    public static int safeAdd(int current, int reward) {
        if (reward < 0) throw new IllegalArgumentException("Negative mastery reward");
        return (int) Math.min(Integer.MAX_VALUE, (long) Math.max(0, current) + reward);
    }
}
