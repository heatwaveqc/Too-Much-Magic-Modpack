package io.github.heatwaveqc.tmm.economy;

import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

/**
 * Prevents one-bottle consumers from destroying an eight-charge Neo Vitae
 * flask by treating its mirrored POTION_CONTENTS as a disposable potion.
 *
 * <p>This does not prohibit native flask drinking, throwing, rinsing, or
 * dedicated Neo Vitae flask recipes. Do not expand to other modded carriers
 * without reviewing their native dose and remainder contracts.</p>
 */
public final class NeoFlaskDoseGuard {
    private static final Set<String> MULTI_USE_FLASKS = Set.of(
            "neovitae:alchemy_flask",
            "neovitae:alchemy_flask_throwable",
            "neovitae:alchemy_flask_lingering"
    );

    private NeoFlaskDoseGuard() {}

    public static boolean isMultiUseFlaskId(String itemId) {
        return MULTI_USE_FLASKS.contains(itemId);
    }

    public static boolean mustRefuseOneBottleConversion(ItemStack stack) {
        return !stack.isEmpty()
                && isMultiUseFlaskId(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
    }
}
