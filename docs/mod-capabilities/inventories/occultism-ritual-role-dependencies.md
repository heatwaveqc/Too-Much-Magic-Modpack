# Occultism 1.227.0 — named ritual, worker, familiar and infrastructure dependency crosswalk

**Atlas second pass — 2026-11-21. Scope:** Exact pinned `occultism-1.21.1-neoforge-1.227.0.jar`, SHA-512 `063d775420e1efa916d1ed94f3f5bfc3b00b976b1de6fc333eda9886ea3665aa38254407a18469ed0a334bcac67b204392c9bf197fc10916ce36a9e29abd5ef0`. Cross-index of already source-verified [158 native ritual records](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/research/mod-comprehension-local-jars-20261116/docs/mod-comprehension/mods/occultism-pinned-rituals.md), [all 20 built patterns and 244 ordinary recipes](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/research/mod-comprehension-local-jars-20261116/docs/mod-comprehension/mods/occultism-pinned-crafting-guide.md), [worker implementation](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/research/mod-comprehension-local-jars-20261116/docs/mod-comprehension/mods/occultism-pinned-worker-jobs.md), and [familiar lifecycle](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/research/mod-comprehension-local-jars-20261116/docs/mod-comprehension/mods/occultism-pinned-familiars-tools.md). [Full 29-axis dossier](../mods/occultism.md) · [quest-source map](../mods/occultism-quest-map.md). Research-only: no source JAR recompiled, no mod/test/client/quest/recipe changes.

**Purpose:** A usable authoring reference by **which exact family of pentacle, activation, prerequisite item, sacrifice/event, target entity/item, and actual resulting capability**. A generic “perform Afrit ritual” checkbox would miss the material and action differences below. We deliberately do **not** copy all 158 recipe rows (available in the pinned reference); this is their first/mid/high-end crosswalk.

## 1. All 158 ritual recipes classified by 17 exact pentacle IDs

The native 158 recipes are distributed over **17** ritual-use pentacle identifiers, independent of the **20** serialized multiblock reference layouts (some references are demonstration/debug, and possession/summon/craft variants are distinct). Use the exact Dictionary preview *for that recipe*, not the tier's other circle.

| Native ritual pentacle ID (`occultism:` prefix) | Native JSONs | Acquired capability — examples | Correct level/story |
| --- | ---: | --- | --- |
| `craft_foliot` | 15 | Fragile Soul Gem, first miner, Storage Controller Base, Satchel, Stable Wormhole, White/other materials, tier1 Stabilizer, Ritual Satchel T1 | Foliot **craft** pattern, not `summon_foliot` |
| `craft_djinni` | 16 | Dimensional Matrix, Mineshaft, Extractor, Remote, Soul Gem, Familiar Ring, infused pickaxe, entity wormhole, tier2 Stabilizer | Djinni **craft** circle |
| `craft_afrit` | 13 | Afrit Miner Deeps, tier3 Stabilizer, Battlefield, ritual satchel upgrade/T2, Iesnium Butcher Knife/special bowls | Afrit **craft** circle |
| `craft_marid` | 6 | Marid Master Miner, tier4 Stabilizer, True Sight Staff, Iesnium Anvil, Dragonyst Dust | Marid **craft** circle |
| `summon_foliot` | 10 | Farmer, Lumberjack, Cleaner, Transporter, Crusher/Crystallizer/Smelter, Otherstone/Otherrock/Sapling Traders | Foliot **worker**; job bound to ritual |
| `summon_djinni` | 11 | Processor tiers2, Machine Manager, Gambler, time/weather jobs, Wandering Trader, Demonic Husband/Wife | Djinni **worker/merchant/partner**, output type differs |
| `summon_afrit` | 5 | Processor tiers3, rain and thunder weather jobs | Afrit **worker** |
| `summon_marid` | 3 | Processor tiers4 | Marid **worker** |
| `possess_foliot` | 13 | Early **Beaver, Blacksmith, Deer, Greedy** familiars, other possession encounters, special Parrot/Chicken gamble | Foliot **possession**, not ordinary summon |
| `possess_djinni` | 19 | **Bat, Beholder, Chimera, Cthulhu, Devil, Dragon, Fairy, Headless, Mummy, Otherworld Bird** familiars; higher possessed mobs | Djinni **possession** |
| `possess_afrit` | 5 | **Guardian Familiar**; higher-risk possessed entities | Afrit **possession** |
| `possess_marid` | 2 | Possessed Goat/Mercy Goat and Iesnium Golem | Marid **possession**; neither is normal familiar ritual |
| `possess_unbound_afrit` | 2 | Possessed Guardian or Zombie Piglin | Dedicated unbound Afrit circle |
| `contact_wild_spirit` | 23 | Wild-contact world materials: budding amethyst, reinforced deepslate, bee nest, bells, horse armor and trims | Not the normal bound-worker or Eldritch contact route |
| `contact_eldritch_spirit` | 10 | Ancient Eldritch Miner, tier5 Stabilizers, stabilized Controller, Trinity Gem, Void/Rainbow Chalk, special Chalices | Distinct late ritual shape and sacrifices |
| `resurrect_spirit` | 3 | Restore Familiar/eligible mob shard, special Allay rite | Requires actual shard/entity/event, not a free pet item |
| `summon_unbound_afrit` | 1 | Hostile Afrit encounter→Afrit Essence via *mob kill/loot* | Different pattern than Afrit craft/summon |
| `summon_unbound_marid` | 1 | Hostile Marid encounter→Marid Essence via *mob kill/loot* | Requires Trident-use event; not direct essence |

