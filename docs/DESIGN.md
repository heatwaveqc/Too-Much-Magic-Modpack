# Design contract (2026-11-13)

> **2026-11-14 owner decision:** Change only the **Philosopher's Stone center** from diamond to one of three boss-associated items (Wilden Tribute, Botania Gaia Spirit, Neo Vitae Mine Key). Keep both original outer-ring layouts. Require **Chaos Shard** in Red MK3+ Power Flower direct recipes and MK3 upgrade. All-items EMC is a coverage goal with cheap glass-pane exceptions. Existing older post-Chaos-only text below is historical. See [tiered gate design](PROJECTE-TIERED-GATING-PROPOSAL.md) and [feature checklist](FEATURE-CHECKLIST.md). No runtime gate has been implemented yet.

## Creative-tier items and ProjectE endgame — owner decision (2026-11-23)

**Locked pack-wide rule:** ProjectE/Project Expansion is the common late-game resource endpoint; **do not make modded creative-tier/infinite-resource blocks or items obtainable through ordinary survival progression**, even as a final reward. This applies to **every installed mod**, not just Botania. Do not introduce a parallel Avaritia/Extended Crafting-like infinite-resource tier. Preserve genuinely powerful, deliberately productive **noncreative** magic, farms, equipment and bosses; classify by actual mechanical behavior, **not merely whether the name includes "creative"**. Exceptions require explicit owner approval for the specific item/family.

**Enforcement must cover all entry points:** normal and custom-workstation recipes, generated/conditional datapacks, crafting upgrades/transformations, loot and reward bags, quests, vendors/shops, cross-mod processing, and EMC/ProjectE values or transmutation learning of creative variants (including item components/NBT). Creative/admin/test spawning may remain; do not unnecessarily remove registered creative-tab entries or disable unrelated survival mechanics. Prefer narrow recipe/loot removals or a confirmed-safe optional module flag. For installed mods, verify effective loaded recipes and guidebook/EMI listing; source edits alone are not gameplay PASS.

**Botania example:** Original Botania's `botania:creative_pool` / Everlasting Guilty Pool is not normally craftable. The pinned Botania Revived 2.0.2 adds default-enabled Creative Crafting Terra Plate recipes for Creative Pool, creative-flagged Mana Tablet and Creative Corporea Spark; those are explicitly **not permitted survival rewards**. Its `[modules] creative=false` flag is a possible integration remedy after checking collateral behavior such as Infrangible Platforms. Do not disable core Botania. The separately owner-approved **EMC Catalyst under a normal Mana Pool** consumes the *placing player's* personal EMC at **20 Mana per EMC**, and is the intended ProjectE-era mechanism for abundant Botania Mana, **not free infinite Mana**; see [RESOURCE-ECONOMY-STANDARD.md](RESOURCE-ECONOMY-STANDARD.md). The catalyst is design-approved, not automatically implemented.

