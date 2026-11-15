# Standalone Monk — complete classic ability target, **before Tyz**

**Version:** 2026-11-15 • **Status:** approved design direction / implementation target, NOT installed gameplay.  
**Owner:** `tmm_monk` standalone Monk engine and gameplay; the separate core-engine/Hunter work should consume this contract.  
**Machine-readable source:** [25-entry parity manifest](standalone-pre-tyz-classic-parity-v1.json) (source semantics, implementation contract, tests, explicit exceptions).  
**Historical source:** [RWTema's 1.12.2 Monk Mod 1.4](https://github.com/rwtema/Monk-Mod), especially [default configuration](https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/config/MonkConfiguration.java) and [25 registered abilities](https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/factory/Factory.java). Original is All Rights Reserved; **do not copy original Java source, textures, sounds, or data assets**.

> **The acceptance goal is native capability parity, not a literal 1.12.2 port.** One maxed Monk must be able to use **all 25 registered classic abilities** through Monk advancement alone, without Vampirism, Werewolves, Tyz, MXP, Tyz SP, or any third-party spell mod. Of those, 21 were in the classic *default* 20-milestone configuration; the four additional registered abilities were **Armor Toughness, Health, Glass Touch and Full Water Breathing**. Preserve their availability too. One expressly approved substitution: original Creative Flight is **replaced by native Momentum Gliding** after verified Ender Dragon defeat; the Monk never obtains Creative Flight itself. Thus exact, literal flight parity is deliberately *not* a target.

## 1. This is the baseline — not a Tyz supplement

The standalone Monk owns **all classic baseline attributes and mechanics**, including attack damage, natural armor, toughness, max health, speed, jump, harvest *speed*, harvesting *tier*, fire/explosion/fall reduction and all unusual interactions.

**Tyz is optional, additive and later.** There is exactly one global MXP/Tyz wallet for the overall pack, and it can supply extra ranks or specialized skill families later. **It must never be the only source of a classic Monk ability, or the missing final stages of its native baseline.** Do not silently remap classic Strength → Tyz attack power, classic Swift → Tyz movement, classic Mining → Tyz mining speed, classic Protection → Tyz damage reductions, etc. This corrects the ownership implied by older Vampirism-era skill-placement documents.

All classic core techniques must be simultaneously attainable by one character. Do **not** gate them behind mutually exclusive branches, a 20-point/48-node draft tree, or purchases that mathematically prevent complete classic coverage. A native specialization tree can offer *enhancements*, interesting technique order and unique new powers, but cannot make the 25 classic powers unachievable together.

**Three distinct progression notions:**
1. **Cultivation rank/mastery** — native Monk engine, earned through actual activity. Eventually grants complete classic baseline as rank rewards or zero-cost, accessible native milestones.
2. **Optional Monk technique choices** — any separate native points may buy enhancements and novel talents, but may not steal a baseline feature already owed to every trained Monk.
3. **Tyz/MXP** — external optional, pack-global generic and Monk-specialized improvement, after all baseline classic powers function without it. No duplicate reward, purchase or effect value.

## 2. Complete registered-ability inventory

Original stage references are the **old default milestones**, not requirements for the standalone game. The four `off-default` abilities were registered and configurable in the source but never awarded by the default stage sequence. They're included as native capabilities now.

| Classic ID | Native capability | Original milestone | Required outcome |
| --- | --- | --- | --- |
| `armor` | Natural Armor | 2,3,5,7,13,16,19 | Classic +20 armor at final default stage |
| `armor_toughness` | Natural Toughness **(off-default)** | registered but unused by defaults | Optional registered ability; new native value to tune |
| `blindness` | Blindness Ordeal | 17 then removed at 18 | Temporary trial state, NOT a purchased buff |
| `blink` | Blink | 11 | ~16-block short-hop; cooldown and safe-arrival checks |
| `catch_arrows` | Catch Arrows | 8 | Catch valid incoming arrows while unarmed |
| `kiss_creeper` | Creeper Kiss | 14 | Empty-hand creeper pacification, persists |
| `explosion_resistance` | Explosion Resistance | 14 | Classic ~50% blast damage reduction |
| `feather_fall` | Feather Fall | 16 | Classic ~80% fall damage reduction |
| `fly` | Ascension / Momentum Gliding | 20 | Substitute: post-Dragon Momentum Glide, NOT Creative Flight |
| `glass_touch` | Glass Touch **(off-default)** | registered but unused by defaults | Original off-default glass-only silk harvest |
| `potion_immunity` | Purification | 19 | Continuous normal harmful-potion cleanse at endgame |
| `health` | Vitality **(off-default)** | registered but unused by defaults | Registered off-default native max-health trait |
| `hunger` | Hunger Discipline | 2 | Classic about 50% ordinary hunger drain |
| `jump` | Monk Leap | 6 | Classic 1.2× jump plus 3 safe-fall blocks |
| `mining` | Hand Mining | 1,4,9,13,17 | Separate effective hand tool tier and intrinsic hand speed |
| `fire_resistance` | Fire Resistance | 12 | Classic about 50% fire-damage reduction |
| `lava_protection` | Lava Protection | 12 | Contextual lava handling distinct from fire protection |
| `swift` | Swift Step | 6,8 | Sprinting-oriented speed with sprint double modifier |
| `step_up` | Step Assist | 8 | One Step Assist; crouch changes behavior |
| `strength` | Iron Palm | 1,6,7,17,18 | Classic +1→+16 unarmed damage ladder |
| `tame_animals` | Animal Friendship | 5 | Calm eligible animals / promote breeding |
| `water_walking` | Water Walking | 10 | Support over water; sneak/dive escape |
| `water_breathing_partial` | Partial Water Breathing | 3 | Extended air but drowning remains possible |
| `water_breathing_full` | Full Water Breathing **(off-default)** | registered but unused by defaults | Original registered off-default full breathing |
| `withering_stare` | Withering Stare | 15 | LOS gaze on undead; special Wither boss interaction |

The [manifest](standalone-pre-tyz-classic-parity-v1.json) defines a **specific Minecraft-facing effect and acceptance test for each row**. Merely registering a node, tooltip or attribute name does not pass this target. Each unlocked ability must work when the Tyz mod is absent.

## 3. Gameplay design by family

### Physical defense and offense

**Natural Armor** must work without worn armor and eventually provide substantial protection comparable to the original progression (reference increments +1/+2/+4/+8/+14/+18/+20; final original +20). **Natural Toughness** is an additional *registered, never-default* classic effect; it gets a native accessible milestone and independent numeric balancing. **Vitality/Health** is another registered, never-default effect: give Monks a bounded native max-health improvement, independent of the Werewolf tree and Tyz.

**Iron Palm** is intrinsic barehand combat: original reference +1 → +2 → +4 → +8 → +16 added damage by its late milestones. The initial Open Palm pilot remains a useful first demonstration, not the whole class baseline. The full ladder must work without purchasing any Tyz Strength/attack-stat skill. Use stable modifier IDs, real equipment eligibility and no repeated duplicate buff applications. Clearly distinguish bare hands from allowed magical implements; a Monk may hold an Ars/Iron's focus to cast spells, but may not claim **empty-fist melee** damage while wielding a conventional weapon.

**Catch Arrows** is a real frontal/reaction projectile impact interaction, not a global projectile immunity. Eligible arrows are negated or physically caught before hit resolution; recover only pick-up-allowed arrows and never duplicate Infinity or special ammunition. No bow, innate spirit arrow, or ranged Monk projectile is required.

**Withering Stare** is a separate **sustained gaze attack on eligible undead**, with LOS and range checks. The original source applies Wither to undead at approximately 20 blocks and explicitly damages the exposed phase of the Wither boss. Replicate that kind of distinctive anti-undead behavior rather than treating it as generic spell potency or an unlock for a new ranged weapon.

### Movement and traversal

**Swift Step** has a staged intrinsic running-speed bonus: the classic source's final `increase=0.8` doubles its movement-speed modifier while sprinting. Preserve meaningful sprint specialization; calibrate the final percentages against 1.21.1 and other speed modifications, rather than blindly copying the old attribute calculation.

**Monk Leap** is an upward impulse improvement (original ×1.2) and a separate small safe-fall increase (original subtracts 3 from fall distance). **Step Assist** is a distinct traversal ability: the Monk steps over an ordinary one-block rise, with crouching reducing the assist. Do not substitute Step Assist for Leap, and do not buy a second duplicate Apotheosis Elastic skill.

**Blink** is its own native ability: a small aimed teleport (original range roughly 16 blocks, 40-tick cooldown). The client may preview a destination but only the server authorizes actual range, collision, world boundary, protection/claim and chunk validity. No pass-through-wall exploit or external spellbook needed.

**Water Walking** must truly support standing and moving across water without spawning blocks; deliberate crouch or swimming must still permit the Monk to descend. Avoid the classic implementation's client-server collision desynchronization. **Feather Fall** independently reduces large fall damage (original multiplier 0.2, i.e. about 80% mitigation), rather than merely increasing safe-fall distance.

**Ascension** deliberately differs from the classic: the original `fly` set Creative-like `allowFlying` after a void-fall trial. We **do not** do that. At a server-verified Ender Dragon milestone, the standalone Monk gains **Momentum Gliding** — actual controlled non-creative aerial motion, with meaningful momentum and limited thrust, and with no purchased/installed Curio requirement. A permitted Draconic Evolution flight Curio can grant Creative Flight separately and must not be disabled by Monk, but it **cannot** be required to finish Monk cultivation. Void-falling is not a mandatory advancement deed.

### Mining and gathering

Classic Mining was a **single ability with two mechanical dimensions**: harvest level and barehand mining speed. The new engine must own **both** before Tyz. Native stages must confer real effective tool tiers for the current pack's block/item tags, plus independent native speed advancement. Original harvest ordinals 0/1/2/3/5 are historical, **not** direct 1.21.1 tool tags. Original `speed_increase` values 0/2/5/9/12 reflect a large intended late-game acceleration; use as tuning anchors but avoid breaking the pack economy through infinite-area or protected-block mining.

The finite hand-tier path should be source-verified against stone, metal ores, diamond ores, obsidian and real modded tier requirements; no false drops for insufficient harvest capability. Keep **TreeChop's actual removed-log count** and successful player-owned magic mining in native mastery awards, and never award a claim-denied/canceled break. Separate block-breaking mastery from Tyz's MXP system.

**Glass Touch** is required because it was a registered classic ability, even though it was never in the default milestone sequence. It specifically permits harvesting ordinary glass by hand; it is not a second paid universal Silk Touch and must avoid block-entity/loot duplication.

The agreed **Treasure Hand** extension provides one manually switchable **Fortune I ↔ Silk Touch I** native hand-harvest mode, at the appropriate stage, with exactly one mode operating per successful break. This must be functional **before Tyz**; optional Tyz Fortune ranks may later improve it, but do not gate its baseline. Glass Touch remains a distinct earlier/narrower capability, rather than an accidental duplicate purchase.

### Resilience, survival and meditation

**Fire Resilience** mitigates fire-source damage (original 0.5×). **Lava Protection** is distinct: source behavior conditionally protects/recovers a Monk in lava when they were not already burning. Do not merge them into generic universal fire immunity; test both normal flames and lava contact, including their interactions.

**Explosion Resilience** mitigates legitimate explosion damage (original 0.5×). Its handler should be damage-source aware and must not accidentally reduce unrelated magic or protected boss damage. **Purifying Meditation** was powerful in the original: once unlocked, it removes ordinary negative potion effects repeatedly. A late-game native effect must provide comparably thorough vanilla harmful-potion cleansing **without Tyz**. Explicit exceptions for boss/system curses are acceptable balance decisions, but document and test them; do not simply replace source parity with a weak once-a-minute potion cleanse.

**Hunger Discipline** lowers ordinary hunger consumption (source configured 50% chance of restoring a lost food point), but doesn't create infinite food or cancel the distinct native reward for eating *new* foods. **Breath Meditation I** slows drowning/partially restores air (original adds small amounts of air under water) and does **not** permit infinite underwater stays. **Breath Meditation II** is full underwater breathing (original registered but unused by defaults), independently unlockable natively and distinct from stage I.

**Animal Friendship** is not a new pet collection system. The original allows an empty-hand interaction to calm a revenge-targeting passive animal or encourage an eligible adult animal to breed; preserve those functional interactions. **Creeper Kiss** is its deliberately humorous counterpart: empty-hand creeper interaction harmlessly pacifies that creeper; persist the result without commandeering every creeper AI in the world.

**Blindness Ordeal** is an intentional negative source ability, not a positive power and not an indefinite punishment. Original milestone 17 enforces Blindness until the player completes a blind-kill trial. Preserve the *option* for an authentic trial and related rewards, but provide an accessible alternative, server-owned start/end flags, and cleanup after death/relog/abort. A Monk must not be stuck permanently blind due to a quest or networking issue.

## 4. Classic deeds and the new advancement scheme

**Advancement can change; capability coverage cannot.** The original stages are reference points and recognizable optional trials, not a compulsory linear remake.

| Phase | Classic inspiration | New progression target |
| --- | --- | --- |
| First Sunset | Original sky contemplation and first log trials | **First Sunset is still initiation**: ten seconds of qualifying west-facing, sky-visible sunset contemplation (200 server ticks), with one authoritative native membership grant |
| Novice cultivation | Breaking wood/stone, first breath and hunger, taming | Early empty-hand gathering, native Open Palm/Poise, partial breath, hunger discipline, early mastery |
| Adept cultivation | Sprint, undead, arrow-dodge, ore challenges | Strength progression, sprint speed, jump, Step Assist and arrow catching; progressively better hand mining |
| Advanced cultivation | Moon/boat/endermen, fire, obsidian, creeper | Water Walking, Blink, lava/fire protection, high harvest tiers, Creeper Kiss |
| Master cultivation | Wither skeleton gaze, falls, blind training, Wither | Withering Stare, Feather Fall, max defense/strength, full potion cleansing, optional Blindness Ordeal |
| Ascension | Source void ordeal and Creative Flight | **Verified Ender Dragon → native Momentum Gliding**; endgame evolution without Creative Flight |

The current pilot's thresholds (rank 2 at 20 mastery, rank 3 at 55, rank 4 at 105) may be retained for the first client tests. The new core engine owns the full advancement curve and may use configurable rank windows rather than forcing a strict 20-level copy.

Native mastery sources: **completed, legitimate hand breaks** (TreeChop credits actual removed logs, not one action); verified direct hostile defeats including correctly attributed spells; **one-time discovery of edible registry IDs** after successful consumption; and optional deeds/trials. Avoid placed-block loops, dummy kills, farmed ownerless spells and repeated NBT-variant food rewards. Always respect claims and multiplayer attribution. Native Monk mastery is **not MXP**, and awarding it must not mint Tyz SP.

**One character can collect the whole classic suite.** Rank-up baseline effects can be auto-granted, while optional technique nodes can permit choosing the order. But no mutual-exclusion choice or insufficient available points may permanently lock a classic ability out. For the four registered-but-off-default abilities, define optional native late-game feats or accessible bonus paths, not a Tyz gate.

## 5. Core engine interface expected from the parallel work

This is a functional contract, **not a demand to rewrite or duplicate the other chat's core implementation**.

**Native state** must be able to answer: is the player a Monk, current cultivation rank, earned mastery, which classic ability/stage is unlocked, whether discipline conditions allow its use, which trials are completed, food-discovery history, native technique-point balance if any, Fortune/Silk mode, Armor Lock state, reserved fist status, cooldowns and world milestone credit. Keep the authoritative data server-side with versioned persistence through logout, death/clone and dimension changes.

**Effect layer** uses that state through a narrow accessor/predicate; it must not import `de.teamlapen.vampirism` or ask another mod to provide a native Monk level, skill-point handler or membership. Attribute and event adapters must reconcile when changing armor/weapon, rank, skill choice, death, dimension or state. Removable modifiers should not stack in saves.

**Visual layer** needs a native Monk progress/technique UI: clear rank/mastery, learned baseline abilities, available native choices, original-inspired optional deeds, current Armor Lock and Treasure Hand mode, and actionbar feedback. A bottom-right radial deed/progress display remains a desirable later UI target, not an excuse to delay the functioning baseline.

**Input/network layer** must own Blink, Treasure Hand switch, Armor Lock switch and any technique actions with server-side validation and cooldowns. The first hotbar position (key `1`, index `0`) is reserved as a **real empty hand** rather than an item. Conventional armor and weapons still suppress qualifying techniques; legal Curios remain permitted.

**Compatibility layer** may optionally integrate TreeChop, Ars, Iron's, Roots, Apotheosis, Witchery, transformation arbitration and Tyz. None is allowed as a *mandatory class dependency*. Monk's unarmed physical baseline and unusual interactions must run on NeoForge + Minecraft alone. Spellbooks remain the Monk's intended ranged magic in the full pack; **do not generate Spirit Projectile**.

## 6. Additional free traits already approved

These are required as **standalone Monk-native utilities**, although not part of the historical 25 abilities. They cost **zero** Monk or Tyz points:

- **Reserved Fist:** exactly **hotbar slot 1**, numeral `1`, player inventory index `0`. Never fill it through pickup, drag, shift-click, chest hotbar swaps, creative/admin insertion, or modded recovery; safe no-loss relocation and full-inventory behavior. All other eight hotbar slots remain available for spells/items.
- **Armor Lock:** default **ON**, independent toggle with **no purchase**. While ON, reject new equipment in vanilla head/chest/legs/feet (including grave mod restores) but do not interfere with Curios. ON always succeeds. Removable armor goes to storage only when safely possible; **non-removable, damageable equipped armor takes one ordinary durability-wear attempt per 20 server ticks per qualifying piece**, until broken. Ordinary Unbreaking/repair and Life Mending apply. The Life Mending repair-versus-wear cycle can intentionally drain the wearer's health to death; Armor Lock itself does not directly inflict health damage or force a bound item out. OFF stops additional wear but doesn't waive existing Monk power restrictions.
- **Treasure Hand:** one persistent manual **Fortune/Silk Touch** toggle for legal barehand breaks, with correct loot context and only one effective mode at once. **Baseline works without Tyz**; Tyz may later enhance Fortune but not replace this native ability.

These must be tested for no item loss, no duplication, proper cooldown/persistence and no armor/Curio false positives.

## 7. Suggested implementation slices and their exit tests

| Slice | Functionality delivered | Exit gate |
| --- | --- | --- |
| A — identity | Remove Vampirism classes, hard Gradle and mod metadata deps; standalone First Sunset and persistent player state | Game loads with Vampirism/Werewolves/Tyz absent; one valid initiate saves/reloads correctly |
| B — minimum 6 | Port root + Open Palm, Unarmored Poise, Stone Fist, Fleet Footwork and Step Assist to native state/technique mechanics | Native progression and purchases correctly grant/respec/suppress, zero Vampirism references in runtime |
| C — complete classic numerics | Strength, armor/toughness/health, swift, jump, fall, hunger, fire/explosion, hand-speed/tier progression | Correct late cultivation **without Tyz**; values and modifiers stable across gear/death |
| D — classic interactions | Water breathing I/II, Water Walking, Blink, Catch Arrows, Animal Friendship, Creeper Kiss, Withering Stare, Glass Touch, negative effects and Blindness Ordeal | Each source-equivalent action works server-side, with explicit tests for targeting/claims/LOS |
| E — native safety/harvest | Protected fist slot, Armor Lock, Binding durability attrition, Treasure Hand toggle | No loss/dupe, exact hand loot, default lock behavior and approved Life Mending risk |
| F — capstone and pack compatibility | Ender-Dragon verified Momentum Glide, optional TreeChop/magic adapters, world milestones | Fully cultivated Monk works with Tyz absent, then with Tyz present; no Creative Flight or stale states |

**Definition of done:** Review the manifest's full 25-row acceptance cases and the above criteria. If **any** classic source ID is only present as a design node, tooltip or Tyz feature, the standalone Monk is **not yet parity complete**.

## 8. Open numeric and interaction tuning (not parity exemptions)

- **Final attribute magnitudes:** original +16 unarmed damage, +20 natural armor, high sprint multipliers and +12 mining speed were intended for a different ecosystem. Preserve a recognizable capability and meaningful high-end strength *independently of Tyz*, then benchmark versus this pack's gear, bosses and superpowered transformations before ratifying exact numbers.
- **Off-default toughness and max health:** original factory registered these, but default config provides no numeric rewards. Expose real native attainable effects with explicit modern values after benchmark tests, not invented legacy constants.
- **Purification coverage:** ordinary vanilla harmful potion effects must be extensively suppressed at late rank. Use declarative boss/curse exceptions only when required, and document each exception rather than calling an anemic cleanse “parity.”
- **Withering Stare and boss interaction:** validate the Wither phase behavior with actual boss immunity, tags and source contracts; do not bypass legitimate invulnerability.
- **Conflicts with Hunter/transformations:** the core engine owns one authoritative eligibility decision. The Monk's class is not a Vampirism faction. Spell use does not itself change species. Hunter coexistence and species conflicts must be explicitly decided in the common class engine, not inherited from a removed mod.

## 9. Relationship to prior design work

- [Monk PR #71](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/71) contains the Vampirism-dependent **prototype**, first six live abilities and **48-entry conceptual native tree**. This document supersedes its **dependency model** and the assumption that native ability baseline must be bought through its scarce points; do not remove historical research or confuse its green CI with standalone gameplay.
- [Original 25-ability audit](../MONK-ORIGINAL-AUDIT.md) and `compat/monk/profiles/original-monk-abilities-1.4.json` are historical sources. This new contract is **the release acceptance source of truth** for pre-Tyz classic parity.
- [Current Monk design](../../compat/monk/design/README.md) contains the expanded 48-node concept and novel techniques. Its extra Apotheosis/speculative affixes are **later elective material**, **not required to call the standalone classic suite complete**.
- [Nine-tree Tyz blueprint](../TYZS-NINE-TREE-BLUEPRINT-2026-11-15.md) remains a separate external progression project and must revise its old assumption that native Monk membership is held in Vampirism. The standalone Monk state is the authoritative class eligibility. Tyz's Monk tab, rank wallets and MXP adapters come **after** this target is implemented.

**Design independence:** This document and manifest live on a design-only branch. They change no `mods/`, `compat/monk/src`, `manifest/integration-sources.json`, pack modlist, runtime skill JSON or playable progression.
