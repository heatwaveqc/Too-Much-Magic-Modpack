# Development target — retire TeamLapen Vampirism and Werewolves (owner decision 2026-11-15)

> **AUTHORITATIVE FUTURE TARGET; NOT A LIVE PACK CHANGE.** The owner approved removal of `Vampirism - Become a Vampire!` and `Werewolves - Become a Beast!`, retaining **Just Another Witchery Remake (JAWR)** as the vampire and lycanthrope physiology/progression owner. This decision supersedes the dual-provider vampire ascent target and the TeamLapen-based Werewolves/Abomination faction architecture. No packwiz pin, config, current integration build, or saved character has been altered in this design pass.

## Non-negotiable scope

- **Remove both third-party mods** from the next appropriate staged integration *only after replacing outstanding hard dependencies*, primarily the existing Monk implementation. Treat the current test build as unchanged until a migration build is validated.
- **Do not preserve the removed mods' world content.** No copied/reimplemented vampire/hunter villages, vampire biomes, crypts, hunter outposts, world structures, raids, village captures, faction wars, NPC village systems or new worldgen from either removed mod.
- **Do not preserve their pet/minion systems.** No recruitable faction followers, wolves summoned as permanent or temporary pets, faction minion improvements, resource/blood gathering minions, or faction lord command systems. Avoid a new raid mechanic.
- **Preserve only selected player mechanics**, classified in [source-tree skill triage](TEAM-LAPEN-SKILL-TREE-TRIAGE-2026-11-15.md), as original pack-owned logic when not already covered by JAWR, Tyz, or other magic mods. No port of original art/assets/code without separate licensing review.
- **Hunter and Monk are separate workstreams in other chats.** Do not implement or redesign either in this PR. This decision places requirements on those workstreams: independent player eligibility, no TeamLapen faction API dependency, one shared Tyz/MXP spending wallet. Any item/combat concepts from Hunter are a handoff only, not implementation authorization.
- **No extra XP/SP wallets**. Continue the approved nine-custom-Tyz-category plan (Core, Cave/Sea/Forest Dragons, Vampire, Werewolf, Lich, Hunter, Monk); the original four stock Tyz categories remain subject to the separate migration plan. Only magic-practice MXP buys Tyz ranks. No duplicate innate transformation acquisitions.
- **JAWR is authoritative** for vampire and lycanthrope state, form/morph, feeding, hunger, native abilities, cure, sun/silver/moon effects, innate advancement, and species identity. Tyz can enhance verified capabilities; it cannot silently emulate JAWR transformations or supersede JAWR physiological ownership.
- A potential **JAWR vampire + lycanthrope Abomination** stays research-only until both player states actually coexist safely in-world (including one hunger/blood/death/form owner). Abomination never needs TeamLapen native factions.
- Preserve the general cross-transformation compatibility bridge for Dragon Survival, Forbidden Magic, Goety, Dracolich/Archlich, JAWR, and exclusive/approved hybrid states. Retire only TeamLapen probes, actions, mixins, faction hooks and assumptions.

## Explicitly superseded work

- `docs/VAMPIRE-FUSION-AUDIT-2026-11-14.md`, `docs/VAMPIRE-CHASSIS-V1-TEST-PLAN.md` and experimental V1 dual-pool logic are historical. The later *Lilith one-way TeamLapen ascent* proposed in [PR #43](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/43) is **also superseded**, even though it superseded V1. JAWR/Lilith advancement is now JAWR-native without automatic TeamLapen enrollment.
- [PR #73](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/73) TeamLapen Hissing/Howling adapter proof is historical/test-only and must not become the shipped Abomination faction. Retain [PR #70](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/70) JAWR-only coexistence research.
- Existing nine-tree [PR #83](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/83) still provides the form-tree direction, but **any text describing two vampire native providers, a Werewolves native owner, dual pools, or TeamLapen lord levels is obsolete**. Merge/rebase that design only after applying the [triage](TEAM-LAPEN-SKILL-TREE-TRIAGE-2026-11-15.md).
- TeamLapen-specific food tags, recipes, quest chapters, transforms and tests are **deletion candidates**, not accepted live removals. Preserve JAWR food and its Neo Vitae hooks.

## Staged migration / acceptance gates

1. **Inventory dependencies and IDs:** search packwiz `mods/*.pw.toml`, `index.toml`, manifests, `pack.toml`, KubeJS recipe/tag logic, FTB Quests task/reward item IDs, language/UI, test builders, scripts, and selected feature heads. Find hard uses of `vampirism:`, `werewolves:`, Vampirism faction APIs and third-party addons that depend on either. Confirm `Vampire's Delight` or other candidate addons are not left requiring the deleted mod.
2. **Independent Monk proof (separate workstream):** First Sunset, existing six-skill pilot, mastery, skill GUI, perks, first-hotbar-fist and armor lock rules must no longer rely on Vampirism class registries. No item loss, point duplication or vanilla XP dependency.
3. **Independent Hunter proof (separate workstream):** class identity and combat effects use standalone authoritative rules; no forced Hunter/vampire faction exclusivity unless the new design explicitly asks for it. Do not accidentally add worldgen, raids, or minions.
4. **Tyz migration:** add only approved per-skill candidates from the audit; preserve one shared wallet, enforce server-side JAWR vampire/werewolf gates at purchase and effect time, suspend effects after cure, keep invested ranks dormant unless legitimately refunded. Re-evaluate the existing conceptual blueprint rather than double-registering similar nodes.
5. **JAWR verification:** source-/binary-check actual 0.5.39 ability overlap and state callbacks; exercise vampire/lycan forms, hunger, blood transaction safety, level-up, full moon, silver, daylight, cures, respawn, dimension travel and multiplayer. Don't promise a former TeamLapen ability works without writing a replacement adapter.
6. **Remove and clean:** remove the two mod pins/index entries and dependency-only files in one integration PR, strip stale `vampirism:`/`werewolves:` hard references, obsolete TeamLapen compatibility implementations and workflows, and deleted-mod quest chapters (do not substitute world content). Leave historical docs with prominent supersession notices rather than rewriting recorded test history.
7. **Gate the assembled client/server:** clean launch with no missing mandatory dependency, FTB Quests/JEI clean IDs, no broken login or GUI, correct forms and skill guards, persisted spent SP, no free XP loops, no loot/worldgen remnants, and no loss/dupes across death/cure/relog. Compare with prior immutable build; do not promote Stable on boot alone.

## Responsibility and immediate status

- **This design PR:** decision record, vetoes against worldgen/raids/minions, upstream vampire + werewolf skill audit and Tyz candidate handoff, documentation supersession notices. **No runtime edits.**
- **Other workstreams:** implementation of standalone Hunter and Monk.
- **Later integration:** edit mod list, code, quests/config, validation and test builds *after* prerequisites are met.

Sources: pinned pack entries `mods/vampirism-become-a-vampire.pw.toml` (Vampirism 1.10.13) and `mods/werewolves-become-a-beast.pw.toml` (Werewolves 2.0.3.4); JAWR compatibility audit, current transformation bridge README; [upstream Vampirism 1.21 sources](https://github.com/TeamLapen/Vampirism/tree/version/1.21/latest), [upstream Werewolves 1.21 sources](https://github.com/TeamLapen/Werewolves/tree/1.21).
