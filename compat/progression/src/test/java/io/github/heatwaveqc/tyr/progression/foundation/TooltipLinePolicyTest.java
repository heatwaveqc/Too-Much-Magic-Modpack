package io.github.heatwaveqc.tyr.progression.foundation;

import io.github.heatwaveqc.tyr.progression.foundation.TooltipLinePolicy.Kind;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Pure source-level regressions for exact translated provider tooltip roots. */
final class TooltipLinePolicyTest {
    @Test void allFourProjecteNumericalVariantsAreGated() {
        for (String key : Set.of(
                "emc.projecte.tooltip", "emc.projecte.tooltip.stack",
                "emc.projecte.tooltip.with_sell", "emc.projecte.tooltip.stack.with_sell")) {
            assertEquals(Kind.ITEM_EMC, TooltipLinePolicy.classify(key), key);
            assertTrue(TooltipLinePolicy.remove(Kind.ITEM_EMC, false, true, false));
            assertTrue(TooltipLinePolicy.remove(Kind.ITEM_EMC, true, false, false));
            assertFalse(TooltipLinePolicy.remove(Kind.ITEM_EMC, true, true, false));
        }
    }

    @Test void sourceOwnedLearnedIndicatorsDoNotLeakBeforeStone() {
        for (String key : Set.of(
                "emc.projecte.has_knowledge", "emc.projecte.no_knowledge",
                "text.projectexpansion.learned", "text.projectexpansion.not_learned")) {
            Kind kind = TooltipLinePolicy.classify(key);
            assertEquals(Kind.LEARNED_STATUS, kind, key);
            assertTrue(TooltipLinePolicy.remove(kind, false, false, true));
            assertFalse(TooltipLinePolicy.remove(kind, true, false, false));
        }
    }

    @Test void storedEmcRequiresOnlyStoneNotLearnedItem() {
        Kind kind = TooltipLinePolicy.classify("emc.projecte.stored");
        assertEquals(Kind.STORED_EMC, kind);
        assertTrue(TooltipLinePolicy.remove(kind, false, true, false));
        assertFalse(TooltipLinePolicy.remove(kind, true, false, false));
    }

    @Test void replicationPromptAndNativeGraphicBothUseSeparateIngotGate() {
        assertEquals(Kind.REPLICATION_HINT,
                TooltipLinePolicy.classify("tooltip.replication.matter_value"));
        assertTrue(TooltipLinePolicy.remove(Kind.REPLICATION_HINT, true, true, false));
        assertFalse(TooltipLinePolicy.remove(Kind.REPLICATION_HINT, false, false, true));
        assertTrue(TooltipLinePolicy.removeReplicationComponent(
                "com.buuz135.replication.client.MatterTooltipComponent", false));
        assertFalse(TooltipLinePolicy.removeReplicationComponent(
                "com.buuz135.replication.client.MatterTooltipComponent", true));
        assertFalse(TooltipLinePolicy.removeReplicationComponent("othermod:MatterTooltipComponent", false));
    }

    @Test void machineOperationAndJeiRecipeCostsAreNotItemValuations() {
        // Project Expansion's Jade provider projectexpansion:provider reports
        // stored EMC, generation, import/export limits and relay rates.
        // Replication's Jade matter_tank is a stored-tank readout.
        // ProjectE's JEI collector upgrade value is a recipe cost.
        // None is a personal per-item appraisal or a learned marker.
        for (String key : Set.of(
                "text.projectexpansion.emc_storage",
                "text.projectexpansion.emc_per_second",
                "text.projectexpansion.emc_import_limit",
                "text.projectexpansion.emc_transfer_rate",
                "text.projectexpansion.relay_bonus",
                "tooltip.replication.tank.amount",
                "tooltip.replication.tank.matter",
                "emc.projecte.emc")) {
            Kind kind = TooltipLinePolicy.classify(key);
            assertEquals(Kind.UNRELATED, kind, key);
            assertFalse(TooltipLinePolicy.remove(kind, false, false, false));
        }
    }

    @Test void neverStripOtherProjecteExpansionOrVanillaTooltipLines() {
        for (String key : Set.of(
                "pedestal.projecte.on_pedestal", "text.projectexpansion.alchemical_collection",
                "tooltip.projecte.philostone", "emc.projecte.balance",
                "text.projectexpansion.learned_extra", "tooltip.replication.tank.amount",
                "tooltip.replication.tank.matter", "item.minecraft.diamond", "EMC: 2048")) {
            Kind kind = TooltipLinePolicy.classify(key);
            assertEquals(Kind.UNRELATED, kind, key);
            assertFalse(TooltipLinePolicy.remove(kind, false, false, false));
        }
        assertEquals(Kind.UNRELATED, TooltipLinePolicy.classify(null));
    }
}
