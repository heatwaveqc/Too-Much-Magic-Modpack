# Dimension Access, Survival, and Return Registry (research baseline)

**Date:** 2026-11-20  
**Pack:** Too Much Magic — Minecraft 1.21.1 / NeoForge.  
**Authority:** `test/full-integration` Packwiz `index.toml`, `manifest/mods.json`, and `manifest/next-qa-target.json`.  
**Data:** [dimension-inventory.csv](dimension-inventory.csv) — canonical reviewable rows; analogous to `docs/hunter/creature-inventory.csv`.  
**Status:** preliminary **design/reference registry only**; not a live dimension registry dump or a teleport config. Do not change configs, progress gates, mods, builds, or scripts from this PR.

## Purpose and three separate criteria

1. **Bare survival** — how difficult surviving the first hours would be when an ordinary human player arrives with **empty inventory, no equipment, no prepared shelter, and no privileged powers**. Avoid conflating inherent environmental danger with a dimension's late-game place in the questbook.
2. **Stranded return** — how difficult returning **alive and without commands, suicide, pre-existing transport gear, or an arrival portal** would be, assuming a non-standard teleport at arbitrary X/Z. The dimension's *usual* return mechanics might require a remembered portal, dream shell, placed block, or boss/ritual.
3. **Native arrival gates** — the exact native entry mechanism and its relationship to progression (materials, boss loot, ritual tier, environmental trigger, dream state, etc.). **A native gate on constructing/activating a portal is not proof that the mod blocks external teleports.** The `gate_applies_to_nonstandard_arrival` field remains unverified wherever this distinction matters.

Difficulty scale: **1** easy, **2** manageable, **3** challenging, **4** very hard, **5** extreme / practically stranded. Return **0** means already in the Overworld; blank means unverified and should not be assigned a risk tier.

The data intentionally keeps `dimension_id` **empty where not confirmed against the actual installed mod**. Never substitute the human-facing name for a registry ID in a config. The `weeping_angel_review` column is a caution flag, not an approved allowlist.

## Current candidate inventory

| Dimension | Bare survival | Stranded return | Native entry/gating theme | Main caveat |
| --- | ---: | ---: | --- | --- |
| Overworld | 1 | 0 | Normal spawn | Control baseline |
| Nether | 4 | 4 | Obsidian/fire portal | Local resource escape possible but difficult; Angels hard-exclude it upstream |
| End + BetterEnd | 5 | 5 | Stronghold portal and dragon/exit state | BetterEnd changes resources; no normal portal guaranteed at abnormal arrival |
| Aether | 3 | 2 | Glowstone/water portal | Fall-through void return and interactions with other mods need proof |
| Otherside | 5 | 5 | Ancient City portal and Warden-derived Heart of the Deep | Serious boss-ingredient entry gate |
| Dive Into the Abyss | 5 | 5 | Reported void-entry / progression | Exact ID and no-portal exit not yet independently verified |
| Eternal Starlight | 4 | 4 | Ruins Gatekeeper / Orb of Prophecy | Native arrival portal is absent, but frame and Orb are locally craftable |
| Aurorian | 3 | 3 | Crafted/activated portal | Exact port recipe and available return materials need audit |
| Bumblezone | 3 | 2 | Bee-nest / pearl entry | Return via vertical boundary may use remembered origin |
| Twilight Forest | 3 | 3 | Flower pool + diamond; separate boss/biome sequence | External entry does not inherently lift internal boss/biome protection |
| Voidscape | 5 | 5 | Standing on bedrock / voidic travel | Severe environmental hazards, no proven naked escape |
| Pastel Deeper Down / Imbrifer | 4 | 5 | Late progression portal | Exact **Pastel** dimension ID and return path unverified |
| Neo Vitae Endless Realm | 5 | 5 | Tier II / III ritual dungeon | **Instance/lobby + placement risk** under arbitrary teleport |
| JAWR Spirit World | 4 | 5 | Dream/shell state | **Player shell and inventory-state risk** without native entry |
| Feywild realm? | — | — | Not verified for Reboot 5.6.4 | **Not confirmed to exist in target runtime** |
| Pastel Conservatory? | — | — | Found in related upstream source tree | **May not be a playable/registered 1.21.1 Pastel dimension** |

