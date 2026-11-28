package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.Optional;

/** Four ordered vanilla dye IDs packed into an unsigned 16-bit value.
 * No texture generation, client dependency, arbitrary RGB or unbounded list. */
public record TartanPalette(int packed) {
    public static final int EMPTY = -1;
    public static final int INVALID = -2;
    public TartanPalette {
        if (packed < 0 || packed > 0xffff)
            throw new IllegalArgumentException("Tartan palette must contain exactly four dye IDs");
    }
    public static TartanPalette of(int base, int broad, int secondary, int accent) {
        for (int dye : new int[]{base, broad, secondary, accent})
            if (dye < 0 || dye > 15) throw new IllegalArgumentException("Invalid vanilla dye ID: " + dye);
        return new TartanPalette(base | broad << 4 | secondary << 8 | accent << 12);
    }
    public int dye(int channel) {
        if (channel < 0 || channel >= 4) throw new IllegalArgumentException("Invalid tartan channel");
        return (packed >>> (channel * 4)) & 15;
    }
    /** Strict WWW/.W. recipe; accepts vertical translation in a vanilla 3x3 grid
     * or the cropped 3x2 CraftingInput. No extras, shapeless matches or color sorting. */
    public static Optional<TartanPalette> match(int width, int height, int[] cells) {
        if (width != 3 || height < 2 || height > 3 || cells == null || cells.length != width * height)
            return Optional.empty();
        for (int row = 0; row <= height - 2; row++) {
            int start = row * width;
            boolean valid = true;
            for (int i = 0; i < cells.length; i++) {
                boolean wool = i == start || i == start + 1 || i == start + 2 || i == start + 4;
                if (wool ? cells[i] < 0 || cells[i] > 15 : cells[i] != EMPTY) {
                    valid = false;
                    break;
                }
            }
            if (valid) return Optional.of(of(cells[start], cells[start + 1], cells[start + 2], cells[start + 4]));
        }
        return Optional.empty();
    }
}
