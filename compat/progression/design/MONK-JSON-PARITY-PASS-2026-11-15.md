# Monk standalone JSON — first pre-Tyz parity pass (2026-11-15)

**Branch:** `feature/monk-standalone-json-parity-pass-2026-11-15` (based on the shared core PR #127). **Status:** updated **loadable Monk datapack** and contract tests; **not yet an in-game release**. Neither full integration's source manifest nor the existing Hunter path is touched.

## What was already there

The core engine has a **20-rank Monk path with 28 skills** and server-side effect handlers for nearly every source family. Twenty-four of the **25 registered classic Monk 1.4 abilities** are represented through existing native, automatically granted skills (one original ability, Mining, maps to two separate native skills). Four classic registered-but-unused-by-default abilities—natural Toughness, Vitality, Glass Touch, Full Water Breathing—are already covered. Creative Flight is deliberately **not** ported: the approved substitute, Momentum Gliding, is post-Dragon and native.

Canonical source for the absolute **pre-Tyz** requirement: [PR #100](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/100). The config's native baseline does not require Tyz, MXP or Tyz points. Any elected talent style such as `focus` is extra; source classic powers are automatic and not mutually exclusive.

The [25-row source-to-runtime coverage map](monk-classic-coverage-v1.json) names the actual skill/effect ID, implementation status, and missing test/handler for **every classic ability**. It also tracks the free Fist, Armor Lock, Treasure Hand and First Sunset systems.

## Changed in loadable `monk.json`

| Skill | JSON change | Why |
| --- | --- | --- |
| `hand_mining` | `break_speed` target **`*`** (not only `#minecraft:mineable/pickaxe`); `per_rank: 2.4`, five native auto-ranks at 1/4/9/13/17 | Provides all-category hand-breaking speed, not only stone/ore. Server speed formula `1 + 2.4 × effective rank` gives a provisional **13× top baseline**, matching the original high-end 12+1 speed intent without Tyz. Early-rank speed differs from the old balance curve and needs playtest. |
| `hand_harvest` | Auto-ranks **4/9/13** (previously 4/9/14), three eligible block tags **pickaxe/axe/shovel**, maximum builtin tier **3** | Aligns the original milestone ordering and extends native tool-tier eligibility to conventional chopping/digging categories. Native `harvest_tier` compares rank 1–3 against vanilla need-stone/iron/diamond tags. |

These are **real fields and built-in effects accepted by PathJson schema 1**. There are no made-up JSON effect types, no Tyz purchases and no placeholder skills in the loadable path.

## Material gaps found during the JSON audit

| Issue | Can schema 1 alone implement it? | What the core-engine thread needs to add or validate |
| --- | --- | --- |
| **Lava Protection** (`MONK-LAVA-01`) | **No** | The original registered ability is specifically distinct from ordinary Fire Resistance. A generic second `damage_multiplier` on `minecraft:lava` would **stack with** the already-enabled `#minecraft:is_fire` handler, so it would be misleading to add it as an ordinary auto-node. Add a native lava-state-aware handler or a verified exclusive-damage rule; then add its rank-12 automatic node. This is the **only source classic ability with no native skill ID** yet. |
| Effective hand harvest (`MONK-HARVEST-02`) | Partially | Parser and current handler expose pickaxe/axe/shovel tier values but only calculate old numeric `needs_stone/iron/diamond`; verify 1.21.1 `incorrect_for_*` loot checks and modern modded tiers, including tools required by Netherite/exotic ores. Do not claim a 17th-rank tool tier until supported by actual code/tags. |
| Glass Touch vs Treasure Hand (`MONK-GLASS-03`) | **No** | Current post-break loot handler processes `treasure_hand` **before** `glass_touch`. With Fortune selected it can reroll glass to no drops, overriding earlier Glass Touch. Need safe hand-loot precedence, correct loot context and real tool type for non-pickaxe harvest. |
| Armor Lock and Fist reservation (`MONK-ARMOR-04`, `MONK-FIST-05`) | **No** | Both native auto skills and their server logic exist, and Armor Lock already applies 1 durability wear/20 ticks to Curse of Binding pieces, preserving the agreed Life Mending trap. But pickup/tick cleanup is not a hard pre-write guard against hotbar key-1 swaps, inventory GUI, modded grave restoration, direct `setItem`, etc. Need no-loss, no-dupe inventory interception. |
| Animal Friendship (`MONK-ANIMAL-06`) | Partially | Runtime implements adult breeding love; classic also calmed angry passive creatures. Add server-side anger/retaliation reset with modded mob guard. |
| Withering Stare (`MONK-GAZE-07`) | Partially | Current LOS/undead effect works in code, but current handler excludes WitherBoss entirely, while classic has explicit boss-phase behavior. Verify boss-specific rules before enabling. |
| Water Walking (`MONK-WATER-08`) | Partially | Current buoyancy changes vertical speed over water. Verify actual surface walking/collision and crouch-to-dive, especially multiplayer; install a proper collision or equivalent handler if necessary. |
| Gear suppression (`MONK-MELEE-09`) | Partially | Current empty-hand check looks primarily at the **main hand**, while illegal offhand weapons may remain equipped; confirm intended discipline eligibility and legal spellbook/Curio exceptions. |
| First Sunset and co-classes (`MONK-INIT-10`) | Partially | 200-tick ritual works at prototype level. Verify sky ray to sun, transformations and Monk/Hunter coexistence arbitration before production. |
| Native XP source trust (`MONK-XP-11`) | Partially | Current activity awards are intentionally restricted to logs, undead, food variety and interactions. Add post-commit validation and anti-farm checks before broadening sources to arbitrary blocks or all mobs. |

## Verification scope

**Automated:** `MonkPathParityCoverageTest` loads the **actual installed** Monk JSON via `PathJson.read` and asserts parser support, a 20-rank curve, source crosswalk of exactly 25 registered abilities, four off-default abilities, 24 native auto-granted IDs and explicit Lava Protection omission, no paid or exclusive baseline nodes, first-fist/Armor Lock/Treasure Hand free grants, native hand mining speed and three harvest category tags, and Dragon milestone gating. It does not pretend to test runtime hooks, mobs, loot drops or client GUI.

**Manual / full client:** after the shared core incorporates missing code: disposable dedicated server + two clients, player saves, spell-focus mining, Curse of Binding + Life Mending, deepwater, Wither boss, creative and inventory interactions, world claims, Ender Dragon advancement and without Tyz/Vampirism/Werewolves.

The [shared core PR #127](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/127) remains the implementation owner. This branch deliberately targets that branch so core changes and the JSON configuration can be reviewed together. No new module and no duplicate progression engine were created.
