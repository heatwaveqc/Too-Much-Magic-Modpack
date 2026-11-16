# Thermalily No-Waste & ProjectE Energy Converters - 2026-11-15

## Approved scope

Minecraft 1.21.1, NeoForge 21.1.265, Java 21. The owner approved removing Thermalily's cooldown outright, preventing cooldown-related lava loss, and adding a ProjectE-based late-game FE/OP conversion path. **No new Neo Vitae FE ritual or blood flower** is needed to satisfy this request.

## Source-audited Thermalily behavior - owner revision: NO COOLDOWN

Botania Revived 2.0.2's `ThermalilyBlockEntity.getCooldownTime(boolean)` normally returns 400 ticks multiplied by a randomly selected cooldown strength. The owner has now approved **removing that entire cooldown** rather than merely preventing wasted lava.

The pinned port's `FluidGeneratorBlockEntity` superclass already gates source pickup on **`burnTime == 0` and `getMana() < getMaxMana()`**. Thus no source is consumed while the flower is actively burning or has a full mana buffer. When its existing 600-tick burn ends, it can consume the next adjacent lava source immediately if its mana buffer has room.

### Minimal implementation - single subclass mixin

`BotaniaThermalilyNoCooldownMixin` now targets only the actual `ThermalilyBlockEntity` class. A cancellable injection at the beginning of `getCooldownTime(Z)I` returns **0**. We removed the earlier shared-fluid-generator injection and its associated policy helper/unit tests entirely; they are no longer necessary.

This leaves untouched:
- The native burn duration (600 ticks) and total mana-buffer capacity; the output rate is deliberately increased to **75 mana/tick** in the 50-Endoflame balance patch.
- Existing lava source consumption **when the flower successfully starts a new burn**.
- The superclass' check that prevents consumption while internal mana is full.
- Hydroangeas, all other flowers, normal mana transfer and all ProjectE EMC/FE converter settings.

The former randomized cooldown and its comparator output are effectively bypassed. This is an intentional **balance increase**, allowing repeat burning when supplied with lava; output can still stall when its mana buffer is full. Under default values a fresh 600-tick burn lasts **30 seconds** of loaded-world time (at 20 ticks/second); the stored mana buffer is smaller than the theoretical 45,000 mana generated across the complete burn, so a fully blocked output can waste potential mana during a burn. It will not *start another* burn while still full.

This is a pinned-version-specific mixin requiring first-party `tmm_transformations` to be built and shipped. It does not change Botania Revived's own mod JAR.

### Disposable-world acceptance

1. Adjacent lava while a ready Thermalily has an empty buffer: exactly one source is consumed and a 600-tick burn begins.
2. Supply a continuous stream of adjacent lava sources: there must be **no cooldown** between two completed burns if mana storage has room.
3. Fill its internal mana storage and stop external mana movement: it must not consume another lava source while full.
4. Save/reload during a burn and immediately after it ends; check item displays, comparator, mana output, and state.
5. Confirm Hydroangeas behaves identically to its native implementation.
6. Verify dedicated-server boot on pinned Botania Revived 2.0.2 plus graphical client interaction before promoting.

## Locked generation goal: one Thermalily ≈ **50 Endoflames**

The owner clarified that the earlier 10-Endoflame goal was intended as a buff, rather than a nerf. The new goal is **50 Endoflames**. Confirmed in Botania Revived's 1.21.1 source for the pinned 2.0.2 port:

| Flower | Mana generated while burning | Average active output | Endoflame equivalent |
| --- | --- | ---: | ---: |
| Endoflame (unchanged) | 3 mana every 2 ticks | **1.5 mana/tick** | 1 |
| Thermalily (native) | 45 mana every tick | **45 mana/tick** | 30 |
| **Thermalily (new target)** | **75 mana every tick** | **75 mana/tick** | **50** |

This is a **66.7% buff over the native Thermalily**, together with the separately approved removal of its cooldown. The lava cost and underlying burn duration stay the same.

**Implementation:** A small `BotaniaThermalilyFiftyEndoflamesMixin` uses MixinExtras `@ModifyExpressionValue` to replace the `manaPerTick` field's value **only when accessed by the Thermalily instance during `FluidGeneratorBlockEntity.tickFlower()`**. The native 1-tick generation interval is restored, so the output is 75 mana on every active production tick. Other flowers retain their original `manaPerTick`, generation intervals, recipes and consumption logic. No direct modification of Botania Revived's distributed JAR.

With a continuously burning Thermalily and sufficient mana transfer, the ideal output over each native 600-tick burn is **45,000 mana per 1,000 mB lava source** (75 mana/tick × 600 ticks), equivalent to **50 Endoflames**, each generating 1.5 mana/tick. Lava sources can be consumed consecutively without a cooldown when the internal buffer has room.

**Capacity caveat:** The Thermalily's local mana buffer holds only 750 mana. If its spreader / downstream receiver cannot keep up, generated mana is capped and actual output will be less than 75 mana/tick. The new rate also merits checking spreader throughput before calling the 50:1 ratio the *delivered* production rate.

### Acceptance tests

1. On a freely draining setup, compare **one Thermalily** against **50 continuously fueled Endoflames** over matching, sustained intervals; both should generate approximately 75 mana per game tick.
2. Verify **45,000 mana** from one 600-tick uninterrupted Thermalily burn when all generated mana can be drained.
3. Verify immediate consecutive lava burns with zero cooldown, and no extra lava consumption while the local mana buffer is full.
4. Confirm native Endoflame and Hydroangeas outputs are unchanged; ProjectE converter rates and recipes are unchanged.
5. Repeat after save/reload and on a client with the real packaged `tmm_transformations` compatibility JAR.

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
