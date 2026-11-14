# Monk addon — charter and technical plan (2026-11-14)

**Owner-approved direction:** a new standalone addon module
`compat/monk/` following Werewolves' Vampirism-faction structure, **not**
Pufferfish Skills. Pinned game: Minecraft 1.21.1; NeoForge 21.1.265;
Vampirism 1.10.13; Werewolves 2.0.3.4.

## Design contract

1. Monk is a playable **Vampirism-style faction**, not a transformation.
2. Ascetic tradeoff: give up **regular weapons and armor**. Magical implements,
   decorative wearables, shields and exception cases need precise item-tag
   policies. Do not delete items; later power logic should detect eligibility
   and temporarily suspend dependent abilities if necessary.
3. Monks forgo supernatural transformation advancement, including dragon and
   lich paths, and also may not coexist with vampire, werewolf, archlich,
   dracolich or abomination. The Vampirism faction slot automatically prevents
   monk/vampire/werewolf/hunter simultaneous membership *within that API*;
   external mods require transition interception by the bridge.
4. Compensations: **more generous skill-tree development**, **increased
   experience gain**, and **exceptional magic proficiency** across the pack's
   magic traditions. The exact multipliers, caps and magic attributes need
   playtesting and owner approval; do not add an unrelated mana pool.
5. Initiation rituals, rank advancement challenges and optional Master tree
   imitate Vampirism's progression loop. 14 is a *provisional code baseline*,
   not a locked final rank count; original Monk's 20-level arc remains a
   possible target.
6. Data-driven skill tree: JSON under `data/tmm_monk/vampirism/` for
   `skill_tree`, `skill_node`, `configured_skill_tree`, later `tasks`.
   Java registers faction, player attachment, skills and actions. JSON cannot
   directly implement unarmed physics, projectile interception or casting bonuses.
7. Intended four branches: Martial Discipline, Body & Mobility, Contemplation,
   Arcane Mastery. These are placeholders pending design; **not implemented**.

## State of implementation

Scaffold only. One inert root skill and JSON tree; no functioning
initiation or attribute/progression bonuses, no mechanics enforcement.
Unit-tested pure policy is neither a player-state observer nor a protection
hook. Avoid using any new custom faction on meaningful player saves until
the world loading and removal pathways are validated.

## Next engineering gates

1. Build against exact pinned Vampirism artifacts; confirm API compatibility
   and JSON resource parsing. Run combined server boot, client world and UI.
2. Confirm faction registration with console/commands and MonkPlayer attached
   data across logout, respawn and dimension travel.
3. Implement **one initiation ritual** that verifies both faction exclusivity
   and bridge-resolved external physiology *before* changing membership.
4. Implement one ordinary advancement challenge via JSON tasks and one
   custom skill end-to-end. Test points, availability and reset.
5. Define item tag groups for conventional weapons, armor and exemptions;
   enforce per-ability eligibility, with no inventory deletion.
6. Research the game's experience gain callbacks and magic mod API hooks.
   Pick bounded, measurable XP and magic improvements before coding.
7. Advance other branches and the Master track once milestone tests pass.

## Sources and license

- Vampirism 1.10.x API: https://wiki.vampirism.dev/docs/1.10%20NeoForge/api/intro
- Werewolves 1.21: https://github.com/TeamLapen/Werewolves/tree/1.21
- Original Monk mod: https://github.com/rwtema/Monk-Mod
- Upstream code may be studied for implementation, but must not be copied
  without checking original code and asset licenses.
- No third-party JARs or assets are committed to this repository.
