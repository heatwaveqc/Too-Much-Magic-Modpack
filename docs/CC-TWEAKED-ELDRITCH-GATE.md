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

## Easier Iesnium through real Spirit Fire

**Source-verified recipe:** `1 draconicevolution:end_draconium_ore` thrown into **Occultism Spirit Fire** produces `1 occultism:iesnium_ore`. Data path:
`kubejs/data/tmm/recipe/spirit_fire/iesnium_from_end_draconium.json`.

This is a genuine Occultism `occultism:spirit_fire` recipe, not a KubeJS item-entity scan. The native SpiritFireBlock looks for a matching single-input recipe when an item entity enters the fire and drops the result. **The exact 1.227.0 recipe assembly preserves the input stack count**. It makes Iesnium accessible through End exploration while retaining the normal Nether/Otherworld routes. **Not a strict End-only gate:** Occultism also has a generic Draconium Ore miner result using the shared `c:ores/draconium` tag, which includes the End variant in Draconic Evolution's 1.21 source. Confirm actual loaded miner outputs if early access is undesirable.

**Silk Touch note:** The matching Draconic Evolution loot table normally drops 2–4 Draconium Dust from End Draconium Ore; obtaining the ore **block** for the fire requires Silk Touch (or another approved ore-block route). This deliberate block-input requirement prevents converting freely obtained Overworld Draconium Dust straight into Iesnium. If the player experience remains too annoying, consider a separate *End Stone + Draconium Dust* item pre-craft rather than unrestricted dust-to-Iesnium conversion.

**End Silver / End Lead:** Not identified as registered pack materials from the selected mod source inventories. Occultism's source has Overworld Silver and Deepslate Silver; its generic Lead Ore miner recipe uses `c:ores/lead`. Neither constitutes an End Silver or End Lead block. **Do not create phantom ore IDs**.

## Source-backed contracts

- [CC: Tweaked 1.121.0 NeoForge file, Modrinth version KNdTEfUA](https://modrinth.com/mod/cc-tweaked/version/KNdTEfUA); 1.21.1 beta `cc-tweaked-1.21.1-forge-1.121.0.jar`, published SHA-1 `946c83390aadfe0d8077198fcf22804cee206637`. Actual pack download/hash verification pending.
- [CC 1.121.0 computer crafting source](https://github.com/cc-tweaked/CC-Tweaked/tree/v1.21.1-1.121.0/projects/common/src/generated/resources/data/computercraft/recipe). All four corresponding direct recipe IDs replaced in the pilot.
- [Draconic Evolution native CC support](https://github.com/Draconic-Inc/Draconic-Evolution/tree/1.21/src/main/java/com/brandon3055/draconicevolution/integration/computers). Reactor exposes telemetry (temperature, field charge/drain, saturation, fuel and generation), activation, safe-shutdown request and native fail-safe toggle. Flux Gates support flow overrides; Pylons expose energy/flow.
- [Occultism matching Spirit Fire recipe codec](https://github.com/klikli-dev/occultism/blob/990303e/src/main/java/com/klikli_dev/occultism/crafting/recipe/SpiritFireRecipe.java), [Spirit Fire collision handler](https://github.com/klikli-dev/occultism/blob/990303e/src/main/java/com/klikli_dev/occultism/common/block/SpiritFireBlock.java), and [Eldritch miner item tag](https://github.com/klikli-dev/occultism/blob/990303e/src/generated/resources/data/occultism/tags/item/miners/eldritch.json).
- [End Draconium Ore loot table](https://github.com/Draconic-Inc/Draconic-Evolution/blob/1.21/src/main/generated/data/draconicevolution/loot_table/blocks/end_draconium_ore.json): Silk Touch produces block; ordinary mining produces dust.

## Mandatory disposable-world acceptance

1. Verify the CC beta's exact JAR SHA and presence on **both client and dedicated server**, with no extra unapproved dependencies.
2. Restart after KubeJS startup registration; inspect `tmm:eldritch_logic_core` in JEI and confirm its texture and ID.
3. Confirm the live Occultism recipe manager registers `tmm:miner/eldritch_logic_core`; run an **Ancient Eldritch Miner** in a Dimensional Mineshaft long enough to witness at least one core. Run Marid/earlier miners to confirm none can yield it. Check the actual frequency and server logs.
4. Check all four base CC recipes in JEI require the core. Verify normal/advanced turtles and pocket-computer upgrade recipes cannot bypass it. Check loot, Quest Shop, duplication and other source paths before calling the gate complete.
5. Mine `draconicevolution:end_draconium_ore` with Silk Touch. Throw one block, then a stack, into Spirit Fire and confirm matching Iesnium Ore counts with no duplication; verify unrelated items and ordinary End Stone do not transmute.
6. Construct a small safe Draconic testing setup and use a CC peripheral to inspect the reactor and Flux Gate API. **Do not activate an untested reactor in a valued world.** A Lua controller is an extra protective layer, not an infallible failsafe.
7. Validate that running `python3 scripts/validate_cc_iesnium_pilot.py` passes before packaging; this is static validation only.
