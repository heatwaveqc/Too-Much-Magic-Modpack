# Unified Progression implementation roadmap
Updated 2026-11-18. Owner requested durable batch estimates before Batch 5. This is a working estimate, not a release promise. Maintain this file after each tranche; preserve branch, exact revision, checks, and blockers. See NAMED-XP-INTEGRATION-BATCHES-1-4.md and PERMISSIVE-DONOR-INVENTORY.md. One engine, data-only paths, no fixed XP-system count, no pre-stable save migration, no unsolicited full smoke. PR #347 stacked on donor foundation #344; reconcile #340/#341 with integration writer.
 
| Batch | Estimated scope | Relative effort | Status at checkpoint |
|---|---|---|---|
| 1 | Independent named XP registries, wallets, per-node currency and gate, exact refunds | Medium | Implemented; source CI passed at ccdd123 |
| 2 | Absorb MXP hooks, growth/dedup, configuration and APIs; cutover later | Medium | Source port; CI passed; legacy guard remains |
| 3 | Generalize existing Monk/Hunter hooks, enrollment, computed XP, mutation state | Large | Partial; 5569fce follow-up needs CI |
| 4 | Epiphany-inspired graph UI, snapshots, purchase/respec, named balances | Medium | First implementation; CI passed; client QA pending |
| 5 | Reusable mechanical effects from permissive donors and independent code: bounded magnet, caps, status/reaction/area primitives, additional conditions, common skill effects; activate donor vision hooks if feasible | Large (2–4 source slices) | Starting |
| 6 | Expand optional magic adapters (pool/cost/regen/potency/other cast properties), provider form conditions, dietary gates; Psi and JAWR debit verification | Large (2–4 slices) | Planned |
| 7 | Oritech-style research/install/uninstall, toggles and Everlasting-style rank ownership, collectible/transferable abilities; generic owner lifecycle and cost transactions | Very large (3–5 slices) | Planned |
| 8 | Apotheosis/Astral-style sockets, weighted affixes/rarity/facets and gear/perk ownership; auditable rolls and transactional changes | Very large (3–5 slices) | Planned |
| 9 | Complete Core and individual transformation/path tree definitions; import approved content/configs, curve/stacking balance, one shared Lich tree; parallel config-only work where engine supports it | Very large, content-dependent | Planned |
| 10 | Cutover from legacy Tyz and MXP; source and license audit, integrate manifests/scripts, disposable server/client progression QA, balance/regression and release review | Large; runtime QA dependent | Planned |
 
Estimates are relative implementation effort, not elapsed time or an assertion that any batch can be completed in one chat. Batch 5 prioritizes existing attributed foundations and separately verified upstream code; Tyz imports remain blocked pending license clarification, Astral code/assets excluded. Do not introduce donor mods as required dependencies. Do not make new skill purchases obtainable until handlers and actual tree design are approved. Preserve existing gameplay values. Use inactive fixtures/tests for new mechanics. Treat missing provider flags as unavailable. Keep bounded event and area work; no global suppression of third-party effects, no duplicate awards. 
 
## Recovery checkpoint before batch 5
- Branch: feature/progression-channel-integration-2026-11-18; PR #347; parent revision b95b7413b57560970ab58400499e6aa6bae79696.
- Verified prior head ccdd123877d0737ddbe105a1b3bd5269679a9c7c: three relevant workflows passed. Later mutation/eligibility changes require separate verification.
- Next: inspect EffectHandlers, EffectRuntime, ProgressionEvents, donor inventory and foundation files. Implement and test in independently reviewable commits. Update this roadmap with actual results rather than treating source compilation as gameplay acceptance.
