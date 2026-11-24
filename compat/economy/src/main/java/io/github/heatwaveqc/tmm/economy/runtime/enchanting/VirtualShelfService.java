package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import io.github.heatwaveqc.tmm.economy.state.VirtualShelfState;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.AbilitiesIS;

/** Server-owned native Portable Enchanter storage. Dormant until the native menu cutover. */
public final class VirtualShelfService {
    private VirtualShelfService() {}
    private static final String KEY = "tmm_economy:portable_shelves";
    private static final String ITEMS = "Items", REVISION = "Revision", RETIRED = "LegacyRetired";
    private record Access(PlayerSkillData data, RegistryOps<Tag> ops, VirtualShelfState state, boolean retired) {}

    private static Optional<Access> access(ServerPlayer owner) {
        if (owner == null || !owner.isAlive() || owner.isSpectator() || !EnchantingFeatureGate.enabled())
            return Optional.empty();
        PlayerSkillData data = PlayerDataManager.getDataFor(owner);
        if (data == null || !data.hasAbility(AbilitiesIS.ENCHANTING) || data.persistedData == null)
            return Optional.empty();
        var ops = RegistryOps.create(NbtOps.INSTANCE, owner.registryAccess());
        if (!data.persistedData.contains(KEY)) return Optional.of(new Access(data, ops, VirtualShelfState.empty(), false));
        if (!data.persistedData.contains(KEY, Tag.TAG_COMPOUND)) return Optional.empty();
        CompoundTag saved = data.persistedData.getCompound(KEY);
        if (!saved.contains(ITEMS) || !saved.contains(REVISION, Tag.TAG_LONG)
            || saved.getLong(REVISION) < 0 || !saved.contains(RETIRED, Tag.TAG_BYTE)) return Optional.empty();
        return VirtualShelfState.CODEC.parse(ops, saved.get(ITEMS)).result()
            .map(state -> new Access(data, ops, state.restoreRevision(saved.getLong(REVISION)), saved.getBoolean(RETIRED)));
    }

    private static Optional<CompoundTag> encode(Access access, VirtualShelfState state, boolean retired) {
        return VirtualShelfState.CODEC.encodeStart(access.ops(), state).result().map(items -> {
            CompoundTag tag = new CompoundTag();
            tag.put(ITEMS, items);
            tag.putLong(REVISION, state.revision());
            tag.putBoolean(RETIRED, retired);
            return tag;
        });
    }

    /** Call only after old native deposit/withdraw and old numeric enchanting power paths are disabled.
     * Refuse a full inventory, corrupt/fractional value or unexpected post-retirement power.
     * Normal repeated calls cannot refund the old books twice.
     */
    public static boolean retireLegacyBooks(ServerPlayer owner) {
        var resolved = access(owner);
        if (resolved.isEmpty()) return false;
        var a = resolved.get();
        var count = VirtualShelfLayout.legacyBooks(a.data().enchantPower);
        if (count.isEmpty()) return false;
        int books = count.getAsInt();
        if (a.retired()) return books == 0;
        int free = books == 0 ? -1 : owner.getInventory().getFreeSlot();
        if (books > 0 && free < 0) return false;
        var saved = encode(a, a.state(), true);
        if (saved.isEmpty()) return false;
        if (books > 0) owner.getInventory().setItem(free, new ItemStack(Items.BOOK, books));
        a.data().enchantPower = 0;
        a.data().persistedData.put(KEY, saved.get());
        owner.getInventory().setChanged();
        owner.containerMenu.broadcastChanges();
        return true;
    }

    private static Optional<Access> active(ServerPlayer owner) {
        return access(owner).filter(a -> a.retired() && a.data().enchantPower == 0);
    }

    public static List<ItemStack> items(ServerPlayer owner) {
        return active(owner).map(a -> a.state().items()).orElse(List.of());
    }

    public static boolean deposit(ServerPlayer owner, int sourceSlot) {
        var resolved = active(owner);
        if (resolved.isEmpty() || sourceSlot < 0 || sourceSlot >= 36) return false;
        var a = resolved.get();
        ItemStack original = owner.getInventory().getItem(sourceSlot);
        if (original.isEmpty() || VirtualShelfProvider.resolve(original.copyWithCount(1)).isEmpty()) return false;
        var next = a.state().depositOne(original);
        if (next.isEmpty()) return false;
        var encoded = encode(a, next.get(), true);
        if (encoded.isEmpty()) return false;
        original.shrink(1);
        a.data().persistedData.put(KEY, encoded.get());
        owner.getInventory().setChanged();
        owner.containerMenu.broadcastChanges();
        return true;
    }

    /** Refund the actual whole cluster, even if its stats are no longer available after reload. */
    public static boolean withdraw(ServerPlayer owner, int position) {
        var resolved = active(owner);
        if (resolved.isEmpty() || position < 0 || position >= VirtualShelfState.SLOTS) return false;
        var a = resolved.get();
        ItemStack stored = a.state().item(position);
        int free = owner.getInventory().getFreeSlot();
        if (stored.isEmpty() || free < 0) return false;
        var encoded = encode(a, a.state().remove(position), true);
        if (encoded.isEmpty()) return false;
        owner.getInventory().setItem(free, stored);
        a.data().persistedData.put(KEY, encoded.get());
        owner.getInventory().setChanged();
        owner.containerMenu.broadcastChanges();
        return true;
    }

    /** Re-resolve live stats each time. Unrepresentable occupied positions fail closed. */
    public static Optional<dev.shadowsoffire.apothic_enchanting.table.EnchantmentTableStats> stats(ServerPlayer owner) {
        var resolved = active(owner);
        if (resolved.isEmpty()) return Optional.empty();
        var positions = new java.util.ArrayList<VirtualShelfStats.Position>();
        for (ItemStack stack : resolved.get().state().items()) {
            if (stack.isEmpty()) continue;
            var position = VirtualShelfProvider.resolve(stack);
            if (position.isEmpty()) return Optional.empty();
            positions.add(position.get());
        }
        return Optional.of(VirtualShelfStats.aggregate(positions));
    }
}
