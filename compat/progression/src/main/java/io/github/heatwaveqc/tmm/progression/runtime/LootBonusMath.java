package io.github.heatwaveqc.tmm.progression.runtime;

/**
 * Effective enchantment levels, not invented extra items or second loot rolls.
 * Pure calculations keep the gameplay contract testable without Minecraft.
 */
public final class LootBonusMath {
    private LootBonusMath() {}

    /** Base Fortune I at Treasure Hand, plus up to Fortune III from Tyz Fortunate Hands. */
    public static int monkFortune(int treasureRank, int tyzFortuneBonus,
                                  boolean fortuneMode, boolean validEmptyHand) {
        return treasureRank > 0 && fortuneMode && validEmptyHand
                ? 1 + Math.min(3, Math.max(0, tyzFortuneBonus)) : 0;
    }

    /** Favored prey counts as Looting I, including kills made with a bow. */
    public static int favoredLooting(boolean joinedHunter, boolean validKill,
                                    boolean matchesSpecialty) {
        return joinedHunter && validKill && matchesSpecialty ? 1 : 0;
    }
}
