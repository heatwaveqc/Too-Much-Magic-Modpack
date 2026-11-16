# Thermalily No-Waste & ProjectE Energy Converters - 2026-11-15

## Approved scope

Minecraft 1.21.1, NeoForge 21.1.265, Java 21. The owner approved removing Thermalily's cooldown outright, preventing cooldown-related lava loss, and adding a ProjectE-based late-game FE/OP conversion path. **No new Neo Vitae FE ritual or blood flower** is needed to satisfy this request.

## Source-audited Thermalily behavior - owner revision: NO COOLDOWN

Botania Revived 2.0.2's `ThermalilyBlockEntity.getCooldownTime(boolean)` normally returns 400 ticks multiplied by a randomly selected cooldown strength. The owner has now approved **removing that entire cooldown** rather than merely preventing wasted lava.

The pinned port's `FluidGeneratorBlockEntity` superclass already gates source pickup on **`burnTime == 0` and `getMana() < getMaxMana()`**. Thus no source is consumed while the flower is actively burning or has a full mana buffer. When its existing 600-tick burn ends, it can consume the next adjacent lava source immediately if its mana buffer has room.

### Minimal implementation - single subclass mixin

`BotaniaThermalilyNoCooldownMixin` now targets only the actual `ThermalilyBlockEntity` class. A cancellable injection at the beginning of `getCooldownTime(Z)I` returns **0**. We removed the earlier shared-fluid-generator injection and its associated policy helper/unit tests entirely; they are no longer necessary.

This leaves untouched:
- The native burn duration (600 ticks), per-tick mana generation (45 mana), and total mana-buffer capacity.
- Existing lava source consumption **when the flower successfully starts a new burn**.
- The superclass' check that prevents consumption while internal mana is full.
- Hydroangeas, all other flowers, normal mana transfer and all ProjectE EMC/FE converter settings.

The former randomized cooldown and its comparator output are effectively bypassed. This is an intentional **balance increase**, allowing repeat burning when supplied with lava; output can still stall when its mana buffer is full. Under default values a fresh 600-tick burn lasts **30 seconds** of loaded-world time (at 20 ticks/second); the stored mana buffer is smaller than the theoretical 27,000 mana generated across the complete burn, so a fully blocked output can waste potential mana during a burn. It will not *start another* burn while still full.

This is a pinned-version-specific mixin requiring first-party `tmm_transformations` to be built and shipped. It does not change Botania Revived's own mod JAR.

### Disposable-world acceptance

1. Adjacent lava while a ready Thermalily has an empty buffer: exactly one source is consumed and a 600-tick burn begins.
2. Supply a continuous stream of adjacent lava sources: there must be **no cooldown** between two completed burns if mana storage has room.
3. Fill its internal mana storage and stop external mana movement: it must not consume another lava source while full.
4. Save/reload during a burn and immediately after it ends; check item displays, comparator, mana output, and state.
5. Confirm Hydroangeas behaves identically to its native implementation.
6. Verify dedicated-server boot on pinned Botania Revived 2.0.2 plus graphical client interaction before promoting.

## Locked generation goal: one Thermalily ≈ ten Endoflames

Exact Botania Revived 2.0.2 source figures, **while continuously burning and successfully exporting mana**:

| Flower | Native mana event | Mean active output | Equivalent Endoflames |
| --- | --- | ---: | ---: |
| Endoflame | +3 mana every second game tick | **1.5 mana/tick** | 1 |
| Thermalily, before balance patch | +45 mana each game tick | **45 mana/tick** | 30 |
| Thermalily, approved target | +45 mana every **three** game ticks | **15 mana/tick** | **10** |

**Achievable with a tiny subclass-conditional mixin.** `FluidGeneratorBlockEntity.getGenerationDelay()` normally returns 1; the new `BotaniaThermalilyTenEndoflamesMixin` sets that getter to **3 for Thermalily only**, leaving Endoflame, Hydroangeas, other flowers, and the Thermallily's underlying mana-per-event field untouched. No new block or recipe required.

