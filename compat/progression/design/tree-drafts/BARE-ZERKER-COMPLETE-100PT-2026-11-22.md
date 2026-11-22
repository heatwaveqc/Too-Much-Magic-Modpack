# Bare Zerker — Complete 100-Point Skill Tree

Too Much Magic · 2026-11-22 · DESIGN ONLY — NOT IMPLEMENTED

Stacked design branch: Bare Zerker → Gilded PR #547 → Forest #536 → Cave #530 → Lich #526 → Werewolf #520 → Vampire #518 → Sea #514 → original #495. This document and the JSON/MD ledgers are design artifacts, not a runnable Path, mod JAR, Packwiz change, or gameplay test.

## Identity and locked free powers

The Bare Zerker is the armor-spurning melee berserker: sword and axe, no shield, a blood-red wool cap, blue body paint, genuine fury from hurting enemies, and survival through repeated real hits. It is generally poor at ordinary magic but has approved goals for specific Neo Vitae and EvilCraft blood-magic synergies. It has NO food restrictions.

**Fury is free:** max 100; gain EXACTLY 1 Fury for 1 actual post-mitigation damage dealt to a target, with no award for taking damage, merely killing, chafing, mining a magic block, or a skill triggering. It decays at exactly 1 Fury per second. A damage-increasing purchased skill may only improve Fury through a larger real damage receipt, never a second invented Fury award. No purchased rank may raise maximum Fury or slow decay.

**Incoming damage is free:** each distinct actual received damage instance adds exactly ONE second to Regeneration I and exactly ONE second to Resistance I. Durations append; hit frequency does not automatically increase effect levels. Ordinary vanilla damage/hurt invulnerability frames are removed for Bare Zerker, but true separate immunities remain. Paid Regen/Resistance Amplifier ranks may raise levels to II and III, never change the one-second duration rule or apply retroactive protection to the triggering hit.

**Too Angry to Die is FREE:** on a would-be lethal hit, test Fury from BEFORE that hit. Strictly greater than 50 permits a COMPLETE vanilla Totem of Undying activation including effects, sound and visuals, then drains ALL currently held Fury to zero. Exactly 50 fails. No fixed cooldown and no arbitrary once-per-day limit; repeat as soon as real damage dealt refills Fury above 50. A fatal hit cannot generate Fury to qualify. Paid Last Stand does not sell or alter the life-saving activation.

**Chest taboo and clothing are free:** any chest item causes recurring funny-message chafing damage. Leather boots, two-string/one-leather loincloth, four-wool color-patterned tartan kilt and white wool Cap (three top-row wool, then one wool at each side of second row) are zero-armor clothing. Clothing except Cap dislodges on damage; inventory/drop behavior is still under review. Any active shield suppresses the Path's sword/axe purchased combat bonuses. No bought node grants armor or an exception to the chest rule.

**Red Cap and Woad are free temporary mechanics:** dip white cap using real EvilCraft-compatible blood to get a timed Red Cap with Strength and Resistance while worn. Blue dye-based Woad gives timed, player-owned body paint effects, with owner goal of practical parity with Neo Vitae Sentient Armor without equipping its chestplate. Exact power catalog, source cost, recipe, uptime and stacking require an owner-approved source audit. Purchased ranks improve a validated duration; they never grant an unverified source power.

**XP is independent:** genuine kills and approved destruction of real magical objects may award shared MXP, subject to source owner and anti-place/break farm rules. They award NO Fury unless the player truly dealt target damage. Core is separate. No new class-owned XP wallet.

## Budget by branch

| Branch | Skills | 1-point ranks |
|---|---:|---:|
| Fury Combat | 7 | 34 |
| Endurance Under Fire | 6 | 22 |
| Blood & Woad | 4 | 22 |
| Magebreaker | 3 | 12 |
| Berserk Mastery | 2 | 10 |
| **Total** | **22** | **100** |

