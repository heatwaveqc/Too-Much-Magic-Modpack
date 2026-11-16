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
| `MANA_REGEN_PER_TICK=1`, `MANA_RECOVERY_SLEEP=0.5` | Regeneration inputs only; event cadence and full modifiers remain unaudited. Do not translate the field name into a measured mana/second rate. |

`regenMana` and `getManaRegen` include additional context; a full leyline/Fae/hunger/Geas/death interaction and tick-caller audit remains open. Attuned-gem charging, capacity variants, creation, transfer and break/drop behavior are not complete here. Magitech energy conversion/interfaces also remain open. Individual spell callers must be traced for option flags, cost timing, interruption and effect-before-payment behavior. No runtime/config changes or test recipes were made.

## Reproducible evidence

Exact artifact classes under `stepsword/mahoutsukai/`: `mana/PlayerManaManager.class` (`drainMana`, `gemsInInventory`, `chargeCircuit`, nearby battery count/drain/search, `updateManaCounter`, `damagePlayer`); `dataattachments/mahou/Mahou.class`; `block/circuits/ManaCircuitBlock.class`; `tile/circuits/{ManaCircuitTileEntity,ManaCircuitMagitechTileEntity}.class`; `item/{attunedgems/AttunedGem,FaeEssence}.class`; `config/MTConfig$Server.class`; `mana/ManaEvents.class`. Payment branch ordering was cross-checked with `javap -p -c`, in addition to selected-class CFR inspection. Scratch decompilation stays outside Git; only research prose is committed.
