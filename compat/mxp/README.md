# TMM MXP — initial NeoForge ledger and award dispatcher

**Stage:** feature/mxp-ledger-dispatcher, 2026-11-14. Actual Java source, build and progression tests exist; **not yet packwiz-pinned, game-launched or verified with the Tyz 6.4.2 runtime JAR**. Designed not to delay the next full test build. Based on the Core Magic feature branch; reconcile with [MXP specification PR #27](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/27) and [Core Magic implementation PR #33](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/33).

## Implemented

- Standalone **tmm_mxp** NeoForge 1.21.1 / Java 21 module under compat/mxp. Tyz's Skills 6.4.2 is declared a required runtime dependency; its source-verified public API is called reflectively so the third-party JAR need not be redistributed.
- Pure Java progression math: base = **1000 × nominalCost / first-access resource pool B**; ordinary effective XP = base / (1.2^P), with the divisor **recomputed mid-award** at each 1000-effective-XP point boundary. No vanilla XP spent.
- First successful ritual and first boss victory are **undivided**, repeat rituals/bosses are divided. An explicit boss rule may mark repeat victories as fixed (Chaos Guardian **proposed** 100 MXP repeat, not activated). First-time magical crafts are **still divided**.
- Overworld SavedData by player UUID persists lifetime MXP-earned levels P, effective progress, undelivered fractional XP, discovered ritual/boss/craft IDs, and the latest 1024 transaction IDs. Respec/death/dimension changes cannot reset the earned lifetime divisor.
- Tyz public API bridge measures **actual** Tyz level/XP change after the award. It stores sub-0.01 fractional awards until deliverable, and retains undelivered portions of partially accepted awards.
- Public server integration API via MxpService.resource, ritual, craft, boss and inspect. Resource awards require both nominal pre-efficiency cost and **positive actual debit after successful player action**.
- Generic LivingDeathEvent handler awards only the **server player with the final blow** for explicitly registered boss IDs. **Boss rule registry empty by default**; no invented Chaos Guardian IDs or unapproved rewards are live.
- Operator diagnostic commands: /tmmmxp status, /tmmmxp test ordinary <base>, /tmmmxp test fixed <amount>. Only diagnostics require operator permission 2.
- Source-backed **Psi 110** KubeJS pilot lives in pilot/kubejs/server_scripts; preSpellCast captures compiled nominal cost, spellCast verifies actual Psi/CAD drain, then calls MxpService.resource with **first-access player Psi pool 5000**. It is not shipped into packwiz.

## Source integration contract

Call MxpService.resource(player, sourceId, transactionId, nominalCost, firstAccessPool, actualDebit) **after** an accepted and committed magical action. Transaction IDs must uniquely identify the world operation, and should be common across two adapters observing the **same cast**. No XP for previews, simulation, transfers, idle mana generators, cost-free casts or machine maintenance. Shared Ars/Iron mana is **one logical transaction** and should not be counted twice. Positive regen and efficiency effects still improve sustainable nominal MXP earned.

Call MxpService.ritual(player, id, completionTxId, firstFixed, repeatDivided) on confirmed ceremony completion, not on every ritual tick. Call MxpService.craft similarly, but **both** first and later craft awards are divided. Boss rules use MxpService.registerBossRule(canonicalEntityId, first, repeat, fixedRepeat), then the native final-death hook handles configured entities. Example rules in pilot/kubejs/server_scripts/tmm_mxp_boss_rules_example.js are disabled, with purely illustrative amounts.

The core does NOT store mod resource baselines for all 42 entries yet; the per-source adapter currently supplies a verified reference B. See [per-mod registry](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/design/universal-mxp-progression-registry/docs/MAGIC-MXP-SOURCE-REGISTRY.md).

## Deliberately deferred automatic sources

- **Neo Vitae AnimaEvent.PostSyphon:** owner UUID, nominal ticket and actual EV are exposed, but **AnimaTicket has only an integer amount, no activity provenance**. A blanket callback would credit machine transfers and passive drains. Require proven player-activated sigil/action context first.
- **Ars Nouveau/Iron's:** spell events expose prepayment data, but the shared Ars 'n Spells bridge can redirect/refund the actual debit. XP must be granted at a verified committed payment, not when Thrift prices the cast.
- **Botania Revived:** centralized item mana requests include availability simulation, cross-funding via Ars, item transfers and armor/weapon upkeep; classify the actual activation before awarding.
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
