# Standalone progression chassis — Monk and Hunter capability contract

**As of 2026-11-15.** Target Minecraft 1.21.1 / NeoForge 21.1.265. This audits the **shared engine**, not a claim that either complete class is playable. See `compat/progression/README.md` for JSON and commands.

## Current implementation (November 15 follow-on pass)\n\nPR #127 now contains actual server-side First Sunset and manual Hunter enrollment, Blink, frontal arrow catches, bound-armor wear and first-slot no-loss relocation, pickup backstop, persistent power modes, Glass Touch and Fortune/Silk drop replacement, 20-rank Monk baseline pilots, breath/purify/hunger/jump/step/water/gaze, creeper pacification, retaliation-aware target neutrality, Ender Dragon gated noncreative Momentum Glide and optional blindness ordeal. A serverbound five-action keybind packet is available, but the visual tree GUI and server-to-client rank state sync are absent.\n\n**These are code pilots, not acceptance-complete mechanics.** Strict inventory-click interception for all vanilla/modded interfaces, Forge claim permissions, modded tier drops, client physics/lag and complete saved-data and multiplayer edge tests remain. The matrix below records the original contract and its remaining verification requirements; entries describing a callback as missing refer to the initial skeleton before this pass.\n\n## Ownership boundary

- **Native XP**: discipline-specific, server-authoritative. Not vanilla orb XP and not Tyz/MXP/SP. `rank_xp`, `max_level`, `points_per_rank`, `activities[].xp`, `milestones[].xp/points` and optional skill prices are JSON-configurable.
- **Mandatory baseline**: `auto_ranks` grants native powers automatically. No branching choice or missing point budget can permanently hide the required 25 classic Monk powers.
- **Optional branches**: `requires_skills` (AND), `requires_any_skills` (OR), `choice_group` (one skill per group), `required_rank`, `requires_milestones`, per-skill `max_rank` and level-based points. Server validates purchase and refunds paid ranks on respec. A visual client tree and networking are still absent.
- **Script-safe extension**: five initial built-ins plus new damage, gear, and animal handlers; further powers register namespaced Java effect handlers. JSON is data, not executable code. New event families require trusted server listeners.
- **Species/faction**: no TeamLapen faction, Vampirism, JAWR or Tyz dependency. External class, form and creature-neutrality arbitration must respect actual player's form and retaliatory context; raw Hunter/Monk path enrollment alone does not infer faction.

## Implemented in shared engine (subject to gameplay QA)

| Requirement | Mechanism | Status |
| --- | --- | --- |
| Flexible native XP/level cap | `rank_xp`, `max_level`, `activities[].xp`, cooldowns, milestone XP | Implemented/parser-tested |
| Kill-credit and target-tag XP | `LivingDeathEvent`, `kill`, registry IDs/tags | Immediate player-source only; summons/spells/anti-farm bridges not done |
| Food variety mastery | `LivingEntityUseItemEvent.Finish`, `food_consumed`, `food_discovered` | Item-ID first-use persisted copy-on-death; verified custom-food compatibility still needed |
| Conditional base/game mod attributes | `attribute`, `operation`, `per_rank`, optional unarmored/empty hand | Implemented; real modded attribute IDs need verification |
| Damage against entity tag / while holding weapon tag | `melee_bonus` and `damage_bonus` | Melee, bow/crossbow Arrow weapon snapshot; custom spell and summon ownership not yet covered |
| Durability damage or repair on equipped gear | `gear_durability`, `target` item selector, `params.slot/action/interval_ticks` | Periodic, rank-scaled and native damage; Curios/crafting/usage-level conservation not yet covered |
| Harvest speed and mining tier | `break_speed`, `harvest_tier` | Vanilla-tier pilot, not modded mining tiers or legitimate loot mode |
| Animal pet-to-breed | `animal_friendship`, server-side `EntityInteract`, adult `Animal#canFallInLove` | Basic love-state implementation; special modded animals and protected interactions untested |
| Branching skill prerequisites | `requires_skills`, `requires_any_skills`, `choice_group` | Server graph and choices; no client tree UI |
| Special power extension points | `EffectHandlers.Handler` | Tick, incoming damage, harvest, break speed, pre-break, death, food, entity interaction, block drops, projectile impact, equipment change, jump, target change |

## Monk — full classic parity audit

Authoritative baseline: `docs/monk/STANDALONE-PRE-TYZS-CLASSIC-TARGET.md` / 25-entry manifest from PR #100.

