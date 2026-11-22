# Personal valuation milestones — engine slice for #237

**Status:** server-owned persistent milestone service and client entitlement projection in the existing Progression module. Not complete ProjectE/Project Expansion/Replication tooltip functionality or a Minecraft gameplay PASS.

**Source:** isolated feature branch feature/progression-tooltip-milestones-237-20261121 based on dev/progression@dffd83decb2c4e0aab817ba84549df80855b69ba (post rename and OwnedWear fix). Changes ONLY compat/progression paths, the designated engine owner. No new wallet, path, Newbie mode or MXP coupling.

## Code-ready / implementation slices

- [x] Immutable two-bit TooltipMilestones state with independent ProjectE and Replication discoveries; strict monotonic, idempotent transitions, Codec attachment, copyOnDeath, explicit nondeath clone copy.
- [x] First possession of the precise registry IDs projecte:philosophers_stone and replication:replica_ingot through server player inventory, offhand/armor/Ender Chest inspection and crafting event. Handles trading, gifts, existing migrated inventory and independent Newbie behavior. Scan every server tick until both are unlocked, then sync every 20. No recipe advancement false positives; loss/death never relocks. Creative/manual acquisition is not a security concern under owner policy.
- [x] Two-boolean one-way server -> client network. Forced authoritative sync on login/respawn/dimension change and unlock, plus periodic renewal; clear server caches on logout. Client binds to actual LocalPlayer object, fails closed without timely server packet, and cannot send an unlock request.
- [x] Pure presentation policy: numeric ProjectE item/stack EMC requires Stone unlocked AND current native knowledge AND native display setting. Learned marker requires Stone and native preference; Replication value requires Replica Ingot and preference. Native Divining Rod chat readouts explicitly exempt, including before Stone. Never creates parallel knowledge state.
- [x] Pure JUnit regressions for independent ordering, permanence, ProjectE learned-only numeric gate, native preferences, Replication independence and rod exemption.
- [ ] ProjectE native adapter: verify pinned tooltip insertion/knowledge methods, selectively suppress numeric item and stack values, learned marker and applicable stored EMC before Stone, leave unrelated text and Divining Rod chat alone.
- [ ] Project Expansion 1.0.6 adapter: verify LOW-priority tooltip listener and prevent independent learned/value line leakage without stripping Alchemical Collection state.
- [ ] Replication 1.2.7 adapter: exact RenderTooltipEvent.GatherComponents and TerminalScreen provider source audit; suppress pre-Ingot numeric Matter component AND Shift hint, honor native settings after unlock; preserve machine functionality.
- [ ] Audit JEI, Jade and per-machine stored-value overlays; tests require exact pinned JARs and in-game source behavior. Do not globally disable emcToolTips or alter actual resource mappings.

## Design blockers and engineering limits

**No unresolved owner choice for the two milestones.** The issue approved first Stone including trade, first Replica Ingot and learned-only post-Stone numerical display; pre-Stone native Divining Rod messages are an explicit exception. Exact provider adapters and UI event ordering are technical work, not new balance decisions. This foundation alone does not yet hide text in the live tooltip.

## B/C in-game QA (not executed)

- [ ] B: attachment Codec, registered module, server sync login/respawn/dimension/relog, absence of optional providers.
- [ ] C: simultaneous players with distinct milestones, craft/trade/gift/fishing, Newbie, death/loss, relog/new world, migrated inventory.
- [ ] C: ProjectE learned/unlearned/forgot/relearned, stack values and Shift; Project Expansion independent marker; native pre-Stone Divining Rod modes and values; Replication Shift/terminal/matter; JEI/Jade and other tooltip text preservation.
- [ ] C: native conversions and machine operation unaffected; no cross-player leak or client-supplied unlock.

**Test boundary:** Java 21 standalone Progression quick CI, offline unit tests; no Integration Assembly, server smoke or Prism/gameplay run authorized or performed. Progression writer reviews/merges to dev/progression; separate integration writer promotes exact source once coherent.
