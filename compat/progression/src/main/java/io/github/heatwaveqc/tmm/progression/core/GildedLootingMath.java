package io.github.heatwaveqc.tmm.progression.core;

/** Golden sword/axe Looting is native +3, plus up to seven independent Tyz skill ranks.
 * The additive bonus is applied once within mob loot calculations, not to the item.
 */
public final class GildedLootingMath {
    public static final int BASE_BONUS = 3;
    public static final int TYZ_RANKS = 7;
    public static final int MAX_BONUS = BASE_BONUS + TYZ_RANKS;
    private GildedLootingMath() {}

    public static int virtualBonus(boolean nativeLootingUnlocked, int tyzRank) {
        if (!nativeLootingUnlocked) return 0;
        return BASE_BONUS + Math.max(0, Math.min(TYZ_RANKS,tyzRank));
    }

    public static int effectiveLooting(int physical, boolean nativeLootingUnlocked, int tyzRank) {
        return Math.max(0,physical) + virtualBonus(nativeLootingUnlocked,tyzRank);
    }
}