All 100 purchased ranks cost exactly one point. The older 13 names/IDs survive and nine distinct paid powers were added. Numerical effects are design candidates, not owner-approved balance or running Java code.

## Full 22-skill table

### Fury Combat — 34 points

| Skill and stable ID | Ranks | Effect candidate | Gate / prerequisite | Source hold |
|---|---:|---|---|---|
| **Reckless Sword** (tyr_progression:bare_zerker_reckless_sword) | 6 | +3% direct melee sword damage per rank (+18% max), only with empty chest and no shield; never a separate Fury award. | entry; none | Verify actual sword-hit and shield/slot attribution. |
| **Cleaving Axe** (tyr_progression:bare_zerker_cleaving_axe) | 6 | +3% direct melee axe damage per rank (+18% max), chest empty and shield absent; no extra Fury. | entry; none | Verify actual axe-hit and shield/slot attribution. |
| **Battle Rhythm** (tyr_progression:bare_zerker_battle_rhythm) | 5 | Actual successful shieldless sword/axe direct hits within six seconds build up to three nonstacking-renewal Rhythm stacks; each stack grants +1.5% damage per purchased rank (+22.5% at three stacks and five ranks). No stack from missed/blocked/non-damaging hits. | advanced; reckless_sword | One real damage receipt per melee hit; expire combo state. |
| **Bloodied Drive** (tyr_progression:bare_zerker_bloodied_drive) | 5 | On a real personally credited hostile kill by direct sword or axe, +3% movement speed per rank (+15%) for five seconds; refresh, never stack. Kill itself adds no Fury. | entry; none | True melee kill ownership and dummy-farm limits. |
| **Fury Finesse** (tyr_progression:bare_zerker_fury_finesse) | 5 | While real Fury >=25, chest empty and shield absent, +2% sword/axe attack speed per rank (+10% max). No change to Fury decay, max, or gain. | advanced; battle_rhythm | Read-only Fury predicate and equipment state cleanup. |
| **Headsplitter** (tyr_progression:bare_zerker_headsplitter) | 4 | At actual Fury >=50, +3% axe direct melee damage per rank (+12% max) against genuinely armored hostile mobs, with normal armor mitigation retained. | advanced; cleaving_axe | Armored target and post-mitigation Fury damage accounting. |
| **Relentless Pursuit** (tyr_progression:bare_zerker_relentless_pursuit) | 3 | Following actual hostile direct sword/axe hit, +3% sprint speed per rank (+9% max) for up to three seconds while approaching the same living target. No dash button. | advanced; bloodied_drive | Attacker/target relation and target-death cooldown cleanup. |

### Endurance Under Fire — 22 points

| Skill and stable ID | Ranks | Effect candidate | Gate / prerequisite | Source hold |
|---|---:|---|---|---|
| **Skin Hardened** (tyr_progression:bare_zerker_skin_hardened) | 6 | -2% eligible direct physical damage taken per rank (-12% max) while chest empty, before damage response; no wearable armor item. | entry; none | Damage pipeline ordering and Resistance stacking. |
| **Regeneration Amplifier** (tyr_progression:bare_zerker_regen_amplifier) | 2 | Rank 1 upgrades FREE on-damage Regeneration I to II; rank 2 to III. Each actual distinct incoming damage instance still adds exactly ONE SECOND and no duration increase. | advanced; skin_hardened | Integer amplifier/status duration interaction with no vanilla hurt frames. |
| **Resistance Amplifier** (tyr_progression:bare_zerker_resistance_amplifier) | 2 | Rank 1 upgrades FREE on-damage Resistance I to II; rank 2 to III. Each actual distinct incoming damage instance still adds exactly ONE SECOND; cannot retroactively negate triggering damage. | advanced; skin_hardened | Real damage ordering, mitigation caps and distinct hit receipts. |
| **No Armor No Fear** (tyr_progression:bare_zerker_no_armor_no_fear) | 5 | +2 max HP per rank (+10 HP full) while enrolled and chest empty, with health clamping and no equipment-owned trait. | entry; none | Attribute add/remove on chafe, cure, respawn and respec. |
| **Pain Into Purpose** (tyr_progression:bare_zerker_pain_into_purpose) | 4 | Following a qualifying hostile damage instance, +2% shieldless sword/axe melee damage per rank (+8% max) for three seconds; refresh only, no stacks or Fury from received pain. | advanced; regen_amplifier | Exclude self-chafe and attack-reflection farms pending owner decision. |
| **Unbroken Stance** (tyr_progression:bare_zerker_unbroken_stance) | 3 | Following real hostile incoming hit, +10 percentage points of knockback resistance per rank (+30 pp) for four seconds; no invulnerability frames added. | advanced; resistance_amplifier | True hostile incoming hit/knockback attribute caps. |

