# Goety × Goety Cataclysm — staged effects and cast payment

| Field | Value |
| --- | --- |
| Participants | Goety 3.2.2; Goety Cataclysm locked 1.21.1-1.8.4 artifact |
| Profiles | [Goety](../mods/goety.md), [Goety Cataclysm](../mods/goety-cataclysm.md) |
| Research base | `cb8e2dcab59cc625500b3ca0329df1511823610b`; inspected 2026-11-16 |
| Status | **source-supported ordering risk; untested in TMM** |
| Mechanism | Native addon spell callbacks through pinned parent Dark Wand; no proposed resource bridge |
| Stage | Optional advanced combat; research/recipe prerequisites still apply |

## Why this matters

Battlefield, Storm Serpent and Thunder Rage are sustained, visibly staged attacks rather than one projectile emitted after windup. Their effects need to be taught as sequences. The parent wand's normal payment happens on completion, while these addon classes emit effects earlier in `useSpell`. Releasing early can therefore leave emitted entities or world effects while avoiding the normal completion debit in the inspected native path. Cancellation can still impose cooldown. This is a testable transaction boundary, not a measured exploit rate or proof that current pack hooks permit it.

## Both sides of the contract

`Focus item in Goety wand → parent start checks personal Soul Energy availability → per-use-tick addon callback emits entities/forces → optional release callback → parent completion debit only if normal completion occurs`

| Step | Typed state / operation | Payment, output and evidence |
| --- | --- | --- |
| Start | Player + wand ItemStack + contained focus + personal/portable Soul Energy availability | `DarkWand.use` checks sufficient souls before `startUsingItem`; the inspected non-instant start path does not decrease them. |
| While casting | `castTime` game ticks → `GoetyEventFactory.onCastingSpell` → `ISpell.useSpell` | `DarkWand.onUseTick` dispatches addon stage effects before eventual `finishUsingItem`. These three extend ordinary `Spell`, not charging/breath `EverChargeSpell`. |
| Completion | Parent `finishUsingItem` → `MagicResults` → `SEHelper.decreaseSouls(SoulUse)` → `SpellResult` → cooldown | Ordinary survival completion pays before final result. Having no final addon `SpellResult` override does not prevent paying at completion. Cached/modified SoulUse, attributes, gear and event hooks can change effective cost. |
| Release | Parent `releaseUsing` → addon `stopSpell` | Non-charging release does not call `MagicResults` in this inspected method; each addon handler below adds cooldown at a time threshold but does not debit Soul Energy or revoke previously spawned entities. |

Exact artifact evidence: parent SHA-512 `d68103012faad1b74cf96017aa22711d80033d04035e98e5452f4f70d6fbc30c6523b4d3f561fedba798e116055caf598432b472b13e8a11dca07d00468b8b46`; addon SHA-512 `45e390fb44ea7aa6b5f81b95b57ca224489a527a66167de1bdcf9a53c71cb5a54f6d29b69fa30b3f342e853c30bee677cf2c3701d141edf76b0be8515b6b6b60`. Both match packwiz. Paths below are class paths in those artifacts, inspected through transient CFR output, not claims of recompilation equivalence.

## Four staged paths with different cancellation handling

| Registered focus / class | Effect timing in use callback | Default completion / raw Soul cost | Cancellation boundary |
| --- | --- | --- | --- |
| `goety_cataclysm:abyssal_beam_focus`; `.../abyss/AbyssalBeamSpell.class` plus `entities/util/AbyssMark.class` | `startSpell` creates a mark during the first cast tick. Mark uses configured lifespan80 before emitting AbyssBlastPortal(s); full spell duration is80+10=90. | Raw cost32; duration90 fixed by override; raw cooldown400 | Mark discards when player is no longer spellcasting, so immediate cancellation is guarded. Test release after portal emission but before90: portal has its own lifetime/laser emission and no cast-state check in inspected tick path. No addon `stopSpell` override was found for this spell. |
| `goety_cataclysm:battlefield_focus`; `common/magic/spells/necromancy/BattlefieldSpell.class` | Standing: halberd waves at even `castTime` values from 44 through `44 + howFar + radius`, with `howFar` 9 or 14 for matching staff. Crouching: windmill halberds at finalCount−2. Geometry must find valid ground. | `castDuration` hardcoded 60 ticks; raw cost 128 | `stopSpell` adds cooldown if castTime≥37 and focus nonempty. Cancelling after waves but before 60 is the payment boundary; excessive Radius may schedule some effects after the fixed cast window. |
| `goety_cataclysm:storm_serpent_focus`; `.../storm/StormSerpentSpell.class` | Two StormSerpent entities at tick 28; matching staff adds two at tick 40. Aimed location/target is resolved for each emission. | `castDuration` hardcoded 75 ticks; raw cost 128 | Release at/after28 adds cooldown; no explicit soul debit or emitted-entity cleanup in that handler. Compare completed75 with release after29 and after41. |
| `goety_cataclysm:thunder_rage_focus`; `.../storm/ThunderRageSpell.class` | Knockback during ticks56–114, then Water Spear and Lightning Spear emission at115. Matching staff changes lightning behavior. | `castDuration` hardcoded 150 ticks; raw cost64 | Release at/after115 adds cooldown. The earlier knockback window is before that handler's cooldown threshold; payment/cooldown consequences of release at60 need independent testing. |

