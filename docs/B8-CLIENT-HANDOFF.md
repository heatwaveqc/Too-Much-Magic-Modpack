# Too Much Magic — B8 local Prism test guide

**140 pinned mods.** [Dedicated-server boot passed](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560055987) (127 server/both-side jars, 13 client-only). **The B8 graphical client, Market economy and backups have NOT been tested.**

## Download and launch
1. [Download the final GitHub Actions Prism ZIP artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43560531515/artifacts/14099663054). GitHub usually supplies a wrapping ZIP archive. **Extract the outer ZIP only.**
2. Launch Prism, **Add Instance → Import**, and choose the inner **`Too-Much-Magic-B8-QoL-Compass-Market-Prism.zip`**. The previous B7 instance remains your known-good rollback.
3. Select **Java 21**. The test instance defaults to maximum **8 GiB RAM**. On launch, MIT-licensed packwiz bootstrap fetches original host mod files and checks exact hashes. **No third-party mod JARs are bundled in the ZIP.**
4. Create a **new disposable world**; don't open valuable old saves. Exact immutable snapshot: `0c214d24793f2c9d912b4540e609bd04cb7b5e9a`.

## Functional checks (in a disposable world)
- **Nature's Compass**: crafting/activation, GUI lists and locates vanilla and modded biomes. **Waystones and Explorer's Compass are not installed**.
- **Crafting Tweaks**: rotation/balance/clear of vanilla crafting grid and a modded crafting table. Check right-click batch crafting and Mouse Tweaks interaction.
- **Akashic Tome**: add/toggle at least two distinct magic mod guidebooks; verify it does **not** become general storage.
- **KeyBind Bundles**: bind a radial key to several spell controls and check conflicts against Controlling, Pufferfish and Improvable Skills.
- **Enchantment Descriptions, Better Advancements, Oracle Index**: inspect descriptions, advancement UI, available wikis and tooltip search.
- **More Overlays Updated**: F7 light/spawn overlay, F9 chunk borders and JEI chest-item search.
- **Farming for Blockheads**: build Market, verify Pam's and Farmer's Delight *mundane* seeds/saplings appear as expected; screenshot the purchasable list. **If magical seed or sapling items appear, do not treat that as approved progression.** The Market needs a whitelist/blacklist review before survival release.
- **Simple Backups (beta)**: make a disposable test backup, check retention/storage limits, and practice a full restore. Keep independent external saves of important worlds until this works.
- **spark**: if diagnosing performance, run profiling only as needed with appropriate operator/admin permissions.
- Confirm our existing ReFoxed middle-click chest sorting and Mouse Tweaks drag/scroll still work.

## What remains open
The Pufferfish custom skill tree, actual quests, ProjectE Chaos Guardian gates, special ore restrictions, species and lich/vampire interactions, and Market magic trade limitations are still pending integration. Report failures with Prism console + `logs/latest.log` or `crash-reports/`.

[Integration branch and PR](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/15) · [Exact versions](B8-RESOLUTION.json) · [Full B8 design/market analysis](B8-QOL-AND-SEED-MARKET.md).
