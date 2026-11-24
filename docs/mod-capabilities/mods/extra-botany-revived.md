# Extra Botany Revived 1.0.4 — 29-axis Mod Capability Atlas

**Identity/status:** `extra-botany-revived` Packwiz; mod ID `extrabotany`; NeoForge MC1.21.1; exact pinned `ExtraBotany-1.21.1-1.0.4-NeoForge.jar`. **Research status: source-backed-substantive; not runtime-verified / not fully recipe-exhaustive.** [Immutable source b58857d3](https://github.com/Velron-Devs/Extra-Botany-Revived/tree/b58857d3658479f6e8684d3b45f6d3138a294548). Unlike `botania` base, this addon has its own **Nature Aura** system, **Fluided Mana**, gear, bosses, machines and native portable storage. It is not just three optional boss fights.

## Actual progression: what the player does, and why

| Phase | In-world actions and authentic resource cost | Observable capability / quest-ready success proxy |
| --- | --- | --- |
| A — Unlock addon | Learn its Patchouli/JEI ingredients via existing Botania infrastructure; make one actual lower-tier addon flower and power a native Mana Spreader/Pool. | First real addon flower event/operation; item possession is only acquisition. |
| B — Sustainable generation | Try **Sunshine/Moonlight Lily** (time-limited), Bellflower (wind), Resoncund (sounds), Omniviolet (burns eligible books), Twinstar (temperature contrast) or Reikarlily (lightning). | Actual Mana credited with the documented trigger; do not pretend all flowers yield equal Mana/tick. |
| C — Functional flowers | Demonstrate **Trade Orchid** villager discount, **Enchanter** paid Enchanted Soil conversion, **Woodienia** owner-aware log breaking, **Mirrowtunia** cleansing, **Annoying Flower** fishing loot, or **Serenitian** timer refresh. | True target state or result, not possession of the flower. |
| D — Mana industry | Build **Flux Manafield** (FE→Botania Mana, 1,000 FE→99 Mana default), **Mana Liquefaction Device** (Fluided Mana↔Mana via internal energy) or **Mana Buffer**; monitor input/output and net debit. | Measured before/after debit and output; FE/Fluided Mana/Mana are not the same resource. |
| E — Equipment and Aura | Craft/add Mana rings, weapons/Curios/armor; **Nature Orb** accumulated Aura from its Pedestal, and charges as equipped effect source. Move through Manasteel, Elementium and Terrasteel items; do not skip native mana/gear rules. | Actual worn effect and payment; Nature Orb `SUMMON_EGO_COST=200000`. |
| F — Gaia III | Craft **Challenge Ticket**: 4 Life Essence, 2 Shadowium, 2 Photonium, 1 Gaia Ingot; use on a proper Beacon/Gaia-Pylon arena radius 15 height 7; defeat the separate boss. | Real player-contributor kill; not acquired Pandora's Box item alone. |
| G — Ego (distinct branch) | Charge Nature Orb and **sneak-use** at valid Beacon, paying **200,000 Nature Aura**, meet strict pre-summon inventory; defeat multi-stage Ego. | Real last-stage kill, no early phase loot. |
| H — Herrscher of the Void | Craft **`extrabotany:voidcaller`**, sneak-use on Beacon, pass strict equipment rules; defeat final multi-rank void boss. | Real attacker UUID kill; rare **2% Core of Void** loot is *not* guaranteed on one kill. |

Technical evidence:
- [20 native addon flower algorithms](../inventories/extra-botany-1.0.4-flora.md) (11 generating +9 functional)
- [Exact FE/Fluided Mana/Mana converters and all three bosses](../inventories/botania-extra-1.0.4-energy-and-bosses.md)
- [Portable Mini Handbag and default configuration](../inventories/extra-botany-1.0.4-storage-power-first-source.md)
- [Pinned Botania base systems/43 flowers](botania-revived.md)

## Full 29-axis capability navigation

| Axis | Evidence-based classification and concrete examples |
| --- | --- |
| `gear` | **present** — multiple custom hammers, tiers of shields, armor and Curios; Mana Ring Master, swords, relics, boss equipment. Exact per-item modifiers still native code/JEI. |
| `movement` | **present** — Manasteel/Elementium/Terrasteel Airships, Stardust Lotus creature teleport, gear mobility; item and multiblock prerequisites. |
| `ore_processing` | **partly present** — Stonesia consumes eligible blocks for Mana and Woodienia mines logs; no demonstrated all-ore refinery parity or generic furnace. |
| `storage` | **present** — Mana Buffer 64m, Quantum Buffer 1.024b, Fluided Mana tanks and **24-slot Mini Handbag** with #16 no-nesting compatibility risk. |
| `crafting` | **present** — addon crafting, rune/flower recipes, pedestals, Nature Aura gear, Mana-powered Enchanted Soil, boss tickets. |
| `logistics` | **present** — Manalink bound Mana Pool relay, liquefaction/Fluided Mana transport, buffered Mana transfer and Airship item transport by player. |
| `buffs` | **present** — Nature Orb tier effects, Mirrowtunia cleansing/Thirror, Trade Orchid vendor discount, equipment boosts, specialized potion/effect items. |
| `external_compatibility` | **present** — specifically built for Botania Revived 2.0.2+, NeoForge FE adapters, Curios, real player ownership/protection hooks for Woodienia; no general FE=Mana equality. |
| `mob_farming` | **partly present** — Necrofleur applies harmful effects/damage, Blood Enchantress consumes entity-damage events; no universal spawning without proof. |
| `ore_generation` | **unresolved** — no pinned-source, explicitly arbitrary ore-output generator confirmed here; don't inherit Botania Orechid outputs as an Extra Botany feature. |
| `permanent_power` | **present as item-bound progression** — Nature Orb tier/Aura and relic owner binding; not a persistent Tyr XP system. |
| `general_generation` | **present** — paid Enchanted Soil, fishing loot, passive/specialty Mana production and consumable item rewards from bosses; no creative item replication. |
| `direct_damage` | **present** — boss/relic swords, hammers, Necrofleur damage/debuff; support multiple weapon types and true-damage boss abilities. |
| `block_breaking` | **present** — Woodienia controlled owner-aware log harvesting and assorted hammers; no blanket TMM protection bypass assumed. |
| `permanent_pets` | **unresolved** — no verified persistent summoned ally ownership skill; boss minions are hostile, not player pets. |
| `temporary_pets` | **unresolved** — cannot claim combat minions become player-controlled without verified source. |
| `defense` | **present** — shields, armor, defensive Curios, Mirrowtunia debuff cleanse, Nature Orb tiers. |
| `healing_recovery` | **present** — Nature Orb tier 3 regeneration/healing and Mirrowtunia harmful-effect removal, native brews and accessories. |
| `debuff_control` | **present** — Necrofleur healing reversal, other negative effects, Mirrowtunia cleansing and varied relic/gear effects. |
| `energy` | **present** — Botania Mana (machines/flowers), separate Nature Aura (Nature Orb), addon Fluided Mana (liquid) and NeoForge FE (Flux Manafield); explicit source ledger. |
| `automation_control` | **present** — book/stone/sound/fluid/lightning/event-triggered generators, Woodienia, Enchanter, Manalink, fishing flower, Redstone power gating. |
| `agriculture` | **present** — Enchanted Soil, passive flora refresh and Woodienia timber harvesting; do not infer universal crop automation. |
| `detection_information` | **present** — Mana Reader and environmental stimulus-derived generating flowers; operational named UI/telemetry details require targeted item review. |
| `equipment_augmentation` | **present** — Mana Drive Ring/master ring, lenses, relic swords, enhancements and gear tiers; per-item costs source-specific. |
| `world_manipulation` | **present** — Enchanted Soil conversion, Woodienia breaks logs, Stardust Lotus teleports creatures, environmental flowers; no dimension creation. |
| `world_content` | **present** — addon flowers, materials, boss entities, mount models, loot/advancements, and pedestal structures; worldgen structure counts unresolved. |
| `death_respawn` | **unresolved** — item/Curio defense is not automatically a totem/revive feature; do not invent permanent death prevention. |
| `trade_economy` | **present** — Trade Orchid modifies Villager costs through a Mana-paid discount; not TMM Q Shop currency. |
| `progression_tiers` | **present** — base Botania Mana → addon flowers/gear/Aura → Gaia III, separate Ego and Herrscher boss gates → special relics/airships; 2% Core of Void is rare, not mandatory first-kill guarantee. |

## Decisions, hazards and evidence maturity

- **Configured energy:** TMM economy standard values 20 Mana/EMC and 200 FE/EMC. The inspected active repository contains **no tracked Extra Botany override** of default FE→Mana conversion. Expected upstream **1,000 FE→99 Mana** creates an approximately **1%-loss loop** when paired with 1 Mana→10 FE native Botania Mana Fluxfield. No new design decision is necessary to preserve that default; no client operation verified.
- **Bag policy:** `extrabotany:mini_handbag` is a legitimate 24-slot, craftable portable bag. Its upstream GUI has unrestricted destination slots. It must be reconciled by storage integrator with [#16](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/16) before survival release, not solved by removing the entire mod.
- **Boss loot / item criteria:** use player-owned native kill contributions and separate first-time boss paths. Ego's loot happens only after final stage; Herrscher's Core of Void is probabilistic. Do not claim an item drop proves a unique fight.
- **Native config is not the delivered pack:** configuration specs, registry and Java describe defaults. Check actual world-generated config and merged datapack when claiming `verified_in_pack`.

**Source handoff status:** 20/20 flower-family operations and three bosses mapped, core energy routes mapped, item/mount/relic breadth named, 29 axes classified. **Not fully recipe-exhaustive:** precise recipes for every variant, airship flight physics, all gear effects/Curios slots, every brew/lens and actual pack quest detection remain follow-up. Quest authors should use the current in-game JEI/Patchouli recipe values, not invent node prerequisites. No runtime/FTB modifications or game QA.
