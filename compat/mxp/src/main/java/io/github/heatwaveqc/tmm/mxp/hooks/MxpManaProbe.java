package io.github.heatwaveqc.tmm.mxp.hooks;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/** Observe the ACTUAL authoritative payer for an Ars or Iron's cast. */
public final class MxpManaProbe {
    public enum Origin { ARS, IRON }

    private MxpManaProbe() {}

    public static double balance(ServerPlayer player, Origin origin) {
        if (ModList.get().isLoaded("ars_n_spells")) {
            // Shared/independent/hybrid routing is owned by Ars 'n Spells.
            // Never infer the paying resource from the school that cast.
            try {
                Class<?> unit = Class.forName("com.otectus.arsnspells.contract.ResourceUnit");
                @SuppressWarnings({"unchecked", "rawtypes"})
                Object which = Enum.valueOf((Class) unit,
                        origin == Origin.ARS ? "ARS_MANA" : "IRONS_MANA");
                return MxpReflect.number(MxpReflect.staticCall(
                        "com.otectus.arsnspells.bridge.BridgeManager",
                        "getManaForMode", player, which));
            } catch (ClassNotFoundException | LinkageError unavailable) {
                return Double.NaN;
            }
        }
        if (origin == Origin.IRON) {
            Object data = MxpReflect.staticCall(
                    "io.redspace.ironsspellbooks.api.magic.MagicData",
                    "getPlayerMagicData", player);
            return MxpReflect.number(MxpReflect.call(data, "getMana"));
        }
        Object cap = MxpReflect.staticCall(
                "com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry",
                "getMana", player);
        return MxpReflect.number(MxpReflect.call(cap, "getCurrentMana"));
    }
}
