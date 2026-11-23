# Gaze 1.1.7.1 — complete binary recipe and Rite registration census (Atlas)

**Verified directly from owner-supplied unchanged release binaries, 2026-11-23.** SHA-512:
- Gaze `gaze-1.1.7.1.jar`: `7e9ecdcc996f376e280bf46f5c0cfd328fb6f1228b1dac469390031458e7a81ad56acff03d9bb6b57b8209586798ab443be1e91fa8fe56297a9e7c9451db03e2`
- Malum `malum-1.21.1-1.8.2.jar`: `dd9ccc1dea3b090f69f807be3e7a8b99ec6d3d7d6b047e2893cb49ca505860bbf640cf767082c1a32ea403b35e2a9f9d1755327890bbfb2f8778058a9597a99e`

**Both hashes match the current TMM Packwiz pins.** Static inspection used ZIP entries, JSON parsing and `javap -c -p` bytecode disassembly. No game boot, live registry initialization, recipe-datapack merge, TMM overrides/EMC check, or actual world acceptance. No JAR assets/binaries committed. This record **corrects** the earlier Atlas and PR #375 assumption that Gaze's 26 Rite classes are only effect variants.

## 1. All 24 bundled Gaze-authored recipes, fully inventoried

The recipes are physically in `data/malum/recipe/` inside **Gaze's JAR** rather than `data/gaze/recipe/`. Exact count: **14 Spirit Infusion + 8 Runeworking + 2 Soul Binding**. All are real serialized JSON; no extrapolated recipes. These tables use full registry IDs for base inputs and output. Unprefixed extra ingredients are `malum:` unless obvious vanilla names (Nether Star, Prismarine Crystals, Amethyst Shards, Wind Charge, Nautilus Shell). Spirit names all refer to `malum:` types. Count abbreviations are **recipe costs**, not stacks per inventory slot.

### 14 Spirit Infusions

| Recipe / result (`gaze:`) | Main input | Other item inputs | Typed Spirit costs |
|---|---|---|---|
| `spirit_infusion/adaptive_wheel` → `gaze:adaptive_wheel` | malum:large_strange_crystal ×1 | Fused Consciousness ×1; Auric Embers ×4; Malignant Pewter Plating ×8; Hallowed Gold Ingot ×4 | Umbral 1; Wicked 32; Eldritch 32; Earthen 64 |
| `spirit_infusion/charge_necklace` → `gaze:charge_necklace` | malum:ornate_necklace ×1 | Fused Consciousness 1; Void Salts 6; Null Slate 18; Mnemonic Fragment 6 | Umbral 1; Eldritch 32; Arcane 36; Wicked 54 |
| `spirit_infusion/compromise_ring` → `gaze:compromise_ring` | malum:ornate_ring ×1 | Fused Consciousness 1; Malignant Lead 12; Cthonic Gold 6; Nether Star 1 | Umbral 1; Arcane 30; Sacred 54; Wicked 18 |
| `spirit_infusion/enchantment_workbench` → `gaze:enchantment_workbench` | malum:soulwood_item_pedestal ×1 | `#malum:soulwood_planks` 4; Soulwoven Silk 4; Hallowed Gold 2; Malignant Pewter Plating 2 | Sacred/Arcane/Eldritch/Wicked 4 each; Infernal/Aerial/Earthen/Aqueous 8 each; Umbral 1 |
| `spirit_infusion/encyclopedia_unveiled` → `gaze:encyclopedia_unveiled` | malum:encyclopedia_esoterica ×1 | None | Umbral 1 |
| `spirit_infusion/meditation_ring` → `gaze:meditation_ring` | malum:ring_of_echoing_arcana ×1 | Fused Consciousness 1; Malignant Pewter Ingot 6; Null Slate 12; Mnemonic Fragment 6 | Umbral 1; Wicked 60; Eldritch 36; Arcane 30 |
| `spirit_infusion/seidhr` → `gaze:seidhr` | malum:tyrving ×1 | Fused Consciousness 1; Auric Embers 6; Blazing Quartz 6; Malignant Pewter Plating 12; Hallowed Gold 6 | Umbral 1; Wicked 30; Arcane 18; Eldritch 36; Infernal 54 |
| `spirit_infusion/sluggish_brooch` → `gaze:sluggish_brooch` | malum:runic_brooch ×1 | Concentrated Gluttony 6; Soul-Stained Steel Plating 6 | Infernal 30; Wicked 42; Earthen 16 |
| `spirit_infusion/soul_rapier` → `gaze:soul_rapier` | malum:soul_stained_steel_sword ×1 | Fused Consciousness 1; Natural Quartz 8; Tainted Rock 8; Malignant Pewter Plating 4 | Infernal 32; Wicked 16; Arcane 16; Aqueous 8; Earthen 24 |
| `spirit_infusion/spirit_channel_pouch` → `gaze:spirit_channel_pouch` | malum:soulwoven_pouch ×1 | Hallowed Gold Ingot 6 | Umbral 1; Eldritch 18 |
| `spirit_infusion/spirit_saber` → `gaze:spirit_saber` | malum:malignant_pewter_ingot ×2 | Complete Design 1; Prismarine Crystals 8; Amethyst Shards 8; Malignant Pewter Plating 4 | Umbral 1; all other eight Spirit types 32 each |
| `spirit_infusion/splintered_world` → `gaze:splintered_world` | minecraft:dragon_egg ×1 | Complete Design 1; Auric Embers 6; Blightpearl 6; Malignant Pewter Plating 24; Hallowed Gold 12 | Umbral 1; Earthen/Aerial/Aqueous/Infernal 30 each |
| `spirit_infusion/swift_spiral` → `gaze:swift_spiral` | minecraft:trident ×1 | Prismarine Crystals 12; Nautilus Shell 4; Malignant Pewter Plating 4; Wind Charge 8; Zephyr Impetus 1 | Aerial 32; Arcane 16; Aqueous 24 |
| `spirit_infusion/veil_dagger` → `gaze:veil_dagger` | malum:soulwood_log ×26 | Cursed Sapball 1; Living Flesh 6; Blighted Gunk 6 | Earthen 18; Aqueous 30; Aerial 24; Infernal 54; Wicked 48; Sacred 24; Arcane 42; Eldritch 42 |

