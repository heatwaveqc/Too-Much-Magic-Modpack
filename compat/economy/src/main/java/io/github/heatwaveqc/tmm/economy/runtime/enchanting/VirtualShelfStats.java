package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import dev.shadowsoffire.apothic_enchanting.table.EnchantmentTableStats;
import dev.shadowsoffire.apothic_enchanting.table.EnchantingStatRegistry;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Native Apothic 1.6.4 accumulator for already resolved virtual positions.
 * No fake level, placed block, ambient shelves or synthetic numeric stats.
 * Resolution (including stateful providers) is a separate authoritative step.
 */
public final class VirtualShelfStats {
    private VirtualShelfStats() {}

    /** Exactly one resolved shelf position, or one real 1-4 candle cluster. */
    public record Position(EnchantingStatRegistry.Stats stats,
                           Set<Holder<Enchantment>> blacklist,
                           boolean treasure, boolean stable) {
        public Position {
            Objects.requireNonNull(stats);
            blacklist = Set.copyOf(blacklist);
            if (!Float.isFinite(stats.eterna()) || !Float.isFinite(stats.maxEterna())
                || !Float.isFinite(stats.quanta()) || !Float.isFinite(stats.arcana()))
                throw new IllegalArgumentException("Non-finite native shelf stats");
        }
    }

    /** The native builder supplies defaults, ordering, step caps and clamping. */
    public static EnchantmentTableStats aggregate(List<Position> positions) {
        Objects.requireNonNull(positions);
        if (positions.size() > VirtualCandlePacking.MAX_POSITIONS)
            throw new IllegalArgumentException("Virtual shelf capacity exceeds 32 positions");
        EnchantmentTableStats.Builder builder = new EnchantmentTableStats.Builder();
        for (Position pos : positions) {
            var stats = pos.stats();
            builder.addEterna(stats.eterna(), stats.maxEterna());
            builder.addQuanta(stats.quanta());
            builder.addArcana(stats.arcana());
            builder.addClues(stats.clues());
            pos.blacklist().forEach(builder::blacklistEnchant);
            if (pos.treasure()) builder.setAllowsTreasure(true);
            if (pos.stable()) builder.setStable(true);
        }
        // Do not pre-cap raw Eterna to a tier: the native menu applies the
        // actual player's MAX_ETERNA via stats.eterna(player) on use.
        return builder.build();
    }
}
