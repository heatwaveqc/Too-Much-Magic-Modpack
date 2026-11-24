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
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import dev.shadowsoffire.apothic_enchanting.objects.TreasureShelfBlock;
import dev.shadowsoffire.apothic_enchanting.objects.GeodeShelfBlock;
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
            if (stack.getCount() != 1) return Optional.empty();
            // Exact native constant-only providers; their bytecode does not read world/pos.
            // Honor data overrides for numeric stats while retaining native flag semantics.
            if (block.getClass() == TreasureShelfBlock.class || block.getClass() == GeodeShelfBlock.class) {
                var nativeBlock = (EnchantmentStatBlock) block;
                var state = block.defaultBlockState();
                var stats = value instanceof EnchantingStatRegistry.Stats override ? override
                    : new EnchantingStatRegistry.Stats(
                        nativeBlock.getMaxEnchantingPower(state, null, null),
                        state.getEnchantPowerBonus(null, null) * 2,
                        nativeBlock.getQuantaBonus(state, null, null),
                        nativeBlock.getArcanaBonus(state, null, null),
                        nativeBlock.getBonusClues(state, null, null));
                return eligible(stats).map(pos -> new VirtualShelfStats.Position(pos.stats(),
                    Set.of(), nativeBlock.allowsTreasure(state, null, null),
                    nativeBlock.providesStability(state, null, null)));
            }
            // Apothic BlockMixin adds EnchantmentStatBlock to ALL blocks.
            // For any data-backed block, only the three special callbacks can
            // add state/world-dependent effects beyond the live numeric record.
            // Permit inherited native defaults; refuse custom overrides until reviewed.
            if (!(value instanceof EnchantingStatRegistry.Stats stats)) return Optional.empty();
            if (!defaultCallback(block, "getBlacklistedEnchantments")
                || !defaultCallback(block, "allowsTreasure")
                || !defaultCallback(block, "providesStability")) return Optional.empty();
            return eligible(stats);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            return Optional.empty();
        }
    }

    private static boolean defaultCallback(Block block, String method) throws NoSuchMethodException {
        return block.getClass().getMethod(method, BlockState.class, LevelReader.class, BlockPos.class)
            .getDeclaringClass() == EnchantmentStatBlock.class;
    }

    private static Optional<VirtualShelfStats.Position> eligible(EnchantingStatRegistry.Stats stats) {
        if (stats.eterna() == 0 && stats.quanta() == 0 && stats.arcana() == 0 && stats.clues() == 0)
            return Optional.empty();
        return Optional.of(new VirtualShelfStats.Position(stats, Set.of(), false, false));
    }
}
