# Vampire Ascension v2 — implementation and test contract

**2026-11-14 architecture change. Experimental branch; do not merge into stable or use permanent saves without live acceptance.**

Target: Minecraft 1.21.1 / NeoForge 21.1.265, Vampirism 1.10.13, JAWR 0.5.39.

## Player states and acquisition

| Entry | Native states | Economy |
| --- | --- | --- |
| Normal human | Neither vampire | Vanilla |
| Normal vampire infection (Sanguinare Vampiris) | Vampirism only | Untouched Vampirism mechanics, hunger and HUD |
| Drink Lilith's Blood as human | JAWR + Vampirism | JAWR canonical blood, native JAWR hunger and healing |
| Drink Lilith's Blood as normal vampire | JAWR added, Vampirism preserved | JAWR canonical blood, native JAWR hunger and healing |

The enrollment link is **one-way only**: JAWR becoming active initializes Vampirism if absent, but Vampirism infection never initializes JAWR. An ordinary vampire keeps progressing on the native Vampirism track. An ascended vampire keeps both independent progression tracks.

## Canonical resource

For ascended players, JAWR `BloodPoolHandler` is the *only source of truth* for blood. It runs native blood feeding, spending, regeneration, hunger drain and healing. Interception of JAWR `increaseBlood`, `decreaseBlood` and `tickBloodRegen` happens at **RETURN** and mirrors values proportionally into Vampirism's internal `BloodStats` for existing action checks.

Vampirism `BloodStats.addBlood` and `removeBlood` are intercepted in ascended state only. Feeding converts native Vampirism units into JAWR units; expenditures charge JAWR all-or-nothing. Vampirism `BloodStats.onUpdate` is suppressed only for ascended vampires so the two systems don't both process FoodData hunger/exhaustion/healing. JAWR's native hunger and passive healing are deliberately retained and **do cost blood** in their native implementation.

Vampirism's internal blood data is a **read-only gameplay mirror** except through the bridge's explicit JAWR-to-Vampirism mirror setter. DO NOT run the reverse canonical-blood mixins from PR #42 in this architecture.

## Cures and sunlight

All curing must remove BOTH vampire states. A JAWR cure preflights Vampirism's canLeaveFaction and removes the Vampirism faction before allowing JAWR `setLevel(0)`. A native Vampirism faction removal runs a preflight and, after removal, clears JAWR level. Cases where native mod hooks bypass these paths are covered by a persisted ascended-state marker and a server-side one-second reconciliation check, but those alternate cure routes still require live test coverage. Partial rollback on failed external mutation remains a risk; **do not use permanent saves**.

JAWR's `VampireEvents.handleSunExposure` is intercepted **before sunlight timers accumulate** inside the explicit `vampirism:vampire_forest` biome or the `vampirism:has_faction/vampire` biome tag. Its native `decreaseSunTick` method is called while sheltered. JAWR's cancellable `VampireAfflictionEvent.SunDamageApplied` is also canceled in those biomes as a defensive fallback. Outside it, original JAWR sunlight behavior is unchanged. No additional sunstone mapping is activated. Actual bytecode/runtime event timing still needs validation: this event might fire too late to prevent damage.

## Fang item and config

`defaultconfigs/vampirism-server.toml` sets `fangInfection=false`. Vampire Fangs remain craftable/lootable for vampire hunters' progression recipes. This setting also disables the Sanguinare Injection shortcut; mob-bite infection remains separate.

Packwiz `index.toml` includes and hashes the defaultconfig; `pack.toml` has the corresponding new index SHA256. **Existing saves must manually set** `fangInfection = false` in `saves/<world>/serverconfig/vampirism-server.toml`: defaultconfigs only applies to newly created worlds.

## Pilot config

All `tmm_transformations` experimental settings default false. In throwaway, backed-up worlds only:
`experimentalVampireChassis=true`, `experimentalAutomaticDualAcquisition=true`, and, until probes for other transformations are implemented, `experimentalAllowUnverifiedOtherProviders=true`.
Never activate the latter in a permanent world. `experimentalOperatorCure` remains OFF unless conducting a backed-up cure-specific test.

## Mandatory live acceptance gates

1. Human -> ordinary infected vampire: only Vampirism flag present; Vampire blood, FoodData, abilities, fang-disabled behavior unchanged.
2. Ordinary vampire -> Lilith ascension: both flags present; existing Vampirism level preserved; JAWR starts at its normal level, progresses via native books/quests.
3. Human -> Lilith: both native states present; verify JAWR blood is authoritative and Vampirism's mirror follows after feed, bite, costs and reload.
4. Ascended vampire: JAWR hunger spends its native blood; Vampirism's native exhaustion/regen doesn't run; healing spends exactly once; none of JAWR's normally free abilities are charged.
5. Vampirism feeding by cow/villager/bottles, JAWR feeding and Neo Vitae blood: exactly one gain. Check near full, 0, rounding carry, repeated use and save/reload.
6. Vampirism Blood Charge/other explicit spends: exactly one JAWR debit, insufficient balance refuses ability. Check JAWR's own expenditures.
7. Either cure path (JAWR Lilith garlic or Remove Curse; Vampirism Altar of Cleansing): BOTH vampire states absent; no half-cure on failed preflight. Validate even for operator/admin/alternate routes.
8. Vampire Forest day at open sky: no JAWR sun damage. Leave forest: native JAWR sun behavior resumes. Check boundary, logged out/in, daylight/rain, and whether double sun damage exists outside forest.
9. Single-player and dedicated server, two players at different states, disconnect/dimension, death and grave behavior.
10. Client HUD: ordinary Vampirism bar, ascended JAWR bar only. A client-only BloodBarOverlay mixin is **implemented but not yet gameplay-validated**; verify the pinned 1.10.13 class signature and actual player sync.

**Known blockers:** source-compatible compiled hooks are not equivalent to in-game success. In particular, cure atomicity across third-party hooks, complete forwarding of every Vampirism blood source (including auxiliary inventory APIs), and the ascended-only Vampirism HUD suppression remain to be proven in-game. This is not release ready.
