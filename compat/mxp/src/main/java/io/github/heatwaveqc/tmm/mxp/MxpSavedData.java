package io.github.heatwaveqc.tmm.mxp;

import io.github.heatwaveqc.tmm.mxp.core.MxpAccount;
import io.github.heatwaveqc.tmm.mxp.core.MxpMath;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Single overworld SavedData file: stable across death, logout and dimensions. */
public final class MxpSavedData extends SavedData {
    private static final String FILE_NAME = "tmm_mxp_players_v1";
    private final Map<UUID, MxpAccount> accounts = new HashMap<>();

    public static MxpSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
            new SavedData.Factory<>(MxpSavedData::new, MxpSavedData::load),
            FILE_NAME
        );
    }

    public MxpAccount snapshot(UUID player) {
        return accounts.getOrDefault(player, new MxpAccount()).copy();
    }

    public void commit(UUID player, MxpAccount account) {
        accounts.put(player, account.copy());
        setDirty();
    }

    public static MxpSavedData load(CompoundTag root, HolderLookup.Provider lookup) {
        MxpSavedData data = new MxpSavedData();
        ListTag players = root.getList("Players", Tag.TAG_COMPOUND);
        for (int i = 0; i < players.size(); i++) {
            CompoundTag row = players.getCompound(i);
            try {
                UUID id = row.getUUID("UUID");
                long levels = row.getLong("LifetimeMXPLevels");
                double progress = row.getDouble("CurrentProgress");
                double pending = row.getDouble("PendingTyzDelivery");
                MxpAccount acc = new MxpAccount();
                acc.restore(new MxpMath.Counter(levels, progress), pending,
                    readStrings(row, "Rituals"), readStrings(row, "Bosses"),
                    readStrings(row, "Crafts"), readStrings(row, "RecentTransactions"));
                data.accounts.put(id, acc);
            } catch (IllegalArgumentException e) {
                // Corrupt player row must not reset the entire world's records.
                MxpMod.LOGGER.error("Skipping invalid persisted MXP account row #{}: {}", i, e.toString());
            }
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider lookup) {
        tag.putInt("FormatVersion", 1);
        ListTag players = new ListTag();
        accounts.forEach((uuid, acc) -> {
            CompoundTag row = new CompoundTag();
            row.putUUID("UUID", uuid);
            row.putLong("LifetimeMXPLevels", acc.counter().lifetimeLevels());
            row.putDouble("CurrentProgress", acc.counter().progress());
            row.putDouble("PendingTyzDelivery", acc.pendingDelivery());
            row.put("Rituals", strings(acc.firstRituals()));
            row.put("Bosses", strings(acc.firstBosses()));
            row.put("Crafts", strings(acc.firstCrafts()));
            row.put("RecentTransactions", strings(acc.recentTransactions()));
            players.add(row);
        });
        tag.put("Players", players);
        return tag;
    }

    private static List<String> readStrings(CompoundTag row, String key) {
        ListTag entries = row.getList(key, Tag.TAG_STRING);
        List<String> result = new ArrayList<>(entries.size());
        for (int i = 0; i < entries.size(); i++) result.add(entries.getString(i));
        return result;
    }

    private static ListTag strings(Iterable<String> values) {
        ListTag list = new ListTag();
        for (String value : values) list.add(StringTag.valueOf(value));
        return list;
    }
}
