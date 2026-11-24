package io.github.heatwaveqc.tmm.economy.runtime;

import io.github.heatwaveqc.tmm.economy.state.PersonalEnchantFilterState;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PersonalEnchantFilter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Economy-owned server transactions. Native Improvable Skills persists the one
 * actual six-book inventory. No registration/consumer is enabled in this slice.
 * Corrupt or unresolvable saved data fails closed, never replaced with empty data.
 */
public final class PersonalEnchantFilterService {
    private PersonalEnchantFilterService() {}
    private static final String KEY = "tmm_economy:arcane_discrimination";
    private static final String BOOKS = "Books";
    private static final String REVISION = "Revision";
    private record Access(CompoundTag nativeData, RegistryOps<Tag> ops, PersonalEnchantFilterState state) {}

    private static Optional<Access> access(ServerPlayer player) {
        return PersonalEnchantFilterAbilityGate.unlockedData(player).flatMap(data -> {
            RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, player.registryAccess());
            if (!data.contains(KEY)) return Optional.of(new Access(data, ops, PersonalEnchantFilterState.empty()));
            if (!data.contains(KEY, Tag.TAG_COMPOUND)) return Optional.empty();
            CompoundTag saved = data.getCompound(KEY);
            if (!saved.contains(BOOKS) || !saved.contains(REVISION, Tag.TAG_LONG)
                || saved.getLong(REVISION) < 0) return Optional.empty();
            return PersonalEnchantFilterState.CODEC.parse(ops, saved.get(BOOKS)).result()
                .map(state -> new Access(data, ops, state.restoreRevision(saved.getLong(REVISION))));
        });
    }

    private static Optional<CompoundTag> encode(Access access, PersonalEnchantFilterState state) {
        return PersonalEnchantFilterState.CODEC.encodeStart(access.ops(), state).result().map(books -> {
            CompoundTag saved = new CompoundTag();
            saved.put(BOOKS, books);
            saved.putLong(REVISION, state.revision());
            return saved;
        });
    }

    public static Set<ResourceLocation> excluded(ServerPlayer owner) {
        return access(owner).map(a -> a.state().exclusions(true)).orElse(Set.of());
    }

    public static PersonalEnchantFilter snapshot(ServerPlayer owner) {
        return access(owner).map(a -> new PersonalEnchantFilter(true, a.state().revision(),
            a.state().exclusions(true).stream().map(ResourceLocation::toString).sorted().toList()))
            .orElseGet(PersonalEnchantFilter::locked);
    }

    /** For native random candidate assembly only, never deliberate application. */
    public static boolean removeExcludedRandomCandidates(ServerPlayer owner, List<Holder<Enchantment>> candidates) {
        Set<ResourceLocation> excluded = excluded(owner);
        return !excluded.isEmpty() && candidates.removeIf(holder -> holder != null
            && holder.unwrapKey().map(key -> excluded.contains(key.location())).orElse(false));
    }

    public static List<ItemStack> books(ServerPlayer owner) {
        return access(owner).map(a -> a.state().books()).orElse(List.of());
    }

    public static boolean deposit(ServerPlayer owner, int sourceSlot, int bookSlot) {
        Optional<Access> resolved = access(owner);
        if (resolved.isEmpty() || bookSlot < 0 || bookSlot >= PersonalEnchantFilterState.SLOTS) return false;
        Access access = resolved.get();
        Inventory inv = owner.getInventory();
        if (sourceSlot < 0 || sourceSlot >= inv.getContainerSize() || !access.state().book(bookSlot).isEmpty()) return false;
        ItemStack original = inv.getItem(sourceSlot);
        if (original.isEmpty()) return false;
        ItemStack book = original.copyWithCount(1);
        if (PersonalEnchantFilterState.enchantmentId(book).isEmpty()) return false;
        Optional<CompoundTag> saved = encode(access, access.state().replace(bookSlot, book));
        if (saved.isEmpty()) return false;
        // Prepare full exact component encoding before touching either inventory.
        original.shrink(1);
        access.nativeData().put(KEY, saved.get());
        inv.setChanged();
        owner.containerMenu.broadcastChanges();
        return true;
    }

    public static boolean withdraw(ServerPlayer owner, int bookSlot) {
        Optional<Access> resolved = access(owner);
        if (resolved.isEmpty() || bookSlot < 0 || bookSlot >= PersonalEnchantFilterState.SLOTS) return false;
        Access access = resolved.get();
        ItemStack book = access.state().book(bookSlot);
        if (book.isEmpty()) return false;
        Inventory inv = owner.getInventory();
        int free = inv.getFreeSlot();
        if (free < 0) return false;
        Optional<CompoundTag> saved = encode(access, access.state().replace(bookSlot, ItemStack.EMPTY));
        if (saved.isEmpty()) return false;
        inv.setItem(free, book);
        access.nativeData().put(KEY, saved.get());
        inv.setChanged();
        owner.containerMenu.broadcastChanges();
        return true;
    }
}
