# Progression — permissive donor work inventory (2026-11-18)

Owner decision: **Epiphany primary UI and architectural donor**, existing Progression/MXP authoritative, Tyz source imports blocked pending owner contact. No new required dependencies. This is an initial isolated source batch, not live gameplay or a complete merge of the six donor mods. Preserve all native features and the ten-tree plan. Directly adapted code has headers, immutable provenance and all original notices; source selection is not a claim of compiled/runtime parity.

## Current scope (supersedes historical donor inventory)

Owner decision 2026-11-18: only innate/automatic/form-granted powers and player-owned skill-tree unlocks/ranks. Installation/research is a flavor of skill acquisition, never an independent removable augment. Keep configured respec and activation toggles; no equipment-owned effects, gem/perk sockets, random item affixes, removable augments, transferable tokens, trading or PvP loss. Equipment may condition a player-owned skill; other installed mods' equipment systems are unaffected. Supersedes full donor ownership parity. Retain donor licenses and useful effect algorithms; unused AugmentState foundation remains inactive and is not an obligation to implement augmentation ownership.

| Original donor work | Revised status |
|---|---|
| Oritech install/uninstall lifecycle | Only research/prerequisite and one-time unlock patterns in scope; independent augment state unused |
| Everlasting transfer/token storage | CANCELED; retain magnet, passive/ranked and proc algorithms |
| Apotheosis item affixes, rarity, sockets, facets | CANCELED; retain attribute caps and relevant player effects |
| Astral perk sockets | CANCELED; retain graph/attunement as player progression reference |
| Legacy save conversion | Not required before stable release |

The ledger below records historical survey evidence, not authorization to develop excluded systems.

## Import and development ledger

Status: ADAPTED = code staged under foundation (not connected to live paths); REUSE = existing first-party code; PORT = source identified, dependency/semantics adaptation needed; NEW = TMM-specific design/engineering. More donor features need exact per-file audits.

