# Mahou Tsukai — pinned exchange and life-drain economy

Companion to [main profile](mahou-tsukai.md), [recipe crosswalk](mahou-tsukai-pinned-onboarding.md) and [mana/circuit contract](mahou-tsukai-mana-circuits.md). Exact `mahoutsukai-1.21.1-v1.36.31.jar`, SHA-512 `0c4f3bc823d681ad9cb00895d348d2b414201c4d397ca3aa6427b45b8d2b369e0bae8c1f354a9541e4205dadb58c49203d3570e2433d772c3eef1227bc8bfa5a`, matched to [pin](../../../mods/mahou-tsukai.pw.toml); inspected2026-11-16. **Researching; no economy/claims/client tests.** Native defaults are distinct from effective world settings.

## Three different resource paths

| Native circle / catalyst recipe | Actual resource path | Guide reference / role |
| --- | --- | --- |
| Catalyst Exchange:3 Emerald powder | Consume one native PowderedCatalyst and50 mana per successful exchange; spawn one random registered powder. | Compendium Catalyst Exchange; optional conversion of plentiful catalysts into scarce catalyst types. No guaranteed chosen output. |
| Durability Exchange:2 Emerald+1 Diamond powder | Damage eligible items above or in inventory below; restore caster's personal mana up to maximum. | Compendium Durability Exchange; trade tools/equipment lifetime for mana. Not a charged-gem/circuit output. |
| Boundary of Drain Life:2 Iron+1 Emerald powder | Pay periodic boundary cost, attack eligible nearby living entities; reward caster health, then hunger, then personal mana. | Compendium Boundary of Drain Life; target-dependent sustain rather than an item conversion. |

Use the stationary circle for these researched tile contracts. Cloth-required=false in the registrar does not establish identical scroll behavior; the Drain Life scroll is a separate class and is not audited here. Consult native named pages and JEI for powder preparation; see the starter appendix for exact counted-unordered circle creation.

## Catalyst Exchange: randomness, repeated billing and output handling

`CatalystExchangeMahoujinTileEntity` implements `AboveChecker`: every4 level game ticks by default it queries entities intersecting the circle's one-block AABB, after the active-circle gate. It accepts `PowderedCatalyst` instances, not arbitrary forge dust tags or Neo Vitae powders. The registrar's seven-key ingredient map supplies Iron, Diamond, Emerald, Quartz, Ender, Gold and Eye powders. Selection is `nextInt(map size)` over keys; the input type is not excluded, so unchanged-type output is possible. No evidence supports weighted rarities or a guaranteed Diamond from one input.

For each unit it first tries eligible nearby circuits centered on the circle, then the online caster's ordinary personal/gem/circuit drain for50. Once funded it creates one item entity at the circle position, ignores spawn-add's boolean, and shrinks the input by1. Rejected spawning can therefore lose the paid unit without output/refund in this path. Insufficient payment returns from this entity callback; the common scanner then stops processing further entities for that scan.

The loop compares an increasing index against **the shrinking current stack count**. With an isolated original stack of N and sufficient funds, one invocation exchanges `ceil(N/2)` units, not all N (e.g.64→32,3→2,1→1). The exact loop condition, shrink and increment were confirmed with `javap -p -c`. This is a code-derived batch count; merging, newly spawned entities, physics and later scans affect in-world throughput.

Outputs are themselves native catalysts, and are spawned at the circle. If they remain/re-enter the scanner volume, they remain eligible on later scans: a desired output is not marked completed or excluded. Collect promptly and test handling before calling this an automatic farm. There is no net item-count multiplication in a successful one-to-one exchange. Repeated rerolls consume additional mana and can reroll the item back to its original type. The disabled TMM Neo Vitae adapter is not authorized or fixed by this native audit.

## Durability Exchange: two independent input routes

The tile's ordinary20-tick work callback invokes `ActiveCircleConfig.tryToOperate` and then inspects a block entity directly below. Its `AboveChecker` separately runs on game-time multiples of20, requiring a non-null caster but **not calling that active-circle gate in its own preconditions**. Both routes can operate; do not infer a single global10-mana/second ceiling from the config name.

### Inventory below

