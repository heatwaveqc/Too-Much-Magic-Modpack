package io.github.heatwaveqc.tmm.mxp;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Immutable first-access reference pools. Never derive from the current,
 * skill-increased capacity: efficiency/regeneration are intended to improve
 * sustainable MXP gain. Neo's Anima has no intrinsic cap, so its baseline
 * must be measured and explicitly configured before that adapter awards XP.
 */
public final class MxpSourceConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue ARS_FIRST_POOL;
    public static final ModConfigSpec.DoubleValue IRON_FIRST_POOL;
    public static final ModConfigSpec.DoubleValue BOTANIA_PORTABLE_POOL;
    public static final ModConfigSpec.DoubleValue NEO_EV_NETWORK_POOL;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        b.push("source_baselines");
        ARS_FIRST_POOL = b.comment("Ars Nouveau initial server baseMax=100 (5.13.5 source). Set 0 to disable Ars MXP.")
            .defineInRange("ars_initial_mana", 100.0, 0.0, 1.0e12);
        IRON_FIRST_POOL = b.comment("Iron's Spells native MAX_MANA attribute default 100 (3.16.3). 0 disables.")
            .defineInRange("irons_initial_mana", 100.0, 0.0, 1.0e12);
        BOTANIA_PORTABLE_POOL = b.comment("A first mana tablet or ordinary mana ring holds 500000 (Botania Revived 2.0.2). 0 disables.")
            .defineInRange("botania_portable_initial_mana", 500000.0, 0.0, 1.0e12);
        NEO_EV_NETWORK_POOL = b.comment("Tier-0 Novicius/Weak Blood Orb Anima capacity = 5000 EV in Neo Vitae 1.1.53 blood_orb_stats data map. Fixed MXP reference; 0 disables the adapter.")
            .defineInRange("neovitae_ev_network_initial", 5000.0, 0.0, 1.0e12);
        b.pop();
        SPEC = b.build();
    }
    private MxpSourceConfig() {}
}