### Blood & Woad — 22 points

| Skill and stable ID | Ranks | Effect candidate | Gate / prerequisite | Source hold |
|---|---:|---|---|---|
| **Red Cap Fortitude** (tyr_progression:bare_zerker_redcap_fortitude) | 5 | +10% per rank (+50% max) to owner-verified temporary blood-dipped Red Cap Strength/Resistance duration while Cap worn; does not sell base cap or effects. | entry; none | Blood container/Cap item, real baseline duration, stacking with incoming-hit Resistance; owner hold. |
| **Woad Attunement** (tyr_progression:bare_zerker_woad_attunement) | 5 | +10% per rank (+50% max) to genuine blue Woad application's duration, once pinned Neo Vitae 1.1.53 Sentient Armor-equivalent ability matrix is owner-approved; no fake armor slot or invented powers. | entry; none | Owner/source hold: complete pinned Neo Vitae upgrade-by-upgrade equivalence, costs and exclusions. |
| **Neo Vitae Bloodmaster** (tyr_progression:bare_zerker_neo_bloodmaster) | 6 | CANDIDATE +3% per rank (+18% max) to actual personally performed source-verified Neo Vitae blood magic beneficial effect output, with real EV payment and no altar/machine resource inflation. | advanced; woad_attunement | OWNER HOLD: select exact 1.21.1 source abilities, costs and magnitude; no blood duplication. |
| **EvilCraft Hematurgy** (tyr_progression:bare_zerker_evilcraft_bloodcraft) | 6 | CANDIDATE +3% per rank (+18% max) to actual verified personal EvilCraft blood-powered effect duration or strength; real blood debit required; no infuser/ritual-machine output multiplication. | advanced; redcap_fortitude | OWNER HOLD: select exact effects/transactions and bound use of canonical EvilCraft blood. |

### Magebreaker — 12 points

| Skill and stable ID | Ranks | Effect candidate | Gate / prerequisite | Source hold |
|---|---:|---|---|---|
| **Antimagic Grit** (tyr_progression:bare_zerker_antimagic_grit) | 5 | -3% per rank (-15% max) source-tagged direct magical damage with chest empty, not a blanket reduction against every modded caster or true damage. | entry; none | Source-specific incoming magic damage tags and resistance stacking. |
| **Spellbreaker** (tyr_progression:bare_zerker_spellbreaker) | 4 | +4% per rank (+16% max) shieldless direct sword/axe damage to a target actively performing a confirmed real spell cast, not simply wearing a mage item. | advanced; antimagic_grit | Server real spell casting window and damage attribution. |
| **Hex Shatter** (tyr_progression:bare_zerker_hex_shatter) | 3 | -8% per rank (-24% max) duration of source-confirmed incoming harmful SPELL-origin status effects, without removing curses, ordinary potion effects or objective magic rules. | mastery; spellbreaker | Verified harmful spell event origin and safe duration modifier. |

### Berserk Mastery — 10 points

