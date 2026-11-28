package io.github.heatwaveqc.tyr.progression.foundation;

/** Three ordered vanilla dye IDs: primary, secondary and belt/trim.
 * Immutable and bounded; separate from the existing four-channel tartan ABI. */
public record GarmentPalette(int packed) {
    public GarmentPalette {
        if (packed < 0 || packed > 0xfff) throw new IllegalArgumentException("Invalid garment palette");
    }
    public static GarmentPalette of(int primary, int secondary, int trim) {
        for (int dye : new int[]{primary, secondary, trim})
            if (dye < 0 || dye > 15) throw new IllegalArgumentException("Invalid dye ID");
        return new GarmentPalette(primary | secondary << 4 | trim << 8);
    }
    public int dye(int channel) {
        if (channel < 0 || channel > 2) throw new IllegalArgumentException("Invalid channel");
        return (packed >>> (4 * channel)) & 15;
    }
}
