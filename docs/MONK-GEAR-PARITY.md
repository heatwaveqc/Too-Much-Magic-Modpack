# Monk gear-equivalence and infinite progression contract

Date: 2026-11-14. **Owner's design direction; source inventories committed;
gameplay not implemented.**

## The hard profile boundary

**The original Monk Mod** is a separately preserved historical reference:
RWTema's Minecraft 1.12.2 Monk 1.4, 21 configured milestones 0–20,
one named deed per milestone and a finite package of unarmed/unarmored powers.
This classic-style profile is the intended **standalone default** once the
new addon is playable; it is currently only a reference specification. The
original author's published code is source for factual study, not the
source for our copied assets or code.

**The Too Much Magic Monk** must be explicitly selected by the pack and
is not intended to be balanced to that original
mod. It is a new endgame path based on a Vampirism 1.10.13 playable faction
and JSON skill tree, supplemented by an independent **repeatable mastery
investment backend**. It must stand alongside top-end Abominations,
Archliches and Dracoliches with the pack's best enchanted and Apotheosis-
enhanced Gem Armor and weapons. It is not enough to compete with vanilla
Netherite equipment. Monk cannot use regular tools, conventional weapons
or armor, and forgoes those transformations.

Curios **are allowed**, including the Draconic Evolution chestpiece in the
body Curios slot when confirmed working on the exact pinned build.
Any chestpiece modules and other Curios powers actually available to the
Monk are **not** counted as absent gear to compensate. Draconic Evolution's
Curios support and individual module behavior must be tested, not assumed.

## Three distinct advancement surfaces

1. **Monk faction progression**: entry via sunset meditation; rank/mastery
   experience from qualifying breaking, fighting and unique food; only a few
   hard deeds. Faction maximum is **configurable at registration** via the Monk STARTUP
   config (default 20, previously provisional 14). Native Vampirism uses
   the registered highest level to validate faction levels, but it is
   not itself a cap on independently stored repeatable attribute purchases.
   Cap changes require a full restart and identical client/server config. Faction UI might require a Monk-specific upgrade tab or
   button; Vampirism JSON skill nodes are not arbitrarily repeatable.
2. **Apotheosis affix unlocks**: **all 99 pinned built-in affix definitions**
   appear on the target coverage inventory, including offensive, defensive,
   breaker, ranged, shield, and shears classes. Every affix must have a
   reviewed Monk effect analogue, with clear triggering conditions, unlock
   location and testing. Item-only/no-op effects need explicit redesign
   rather than silent omission. Affix JSON defines effects on ITEMS;
   it cannot simply be attached to a naked player as a skill.
3. **Attribute investment**: arbitrary purchases in every applicable
   numeric **player** attribute across vanilla, NeoForge and compatible
   mods, including all applicable Apothic Attributes and magic-mod
   attributes. No arbitrary **trait rank** limit; increasing cost per
   investment and underlying engine value bounds are separate concerns.
   Boolean/one-time properties become unlocks, not meaningless numeric
   ranks. Runtime allow/exclude policies must be audited; do not expose
   internal or mob-only attributes without validating player semantics.

Inventory pointers:
- `compat/monk/profiles/original-monk-1.4.json`
- `compat/monk/profiles/too-much-magic.json`
- `compat/monk/profiles/affix-inventory-apotheosis-8.9.0.json`
- `compat/monk/profiles/attributes-apothic-2.11.0.json`

## Attribute engine rules and nontrivial ceilings

Apothic Attributes 2.11.0 provides 22 built-in custom attributes, including
Armor Pierce/Shred, Arrow Damage/Velocity, Cold/Fire Damage, Crit Chance/
Damage, Current-HP Damage, Dodge, Draw Speed, Experience Gained,
Ghost Health, Healing Received, Life Steal, Mining Speed, Overheal,
Projectile Damage, Protection Pierce/Shred, Elytra Flight and Cooldown
Reduction.

- Numeric `Attribute` objects have native minimum/maximum values.
  A **repeatable purchase** doesn't imply infinite effective power; a player
  approaching the native limit must see the real effect and must not pay
  for invisible capped benefits. Design overflow/alternate benefits for
  chance-based and otherwise bounded attributes before release.
- `apothic_attributes:cooldown_reduction` specifically caps at 0.95 (95%).
  `apothic_attributes:elytra_flight` is boolean, not a per-rank boost.
  `apothic_attributes:experience_gained` affects XP from kills/ore
  under upstream rules, and is **not** the separate Monk Mastery-XP system.
- Vanilla/NeoForge examples for review: max health, armor, toughness,
  attack damage, attack speed, movement/swim speed, jump strength,
  step height, knockback resistance, luck, reach, mining efficiency,
  gravity. Don't assert all are included without reading the combined
  pack's actual player attribute registry.
- Some weapon-focused attributes or affixes would do nothing for an
  unarmed character: Arrow Velocity, Draw Speed, Cleaving, and similar.
  For each, either provide a specific Monk projectile/unarmed analogue
  or design an alternate benefit **with the unlock still represented**.
- Backend should persist purchased rank counts (e.g. long precision),
  use **one stable namespaced attribute modifier per trait**, recalculate
  on every login/reload/respec, and synchronize client-facing values.
  Use actual API registration and safe modifier replacement, not additive
  modifiers per purchase. No daily ticking through the entire roster.
- Do not reinterpret simple enchantments as universal attributes when
  game events are necessary (Silk Touch, Fortune, projectile deflection,
  environmental protection, area mining, life preservation).

## Compensation matrix

