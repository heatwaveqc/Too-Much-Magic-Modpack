package io.github.heatwaveqc.tyr.progression.runtime.enchanting;

/**
 * Revision pair captured when a server creates a random-enchantment preview.
 * Commit must reject and recompute whenever either the personal filter or live
 * enchanting datapack registry has changed while the menu was open.
 */
public record EnchantPreviewStamp(long filterRevision, long enchantingDataRevision,
                                  boolean unlocked, java.util.List<String> excluded) {
    public EnchantPreviewStamp {
        if (filterRevision < 0 || enchantingDataRevision < 0)
            throw new IllegalArgumentException("preview revisions must be non-negative");
        excluded = java.util.List.copyOf(excluded);
    }

    public static EnchantPreviewStamp capture(PersonalEnchantFilter filter, long enchantingDataRevision) {
        return new EnchantPreviewStamp(filter.revision(), enchantingDataRevision,
            filter.unlocked(), filter.excluded());
    }

    public boolean current(PersonalEnchantFilter filter, long currentEnchantingDataRevision) {
        return filter.revision() == filterRevision
            && currentEnchantingDataRevision == enchantingDataRevision
            && filter.unlocked() == unlocked && filter.excluded().equals(excluded);
    }
}
