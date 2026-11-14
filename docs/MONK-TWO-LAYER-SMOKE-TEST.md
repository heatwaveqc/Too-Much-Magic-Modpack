> **SUPERSEDED two-layer prototype design (2026-11-14).**
> Faction unlocks still license leveled skills, but **Tyz's Skills**
> has been selected for purchased levels, not Pufferfish or
> Improvable Skills. Pufferfish retains nonoverlapping one-time
> nodes. This older smoke test is preserved as engineering history.
> Use [current Tyz acceptance tests](MONK-TYZS-TIERED-ADVANCEMENT.md).

> **Backend update, 2026-11-14:** The Pufferfish repeatable-skill purchase
> hypothesis is not proven. First prototype should compare already-installed
> **Improvable Skills 3** (real per-trait ranks, 32766 short upper cap,
> server-side `canUpgrade`) as an alternate. Critical scroll bypass and
> vanilla XP currency issues are in
> [MONK-SKILL-BACKEND-EVALUATION.md](MONK-SKILL-BACKEND-EVALUATION.md).

# Monk faction + unified Pufferfish skills: technical smoke-test proposal

**Status 2026-11-14: owner-proposed architecture; not a runtime
implementation or completed smoke test.**

## Goal

The player should see **what a Monk can learn** in the native Vampirism
faction tree and **how proficient a character has become** in the
pack-wide unified Pufferfish stat tree.

Use these three separate truths:
1. `Monk faction` stores **identity** and **once-only ability unlocks**;
   e.g. Step Assist, Arrow Catch, Glass Touch, short Blink,
   after-Dragon Momentum Gliding. Each ability's action and native
   entitlement come from the Monk mod.
2. `Monk license` is one **native faction unlock** for each leveled
   discipline (or appropriately broad related set): grants permission
   to invest into the relevant Pufferfish skill or higher-tier branch.
   For example: Barehand Mining Discipline, Advanced Armorless Training,
   Universal Magic Capacity discipline, or critical prowess.
3. `Shared Pufferfish skill` is one **canonical upgrade** across
   every valid mod stat that represents the same benefit. Examples:
   Magic Capacity -> Ars max mana, Iron's max mana, Mahou max, Roots
   Terra; Mining Speed -> vanilla break speed + Apothic speed +
   permitted Monk barehand speed. Some systems only have personal
   attachment/command hooks and need adapters. Mod-specific school
   specializations and materially different resource mechanics do
   not collapse into a fake generic stat.

A shared Pufferfish core skill remains available to **non-Monks**. A
Monk's native license may unlock extra training/its **equipment-free
application**, but must never charge twice for the same universal rank.
The extra value of being Monk comes from innate powers, compliant-hand
mechanics, growth, specific additional access and lack-of-gear
compensation.

## First design acceptance examples

| Monk faction node | Pufferfish role | Why |
| --- | --- | --- |
| **Step Assist** (once) | None | Exactly one native movement/collision ability; `minecraft:generic.step_height` is a one-time applied trait for TMM Monk |
| **Momentum Glide** (once, after Ender Dragon) | None for unlock | Pufferfish must not independently grant Elytra access before the world gate |
| **Blink** / **Catch Arrows** (once) | Optional later improvements, only if we intentionally define them | Distinctive Monk event mechanics |
| **Barehand Harvest Discipline** (license) | Universal Mining Speed + finite tool-tier ranks, separate purchase rules | Speed must never authorize drops from a higher harvest tier |
| **Arcane Discipline** (license) | Universal Magic Capacity, Regen, Power, Cast Speed, Cooldown | One cross-mod investment per category; independent mana types |
| **Hardiness** (license) | Shared Max Health / Armor / Magic Protection | Same attribute as core tree; Monk uses unarmored effects, not duplicate reward |
| **Affix Technique: e.g. Cleaving** (license if leveled) | Finite tiers with distinct effective source values | Pufferfish nodes cannot repeat; generate only useful rank nodes after bridge gating |
| **Apotheosis Unbound** | **NO node in either tree** | Source effect is Creative Flight, and owner chose Draconic Curios as the source |

## What Pufferfish 0.19.x actually provides

Based on publisher's documentation (validate **exact pinned 0.19.2**
API and command syntax on the modpack's server):
- `category.json` has `unlocked_by_default=false` for a locked
  category. API/commands can unlock a category for one player.
- `skills.json` nodes are **unlocked/not-unlocked IDs**, not numeric
  rank counters. Neighbour connections and point costs control
  purchasing individual one-time nodes.
- `/puffish_skills skills unlock <player> <category> <skill>` exists.
  A **disconnected, externally force-unlocked prerequisite** might
  expose an otherwise unavailable Monk-only branch. **This is a
  hypothesis requiring server testing**, not an existing verified
  native "required Monk faction" condition.
