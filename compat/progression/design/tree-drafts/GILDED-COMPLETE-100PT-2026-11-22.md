# Gilded — complete role-first 100-point gold-and-Nether skill tree

**Too Much Magic · 2026-11-22 · DESIGN ONLY, NOT ACTIVE OR SOURCE-TESTED IN THE PACK**

**Source chain:** `feature/gilded-100pt-complete-tree-20261122` stacked on Forest Dragon #536 → Cave Dragon #530 → Lich #526 → Werewolf #520 → Vampire #518 → Sea Dragon #514 → original Paths #495. Three design-only files under `compat/progression/design/tree-drafts/` changed. No new Java, current Path datapack, KubeJS, mod pins, FTB Quests, pack assembler, Prism or smoke test permission.

## Identity, native/source authority and locked free baseline

Gilded is the **Piglin-aligned golden adventurer**: golden arms and armor perform like diamond without losing gold's speedy mining/enchantability; it earns powerful honest Fortune/Looting bonuses, protects gold/Netherite equipment, barters with allies, fishes lava, thrives in Nether, and reaches a golden-regalia apex. Unlike Sea Dragon's extreme Luck, Gilded derives most wealth from *actual equip-qualified loot and trades*, not a permanent whole-world loot mutation.

**Enrollment is free and exact:** in the Nether, wear a genuine full set of four gold armor pieces, and *finish eating* a golden apple, enchanted golden apple, or golden carrot. The successful server consumption is the only ordinary join trigger. Failed/canceled uses, partial armor, wrong dimension, nonapproved food and self-serve manual commands do not enroll. Ordinary operators can use authorized QA controls. This ritual is **not a purchased skill**.

**Already-locked free Gilded effects, never sold again:**
- Gold tools have **effective diamond harvest tier** and diamond-equivalent combat while a Gilded legitimately uses them; golden armor has diamond-equivalent points/toughness per slot while worn. Native gold faster tool speed, higher enchantability and item max-durability metadata are unchanged. Non-Gilded items remain vanilla.
- Gold + Netherite mining tools receive **+3 virtual Fortune** at initiation. Gold sword/axe **direct melee** receives **+3 virtual Looting** at initiation, independent of Fortune. Both virtual bonuses add to actual stored levels; real enchantment levels are *not* newly capped. Silk Touch still overrides Fortune; no effect from merely holding gold on an arrow kill.
- Correctly equipped gold + Netherite tools/armor already receive **additional virtual Unbreaking and same configured, rapid, real durability regeneration** in their armor slots or active mainhand; offhand, container and idle inventory do not repair. Lasting Gilt/Golden Renewal only improve verified free baselines, never unlock repair/Unbreaking permission for the first time.
- Ordinary Piglins **and Piglin Brutes** begin friendly even without gold worn. Appropriate gold mining/container actions do not **initiate** anger at Gilded, but legitimate direct aggression still triggers retaliation; other players keep normal hostility. This is not a purchased friendship skill.
- Lava-fishing permission exists as soon as enrollment succeeds (no skill buy or level-12 requirement), for genuine eligible lava hooks from **Lava Fishing** and **Nether Depths Upgrade** only if those content mods are actually installed. Do not install the overlapping separate Aquaculture 2 Lava Fishing Addon. Fishing XP must be **one real activity receipt per hook UUID**, not per fish/drop/bonus reroll.
- Gilded diet: eligible actual Nether foods, explicit golden foods, and food with a *verified gold-containing recipe provenance chain*; source whitelists and real lava-caught food marker are recognized only when real. Golden/gold-crafted foods restore **2× base nutrition and 2× actual saturation points** after enrollment (NOT 4× saturation via doubling modifier), ordinary Nether foods 1×. Special food effects must fire once. Food and inclusion rules remain free.
- **Undying Ember is FREE at native Gilded level 18:** immunity to actual `#minecraft:is_fire` damage, including lava, and burning-tick clearing, not immunity to normal weapons or unrelated magic. Gilded level 20 may be an appropriate *late paid capstone gate*, but native rank18 fire immunity cannot be recharged or repurchased by the tree.
- Native Gilded level/milestones (if present in the coherent future engine) are non-purchase eligibility, not a second skill shop. Unified MXP remains the default shared purchase currency under issue #475; gold-ore XP is awarded once for one **successfully harvested** qualifying gold ore block without Silk Touch, independent of Fortune drops, and a real legitimate lava catch may award its separately supported activity XP. Paths may modify source-award rates, but not own/duplicate the MXP wallet.

