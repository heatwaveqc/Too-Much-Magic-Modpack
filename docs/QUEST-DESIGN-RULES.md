# Too Much Magic — quest design contract

> **Current ProjectE recipe policy:** The Philosopher's Stone must consume *one of several magical boss-associated drops* (Wilden Tribute, Gaia Spirit, Mine Key) in its center; all original other ingredients remain. Red MK3+ Power Flowers need Chaos Shard via direct recipes and MK3 upgrade. This is a recipe requirement, **not a player-kill quest lock**. Previous all-post-Chaos text below is historical. Until in-game enforcement and quest revisions are completed, all older quest text must be treated as stale design guidance. See [ProjectE plan](PROJECTE-TIERED-GATING-PROPOSAL.md).

Approved 2026-11-14. Inspired by ATM10's navigational and branching approach. Quest text, reward choices, IDs and layouts are original; no copied ATM10 quests, images, loot tables or proprietary assets.

**2026-11-22 visual authoring decision (v3):** New or substantially rewritten quest chapters must follow the [Quest Presentation and Visual Authoring Standard v3](QUEST-PRESENTATION-STANDARD-V3.md), in addition to the walkthrough standard. The owner-provided ATM10 configuration was used as a structural reference only; do not copy its artwork or exact layouts. Each authored quest needs an intentionally selected **objective-specific icon** (exact task item or meaningful apparatus, glyph, result, mob/trophy or original emblem), **not a generic enchanted book placeholder**. Substantive chapters also need original/reusable **header and relevant illustration/background-like image composition**, readable section styling, sensible node-shape/size hierarchy, and a left-to-right, center-out or convergent graph chosen to reflect *real* progression. Author visual data separately from stable quest IDs and logic; validate shipped assets and in-client rendering before claiming visual completion. Existing content is not retroactively claimed compliant.

**2026-11-15 walkthrough decision:** All **mod progression chapters** must follow [QUEST-WALKTHROUGH-STANDARD.md](QUEST-WALKTHROUGH-STANDARD.md). They are **rewarded how-to guides**, not a collection checklist. Together with JEI and the mod's own in-game guide they must enable a new player to follow **the complete native progression without external references**. The five rules below remain binding; the walkthrough standard adds instructional coverage, not an exemption to rewards or mechanical gates.

1. **Every quest must grant positive experience and a meaningful reward.** Strongly prefer a **thematic, fixed item**, not currency; coins may substitute **only when no good fixed item makes sense**, principally selling-contract quests. The 42 provisional overview checkmarks still use temporary 2 XP + one paper rewards, to be redesigned as those chapters mature. No random-only or no-reward quests.
2. **Item rewards must not auto-complete another quest.** The initial stricter rule forbids ANY fixed reward item ID from matching ANY authored item-task ID; validator checks this. Exception for introductory quests or awarding more of the SAME resource is permissible only if individually documented and tested. Shop purchases are deliberate paid choices, not automatic quest rewards; they may fulfill eligible ordinary resource tasks but must never bypass boss/technology gates.
3. **Completion criteria should be unique across quests unless intentionally reused.** Repeated items are allowed only in the expressly documented ascending-volume copper, emerald and diamond sale tiers (six specific item IDs, each reused twice), using consuming tasks and different submission counts. No other duplicates or advancements. Informational checkmarks are manual and use unique task IDs.
4. Branch freely across magic schools, exploration and food. Within a chapter, give sensible optional dependency lines without enforcing unrelated specialties or duplicating rewards.
5. **Follow the current, externally enforced ProjectE/Project Expansion recipe and tier gates** (see the current policy note above and its linked plan): the Philosopher's Stone has a boss-associated ingredient route and red MK3+ Power Flowers require a Chaos Shard; do not resurrect the superseded blanket all-post-Chaos statement. No quest may award a bypass, the excluded Ring of the Seven Deadly Sins, Re:Avaritia or Extended Crafting items. Quests explain real gates; they do not implement those gates.

## Required instructional structure for every mod chapter

