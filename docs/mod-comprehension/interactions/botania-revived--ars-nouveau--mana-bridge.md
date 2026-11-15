# Botania Revived × Ars Nouveau — mana and Source bridge

| Field | Value |
| --- | --- |
| Participant manifest IDs / versions | `botania-revived` 2.0.2 / `ars-nouveau` 5.13.5 |
| Source profiles | [Botania Revived](../mods/botania-revived.md), [Ars Nouveau](../mods/ars-nouveau.md) |
| Pack integration base | `test/full-integration`; reviewed against current `origin/test/full-integration` on 2026-11-15 |
| Interaction status | source-supported |
| Mechanism class | Native compatibility code in Botania Revived, enabled when Ars Nouveau is loaded; capability adapters and player resource hooks |
| Recommended game stage | Midgame; may be available earlier if both source systems are obtained |
| Reviewed | 2026-11-15 |

## Player-facing payoff

Botania Revived 2.0.2 implements a substantial bridge rather than a recipe-only cross-mod tag. Mana Bursts can charge Ars Nouveau Source containers/machines; Botania Mana Pools expose their stored mana to Ars Source consumers; Ars Source machines can receive Botania bursts; Ars player mana can power Botania mana items; Botania mana items can cover missing Ars spell mana; and Mana Cookies grant Ars Mana Regen. This can connect two otherwise distinct resource systems and reduce the need to carry the matching mod's stored item. It may also erase intended scarcity unless the exact ratios and eligible item paths are tested. [source: exact Botania JAR/source; Ars 5.13.5 pin]

## End-to-end mechanism

| Step | Mod and system | Actual type / registry hook / conditions | Consumed, transferred or transformed? | Evidence |
| --- | --- | --- | --- | --- |
| 1 | Botania Mana Burst → Ars machine | Mana Burst transfer into Botania `MANA_RECEIVER` capability registered for `Source Jar`, Creative Source Jar, Arcane Relay, Relay Collector, Relay Deposit, Relay Warp and Imbuement Chamber | Botania mana converts into Ars Source; default ratio is 15 Botania mana = 1 Ars Source. Partial units are held as a machine attachment remainder. | Botania exact source `integration/arsnouveau/ArsNouveauIntegration.java`, `SourceManaReceiver.java`, config default `mana_per_source=15.0` |
| 2 | Ars Source storage → Botania Mana Burst target | Botania receiver capability wraps Ars `AbstractSourceMachine`/`ISourceCap`; custom `SourceManaReceiver` reports pool-equivalent capacity/current mana and accepts bursts | Botania mana removes/adds Ars Source in 15-mana chunks with a saved remainder. | Same exact source; registerSourceMachine implementation |
| 3 | Ars Source network → Botania Mana Pool | Botania `ManaPoolBlockEntity` exposes Ars `ISourceCap` via `PoolSourceCap` | Ars Source extraction/receipt converts to/from Pool mana at 15 mana per Source. Transfer max defaults to 1,000 Source per operation. | Botania exact source `PoolSourceCap.java`, config defaults |
| 4 | Ars player mana → Botania mana items | Botania's `PlayerManaSources.Source` bridge offers the player's current Ars mana to eligible Botania mana item requests | Default `ars_to_botania=4.0`: one Ars mana unit offers four Botania mana units; Botania items consume the Ars ManaCap by requested amount divided by ratio. Mana items are excluded as requestors. | `ArsManaBridge.getAvailable/take`; `BotaniaConfigSpec.ArsNouveauValues` |
| 5 | Botania mana items → Ars spell deficit | Ars `SpellCostCalcEvent.Pre` for a server player; creative players and costs above Ars max are skipped | If Ars mana is short, Botania mana items are requested to cover deficit at default `botania_to_ars=0.006` Ars mana per Botania mana; sufficient Botania mana is consumed and Ars mana is temporarily topped up for the cast. | `ArsManaBridge.onSpellCost`; `BotaniaConfigSpec.ArsNouveauValues` |
| 6 | Ars overflow → Botania mana items | Once per second at full Ars ManaCap; only when `overflow_charges_botania_items=true` | Converts Ars regeneration into Botania item charge at the `ars_to_botania` rate. The option defaults false. | `ArsManaBridge.onPlayerTick`, config default |
| 7 | Mana Cookie → Ars effect | Finish-use event for exact `botania:mana_cookie` | Applies Ars Mana Regen effect for 3,600 ticks at amplifier 1; config default `mana_cookie_regen=true`. | `ArsManaBridge.onFinishUsingItem`, `COOKIE_REGEN_TICKS=3600` |

