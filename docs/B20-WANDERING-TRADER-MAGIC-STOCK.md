# B20 — Wandering trader: guaranteed premium magic stock

**Date:** 2026-11-15. **Status:** 1.21.1 NeoForge KubeJS source pilot staged, packwiz-indexed; **NOT gameplay/server/client-verified**. The user has explicitly approved three stock guarantees, and asked for a survey of other magic mods. The survey entries are **suggestions**, not active trades.

## Exact user requirements → implementation

The trader should offer **at least**:
1. One *real affixed Apotheosis item*, created via `LootController.createRandomLootItem` in an actual `GenContext`, validated by `AffixHelper.hasAffixes`. We will **never** substitute an ordinary or simply-enchanted sword and claim it is Apotheotic. Randomized offers scale to a nearby player's world tier (Haven → Pinnacle), price 24–64 emeralds + 1–12 diamonds. One purchase.
2. One random **enabled** Ars Nouveau/addon glyph **from EACH configured glyph tier I, II, and III**; exclude starter glyphs. Glyphs come from the **live Ars GlyphRegistry**, not static guessed names. Tier I: 12 emeralds; tier II: 28 emeralds + 4 lapis; tier III: 48 emeralds + 4 diamonds. One purchase each. Glyphs are real Glyph item stacks with the associated SpellPart and respect config tier overrides.
3. One Iron's Spells **Uncommon or higher ink**, weighted Uncommon 55%, Rare 30%, Epic 12%, Legendary 3%. Price/stock from the recipe script. Iron's actual registered item IDs are `irons_spellbooks:uncommon_ink`, `rare_ink`, `epic_ink`, `legendary_ink`. No Common Ink can fill the guaranteed slot.

**Preserve existing vanilla and modded trades.** Iron's already modifies `WandererTradesEvent` by adding scrolls, inks, curios and very rare specialty products. Simply appending new entries to the *event* would still lead to random selection; it CANNOT enforce all five offers. The B20 script instead handles `EntityEvents.spawned('minecraft:wandering_trader', ...)` on server, initializes the trader's actual `MerchantOffers`, appends five offers, and puts a versioned persistence marker on the entity. KubeJS confirms the event also fires on chunk-load; the marker prevents repeated stock injection. The five offers are built before any modification and rolling back a partially added set is attempted on error; any exception logs a visible error and never claims that minimum stock was provided. Old traders will be updated as they re-enter a server chunk; this must be verified.

### Source/API evidence

- Exact pinned Apotheosis `Apotheosis-1.21.1-8.9.0.jar`; source `https://github.com/Shadows-of-Fire/Apotheosis/blob/1.21/src/main/java/dev/shadowsoffire/apotheosis/loot/LootController.java`, `.../tiers/GenContext.java` and `.../affix/AffixHelper.java` (`hasAffixes`).
- Exact pinned Ars Nouveau 5.13.5, source `https://github.com/baileyholl/Ars-Nouveau/blob/1.21.x/src/main/java/com/hollingsworth/arsnouveau/api/registry/GlyphRegistry.java`, `.../api/spell/AbstractSpellPart.java`. The field `part.getConfigTier().value` reads the configured tier and `GlyphRegistry.getGlyphItemMap()` includes third-party addon glyph items.
- Exact pinned Iron's 3.16.3, source `https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/registries/ItemRegistry.java` and `.../player/AdditionalWanderingTrades.java`.
- KubeJS 1.21.1 event/interoperability docs: `https://kubejs-wiki.tysontheember.dev/1.21.1/reference/events/EntityEvents/`; `https://kubejs-wiki.tysontheember.dev/1.21.1/concepts/java-interop/`.
- NeoForge random-trade-list API: `https://aldak.netlify.app/javadoc/1.21.1-21.1.x/net/neoforged/neoforge/event/village/WandererTradesEvent`.

## User-approved B20.1 premium merchant rotation

**2026-11-15 owner correction:** the merchant must stock valuable midgame accelerators, not commonly gathered starter seeds or gems. High value is priced in **more emeralds**. The existing five guaranteed offers remain in place. Each trader gets **four distinct rotating premium offers** drawn uniformly from the ten approved item categories below (no duplicate category per trader). These are server-side `MerchantOffers` additions, not unreliable additions to the `WandererTradesEvent` random-pool.

| Mod | Item / generation | Price (emeralds) | Uses |
| --- | --- | ---: | ---: |
| Occultism | `occultism:iesnium_ingot` (**ONE ingot**, not ore/raw) | 96 | 1 |
| Astral Sorcery | Empty/undiscovered `astralsorcery:constellation_paper` | 48 | 1 |
| EvilCraft | `evilcraft:broom` with **random rod + brush + cap + 2 genuine random broom modifiers**, initially uncharged | 112 | 1 |
| Botania Revived | Normal `botania:mana_tablet` filled to **500,000 mana** via `ManaTabletItem.setMana` (NOT creative) | 128 | 1 |
| Botania Revived | One randomized utility Curio among magnet, mana, aura, far-reach, water or mining rings (no Gaia rings) | 80 | 1 |
| Botania Revived | `botania:black_lotus` | 32 | 2 |
| Roots Classic | `rootsclassic:runic_focus` — ingredient for Engraved Blade | 72 | 1 |
| Malum | One `malum:chunk_of_brilliance` (provisional suggestion for unspecified valuable resource) | 48 | 1 |
| Theurgy | One randomized **Rare Niter** from gems, metals, mobs or other minerals categories | 56 | 1 |
| Forbidden & Arcanus | One `forbidden_arcanus:growing_edelwood` (the plantable Edelwood item) | 64 | 1 |