These are preliminary comparative grades, **not measured survival completion rates**. The severe-return score means no safe independent return is currently established; it does not assert mathematical impossibility. In dimensions such as the Aether and Bumblezone, native mechanisms suggest a viable return, but we must verify them on nonstandard arrivals, not extrapolate from portal travel.

## Pack scope and aliases

- The active QA target includes **The Aether, Deeper and Darker, Dive Into the Abyss, Eternal Starlight, The Aurorian (unofficial port), The Bumblezone, Twilight Forest, and Voidscape**; each has a Packwiz indexed mod in the inspected integration source. `BetterEnd: New Dawn` **overhauls** `minecraft:the_end`; it does not create a second End dimension.
- **Pastel**, **Neo Vitae**, and **JAWR** are indexed magic mods that also add dimension-related gameplay. They must not be omitted simply because the mod's primary category is magic.
- **Deep Aether** is a presently deferred addon, **not** a distinct dimension. **Liroth: Revamped** is deferred from the current QA target (and may add multiple dimensions if later restored). They are **outside this active roster**.
- The **Feywild Reboot** and potential **Conservatory** rows are provisional research leads. Omit them from live config exports unless a loaded dimension registry verifies them.
- **No duplicate dimension row** for YUNG's structures, Tectonic, Cataclysm, Twilight Forest Final Boss remake, or Aquamirae; these enhance existing worlds/realms rather than establishing a verified new Level.

## Important transport and gate distinctions

