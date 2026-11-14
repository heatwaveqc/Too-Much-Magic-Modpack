# Original Monk mod audit and TMM progression proposal

**Date:** 2026-11-14. **Status:** source-backed design proposal, **NOT implemented**.
The owner requested sunset initiation, mostly activity-earned advancement, only a few
mandatory deeds, and the original Monk abilities/deeds as the design baseline.

## Authority and correction

- Original mod is RWTema's archived 1.12.2 **Monk Mod 1.4**:
  https://github.com/rwtema/Monk-Mod
- **Authoritative default configuration is**
  [MonkConfiguration.genDefaultConfig](https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/config/MonkConfiguration.java),
  **not** the mostly commented-out historical `MonkLevels.java`.
- Initial progress in the original default configuration was to break **one wooden log
  bare-handed at level 0** and then **ten** for level 1. Meditating while looking at
  **sunrise or sunset** was the **level 2** deed, not initiation. Original mod page:
  https://www.curseforge.com/minecraft/mc-mods/the-monk-mod
- The original [Factory.java](https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/factory/Factory.java) sets **10 × 20 = 200 ticks**, so the proposed 10-second initiation matches its actual duration.
- The original sunrise/sunset handler
  [MonkRequirementSunriseSunset.java](https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/advancements/criteria/MonkRequirementSunriseSunset.java)
  computes celestial angle, checks sun near horizon (abs(cos(angle)) < 0.1), and
  checks the player's gaze dot product > 0.99. It resets consecutive tick progress
  on failure. It does **not** test an unobstructed ray to the sun, sky visibility,
  or weather. We must add those where appropriate.
- Original mod is All Rights Reserved on its CurseForge listing. The new
  implementation must be independent; do **not copy original code or art**.

## Original defaults: deeds and powers

Numbers correspond to original **configured milestone 0 through 20**, which
is not directly the same as Vampirism faction level 1 through 20. Items in the
right column are awarded by the original defaults; multiple bonuses at some ranks.

| Original | Default deed | Main rewards |
| --- | --- | --- |
| 0 | Break 1 wooden log bare-handed | Starts monk progression |
| 1 | Break 10 more wooden logs | Basic hand mining, unarmed +1 |
| 2 | Watch sunrise/sunset | Lower hunger use, natural armor +1 |
| 3 | Dive underwater / face drowning | Partial water breathing, armor +2 |
| 4 | Break 5 stone blocks | Stronger hand mining |
| 5 | Pet 20 animals | Animal friendship, armor +4 |
| 6 | Sprint 200 blocks | Movement speed, jump, unarmed +2 |
| 7 | Kill 10 undead | Unarmed +4, armor +8 |
| 8 | Dodge 5 skeleton arrows | Catch arrows, speed, step-up |
| 9 | Break 4 iron blocks | Stronger hand mining |
| 10 | Meditate under moon from boat for 10 seconds | Walk on water |
| 11 | Meditate near calm Endermen | Blink teleportation |
| 12 | Walk through fire for 10 blocks | Fire damage reduction, lava protection |
| 13 | Break obsidian bare-handed | Higher-tier hand mining, armor +14 |
| 14 | Kiss a creeper | Creeper interaction, blast damage reduction |
| 15 | Hold gaze on hostile wither skeleton | Withering stare |
| 16 | Survive a 40-block fall | Feather fall, armor +18 |
| 17 | Sleep on bedrock | Very high hand mining, strength, blindness |
| 18 | Kill 5 hostile mobs while blind | Greatly improved unarmed damage, blindness removed |
| 19 | Defeat a Wither | Harmful-effect immunity, armor +20 |
| 20 | Survive a special sequence of void falls | Flight |

### Candidate power families for our JSON tree

| Branch | Original inspiration | Tentative TMM adaptation |
| --- | --- | --- |
| Martial Discipline | Strength, arrow catch, step-up | Unarmed damage, defense/counterattack, projectile parry |
| Body & Mobility | Mining, speed, jump, feather fall, water walk, blink, flight | Hand-based harvest, movement, traversal, safe short blink, late flight |
| Contemplation | Animal friendship, breath, hunger, fire, wither stare, potion immunity | Meditation and resilience, withering-gaze and cleansing **limited** by balance |
| Arcane Mastery | **NEW TMM identity, not present as such in original** | XP boosts, additional skills, magic-school proficiency and cross-mod spell enhancements |

**Balance differences:** No full no-cost immunity to every negative effect,
no unrestricted maximum mining tier, and no direct blanket flight capability
override until compatibility with other mods is verified. Original hunger
reduction is a poor fit with rewarding food variety; use food appreciation/
digestive benefits instead. Original void-fall challenge intentionally interferes
with death/revival and should not be ported unchanged.

## Proposed initiation: First Sunset

This is an **owner-proposed direction**. Specific thresholds and edge policies
remain playtest-tunable, not approved hardcoded constants.

1. In the Overworld (or a curated dimension with an actual sun), player is
   human/untransformed, neutral to Vampirism factions, and not wearing
   conventional armor or holding a conventional weapon. Special spell implements
   and accessories require a later equipment-tag policy.
2. Detect **sunset only** in a server-authoritative time/sky window
   (provisional around dayTime modulo 24000 = 12000). Use a 3D sun direction
   and a reasonably narrow gaze cone (~8 degrees inspired by original).
   Raycast/sky-visibility check must disallow staring through a roof or wall.
