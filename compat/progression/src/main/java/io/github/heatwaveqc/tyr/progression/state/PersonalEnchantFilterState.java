package io.github.heatwaveqc.tyr.progression.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.heatwaveqc.tyr.progression.foundation.PersonalEnchantFilterPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/**
 * Persist actual book ItemStacks (including all components), not synthetic
 * enchantment IDs. Nothing is dropped when the skill is temporarily locked.
 * Stored stacks are copied on both ingress and egress to prevent aliasing.
 */
public final class PersonalEnchantFilterState {
    public static final int SLOTS = PersonalEnchantFilterPolicy.BOOK_SLOTS;

    public static final Codec<PersonalEnchantFilterState> CODEC =
        ItemStack.OPTIONAL_CODEC.listOf().comapFlatMap(
            list -> list.size() == SLOTS
                ? DataResult.success(new PersonalEnchantFilterState(list))
                : DataResult.error(() -> "Personal filter must have six book slots"),
            state -> state.books());

    private final List<ItemStack> slots;

    private PersonalEnchantFilterState(List<ItemStack> books) {
        if (books.size() != SLOTS) throw new IllegalArgumentException("Six book slots required");
        List<ItemStack> owned = new ArrayList<>(SLOTS);
        for (ItemStack stack : books) owned.add(stack.copy());
        slots = List.copyOf(owned);
    }

    public static PersonalEnchantFilterState empty() {
        return new PersonalEnchantFilterState(
            java.util.Collections.nCopies(SLOTS, ItemStack.EMPTY));
    }

    public List<ItemStack> books() {
        return slots.stream().map(ItemStack::copy).toList();
    }

    public ItemStack book(int index) {
        return slots.get(index).copy();
    }

    /** Enchanted book with exactly one stored enchantment and a valid ID. */
    public static Optional<ResourceLocation> enchantmentId(ItemStack book) {
        if (book == null || !book.is(Items.ENCHANTED_BOOK) || book.getCount() != 1)
            return Optional.empty();
        ItemEnchantments stored = book.getOrDefault(
            DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (stored.size() != 1) return Optional.empty();
        for (var entry : stored.entrySet()) {
            Holder<Enchantment> enchantment = entry.getKey();
            if (entry.getIntValue() <= 0 || enchantment == null) return Optional.empty();
            return enchantment.unwrapKey().map(key -> key.location());
        }
        return Optional.empty();
    }

    /**
     * True IDs are computed from currently stored books every time; no
     * parallel knowledge cache or cross-player static blacklist.
     */
    public Set<ResourceLocation> exclusions(boolean skillUnlocked) {
        if (!skillUnlocked) return Set.of();
        List<ResourceLocation> bookIds = new ArrayList<>(SLOTS);
        for (ItemStack book : slots) bookIds.add(enchantmentId(book).orElse(null));
        return PersonalEnchantFilterPolicy.excluded(true, bookIds);
    }

    /** Call only from a server-validated escrow transaction. */
    public PersonalEnchantFilterState replace(int slot, ItemStack book) {
        if (slot < 0 || slot >= SLOTS) throw new IndexOutOfBoundsException(slot);
        if (book == null || (!book.isEmpty() && enchantmentId(book).isEmpty()))
            throw new IllegalArgumentException("A filter slot requires one single-enchantment book");
        List<ItemStack> newSlots = books();
        newSlots.set(slot, book.copy());
        return new PersonalEnchantFilterState(newSlots);
    }
}
