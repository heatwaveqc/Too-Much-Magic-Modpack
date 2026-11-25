package io.github.heatwaveqc.tmm.economy.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualShelfLayout;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualCandlePacking;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CandleBlock;

/** Exact item escrow; one physical block per position, or up to four identical vanilla candles. */
public final class VirtualShelfState {
    public static final int SLOTS = VirtualShelfLayout.CAPACITY;
    public static final Codec<VirtualShelfState> CODEC = ItemStack.OPTIONAL_CODEC.listOf().comapFlatMap(
        list -> {
            try { return DataResult.success(new VirtualShelfState(list, 0)); }
            catch (IllegalArgumentException e) { return DataResult.error(e::getMessage); }
        }, VirtualShelfState::items);

    private final List<ItemStack> positions;
    private final long revision;
    private VirtualShelfState(List<ItemStack> stacks, long revision) {
        if (stacks.size() != SLOTS || revision < 0) throw new IllegalArgumentException("Invalid shelf state");
        var copy = new ArrayList<ItemStack>(SLOTS);
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty() && (!(stack.getItem() instanceof BlockItem) ||
                stack.getCount() > (vanillaCandle(stack) ? VirtualCandlePacking.CANDLES_PER_POSITION : 1)))
                throw new IllegalArgumentException("Invalid stored shelf position");
            copy.add(stack.copy());
        }
        this.positions = List.copyOf(copy);
        this.revision = revision;
    }

    public static VirtualShelfState empty() {
        return new VirtualShelfState(Collections.nCopies(SLOTS, ItemStack.EMPTY), 0);
    }
    public VirtualShelfState restoreRevision(long saved) { return new VirtualShelfState(positions, saved); }
    public long revision() { return revision; }
    public List<ItemStack> items() { return positions.stream().map(ItemStack::copy).toList(); }
    public ItemStack item(int slot) { return positions.get(slot).copy(); }

    public static boolean vanillaCandle(ItemStack stack) {
        return stack.getItem() instanceof BlockItem item && item.getBlock() instanceof CandleBlock
            && BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("minecraft");
    }

    /** One actual item at a time; native stat eligibility is checked by the server service. */
    public Optional<VirtualShelfState> depositOne(ItemStack source) {
        if (source.isEmpty() || !(source.getItem() instanceof BlockItem)) return Optional.empty();
        boolean candle = vanillaCandle(source);
        // A component-aware identity avoids conflating custom-named or modified candles.
        record Identity(ItemStack one) {
            @Override public boolean equals(Object other) {
                return other instanceof Identity id && ItemStack.isSameItemSameComponents(one, id.one);
            }
            @Override public int hashCode() { return 0; } // at most 32 comparisons, no hash collection
        }
        var layout = new ArrayList<VirtualShelfLayout.Occupied<Identity>>(SLOTS);
        for (ItemStack stack : positions) {
            layout.add(stack.isEmpty() ? null : new VirtualShelfLayout.Occupied<>(
                new Identity(stack.copyWithCount(1)), stack.getCount(), vanillaCandle(stack)));
        }
        var dest = VirtualShelfLayout.destination(layout, new Identity(source.copyWithCount(1)), candle);
        if (dest.isEmpty()) return Optional.empty();
        int index = dest.getAsInt();
        ItemStack old = positions.get(index);
        return Optional.of(replace(index, source.copyWithCount(old.isEmpty() ? 1 : old.getCount() + 1)));
    }

    public VirtualShelfState remove(int index) {
        if (positions.get(index).isEmpty()) return this;
        return replace(index, ItemStack.EMPTY);
    }
    private VirtualShelfState replace(int index, ItemStack stack) {
        var next = new ArrayList<>(items());
        next.set(index, stack.copy());
        return new VirtualShelfState(next, Math.incrementExact(revision));
    }
}