**Counts:** 15+16+13+6+10+11+5+3+13+19+5+2+2+23+10+3+1+1 = **158**; these are 18 entries, *not 17*. Correct family count is **18**, counted directly from the source table and including `possess_unbound_afrit`, `summon_unbound_afrit` and `summon_unbound_marid` independently. 20 native multiblocks include storage demo/debug and possible pentacle variants; do not equate raw 20 layouts with 18 recipe-used IDs. The above row inventory, not the informal “17”, is authoritative.

**Ritual output is not the JEI preview icon.** The native factory roster is 47 craft, 9 craft-with-spirit-name, 5 craft-miner-spirit, 4 repair, 3 upgrade, 29 summon, 17 summon-tamed, 26 summon-spirit-with-job, 15 summon-wild, 1 chance-of-chicken summon, and 2 resurrect-familiar = **158**. Item delivery, *world entity*, and virtual/conditional effect need different quest completion criteria. Duration JSON values below are **counters**, not wall-clock seconds for every Golden Bowl upgrade. Standard tier1 advances about one counter step/20 ticks at default duration multiplier and tier2 at 1/5 ticks; the advanced bowls use other source paths. The extra sacrifice or item-use is an **event**; entity drops/held-use events are not automatically an extra stack consumed by the ritual. Follow source and actual named rite.

## 2. First actual job and physical worker control

First Crusher recipe `ritual/summon_foliot_crusher`: `summon_foliot` circle, bound Foliot Book, counter60, four distinct offerings **Iron Ingot, Gold Ingot, Copper Ingot, Silver Ingot** tags; native output is **owned SpiritEntity with `occultism:crush_tier1`**, *not* one `foliot` spawn-egg item. Drop a valid raw input and confirm delivered result (default raw iron→2 dust). Its worker is separate from a book of binding or later Crushing output. There are **10 Foliot job rites**; a generic Foliot doesn't automatically farm/carry/mine.

