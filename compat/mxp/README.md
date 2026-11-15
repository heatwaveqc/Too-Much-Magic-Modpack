# TMM MXP — initial NeoForge ledger and award dispatcher

**Stage:** feature/mxp-ars-irons-botania-neo-hooks, 2026-11-14 (extends MXP dispatcher). Actual Java source, build and progression tests exist; **not yet packwiz-pinned, game-launched or verified with the Tyz 6.4.2 runtime JAR**. Designed not to delay the next full test build. Based on the Core Magic feature branch; reconcile with [MXP specification PR #27](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/27) and [Core Magic implementation PR #33](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/33).

## Implemented

- Standalone **tmm_mxp** NeoForge 1.21.1 / Java 21 module under compat/mxp. Tyz's Skills 6.4.2 is declared a required runtime dependency; its source-verified public API is called reflectively so the third-party JAR need not be redistributed.
- Pure Java progression math: base = **1000 × nominalCost / first-access resource pool B**; ordinary effective XP = base / (1.2^P), with the divisor **recomputed mid-award** at each 1000-effective-XP point boundary. No vanilla XP spent.
- First successful ritual and **every registered first boss victory awards exactly 1,000 effective MXP** without the divisor; repeat rituals/bosses are divided. An explicit boss rule may mark repeat victories as fixed (Chaos Guardian **proposed** 100 MXP repeat, not activated). First-time magical crafts are **still divided**.
- Overworld SavedData by player UUID persists lifetime MXP-earned levels P, effective progress, undelivered fractional XP, discovered ritual/boss/craft IDs, and the latest 1024 transaction IDs. Respec/death/dimension changes cannot reset the earned lifetime divisor.
- Tyz public API bridge measures **actual** Tyz level/XP change after the award. It stores sub-0.01 fractional awards until deliverable, and retains undelivered portions of partially accepted awards.
- Public server integration API via MxpService.resource, ritual, craft, boss and inspect. Resource awards require both nominal pre-efficiency cost and **positive actual debit after successful player action**.
- Generic LivingDeathEvent handler awards only the **server player with the final blow** for explicitly registered boss IDs. **Boss rule registry empty by default**; no invented Chaos Guardian IDs or unapproved rewards are live.
- Operator diagnostic commands: /tmmmxp status, /tmmmxp test ordinary <base>, /tmmmxp test fixed <amount>. Only diagnostics require operator permission 2.
- Source-backed **Psi 110** KubeJS pilot lives in pilot/kubejs/server_scripts; preSpellCast captures compiled nominal cost, spellCast verifies actual Psi/CAD drain, then calls MxpService.resource with **first-access player Psi pool 5000**. It is not shipped into packwiz.

## Source integration contract

Call MxpService.resource(player, sourceId, transactionId, nominalCost, firstAccessPool, actualDebit) **after** an accepted and committed magical action. Transaction IDs must uniquely identify the world operation, and should be common across two adapters observing the **same cast**. No XP for previews, simulation, transfers, idle mana generators, cost-free casts or machine maintenance. Shared Ars/Iron mana is **one logical transaction** and should not be counted twice. Positive regen and efficiency effects still improve sustainable nominal MXP earned.

Call MxpService.ritual(player, id, completionTxId, firstFixed, repeatDivided) on confirmed ceremony completion, not on every ritual tick. Call MxpService.craft similarly, but **both** first and later craft awards are divided. Boss rules use MxpService.registerBossRule(canonicalEntityId, repeat, fixedRepeat), then the native final-death hook handles configured entities. Example rules in pilot/kubejs/server_scripts/tmm_mxp_boss_rules_example.js are disabled, with purely illustrative amounts.

The core does NOT store mod resource baselines for all 42 entries yet; the per-source adapter currently supplies a verified reference B. See [per-mod registry](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/MAGIC-MXP-SOURCE-REGISTRY.md).

## Deliberately deferred automatic sources

- **Neo Vitae AnimaEvent.PostSyphon:** now hooked. Only real EV-network PostSyphon debits earn MXP for an online owner; other resources do not. Anima capacity is determined by the bound orb: the Tier 0 Novicius/Weak Blood Orb has **5,000 EV Anima capacity**, now our approved fixed starting baseline; the source hook is enabled by default. Because AnimaTicket does not label its source, every actual network expense (including automated upkeep) is eligible while the owner is online; revisit this rule before survival release.
- **Ars Nouveau/Iron's:** optional native mixins capture original spell costs and observe native committed debits; Ars 'n Spells paid reservations are observed at its commit path, with cross-cast transaction IDs to prevent double credit. **Pinned ANS 3.3.5 ABI and payer behavior still need a runtime smoke test**; no MXP is awarded by a prepayment event alone.
- **Botania Revived:** native handler hook counts actual mana drained exclusively from eligible tablets and mana rings in player inventory/offhand or Curios, including equipment repairs. Excludes world pools, mana mirrors, transfers, simulations and outside Ars funding. Mixed payments are prorated, and Botania financing another spell is suppressed to prevent double credit.
- **Totemic:** ceremonyStartupSuccess can be canceled and does not itself identify meaningful music participants. Determine the successful completion + UUID attribution before granting fixed first-time XP.
- **Boss exceptions:** multipart dragon and Chaos Guardian completion need source-verified final defeat. MVP only attributes the final blow to a direct player; party contribution, pet damage and owner attribution are future work.

## Compile and world-test protocol

Run in compat/mxp: gradle --no-daemon clean build. GitHub CI compiles the pure Java core and runs JUnit + NeoForge addon compilation.

In a disposable NeoForge 1.21.1 world with **Tyz 6.4.2 and the Core Magic level-pool settings**:
1. Run /tmmmxp status, /tmmmxp test ordinary 1000, /tmmmxp status and /tmmmxp test fixed 1000. Verify exact Tyz levels/SP and persisted MXP P.
2. Run further ordinary awards to verify divisor growth, fractional precision and per-level repricing. Ensure ordinary vanilla kills/mining/crafting do not award Tyz XP.
3. Save, relog, die and change dimensions; verify lifetime P, progress and discovery history remain intact. Check world/data/tmm_mxp_players_v1.dat.
4. Optionally stage **only** the Psi pilot with this compiled mod, then cast a paid CAD spell with/without Thrift and verify positive actual debit. Test failure, no-debit casts and different CAD configurations.
5. Once values/IDs are approved, activate an explicit Wither boss rule in a test world; verify initial fixed and repeat divided awards, and deduplication.
6. Compile and client/dedicated server smoke **separately**; unit tests are not game evidence.

## Production blockers

- The exact pinned Tyz 6.4.2 runtime API, KubeJS Java access policy, the assembled mod set and clean server/client boot still need testing. No compiled mod JAR is checked into Git.
- **No strict Tyz XP-source firewall** yet beyond the chosen Tyz activity config. Later hook SkillXPChangeEvent to block other mod XP grants while permitting legitimate leveling deductions and admin recovery.
- A Tyz XP update and the Overworld SavedData write are not an atomic two-mod database transaction; power-loss between them could require reconciliation. A partial accepted update is measured and the uncredited balance retained.
- Dedup cache is bounded at 1024 transaction IDs; permanent first-completion sets persist without expiration. Requires event-specific unique IDs and same-source attribution.
- Boss rule registry is empty by default; 41+ other magic-source bridges, cooperative ritual/boss participant attribution, and exact Chaos Guardian death tracking are not implemented.
- The command/test hooks are for trusted server operators, not arbitrary client packets. Do not enable artificial grants as normal gameplay rewards.

**Implementation milestone, not approved as next-build integrated or survival-ready.**


## Ars / Iron's / Botania / Neo source hooks (2026-11-14)

**Implemented, not game-tested:** branch `feature/mxp-ars-irons-botania-neo-hooks` adds compiled NeoForge mixins for all four resources. See [source hookup and runtime acceptance matrix](SOURCE-HOOKS-2026-11-14.md). Nothing is installed in the current packwiz client snapshot, and neither a dedicated server nor Prism client has loaded these new native mixins.

**Default baselines** in the generated `tmm_mxp-common.toml`: Ars 100 (5.13.5 baseMax), Iron's 100 (3.16.3 MAX_MANA default), Botania 500,000 (ordinary tablet and mana ring, 2.0.2), Neo Vitae **5,000 EV** (Tier 0 Novicius blood orb's Anima capacity, approved). A source baseline is fixed, not updated as player max mana grows. All registered first-boss rewards are now fixed at 1,000 effective MXP regardless of the configured repeat reward. A special repeated Chaos Guardian payout remains prospective only, awaiting exact entity verification.

