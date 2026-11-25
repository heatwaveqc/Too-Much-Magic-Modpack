package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import dev.shadowsoffire.apothic_enchanting.table.EnchantmentTableStats;
import io.github.heatwaveqc.tmm.economy.runtime.PersonalEnchantFilterService;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantment;

/** Pinned Apothic Enchanting 1.6.4: extend only the native random-roll blacklist.
 * Preserve table Eterna (not the player's capped value), all other stats and
 * every native restriction. Never mutate a table's stored stats or its shelves.
 */
public final class ApothicPersonalFilter {
    private ApothicPersonalFilter() {}

    public static EnchantmentTableStats forRandomRoll(ServerPlayer owner, EnchantmentTableStats nativeStats) {
        Set<ResourceLocation> personal = PersonalEnchantFilterService.excluded(owner);
        if (personal.isEmpty()) return nativeStats;
        var registry = owner.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Set<Holder<Enchantment>> blacklist = new HashSet<>(nativeStats.blacklist());
        for (ResourceLocation id : personal) {
            registry.get(ResourceKey.create(Registries.ENCHANTMENT, id)).ifPresent(blacklist::add);
        }
        if (blacklist.equals(nativeStats.blacklist())) return nativeStats;
        return new EnchantmentTableStats(nativeStats.tableEterna(), nativeStats.quanta(),
            nativeStats.arcana(), nativeStats.clues(), Set.copyOf(blacklist),
            nativeStats.treasure(), nativeStats.stable());
    }
}
