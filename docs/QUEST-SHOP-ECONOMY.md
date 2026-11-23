# Too Much Magic — Q Shop migration (2026-11-22)

**Status:** Owner-approved source candidate for the *next* integration/QA build; NOT gameplay-tested. See #523, Bountiful #365.

## Player wallet and mod transition

Replace Quest Shop 1.4.0 (CF 1338320/9046797) with Q Shop 1.8.5 (NeoForge MC 1.21.1, CF 1654563/9407426). Use the official upstream GitHub release SHA256 pin, not a bundled/redistributed All Rights Reserved JAR. Currency ID `coins` is **personal per player**; the old FTB Teams shared coin wallet is retired. Open the new shop with `/qshop open tmm`; `/hqs shop` and its old sidebar shortcut do not carry over.

Q Shop imports the template `config/qshop/currencies.json` and `config/qshop/shops/tmm.json` into a **new** world's `serverconfig/qshop/`. It does not automatically overwrite existing worlds with Q Shop serverconfig; use `/qshop overwrite` deliberately, after backup, if needed. **Prior team-wallet balances are not automatically converted to personal balances**; splitting them requires a separate owner decision and an existing-world migration/backup plan.

## Exact initial catalog

The canonical `manifest/quest_shop_catalog.json` retains all **48** existing purchasable offers, quantities and coin prices, grouped into six navigable tabs: Supplies & Building (9), Common Magical Materials (6), Ore Blocks & Mining (18), Exploration Finds (5), Artifacts (6), Food & Farming (4). Bountiful board remains 5 coins. No additional enchanted books, inks, repairs, Relics or expensive artifact stock are silently added; those are the **approved next catalog expansion target** in #523.

## Twelve FTB Quests sales

The original twelve copper/emerald/diamond sales keep their **identical quest/task/reward IDs**, required material counts, item-consumption tasks, single coin amount and one-time/repeatable pattern. Their reward command changes from `hqs coins add {p} 1` to `qshop currency give {p} coins 1` at FTB op2 permissions, with `team_reward: false` for the claiming player's coins. XP remains team-shared. Underlying FTB quest completion may still be shared in teams; two-player claim semantics must be verified, not assumed to be per-player task ownership.

We deliberately avoid native `qshop:money` rewards pending a binary ABI audit: inspected 1.21.1 QShop source declares `void claim(...)` while the pinned FTB Quests reward API expects `boolean`. The op2 command bridge is the migration path for now.

Bountiful remains a separate owned workstream (#365): general decrees mix coin/item payout, ordinary Hunter's Decree becomes coin-only, magic-school/specialist decrees primarily offer thematic material with rare coins. Later payouts must use this same player `coins` wallet; this PR does not change Bountiful pools.

## Next authorized build QA

- **A:** Verify provider JAR SHA256/dependencies, Packwiz hash closure, old Quest Shop absence, and 48/48 tab/catalog parity.
- **B/C:** New disposable world `/qshop open tmm`, all six category tabs/search, buy representative ores/food/artifact/board with precise personal debit, relog persistence.
- **C:** Consume and claim copper/diamond/emerald sale quests solo and with two teammates; check claimant-only credit, once/repeat behavior and cooldown.
- **C:** Verify UI, Relics/Artifacts initialization and absence of unexpected free currency or default shop. Bountiful integration can be tested once its separately owned payout patch is integrated.
- **C:** Existing-world migration requires backup and explicit team-to-player wallet balance allocation; no silent data-loss claim.

No full Minecraft server boot or Prism package is authorized by this PR alone.