## Five-branch purchased mastery budget

| Branch | Skills | One-point ranks |
|---|---:|---:|
| Goldcraft & Enchantment | 6 | 32 |
| Piglin Kinship & Barter | 4 | 18 |
| Lava Livelihood & Food | 5 | 20 |
| Nether Mastery | 5 | 20 |
| Sovereignty | 2 | 10 |
| **Total** | **22** | **100** |

Each purchased rank costs exactly **one** point, including the Gilded Sovereign capstone. All **12** prior proposed IDs are retained, with **10** new distinct candidates. Historical per-skill 10-point filler and 2026-11-15 experimental source levels are *not* automatic numerical balance targets.

## Every proposed paid skill and implementation hold

### Goldcraft & Enchantment — 32 points

| Skill | One-point ranks | Candidate per-rank/full benefit | Unlock | Source/handler HOLD |
|---|---:|---|---|---|
| **Fortune Mastery** (`tyr_progression:gilded_fortune_mastery`) | 7 | +1 virtual Fortune level per rank, from FREE +3 at initiation to +10 purchased maximum, for real gold/Netherite mining tools only. Stored Fortune levels add without an artificial cap; Silk Touch wins. Exactly one player-owned eligible block-loot context; no drop multiplication after loot. | entry | Proof of effective enchantment at loot function, held gold/Netherite tool, source correct level stacking, Silk and Apotheosis enchant overlevels |
| **Looting Mastery** (`tyr_progression:gilded_looting_mastery`) | 7 | +1 virtual Looting level per rank, from FREE +3 at initiation to +10 purchased maximum, for actual direct melee kills with golden sword or axe only. Stored Looting adds; never projectiles, spell kills, offhand gold held during other kills or another player's loot. | entry | Player-and-weapon melee attribution, native loot function count+chance wrapper once, excludes arrows/summons/Drygmy double adjustments |
| **Lasting Gilt** (`tyr_progression:gilded_lasting_gilt`) | 5 | +1 additional virtual Unbreaking level per rank on legitimately worn gold/Netherite armor or actively held mainhand gold/Netherite tool, *above* the FREE baseline extra Unbreaking. Do not alter stored enchantment, speed or durability max. | entry | Proof baseline Unbreaking present free, correct one-roll durability hooks/armor wear and physical+virtual additive semantics; 5-rank cap candidate |
| **Golden Renewal** (`tyr_progression:gilded_golden_renewal`) | 5 | Reduce the source-configured FREE base interval between each legal equipped gold/Netherite 1-point durability repair by 10% per rank (max 50% shorter interval), with identical actual ticks/amounts for both materials and bounded min interval. Never repair backpack/offhand/container gear. | advanced, after lasting_gilt | Equipped armor/mainhand selector, native repair scheduler and shared material rate; no overrepair, no resurrection of broken gear |
| **Gilded Grip Mastery** (`tyr_progression:gilded_gilded_grip_mastery`) | 4 | +5% per rank (max +20%) ordinary mining speed with correctly held golden pickaxe/axe/shovel/hoe on legally harvestable blocks *after* FREE diamond-tier equivalence. Preserve golden tool's already high speed; no harvest tier increase from purchases. | entry | Legal tool/block harvest, source tool material identity, avoid stacked break-speed / other class bypass |
| **Metallurgist** (`tyr_progression:gilded_metallurgist`) | 4 | +4% per rank (max +16%) direct physical damage with a genuinely held golden sword or axe against legal hostile mobs, without changing base item material, offhand weapon or all Netherite tools; no damage from passive fire or distant projectiles. | advanced, after gilded_grip_mastery | Gold mainhand identity, melee damage handling and native damage order |

