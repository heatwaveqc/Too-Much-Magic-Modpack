# Quest-book client feedback and repair — 2026-11-14

This branch fixes quest UI problems reported after importing the **140-mod / 168-quest** Prism test ZIP. The new authored branch has **179 quest objects across the same 45 chapters and five category groups**. It is a **candidate**, not proof of client render correctness.

## Two independent root causes

1. **Wrong quest-text storage.** Previously each chapter's `quests[]` compounds contained inline `title` and `description` fields. The FTB Quests 1.21.1 format instead loads user-facing quest text from `config/ftbquests/quests/lang/en_us/chapters/<chapter_filename>.snbt` with keys `quest.<16_HEX_ID>.title` and `quest.<16_HEX_ID>.quest_desc`. See the working ATM10 quest configuration for this exact locale structure. The old inline properties had been ignored, giving broken display text. The 45 individual English locale files now exist and are **packwiz-indexed**, with one title and description for every one of 179 quest IDs.
2. **The 65 Progression quests contained the literal text `undefined` in their generated descriptions** due to a previous source-to-SNBT serialization mistake. The canonical descriptive prose remained intact in `manifest/quest_content.json`. It has been restored in all 65 proper `quest_desc` locale entries. A validator now fails any future `undefined` text.

The five approved top-level names are **Introduction; Progression; Magic; Exploration & Combat; Endgame**. Chapter-group localization and all 45 chapter titles have also been regenerated directly from `manifest/quest_chapters.json` to prevent drift. Chapter subtitles use the supported `chapter.<ID>.chapter_subtitle` string-array key.

## New Getting Started continuation

Rather than treating Iron Pickaxe as a finish line, the main orientation now branches into building a bookshelf, discovering an early magical shortcut, trying one real spell or ritual, optional iron armor, and exploration, before directing the reader to the open-ended Magic, Progression, Food and Exploration chapters. **No particular school is required.** Seven new quests bring Getting Started to **18** quests.

## Tips & Tricks addition

Four optional manual-read entries bring the chapter to **20** quests:
- **Smelt Without Fuel: Scriptor Lectern.** `scriptor:casting_lectern` plus a written **Inflame** spell automates ordinary furnace smelting without fuel, FE, or player mana per upstream source. Casting cooldown/complexity limits still apply. Its 1.21.1 source recipe is three wooden slabs, a bookshelf and purple wool. **Adjacent chest input/output on our pinned 2.0.14 build needs a live test**; do not call it fully proven until that works.
- **Double Raw Ore: Occultism or Mahou?** Distinguishes a Foliot Crusher's supported two-dust outputs from Mahou's different two-powder catalyst mechanic. The planned raw-ore→two smeltable unified dust compatibility patch is NOT yet implemented.
- **Need Early Flight? Compare Brooms.** EvilCraft's blood-powered broom versus Hexerei's different broom.
- **Vengeance Pickaxe: Fortune with a Price.** Explains high Fortune and the curse without claiming free smelting or vein mining.

## UI layout

Instead of ad-hoc straight lines and scattered icon positions, `scripts/rebuild_quest_ui.py` gives **consistent branch lanes** to the eight populated chapters: Getting Started, Tips & Tricks, Food & Fishing, and all five Progression chapters. The 42 chapter placeholders remain single centered introductory entries. Item, task, reward, and chapter IDs are **unchanged**; new quests use stable fresh IDs. New `manifest/quest_layouts.json` records all 179 positions. Current validator checks uniqueness, canonical titles/descriptions, valid group names, non-overlapping nodes and rewards.

## Test checklist for a fresh Prism import

Use the refreshed **Quest UI Fix** Prism package, NOT the old 168-quest Progression ZIP. In a disposable new world:

1. Confirm the five top-level categories and all chapter titles render with the expected names, including **Exploration & Combat** and **Exceptional Power: Reactors**.
2. Inspect titles and descriptions in Getting Started, Basic/Advanced Progression, Skills, Endgame Roadmap and the 32 reactor technology objectives; none should read `undefined`, `Unnamed` or `ERROR`.
3. Inspect the new vertical Getting Started structure and its magic onboarding after iron; ensure independent branches and prerequisite lines are readable.
4. Read the Scriptor tip and verify the casting-lectern recipe with JEI. If possible test chest input/output using a valid world-specific Inflame spell. Log any unexpected FTB quest error.
5. Verify existing reward claims, coins, the separate once-per-player first-log Trinkets Pouch, and basic item tasks; test sale consumption on a disposable world only.
6. Capture screenshots and `logs/latest.log` from Prism if any title, tooltip, quest, or group renders incorrectly.

**Important:** This work repairs quest book text/layout. It does **not** implement the planned Pufferfish skill tree, arbitrary raw-ore doubling, universal bag nesting, Market magic-seed restrictions, or ProjectE/Chaos Guardian gates. Those remain separate pending features. The Quest Shop has not been altered or claimed gameplay-tested.
