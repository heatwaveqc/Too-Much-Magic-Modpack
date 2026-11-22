package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.state.PersonalEnchantFilterState;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Server-only personal six-book escrow and one shared random-roll predicate.
 * Native Improvable Skills is the only source of ability authorization.
 * This class intentionally exposes no client-writable inventory or unlock.
 */
public final class PersonalEnchantFilterService {
    private PersonalEnchantFilterService() {}

    public static void register() {
        NeoForge.EVENT_BUS.addListener(PersonalEnchantFilterService::copyNonDeath);
    }

    public static Set<ResourceLocation> excluded(ServerPlayer player) {
        if (!PersonalEnchantFilterAbilityGate.unlocked(player)) return Set.of();
        return player.getData(ProgressionAttachments.PERSONAL_ENCHANT_FILTER.get())
            .exclusions(true);
    }

    public static boolean excludes(ServerPlayer player, ResourceLocation enchantment) {
        return enchantment != null && excluded(player).contains(enchantment);
    }

    /**
     * Consume exactly one real enchanted book from inventory to an empty slot.
     * Source slot and ability authorization are revalidated on the server.
     */
    public static boolean deposit(ServerPlayer player, int sourceSlot, int bookSlot) {
        if (!PersonalEnchantFilterAbilityGate.unlocked(player)
            || bookSlot < 0 || bookSlot >= PersonalEnchantFilterState.SLOTS) return false;

        Inventory inv = player.getInventory();
        if (sourceSlot < 0 || sourceSlot >= inv.getContainerSize()) return false;
        PersonalEnchantFilterState prior =
            player.getData(ProgressionAttachments.PERSONAL_ENCHANT_FILTER.get());
        if (!prior.book(bookSlot).isEmpty()) return false;

        ItemStack inventoryBook = inv.getItem(sourceSlot);
        if (inventoryBook.isEmpty()) return false;
        ItemStack escrowBook = inventoryBook.copyWithCount(1);
        if (PersonalEnchantFilterState.enchantmentId(escrowBook).isEmpty()) return false;

        PersonalEnchantFilterState updated = prior.replace(bookSlot, escrowBook);
        inventoryBook.shrink(1);
        player.setData(ProgressionAttachments.PERSONAL_ENCHANT_FILTER.get(), updated);
        inv.setChanged();
        player.containerMenu.broadcastChanges();
        return true;
    }

    /**
     * Return the exact original stack/components, not a new generic book.
     * Refuse when the player's inventory has no free main-inventory slot.
     */
    public static boolean withdraw(ServerPlayer player, int bookSlot) {
        if (!PersonalEnchantFilterAbilityGate.unlocked(player)
            || bookSlot < 0 || bookSlot >= PersonalEnchantFilterState.SLOTS) return false;
        PersonalEnchantFilterState prior =
            player.getData(ProgressionAttachments.PERSONAL_ENCHANT_FILTER.get());
        ItemStack book = prior.book(bookSlot);
        if (book.isEmpty()) return false;
        Inventory inv = player.getInventory();
        int free = inv.getFreeSlot();
        if (free < 0) return false;

        PersonalEnchantFilterState updated = prior.replace(bookSlot, ItemStack.EMPTY);
        // Original book is returned before escrow is cleared; all operations are
        // on the authoritative server thread, without a client-side grant.
        inv.setItem(free, book);
        player.setData(ProgressionAttachments.PERSONAL_ENCHANT_FILTER.get(), updated);
        inv.setChanged();
        player.containerMenu.broadcastChanges();
        return true;
    }

    private static void copyNonDeath(PlayerEvent.Clone event) {
        if (event.isWasDeath()) return; // attachment already uses copyOnDeath
        if (!(event.getEntity() instanceof ServerPlayer current)
            || !(event.getOriginal() instanceof ServerPlayer previous)) return;
        current.setData(ProgressionAttachments.PERSONAL_ENCHANT_FILTER.get(),
            previous.getData(ProgressionAttachments.PERSONAL_ENCHANT_FILTER.get()));
    }
}
