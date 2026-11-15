# Ars Elemental — focused addon profile

| Field | Value |
| --- | --- |
| Manifest ID / mod ID | `ars-elemental` / `ars_elemental` |
| Parent | [Ars Nouveau](ars-nouveau.md) |
| Pack pin | `ars_elemental-1.21.1-0.7.10.4.jar`; SHA-1 `562f640916aa6f5c83b3b3ee2384ca828b870e01` |
| Source snapshot | Exact release-version source found at [commit 2c433b6](https://github.com/Alexthw46/Ars-Elemental/commit/2c433b6a4e6ff5c3b314b5f5442881e0ad2dd0c2); Gradle declares 0.7.10.4 / MC 1.21.1 and Ars 5.13.2.1418, while TMM pins Ars 5.13.5. |
| Status | **researching** — addon source now matches the 0.7.10.4 pin and exposes substantially more than the three pilot items; source-to-JAR equivalence, Ars dependency compatibility and gameplay remain open. |
| Guide | Exact release source includes Ars Notebook content; its page inventory and presentation still need assembled-client confirmation. Cite the item's JEI recipe/use plus the matching Focus or armor page when present. |

## Player systems and progression

There is no single linear tier ladder. The player chooses elemental schools and optional gear/automation. The addon declares four elemental schools (Fire, Water, Air, Earth), an Anima/Necromancy branch, equipment, familiars, turrets, relays, worldgen, spell tools and utility blocks.

| Branch | How it works | What it unlocks / teaching notes |
| --- | --- | --- |
| Focus selection | Craft lesser elemental foci; the school’s glyphs gain amplification/discount while other elemental schools are weakened. Major focus can add condition-based benefits. | Teach one choice as a reversible specialization; identify tradeoff before equipping. Pilot teaches Lesser Focus of Fire only. |
| Focus-enhanced spell effects | Fire Focus changes Ignite (Magic Burn/armor interaction), Water changes Freeze and Conjure Water, Air changes Launch/Cut, Earth changes Grow/Gravity. Necrotic focus is a separate Anima route. | Recipe page and focus-specific spell page required; effects are conditional on wearing the focus. Details derive from 0.7.11.0 source and require 0.7.10.4 check. |
| Elemental armor | Four elemental sets: each piece amplifies and discounts school glyphs and reduces matching damage; full set converts some mitigated damage to mana and triggers a school benefit. Higher armor tiers and Mark of Mastery are part of the route. | Teach recipe tier, retained threads/enchants, full-set activation and the proper element. Pilot uses Fire Hat only, not complete armor progression. |
| Urn of Endless Waters | Convert Ars Source into water and link a supported destination (e.g., cauldron or Botania Apothecary). | A Source sink with fluid output; verify supported targets and rate. Pilot has a recipe milestone, not an output test. |
| Siren Shrine | Befriend wild Siren with sea pickle; summon charm; place shrine and build diverse water-animal/plant habitat; supply Source per fishing-loot cycle. | Renewable fishing loot. Requires setup and time; pilot omits it. |
| Flashjack / Flarecannon / Weald Walker | Find/befriend/assign helpers; link Flashjack to compatible turrets, use Dominion Wand for patrol/targets. Some defeated sentries can be reactivated with blaze powder or magma cream. | Defensive helper branch; exact acquisition, targets and output need exact-pin comparison. |
| Turrets, relays and prisms | Elemental turrets inherit some focus traits and discount matching spells; elemental Source Relays raise capacity/transfer; prism lenses can alter redirected spells and some consume Source. | Utility and defense. Teach placement/configuration, Source input and limits; not covered by the pilot's 3 quests. |
| Other items / dimensions | Exact 0.7.10.4 generated recipes include caster bags, elemental bangles, flight alternative, prism lenses, elemental relays/turrets, specialized spells and caster gear; source also contains archwood/biome worldgen and Vexing Caves. | These are exact-version source findings, but their acquisition, structure/biome conditions and current JEI visibility still need walkthrough review. |

## Walkthrough map

1. Choose and craft a lesser Focus; equip and compare an affected and unaffected spell/school.
2. Build elemental armor via JEI recipe chain; teach its tier, school bonuses, full-set condition and thread/enchantment retention.
3. Configure an Urn into a test cauldron, confirm Source decreases and water appears; test unsupported/no-Source boundaries.
4. For any chosen helper/shrine/turret/prism branch, teach its acquisition, placement/configuration, resource cost, observable operation and maintenance.
5. Keep worldgen/exploration content optional; locate exact biome/structure rules and Notebook entry before promising an item route.

The pilot chapter has Lesser Fire Focus, Urn of Endless Waters and Pyromancer's Hat (quest IDs 35–37). It has no tutorial for Sirens, Flashjacks, elemental turrets/relays/prisms, foci for other schools, complete armor upgrades or worldgen. These are named coverage gaps, not completed quests.

## Evidence and tests

- Exact pin/hash and item/recipe evidence: `manifest/ars_quest_pilot.json`.
- Version-ahead primary source for focus and subsystem descriptions: [AELangDatagen.java at commit f2a1dd1](https://github.com/Alexthw46/Ars-Elemental/blob/f2a1dd1faa2ad7e6ad00c2919120ea4fc241cdf0/src/main/java/alexthw/ars_elemental/datagen/AELangDatagen.java).
- Unresolved: inspect the exact JAR against release commit 2c433b6; verify recipes, biome/loot conditions, attribute hooks and all addon guide entries in the assembled pack.
- Not run: Source-to-water output and rate; full-set armor trigger; Siren Shrine source/output cycle; turret focus synergy; prism costs and target boundary.
