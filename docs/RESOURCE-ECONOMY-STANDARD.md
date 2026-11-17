# TMM resource economy — owner-approved accounting baseline (2026-11-17)

Status: owner approved the accounting rates and audit targets; implementation and final item map remain pending. Ore hierarchy below is a separate design proposal, not yet owner-approved. Tracks #47 (EMC coverage), #44 (ProjectE gates), #318/#319 (parchment). Source baseline: integration 2f662b9e8b73cee47a1e23bf6d1f3510dd41d8ae.

## Locked accounting constants

| Resource | Units per 1 EMC |
| --- | ---: |
| EMC | 1 |
| Botania Mana | 20 |
| Forge Energy / equivalent OP (verify native transport parity) | 200 |
| Ordinary canonical EvilCraft blood | 5 mB |

These are valuation units, not an instruction to expose universal bidirectional exchange. Classify transactions as equivalent (conserve value), lossy (convenience), or productive (explicit renewable resource, time, infrastructure or progression investment). Record deliberate profitable paths and reject unintended unbounded conversion cycles. Compute net value from all inputs, outputs and container remainders; use final in-game EMC map, not assumed defaults. Do not nerf intended farms solely because they produce EMC.

## Implementation targets, not yet deployed

- Keep Mana Fluxfield at 10 FE/Mana; verify actual transfer, buffers and throttling.
- ProjectE Energy Converters 0.1.4: documented defaults 1,000 EMC/FE forward and 1,000 FE/EMC reverse. Eventual target 160 FE per EMC and 250 FE per EMC reverse (80% efficiency each direction; 64% round trip). Retain existing 1,000 FE/EMC reverse until major loops are audited. Verify exact config type/semantics, fractional conversion or batching support and source debit, integer conservation, tiers/throughput, gating, and OP support before implementation. Never change just one direction without checking the combined economy.
- Botania Conjuration Catalyst: price net item gain at 20 Mana per EMC, round up to 100 Mana and apply minimum 200 Mana. Example upstream EMC values (not final pack map): coal 2,600, redstone 1,300, glowstone dust 7,700, quartz 5,200, soul sand 1,000, netherrack 200 Mana. Audit every recipe, including addon/pack recipes; recompute if EMC changes.
- Mana-refined materials: baseline item EMC plus consumed Mana / 20; use integer rounding consistently. Existing native infusion costs: manasteel 3,000, mana pearl 6,000, mana diamond 10,000 Mana. Example upstream item values 406, 1,324, 8,692 respectively; these are illustrative and must follow final metal map. Parchment is an explicit acquisition-gate exception: 100,000 Mana, seven-paper EMC parity (provisional 224), reverse to paper retained.
- Preserve approved Thermalily buff: 75 Mana/tick for 600 ticks = 45,000 Mana per lava source under sufficient transfer, zero cooldown. Intentional productive infrastructure; audit renewable lava and ProjectE/Volcanite loops, not a reason to revert the owner-approved buff automatically.
- Preserve native Draconic Generator efficiency differences pending audit. Coal: 80,000 / 24,000 / 16,000 / 12,800 / 8,000 FE/OP-equivalent by Eco Plus, Eco, Normal, Performance, Performance Plus (source calculation). Reactor is intended to be highly productive endgame infrastructure; measure real net power including containment and fuel.
- EvilCraft tier-3 bone + 2,500 mB blood -> blaze rod is an approved productive progression example (upstream 144 + 500 -> 1,536 EMC, net +892). Do not automatically reprice every inefficient convenience recipe. Ordinary blood is valued 50 EMC per 250 mB and 200 per 1,000 mB; maintain canonical blood boundaries.
- Fluid transmutation via filled containers is a candidate, not permission to bypass gates: container value plus fluid (ordinary blood 200/bucket, water 0, lava upstream mapping 64/bucket). Audit component-bearing filled items, hardened blood, ordinary vs special JAWR blood and every drain/fill loop before enabling synthesis. No generic direct fluid machine assumed.
- Botania/Ars machine bridge native default 15 Mana per Source implies 0.75 EMC/Source as a candidate derived valuation; player Ars mana is a separate resource, native directional personal bridge is asymmetric. Audit before extending standard.

## Full audit targets

