# Locked design decisions — implementation deferred

These are design invariants, not claims about live game behavior.

1. **ProjectE and Project Expansion:** all usable systems post-Chaos Guardian (Draconic Evolution). Lock all acquisition routes and functionality, not just crafting-table recipes. Check loot, villagers, quests, advancements, item duplication/EMC, starter items, commands and integrations for leaks; admin commands may remain a test bypass.
2. **Replication:** its Deepslate Replica Ore must be available through the highest-tier Occultism miner (Eldritch Ancient Miner) instead of ordinary worldgen. Confirm the exact mod, recipe/item IDs and whether any generation path can leak before coding this.
3. **Eldritch Ancient Miner:** gate by advanced Cataclysm materials, provisionally from the Harbinger and Ignis. Reduce the previously assumed eight-Marid input to a balance-target range of **two to four**. Consider very late Botania materials as another pathway or extra requirement; compatibility and balancing still pending.
4. **Tech materials:** uranium and draconic dust belong in dangerous deep exploration or Occultism mining, not routine overworld ore farm. Final sources and generation changes pending.
5. **Ring of the Seven Deadly Sins:** no normal acquisition at all, including crafting, world loot, trades, quest rewards, mob drops or generated containers. Audit similarly overpowered one-shot gear.
6. **Renewability:** every item/resource eventually needs a reproducible route. Boss loot can remain challenging; alternative routes must not bypass the intended boss/technology gates.
7. **Quests:** FTB Quests will communicate the pathways after core mechanics stabilize, not substitute for locks.

Do not add placeholder broken KubeJS recipes using speculative registry names.

## B4 compatibility proviso
- BiggerReactors Rework 0.1.6 adds a separate battery and industrial processor along with reactor energy generation. Review the energy/storage features against our limited-tech policy before progression work; it was explicitly approved as the newer fork for testing.
- Botania Revived may expose optional Ars Nouveau integration; balance mana/source conversions and remove any forbidden storage acquisition path.
- Experimental Astral Sorcery and Draconic Evolution are boot-tested, **not yet gameplay-accepted**; Draconic Chaos Guardian remains the ProjectE gate.

## Endgame equipment hierarchy and excluded mods (owner decision, 2026-11-14)

- **Exclude Re:Avaritia and Extended Crafting** (originally on hold, inadvertently pinned). No Infinity-tier equipment or separate ultra-crafting mechanic should obsolete Draconic Evolution or ProjectE Gem Armor.
- Draconic Evolution and Chaos Guardian remain the critical late-game step; ProjectE and Project Expansion unlock **only after** the Guardian, as described above, and Gem Armor remains a meaningful late-game reward.
- The Endgame category has detailed chapters for Draconic Evolution, ProjectE/Expansion and Replication. Progression has an Endgame Roadmap overview, as well as owner-approved Skills & Advancement and Exceptional Power: Reactors chapters.

## Monk flight and mining distinction (updated 2026-11-14)

The Monk has a **post-Ender Dragon innate momentum glide only**; it does
**not** unlock Creative Flight at any rank. Draconic Evolution's actual
flight modules may supply Creative Flight from the permitted chestpiece
Curio. Their acquisition and module semantics are part of the gear
progression and should not be duplicated as Monk skills.

Monk barehand **harvest tier** is a finite capability path whose last
rank matches the highest real tool harvest requirements in the final
pack. Barehand **harvest speed** is a **separately repeatable numeric
trait** with rising cost; more speed never bypasses harvest/drop checks.
We have source-confirmed that Dragon Survival models harvest bonus and
break-speed multiplier separately. See `MONK-WORLD-TIERS.md`.

## Dragon Survival mod selection (2026-11-14)

Dragon Survival is approved for Dragon/Dracolich transformation work. The manifest now lists 1.21.1 NeoForge v2.0.74, CurseForge file 9539454, `desired=true`, but **`enabled_in_pack=false`** until packwiz install and a combined boot/client world test. GeckoLib is present but exact compat not yet smoke-tested. This selection does not itself add a Dragon Survival player transformation or any dracolich progression.
## Tyz's Skills chosen for leveled traits (2026-11-14)

The user selected **Tyz's Skills 6.4.2**, verified NeoForge
1.21.1 CF project 990190 / file 8118598, as the dedicated
SP-purchased trait backend. It is **manifest-approved** (`desired=true`)
but not yet packwiz-installed or smoke-tested. The tier strategy
uses five separate 99-rank skill stages for a sufficiently scalable
attribute, tenfold stronger and tenfold more costly per stage.
Tyz SP is derived from the mod's own activity-based XP, not vanilla
experience. Native Monk tree still handles one-time powers and
training licenses; Pufferfish still handles nonoverlapping one-time
shared choices. Effective caps, boss milestones, progression placement
and any sixth stage are deferred to later review.
See [MONK-TYZS-TIERED-ADVANCEMENT.md](MONK-TYZS-TIERED-ADVANCEMENT.md).
