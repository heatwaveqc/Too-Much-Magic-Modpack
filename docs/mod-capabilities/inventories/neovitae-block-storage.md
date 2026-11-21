# Neo Vitae — typed block energy/storage and creative battery audit

**Evidence:** directly inspected `BloodTankBlockEntity.java`, `BloodTankBlock.java`, `FluidTieredRecipe.java`, `BaseTieredRecipe.java`, `BloodBatteryBlockEntity.java`, `BloodBatteryBlock.java` and generated recipes at frozen [source `2744123bb6e4f72c602178b23384be41956eef23`](https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23). Exact TMM pin `neovitae-1.21.1-1.1.53.jar` is version-aligned but **not JAR-byte-compared, datapack-reconciled or gameplay-tested**.

## 1. Blood Tank — a fluid, not an FE battery

Block `neovitae:blood_tank` stores **NeoForge fluid** through `FluidTank` and exposes a block fluid handler. Its source recipe is [`hellfire_forge/blood_tank.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/hellfire_forge/blood_tank.json): three colorless glass blocks + Bloodstone, minimum 30 Spiritus in gem, consuming 5. The block-item registers with `CONTAINER_TIER=1`; if an item lacks a tier component, tooltip warns that the tier is missing. Its Fluid Content component stores the fluid on pickup and can restore it on re-placement.

**Native capacity scale:** there are **16 sequential Blood Tank tiers**, doubling the capacity at each tier. Each bucket is 1,000 mB by standard NeoForge units. Capacities come from exact source `BloodTankBlockEntity.CAPACITIES`, which the block multiplies by `FluidType.BUCKET_VOLUME`.

| Tank tier | Capacity buckets | Capacity mB |
|---|---:|---:|
| 1 | 16 | 16,000 |
| 2 | 32 | 32,000 |
| 3 | 64 | 64,000 |
| 4 | 128 | 128,000 |
| 5 | 256 | 256,000 |
| 6 | 512 | 512,000 |
| 7 | 1,024 | 1,024,000 |
| 8 | 2,048 | 2,048,000 |
| 9 | 4,096 | 4,096,000 |
| 10 | 8,192 | 8,192,000 |
| 11 | 16,384 | 16,384,000 |
| 12 | 32,768 | 32,768,000 |
| 13 | 65,536 | 65,536,000 |
| 14 | 131,072 | 131,072,000 |
| 15 | 262,144 | 262,144,000 |
| 16 | 524,288 | 524,288,000 |

**Upgrade operation (distinct from magical altar tiers):** [`recipe/blood_tank_upgrade.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/blood_tank_upgrade.json) is a `neovitae:fluid_tiered` shaped recipe:

```text
g g
tgt
gsg
```

Where `g` = colorless glass block tag, `t` = a **Blood Tank**, `s` = Bloodstone. Special recipe fields `primary=3` and `secondary=5` identify the two Tank slots. The `BaseTieredRecipe` implementation requires **both tanks have the same tier**, not tier 0 and not tier 16. The output is **one tank of tier +1** (capped at 16, but tier-16 inputs do not match at all).

**Fluid persistence/merge behavior:** both source tanks carry a `FLUID_CONTENT` item component. When assembling, equal-fluid tanks combine their fluid amounts. When exactly one is empty, it uses the nonempty tank. When both hold different fluids, `FluidTieredRecipe` defaults to the primary tank's contents—**potential loss of the secondary tank's fluid**. Test this with harmless fluids in a disposable world and caution users; don't award a tank upgrade before checking its contents. Maximum-tier capacity and recipe shape are source-backed, but exact overflow handling of fluid components is a separate runtime question.

**Quest coverage:** first fluid transfer (Forge Tank), two same-tier tanks to make a tier-2 Tank, one safe empty-tank upgrade, one persistence break/re-place test; late-game capacity as a voluntary storage specialization, **not** a required Ara Vitae tier gate. Record tank fluid and player Anima separately.

## 2. Blood Battery — explicitly **FE**, and source creative/debug behavior

`neovitae:blood_battery` is **not** an EV/fluid tank and **not** a demonstrated survival mana→FE converter. The source `BloodBatteryBlockEntity` owns `net.neoforged.neoforge.energy.EnergyStorage`:

- FE capacity: **10,000,000 FE**.
- Maximum transfer: **100,000 FE** per storage operation.
- When powered by a neighboring redstone signal, **its tick routine refills any missing energy**. This is a *creative-style FE source*, not a finite blood-to-electricity conversion: there is no EV/Anima/Spiritus debit anywhere in the inspected class.
- It persists FE in NBT and exposes a block FE handler to capable pipes, but the source tooltip explicitly includes `tooltip.neovitae.blood_battery.creative`.
- No survival recipe for `blood_battery` appeared in the inspected generated recipe root/subdirectories; **other loot/commands or pack scripts were not exhaustively searched**. Mark normal-survival access `unverified / likely creative-only` rather than granting a quest or claiming a legitimate battery farm.

**Pack-design consequence:** this block is a high-priority **creative/debug and EMC bypass audit**. TMM is intended to earn magical conversion and Draconic FE progression; accidentally providing an infinitely redstone-powered 100k FE/tick-or-transfer source would be a major progression shortcut. Inspect reachable loot, shop, EMC, tech gadgets and crafting before including the block in any quest or resource-production ranking. This is a **research warning**, not a request to blacklist without owner approval.

## 3. Other separate scopes for completeness

| Resource | Carrier | Source scale | Caveat |
|---|---|---|---|
| EV fluid | Altar basin/hidden input-output and reserve | Tier-0 basin 10,000 EV; overflow to 20× capacity per guide | Distinct fluid compartments, rune throughput modifiers |
| Portable EV fluid | Six Orbs | 4,000 → 14,000 fluid units | Not the same as player Anima |
| Anima | Per-player bound network | Max 5,000 → 10,000,000, determined by Orb + modifiers | Stored on player-linked system, not an item fluid |
| Spiritus | Five Gems and infused equipment | 64 → 16,384 for Gems | One aspect per Gem, actual item charge |
| Spiritus Aura | Chunk field | 100 per Aspect by default | Not a block inventory; pumps and seeds move between scopes |
| Spiritus Accumulator | Attuned block | 1,000 of one selected Aspect | Only Aura-skims until source floor and network output |
| Forge Energy | Blood Battery | 10,000,000 FE, self-refilling with redstone | Creative/debug interface; **no established blood energy conversion** |

**Evidence completeness:** all numbers are direct version-aligned source class constants or generated JSON. The actual 1.21.1 TMM build, FTB detector for container tier, and source-to-binary parity remain unverified.
