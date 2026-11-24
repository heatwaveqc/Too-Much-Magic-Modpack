package io.github.heatwaveqc.traders.vengeance.state;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/** World-global permanent UUID blacklist. Stored in the Overworld's saved data. */
public final class TraderBlacklist extends SavedData {
    private static final String NAME = "traders_vengeance_offenders";
    private static final SavedData.Factory<TraderBlacklist> FACTORY =
        new SavedData.Factory<>(TraderBlacklist::new, TraderBlacklist::load, null);
    private final Set<UUID> offenders = new HashSet<>();

    public static TraderBlacklist get(ServerLevel anywhere) {
        return anywhere.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, NAME);
    }

    public boolean contains(UUID player) {
        return offenders.contains(player);
    }

    public boolean accuse(UUID player) {
        if (offenders.add(player)) {
            setDirty();
            return true;
        }
        return false;
    }

    public boolean pardon(UUID player) {
        if (offenders.remove(player)) {
            setDirty();
            return true;
        }
        return false;
    }

    private static TraderBlacklist load(CompoundTag tag, HolderLookup.Provider registries) {
        TraderBlacklist result = new TraderBlacklist();
        ListTag entries = tag.getList("offenders", Tag.TAG_STRING);
        for (int i = 0; i < entries.size(); i++) {
            try {
                result.offenders.add(UUID.fromString(entries.getString(i)));
            } catch (IllegalArgumentException ignored) {
                // A malformed entry must not prevent the world loading.
            }
        }
        return result;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag entries = new ListTag();
        offenders.stream().map(UUID::toString).sorted()
            .map(StringTag::valueOf).forEach(entries::add);
        tag.put("offenders", entries);
        return tag;
    }
}
