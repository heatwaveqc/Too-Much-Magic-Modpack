package io.github.heatwaveqc.tmm.monk.rules;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFactionPlayerHandler;
import io.github.heatwaveqc.tmm.monk.faction.MonkFaction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Server-authoritative Monk eligibility that can be validated with the
 * 1.10.x Vampirism faction API. External dragon/lich/other transformations
 * require a separate Transformation Compat bridge before survival release.
 */
public final class MonkEligibility {
    private MonkEligibility() { }

    public static boolean equipmentAllowsDiscipline(ServerPlayer player) {
        boolean conventionalWeapon = isConventionalWeapon(player.getMainHandItem())
                || isConventionalWeapon(player.getOffhandItem());
        boolean conventionalArmor = false;
        for (ItemStack stack : player.getArmorSlots()) {
            if (!stack.isEmpty()) {
                conventionalArmor = true;
                break;
            }
        }
        return DisciplineRules.equipmentPermitsDiscipline(conventionalWeapon, conventionalArmor);
    }

    private static boolean isConventionalWeapon(ItemStack stack) {
        return !stack.isEmpty() && stack.is(MonkItemTags.CONVENTIONAL_WEAPONS);
    }

    public static boolean canBeginRite(ServerPlayer player) {
        if (MonkFaction.FACTION == null || player.isCreative() || player.isSpectator()) return false;
        IFactionPlayerHandler factions = VampirismAPI.factionPlayerHandler(player);
        // Never trust an ALLOW override from another addon to displace a faction.
        return factions.getCurrentFaction() == null
                && factions.canJoin(MonkFaction.FACTION)
                && equipmentAllowsDiscipline(player);
    }

    public static boolean eligibleForMonkPower(ServerPlayer player) {
        return MonkFaction.FACTION != null
                && VampirismAPI.factionPlayerHandler(player).isInFaction(MonkFaction.FACTION)
                && equipmentAllowsDiscipline(player);
    }
}