| Gear-equipped endgame character possesses | Monk counterweight | Notes |
| --- | --- | --- |
| Armor enchantments and vanilla gear attributes | Natural armor, toughness, protections, resistance, regeneration | Environmental, magical and bypass damage need independent testing |
| High-rarity Apotheosis armor, weapon and tool affixes | Individually unlockable Monk analogues from **all 99** definitions | Need effect adapters, not fake inventory equipment |
| Socketed Apotheosis gems and their conditional effects | Equivalent innate/martial/mystical progression opportunities | Gem effects are separate from the 99 affix definitions |
| Melee and ranged weapon enchants | Unarmed combat, ki projectile and effect techniques, loot acquisition | Include damage, reach, sweep, knockback, looting, on-hit properties |
| Mining/tool enchants and utility | Bare-hand harvest rank, mining multiplier, Fortune/Silk Touch analogue, AOE mining | Permission and modded-block rules need explicit tests |
| ProjectE Gem Armor and its special powers | Extreme defensive, environmental and mobility techniques | Only compensate what isn't shared through Curios |
| Draconic Evolution chestpiece body Curio | **No compensation where Monk actually retains function** | Test flight-module/energy-module differences in Curio slot |
| Other Curios and accessories | **No compensation** if Monks can use the same gear | Compare equal accessory loadouts |

## High-end parity procedure

Benchmark Monk versus Abomination/Archlich/Dracolich in **the same world
tier, with equal Curios loadouts and comparable playtime/rare resources**.
Evaluate both build extremes and flexible builds:

- Damage to single targets and bosses; burst, sustain and defenses.
- Survival against magical, environmental, armor-bypassing and true
  damage; death protections and recovery.
- Unarmed mining speed, correct drop tiers, Fortune/Silk Touch, AOE mining,
  ore efficiency and boss-gated harvest locks.
- Mobility: flight, blink, water traversal, movement, fall protection.
- Magic: school-specific spell power, casting speed, cooldowns, mana/
  essence regeneration where relevant, casting in adverse conditions.
- Utility: looting, luck, experience, farming, inventory and crafting
  integration; Curios shared capabilities counted on both sides.

The goal is **endgame viability at comparable total investment**, not
identical DPS and not a requirement that every Monk exceed every
gear-based class in every category. No numbers are locked until tests.

## Implementation gates

1. Build read-only player-attribute registry dump from the actual
   integrated pack and categorize player-applicable numeric, bounded,
   boolean, derived, equipment-specific and internal attributes.
2. Read every pinned Apotheosis affix definition (99 in this upstream
   source snapshot); map to a real Monk action/effect. Count missing
   adapters in a generated coverage report: **target 99/99**.
3. Build a server-authoritative upgrade backend outside the unique-node
   Vampirism GUI; identify unlock, cost and repeatable investment in UI.
4. Confirm equipped Curios body-slot Draconic chestpiece capabilities
   before defining Monk compensation for those effects.
5. Define robust gear restriction/allowed focus tags. Never delete
   equipped items; suspend abilities if noncompliant.
6. Run parity tests on disposable worlds with actual fully enchanted
   and affixed top-end items, using identical Curios and appropriate
   enemy damage scales; inspect anti-farming/progression exploits.
7. Only then author reward curves and enable the TMM profile.

No profile files under `compat/monk/profiles/` are actively loaded or
enforced in-game; they are **design-only machine-readable specifications**.

## Direct upstream sources (matching pinned 1.21.1 branches)

- Apotheosis 8.9.0: https://github.com/Shadows-of-Fire/Apotheosis/tree/1.21
- Apothic Attributes 2.11.0: https://github.com/Shadows-of-Fire/Apothic-Attributes/blob/1.21/src/main/java/dev/shadowsoffire/apothic_attributes/api/ALObjects.java
- Curios body-slot chestpiece description: https://www.curseforge.com/minecraft/mc-mods/draconic-evolution

## Post-inventory architecture decisions still open

The Monk's Vampirism JSON tree is the **single-level eligibility/unlock
layer**. For repeated numeric upgrades, Pufferfish Skills is now a candidate
alongside a standalone Monk purchase backend; the choice is **not yet made**.
Pufferfish's standard `skills.json` nodes store unlock state only, while
`experience.json` can manage independent category levels and XP. Verify
whether a suitable native or API-backed per-trait rank exists before
assuming Pufferfish can represent infinitely repeatable purchases.

Classification principle: boolean/fully effective capped upgrades must
cease accepting payments at the point of maximum effect; repeatable numeric
upgrades may continue with rising costs where the underlying mod permits.
An attribute like Attack Speed requires practical engine-tick testing in
addition to its declared numeric range.

A proposed **Apotheosis-rarity infused food** converts rarity materials into
fixed, tier-based Monk XP on consumption. It is separate from one-time
unique-food discovery, and uses the pinned five-tier rarity registry. See
`docs/MONK-MOD.md` and `compat/monk/profiles/rarity-food-infusion.json`.

## Original Monk completeness and mining (2026-11-14)

Every one of the original mod's **25 registered ability types** is
present in the new
`compat/monk/profiles/original-monk-abilities-1.4.json` crosswalk.
Original defaults used 21 of those; optional Armor Toughness, Health,
Glass Touch and Full Water Breathing are retained. In TMM, full
Creative Flight is an **external Draconic Evolution Curio capability**,
not a Monk skill point purchase or capstone; the temporary Blindness
trial is a deed/handicap rather than a positive ability.

Unarmed mining has **two orthogonal tracks**: finite harvest tier up
to the highest legitimate tool requirement in the installed pack, and
independently repeatable harvest **speed** with rising cost. Speed
does not grant loot or override required tool tiers. Dragon Survival
already has separately modeled harvest strength and speed, offering
an implementation precedent without coupling the mutually exclusive
Monk and Dragon forms.
