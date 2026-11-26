# Relics / Artifacts / Reliquified Artifacts — preliminary combined evaluation

2026-11-25; Atlas #410. Independent documentation-only review boundary for the installed artifact ecosystem; base 017447747232a0c7f9699f3801d60776b7ba68d8. Minecraft 1.21.1 NeoForge. No duplicate credit for Artifacts abilities replaced by Reliquified Artifacts. Source-backed preliminary rating, not full exact-JAR dossier.

## Provenance

- Relics 0.12.9, SHA1 6b17e3a3292bf678415b4763cf830df1d01ebc33, [release](https://modrinth.com/mod/relics-mod/version/CWPAvFMI), [version bump commit](https://github.com/Octo-Studios/relics/tree/25a86420867481870470ed1967802948b4aade85).
- Artifacts 13.2.5, SHA512 9079bc8a22c237881b22b38027bbab2a8a2e685a1f49c1e1e0e5796f3e0a5509a130d73753c7b2980f12e4f00845f8e8c967ee969f1eadcd52139c2780afe09d, [release](https://modrinth.com/mod/artifacts/version/lMfuK0o7), [version bump commit](https://github.com/ochotonida/artifacts/tree/7cf7dc42e322e13f096eea16cee17a4b400b75f7).
- Reliquified Artifacts 1.0.9, SHA512 8797ce1a64d483f74fccc380fbaa64821d04505e930b199c1300bbb6c1acfaa40ee2268b926802f3592915320ff500dd509a00308bfd7b69dd0f8c9e8f3172fb, [release](https://modrinth.com/mod/reliquified-artifacts/version/rAl46NpA), [version bump commit](https://github.com/Octo-Studios/reliquified-artifacts/tree/37a8e757b809b8d49aa554037cd94537ceb730fd), preceding da3d68cb422a867acd6ca45956e5197cea9ab8db explicitly fixes Artifacts 13.2.5 compatibility.
- All three Packwiz pins inspected. Source trees and en_us localization enumerated; targeted behavior inspected in Relics and RA implementation. No exact binary equivalence claim; attempted CDN download failed DNS in local research environment. Full binary census, 29-axis survey, all rank/stat curves, loot eligibility and instructional mapping remain due. Relics 0.12 beta is not the older 0.8/0.11 system.

## Ratings

0 absent, 1 assistance, 2 significant assistance, 3 independent, 4 superior, 5 transformative. Major 9–11 independent with personal/base breadth; Substantial 4–8; Minor 0–3.

| Domain | Score |
| --- | ---: |
| Combat | 4 |
| Protection | 5 |
| Survival | 5 |
| Gathering | 4 |
| Industry | 2 |
| Construction | 2 |
| Transportation | 4 |
| Logistics & storage | 2 |
| Life & agriculture | 2 |
| Death & recovery | 4 |
| Advancement | 5 |
| Total | 39 |
| Independent (>=3) | 7 |

Combat4 direct offensive/retaliatory active and passive effects (Midnight Mantle falling stars/constellations, Reflective Necklace orbs, Shield of Retaliation, Feral Claws, Power Glove, Fire Gauntlet, Vampiric Glove), not a complete weapon/armor crafting tree. Protection5 shields/parry, invisibility, damage avoidance, projectile repel, elemental and fall protection, extra HP, resistance and crowd control. Survival5 reusable regenerating food and grass consumption, lifesteal, foliage/water healing, air management, night vision. Gathering4 speed/Fortune/Looting/fishing, gold tooth drops, collection; no general ore generation. Industry2 probabilistic auto-smelting during mining and barter enhancement, not a factory network. Construction2 reach and mobility/access, no general automated block placement. Transportation4 actual recharge blink, path rewind including dimension transition under conditions, mount handling, water/lava traversal, bounded hover, jump and glide; no unconditional sustained creative flight or general portal network. Logistics2 local item/XP attraction and teleport to bearer, specialized fluid storage and mount serialization, no general managed inventory or assigned transport. Life2 limited breeding enhancements (native ring), food and fishing bonuses, pet damage; no actual crop cultivation or general livestock farm. Death4 multiple repeatable death saves, not post-death inventory retrieval. Advancement5 research and item-specific XP, upgrade/reroll/reset, rank modifiers and progression-sharing Experience Disperser, not innate permanent skill tree. Score reflects demonstrated native family capabilities, not every effective TMM condition. Ring restricted access does not determine any independent domain.

## Installed-family boundary

[RA ModItemsMixin](https://github.com/Octo-Studios/reliquified-artifacts/blob/37a8e757b809b8d49aa554037cd94537ceb730fd/src/main/java/it/hurts/shatterbyte/reliquified_artifacts/mixin/init/ModItemsMixin.java) redirects Artifacts registrations to RA-backed relic implementations under existing item IDs; both drinking hats share a class. Umbrella and both everlasting foods have separate register redirects. Do not count old artifact effects as simultaneously available. Source census: 5 Relics recipe JSON paths, 3 Artifacts, 1 RA; not effective recipes or registry counts. Localization includes legacy keys (e.g. Sphere of Self-Sacrifice) so names alone are not a reliable registered count; no score credit for unverified obsolete items. Mimi-Dust and mimic behavior require effective loot/registry audit.

## Targeted source-confirmed mechanics

Relics source base src/main/java/it/hurts/sskirillss/relics/items/relics/:
- ChorusStaffItem blink initial rolled range10–15 blocks, 3–5 charges, 15–30s recharge interval; rank modifiers 1 flicker,3 safe_fall,5 ascent. Actual destination selection and integer cooldown rounding affect use. These are initial ranges, not maxima.
- ClotOfTimeItem rewind initial3–5s stored path, rank1 invulnerability,3 oblivion,5 health rewind; server teleport path supports dimension change if target available. Does not rewind world blocks or inventory.
- GhostlyMantleItem spectral_escape actual LivingDeathEvent cancellation for player with usable equipped ability off cooldown; set HP at least1, reset deathTime, temporary FLIGHT and IMMORTALITY, cooldown. Initial cooldown100–125s, duration1–3s, reprisal rank5. Charge and duration values scale with ability; not maximum values. 
- LeafyMantleItem camouflage heals in leaves, revival uses nearby foliage under damage conditions; initial revival radius5–10, heal stat0.5–1, damage stat0.5–1, rank1 piercing. Full damage/leaf accounting and claims need follow-up; no universal rescue inferred.
- RingOfTheSevenDeadlySinsItem seven named abilities; pride/envy/wrath combat tradeoffs, sloth stationary defense and moving penalty, greed loot bonuses with destruction risk, gluttony hunger-conditioned stats, lust extra offspring with time-limited survival condition. TMM config/relics/relics/ring_of_the_seven_deadly_sins.yaml lootData.entries empty, explicitly not registry removal; no natural access assumed. Preserve existing Hard Mode design and owner restrictions; native capability only.
- Other named artifact functions supported by pinned localization: springy boots bounce/fall safety, cut glass boot multi-fluid bucket and traversal, Rider Flute stores/retrieves tamed rideable animals, Chef Hat chance of meatballs on kills, Hunting/Kinetic belts charm slots, Experience Disperser shares a fraction of relic XP, Piglin Mask barter and gold teeth. These need individual implementation checks for exact limits.

RA source base src/main/java/it/hurts/shatterbyte/reliquified_artifacts/items/:
- charm/ChorusTotemItem actual LivingDamageEvent.Pre: eligible equipped usable ability off cooldown, lethal threshold health+absorption, cap damage at threshold minus1, attempt safe-location teleport, set cooldown. Teleport failure does not undo damage cap; rank modifiers for recovery/disorientation/vanishing. Initial cooldown75–100s; not guaranteed teleport or maximum-level figure.
- feet/KittySlippersItem incoming dodge/fall reduction plus separate lethal pre-damage survival handler; initial survival chance2.5–10%, evasion10–25%; conditional, not nine guaranteed extra lives.
- EverlastingFoodRelicItem, EternalSteakItem, EverlastingBeefItem: actual regenerating durability; use fails when depleted, inventory tick restores over time. Initial regeneration25–30s per durability, 1–3 initial capacity; rank1 restoration,3 preservation,5 quick meal. Steak nutrition8/saturation modifier.8, beef3/.3. No ordinary infinite immediate consumption or free bulk food export; TMM diet interaction separate. 
- hands/PickaxeHeaterItem checks tagged ore and PickaxeItem, probabilistic smelting with rank pity/fortune mechanics, actual recipe lookup; not general furnace replacement.
- charm/UniversalAttractorItem range-based item pull/push, rank5 teleport to wearer, rank-dependent XP/projectile behavior; no designated inventory routing. 
- Further localization-backed abilities: Helium Flamingo time-limited hover, Cloud in Bottle extra jumps, Warp Drive reusable pearl cooldown, Rooted Boots consume grass into dirt for food/saturation, Charm of Sinking and Snorkel conditional breath, Crystal Heart recoverable reserve, Obsidian Skull limited lava buffer, Lucky Scarf/Angler Hat/Superstitious Hat loot enhancements. Numerical curves and exact eligibility not fully inspected.

## Follow-up and handoff

Combined item-by-item registry and all ability templates/rank/stat progression, actual loot tables, Curios slot interactions, death event order, pack diet/capture/Hard Mode/claim integration and teaching map remain research work. Existing config/relics.yaml enables extended config; ring-specific loot exclusion preserved. No change to existing placeholder artifacts quest chapter, configs, recipes, power ceilings, hooks, pins or builds. Quick CI and designated integration writer review; no gameplay acceptance claimed. Next Dragon Survival and Improvable Skills.
