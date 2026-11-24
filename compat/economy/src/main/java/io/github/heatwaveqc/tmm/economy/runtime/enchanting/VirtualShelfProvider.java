package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import dev.shadowsoffire.apothic_enchanting.table.EnchantingStatRegistry;
import io.github.heatwaveqc.tmm.economy.state.VirtualShelfState;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import dev.shadowsoffire.apothic_enchanting.api.EnchantmentStatBlock;

/**
 * Conservative exact-pin resolver for ordinary data-backed blocks and vanilla
 * candles. Unknown state/world/block-entity providers require a faithful
 * adapter, not a fake world or sampled unrelated block position.
 */
public final class VirtualShelfProvider {
    private VirtualShelfProvider() {}

    public static Optional<VirtualShelfStats.Position> resolve(ItemStack stack) {
        if (!EnchantingFeatureGate.enabled() || stack.isEmpty() ||
            !(stack.getItem() instanceof BlockItem item)) return Optional.empty();
        Block block = item.getBlock();
        try {
            // Exact 1.6.4: native stat methods prefer the reloaded map over block fallbacks.
            Field field = EnchantingStatRegistry.class.getDeclaredField("statsPerBlock");
            if (!field.trySetAccessible()) return Optional.empty();
            Object raw = field.get(EnchantingStatRegistry.INSTANCE);
            if (!(raw instanceof Map<?, ?> map)) return Optional.empty();
            Object value = map.get(block);
            if (VirtualShelfState.vanillaCandle(stack)) {
                if (stack.getCount() < 1 || stack.getCount() > 4) return Optional.empty();
                var state = block.defaultBlockState().setValue(CandleBlock.CANDLES, stack.getCount());
                // Exact CandleBlockMixin reads only CANDLES; data-backed override has precedence.
                // Only the vanilla candle implementation can use a context-free fallback.
                if (value instanceof EnchantingStatRegistry.Stats stats)
                    return eligible(stats);
                if (!(block instanceof EnchantmentStatBlock candle)) return Optional.empty();
                var stats = new EnchantingStatRegistry.Stats(
                    candle.getMaxEnchantingPower(state, null, null), 0,
                    candle.getQuantaBonus(state, null, null), candle.getArcanaBonus(state, null, null),
                    candle.getBonusClues(state, null, null));
                return eligible(stats);
            }
            // Special flags/blacklists and arbitrary state-dependent stat callbacks
            // need explicit parity review. Do not silently discard their effects.
            if (block instanceof EnchantmentStatBlock) return Optional.empty();
            if (!(value instanceof EnchantingStatRegistry.Stats stats) || stack.getCount() != 1)
                return Optional.empty();
            return eligible(stats);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            return Optional.empty();
        }
    }

    private static Optional<VirtualShelfStats.Position> eligible(EnchantingStatRegistry.Stats stats) {
        if (stats.eterna() == 0 && stats.quanta() == 0 && stats.arcana() == 0 && stats.clues() == 0)
            return Optional.empty();
        return Optional.of(new VirtualShelfStats.Position(stats, Set.of(), false, false));
    }
}
