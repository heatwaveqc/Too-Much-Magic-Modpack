# Nov 26 mining QA repair (#805 / #808)

Independent runtime-repair review boundary, one shared Progression engine PR from da45fa2e428efcd39ff1ae7a1d876af6f5b2d9c6; no Path JSON, balance, IDs, pack locks or save migration. #798 clothing edits EffectRuntime/NativeHarvest and must be reconciled with local unarmored prediction if merged; this patch deliberately does not edit those files. #656 touches separate enrollment/Events. Flying Kick unchanged.

## #805
Current server-only break speed hook excludes LocalPlayer, and NeoForge 21.1's Player.getDigSpeed dispatches BreakSpeed after native modifiers. Mirror only current server-derived ordinary break_speed and earned native_harvest/legacy harvest_tier rules using existing owner/dimension/expiry passive snapshot; local block tag checks and equipment gates; no client -> server grant. No arbitrary form/provider native mining prediction, no added 3.33x compensation. Client HarvestCheck and deprecated one-argument Player.hasCorrectToolForDrops predicate are both covered; real loot/tier authority is unchanged. Protocol changes require same Progression JAR both ends. No rank/XP changes.

## #808
Prior verified #223 source notes TreeChop 0.19.3 LevelUtil.harvestBlock uses Block.dropResources, bypassing native Occultism playerDestroy. Reuse the already installed six-argument Block.getDrops wrapper; only two exact natural Occultism log classes, real player attribution, existing server-side sight gate; change only the loot state, never world state or drop count. Preserve native tool scope using original state. Exact TreeChop JAR unavailable here: whether its indirect drop path supplies the responsible player and recovered native log state remains an explicit acceptance dependency. Actorless drops fail closed; no inferred attribution or fake player. No extra loot roll. Native Third Eye handling remains unchanged.

## B/C acceptance (World 1, preserve ranks)
- #805: same original rank, barehand, unarmored, grounded dry, no effects, compare stone/copper/coal/dirt/sand and real tools; immediately after buy, relog, armor/weapon equip, respec/leave. Verify actual drops/XP/incorrect tier no-drop and Jade independently. Check dedicated server and different player ranks. Do not add a speed multiplier merely to mask a missing correct-tool divisor.
- #808: native Otherwood single block and full fell including already chopped log, normal oak, no sight, native potion, claims, mixed tree and XP counts. If indirect loot lacks actor or original state, inspect exact pinned JAR and adapt native TreeChop scope, do not widen harvest rules.
- No full build, smoke or game run authorized. Module compile/test and integration preflight pending.
