package io.github.heatwaveqc.tmm.transformations.dracolich;

import static org.junit.jupiter.api.Assertions.*;
import io.github.heatwaveqc.tmm.transformations.network.DracolichVisualState;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class DracolichAppearancePolicyTest {
    private boolean eligible(boolean dragon, boolean goety, boolean forbidden, boolean verified) {
        return DracolichAppearancePolicy.eligible(dragon, goety, forbidden, verified,
                "dragonsurvival:cave_dragon", "dragonsurvival:center", "dragonsurvival:dragon_model");
    }

    @AfterEach void clean() { DracolichVisualState.clear(); }

    @Test void bothLegitimateProvidersWorkButOrdinaryAndConflictingFormsDoNot() {
        assertTrue(eligible(true, true, false, true));
        assertTrue(eligible(true, false, true, true));
        assertFalse(eligible(true, false, false, true));
        assertFalse(eligible(true, true, true, true));
        assertFalse(eligible(false, true, false, true));
        assertFalse(eligible(true, true, false, false));
    }

    @Test void onlyNativeCaveCenterIsSupported() {
        assertTrue(DracolichAppearancePolicy.supports("dragonsurvival:cave_dragon", "dragonsurvival:center", "dragonsurvival:dragon_model"));
        for (String body : new String[]{"east", "north", "south", "west"}) {
            assertFalse(DracolichAppearancePolicy.supports("dragonsurvival:cave_dragon", "dragonsurvival:"+body, "dragonsurvival:dragon_model"));
        }
        for (String species : new String[]{"sea_dragon", "forest_dragon"}) {
            assertFalse(DracolichAppearancePolicy.supports("dragonsurvival:"+species, "dragonsurvival:center", "dragonsurvival:dragon_model"));
        }
        assertFalse(DracolichAppearancePolicy.supports("dragonsurvival:cave_dragon", "dragonsurvival:center", "addon:custom_dragon"));
    }

    @Test void replicatedStateIsIndependentAndCureAndDisconnectRemoveIt() {
        UUID first = UUID.randomUUID(), second = UUID.randomUUID();
        DracolichVisualState.apply(first, true);
        assertTrue(DracolichVisualState.active(first));
        assertFalse(DracolichVisualState.active(second));
        DracolichVisualState.apply(second, true);
        DracolichVisualState.apply(first, false);
        assertFalse(DracolichVisualState.active(first));
        assertTrue(DracolichVisualState.active(second));
        DracolichVisualState.clear();
        assertFalse(DracolichVisualState.active(second));
    }
}
