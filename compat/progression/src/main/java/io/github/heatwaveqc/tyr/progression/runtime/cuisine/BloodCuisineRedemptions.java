package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Single authoritative world ledger for blood-food receipts. ItemStack copies
 * retain the same source receipt, so only one copy can generate native JAWR
 * blood. Durable across relogs, death, player transfers and dimension changes.
 */
public final class BloodCuisineRedemptions extends SavedData {
    private static final String FILE = "tyr_progression_blood_cuisine_receipts_v1";
    private final Set<UUID> spent = new HashSet<>();

    public static BloodCuisineRedemptions get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
            new SavedData.Factory<>(BloodCuisineRedemptions::new,
                BloodCuisineRedemptions::load), FILE);
    }

    public boolean spent(UUID id) {
        return id == null || spent.contains(id);
    }

    public boolean redeem(UUID id) {
        if (id == null || !spent.add(id)) return false;
        setDirty();
        return true;
    }

    public static BloodCuisineRedemptions load(CompoundTag tag, HolderLookup.Provider provider) {
        BloodCuisineRedemptions out = new BloodCuisineRedemptions();
        ListTag data = tag.getList("Receipts", Tag.TAG_COMPOUND);
        for (int i = 0; i < data.size(); i++) {
            CompoundTag receipt = data.getCompound(i);
            if (receipt.hasUUID("Id")) out.spent.add(receipt.getUUID("Id"));
        }
        return out;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("FormatVersion", 1);
        ListTag entries = new ListTag();
        for (UUID id : spent) {
            CompoundTag row = new CompoundTag();
            row.putUUID("Id", id);
            entries.add(row);
        }
        tag.put("Receipts", entries);
        return tag;
    }
}
