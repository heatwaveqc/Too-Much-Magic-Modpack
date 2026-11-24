package io.github.heatwaveqc.tyr.progression.runtime.external;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** Presentation-only projection of the existing external receipt/offer.
 * No extra Skill or paid-rank ownership, and no authority delegated to client. */
public final class ExternalRewardNode {
    private ExternalRewardNode() {}

    public static JsonObject project(ExternalRewardPolicy.Offer offer, int owned,
                                     int price, boolean buyable) {
        var row = new JsonObject();
        row.addProperty("id", offer.id());
        row.addProperty("external_reward", true);
        row.addProperty("title", "Astral Mastery");
        row.addProperty("description", "Commit one Progression skill point per additional native Astral perk point. Permanent; unlocked at the configured native Astral level cap.");
        row.addProperty("lane", "Universal Magic");
        row.addProperty("owned", owned);
        row.addProperty("maximum", offer.cap());
        row.addProperty("required_rank", 0);
        row.addProperty("eligible", true);
        row.addProperty("auto", false);
        row.addProperty("currency", offer.currency());
        row.addProperty("permanent", true);
        row.addProperty("point_name", "Skill points");
        row.addProperty("level_system", "Astral mastery");
        row.addProperty("price", Math.max(0, price));
        row.addProperty("choice_group", "");
        row.addProperty("buyable", buyable && price > 0 && owned < offer.cap());
        var source = new JsonObject();
        source.addProperty("system", offer.currency());
        source.addProperty("max_ranks", offer.cap());
        source.addProperty("owned", owned);
        source.addProperty("price", Math.max(0, price));
        source.addProperty("buyable", row.get("buyable").getAsBoolean());
        var sources = new JsonArray();
        sources.add(source);
        row.add("purchase_sources", sources);
        row.add("requires", new JsonArray());
        row.add("requires_any", new JsonArray());
        row.add("milestones", new JsonArray());
        var effects = new JsonArray();
        effects.add("+1 native Astral perk point");
        row.add("effects", effects);
        return row;
    }
}