**Progression:** The Soul Rapier, Swift Spiral and Sloth Brooch omit Umbral in their actual recipes; many others require one Umbral. The Enchantment Workbench and Encyclopedia Unveiled both directly require Umbral. Splintered World has a `minecraft:dragon_egg` input; **do not presume renewable/EMC-eligible acquisition**. Veil's Edge has a 26-log central count; preserve it rather than simplifying to one log. These are useful tutorial/gate distinctions.

### 8 Rune recipes

All eight use recipe type `malum:runeworking`, consume the listed primary input and **one `malum:umbral_spirit` as `secondaryInput`**. Each outputs one of the exact Gaze Rune IDs and is eligible for the Curios `#curios:rune` item tag.

| Result ID | Display name (JAR en_us) | Main input | Primary count | Source-level effect/description |
|---|---|---|---:|---|
| `gaze:rune_of_beyond_the_veil` | Mimir | `malum:revealed_soulwood_log` | 8 | Reveal invisible entities |
| `gaze:rune_of_out_of_phase` | Svalinn | `malum:astral_weave` | 8 | Physical protection with nearby enemy tradeoff |
| `gaze:rune_of_blighted_presence` | Hodr | `malum:blighted_soulwood` | 8 | Occasionally blight attackers |
| `gaze:rune_of_touch_of_void` | Nott | `malum:void_salts` | 16 | Void-related strike effect (exact runtime not yet verified) |
| `gaze:rune_of_ymir` | Ymir | `malum:azure_runewood_sapling` | 16 | Weigh down flying enemies |
| `gaze:rune_of_gymir` | Gymir | `minecraft:glow_ink_sac` | 16 | Pull struck creatures toward depths |
| `gaze:rune_of_fafnir` | Fafnir | `malum:blighted_gunk` | 16 | +0.1 malignant conversion base modifier; conditional overflow benefits need client validation |
| `gaze:rune_of_eir` | Eir | `minecraft:wind_charge` | 16 | Recover Soul Ward after shatter |

