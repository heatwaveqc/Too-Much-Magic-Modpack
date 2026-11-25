package io.github.heatwaveqc.tmm.economy;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.EnchantingFeatureGate;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

final class EnchantingFeatureGateTest {
    @Test void portableAnvilAlsoRequiresReviewedOccultism() {
        var mods = List.of("improvableskills", "apotheosis", "apothic_enchanting", "occultism");
        for (int mask = 0; mask < 16; mask++) {
            var present = new HashSet<String>();
            for (int bit = 0; bit < mods.size(); bit++)
                if ((mask & (1 << bit)) != 0) present.add(mods.get(bit));
            assertEquals(mask == 15, EnchantingFeatureGate.portableAnvilEnabled(present::contains, true));
            assertEquals(false, EnchantingFeatureGate.portableAnvilEnabled(present::contains, false));
        }
    }

    @Test void onlyAllRequiredProvidersEnableTheOptionalFeature() {
        var mods = List.of("improvableskills", "apotheosis", "apothic_enchanting");
        for (int mask = 0; mask < 8; mask++) {
            var present = new HashSet<String>();
            for (int bit = 0; bit < mods.size(); bit++)
                if ((mask & (1 << bit)) != 0) present.add(mods.get(bit));
            assertEquals(mask == 7, EnchantingFeatureGate.enabled(present::contains), present.toString());
        }
    }
}