## Existing support vs work required

- **Native support and present:** yes. Botania Revived's pinned mod metadata declares Ars Nouveau optional with minimum 5.13.0; the source includes an explicit integration package and capability registration. TMM pins Ars Nouveau 5.13.5 and Botania Revived 2.0.2, meeting those version declarations.
- **Pack implementation already exists:** yes, in the Botania Revived JAR itself. No TMM KubeJS bridge or config override is needed for its defaults. The bridge is conditional on Ars being loaded.
- **What remains unknown:** exact version pair gameplay, all affected mana item IDs/categories, interaction with PlayerManaSources contributed by other mods, fraction/rounding behavior, multiplayer synchronization, source-network side restrictions and balancing. `docs/COMPATIBILITY.md` records server co-load only.
- **Not implied:** this does not make Botania mana a fluid or FE, nor prove that every Ars ritual/device accepts a Botania mana burst. Registered receivers and Ars consumers are bounded by the listed capability hooks.

## Minimum practical setup

1. Reach a working Botania mana network: generating flower → spreader → Mana Pool, with a Wand of the Forest to bind the network.
2. Reach an Ars Source Jar/Relay/Imbuement Chamber or player ManaCap and confirm both mods' status displays.
3. For machine tests, aim a spreader at a specific registered Ars Source machine and inspect Source before/after; for pool tests, use an Ars Source consumer against a Botania pool capability.
4. For player tests, compare a Botania mana item alone, Ars mana alone, and both resources during one low-cost spell and one Botania tool/item action. Record exact before/after HUD and item charge.

**Cost and power comparison:** code defaults establish the conversion constants but not measured throughput. The machine bridge uses 15 Botania mana per Ars Source, a 1,000 Source/operation cap; player item requests use 4 Botania mana per Ars mana in one direction and 0.006 Ars mana per Botania mana in the spell fallback direction. These directional defaults are asymmetric by design/config; no rate or balance equivalence is claimed.

## Balance, safety and compatibility

- Progression locks and item-gate leakage: Ars mana may power Botania mana items; Botania item mana may cover Ars spell costs. Audit high-value flight, combat, teleportation and high-capacity item abilities before making either system a mandatory gate.
- Resource loops or duplication: the conversions are directionally distinct and not a single symmetric exchange rate. Test conversion round trips, fractional values and simultaneous item/container/player paths for duplication or rounding leakage.
- Entity/world performance: not material to the bridge; flower production and burst networks still need throughput/performance tests separately.
- Multiplayer, teams, dimensions and persistence: player mana updates call Ars `syncToClient`; machine remainder is stored as a BlockEntity attachment. Verify values after relog, chunk unload/reload and multiplayer. Remainder persistence is code-backed, state sync is not tested.
- Exclusion risks: Botania Revived also contains optional Sophisticated modules, but TMM excludes Sophisticated mods; no dependency should be added. Create support is absent and prohibited by TMM. No ProjectE/Chaos Guardian outcome is implied by this bridge.
- Version invalidation: Botania Revived source pins Ars minimum 5.13.0; TMM has Ars 5.13.5. Any change to either artifact or the Botania config ratios requires targeted re-review.