Rune names deliberately differ from IDs for **Mimir, Svalinn, Hodr and Nott**. Item ID aliases must never be fabricated from the translated names.

### 2 Soul Bindings

| Recipe file | Registered effect | Main input | Extras | Spirits |
|---|---|---|---|---|
| `soul_binding/domain_of_swords` | `gaze:domain_of_swords` | `gaze:spirit_saber` ×1 | `malum:hallowed_gold_ingot` ×16; `malum:mimicry_relay` ×4; `malum:mnemonic_fragment` ×8 | Umbral 1; Arcane/Aerial/Infernal 64 each |
| `soul_binding/pact_of_encroaching_malice` | `gaze:pact_of_encroaching` | `malum:void_salts` ×12 | `malum:living_flesh` ×6; `malum:imitation_heart` ×6; `malum:malignant_pewter_ingot` ×6 | Umbral 1; Arcane/Eldritch/Wicked 60 each |

Recipe names/effect IDs are intentionally not identical for Encroaching Malice. Binding occurs on target via native Soul Brazier; a Geas is not an ordinary produced output stack.

## 2. Major correction: 26 new *registered* Gaze Rites

The exact `com.strawberry.gaze.registry.GazeRiteRegistry` class contains **26 `RiteHolder<SpiritRiteType>` fields and 26 actual `register(...)` calls to `MalumSpiritRiteTypes.RITE_TYPES`**. This is the same shared `DeferredRiteTypes` collection whose `getEntries()` the pinned Malum `MalumSpiritRiteTypes.getRite` scans for pattern matches. Gaze also constructs a separate `GazeRiteRegistry.RITES` deferred register, but **its 26 definitions are placed into the shared Malum register**, not via that separate field.

**Therefore the 26 are additional native-discoverable Rite type identities, not merely 26 independent effect classes attached to the existing 26 base Malum Rites.** The registration namespace derives from the **Malum** `RITE_TYPES` register, so the expected IDs are `malum:gaze_...` and `malum:corrupt_gaze_...`, **not** `gaze:...`. They require separate live-registry QA to confirm startup/ordering, but the register and lookup path are positively established in pinned bytecode. Prior PR #375's 26-only MXP catalog is now a known undercount requiring separate Progression code review.

**Ordered builder Spirit sequences** (the precise in-world pole orientation must be verified against the native user interface). Corrupted variants use `setCorrupted()`, i.e. the separate corrupted-base route. `malum:` namespace applies to all row IDs.

| Registered Rite ID after `malum:` | Spirits in builder order | Base |
|---|---|---|
| `gaze_lesser_arcane_rite` | Eldritch, Arcane, Arcane | ordinary |
| `gaze_greater_arcane_rite` | Eldritch, Eldritch, Sacred, Arcane, Arcane | ordinary |
| `gaze_lesser_sacred_rite` | Eldritch, Infernal, Sacred | ordinary |
| `corrupt_gaze_lesser_sacred_rite` | Eldritch, Infernal, Sacred | corrupted |
| `gaze_greater_sacred_rite` | Eldritch, Eldritch, Sacred, Sacred | ordinary |
| `corrupt_gaze_greater_sacred_rite` | Eldritch, Eldritch, Sacred, Sacred | corrupted |
| `gaze_lesser_aerial_rite` | Eldritch, Aerial, Aerial | ordinary |
| `corrupt_gaze_lesser_aerial_rite` | Eldritch, Aerial, Aerial | corrupted |
| `gaze_greater_aerial_rite` | Eldritch, Eldritch, Earthen, Aerial | ordinary |
| `corrupt_gaze_greater_aerial_rite` | Eldritch, Eldritch, Earthen, Aerial | corrupted |
| `gaze_lesser_aqueous_rite` | Eldritch, Aerial, Aqueous | ordinary |
| `corrupt_gaze_lesser_aqueous_rite` | Eldritch, Aerial, Aqueous | corrupted |
| `gaze_greater_aqueous_rite` | Eldritch, Eldritch, Wicked, Aqueous | ordinary |
| `corrupt_gaze_greater_aqueous_rite` | Eldritch, Eldritch, Wicked, Aqueous | corrupted |
| `gaze_lesser_earthen_rite` | Eldritch, Aerial, Earthen | ordinary |
| `corrupt_gaze_lesser_earthen_rite` | Eldritch, Aerial, Earthen | corrupted |
| `gaze_greater_earthen_rite` | Eldritch, Eldritch, Earthen, Earthen | ordinary |
| `corrupt_gaze_greater_earthen_rite` | Eldritch, Eldritch, Earthen, Earthen | corrupted |
| `gaze_lesser_infernal_rite` | Eldritch, Earthen, Infernal | ordinary |
| `corrupt_gaze_lesser_infernal_rite` | Eldritch, Earthen, Infernal | corrupted |
| `gaze_greater_infernal_rite` | Eldritch, Eldritch, Sacred, Infernal | ordinary |
| `corrupt_gaze_greater_infernal_rite` | Eldritch, Eldritch, Sacred, Infernal | corrupted |
| `gaze_lesser_wicked_rite` | Eldritch, Earthen, Wicked | ordinary |
| `corrupt_gaze_lesser_wicked_rite` | Eldritch, Earthen, Wicked | corrupted |
| `gaze_greater_wicked_rite` | Eldritch, Eldritch, Arcane, Wicked | ordinary |
| `corrupt_gaze_greater_wicked_rite` | Eldritch, Eldritch, Arcane, Wicked | corrupted |

