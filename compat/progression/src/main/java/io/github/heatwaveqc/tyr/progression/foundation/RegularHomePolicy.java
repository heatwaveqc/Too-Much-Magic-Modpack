package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.List;
import java.util.Set;

/**
 * Stable, class-independent Regular-stage home entitlement and frozen trial roster.
 * Pure policy: runtime and providers must not accept arbitrary client house IDs.
 */
public final class RegularHomePolicy {
    private RegularHomePolicy() {}

    public static final String LOCKED = "locked";
    public static final String PENDING = "pending_graduation";
    public static final String LEGACY_REVIEW = "legacy_review";
    public static final String AVAILABLE = "available";
    public static final String SELECTED = "selected_";
    public static final String RESERVED = "reserved_";
    public static final String SPENT = "spent_";
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
        return regular && AVAILABLE.equals(stored);
    }

    /** Only an actual personal graduation can turn a locked record into a pending grant. */
    public static String graduate(String stored) {
        return stored == null || LOCKED.equals(stored) ? PENDING : stored;
    }

    public enum NativeGrant { NOT_GIVEN, GIVEN, UNKNOWN }

    /** Migrate native receipts without confiscating anything; unknown existing
     * Normies require review, not a guessed free grant. */
    public static String reconcile(String stored, boolean regular, NativeGrant nativeGrant) {
        if (stored != null && stored.startsWith("spent:")) return stored.replace(':','_');
        if (stored != null && stored.startsWith("reserved:")) return stored.replace(':','_');
        if (terminal(stored) || (stored != null && stored.startsWith(RESERVED))) return stored;
        if (nativeGrant == NativeGrant.GIVEN) return SPENT + "legacy_prefab";
        if (stored == null) return regular ? LEGACY_REVIEW : LOCKED;
        if (PENDING.equals(stored) && regular && nativeGrant == NativeGrant.NOT_GIVEN)
            return AVAILABLE;
        return stored;
    }

    /** Select exactly one approved custom home; the owner-bound blueprint is issued once. */
    public static String select(boolean regular, String stored, String choice) {
        return canUse(regular, stored) && custom(choice) ? SELECTED + choice : null;
    }

    public static String reserveSelected(boolean regular, String stored, String choice) {
        return regular && custom(choice) && (SELECTED + choice).equals(stored)
            ? RESERVED + choice : null;
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
