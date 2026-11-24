package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authoritative, atomically reloaded server diet rules. Datapacks override
 * data/tyr_progression/tyr_progression/food_policies/default.json; item tags
 * are resolved from current registries on each lookup, never snapshotted to
 * a static item-ID whitelist.
 */
public final class FoodPolicyRegistry extends SimpleJsonResourceReloadListener {
    private static volatile FoodPolicyRules active;

    public FoodPolicyRegistry() { super(new Gson(), "tyr_progression/food_policies"); }

    public static Optional<FoodPolicyRules> current() { return Optional.ofNullable(active); }

    public static Set<String> tags(ItemStack stack) {
        return stack.getTags().map(tag -> "#" + tag.location()).collect(Collectors.toUnmodifiableSet());
    }

    /** Non-thermal source units and optional composition supplied by provenance. */
    public static Optional<FoodPolicyRules.Nutrition> evaluate(
            String profile, ItemStack stack, double rawSourceUnits, Double preferredShare) {
        FoodPolicyRules rules = active;
        return rules == null || stack.isEmpty() ? Optional.empty()
            : rules.evaluate(profile, tags(stack), rawSourceUnits, preferredShare);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data,
                         ResourceManager manager, ProfilerFiller profiler) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("tyr_progression", "default");
        JsonElement raw = data.get(id);
        if (raw == null) throw new IllegalArgumentException("Missing bundled food policy " + id);
        FoodPolicyRules next = FoodPolicyRules.parse(raw.getAsJsonObject());
        active = next; // publish only after complete validation
        ProgressionMod.LOGGER.info("Loaded tag-driven food policy schema {} with {} physiologies",
            next.schema(), next.profiles().size());
    }
}
