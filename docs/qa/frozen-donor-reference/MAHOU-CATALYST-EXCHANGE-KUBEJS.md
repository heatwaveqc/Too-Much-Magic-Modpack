# Mahou Catalyst Exchange accepts Neo Vitae dust — KubeJS candidate

Status: **IMPLEMENTED EXPERIMENT / needs actual world test**. Not a survival-ready compatibility guarantee.

## What we actually verified

The **pinned** Mahou Tsukai `mahoutsukai-1.21.1-v1.36.31.jar` was downloaded in a disposable GitHub Actions inspection with the SHA-512 pin checked. Java disassembly from [inspection run 43644066967](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43644066967) confirmed:

- `CatalystExchangeMahoujinTileEntity.checkForItemClass()` returns `PowderedCatalyst.class`, rather than a `c:dusts` tag.
- `ifItemFoundAbove(ItemEntity)` checks that registered class; invokes `PlayerManaManager.getManaFromBatteriesFirst()` or `drainMana()`; selects a native random catalyst; calls `Level.addFreshEntity` for a **new one-item output**; calls `ItemStack.shrink(1)` on the **original dropped entity**, discarding it when empty.
- The original event system is driven by Mahou's `checkTicks()`, using `CATALYST_EXCHANGE_BLOCK_CYCLE`. Mana cost is `CATALYST_EXCHANGE_MANA_COST`. No KubeJS mimicry of mana or random selection is necessary.
- The native method's loop compares its incrementing index to the **shrinking source stack count**. A stack can therefore require multiple native cycles before complete conversion. Do not falsely assert whole-stack conversion in a single tick.

## Implementation

`kubejs/server_scripts/mahou_neo_vitae_catalyst_exchange.js` registers NeoForge native **LevelTickEvent.Pre / Post** and **EntityTickEvent.Pre** through KubeJS; the latter is cancellable while a stack is temporarily substituted. `EntityEvents.spawned('minecraft:item')` additionally finds fresh or reloaded eligible item entities.

It tracks only these exact source items, **not generic c:dusts tags**:

| Dropped source | Temporary native catalyst |
| --- | --- |
| `neovitae:iron_dust` | `mahoutsukai:powdered_iron` |
| `neovitae:gold_dust` | `mahoutsukai:powdered_gold` |
| `neovitae:diamond_dust` | `mahoutsukai:powdered_diamond` |
| `neovitae:emerald_dust` | `mahoutsukai:powdered_emerald` |
| `neovitae:quartz_dust` | `mahoutsukai:powdered_quartz` |

Each level tick, **only when an actual `mahoutsukai:mahoujin_catalyst_exchange` block is nearby**, a matching source ItemEntity is temporarily set to its Mahou counterpart. The normal Mahou tile entity scans above it and, if it can charge mana, spawns its original random outputs while shrinking the input entity. KubeJS restores whatever input count remains (including its original components) at **LevelTick.Post**, or leaves a fully consumed entity discarded. If Mahou has no mana, the unchanged count is fully restored as Neo dust. Item entity ticking is suppressed during the temporary window to avoid movement and same-entity merging. Existing native Mahou powders and outputs are untouched.

No extra copper/sulfur/ender/eye mapping is created. Ender/Eye can still appear as native outputs. No KubeJS-generated random catalysts or artificial mana charges exist.

## Manual disposable-world checks (not yet passed)

1. Place a native Catalyst Exchange circle, charge the caster and nearby Mana Circuit. Drop one `neovitae:iron_dust` on the circle. Confirm *one native random powdered catalyst* appears, original Neo item is gone, normal mana cost was charged and no KubeJS error.
2. Drop 16 of each Neo iron, gold, diamond, emerald and quartz dust; allow multiple cycles. Count outputs + remaining source before/after, including a result equal to the temporary source type; **no losses or duplicates**.
3. Repeat with **zero mana** and with only enough mana for a **partial** stack. Ensure all unpaid input remains Neo dust, not a permanent Mahou powder.
4. Drop Neo dust **near but not on** the circle, above an unfinished `mahoutsukai:mahoujin`, and away from the circle: no exchange, no permanent item change.
5. Repeat with native Mahou powders; they must retain original native behavior.
6. Verify a second circle, relog/restart with Neo dust on top, and moving dust via water flow. Check both LevelTick hooks run and no phantom item changes occur.
7. Exercise crowded piles of both Neo and Mahou powders; watch for unexpected merging, pickup or movement while a native stack is briefly substituted.
8. Check client/server entity sync in Prism, including no residual temporary Mahou powder when no mana exists.

Static JS parse, packwiz validation and dedicated-server boot are **necessary but insufficient**. Any entity persistence, duplication, incorrect mana cost, or client desync blocks promotion. If such a problem is demonstrated, disable the script and write a small targeted Java bridge using Mahou's verified `ifItemFoundAbove` instead of inventing a JS mana algorithm.

## Build routing

This source branch is intentionally separate from the Neo Vitae canonical-dust PR: next all-workstreams integration must import **only** this script and run the full server smoke. Do not replace the already-tracked source or alter other concurrent workstreams.
