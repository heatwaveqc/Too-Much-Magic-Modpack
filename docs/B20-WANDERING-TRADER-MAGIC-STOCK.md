# B20 — Wandering trader: guaranteed premium magic stock

**Date:** 2026-11-15. **Status:** 1.21.1 NeoForge KubeJS source pilot staged, packwiz-indexed; **NOT gameplay/server/client-verified**. The user has explicitly approved three stock guarantees, and asked for a survey of other magic mods. The survey entries are **suggestions**, not active trades.

## Exact user requirements → implementation

The trader should offer **at least**:
1. One *real affixed Apotheosis item*, created via \`LootController.createRandomLootItem\` in an actual \`GenContext\`, validated by \`AffixHelper.hasAffixes\`. We will **never** substitute an ordinary or simply-enchanted sword and claim it is Apotheotic. Randomized offers scale to a nearby player's world tier (Haven → Pinnacle), price 24–64 emeralds + 1–12 diamonds. One purchase.
2. One random **enabled** Ars Nouveau/addon glyph **from EACH configured glyph tier I, II, and III**; exclude starter glyphs. Glyphs come from the **live Ars GlyphRegistry**, not static guessed names. Tier I: 12 emeralds; tier II: 28 emeralds + 4 lapis; tier III: 48 emeralds + 4 diamonds. One purchase each. Glyphs are real Glyph item stacks with the associated SpellPart and respect config tier overrides.
3. One Iron's Spells **Uncommon or higher ink**, weighted Uncommon 55%, Rare 30%, Epic 12%, Legendary 3%. Price/stock from the recipe script. Iron's actual registered item IDs are \`irons_spellbooks:uncommon_ink\`, \`rare_ink\`, \`epic_ink\`, \`legendary_ink\`. No Common Ink can fill the guaranteed slot.

**Preserve existing vanilla and modded trades.** Iron's already modifies \`WandererTradesEvent\` by adding scrolls, inks, curios and very rare specialty products. Simply appending new entries to the *event* would still lead to random selection; it CANNOT enforce all five offers. The B20 script instead handles \`EntityEvents.spawned('minecraft:wandering_trader', ...)\` on server, initializes the trader's actual \`MerchantOffers\`, appends five offers, and puts a versioned persistence marker on the entity. KubeJS confirms the event also fires on chunk-load; the marker prevents repeated stock injection. The five offers are built before any modification and rolling back a partially added set is attempted on error; any exception logs a visible error and never claims that minimum stock was provided. Old traders will be updated as they re-enter a server chunk; this must be verified.

### Source/API evidence

- Exact pinned Apotheosis \`Apotheosis-1.21.1-8.9.0.jar\`; source \`https://github.com/Shadows-of-Fire/Apotheosis/blob/1.21/src/main/java/dev/shadowsoffire/apotheosis/loot/LootController.java\`, \`.../tiers/GenContext.java\` and \`.../affix/AffixHelper.java\` (\`hasAffixes\`).
- Exact pinned Ars Nouveau 5.13.5, source \`https://github.com/baileyholl/Ars-Nouveau/blob/1.21.x/src/main/java/com/hollingsworth/arsnouveau/api/registry/GlyphRegistry.java\`, \`.../api/spell/AbstractSpellPart.java\`. The field \`part.getConfigTier().value\` reads the configured tier and \`GlyphRegistry.getGlyphItemMap()\` includes third-party addon glyph items.
- Exact pinned Iron's 3.16.3, source \`https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/registries/ItemRegistry.java\` and \`.../player/AdditionalWanderingTrades.java\`.
- KubeJS 1.21.1 event/interoperability docs: \`https://kubejs-wiki.tysontheember.dev/1.21.1/reference/events/EntityEvents/\`; \`https://kubejs-wiki.tysontheember.dev/1.21.1/concepts/java-interop/\`.
- NeoForge random-trade-list API: \`https://aldak.netlify.app/javadoc/1.21.1-21.1.x/net/neoforged/neoforge/event/village/WandererTradesEvent\`.

## Candidate second-stage merchant stock (survey, NOT enabled)

**High value, low progression risk:**
- **Occultism Demon's Dream Seeds** (\`occultism:datura_seeds\`): player's first occultist garden can start without endless grass punching. Proposal: 2 seeds for 4 emeralds.
- **Astral Sorcery Aquamarine** (\`astralsorcery:aquamarine\`): starting altar crafting without finding a shale deposit; 2 for 8 emeralds. Don't sell rare celestial crystals or Iesnium.
- **EvilCraft Dark Gem** (\`evilcraft:dark_gem\`): choose first EvilCraft portal project without cave-ore RNG; 1 for 12 emeralds. Doesn't grant finished Blood Infuser, vengeance tech or second-age progression.
- **Botania Black Lotus** (\`botania:black_lotus\`, item ID requires pinned Botania Revived confirmation): portable mana bonus, useful after the player has a mana pool but not a replacement for building a mana network.

**More useful after exact item IDs + balance review:** Witchery Belladonna/Mandrake/Wolfsbane seeds, Hexerei herbs and special saplings, Roots Wildroot/Terra Moss, Theurgy starter reagents, small Malum soulcraft inputs, Goety basic ritual materials, Neo Vitae entry-level alchemy reagents and Primal Magick regional reagents. The proposed stock catalog, evidence status and policy are in \`manifest/wandering_trader_magic_stock.json\`. The script intentionally adds **none** of these second-stage trades until the owner approves specific candidates.

**Never sell:** boss-gated trophies, Chaos Shards, ProjectE Philosopher's Stones or Power Flower shortcuts, Thallasium/Iesnium ore/ingots, high-tier ritual/altar keys, or unique legendary/creative-only finished artifacts. This respects the user's endgame acquisition gates.

## Runtime test contract

1. Spawn a **new wandering trader** in a disposable full-pack world. Confirm minimum five premium offers **in addition to** the vanilla/other mod random trades. Scroll the GUI; some offers may be offscreen.
2. Verify actual affix data, name, and rarity via advanced tooltip/component inspection (not just purple enchantment glow), including one low-tier and one highest-world-tier test if feasible. Make sure no crash when Apotheosis loot tables are reloaded.
3. Verify glyph item is genuinely learnable and configured tier-matched for tier I/II/III, including Ars addon glyphs. Test disabled/tier-reassigned config settings. Glyph-learning XP costs are bypassed through a *purchased item*, but casting still respects spellbook tier.
4. Confirm the ink offer is never Common and runs at advertised price/uses. Verify Iron's original random ink/scroll/curio stock remains present.
5. Unload/reload the chunk and relog. Verify offers are **not duplicated** or re-rolled, and the persistent stock-version marker is saved; existing old traders should receive offers after the next server-side entity-add event. If no required slot can be generated, visible error and no misleading successful marker.
6. On dedicated server and Prism client, inspect \`logs/kubejs/server.log\` and \`logs/latest.log\` for Java classfilter restrictions, Rhino overload resolution and loaded-registry failures. The script has been **syntax-checked**, not booted.
7. Balance test prices versus buying mana materials at the planned Quest Shop, Scribes Table glyph-crafting XP, Iron's existing trader ink pricing and world-tier rewards. No new optional magic-mod stock should be included until user chooses a subset.

### Relevant paths
- \`kubejs/server_scripts/tmm_wandering_trader_magic_stock.js\` — actual event/guarantees, no separate Java mod needed for the source pilot.
- \`manifest/wandering_trader_magic_stock.json\` — formal requirement and future-stock shortlist; does not itself modify gameplay.
- \`scripts/validate_b20_trader_stock.py\` — static checking of script contents and packwiz hashes.
