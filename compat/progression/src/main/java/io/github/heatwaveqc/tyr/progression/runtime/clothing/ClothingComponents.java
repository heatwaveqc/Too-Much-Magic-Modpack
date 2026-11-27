package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import com.mojang.serialization.Codec;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.foundation.TartanPalette;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Server-owned immutable palette; persistent and network codecs share the same bounds. */
public final class ClothingComponents {
    private ClothingComponents() {}
    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, ProgressionMod.MOD_ID);
    public static final Codec<TartanPalette> PALETTE_CODEC =
            Codec.intRange(0, 65535).xmap(TartanPalette::new, TartanPalette::packed);
    public static final Supplier<DataComponentType<TartanPalette>> TARTAN_PALETTE =
            COMPONENTS.registerComponentType("tartan_palette", builder -> builder
                    .persistent(PALETTE_CODEC)
                    .networkSynchronized(ByteBufCodecs.INT.map(TartanPalette::new, TartanPalette::packed)));
}
