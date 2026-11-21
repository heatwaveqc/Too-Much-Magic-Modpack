# Mahou Tsukai — pinned mana and circuit contract

Companion to [the main profile](mahou-tsukai.md) and [starter/catalyst crosswalk](mahou-tsukai-pinned-onboarding.md). Inspected 2026-11-16 from exact `mahoutsukai-1.21.1-v1.36.31.jar`, SHA-512 `0c4f3bc823d681ad9cb00895d348d2b414201c4d397ca3aa6427b45b8d2b369e0bae8c1f354a9541e4205dadb58c49203d3570e2433d772c3eef1227bc8bfa5a`, matched to [packwiz pin](../../../mods/mahou-tsukai.pw.toml). **Partial source research; no gameplay acceptance.** Values below are binary defaults, not a claim about effective world/server overrides.

## Personal resource and payment

`Mahou` initializes personal stored and maximum mana to100, charge rate0.1 and growth counter100. `PlayerManaManager.drainMana` has per-call switches for gems, circuits, maximum growth, client updates and draining when insufficient. Do not assume every spell uses the same options.

| Ordinary player payment case | Exact manager behavior |
| --- | --- |
| Personal mana covers request | Uses personal mana alone. |
| Personal is short, permitted total covers request | Uses all available personal mana, then carried gems, then eligible nearby circuits for the remainder. |
| Total is short; gems allowed; `drainifnotenough=false` | Reports zero and does not debit personal/gems/circuits. It still clears the personal mana-full flag and can run update logic. |
| Total is short; `drainifnotenough=true` | Debits available personal mana and attempts circuits before gems; reports the sum actually debited. |
| Total is short; gems disabled; `drainifnotenough=false` | Still debits available personal and eligible circuits. This branch is not an atomic failure. |

The ordinary four-argument overload enables maximum growth and client updates, and disables `drainifnotenough`. Creative bypass is enabled by the native default and returns the requested amount without ordinary resource payment on the server. Fake players have separate handling: generic fake-player personal balance is treated as zero; `SafeFakePlayer` supplies its own balance. This is not proof of universal automation compatibility.

`gemsInInventory` searches inventory items, armor and offhand, in that order, for `AttunedGem` instances. No Curios search or team wallet is established by this helper. Circuit enumeration uses a HashSet, so a nearest-first or stable battery priority is not promised.

Only the **personal amount spent** is passed to `updateManaCounter` when growth is enabled. Gem/circuit spending does not itself count as personal expenditure. With unchanged defaults, spending100 personal mana advances maximum mana by1, carrying surplus expenditure through the counter; configured ratio, punishment and cap can alter this. `chargeCircuit` also passes personal expenditure to growth, so donating personal mana is an eligible expenditure path. This is a binary mechanic, not a tested or balanced progression strategy.

Optional `damagePlayer` applies when personal mana was spent, but native `MAGIC_DAMAGES_PLAYER=false`; do not teach damage-per-cast as enabled by default. The optional flat default is1 health point with generic damage selected. Dagger damage/Bleeding is a separate onboarding mechanism.

## Circuit access, range and control

1. Place a circuit, consult Compendium **Mana Circuits**, and interact with it. The server helper claims an unowned circuit or one assigned to the special Fae UUID for the interacting player. Exact placement/break/drop preservation still needs a full lifecycle audit.
2. Ordinary charging requests `ceil(personal maximum × charge rate)`, capped by free circuit space. Initial charge rate0.1 therefore requests10 at maximum100. The ordinary player charging helper draws personal mana then gems and accepts partial available payment; it does not drain another circuit in that normal capability branch. Another player can donate: that branch has no owner equality requirement.
3. The helper's sneaking/discrete branch toggles the stored on flag **without checking the owner**. This is a multiplayer control boundary requiring a pack-protection test, not proof claims protection is bypassed.
4. Its non-sneaking main-hand `FaeEssence` branch allows only the owner to toggle the shared/Fae flag. Fae Essence also has its own `useOn` behavior; actual held-item dispatch needs a client test before prescribing a guaranteed click sequence.
5. Consumption requires a non-null caller UUID and either matching owner UUID or the circuit's shared/Fae flag. This is local circuit sharing, not FTB-team ownership or a separate currency conversion.

