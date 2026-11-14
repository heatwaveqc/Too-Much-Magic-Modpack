# Hunter and Vampire Provider Compatibility — 2026-11-13

STATUS: User-approved DESIGN rule, unimplemented. Minecraft 1.21.1 NeoForge. No transformation enforcement or gameplay smoke test is claimed.

## Decision: hunter is a modified-human physiology

- Vampirism Hunters receive a **Garlic Injection** at the special chair and are immune to Vampirism's Sanguinare infection in the upstream 1.10 branch. They also have their own faction and skill progression.
- Pack-specific extension: **all involuntary persistent species/undead transformations are blocked** while the Hunter state is active, including infections or bites from other mods (JAWR/other lycanthropy). This is NOT a claim of native JAWR immunity and requires a compatibility bridge with mod-specific entry points.
- **A deliberate transformation ritual can override Hunter protection.** A ritual is explicitly initiated and confirmed by the player; ambient bites, status effects, stray consumption, infection timers, etc. do not count. The transition must end the Hunter state BEFORE applying the selected new physiology. If unsuccessful, leave the Hunter intact.
- Ordinary magic schools, temporary transformation effects and armor/item access are NOT species conversions, and remain generally available.
- Native Hunter faction exit uses Sanguinare Injection at the chair. User-facing exit costs (Hunter levels, death, items) need a deliberate cross-mod progression policy so deliberate rituals do not create a free exploit; a confirmed ritual may remove Hunters without keeping Hunter-specific perks.
- Hunter is a protected human body, NOT a concurrent human + vampire/dragon/werewolf/lich hybrid. The earlier documents' purely professional classification is superseded by this rule.

## Technical implementation observations (upstream 1.21 branch)

- Vampirism 1.10 NeoForge documents Garlic Injection, hunter protection, and special injection chair: https://wiki.vampirism.dev/docs/1.10%20NeoForge/wiki/factions/getting-started
- Vampirism's player faction handler contains one active faction, not an unrestricted set. Its faction pre-change event is cancellable and can be used to enforce cross-mod conversion. HunterPlayer.canLeaveFaction() is true; quitting normally has progression consequences. The Sanguinare expiry path checks whether a player can become a vampire.
- Source: https://github.com/TeamLapen/Vampirism/tree/version/1.21/latest
- Native Hunter quit documentation: https://wiki.vampirism.dev/docs/1.10%20NeoForge/wiki/factions/hunter/hunter-player

## Vampirism vampire versus JAWR vampire

| Topic | Vampirism 1.10 NeoForge | JAWR 0.5.x NeoForge |
| --- | --- | --- |
| Fundamental gameplay | Faction-focused vampire society, territories, hunting, vampire NPCs, lords/minions | Witchcraft/occult vampire transformation and quest progression tied to Lilith, blood, rituals |
| Becoming vampire | Sanguinare infection by bite/fangs, then delayed conversion | Occult initiation/quest route; verify precise current 0.5.39 steps in game rather than assume classic Witchery is unchanged |
| Leveling | 14 levels; Altar of Inspiration and Altar of Infusion, rare blood and structure requirements; skill tree | Progression by vampire quests and abilities; exact 0.5.39 level cap and milestone list require game-data inspection |
| Feeding | Blood bar REPLACES hunger; feeds on mobs/villagers; blood bottle/container storage | Independently managed vampire blood feeding, including bottles/wineglass, Neo Vitae blood orbs and altars |
| Food drawbacks | No normal food sustenance; blood shortage penalizes strongly | Ordinary food causes Nausea/Hunger, blood starvation weakens; AppleSkin support specifically adapted |
| Weaknesses | Sunlight effects escalate by vampire level, fire/garlic vulnerabilities; vampire armor and counter-skills | Sunlight, fire, garlic; rain/storm protection, dimensional sunlight config, special protective armor |
| Powers | Skill-selectable bat transformation, rage, regen, blood projectile, freezing, invisibility, teleport, blood vision, summon bats; lord/minion skills | Quest abilities including bat flight, vampire immortality configurable; do not copy original Witchery's complete skill list as facts for JAWR |
| Progression ecosystem | Village control, vampire vs hunter conflict, specialist gear | Witchery rituals, Lilith, vampiric quests, Neo Vitae synergy |
| Reversibility | Altar of Cleansing removes faction and progress, ordinarily kills | Remove Curse ritual (0.5.13+) and Lilith-with-garlic cure (0.5.39+) |
| Other integration | Vampire's Delight is planned for this vampire branch, not automatically JAWR | Explicit blood orb/altar feeding integration with Neo Vitae |
| Existing hybrid awareness | Werewolves faction excludes regular vampire faction; custom Abomination needed | JAWR 0.5.17 changelog notes a simple handler for JAWR vampire + JAWR Forbidden Magic lich |

Sources:
- https://wiki.vampirism.dev/docs/1.10%20NeoForge/wiki/factions/vampire/vampire-player
- https://wiki.vampirism.dev/docs/1.10%20NeoForge/wiki/factions/vampire/skills
- https://wiki.vampirism.dev/docs/1.10%20NeoForge/wiki/factions/vampire/leveling
- https://modrinth.com/mod/just-another-witchery-remake/version/0.5.10
- https://modrinth.com/mod/just-another-witchery-remake/version/0.5.11
- https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake/files/9089145
- https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake/files/9166145
- https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake/files/9525978

## Remaining proof/tests

1. Verify JAWR 0.5.39 progression, abilities and exact occult initiation in Modonomicon/in-game; B3 only has 0.5.11 pinned and server-smoked. Newer publication is not installation.
2. Test Hunter v. all involuntary conversion sources, and EVERY deliberate transformation ritual. Prevent conversion before committing new player state, preserve player data on failure, sync both mods and verify death, logout and dimension changes.
3. Determine intentional Hunter exit costs; must not let custom rituals cheaply skip existing Hunter's faction-exit cost.
4. Confirm Vampire's Delight responds only to Vampirism vampire feeding and whether JAWR's separate food tags warrant custom recipes.
5. Keep two vampire systems distinct: they are not complementary flags and are blocked from accidentally stacking.

This policy belongs to the modpack's compatibility layer; no code is implemented yet.
