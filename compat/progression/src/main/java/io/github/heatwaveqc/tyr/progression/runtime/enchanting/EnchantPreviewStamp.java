package io.github.heatwaveqc.tyr.progression.runtime.enchanting;

/**
 * Revision pair captured when a server creates a random-enchantment preview.
 * Commit must reject and recompute whenever either the personal filter or live
 * enchanting datapack registry has changed while the menu was open.
 */
public record EnchantPreviewStamp(long filterRevision, long enchantingDataRevision) {
    public EnchantPreviewStamp {
        if (filterRevision < 0 || enchantingDataRevision < 0)
            throw new IllegalArgumentException("preview revisions must be non-negative");
    }

    public static EnchantPreviewStamp capture(PersonalEnchantFilter filter, long enchantingDataRevision) {
        return new EnchantPreviewStamp(filter.revision(), enchantingDataRevision);
    }

    public boolean current(PersonalEnchantFilter filter, long currentEnchantingDataRevision) {
        return filter.revision() == filterRevision
            && currentEnchantingDataRevision == enchantingDataRevision;
    }
}
