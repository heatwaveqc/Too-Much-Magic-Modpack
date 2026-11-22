package io.github.heatwaveqc.tyr.progression.foundation;

/**
 * Exact upstream translation identifiers, not English text or a broad "EMC"
 * substring. A presentation adapter may remove only lines it recognizes.
 *
 * ProjectE 1.21.1 ToolTipEvent/PELang and Project Expansion 1.0.6
 * ItemTooltipEvents/Lang supply the native component roots.
 */
public final class TooltipLinePolicy {
    private TooltipLinePolicy() {}

    public enum Kind {
        ITEM_EMC, STORED_EMC, LEARNED_STATUS, REPLICATION_HINT, UNRELATED
    }

    public static Kind classify(String translationKey) {
        if (translationKey == null) return Kind.UNRELATED;
        return switch (translationKey) {
            case "emc.projecte.tooltip",
                 "emc.projecte.tooltip.stack",
                 "emc.projecte.tooltip.with_sell",
                 "emc.projecte.tooltip.stack.with_sell" -> Kind.ITEM_EMC;
            case "emc.projecte.stored" -> Kind.STORED_EMC;
            case "emc.projecte.has_knowledge", "emc.projecte.no_knowledge",
                 "text.projectexpansion.learned",
                 "text.projectexpansion.not_learned" -> Kind.LEARNED_STATUS;
            case "tooltip.replication.matter_value" -> Kind.REPLICATION_HINT;
            default -> Kind.UNRELATED;
        };
    }

    public static boolean remove(Kind kind, boolean stoneUnlocked,
                                 boolean nativeKnowsItem, boolean replicaUnlocked) {
        return switch (kind) {
            case ITEM_EMC -> !stoneUnlocked || !nativeKnowsItem;
            case STORED_EMC, LEARNED_STATUS -> !stoneUnlocked;
            case REPLICATION_HINT -> !replicaUnlocked;
            case UNRELATED -> false;
        };
    }

    public static boolean removeReplicationComponent(String className, boolean replicaUnlocked) {
        return !replicaUnlocked && "com.buuz135.replication.client.render.MatterTooltipComponent".equals(className);
    }
}
