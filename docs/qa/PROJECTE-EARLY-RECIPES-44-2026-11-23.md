# Issue #44 — ProjectE early utility / Covalence Dust recipe slice

**Status:** source implementation staged for review, **NOT** integrated or accepted in-game.
Approved decisions: #44 owner lock of 2026-11-21. This source slice is
**not** the Progression engine and does not modify MXP, player Paths or
ProjectE tooltip milestone implementation.

## Source changes

| Native ProjectE recipe ID | Source-only outcome |
| --- | --- |
| `low_covalence_dust`, `medium_covalence_dust`, `high_covalence_dust` | Native 40-output crafting recipes disabled via same-ID `neoforge:false`; **separate virtual EMC mapping conversions added** to retain their native input costs, without physical crafting |
| `repair_talisman` | Exactly one Eternal Stella + one Occultism Iesnium Anvil, shapeless, one Talisman |
| `repair_talisman_alt` | Original covalence/paper/string variation disabled via same-ID `neoforge:false` |
| `divining_rod_1` | 8 Neo Vitae Iron Dust + center vanilla Stick |
| `divining_rod_2` | 8 Neo Vitae Diamond Dust + center Divining Rod I |
| `divining_rod_3` | 8 Occultism Netherite Dust + center Divining Rod II; NOT Neo Vitae Netherite Scrap Dust |
| `evertide_amulet` | Retain three Dark Matter, five Water Buckets, replace top-center Water Bucket with Neo Vitae Water Sigil |
| `volcanite_amulet` | Retain three Dark Matter, five Lava Buckets, replace top-center Lava Bucket with Neo Vitae Lava Sigil |

Recipe overrides are in `kubejs/data/projecte/recipe/<id>.json`, matching
the pinned ProjectE `mc1.21.1` generated native recipe IDs. The shapes,
outputs and original ingredients were checked against the matching official
generated files. The Talisman and sigil inputs were checked against
Forbidden & Arcanus `ModItems`, Occultism Iesnium Anvil generated assets
and Neo Vitae `NVItems`. Iron and Diamond dust stages were checked against
the **actual TMM `config/neovitae/materials.json`**.

**EMC values are part of this source slice, not deferred to #47.**
The new `kubejs/data/tmm/pe_custom_conversions/covalence_dust_virtual.json`
uses ProjectE's native `CustomConversionMapper` and three `count: 40`
conversions, matching the old vanilla recipes exactly: charcoal + 8
cobblestone, iron ingot + redstone, diamond + coal. These are
**EMC calculation edges only**; they never expose a crafting recipe.
Under original ProjectE input prices they reproduce the familiar
1/8/208 values, but TMM's effective values follow the mapped inputs
rather than freezing those historical numbers. The exact three
conversions and ingredients are enforced by the source validator and
negative fixtures. The mapper must be enabled in the integrated build;
positive post-mapping output is checked **after integration**.

**Already integrated, DO NOT duplicate:** the two top-center
Philosopher's Stone recipes for `collector_mk1` and `relay_mk1` exist
in `test/full-integration` from completed prior work. This PR does
not modify either. Validator asserts both are still Stone-gated.

## Validation

Run local read-only checks:

```sh
python3 scripts/validate_progression_gates.py
python3 scripts/validate_projecte_early_recipes.py
python3 -m unittest discover -s scripts -p 'test_validate_projecte_early_recipes.py' -v
```

The dedicated `ProjectE early recipe source safety (#44)` workflow runs
those and the general repository validator. Negative fixtures cover an
unintentionally reenabled dust recipe, old alternative Talisman, added
Talisman input, wrong Netherite dust, missing Water Sigil, lost MK1 Stone
gate and absent/zero Covalence Dust EMC values.

**This source-only validator cannot confirm loaded effective recipes,
native crafting remainders, JEI visibility, Transmutation Table
knowledge/capability sync, or whether a KubeJS script adds a second
production route.**

## Correct integration and testing sequence

1. **Source complete and CI GREEN:** validate the recipe overrides and
   virtual mapping edges, verify that ProjectE recognizes the native
   conversion schema, and run repository quick/preflight checks. This
   is the review-for-integration stage, **not a Minecraft test**.
2. **Integration writer merges/reconciles source:** add the frozen PR
   source to `test/full-integration` and update the current Packwiz
   index/root hashes and source provenance without overwriting
   simultaneous changes. The original MK1 Stone recipes already in
   baseline must remain intact. The virtual EMC conversions belong in
   the **same integration** as the disabled dust crafts.
3. **Gameplay tests happen *after integration*:** from the assembled
   test build, export actual registry and post-mapping ProjectE EMC
   values and run `python3 scripts/validate_projecte_early_recipes.py
   --emc-map <real_emc_export.json>`. Confirm dust values are all
   positive, no physical dust recipe exists, and native transmutation
   consumes EMC. Also exercise rods, Talisman, amulets, Stone crafting
   remainders, JEI and multiplayer.
4. **Fix-and-retest loop:** if a conversion parses poorly, a dust maps
   to zero, or a secondary crafting recipe survives, correct that in
   the corresponding source branch, reintegrate and retest. Runtime
   failures are **not a reason to prohibit initial integration**.

### Parallel outstanding code: first-Stone knowledge grant

The owner's separate #44 requirement still calls for native ProjectE
personal knowledge learning of all three dusts upon first legitimate
Stone acquisition (crafted, traded or received), with native sync,
persistence and no free EMC/items. It is **not** implemented in this
recipe/EMC PR and must be tracked as an independent code-ready slice
with the designated personal milestone/event writer. It should ideally
be included in the same test build before declaring the full #44
experience feature-complete. This is a **source implementation
dependency**, **not a demand to playtest before integrating**.

No full Integration Assembly, Minecraft boot or Prism package was
performed by this PR. Gameplay QA remains Type B/C, requiring an
integrated build and normal authorization. The authoritative QA
acceptance occurs only after that build exists.

## Final technical disposition

- **Source-coded / integration-review ready**: the 10 exact recipe overrides and false-condition
  removals, three native virtual EMC mapping conversions, source validator and negative fixture tests.
- **Source-dependent, tracked separately**: first-Stone native knowledge grant in the personal milestone service. **Post-integration QA**: actual computed dust EMC values and native knowledge behavior. No invented fixed dust prices or new owner balance vote.
- **Not tested in game**: all user-visible behavior and alternate
  recipe acquisition across the loaded modpack.
