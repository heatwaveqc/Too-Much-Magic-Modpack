# Eldrich Echoes and TMM sanity-system audit — 2026-11-22

Issue: [#420](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/420)

Frozen TMM source: `test/full-integration@c11a95fc14955b3e646885d37e33636b9e37ddf1`

Companion matrix: [`eldrich-echoes-sanity-matrix.csv`](eldrich-echoes-sanity-matrix.csv)

## Result

**Do not add Eldrich Echoes to the current pack.** The exact 1.7.1 binary contains a real, server-authoritative 0–100 sanity system, a permanent HUD, sanity-gated attacks and spawns, dream-inventory custody, world generation, rituals, dimensions, and bosses. It does not provide a clean native “monsters only, no sanity system” mode. TMM already ships Voidscape, whose exact pinned binary has a separate serialized 0–600 paranoia/infusion system and client turmoil effects. Treating Eldrich Echoes as another independent meter would violate the owner’s one-effective-sanity-system rule.

This is a research conclusion, not an installation or exclusion decision. Promotion still requires an owner choice about whether Voidscape’s dimension-local paranoia counts as the pack’s one sanity authority, whether it should be bridged/disabled, and whether Eldrich Echoes is worth replacing it. No mod pin, manifest, config, runtime code, dimension entry, or pack hash changed.

## Artifact correction and provenance

The issue’s candidate mapping became stale after it was written:

| Claim | Exact provider state found 2026-11-22 |
| --- | --- |
| CurseForge file `9622830` is 1.7.1 | `9622830` is `eldritch-echoes-1.5.0.jar`, uploaded 2026-11-15 |
| Current 1.7.1 artifact | CurseForge project `1806409`, file `9717907`, `eldritch-echoes-1.7.1.jar`, uploaded 2026-11-20 |
| Size | 9,129,934 bytes (provider page rounds to 8.7 MB) |
| SHA-256 | `1826d0f440ac958134daa2424197c4cf66afd05dcafeadd38676e6feafca57d8` |
| Minecraft / loader | Minecraft 1.21.1, NeoForge; embedded dependency declares NeoForge `[21.1.200,)`, side `BOTH` |
| Dependencies | Embedded metadata declares only NeoForge and Minecraft |
| License | **Conflict:** CurseForge labels the project “Public Domain”; embedded `neoforge.mods.toml` declares `All Rights Reserved` |

Sources: [current CurseForge 1.7.1 file](https://www.curseforge.com/minecraft/mc-mods/eldrich-echoes/files/9717907), [older file 9622830](https://www.curseforge.com/minecraft/mc-mods/eldrich-echoes/files/9622830), and offline inspection of the exact downloaded JAR. The binary and decompiler output were temporary research inputs and are not committed.

The provider’s “Environment: Client” label is inconsistent with the binary. The JAR declares side `BOTH` and registers blocks, items, entities, world generation, attachments, common configuration, common events, and server-to-client payloads. Static inspection supports “both sides intended”; it does **not** prove dedicated-server startup safety.

## Exact 1.7.1 architecture

### Authoritative state and persistence

- `ModAttachments.SANITY` registers `eldritchechoes:sanity` as a serialized NeoForge attachment with `copyOnDeath()`.
- `SanityData` persists `sanity` (default 100, clamped 0–100), `fractured`, `wind_ticks`, and `next_wind`.
- The server ticks `SanityManager` for every `ServerPlayer`. Changes are sent with `SanitySyncPayload(float sanity, boolean fractured)`; the client does not author the value.
- `SanityHud` is registered above the vanilla food layer and renders for ordinary survival players even at 100 sanity. There is no HUD-enable setting.
- Separate serialized, copy-on-death attachments hold dream state, known rituals, and bestiary progress.

### Loss, recovery, thresholds, and consequences

| Mechanic | Exact static behavior |
| --- | --- |
| Passive darkness loss | Total raw brightness 0 adds `0.0175` loss per player tick unless warded, holding a lit Eldritch Lantern/starlight, or under vanilla Night Vision |
| Other ambient loss | Nearby gaze data map, whispering wind, dreamspace, and holding the Forbidden Tome add independent loss |
| Recovery | Nearby comfort `+0.025/tick`; sleeping `+0.1/tick`; ward strengths `+0.025` or `+0.05/tick`; food data map and specific content add more |
| Fracture | Reaching 0 sets `fractured`; returning above 20 mends it |
| Low sanity | Below 40 may spawn a Star-Spawned Stalker and may redirect sleep into dream realms |
| Night creatures | Event spawns occur even at 100 sanity: base chance is 1% per eligible once-per-second player check, scaling to 3% at zero before moon/rite/underground multipliers and local caps |
| Death/dreams | Sanity and dream attachments copy on death. Dream death/exit code stashes/restores inventory and raises sanity to at least 50; this directly intersects Smart Keep/Corail and must be tested if ever admitted |

`drainMultiplier=0` prevents `SanityManager.drain` from reducing the value, including direct mob/ritual/dimension calls. That is useful as a safety switch, but it is not removal of the system: the attachment, packets, meter, recovery loop, data maps, content calls, recipes, dimensions, structures, rituals, and most registrations remain.

### Native configuration boundary

Common configuration exposes:

- sanity drain and recovery multipliers;
- manifestations at zero, whispering wind, sub-40 stalkers, and dreamshift;
- night creatures and changed animals.

Client configuration can disable hallucinations, screen distortion, temporary inverted controls, chat whispers, and jumpscares, and can lower graphics quality. It cannot disable the sanity meter. No exact 1.7.1 setting was found for disabling structures, rituals, dream/void/R’lyeh content, attachments, or all sanity coupling while retaining only selected mobs.

## Can the monsters be used separately?

Only partially, and not cleanly enough for admission:

1. `nightCreatures=true`, `changedAnimals` as desired, and `drainMultiplier=0` would retain several Overworld event spawns while freezing sanity at 100.
2. This leaves the visible sanity meter and the whole server/client state machine active. Night creatures still call sanity drain, merely reduced to zero by configuration.
3. Numerous mobs are tied to rituals, structures, the Void, R’lyeh, the Sleeper, and 21 bundled dimensions (20 dream realms plus `the_void` in the inspected data). There is no single native “entities only” toggle.
4. Disabling bundled worldgen/content through external datapacks or suppressing the HUD with first-party code would be a TMM fork/adapter, not verified native isolation. It needs explicit owner scope and ongoing version-specific maintenance.

Therefore “monsters only” is **not** a safe configuration-only route. If the owner later wants selected creature concepts, prefer an explicit design decision: accept the complete system with one chosen sanity authority, omit it, or separately authorize a narrow original implementation. Do not copy ARR-marked binary code/assets based on the contradictory provider license.

## Existing and nearby TMM mechanics

The companion CSV records the exact comparison. The important distinctions are:

- **Voidscape 1.9.588 is the only installed second numerical player-state system found.** Its exact hash-pinned JAR stores serialized `paranoia` and `infusion` values from 0–600 on an `insanity` attachment, syncs them to clients, renders turmoil/fog effects, applies thresholds, and summons a hunter at maximum paranoia. Paranoia rises in the Void and resets to zero outside; infusion decays outside. This is narrower than a global sanity system but is not merely flavor.
- **Dive Into the Abyss 1.1.4 uses timed mob effects**, including `Corrupted`, screen overlays, corruption blocks/fluids, and damage. Its player attachments are task/whisper knowledge, not a numeric sanity wallet.
- **JAWR 0.5.39 Insanity is a curse**, persisted inside its general curse attachment. It spawns mimics and sounds; it is not a continuously earned sanity meter or standalone HUD wallet.
- **Primal Magick ritual stability is altar-local process state**, not a player mental-health stat.
- Registry names such as Goety’s Corruption Focus, Malum corruption, Mahou insight, Psi stress sensor, and performance “stability” are not evidence of player sanity. The repository-wide term sweep found no documented additional global sanity wallet. Exact-JAR negatives were only established for the three high-risk installed candidates above, so absence from the matrix is not a universal runtime proof for every one of the 222 active pins.

## Progression integration surface

Eldrich Echoes has technically reachable static methods (`SanityManager.get`, `drain`, `restore`, `set`) and a registered attachment, payload, item restore data map, and entity gaze-drain data map. These are implementation details, not a documented compatibility API. A Progression adapter would need optional class/method guards or a negotiated upstream API, plus exact-version tests.

If the owner later chooses Eldrich Echoes as the single authority, the already-approved *categories* that Progression could support remain: source-specific gain/loss modifiers, recovery, threshold resistance, perception/risk tradeoffs, and unlock gates. This audit deliberately does not select Paths, skill values, costs, thresholds, or balance.

## Recommendation and decision gate

Current recommendation: **do not include**.

Before reconsideration, the owner must choose one of these directions:

1. Keep Voidscape paranoia as the only effective sanity-like system and omit Eldrich Echoes.
2. Treat Voidscape paranoia as a dimension-local hazard, choose Eldrich Echoes as the global authority, and authorize an explicit bridge/HUD policy.
3. Disable or replace Voidscape paranoia and choose a different single authority.
4. Omit all global sanity systems and retain only ordinary effects/curses such as Abyss corruption and JAWR Insanity.

The license contradiction must also be resolved with the author/provider before any asset or code reuse. Merely installing the intact JAR still requires exact provider pinning, dedicated-server and client launch checks, dream inventory/death interoperability, two-player state isolation, dimension return, worldgen density, mob allegiance/taxonomy, accessibility, and performance QA.

## Required readiness ledger

### Implementation-ready slices

- None. Research is complete for the exact 1.7.1 candidate and the highest-risk installed overlaps. Installation, adapter code, config policy, or asset reuse would require owner decisions above.

### Explicit design blockers

- Does Voidscape’s dimension-local 0–600 paranoia count as TMM’s one sanity authority, or may it coexist with a single global authority?
- Is the desired outcome the full Eldrich Echoes overhaul, no sanity system, or selected creature concepts implemented separately?
- If Eldrich Echoes is selected, which Progression Paths/skills interact with it and what HUD/accessibility policy applies?
- Can the project author clarify the provider “Public Domain” versus embedded `All Rights Reserved` conflict?

### In-game testing queue

None is ready for QA because the mod is not approved or pinned. If later approved, classify server commands and static registry inspection as B only when exact commands are verified; client visuals, dream travel, death/inventory custody, two-player isolation, creature behavior, accessibility, and performance are C. This audit ran no Integration Assembly, server/client launch, or gameplay test and claims no runtime acceptance.

## Method and limitations

- Compared issue #420, open PRs and branches (none owned this workstream), the frozen integration manifest and exact installed Packwiz pins before starting.
- Downloaded the provider’s exact 1.7.1 JAR to a temporary directory, verified SHA-256, listed resources/registries, and decompiled bytecode with CFR 0.152 for read-only inspection.
- Downloaded and SHA-512-verified the exact current Voidscape, Dive Into the Abyss, and JAWR JARs against their checked-in Packwiz hashes; inspected the relevant attachments/state/effect/curse classes.
- Searched current integration docs, manifests, configs, data, and Progression source for sanity/insanity/madness/corruption/stress/stability/insight terms.
- Did not execute third-party code, start Minecraft, inspect generated runtime configs, test mixin/classloading, or prove every behavior in-game. Decompiled output is evidence of binary structure and control flow, not a substitute for runtime acceptance.
