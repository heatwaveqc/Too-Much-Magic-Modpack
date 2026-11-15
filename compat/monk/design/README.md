# Monk faction — full native skill-tree skeleton (v1, 2026-11-15)

**State: fully outlined, NOT active gameplay.** This is the progression blueprint for the 20-rank Monk faction, not a silent activation of 44 unfinished abilities. It is carried into the next all-stream integration build by the existing `compat/monk/` import. The six already-live skills and their JSON/Java/effects remain unchanged while the initial Monk UI/purchase test is pending.

**Canonical machine-readable plan:** [native-full-tree-skeleton-v1.json](native-full-tree-skeleton-v1.json). CI validates the graph and complete native/source-affix coverage through `MonkFullTreeSkeletonTest`.

## Important architecture

The **native Vampirism Monk tree** spends native Monk points on one-time techniques, limited meaningful stages (barehand tool tiers and breath training), and **permissions** to buy separately implemented Monk-only Tyz ranks. It does not own general numeric training in attack, movement speed, spell power, mana, mining speed, or the 40 bounded special-affix ranks: those belong to **one primary Tyz/MXP economy**.

There are **50 entries**: 6 live initial nodes (including the zero-cost root) and 44 **blueprint-only, unregistered nodes**. There are five tracks, some cross-track prerequisites, and two provisional post-Chaos capstones. The current one-native-point-per-rank pilot would award 20 native points by rank 20, so this represents **choice and specialization**, not a promise one character can buy every technique. Costs, rank pacing, rank-up deeds and capstone balance are open.

Rank numbers below are **suggested earliest progression windows**. They are *not* currently enforced by the Vampirism native skill GUI. When a technique is implemented, a server-authoritative unlock must enforce its real gate; prerequisite layout alone is not an authoritative boss-defeat test.

## Milestone roadmap

| Rank window | Expected developments | Tests / hard gates |
| --- | --- | --- |
| 1–4 | First Sunset; choose Open Palm, Unarmored Poise or Stone Fist; early fleet/step and meditation/guarding | First Sunset is live. Native purchase/UI fix pending player test |
| 5–8 | Iron-hand mining, spirit projectile, arrow-catching, basic breath/animal practice, water walking | Complete block/spell credits; no forced optional stunts |
| 9–12 | Diamond-hand mining, refinement, cleansing, legal advanced ranged skills, Blink | Verify source hooks/claims and keep ordinary Tyz stats universal |
| 13–16 | More specialized affixes, high-tier resilience and tool-free harvesting, Ender-Dragon-gated momentum glide | Ender Dragon evidence must be server-verified, not item/quest-only |
| 17–20 | Optional rare harvest effects, extreme techniques, two proposed capstones | Highest tool requirements require evidence; provisional capstones gated on actual Chaos Guardian defeat |

## Proposed faction-tree branches

### Martial Discipline

Legal unarmed combat, counters, spirit projectiles, boss interaction and selected affix conversions.

| Suggested rank | Native node | Prerequisite | Status |
| ---: | --- | --- | --- |
| 1 | Open Palm | monk root | **Live pilot** |
| 4 | Guarding Palm | open palm | Planned |
| 6 | Catch Arrows | guarding palm | Planned |
| 6 | Spirit Projectile | open palm | Planned |
| 9 | Looting Palm License | open palm | Planned (Tyz license) |
| 11 | Galvanizing | guarding palm | Planned |
| 12 | Withering Stare | spirit projectile | Planned |
| 14 | Retreating | galvanizing | Planned |
| 16 | Omniscient | withering stare | Planned |
| 18 | Deathbound Spirit | spirit projectile | Planned |
| 20 | Empty-Hand Paragon | omniscient | Planned; gate: verified chaos guardian milestone |

### Body & Mobility

Safe traversal, footwork, water movement, teleportation and post-Dragon momentum glide.

| Suggested rank | Native node | Prerequisite | Status |
| ---: | --- | --- | --- |
| 2 | Fleet Footwork | open palm | **Live pilot** |
| 3 | Step Assist | fleet footwork | **Live pilot** |
| 7 | Steadfast | step assist | Planned |
| 8 | Water Walking | step assist | Planned |
| 10 | Surefooted | steadfast | Planned |
| 11 | Blink | water walking | Planned |
| 14 | Unhurried | surefooted | Planned |
| 15 | Momentum Gliding | blink | Planned; gate: personal or credited ender dragon defeat verified server side |

### Contemplation & Resilience

Meditation, animal empathy, breathing and non-absolute environmental protection.

| Suggested rank | Native node | Prerequisite | Status |
| ---: | --- | --- | --- |
| 1 | Unarmored Poise | monk root | **Live pilot** |
| 4 | Breath Meditation I | unarmored poise | Planned |
| 6 | Animal Friendship | breath meditation 1 | Planned |
| 6 | Hunger Discipline License | breath meditation 1 | Planned (Tyz license) |
| 7 | Fire Resilience License | unarmored poise | Planned (Tyz license) |
| 9 | Explosion Resilience License | unarmored poise | Planned (Tyz license) |
| 10 | Breath Meditation II | breath meditation 1 | Planned |
| 12 | Creeper Kiss | animal friendship | Planned |
| 12 | Purifying Meditation | breath meditation 2 | Planned |
| 13 | Lava Survival | fire resilience license | Planned |
| 20 | Stillness Paragon | purifying meditation | Planned; gate: verified chaos guardian milestone |