| Capability | Source | Current delivery | Remaining |
|---|---|---|---|
| Logical AND/OR/NOT, numeric predicates | Epiphany | ADAPTED: Condition, Comparison | Typed codec/registry with strict load limits, event context, provider-missing semantics; wire acquisition and effect-time filters separately |
| Stable reward ownership | Epiphany | ADAPTED: RewardSource | Versioned ledger owner identity, transient modifier reconciliation, migration, typed reward registry, lifetime tests |
| Centered branch layout and orthogonal connectors | Epiphany | ADAPTED: TreeLayout | Actual vanilla Screen renderer, panning/zoom, hover/click, module cards, node state, icons/localization, explicit AND/OR/choice visualization, server snapshots |
| Dimension, biome ID/tag, advancement, effect, attribute predicates | Epiphany | ADAPTED: PlayerConditions | Typed registry/codec, live tests and acquisition/effect-time schema integration; absent registry definitions return UNAVAILABLE |
| Item, player statistics, structure, quest/stage, native tree/points/XP predicates | Epiphany | PORT | Extract individual leaves, replace Epiphany-specific state checks with Progression ledger; optional FTB/KubeJS adapters |
| Modular selection, insight rewards, persistent reapply | Epiphany | PORT | Map to existing rank+choice+effect owner model; never blindly reapply one-time rewards; idempotent reversible effects |
| UI controllers and top bar, insight select | Epiphany | PORT | LDLib2 is coupled to upstream UI; adapt presentation to existing vanilla ProgressionScreen, not wholesale import. No upstream assets imported |
| Native ranks, gates, activities/milestones, toggles, commands/network | first-party Progression | REUSE | Generalize; validate/authorize packet operations and data reload |
| MXP economy, source debit and deduplication | first-party MXP | REUSE | Internalize wallet; replace bridge; save migration/anti-double award; resource hooks not all complete |
| Installation/enable/disable | Oritech CC0 | ADAPTED: AugmentState | Research prerequisites, timed/costed jobs, transaction, save lifecycle, toggle sync and refund policy |
| Area item attraction | Everlasting Abilities MIT | ADAPTED: MagnetMotion | Bounded selection cadence, ownership/pickup delays, claim safety, filters, XP-orb option; no active magnet |
| Transferable ability tokens, auras, flight, auto fertilization, power stare | Everlasting Abilities | PORT | Replace CyclopsCore, define progression item metadata, transfer/PvP/death rules, bounded area effects, flight gating |
| Attribute suppression | Apotheosis MIT | ADAPTED: AttributeCap (only clamp function) | Explicit effect scope, sprint/base calculation, reconciliation, no blanket suppression |
| Random affix selection, rarity, rolls, sockets, facets, special gem procs | Apotheosis | PORT | Extract without Placebo/Apoth registries; player vs item ownership, rolls, uniqueness, equip/unequip, socket transactions; do not duplicate actual Apotheosis item behavior or assets |
| Improved night vision | Just Dire Things MIT | ADAPTED: VisionMath lightmap policy | Mixin targets and client registration, authenticated synced grant, shader/fog precedence; no live mixin |
| Lava fog | Create MIT code | ADAPTED: VisionMath, client LavaFogHook | Client subscriber with authenticated synchronized eligibility; 20-block reference not live config; overlay/shader tests |
| All rank purchases, per-rank scaling/cost/refund | first-party + Epiphany | REUSE/NEW | One MXP/SP ledger with fixed cost records, 10 trees, graph + ranks; native technique points distinct |
| Tyz bespoke 32 stock skill effects | Tyz pending | BLOCKED for import | Existing Tyz integration remains; already-permissive substitutes where appropriate; otherwise implement independently or wait for permission |
| Attunement traversal, socketed perks, unique Astral mechanics | Astral Sorcery restricted | NEW / reference only | Independent implementation leveraging Epiphany graph and Apotheosis sockets; no Astral code/assets |
| Transformation authority + mixed-form gates/cures | first-party + provider APIs | NEW / partial REUSE | JAWR, Goety, Dragon Survival, Gilded, Monk/Hunter gates; shared Lich once; hybrid eligibility and stacking |
| Universal capacity/regen/efficiency/potency/speed/range/duration and summons | first-party scripts/adapters | NEW / partial REUSE | Per-school resource adapters, caps, actual spend, source-specific tests |
| All actual ten skill-tree content, prices, progress gates and balance | TMM | NEW | Design and QA in separate data PRs, no new balance values in this batch |

## Scope boundaries and verification
Only small, dependency-light algorithms and five player predicate factories adapted now, including centered layout, comparison/logic, identity, lifecycle, attraction, cap and vision. No active hook registration, GUI replacement, permission grants, skills, gameplay change, or external mod installs. JDT and Create client code must not be loaded by dedicated servers. Condition missing-data semantics are three-valued; explicit satisfied OR may succeed despite unavailable alternate; NOT of unavailable stays unavailable. Layout uses explicit edges instead of Epiphany's inferred layer prerequisites. No donor source import from Tyz/Astral/Pufferfish. No original assets copied. No whole-modded JAR reused. Source versions pinned in THIRD_PARTY_NOTICES and original license files.
 
All first-party Progression MIT license files/metadata are reconciled from PR #341; include full donor licenses in distribution. Integration writer must preserve added third_party_licenses and notices when reconciling #341; do not overwrite with its earlier initial notice. Unit tests cover pure primitives; CI and in-world proof remain pending. No pack manifest promotion or full build approved. Progression UI remains current until actual renderer is connected.

PlayerConditions uses Minecraft APIs for dimension/biome/advancement/status/attribute lookup; those calls have not been compiled or runtime-verified in this connector session. No player predicate is active in shipped path data. Verify negative tests (missing registry IDs, absent dimension, unsupported player attribute, absent positive status, nested NOT) before connecting. Advancement `getOrStartProgress` mirrors the donor's vanilla lookup and may initialize an internal progress tracker; it never awards the advancement.

## Remaining effort — corrected reuse-first assessment (2026-11-18)

Estimate **incremental engineering**, not complexity of finished system or amount of QA. The table assumes existing server-owned Progression and MXP persistence and APIs are retained or adapted, not replaced. Estimates are relative and source-level, not person-days or evidence of live readiness.

