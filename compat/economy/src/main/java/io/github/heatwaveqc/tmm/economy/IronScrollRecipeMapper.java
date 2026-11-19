package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.item.InkItem;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ModTags;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;

/**
 * Iron's 3.16.3 Scroll Forge and Arcane Anvil. These are native menu
 * operations, not a RecipeManager recipe type. DO NOT activate before
 * burn-only output enforcement and actual-burn knowledge routing are verified.
 */
@EMCMapper(requiredMods = {"irons_spellbooks"})
public final class IronScrollRecipeMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    // Deliberately not a config option. Positive exact item mappings otherwise
    // become copyable through full knowledge, condensers and Expansion.
    private static final boolean BURN_ONLY_POLICY_ENFORCED = false;
    private static final int MAX_LEVELS = 1024;
    @Override public String getName() { return "TMM Iron Scroll Recipe Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.iron_scrolls"; }
    @Override public String getDescription() { return "Staged scroll valuation, disabled until burn-only transaction enforcement."; }
    @Override public boolean isAvailable() { return BURN_ONLY_POLICY_ENFORCED; }

    private record Route(NSSItem output, Object2IntOpenHashMap<NormalizedSimpleStack> consumed) {}
    static ItemStack canonical(AbstractSpell spell, int level) {
        ItemStack stack = new ItemStack(ItemRegistry.SCROLL.get());
        ISpellContainer.createScrollContainer(spell, level, stack);
        return stack;
    }

    private static Route route(AbstractSpell spell, int level, NSSItem... inputs) {
        var consumed = new Object2IntOpenHashMap<NormalizedSimpleStack>();
        for (var input : inputs) consumed.addTo(input, 1);
        return new Route(NSSItem.createItem(canonical(spell, level)), consumed);
    }

    private static List<Route> plan(AbstractSpell spell, List<Item> focuses,
                                    Map<SpellRarity, List<Item>> inks, boolean merge) {
        int minimum = spell.getMinLevel(), maximum = spell.getMaxLevel();
        if (minimum < 1 || maximum < minimum || maximum > MAX_LEVELS)
            throw new IllegalArgumentException("invalid/unbounded spell level range");
        List<Route> routes = new ArrayList<>();
        TreeSet<Integer> reachable = new TreeSet<>();
        var paper = NSSItem.createItem(Items.PAPER);
        for (var entry : inks.entrySet()) {
            var rarity = entry.getKey();
            if (rarity.getValue() < spell.getMinRarity() || rarity.getValue() > spell.getMaxRarity()) continue;
            int level = spell.getMinLevelForRarity(rarity);
            if (level < minimum || level > maximum) continue;
            for (Item focus : focuses) for (Item ink : entry.getValue()) {
                // ScrollForgeMenu.onTake removes one of each and returns no remainder.
                routes.add(route(spell, level, paper, NSSItem.createItem(ink), NSSItem.createItem(focus)));
                reachable.add(level);
            }
        }
        if (merge) {
            for (int level = minimum + 1; level <= maximum; level++) {
                if (!reachable.contains(level - 1)) continue;
                var required = inks.getOrDefault(spell.getRarity(level), List.of());
                for (Item ink : required) {
                    routes.add(route(spell, level,
                            NSSItem.createItem(canonical(spell, level - 1)), NSSItem.createItem(ink)));
                    reachable.add(level);
                }
            }
        }
        if (reachable.size() != maximum - minimum + 1)
            LOG.info("TMM Iron scroll mapper {}: {} of {} levels have a native crafting route",
                    spell.getSpellResource(), reachable.size(), maximum - minimum + 1);
        return routes;
    }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        if (!BURN_ONLY_POLICY_ENFORCED) {
            LOG.warn("TMM Iron scroll mapping held: burn-only transaction gate is not verified");
            return;
        }
        // Invalid blank scrolls are not a purchasable or burnable material.
        var blank = NSSItem.createItem(ItemRegistry.SCROLL.get());
        mapper.setValueBefore(blank, 0L);
        mapper.setValueAfter(blank, 0L);
        Map<SpellRarity, List<Item>> inks = new EnumMap<>(SpellRarity.class);
        List<Item> allFocus = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack stack = new ItemStack(item);
            if (item instanceof InkItem ink && !stack.isEmpty())
                inks.computeIfAbsent(ink.getRarity(), ignored -> new ArrayList<>()).add(item);
            if (!stack.isEmpty() && stack.is(ModTags.SCHOOL_FOCUS)) allFocus.add(item);
        }
        boolean merge = ServerConfigs.SCROLL_MERGING.get();
        int spells = 0, conversions = 0, held = 0;
        for (AbstractSpell spell : SpellRegistry.REGISTRY) {
            if (spell == SpellRegistry.none() || !spell.isEnabled()) continue;
            if (!spell.allowCrafting()) {
                held++; LOG.info("TMM Iron scroll mapper held nonforgeable spell {}", spell.getSpellResource());
                continue;
            }
            try {
                var focuses = allFocus.stream().filter(item ->
                        SchoolRegistry.getSchoolsFromFocus(new ItemStack(item)).contains(spell.getSchoolType())).toList();
                if (focuses.isEmpty()) throw new IllegalArgumentException("no valid school focus");
                var planned = plan(spell, focuses, inks, merge);
                if (planned.isEmpty()) throw new IllegalArgumentException("no native valid scroll crafting route");
                for (var route : planned) {
                    mapper.addConversion(1, route.output(), route.consumed());
                    conversions++;
                }
                spells++;
            } catch (IllegalArgumentException | ArithmeticException | IndexOutOfBoundsException ex) {
                held++; LOG.warn("TMM Iron scroll mapper held {}: {}", spell.getSpellResource(), ex.getMessage());
            }
        }
        LOG.info("TMM Iron scroll mapper: {} spells, {} routes, {} held", spells, conversions, held);
    }
}
