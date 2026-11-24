package io.github.heatwaveqc.tyr.progression.state;

import io.github.heatwaveqc.tyr.progression.foundation.TooltipMilestonePolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class TooltipMilestonesTest {
    @Test void independentAndMonotonic() {
        TooltipMilestones first = TooltipMilestones.empty();
        assertFalse(first.projecte());
        assertFalse(first.replication());
        assertSame(first, first.discover(false, false));

        TooltipMilestones stone = first.discover(true, false);
        assertTrue(stone.projecte());
        assertFalse(stone.replication());
        assertSame(stone, stone.discover(false, false));
        assertSame(stone, stone.discover(true, false));

        TooltipMilestones both = stone.discover(false, true);
        assertTrue(both.projecte());
        assertTrue(both.replication());
        assertSame(both, both.discover(false, false));

        TooltipMilestones replicaFirst = first.discover(false, true);
        assertFalse(replicaFirst.projecte());
        assertTrue(replicaFirst.replication());
        assertEquals(both, replicaFirst.discover(true, false));
    }

    @Test void covalenceDeliveryIsOneTimeAndIndependent() {
        TooltipMilestones noStone = TooltipMilestones.empty().discover(false, true);
        assertFalse(noStone.covalencePending());
        TooltipMilestones pending = noStone.discover(true, false);
        assertTrue(pending.projecte());
        assertTrue(pending.replication());
        assertTrue(pending.covalencePending());
        assertSame(pending, pending.discover(false, false));
        TooltipMilestones delivered = pending.covalenceDelivered();
        assertFalse(delivered.covalencePending());
        assertSame(delivered, delivered.discover(true, true));
        assertSame(delivered, delivered.covalenceDelivered());
        assertFalse(new TooltipMilestones(true, false).discover(true, false).covalencePending());
    }

    @Test void numbersRequirePersonalNativeKnowledgeAndPreference() {
        assertFalse(TooltipMilestonePolicy.projecteNumeric(false, true, true));
        assertFalse(TooltipMilestonePolicy.projecteNumeric(true, false, true));
        assertFalse(TooltipMilestonePolicy.projecteNumeric(true, true, false));
        assertTrue(TooltipMilestonePolicy.projecteNumeric(true, true, true));
        assertFalse(TooltipMilestonePolicy.projecteLearnedIndicator(false, true));
        assertTrue(TooltipMilestonePolicy.projecteLearnedIndicator(true, true));
        assertFalse(TooltipMilestonePolicy.projecteLearnedIndicator(true, false));
    }

    @Test void replicationIndependentAndDiviningRodExempt() {
        assertFalse(TooltipMilestonePolicy.replicationMatter(false, true));
        assertFalse(TooltipMilestonePolicy.replicationMatter(true, false));
        assertTrue(TooltipMilestonePolicy.replicationMatter(true, true));
        assertTrue(TooltipMilestonePolicy.keepNativeDiviningRodReadout());
    }
}
