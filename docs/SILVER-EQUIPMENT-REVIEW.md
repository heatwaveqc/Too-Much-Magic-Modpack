# Silver equipment — source audit and consolidation target (2026-11-14)

**Status:** Design analysis, not yet implemented. User goal: stop offering three indistinguishable, identically crafted Silver Swords. Retain one craftable silver tool/armor entry per slot with useful properties from the included mods. This work depends on the [Polymorph recipe audit](RECIPE-CONFLICT-AUDIT.md) and the [silver material audit](../manifest/material-ore-audit.json).

## What the pinned pack actually contains

Four sources of **silver ore/ingots**: Occultism, Embers Re-Ignited, Eidolon: Repraised, Werewolves. But there are only **three complete silver tool lines**: Embers, Eidolon and Werewolves. Occultism supplies metal, silver dust and magic implements, **not a generic Silver Sword/pickaxe/axe set**.

### Native silver tool tiers (source: installed mod 1.21.x upstream trees)

| Property | Embers | Eidolon | Werewolves | Proposed iron-class composite |
|---|---:|---:|---:|---:|
| Durability | 202 | 193 | **250** | **250** |
| Mining speed | **7.6** | 7.0 | 6.0 | **7.6** |
| Attack-damage tier bonus | 2.0 | 2.0 | 2.0 | 2.0 (do not stack) |
| Enchantability | **20** | **20** | 14 | **20** |
| Harvest capability | Iron-class | Iron-class | Iron-class (special incorrect-for-silver tag excludes diamond needs) | Iron-class |
| Silver-sword unique behavior | None in generic ToolSet sword | **Ignites Eidolon-valid undead on hit for 5 seconds** | **Triggers Werewolves silver effect on hit; class also implements `ISilverItem`** | Both effects exactly once |

Eidolon's undead predicate is a vanilla **undead entity-type tag** or Eidolon's **Undeath effect**, *not* automatically all transformed vampire players or every supernatural species. Cross-mod vampire recognition is a separate transformation-compat decision; avoid inventing a global 'silver kills vampires' bonus here.

Other tool differences include Eidolon's faster axe/pickaxe/shovel attack speed, but none of these three sets has a unique mining spell, ore-revealing function, automatic smelting, or charge-dependent enchantment. **Embers' special Clockwork tools are separate items and not part of this consolidation.**

### Silver armor

| Armor property | Eidolon | Werewolves | Target |
|---|---|---|---|
| Protection helmet / chest / legs / boots | **2 / 6 / 4 / 2** (14 total) | **2 / 6 / 5 / 2** (15 total) | Werewolves defense (15) |
| Enchantability | **20** | 9 | 20 if supported by tested compatibility code |
| Durability | Type-specific `getDurability(20)` (helmet 220, chest 320, legs 300, boots 260) | All four slots currently get `Type.CHESTPLATE.getDurability(15)` = **240 each** | Prefer per-slot max(Eidolon, Werewolves), but confirm repair/breakage behavior |
| Werewolf contact sensitivity | None inherent | **Yes**, implemented directly by `SilverArmorItem` and `ISilverItem` | Preserve |
| Werewolf attacker retaliation | None inherent | **Yes**, code checks `instanceof ISilverItem` (NOT just an item tag) | Preserve |
| Visual/model | Eidolon custom silver armor model and texture | Werewolves' silver material and texture | One practical default, optional cosmetic replacement later |

Werewolves' silver-armor retaliation and direct on-wear silver status effects are not automatically portable via ordinary item tags: the relevant event handlers test `ISilverItem`. An arbitrary existing Eidolon armor item will *not* gain this particular retaliation by adding `werewolves:armor/silver`. **Prefer Werewolves' native silver armor for canonical crafting** unless implementing a dedicated shared-class/event compatibility bridge.

### Critical native mechanics in Werewolves

- `onEntityAttacked` applies Silver effect when the attacker's weapon belongs to **`werewolves:tools/silver`**. This works for *other mods'* swords **if added to that tag**. See `ModEntityEventHandler.onEntityAttacked`.
- `werewolves:type/silver` already nests the `werewolves:tools/silver` tag, and inventory checks use this tag or `ISilverItem`. Therefore tagging an Eidolon sword as a silver tool will also preserve werewolf *wielder* sensitivity. Do NOT accidentally make unified silver harmless to lycanthropes who wield it.
- `SilverSwordItem` itself is a basic `SwordItem` subclass plus `ISilverItem`; it does **not** implement a different sword attack algorithm. The actual anti-lycan debuff is an event listener driven by the tag.
- `SilverArmorItem` inflicts the Silver effect on a werewolf wearing it, while defensive retaliation to werewolf attackers is checked by `instanceof ISilverItem`. A simple tag-only bridge is insufficient for non-Werewolves armor to duplicate that particular effect.
- Silver effect affects movement speed and attack damage, and increases damage taken by werewolf players. Strength/duration come from Werewolves config and the Silver status effect; **do not stack a second flat damage multiplier** on top.