**Native portal vs arbitrary teleport:** A Weeping Angel does not craft a portal on arrival. It chooses a loaded dimension from the native pool (minus `banned_dimensions`; Nether excluded by its own code), then attempts random X/Z offsets near the Angel's coordinates at a heightmap landing. That is **not** a mod-specific dimension spawnpoint, and it may be unusable for procedural enclosed worlds. A selected destination can be the player's current dimension. The owner explicitly chose to keep **both local and cross-dimensional native teleports**; no new teleport patch or weighted destination implementation is authorized. [Weeping Angels intake #387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387).

**Source-aware progression:** Native *entry item* requirements (Gatekeeper orb, Warden heart, advanced ritual) often gate only the standard portal/ritual. Do **not** assume a forced teleport is refused. Likewise, **Newbie** mode's potential travel limitations must be checked against real current Progression code rather than invented. A narrow Angel exception is research only; none has been implemented or approved.

**Return state:** Some destinations rely on an arrival portal, remembered coordinates, an attached dream/player shell, or an instance/lobby. A forced teleport might skip that initialization, leaving a stranded player or inconsistent state. Prioritize JAWR Spirit World and Neo Vitae Endless Realm as **high-risk nonstandard arrivals**. Environmental survival should additionally test elemental/time/void damage, flight restrictions, and forms independently.

**Score does not equal eligibility:** The most interesting weird-progression destinations may be difficult. These scores are inputs for future mod configuration, quest, and Newbie decisions, not an automatic judgment that a dimension is suitable or unsuitable.

## Return-route source audit update — 2026-11-20

Owner selected an initial **native-config-only** Angel destination set of `minecraft:overworld`, `aether:the_aether`, `the_bumblezone:the_bumblezone`, `twilightforest:twilight_forest`, `theaurorian:the_aurorian`, `eternal_starlight:starlight`. All other loaded dimensions must be excluded with `banned_dimensions`, including `neovitae:dungeon` and JAWR Spirit World. Draft config and exact return-route audit are in [PR #392](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/392). Native blacklist is not fail-closed; exhaustive level census needed to finish JAWR and any other unknown realm IDs.

Verified source IDs: `wardentools:abyssdim` (Dive Into the Abyss, source `ModDimensions.java`) and `pastel:deeper_down` (Pastel 1.21.1 dimension JSON). No longer leave either ID blank in CSV. JAWR's runtime Spirit World ID remains unresolved.

The Aether falls below Y=0 to Overworld (risk of lethal Overworld fall); Bumblezone leaves via Y<0 or Y>255, with origin-record lookup and Overworld fallback. Twilight Forest 1.21.1 has a portal in the Forest leading to `originDimension` (default Overworld), but an involuntary arrival has no portal. Its diamond/water/vegetation recipe must be obtained locally. Aurorian 1.21.1 port crafts portal frames from native stone and lights them with two Silentwood Sticks; its portal sends from Aurorian to Overworld. Eternal Starlight's `ESPortalBlock`/`ESTeleporter` creates a return portal only after *native portal transit*; Angel entry does not create one. However, the **Orb of Prophecy is craftable inside** from 5 Blue Starlight Crystal Shards and 4 Glass. Crystals generate locally (but iron-tier pickaxe required; Deepsilver pickaxe qualifies); Twilight Sand smelts to Glass, and Chiseled Voidstone frame blocks are made from Cobbled Voidstone slabs. Escape via self-built native return portal is source-supported rather than a hard lock. Confirm exact pinned 0.9.1 recipes and test naked return in-world.

Source details and explicit unknowns: [Weeping Angels return-route audit](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/weeping-angels-native-dimension-blacklist-20261120/docs/dimensions/WEEPING-ANGELS-NATIVE-DESTINATIONS-2026-11-20.md).

## Verification and acceptance workflow (future owner-authorized test)

1. **Authoritative registry census:** from the actual frozen assembled candidate record the full set of `ResourceKey<Level>` dimension IDs. Compare it to this CSV, check every `required` row, and resolve all empty IDs. Do not interpret a dimension JSON present in a development repository as a dimension registered in the tested JAR.
2. **Config census:** read pinned mod configs/data (especially Angel `banned_dimensions`, spawn dimension blacklist, travel settings, Twilight Forest progression and any Aether/void travel overrides). Confirm exact mod versions/hashes and latest desired QA target.
3. **Naked arrival test:** external teleport to fresh X/Z in each real world with zero inventory; verify placement at surface/void/inside solid blocks, environmental survival, resources/food/shelter, difficulty effects, and what happens on restart.
4. **Return-without-portal test:** no cheat commands, no prebuilt entry portal, no `/home`/Newbie home shortcuts when scoring. Record a reproducible normal survival return method and whether it depends on hidden player-visit state. Verify Aether-fall, Bumblezone exit, Neo Vitae lobby/pillar, and Witchery wake-up specially.
5. **Native gate test:** compare normal entry, cross-dimensional Angel abduction, and direct dimension command only as diagnostic controls. Test whether the mod requires a specific advancement, boss drop, ritual resource, portal frame, or persisted player state **at entry time**, versus only to open a portal. Avoid breaking native quest/item acquisition gates.
6. **Newbie and multiplayer test:** first-time Newbie and graduated Regular, separated inventories and teams; verify actual gate behavior, persistence, portal restriction, death/respawn and no accidental cross-player unlock or hard lock.
7. **Update scores with evidence:** source URL, version, runtime log/ID, manual result, status and test SHA. Only after review derive a *separate* dimension allowlist/blacklist from this inventory. Keep uncertain targets outside automatic generation.

**No full build, integration assembly, server boot, or gameplay acceptance was performed by this research-only branch.**

## Evidence trail

- Pack: [Packwiz index](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/index.toml), [decision roster](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/manifest/mods.json), [next QA target](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/manifest/next-qa-target.json), [Astral dimension ID audit](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/qa/ASTRAL-DIMENSIONS-2026-11-19.md).
- [Aether wiki](https://aether.wiki.gg/wiki/The_Aether/The_Aether), [Deeper and Darker](https://www.curseforge.com/minecraft/mc-mods/deeperdarker), [Twilight Forest](https://www.curseforge.com/minecraft/mc-mods/the-twilight-forest), [Bumblezone official project wiki](https://github.com/TelepathicGrunt/Bumblezone/wiki), [Voidscape](https://www.curseforge.com/minecraft/mc-mods/voidscape).
- [Neo Vitae 1.21.1 Endless Dungeon](https://github.com/breakinblocks/NeoVitae/blob/1.21.1/wiki/The-Endless-Dungeon.md), [Pastel](https://modrinth.com/mod/pastel), [JAWR](https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake), [Eternal Starlight](https://www.curseforge.com/minecraft/mc-mods/eternal-starlight), [Aurorian unofficial port](https://www.curseforge.com/minecraft/mc-mods/the-aurorian).
- Distinguish **upstream description** from **installed-version source or JAR confirmation** and **actual in-world test**; details for each row are in the CSV's `verification` and `source_url` fields.
