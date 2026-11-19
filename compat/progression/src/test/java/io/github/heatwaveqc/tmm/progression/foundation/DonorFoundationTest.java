package io.github.heatwaveqc.tmm.progression.foundation;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DonorFoundationTest {
    @Test void comparisonsAreStrictAndPreserveIntegerPrecision() {
        assertTrue(Comparison.GREATER.test(Long.MAX_VALUE, Long.MAX_VALUE - 1));
        assertFalse(Comparison.EQUAL.test(Long.MAX_VALUE, Long.MAX_VALUE - 1));
        assertThrows(IllegalArgumentException.class, () -> Comparison.fromKey("typo"));
    }
    @Test void unavailableProviderDoesNotBecomeTrueByNegation() {
        Condition<String> yes = c -> Condition.Result.PASS;
        Condition<String> no = c -> Condition.Result.FAIL;
        Condition<String> missing = c -> Condition.Result.UNAVAILABLE;
        assertFalse(Condition.not(missing).test(""));
        assertEquals(Condition.Result.UNAVAILABLE, Condition.and(List.of(yes, missing)).evaluate(""));
        assertEquals(Condition.Result.UNAVAILABLE, Condition.or(List.of(no, missing)).evaluate(""));
        assertTrue(Condition.or(List.of(yes, missing)).test(""));
        assertFalse(Condition.and(List.of(yes, no)).test(""));
        assertThrows(IllegalArgumentException.class, () -> Condition.and(List.<Condition<String>>of()));
    }
    @Test void sourcesDistinguishDifferentOwnershipKinds() {
        assertNotEquals(new RewardSource("skill", "tmm:node", "effect/1"), new RewardSource("augment", "tmm:node", "effect/1"));
        assertThrows(IllegalArgumentException.class, () -> new RewardSource("skill", "malformed", "fx"));
    }
    @Test void layeredUiUsesOnlyExplicitEdges() {
        var nodes = List.of(new TreeLayout.Node("a",0), new TreeLayout.Node("b",0), new TreeLayout.Node("c",1));
        var layout = TreeLayout.arrange(nodes,List.of(new TreeLayout.Edge("a","c")),20,10);
        assertEquals(new TreeLayout.Point(15,30), layout.positions().get("c"));
        assertEquals(3, layout.lines().size());
        assertTrue(TreeLayout.arrange(nodes,List.of(),20,10).lines().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> TreeLayout.arrange(nodes,List.of(new TreeLayout.Edge("c","a")),20,10));
    }
    @Test void augmentTogglePreservesInstallation() {
        assertEquals(AugmentState.DISABLED, AugmentState.ENABLED.toggle(true));
        assertEquals(AugmentState.ENABLED, AugmentState.DISABLED.toggle(true));
        assertEquals(AugmentState.NOT_INSTALLED, AugmentState.NOT_INSTALLED.toggle(true));
        assertEquals(AugmentState.ENABLED, AugmentState.ENABLED.toggle(false));
    }
    @Test void magnetAndAttributeCapMath() {
        assertEquals(new MagnetMotion.Velocity(-0.5, -0.0, -0.0), MagnetMotion.toward(8,0,0,false));
        assertEquals(new MagnetMotion.Velocity(0,0,0), MagnetMotion.toward(0,0,0,false));
        assertEquals(0.3, MagnetMotion.toward(8,0,0,true).y());
        assertEquals(10, AttributeCap.suppressBonuses(15,10));
        assertEquals(7, AttributeCap.suppressBonuses(7,10));
    }
    @Test void visionMathPreservesVanillaWhenDisabled() {
        assertTrue(VisionMath.hasNightVision(true,false));
        assertFalse(VisionMath.hasNightVision(false,false));
        assertTrue(VisionMath.hasNightVision(false,true));
        assertEquals(0.4f,VisionMath.nightVisionScale(0.4f,false));
        assertEquals(1f,VisionMath.nightVisionScale(0.4f,true));
        assertEquals(new VisionMath.Fog(-4f,20f),VisionMath.lavaFog(20f));
        assertThrows(IllegalArgumentException.class, () -> VisionMath.lavaFog(Float.NaN));
    }
}
