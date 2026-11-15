package io.github.heatwaveqc.tmm.transformations.botania;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ThermalilyLavaPolicyTest {
    @Test void emptyAndReadyConsumes() {
        assertTrue(ThermalilyLavaPolicy.mayCollectNewLava(0, 750, 0, 0));
    }
    @Test void fullManaDoesNotConsume() {
        assertFalse(ThermalilyLavaPolicy.mayCollectNewLava(750, 750, 0, 0));
    }
    @Test void cooldownWithRoomDoesNotConsume() {
        assertFalse(ThermalilyLavaPolicy.mayCollectNewLava(0, 750, 0, 240));
    }
    @Test void runningBurnDoesNotConsumeAnotherSource() {
        assertFalse(ThermalilyLavaPolicy.mayCollectNewLava(0, 750, 100, 0));
    }
    @Test void resumesExactlyWhenCooldownExpires() {
        assertTrue(ThermalilyLavaPolicy.mayCollectNewLava(0, 750, 0, 0));
    }
}
