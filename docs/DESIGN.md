# Design contract (2026-11-13)

## Vision
Magic is the everyday infrastructure, not a cosmetic wrapper around industrial logistics. Independent magic schools must have sustained roles. Resource transport, crafting, storage, navigation and mining should favor occult/magical mechanics. The technology branch is exceptional and late-game.

## Approved systems
Neo Vitae (not original Blood Magic); Occultism; Ars Nouveau; Iron's Spells 'n Spellbooks; Theurgy; Astral Sorcery; Botania; Just Another Witchery Remake; Mahou Tsukai; EvilCraft; Roots Classic. Preserve experimental status where appropriate.
Apotheosis, Artifacts/Relics and supernatural dungeons/bosses fit the power curve subject to item audits. Gateways to Eternity may support renewable combat drops.

## Exceptional technology
Only Bigger Reactors (energy source) and Draconic Evolution (energy use/transfer/high-end gear). Avoid introducing another general energy/logistics stack. Uranium and draconic materials should be restricted to deep exploration and/or Occultism extraction. Do not assume these restrictions are implemented.

## World and loot
Rich exploration via Cataclysm, Stalwart/Starlight themed structures and Deeper and Darker where compatible. Evaluate early exploration density and performance. Review and block game-breaking single-hit artifacts in normal loot, not just crafting.

## QoL / performance
Borrow the *idea* of ATM10's tested performance, mapping, chunk claims and UI infrastructure, not the whole ATM10 mod list or proprietary assets. Candidate libraries/performance mods: ModernFix, FerriteCore, Embeddium, Entity Culling, ImmediatelyFast; candidate QoL: JEI, Jade, AppleSkin, Mouse Tweaks, Just Zoom, FTB Chunks. Verify dependency graph and current 1.21.1 loader builds individually.

## Storage
Occultism storage is central. Magical satchels, named magical bags, and portable storage from approved magic mods are allowed after compatibility and balance review. Generic backpacks and generic storage/logistics are not.

## Reproducibility
Eventually every resource, boss drop and progression item has at least one renewable or reproducible route, with multiple distinctive magical paths where practical. Some paths should be expensive or late-game. Renewable != instantly cheap or farmable without limits.

## Non-goals for first prototype
No implemented progression locks, recipe overhaul, questline, substitutions, balance guarantees, compatibility claims beyond tests actually performed.

## B6 scope and skill systems
- **Ars Nouveau remains a core mod**, not excluded; its approved Ars add-ons remain pinned.
- **Pufferfish's Skills and Attributes** supply a future **custom cross-mod skill tree**. Only the two underlying frameworks are currently present; the tree still needs authoring and functional tests.
- **Improvable Skills 3** is a separate experience-point upgrade system installed on explicit user request. Audit overlap and balance against the custom Pufferfish tree once that is designed.
- New food suite is Pam's HarvestCraft 2 all four modules, Farmer's Delight, Aquaculture 2, Harvest with Ease and Cobweb. **Aspect Alchemy is a current exclusion.**
- **125 mods have passed a server-only boot**, not a client/world/integration certification.

## Unified magic progression and resource interoperability (approved 2026-11-13)
- The custom Pufferfish skill tree is a **unified, cross-school progression system**. Prefer interoperability of magical resource pools, spell power and general caster attributes **where the participating mods support it reliably**. Do not artificially separate otherwise compatible magic systems just to preserve multiple UI bars.
- For **Ars 'n Spells**, begin with its built-in `iss_primary` default (Iron's Spells mana is authoritative) as the **first test configuration**. Do not force `separate` or `disabled` by policy. The exact mode remains subject to functional testing of both parents and the pinned addon version; this decision alone does not change a pack config.
- A unified progression framework does **not** imply that every magic system has the same mechanics. Preserve distinctive research, rituals, attunement, spell design and transformations when combining them would break their gameplay; bring common stats and resources together when safe.
- Avoid duplicate spell-power multipliers, double mana deductions, accidentally free cross-casting, unbounded stacking, and skill refunds that leave stale bonuses. Cross-system buffs must be audited with both mods active before promotion to gameplay-ready.
- Pufferfish infrastructure is installed in B6 but **the custom tree is still absent**. The Roots KubeJS prototype uses temporary tags and is not a live skill binding.