## Quest / Tips & Tricks opportunity

Optional cross-system guide: show a Botania Mana Burst charging an Ars Source block and compare one stored Ars Source with the pool's corresponding mana amount. Teach the player to keep the Lexica open to Mana Manipulation → Spreader and the Ars source machine guide; then show a single spell-cost fallback in a disposable test or omit it from progression until its player item list is audited. A quest task should detect the resulting operation only if a reliable detector exists; a crafted spreader or Source Jar alone proves no transfer. Safe reward: one matching useful component, selected after balance review. No quest files changed.

## Verification plan

| Test ID | Positive or boundary case with disposable-world steps | Expected observable value/behavior; distinguish source formula from measured rate | Observed status |
| --- | --- | --- | --- |
| INT-01 source machine | At exact pins, record source amount; send known Botania mana burst totals below, equal to and above 15 mana | Source rises by floor((burst + remainder)/15); remainder persists; packet/render value syncs | not run |
| INT-02 pool adapter | Query Botania Pool through Ars Source capability; receive/extract 1, 15, 1,000 and 1,001 Source | Correct pool value changes; max source transfer defaults to 1,000; no free fractional Source | not run |
| INT-03 Ars mana powers Botania | With no Botania charge, use representative Mana Tool and bauble while holding different Ars mana amounts | Default output offers 4 Botania mana per Ars mana; amount is removed according to code and HUD remains synced | not run |
| INT-04 Botania pays spell | Cast a spell at full, partial, zero, and insufficient combined mana; compare a cost below and above Ars max | Botania items cover only eligible Ars deficit at 0.006; correct item mana is removed and player cast succeeds/fails consistently | not run |
| INT-05 boundary rounding | Request 1–3 Botania mana from an Ars-backed requestor at low Ars mana; repeat, then test ratio values that are noninteger | Determine whether `amount / arsToBotania` truncation creates a fractional/free residual; do not claim exploit until observed | not run |
| INT-06 persistence | Build conversion with partial remainders; unload/reload chunk, relog and test multiplayer | Machine remainder and player mana/display survive without duplication/loss | not run |
| INT-07 option defaults | Test config toggles for spell payment, Ars item power, overflow charging and cookie regen | Defaults match source; each disabled option disables only its feature | not run |

## Evidence and open questions

| Claim | Primary source URL / pinned artifact file + version | What verified / unknown |
| --- | --- | --- |
| Botania bridge source | Exact Botania Revived 2.0.2 JAR and matching source commit `5f803227` | Registered capability types, default ratios, consuming handlers and spell event implementation |
| Botania dependency floor | Exact `META-INF/neoforge.mods.toml` | Ars Nouveau optional minimum is 5.13.0; TMM pin 5.13.5 satisfies it |
| Ars exact pin | `mods/ars-nouveau.pw.toml` | TMM targets 5.13.5; no in-world pair test |
| Botania capability boundaries | `integration/arsnouveau/{ArsNouveauIntegration,PoolSourceCap,SourceManaReceiver,ArsManaBridge}.java` | Exact Source machines and player resource hooks, but not every feature from promotional/project description |

- [ ] Test rounding, transfer, and multiplayer synchronization at exact locked versions.
- [ ] Identify the entire Botania mana item registry accepted by PlayerManaSources and compare it with TMM progression-critical gear.

## Recommendation

**Disposition:** test native interaction before designing quests or balance gates.

**Why:** both TMM versions satisfy the port's declared Ars range and code establishes multiple real resource paths. Because the bridge can cover player mana deficits and power utility items, its value and progression impact are large enough that source formulas alone are insufficient.

## Revision notes

- 2026-11-15 — Traced both sides of Botania Revived's Ars bridge from pinned source: Source machine capabilities, Mana Pool Source capability, player mana item source, spell-cost fallback, optional overflow charge and Mana Cookie effect. Source-supported only; no runtime test.