`Utils.getInventory(level,pos)` requests the NeoForge block item-handler capability with **Direction.NORTH**, not a null/omnidirectional side. It scans slots and directly uses `getStackInSlot`; no extraction of the whole tool is required in this method. Compatibility with handlers returning non-mutable views or sided restrictions needs testing; a generic inventory interface alone is insufficient proof.

Eligible input is a nonempty damageable item below maximum damage, not item/enchantment-blacklisted. Native blacklist is empty. The loop repeatedly computes a tax, calls `hurtAndBreak` through a fake player carrying caster UUID, re-reads the slot, and credits mana only if observed damage value increased. Full personal mana stops work. Failure to increase damage stops work on that item (including a possible ignored damage attempt); do not assume Unbreaking has no effect.

### Dropped item above

The separate callback accepts any Item class, then tests damageability/blacklist. It uses the circle's1×1×1 entity volume and requires the caster. Each matching ItemEntity has its own local restoration counter; `firstEntityOnly=false`, so several eligible entities can each run. There is no thrower-owner test in this item callback. Test other-player tools and pack protections before recommending public deployment.

Both routes call the normal damage/break helper and compare the post-call damage value. Broken/empty-stack behavior can reset the observed damage and end without a credit for the final damage attempt. The dropped route's explicit discard occurs only after its successful-increase check. Do not guarantee one mana for the last durability point or preservation of a broken ItemEntity without testing.

### Tax and native limits

Native efficiency1 means1 attempted durability damage for1 mana credit when damage increases. `computeTax` selects the highest configured remaining-durability bracket reached. Defaults:5000→2%,20000→3%,100000→4% of current remaining durability (capped at all remaining durability). Below the first bracket it uses efficiency1. At5000 remaining, tax100 per credit; at20000, tax600; at100000, tax4000. This intentionally makes very high-durability items costly, rather than a linear mana jackpot.

For a positive computed tax below1, credit becomes integer truncation of `1/tax`, and damage tax is set to1. Otherwise damage uses integer truncation of tax. Configurations producing zero/negative tax need explicit acceptance; do not extrapolate their intended design.

`DURABILITY_EXCHANGE_MANA_GAIN_CAP=10` is checked against the **count of successful credited loop iterations**, not the total mana amount. Default1-credit iterations yield at most10 per below-inventory callback and10 per matching dropped-entity callback, limited by available durability/personal deficit. Sub-unit tax configurations can credit more than10 mana because each iteration can award several. There is no direct maximum-mana growth call in these restoration methods; spending the recovered personal mana is the separate growth path.

Useful controlled setup: create one circle above an expendable inventory, put one ordinary damaged tool inside, leave a known personal mana deficit, and compare durability/balance after one work interval. Avoid valuable enchanted equipment until the blacklist/break test passes. This is a consumed-durability resource loop; renewable tool supply or external repair must be traced separately before calling it infinite or balanced.

## Drain Life: operation, targets and reward order

The boundary starts switched off. Toggle authorization delegates to `ContractMahoujinTileEntity.isImmuneToSpell` (or Fae claiming), rather than unconditional public access. Neighbor redstone inverts the stored switch. Native cost5 is checked after `getManaCycle ×20` tick counts, with mana-cycle1; effect-cycle20 scans a radius10 **axis-aligned cube**, not a sphere. It uses a separate private boundary counter; the inherited base ticker counter is a different field. Nominal steady cadence is one cost/effect opportunity per20 game ticks; initial toggle/startup order and blocked payment need client tests.

Payment tries circuits first at circle position; otherwise player drain allows gems but disables circuits in that fallback. It then checks active-circle limits. Native configuration limits Drain Life to10 active circles per caster. Gate failure can occur **after payment** and toggles the boundary off when caster exists. The cap helper tracks block positions and last-active time, replaces an old entry only when elapsed time is strictly greater than201, and clears its runtime map on server stop; dimension/position collisions and reload behavior remain open. Catalyst/Durability names are absent from the native configured restriction list, so their generic gate call alone does not establish a default cap for them.

Eligible scan entries are alive LivingEntities, stopping at default affected limit100000 after accepted drains. There is no hostile-only test in the Drain Life method. It skips exact configured entity blacklist (empty by default), contract immunity and an optional exact display-name exemption.