The Gaze values include **2 Arcane, 6 groups of four Sacred/Aerial/Aqueous/Earthen/Infernal/Wicked variants**, total 26. This preserves exact builder sequences without inventing new FTB detectors or claiming every effect is proven in-world.

## 3. Gear, Curios, spells and UI

**Six base combat items** tied to native Gaze `GazeMod` registrations: `gaze:veil_dagger` (Veil's Edge), `gaze:splintered_world`, `gaze:soul_rapier`, `gaze:spirit_saber`, `gaze:swift_spiral`, `gaze:seidhr`. Additional `gaze:astral_splintered_world` and `gaze:astral_splintered_worldv1` variants appear in code, but have **no independent static recipe among the 24**; do not present them as ordinary crafted seventh/eighth weapons. There are seven Better Combat weapon-attribute JSONs in Gaze (`astral_splintered_world`, `seidhr`, `soul_rapier`, `spirit_saber`, `splintered_world`, `swift_spiral`, `void_dagger`), not seven additional survival crafting recipes. `gaze:spirit_saber` receives its active Spirit from the player's offhand according to the pinned English guide text; changing inserted Spirit has an item-consumption warning.

**Five distinct equipable Curios other than the runes:** `gaze:sluggish_brooch` (#curios:brooch), `gaze:meditation_ring` and `gaze:compromise_ring` (#curios:ring), `gaze:charge_necklace` (#curios:necklace), `gaze:adaptive_wheel` (#curios:head). Their localized abilities are respectively sluggish/sloth brooch, idle Soul Ward/staff recovery, mage Soul Integrity compromise, Spirit-fueled charge recovery, and up to 30% adaptive damage resistance (last is an in-JAR displayed figure, not gameplay measured). Malignant rune Fafnir explicitly adds +0.1 to Malignant Conversion in `RuneFafnirItem`; higher-value overflow interactions need QA.

**Guidebook progression is now verified:** `malum:encyclopedia_esoterica` + 1 Umbral Spirit → `gaze:encyclopedia_unveiled` through native Spirit Infusion. `EncyclopediaGazeItem.use` on the client calls `GazeProgressionScreen.getScreen().openCodexViaTransition()`. Gaze's `GazeProgressionScreen` registers dedicated codex entries, including subspaces and rune/Geas entries; it is a **guide/progression UI**, not proof of a separate server skills tree, leveling currency or enforced capability gates. Player visit and effective unlock behavior need client verification.

**Enchantment Workbench:** `gaze:enchantment_workbench` has the explicit recipe above. `FusionAltarBlockEntity` exposes central/pedestal enchantment collection, `validatePedestalItems`, `recalculateCost`, `hasAllSpirits`, `consumeAllSpirits`, and extraction; constants show horizontal range 4, vertical range 3, warmup 30 ticks, craft 200 ticks. Native Gaze guide describes placing a book centrally, enchanted items on pedestals, combining enchantments onto the book while stripping them from materials, and altered behavior with Soulwood Pedestals/Umbral. **No live preservation/no-duplication behavior established**; as a cross-mod enchanting mechanism it warrants scoped TMM testing if configured.

**Iron's compatibility:** one addon spell class `com.strawberry.gaze.compat.irons_spellbooks.spells.soulward_shield.SoulwardShieldSpell` registered via `SpellRegistry`; in-JAR language calls it **Arcana Shield**. Do not infer it is always available if Iron's optional compatibility is not loaded or not registered; no second spell class found.

## 4. Spirit-Channel Pouch — resolved contents semantics and design conflict

Pinned class `SpiritChannelPouchItem extends SoulwovenPouchItem`, **not** a new Spirit-only item handler. Its constructor inherits the `SOULWOVEN_POUCH_CONTENTS` component and the generic parent insertion/transfer logic, whose acceptance predicate checks `Item.canFitInsideContainerItems()`, not a Spirit-only tag. Pinned Malum `SoulwovenPouchContentsComponent.getStorageSize()` returns **512** generic weighted units.

- **Shift + use** sets the held item's `gaze:dump_location` (BlockPos) and `gaze:dump_dimension` component to the player's current position and dimension.
- **Ordinary use** with contents attempts to drop **copies of all contained item stacks** into the target block location in the **same dimension only**, at a **loaded chunk**. On successful drop loop it clears the pouch contents and assigns a **200-tick cooldown**. Target never loads chunks merely to dump. Missing binding, dimension mismatch, or unloaded target reports failure (and should preserve contents).
- Storage and normal item insertion remain inherited and **general-purpose**. The remote dump is additional **cross-location item routing**, which is a stronger conflict with TMM's no-generic-backpack/logistics exclusions; no Spirit-only exception can be assumed.
- **UI inconsistency:** pouch tooltip computes fullness via weight×128 but displays a fixed denominator of **256**, while inherited contents capacity is **512**. Treat as a potential visual bug requiring in-game confirmation; do not advertise the tooltip number as actual capacity.
- The Gaze recipe **upgrades a real `malum:soulwoven_pouch`**, so disabling only the base pouch recipe can also incidentally block this Gaze item. Source-level recipe guards, loot and other acquisition must be reviewed together before selecting the eventual policy. If any pouch remains available, the #16 server-side *cross-mod bag nesting* protection must cover its exact ID.

## 5. Pinned Malum constructor parity

Exact Malum 1.8.2 `BerserkerGeas.class` constructor disassembly confirms `MalumGeasEffectTypes.PACT_OF_THE_REAPER` is passed to the base `GeasEffect`, agreeing with its versioned upstream source. Registry `MalumGeasEffectTypes` still maps `pact_of_the_berserker` to `BerserkerGeas::new` and its persistence registry entry is distinct. The mismatch is **confirmed in the published JAR**, not just a source-branch suspicion. Runtime impact still depends on event behavior/identity consumers; do not claim game failure without a scoped test.

## 6. Research/QA boundary and action

- **Source-ready for Atlas quest research:** all 24 Gaze recipe objects; 26 additional registered Rite type IDs + patterns; eight Rune crafting paths + equip slots; six base weapons + explicit special variants; five Curios; exact book progression UI open route; exact generic remote pouch behavior; Malum Berserker published-bytecode identity mismatch.
- **Separate implementation:** Progression's first-only Gaze Rite receipt catalog needs a targeted new code PR if intended to cover all native registered Rites (see linked handoff issue). Portable storage inclusion/removal and bag-nesting bridge are pack-owned, not Atlas runtime edits.
- **Still requires in-game or deeper exact-bytecode checks:** quantitative Geas/weapon/Rite effects, exact Fafnir set interaction, conditional Iron's integration, client UI and Bestiary display, Enchantment Workbench item retention, TMM-specific EMC and tag overrides, game registration/load results, effective cross-mod compatibility. No client/server/process test was performed.

**Do not copy these JARs or their image/code assets into the repository.** Source facts only, pinned checksums for reproducibility.
