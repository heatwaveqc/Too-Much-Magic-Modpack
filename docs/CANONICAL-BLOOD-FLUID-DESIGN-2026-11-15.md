# Canonical Physical Blood — EvilCraft Fluid Unification Target (2026-11-15)

> **Owner-approved design target; partially source-implemented 2026-11-16.** [PR #282](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/282) selectively integrates 17 KubeJS datapack JSONs, the Iron's vial startup script, and strict JAWR bottle component handling into the current source baseline. Player-facing EvilCraft fluid/recipe boundaries are present in source, **but full universal external-fluid normalization remains unimplemented**, notably the Neo Vitae native drain/pipes and strict-ID consumers. No native in-world conversions or client/server acceptances have been demonstrated. Do not rewrite old saves or suppress unique items. See [exact implementation QA](qa/CANONICAL-BLOOD-JAWR-RECONCILIATION-2026-11-16.md).

## Decision

Use **`evilcraft:blood`** as the **canonical physical, ordinary blood fluid** emitted, handled, bucketed, transported, and world-placed in Too Much Magic. EvilCraft's *source blood block* is necessary for the existing random-tick drying-to-Hardened-Blood progression. Preserve existing independent internal resources (Neo Vitae Anima/Spiritus and other non-fluid meters; JAWR vampire blood bank) and special bottle metadata.

The intended player-visible external flow is:

```
Any supported ordinary blood production -> evilcraft:blood
    -> EvilCraft bucket/world block/tanks/pipes
    -> Neo Vitae Ara Vitae (native internal EV accounting)
    -> extract via ordinary external fluid capability as evilcraft:blood
    -> place in world as EvilCraft blood -> naturally dry into Hardened Blood
```

**Not** in scope: replacing all internally stored meters, making vampire power free, replacing special/vampiric blood in JAWR, altering EV-to-Anima or Spiritus mathematics, changing native progression costs, or globally deleting mod registry objects.

## Source-confirmed technical boundaries

| Mod/system | Verified fact | Design consequence |
| --- | --- | --- |
| EvilCraft 1.2.100 (1.21.1) | `evilcraft:blood` is in `c:blood`. `BlockFluidBlood` extends `LiquidBlock`; full source blocks randomly dry into Hardened Blood if conditions permit, with rain and nearby water relevant. | Choose EvilCraft fluid as source of truth, not a cosmetic clone; preserve placed source-block behavior. |
| Neo Vitae 1.1.53, Ara Vitae | `AraVitaeTile` is an `IFluidHandler`. `isFluidValid` accepts `NVTags.Fluids.ESSENTIA_VITAE`. However `getFluidInTank` and `drain` explicitly construct `NVFluids.ESSENTIA_VITAE_SOURCE`. Internal tank amounts are integers. | A datapack fluid-tag extension permits *input*; **it does not canonicalize output**. Implement a capability-boundary adapter or equivalent verified patch for extracted fluid. |
| Neo Vitae 1.1.53, Athanor | At least one internal consumer compares input fluid identity directly against `NVFluids.ESSENTIA_VITAE_SOURCE` (rather than a tag). | Externally canonical blood must be translated **at the exact Neo Vitae consumer boundary** or the consumer's predicate must be patched; never assume altar tag coverage equals full Neo Vitae coverage. |
| Neo Vitae 1.1.53, blood tanks | Fluid tank block entity holds `FluidStack` rather than only a primitive EV count. | Test native blood tanks and all routed Neo Vitae fluid paths for physical-fluid normalization and persistence. |
| Iron's Spells 'n Spellbooks (1.21 branch source) | Alchemist Cauldron `fill_blood_vial` / `empty_blood_vial` recipes name `irons_spellbooks:blood` explicitly, 250 mB per vial. | Override the exact pinned version's cauldron recipes or provide a handler adapter; treat blood vials as containers/items rather than generic liquid source blocks. |
| JAWR 0.5.39 | Blood is also represented by items and vampire state. Its native Neo Vitae integration exists; not every bottle/item is necessarily ordinary blood. | Add compatible ordinary-blood bottle recipe/handling without replacing blood bank or erasing special vampire-related data. Verify actual item registry and metadata from pinned JAR. |
| Hexerei 0.5.0.3 | Blood-fluid handling and registry ID still require exact pinned-binary inspection for this integration. | Do not assume earlier-version registry IDs, producer recipes or bucket behavior. |

**Key distinction:** A shared fluid tag is recipe/capability matching where supported, not a registry alias. A named `FluidStack`, registered bucket item, placed fluid block, hand-coded entity effect, internal tank or saved NBT retains its own ID until a supported conversion point changes it. Hiding entries in JEI does not remove those fluid objects or change world mechanics.

## Approved compatibility behavior

1. **Canonical output.** Every supported *ordinary physical blood* production path must create `evilcraft:blood` (or a deliberately isolated mod-internal quantity never exposed externally). Inventory, container, recipe, entity, machine, world-block and fluid-handler outputs are audited **per producer**. There is no presumed universal KubeJS hook for all fluid creation.
2. **Canonical world placement and pickup.** Player-obtainable buckets of ordinary blood should be EvilCraft buckets, and placed ordinary blood sources should be EvilCraft fluid blocks. Fluid pickup, dispensing, filling and pipes must honor amount conservation. Preserve EvilCraft drying without new block-tick replacement loops.
3. **Neo Vitae altar.** Allow canonical input by adding `evilcraft:blood` to `neovitae:essentia_vitae` (test exact source/flowing names and avoid unintentionally accepting exceptional fluids). Keep altar internal amounts and recipe semantics intact. For external drain, normalize to `evilcraft:blood`, including `getFluidInTank` reporting and `drain(FluidStack)` contract consistency, not just `drain(int)`.
4. **Other Neo Vitae consumers.** Patch/adapt strict identity checks (Athanor and any others found); make sure Neo Vitae-to-Neo Vitae routing remains intact and does not repeatedly convert, double-credit, or reject canonical exported blood.
5. **Other mods.** Target Iron's, Hexerei and EvilCraft ordinary recipes/machines with recipe replacements, tags when supported, and minimal first-party adapters where needed. Avoid wholesale source-mod forks.
6. **Bottled blood.** Make regular JAWR bottled blood and Iron's Blood Vials substitute in *ordinary* recipe uses, preserving exact contents / volume (250 mB is source-backed for Iron's but JAWR bottle volume still requires verification). JAWR empowered/vampire-transformation or owner-sensitive bottles do not silently become mundane blood and must retain relevant data.
7. **Visibility.** Hide redundant blood buckets/fluids from JEI/REI/EMI and normal acquisition once output and recipe verification passes; avoid hiding unique mod progression items or stripping creative/debug access prematurely. Disabled normal recipes are preferable to deleting registered objects.
8. **Persistence and migration.** New worlds should use canonical ordinary blood; old worlds with saved alternate `FluidStack` IDs or source blocks need a separate migration/test plan. Never mass-transform arbitrary bloodlike NBT or active internal vampire state.
9. **Resource safety.** Default ordinary-fluid conversion is 1:1 only where source units match the NeoForge millibucket convention; no creative generation, duplicate fills, loss on simulation vs execute, truncation leaks, or conversion loops. Review progression exploits against ProjectE, ritual automations and all blood farms.

