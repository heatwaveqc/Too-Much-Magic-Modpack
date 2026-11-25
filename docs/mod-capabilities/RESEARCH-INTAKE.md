# Atlas mod research intake — live magic roster and new approvals

**Updated 2026-11-24 from the current `test/full-integration` manifest and Packwiz index.** This is a **resumable Atlas research queue**, not a release plan, implementation task, ratings table or gameplay-test list. It closes the intake gap after the new mod selections: **Arcane Archives: Reborn** and **Aura Cascade Reimagined** were indexed in the modpack but missing from Atlas; **Aquamirae & Nouveau** and **Unicorn Academy: Bonded Magic** likewise lacked standalone Atlas entries. The older [Mod Comprehension INDEX](../mod-comprehension/INDEX.md) still represents an older 2026-11-15 roster snapshot. Consult the [Atlas index](README.md) and [Atlas issue #410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410) before starting work.

**Current progress:** Arcane Archives 1.0.2 has completed its [binary/source-backed 29-axis capability dossier](mods/arcane-archives-reborn.md) and [exact release JAR census](inventories/arcane-archives-reborn-1.0.2-pinned-jar-census.md): all 34 recipes, 40 guide entries, 24 advancements, registered items/blocks and core source behavior. **Arcane Archives, Aura Cascade Reimagined, Astral Sorcery, JAWR, Goety, Mahou Tsukai, Forbidden & Arcanus, Hexerei and Hexcasting are research COMPLETE, not QA tested. Psi is the next proposed standalone target.**

**Evidence and status:** [`manifest/mods.json`](../../manifest/mods.json) records the exact named selections, versions, `enabled_in_pack` and release status. [`index.toml`](../../index.toml) plus the respective `mods/<packwiz_slug>.pw.toml` file confirm every entry **in the four sections below** has an indexed Packwiz source at this snapshot. **Indexed means selected/source-pinned, not gameplay-verified.** Queued entries may lack their own `docs/mod-capabilities/mods/<manifest-id>.md` dossier; some have coverage in parent family dossiers or Mod Comprehension—reuse it. Refresh the current live roster and profile paths before beginning any research. "Priority" here means **work intake order**, not mod quality or importance.

**Owner-defined boundary:** The Atlas researches released third-party mod capabilities from exact JAR/source/data/guide evidence. Normal released-mod behavior is presumed to function. No automatic game testing, redesign, recipe changes, FTB quest implementation or pack integration is authorized. Escalate only a genuine source ambiguity that cannot be resolved through further inspection or an actual pack-specific cross-mod operation with unresolved input/output semantics.

## 1. New major magic systems — first Atlas research targets

| Manifest ID | Mod | Manifest version | Release designation | Atlas research scope |
| --- | --- | --- | --- | --- |
| `arcane-archives-reborn` | [Arcane Archives: Reborn](mods/arcane-archives-reborn.md) | `1.0.2` | **Atlas source/binary COMPLETE** | **DONE** — exact 1.0.2 published JAR hash, [34 recipes/40 Tome pages](inventories/arcane-archives-reborn-1.0.2-pinned-jar-census.md), 29 axes, 27 research-only source nodes, concrete 32,768-item Trove/16-bucket Tank capacities and upgrade tiers, code-backed search/routing/ownership. **No gameplay validation required for Atlas completion.** |
| `aura-cascade-reimagined` | [Aura Cascade Reimagined](mods/aura-cascade-reimagined.md) | `0.2.1+1.21.1` | **Atlas research COMPLETE** (experimental release) | **DONE** — exact JAR hash, 29 axes, 36 instructional nodes; Miner tag interaction belongs to integration. |

## 2. New magic addons and newly selected player-facing magic — deferred behind major systems

| Manifest ID | Mod | Manifest version | Release designation | Atlas research scope |
| --- | --- | --- | --- | --- |
| `aquamirae-nouveau` | Aquamirae & Nouveau | `0.1.3` | Verified release pin | **DEFERRED** — [initial publisher/source checkpoint](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7137136469). Exact Ars Nouveau–Aquamirae bridge: new effects/glyphs, materials/recipes, source-to-source I/O and native gates; reuse existing Ars research. |
| `unicorn-academy-bonded-magic-unofficial` | Unicorn Academy: Bonded Magic (unofficial) | `1.21.1-1.0.0` | Experimental | Source-grounded bonding, magic abilities/flight, gates and world content. Experimental release; do not infer whole-game behavior from inclusion metadata. |
| `occult-cooking` | Occult Cooking | `1.0.1` | Verified release pin | Native Occultism cooking recipe/effect interface. Treat as focused addon unless its source has a substantial independent progression. |

## 3. Major standalone magic systems — active priority queue

| Manifest ID | Mod | Manifest version | Release designation | Atlas research scope |
| --- | --- | --- | --- | --- |
| `astral-sorcery` | [Astral Sorcery](mods/astral-sorcery.md) | `AstralSorcery-2.0.3.59` | **Atlas research COMPLETE** (experimental release) | **DONE** — version-matched source + prior exact JAR resource audit, 29 axes, 47 lessons. |
| `witchery-remake` | [Just Another Witchery Remake](mods/witchery-remake.md) | `witchery-0.5.39+mc1.21.1` | **Atlas research COMPLETE** | **DONE** — exact JAR SHA512 and bytecode, 29 axes, 68 lessons, both independent 10-level how-tos, guide unlock conditions. TMM overrides separate. |
| `goety` | Goety — The Dark Arts | `goety-3.2.3` | Research COMPLETE — [dossier](mods/goety.md), #668 | Soul energy, servants, rituals, spells, gear, bosses and native unlock sequence. |
| `mahou-tsukai` | Mahou Tsukai | `mahoutsukai-1.21.1-v1.36.31` | Research COMPLETE — [dossier](mods/mahou-tsukai.md), #671 | Mana, circles, rituals, spell effects, material costs and actual utility/resource loops. |
| `forbidden-arcanus` | Forbidden & Arcanus | `forbidden_arcanus-2.6.1` | Research COMPLETE — [exact-JAR dossier](mods/forbidden-arcanus.md) | Native magical materials, ritual workstations, unlocks, resource costs and effects. |
| `hexerei` | Hexerei | `hexerei-0.5.0.3` | Research COMPLETE — [exact-JAR dossier](mods/hexerei.md) | Brooms, cauldrons, ingredients, mixing, witchcraft and magical utilities. |
| `hexcast-neoforged` | HexCast NEOFORGED | `hexcast-neoforged-1.21.1-0.11.1-19+neoforge.1.21.1` | Research COMPLETE — [exact-JAR dossier](mods/hexcast-neoforged.md) | Patterns, media sources, spell programming/casting, costs and tiers. |
| `psi` | Psi | `Psi-neoforge-1.21.1-110` | Verified release pin | Spell programming, resources, casting devices, costs and capabilities; link Psi Tweaks. |
| `eidolon-repraised` | Eidolon: Repraised | `eidolon_repraised-1.21.1-0.5.0.8` | Verified release pin | Rituals, resource collection, crafting workstations, spell progression and tiers. |
| `embers-re-ignited` | Embers Re-Ignited | `embers-reignited-1.21.1-1.5.9` | Verified release pin | Ember power, machines, logistics, augmentation and advanced processing. |
| `enchanted-witchcraft` | Enchanted: Witchcraft | `enchanted-neoforge-1.21.1-4.2.7` | Verified release pin | Native witchcraft equipment, rituals and progression in the pinned version. |
| `feywild` | Feywild | `feywild-1.21.1-5.6.4` | Verified release pin | Fey creatures/factions, magical resource loops and player-facing advancement. |
| `pastel` | Pastel | `Pastel-1.1.5.5` | Verified release pin | Native magic and color progression, resources, spell/recipe systems. |
| `scriptor-magicae` | Scriptor Magicae | `scriptor-neoforge-2.0.14-1.21.1-sgd` | Verified release pin | Casting lecterns, programmable spellcasting and resource/automation flows. |
| `summoning-rituals` | Summoning Rituals | `summoningrituals-neoforge-1.21.1-3.14.2` | Verified release pin | Actual supported ritual item/entity inputs, outputs, timings and source/config extension limits. |
| `totemic` | Totemic | `Totemic-neoforge-1.21.1-0.12.18` | Verified release pin | Totemic native rituals and effects; mark TMM ritual-only use separately. |
| `reliquary-reincarnations` | Reliquary Reincarnations | `reliquary-1.21.1-2.0.85.1599` | Verified release pin | Distinct useful magical items, tools, consumables/charges and usage limits; full tier path only if native. |
| `projecte` | ProjectE | `ProjectE-1.21.1-PE1.1.0` | Verified release pin | Native EMC/transmutation capabilities and progression. Keep the TMM boss gate as separate pack design. |

## 4. Additional addons and encounter-adjacent research

| Manifest ID | Mod | Manifest version | Release designation | Atlas research scope |
| --- | --- | --- | --- | --- |
| `goety-cataclysm` | Goety Cataclysm | `goety_cataclysm-1.21.1-1.8.4` | Verified release pin | Exact addon-specific abilities, rituals/mobs, recipes and parent/child distinction. |
| `psi-tweaks` | Psi: Tweaks and Additions | `psitweaks-1.21.1-0.10.10-hotfix` | Experimental | Only added/changed Psi mechanics and costs; no duplicate Psi dossier. |
| `ars-conflux` | Ars Conflux | `0.1.56` | Verified release pin | Library/interface evidence for Aquamirae & Nouveau; do not invent player-facing progression. |
| `aquamirae` | Aquamirae | `7.2.13` | Verified release pin | Source/loot/encounter inputs needed for the linked Ars addon, plus native adventure scope. |
| `mowzies-mobs` | Mowzie's Mobs | `1.8.2` | Verified release pin | Native bosses/mobs/abilities, items, encounters and magic-like drops; adjacent, not assumed full magic school. |
| `sons-of-sins` | Sons of Sins | `2.2.1d` | Verified release pin | Source-based encounters and magic-relevant native loot/abilities; no hypothetical ritual content. |
| `the-graveyard` | The Graveyard | `4.0.1` | Verified release pin | Native encounters, world content and any genuine necromancy/magic items. |
| `born-in-chaos` | Born in Chaos | `1.7.6` | Verified release pin | Native mobs/encounters, drops and any source-confirmed magical tools. Hunter taxonomy is downstream. |


## 5. Approved or discussed separately, but not currently Packwiz-indexed

**Doctor Who: Weeping Angels** was selected in [#387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387), but no matching record or Packwiz source file appears in the current selected manifest/index as of this snapshot. Its native teleport/dimension behavior may merit a **separate adjacent Atlas research slice after identity/pin reconciliation**; inclusion configuration belongs to the owner/integration workstream, not Atlas. **Runes of Wizardry**, **Thavma** and **Primeva** remain research-only/unresolved candidates according to [#431](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/431); they should not be represented as installed magic mods without a selected, version-compatible release.

## 6. Already covered — avoid redundant Atlas re-research

Existing major Atlas dossiers represent **HexCast NEOFORGED, Hexerei, Forbidden & Arcanus, Goety, Mahou Tsukai, JAWR, Astral Sorcery, Arcane Archives, Aura Cascade, Ars Nouveau and its seven already surveyed addons, Botania Revived + Extra Botany Revived, EvilCraft, Iron's Spells 'n Spellbooks, Malum + Gaze, Neo Vitae, Occultism, Primal Magick, Roots Classic and Theurgy**. Some family addons have no individual standalone filename by design; check the parent dossier before assigning new work. Example: Aquamirae & Nouveau needs **new addon-specific** research but no new study of the entire Ars Nouveau spell system.

For the new storage mod, preserve the owner-approved **Arcane Archives early storage → Occultism personal/remote storage → ProjectE endgame transmutation** as a *teaching comparison*, not a mechanical prerequisite or suggested feature trim.

## 7. Research status and next action

Entries are **queued, research-in-progress, or completed with direct source/JAR evidence**, never complete just because they have a manifest entry and never blocked because nobody ran gameplay. **Arcane Archives, Aura Cascade, Astral Sorcery, JAWR, Goety, Mahou Tsukai, Forbidden & Arcanus, Hexerei and Hexcasting have completed their native surveys**; proposed next **Psi**. Owner priority: standalone magic systems with substantial quest lines first; focused addons/bridges/libraries and encounter-adjacent research deferred, regardless of earlier section order. Evaluate independent systems such as Unicorn Academy on actual chapter scope. Ordinary JEI recipes and nonessential numerical details do not block completion; research progression gates and meaningful operating conditions. No ordinary native gameplay-validation prerequisite remains for completed modules. Continue section 3 major systems before deferred focused addons. When a profile is complete, link it from [Atlas README](README.md) and record its exact pinned version, research conclusions, source confidence and any truly unresolved mechanics here. Reconcile after every roster promotion/change; do not quietly drop newer modules.

