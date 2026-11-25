# Primal Magick 6.0.7 — exact pinned release-JAR census and static parity

**Evidence status: PUBLISHED BINARY MATCH + ZIP/DATA/SELECTED BYTECODE VERIFIED; NOT TMM RUNTIME PASS.** Owner supplied the artifact `primalmagick-neoforge-1.21.1-6.0.7.jar` for a *read-only local* inspection on 2026-11-23. **The JAR itself, compiled class files, textures, source artifacts and generated game saves are not committed to Git.** The earlier [source-only dossier](../mods/primal-magick.md) is supplemented, not replaced.

| Release property | Verified artifact value |
| --- | --- |
| Exact filename | `primalmagick-neoforge-1.21.1-6.0.7.jar` |
| Bytes | **9,408,493** |
| SHA-1 | `165a849fe2b95355067ede9cda014dec08129731` — **identical to TMM's installed Packwiz pin** |
| SHA-256 | `f78cab2206b5c05c1d9615cf42bd2d728971156343658c2354bd496b7544f98f` |
| ZIP entries | **9,228**, no duplicate paths; `ZipFile.testzip() = None` (CRC valid) |
| Embedded mod | `META-INF/neoforge.mods.toml`: `modId = primalmagick`, `version = 6.0.7`, Minecraft `[1.21,1.22)`, NeoForge `[21.1.132,)`, `commonnetworking` `[1.0.18,)` |
| Source comparison basis | Official [6.0.7 source commit `5977468bda968b994e54066236ec5719295ac9c3`](https://github.com/daedalus4096/PrimalMagick/tree/5977468bda968b994e54066236ec5719295ac9c3); previous [855 source builder callsite index](primal-magick-6.0.7-arcane-workbench-recipes.md) |

## A. **970 physically bundled recipe JSONs** vs 855 literal Java builder callsites

All **970** paths matching `data/*/recipe/*.json` successfully parse as JSON, 925 in the `primalmagick` namespace and **45 in `minecraft`**. A `minecraft` namespace path by itself **does not imply replacing a vanilla recipe**; the ID determines override behavior. The generator's former **855** count was explicitly *Java builder invocation locations*, not generated output files. The following table now resolves both counts.

| Recipe kind | Source builder callsites | **JAR JSON files** | Materialized delta |
| --- | ---: | ---: | ---: |
| Arcane Shaped | 290 | **316** | +26 |
| Arcane Shapeless | 176 | **202** | +26 |
| Concocting | 122 | **122** | 0 |
| Ritual | 76 | **76** | 0 |
| Vanilla Shapeless | 58 | **93** | +35 |
| Vanilla Shaped | 35 | **35** | 0 |
| Stonecutting | 54 | **54** | 0 |
| Smelting | 7 | **7** | 0 |
| Blasting | 4 | **4** | 0 |
| Runecarving | 10 | **10** | 0 |
| Dissolution (direct item result) | 12 | **12** | 0 |
| Special recipe serializers (Wand Assembly, Inscription, Spellcrafting, etc.) | 10 | **10** | 0 |
| Smithing Trim | 1 | **1** | 0 |
| Conditional tag-output Crafting Shapeless | Not in ordinary 855 callsite count | **14** | +14 |
| Conditional tag-output Dissolution | Not in ordinary 855 callsite count | **14** | +14 |
| **Total** | **855** | **970** | **+115** |

This is a **file/type comparison**, not a claimed one-to-one `Recipes.java` expression→JSON ID proof. The deltas are consistent with source generator loops and additional tag-output generator routes. The authoritative 970 files are now inspectable with exact IDs, NBT/data components, conditions, output counts, Research requirements and centimana amounts. Do **not** rewrite the old callsite index as though it was false: it measured a different valid quantity.

**28 packaged recipes have `neoforge:conditions`**, all condition groups contain two `not(tag_empty)` checks, and consist of the **14 tag-output Crafting** and **14 tag-output Dissolution** variants. Some/all can be rejected at datapack load time depending on TMM-effective tags. Therefore even **970 bundled JSONs ≠ 970 confirmed recipes in an active TMM world**. Effective runtime tags and other mod datapack overrides were **not** inspected.

## B. Research, theory, books and first-player walkthrough actually bundled

These resource paths are present and valid JSON:

| Registry/resource in the pinned JAR | JSON files | Confirmation |
| --- | ---: | --- |
| `data/primalmagick/primalmagick/research_entries/` | **298** | Matches 298 registered research names mapped in [native stage graph](primal-magick-6.0.7-research-stage-graph.md); the individual JSON has exact `parents`, `stages`, `completionRequirementOpt`, `recipes` and `attunements` |
| `data/primalmagick/primalmagick/project_templates/` | **38** | Matches all 38 [source project families](primal-magick-6.0.7-theorycraft-projects.md) |
| `data/primalmagick/primalmagick/research_disciplines/` | **8** | Actual research discipline resource definitions |
| `data/primalmagick/primalmagick/books/` | **70** | **Separate static lore/book definitions**; do not count them as 70 Grimoire research chapters |
| `assets/primalmagick/lang/en_us.json` | **3,287 translated keys** | Exact English First Steps stage text and menu labels. Other bundled locales: es_es, zh_cn, it_it, fr_fr, de_de |

In the **bundled `first_steps.json`** stage graph, the first four stages show (in order): **craft Arcane Workbench**, **10 Mana siphoned**, **1 Observation made**, then a final completion stage. The bundled English guide text explains the **Mundane Wand→crafting table** transformation for Arcane Workbench; after that, return to a Shrine holding the Wand for Mana, and later make a **Wood Table + Magnifying Glass + Paper** for Analysis. This confirms and improves the player-facing [41-node research-only map](../mods/primal-magick-quest-map.md); the map remains a teaching graph, not 41 implemented FTB tasks.

**Selected direct JSON checks:**

- `data/primalmagick/recipe/mundane_wand.json`: **normal Minecraft shapeless**, `c:rods/wooden` + `primalmagick:essences/terrestrial_dusts`. No Arcane Workbench required to craft the first Wand.
- `data/primalmagick/recipe/analysis_table.json`: Wood Table + Magnifying Glass + Paper, `primalmagick:first_steps` **research stage 2**.
- `data/primalmagick/recipe/research_table.json`: Wood Table + Book + Paper, `primalmagick:theorycrafting` **research stage 2**.
- `data/primalmagick/recipe/manafruit.json`: **Apple + Honey Bottle + Mana Salts**, one `primalmagick:ritual_candles` prop, instability **1**, **500 centimana of each** Earth/Sea/Sky/Sun/Moon (5 displayed Mana each), actual `primalmagick:manafruit` Research requirement. [Previous source-only ritual inventory](primal-magick-6.0.7-ritual-recipes.md) is **binary-resource confirmed** for this example.

## C. Creative-tier and ore-economy integration findings

**Creative Grimoire is confirmed in the compiled release.** Using `javap -c -p` on the bundled `com.verdantartifice.primalmagick.common.items.misc.GrimoireItem` class, the server-side `use` method checks `unlockAll` and directly invokes `ResearchManager.forceGrantAll(Player)`, then opens the UI via `OpenGrimoireScreenPacket`. The registered `primalmagick:grimoire_creative` item uses that path in upstream source, distinct from normal `primalmagick:grimoire`. The compiled `SpellPackage.getManaCost` also shows the source-prescribed **payload→single Source→vehicle/mod flat additions×multipliers** calculation. **This confirms the relevant release-class behavior without asserting that an arbitrary world can obtain the creative book**.

A full-text scan of the packaged recipe JSON found **no `result.id` containing `creative`**, and no recipe/loot JSON refers to `grimoire_creative`; the item's occurrences are in registered item model, affinities, **bookshelf/lectern tags** and language assets. Creative `mana_singularity_creative` **does** have a standard *self-drop block loot table* (drops itself if an already-placed creative block is mined), which is **not** proof of natural generation or survival obtainability. Continue the global creative-acquisition audit [#596](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/596) against all combined mods/loot/EMC/scripts/shops; **do not delete the legitimate normal Grimoire**.

**Uranium gate/ore economy:** 6.0.7 bundles four uranium-related **conditional** recipes. With `c:dusts/uranium` and a corresponding nonempty ore/raw tag, the Earthshatter Hammer produces **2 tagged uranium dust** from **one tagged uranium Ore or Raw Material**, and the Dissolution Chamber uses **10 Earth Mana** to produce **3 tagged uranium dust** per equivalent input. None of those JSONs manufactures an initial ore/raw uranium item from common materials. However, the mod's `lucky_strike` loot modifier also has a **`c:ores/uranium → c:nuggets/uranium`** mapping, subject to the Lucky Strike enchantment and event conditions. **Check this against TMM's owner-approved uranium access and canonical dust/tag policy**, particularly renewable ore generation and EMC feedback. Do not assert a bypass without the actual TMM combined datapack and loot context.

## D. Reproducibility and untested boundary

- Inspected the uploaded release JAR **in place**, read-only, via Python standard library `hashlib`, `zipfile` and `json`, plus JDK `javap` for selected compiled classes; no JAR extracted into the repository or committed.
- The ZIP's **9,228 entries** have valid CRC and no duplicate filenames; all 970 recipe JSONs and 298 Research/38 Project/8 Discipline/70 book JSONs parse.
- **Static binary/data parity CONFIRMED** for release version, Packwiz SHA-1, listed recipe files, Research/Project counts, specific first steps/ritual requirements and selected bytecode methods. This does **not** prove every upstream source statement is byte-for-byte identical to the distributed JAR, nor prove every bundled recipe loads with TMM's other mods.
- **Still required for in-game PASS:** assembled effective tags/datapack override resolution, NeoForge/client/server start, UI navigation, special recipe handlers, item drops/creative gate, worldgen/Tectonic and all cross-mod protected-block/Spell effects. Per TMM policy, no full-world/Prism launch or build without owner authorization.
