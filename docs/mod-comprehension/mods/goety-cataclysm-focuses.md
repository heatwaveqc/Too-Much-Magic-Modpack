# Goety Cataclysm — registered focus operation crosswalk

Companion to [the profile](goety-cataclysm.md), [recipe crosswalk](goety-cataclysm-recipes.md) and [altar/research gates](goety-ritual-gates.md). Inspected 2026-11-16; **researching, no gameplay validation**. Uses the exact hash-pinned addon 1.21.1-1.8.4 and Goety 3.2.2, whose full hashes are in [the operating evidence](goety-cataclysm.md#pinned-bytecode-operating-audit--2026-11-16).

## How to use this reference

Choose a focus using its named Black Book page under Cataclysm → Focuses and the corresponding JEI recipe in the recipe appendix. Obtain the parent wand, slot the focus using the parent focus UI, charge a valid Soul Energy source, aim into a disposable practice area and complete the cast. These are optional spell choices, not 28 mandatory quest tiers. A recipe/guide match establishes acquisition instructions; this table adds operation and variant behavior.

`GCItems.class` registers exactly the 28 focus→spell mappings below. `EMPSpell.class` exists in the JAR but has no focus registration in this inspected GCItems set; do not invent an obtainable EMP Focus. Source columns are relative to `com/Polarice3/goety_cataclysm/common/magic/spells/` in the exact addon. Configuration defaults come from `config/GCSpellConfig.class` initializer, not comments (some comments disagree with actual values).

**Units differ from rituals:** spell cast/cooldown values below are **game ticks**, not the altar's charged 20-tick advances. Costs are raw base Soul Energy, not measured/effective bills. `ISpell.SoulCalculation` changes cost for gear, school, enabled environmental rules and attributes, then casts to int. `castDuration` normally applies cast-speed reduction/attributes; the fixed-stage overrides shown do not use that generic reduction. Cooldown is normally scaled by cooldown-discount attribute. Matching staff means an `IWand` whose supported SpellTypes contains this spell's school (`Spell.rightStaff`), not simply an item named Staff.

## All 28 registered focus choices

| Focus item suffix (`goety_cataclysm:`) | School / source class | Raw cost | Base cast ticks / cooldown ticks | Accepted focus enchantments | Actual output or useful variant |
| --- | --- | ---: | --- | --- | --- |
| `abyssal_mine_focus` | abyss; `abyss/AbyssalMinesSpell.class` | 16 | 70 / 800 | range, radius | Ground-seeking Abyss Mine pattern; 17 placement attempts, or 35 with matching staff. Valid ground/collision and entity behavior can reduce actual output; Radius affects mine area. |
| `abyssal_beam_focus` | abyss; `abyss/AbyssalBeamSpell.class` | 32 | 90 fixed by override / 400 | potency, range | AbyssMark is created at cast start; its configured 80-tick delay creates beam portal(s) before the 90-tick completion. Mark is discarded when creator stops spellcasting; already emitted portal behavior is separate. Matching staff adds portal branches. See staged-payment report. |
| `abyssal_orb_focus` | abyss; `abyss/AbyssalOrbsSpell.class` | 16 | 90 / 400 | potency, radius | Nine directional orb projectiles normally; matching staff adds fifteen (24 total). Optional target and radius are passed into addon orb entities. |
| `water_spear_focus` | abyss; `abyss/WaterSpearSpell.class` | 12 | 0 / 50 | potency, velocity | One aimed water spear, or five in 15-degree fan with matching staff. Sets bounces to 10+Potency normally, 8+Potency for staff fan; delegated projectile handles impacts. |
| `sunken_swell_focus` | abyss; `abyss/DeeplingSpell.class` | 24 | 60 / 100 | potency, duration | One temporary Deepling-family servant, 25% code chance of Angler variant; matching staff requests 2–5 servants. Crouch-cast recalls owned matching servants instead. |
| `sunken_current_focus` | abyss; `abyss/DeeplingBruteSpell.class` | 32 | 20 / 600 | potency, duration | One temporary Deepling Brute; matching staff requests 2–5. Crouch-cast recalls owned matching servants instead. |
| `sunken_tribune_focus` | abyss; `abyss/DeeplingCasterSpell.class` | 24 | 100 / 1200 | potency, duration | One temporary Priest or Warlock chosen by random boolean; matching staff requests two. Crouch-cast recalls both owned caster variants. |
| `kakourgos_focus` | abyss; `abyss/UrchinkinSpell.class` | 8 | 60 / 100 | potency, duration | One temporary Urchinkin; matching staff requests 2–5. Crouch-cast recalls matching owned servants. |
| `polemistis_focus` | abyss; `abyss/OctoHostSpell.class` | 32 | 60 / 100 | potency, duration | One temporary Drowned Host; matching staff requests 2–5. Crouch-cast recalls matching owned servants. |
| `kyria_focus` | abyss; `abyss/CindariaSpell.class` | 64 | 20 / 900 | potency, duration | One temporary Cindaria; matching staff requests 2–5. Crouch-cast recalls matching owned servants. |
| `amethyst_cluster_focus` | geomancy; `geomancy/AmethystClusterSpell.class` | 32 | 50 / 240 | potency, velocity | Radial amethyst projectiles: 16 normally, 32 with matching staff. Potency and Velocity feed each projectile; this is combat output, not inventory amethyst. |
| `desert_crush_focus` | geomancy; `geomancy/DesertCrushSpell.class` | 64 | 70 / 160 | potency, range | Ground-seeking spike rings around caster aimed toward location/target; matching staff adds an outer ring. Requires suitable placement surfaces; Range affects aiming. |
| `earth_shake_focus` | geomancy; `geomancy/EarthShakeSpell.class` | 48 | 16 / 160 | potency, velocity | Radial EarthQuake entities: 16 normally, 22–29 with matching staff plus velocity bonus. Damage/effects are delegated to Cataclysm entities. |
| `desert_raid_focus` | necromancy; `necromancy/KoboletonSpell.class` | 16 | 60 / 100 | potency, duration | One temporary Koboleton; matching staff requests 2–5; Nameless Staff requests seven. Crouch-cast recalls matching owned servants. |
| `cursed_grave_focus` | necromancy; `necromancy/DraugrSpell.class` | 24 | 60 / 100 | potency, duration | One temporary Draugr; matching staff requests 2–5, Frost staff two, Nameless Staff seven. Crouch-cast recalls matching owned servants. |
| `cursed_cairn_focus` | necromancy; `necromancy/EliteDraugrSpell.class` | 32 | 60 / 100 | potency, duration | One temporary Elite Draugr; matching staff requests 2–5, Frost staff two. Crouch-cast recalls matching owned servants. |
| `cursed_tomb_focus` | necromancy; `necromancy/RoyalDraugrSpell.class` | 48 | 100 / 1200 | potency, duration | One temporary Royal Draugr; matching staff requests 2–5, Frost staff two; Nameless Staff requests a seven-wide formation. Crouch-cast recalls matching owned servants. |
| `battlefield_focus` | necromancy; `necromancy/BattlefieldSpell.class` | 128 | 60 fixed by override / 250 | potency, radius | Halberd attack geometry emitted during useSpell: standing advancing waves or crouching windmill pattern. Matching staff extends reach. Fixed cast time; see staged-payment report. |
| `ashen_breath_focus` | nether; `nether/AshenBreathSpell.class` | 16 | charging: 72000 use limit; 0 charge-up / 25 shots / 200 | potency, range, duration | Repeated directional breath hit checks (base range seven) with damage and Blindness; charging/shot pipeline differs from one-shot spells. Base charge-up zero; shots limit 25. Inspect partial-payment boundary below. |
| `extinct_flame_focus` | nether; `nether/ExtinctFlameSpell.class` | 64 | 70 / 300 | potency, radius | Five-shot fireball volley with an Abyss fireball among ordinary variants. Matching staff changes the pattern and Abyss-versus-fireball distribution. Explosion and hit rules are delegated. |
| `flare_bomb_focus` | nether; `nether/FlareBombSpell.class` | 32 | 60 / 120 | potency, velocity | One thrown FlareBomb, or five with matching staff; projectile damage configured separately. Do not assume an item output or safe block interaction. |
| `death_laser_focus` | storm; `storm/DeathLaserSpell.class` | 128 | 40 / 200 | potency, duration | Immediate DeathLaserBeam entity after completion, lifespan constructor 28 + 20×Duration stat; damage and HP parameter are passed separately. It is distinct from Abyssal Beam's start-stage mark. |
| `lightning_spear_focus` | storm; `storm/LightningSpearSpell.class` | 64 | 27 / 80 | potency, velocity, radius | One aimed lightning spear normally, two with matching staff. Staff variant supplies HP-damage parameter; area damage and radius passed to projectile. Collision/targeting delegated. |
| `thunder_rage_focus` | storm; `storm/ThunderRageSpell.class` | 64 | 150 fixed by override / 350 | potency, velocity, radius | Staged knockback and lightning visuals then Water/Lightning spear emission at tick115 of fixed150. Matching staff changes lightning HP behavior. See staged-payment report. |
| `storm_serpent_focus` | storm; `storm/StormSerpentSpell.class` | 128 | 75 fixed by override / 350 | potency | Two attack-serpent entities at tick28 of fixed75; matching staff adds two at40. These are attack entities, not the permanent servant roster. See staged-payment report. |
| `void_rune_focus` | void; `void_spells/VoidRuneSpell.class` | 12 | 0 / 160 | potency, range | Nonmatching wand: line of ten ground runes; crouch gives two nearby rings (5+8). Matching staff uses target-centered larger pattern. Ground eligibility controls actual placements. |
| `void_vortex_focus` | void; `void_spells/VoidVortexSpell.class` | 64 | 50 / 280 | range, radius | Attempts a target-location vortex on a valid support surface; radius from config/stat, lifetime constructor300 ticks. Ground failure can leave no entity after parent payment; pull/damage rules delegated. |
| `sandstorm_focus` | wind; `wind/SandstormSpell.class` | 32 | 55 / 500 | potency, duration | One orbiting Sandstorm attack entity, four with matching staff. Lifetime300×(Duration+1) ticks and Potency passed as extra damage. It is not a weather-change resource. |

## Configured effect parameters — not final damage guarantees

The following are initializer values, not combat measurements or a claim that every hit uses the parameter identically. Most spawn paths apply `WandUtil.damageMultiply()` and Potency before delegating to an entity's hit code. HP-damage parameters in particular have inconsistent scales/comments; do not read 5.0 as “5%” without tracing the actual hit formula.

| Config field family | Exact raw initializer values inspected |
| --- | --- |
| `AbyssalMines` | `Radius=1.0` |
| `AbyssalBeam` | `Damage=10.0`, `HPDamage=0.1` |
| `AbyssalOrbs` | `Damage=4.0`, `Radius=1.0` |
| `AmethystCluster` | `Damage=12.0` |
| `AshenBreath` | `Damage=4.0` |
| `ExtinctFlame` | `Radius=1.0`, `Damage=6.0`, `SoulDamage=8.0`, `AbyssDamage=10.0` |
| `DeathLaser` | `Damage=5.0`, `HPDamage=5.0` |
| `DesertCrush` | `Damage=18.0` |
| `Battlefield` | `Damage=10.0` |
| `EarthShake` | `Damage=8.0` |
| `FlareBomb` | `Damage=7.0` |
| `LightningSpear` | `Damage=14.0`, `AreaDamage=4.0`, `HPDamage=0.04` |
| `Sandstorm` | `Damage=7.0` |
| `StormSerpent` | `Damage=16.0` |
| `VoidRune` | `Damage=7.0` |
| `VoidVortex` | `Radius=2.0` |
| `WaterSpear` | `Damage=8.0` |

## Temporary summons, recall, caps and upkeep

The ten servant-producing focus classes use the parent `SummonSpell` pattern; this is separate from the 13 altar summon recipes and 11 conversions. All ten set the caster as owner and write `limitedLife = MobUtil.getSummonLifespan(world) × Duration stat`. The parent helper returns `20 × (30 + random.nextInt(90))`: **30–119 seconds times Duration**, code-derived, per created summon. Ordinary `SummonSpell.defaultStats` has Duration1; enchanted-focus branches add Duration enchantment and an additional1 in these classes, so do not infer lifespan solely from enchantment level or promise permanent companions because `setPersistenceRequired` is also called. Servant entity upkeep/robe logic remains a separate layer.

Most matching-staff multi-summons request 2–5 per cast. Requests are not guaranteed successful spawns. The parent's focus-summon eligibility counts alive matching owned entities returned by the **current ServerLevel** and compares with the class limit before casting; the individual result loops do not recheck that limit for every member. A near-cap batch can therefore overshoot in the inspected path; verify before calling the cap a strict maximum. This differs from altar `RitualRequirements.canSummon`, which scans all server levels' currently returned entities, and from the Fabricator's own current-level Watcher scan.

| Spell class family | Default focus cap / relevant shared predicate |
| --- | --- |
| `CindariaSpell` | `CindariaLimit=8` |
| `DeeplingSpell` | `DeeplingLimit=32` |
| `DeeplingBruteSpell` | `DeeplingBruteLimit=8` |
| `DeeplingCasterSpell` | `DeeplingCasterLimit=4` |
| `DraugrSpell` | `DraugrLimit=32` |
| `EliteDraugrSpell` | `EliteDraugrLimit=16` |
| `RoyalDraugrSpell` | `RoyalDraugrLimit=16` |
| `KoboletonSpell` | `KoboletonLimit=32` |
| `OctoHostSpell` | `OctoHostLimit=16` |
| `UrchinkinSpell` | `UrchinkinLimit=32` |

`OctoHostLimit` is actually **16**, although its config comment claims32. The code value is the default used here. Check the effective generated file before gameplay conclusions.

Crouch-casting servant focuses follows the matching servant recall branch: `teleportServants` moves only same-owner servants and refuses ones marked grounded. It scans the current level's entities, not all saved dimensions. Recall does not create a new companion but still goes through the normal casting/payment path; do not promise free global recall. The Summon Down mechanic can add Weakness/Sapped and badly damage summon equipment when repeated summoning occurs under that effect (`ISummonSpell.SummonSap`), and Potency buffing is suppressed under Summon Down. Cooldown, summon count and summon weakening are separate limitations.

## Payment and failure boundaries

- Standard completed one-shot cast: parent checks available souls, decreases SoulUse, then invokes final result and cooldown. A final placement search can fail after payment (e.g. Void Vortex without valid ground); no generic refund exists in `DarkWand.MagicResults` for a void-returning spell result. Do not promise an entity for every paid cast.
- Battlefield, Storm Serpent, Thunder Rage and Abyssal Beam have earlier callbacks. See [staged-cast payment report](../interactions/goety--goety-cataclysm--staged-cast-payment.md), including release cooldown thresholds and AbyssMark's cancellation guard. Treat all exploit conclusions as untested until the exact world/event path is observed.
- Ashen Breath is `EverChargeSpell`: parent `MagicResults` increments a per-wand Seconds counter per execution, charging on the twentieth execution, while the spell's hit effect runs each execution with sufficient available souls. Base charge-up0, shots25, cooldown200 differ from an ordinary single finish. Partial shots, counter carryover/reset and early release need direct testing; “16 per breath tick” and “16 once per arbitrary hold” are both unsupported summaries.
- School-specific damage, friendly-fire filtering, block destruction, invulnerability frames, HP scaling and effect caps may live in addon or Cataclysm projectile classes. This operation crosswalk identifies the producer and configured inputs; it does not mark those delegated channels fully audited.

## Three practical optional setups

1. **Water Spear practice — short chain after parent focus access and Cataclysm Lacrima.** JEI's shaped `focus/water_spear_focus` upgrades Bouncy Bubble Focus using four Lacrima and four Pointed Dripstone. Load it in the wand; use a safe target/backstop. Base cast0, cost12, cooldown50 ticks; verify one projectile and bounces. Matching Abyss-supporting staff creates a five-spear fan. It is not universally early because Lacrima/base focus acquisition must be available.
2. **Kakourgos/Urchinkin support — after Deep ritual setup.** Craft the focus from Animation Core activation and the eight Pufferfish/Kelp offerings in the recipe crosswalk; use a waterlogged Deep altar. Cast to create temporary support; crouch-cast to recall matching owned ungrounded servants. Raw spell cost8 is the lowest registered servant-focus base here, but altar creation, availability of Pufferfish and Summon Down matter. Do not promise permanent or farmable loot-producing pets.
3. **Mechanized guard post — after Forge/Witherite access.** Follow the separate Mechanical Fabricator walkthrough for a recurring item-fuel route to Watchers. This is a complementary servant route, not a focus cast; minimum five iron plus four redstone per fuel block, clear spawn space and cap monitoring. It avoids repeatedly using a temporary-servant focus but has its own queue-loss failure and maintenance tests.

## Quest and validation coverage

Each optional focus lesson should teach its named Black Book entry, exact JEI route, why its school/aiming/hold-or-crouch mode matters, observed result and maintenance. Use XP plus a modest fixed vanilla supply after checking global reward uniqueness; do not reward the next gated focus, boss ingredient or large Soul Energy reserve. A focus item task is an acquisition proxy, not casting, servant ownership or damage detection. Manual operation confirmation is honest until a validated native detector is found.

Required disposable-world tests: one completed positive cast per effect family; matching versus nonmatching staff; target absent/behind wall/ally; valid versus invalid ground; exact available souls versus one-short; full cast versus stage interruption; summon batch at cap−1 and cap; recall owned/other-owner/grounded/unloaded servants; temporary lifespan with/without Duration; repeated Summon Down; Ashen Breath release before/after20 executions; HP damage against controlled max-health targets. Record config/build/world, effective costs and actual entity/health changes separately from advancement credit.

## Remaining work

All 28 registered producers/default triples/accepted enchantment lists are mapped. Still researching: delegated hit/effect/formula behavior (especially HP damage), servant feeding/robe/death recovery, every acquisition gate, actual core wand UI/keybindings in TMM, effective configuration and full client acceptance. This is an operation inventory, not source-complete gameplay coverage.