`nearbyBatteries` scans chunks in the current level covering horizontal position ± configured range, then filters circuit block entities by strict `abs(dx)<range` and `abs(dz)<range`. **There is no Y-distance filter.** At native range10, a horizontal difference9 qualifies and10 does not; a circuit directly above/below can qualify regardless of height within the same level. It is a square horizontal test, not a radius10 sphere. The helper calls `world.getChunk`; it is not evidence of a loaded-chunks-only or cheap search.

Default consumption searches active circuits. `ManaCircuitTileEntity.isRedstoneOn` returns the stored flag when no neighboring signal exists and its inverse when a signal exists. Redstone **inverts** the flag; it does not always mean disable. Teach/test both stored states with and without power.

The block entity saves stored mana, on flag, owner UUID and shared/Fae flag to NBT. Its implicit item-component collection includes mana and owner but does not itself collect on/shared flags. Do not assume every break/place route preserves all four fields.

## Native configuration defaults and remaining limits

| Binary field/default | Interpretation |
| --- | --- |
| `MAX_MANA_CAP=200000` | Personal maximum growth ceiling. |
| `MANA_UP_COUNTER=100`, ratio0, counter maximum100 | Default growth threshold; ratio mode is inactive. |
| `MANA_INCREASE=1`, punishment factor0, logarithmic punishment false | Default growth increment; no configured growth penalty. |
| `MANA_CIRCUIT_CAPACITY=100000` | Ordinary circuit capacity. |
| `MANA_CIRCUIT_MAGITECH_CAPACITY=100000` | Magitech subclass capacity; equal defaults do not establish identical interfaces. |
| `MANA_CIRCUIT_RANGE=10` | Strict horizontal coordinate bounds described above. |
| `MANA_REGEN_PER_TICK=1`, `MANA_RECOVERY_SLEEP=0.5` | Regeneration inputs; exact periodic caller is every100 game ticks, described below. This is not one mana every game tick. |

The regeneration caller, leyline formula, Fae-spawn side effect and ordinary gem charging are now traced below. Geas/William acquisition, Death Collection lifecycle, entity population/despawn and complete break/drop preservation remain open. Individual spell callers must be traced for option flags, cost timing, interruption and effect-before-payment behavior. No runtime/config changes or test recipes were made.

## Reproducible evidence

Exact artifact classes under `stepsword/mahoutsukai/`: `mana/PlayerManaManager.class` (`drainMana`, `gemsInInventory`, `chargeCircuit`, nearby battery count/drain/search, `updateManaCounter`, `damagePlayer`); `dataattachments/mahou/Mahou.class`; `block/circuits/ManaCircuitBlock.class`; `tile/circuits/{ManaCircuitTileEntity,ManaCircuitMagitechTileEntity}.class`; `item/{attunedgems/AttunedGem,FaeEssence}.class`; `config/MTConfig$Server.class`; `mana/ManaEvents.class`. Payment branch ordering was cross-checked with `javap -p -c`, in addition to selected-class CFR inspection. Scratch decompilation stays outside Git; only research prose is committed.

## Regeneration cadence and leyline arithmetic

`ServerHandler.worldTick(LevelTickEvent.Pre)` sets the shared counter to the level game time modulo50000. Its server-side `PlayerTickEvent.Post` handler calls ordinary regeneration when that counter is divisible by100. Under normal synchronized level time this is **one opportunity every100 game ticks**, nominally five seconds at20TPS. The modulus50000 preserves that divisibility. This is code-derived timing, not a measured rate under server lag or altered level time.

For a personal balance below maximum, `getManaRegen` adds these terms, then `regenMana` subtracts the Death Collection penalty and clamps balance to0..maximum:

