# Progression — permissive donor work inventory (2026-11-18)

Owner decision: **Epiphany primary UI and architectural donor**, existing Progression/MXP authoritative, Tyz source imports blocked pending owner contact. No new required dependencies. This is an initial isolated source batch, not live gameplay or a complete merge of the six donor mods. Preserve all native features and the ten-tree plan. Directly adapted code has headers, immutable provenance and all original notices; source selection is not a claim of compiled/runtime parity.

## Import and development ledger

Status: ADAPTED = code staged under foundation (not connected to live paths); REUSE = existing first-party code; PORT = source identified, dependency/semantics adaptation needed; NEW = TMM-specific design/engineering. More donor features need exact per-file audits.

| Capability | Source | Current delivery | Remaining |
|---|---|---|---|
| Logical AND/OR/NOT, numeric predicates | Epiphany | ADAPTED: Condition, Comparison | Typed codec/registry with strict load limits, event context, provider-missing semantics; wire acquisition and effect-time filters separately |
| Stable reward ownership | Epiphany | ADAPTED: RewardSource | Versioned ledger owner identity, transient modifier reconciliation, migration, typed reward registry, lifetime tests |
| Centered branch layout and orthogonal connectors | Epiphany | ADAPTED: TreeLayout | Actual vanilla Screen renderer, panning/zoom, hover/click, module cards, node state, icons/localization, explicit AND/OR/choice visualization, server snapshots |
| Conditions (dimension, biome tag, advancement, item, status, attribute, statistics, structure, quest/stage) | Epiphany | PORT | Extract typed individual leaves; optional FTB/KubeJS integrations; missing or unloaded registry data must not bypass gating |
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
Only small, dependency-light algorithms adapted now, including centered layout, comparison/logic, identity, lifecycle, attraction, cap and vision. No active hook registration, GUI replacement, permission grants, skills, gameplay change, or external mod installs. JDT and Create client code must not be loaded by dedicated servers. Condition missing-data semantics are three-valued; explicit satisfied OR may succeed despite unavailable alternate; NOT of unavailable stays unavailable. Layout uses explicit edges instead of Epiphany's inferred layer prerequisites. No donor source import from Tyz/Astral/Pufferfish. No original assets copied. No whole-modded JAR reused. Source versions pinned in THIRD_PARTY_NOTICES and original license files.
 
All first-party Progression MIT license files/metadata are reconciled from PR #341; include full donor licenses in distribution. Integration writer must preserve added third_party_licenses and notices when reconciling #341; do not overwrite with its earlier initial notice. Unit tests cover pure primitives; CI and in-world proof remain pending. No pack manifest promotion or full build approved. Progression UI remains current until actual renderer is connected.
