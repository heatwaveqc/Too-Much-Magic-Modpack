# Issue #44 — ProjectE early utility / Covalence Dust recipe slice

**Status:** source implementation staged for review, **NOT** integrated or accepted in-game.
Approved decisions: #44 owner lock of 2026-11-21. This source slice is
**not** the Progression engine and does not modify MXP, player Paths or
ProjectE tooltip milestone implementation.

## Source changes

| Native ProjectE recipe ID | Source-only outcome |
| --- | --- |
| `low_covalence_dust`, `medium_covalence_dust`, `high_covalence_dust` | Native 40-output crafting recipes disabled via same-ID `neoforge:false`; dust items stay registered and usable through transmutation |
| `repair_talisman` | Exactly one Eternal Stella + one Occultism Iesnium Anvil, shapeless, one Talisman |
| `repair_talisman_alt` | Original covalence/paper/string variation disabled via same-ID `neoforge:false` |
| `divining_rod_1` | 8 Neo Vitae Iron Dust + center vanilla Stick |
| `divining_rod_2` | 8 Neo Vitae Diamond Dust + center Divining Rod I |
| `divining_rod_3` | 8 Occultism Netherite Dust + center Divining Rod II; NOT Neo Vitae Netherite Scrap Dust |
| `evertide_amulet` | Retain three Dark Matter, five Water Buckets, replace top-center Water Bucket with Neo Vitae Water Sigil |
| `volcanite_amulet` | Retain three Dark Matter, five Lava Buckets, replace top-center Lava Bucket with Neo Vitae Lava Sigil |

All JSON paths are `kubejs/data/projecte/recipe/<id>.json`, matching
the pinned ProjectE `mc1.21.1` generated native recipe IDs. The shapes,
outputs and original ingredients were checked against the matching official
generated files. The Talisman and sigil inputs were checked against
Forbidden & Arcanus `ModItems`, Occultism Iesnium Anvil generated assets
and Neo Vitae `NVItems`. Iron and Diamond dust stages were checked against
the **actual TMM `config/neovitae/materials.json`**.

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

## Integration dependencies: DO NOT promote the dust disable alone

1. **Positive EMC for all three dusts.** Removing native crafts removes
   their ordinary recipe-derived mapping path. Before promoting, obtain
   a frozen-build item registry **and post-mapping EMC export** and run:
   `python3 scripts/validate_projecte_early_recipes.py --emc-map <real_emc_export.json>`.
   If any dust is zero/absent, source-review approved positive manual
   anchors or native equivalent mappings through #47/#322. Do **not**
   insert speculative historical 1/8/208 constants as if owner-approved.
2. **First-Stone native knowledge grant.** On each player's *first
   legitimate* acquisition of Philosopher's Stone (crafted, trade,
   received), add all three dust IDs to their native ProjectE
   transmutation knowledge and synchronize. This needs to integrate
   with the existing personal acquisition/milestone event service
   owned by the designated engine writer, **not** a duplicate
   PlayerTick inventory-scan service in this recipe-only PR. No
   free EMC/items; persist learning on relog; no pre-stable backfill
   is required. Keep the implementation/QA checkbox open on #44.
3. **Packwiz and shared source ownership.** Only designated
   `test/full-integration` integration writer reconciles
   `kubejs/data` source paths, indexed file hashes, `index.toml`,
   `pack.toml` and frozen source references against current HEAD.
   Do not cherry-pick old PR #499's whole tree: its valid MK1
   source is already in the baseline.
4. **Legacy overlap.** No change to Philosopher's Stone twelve-category
   Occultism ritual, Project Expansion Chaos Shard flower gates,
   Evertide/Volcanite native abilities, Condenser or Alchemical Chest.
   They must retain their intended ingredient dependencies.
5. **QA authorization.** No full Integration Assembly, game boot or
   Prism package in this source PR. Type C after separately authorized
   build: validate absent dust crafts; positive learned dust
   transmutation/relog; rods/repair/amulet crafting including
   containers/remainders and native ability preservation; MK1 gates,
   JEI displays, multiplayer sharing and alt recipe bypasses.

## Final technical disposition

- **Code-ready / staged**: the 10 exact recipe overrides and false-condition
  removals, source validator and negative fixture tests.
- **Technical dependencies / not source-complete**: post-mapping dust
  anchors and first-Stone native knowledge grant. No new owner
  balancing vote is being requested by this slice.
- **Not tested in game**: all user-visible behavior and alternate
  recipe acquisition across the loaded modpack.
