# Too Much Magic — Quest Shop and player sales

Owner-approved 2026-11-14. **Integration candidate**, not a tested economy. Quest Shop is the only new shop; Cooking for Blockheads is in Food & Fishing.

## Quest rewards
Strongly prefer a specific, useful item appropriate to each quest, in addition to XP. **Coins substitute for the item only where a meaningful fixed item would be forced or nonsensical** (such as handing in a commodity for money). Important milestones may grant both item and coins if deliberately balanced. No random-only or paper filler for long-term authored quests. Keep all critical progression gates enforced by recipes, loot restrictions or actual game logic; quest locks alone do not enforce them.

## Source-driven shop implementation
Quest Shop official 1.4.0 CurseForge project 1338320, file 9046797, NeoForge 1.21.1; FTB Quests reward type `questshop:coins`. Cooking for Blockheads 21.1.24 NeoForge 1.21.1 Modrinth vJnhuDde version MQCIy6VF, requires Balm already pinned. Packwiz resolution and fresh combined **140-mod dedicated-server boot passed** in [Actions run 43610383608](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43610383608). Client/shop UI, currency claiming and repeatability still require in-world verification.

- Current Quest Shop source code integrates FTB Teams by using a **shared team currency account**. Even though the FTB Quests book normally defaults to per-player item rewards, coin rewards in this mod credit the team. Sale rewards must therefore be **one claim per team**, not one credit for every online teammate. Verify in multiplayer with two or more players.
- Quest Shop supports **purchases** but does **not** natively support arbitrary sell offers. Sales are built as **FTB Quests item-consumption tasks with coin rewards**, not a separate vendor interface.
- KubeJS datapack entries live in `kubejs/data/tmm/questshop/shop_categories/` and `shop_entries/`. Entries use the explicit `tmm:<category>` namespaced category to match the source codec. `config/questshop-common.toml` disables mob coin drops, to avoid accidental currency farming.
- `/hqs shop` opens the store; Quest Shop 1.4.0 also includes a FTB Quests-screen entry. Do not add a second shop mod.

## Initial purchasable catalogue

See `manifest/quest_shop_catalog.json`: five categories, ${catalog.categories.reduce((a,x)=>a+x.entries.length,0)} exact entries. Prices are preliminary coins, subject to in-game balance.

| Category | Examples | Policy |
| --- | --- | --- |
| Supplies & Building | Cobblestone, planks, torches, glass | Routine inconvenience only |
| Common Magical Materials | Amethyst, lapis, quartz, slimeballs | Basic ingredients without skipping a magic tier |
| Exploration Finds | Heart of the Sea (80 coins), nautilus shells, saddle, name tag | Bad-luck protection |
| Artifacts | Cloud in a Bottle, Charm of Sinking, Digging Claws, Night Vision Goggles, Everlasting Beef, Crystal Heart | Specific uncraftable exploration finds, expensive |
| Food & Farming | Bread, cooked beef, bone meal, honey | QoL |

**Not sold:** Chaos Guardian loot; boss-gated Cataclysm materials; restricted Occultism miner and Replication ore; ProjectE or Project Expansion before Chaos Guardian; Ring of the Seven Deadly Sins; Re:Avaritia or Extended Crafting; items with unknown IDs. Specific Enigmatic Legacy+ relics remain for a later verified-ID pass.

## Selling via FTB Quests

Three commodities, four deliberate sales milestones each. Each sale **consumes** its input and grants positive XP plus **one** Quest Shop coin. The first three per commodity are **one-time** introductory transactions; the final one is **repeatable** and intentionally very inefficient.

| Item | First sale | Second sale | Third sale | Repeatable late sale |
| --- | --- | --- | --- | --- |
| Copper | 8 ingots | 32 ingots | 16 blocks | 256 blocks (4 stacks) |
| Emeralds | 2 emeralds | 8 emeralds | 16 blocks | 128 blocks (2 stacks) |
| Diamonds | 1 diamond | 4 diamonds | 8 blocks | 64 blocks (1 stack) |

One coin for every transaction, with rewards falling sharply as those questlines progress. All twelve objectives are deliberately documented exceptions to the general avoidance of repeat item types: copper ingots appear twice, emeralds twice, and diamonds twice because the sale tiers are a **designed progression**, not accidental double credit. Non-consuming item detection must not be used on sales; **`consume_items: true` is mandatory**, and repeatable sale quests need a tested manual-claim lifecycle.

The design **allows ProjectE to generate unlimited sellable materials after Chaos Guardian**; a renewable shop budget is intended at that point. Buying artifacts with money should be feasible but not trivial during initial exploration. Team swaps and quest-reset abuse require testing; note FTB Quests 1.21.1 has had historical repeat-cooldown exploits. No caps are promised.

## Release tests

1. Resolve the exact two official mod releases via packwiz. Verify total rises from 138 to 140 without extra unapproved mods. Fresh 140-mod server boot and Prism GUI test are distinct gates.
2. Confirm the Quest Shop catalog actually loads **five categories**, correct prices and namespace references. Verify modded Artifacts item IDs in this pack's installed registry and that unavailable items never give blank purchases.
3. Check `/hqs shop` and Quest Shop button in FTB Quests; check coins cannot be acquired from killing mobs. Verify real purchase decrements shared account and grants specified stack.
4. Confirm every one-time sale is item-consuming and cannot be claimed again; repeatable bulk consumes exactly 256/128/64 blocks for **one coin**. Test with one player and then multiple teammates; verify no double payment via claim-all or team changes.
5. Ensure no shop purchase bypasses configured magic/boss locks; run resource regression checks with ProjectE EMC production enabled **after Chaos Guardian**. Back up worlds before migrating.


## Runtime compatibility correction — November 14, 2026

Pinned Quest Shop 1.4.0's `CoinsReward.claim` returns `void`, but pinned FTB Quests 2101.1.38 requires a `boolean` return. The previously authored `questshop:coins` reward is therefore not safe to claim, despite successful mod and server boot. A team-reward serialization error was also found (`team: true` should be `team_reward: true`). The combined QA branch changes all 12 commodity sales to the native FTB command reward calling `hqs coins add {p} 1` with permission level 2 and `team_reward: true`. This is a compatibility bridge, not a permanent replacement for repairing Quest Shop against the new API. Validate coin balance and team-wide single credit in a disposable world before publishing the economy.