| Skill and stable ID | Ranks | Effect candidate | Gate / prerequisite | Source hold |
|---|---:|---|---|---|
| **Last Stand Mastery** (tyr_progression:bare_zerker_last_stand_mastery) | 5 | For three seconds after a genuine FREE Too Angry to Die full vanilla Totem activation, +4% shieldless sword/axe direct melee damage per rank (+20% max). Does not alter >50 prehit condition, full Fury drain, Totem effects or zero cooldown. | mastery; resistance_amplifier | True vanilla Totem activation postevent and no second death-save. |
| **Red Mist** (tyr_progression:bare_zerker_red_mist) | 5 | While actual Fury strictly >75, chest empty and no shield, +3% sword/axe direct melee damage per rank (+15% max); threshold loss immediately disables. No Fury generation or decay alteration. | mastery; fury_finesse | Read-only Fury threshold, real outgoing damage and equipment cleanup. |

## Fury sequencing and no-frame combat

Outgoing damage must resolve via the authoritative server damage pipeline and actual target result. Only that result, once, may increase Fury. Clarify the exact source semantics for absorption, excess overkill and personally attributed indirect/summoned damage; do not silently treat a nominal pre-armor hit as HP loss. Fury decay remains exactly 1/s even while Red Cap, Woad or mastery bonuses are active.

Incoming hits may arrive rapidly because ordinary hurt frames are removed. Each eligible instance appends one second of both effects. Raising a potion amplifier does not extend time or make a new duration grant. Resistance III is a strong proposed upper bound; source and balance review must ensure it cannot combine with Skin Hardened and other sources into total invulnerability. The triggering hit must use its previous defense state, not new resistance granted by that same hit.

Chafing creates an open source-design edge case: the owner says every distinct actual damage instance grants Regeneration and Resistance, which may include self-inflicted chafe; automatically excluding it would rewrite the contract, but automatically counting it could create a self-healing exploit. This must be deliberately resolved and tested. Purchased hostile-hit boosts require genuine hostile damage, not chafe farming.

Shields suppress every sword/axe bonus, including combo, threshold, after-Totem and magebreaker damage enhancements, rather than only Reckless Sword and Cleaving Axe. Free Fury nevertheless continues following the locked actual-damage rule.

## Woad/Sentient Armor parity audit — mandatory before live powers

Neo Vitae's Sentient Armor is a broad upgrade system rather than one fixed buff. The upstream wiki enumerates Body Builder, Healthy, Tough, Fierce Strike, Charging Strike, Pin Cushion, movement upgrades, poison/fire mitigation, Gills, equipment repair, Skilled, Elytra and Curios upgrades, often tied to an actual chestplate/full set, upgrade-point budgets, rituals or tomes. This wiki inventory may not exactly match the pinned Neo Vitae 1.21.1 build and is NOT proof of v1.1.53 effect IDs.

Before making a Woad effect or Woad Attunement rank purchasable, write an ability-by-ability matrix specifying: exact pinned Neo Vitae ability ID and effect; source level/tier/EV cost; player-owned Woad body-effect analogue and any exclusion; owner decision on whether to copy it; recipe and application method; duration; Red Cap/Resistance/Fury stacking; actual source ingredient debit; physical-rendering and Curios/armor-slot exclusions. No ghost Sentient Armor chestplate, hidden Curios sockets, unsourced elytra wings or unrestricted highest-tier upgrade. If the source effect cannot be delivered as a body-paint bonus, hold/revise it rather than asserting parity.

Reference: https://github-wiki-see.page/m/breakinblocks/NeoVitae/wiki/Sentient-Armor (version-aware background only).

## Blood synergy and poor ordinary spellcasting

Owner issue #454 approves targeted Neo Vitae and EvilCraft blood-synergy skills but NOT exact skill powers or magnitudes. Neo Vitae Bloodmaster and EvilCraft Hematurgy are therefore both marked source/owner HOLD. The +3%-per-rank benefit shown is only an example for a personally cast, source-verified effect with an actual EV or canonical EvilCraft blood payment. It must not multiply automated altars, EV networks, blood containers, blood recipes, ritual machines or sacrifices. No extra Fury from blood self-damage. Do not impose a guessed across-the-board magic handicap before the owner specifies it; this Path should remain able to use its intentional blood synergies.

