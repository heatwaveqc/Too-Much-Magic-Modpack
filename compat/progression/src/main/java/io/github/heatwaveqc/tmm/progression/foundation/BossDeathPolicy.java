package io.github.heatwaveqc.tmm.progression.foundation;

import java.util.Set;

/** Prevent generic type-death MXP from settling encounter-only or derivative IDs. */
public final class BossDeathPolicy {
    private BossDeathPolicy() {}
    private static final Set<String> NON_GENERIC = Set.of(
        "botania:pink_wither",
        "cataclysm:modern_remnant",
        "cataclysm:netherite_ministrosity",
        "cataclysm:the_baby_leviathan",
        "draconicevolution:guardian_wither",
        "extrabotany:ego_minion",
        "goety:brood_mother_servant",
        "goety:elder_guardian_servant",
        "goety:hurricane_servant",
        "goety:redstone_golem",
        "goety:redstone_monstrosity",
        "goety:vizier_clone",
        "goety:wither_necromancer_servant",
        "goety_cataclysm:ancient_remnant",
        "goety_cataclysm:aptrgangr",
        "goety_cataclysm:cindaria_servant",
        "goety_cataclysm:clawdian",
        "goety_cataclysm:coral_golem",
        "goety_cataclysm:coralssus",
        "goety_cataclysm:deepling_angler_servant",
        "goety_cataclysm:deepling_brute_servant",
        "goety_cataclysm:deepling_priest_servant",
        "goety_cataclysm:deepling_servant",
        "goety_cataclysm:deepling_warlock_servant",
        "goety_cataclysm:draugr_necromancer",
        "goety_cataclysm:draugr_necromancer_servant",
        "goety_cataclysm:draugr_servant",
        "goety_cataclysm:drowned_host_servant",
        "goety_cataclysm:elite_draugr_servant",
        "goety_cataclysm:ender_golem",
        "goety_cataclysm:hippocamtus",
        "goety_cataclysm:ignited_berserker",
        "goety_cataclysm:ignited_revenant",
        "goety_cataclysm:kobolediator",
        "goety_cataclysm:koboleton_servant",
        "goety_cataclysm:leviathan",
        "goety_cataclysm:lionfish",
        "goety_cataclysm:netherite_monstrosity",
        "goety_cataclysm:royal_draugr_servant",
        "goety_cataclysm:symbiocto_servant",
        "goety_cataclysm:the_prowler",
        "goety_cataclysm:urchinkin_servant",
        "goety_cataclysm:wadjet",
        "goety_cataclysm:watcher_servant",
        "goety_cataclysm:zombie_amethyst_crab",
        "neovitae:daemonium_doloris",
        "the_bumblezone:bee_queen",
        "the_bumblezone:cosmic_crystal_entity",
        "the_bumblezone:sentry_watcher",
        "twilightforest:ice_crystal",
        "twilightforest:knight_phantom",
        "twilightforest:lich_minion",
        "twilightforest:plateau_boss",
        "voidscape:null_servant",
        "wardentools:parasyte",
        "witchery:elle"
    );
    public static boolean permitsGenericDeath(String entityId) {
        return !NON_GENERIC.contains(entityId);
    }
}
