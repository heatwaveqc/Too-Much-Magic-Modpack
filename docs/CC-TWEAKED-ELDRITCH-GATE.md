# CC: Tweaked + Eldritch Logic Core + Iesnium Spirit Fire pilot

**Target:** Minecraft 1.21.1, NeoForge 21.1.265, Occultism 1.227.0, Draconic Evolution 3.1.4.633, CC: Tweaked 1.121.0 **beta** from Modrinth version `KNdTEfUA`. This implementation is source-checked, **not yet runtime-verified**. No additional ComputerCraft addons or AE2 were approved.

## Scope and progression

- **CC: Tweaked** adds Lua computers, modems, displays and (unless separately disabled) turtles. Existing Draconic Evolution 1.21 sources register a native ComputerCraft peripheral for reactor stabilizers/injector, Flux Gates and Energy Pylons when the `computercraft` mod is loaded.
- **New item:** `tmm:eldritch_logic_core` (KubeJS startup item, currently reusing the vanilla Ender Eye icon pending a custom asset). This is the computer access component.
- **Sole intended ordinary survival source:** `kubejs/data/tmm/recipe/miner/eldritch_logic_core.json` adds a weighted output to Occultism's `occultism:miners/eldritch` miner tag. In the matched Occultism release this tag contains only `occultism:miner_ancient_eldritch`, not Foliot/Djinni/Afrit/Marid.
- **Output weight 250** is a relative selection weight, **not** a percentage. Assess live production rate versus the many other Eldritch outputs and tune for a practical one-time unlock rather than a new grind. Do not assume an exact chance from the number 250.
- Four base craft paths are explicitly overridden: `computercraft:computer_normal`, `computer_advanced`, `pocket_computer_normal`, and `pocket_computer_advanced`. All require one `tmm:eldritch_logic_core`. Pocket recipes retain their original golden apple; desktop recipes replace the redstone center with the core.
- CC turtle recipes depend on one of the already gated computers and inherit their access gate. *Command Computers* use an uncraftable-in-survival Command Block and are not an ordinary bypass. If turtles are judged too powerful later, address them as a separate balance decision, not by silently weakening the computer access gate.
- The original Eldritch Miner recipe and its eight Marid ingredients remain unchanged by this pilot. The broader *two-to-four Marid plus Cataclysm materials* balance target is independent and still pending runtime implementation.
- ProjectE learns items once acquired; one normally obtained Eldritch Logic Core may later be duplicated using EMC. This is acceptable for a **first-acquisition** gate; do not promise a permanent anti-EMC restriction.

## Easier Iesnium through BetterEnd Thallasium Ore (B16 planned)

**Approved next-build replacement:** Throw **1 `betterend:thallasium_ore`** into **Occultism Spirit Fire** to produce **1 `occultism:iesnium_ore`**. Datapack path: `kubejs/data/tmm/recipe/spirit_fire/iesnium_from_thallasium.json`. This supersedes the prior End Draconium Ore pilot recipe, which is deleted on the B16 feature branch; it is not an additional parallel recipe.

The original pilot remains the current integrated-build behavior **until** the B16 branch is promoted with BetterEnd: New Dawn and its three New Dawn library dependencies pinned. This is a native `occultism:spirit_fire` recipe (not a KubeJS item-entity scan). The native Spirit Fire behavior preserves stack count in the matching Occultism 1.227.0 source; verify the live stack result before certifying it. This route rewards exploring the newly expanded End rather than requiring Draconic Evolution ore.

**Block input and harvesting:** The input is Thallasium **ore block**, not Thallasium ingot or raw material. Verify the live BetterEnd loot table and whether Silk Touch is needed to collect intact ore, then test ordinary mining, one-block throw, and stack throw. Do not silently loosen this to End Stone or an unrestricted ingot conversion.

**Release dependency:** The B16 change must be deployed as one unit with BetterEnd: New Dawn 21.0.39 plus BCLib, WunderLib and WorldWeaver New Dawn; otherwise Spirit Fire cannot match the input. No separate early-game fallback recipe is approved.

**End Silver / End Lead:** No verified registered End ore of either name was found in the pre-B16 pack. Do not create phantom IDs.

See [B16 BetterEnd integration contract](B16-BETTEREND-THALLASIUM-IESNIUM.md).

## Source-backed contracts

- [CC: Tweaked 1.121.0 NeoForge file, Modrinth version KNdTEfUA](https://modrinth.com/mod/cc-tweaked/version/KNdTEfUA); 1.21.1 beta `cc-tweaked-1.21.1-forge-1.121.0.jar`, published SHA-1 `946c83390aadfe0d8077198fcf22804cee206637`. Actual pack download/hash verification pending.
- [CC 1.121.0 computer crafting source](https://github.com/cc-tweaked/CC-Tweaked/tree/v1.21.1-1.121.0/projects/common/src/generated/resources/data/computercraft/recipe). All four corresponding direct recipe IDs replaced in the pilot.
- [Draconic Evolution native CC support](https://github.com/Draconic-Inc/Draconic-Evolution/tree/1.21/src/main/java/com/brandon3055/draconicevolution/integration/computers). Reactor exposes telemetry (temperature, field charge/drain, saturation, fuel and generation), activation, safe-shutdown request and native fail-safe toggle. Flux Gates support flow overrides; Pylons expose energy/flow.
- [Occultism matching Spirit Fire recipe codec](https://github.com/klikli-dev/occultism/blob/990303e/src/main/java/com/klikli_dev/occultism/crafting/recipe/SpiritFireRecipe.java), [Spirit Fire collision handler](https://github.com/klikli-dev/occultism/blob/990303e/src/main/java/com/klikli_dev/occultism/common/block/SpiritFireBlock.java), and [Eldritch miner item tag](https://github.com/klikli-dev/occultism/blob/990303e/src/generated/resources/data/occultism/tags/item/miners/eldritch.json).
- [BetterEnd: New Dawn 21.0.39 NeoForge 1.21.1](https://www.curseforge.com/minecraft/mc-mods/betterend-neoforge/files/9306604): next-build target; verify live Thallasium Ore loot table and pack registry.

## Mandatory disposable-world acceptance

1. Verify the CC beta's exact JAR SHA and presence on **both client and dedicated server**, with no extra unapproved dependencies.
2. Restart after KubeJS startup registration; inspect `tmm:eldritch_logic_core` in JEI and confirm its texture and ID.
3. Confirm the live Occultism recipe manager registers `tmm:miner/eldritch_logic_core`; run an **Ancient Eldritch Miner** in a Dimensional Mineshaft long enough to witness at least one core. Run Marid/earlier miners to confirm none can yield it. Check the actual frequency and server logs.
4. Check all four base CC recipes in JEI require the core. Verify normal/advanced turtles and pocket-computer upgrade recipes cannot bypass it. Check loot, Quest Shop, duplication and other source paths before calling the gate complete.
5. **After B16 is pinned and installed:** obtain intact `betterend:thallasium_ore` (verify whether Silk Touch is required), then throw one block and a stack into Spirit Fire; confirm matching Iesnium Ore counts with no duplication. Verify End Draconium Ore, unrelated inputs and ordinary End Stone no longer transmute.
6. Construct a small safe Draconic testing setup and use a CC peripheral to inspect the reactor and Flux Gate API. **Do not activate an untested reactor in a valued world.** A Lua controller is an extra protective layer, not an infallible failsafe.
7. Run `python3 scripts/validate_cc_iesnium_pilot.py` for the staged source and packwiz hashes; this is static validation only and **does not prove the BetterEnd mod or its dependencies are installed**.
