# Enchanted: Witchcraft — version 4.2.7 deep dive and Pufferfish compatibility

**Date:** 2026-11-13 (Phoenix local). **Pinned:** `enchanted-neoforge-1.21.1-4.2.7`. **Authoritative source:** [Favouriteless/Enchanted 1.21.1, commit `55f37c7` (2026-05-23)](https://github.com/Favouriteless/Enchanted/tree/55f37c7e15a2848be2cf25ff20232a1893795e75); the release's two fixes landed 2026-05-07 in commits `f803451` (kettle) and `5e7bb3a` (rite time). Official overview: https://www.curseforge.com/minecraft/mc-mods/enchanted-witchcraft ; pinned release: https://modrinth.com/mod/enchanted-witchcraft/version/HCjdv5KS

**Classification: Compatible with work**, source-informed, not Pufferfish-integrated or tested for gameplay in B6. **No gameplay mod, recipes, configs, skill tree, or mod pins were changed.**

## What a player actually does

Unlike Ars/Iron's, Enchanted has no conventional spell bar, direct fireball casting or native caster mana meter. It is a ritual-and-craft witchcraft workshop. The mod's four core disciplines are **herbology, brewing, circle magic, and poppetry**.

1. **Begin** by breaking grass for belladonna seeds and crafting the **Art of Witchcraft** guide (book, ink sac, feather, belladonna seeds). Garden common and magical plants. Mutandis mutates plants from nearby *mutagens* over time; useful to acquire Rowan, Alder, and Hawthorn saplings. Mandrake and other ingredients matter to progressing.
2. **Extract** ingredients with **Witch's Oven** (fumes collected in Clay Jars; side Fume Funnels improve chance and two filters guarantee fume yield), then **Distillery** for ingredient processing. The witch's oven cannot smelt ores. **Important 4.2.7 difference:** in-game guide explicitly calls the old **Witch's Cauldron non-functional**; use **Kettle** for brewing.
3. **Brew** with **Kettle**: bucket of water, heat source below (e.g., fire/campfire/lava), wait until boiling, drop ingredients **in order**, let cook, collect item or bottle. Wrong ingredient or losing heat may spoil. `KettleRecipe` has ordered ingredients, optional `power`, final output; `KettleBlockEntity` spends altar power if recipe requires it. Modern Kettle output supports hopper and NeoForge item/fluid interfaces according to 4.2.2 change log, but actual input/output behavior must be tested.
4. **Build an altar** from **six altar blocks in a 3×2 rectangle**. It draws capacity from nearby *diverse* plants; upgrade blocks placed atop improve effective capacity and recharge. The base `AltarBlockEntity` stores/consumes **world structure power**, not character mana. Defaults in `ServerConfig`: `altar_range = 32`, base recharge **2 power/tick**, subject to upgrades/environment. Workstations and chalk circles within range draw from it. An **Attuned Stone (charged)** serves as a portable altar-energy source for rituals away from the altar.
5. **Perform chalk rites:** place a **golden chalk heart glyph**, arrange required ring sizes and chalk *colors* (ritual / infernal / otherwhere), throw specified item foci into the pattern, perhaps supply a living entity / specific time or weather, then activate the heart glyph. `GoldChalkBlockEntity.use` records the invoking **player UUID**, validates matching data-driven `RiteType`, picks up specific inputs, deducts initial structure power and starts the rite. Some rites also consume `tick_power` while running. A ritual is bound to its physical circle; not an arbitrary constant cast from player anywhere.
6. **Bind taglocks/waystones** to selected targets/locations for distant curses, teleportation and selected ritual effects. Distinct from the generic **Waystones mod** that the user decided to exclude. Craft and bind poppets via **Spinning Wheel**, which also consumes altar power.
7. **Poppets**: protect players from potentially fatal damage, protect tools/armor against breaking, defend against hostile voodoo, or inflict effects on a bound target via voodoo doll. A **Poppet Shelf** can hold four poppets belonging to any player, activating protection without taking inventory space; source `PoppetHelper.tryTriggerAll` checks inventory first then shelf, and wears poppet durability on use.
8. **Familiars and travel:** current native familiar registry in audited source registers **cat only** (not proven other types). Bind an existing tamed cat; owner UUID preserved, can dismiss/summon it through a world-saved `FamiliarSavedData` record. Its specific witchcraft effect: `ApplyCurseRite` uses caster's cat familiar to raise applied curse strength to 1 vs 0, even when dismissed (per 4.1.7 update). A rite infuses the regular broom with **flight**.

## Examples of real rites — stock datapacks

Source: `common/src/main/resources/data/enchanted/enchanted/circle_magic/rite/*.json` (about **50** stock rite variants; some are charged/waystone/targeted variants of one effect).

| Rite | Source semantics |
|---|---|
| **Charging Stone** | Small + medium ritual chalk, 5 ingredients, **2,000 initial altar power**, yields charged Attuned Stone. |
| **Protection** | 500 initial power and **4/tick** for a 4-block radius dome; larger variant 1,000 and 6/tick for radius 6. Protective barrier; special interactions with sneaking players. |
| **Fertility** | Rejuvenates flora and certain entities (e.g., cures zombie villagers), radius 25 in default recipe. |
| **Forest** | Generates plants/forest in area (example radius 25), costs 4,000 initial altar power. |
| **Transpose Iron** | **Extracts existing iron ore** under the circle, destroys world ore blocks and drops ore resources at ritual location. Tags `minecraft:iron_ores`; NOT ore conjuration and not a generic tech-metal miner by default. |
| **Sky's Wrath** | 1,000 initial power, lightning around circle or targeted locations with relevant bound waystone; player/structure impact to test. |
| **Curse of Misfortune** | 2,000 initial power, specific infernal chalk and taglock/ingredients; applies an actual world-saved curse to bound target. The cat familiar gives a higher curse level. Other stock curses include Clumsy, Overheating, Sinking. Curse removal rites also present. |
| **Summon Familiar / Summon Entity** | Separate targetable rites requiring ingredients and at least 1,000/3,000 initial power, respectively. |
| **Total Eclipse** | 3,000 initial power, global environmental manipulation. |
| **Duplicate Waystone** | **Special-purpose bound waystone duplication**, not a generic item dupe: factory `enchanted:duplicate_item` has `item: enchanted:waystone`; actual `DuplicateItemRite` copies a specified consumed item. Do not mistake it for unrestricted duplication. |

The `RiteType` codec supports data-driven `items`, `shapes`, `entities`, `power`, `tick_power`, `weather`, `time`, `dimensions`, and `factory`; modpacks can add or revise **specific rites** via datapacks without compiling new Java, provided their referenced factory exists. Some event priorities and exact cross-loader integrations require runtime verification.

## Pufferfish integration: no registered player stat; concrete bridge seams

- **Altar energy is block-entity state.** `AltarPowerData` computes capacity from environmental `PowerProvider` entries, limited by species/provider and changed by placed upgrades; `AltarBlockEntity` stores power and recharge. **Do not add player magic regen to altar** or merge this into Ars/Iron's shared mana, Psi, Hex media, or Embers resources. No native Pufferfish-compatible player spell/cost attribute was identified.
- **Rite caster is real and persistent:** `GoldChalkBlockEntity.use(... Player)` records player UUID and creates `RiteParams(caster, consumedItems)`. Most player skill adjustments would need a **narrow NeoForge bridge/mixin** at ritual activation (initial cost) or tick charge, not merely a datapack JSON edit. Check ownership, online/offline caster, player changes and whether a standing circle should retain the *original* caster's skill.
- **First reasonable candidate:** a **per-caster ritual-power efficiency** benefit applied at **start time**. Persistent rites' later `tick_power` spending needs an explicit choice—freeze the creator's benefit or never apply it to unattended rites. In either case, avoid a repeatedly recomputed online owner's live bonuses unless intended.
- **Second candidate:** **brewing / witchcraft craft quality**. Recipes and results are item-side; any player-specific yield, potions or successful crafting bonuses require an actual player-attributable creation event or item provenance imprint. Existing kettle/oven/altar machinery often lacks a player context; **do not globally boost an unattended kettle based on a nearby player**. Kettle native `KettleRecipe` optional `power` is datapack-configurable *globally*, not a player attribute.
- **Third candidate:** **poppetry craftsmanship** (longer doll durability, more trigger capacity, better one-use protection). Poppets already store binding target in the item component, and **poppet shelf allows use even away from creator**; imprint any quality permanently on the created item, avoiding skill-buff inheritance or cloning exploits. No implementation approved yet.
- **Familiars:** `FamiliarCat extends Cat` / tameable model with its own registered vanilla attributes (max health 50, armor 5, attack 3). Generic pet buffs can work on the *entity*, but its **curse upgrade is not combat damage** and won't respond to generic magic-damage attributes. Its familiar effect is owner-UUID dependent and may work while dismissed; test pet bonuses against tombstone/death mechanics.
- Source `common/src/main/java` and `neoforge` trees did not reveal an explicit registered personal `enchanted:spell_power`/caster mana attribute nor a first-party Pufferfish integration. **Compatible with work**, not Confirmed runtime support and not Nothing separate to enhance.

## Cross-mod compatibility and technical risks worth explicitly testing

1. **Death interception:** `PoppetEvents.onLivingEntityHurt` checks whether incoming damage would be lethal for a ServerPlayer, then `PoppetHelper.tryTriggerAll` attempts to use a matching doll. This is an **on-hit, before-actual-death rescue**, not obviously an `onDeath` resurrection; compatible in principle with pre-death totems, but **test event order** with vanilla Totem, Hexerei, Enigmatic Legacy+, Vampirism/JAWR lich/vampire traits, Tombstone, and custom transformation compat. Test shelves in unloaded chunks, broken shelf, switching dimensions, player offline, poppet depletion.
2. **Multi-user shared altars:** structure power is not one person's resource. Avoid live Pufferfish user buffs that boost everyone on a shared network, or that let nearby players lend huge bonuses; record who paid and who performed the rite.
3. **Protection / chunk interaction:** protection domes place temporary barrier blocks and maintain observers. Check chunk unloading, external claims, automation inside/outside, cancellation, and restore after server restart.
4. **Magic construction and ore:** Transpose Iron extracts *existing* `minecraft:iron_ores` (and consumes them). The source also has data-driven `TransposeBlocksRite` with configurable block tag, so **audit any new custom rites** that could target previously gated tech ores; stock rite does not. `Duplicate Waystone` is item-specific, not a blanket dupe.
5. **PvP / curse target:** `taglock` binds target via entity UUID, can inflict player curses and remotely applied damage; review fair multiplayer defaults and anti-grief opt-in if desired, **do not introduce bans without user approval**.
6. **Gameplay-specific gotchas:** old Witch's Cauldron marked **nonfunctional** in pinned guide: teach Kettle instead. Specific brewing inputs are ordered and heat is required; do not instruct player to casually drop all items at once. `RiteType.time` got a 4.2.7 bug fix. Kettle result automation exists but source-level semantics and inventory interfaces still warrant in-world test.
7. **Overlapping mods:** Hexerei emphasizes witch tools/cauldrons and brooms; JAWR emphasizes its own Witchery/transformations; Enchanted's altar, circle power and poppets are **independent systems**. Theming overlap does not imply native shared power or mutual recipe compatibility.

## Proposed disposable-world test sequence

- **Onboarding:** obtain belladonna seed by grass, craft Art of Witchcraft; build 3×2 altar; add contrasting plants and confirm capacity/recharge change on GUI; confirm default altar range and environmental limit.
- **Production:** witch oven + jar + fume filters, distiller, kettle with sequential inputs and correct heat; test handling spoiled brews, item extract and hopper automation.
- **Ritual:** small and medium chalk ring, golden heart, recipe items, initial/tick altar power debit and redstone/unload/reload; try charged attuned stone away from altar.
- **Protection and world:** test protection radius/barrier behavior, time/weather/area rites, Transpose Iron on real ore and resource consumption, Sky's Wrath on claims.
- **Poppet:** basic fatal-damage prevention vs vanilla totem; two poppets in order inventory/shelf, depletion, dimensions, shelf chunk unload; tool-break intervention.
- **Familars/players:** bind tame cat, dismiss/resummon, curse strength and the persistence of the effect; ritual initiated by Player A from shared altar vs Player B.
- **Skills proof of concept:** at most opt-in proof (a) 10% initial rite power debit based on temporary test tag/skill, keeping long-term tick charge unchanged (b) single created poppet quality imprint; test moved item, shelf storage, death, login and respec. Require a separate explicit implementation decision before shipping.

## Primary source URLs

- [4.2.7 NeoForge release](https://modrinth.com/mod/enchanted-witchcraft/version/HCjdv5KS)
- [Official description and getting started](https://www.curseforge.com/minecraft/mc-mods/enchanted-witchcraft)
- [In-game guidebook generated JSON](https://github.com/Favouriteless/Enchanted/tree/55f37c7e15a2848be2cf25ff20232a1893795e75/common/src/generated/resources/assets/enchanted/modopedia/books/art_of_witchcraft/en_us)
- [Altar block entity](https://github.com/Favouriteless/Enchanted/blob/55f37c7e15a2848be2cf25ff20232a1893795e75/common/src/main/java/net/favouriteless/enchanted/common/blocks/entity/AltarBlockEntity.java)
- [Rite codec](https://github.com/Favouriteless/Enchanted/blob/55f37c7e15a2848be2cf25ff20232a1893795e75/common/src/main/java/net/favouriteless/enchanted/common/enchanted/circle_magic/RiteType.java)
- [Golden-chalk activation](https://github.com/Favouriteless/Enchanted/blob/55f37c7e15a2848be2cf25ff20232a1893795e75/common/src/main/java/net/favouriteless/enchanted/common/blocks/entity/GoldChalkBlockEntity.java)
- [Poppet lethal-damage protection](https://github.com/Favouriteless/Enchanted/blob/55f37c7e15a2848be2cf25ff20232a1893795e75/common/src/main/java/net/favouriteless/enchanted/common/enchanted/poppet/PoppetEvents.java)
- [Familiar curse level](https://github.com/Favouriteless/Enchanted/blob/55f37c7e15a2848be2cf25ff20232a1893795e75/common/src/main/java/net/favouriteless/enchanted/common/enchanted/circle_magic/rites/ApplyCurseRite.java)
