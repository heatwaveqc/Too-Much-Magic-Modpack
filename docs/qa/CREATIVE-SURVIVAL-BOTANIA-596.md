# Creative-only survival restriction — Botania Revived pilot (#596)

**Approved policy:** ProjectE is the endgame resource capstone; no separately
survival-obtainable creative Mana Pool, creative Mana Tablet component state or
creative Corporea Spark. **This is only the first known recipe-source slice of
a pack-wide policy, not a completed creative-item prohibition.**

## Exactly verified source (Botania Revived 1.21.1 2.0.2)

TMM pin: `Botania-Revived-1.21.1-2.0.2-NeoForge.jar`, SHA-1
`b333e5ac4fd55b0e8822238ced0b25b193db9628`, frozen upstream
[commit 5f803227](https://github.com/Velron-Devs/Botania-Revived/tree/5f803227d484598da1f1093a91d5d7cf5cf9600d).

Six *real* native recipe IDs under `data/botania/recipe/creative/` produce
the three prohibited outputs: each has a 1.21.1 `terra_plate/` route plus
an `avaritia:shaped_table` extreme route. The latter is normally inactive
because Avaritia is excluded from TMM, but is fail-closed as future safety.

All six native recipe IDs now receive one-to-one KubeJS datapack overrides
with `neoforge:conditions: [neoforge:false]`, the known working TMM pattern
for disabling first-party recipe acquisition (#44). A secondary required
creative-only `minecraft:barrier` ingredient makes the recipe impossible
in ordinary survival even if the false condition is ignored.

The native creative crafting *module remains enabled* (no config flip).
The `infrangible_platform` recipe and legitimate ordinary Mana Tablet are
deliberately **untouched**; infrangible platform is not yet owner-classified
as forbidden creative-tier content. No changes to normal Terra Plate mechanics,
Mana production, or unrelated Botania modules. Creative inventory/test grants
remain possible.

## Integration and further work

**Source-only candidate; NOT yet indexed or installed.** Designated integration
writer adds these six data files to Packwiz, recalculates `index.toml` and
`pack.toml` hashes and reconciles live integration source. Run
`python -m unittest tests/test_botania_creative_bans.py`
and `python scripts/verify_botania_creative_bans.py` before that selection.

**Still open under #596:** audit creative outputs from all installed mods,
all surviving alternate recipes/machines/loot/shops/advancements/EMC mappings,
and component-sensitive transmutation learning (creative Tablet is an
`ItemStack` component variant, not a distinct registered item). The current
Nov 18 census is historical and must not prove future full coverage. A
future integrated B/C build must verify the six recipe IDs absent in EMI/JEI,
actual Terra Plate refuses them, creative inventory still usable, and no
unexpected side-effects. No full build/server/Prism/client QA authorized.
