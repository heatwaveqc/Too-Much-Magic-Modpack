package io.github.heatwaveqc.tmm.monk.rules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Design-only serialized Tyz's Skills five-stage 99-rank test.
 * Source-conforming inactive JSON is NOT installed gameplay or a
 * substitute for cancellable server-side purchase/refund hooks.
 */
final class MonkTyzTierPolicyTest {
    private static JsonObject load(String path) throws Exception {
        try (var reader = Files.newBufferedReader(Path.of(path))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test void fiveDisabledCustomStrengthStagesHaveValidLinearValueAndRisingSpLists() throws Exception {
        var policy = load("profiles/tyzs-tiered-skills-v1.json");
        var stageModel = policy.getAsJsonObject("stage_model");
        assertEquals("tyzs_skills", policy.get("mod_id").getAsString());
        assertEquals("6.4.2", policy.get("version").getAsString());
        assertEquals(99, stageModel.get("max_ranks_per_stage").getAsInt());
        assertEquals(5, stageModel.get("stage_count_default").getAsInt());
        assertEquals(10, stageModel.get("stage_multiplier").getAsInt());
        assertTrue(stageModel.get("sixth_stage_optional").getAsBoolean());
        assertFalse(policy.get("prototype_active").getAsBoolean());

        long totalBenefit = 0;
        long totalPrice = 0;
        int previousPrice = 0;
        int factor = 1;
        Set<String> ids = new HashSet<>();
        for (int tier = 1; tier <= 5; tier++) {
            String id = "tmm_strength_" + tier;
            var skill = load("tyzs-prototypes/config/tyzs_skills/custom/skills/" + id + ".json");
            assertEquals(id, skill.get("id").getAsString());
            assertTrue(ids.add(id));
            assertFalse(skill.get("active").getAsBoolean(), "Prototype must stay disabled");
            assertEquals("CUSTOM", skill.get("type").getAsString());
            assertEquals("combat", skill.get("category").getAsString());
            assertEquals(99, skill.get("maximumLevel").getAsInt());
            JsonArray prices = skill.getAsJsonArray("prices");
            JsonArray modifiers = skill.getAsJsonArray("modifiers");
            assertEquals(99, prices.size());
            assertEquals(1, modifiers.size());
            var modifier = modifiers.get(0).getAsJsonObject();
            assertEquals("minecraft:generic.attack_damage", modifier.get("attribute").getAsString());
            assertEquals("ADD_VALUE", modifier.get("operation").getAsString());
            JsonArray values = modifier.getAsJsonArray("values");
            assertEquals(99, values.size());

            var prerequisites = skill.getAsJsonObject("config").getAsJsonArray("skillPrerequisites");
            assertEquals(tier == 1 ? 0 : 1, prerequisites.size());
            if (tier > 1) assertEquals("tmm_strength_" + (tier - 1),
                    prerequisites.get(0).getAsString());

            for (int rank = 1; rank <= 99; rank++) {
                int price = prices.get(rank - 1).getAsInt();
                int effect = values.get(rank - 1).getAsInt();
                assertEquals(factor * (1 + (rank - 1) / 11), price);
                assertEquals(rank * factor, effect);
                assertTrue(price >= previousPrice, "Price must not drop at a rank or stage boundary");
                previousPrice = price;
                totalPrice += price;
            }
            totalBenefit += values.get(98).getAsInt();
            factor *= 10;
        }
        assertEquals(5, ids.size());
        assertEquals(1_099_989L, totalBenefit);
        assertEquals(5_499_945L, totalPrice);
        assertEquals(totalBenefit, stageModel.get("total_at_99_all_five_for_strength").getAsLong());
        assertEquals(totalPrice, stageModel.get("strength_example_total_SP_for_all_495").getAsLong());
    }

    @Test void factionAndRefundGatesAreRequiredButNotYetImplemented() throws Exception {
        var policy = load("profiles/tyzs-tiered-skills-v1.json");
        var gates = policy.getAsJsonObject("eligibility");
        assertTrue(gates.get("stage_unlock").getAsString().contains("99"));
        assertTrue(gates.get("purchase_guard").getAsString().contains("PurchasePre"));
        assertTrue(gates.get("refund_guard").getAsString().contains("RefundPre"));
        assertTrue(gates.get("admin_bypass").getAsString().contains("setSkillLevel"));
        assertTrue(gates.get("respec_policy").getAsString().contains("suspended"));
        assertFalse(policy.getAsJsonObject("stage_model").get("sp_currency").getAsString()
                .toLowerCase().contains("vanilla xp"));
    }

    @Test void selectedTyzModIsApprovedButNotYetDeclaredInstalled() throws Exception {
        var manifest = load("../../manifest/mods.json");
        JsonObject tyz = null;
        for (var item : manifest.getAsJsonArray("mods")) {
            var row = item.getAsJsonObject();
            if ("tyzs-skills".equals(row.get("id").getAsString())) tyz = row;
        }
        assertNotNull(tyz);
        assertTrue(tyz.get("desired").getAsBoolean());
        assertFalse(tyz.get("enabled_in_pack").getAsBoolean());
        assertEquals("verified_release", tyz.get("release_status").getAsString());
        assertEquals(8118598, tyz.get("candidate_file_id").getAsInt());
        assertEquals(990190, tyz.get("curseforge_project_id").getAsInt());
    }
}
