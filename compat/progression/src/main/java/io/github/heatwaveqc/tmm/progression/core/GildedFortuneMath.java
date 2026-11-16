package io.github.heatwaveqc.tmm.progression.core;

/** Native Gilded base Fortune is +3; seven purchased Tyz ranks raise it to +10.
 * Actual enchantment levels are separate and never capped by this policy. */
public final class GildedFortuneMath {
    public static final int BASE_BONUS = 3;
    public static final int MAX_BONUS = 10;
    public static final int TYZ_RANKS = MAX_BONUS - BASE_BONUS;
    private GildedFortuneMath() {}

    public static int virtualBonus(boolean gildedSkillActive, int tyzRank) {
        if (!gildedSkillActive) return 0;
        return BASE_BONUS + Math.max(0, Math.min(TYZ_RANKS, tyzRank));
    }

    public static int totalEffectiveFortune(int storedLevel, boolean gildedSkillActive, int tyzRank) {
        return Math.max(0, storedLevel) + virtualBonus(gildedSkillActive, tyzRank);
    }
}