### Empty-Hand Harvest

Real finite harvest tiers and distinctive tool-free gathering while repeatable mining speed stays in Tyz.

| Suggested rank | Native node | Prerequisite | Status |
| ---: | --- | --- | --- |
| 1 | Stone Fist | monk root | **Live pilot** |
| 5 | Iron Fist | stone fist | Planned |
| 7 | Gentle Shearing | stone fist | Planned |
| 7 | Glass Touch | iron fist | Planned |
| 9 | Diamond Fist | iron fist | Planned |
| 9 | Gardening | gentle shearing | Planned |
| 9 | Sandforming | iron fist | Planned |
| 10 | Delicate Hands | glass touch | Planned |
| 13 | Fortunate Hands License | delicate hands | Planned (Tyz license) |
| 13 | Leafforming | gardening | Planned |
| 13 | Stoneforming | diamond fist | Planned |
| 14 | Netherite Fist | diamond fist | Planned |
| 18 | Exotic Fist | netherite fist | Planned; gate: discover actual pack tool tiers and effective drops |
| 18 | Supermassive | netherite fist | Planned |

### Arcane Mastery & Licenses

Only faction-specific one-time/progression permission, not a second generic mana or spell-stat wallet.

| Suggested rank | Native node | Prerequisite | Status |
| ---: | --- | --- | --- |
| 5 | Monastic Mastery Learning License | monk root | Planned (Tyz license) |
| 10 | Focused Advancement License | monastic learning license | Planned (Tyz license) |
| 11 | Magical Spirit | spirit projectile | Planned; gate: spirit projectile enabled |
| 12 | Telepathic | monastic learning license | Planned |
| 14 | Acidic Spirit | magical spirit | Planned |

## Separate rules that must not get lost

- **No Creative Flight skill.** Flight comes from permitted Draconic Evolution Curios. Momentum Gliding, once implemented, is a different ability: controlled non-creative fall-flying with thrust after verified Ender Dragon participation.
- **No Apotheosis Unbound.** All **15** selected, unique, one-time Apotheosis affix behaviors have a place in the blueprint; the 43 attribute aliases and 40 bounded Tyz-owned affix ranks are deliberately not cloned here.
- **Finite harvest tier vs repeatable harvest speed.** Stone Fist is live. Later Iron, Diamond, Netherite and *optional* exotic hand tiers must reflect real pack mining requirements. General mining efficiency belongs to Tyz, not more fixed native purchases.
- **No free global immunity.** Fire/Lava, explosions, cleansing and arrow catching require limited mechanics and real defensive event hooks.
- **No compulsory checklist of the original Monk's twenty stunts.** Most rank XP remains from real block breaking, fighting and food variety; special deeds can be optional challenges and relevant unlocks.
- **Native skill licenses aren't Tyz points.** Buying a license doesn't silently purchase a numeric Tyz level, manufacture shared MXP, or force another purchase of a universal spell-power stat.
- **New capstones are proposals.** Empty-Hand Paragon and Stillness Paragon are reserved names/effects for discussion; neither is approved as a working perk.

## Making blueprint nodes active later

Before converting any planned node into an installed `skill_nodes` and `configured_skill_tree` resource:

1. Implement and test its actual server-side effect with the Monk equipment/faction gate.
2. Validate native Vampirism skill-point purchase, respec, save/restore and client display on the current six-skill test.
3. Add the necessary Java skill registration and stable internal skill ID; add localization/descriptions and any dedicated cross-mod event adapter.
4. Enforce **real minimum rank, prior skill, and world gate** on the server; avoid unlock predicates that only make the client look locked.
5. Update the canonical blueprint entry to `live` and extend the CI test's list of permitted active skills, then confirm the combined server smoke and disposable client purchases.
6. For a Tyz license, coordinate with the **primary** Tyz/MXP implementation and verify purchase/refund/direct grant restrictions without a duplicate wallet.

**Near-term test safety:** No blueprint-only JSON lives in `src/main/resources`; the currently active skill tree still contains the root plus Open Palm, Unarmored Poise, Stone Fist, Fleet Footwork and Step Assist. The next test can distinguish existing broken purchase behavior from future planned abilities.

## Sources

- [Original Monk audit](../../../docs/MONK-ORIGINAL-AUDIT.md): baseline milestones and 25 registered abilities, including historical flight and blindness exceptions.
- [Skill placement v2](../profiles/skill-placement-no-pufferfish-v2.json): 17 native technique candidates, seven Monk-only Tyz licenses and 15 one-time affix adaptations.
- [World tiers](../profiles/world-tier-unlocks.json): verified milestone targets; no claimed completion logic.
- [Current pre-tree QA matrix](../NEXT-QA-2026-11-15.md): player tests required before expanding active purchases.