**Hook inventory:** MxpArsResolverMixin, MxpAnsArsCommitMixin, MxpIronsEventMixin, MxpIronsCastMixin, MxpBotaniaManaMixin, MxpNeoNetworkMixin. Ars 'n Spells cross-paying spells need a dedicated in-game dedup test. Botania snapshots verify the physical eligible stacks and *actual debit*, not the broader handler's total granted amount.


## Complete future source coverage — design checklist

The full follow-on implementation target inventory is [42/42 MXP magic source candidates](../../docs/MXP-ALL-MAGIC-SOURCE-IMPLEMENTATION-TARGETS.md). It distinguishes real paid personal resources, meaningful manually completed rites/crafts, parent-inherited addons, the shared Ars/Iron bridge, and intentionally excluded standalone sources. It prioritizes Roots, Hex Casting, Occultism's tiered **Book of Binding** completion rewards, Mahou and other missing adapters. **No implementation in this documentation branch.**


## Future JAWR vampire blood and form-tree interface (planning only)

The [complete source target list](../../docs/MXP-ALL-MAGIC-SOURCE-IMPLEMENTATION-TARGETS.md) now reserves **one JAWR-primary 900-blood MXP source** for active blood-expending vampire powers **only after Lilith's Blood upgraded the player to full JAWR vampirism**. Abilities from regular Vampirism can qualify after that upgrade **if the actual debit is from JAWR's authoritative blood pool**. Infection-only Vampirism, hunger upkeep, feeding/refilling, resource synchronization and passive buffs do not earn MXP; never double-credit a mirrored debit. JAWR personal Infusion has an independent TBD baseline. Lich/Dragon progression predominantly belongs to their externally gated Tyz transformation skill trees. **No vampire code hook exists in this branch.**
