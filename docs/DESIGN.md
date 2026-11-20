# Design contract (2026-11-13)

> **Current mod disposition (November 20, 2026):** [Owner Mod Dispositions](qa/OWNER-MOD-DISPOSITIONS-2026-11-20.md) supersedes the historical prototype/dimension selection below where inconsistent. YUNG's Better End Island is now owner-approved for next authorized live QA with BetterEnd compatibility **unverified**; five older dimension candidates (Dimensional Dungeons, Realms of Redemption, Undergarden, Blue Skies, Advent of Ascension) remain deferred. Dragon Survival, Aether and Twilight Final Boss addon are already pinned; standalone Surprisingly Substantial Satchel is actually in Occultism. See #390 for scope freeze and first-party progression rules. No assembly/smoke or build approval implied.

> **2026-11-14 owner decision:** Change only the **Philosopher's Stone center** from diamond to one of three boss-associated items (Wilden Tribute, Botania Gaia Spirit, Neo Vitae Mine Key). Keep both original outer-ring layouts. Require **Chaos Shard** in Red MK3+ Power Flower direct recipes and MK3 upgrade. All-items EMC is a coverage goal with cheap glass-pane exceptions. Existing older post-Chaos-only text below is historical. See [tiered gate design](PROJECTE-TIERED-GATING-PROPOSAL.md) and [feature checklist](FEATURE-CHECKLIST.md). No runtime gate has been implemented yet.

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
