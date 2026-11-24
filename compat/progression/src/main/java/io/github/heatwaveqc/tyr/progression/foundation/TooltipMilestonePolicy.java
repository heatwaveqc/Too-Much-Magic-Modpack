package io.github.heatwaveqc.tyr.progression.foundation;

/**
 * Presentation rules only. Does not touch native EMC, matter, machines, ItemInfo
 * knowledge, recipes, tool behavior or pre-Stone Divining Rod messages.
 *
 * Exact third-party tooltip adapters must pass current native knowledge/config
 * each time; this policy deliberately stores no duplicate knowledge cache.
 */
public final class TooltipMilestonePolicy {
    private TooltipMilestonePolicy() {}

    public static boolean projecteNumeric(boolean stoneUnlocked, boolean nativeKnowsItem,
                                          boolean nativeTooltipEnabled) {
        return stoneUnlocked && nativeKnowsItem && nativeTooltipEnabled;
    }

    public static boolean projecteLearnedIndicator(boolean stoneUnlocked,
                                                   boolean nativeIndicatorEnabled) {
        return stoneUnlocked && nativeIndicatorEnabled;
    }

    public static boolean replicationMatter(boolean replicaUnlocked, boolean nativeSetting) {
        return replicaUnlocked && nativeSetting;
    }

    /** Rod chat/system readings are a separately approved pre-Stone exception. */
    public static boolean keepNativeDiviningRodReadout() {
        return true;
    }
}