### Proposed consolidation, minimal custom code

| Slot | Proposed craftable item | Reason |
|---|---|---|
| **Sword** | `eidolon_repraised:silver_sword` | Already ignites undead; add to `werewolves:tools/silver` (automatic anti-werewolf effect and sensitivity through existing hooks) |
| **Axe, pickaxe, shovel, hoe** | Default to **Eidolon** silver tools | Their fast attack animations and baseline magic-tier enchantability are useful; a startup item/tier modification can target Embers' **7.6** mining speed and Werewolves' **250** durability, subject to exact 1.21.1 KubeJS verification |
| **Helmet, chestplate, leggings, boots** | `werewolves:silver_<slot>` | Keeps native `ISilverItem` armor retaliation and contact weakness; selectively improve enchantability/durability if technically supported |

This is the **one-craftable-result-per-slot** model. It does NOT mean deleting native registered items belonging to installed mods; doing so would break integration, loot, savefiles and potential recipe references.

Recipe migration and upgrades need to:
1. Retain the canonical item's ordinary silver-ingot + stick/pattern recipe and remove only the duplicate crafting outputs. For the sword, remove `embers:silver_sword` and `werewolves:silver_sword`; keep `eidolon_repraised:silver_sword`. For armor, remove Eidolon's armor recipes; keep Werewolves'. Audit exact recipe IDs and all downstream consumers first.
2. Retain existing noncanonical silver items as *legacy-compatible*: old inventory stacks and explicit recipe ingredients continue to work. Convert or hide them only after verifying no required uses. Do not repurpose tags in ways that allow magical transformations to be bypassed.
3. Add selected Eidolon tools to `werewolves:tools/silver`; this also gives the native Werewolves Silver status effect to targets when appropriate. No new damage event required for the sword.
4. Verify that an alternate tool item in the Werewolves silver tag suffers its own silver-contact penalties when wielded by a werewolf, and damages werewolves as intended.
5. Test whether KubeJS `ItemEvents.modification` can safely raise item max durability and tools' effective speed/enchantability **for this pinned build**. The KubeJS 1.21.1 docs warn some tier mutation APIs are broken; do not promise full min/max stat combinations without a modded client test. If not possible, retain vanilla native differences or build a small compatibility mod with explicit rules.
6. Test that Werewolves armor really retaliates against werewolves and causes weakness when worn by lycanthropes. Verify no double application and no unexpected effects on other transformations.
7. Re-run Polymorph native diagnostic and confirm all silver equipment collision recipe groups are gone (other duplicates may remain and need independent fixes). Confirm JEI and manual crafting after startup.
8. Ensure flavor-specific upgraded weapons or rituals (Eidolon Athame, Embers Clockwork, Werewolves Silver Oil/Bolts, Occultism Infused tools) stay available and are *not* collapsed into this generic silver tier.

## Evidence (actual source, not name-based inference)

- **Eidolon sword undead ignition:** `Alexthw46/Eidolon-Repraised@1.21.1`, `src/main/java/alexthw/eidolon_repraised/registries/Registry.java`, `SILVER_SWORD` anonymous `hurtEnemy` override. `Eidolon.java` defines `isValidUndead`.
- **Eidolon tool stats:** `src/main/java/alexthw/eidolon_repraised/common/item/Tiers.java`, `SilverTier`; armor `EidolonMaterials.java` and `SilverArmorItem.java`.
- **Embers tools:** `Riieno/EmbersRekindled-Reignited@main`, `src/main/java/com/rekindled/embers/util/EmbersTiers.java` and `RegistryManager.java`, `ToolSet` generic constructors.
- **Werewolves tool stats:** `TeamLapen/Werewolves@1.21`, `util/WUtils.java` and `core/ModItems.java`; armor `ModArmorMaterials.java` and `items/SilverArmorItem.java`.
- **Werewolves event/class hooks:** `entities/ModEntityEventHandler.java` and `entities/player/ModPlayerEventHandler.java`; `core/ModTags.java` and generated `data/werewolves/tags/item/{tools/silver,type/silver,armor/silver}.json`.
- **Practical balance:** all three native generic silver tool tiers are iron-harvest class and have the same tier attack bonus. The composite raises convenience without approaching diamond durability or mining tier.
- Mod sources are tied to the 1.21.x line; compare to the actual pinned released JARs before calling behavior fully integrated.

## Decision to implement later, not now

The **principle** is locked: a single usable, integrated silver equipment result per slot, with undead ignition + anti-lycan silver effect + werewolf armor retaliation preserved. The exact numeric stat normalization, final visual asset, method of item/tier modification, and conversion of legacy stacks remain implementation choices pending verification, not an implicit authorization to weaken or delete features.
