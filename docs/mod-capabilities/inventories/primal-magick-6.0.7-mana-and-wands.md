# Primal Magick 6.0.7 — exact Wand components and typed Mana network economics

**Scope:** 6.0.7 immutable source commit `5977468bda968b994e54066236ec5719295ac9c3`, **not** a published-JAR disassembly or pack runtime throughput test. Mana is stored in **centimana**: 100 internal = 1 displayed Mana. Do not confuse this pool with TMM MXP, Ars Source, Iron's Mana, FE or Neo Vitae EV.

## 1. All 12 registered Wand Core capabilities

[WandCore.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/wands/WandCore.java) defines these actual reusable core data values. Spell slots are native **Wand spell slots**, not inventory/storage slots. Bonus Source adds a slot/alignment specialization; no universal discount is inferred.

| Core | Spell slots | Bonus Source | Native aligned Sources / access character |
| --- | ---: | --- | --- |
| `heartwood` | 1 | none | no listed alignment |
| `obsidian` | 1 | Earth | Earth |
| `coral` | 1 | Sea | Sea |
| `bamboo` | 1 | Sky | Sky |
| `sunwood` | 1 | Sun | Sun |
| `moonwood` | 1 | Moon | Moon |
| `bone` | 2 | Blood | Blood |
| `blaze_rod` | 2 | Infernal | Infernal |
| `purpur` | 2 | Void | Void |
| `primal` | 2 | none | Earth/Sea/Sky/Sun/Moon |
| `dark_primal` | 3 | none | Five terrestrial + Blood/Infernal/Void |
| `pure_primal` | 4 | none | All nine (includes Hallowed) |

These are **component core definitions**, not 12 simultaneously obtainable starter Wands. The component item recipes and ritual upgrade routes belong to the applicable research stages. [Ritual recipe census](primal-magick-6.0.7-ritual-recipes.md) includes the Primal/Dark/Pure Wand and Staff core rituals.

## 2. Mana storage gem and Mana Orb tiers

[WandGem.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/wands/WandGem.java) and [ModularWandItem.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/wands/ModularWandItem.java) establish **per-source stored max** from the selected Gem. [ManaOrbItem.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/tools/ManaOrbItem.java) maps its BASIC/ENCHANTED/FORBIDDEN/HEAVENLY tier to the corresponding Wand Gem capacity and uses an **offhand equipment slot**. Its attunement/state controls which Sources it actually holds.

| Gem and corresponding Orb grade | Maximum stored per eligible Source (centimana) | Displayed Mana per eligible Source |
| --- | ---: | ---: |
| `apprentice` / Basic | 7,500 | 75 |
| `adept` / Enchanted | 25,000 | 250 |
| `wizard` / Forbidden | 75,000 | 750 |
| `archmage` / Heavenly | 250,000 | 2,500 |
| `creative` | -1 sentinel | Infinite; **creative-only, never an ordinary TMM survival recipe** |

Starter `mundane_wand` has 2,500 centimana (25 displayed) max per Source and **zero spell slots**; source [MundaneWandItem.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/wands/MundaneWandItem.java). Its siphon increment is 100 centimana (1 displayed Mana) at each applicable Font siphon interval.

## 3. Wand Cap cost and siphon upgrades

[WandCap.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/wands/WandCap.java) defines the **raw** cost-modifier value and the per-action **centimana** siphon amount:

| Cap | Base cost modifier | Siphon centimana | Siphon displayed Mana |
| --- | ---: | ---: | ---: |
| `iron` | 10 | 100 | 1 |
| `gold` | 20 | 200 | 2 |
| `primalite` | 25 | 400 | 4 |
| `hexium` | 30 | 800 | 8 |
| `hallowsteel` | 35 | 1600 | 16 |

[IManaContainer.getModifiedCost](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/wands/IManaContainer.java) computes `floor(baseCost / (1 + totalModifier/100))`, with a minimum of one internal unit when a nonzero amount is consumed. The **total** modifier also includes relevant Wand Core alignment, gear, enchantments and player attunements. A raw cap value 35 is **not** automatically a flat 35% discount from the final cost.