- **Start with the guide:** If an in-game guide/manual/Notebook/UI guide exists, include an early quest describing the *actual* way to obtain, craft, receive or open it; alternatively provide it as a safe quest reward. A physical book is not required if the guide is a menu/keybind, but the player must know how to access it.
- **Teach each real progression step:** A quest explains **why** the stage matters, what it requires, **how** to do it, how success looks, and which meaningful next stage it enables. Cover mandatory tiers from initial setup to capstone and all significant optional systems and addons. Do not require a player to know recipes/build shapes from an external wiki.
- **Explicit guidebook/JEI references:** When a quest needs a multiblock shape, altar arrangement, ritual, machine UI, dimension/biome-specific setup or other hard-to-recall procedure, identify the **specific in-game guide section/page or JEI recipe/use category**, with enough instructions in the quest itself to begin. "See the guidebook" without naming where to look is insufficient.
- **Detection must be honest:** Owning a block does not prove it was operated. Prefer native behavior advancements/events; otherwise teach the actual behavior while describing acquisition criteria as acquisition proxies, or use an expressly permitted manual confirmation. Never assert that FTB verified an event it could not detect.
- **Reward instructional milestones:** Keep positive XP, a useful thematic fixed item by default, existing documented exception policy, unique criteria and no automatic unrelated quest completions. Rewards must not bypass boss/faction/material gates.
- **Audit before declaring completion:** Build a progression coverage matrix first, then client-test whether a fresh player can complete the mod using **quests + JEI + guidebook** alone. A chapter with gaps is a pilot or partial walkthrough, even if its data compiles and all task IDs resolve.

The research-side [mod profile template](mod-comprehension/templates/MOD-PROFILE.md) and [quest handoff template](mod-comprehension/templates/QUEST-COMPAT-HANDOFF.md) supply the path, real prerequisites and guide references needed for authoring.

## First authored batch

**Getting Started: 11 quests** covering the welcome page, first-log pouch, basic crafting, tools, stone, furnace, chest, iron and first book. The first-log FTB quest gives **10 XP and four torches** when claimed; the **Trinkets Pouch is separate, automatically delivered once per player by the persistent advancement**. No second pouch is claimable.

**Tips & Tricks: 15 optional, manual read-and-check quests** covering JEI, Jade, inventory sorting, crafting tweaks, controls, guidebooks, Curios/bag nesting, chunk claims, Lootr, Tombstone, biomes, Polymorph, skills and overlays. Each provides 5 XP and a small item.

**Food & Fishing: 20 quests** covering crop basics, fishing (including Aquaculture rods), Farmer's Delight prep and cooking, the Farming for Blockheads Market, and optional Pam's HarvestCraft cooking and starfruit. No food or fishing task reuses a criterion from another authored quest.

**The other 42 chapters** retain a single optional read/checkmark overview each, with 2 XP and one sheet of paper rather than the previous no-reward placeholders. Remove or replace these as actual content is written.

Sources for modded item ID selection: ATM10 1.21.1 quests contain Aquaculture rods, Farming for Blockheads Market, Pam's Fried Chicken and Starfruit IDs; Farmer's Delight references use `farmersdelight:cutting_board` and `farmersdelight:cooking_pot`. The installed pack's client registry/JEI must still be verified.

## Source and tests

- Authoring plan: `manifest/quest_content.json`; stable IDs: `manifest/quest_content_resolved.json`; chapter membership: `manifest/quest_chapters.json`.
- `scripts/validate_quests.py` must check this contract in addition to structure/ID consistency.
- Confirm every task, XP reward, item reward, dependency and tooltip in a disposable Prism world. Check each reward once, including per-player first-log grant and multiplayer team independence. Server startup is only a smoke, not an item/quest UI test.

## B11 changes — Quest Shop and Cooking for Blockheads

- **Quest Shop** introduced to serve as an anti-grind and anti-bad-luck market, not an alternate boss progression route. Vanilla amethyst and Heart of the Sea plus six known Artifacts are initial candidates; per-team currency balance is upstream behavior and needs multiplayer acceptance checks.
- **Cooking for Blockheads** is folded into Food & Fishing, with unique Cooking Table and Oven tasks, rather than creating a new quest chapter. Existing Balm covers its library dependency.
- **Currency is not the default reward.** Only the 12 dedicated sale quests currently grant `questshop:coins` instead of a fixed item. All other authored quests continue to use useful physical items plus XP.
- **Copper, emerald, diamond sales** consume inventory and pay one team coin. First three per resource are one-time, fourth deliberately inefficient and repeatable (256 copper blocks; 128 emerald blocks; 64 diamond blocks). These are **design-approved shared item criteria**, not accidental overlap.
- Exact rules and shop prices are in [QUEST-SHOP-ECONOMY.md](QUEST-SHOP-ECONOMY.md).
