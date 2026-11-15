package io.github.heatwaveqc.tmm.forms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tyzsskills.api.Enums;
import com.tyzsskills.api.events.SkillActionEvent;
import com.tyzsskills.api.events.TyzsSkillsCommonSetupEvent;
import com.tyzsskills.api.model.SkillConfiguration;
import com.tyzsskills.api.records.SkillPrefab;
import com.tyzsskills.api.records.ValueSet;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registers complete Tyz native prefab defaults from the one shared form catalog.
 *
 * Important: IMMUTABLE skills cannot be introduced as loose "custom" JSON files
 * in Tyz 6.4.2. They MUST have matching prefab registration before a custom
 * JSON override can turn them on. No form skill is enabled by default.
 */
public final class FormSkillRegistration {
    private FormSkillRegistration() {}

    public record Node(String id, String form, String gate, List<String> prerequisites,
                       List<String> serverPrerequisites) { }

    private static final Map<String, Node> NODES = new HashMap<>();

    public static void onSetup(TyzsSkillsCommonSetupEvent event) {
        try (var stream = FormSkillRegistration.class.getResourceAsStream("/tmm-form-skills.json")) {
            if (stream == null) throw new IllegalStateException("Missing packaged form-skill catalog");
            var root = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonArray skills = root.getAsJsonArray("skills");
            if (skills.size() != 37) throw new IllegalStateException("Unexpected form skill count");
            for (var element : skills) {
                JsonObject source = element.getAsJsonObject();
                String id = source.get("id").getAsString();
                String form = source.get("form").getAsString();
                String gate = source.get("externalGate").getAsString();
                int max = source.get("maximumLevel").getAsInt();
                List<Integer> prices = new ArrayList<>();
                for (var x : source.getAsJsonArray("prices")) prices.add(x.getAsInt());
                List<Float> values = new ArrayList<>();
                for (var x : source.getAsJsonArray("values")) values.add(x.getAsFloat());
                List<String> prereqs = strings(source.getAsJsonArray("skillPrerequisites"));
                List<String> serverPrereqs = source.has("additionalServerPrerequisites")
                        ? strings(source.getAsJsonArray("additionalServerPrerequisites")) : List.of();
                List<String> incompatible = strings(source.getAsJsonArray("incompatibleSkills"));
                if (!id.startsWith("tmm_" + form + "_") || prices.size() != max || values.size() != max
                        || !List.of("vampire", "werewolf").contains(form)) {
                    throw new IllegalStateException("Unsafe form-skill definition: " + id);
                }

                Node node = new Node(id, form, gate, List.copyOf(prereqs), List.copyOf(serverPrereqs));
                if (NODES.putIfAbsent(id, node) != null) throw new IllegalStateException("Duplicate form skill " + id);

                var prefab = new SkillPrefab(
                        false, // ALL inactive by default; test-gated overrides only after full backend approval.
                        id, max, prices, Enums.SkillType.IMMUTABLE, source.get("category").getAsString(),
                        form.equals("vampire") ? "minecraft:textures/item/fermented_spider_eye.png"
                                             : "minecraft:textures/item/bone.png",
                        source.get("displayName").getAsString(), source.get("description").getAsString(),
                        List.of(),
                        Map.of("magnitude", new ValueSet(values, source.get("unit").getAsString())),
                        null, // Effects are owned by separate server-authoritative JAWR-aware listeners.
                        new SkillConfiguration(null, incompatible, prereqs, true, true, true)
                );
                event.wrapper().registerSkill(prefab);
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Refusing unsafe Tyz form registration", ex);
        }
    }

    private static List<String> strings(JsonArray array) {
        List<String> out = new ArrayList<>();
        for (var element : array) out.add(element.getAsString());
        return out;
    }

    public static Node node(String skillId) { return NODES.get(skillId); }

    /**
     * Buying/refunding uses the Tyz shared SP wallet and its cancellable events.
     * Effect-time eligibility is separately enforced, including after cure.
     * Advanced stage requirements remain FAIL-CLOSED until native JAWR levels
     * are validated; direct /skills setSkillLevel still bypasses purchase events.
     */
    @SubscribeEvent
    public static void onPurchase(SkillActionEvent.PurchasePre event) {
        Node node = NODES.get(event.getSkill().getID());
        if (node == null) return;
        ServerPlayer p = event.getPlayer();
        if (!JawrFormState.present(p, node.form())) {
            event.setCanceled(true);
            return;
        }
        if (!"jawr_form_present".equals(node.gate())) {
            // Milestone thresholds are not invented from TeamLapen levels.
            event.setCanceled(true);
            return;
        }
        for (String other : node.serverPrerequisites()) {
            if (com.tyzsskills.api.TyzsSkillsAPI.skills().getSkillLevel(p, other) <= 0) {
                event.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onRefund(SkillActionEvent.RefundPre event) {
        var id = event.getSkill().getID();
        if (!NODES.containsKey(id)) return;
        var manager = com.tyzsskills.api.TyzsSkillsAPI.skills();
        int remainingAfterSingleRefund = Math.max(0, manager.getSkillLevel(event.getPlayer(), id) - 1);
        // Tyz invokes this same RefundPre event for bulk refunds. Therefore
        // fail closed when *any* descendant is still owned, even for a
        // hypothetical single-rank refund of a multi-rank parent.
        if (remainingAfterSingleRefund > 0) return;
        for (Node node : NODES.values()) {
            if ((node.prerequisites().contains(id) || node.serverPrerequisites().contains(id))
                    && manager.getSkillLevel(event.getPlayer(), node.id()) > 0) {
                event.setCanceled(true);
                return;
            }
        }
    }
}
