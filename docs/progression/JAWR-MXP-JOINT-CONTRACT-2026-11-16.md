# JAWR-only Vampire/Werewolf + MXP joint integration contract

**Date:** 2026-11-16. **Status:** coherent shared-engine **source** integration, no dedicated-server or client gameplay PASS. Original development PRs: [#94](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/94), [#161](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/161). Shared review [#279](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/279). Scope is limited to 1.21.1 NeoForge/Tyz 6.4.2/JAWR 0.5.39 and the previously approved global MXP model.

## Ownership and one-wallet contract

- `compat/mxp` / `tmm_mxp` is the **single global MXP award/normalization ledger**. Its standalone module implements NeoForge server configuration, full event transactions/deduplication, first-time fixed boss/ritual award policy, and an existing reflective Tyz XP bridge. Its **growth_factor default is 1.02**, range 1.00–1.20; one ordinary magic award recomputes cost scaling after crossing a level boundary.
- `compat/progression` / `tmm_progression` is the **only executable owner for path/class gameplay**, including JAWR Vampire/Werewolf form skill registration and effects. All form source data, optional Tyz prefab implementation and native read-only JAWR state checks live here. **No `compat/forms` runtime or second mod/wallet is introduced.**
- Tyz is the **single spendable global XP/skill-point wallet**. MXP delivers XP into it; form ranks purchase/spend Tyz SP. Form purchases/refunds must **not** award more MXP. The shared native Monk/Hunter track's technique points are not Tyz SP.
- The #94 source catalog retains **37 unique skills**, exactly 16 Vampire and 21 Werewolf, all marked `active: false`. It is both the read-only configuration and packaged runtime registration input. All advanced native milestones **fail closed** until the actual JAWR 0.5.39 state/level IDs are checked; cured players retain dormant purchases with no passive effects. In particular, source presence and JUnit compilation **do not activate 37 skills**.
- Four provisional Werewolf effect types are implemented in engine-owned Java: bounded direct-hostile-kill healing, barehand mining speed (not drops/harvest tier), conditional bonus max HP, and post-hit delayed regeneration. These remain off until reviewed Tyz source overrides and client/dedicated-server acceptance.

## Sources and transactions

| Source | Currently executable source? | XP rule and gate |
| --- | --- | --- |
| Ars Nouveau / Iron's Spells / Ars 'n Spells cross-casts | Experimental native Mixin source **exists** in `tmm_mxp`; not loaded/tested in current pack | Real committed spell-cost debit, one transaction ID even if multiple bridges observe it |
| Botania Revived portable tablets/rings | Experimental hook exists, not gameplay-tested | Only portable owned item-stack mana debit; excludes pools, mirrors, previews and other spells' financing |
| Neo Vitae EV network | Experimental post-siphon hook exists, not gameplay-tested | Online player-owned EV debit only; Tier 0 starting normalization B=5,000 |
| **JAWR Vampire/Werewolf blood** | **Contract API only; NO native payment hook yet** | `MxpService.jawrCommittedBloodDebit(player, tx, nominalCost, actualBloodDebit)` after a native JAWR-confirmed real debit; configurable reference B=900, default **0 disables**, never called by passive regen/feeding/bottle transfers/previews; must reuse the cross-payment transaction ID |
| Rituals / bosses | Public API and registered-boss final-blow pilot, but no invented boss rules | First successful registered boss each 1,000 fixed effective MXP; first ritual fixed; repeat divided unless explicitly fixed; no XP for unregistered targets |

**JAWR safety:** The first-access 900 blood pool is the planned source-normalization target, not proof that the installed 0.5.39 native max is 900. The new method is inert until a later adapter verifies a post-commit blood delta, native account/ownership and success condition. It must be called **after** a real committed payment. Do not turn amount-only item use, blood-bottle drinking, replenishment, or mere casting into XP. The MXP ledger deduplicates a reused transaction ID across multiple school adapters; regression tests enforce this contract.

## Native gameplay blockers and next test tasks

1. **Quick/module-only compile and tests:** Gradle Java 21 `compat/mxp classes test`, `compat/progression classes test`, 37-node no-activation Python preview tests, and default pack metadata static CI. These do not load any Minecraft game world.
2. **Tyz native registration:** In a dedicated 1.21.1 NeoForge disposable server with exact Tyz 6.4.2, verify 37 IMMUTABLE prefab identifiers, merged categories, actual PurchasePre/RefundPre listeners, no new mod ID, and no startup/linkage failures when Tyz or JAWR is missing.
3. **JAWR native eligibility:** Test non-vampire/non-werewolf, Lilith-blood Vampire initiation, Wolfsbane Werewolf initiation, cure, advancement, relog/death/dimension, server/client and multiplayer. Missing JAWR state must fail closed. Unknown milestone IDs remain unavailable until validated.
4. **Form pilot effects:** Validate only a truly direct hostile player kill yields capped Werewolf healing (no AFK/pet double kill), bare-fist mining speed works client/server but does not grant invalid drops, max HP toggles immediately on cure and cannot retain extra hearts, and regeneration never consumes/generates nonexistent mana or blood.
5. **MXP payments:** Confirm current Tyz 6.4.2 addXP XP/level differential matches its configured level pool, persisted lifetime P survives relog/death/respec, `growth_factor=1.02` controls ordinary awards, and first fixed boss awards bypass the divisor. Check no other native Tyz XP source independently pays for the same action.
6. **JAWR blood:** Implement audited native 0.5.39 post-debit adapter and verify 900 baseline/actual cap, player ID and one transaction per logical action; include explicit failure/cancellation/refund/no-debit tests before enabling. **This is not complete in this source PR.**
7. **Optional spell hooks:** Confirm Ars, Iron's, Ars 'n Spells, Botania and Neo Vitae Mixins attach to the **pinned** JARs in a dedicated-server boot, and that cross-payments do not double credit. KubeJS Psi and boss-rule pilot scripts remain outside active packwiz.
8. **Activation:** Enable selected individual form skills only after their exact effect handler and native JAWR gate are validated. Most proposed abilities—including qualified bites, blood reservoir/debits, advanced harvest legality, silver mitigation, and later native milestones—remain **design-only**, not playable 37-skill parity.
9. **Packaging:** These are **two source modules**, `tmm_progression` and `tmm_mxp`. Their code must be built, packaged into first-party artifacts and indexed before a test build can actually exercise them. Source-only integration is not Packwiz installation or an automated full test.

No new worldgen, Vampirism or Werewolves legacy mods, Tier 0 full blood reservoir changes, invented boss IDs, FTB quest changes, or full server/Prism build are authorized by this reconciliation.