The separate owner-approved `BotaniaThermalilyNoCooldownMixin` continues to set its cooldown to zero. With a continuous lava supply and a Mana Spreader able to drain its local 750-mana buffer, one Thermalily generates 15 average mana/tick throughout an active 600-tick burn: **9,000 mana per 1,000 mB lava source**, a source every 30 seconds at 20 TPS. If the buffer or collector is blocked, *actual delivered mana will be less*: this target is a production ceiling for a constantly burning, properly connected flower, not a guarantee regardless of transport capacity.

The 3-tick pulse is not mathematically uniform each individual tick: production arrives in 45-mana pulses spaced three ticks apart. Its **time-averaged** throughput is exactly 15 mana/tick. We prefer this to an invasive patch of constructor arguments.

### Acceptance tests

1. Verify **ten** fueled Endoflames in a properly drained mana network deliver the same output as **one** fueled Thermalily when both run continuously over a long matching interval, after excluding startup transients.
2. Verify exactly 9,000 mana can be generated during a complete, uninterrupted 600-tick Thermalily burn when its buffer drains freely.
3. Test adjacent lava source pickup at the end of consecutive burns with the zero-cooldown patch enabled; ensure no additional lava is destroyed while the buffer is full.
4. Confirm Hydroangeas output and ordinary Endoflame output remain unmodified; no changes to ProjectE converter efficiency or recipes.
5. Repeat after a server restart and in a multiplayer client; the test must run with the real packaged `tmm_transformations` compatibility JAR.

## Added converter

- **Project:** [ProjectE Energy Converters](https://www.curseforge.com/minecraft/mc-mods/projecte-energy-converters)
- **Release:** 0.1.4, `projecte-energy-0.1.4.jar`, CurseForge **project 1727449 / file 9072881**, *Release*, MIT license, Minecraft 1.21.1 / NeoForge 21.1.250+ / Java 21, client and server.
- **SHA-1:** `22e6a476508b9d9d9fa35cbfdecf5677ec5c623c`, sourced from independent hash-pinned external pack metadata; **must still be confirmed against the downloaded upstream file during smoke test**.
- **Required mods already present:** ProjectE 1.1.0; Project Expansion 1.21.1-1.0.6.
- **Optional native OP integration:** Brandon's Core 3.2.1.309, already pinned. Draconic Evolution is separately installed.
- **Packwiz:** `mods/projecte-energy-converters.pw.toml` indexed, new manifest entry `projecte-energy-converters`, manifest `enabled_in_pack=true`.
- **Default forward conversion:** **1,000 EMC consumed for 1 FE**.
- **Default reverse conversion:** **1,000 FE consumed for 1 EMC**.
- **Config:** `config/projecte-energy-server.toml` or per-world `serverconfig` (may take precedence); leave defaults until balance audit. A round-trip loses almost all value by design.
- **Inputs:** charged Klein or Project Expansion stars/real supported EMC storage, NOT direct extraction from Project Expansion 1.0.6 EMC Links (mod authors blocked this because source debit did not work).
- **Progression:** 15 tiers in each direction. Source mod advertises recipe checks at higher tiers, but **do not assume our pack's post-Chaos Guardian gate is enforced**. The first converter requires a basic EMC Link; all recipes, obtained item EMC costs, bypass routes and learned transmutations must be checked before a survival release.

### Verification status

Static manifest and packwiz checks, Java compilation and the new `.github/workflows/thermalily-energy-server-smoke.yml` combined real modded dedicated-server test constitute separate gates. A headless ready message does not prove mixin-side lava behavior, 30 converter types, FE transfer rates, recipes, or client graphics. No proprietary jars, generated worlds, logs or third-party files should be committed.

**Do not merge into the main integration branch before review and the appropriate CI/runtime evidence.**