The classes' raw default cooldowns are Abyssal Beam400, Battlefield250, Storm Serpent350 and Thunder Rage350 game ticks. Effective cooldown is scaled through the parent interface/attributes. These numbers are not throughput measurements. No promise is made about damage, entity lifetime, armor, friendly fire, block claims or boss immunity until delegated projectile/hit implementations and runtime are checked.

## Existing support versus proposed work

The registered focus items, class mappings and parent dispatcher are present in the hash-matched pack artifacts. No new item/fluid/FE resource bridge is involved. This report implements **nothing**. A fix, if the behavior is reproduced and rejected by the owner, would need a transaction design covering both early effects and late completion: charging once at a validated stage, tracking that payment, preventing duplicate completion charge, and deciding cancellation/refund policy. No safe generic config-only fix has been established; changing recipe cost or raw Soul cost alone does not repair event order. Do not add ad-hoc KubeJS charging or disable a spell during research.

## Reproducible disposable-world tests — not run

Use survival mode, a fixed plain wand/focus, no robes/enchantments/discount attributes, known initial Soul Energy and empty cooldown. Record exact build, world, config and active event adapters. Creative mode bypasses the relevant payment and is not a valid economics test.

| Case | Action | Separate observations |
| --- | --- | --- |
| Positive completed cast | Finish each spell through its hardcoded duration with sufficient souls | Soul delta, emitted entity count/timing, effective cooldown, actual target damage/knockback; note completion versus first visible effect. |
| Early release before effects | Release before first stage | No emitted stage entity/effect; capture Soul delta and cooldown rather than assuming refund. |
| Abyssal Beam boundary | Release shortly after mark creation; separately release after the mark emits a portal but before full90-tick completion | Early mark cleanup versus emitted portal/laser persistence, actual Soul delta and cooldown. Exact tick alignment must be observed; do not treat the mark itself as damage. |
| Storm Serpent boundary | Release after first emission but before75, and repeat with matching staff after40 | Whether serpents persist/attack, actual Soul delta, cooldown; no assumption that a visible serpent proves damage. |
| Battlefield boundary | Release after a standing wave before60; test crouching variant and a Radius scheduling boundary | Existing halberds versus full completion, ground failure, Soul delta, cooldown and missed late geometry. |
| Thunder Rage boundary | Release during early knockback (e.g.60) and after115 before150 | Forces and projectile output versus Soul delta and cooldown; repeat near allies, crouching entity and protected test area. |
| Insufficient resource | Below effective Soul threshold at start; separately remove charge after start before final completion | Start rejected versus stage effects already emitted, remaining resource and final result/cooldown. |
| Pack hook boundary | Same cases with current TMM first-party adapters/config and another player | Native expectation versus real event replacement/cancellation, team and faction handling. |

## Quest guidance and recommendation

Teach the Black Book's named focus page and JEI craft route, the full hold-to-complete sequence, aiming and a safe practice target. A focus inventory task cannot prove a cast or payment; use an honestly labeled manual operation task until a supported detector is verified. A proposed lesson gets positive XP and modest ordinary supplies after reward-policy review, without granting the gated focus or recipe drop. This ordering concern is not an exploit tutorial to put into the player's questbook.

**Disposition: document and test native behavior before any implementation.** Keep the optional advanced branch source-backed but unaccepted until both effect and economics boundaries are observed. Remaining work: effective SoulUse/attribute caching, event adapters, delegated hit filtering/damage and server/client cancellation synchronization.

## Revision notes

- 2026-11-16 — Added two-sided exact-JAR callback/payment analysis and distinct positive/release-boundary tests for the staged focuses. No live tests or runtime changes.

- 2026-11-16 — Extended the report to Abyssal Beam after tracing AbyssMark and AbyssBlastPortal: the mark has a spellcasting cancellation guard, but portal emission precedes the full cast window and portal tick uses an independent lifetime. Added separate early-mark and post-emission boundaries.