### Piglin Kinship & Barter — 18 points

| Skill | One-point ranks | Candidate per-rank/full benefit | Unlock | Source/handler HOLD |
|---|---:|---|---|---|
| **Piglin Diplomacy** (`tyr_progression:gilded_piglin_diplomacy`) | 5 | -5% per rank (max -25%) to *ordinary Piglin*'s normal processing/wait time after it has actually accepted a legitimate gold ingot barter, respecting native minimum delay. Brutes do not trade. Innate Piglin/Brute friendship is FREE and unchanged. | entry | Native 1.21.1 Piglin barter delay and paid transaction receipt; do not clear retaliation memories |
| **Barter Acumen** (`tyr_progression:gilded_barter_acumen`) | 4 | +3 percentage points per rank (max +12 pp) to award exactly one extra *common, allowed* item from an ordinary Piglin barter response when the original common reward has actually resolved; excluded rare tiers, boss ingredients and guaranteed high-value loops. | advanced, after piglin_diplomacy | Piglin loot table rarity/category and one completed barter receipt, modded barter tables, no reroll or double wholesale output |
| **Hoglin Pursuit** (`tyr_progression:gilded_hoglin_pursuit`) | 4 | +4% per rank (max +16%) direct melee damage against source-tagged hostile Hoglins/Zoglins when wielding a real golden sword/axe; does not apply to Piglins, friendly companions or every Nether creature. | entry | Native mob tags and player-owned direct gold melee only; defend against broad monster-taxonomy misclassification |
| **Warband Camaraderie** (`tyr_progression:gilded_warband_camaraderie`) | 5 | Reduce ordinary incoming direct physical hit damage by 1% per rank (max 5%) while physically within eight blocks of at least two non-angry genuine Piglins/Brutes in the Nether. No AI mind control or global Piglin invulnerability. | advanced, after piglin_diplomacy | Server valid live ally/anger state, nearby entity bounds and player-only defensive computation |

### Lava Livelihood & Food — 20 points

| Skill | One-point ranks | Candidate per-rank/full benefit | Unlock | Source/handler HOLD |
|---|---:|---|---|---|
| **Lava Angler** (`tyr_progression:gilded_lava_angler`) | 5 | -4% per rank (max -20%) normal bite/wait interval for an eligible *real lava* fishing bobber from the selected providers, never water rods or ordinary fishing reward loops. Permission to fish lava is FREE at initiation. | entry | Distinct source hooks for Lava Fishing and Nether Depths; no Aquaculture duplicate addon, no mod-pins implied |
| **Magma Linecraft** (`tyr_progression:gilded_magma_linecraft`) | 4 | 5% chance per rank (max 20%) to prevent one actual fishing-rod durability loss on a *completed, eligible lava catch*, no effect on casts, canceled reels, empty catches or water fishing; no free repair. | advanced, after lava_angler | Rod loss event + valid hook-UUID dedup across Aquaculture double catches, item durability and held rod owner |
| **Ember Salvage** (`tyr_progression:gilded_ember_salvage`) | 4 | +2 percentage points per rank (max +8 pp) to legal *ordinary* treasure-category odds within the original provider's lava fishing table, renormalizing other legal categories; never generate banned ores, boss items or unlisted loot and never duplicate an entire catch. | mastery, after lava_angler | Exact two providers' loot category tables, source-controlled roll rather than synthetic second output, worldgen/economy review |
| **Gold Nourishment** (`tyr_progression:gilded_gold_nourishment`) | 4 | Heal +0.5 HP per rank (max +2 HP) after one actually consumed, qualified gold-containing food, with 30-second personal cooldown; FREE 2× hunger + 2× saturation-points policy remains unchanged and special potion effects run once. | entry | Source tag/recipe provenance and LivingEntityUseItemEvent.Finish valid receipt; no double food component/potion processing |
| **Nether Forager** (`tyr_progression:gilded_nether_forager`) | 3 | +5% per rank (max +15%) theoretical saturation points from one legitimately eaten qualifying *non-gold* Nether food; ordinary hunger value unchanged, gold foods excluded and vanilla actual saturation clamp preserved. | entry | Nether-only food allowlist, true consumed food instance and no overlap with 2× gold nutrition rule |