| Family / requirement | Core support | Specialized Monk behavior still required |
| --- | --- | --- |
| Iron Palm, natural armor/toughness, vitality, fire/blast/fall damage | Configurable `melee_bonus`, `attribute`, `damage_multiplier` | Proper equipment and boss stacking tests, actual late-game balancing |
| Hand mining speed and tier | `break_speed`, `harvest_tier` | Extra modded tiers, true post-break attribution, TreeChop/magic attribution |
| First Sunset, rank milestones | External event / one-time milestone / join APIs | Server-validated 200-tick sunset and non-OP enrollment |
| Animal Friendship | Added `animal_friendship`, entity interaction | Original animal calming, unusual modded breeding / permission edge cases |
| Glass Touch; Treasure Hand Fortune/Silk toggle | `blockDrops` extension hook | Safe legal loot-context implementation, player toggle state and networking, no duplication |
| Catch Arrows | `projectileImpact` extension hook | Facing cone, pre-impact cancellation, pickup legality/Infinity handling |
| Jump, Step Assist, sprint speed | `jump`, tick and attributes | Exact movement physics/crouch logic and client prediction |
| Blink, Water Walk, breathing, Withering Stare | Tick/damage/item/entity callbacks | Native collision/teleport/gaze/breath and protected-target mechanics |
| Hunger Discipline, poison cleanse, lava protection | Food event, tick and damage hooks | Food exhaustion and effect-filter behavior |
| Creeper Kiss, Blindness Ordeal | Entity interaction / persistent rank and milestones | Permanent per-creeper pacification; ordeal state, abort and reset lifecycle |
| First hotbar reserved fist, Armor Lock, Binding wear | Equipment callbacks, native `gear_durability` | Slot/interception/relocation rules; toggle persistence and safe re-entry; use built-in wear after lock state checks |
| Ender Dragon -> Momentum Gliding | Advancement event, external trust API, tick hook | Verified dragon credit, client input/network, native gliding control, no Creative flight |
| All 25 simultaneously attainable | Mandatory `auto_ranks` separate from paid choices | All missing effect implementations and 25-case acceptance suite |

No Tyz skill, Curio or removed TeamLapen mod may be required to complete Monk baseline.

## Hunter — source-inspired abilities, not TeamLapen world content

The older Vampirism Hunter class demonstrates leveling, branching skills, weapon/faction specialization and gear gating. Its actual NPC trainers, faction minions, forts/villages/raids and worldgen are **explicitly excluded** by the pack's TeamLapen retirement decision. The new Hunter uses its own native XP and optional branching tree, and its species/faction neutrality is managed by pack-specific policy rather than the removed Vampirism faction API.

| Hunter ability family | Common engine now | Still needed |
| --- | --- | --- |
| Creature-type kills / research milestone / bonus damage vs quarry | `kill`, tags, milestone counts, weapon-tag `damage_bonus` | Authoritative tag inventory and ownership, kill legitimacy, boss exceptions |
| Sword, axe, crossbow or other tag-specific damage | `melee_bonus`, `damage_bonus` | Verify modded ranged/projectile weapons; cross-mod spell damage API |
| Equipment care and endurance | `gear_durability` repair/wear, attributes, damage resistance | Genuine durability-consumption prevention, ammunition-saving, per-use rules |
| Skill branches / specialist style | `requires_skills`, `requires_any_skills`, `choice_group` | Visual tree with node coordinates and UI sync |
| Camouflage, tracking / sensing, stake execute | Tick/damage and event hooks | Bespoke targeting/stealth/execute handlers with player-PvP and boss safeguards |
| Faction neutrality and hostile pursuit | `targetChange` extension callback | Actual form-neutrality/arbitration logic, retaliation and boss exemptions |
| Higher-tier Hunter tools/recipes and rank gates | Enrollment/rank APIs available | Workbench/recipe and item-use permission integration; no NPC or village reintroduction |

Authoritative creature taxonomy from PR #97 is **not** the two vanilla pilot tags under `tmm_progression`; a separate tag generation and QA pass is required for the recorded 477 candidate creatures / 1,393 registry IDs.

## Non-negotiable release gates

1. All Gradle/JUnit and branch/JSON-negative tests pass, plus a dedicated NeoForge server startup with standalone mod JAR and both datapacks loaded.
2. Join/leave, save/death/clone/relog/dimension, respec, reload, dropped/removed datapack paths, two players, explicit achievement grants and malformed config are tested.
3. No false XP from canceled/claimed breaks, placed-block loops, summoned or ownerless pets, automation, creative/spectator, and duplicate item data variants.
4. Hostile and friendly mob tags validated against exact pinned mod version/runtime registrations; neutralities permit retaliation.
5. Monk all-25 classic parity plus 3 free traits separately tested without Tyz/Vampirism/Werewolves, including endgame gearless Chaos Guardian combat.
6. Hunter damage includes modded weapon categories, rank cap, branch/price/point integrity and actual multiplayer target attribution.
7. Client UI and toggle packets are authoritative, validated server-side, synchronized, do not trust client XP/skill ranks, and do not duplicate gear/loot.

**This PR is an engine-extension pilot, not a production class release.**