3. Accumulate ~200 consecutive server ticks (~10 seconds) while the sun is
   actually setting. On gaze loss or eligibility loss, reset/reduce progress;
   disconnects/reset must be safe. Prevent another faction/physiology
   changing during the final tick using a pre-transition guard.
4. Grant level 1 in `tmm_monk:monk` **exactly once** through Vampirism's
   faction handler, with server-to-client synchronization and advancement.
   Do not grant it through a detached shadow record or client packet.
5. Give clear subtle progress feedback and descriptive hint to an uninitiated
   player without requiring a crafting item or quest reward.
6. Test weather, horizon mountains, water, sky/light obstruction, dimensions
   with no sun, time manipulation, lag, two clients watching simultaneously,
   and save/reload. Provide operator repair/testing command, not public bypass.

## Proposed rank advancement: earned mastery plus a few deeds

Separate **Monastic Mastery XP** (determines Monk ranks and unlock eligibility)
from vanilla XP and from **Vampirism skill points** (spent on the JSON tree).
Rank rewards can grant *more skill points per level* than the usual factions.
Vanilla XP gain bonus is a distinct perk/balancing question; don't mix the
two XP currencies in implementation.

Mastery XP sources:
- **Breaking blocks:** valid player-driven completion with tool-free/qualified
  hand use; more value for valuable and new materials. Verify actual successful
  break and respect protected areas/claims. Prevent placed-block loops or use
  severe diminishing returns per block class; avoid assuming natural generation
  can be identified just from block ID.
- **Killing enemies:** server-verified player credit, difficulty/health weighting
  and small diminishing returns for repeating easy mobs. Exclude owned summons,
  player-farmed dummy targets and no-participation kills. Allow meaningful
  combat from designed Gateways/Cataclysm where fair.
- **Eating new foods:** award a meaningful **one-time** bonus per distinct
  edible **item registry ID** per character, following actual successful
  consumption. Save a persistent per-player consumed-food set. Ordinary NBT
  variations must not mint infinite unique-food credit; handle foods that
  can be consumed at full hunger or award potion-like effects explicitly.
  Pam's HarvestCraft 2, Farmer's Delight and Aquaculture should matter.
  Re-evaluate duplicate IDs/recipe unification before release.

Suggested tuning, NOT fixed values: a gradually rising level threshold;
no XP farm of a single block or mob type should dominate; food diversity
provides a satisfying finite bonus, not a mandatory exhaustive catalog.
A high-level player should be able to specialize in multiple activity streams
without compulsory completion of every original Monk stunt.

**Proposed mandatory deeds:** initiation at sunset, one major mid-course
trial (e.g., moon meditation or projectile mastery), and one capstone
trial (e.g., encounter/meditation challenge). These are examples pending owner
choice, *not* a list of forced gates. The other original deeds can become
optional feats that grant Mastery XP, unlock specific skills, or appear in
FTB Quests without blocking every Monk rank.

### Technical event hook map (research candidates)

- Sunset: server player tick, dimension/time checks and line-of-sight.
- Mining: NeoForge `BlockDropsEvent` or post-verifiable player block-break
  capture; beware `BlockEvent.BreakEvent` reports attempted breaks and may
  be cancelled, and some modded blocks have no drops.
- Combat: `LivingDeathEvent` / experience events, player attribution from
  `DamageSource` and effective difficulty/anti-farm flags.
- Food: `LivingEntityUseItemEvent.Finish` with real food metadata and the
  original consumed stack, **not** simply right-click.
- Persistence: Vampirism faction level, skill handlers and Monk attachment
  must remain single-authority; place Mastery XP and eaten-food set in
  the Monk attachment with versioned serialization and copy-on-death.

All hooks are candidates, not production code. Validate them against the
actual 1.21.1 NeoForge pinned release and running pack. The bridge must
pre-check other transformations and guard bidirectional races.

## Test and promotion checklist

- [ ] Single initiation exactly once, no duplicate grant on rejoin.
- [ ] Non-Monk, Vampire/Hunter/Werewolf and any external transformed states
      cannot bypass initiation or acquire the excluded physiology after joining.
- [ ] Track accumulated XP after each source on dedicated server, handle
      block break cancellation, protected chunks and modded resources.
- [ ] Player-placed blocks, summoned mobs, mob farms, fake players, duplicate
      food identities and automated consumption do not award unlimited XP.
- [ ] Save/load, death, dimension travel, offline state and multiplayer
      isolation preserve rank/food history without skill-point duplication.
- [ ] Skill point grants, respec, user interface, source bonuses and equipment
      eligibility behave correctly at each rank.
- [ ] No unauthorized copying of original code or artwork.

## Technical references

- Original default config: https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/config/MonkConfiguration.java
- Original meditation check: https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/advancements/criteria/MonkRequirementSunriseSunset.java
- Original wear rules: https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/abilities/MonkAbility.java
- Original void-fall death avoidance: https://github.com/rwtema/Monk-Mod/blob/master/src/main/java/com/rwtema/monkmod/advancements/criteria/MonkRequirementDescentAscent.java
- Werewolves 1.21 source pattern: https://github.com/TeamLapen/Werewolves/tree/1.21
- NeoForge 1.21.1 event docs: https://docs.neoforged.net/docs/1.21.1/concepts/events/
