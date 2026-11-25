package io.github.heatwaveqc.tmm.economy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/** Pure exact-identity guard: no ambiguous #c:potions, namespace or PC tag. */
class NeoFlaskDoseGuardTest {
    @Test void protectsAllThreeEightUseNeoFlasks() {
        assertTrue(NeoFlaskDoseGuard.isMultiUseFlaskId("neovitae:alchemy_flask"));
        assertTrue(NeoFlaskDoseGuard.isMultiUseFlaskId("neovitae:alchemy_flask_throwable"));
        assertTrue(NeoFlaskDoseGuard.isMultiUseFlaskId("neovitae:alchemy_flask_lingering"));
    }

    @Test void doesNotSuppressRegularNativePotionUse() {
        for (String id : new String[] {
                "minecraft:potion", "minecraft:splash_potion",
                "minecraft:lingering_potion", "minecraft:suspicious_stew",
                "neovitae:guide_book", "irons_spellbooks:potion_fluid",
                "reliquary:potion", "ars_nouveau:potion_flask",
                "neovitae:alchemy_flask_fake"
        }) assertFalse(NeoFlaskDoseGuard.isMultiUseFlaskId(id), id);
    }
}
