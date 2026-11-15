# JAWR cure -> Tyz complete per-form reset and refund

**Owner-approved design target — 2026-11-15.** Target Minecraft 1.21.1 / NeoForge, JAWR 0.5.39, Tyz's Skills 6.4.2, KubeJS 2101.7.2-build.381.

## Locked behavior

1. JAWR's **current native Vampire and Werewolf attachment state** authorizes purchasing form skills. Permanent advancement history, stages, visual forms and player tags alone do not prove a present supernatural condition. If the native state cannot be read, purchases fail closed and **no cure/reset happens**.
2. Any **confirmed loss of JAWR vampirism or lycanthropy**, not merely one specific ritual animation, triggers cleanup of exactly that form's Tyz category. This covers native Remove Curse, Lilith/garlic, commands and new cure paths where they alter the JAWR flag.
3. **Set all purchased ranks in the cured category to zero and refund 100% of actual, still-invested SP**, regardless of the global Tyz refund percentage (30% stock). Return SP to the existing global Tyz wallet; the operation is NOT an MXP award, level gain or new currency.
4. Clear the revocable stage **tmm_vampire_active** or **tmm_werewolf_active** only. Permanent advancements, Core Skills, other supernatural trees, native JAWR advancement and global lifetime MXP survive. Re-infection permits purchase again from rank zero.
5. Repeated observations after a cure must not create further refunds. Normal partial or bulk Tyz refunds reduce the outstanding paid-rank ledger, so their SP cannot be paid again during cure.
6. Server purchase gates check live JAWR state through Tyz's built-in **TyzsSkillsEvents.skill_purchase_pre**; no standalone compatibility mod or Tyz fork needed. Native Tyz category tabs may remain visible despite a lock.
7. The current phase covers **Vampire/Werewolf only**; standalone Monk and Hunter implementations are separate. The 37-ability JAWR/Tyz behavior experiment, draft PR #94, is not required to reset a tree.

## Implemented, but not enabled in the playable pack

- [KubeJS server script](../compat/form-cure/tyzs_form_cure_refund.js): public JAWR getData(Player).isVampire()/isWerewolf() status probes, native-gated purchases, synchronized revocable stages, login and once-per-second monitoring with **two consecutive false readings** to mitigate transient-state flicker.
- On confirmed cure: enumerate Tyz skills and match exact category, remove skill levels via the Tyz API to synchronize and remove attribute modifiers, return actually charged SP using the **addSP(player, amount, false)** overload, reset only that stage.
- Per-rank spending ledger in KubeJS persistent player data: records actual SP deducted from PurchasePre to PurchasePost and accounts for bulk purchasing; ordinary RefundPost removes ledger entries for refunded levels. Direct rank grants (admin / trusted API) and old purchases from before ledger deployment receive **zero assumed SP** rather than manufacturing currency. Migrate old true purchases manually before production.
- If Tyz is absent, script does not register handlers. If JAWR is absent or its classes cannot be accessed, purchases are denied and no form cleanup occurs.
- [Node regression harness](../scripts/test_tyz_form_cure_refund.js) models class locks, healthy purchases, differing rank costs, 100% cure refund, native partial refunds, hybrid isolation, Core Skills retention, admin-granted ranks, transient invalid forms, reinfection, two players and missing dependency behavior. [CI workflow](../.github/workflows/tyzs-form-cure-refund.yml) runs syntax and simulated tests.
- The script is stored under **compat/form-cure**, not **kubejs/server_scripts**. This is intentional: the current full-integration branch does not yet stage the Tyz skills runtime, and **in-world tests have not been performed**. The CI explicitly fails if the script appears accidentally in the live script directory.

## Disposable QA / rollout checklist

1. Assemble a fresh throwaway server/client with Tyz 6.4.2, JAWR 0.5.39 and KubeJS 2101.7.2-build.381. Enable one Tyz native CUSTOM attribute skill in each Vampire and Werewolf category with two or three uneven SP prices. Preserve existing Core Skills. Do **not** install the 37-skill experimental Java addon.
2. Copy the staged KubeJS script to **kubejs/server_scripts/tyzs_form_cure_refund.js** in the disposable instance, reload scripts and inspect the KubeJS server log. Verify Java class access to both pinned JAWR attachment classes and Tyz event handlers. Do not enable on any established save until this succeeds.
3. As a human, attempt a Vampire/Werewolf purchase: no SP charge. Become a JAWR vampire, buy ranks, note SP, cure with Remove Curse and verify after at most a few seconds: ranks zero, full actual SP returned, stage cleared, health/attack modifiers removed, Core Skills unchanged.
4. Repeat on the **Lilith/garlic** vampire cure path and on the native Werewolf cure. Verify separate identities/hybrids lose only the cured tree. Cure must be based on a successful authoritative JAWR state change, not a triggered-but-failed ritual.
5. With Tyz normal refunds at 30%, partially refund ranks before curing; only the remaining paid investment is restored in full. Test a bulk refund, bulk purchase, admin direct rank set, re-infection, repeated cures, relog, death/respawn, dimension travel, max-SP caps and multiplayer isolation.
6. **Known limitation**: removing ranks and adding compensating SP are two different persistent changes, not an atomic database transaction. There is a narrow process-crash window between ledger clearing and SP award. Harden with a once-only refund journal and an operator recovery procedure before deploying to valuable long-lived saves.
7. Verify API method names and overload resolution in the exact pinned server. KubeJS 1.21.1 stages persist as entity tags; do NOT register stageAdded/stageRemoved handlers on this KubeJS release because they are known broken. No Java bytecode or source test is equivalent to this client/server QA.
8. On success, integrate the script through the Tyz runtime activation step and add a real-server cure acceptance workflow. Until then, this is **coded and mock-tested only**, not a shipped gameplay system.

## Source evidence

- [Tyz SkillManager](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillManager.java): server purchase, bulk purchase, normal 30%-default refunds, rank reset, client sync and generic attribute recalculation.
- [Tyz ISpManager](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/api/interfaces/ISpManager.java): SP credit overload with explicit applyLimits boolean.
- [Tyz KubeJS event documentation](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/kubejs/kubejs_events.md): pre/post purchase/refund events; actual pinned 1.21.1 method spelling is getID().
- [KubeJS 1.21.1 Java interop](https://kubejs-wiki.tysontheember.dev/1.21.1/concepts/java-interop/) and [player data/stages](https://kubejs-wiki.tysontheember.dev/1.21.1/guides/player-data/).
- [JAWR binary audit](VAMPIRE-FUSION-AUDIT-2026-11-14.md) and [existing Vampire attachment observer](../compat/transformations/src/main/java/io/github/heatwaveqc/tmm/transformations/adapters/JAWRVampireProbe.java).
