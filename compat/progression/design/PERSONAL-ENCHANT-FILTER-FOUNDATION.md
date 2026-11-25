> Historical donor #449; consolidated into #471. Read PERSONAL-ENCHANT-FILTER-328.md for current scope and authority.

> **Historical / superseded (2026-11-24):** Economy owns Arcane Discrimination and portable enchanting via [#682](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/682). Do not restore this dormant Progression implementation. Original #471 source remains at `4209a4184ea146b071b503ab3a0d94cb63668c7b`. Retirement must follow #682 integration and confirmation of no deployed legacy escrow; Economy B/C acceptance remains outstanding.


# Personal random-enchantment filter foundation (#328)

## Frozen source and exact runtime pins

- Development base: `dev/progression` at `bae3599dc9bb1520fd1fd0d16b7338b3fbb8e107`.
- Improvable Skills 3 21.0.2: CurseForge `252902/5685129`, SHA-1 `b082c298dafdf74dbe96cdfb5422d262b3499643`.
- Apothic Enchanting 1.6.4: Modrinth `pL8MtgqY/JAQJkLJ1`, SHA-512 `b560ed61810030472c42fa39e6199b1db07c19baba8f78d98b44ab3e1d00a1bebff5f69942bd076fea9754f63940df920cfb19906073b77bcc459bb8d88575de`.
- Apotheosis 8.9.0: Modrinth `rqFWfVlz/veL1ccTb`, SHA-512 `b3022bc31f4e9f99d298c36486e09cad308d4aa87e2aa65a341809aeca41f0067e744086768884959036a46685d4c0babc4274600884ad8962fe9d080d8ef3ee`.
- Eidolon Repraised 0.5.0.8: CurseForge `870250/9332315`, SHA-1 `12a816c11a064a7050e74c5c15cf07fb8269b6d4`.

## Implemented engine contract

- `PersonalEnchantFilter` is the immutable server-owned consumer view: at most six unique namespaced enchantment IDs, an unlock flag, and a monotonic revision.
- Filtering is restricted to random candidate selection. Player-directed books, recipes, anvils and deterministic applicators are never vetoed by this API.
- `EnchantPreviewStamp` requires random previews to be recomputed when either the player's filter revision or the live enchanting-data revision changes before commit.
- `VirtualCandlePacking` implements the approved `ceil(count-per-exact-type / 4)` rule, emits real one-to-four-candle clusters, never mixes colors, and fails above the 32-position capacity.

## Deliberately remaining

This slice does **not** register the ability, invent a scroll recipe/name, add a menu, accept client-authored IDs, or pretend IDs are escrowed books. The next Progression-owned state/menu slice must preserve six exact single-enchantment `ItemStack`s, validate them server-side, refund without duplication, persist/copy/sync state, and publish this consumer view only after a committed transaction. Apothic/Raven and Eidolon adapters then consume the shared view; #407 must not create a second store.

No mod pin, pack index, shared integration manifest, Integration Assembly, Minecraft boot, Prism package or gameplay acceptance is part of this foundation.