**Tracking:** [issue #596](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/596) owns the universal audit and integration handoff; [Atlas PR #411](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/411) documents the exact pinned Botania source/recipe exception. **Design locked, implementation and authorized gameplay validation pending.**

## Vision
Magic is the everyday infrastructure, not a cosmetic wrapper around industrial logistics. Independent magic schools must have sustained roles. Resource transport, crafting, storage, navigation and mining should favor occult/magical mechanics. The technology branch is exceptional and late-game.

## Approved systems
Neo Vitae (not original Blood Magic); Occultism; Ars Nouveau; Iron's Spells 'n Spellbooks; Theurgy; Astral Sorcery; Botania; Just Another Witchery Remake; Mahou Tsukai; EvilCraft; Roots Classic. Preserve experimental status where appropriate.
Apotheosis, Artifacts/Relics and supernatural dungeons/bosses fit the power curve subject to item audits. Gateways to Eternity may support renewable combat drops.

## Exceptional technology
**2026-11-15 owner decision:** remove Bigger Reactors original and Rework. Draconic Evolution supplies the dedicated furnace-fuel generator, FE storage/transfer and dangerous endgame reactor; Botania Revived converts mana to FE with its Mana Fluxfield. Psi, Hex Casting and other magic systems may also supply FE, but require exact-version research before promising output rates. No separate nuclear/uranium tech progression or generic FE/logistics stack. Preserve real Draconic endgame material gates, not the obsolete reactor-uranium plan.

## Approved dimension expansion — next owner-authorized comprehensive test (2026-11-16)

**Owner-locked selection, not installed yet.** Attempt to resolve, hash-pin, and stage all of the following together for the next comprehensive unstable integration test, subject to exact Minecraft 1.21.1 NeoForge releases, dependencies, conflicts, and authorized test stages. No unattended server boot or Prism build is authorized by this decision.

- **Nine new mods/addons:** The Aether; Deep Aether (addon to Aether, not separate dimension); The Bumblezone; Dive Into the Abyss; Eternal Starlight; The Twilight Forest; The Aurorian (unofficial port); Voidscape; Liroth: Revamped.
- **Already installed:** Deeper and Darker (1.4.1); retain it, verify Otherside progression in client QA.
- **Explicitly deferred / not selected:** Dimensional Dungeons (Neo Vitae already provides endless procedural dungeons; reconsider only for puzzle-specific gameplay); Realms of Redemption (reserve); Undergarden; Blue Skies; Advent of Ascension.
- **Twilight Forest endgame:** replace the unfinished Final Castle climax with an intentionally dangerous **Killer Rabbit of Caerbannog** encounter and a real quest/trophy completion path. The separately staged Caerbannog/Chaos Bunny Gateways prototype is *not* confirmation that this final-castle encounter exists. Preserve upstream Twilight boss progression; do not claim complete until an actual rabbit final encounter is tested.
- **Acceptance for the new cohort:** exact official files and compatible libraries, clean dedicated-server co-load, fresh-world generation, portal round trips/re-entry, meaningful content/bosses and quest access, multiplayer/dimension-save restart, performance/structure density, and mobility/teleport/flight bypasses. These are QA tasks, not claimed PASSes.

## World and loot
Rich exploration via Cataclysm, Stalwart/Starlight themed structures and Deeper and Darker where compatible. **Alex's Caves Continued** is approved as *planned* exploration content on NeoForge 1.21.1, with required CodxLib; exact pins are on staging PR #56, but neither mod is currently installed. Evaluate early exploration density and performance. Review and block game-breaking single-hit artifacts in normal loot, not just crafting.

## QoL / performance
Borrow the *idea* of ATM10's tested performance, mapping, chunk claims and UI infrastructure, not the whole ATM10 mod list or proprietary assets. Candidate libraries/performance mods: ModernFix, FerriteCore, Embeddium, Entity Culling, ImmediatelyFast; candidate QoL: JEI, Jade, AppleSkin, Mouse Tweaks, Just Zoom, FTB Chunks. Verify dependency graph and current 1.21.1 loader builds individually.

## Storage
Occultism storage is central. Magical satchels, named magical bags, and portable storage from approved magic mods are allowed after compatibility and balance review.

**Owner clarification (2026-11-23):** The exclusions on **generic backpacks and generic item logistics** are **mod-selection exclusions**: do not install a standalone mod whose primary/only purpose is to provide generic backpacks or a general-purpose logistics network. **Do not interpret the exclusion as a ban on storage items or incidental transfer mechanics within already-selected multi-system magic mods.** In particular, the native `malum:soulwoven_pouch`, `malum:ravenous_pouch` and Gaze addon `gaze:spirit_channel_pouch` are permitted survival features, despite ordinary-item storage and the Spirit-Channel Pouch's bound-position, same-dimension dropped-item delivery. They need no separate exception or source recipe removal. Ars and Occultism personal storage follow the same principle.

**Independent non-nesting rule (release blocker [#16](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/16)):** no portable bag may be placed inside any other portable bag, even within the same mod or across mods. All three Malum/Gaze pouches join the compatibility/tag/acceptance coverage; no nested storage, silent item deletion, or dupes. Ordinary chests and approved stationary storage are outside this blanket prohibition. This owner decision permits native survival acquisition; it does not automatically award pouches as starter items or quest rewards.

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

## Owner-locked endgame exclusions (2026-11-14)

- **Re:Avaritia and Extended Crafting are excluded.** Both were placed on hold in the selection discussion but incorrectly remained pinned through B8. Their infinity-tier/ultimate crafting power would compete with signature Draconic Evolution progression and ProjectE's Gem Armor.
- Maintain Draconic Evolution, the Chaos Guardian, and post-Guardian ProjectE/Project Expansion as the intended equipment apex. Do not reintroduce those two mods or design quests for them without new owner approval.
- **Skills & Advancement** and **A Sufficiently Advanced Magic** are approved as distinct subchapters of the **Progression** top-level category.

## Quest shop economy (2026-11-14 owner decision)

- Keep fixed, thematically appropriate **item rewards as the strong default**. Use Quest Shop coins only where a fixed item would be arbitrary, especially intentional player sales of copper, emeralds and diamonds.
- Shop is a **bad-luck and repetitive-grind relief valve**, covering common supplies, amethyst, Heart of the Sea and selected Artifacts. It must not grant boss-critical or pre-Guardian ProjectE progression.
- Allow ProjectE to print sellable resources (and hence coins) after its actual Chaos Guardian unlock. Keep per-sale payouts very small at industrial quantities; do not add general buy/sell arbitrary inventory conversions.
- Quest Shop's current upstream FTB Teams integration provides a **team-shared coin account**. Audit repeatable item-consuming quests for team overpayment, then document/resolve any policy questions before release.

## Programmable technology and Iesnium shortcut (2026-11-15 B14)

CC: Tweaked 1.121.0 is selected for Draconic Reactor monitoring and control, **not** AE2/general FE logistics. Desktop/pocket computer crafting requires an Eldritch Logic Core from the **Ancient Eldritch Miner** output table. Its weighted result is a late-game first-acquisition gate; CC turtles inherit that requirement. Source-verified native Occultism Spirit Fire can convert **Silk-Touched End Draconium Ore** into Iesnium Ore 1:1, without a custom entity tick handler. See [CC pilot and acceptance](CC-TWEAKED-ELDRITCH-GATE.md). Exact installed binary, JEI and server/client behavior remain untested.