- Pufferfish supports custom reward implementations via `SkillsAPI`,
  but that does not itself grant a repeatable UI or rank store.

**Implication:** normal finite skills can use one Pufferfish node per
effective rank after permission is granted. **Truly indefinitely
repeatable** skills cannot be represented as one ordinary Pufferfish
skill node. We must prototype (A) a custom repeatable Pufferfish-side
counter/UI with persistent ranks, or (B) a small custom purchase
backend integrated into Pufferfish's category/UX while sharing its
canonical effects. Neither exists today. **Do not generate hundreds
of thousands of single-use nodes** to fake an unlimited skill.

## Proposed sync and anti-exploit rules

- Server-authoritative entitlement = validated Monk faction state +
  native faction skill flag + any world-tier gate. Do not trust a
  client tooltip or bare FTB quest completion.
- On one-time Monk skill purchase, enable its native effect.
- On leveled Monk skill purchase, issue an **idempotent permission**.
  For a Monk-only Pufferfish subtree, grant a category or seed
  connection through official API/command if confirmed; a shared
  universal stat remains accessible to ordinary players.
- On ordinary Pufferfish skill purchase, apply **one canonical stat
  modifier** through cross-mod adapters. Do not apply duplicate
  attribute modifiers merely because multiple mods share the stat.
- On Monk native respec, remove permission and suspend Monk-specific
  effects; **preserve purchased Pufferfish ranks** for ordinary
  universal traits. For Monk-only specialized ranks, pause spending
  and effect while ineligible, but do not erase the saved progress.
- On Pufferfish respec, refund what was actually paid once; native
  Monk licenses remain purchased. Reconcile branch prerequisites.
- Reconcile at login, respawn, dimension change and config/data
  reload; enforce authorization during purchase (if API supports
  an appropriate callback), not only via tooltip hiding.
- Missing external mods disable only relevant adapters, not shared
  skills. Mod-type and player-instance detection must be real.
- Cost curves for bounded upgrades stop before native/effective caps,
  and repeatable ranks must not overflow integer storage/attribute
  maximums. Keep Monastic Mastery XP separate from universal Pufferfish
  skill experience unless explicitly designing a conversion.

## Minimum runtime smoke tests — before building 270 UI nodes

1. Start disposable dedicated server with pinned Vampirism, Monk,
   Pufferfish Skills 0.19.2 and Pufferfish Attributes 0.8.3.
2. Make a human Monk through a test command or real sunset gate when
   implemented. Verify native Step Assist works immediately with zero
   Pufferfish purchases.
3. Register a locked Monk-only Pufferfish category. Unlock on Monk
   initiation, then lock/suspend on legal departure. Test both
   dedicated and integrated client.
4. Build **one locked-off branch seed** for Barehand Mining Discipline.
   Purchase the native Monk license; verify API/command grants seed
   and makes one downstream Pufferfish upgrade available, and no
   earlier path or client packet bypasses the license.
5. Purchase 3 finite Pufferfish Mining Speed ranks and verify block
   break-speed changes while harvest tier/drop requirements remain
   unchanged. Test the same Pufferfish stat on a non-Monk wielding
   an allowed normal pickaxe; no extra duplicate modifier.
6. Native-respec the license and verify Pufferfish trait investment
   persists safely but Monk-only effects are suspended.
   Re-purchase native license; no free duplicate points or ranks.
7. Pufferfish-respec and verify only Pufferfish investments/refunds
   reset, not the original Monk one-time abilities.
8. Prototype an **indefinitely repeatable** rank and persisted rising
   cost without hundreds/thousands of generated nodes. If no
   practical interface/API exists, stop and choose a custom rank
   purchase panel before installing the full skill catalog.
9. Verify one bounded skill at its true cap (Cooldown Reduction),
   nonredundant affix ranks (Cleaving), post-Dragon Momentum Glide,
   and total absence of any Unbound Monk unlock.
10. Test logout/respawn, dimension travel, co-op players with different
    factions, server reload, and changing the set of optional mods.

**Pass condition:** all benefits above work without duplicating
universal skill stats or creating new manual UI workload for each
unbounded rank. If it fails, retain the conceptual separation
but use a custom investment module rather than claiming Pufferfish
supports native unlimited levels.

## Sources

- https://puffish.net/skillsmod/docs/creators/configuration/skill
- https://puffish.net/skillsmod/docs/creators/configuration/definition
- https://puffish.net/skillsmod/docs/creators/configuration/files/category
- https://puffish.net/skillsmod/docs/creators/commands
- https://puffish.net/skillsmod/docs/developers/reward
- [Unified skill ownership proposal](../compat/monk/profiles/skill-unification-proposal.json)
- [Current source candidate skills](../compat/monk/profiles/skill-catalog-v1.json)
- [Proposed 98 Apotheosis skills](../compat/monk/profiles/skill-affix-techniques-v1.json)