### Nether Mastery — 20 points

| Skill | One-point ranks | Candidate per-rank/full benefit | Unlock | Source/handler HOLD |
|---|---:|---|---|---|
| **Nether Affinity** (`tyr_progression:gilded_nether_affinity`) | 5 | +3% grounded walking speed per rank (max +15%) only in the actual Nether while standing on native Nether rock/soil; no flight/glide boost, no free fire immunity and no generic Overworld speed. | entry | Dimension/resource key, server ground-contact and source block tags, safe speed stacking |
| **Basalt Footing** (`tyr_progression:gilded_basalt_footing`) | 4 | -5% ordinary fall damage per rank (max -20%) when physically landing on verified basalt/blackstone or analogous natural Nether stone; no cancel of void/kill-plane and no new flight permission. | entry | Actual landing block, claims and fall source; exempt teleport/fall reset bypass |
| **Molten Reflex** (`tyr_progression:gilded_molten_reflex`) | 4 | +2 percentage points per rank (max +8 pp) to evade legitimately incoming projectile hits while in the Nether **after** FREE native rank-18 Undying Ember is already earned; no dodge of fire-tagged damage that immunity already cancels. | mastery, after nether_affinity | Authoritative Gilded rank18 state, projectile attacker and damage tags; avoid second fire immunity |
| **Prospector's Practice** (`tyr_progression:gilded_prospectors_practice`) | 4 | +5% per rank (max +20%) to the *one real, qualifying per-block* Gilded gold-ore activity XP/MXP credit under configured XP mode, never a second grant, no Fortune/yield amplification, and no Silk Touch or canceled/failed break credit. | advanced, after nether_affinity | Unified XP mode #475 and native activity multiplier hook; actual silk status / block-drops receipt, single award even if mining yields multiple items |
| **Ashen Resolve** (`tyr_progression:gilded_ashen_resolve`) | 3 | -3% direct ordinary physical damage per rank (max -9%) when the enrolled Gilded is below half max health and in the Nether, excluding void, bypass-armor and innate #minecraft:is_fire negations. | advanced, after basalt_footing | Low-health condition, exact source damage tags and stacking caps; no permanent mitigation |

### Sovereignty — 10 points

| Skill | One-point ranks | Candidate per-rank/full benefit | Unlock | Source/handler HOLD |
|---|---:|---|---|---|
| **Glittering Luck** (`tyr_progression:gilded_glittering_luck`) | 4 | +0.25 player Luck attribute per rank (+1.0 total); only effects whose legal loot/ability code truly consults the Luck attribute. No virtual Fortune/Looting levels, automatic rare-drop duplication or Sea Dragon-level luck. | advanced | Player Luck attribute implementation, modifiers per-rank and legitimate loot tables that consume Luck |
| **Gilded Sovereign** (`tyr_progression:gilded_gilded_sovereign`) | 6 | After genuine native Gilded level 20, while wearing a complete four-piece golden armor set AND directly holding a golden sword/axe in the Nether, gain +3% direct gold melee damage and -2% incoming ordinary physical damage per rank (max +18% and -12%). Netherite armor or weapon does not trigger this *gold-regalia* capstone. | mastery, after glittering_luck | Gilded native level20 milestone, exact full-gold equipment, one gold mainhand and Nether dimension; no free gold-equivalence repurchase or innate Undying Ember price |

## Free versus purchased mechanics — common points of confusion

**Fortune and Looting:** the ordinary Gilded starts at +3 of each. Seven *separate* purchased one-point ranks in Fortune Mastery add +7 virtual Fortune, and seven *other* purchased ranks in Looting Mastery add +7 virtual Looting. At seven ranks of either skill its own Gilded additive bonus reaches **+10**; the stored item enchant remains independent. No need to buy one to improve the other; direct melee with golden sword/axe only for Looting; gold or Netherite mining tools for Fortune, subject to Silk Touch. This is different from Sea Dragon Luck and from Gilded's purchased modest player Luck.

