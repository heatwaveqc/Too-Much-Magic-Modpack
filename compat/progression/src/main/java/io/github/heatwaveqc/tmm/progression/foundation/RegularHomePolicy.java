package io.github.heatwaveqc.tmm.progression.foundation;

import java.util.List;
import java.util.Set;

/**
 * Stable, class-independent Regular-stage home entitlement and frozen trial roster.
 * Pure policy: runtime and providers must not accept arbitrary client house IDs.
 */
public final class RegularHomePolicy {
    private RegularHomePolicy() {}

    public static final String AVAILABLE = "available";
    public static final String RESERVED = "reserved:";
    public static final String SPENT = "spent:";
    public static final String GENERIC = "generic";

    public static final List<String> CUSTOM = List.of(
        "adventurers_inn", "arcane_forge", "arcane_sanctum",
        "castaways_retreat", "celestial_observatory", "chromatic_manor",
        "dark_citadel", "dwarven_foundry", "elven_garden",
        "farmers_homestead", "hearthkeeper", "mages_academy",
        "summoners_lodge", "sanguine_temple", "spirit_shrine",
        "witch_cottage", "witchs_manor", "wizards_tower"
    );
    private static final Set<String> ALLOWED = Set.copyOf(CUSTOM);

    public static boolean custom(String name) {
        return name != null && ALLOWED.contains(name);
    }

    public static boolean choice(String name) {
        return GENERIC.equals(name) || custom(name);
    }

    public static boolean canUse(boolean regular, String stored) {
        return regular && (stored == null || AVAILABLE.equals(stored));
    }

    public static String reserve(boolean regular, String stored, String choice) {
        return canUse(regular, stored) && choice(choice) ? RESERVED + choice : null;
    }

    public static String finish(String reserved, String choice, boolean placed) {
        if (!((RESERVED + choice).equals(reserved) && choice(choice))) return reserved;
        return placed ? SPENT + choice : AVAILABLE;
    }

    public static boolean terminal(String state) {
        return state != null && state.startsWith(SPENT);
    }
}
