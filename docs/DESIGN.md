# Design contract (2026-11-13)

## Vision
Magic is the everyday infrastructure, not a cosmetic wrapper around industrial logistics. Independent magic schools must have sustained roles. Resource transport, crafting, storage, navigation and mining should favor occult/magical mechanics. The technology branch is exceptional and late-game.

## Core magic and progression policy (clarification 2026-11-13)
- **Ars Nouveau is a core, selected magic mod** of this pack. Keep it and its seven user-approved addons: Ars Additions, Ars Controle, Ars Elemancy, Ars Elemental, Ars 'n Spells, Ars Ocultas, and Ars Unification. A former suggestion to "avoid Ars Nouveau" was mistaken and does not apply to this modpack.
- **Pufferfish's Skills** and **Pufferfish's Attributes** are now approved *planned* components for a custom, cross-mod player skill tree (combat, magic, summon, crafting). The intent is not to install Default Skill Trees automatically or replace mods' native progression, such as Astral Sorcery's perk tree.
- Both remain **inactive** until packwiz pinning, dependency validation, server/client smoke, and skill-tree design. Upstream Skills 0.19.2 for 1.21.1 NeoForge is beta-tagged; this planning decision **does not authorize installing that beta** into the base release build. Attributes 0.8.3 has a release-channel artifact.

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