| Term | Exact calculation / native default |
| --- | --- |
| Base | `MANA_REGEN_PER_TICK`, default1 per regeneration opportunity. |
| Scaling | Integer truncation of scaling factor × personal maximum; factor defaults0. |
| Hunger | Ceiling of `(food level + saturation) × (maximum / MAX_MANA_CAP) × hunger factor`; factor1, cap200000. |
| Leyline | If enabled, integer truncation of `punishment + (nearbyLeyValue / 19) × maximum × factor` in proportional mode; punishment−1, factor0.01, flat mode false. Invalid dimensions zero this term. Flat mode replaces maximum scaling with factor alone. |
| Geas | Integer truncation of minimum(configured cap, unique Geas count × per-Geas amount); defaults cap10000000, amount3. The unique-count helper/acquisition still needs separate audit. |
| William | Integer truncation of `William.itemWilliamPlethora(player) × factor`, default factor0.01. William's supply/acquisition remains a separate branch. |
| Death Collection | Subtract integer truncation of drain-per-soul × uses-left; drain default1. It can overwhelm positive regeneration. With its negative-regeneration option enabled, excessive debt can also reduce uses-left and send a warning. |

Java casts truncate toward zero: a ley term between−1 and0 becomes0, not−1. For a controlled native-default example with maximum100, food20, saturation0, no Geas/William/souls and valid dimension: away from lines (`ley=0`), base1 + hunger1 − ley1 yields **1 mana per100 ticks**; at a location with score19, ley term0 yields **2 per100 ticks**. These are formula examples, not measured TMM rates. When balance is already at/above maximum, the helper skips the regeneration formula and sets balance to `max(0, maximum − soul penalty)`.

Ley geometry uses configured distance300 and offset0 on X/Z coordinates, including normalized negative remainders. Vertical height does not affect line score. `nearbyLeyValue` examines a7×7 horizontal square: STAR positions contribute3, diagonal CROSS2, other non-NONE shapes1. It is a weighted coordinate pattern, not a count of nearby Fae or world-generated blocks. Render-height70 is not a required player Y coordinate. Dimension matching accepts full dimension ID or namespace; the native list is empty with blacklist mode true, so all dimensions pass that native filter. Pack overrides remain separate.

On a qualifying wake event (`!wakeImmediately && !updateLevel`), `ManaEvents.mahouSleep` checks the time/thunder condition and calls sleep regeneration. Sleep adds integer truncation of0.5×maximum by default, subject to soul penalty and clamp; it does not simply set mana to full. The exact event/time guard should be tested before promising a repeatable bed exploit or every interrupted sleep restoring mana.

### Fae supply for ordinary circuits

The ordinary periodic formula can attempt a Fae spawn when dimension is valid, score≥13, the player's Fae-spawn setting and `hasMagic` are true, and random float is below `0.1 × FAE_SPAWN_RATE` (native factor1). This branch is outside the `LEY_AFFECTS_MANA` check: disabling the ley mana bonus alone does not disable this spawn attempt. It only runs when regeneration calls `getManaRegen`, so the already-full branch and sleep branch do not make this attempt.

The candidate position is player X plus random0..9, player Y, player Z plus random0..9. The candidate block must be air. The code then calls addFreshEntity without using its boolean return; neither a successful spawn nor exact real-time yield is guaranteed. Natural-spawn checks exist separately in `FaeEntity`; this direct construction path does not itself call those checks. No local population cap is visible in this branch; broader spawn/despawn/protection behavior remains open.

`FaeEntity` points to current singular loot table `mahoutsukai:fae`. That table has one essence pool, count1..2 plus its Looting count-increase function, and no killed-by-player condition in the table. This establishes a native Fae Essence source, not guaranteed drops through every event/protection route. Fae are invisible by default; `isInvisibleTo(player)` becomes false for a player with Fay Sight Eyes. Obtain the corresponding spell through the [native recipe crosswalk](mahou-tsukai-pinned-onboarding.md) and consult **The Fae/Leylines** before teaching collection. Full targeting/damage acceptance remains untested.

Ordinary circuit recipe is eight `mahoutsukai:fae_essence` around one `mahoutsukai:powdered_emerald`. The separate Magitech recipe is six Iron Ingots plus three Powdered Emerald (left/right iron columns, central powder column). These are alternatives with different material burdens; do not require eight essence for the Magitech variant.