All prices exceeding 64 emeralds use the merchant's second input slot for another 1–64 emeralds; no diamonds are charged for these premium rotations. Each item is expensive because of its time saved, not its vanilla rarity.

### Version-specific caveats

- **Roots Classic 1.5.10 has no Terra Moss**: that herb belongs to a different Roots mod lineage. The user likely meant **Runic Focus**, a ritual-crafted ingredient used to make the *Engraved Blade*. Sell the Focus and preserve the sword's own ritual. The Engraved Blade is the finished sword itself.
- **Astral Sorcery 2.0.3** blank constellation paper is not permanently blank: its own `inventoryTick` checks `ResearchManager` and `canDiscover(player, progress)`, selects a currently eligible unseen constellation, and calls `memorizeConstellation`. This grants the intended discovery but does not skip a constellation's `canDiscover` gate. Acceptance must verify research/advancement semantics.
- **EvilCraft 1.2.100** already uses a random-part/random-modifier loot algorithm. B20.1 mirrors the same APIs and gives an *uncharged* randomized broom; player must supply the normal blood fuel. This is not just a vanilla item with a renamed tooltip.
- **Botania Revived 2.0.2** uses `ManaTabletItem.MAX_MANA=500000`, with `setMana(ItemStack,int)`. The random accessory whitelist is intentionally lower than boss/gaia equipment.
- **Malum**: `chunk_of_brilliance` is a provisional but source-backed valuable resource, not a claim that it's the particular rare material the user had in mind. **Do not sell Umbral Spirits or Weeping Well output** without a progression audit.
- **Theurgy** rare Alchemical Niters differ from *Precious* Niters. Only the Rare tier is selected; verify the four IDs in the pinned 1.79.0 registry.
- **Forbidden & Arcanus** calls the Edelwood sapling-equivalent **Growing Edelwood**; audit growth on MC 1.21.1 before declaring success.

### Witchery and Hexerei — vetted suggestions, not active

**Witchery / JAWR:** recommend a **Drop of Luck**: it represents substantial cauldron/distillery effort and supports other recipes, making it a better premium trade than seeds. An alternative is **Mutandis Extremis** to reduce plant-variant hunting, but avoid shortcuts around the Altar/cauldron research. Confirm exact IDs and advancement side effects against installed JAWR `0.5.0.39` before enabling.

**Hexerei:** consider an **Ender Satchel** (magic portable storage, perhaps 96 emeralds) or an **Entangled Coffer** (magic logistics, perhaps 112 emeralds). Both relieve materials/organization tedium; choose one that complements rather than displaces Occultism's storage role. Their IDs and actual 0.5.0.3 crafting/research gates need verification. Do not enable until the owner chooses.

### Updated runtime QA

1. Check that a new trader still has all five guaranteed base offers **and four distinct premium offers** in a usable UI. The vanilla and Iron's stock must remain. Existing v1 traders get only the new rotation, no duplicate original offers.
2. Validate high-price merchant 2-slot emerald purchases (96, 112, 128), especially purchases where both input slots contain the same item.
3. Inspect EvilCraft random broom rod/brush/cap and two modifiers; verify riding and blood refilling, durability and tooltip.
4. Confirm exactly 500,000 normal Botania mana, never creative; confirm all accessory possibilities exist and work in Curios.
5. Verify Astral's paper grants only an **currently eligible** constellation after being placed into the player's inventory (and doesn't pre-unlock later research tiers).
6. Confirm 1 Iesnium ingot is unraw, one use/trader; check no TMM progression milestone is skipped merely by item acquisition.
7. Verify Roots Classic Runic Focus is accepted in the Engraved Blade ritual and all four Theurgy rare niter item IDs exist. If one ID or API is unavailable, the selector skips it and draws another distinct premium offer from the remaining catalog; if fewer than four valid options exist, the rotation fails its transaction and logs an error. Do not claim runtime compliance until tested.
8. Maintain no hard gate/boss purchase bypass; in particular ensure rare stock does not grant custom Monk/Hunter achievements or quest unlocks just for item possession.

## Source inspection references
- Astral paper: `HellFirePvP/AstralSorcery` branch `1.21.1`, `ConstellationPaperItem.java`
- EvilCraft broom: `CyclopsMC/EvilCraft` tag `1.21.1-1.2.100`, `LootModifierInjectBroom.java`
- Botania Revived: `Velron-Devs/Botania-Revived` branch `Neo/1.21.1`, `ManaTabletItem.java`, `BotaniaItems.java`
- Roots Classic version-aligned item catalog and ritual recipe: `rootsclassic:engraved_blade`.
- Theurgy Rare Niter IDs: `theurgy:alchemical_niter_{metals,gems,mobs,other_minerals}_rare`.
- Forbidden & Arcanus growing tree item: `forbidden_arcanus:growing_edelwood`.

**Source staged only:** Java API interop and actual trade inventory cannot be certified without running a real NeoForge/Prism world. Existing B20 five-guarantee runtime QA still applies.