| Job / exact native ritual | Native tier / inputs that distinguish it | How player proves useful work | Important boundary |
| --- | --- | --- | --- |
| `summon_foliot_farmer` | Foliot Book, `summon_foliot`, counter60; Datura, Wheat, Carrot, Potato and hoe tag | configure Calling Book **Set Base/Deposit**, watch mature **CropBlock** crop and deposited yield | 5×5 target-centered mature-crop harvest; default-state replant; not a tag-general kitchen crop farm |
| `summon_foliot_lumberjack` | Foliot Book, counter60; Otherworld/Oak/Birch/Spruce Saplings and axe tag | witness selected tree fall and sapling replanted into dirt | source breadth-first log destruction follows **connected logs outside initial search boundary**; isolate structures |
| `summon_foliot_transport_items` | Foliot Book, counter60; Minecart, Chest, Dispenser, Hopper | configure Set Extract and Set Deposit and confirm item crosses | physically walks; native actual extract may use deposit-facing side rather than selected extract side |
| `summon_foliot_cleaner` | Foliot Book, `summon_foliot`, filter eligible items and deposit | demonstrate actual dropped-item pickup + delivery | matching blacklist/tag logic; source scan chance throttles pickups |
| `summon_djinni_manage_machine` | Djinni Book, `summon_djinni`, counter120; Coal Block, Gold Ingot, Iron Ingot, Furnace | assign machine + Controller, request input and observe real processed output | requires loaded controller/proxy, capacity, facings; queued request is not stock reservation |
| `summon_djinni_gambler` | Djinni Book, counter120; Emerald, Diamond Block, Gold Ingot, Silver Block | trade one native matching input and observe returned result | selectable weighted result, limited worker lifetime; not a guaranteed specific item |
| `summon_djinni_day_time` / `night_time` | Djinni Book and different time-symbol offerings, counter60 | observe actual world-time transition | finite one-shot jobs, not persistent clock automation |
| `summon_afrit_rain_weather` / `thunder_weather` | Afrit Book, counter90; weather-specific offerings + **cow-tag sacrifice** | observe matching world rain/thunder outcome | initialized bolt is visual-only and not added to world by inspected parent method: don't teach as real lightning generator |

**Calling Book controls (pinned `BookOfCalling` methods):** (1) acquire the job-specific book; (2) sneak-interact with **owned** compatible worker to link; **reusing the linked book on the same spirit captures it**, so don't mislabel that action as “configure”; (3) use book nonsneak to choose mode in GUI; (4) sneak-use a block with valid item-handler face to set area/extract/deposit/machine; (5) observe *actual work and inventory movement*, not merely book possession. Work-area choices Small/Medium/Large are **16/32/64 width units**, but pickup goals use different boxes/radii; don't give one universal “16-block radius”. Transporter and Machine Manager do not turn this pack into generic global logistics.

**Tiered processing worker defaults** (not guaranteed effective TMM TOML):

| Family | Foliot | Djinni | Afrit | Marid | Caveat |
| --- | --- | --- | --- | --- | --- |
| Crusher time multiplier | 2 | 1 | 0.5 | 0.3 | On base raw iron time200→400/200/100/60 counter ticks |
| Crusher/crystallizer output factor | 1 | 1.5 | 2 | 3 | `floor(base result×factor)`, with recipe opt-out of multiplier |
| Crystallizer time multiplier | 1 | 0.5 | 0.3 | 0.1 | Separate 54 recipe JSONs, not “all crusher material becomes crystals” |
| Smelter time multiplier | 1 | 0.5 | 0.1 | 0.01 | Fuel-free worker across smelting/blasting/smoking/campfire; **no separate output multiplier** |
| Batch operations | 1 | 1 | 1 | 1 | Native server defaults; consumption/production may differ after config/events |

**Output loss:** Crusher/Crystallizer/Smelter/Trader use a Dimensional Extractor directly beneath a worker and compatible inventory two blocks below, but **suppress loose output and ignore insertion remainder** when a handler is present. Full/rejecting inventories can lose results after input was spent. Teach one empty receiving inventory before scaled processing; don't promise all automation paths are transactional.

## 3. Five miner-item ritual steps — what “Afrit mining” concretely requires

