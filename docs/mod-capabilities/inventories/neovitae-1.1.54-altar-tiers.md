# Neo Vitae 1.1.54 — six exact Ara Vitae altar-tier geometries

**Source:** [upstream MC 1.21.1 branch @ `501a8d6a0431ed8aeb19e18be2e83f1856f29a21`](https://github.com/breakinblocks/NeoVitae/tree/501a8d6a0431ed8aeb19e18be2e83f1856f29a21); declared `mod_version=1.1.54`, matching the current TMM pinned artifact version, **not a binary hash or assembled-game proof**. All six upstream `src/generated/resources/data/neovitae/neovitae/altar_tier/*.json` definitions were parsed, rather than inferred from guide images.

## How to read the structures

All `pos` are relative to **Ara Vitae at (0,0,0)**, so negative y is below it. `valid` identifies an actual block or tag, not a requirement for a particular named rune. `upgrade:true` identifies altar-rune **stat contribution positions**; `NVMultiblock.createMatcher` still matches the whole `#neovitae:altar/runes` tag regardless of this flag. Nonblank upgrade runes are presented in the in-book preview, but the tag matcher by itself does **not** prove nonblank is required. `optional:true` allows air, fluid/non-occluding blocks, or a matching designated pillar, **not arbitrary solids**. Highest complete tier is selected by `AltarUtil.getTier` across registered validators. See [AltarComponent](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/main/java/com/breakinblocks/neovitae/common/registry/AltarComponent.java), [NVMultiblock](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/main/java/com/breakinblocks/neovitae/common/structure/NVMultiblock.java), [AltarUtil](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/main/java/com/breakinblocks/neovitae/util/AltarUtil.java). Resource/generation/Orb thresholds are in the existing [Neo Vitae resource ledger](neovitae-resource-and-ritual-stats.md) and [quest map](../mods/neovitae-quest-map.md); geometry alone never guarantees a craft.

| Native tier | Datapack key | Total listed components | Rune upgrade slots | Optional pillar slots | Vertical layers | Selectors | Source |
|---:|---|---:|---:|---:|---|---|---|
| 0 | `weak` | 1 | 0 | 0 | 0 | `neovitae:ara_vitae` ×1 | [JSON](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/weak.json) |
| 1 | `apprentice` | 9 | 4 | 0 | -1,0 | `neovitae:ara_vitae` ×1; `#neovitae:altar/runes` ×8 | [JSON](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/apprentice.json) |
| 2 | `mage` | 37 | 24 | 8 | -2,-1,0,1 | `neovitae:ara_vitae` ×1; `#neovitae:altar/runes` ×24; `#neovitae:altar/pillars` ×8; `#neovitae:altar/t3_capstones` ×4 | [JSON](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/mage.json) |
| 3 | `master` | 69 | 52 | 8 | -3,-2,-1,0,1,2 | `neovitae:ara_vitae` ×1; `#neovitae:altar/runes` ×52; `#neovitae:altar/pillars` ×8; `#neovitae:altar/t3_capstones` ×4; `#neovitae:altar/t4_capstones` ×4 | [JSON](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/master.json) |
| 4 | `archmage` | 121 | 100 | 8 | -4,-3,-2,-1,0,1,2 | `neovitae:ara_vitae` ×1; `#neovitae:altar/runes` ×100; `#neovitae:altar/pillars` ×8; `#neovitae:altar/t3_capstones` ×4; `#neovitae:altar/t4_capstones` ×4; `#neovitae:altar/t5_capstones` ×4 | [JSON](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/archmage.json) |
| 5 | `transcendent` | 225 | 172 | 36 | -5,-4,-3,-2,-1,0,1,2,3 | `neovitae:ara_vitae` ×1; `#neovitae:altar/runes` ×172; `#neovitae:altar/pillars` ×36; `#neovitae:altar/t3_capstones` ×4; `#neovitae:altar/t4_capstones` ×4; `#neovitae:altar/t5_capstones` ×4; `#neovitae:altar/t6_capstones` ×4 | [JSON](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/transcendent.json) |

The table counts **cumulative** components at each tier. An upgrade slot counts as one block at a specific position, not an extra block on top of that location. The following exhaustive coordinate lists preserve mandatory/optional and rune-stat flags.

## Tier 0 — weak

**New positional cells compared with previous tier:** 1 (0 optional); this is only positional delta, not a cost or named-material requirement. [Exact datapack](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/weak.json).

**Layer y=0:**

- `neovitae:ara_vitae [REQUIRED]`: (0,0,0)

## Tier 1 — apprentice

**New positional cells compared with previous tier:** 8 (0 optional); this is only positional delta, not a cost or named-material requirement. [Exact datapack](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/apprentice.json).

**Layer y=-1:**

- `#neovitae:altar/runes [STAT RUNE]`: (1,-1,0) (0,-1,1) (-1,-1,0) (0,-1,-1)
- `#neovitae:altar/runes [REQUIRED]`: (1,-1,1) (1,-1,-1) (-1,-1,1) (-1,-1,-1)

**Layer y=0:**

- `neovitae:ara_vitae [REQUIRED]`: (0,0,0)

## Tier 2 — mage

**New positional cells compared with previous tier:** 28 (8 optional); this is only positional delta, not a cost or named-material requirement. [Exact datapack](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/mage.json).

**Layer y=-2:**

- `#neovitae:altar/runes [STAT RUNE]`: (-3,-2,-1) (-3,-2,0) (-3,-2,1) (-2,-2,-2) (-2,-2,2) (-1,-2,-3) (-1,-2,3) (0,-2,-3) (0,-2,3) (1,-2,-3) (1,-2,3) (2,-2,-2) (2,-2,2) (3,-2,-1) (3,-2,0) (3,-2,1)

**Layer y=-1:**

- `#neovitae:altar/runes [STAT RUNE]`: (-1,-1,-1) (-1,-1,0) (-1,-1,1) (0,-1,-1) (0,-1,1) (1,-1,-1) (1,-1,0) (1,-1,1)
- `#neovitae:altar/pillars [OPTIONAL]`: (4,-1,0) (-4,-1,0) (0,-1,4) (0,-1,-4)

**Layer y=0:**

- `neovitae:ara_vitae [REQUIRED]`: (0,0,0)
- `#neovitae:altar/pillars [OPTIONAL]`: (4,0,0) (-4,0,0) (0,0,4) (0,0,-4)

**Layer y=1:**

- `#neovitae:altar/t3_capstones [REQUIRED]`: (4,1,0) (-4,1,0) (0,1,4) (0,1,-4)

## Tier 3 — master

**New positional cells compared with previous tier:** 32 (0 optional); this is only positional delta, not a cost or named-material requirement. [Exact datapack](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/master.json).

**Layer y=-3:**

- `#neovitae:altar/runes [STAT RUNE]`: (-5,-3,-2) (-5,-3,-1) (-5,-3,0) (-5,-3,1) (-5,-3,2) (-4,-3,-3) (-4,-3,3) (-3,-3,-4) (-3,-3,4) (-2,-3,-5) (-2,-3,5) (-1,-3,-5) (-1,-3,5) (0,-3,-5) (0,-3,5) (1,-3,-5) (1,-3,5) (2,-3,-5) (2,-3,5) (3,-3,-4) (3,-3,4) (4,-3,-3) (4,-3,3) (5,-3,-2) (5,-3,-1) (5,-3,0) (5,-3,1) (5,-3,2)

**Layer y=-2:**

- `#neovitae:altar/runes [STAT RUNE]`: (-3,-2,-1) (-3,-2,0) (-3,-2,1) (-2,-2,-2) (-2,-2,2) (-1,-2,-3) (-1,-2,3) (0,-2,-3) (0,-2,3) (1,-2,-3) (1,-2,3) (2,-2,-2) (2,-2,2) (3,-2,-1) (3,-2,0) (3,-2,1)

**Layer y=-1:**

- `#neovitae:altar/runes [STAT RUNE]`: (-1,-1,-1) (-1,-1,0) (-1,-1,1) (0,-1,-1) (0,-1,1) (1,-1,-1) (1,-1,0) (1,-1,1)
- `#neovitae:altar/pillars [OPTIONAL]`: (4,-1,0) (-4,-1,0) (0,-1,4) (0,-1,-4)

**Layer y=0:**

- `neovitae:ara_vitae [REQUIRED]`: (0,0,0)
- `#neovitae:altar/pillars [OPTIONAL]`: (4,0,0) (-4,0,0) (0,0,4) (0,0,-4)

**Layer y=1:**

- `#neovitae:altar/t3_capstones [REQUIRED]`: (4,1,0) (-4,1,0) (0,1,4) (0,1,-4)

**Layer y=2:**

- `#neovitae:altar/t4_capstones [REQUIRED]`: (6,2,6) (-6,2,6) (6,2,-6) (-6,2,-6)

## Tier 4 — archmage

**New positional cells compared with previous tier:** 52 (0 optional); this is only positional delta, not a cost or named-material requirement. [Exact datapack](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/archmage.json).

**Layer y=-4:**

- `#neovitae:altar/runes [STAT RUNE]`: (-8,-4,-2) (-8,-4,-1) (-8,-4,0) (-8,-4,1) (-8,-4,2) (-7,-4,-4) (-7,-4,-3) (-7,-4,3) (-7,-4,4) (-6,-4,-6) (-6,-4,-5) (-6,-4,5) (-6,-4,6) (-5,-4,-6) (-5,-4,6) (-4,-4,-7) (-4,-4,7) (-3,-4,-7) (-3,-4,7) (-2,-4,-8) (-2,-4,8) (-1,-4,-8) (-1,-4,8) (0,-4,-8) (0,-4,8) (1,-4,-8) (1,-4,8) (2,-4,-8) (2,-4,8) (3,-4,-7) (3,-4,7) (4,-4,-7) (4,-4,7) (5,-4,-6) (5,-4,6) (6,-4,-6) (6,-4,-5) (6,-4,5) (6,-4,6) (7,-4,-4) (7,-4,-3) (7,-4,3) (7,-4,4) (8,-4,-2) (8,-4,-1) (8,-4,0) (8,-4,1) (8,-4,2)
- `#neovitae:altar/t5_capstones [REQUIRED]`: (9,-4,0) (-9,-4,0) (0,-4,9) (0,-4,-9)

**Layer y=-3:**

- `#neovitae:altar/runes [STAT RUNE]`: (-5,-3,-2) (-5,-3,-1) (-5,-3,0) (-5,-3,1) (-5,-3,2) (-4,-3,-3) (-4,-3,3) (-3,-3,-4) (-3,-3,4) (-2,-3,-5) (-2,-3,5) (-1,-3,-5) (-1,-3,5) (0,-3,-5) (0,-3,5) (1,-3,-5) (1,-3,5) (2,-3,-5) (2,-3,5) (3,-3,-4) (3,-3,4) (4,-3,-3) (4,-3,3) (5,-3,-2) (5,-3,-1) (5,-3,0) (5,-3,1) (5,-3,2)

**Layer y=-2:**

- `#neovitae:altar/runes [STAT RUNE]`: (-3,-2,-1) (-3,-2,0) (-3,-2,1) (-2,-2,-2) (-2,-2,2) (-1,-2,-3) (-1,-2,3) (0,-2,-3) (0,-2,3) (1,-2,-3) (1,-2,3) (2,-2,-2) (2,-2,2) (3,-2,-1) (3,-2,0) (3,-2,1)

**Layer y=-1:**

- `#neovitae:altar/runes [STAT RUNE]`: (-1,-1,-1) (-1,-1,0) (-1,-1,1) (0,-1,-1) (0,-1,1) (1,-1,-1) (1,-1,0) (1,-1,1)
- `#neovitae:altar/pillars [OPTIONAL]`: (4,-1,0) (-4,-1,0) (0,-1,4) (0,-1,-4)

**Layer y=0:**

- `neovitae:ara_vitae [REQUIRED]`: (0,0,0)
- `#neovitae:altar/pillars [OPTIONAL]`: (4,0,0) (-4,0,0) (0,0,4) (0,0,-4)

**Layer y=1:**

- `#neovitae:altar/t3_capstones [REQUIRED]`: (4,1,0) (-4,1,0) (0,1,4) (0,1,-4)

**Layer y=2:**

- `#neovitae:altar/t4_capstones [REQUIRED]`: (6,2,6) (-6,2,6) (6,2,-6) (-6,2,-6)

## Tier 5 — transcendent

**New positional cells compared with previous tier:** 104 (28 optional); this is only positional delta, not a cost or named-material requirement. [Exact datapack](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/generated/resources/data/neovitae/neovitae/altar_tier/transcendent.json).

**Layer y=-5:**

- `#neovitae:altar/runes [STAT RUNE]`: (-11,-5,-3) (-11,-5,-2) (-11,-5,-1) (-11,-5,0) (-11,-5,1) (-11,-5,2) (-11,-5,3) (-10,-5,-5) (-10,-5,-4) (-10,-5,4) (-10,-5,5) (-9,-5,-7) (-9,-5,-6) (-9,-5,6) (-9,-5,7) (-8,-5,-8) (-8,-5,-7) (-8,-5,7) (-8,-5,8) (-7,-5,-9) (-7,-5,-8) (-7,-5,8) (-7,-5,9) (-6,-5,-9) (-6,-5,9) (-5,-5,-10) (-5,-5,10) (-4,-5,-10) (-4,-5,10) (-3,-5,-11) (-3,-5,11) (-2,-5,-11) (-2,-5,11) (-1,-5,-11) (-1,-5,11) (0,-5,-11) (0,-5,11) (1,-5,-11) (1,-5,11) (2,-5,-11) (2,-5,11) (3,-5,-11) (3,-5,11) (4,-5,-10) (4,-5,10) (5,-5,-10) (5,-5,10) (6,-5,-9) (6,-5,9) (7,-5,-9) (7,-5,-8) (7,-5,8) (7,-5,9) (8,-5,-8) (8,-5,-7) (8,-5,7) (8,-5,8) (9,-5,-7) (9,-5,-6) (9,-5,6) (9,-5,7) (10,-5,-5) (10,-5,-4) (10,-5,4) (10,-5,5) (11,-5,-3) (11,-5,-2) (11,-5,-1) (11,-5,0) (11,-5,1) (11,-5,2) (11,-5,3)

**Layer y=-4:**

- `#neovitae:altar/runes [STAT RUNE]`: (-8,-4,-2) (-8,-4,-1) (-8,-4,0) (-8,-4,1) (-8,-4,2) (-7,-4,-4) (-7,-4,-3) (-7,-4,3) (-7,-4,4) (-6,-4,-6) (-6,-4,-5) (-6,-4,5) (-6,-4,6) (-5,-4,-6) (-5,-4,6) (-4,-4,-7) (-4,-4,7) (-3,-4,-7) (-3,-4,7) (-2,-4,-8) (-2,-4,8) (-1,-4,-8) (-1,-4,8) (0,-4,-8) (0,-4,8) (1,-4,-8) (1,-4,8) (2,-4,-8) (2,-4,8) (3,-4,-7) (3,-4,7) (4,-4,-7) (4,-4,7) (5,-4,-6) (5,-4,6) (6,-4,-6) (6,-4,-5) (6,-4,5) (6,-4,6) (7,-4,-4) (7,-4,-3) (7,-4,3) (7,-4,4) (8,-4,-2) (8,-4,-1) (8,-4,0) (8,-4,1) (8,-4,2)
- `#neovitae:altar/t5_capstones [REQUIRED]`: (9,-4,0) (-9,-4,0) (0,-4,9) (0,-4,-9)
- `#neovitae:altar/pillars [OPTIONAL]`: (12,-4,0) (-12,-4,0) (0,-4,12) (0,-4,-12)

**Layer y=-3:**

- `#neovitae:altar/runes [STAT RUNE]`: (-5,-3,-2) (-5,-3,-1) (-5,-3,0) (-5,-3,1) (-5,-3,2) (-4,-3,-3) (-4,-3,3) (-3,-3,-4) (-3,-3,4) (-2,-3,-5) (-2,-3,5) (-1,-3,-5) (-1,-3,5) (0,-3,-5) (0,-3,5) (1,-3,-5) (1,-3,5) (2,-3,-5) (2,-3,5) (3,-3,-4) (3,-3,4) (4,-3,-3) (4,-3,3) (5,-3,-2) (5,-3,-1) (5,-3,0) (5,-3,1) (5,-3,2)
- `#neovitae:altar/pillars [OPTIONAL]`: (12,-3,0) (-12,-3,0) (0,-3,12) (0,-3,-12)

**Layer y=-2:**

- `#neovitae:altar/runes [STAT RUNE]`: (-3,-2,-1) (-3,-2,0) (-3,-2,1) (-2,-2,-2) (-2,-2,2) (-1,-2,-3) (-1,-2,3) (0,-2,-3) (0,-2,3) (1,-2,-3) (1,-2,3) (2,-2,-2) (2,-2,2) (3,-2,-1) (3,-2,0) (3,-2,1)
- `#neovitae:altar/pillars [OPTIONAL]`: (12,-2,0) (-12,-2,0) (0,-2,12) (0,-2,-12)

**Layer y=-1:**

- `#neovitae:altar/runes [STAT RUNE]`: (-1,-1,-1) (-1,-1,0) (-1,-1,1) (0,-1,-1) (0,-1,1) (1,-1,-1) (1,-1,0) (1,-1,1)
- `#neovitae:altar/pillars [OPTIONAL]`: (4,-1,0) (-4,-1,0) (0,-1,4) (0,-1,-4) (12,-1,0) (-12,-1,0) (0,-1,12) (0,-1,-12)

**Layer y=0:**

- `neovitae:ara_vitae [REQUIRED]`: (0,0,0)
- `#neovitae:altar/pillars [OPTIONAL]`: (4,0,0) (-4,0,0) (0,0,4) (0,0,-4) (12,0,0) (-12,0,0) (0,0,12) (0,0,-12)

**Layer y=1:**

- `#neovitae:altar/t3_capstones [REQUIRED]`: (4,1,0) (-4,1,0) (0,1,4) (0,1,-4)
- `#neovitae:altar/pillars [OPTIONAL]`: (12,1,0) (-12,1,0) (0,1,12) (0,1,-12)

**Layer y=2:**

- `#neovitae:altar/t4_capstones [REQUIRED]`: (6,2,6) (-6,2,6) (6,2,-6) (-6,2,-6)
- `#neovitae:altar/pillars [OPTIONAL]`: (12,2,0) (-12,2,0) (0,2,12) (0,2,-12)

**Layer y=3:**

- `#neovitae:altar/t6_capstones [REQUIRED]`: (12,3,0) (-12,3,0) (0,3,12) (0,3,-12)

## Validation caveats

- `AltarUtil.getTier` selects the **highest geometrically validated** tier, while altar processing depends separately on activation, fluid, orb binding, and individual recipe tier constraints.
- `upgrade` and `optional` are behavioral flags, not direct ingredient counts or substitute materials. Exact tag members may be changed in TMM; verify effective loaded tags rather than assuming generic vanilla stone is always acceptable.
- Source family 1.1.53→1.1.54 comparison contains **no altar-tier JSON edits**, so these layouts carry forward from the prior research; the 1.1.54-declared head used here resolves the earlier source-version mismatch for this subsystem.
- No effective saved-world pack override, JAR byte parity, or live client operation was tested in this research-only document.