| System | Scope |
| --- | --- |
| Botania Revived | Every conjuration, alchemy, mana infusion, Orechid/Ignem/Marimorphosis, Runic Altar, generating flower, mana container and Fluxfield; lava and fuel cycles |
| Extra Botany Revived | Generators, mana items, charger/buffer, recipes, addon transformations and any FE bridge |
| ProjectE / Project Expansion | Final EMC item map, fuel conversions, collectors/power flowers, EMC converters, storage/charged stacks, upgrade bypass, boss gates, fluid items, scaling and arithmetic limits |
| EvilCraft | Blood Infuser including waxed fuels, Blood Chest, spirit furnace, extraction rates, bottles/buckets/hardened blood, repairs and reanimation |
| Neo Vitae | EV production/consumption, blood export/intake, ore/dust progression and converters |
| Draconic Evolution | Fuel generator modes, reactor net FE/OP and fuel, infusion/fusion costs, mob grinder and stabilised spawners |
| Replication | Matter production and replication costs, energy per unit, decomposer/output ratios, Eldritch Miner gating and interaction with EMC |
| Occultism | Dimensional mines by tier, crushers and ore multiplication, ore availability and throughput |
| Ars Nouveau | Source generation, machine bridge, personal mana separation, rituals/imbuement and item conversion |
| Psi / Psi Tweaks | FE-generation paths if present, CAD costs, spells and item transformation |
| Hexcasting | Media expenditure and production, potential FE generation, material and world transformations |
| Embers Rekindled | Ember generation/refinement, ore processing and fuel |
| Theurgy | Alchemy, duplication, transmutation and raw/dust relationships |
| Other enabled magic mods | Inventory any additional cross-resource or material-generation routes found in actual pinned catalogue; do not assume uninspected paths |

For each: exact pinned source/recipe IDs, inputs/outputs including catalyst and remainder, expected/stochastic yield, timing and throughput, native costs, gate/access, value under final EMC map, deliberate exception classification, and QA evidence. Export actual item registry and final EMC map from same frozen build; enforce #47 coverage. Audit cycles including Fortune overmax, Silk Touch, ore placement/remining, ore multiplication, raw block conversions, ore-producing farms, fuel loops (including renewable lava and free magic), charged/NBT items, shop rewards, boss drops and recipe mapping propagation. Use quick checks then separately owner-authorized game QA; no build authorized by this document.

## Ore hierarchy — discussion proposal only

Keep ore/raw/refined items in positive EMC coverage. Candidate standard: metal ingot and canonical/equivalent dust base B; raw metal 6B; metal ore 18B (1 ore = 3 raw = 18 ingots); raw block 54B; ingot block 9B; nugget B/9. For direct-drop gems 1 ore = 3 gems nominally; multi-drop ores must be normalized to vanilla/native drop counts and Fortune limits separately. Equal ore variants for a family unless deliberate exception. No ore base prices approved yet.

Prefer lowering refined base values instead of blindly increasing ore prices by 18x; test baseline choices against downstream recipes/economy. Example iron B=18, raw=108, ore=324, nugget=2, storage block=162, raw block=972; illustration only. Upstream iron 256 and gold 2,048; reprice Philosopher's Stone hardcoded exchanges and recipe-derived tags together if metal values change (8 iron <-> 1 gold; 4 gold <-> 1 diamond; 4 iron <-> 1 ender pearl); no direction should create unapproved EMC. Check smelting, alloys, dust unification, smelted ore/gem and storage blocks; base multiples of 9 preferred for exact nugget reversibility. NEI/JEI ore processors with multiple products require expected and maximum output accounting. Existing upstream ProjectE OreBlacklistMapper and RawMaterialsBlacklistMapper explicitly zero tagged items before/after mapping; disable/configure or replace their behavior and verify actual loaded overrides without collapsing the hierarchy through ordinary recipe propagation. Audit custom mappers and export actual post-map values. Do not assume vanilla defaults are final pack values.

Critically, ProjectE can synthesize expensive ore from refined material, place it, break it with Fortune and send outputs back; ordinary Fortune III single-drop gems average 2.2, but modded Fortune can exceed 3x, and multi-drop ores (copper/lapis/redstone/nether gold) require family-specific rules. Compare to all actual processing paths and place/break loops. Respect special gated ores (e.g. uranium): learn only after first legitimate acquisition, preserve agreed access policies. Ore generators/miners and Orechid receive increased valuations, so inspect output abundance and recipes before final pricing. Preserve pre-ProjectE magical processors as useful progression; no implementation until owner settles ore policy.

## Source references

- TMM [thermalily and converters](THERMALILY-PROJECTE-ENERGY-2026-11-15.md), [tiered ProjectE proposal](PROJECTE-TIERED-GATING-PROPOSAL.md), [blood integration](CANONICAL-BLOOD-CONFIG-FIRST-2026-11-15.md), [Ars bridge](mod-comprehension/interactions/botania-revived--ars-nouveau--mana-bridge.md).
- [Botania Revived 2.0.2 source](https://github.com/Velron-Devs/Botania-Revived/tree/5f803227d484598da1f1093a91d5d7cf5cf9600d), [EvilCraft source](https://github.com/CyclopsMC/EvilCraft/tree/aa8122019d5888381d40f30e6e4be5503abdd542), [Draconic Evolution source](https://github.com/Draconic-Inc/Draconic-Evolution/tree/6251e32bc93eb4bfcd910236ec1eaa6546516229).
- [ProjectE 1.21.1 upstream mapping source](https://github.com/sinkillerj/ProjectE/tree/mc1.21.1/src/main/java/moze_intel/projecte/emc/mappers) and [upstream conversion data](https://github.com/sinkillerj/ProjectE/tree/mc1.21.1/src/datagen/generated/data/projecte). Upstream branch is not a proof of pinned binary behavior.
