package io.github.heatwaveqc.tmm.economy;

import java.util.Set;
import moze_intel.projecte.api.ItemInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

/** Registry-survey allowlist: actual relic/artifact equipment, including
 * Reliquified Artifacts' modifications of the original artifact item IDs.
 * Not a namespace-wide material, consumable, egg or removed-item rule. */
public final class RelicTemplatePolicy {
    private RelicTemplatePolicy() {}
    /** Owner-approved 2026-11-19, independent of native initial rolls. */
    public static final long VALUE = 1024L;
    public static final Set<String> IDS = Set.of(
        "relics:chef_hat",
        "relics:chorus_staff",
        "relics:clot_of_time",
        "relics:cut_glass_boot",
        "relics:experience_disperser",
        "relics:glitchy_mantle",
        "relics:hunting_belt",
        "relics:jellyfish_necklace",
        "relics:kinetic_belt",
        "relics:leafy_mantle",
        "relics:midnight_mantle",
        "relics:piglin_mask",
        "relics:reflective_necklace",
        "relics:rider_flute",
        "relics:roller_skate",
        "relics:sphere_of_self_sacrifice",
        "relics:springy_boot",
        "artifacts:anglers_hat",
        "artifacts:antidote_vessel",
        "artifacts:aqua_dashers",
        "artifacts:bunny_hoppers",
        "artifacts:charm_of_shrinking",
        "artifacts:charm_of_sinking",
        "artifacts:chorus_totem",
        "artifacts:cloud_in_a_bottle",
        "artifacts:cowboy_hat",
        "artifacts:cross_necklace",
        "artifacts:crystal_heart",
        "artifacts:digging_claws",
        "artifacts:eternal_steak",
        "artifacts:everlasting_beef",
        "artifacts:feral_claws",
        "artifacts:fire_gauntlet",
        "artifacts:flame_pendant",
        "artifacts:flippers",
        "artifacts:golden_hook",
        "artifacts:helium_flamingo",
        "artifacts:kitty_slippers",
        "artifacts:lucky_scarf",
        "artifacts:night_vision_goggles",
        "artifacts:novelty_drinking_hat",
        "artifacts:obsidian_skull",
        "artifacts:onion_ring",
        "artifacts:panic_necklace",
        "artifacts:pickaxe_heater",
        "artifacts:plastic_drinking_hat",
        "artifacts:pocket_piston",
        "artifacts:power_glove",
        "artifacts:rooted_boots",
        "artifacts:running_shoes",
        "artifacts:scarf_of_invisibility",
        "artifacts:shock_pendant",
        "artifacts:snorkel",
        "artifacts:snowshoes",
        "artifacts:steadfast_spikes",
        "artifacts:strider_shoes",
        "artifacts:superstitious_hat",
        "artifacts:thorn_pendant",
        "artifacts:umbrella",
        "artifacts:universal_attractor",
        "artifacts:vampiric_glove",
        "artifacts:villager_hat",
        "artifacts:warp_drive",
        "artifacts:whoopee_cushion",
        "artifacts:withered_bracelet"
    );
    public static boolean applies(ItemInfo info) {
        return info != null && IDS.contains(BuiltInRegistries.ITEM.getKey(info.getItem().value()).toString());
    }
}
