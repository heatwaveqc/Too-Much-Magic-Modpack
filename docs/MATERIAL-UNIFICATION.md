# Too Much Magic — Material Unification (candidate)

Status: **source-and-live-tag audited; dedicated-server smoke passed for the initial configuration; new Neo Vitae silver/lead extension still needs a regression server boot, and gameplay/client/worldgen are not verified or release-approved**.

## Policy

- One preferred ore (per stone variant), raw material, ingot/gem and dust for each conventional material. All recipe consumers should accept `c:` tags; player-facing outputs prefer one item.
- Almost Unified 1.4.2 NeoForge handles supported recipe, JEI, loot and vanilla-style ore-feature rewriting. It does not add ores or fix custom generation.
- Source-audited whitelist: copper, diamond, emerald, gold, iron, lead, quartz, silver, sulfur and saltpeter. Aluminum, nickel, platinum, tin and zinc are **not** force-unified: the actual installed sources inspected so far do not demonstrate duplicate standard ore sets for them. No Create dependency.
- Preferred material providers: **Occultism for silver** (ore/raw/ingot/dust), **Embers for lead** (ore/raw/ingot), and Minecraft for copper/iron/gold/diamond/emerald. Mahou wins iron/gold/diamond/emerald/quartz *dusts only*, while Occultism wins copper dust and Neo Vitae wins sulfur dust. Primal Magick grits, Eidolon, Werewolves and Embers are now part of the priority list. The provider inventory and exact source-backed IDs are in `manifest/material-ore-audit.json`.
- No uranium, draconium, iesnium, Replication ore, arcane gold, Malum's transformed metals or Pastel's dimension-specific ores in unification. Corrupted Dust, Ender/Eye powder and ritual items remain distinct. Occultism/Eidolon/Embers/Werewolves conventional silver ore variants are unified; Eidolon and Embers lead ore variants are unified.

## Verified duplicate ore registry sets (source-backed, not yet a runtime dump)

| Material | Preferred | Confirmed duplicate suppliers | Source evidence |
|---|---|---|---|
| **Silver** | `occultism` | `occultism:silver_ore`, `embers:silver_ore`, `eidolon_repraised:silver_ore`, `werewolves:silver_ore`, plus all four deepslate variants | Each mod's published 1.21.x `data/c/tags/item/ores/silver.json` |
| **Lead** | `embers` | `embers:lead_ore`, `eidolon_repraised:lead_ore`, plus deepslate variants | Each mod's `data/c/tags/item/ores/lead.json` |
| **Copper dust** | `occultism` | `occultism:copper_dust`, `primalmagick:copper_grit` | Item tag `c:dusts/copper` |
| **Iron/gold dust** | `mahoutsukai` | Mahou powders, Primal Magick grits; Occultism gold dust | Item tag `c:dusts/iron` / `c:dusts/gold` |
| **Sulfur dust** | `neovitae` | `eidolon_repraised:sulfur`, Neo Vitae's dynamic sulfur dust | Item tag `c:dusts/sulfur` |

Embers and Eidolon have separately verified natural silver/lead ore placed features. Occultism and Werewolves also generate silver by configured ore features. These should converge through Almost Unified **for newly generated chunks**. In old chunks, existing duplicate ore blocks remain, but their drops and smelting recipes should converge.

**These specific source files have been inspected** in the 1.21.1/1.21 GitHub source branches. The exact pinned JAR registries and the dynamic Neo Vitae materials must still be compared against a dedicated-server item/tag dump. `manifest/material-ore-audit.json` is the machine-readable inventory; it is not a fabricated claim of complete runtime coverage.

### Material-specific exclusions

- Eidolon's **Arcane Gold** stays different from gold; Malum's **Cthonic Gold**, modified silver/lead variants, and soulstone remain different.
- Pastel's special ores, including blackslag/deep-dark mineral variants, are separate because their drops and dimension mechanics matter.
- Primal Magick's rock salt is not nether quartz; its **iron, gold and copper grits** do participate in dust unification.
- Draconium, uranium, Iesnium and Deepslate Replica Ore are not regular metals in our pack progression; do not standardize or expose their generation.

## Live NeoForge verification (2026-11-14)

