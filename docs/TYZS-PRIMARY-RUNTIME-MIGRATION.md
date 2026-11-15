# Primary Tyz tree: runtime migration for next full integration

**Owner decision 2026-11-14:** replace `puffish-skills` with **Tyz's Skills 6.4.2**
in the next rolling, disposable full-integration build. Preserve historical B6/B8
rollback snapshots and **do not remove `puffish-attributes` without a dependency
audit**. `Improvable Skills 3` remains its own vanilla-XP based feature.

## Source ownership

The **one canonical global Tyz wallet/tree** is defined in
`design/universal-mxp-progression-registry`. The preexisting
`feature/tyzs-core-magic-first-wave` contains four executable CUSTOM skill JSONs
with five ranks each, and this branch extends it with the integration activation
and validation scripts. The Monk/Vampirism addon still owns its own one-time
faction powers (e.g. Step Assist), not another copy of the shared magic skills.
Transformation category IDs are reserved for future work; do **not** show empty
form tabs or advertise playable form skills.

The initial Tyz UI must include native `abilities`, `combat`, `misc`,
`nature` and `tmm_magic_core`. Core Magic first-wave skills are Reservoir,
Wellspring, Thrift and Potency, modifying source-verified player attributes
from Ars Nouveau, Iron's, Psi, Forbidden & Arcanus, Goety, Neo Vitae and Eidolon.
They are not separate skill purchases per mod. Other magic compatibility
families and native Tyz combat/nature tuning remain additional waves.

## Activation procedure (CI only; disposable world)

1. Import this branch's `compat/magic/`, `scripts/activate_tyzs.py` and
   `scripts/verify_tyz_migration.py` into `test/full-integration`.
2. The assembler calls `activate_tyzs.activate(root, manifest)`, pins the
   exact upstream **Modrinth project 4rKNK5O2 / version OUmvFAPs**
   (CurseForge file 8118598), checks loader 1.21.1 NeoForge and provider
   SHA-512. No `latest` resolver or unhashed JAR download is acceptable.
3. Stage the previously authored Core Magic config into the pack, preserving
   the four original Tyz tabs. Remove `mods/puffish-skills.pw.toml`, mark
   its manifest row disabled, retain `mods/puffish-attributes.pw.toml`.
4. Regenerate packwiz index hashes, run `scripts/verify_tyz_migration.py`
   and general pack/quest validation, boot a clean dedicated server, package
   the test Prism launcher only on success. No binary third-party JAR in Git.
5. In a disposable client world: verify five tabs, /skills reload, four
   five-rank skills, rank purchases/refunds and attributes, relog persistence,
   no default native Tyz XP from everyday activity, no Pufferfish Skills,
   Improvable Skills still accessible, native Monk one-time root still works.

## Progression and gating safeguards

- One **global Tyz skill-point balance**; never duplicate rank/effect ownership.
- An effective 1,000 MXP grants one Tyz level and 1 SP. Ordinary XP from
  Tyz activity is OFF; do not grant Tyz XP by vanilla mining/crafting/food.
- **The actual MXP earning/normalization dispatcher is a separate unresolved
  implementation**: the first client smoke may award only **explicit admin
  test XP** (`/skills xp <player> add 1000`). This is a *testable UI/rank
  slice*, not a playable progression economy. Never describe as MXP complete.
- For rank II and later, **one** rank of the previous stage satisfies native
  prerequisites; do not require rank 5. External boss/ritual/Monk/form gates
  must use server-side `SkillActionEvent.PurchasePre`, not client visibility.
- Bulk buying invokes the pre-event once at the node boundary. RefundPre must
  protect predecessor ownership while higher nodes remain. Direct
  `setSkillLevel` bypasses purchases; only trusted administrative use is
  allowed until reconciliation is implemented.
- Do not ship future form/advanced-tier purchasable nodes without typed
  eligibility predicates, denial feedback, bulk/refund/direct-grant tests,
  and suspending form-specific effects after cure without deleting ranks.
- The automatic `Thrift` KubeJS adapters and native attributes are still
  subject to in-world verification (including Ars/Iron's shared mana
  avoiding duplicate discounts). They do not prove spell effects work.
- Pufferfish's Attributes remains pinned until every dependent module is
  independently audited. Only the Skills mod is retired in this migration.

## Stock-tree migration and five-rank balance update (2026-11-15)

The four Core Magic base nodes now each have **five** ranks, each representing 20% of that attribute's former rank-99 maximum effect. Five SP now fully purchase a base node, rather than 99 SP. This is a deliberate power-curve acceleration and only suitable for disposable QA until replay/migration handling is verified. See [full 32-skill default inventory and migration plan](TYZS-STOCK-SKILL-MIGRATION-2026-11-15.md). The original Tyz four categories remain present **temporarily**; remove them only after every original skill has been safely placed, behavior-verified and gated in the nine custom categories. Existing 6+ ranks must not be silently clipped in retained world data.

## Acceptance status

- Source-backed: exact release IDs/loader, 4x5 JSON preview, native XP-disable
  config, conditional PurchasePre/RefundPre API, one-rank prerequisite semantics.
- CI will enforce: exclusive Skills backend, correct categories, exact hash,
  static JSON/XP checks, real server boot, and correct Prism contents.
- Unverified until player QA: GUI, actual rank effects, modified spell costs,
  no XP leaks, multi-player, high-rank limits, form gates and universal MXP
  awards. Never automatically promote a rolling QA build to stable.

Primary contracts: `docs/TYZS-MXP-EXTERNAL-GATES.md`,
`docs/TYZS-CORE-MAGIC-AND-FORM-TREES.md` on
`design/universal-mxp-progression-registry` and
`compat/magic/README.md` in this branch.
