# Ars Elemental — focused addon profile

| Field | Value |
| --- | --- |
| Manifest ID / mod ID | `ars-elemental` / `ars_elemental` |
| Parent | [Ars Nouveau](ars-nouveau.md) |
| Pack pin | `ars_elemental-1.21.1-0.7.10.4.jar`; SHA-1 `562f640916aa6f5c83b3b3ee2384ca828b870e01` |
| Source snapshot | Exact release-version source found at [commit 2c433b6](https://github.com/Alexthw46/Ars-Elemental/commit/2c433b6a4e6ff5c3b314b5f5442881e0ad2dd0c2); Gradle declares 0.7.10.4 / MC 1.21.1 and Ars 5.13.2.1418, while TMM pins Ars 5.13.5. |
| Status | **researching** — addon source now matches the 0.7.10.4 pin and exposes substantially more than the three pilot items; exact JAR hash matched and all 353 classes decompiled; guide, Urn and focus acquisition/modifier/payment selection now audited in companions below. Other subsystem walkthroughs, dependency compatibility and gameplay remain open. |
| Guide | Exact binary registers native Notebook pages and edits parent pages; see the guide inventory below. Presentation still needs assembled-client confirmation. |

Exact binary companion: [native guide and Urn](ars-elemental-pinned-guide-urn.md), including recipe, wand order, per-target cadence/payment, capability restrictions and manual bucket path. [Focus acquisition, cost selection and greater passives](ars-elemental-pinned-foci.md) covers all four lesser/greater recipes, Mark of Mastery and Necrotic boundaries. [Armor conversion, thread capacity and damage response](ars-elemental-pinned-armor.md) covers all 48 tier-gated recipes and three weight branches. [Shocking/Spores/Summoning threads](ars-elemental-pinned-threads.md) covers all three recipes and event consumers, including integer-division sickness failure and duplicate summon-power paths. [Focus-empowered native glyphs](ars-elemental-pinned-empowering.md) covers burn/freeze/Launch/Grow/Gravity/Cut and exact head-loot rolls. [Focus-selected temporary mounts](ars-elemental-pinned-steeds.md) covers all four replacements, native duration/payment/event order, riding and noSave boundaries. [Undead revival and Homing replay](ars-elemental-pinned-undead-summons.md) traces inherited lifetime, recipient validation/events and capability payment. [Homing Projectile/Propagate Homing](ars-elemental-pinned-homing.md) supplies two Tier3 glyph routes and native targeting/lifetime recipients. [Arc/Propagate Arc](ars-elemental-pinned-arc.md) covers the two Tier2 forms, native bounce/lifetime and propagated offsets. [Six paired filters](ars-elemental-pinned-filters.md) closes12 more glyph recipes, contextual polarity and legacy overload discrepancy;16of39 glyph routes now audited. [Bee/Slime combat summons](ars-elemental-pinned-bee-slime.md) closes two Tier2 glyphs and entity/AI/timer/splitting recipients;18of39 routes audited. [Mist/Oxidize/Sliding/Nullify](ars-elemental-pinned-mist-rust-sliding.md) closes four status/control routes including disabled-by-default Nullify recipe;22of39 recipes audited. Remaining sections retain explicit unaudited/source-version limits.

## Player systems and progression

There is no single linear tier ladder. The player chooses elemental schools and optional gear/automation. The addon declares four elemental schools (Fire, Water, Air, Earth), an Anima/Necromancy branch, equipment, familiars, turrets, relays, worldgen, spell tools and utility blocks.

| Branch | How it works | What it unlocks / teaching notes |
| --- | --- | --- |
| Focus selection | Craft lesser elemental foci; the school’s glyphs gain amplification/discount while other elemental schools are weakened. Greater foci remove the lesser malus, double the configured matching boost and add conditional worn passives; the first held/worn focus selects the mana discount (see companion). | Teach one choice as a reversible specialization; identify tradeoff before equipping. Pilot teaches Lesser Focus of Fire only. |
| Focus-enhanced spell effects | Fire Focus changes Ignite (Magic Burn/armor interaction), Water changes Freeze and Conjure Water, Air changes Launch/Cut, Earth changes Grow/Gravity. Necrotic focus is a separate Anima route. | Recipe page and focus-specific spell page required; effects are conditional on wearing the focus. Native glyph effects now audited from exact0.7.10.4 in companion; four steed replacements now audited separately; combat-summon/Homing recipient endpoints now audited; addon glyph acquisition/operation remains open. |
| Elemental armor | Four schools × three weight choices, converted from internal-tier-2 native armor with Mark+Netherite+two essences/7,000 Source per piece. Matching tagged incoming damage counts pieces by school/weight; mana uses original incoming damage, not measured mitigation. See exact recipe/attribute/response companion. | Teach recipe tier, retained threads/enchants, full-set activation and the proper element. Pilot uses Fire Hat only, not complete armor progression. |
| Urn of Endless Waters | Link a block fluid capability within 30; offer 1,000 mB per target every 60 ticks for default 100 Source per successful attempt. Cauldron needs an external capability; Botania fallback is a false-returning stub. Manual BucketPickup has no Source check. | A Source sink with fluid output; verify supported targets and rate. Pilot has a recipe milestone, not an output test. |
| Siren Shrine | Befriend wild Siren with sea pickle; summon charm; place shrine and build diverse water-animal/plant habitat; supply Source per fishing-loot cycle. | Renewable fishing loot. Requires setup and time; pilot omits it. |
| Flashjack / Flarecannon / Weald Walker | Find/befriend/assign helpers; link Flashjack to compatible turrets, use Dominion Wand for patrol/targets. Some defeated sentries can be reactivated with blaze powder or magma cream. | Defensive helper branch; exact acquisition, targets and output need exact-pin comparison. |
| Turrets, relays and prisms | Elemental turrets inherit some focus traits and discount matching spells; elemental Source Relays raise capacity/transfer; prism lenses can alter redirected spells and some consume Source. | Utility and defense. Teach placement/configuration, Source input and limits; not covered by the pilot's 3 quests. |
| Other items / dimensions | Exact 0.7.10.4 generated recipes include caster bags, elemental bangles, flight alternative, prism lenses, elemental relays/turrets, specialized spells and caster gear; source also contains archwood/biome worldgen and Vexing Caves. | These are exact-version source findings, but their acquisition, structure/biome conditions and current JEI visibility still need walkthrough review. |

## Walkthrough map

1. Choose and craft a lesser Focus; equip and compare an affected and unaffected spell/school.
2. Build elemental armor via JEI recipe chain; teach its tier, school bonuses, full-set condition and thread/enchantment retention.
3. Configure an Urn into a verified water-accepting capability block; follow the exact linking/payment boundaries in the companion. Cauldron/Apothecary guide promises need separate capability proof.
4. For any chosen helper/shrine/turret/prism branch, teach its acquisition, placement/configuration, resource cost, observable operation and maintenance.
5. Keep worldgen/exploration content optional; locate exact biome/structure rules and Notebook entry before promising an item route.

The pilot chapter has Lesser Fire Focus, Urn of Endless Waters and Pyromancer's Hat (quest IDs 35–37). It has no tutorial for Sirens, Flashjacks, elemental turrets/relays/prisms, foci for other schools, complete armor upgrades or worldgen. These are named coverage gaps, not completed quests.

## Evidence and tests

- Exact pin/hash and item/recipe evidence: `manifest/ars_quest_pilot.json`.
- Version-ahead primary source for focus and subsystem descriptions: [AELangDatagen.java at commit f2a1dd1](https://github.com/Alexthw46/Ars-Elemental/blob/f2a1dd1faa2ad7e6ad00c2919120ea4fc241cdf0/src/main/java/alexthw/ars_elemental/datagen/AELangDatagen.java).
- Exact JAR recovered and SHA1 matched: 2,273,778 bytes, 353 classes; 183 singular recipe resources. Guide registration and Urn audited from binary; inventory alone is not source completion. Remaining foci/armor/glyph/device/helper/worldgen routes and assembled-client presentation remain open.
- Not run: Source-to-water output and rate; full-set armor trigger; Siren Shrine source/output cycle; turret focus synergy; prism costs and target boundary.
