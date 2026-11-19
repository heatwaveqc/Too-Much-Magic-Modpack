package io.github.heatwaveqc.tmm.progression.mxp;

import net.neoforged.neoforge.common.ModConfigSpec;
import io.github.heatwaveqc.tmm.progression.mxp.core.ResourceBaselines;

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
    public static final ModConfigSpec.DoubleValue JAWR_INITIAL_BLOOD_POOL;
    public static final ModConfigSpec.DoubleValue PSI_INITIAL_POOL;
    public static final ModConfigSpec.DoubleValue HEX_MEDIA_POOL;
    public static final ModConfigSpec.DoubleValue EVILCRAFT_BLOOD_POOL;
    public static final ModConfigSpec.DoubleValue ROOTS_TERRA_POOL;
    public static final ModConfigSpec.DoubleValue EMBERS_PORTABLE_POOL;
    public static final ModConfigSpec.DoubleValue GOETY_SOUL_POOL;
    public static final ModConfigSpec.DoubleValue PRIMAL_CENTIMANA_POOL;
    public static final java.util.Map<String,ModConfigSpec.DoubleValue> PASTEL_DYE_EMC;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        b.push("source_baselines");
        ARS_FIRST_POOL = b.comment("Ars Nouveau initial server baseMax=100 (5.13.5 source). Set 0 to disable Ars MXP.")
            .defineInRange("ars_initial_mana", ResourceBaselines.ARS_STARTING_MANA, 0.0, 1.0e12);
        IRON_FIRST_POOL = b.comment("Iron's Spells native MAX_MANA attribute default 100 (3.16.3). 0 disables.")
            .defineInRange("irons_initial_mana", ResourceBaselines.IRONS_STARTING_MANA, 0.0, 1.0e12);
        BOTANIA_PORTABLE_POOL = b.comment("A first mana tablet or ordinary mana ring holds 500000 (Botania Revived 2.0.2). 0 disables.")
            .defineInRange("botania_portable_initial_mana", ResourceBaselines.BOTANIA_PORTABLE_MANA, 0.0, 1.0e12);
        NEO_EV_NETWORK_POOL = b.comment("Tier-0 Novicius/Weak Blood Orb Anima capacity = 5000 EV in Neo Vitae 1.1.53 blood_orb_stats data map. Fixed MXP reference; 0 disables the adapter.")
            .defineInRange("neovitae_ev_network_initial", ResourceBaselines.NEO_TIER_ZERO_ANIMA_EV, 0.0, 1.0e12);
        JAWR_INITIAL_BLOOD_POOL = b.comment(
                "JAWR 0.5.39 Vampire native blood pool normalization target = 900.",
                "Owner-approved proposed first-access baseline; set 0 to disable a future native adapter.",
                "IMPORTANT: No automatic JAWR hook has been enabled until a verified post-commit debit event exists.")
            .defineInRange("jawr_initial_blood", ResourceBaselines.JAWR_INITIAL_BLOOD, 0.0, 1.0e12);
        PSI_INITIAL_POOL=b.comment("Psi 110 first access personal pool. 0 disables standard CAD MXP.")
            .defineInRange("psi_initial_pool",ResourceBaselines.PSI_STARTING_POOL,0.0,1.0e12);
        HEX_MEDIA_POOL=b.comment(
                "EMC-derived fixed Hex Casting Media baseline: 50000 Media per 32 EMC, 1 EMC per base MXP.",
                "1562500 native Media = 1000 base MXP; no live ProjectE lookup. 0 disables the native paid Media adapter.",
                "Counts verified personal inventory or packaged-holder payment, not health or circles.")
            .defineInRange("hexcasting_emc_media_reference",ResourceBaselines.HEX_EMC_MEDIA_REFERENCE,0.0,1.0e12);
        EVILCRAFT_BLOOD_POOL=b.comment(
                "Basic Blood Extractor reference: 5000 mB = 1000 base MXP. Actual eligible carried blood only.",
                "0 disables future verified adapter; reference alone does not enable a hook.")
            .defineInRange("evilcraft_portable_blood_reference",ResourceBaselines.EVILCRAFT_PORTABLE_BLOOD,0.0,1.0e12);
        ROOTS_TERRA_POOL=b.comment("Roots Classic 1.5.10 initial native Terra bar 40; fixed reference. 0 disables.")
            .defineInRange("roots_initial_terra",ResourceBaselines.ROOTS_STARTING_TERRA,0.0,1.0e12);
        EMBERS_PORTABLE_POOL=b.comment("Source-backed first Ember Jar capacity 2000, provisional fixed reference; 0 disables.")
            .defineInRange("embers_initial_jar",ResourceBaselines.EMBERS_FIRST_JAR,0.0,1.0e12);
        GOETY_SOUL_POOL=b.comment("Awaiting owner approval of fixed Soul Energy reference. 0 disables; proposed EMC-derived 500 Souls per 1000 base MXP.")
            .defineInRange("goety_soul_reference",0.0,0.0,1.0e12);
        PRIMAL_CENTIMANA_POOL=b.comment("Awaiting approved fixed Primal reference. Native centimana units (100 per displayed mana). Proposed 50000 per 1000 MXP; 0 disables.")
            .defineInRange("primal_centimana_reference",0.0,0.0,1.0e12);
        b.push("pastel_dye_emc");
        var dyeAnchors=new java.util.LinkedHashMap<String,ModConfigSpec.DoubleValue>();
        io.github.heatwaveqc.tmm.progression.mxp.core.PastelInkValuation.DYE_EMC.entrySet().stream()
            .sorted(java.util.Map.Entry.comparingByKey()).forEach(entry ->
                dyeAnchors.put(entry.getKey(),b.comment("Frozen ordinary dye EMC from installed survey #348. Five Ink per dye; 0 disables this color.")
                    .defineInRange(entry.getKey(),entry.getValue().doubleValue(),0.0,1.0e9)));
        PASTEL_DYE_EMC=java.util.Map.copyOf(dyeAnchors);
        b.pop();
        b.pop();
        SPEC = b.build();
    }
    private MxpSourceConfig() {}
}