## Suggested implementation split (not yet authorized as runtime work)

- **Datapack/KubeJS:** explicit fluid tag memberships, exact-version recipe input/output overrides, tagged ordinary bottle substitutions, JEI visibility, and straightforward producer recipe remapping.
- **Small first-party NeoForge compatibility module:** wrap selected fluid capabilities (starting with Ara Vitae) using `RegisterCapabilitiesEvent` and a correctly ordered provider, or patch the selected consumers if capability wrapping cannot cover the operation. NeoForge 1.21.1 allows higher-priority providers. Audit internal calls that bypass fluid capabilities.
- **Surgical per-producer hooks:** where a mod injects a fluid into world blocks, items, machines or entities directly and no recipe/datapack output exists, intercept *that concrete operation*, not every arbitrary fluid update globally. Decide whether source modification/mixins are truly necessary after binary audit.

## Acceptance tests before enabling canonicalization

1. In survival, EvilCraft blood produced by its native collectors remains ordinary, bucketable `evilcraft:blood`.
2. A placed full source exposed to drying conditions becomes Hardened Blood; rain or nearby water exercises the native blocking behavior (verify against pinned world rules).
3. Both Neo Vitae Ara Vitae input and output work via a vanilla bucket, at least one third-party tank, and a pipe; JEI and in-world fluid IDs agree at external boundaries.
4. `getFluidInTank`, fill, drain-by-amount, drain-by-type, and `SIMULATE`/ `EXECUTE` are consistent and conserve fluid through a full altar round trip.
5. Neo Vitae Athanor and every discovered strict-ID consumer still operate with canonical external fluid while preserving internal EV requirements and progression.
6. Iron's alchemist cauldron accepts/produces canonical blood and regular Blood Vials without losing empty bottles or silently bypassing ingredients.
7. Hexerei blood recipes/cauldron/producing sources are audited against actual 0.5.0.3 IDs; successful recipes output canonical physical blood where appropriate.
8. JAWR common bottle interchange works; enchanted/special vampire bottles cannot be substituted as generic transfusion material if that strips or fabricates unique properties; blood bank unaffected.
9. EvilCraft native machinery accepts canonical blood, including hardcoded fluid comparisons that a tag does not automatically modify.
10. No ordinary recipe/loot/collector produces a visible legacy blood bucket or unsupported alternate physical blood fluid in a clean world; legacy entries hidden only after this passes.
11. Fluid transport between all relevant machines and tanks is stable on chunk unload/reload, save/rejoin, server/client sync, and nonempty partially filled containers.
12. Fuzz amounts (1, 249, 250, 999, 1000, near-capacity), automation throughput, interruptions, and chained loops for rounding, dupes and lost resources.
13. No world/NBT migration applied to old saves without a disposable copy, backup and explicit validation.

## Current confidence and open work

**Strong source support:** EvilCraft random-tick drying; EvilCraft `c:blood` tag; Neo Vitae altar's tag-aware fill and hardcoded native-fluid drain; Neo Vitae Athanor strict fluid identity; NeoForge 1.21.1 higher-priority capability provider API.

**Still unverified:** exact pinned binary registries/recipe serializations for Hexerei, JAWR bottles and Iron's cauldron; 1.21.1 EvilCraft event producer inventory; capability wrapper behavior against actual TMM full integration; old-world migration; JEI hiding/UI. No client test, integration runtime or balancing test completed.

### Source pointers

- EvilCraft source branch for 1.21.1: https://github.com/CyclopsMC/EvilCraft/blob/master-1.21-lts/src/main/java/org/cyclops/evilcraft/block/BlockFluidBlood.java
- EvilCraft common blood tag: https://github.com/CyclopsMC/EvilCraft/blob/master-1.21-lts/src/main/resources/data/c/tags/fluid/blood.json
- Neo Vitae exact-version source: https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/blockentity/AraVitaeTile.java
- Neo Vitae Athanor strict check: https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/blockentity/AthanorBlockEntity.java
- Neo Vitae capabilities registration: https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/blockentity/NVTiles.java
- NeoForge 1.21.1 capabilities: https://github.com/PorkChop-ZLG/NeoForge-Documentation/blob/main/versioned_docs/version-1.21.1/inventories/capabilities.md