## Attuned gems and Magitech scope

- Craft `mahoutsukai:attuner` with four Lapis Lazuli at the corners and four Gold Ingots at the edge middles, center empty. Shapeless Attuner + one Diamond or Emerald yields the corresponding attuned gem. `Attuner` extends the simple `ItemBase(String)` constructor without a crafting remainder; these recipes consume the Attuner. It is not the reusable powder tool.
- Each gem starts with a zero-valued `GEM_COMPONENT`, stack limit1. Capacity is read through variant overrides: Diamond10000, Emerald5000 native defaults. The constructor constants alone are not authoritative when config differs.
- Use a held gem without sneaking on the server. Charge request is `ceil(personal maximum × charge rate)`, limited by free gem capacity. It calls `drainMana(player, request, true, false)`: **other gems disabled, circuits allowed**, with ordinary growth/update defaults. Thus nearby eligible circuits can fill a carried gem; personal expenditure counts toward growth, circuit expenditure does not. Insufficient funds take the partial-payment branch already documented above.
- The stored value is written back to the item's component; using a full gem does not request another drain. Ordinary spell payment searches carried gems automatically. No owner UUID check appears in that gem search/charge contract, unlike circuits. Complete cross-player transfer, death-drop and duplication behavior still needs testing.
- Magitech tile extends the ordinary circuit tile and only overrides its capacity; its block reuses the same charge helper and adds facing properties. The pinned capability class/registration audit exposes the Mystic Code item handler, and a class-reference scan found no `IEnergyStorage`/`EnergyStorage`/`Capabilities$Energy` references. **No native FE conversion is established by this artifact audit.** Do not infer it from “Magitech” or an official page for a different version. Third-party adapters remain a separate two-sided research question.

### Walkthrough and boundary tests (not run)

| Lesson | Positive disposable-world case | Negative/boundary case and observable |
| --- | --- | --- |
| Natural regeneration | With recorded effective config, max100, hunger20/saturation0, no other modifiers, sample balance across100 ticks away from lines. | Repeat at score19 and with mana already full; compare actual deltas to formula, not a per-tick tooltip reading. |
| Portable storage | Craft one empty gem, record personal/gem/circuit balances, charge once. | Deplete personal and place one owned active circuit within bounds; verify circuit-to-gem debit. Disable circuit or put at dx10 and verify it stops supplying. |
| Attuner burden | Craft Diamond+Attuner and inspect resulting gem/component. | Check crafting grid for a returned Attuner; binary path predicts none. Do not reward based on an assumed reusable tool. |
| Fae supply | At score≥13, mana below max, magic unlocked and Fae setting on, observe qualifying attempts with air available; use Fay Sight. | Repeat full mana or invalid dimension, distinguish no attempt from an attempt rejected at add/spawn; do not use a fixed timer as completion proof. |
| Circuit access | Owner activates and supplies a spell; shared flag permits another UUID. | Unshared second player cannot consume, but test whether that player can toggle via discrete interaction; protection behavior remains unknown. |

Use Compendium **Mana**, **Mana Circuits**, **Leylines**, **The Fae**, and JEI for the named recipes. Item possession detects crafting only; charging/supply and spawning require observable balances/actions or honest manual fallbacks. Suggested lesson rewards: XP plus ordinary food/lighting or Lapis; avoid awarding a charged circuit or Fae Essence that auto-completes the next acquisition lesson.

Additional exact evidence: `handlers/ServerHandler.class` (world/player/wake handlers); `effects/Leylines.class`; `entity/fae/FaeEntity.class`; `item/attunedgems/{Attuner,AttunedGem,AttunedDiamond,AttunedEmerald}.class`; `item/ItemBase.class`; `capabilities/ModCapabilities.class`; `handlers/ServerHandlerModEventBus.class`; `data/mahoutsukai/recipe/{attuner,attuned_diamond,attuned_emerald,mana_circuit,mana_circuit_magitech}.json`; `data/mahoutsukai/loot_table/fae.json`. No extracted assets or decompiled code are committed.
