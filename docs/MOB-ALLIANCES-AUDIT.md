# TMM cross-mod mob alliances — initial audit (2026-11-14)

**Status: initial implementation / awaiting server and client acceptance.** Built on top of Lilith Ascension v2. No new third-party mods, AI-overhaul dependencies, or saves.

## Design contract

- **Neutrality is non-aggression, not damage immunity**. Natural/nearest-target acquisition is blocked for the specified alliances. Attacking the mob (directly or by projectile) opens a 30-second retaliation window. This applies per aggressor and avoids making undead loot-free punching bags.
- Natural **undead do not initiate against ordinary Vampirism vampires, JAWR vampires, or Goety liches**. This is independent of the experimental dual-vampire chassis flags; ordinary vampires are covered.
- Preserve normal enemy behavior against human players, hunters, werewolves, animals and ordinary mobs. Preserve commanded summoned servants and explicit boss hostility.
- **Forest dragons ↔ most fae** and **cave dragons ↔ demons** are reserved reciprocal relations, using extensible entity-type tags. The current pack does *not* include Dragon Survival or corresponding forest/cave dragon forms, so these rules are not yet player-transform rules. We must wire future dragon player-state adapters when that module is introduced.
- Grouping should be explicit at the entity registry level whenever possible. No fuzzy name matching or guessing IDs.

## Source-verified mob classifications

| Origin | Entities / groups | Notes |
| --- | --- | --- |
| Minecraft vanilla | `EntityTypeTags.UNDEAD` or `EntityTypeTags.UNDEAD` | Covers ordinary zombies, skeletons, drowned, husks and other naturally undead |
| Eidolon: Repraised 1.21.1 | `eidolon_repraised:zombie_brute`, `eidolon_repraised:wraith`, `eidolon_repraised:giant_skeleton`, `eidolon_repraised:necromancer` | Eidolon source places these four under `minecraft:undead`; the explicit optional tag provides a fallback |
| Goety 3.2.x | `goety:wraith`, `goety:border_wraith`, `goety:muck_wraith`, `goety:skeleton_wolf`, `goety:haunted_armor`, `goety:bone_spider`, `goety:zombie_ravager` | Verified Goety 1.21.1 registry IDs. Additional variants may already appear under vanilla undead tags |
| Goety lich | `za.co.infernos.goety.utils.LichdomHelper.isLich(Player)` | Optional reflective call to the Goety 1.21.1 native lichdom accessor; requires validation with the pack-pinned 3.2.2 JAR |
| Feywild 5.6.4 | 14 verified non-boss types: seasonal pixies (4), seasonal tree ents (4), shroomling, mandragora, bellsnickel, bee knight, bee mount, sprite | Explicit `feywild` entity registry IDs from SaphieNyako/Feywild source. `feywild:titania`, `feywild:mab`, `feywild:oberon`, and `feywild:ashen_lord` are excluded as court bosses. The mooshroom cow is not classed as fae |
| Goety demon group | `goety:hellhound`, `goety:damned` | Provisional demon candidates, explicitly tagged, not a claim all Goety monsters are demons |

Source references: `Alexthw46/Eidolon-Repraised` branch `1.21.1` entity classes and `src/generated/resources/data/minecraft/tags/entity_type/undead.json`; `azukaar/Goety-3` branch `1.21.1` `LichdomHelper` and `ModEntityType`; official NeoForge 21.1.265 `LivingChangeTargetEvent` contract.

## Entity-type tags (data-driven and additive)

- `tmm_transformations:undead_extra` adds source-verified cross-mod undead not guaranteed to override their Java MobType.
- `tmm_transformations:fae` allows further mods' living fae beyond Feywild.
- `tmm_transformations:demons` selects specific infernal/demonic entities.
- `tmm_transformations:forest_dragons` and `tmm_transformations:cave_dragons` reserved for actual dragon mobs once present. Currently empty by design.
- `tmm_transformations:boss_exempt` keeps world bosses hostile (currently vanilla Wither) and can be expanded for modded bosses.
- Entries from optional mods use `required=false` so absent mod IDs cannot break data reload.

At runtime the bridge emits `TMM_MOB_CENSUS` for every non-vanilla, non-MISC EntityType, including unclassified ones, followed by `TMM_MOB_CENSUS_SUMMARY`. **This is a census, not a declaration that every unclassified monster has been assigned an alliance**; it is the evidence for extending the mapping without guessing.

## Remaining review candidates

- **Goety**: distinguish undead, illagers, summon/owner-bound servants, demons and bosses; prefer existing MobType and tagging rather than treating the entire Goety namespace as one faction.
- **Malum / Enchanted Witchcraft / JAWR / Forbidden Magic / Iron's / Cataclysm / Neo Vitae**: enumerate registry IDs from the pinned-JAR server census, then classify as undead, other supernatural factions, bosses, or independent.
- **Feywild**: 14 source-verified non-boss registry IDs are now explicitly tagged; re-audit against pinned 5.6.4 JAR after server census. Four court boss IDs are explicitly exempt.
- **Additional lich providers**: Goety is wired; Forbidden Magic and any future lich state needs an exact pinned API adapter.
- **Dragon Survival**: the mod is not currently in this pack. Future dragon transformation player states require adapters to the tags or server-side state rather than interpreting player vanilla entity type as a dragon.

## In-world acceptance checks

1. Stand near Eidolon Zombie Brute and Wraith as **human**: both remain hostile.
2. Convert via ordinary Vampirism infection; approach same mobs: they do **not acquire targets on sight**. Confirm `/tmmtransform vampire` shows Vampirism-only.
3. Attack either creature once; it can fight back for 30 seconds. Step away, wait, and approach again: it is neutral.
4. Drink Lilith's Blood; both vampire progressions active; confirm same neutrality after reload.
5. Become Goety lich; ordinary zombies and Eidolon/Goety wraiths stay neutral until provoked.
6. Confirm hunters, werewolves and ordinary humans have *not* gained vampire/lich neutrality.
7. Spawn boss/servant examples and check that explicit bosses and owner-commanded summons keep their intended combat.
8. Check cross-mod ranged damage/projectile combat separately: this targets normal AI target acquisition, **not immunity to stray area damage**.
9. Read `TMM_MOB_CENSUS_SUMMARY` after the first full-modpack boot; review unclassified mods and extend the taxonomy without blanket friendliness.
10. Once Forest and Cave dragon forms are actually installed, populate dragon entity tags and the **player transformation** adapter, then test reciprocal fae/demon relations.

## Known limitations

- This layer handles vanilla-style AI target selection and clears stale targets every ten ticks. Some modded mobs may use bespoke projectiles or direct attacks without target selection; these require targeted hooks after observing test behavior.
- No global allied/friendly NPC ownership is granted. Cross-mod minion ownership must remain independent.
- The current dragon rules contain empty player-form integration; their mutual non-aggro will not activate for player dragons until the dragon module exists.
- A full-modpack build and disposable-world multiplayer acceptance are required before release.