## 4. Native Font capacities and recharge

[AbstractManaFontBlock.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/blocks/mana/AbstractManaFontBlock.java) and [AbstractManaFontTileEntity.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/tiles/mana/AbstractManaFontTileEntity.java) define capacities and per-tick recharge rates in **centimana**:

| Font class tier | Max stored centimana | Max displayed Mana (its one typed Source) | Regeneration centimana/tick |
| --- | ---: | ---: | ---: |
| Basic / Ancient, naturally occurring | 10,000 | 100 | 5 |
| Enchanted artificial | 1,000 | 10 | 1 |
| Forbidden | 10,000 | 100 | 5 |
| Heavenly | 100,000 | 1,000 | 25 |

[AncientManaFontTileEntity.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/tiles/mana/AncientManaFontTileEntity.java) initializes full; artificial initializes **empty** and recharges each server tick. Native ancient Font scans for players **every 10 ticks, within radius 5**; if player has not already started First Steps, it sets `found_shrine` and displays its trigger. This can be the exact player-centered actor event for `PM-01` if a first-party FTB research criterion bridge is built. It also records each Shrine Source discovery. The Font's network range is **5**, and `canSupply` is restricted to **its own typed Source**.

**Font output is not equivalent to unlimited net Mana:** resource generation, multi-Source requirement, node range and item charge caps remain distinct. No FE output is proven.

## 5. Native injectors, relays and buffers

Source [ManaInjectorTileEntity.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/tiles/mana/ManaInjectorTileEntity.java), [ManaRelayTileEntity.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/tiles/mana/ManaRelayTileEntity.java) and [ManaBatteryTileEntity.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/tiles/mana/ManaBatteryTileEntity.java):

| Device tier | Injector range (blocks) | Relay range (blocks) | Throughput cap (centimana per applicable transfer) |
| --- | ---: | ---: | ---: |
| Basic | 5 | 5 | 400 |
| Enchanted | 5 | 10 | 800 |
| Forbidden | 5 | 15 | 1,600 |
| Heavenly | 5 | 20 | 3,200 |
| Creative | 5 | 20 | 3,200 (source switch only; not approved survival content) |

The **Injector runs a network siphon attempt every 5 server ticks** when its adjacent-below `IManaStorage` can receive Mana; the throughput cap applies in the per-Source transfer method, not as an unconditional every-tick global guarantee. It stores the owner UUID and invokes a native `MANA_NETWORK_SIPHON` advancement criterion on a connected online player. The Relay can route all Source types; the five terrestrial Sources are used in its *visual color cycle*, not to restrict transport. These details matter for FTB event attribution and late-game mixed-Source networks.

**Mana Battery** (the block-entity class used for certain chargers) has a tier-specific storage cap and transfer rate: Forbidden `4 × Wizard Gem` = **300,000 centimana** with **800 centimana/tick transfer cap**; Heavenly `4 × Archmage Gem` = **1,000,000 centimana** with **1,600 centimana/tick transfer cap**. Creative storage is `-1` infinite sentinel. These are **constructor capacities**, not a guarantee of total useful Mana across all nine Source types without wiring and allowed-source checks.

## 6. Concrete quest/QA applications

- **Starter**: verify first Font radius discovery, journal on next sleep, Mundane Wand no spells, 1 displayed Mana siphoned into eligible Source, then `first_steps` requires 10 Mana siphoned overall and one real Observation.
- **Wand upgrades**: compare equivalent modular Wands with Apprentice vs Adept Gem or Iron vs Hallowsteel Cap; verify source-correct capacity, siphon increment and observed modified costs. Do not claim 1:1 FE conversion.
- **Network**: one Source Font → one Relay → Injector above a Source-accepting buffer/device; verify route availability, range failure and correct Source-transfer rate, check owner-only `MANA_NETWORK_SIPHON` advancement rather than item-possession proxy.
- **Risk**: missing Source, offline owner, unloaded chunks, cross-mod inventory adapters and creative-only sentinel flags are separate first-party integration checks. No full server boot, Prism package or gameplay PASS occurred in this source pass.
