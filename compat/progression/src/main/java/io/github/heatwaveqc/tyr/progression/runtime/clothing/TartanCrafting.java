package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import io.github.heatwaveqc.tyr.progression.foundation.TartanPalette;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;

/** Shared server-side input adapter for the dynamic kilt recipe and its preview.
 * The order is vanilla DyeColor ID order, not RGB, registry ID or localization.
 * Exact vanilla wool items only; unknown tagged blocks cannot inject colors. */
public final class TartanCrafting {
    private TartanCrafting() {}
    private static final List<Item> WOOL = List.of(
            Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL,
            Items.YELLOW_WOOL, Items.LIME_WOOL, Items.PINK_WOOL, Items.GRAY_WOOL,
            Items.LIGHT_GRAY_WOOL, Items.CYAN_WOOL, Items.PURPLE_WOOL, Items.BLUE_WOOL,
            Items.BROWN_WOOL, Items.GREEN_WOOL, Items.RED_WOOL, Items.BLACK_WOOL);

    public static int dyeId(ItemStack stack) {
        if (stack.isEmpty()) return TartanPalette.EMPTY;
        int id = WOOL.indexOf(stack.getItem());
        return id < 0 ? TartanPalette.INVALID : id;
    }

    public static Optional<TartanPalette> palette(CraftingInput input) {
        int[] cells = new int[input.size()];
        for (int slot = 0; slot < cells.length; slot++) cells[slot] = dyeId(input.getItem(slot));
        return TartanPalette.match(input.width(), input.height(), cells);
    }
}
