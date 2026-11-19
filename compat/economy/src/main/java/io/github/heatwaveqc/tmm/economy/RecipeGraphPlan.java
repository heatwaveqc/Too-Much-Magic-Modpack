package io.github.heatwaveqc.tmm.economy;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import moze_intel.projecte.api.nss.NSSItem;
import java.util.Map;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/** Builds complete, bounded sets of ordinary material alternatives before
 * contributing anything to the ProjectE graph. Reusable inputs are not debited. */
final class RecipeGraphPlan {
    private static final int MAX_ALTERNATIVES = 1024;
    private Set<Object2IntOpenHashMap<NormalizedSimpleStack>> plans = new LinkedHashSet<>();

    RecipeGraphPlan() { plans.add(new Object2IntOpenHashMap<>()); }

    void slot(Ingredient ingredient, int count, Predicate<ItemStack> reused,
              boolean returnsContainer, boolean knownCustom) {
        if (count <= 0 || ingredient.isEmpty() || (!knownCustom && ingredient.getCustomIngredient() != null))
            throw new IllegalArgumentException("invalid or unsupported custom ingredient");
        ItemStack[] options = ingredient.getItems();
        if (options.length == 0 || options.length > MAX_ALTERNATIVES)
            throw new IllegalArgumentException("unresolved or excessive ingredient choices");
        Set<Object2IntOpenHashMap<NormalizedSimpleStack>> choices = new LinkedHashSet<>();
        for (ItemStack option : options) {
            if (option.isEmpty() || !option.getComponentsPatch().isEmpty() || !ingredient.test(option.copyWithCount(count)))
                throw new IllegalArgumentException("component-sensitive or unmatched input");
            var delta = new Object2IntOpenHashMap<NormalizedSimpleStack>();
            if (!reused.test(option)) {
                add(delta, NSSItem.createItem(option), count);
                if (returnsContainer) {
                    ItemStack remainder = option.copyWithCount(1).getCraftingRemainingItem();
                    if (!remainder.isEmpty()) {
                        if (!remainder.getComponentsPatch().isEmpty())
                            throw new IllegalArgumentException("component-sensitive remainder");
                        add(delta, NSSItem.createItem(remainder), -Math.multiplyExact(count, remainder.getCount()));
                    }
                }
            }
            choices.add(delta);
        }
        combine(choices);
    }

    /** Alternatives expressed as real graph keys, including fluid amounts in mB. */
    void choice(Map<NormalizedSimpleStack, Integer> alternatives) {
        if (alternatives.isEmpty() || alternatives.size() > MAX_ALTERNATIVES)
            throw new IllegalArgumentException("unresolved or excessive graph alternatives");
        Set<Object2IntOpenHashMap<NormalizedSimpleStack>> choices = new LinkedHashSet<>();
        alternatives.forEach((key, count) -> {
            if (count <= 0) throw new IllegalArgumentException("nonpositive graph input");
            var delta = new Object2IntOpenHashMap<NormalizedSimpleStack>();
            delta.put(key, count.intValue());
            choices.add(delta);
        });
        combine(choices);
    }

    void scale(int batches) {
        if (batches <= 0) throw new IllegalArgumentException("nonpositive batch scale");
        Set<Object2IntOpenHashMap<NormalizedSimpleStack>> next = new LinkedHashSet<>();
        for (var prior : plans) {
            var scaled = new Object2IntOpenHashMap<NormalizedSimpleStack>();
            for (var entry : prior.object2IntEntrySet())
                scaled.put(entry.getKey(), Math.multiplyExact(entry.getIntValue(), batches));
            next.add(scaled);
        }
        plans = next;
    }

    private void combine(Set<Object2IntOpenHashMap<NormalizedSimpleStack>> choices) {
        Set<Object2IntOpenHashMap<NormalizedSimpleStack>> next = new LinkedHashSet<>();
        for (var prior : plans) for (var delta : choices) {
            var copy = new Object2IntOpenHashMap<NormalizedSimpleStack>(prior);
            for (var entry : delta.object2IntEntrySet()) add(copy, entry.getKey(), entry.getIntValue());
            next.add(copy);
            if (next.size() > MAX_ALTERNATIVES)
                throw new IllegalArgumentException("excessive distinct material alternatives");
        }
        plans = next;
    }

    void consumed(Ingredient item, int count, boolean returnsContainer) {
        slot(item, count, s -> false, returnsContainer, false);
    }

    private static void add(Object2IntOpenHashMap<NormalizedSimpleStack> map,
                            NormalizedSimpleStack key, int amount) {
        int net = Math.addExact(map.getInt(key), amount);
        if (net == 0) map.removeInt(key);
        else map.put(key, net);
    }

    int contribute(ResourceGraph graph, ItemStack output, ResourceAccounting.Cost cost) {
        if (output.isEmpty() || !output.getComponentsPatch().isEmpty())
            throw new IllegalArgumentException("empty or component-sensitive output");
        return contribute(graph, NSSItem.createItem(output), output.getCount(), cost);
    }

    int contribute(ResourceGraph graph, NormalizedSimpleStack key, int outputCount, ResourceAccounting.Cost cost) {
        if (outputCount <= 0) throw new IllegalArgumentException("invalid output count");
        int emc = cost.emcUnits();
        cost.scaleCount(outputCount);
        // Validate all alternatives before registering them (including remainder debits).
        for (var plan : plans) {
            if (plan.containsKey(key)) throw new IllegalArgumentException("self-referential recipe");
            boolean positive = false;
            for (int count : plan.values()) {
                cost.scaleCount(count);
                if (count > 0) positive = true;
            }
            if (!positive && emc == 0) throw new IllegalArgumentException("no priced consumption");
        }
        for (var plan : plans) graph.add(outputCount, key, plan, cost);
        return plans.size();
    }
}