The **Dimensional Mineshaft** itself is a **Djinni craft** rite `ritual/craft_dimensional_mineshaft` (counter150), with four Otherstone/Otherrock compound offerings, Gold Ingot, **Iesnium Block** and Spirit Attuned Crystal. The five miner recipes below produce a **named miner item**, **not a live walking spirit**; insert it in the actual Mineshaft. Weighted ore rolling, durability/enchantments, output capacity and redstone belong to the block operation, not the summoning rite.

| Miner item (exact ritual ID under `occultism:`) | Circle / counter | Exact required stepping-stone ingredients (plus tier Book unless specified) | Actual first use and what it does **not** prove |
| --- | --- | --- | --- |
| `ritual/craft_miner_foliot_unspecialized` | `craft_foliot` /60 | **Empty Magic Lamp, Iesnium Pickaxe**, Iron Ingot, Gravel | Foliot miner's access already depends on Iesnium Pickaxe despite Foliot book. Do not put it before all Iesnium acquisition or assume an ungated Foliot miner |
| `ritual/craft_miner_djinni_ores` | `craft_djinni` /150 | **Foliot miner**, Iesnium Pickaxe, Gold Ingot, Lapis, Spirit Attuned Crystal | Upgrades the previous item; ordinary native `#occultism:miners/ores` includes Foliot and Djinni, not a material permission wall |
| `ritual/craft_miner_afrit_deeps` | `craft_afrit` /300 | **Djinni miner**, Iesnium Pickaxe, Spirit Attuned Crystal, **Afrit Essence, Echo Shard, Crying Obsidian** | **First approved TMM mining tier for Draconium + Deepslate Replica Ore.** Native `#occultism:miners/deeps` includes Afrit and Marid, but **pack recipes need their own override/addition** |
| `ritual/craft_miner_marid_master` | `craft_marid` /510 | **Afrit miner**, Iesnium Pickaxe, Spirit Attuned Crystal, **Netherite Pickaxe, Dragon's Breath, Totem of Undying, Nether Star, Marid Essence** | Higher miner can inherit Deeps output; do not teach it as *first* Draconium/Replica access |
| `ritual/misc_miner_ancient_eldritch` | `contact_eldritch_spirit` /780 | **Mining Dimension Core Piece** activation + native **8 Marid Master Miners** + **human-tag sacrifice**; draft [#338](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/338) replaces offerings with owner-approved special materials and targets unbreakable result, **not merged** | Separate **`#occultism:miners/eldritch`** pool gives integrated `tmm:eldritch_logic_core` (weight250), **not** the first Replica/Draconium access under current owner policy |

**Ore policy is settled:** native uranium/worldgen/miners are allowed normally (Bigger Reactors retired); Draconium and `replication:deepslate_replica_ore` are Afrit/Marid results, with alternative previously approved fishing and high magic pathways preserved. In pinned native 194 miners, `miner/ores/draconium_ore.json` uses `#occultism:miners/ores`, containing Foliot; move its eligibility via TMM recipe override to `#occultism:miners/deeps` and add **new** Replica weighted result under this tag. The Atlas does **not** invent its exact weight or claim either pack change is integrated. [Owner superseding note](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/236).

## 4. Dimensional storage tiers — a true ritual ladder separate from four spirit tiers

First obtain **Storage Controller Base** with `ritual/craft_storage_controller_base` (Foliot `craft_foliot`, counter60; Otherstone Pedestal + **two copper ingots and one gold ingot**), then **Dimensional Matrix** with `ritual/craft_dimensional_matrix` (Djinni `craft_djinni`, counter150; **three Quartz Blocks + one Ender Pearl**). Place Matrix above Base in a 1×2 craft to form Controller; **actually insert/retrieve** before treating it as storage. Native base capacity **128 item types / 256,000 total items**.

| Stabilizer stage / native recipe | Circle / counter | Input progression and notable payment/event | Source-default *additional* capacity per correctly facing physical stabilizer |
| --- | --- | --- | --- |
| Tier1 `ritual/craft_stabilizer_tier1` | `craft_foliot` /60 | Tier0 pedestal, Copper Block, Blaze Dust, Spirit Attuned Gem | +64 types, +512,000 total item count |
| Tier2 `ritual/craft_stabilizer_tier2` | `craft_djinni` /150 | Tier1, Silver Block, Ghast Tear, 2 Spirit Attuned Gems | +128 types, +1,024,000 items |
| Tier3 `ritual/craft_stabilizer_tier3` | `craft_afrit` /300 | Tier2, Gold Block, Totem of Undying, Spirit Attuned Crystal, Afrit Essence | +256 types, +2,048,000 items |
| Tier4 `ritual/craft_stabilizer_tier4` | `craft_marid` /510 | Tier3, **Iesnium Block, Beacon**, 2 Spirit Attuned Crystals, Marid Essence | +512 types, +4,096,000 items |
| Tier5 `ritual/misc_stabilizer_tier5` | **`contact_eldritch_spirit`** /780 | **Tier4 is activation**, Sculk Shrieker, Netherite Block, Crying Obsidian, Iesnium Block, 2 Spirit Attuned Crystals, Enchanted Golden Apple, Dragonyst Dust, **Enderman-tag death** | +1,024 types, +8,192,000 items |

Every tier1–5 has a separate native **`_dark` variant** with corresponding dark-lineage input but the same capability tier, not five extra progression steps. Real stabilizers must face toward six rays searched out to range5 from controller height+1; not arbitrary blocks placed near the Controller. **Tier5 is not simply “Marid+1”**: it uses the Eldritch contact ritual and sacrifice. **Stabilized Controller** `ritual/misc_stabilized_storage` is a further Eldritch upgrade: Calibrated Sculk Sensor activation, existing Controller, **six Tier5 Stabilizers (normal or dark)**, two Echo Dust and **Shulker-tag sacrifice**, counter780. Native configured stabilized-controller pool is **8× tier5 equivalent** (default8320 types/65,792,000 items), distinct from six physical rays. [Storage source contract](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/research/mod-comprehension-local-jars-20261116/docs/mod-comprehension/mods/occultism-pinned-storage-harvest.md).

**Remote vs Wormholes:** Stable Wormhole is actually craftable at Foliot `craft_foliot` (but cannot act as linked storage until a **Djinni** Matrix/assembled Controller exists). Storage Remote is Djinni `craft_djinni`, linked by native `useOn` on controller; does not load target chunk. Entity Wormhole is a **different Djinni device** with compass destinations, not storage.

## 5. Familiar acquisition by actual possession tier (17 capability roles, not 17 direct summon rituals)

Source-defined normal native familiar rites are **15**: **four Foliot**, **ten Djinni**, **one Afrit Guardian**. Two additional familiar entities, **Goat and Shub Niggurath**, arise through the Chimera/Goat transformation path and are **not** independent `ritual/familiar_goat` / `ritual/familiar_shub` summon recipes. A separate `ritual/familiar_parrot` at Foliot uses `summon_with_chance_of_chicken_tamed`: the handler constructs with tame=false and selects Parrot1/3 or Chicken2/3; this is **not** the same acquisition guarantee as an owned `IFamiliar`. Always label the selected ritual and **required death-tag event**, not simply “buy a familiar.”

| Tier / exact ritual prefix `occultism:ritual/` | Summon event target / key offerings | Demonstrable player benefit or mechanism | Caveat |
| --- | --- | --- | --- |
| Foliot `familiar_beaver` | Pig-tag death; 4 log-tag blocks | log break-speed effect; upgraded Beaver snack/limited log-chop | Not a universal tree machine |
| Foliot `familiar_blacksmith` | Zombie-tag death; Iron Shovel+Pickaxe+Axe, Anvil, 4 Stone | upgrades eligible familiar using iron | Native **18 iron**/upgrade, specific ownership and cooldown |
| Foliot `familiar_deer` | Cow-tag death; 4 wooden rods +2 strings | Speed, Jump, Step Height | movement/attribute and overlap conditional |
| Foliot `familiar_greedy` | Zombie-tag death; Chest, Iron Block, Dispenser, Hopper | pickup and locate sample block after upgrade | Doesn't mine a located block; component/owner filters |
| Djinni `familiar_bat` | Bat-tag death; Golden Carrot, Spider Eye, Glowstone, Lava Bucket, Torch | Night Vision; upgraded lifesteal on eligible **kill**, not every hit | Death credit/handler constraints |
| Djinni `familiar_beholder` | Spider-tag death; 4 Spider Eyes +4 Glowstone Dust | Glowing/target ray and special defense after upgrade | Only recent owner enemies/range; not blanket AoE |
| Djinni `familiar_chimera` | Sheep-tag death; assorted leather/string/feather/wool/bone and four meats | combat and size-based **mount**, detachable Goat branch | size>80 for riding; native Goat starts **attached** |
| Djinni `familiar_cthulhu` | Squid-tag death; 8 fish-tag | Water Breathing, Prismarine trade, upgraded temporary light | not unlimited permanent light |
| Djinni `familiar_devil` | Horse-tag death; magma, bones, Lava Bucket | Fire Resistance, gated Golden Apple→Enchanted Golden Apple | long native cooldown |
| Djinni `familiar_dragon` | Horse-tag death; lava/flint/coal/quartz/gold/gunpowder/obsidian | boosts existing nonzero XP gains | **not** ordinary item-loot multiplier |
| Djinni `familiar_fairy` | Horse-tag death; 2 Golden Apples, Ghast Tear, 3 Gunpowder, Milk Bucket | protects *another* familiar; upgraded Dragon Breath trade | cooldown, sitting, owner restrictions |
| Djinni `familiar_headless` | Snow Golem-tag death; wheat/hay/rods/Carved Pumpkin | head-type attack + Weakness | native six-piece rebuild may never clear disabled state |
| Djinni `familiar_mummy` | Llama-tag death; slime, paper, wool | probabilistic attack dodge | source entity/type and bypass exceptions |
| Djinni `familiar_otherworld_bird` | Parrot-tag death; feathers, egg, leaves, string | jump/double jump, upgraded limited fall defense | nearby vs equipped effect differs |
| Afrit `familiar_guardian` | **Human-tag death**; four Diamonds, two Golden Apples | **finite** player lethal-event rescue | source spends lives before some failure checks; not unconditional immunity |
| Chimera→Goat→Shub | Owned Chimera Golden Apple detaches Goat; Goat transformation uses **black dye + Ender Eye + Flint** in allowed biome | mobile Goat/late support Shub entity; Shub spawns limited-life combat assistants | not a separate summon ritual; effective biome tag/owner/interaction controls |
| Foliot special `familiar_parrot` (not in 17-role count) | **Chicken-tag death**; four dyes, feather/string | mixed Parrot/Chicken outcome | 1/3 versus2/3; tamed assignment not guaranteed |

**Useful quest dependencies newly exposed:** Familiar Ring itself requires **Djinni `craft_familiar_ring`**, **Soul Gem** (also Djinni, crafted from Foliot Fragile Soul Gem), **two Gold and two Silver Ingots**. Consequently **a Foliot familiar cannot immediately be worn in a ring just because its Foliot ritual is available**. Guardian rescue requires Afrit and a **human-tag** ritual sacrifice; don't offer a Guardian lesson as a Foliot/Blacksmith-only continuation. Blacksmith upgrades should be demonstrated only after its own Foliot ritual. Likewise the exact `Iesnium Pickaxe` is an input to *every* normal miner ladder stage, not just the Mineshaft.

**Recovery and truthful credit:** Ring capture/removal serializes entity data; ring release can transfer owner and clears payload before spawn success acknowledgment; unfamiliar shard/Great Resurrection can consume payload without valid spawn. A player may have learned/crafted a ring but never activated its Curios effect. The best quest evidence is **actual chosen familiar owner+effect/trade/upgrade**, with a manual observation proxy until a TMM-authored per-player event predicate exists. If two familiars both provide similar effects, choose one demonstration rather than force all 17.

## 6. Ritual choice boundaries and exact-pack overlays

- **Ars Ocultas** has an owner-approved 5,000 Source nonconsuming Occultism sacrifice/item route. Teach it as an *alternate payment* for applicable ritual flags, **not** a free substitute for bound tier/pentacle/rite requirements.
- **TMM integrated Thallasium→Iesnium** Spirit Fire is 1:1 and provides an **additional material route** in the exact pack, not necessarily a shorter way around all ritual ingredients. [Current recipe](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/kubejs/data/tmm/recipe/spirit_fire/iesnium_from_thallasium.json).
- **Eldritch Logic Core** is already an Eldritch miner-weighted result, not an Afrit reward. [Current recipe](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/kubejs/data/tmm/recipe/miner/eldritch_logic_core.json).
- **Draft #338** (not integrated) changes book crafting/purity/salvage, Gem Dust Spirit Fire and the Ancient Eldritch rite. **Draft #336** (not integrated) introduces five Occultism Iron's ink rituals, with a *consumed bound book* as payment and Common Foliot×4 → higher tiers×2. They must not be counted as current native routes/mandatory quest inputs.
- **Item/permission/loot boundary:** No automatic full-safe storage/worker release promises. The native Dimensional Extractor worker output does not roll back rejected insertion; satchel nesting guard draft #401 is not integrated. Capture/Battlefield/Trinity operate on distinct tags and the shared #373 policy is still staged; do not infer boss-output permission from a drop-table name.
- **Quest rewards:** XP plus safe common items; never pre-pay a scarce miner prerequisite, Spirit Essence, Nether Star, Totem of Undying, Soul Shard, Trinity, special boss loot, Afrit-tier Draconium/Replica or first Logic Core as a trivial chapter reward.

## 7. Handoff: direct quest-map corrections and deferred code-ready changes

The [57-label quest map](../mods/occultism-quest-map.md) should preserve **first accessible ritual then first delivered result**. In particular: (a) `OC-34` explicitly list empty Magic Lamp + Iesnium Pickaxe for first miner; (b) `OC-42` offer Foliot familiar options first, with Djinni and Guardian separately gated; (c) `OC-43` Familiar Ring only after Djinni and Soul Gem, *not* after `OC-42` alone; (d) `OC-45` Guardian only after Afrit, independent of Blacksmith upgrade; (e) `OC-39` separate Tier4 Marid stabilizer from Tier5 **Eldritch** contact/shulker sacrifices; (f) `OC-48` Foliot Fragile Soul Gem can teach low-risk capture **before Afrit**, Djinni Soul Gem later; (g) `OC-47` may use early Foliot possession and should not require Djinni for every choice.

**Code-ready first-party work, not new gameplay decisions:** TMM's Draconium output selector must move from Foliot-inclusive `#occultism:miners/ores` to `#occultism:miners/deeps` for Afrit+Marid; add a corresponding weighted `replication:deepslate_replica_ore` miner entry there. Preserve all native uranium availability and existing Logic Core late gate. The recipe ID, collision/override strategy and weighted chances are implementation choices with narrow tag/loaded-recipe QA after source change, not unresolved balance votes here. **No runtime code change was authorized/performed by this research pass.**

**Evidence discipline:** Source-tables were parsed and counted here, and the exact high-impact recipes/dependencies were cross-compared. No assembled pack, in-game GUI, save/reload or equipment test was performed; native upstream caveats are documentation, not a mandate to test untouched mods.