| Work | Incremental engineering | Reuse / actual delta |
|---|---|---|
| Player ability ownership and persistence | Low | Existing Progression Track/Ledger and copy-on-death ProgressionAttachments; Epiphany PlayerEpiphanyData codec, optional save fields, reward receipts; Everlasting Abilities tag-backed ability stores. Add ownership kinds/source mapping as needed, no new general persistence engine. |
| MXP consolidation and rank purchases | Low | Move existing MxpService/MxpMath/MxpSavedData and retain old persisted file name/keys; redirect delivery from TyzBridge to internal wallet; adapt existing Progression purchase/refund path. |
| Save compatibility/migration | Low–moderate, conditional | Preserve namespace, attachment IDs, existing rank IDs and SavedData filename/schema where possible. Add version defaults/explicit migration receipts (Epiphany compatibility fields are a useful pattern). Mapping Tyz-specific purchases, unspent SP, already-delivered versus pending MXP is a separate one-time import; verify public API/export before removing Tyz. No generic migration rewrite. |
| Form and hybrid eligibility | Low | Transformations StateSnapshot and FormResolver already classify native forms; JawrFormState reads authoritative JAWR attachments. Expose verified flags and provider subtype (dragon type), native discipline flags, OR for both lich routes. Purchase and active checks; unknown denies affected grants, cure vs suspension distinct. Missing provider probes may require separate adapter fixes; transformation rituals/physiology are not skill engine scope. |
| Event dispatch and generic effects | Low–moderate | Existing EffectHandlers interface dispatches tick, damage, harvest, kill, interaction, drops, projectile, equipment, jump/fall, etc. Epiphany AptitudeGainListener covers additional activity events. Wire general conditions and reuse these hooks rather than reimplement events. New specific mechanics remain separate. Use verified committed events, not donor pre-break XP hooks. |
| Rank/stacking rules and native mastery | Low | Track, current purchase/rank math and existing effect scaling; add explicit ownership IDs, additive/multiplicative policy; preserve native discipline advancement separately. |
| Epiphany-style UI | Moderate (port) | Layout already staged, existing server menu/network, donor controllers LDLib2-dependent. Adapt presentation and controls, no new authority. |
| Skill-tree data implementation | Low per skill using registered effects | Data-only node/rank/cost/gates/description/graph. Separate high-effort game-design/balance/test work; new primitive is a handler task, not a skill-tree task. |
| Reference parity (augments, transferable tokens, sockets, affixes) | Low–moderate per feature, aggregate moderate | Port Oritech, Everlasting Abilities and Apotheosis mechanics; join shared state/transactions; Astral behavior independently expressed with existing primitives. |
| Cross-mod magic resource adapters | Moderate–high | Reuse already-supported attributes/debit hooks. Resource systems that lack public interfaces require individual source audits and runtime hooks. This is the largest meaningful bespoke engineering risk. |
| Entire ten-tree roster, balance | High design/QA, low routine implementation | Not a demand to invent ten engines. Separate data design, tuning and playtest cycles. |

Source evidence: existing `compat/progression/src/main/java/io/github/heatwaveqc/tyr/progression/state/Track.java`, `ProgressionAttachments.java`, `runtime/EffectHandlers.java`, `runtime/forms/JawrFormState.java`; existing `compat/mxp/.../MxpSavedData.java` (tmm_mxp_players_v1), `TyzBridge.java`; `compat/transformations/.../core/StateSnapshot.java` and `FormResolver.java`. Epiphany pinned revision as recorded in notices: `attachment/PlayerEpiphanyData.java`, `listener/AptitudeGainListener.java`, `listener/EffectReapplier.java`, `content/reward/PersistentReward.java`. Everlasting Abilities same recorded revision: `api/capability/CompoundTagMutableAbilityStore.java`. These are source-backed estimates, not proof of runtime behavior. Keep one source of truth; do not import duplicate ledgers or invent migration work when storage identity can be preserved. No production implementation changes in this reassessment.
