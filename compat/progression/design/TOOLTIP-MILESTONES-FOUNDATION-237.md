# Personal valuation milestones — engine slice for #237

**Status:** server-owned persistent milestone service and client entitlement projection in the existing Progression module. Not complete ProjectE/Project Expansion/Replication tooltip functionality or a Minecraft gameplay PASS.

**Source:** isolated feature branch feature/progression-tooltip-milestones-237-20261121 based on dev/progression@dffd83decb2c4e0aab817ba84549df80855b69ba (post rename and OwnedWear fix). Changes ONLY compat/progression paths, the designated engine owner. No new wallet, path, Newbie mode or MXP coupling.

## Code-ready / implementation slices

- [x] Immutable two-bit TooltipMilestones state with independent ProjectE and Replication discoveries; strict monotonic, idempotent transitions, Codec attachment, copyOnDeath, explicit nondeath clone copy.
- [x] First possession of the precise registry IDs projecte:philosophers_stone and replication:replica_ingot through server player inventory, offhand/armor/Ender Chest inspection and crafting event. Handles trading, gifts, existing migrated inventory and independent Newbie behavior. Scan every server tick until both are unlocked, then sync every 20. No recipe advancement false positives; loss/death never relocks. Creative/manual acquisition is not a security concern under owner policy.
- [x] Two-boolean one-way server -> client network. Forced authoritative sync on login/respawn/dimension change and unlock, plus periodic renewal; clear server caches on logout. Client binds to actual LocalPlayer object, fails closed without timely server packet, and cannot send an unlock request.
- [x] Pure presentation policy: numeric ProjectE item/stack EMC requires Stone unlocked AND current native knowledge AND native display setting. Learned marker requires Stone and native preference; Replication value requires Replica Ingot and preference. Native Divining Rod chat readouts explicitly exempt, including before Stone. Never creates parallel knowledge state.
- [x] Pure JUnit regressions for independent ordering, permanence, ProjectE learned-only numeric gate, native preferences, Replication independence and rod exemption.
- [x] ProjectE native tooltip adapter: source-audited `ToolTipEvent`/`PELang`, LOWEST item tooltip filtering of four precise numeric translation IDs, learned indicators and stored EMC before Stone. After Stone, numeric lines remain only if the **current native client knowledge capability** recognizes the exact ItemStack; other text and Divining Rod chat remain untouched. Actual pinned JAR bytecode/gameplay still needs verification.
- [x] Project Expansion learned-status adapter: source-audited `ItemTooltipEvents` (LOW priority) and `Lang`, filtered `text.projectexpansion.learned`/`not_learned` only before Stone at LOWEST; leaves Alchemical Collection text and post-Stone unlearned indicators untouched. Actual 1.0.6 binary and client QA still needed.
- [x] Replication tooltip adapter: source-audited `ClientEvents` `RenderTooltipEvent.GatherComponents` (including TerminalScreen branch), removes only `com.buuz135.replication.client.MatterTooltipComponent` or `tooltip.replication.matter_value` before Ingot. No matter tank/item descriptors, settings or actual processing touched. Actual pinned 1.2.7 binary and client QA still needed.
- [ ] Audit JEI, Jade and per-machine stored-value overlays; tests require exact pinned JARs and in-game source behavior. Do not globally disable emcToolTips or alter actual resource mappings.

## Design blockers and engineering limits

**No unresolved owner choice for the two milestones.** The issue approved first Stone including trade, first Replica Ingot and learned-only post-Stone numerical display; pre-Stone native Divining Rod messages are an explicit exception. Exact provider adapters and UI event ordering are technical work, not new balance decisions. This foundation alone does not yet hide text in the live tooltip.

## B/C in-game QA (not executed)

- [ ] B: attachment Codec, registered module, server sync login/respawn/dimension/relog, absence of optional providers.
- [ ] C: simultaneous players with distinct milestones, craft/trade/gift/fishing, Newbie, death/loss, relog/new world, migrated inventory.
- [ ] C: ProjectE learned/unlearned/forgot/relearned, stack values and Shift; Project Expansion independent marker; native pre-Stone Divining Rod modes and values; Replication Shift/terminal/matter; JEI/Jade and other tooltip text preservation.
- [ ] C: native conversions and machine operation unaffected; no cross-player leak or client-supplied unlock.

**Test boundary:** Java 21 standalone Progression quick CI, offline unit tests; no Integration Assembly, server smoke or Prism/gameplay run authorized or performed. Progression writer reviews/merges to dev/progression; separate integration writer promotes exact source once coherent.

## Source-aligned tooltip implementation checkpoint

- First-party source: `compat/progression/src/main/java/io/github/heatwaveqc/tyr/progression/client/{TooltipValueHider,NativeProjecteKnowledge}.java` plus pure `foundation/TooltipLinePolicy.java`; pure regression `TooltipLinePolicyTest`.
- **ProjectE pinned**: 1.21.1 PE1.1.0, CurseForge file 6611984; code references source branch `sinkillerj/ProjectE:mc1.21.1` `events/ToolTipEvent.java`, `utils/text/PELang.java`, `utils/EMCHelper.java`, and API `IKnowledgeProvider`/`PECapabilities`. Values use exact native translated component roots: `emc.projecte.tooltip[.stack][.with_sell]`, `emc.projecte.has_knowledge`, `emc.projecte.no_knowledge`, and `emc.projecte.stored`. Optional knowledge access invokes the existing native `IKnowledgeProvider.hasKnowledge(ItemStack)` via the player EntityCapability; no copied learned cache.
- **Project Expansion pinned**: 1.0.6, Modrinth a7yNQPzW/F5C1OobM SHA-512 97e832da2ca03309f4ef875b42a9250bba3798453f271f82b441e7c0bf739e103ee9f4274839b5e4d9a154a44167cc0f8169bd97ed37b3d16e7e09d34cfe3c85. Source `DonovanDMC/ProjectExpansion@168bcf2491b9fde679295fd412ad9c93fd3d93f1` `events/ItemTooltipEvents.java` and `util/Lang.java`. Native LOW priority listener may append a checkmark to original ProjectE component or add an independent learned/not-learned line; this filter at LOWEST checks translated roots, not localized string or broad EMC regex.
- **Replication pinned**: 1.2.7, CurseForge file 7662870; source `Buuz135/Replication:1.21` `client/ClientEvents.java`. Native events append `Either.right(MatterTooltipComponent)` or `Either.left(Component.translatable("tooltip.replication.matter_value"))`. LOWEST GatherComponents scrub includes native terminal context and does NOT change matter tank item state or GUIs.
- Existing client synchronization lease remains server-issued and player-bound; absent/unsynced state hides values and returns normal nonvaluation tooltips. Exactly recognized translation keys are removed only; unrelated pedestal, tags, alchemical enchantment state, Divining Rod chat, and vanilla item name/lore are not touched. This is a **presentation UX** gate, not a guarantee against a modified client.
- Exact upstream *source* was inspected. **Actual published binary parity, all listeners/priority ordering in a running instance, JEI/Jade/machine-specific valuation displays, and B/C tests still require separate audit/authorization.** Do not claim complete #237 or world PASS from Java CI.