- Dedicated NeoForge server completed cleanly with 128 hash-verified server-side mod JARs and initial Almost Unified configuration: [Actions run 43614734834](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43614734834), [artifact 14121900565](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43614734834/artifacts/14121900565).
- Recovered **actual Almost Unified `tags.txt`** from live JARs: `c:ores/silver` has **eight** blocks from Occultism, Embers, Eidolon and Werewolves; `c:ores/lead` has **four** blocks from Embers and Eidolon. `c:raw_materials/silver`, `c:ingots/silver`, `c:nuggets/silver`, and silver storage tag each have four items.
- **Important oversight corrected**: Deeper & Darker contributes `gloomslate_*` and `sculk_stone_*` versions of vanilla ores; Pastel contributes `blackslag_*` variants. These special-dimension strata are explicitly excluded from vanilla ore replacement in AU with `ignored_items`; their tag membership remains, but unification deliberately does not hide/rewrite those ore blocks.
- Live dusts: gold dusts are Mahou, Neo Vitae, Occultism, Primal Magick; iron dusts the same four. Copper dusts are Neo Vitae, Occultism, Primal Magick. **Sulfur tag uses `neovitae:sulfur` and `eidolon_repraised:sulfur`**, not `neovitae:sulfur_dust`.
- Recovered complete **12-entry Neo Vitae 1.1.53-generated `config/neovitae/materials.json`** from the server artifact. Preserved all original entries and appended **silver** and **lead** to make them valid Athanor/Tabula Vitae dust-processing materials. Their `smelt_to` points to `occultism:silver_ingot` and `embers:lead_ingot` respectively, and `generative:false` prevents extra meteor/dungeon/Prismatic-Demonite injections. Both tags have verified items and canonical targets. Generated `neovitae:silver_dust` should fall into `c:dusts/silver` and `neovitae:lead_dust` into `c:dusts/lead`.
- The **new** 14-material config requires its own fresh smoke after these final edits. A clean startup does not prove worldgen, custom processing results, JS recipe behavior or the GUI.

## Mahou early-game processing

- `raw_iron` or `raw_gold` + Mahou Hammer -> 1 matching Mahou powder.
- `raw_iron` or `raw_gold` + Mahou Mortar and Pestle -> 2 matching Mahou powders.
- Any `#c:dusts/iron` or `#c:dusts/gold` -> 1 ingot by smelting/blasting.
- Native Occultism iron/gold ingot-crushing shortcuts are removed pending exact output-count audit.
- Mahou diamond/emerald/quartz hammer stays 1:1 on gems. Mortar's 2-powder recipe requires a Silk Touch ore block, not one gem; prevents gem -> 2 powders -> 2 gems.
- `mahoutsukai:powdered_ender` and `powdered_eye` are magical reagents, not metal or gem dust.

## Release gates

1. **Registry and JEI**: the server AU tag dump establishes canonical inputs and raw IDs; still confirm Mahou recipe IDs, tool remainders (not consumed), JEI hiding and final recipe targets in a client world. A KubeJS parse is not a working recipe.
2. **Neo Vitae**: 12 defaults were preserved from the actual generated 1.1.53 file, with two more source-backed silver/lead processing materials. Confirm added `neovitae:silver_dust` and `neovitae:lead_dust` dynamically register and integrate, and verify `generative:false` prevents extra ore distribution. Re-run discovery review if future mods are added.
3. **Dust loops**: for every smeltable metal and gem, inspect all input and output counts for Mahou, Occultism, Neo Vitae, Theurgy, crafting, smelting and blasting; no post-ingot route may yield more than 1 ingot-equivalent per ingot. Check custom furnace recipe types too.
4. **World generation**: `world_gen_unification:true` removes duplicate *conventional* ores in new chunks only. Check AU log and representative stone/deepslate/Nether ores. If a canonical source does not generate or a custom generator bypasses AU, block release until fixed. `/almostunified strip` is destructive: disposable worlds only.
5. **Special gates**: no uranium, draconic materials or Deepslate Replica Ore via ordinary generation, Neo Vitae meteor/dungeon, Botania Orechid, Occultism miner, Theurgy, loot, quests or EMC before intended gates. **This candidate does not implement those resource gates.**
6. **World safety**: do not test on existing saves. Old chunks retain old ores; old duplicate items should still be accepted as tag inputs.

## Functional test matrix (disposable world)

| Test | Expected |
|---|---|
| Raw iron + hammer | 1 Mahou powdered iron; tool preserved |
| Raw iron + mortar/pestle | 2 Mahou powdered iron; tool preserved |
| Raw gold + mortar/pestle | 2 Mahou powdered gold |
| Iron/gold ingot + mortar | No 2-powder path |
| Mahou/Occultism/Neo Vitae iron dust in furnace | 1 iron ingot per dust |
| Diamond/emerald/quartz gem + mortar | No 2-powder output |
| Silk Touch diamond/emerald/quartz ore + mortar | 2 powders, provided corresponding tags exist |
| Occultism 2x/3x/4x/6x and Neo Vitae base/full processing | Existing yields preserved, output canonical |
| JEI silver/iron/gold variants | One preferred icon; existing alternatives consumed correctly |
| Ore generation in new chunks | No duplicate ordinary veins; no special-resource gate leaks |
| ProjectE pre/post Chaos Guardian | No pre-gate EMC path; intended post-gate behavior intact |

## References

- https://docs.almostreliable.com/almostunified/
- https://docs.almostreliable.com/almostunified/config/unification.html
- https://docs.almostreliable.com/almostunified/features/world_gen.html
- https://docs.almostreliable.com/almostunified/integration/kubejs.html
- https://mahoutsukai.stepsword.com/guides/getting-started/
- https://github-wiki-see.page/m/breakinblocks/NeoVitae/wiki/Pack-Makers-Materials-and-Dungeons

Unresolved: dedicated-server boot, client/JEI behavior, mod-specific ore-generator audit, special resource gates.