**Unbreaking and repair:** the Gilded's free basic equipment benefits must exist before rank 1 of either improvement. Lasting Gilt adds virtual levels without modifying NBT and should not accidentally make offhand armor or another player's weapons unbreakable. Golden Renewal improves exactly *one shared repair rate for both gold and Netherite*, not two independently stacked schedules; already repaired/broken equipment cannot be over-repaired or resurrected.

**Food:** Gold Nourishment is a small actual HP heal on real successful eating, **not** additional hunger/saturation doubling beyond the FREE 2× policy. Nether Forager applies only to ordinary non-gold Nether foods, leaving golden food's source rules untouched. Lava-caught food provenance must survive pickup/containers. Nether/recipe semantics remain data-driven rather than an exhaustive named-item matrix.

**Fishing:** Lava Angler does not unlock the rod or Nether fishing permission. Magma Linecraft only avoids a *real rod durability use on a legitimate successful catch*; Ember Salvage changes legal original table odds, not a second loot reward source. The two desired fishing providers may be missing from an actual assembled pack; these purchased skills must be held when the eligible provider is absent. Hooks must guard against Aquaculture double catches, Angler's Hat extra drops and Improvable Skills repeated scrolls.

**Piglins:** ordinary Piglins trade, Brutes do not. Initial non-aggression and protected gold/chest anger suppression are innate. Piglin Diplomacy improves a real completed barter exchange's delay, not basic permission, and Warband Camaraderie checks *actual calm allies* rather than ever resetting native anger state.

**Fire immunity:** the rank18 Undying Ember is free and complete for correctly tagged fire attacks. Molten Reflex is separately purchased, projectile-only and cannot double-dip on a hit already canceled by fire immunity. No extra fire resistance potion effect or active skill key. After rank20, Gilded Sovereign rewards full golden regalia in Nether without altering gold's base item stats for anybody else.

## Engine/source implementation crosswalk

| Domain | Reusable candidate | Required verification / missing generic handler |
|---|---|---|
| Native Gilded rite, rank and food | Shared Progression join guard, player food classifier and server-native milestones | Correct finished-consumption event/armor and Nether identity; not bypassable via manual join, twofold *points* not saturation modifier, gold ingredient graph, lava catch provenance |
| Effective gold gear | Conditional attributes, harvest tiers, gear_durability | Correct diamond-equivalent gold armor/damage/harvest without global item edit; gold native fast mining; mainhand/armor only and additive no-double-tick repairs |
| Fortune, Looting and Unbreaking | Existing virtual-enchantment helpers if actually present | Correct native loot-context single debit/call; player+selected item, Silk Touch, Apotheosis, Ars Drygmy, ranged exclusions, exact one durability roll |
| Piglin AI, barter | Read-only Piglin/Brute relationship, event-specific barter modifiers | Intercept source-specific non-attack anger initiation once, retain retaliation; real Piglin trade payment, no bribing Brutes, category-safe ordinary loot bonus |
| Fishing | Player-owned lava hook event, bite time and fishing loot | Two eligible lava providers may be absent; one hook UUID one XP/loot receipt, legal waiting/durability/loot table adjustments, no Aquaculture addon duplicate |
| Nether combat and defense | Player dimension/ground/block predicates, damage/evasion, enemy taxonomy | Hoglin/Zoglin target tags, regular physical-only reductions, one native rank18 fire cancellation, rank20 and gold-regalia gate |
| XP and Core | Player-owned shared MXP, source event and configurable path multiplier | Gold ore harvested without Silk one event; no bonus per Fortune or dropped item; path-rate multiplier acts after provider event, Core never depends on Path |

All handlers, adapters, Java and runtime tests belong in **shared `compat/progression` on `feature/progression-*`**. This Gilded tree branch is **design/configuration only** and must not become a second compiled mod. No feature can be described as ready for a comprehensive QA build until source-coherent handler/data inclusion is verified.

