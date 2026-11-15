package io.github.heatwaveqc.tmm.progression.runtime;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

/** Any standard POTION_CONTENTS input (including registered modded potions). */
public final class HunterBrewing {
    private HunterBrewing() {}
    public static void register(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addRecipe(new IBrewingRecipe() {
            @Override public boolean isInput(ItemStack input) {
                if (!(input.getItem() instanceof PotionItem)
                    || input.is(HunterItems.MUTAGENIC_BREW.get())) return false;
                PotionContents contents = input.get(DataComponents.POTION_CONTENTS);
                return contents != null && HunterMutations.hasEligibleEffect(contents);
            }

            @Override public boolean isIngredient(ItemStack ingredient) {
                return ingredient.is(HunterItems.BIO_MUTAGEN.get());
            }

            @Override public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                if (!isInput(input) || !isIngredient(ingredient)) return ItemStack.EMPTY;
                PotionContents contents = input.get(DataComponents.POTION_CONTENTS);
                if (contents == null) return ItemStack.EMPTY;
                ItemStack result = new ItemStack(HunterItems.MUTAGENIC_BREW.get());
                // PotionContents carries registered vanilla/modded potion references AND
                // custom effects. Never trust client-provided arbitrary XP/progress values.
                result.set(DataComponents.POTION_CONTENTS,contents);
                return result;
            }
        });
    }
}
