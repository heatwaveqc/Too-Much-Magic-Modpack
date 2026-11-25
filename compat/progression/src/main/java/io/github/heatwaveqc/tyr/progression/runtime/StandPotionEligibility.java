package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An eligible potion is an ordinary brewing-stand recipe RESULT, not merely an
 * ItemStack with POTION_CONTENTS. Creative-flight / ultra-long potions produced
 * by alternate machines must not become permanent Hunter mutations just by
 * passing through our Bio-Mutagen brewing recipe.
 *
 * Standard registered addMix recipes are read from PotionBrewing. For ordinary
 * NeoForge IBrewingRecipe recipes, we discover the concrete output signatures
 * from registered plain potion bottles and unmodified ingredient item stacks.
 * Results from custom machines or arbitrary POTION_CONTENTS are NOT admitted.
 */
public final class StandPotionEligibility {
    private StandPotionEligibility() {}

    private static volatile Set<String> standardMixPotions = Set.of();
    private static volatile Set<PotionContents> customRecipeOutputs = Set.of();

    public static void serverStarted(ServerStartedEvent event) {
        var brewing = event.getServer().overworld().potionBrewing();
        Set<String> allowed = new HashSet<>();
        List<ItemStack> regularBottles = new ArrayList<>();

        for (var potionId : BuiltInRegistries.POTION.keySet()) {
            var holder = BuiltInRegistries.POTION.getHolder(potionId).orElse(null);
            if (holder == null) continue;
            if (brewing.isBrewablePotion(holder)) allowed.add(potionId.toString());
            for (Item container : List.of(Items.POTION,Items.SPLASH_POTION,Items.LINGERING_POTION))
                regularBottles.add(PotionContents.createItemStack(container,holder));
        }

        Set<PotionContents> customOutputs = new HashSet<>();
        List<ItemStack> ingredientCandidates = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            if (item != Items.AIR) ingredientCandidates.add(new ItemStack(item));
        }

        for (IBrewingRecipe recipe : brewing.getRecipes()) {
            // Our own mutagen conversion output cannot license another source.
            List<ItemStack> reagents = new ArrayList<>();
            for (ItemStack candidate : ingredientCandidates) {
                try {
                    if (recipe.isIngredient(candidate)) reagents.add(candidate);
                } catch (RuntimeException invalid) {
                    // Third-party recipe rejected the sample item. Ignore it.
                }
            }
            if (reagents.isEmpty()) continue;
            for (ItemStack source : regularBottles) {
                boolean matches;
                try { matches = recipe.isInput(source); }
                catch (RuntimeException invalid) { continue; }
                if (!matches) continue;
                for (ItemStack reagent : reagents) {
                    try {
                        ItemStack output = recipe.getOutput(source.copy(),reagent.copy());
                        if (output.isEmpty() || output.is(HunterItems.MUTAGENIC_BREW.get())
                            || !(output.getItem() instanceof PotionItem)) continue;
                        PotionContents resulting = output.get(DataComponents.POTION_CONTENTS);
                        if (resulting != null && !resulting.customEffects().isEmpty())
                            customOutputs.add(resulting);
                        else if (resulting != null)
                            resulting.potion().flatMap(h -> h.unwrapKey())
                                .ifPresent(key -> allowed.add(key.location().toString()));
                    } catch (RuntimeException invalid) {
                        // An integration recipe may require extra item data.
                        // Fail closed; it needs its own verified adapter.
                    }
                }
            }
        }

        standardMixPotions = Set.copyOf(allowed);
        customRecipeOutputs = Set.copyOf(customOutputs);
        ProgressionMod.LOGGER.info(
            "Hunter mutation stand-brew eligibility: {} registered potion types, {} exact custom recipe results",
            standardMixPotions.size(),customRecipeOutputs.size());
    }

    public static void serverStopped(ServerStoppedEvent event) {
        standardMixPotions = Set.of();
        customRecipeOutputs = Set.of();
    }

    public static boolean eligible(PotionContents contents) {
        if (contents == null) return false;
        if (!contents.customEffects().isEmpty())
            return customRecipeOutputs.contains(contents);
        return contents.potion().flatMap(holder -> holder.unwrapKey())
            .map(key -> standardMixPotions.contains(key.location().toString()))
            .orElse(false);
    }
}