The immunity helper protects ArmorStands; caster's tamed animals and contracted players' tamed animals; corresponding Mental Displacement ownership; the caster, contracted players, creative/spectator players; and Geas-protected living targets when caster is online. No FTB-team check is established by that helper. Other players' pets or allies are not automatically proven safe. Complete Contract/Geas creation and saved-data lookup remain separate branches.

Default initial requested damage is2 via `EffectUtil.magicAttack`. That delegate marks last-hurt-by-player/time but calls `hurt` with the target's magic source and ignores its boolean. Attribution for drops and death handlers must be tested; a player credit marker is not identical to a Player damage-source entity.

For each accepted target with an online caster, a server task compares health before/after. If health fell by at least2 or target is dead, it rewards the caster. Otherwise it requests generic damage `2×1.8=3.6`, clears panic/hurt memories, then **still rewards without checking that second damage succeeded**. Contract/name exclusions are checked before initial drain, but cancellation/invulnerability of the actual damage does not necessarily prevent the reward path. This is a source-supported balance boundary, not a confirmed invulnerable-mob farm; verify protection plugins, task timing, survival and actual health/balance changes.

Per accepted reward:

1. If caster health is below maximum, call heal0.5.
2. Otherwise, if food is below20 and ignore-hunger=false (native), add1 food level.
3. Otherwise add1 personal mana, clamped to maximum.

A hungry or injured caster therefore does not receive the mana branch. At full health/food, six accepted rewarded targets per nominal cycle would supply6 personal mana against a5 boundary cost, before other modifiers/cancellations. This is a formula comparison, **not a measured farm yield**. Targets lose health/can die, sustain consumes entities/food/healing resources, cap/gates and offline behavior apply; do not describe zero-maintenance infinite mana.

For the name exemption, a Name Tag ItemEntity thrown by the caster can set the stored exact hover-name string, which is compared with entity display-name text. The callback does not consume the tag. The common scanner has `firstEntityOnly=true`, which stops after its first scanned entity even if that entry is not a qualifying name tag; keep the volume clear during setup. Named exemption persists in NBT. This is not a species whitelist or FTB-team permission.

## Walkthrough and acceptance matrix

| Lesson | Teach / meaningful payoff | Positive and boundary observations (unrun) |
| --- | --- | --- |
| Random catalyst conversion | Native page,3 Emerald powders, active funding and quick output collection; convert surplus powder. XP+ordinary powder-tool materials. | Single input:50 debit/one native output. Non-native dust: no native conversion. Stack3: at most2 original units first callback with adequate funds; keep outputs isolated to measure. |
| Recover mana from tools | Native page,2 Emerald+1 Diamond powders, expendable tool, known mana deficit, compatible inventory directly below. XP+wood/iron tools only if reward does not auto-complete next task. | Compare durability and personal mana below/above; full-mana case must stop. Test near break, Unbreaking, blacklist and NORTH-sided handler. |
| Controlled life sustain | Native boundary page,2 Iron+1 Emerald powders, safe test targets, funding, toggle/redstone and immunity/name rules. XP+food/lighting. | Observe5 debit, actual target damage and health→food→mana priority. Compare excluded/contracted/name-exempt targets and damage-canceled target; do not infer operation from circle possession. |
| Scale responsibly | Explain10-circle native cap and target upkeep before adding more. | Compare tenth/eleventh positions, elapsed201/202 ticks and relog/server restart; inspect payment before gate rejection. No blanket server-safety claim. |

Further evidence: exact `tile/exchange/{CatalystExchangeMahoujinTileEntity,DurabilityExchangeMahoujinTileEntity,ContractMahoujinTileEntity}`, `tile/boundary/{BoundaryMahoujinTileEntity,DrainLifeBoundaryMahoujinTileEntity}`, `tile/{AboveChecker,MahoujinTickingTileEntity}`, `mana/{ActiveCircleConfig,PlayerManaManager}`, `util/{EffectUtil,Utils}`, `config/MTConfig$Server`, and the seven ingredient registrations. No code/assets copied into the repository. Remaining work includes scroll variants, full Contract/Geas lifecycle, external repair/loot economics, effective configs and actual protections.