## Shared engine and implementation ownership

| Mechanic | Shared Progression implementation requirement |
|---|---|
| Fury and Totem | One server-authoritative post-mitigation target damage receipt, 0–100 meter, exact 1/s decay, pre-hit >50 lethal full vanilla Totem, full Fury reset and no cooldown |
| No hurt frames and incoming response | Safely suppress standard hurt immunity only, preserve genuine immunity, append exactly one-second Regen/Resistance per distinct valid damage with paid amplifier |
| Naked clothing and shield | Source actual chest slot/chafe timer; zero armor clothing recipes/tartan; on-hit auto unequip except cap; shield-active suppression of all bought melee powers |
| Woad and Red Cap | True canonical EvilCraft blood application, timed cap effects, blue Woad body state, accurate pinned Neo Vitae source parity, visual/equipment constraints |
| Personal blood/magic | Neo Vitae EV spend and EvilCraft blood payment receipts for audited specific personal effects, harmful magical damage/source tags and enemy casting state |
| MXP | Actual kill and final magical-object break events, one receipt per credited action, protected/claimed and placed-block anti-loop checks; no purchased separate Fury faucet |

All Java, mixins, generic handlers, effect registries and executable tests belong to shared compat/progression on feature/progression-* branches. A future Bare Zerker config PR only authors declarative data/assets, and must not create a separate game module. Do not enable inert skills.

## Acceptance matrix (not executed)

1. Exactly 22 skills, 100 one-point costs, all 13 prior IDs, valid prerequisites and unchanged other Path budgets.
2. Real attack vs armor, crit, absorption, overkill, immune target, projectile, indirect damage and PvP; exact Fury from real damage only; max100 and -1 Fury/s.
3. Fury 50 lethal fails save; Fury strictly >50 lethal invokes full Totem with native sound/visuals/effects and empties all Fury. Repeat without cooldown after actual earned refill.
4. Multiple hits during removed vanilla frames each add precisely one second Regen/Resistance, purchased II/III changes level only, not duration. Preserve other immunity and correct event sequencing.
5. Chest chafe, cap protection, clothes dislodging, tartan variants, leather boots, full inventory/discard rules; self-chafe effect qualification pending owner decision.
6. Shield suppresses ALL direct sword/axe purchased power. Status-only hits, one hit combo, kills and chest chafe create no Fury by themselves.
7. Woad effect-by-effect pinned Neo Vitae audit, no chest armor slot, costs/tiers, render, timer, Red Cap stacking and preexisting actual Sentient Armor interactions.
8. EvilCraft real source blood dip and Red Cap duration; Neo Vitae and EvilCraft personally paid blood ability effects without generation loops.
9. Antimagic source-defined spell damage/effects and current casting status only; unrelated potions/world magic not indiscriminately nerfed.
10. Kill and actual magic-object break sources award MXP once, no re-placed block, dummy, chest-chafe or passive break farms.
11. No Bare Zerker food restrictions; valid Core/Newbie/other Path compatibility, relog, death, respec and equipment swap state cleanup.
12. No runtime pass, Module CI, assembled NeoForge boot, server smoke or Prism QA claimed. Gameplay verification needs separate owner-authorized integration.

## Source and open owner decisions

Owner-approved baseline and unresolved requirements: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/454. Need source audit/owner selections for exact Neo Vitae 1.21.1 Woad equivalence, Red Cap blood recipe/duration, Neo Vitae/EvilCraft blood skills, spellcasting handicap, magic-object XP taxonomy, armor/chafe and overkill/absorption edge cases, and native initiation. All new numeric effects are provisional.