## Source-backed acceptance matrix (to execute only after authorized integration)

1. Verify exact enrollment (Nether, four gold armor slots, one completed valid golden food); failed/canceled eat, different dimension, missing item, invalid manual join fail. Server source receipt exactly once.
2. Check baseline diamond-effective gold tool combat/harvest and armor per slot, free extra Unbreaking and same gold/Netherite repair rate before any purchases; gold retains faster native mining and enchantability. Item metadata and max durability remain unchanged.
3. Fortune +3 at enrollment, +10 after exactly seven buys; physical enchant (including Apotheosis overlevels) adds without imposed Gilded cap; Silk Touch precedence, protected blocks, purchased ore, only gold+Netherite tools.
4. Looting +3 at enrollment, +10 after its own seven buys; golden sword/axe real direct melee only, no bow/arrow/summon/indirect kill or unheld sword exploit, Ars Drygmy compatibility and one loot-context adjustment.
5. Piglin AND Brute neutral unless attacked, including mining guarded gold/chest access; native retaliation persists when provoked, other players unaffected. Only ordinary Piglin barter is sped/altered; excluded rare loot not duplicated.
6. Baseline lava fishing available immediately on join even before rank18; mod provider absence disables paid fishing nodes; AQ2 hook collisions, Angler's Hat output, Improvable scroll and hook UUID dedup; separate food provenance survives inventory/storage.
7. Free Gilded golden-food 2× nutrition and 2× saturation **points** after join, no duplicated potion effects; ordinary Nether foods normal; prohibited foods denied before effect; rank-gated health heal no duplicate event.
8. Native ore harvest source 1 receipt per completed gold block without Silk, not per drop/stack; earned multiplier scales the original eligible award only under appropriate XP mode, no independent SP wallet or free XP from opening containers/barters.
9. Undying Ember rank18 grants genuine fire-tag immunity and clears fire; projectiles and unrelated damage remain; paid Molten Reflex and other defenses don't negate native fire again. Use exact source milestone, not a number fabricated from XP count.
10. Repair/Unbreaking strict equipment scope: armor/mainhand only, no offhand, backpacks, machines, item transfer, no overrepair and no old decay after changing Paths. Exactly one repair interval shared by gold and Netherite.
11. Native level20 capstone check + full gold regalia + genuine gold mainhand + actual Nether; power disappears when changing any condition. Avoid creative/Spectator, Netherite or false armor set substitutes.
12. Check all 22 node IDs, 100 one-point rank costs, valid prerequisites, independent seven-rank Fortune/Looting, no changes to Core/Hunter or other tree budgets.
13. Multiplayer, respec, logout/restart, PvP/claims/loot contexts and alternating Gold/Netherite: one player cannot buff another's item or duplicate XP/loot. Separate quick source CI, Module CI and owner-authorized Prism gameplay as required.

## Provenance and unresolved review

- Repository historical design: `docs/archive/historical-branches-2026-11-19/gilded/docs/explorations/PIGLIN-GOLD-PROGRESSION.md` and `.../gilded/README.md`, plus `.../gilded/LIVE-TEST-READINESS.md` (compiled historical source only, no current pack/in-world PASS). Its obsolete `tmm_progression` namespace is historical; all new blueprint IDs use `tyr_progression`.
- Fishing provider research: `.../gilded/LAVA-FISHING-FEASIBILITY.md`. Lava Fishing 1.21.1-1.2.13 and Nether Depths Upgrade 3.3 were desired but were **not pinned/included by this PR**. The Aquaculture 2 lava addon has conflicting hook registration and remains excluded.
- Economy: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475 (unified MXP default and source-award handling); source integration and QA gate in repository AGENTS.md.
- **Owner review candidates:** rank5 Unbreaking bonus beyond free level, shortening free repair interval, barter reward odds and rare classification, practical lava loot tier modifiers, equipped-gold capstone magnitude, native Gilded level20 source truth and whether path-specific XP multiplier is balanced. Nothing here has been asserted game-verified or owner-locked beyond original free baseline.
