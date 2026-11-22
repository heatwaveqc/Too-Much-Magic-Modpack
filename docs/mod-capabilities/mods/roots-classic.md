# Roots Classic 1.5.10 — Mod Capability Atlas (source research COMPLETE, 2026-11-21)

**Status:** `atlas-source-research-complete / quest-source-ready / implementation-and-client-acceptance-pending`. This is the **29-axis capability research input for a future instructional quest chapter**, not an implemented chapter or a claim of native behavior tested in-game. Exact active TMM packwiz pin: `RootsClassic-1.21.1-1.5.10.jar`, CurseForge project `295780`, file `8860609`, SHA-1 `7d02ec46e4d276ebf556cdd529a75108b4724efd`; MC 1.21.1 NeoForge. The independent [Comprehension profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic.md), [all 22 component spells/items](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic-pinned-spells-items.md) and [all 46 native rituals](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic-pinned-rituals.md) inspected the hash-matched JAR and a version-setting [source commit `db9aa883`](https://github.com/Lothrazar/RootsClassic/tree/db9aa883b7a54a1558dbc710546be1f42544be72). Recipe counts are **22 component + 46 ritual + 10 shaped + 5 shapeless + 1 smelting**; the first two are independently typed custom recipe families. This Atlas reuses that precise evidence without modifying Comprehension or retesting unchanged native mechanics.

**Exact starter recipes and detector closure:** [shaped Tablet/Mortar/Pestle/Imbuer/Altar/Brazier/Mundane/Attuned patterns, totals and genuine FTB task audit](../inventories/roots-classic-starter-recipes-and-detectors.md). All required entry hardware ingredient patterns are source-linked; Sylvan armor is a **level-2** ritual, not a level-1 recipe. Source Atlas research is complete, but live pack acceptance and real quests are independently pending.

**Evidence scale:** "source" below refers to exact JAR and code documented in those three research artifacts, not a live TMM test. No verified TMM Roots config overrides, successful spell casts, quest detectors or assembled client. Competing later-version Roots/Roots 4 documentation is not evidence for this pinned Roots Classic.

## 1. First working loop and player guide

1. Break tall grass for `rootsclassic:old_root` (native default 1/40). Craft `rootsclassic:runic_tablet` using one Old Root, one `#c:stones` and three `#c:seeds/wheat`, then **use it in main hand**. Its tabs are **Natural Arts**, **Spellcraft** and **Rituals**; they are not advancement-gated.
2. Gather a named component flower plus the other items from its Tablet page. Make Mortar and Diorite Pestle; supply the recipe's plant/other components **and one mixing base**: Old Root allows 0 modifier slots, Verdant Sprig 1, Infernal Bulb 2, Dragon's Eye 3. Grind a `rootsclassic:spell_powder`. Every component recipe is distinct; no "any flower" recipe.
3. Make an Imbuer, insert a stick/accepted wooden rod plus the completed powder; wait beyond 40 ticks. Take the resulting ordinary Staff. Hold right-click **at least 13 ticks** and release to cast. Read both the finite staff uses and the personal Terra bar; insufficient Terra can waste one staff use without a successful effect.
4. For the independent ritual branch, craft Altar and Brazier(s), place **each exact recipe item** on the Altar, place each incense in a nearby Brazier at the same Y level and within four horizontal blocks, ignite, then sneak-use Altar with **empty main hand**. The native ritual runs 200 ticks before effect/output, absent interruption. Use the Tablet ritual page and JEI Ritual category; an output item alone proves possession, not that the player performed the operation.
5. Upgrade the **physical ritual layout** when desired: level 0 no pillars; level 1 eight **Mundane Standing Stones** at offsets (±3,±3), (±3,0), (0,±3) at altar Y; level 2 retains those and adds four **Attuned Standing Stones** at (±5, +1, 0) and (0, +1, ±5). Treat these as *altar* levels, not player classes or Terra levels.

## 2. Formal progression and optional branches

| Node / type | Entry and proof | What becomes newly executable | Limits and next routes |
| --- | --- | --- | --- |
| `RC::gather` analyst stage | Tall grass → Old Root; mature crop → Verdant Sprig; bark-knife logs → bark | Tablet, common natural powders, growth/poultice supplies | Probabilities are source defaults, not promises; rare materials from Nether/End appear later |
| `RC::staff` analyst stage | Mortar + Pestle with exact component recipe → Powder; Imbuer produces staff | 19 ordinary and 3 mutated component spells once ingredients accessible | Personal Terra + finite staff uses; modifiers are powder-stage changes |
| `RC::ritual0` **formal native level 0** | Altar/Brazier, exact burning incense and inputs, empty-hand sneak activation | Living Tools, Grow, Life Drain, charged focus recipes and other native level-0 effects | No standing stones required; no ritual Terra debit demonstrated |
| `RC::ritual1` **formal native level 1** | Eight Mundane stones, correct recipe/altar/incense | Weather, passive-animal summons, Flare, Time Shift, Crystal Staff *imbuing operation* | Tier 1 staff-imbuing requires a **pre-existing Crystal Staff**; it is not the staff crafting route |
| `RC::ritual2` **formal native level 2** | Retain eight Mundane; add four raised Attuned stones | Crystal Staff crafting, hostile mobs, Sylvan/Wildwood armor, Engraved Blade, Runic Focus, powered standing stones, Mass Breeding, Sacrifice | These are *parallel optional branches*, not 46 sequential mandatory quests |
| `RC::mutations` optional environmental branch | Mutating Powder, Nether Star ingredient, precise flower/dimension/effects/offerings | Flare Orchid, Midnight Bloom, Radiant Daisy components and their distinct spells | Dangerous/buggy matcher and raw-world-time behaviors; not a required main-story gate |

**Critical non-linear dependency:** level-2 `ritual/crafting/crystal_staff` makes the base staff; the separate level-1 `ritual/imbuer` then loads 1–4 spell powders. Never make the level-1 imbuing quest a mandatory prerequisite for acquiring a Crystal Staff. Sylvan and Wildwood are alternative armor projects; they are not sequential technology tiers.

## 3. Exact infrastructure and power stores

| Concrete system | Storage scope and typed I/O | Costs, capacity, cadence and behavior |
| --- | --- | --- |
| `rootsclassic:runic_tablet` | Reusable item guide; no consumable energy | Main-hand use opens research; Spellcraft, Natural Arts, Rituals pages and JEI support |
| Mortar + Pestle | Block/crafting inventory: named component ingredients + mixing base + optional dust → one Powder with encoded spell/modifiers | 22 registered custom component recipes; modifiers Glowstone = potency, Redstone = efficiency, Gunpowder = size |
| Imbuer | Block inventory: wooden rod + Spell Powder → Staff | Server progress past 40 ticks; no FE, mana or Source transfer indicated |
| **Terra** (`rootsclassic:mana`) | **Player attachment**, persisted per player; regenerated by player tick; spells debit Terra | Default maximum/current 40; code increments **1 per 5 ticks = nominal 4/sec** at 20 TPS. Config advertises a different 15-tick setting but the observed event uses the hardcoded 5-tick constant. No evidence of FE, Botania mana, Ars Source, MXP or other player resource bridge |
| Ordinary spell staff | **Item**: finite use counter plus encoded component/P/E/size; cast also uses player Terra | Native active default 15 uses + 15 per efficiency level; minimum 13-tick hold. Release removes a use **before** Terra sufficiency; mana component cost divided by efficiency+1 |
| Crystal Staff | **Item**: up to four encoded spells; personal Terra remains external | Ritual-created and then ritual-imbued; cycles spell slots with sneak-use; no finite uses debit. Unlike old Tablet prose, source enforces **no natural-ground check or fallback health cost** |
| Ritual Altar + Braziers | **Block inventories/world layout**: consumed item inputs and burning-incense stacks → effect, item or summon | No independent "ritual mana" pool; 200-tick operation. Nearby Braziers at altar Y inside ±4 X/Z; extra lit incense can fail matching. Altar clears recipe ingredients after finishing whether output succeeds |
| Standing Stones (8 Mundane + 4 raised Attuned) | **World structure**, no rechargeable energy store | Unlocks recipes requiring levels 1 and 2; precise block offsets matter |
| Powered Standing Stones | **Placed blocks/world tick effects**, no native fuel debit in inspected handlers | Seven functional variants plus Aesthetic; persistent area effects after crafting, not player-owned Terra generators |
| Runic Focus / Charged Runic Focus | Two **items** with recipes and guide presence only | Whole-artifact research found **no native active consumer** that charges/spends/transfers a universal resource; do not invent another high-tier mana system |

**Transfer and conversion contract:** natural resources are items from loot/harvest events; Terra is private player-state, finite staff charges are separate item state, Powder carries encoded spell data, incense/items are physical ritual payments. No proven bridge to FE, EV, Source, Botania mana, blood fluid, or MXP. TMM Progression may later award MXP from legitimate magic expenditure, but these operations do not yet prove such an adapter exists.

## 4. All native spell operations — capabilities, not 22 mandatory quests

All **22** registered component spell names, nominal base Terra, detailed *actual* effect predicates, damage and failure boundaries are in the [exact-pin spell inventory](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic-pinned-spells-items.md#first-spell-and-all22-choices). These groupings preserve real operational distinctions:

| Operational family | Exact component IDs (prefix `rootsclassic:`) | Actual effects, beginner to exceptional |
| --- | --- | --- |
| Damage and crowd control | `allium`, `nether_wart`, `rose_bush`, `white_tulip`, `poppy`, `sunflower` | Requested direct damage, fire, slowing, monster target clearing/retargeting and undead-specific effects. Thorns/vulnerability follow-on keys mismatch their readers in source; do **not** teach those bonuses as confirmed |
| Damage plus healing/recovery | `pink_tulip`, `peony`, `apple` | Life-steal-style attack, held floral healing, full-effect clear and selective negative→positive conversion. Apple removes **beneficial and modded** effects too; healed amount is not guaranteed actual damage dealt |
| Block and device manipulation | `azure_bluet`, `blue_orchid`, `lilac`, `lily_pad`, `oxeye_daisy` | Azure destroys ray-hit + possible adjacent blocks, no BreakEvent posting in inspected handler; Blue Orchid copies listed common stone/dirt/gravel/sand blocks into spikes; Lilac grows nearby targets with short-circuit/potency bounds; Lily Pad places replaceable water-state blocks; Oxeye accelerates entity/block-entity ticking. Claim/machine interactions require TMM-specific review |
| Mobility, forces, temporary summon | `chorus`, `dandelion`, `orange_tulip`, `red_tulip` | Direct look-direction teleport without explicit safe-landing scan; entity motion; **held-action** nonliving repulsion; temporary Phantom Skeleton without owner binding |
| Weather/hazard | `poisonous_potato` | Creates real, non-visual vanilla lightning (not just a cosmetic particle) |
| Mutated rare spells | `radiant_daisy`, `flare_orchid`, `midnight_bloom` | Bouncing radiant damage/blindness; block-damaging explosion; entity tick-skip effect. Flowers require separate environment/powder chain and have source bugs/limits |

**Cast order and cost:** spell data selects one component; active cost is its base Terra divided by `efficiency+1`. Powder base from Old Root (0 modifier slots) through Dragon's Eye (3), with the three modifier ingredients, is a *branch within every spell*; an upgraded component is not a new spell family. Some held-action handlers do not share release-path Terra tests. None should be granted blanket PvP/claims safety based on another spell's implementation.

## 5. All 46 rituals and eight powered stones

The hash-verified [ritual inventory](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic-pinned-rituals.md#all46-native-recipes) has **all 46** recipe IDs with exact altar item counts, incense ingredients, level 0–2 and output. Registered effect families: **24 crafting, 12 entity summoning, 1 each engraved_crafting, imbuer, grow, life_drain, mass_breeding, sacrifice, flare, cause_rain, banish_rain, time_shift**. These are **12 effect families**, not all item crafts. Representative route evidence:

- **Level 0 Living Pickaxe**: wooden pickaxe + Verdant Sprig + gold nugget on Altar; **two** Oak Bark incenses. Self-repair is probabilistic **1/80 per inventory tick if damaged**, not a fixed per-second repair. This is an ideal first ritual.
- **Level 0 Grow**: Redstone + Bone Meal + Verdant Sprig on Altar; Wheat, Beetroot, Potato and Carrot as separate lit incenses. Checks a **35×35×9** region and applies independent 1/12 bonemeal chances to valid blocks, not guaranteed field-wide full maturity.
- **Level 1**: passive summons such as Cow (`beef + leather + bone` with Wheat Seeds/Nether Wart incense), rain changes, Time Shift and Flare. Summon effects create a real mob without owner/tame attachment, not an egg or pet.
- **Level 2**: hostile mob summons (Creeper, Zombie, Skeleton, Spider, Cave Spider, Slime, Enderman), Crystal Staff crafting, Sylvan/Wildwood armor, Engraved Blade and stone outputs. Hostile entities remain real mob-farming/replication candidates, not a permission bypass.
- **Crystal Staff**: level-2 `ritual/crafting/crystal_staff` (Diamond Block + Stick + Blaze Powder on Altar; Coal Block, Acacia Bark, Verdant Sprig, Birch Bark incense). Later level-1 `ritual/imbuer` uses the existing staff and Verdant Sprig plus 1–4 Powder incenses.
- **Engraved Blade**: level-2 `ritual/crafting/engraved_crafting` uses Stone Sword, Runic Focus, Glowstone Dust; Golden Carrot + Golden Apple incense and up to four additional bark incenses. Acacia→Spikes, Spruce→Forceful, Birch→Holy, Jungle→Aquatic, Dark Oak→Shadowstep. **Forceful has no demonstrated implementation consumer**. Tablet's named `ritual/engraved_blade` reference is missing; use actual JEI recipe.

| Powered stone | Exact effect and upkeep (source) |
| --- | --- |
| Accelerator | Every 20 ticks, Speed II for 500 ticks to LivingEntities in ±10 XYZ |
| Entangler | Every 20 ticks, Slowness II for 25 ticks to LivingEntities in ±10 XYZ |
| Healer | Every 20 ticks, Regeneration II for 500 ticks to LivingEntities in ±10 XYZ, **including hostile entities** |
| Igniter | Every 20 ticks, ignites LivingEntities for 2 sec in ±10 X/Z, ±2 Y |
| Grower | Every 100 ticks shuffles 9×9 positions, 3% chance per position for growth helper |
| Repulsor | Every tick moves nearby ItemEntities outward; no inventory insertion |
| Vacuum | Every tick pulls ItemEntities inward; no storage network or item ownership |
| Aesthetic | Changes colors with dyes, no production-effect consumer |

**Other significant branches:** Sylvan full armor set adds +1 spell potency, Wildwood individual pieces self-repair and full set has 2% eligible-player-tick chance to heal one health point; **neither is Terra regeneration**. Dragon's Eye is edible and can teleport the eater. Nature foods, bark and herb tools are ordinary natural-resource equipment, not separate player paths.

## 6. Capability-axis navigation (29 axes, evidence-backed status)

A row here is a pointer to the actual operations above and in the linked exact native inventory, not a yes/no scoring mechanism. `not_demonstrated` means no verified native evidence in the inspected set, not a proof of impossibility.

| Axis | Current source status | Concrete capability/evidence |
| --- | --- | --- |
| `gear` | present | Ordinary Staff, Crystal Staff, Living Tools, Sylvan/Wildwood armor, Engraved Blade (§3–5) |
| `movement` | present | Chorus teleport, Dandelion movement, Accelerator stone; Dragon's Eye random teleport (§4–5) |
| `ore_processing` | not_demonstrated | No ore multiplication/refinement station in exact inventories; Mortar makes Powder, not metal dust (§3) |
| `storage` | limited | Stored Powder spell data, staff spell/use state; Runic Focus **not** a proven mana battery (§3) |
| `crafting` | present | Component→Powder Mortar, Imbuer→Staff, 24 crafting rituals, level-specific effects (§3–5) |
| `logistics` | limited | Vacuum/Repulsor item-entity motion, **not** generic item piping/sorting (§5) |
| `buffs` | present | Sylvan potency, Wildwood healing, Accelerator/Healer auras, apple conversions (§4–5) |
| `external_compatibility` | unverified | JEI native categories; TMM Progression, faction, claim and magic-pool bridges require pack audit (§7) |
| `mob_farming` | present-with-risk | Twelve real ritual summons, Phantom Skeleton and Life Drain; owner/drop restrictions unresolved (§4–5) |
| `ore_generation` | not_demonstrated | Event-harvest resources and common-block Blue Orchid duplication; no native ore spawn system (§1,4) |
| `permanent_power` | present-world-only | Placeable persistent powered stone effects; no native permanent player unlock/mana tier (§5) |
| `general_generation` | present | Growth ritual, Grower, Living materials, crop drop loops; common-block spike creation (§1,4–5) |
| `direct_damage` | present | Allium/Nether Wart/Sunflower/White Tulip and others; Flare/Life Drain/Sacrifice (§4–5) |
| `block_breaking` | present-with-risk | Azure Bluet and explosive Flare Orchid; block protection not guaranteed (§4) |
| `permanent_pets` | not_demonstrated | Spawned mobs have no bound-owner contract in ritual handler (§5) |
| `temporary_pets` | limited | Phantom Skeleton summon is temporary entity, **not** proven owner-directed pet (§4) |
| `defense` | present | Shadowstep evasion; armor protection; defensive effects with limits (§5) |
| `healing_recovery` | present | Peony, Pink Tulip, Healer, Wildwood full set, poultice and edible resources (§4–5) |
| `debuff_control` | present | White Tulip Slowness, Poppy targeting, Entangler, tick-skip and Fire (§4–5) |
| `energy` | present | Player Terra 40 with source 1/5-tick regen; finite item charges; ritual items/incense separate (§3) |
| `automation_control` | limited-with-risk | Oxeye BE ticking, passive stones; no configurable controller/whitelist proven (§4–5) |
| `agriculture` | present | Sprig harvesting, Growth Powder, Grow ritual, Grower, mass breeding (§1,5) |
| `detection_information` | present | Runic Tablet guide and JEI pages; no radar or player-vs-mob scanner established (§1) |
| `equipment_augmentation` | present | Powder potency/efficiency/size, Crystal imbue, Engraved bark modifiers (§3–5) |
| `world_manipulation` | present-with-risk | Cause/Banish Rain, Time Shift, Lightning, Flare, water/spikes and mutation (§4–5) |
| `world_content` | limited | Resource drop hooks and three manufactured rare plants; no required native structure/dimension proven (§1,4) |
| `death_respawn` | limited-with-risk | Life Drain/Sacrifice can kill or heal; no native resurrection/respawn system established (§5) |
| `trade_economy` | not_demonstrated | No native currency/merchant/reputation economy evidenced in pinned inventory |
| `progression_tiers` | present | Spell modifier mixing bases, formal ritual levels 0–2, optional level-2 gear (§2) |

## 7. Safety, exact source boundaries and TMM handoff

**Known source hazards, not new balance proposals:** (a) Staff charge is consumed before Terra success; (b) a 15-tick Terra config setting appears unused by the hardcoded five-tick event; (c) source guide says Crystal Staff has a ground/health rule that the pinned class does not enforce; (d) Azure direct block removal and Blue Orchid block placement need protected-area tests; (e) Oxeye ticks arbitrary block entities and can accelerate TMM devices; (f) `sacrifice` subtracts health from the **first queried entity**, potentially a player, rather than each nonplayer match; (g) Life Drain healing is counted from requested damage, not actual successful loss; (h) interrupted/saved/reloaded altar can lose its currentRitual reference and stall; (i) mutations may consume whole stacks/ignore missing offerings, and Radiant Daisy uses raw dayTime 5000..7000 without day modulo; (j) Focus and Forceful cannot be advertised as functioning advanced buffs. Do not turn these into default rewards or mandatory dangerous actions.

**Proposed cross-mod technical verification, not design holds:** TMM skill/MXP caster event attribution; JAWR faction targeting and no automatically allied summoned hostile; capture/reanimation/EMC exclusion parity; protection and machine tick interactions with FTB Chunks and high-throughput mods; player/monster eligibility for healing and powered-stone auras; food dietary tags for Roots berries/stew/poultice/Dragon's Eye. The full TMM first-party change owner should handle actual adapters and tests, not this Atlas branch.

**Existing chapter:** `config/ftbquests/quests/chapters/roots_classic.snbt` is an optional generic checkmark task (2 XP and Paper); it teaches none of these activities. The [research-only Roots quest map](roots-classic-quest-map.md) proposes small core walkthrough and optional lessons. No FTB GUIDs have been written. For true FTB acceptance, **items prove possession only**; use actual operation advancements/events where independently confirmed and manual/checkmark wording otherwise.

**Evidence/staleness:** research source pin/hash and exact JSON/Java relationships are inherited from [profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic.md), [spells](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic-pinned-spells-items.md) and [rituals](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/a86c90befd8dd5dca11c7883590ea49858553e15/docs/mod-comprehension/mods/roots-classic-pinned-rituals.md). Static documentation only. Re-audit on updated Roots pin, relevant native bugfix, or TMM integration affecting these mechanics. **No Minecraft client/server, recipe override, config, FTB file, executable script, JAR, or pack build changed or tested in this pass.**
