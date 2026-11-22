package io.github.heatwaveqc.tmm.friendship;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import net.neoforged.neoforge.common.ModConfigSpec;

/** Per-world policy; CSV ID blacklists support vanilla and individually audited mod effects. */
public final class FriendshipConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue TRANSMIT;
    public static final ModConfigSpec.BooleanValue BENEFICIAL;
    public static final ModConfigSpec.BooleanValue CROSS_DIMENSION;
    public static final ModConfigSpec.ConfigValue<String> BLOCKED_POTIONS;
    public static final ModConfigSpec.ConfigValue<String> BLOCKED_EFFECTS;
    public static final ModConfigSpec.ConfigValue<String> ALLOWED_MODDED_EFFECTS;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        b.push("sympathetic_splash");
        TRANSMIT = b.comment("Allow flowers to transmit ordinary splash potion effects to their ONLINE crafter.")
                .define("enabled", true);
        BENEFICIAL = b.comment("Allow beneficial effects without recipient opt-in.")
                .define("beneficialEnabled", true);
        CROSS_DIMENSION = b.comment("Permit transmissions across dimensions.")
                .define("crossDimension", true);
        BLOCKED_POTIONS = b.comment("Comma-separated namespaced POTION IDs never transmitted.")
                .define("blockedPotions", "");
        BLOCKED_EFFECTS = b.comment("Comma-separated namespaced EFFECT IDs never transmitted.")
                .define("blockedEffects", "");
        ALLOWED_MODDED_EFFECTS = b.comment("Fail-closed modded effect allowlist: only explicitly audited effect IDs are eligible.")
                .define("allowedModdedEffects", "");
        b.pop();
        SPEC = b.build();
    }

    private FriendshipConfig() {}
    public static Set<String> ids(String text) {
        return Arrays.stream(text.split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .filter(s -> s.matches("[a-z0-9_.-]+:[a-z0-9/._-]+"))
                .collect(Collectors.toUnmodifiableSet());
    }

    public static SplashTransmission.Config policy() {
        return new SplashTransmission.Config(
                TRANSMIT.get(), BENEFICIAL.get(), CROSS_DIMENSION.get(),
                ids(BLOCKED_POTIONS.get()), ids(BLOCKED_EFFECTS.get()));
    }
}
