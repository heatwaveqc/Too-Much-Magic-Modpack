# Thermalily No-Waste & ProjectE Energy Converters - 2026-11-15

## Approved scope

Minecraft 1.21.1, NeoForge 21.1.265, Java 21. The owner wants to avoid needless Thermalily lava use and add a ProjectE-based late-game FE/OP conversion path. **No new Neo Vitae FE ritual or blood flower** is needed to satisfy this request.

## Source-audited Thermalily behavior

In the Botania Revived 1.21.1 source, `FluidGeneratorBlockEntity.tickFlower` contains a native `getMana() < getMaxMana()` check, so a **fully filled internal mana buffer already prevents a new lava pickup**.

The actual lossy case is a Thermalily that has **room in its buffer but is still cooling down**. `FluidGeneratorBlockEntity` destroys an adjacent matching lava source *before* testing `cooldown == 0`. For Thermalily, `getCooldownTime(false)` returns zero, so that dropped lava can also disrupt cooldown progression. The documented original Botania behavior intentionally destroys lava during cooldown.

### Implemented narrow fix (experimental Java mixin)

The existing optional first-party `compat/transformations` module adds `BotaniaThermalilyNoWasteMixin`, targeting the **fluid collector's mana-capacity expression** only for runtime class `ThermalilyBlockEntity`. If `burnTime == 0 && cooldown > 0`, the one collector capacity check behaves as full. It does not change the flower's stored mana, FE, normal burn generation, cooldown decrement, other flowers' intake, recipes, or Hydroangeas.

The production change uses MixinExtras `@ModifyExpressionValue` and must be checked against the actual pinned Botania Revived **2.0.2** JAR, not only current upstream source. It is intentionally a separate compatibility change rather than a vendor mod JAR fork. A pure `ThermalilyLavaPolicy` unit test verifies full, cooling, active and ready states.

**IMPORTANT:** The code will not run until the experimental `tmm_transformations` JAR is actually built **and shipped on both sides**, reconciled with other transformation work, and validated in-world. Merely installing ProjectE converters does not turn this behavior on.

### Gameplay acceptance in a disposable world

1. Supply lava with a ready, empty Thermalily: exactly one lava source is consumed and mana generation begins.
2. Place eight lava source blocks while the Thermalily is cooling down, with a free mana buffer: none should disappear; the countdown proceeds.
3. Fill its internal mana storage (disconnect the spreader if needed) while ready: no lava should disappear while full.
4. Check active production, mana transfer, cooldown randomly assigned, save/reload, and redstone if relevant.
5. Check a Hydroangeas adjacent to water: behavior unchanged.
6. Run both a dedicated server and a graphical client; verify the mixin applies cleanly with the pinned Botania Revived 2.0.2 version.

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

Static manifest and packwiz checks, Java compile/JUnit and the new `.github/workflows/thermalily-energy-server-smoke.yml` combined real modded dedicated-server test constitute separate gates. A headless ready message does not prove mixin-side lava behavior, 30 converter types, FE transfer rates, recipes, or client graphics. No proprietary jars, generated worlds, logs or third-party files should be committed.

**Do not merge into the main integration branch before review and the appropriate CI/runtime evidence.**
