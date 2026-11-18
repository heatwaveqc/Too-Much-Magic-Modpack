# Ritual of Equivalent Exchange — configuration staging (2026-11-18)

Source of truth: approved final specification in [#236](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/236), resource economy [#320](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/320), item census `audit/live-registry-surveys-2026-11-18` (`items.csv` blob b89ff1087a6d807afc475f84307ac45720af811b, installed build f7aeb56a0f7b9c5957f922f5a397037f25207c76). The recipe is **intentionally disabled** by `neoforge:false`; no extant Stone recipe retired in this configuration-only pass. Do not remove the condition independently of completing the cutover.

Native Occultism crafting recipe: 12 separate closed tags, one item per tag, bound Marid book, `occultism:craft_marid`, 600 seconds, one ProjectE Philosopher's Stone. Uses the output as ritual_dummy following the existing ink ritual branch convention; exact-pin JEI/bowl activation validation required. No extra sacrifice; no ProjectE input or Chaos prerequisite. The twelve tags contain 40 distinct registered items; activation and output also registry-confirmed. Closed tags use replace:true; no broad common tags or invented item IDs.

| Category | Registry-confirmed alternatives |
| --- | --- |
| primordial powers | `minecraft:nether_star`, `draconicevolution:dragon_heart`, `deeperdarker:heart_of_the_deep` **+ pending actual Twilight Forest final-boss reward** |
| masters of magic | `ars_nouveau:wilden_tribute`, `botania:life_essence`, `neovitae:mine_key`, `goety:unholy_blood` |
| ancient horrors | `cataclysm:abyssal_egg`, `cataclysm:remnant_skull`, `alexscaves:pure_darkness`, `alexscaves:tectonic_shard` |
| titans of destruction | `cataclysm:infernal_forge`, `cataclysm:gauntlet_of_guard`, `cataclysm:witherite_block`, `cataclysm:ignitium_ingot` |
| fallen sovereigns | `irons_spellbooks:bone_key`, `irons_spellbooks:divine_soulshard`, `cataclysm:cursium_ingot`, `cataclysm:essence_of_the_storm` |
| conquerors of other worlds | `theaurorian:trophy_moon_queen`, `eternal_starlight:crescent_pendant`, `the_bumblezone:essence_continuity`, `wardentools:contagion_incarnation_skull` |
| arcane metallurgy | `primalmagick:hallowsteel_ingot`, `malum:malignant_lead`, `goety:dark_metal_block` |
| celestial artifacts | `forbidden_arcanus:eternal_stella`, `astralsorcery:akashic_singularity`, `pastel:moonstone_core` |
| forbidden witchcraft | `enchanted:refined_evil`, `witchery:demon_heart` |
| blood and spirit | `evilcraft:piercing_vengeance_focus`, `neovitae:hellforged_block` |
| magical artifice | `eidolon_repraised:soulbone_amulet`, `embers:eldritch_insignia`, `psi:ebony_psimetal`, `psi:ivory_psimetal` |
| sufficiently advanced magic | `draconicevolution:awakened_core`, `replication:replicator_enclosure`, `tmm:eldritch_logic_core` |

## Acquisition and economic gates (not certified by a registry match)

- Twilight Forest Final Boss remake 2.1.2 (#323) is not represented by a verified final-boss reward in the installed item census. Inspect pinned JAR registry, loot and death/reward handlers; add the actual renewable reward to Primordial Powers or return to owner if no suitable native drop. No substitute or guessed ID.
- `pastel:moonstone_core` appears as White Dye in the default-stack display; registry ID and two native static loot references identify candidate, but confirm actual stack identity and availability. `draconicevolution:awakened_core` displays Draconic Core; native recipe ID is components/awakened_core. Verify fusion tier/catalysts and no Chaos dependency. `goety:dark_metal_block` is Block of Dark Alloy. `the_bumblezone:essence_continuity` has white arena victory reward reference. 
- `wardentools:contagion_incarnation_skull` is registered but census only shows its block loot reference, not a boss drop; check native death code and renewable acquisition. Bone Key has dead_king_per_player loot reference; verify repeatability; Divine Soulshard has fire_boss loot reference. Pure Darkness and Tectonic Shard have no static loot references; audit custom reward code. Heart of the Deep, Unholy Blood, Dragon Heart, Akashic Singularity and other custom reward/crafting handlers also need source/acquisition review. Mine Key has alternate Mines loot provenance previously approved, not personal boss credit.
- Inspect all 41 intended alternatives against pinned source/loot/recipes, acquisition costs, available routes, boss-drop progression, and repeatability. Registry presence, static loot references and generic recipe-output references are not proof of live obtainability. No changed alternatives approved. Ensure at least one genuinely obtainable option per slot and no cheap bypass. Verify EMC graph/learned Stone duplication allowed, Replication excluded; do not apply an arbitrary price in this pass.

## Atomic integration cutover

1. Obtain final-boss reward and verify outstanding acquisition/IDs and exact pinned Occultism codec. Populate missing fourth Primordial Powers alternative. Review advanced offering prerequisites against actual pack graph (no forced Chaos or ProjectE).
2. Existing installed capture shows `tmm:projecte/philosophers_stone_boss_outer_glow` and `tmm:projecte/philosophers_stone_boss_outer_red`. The owner of `kubejs/server_scripts/projecte_boss_stone_and_powerflowers.js` on `feature/projecte-boss-trophy-stone-and-emc-audit` removes native `projecte:philosophers_stone` and `projecte:philosophers_stone_alt` then regenerates both trophy-grid orientations. A native JSON override alone cannot prevent that regeneration. Integration writer must retire its Stone registration only (or migrate relevant behavior to native data), suppress both native orientations, and leave Chaos Shard direct MK3+ and Red upgrade logic intact; separate Matter Upgrader guard remains. This turn authorizes no JS edits.
3. Remove recipe's false condition only in same change that closes both vanilla and KubeJS grid bypasses. Review obsolete trophy tag/quest guidance; preserve independent Newbie functional gate, upward conversion policy, EMC learning and all other approved downstream gates. Reconcile packwiz index/hash ownership.
4. Runtime acceptance: recipe reload and JEI show exactly 12 slots with intended alternatives, no duplicate Stone routes; verify 600 sec, consume one offering each + correct activation, return one Stone, reject missing/duplicate-category/unapproved items. Validate all alternatives, two-player Newbie behavior, first acquisition without ProjectE/Chaos, EMC duplication after learning, no Replicator production and Chaos Flower gates. No test/build/merge triggered here.
