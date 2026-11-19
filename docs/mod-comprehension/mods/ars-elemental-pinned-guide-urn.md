# Ars Elemental 0.7.10.4 — native guide and Urn of Endless Waters

Exact artifact `ars_elemental-1.21.1-0.7.10.4.jar`, 2,273,778 bytes, SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01` matched against packwiz. All 353 classes, including inner classes, decompiled externally with CFR 0.152 into `elemental-fresh`. This report uses packaged resources and binary method bodies, superseding version-ahead descriptions for these routes. [Profile](ars-elemental.md) remains researching for the other systems. No client, server or gameplay tests.

## Opening and navigating the guide

Use the parent Worn Notebook: craft Book + lapis-gem-tag ingredient and right-click; [native guide access](ars-nouveau-pinned-guide-glyphs.md). `documentation/DocumentationEvents` subscribes to `ReloadDocumentationEvent.AddEntries` and `.Post`, invoking `AEDocumentation.init/postInit`. These are native Java Notebook entries, not evidence of a separate Patchouli book.

| Notebook category | Registered addon route |
| --- | --- |
| Getting Started | Spell Schools, including Anima/Necromancy text |
| Crafting | Urn; Air/Water/Lava Upstream blocks; Spell Mirror; Advanced Prism and Arc/Homing/Acceleration/Deceleration/Pierce/Chain lenses; four elemental and Shaping turrets; Siren, Firenando and Flashjack charm entries with entity pages |
| Source | Four elemental relays, related to native relay variants |
| Spell Casting | Spell Horn; Necrotic Focus; four elemental Focus entries, each showing lesser and greater recipes |
| Armor | Mark of Mastery; four school entries, each enumerating medium/light/heavy sets and all four pieces |
| Items | Curio Bag and Caster Bag; Enchanter's Bangle and Fire/Water/Air/Earth/Summon/Anima variants |

`postInit` additionally edits native Archwood (Flashing Pod), Weald Walker (Flashing variant), Imbuement Chamber (Anima Essence recipe), Spell Books (protection recipe/custom page) and Ignite/Gravity/Freeze/Launch/Grow/Cut glyph pages. Enchanter's Shield receives its extra text/relation only if the addon Mirror Shield entry lookup succeeds. This inventory establishes where to teach the systems; it does not finish their operational walkthroughs or prove assembled-client rendering. The schools page names Fire, Water, Air, Earth, Manipulation, Conjuration, Abjuration and Anima.

## Acquiring the Urn

Packaged `data/ars_elemental/recipe/everfull_urn.json` is an Enchanting Apparatus recipe: **Flower Pot reagent**, pedestal ingredients **one `c:gems/source`, two Water Essences and two Prismarine Shards** → **one `ars_elemental:everfull_urn`**. `sourceCost:0`, `keepNbtOfReagent:false`. Use the [native Apparatus structure/activation](ars-nouveau-pinned-crafting.md); the zero crafting cost does not remove the linked refill's running cost. Notebook Crafting → Urn supplies text and recipe pages.

## Linking, running and maintaining the Urn

1. Place the Urn and a water-accepting block within the tile's distance limit **30**, in the same dimension. Provide loaded native Source storage within the Urn's Source search radius **6**. `ConfigHandler.Common.WATER_URN_COST` defaults to **100 Source** (`waterUrnCost`, permitted 0–10,000); this is the binary default, not an asserted live-config value.
2. With an empty Dominion Wand selection, select the **Urn first**, then the receiving block. Native `DominionWand.useOn` invokes the first block's `onFirstConnection` with the second position; `IWandable` bridges to the Urn's `onFinishedConnectionFirst`. The default bridge checks the player's dimension. Strict wand mode passes the **stored first-selection face**, not the newly clicked receiver face, so sided capability behavior must be checked rather than inferred from the final click. Non-strict mode can pass null context.
3. The tile accepts a target when `Capabilities.FluidHandler.BLOCK` exists for that stored context. Successful linking stores position and face, reports the destination and draws a beam. Invalid targets get the incompatible-Urn message; out-of-range targets fail. It does not check current water acceptance at link time. The map has one entry per position, no explicit target-count cap in this class, and no dimension field in saved target records.
4. On server ticks whose global game time is divisible by **60**, loop over targets. Unloaded targets are skipped and retained; targets that have lost eligibility are removed. For each eligible target, first check nearby Source, then simulate filling a **1,000 mB water** stack. Only simulation results **greater than 100 mB** proceed to execute. Full tanks and acceptance of 100 mB or less receive nothing from this path.
5. A successful fill attempt then calls `SourceUtil.takeSourceMultiple` for the configured cost and emits particles. Cost applies **per successful target attempt**, not once per Urn cycle and not proportionally to accepted water. The execute fill return amount and subsequent Source debit return value are unchecked. Output precedes the actual debit; a precheck is not an atomic exchange guarantee. At most 1,000 mB is offered per target per 60-tick pass; this is source behavior, not measured throughput.
6. With an empty wand selection, sneak-use the Urn to invoke `onClearConnections`: clear every saved target and update the block. Re-link a replacement receiver. Saved `to_i` positions and `to_direction_i` restore links, with UP fallback for older direction-less data. Unloaded targets do not force-load chunks.

## Two important discrepancies in the packaged guide

The English Urn text promises linking a **cauldron or Apothecary**. The implementation has no cauldron-specific refill branch: it needs a block fluid capability from some provider. This addon registers no cauldron adapter in the inspected classes, so vanilla cauldron operation cannot be promised from this code alone. Use a verified water-accepting capability block for a future acceptance test and separately verify any assembled-pack cauldron adapter.

`util/BotaniaCompat.isApothecary` and `tryFillApothecary` both **return false** in this exact binary. Merely satisfying `CompatUtils.isBotaniaLoaded()` cannot activate that fallback. An Apothecary could only work through a separately supplied fluid capability, not this stub. Do not teach the guide's named Botania route as confirmed functionality.

There is also a separate manual bucket path: `EverfullUrnBlock` implements `BucketPickup`; `pickupBlock` returns a Water Bucket directly, with no Source debit, target link, state removal or Source check in that method. Thus manual bucket pickup and automated Source-funded refill are different code paths. This is a finding for future acceptance, not permission to alter the mod or assert every interaction/caller succeeds in a live pack.

## Evidence and remaining acceptance

Binary classes: `documentation/AEDocumentation`, `documentation/DocumentationEvents`, `common/blocks/EverfullUrnTile`, `common/blocks/EverfullUrnBlock`, `ConfigHandler`, `util/BotaniaCompat`; packaged recipe and `assets/ars_elemental/lang/en_us.json`. Parent exact Ars Nouveau 5.13.5 `DominionWand` and `IWandable` provide the linking/clearing bridge; native Source storage semantics are covered in [Source](ars-nouveau-pinned-source.md).

Future explicit runtime acceptance: recipe in JEI/Notebook; actual configured cost; strict/non-strict sided target; accepted volume 100 versus 101 mB; full/unloaded/replaced receiver; multiple destinations and Source exhaustion; manual bucket interaction; cauldron capability provenance. Pilot quest 36 only establishes item acquisition, not any of these operating boundaries. Remaining addon foci, armor, glyphs, helpers, devices and worldgen still require exact-pin walkthroughs before source completion.
