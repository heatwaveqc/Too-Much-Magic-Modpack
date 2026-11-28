package io.github.heatwaveqc.traders.vengeance.network;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GolemVisualStateTest {
    private static final long NOW = 2_000_000_000L;

    @Test void idleOrInconsistentFiringDoesNotDeploy() {
        var idle = new GolemVisualState.Sample(0, 0, false, false, NOW);
        var inconsistent = new GolemVisualState.Sample(0, 0.4F, true, false, NOW);
        assertFalse(idle.freshDeploymentAt(NOW));
        assertFalse(inconsistent.freshFiringAt(NOW));
    }

    @Test void aggroDeploysBeforeSpinUpAndRemainsVisibleWhileNotFiring() {
        var aggro = new GolemVisualState.Sample(0, 0, false, true, NOW);
        assertTrue(aggro.freshDeploymentAt(NOW));
        assertFalse(aggro.freshFiringAt(NOW));
    }

    @Test void freshFiringRequiresDeploymentAndExpires() {
        var firing = new GolemVisualState.Sample(0, 0.4F, true, true, NOW);
        assertTrue(firing.freshFiringAt(NOW + 1_499_999_999L));
        assertFalse(firing.freshDeploymentAt(NOW + 1_500_000_000L));
        assertFalse(firing.freshFiringAt(NOW + 1_500_000_000L));
        assertFalse(firing.freshDeploymentAt(NOW - 1));
    }
}
