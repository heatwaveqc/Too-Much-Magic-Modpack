# Too Much Magic — Material Unification (candidate)

Status: **implemented as a reviewable candidate; NOT game-tested or release-approved**.

## Policy

- One preferred ore (per stone variant), raw material, ingot/gem and dust for each conventional material. All recipe consumers should accept `c:` tags; player-facing outputs prefer one item.
- Almost Unified 1.4.2 NeoForge handles supported recipe, JEI, loot and vanilla-style ore-feature rewriting. It does not add ores or fix custom generation.
- Conventional whitelist: aluminum, copper, diamond, emerald, gold, iron, lead, nickel, platinum, quartz, silver, tin, zinc. Expand only after inspecting installed tags. No Create dependency.
- Priority: Minecraft, Mahou, Occultism, Neo Vitae, Theurgy. Mahou wins for iron/gold/diamond/emerald/quartz *dusts only*; other dusts favor Occultism then Neo Vitae.
- No uranium, draconium, iesnium, Replication ore, or special reagents in unification tags. Corrupted Dust, Ender/Eye powder and ritual items remain distinct.

## Mahou early-game processing

- `raw_iron` or `raw_gold` + Mahou Hammer -> 1 matching Mahou powder.
- `raw_iron` or `raw_gold` + Mahou Mortar and Pestle -> 2 matching Mahou powders.
- Any `#c:dusts/iron` or `#c:dusts/gold` -> 1 ingot by smelting/blasting.
- Native Occultism iron/gold ingot-crushing shortcuts are removed pending exact output-count audit.
- Mahou diamond/emerald/quartz hammer stays 1:1 on gems. Mortar's 2-powder recipe requires a Silk Touch ore block, not one gem; prevents gem -> 2 powders -> 2 gems.
- `mahoutsukai:powdered_ender` and `powdered_eye` are magical reagents, not metal or gem dust.

## Release gates

1. **Registry and JEI**: confirm every Mahou powder and recipe ID, `c:` tag, tool-remainder behavior (tools not consumed), and final AU targets. A KubeJS parse is not a working recipe.
2. **Neo Vitae**: `config/neovitae/materials.json` creates generated fragments/gravels/dusts, and may inject ores into special generation sources. Inspect exact pinned 1.1.53 generated config; set `generative:false` for gated materials and `gen_ore`/`gen_raw` only with real IDs. Do not guess item IDs.
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
