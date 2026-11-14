# Exceptional Power: Reactors — TMM build and test guide

**Target:** Minecraft 1.21.1, NeoForge 21.1.265, BiggerReactors Rework 0.1.6 and Draconic Evolution 3.1.4.633 (experimental). **Candidate quest guidance, not an in-game verified construction manual.** The user approved making this chapter the pack's **entire technology progression**. No additional FE transport, logistics, AE2, Create or Refined Storage mods.

**Architecture:** the machines do different jobs. BiggerReactors Rework produces FE from uranium fuel, can buffer FE in its own modular Battery, and optionally supports steam turbines and heat exchangers. Draconic Evolution provides its own Generator, high-capacity **Energy Core bank**, Energy Pylons and crystals, and a **separate extremely dangerous Draconic Reactor** for much later power generation.

## Track 1 — build a BiggerReactors Rework passive generator

1. **Get the Reactor Guide.** It contains current version-specific multiblock diagrams, a calculator and construction troubleshooting. The namespace is `biggerreactorsrework:`, not the older BiggerReactors mod ID.
2. **Prepare fuel and materials.** Uranium Ingots feed the reactor; graphite is usable as moderator material. The pack *intends* to restrict uranium to difficult mining and/or advanced Occultism; **that ore gating has not yet been implemented**, so the quest does not pretend it has.
3. **Build a closed rectangular shell.** Casing is required on frame edges and corners, while appropriate faces may use Reactor Glass. The build needs a Reactor Terminal, continuous interior Fuel Rod columns reaching the roof, and a Control Rod directly above every fuel column. Check the built-in guide for allowed size and minimum valid placements; do not reuse old Extreme Reactors diagrams without verification.
4. **Select a generation mode.** For simple, **passive** FE generation install a Reactor Access Port and a Reactor Power Tap on exterior faces. Fuel comes in and cyanite waste comes out through Access Ports. A passively cooled reactor exports FE through power taps. **Actively cooled** reactor designs use coolant ports, not an arbitrary mix of power taps and coolant ports.
5. **Start conservatively.** Check the terminal's assembly messages; feed uranium, connect the FE output to a consumer or bank, activate the reactor and watch temperature, output, fuel use and waste. Rod insertion reduces reaction rate. Don't mark the operational quest completed until you've actually seen safe, stable FE generation.
6. **Optional industry.** Collect Cyanite Ingots for the Industrial Processor/reprocessing route. Steam turbines require a proper shaft between two bearings, blades, coils, configured fluid ports and an FE output; active coolant setups may use a Heat Exchanger. These are optional specialist branches, **not** prerequisites for the simple passive FE path.

### Rework's own Battery

Build a rectangular shell with **Casing frame**, Battery Cells in the interior, a Battery Terminal, and **at least two Battery Energy Ports** (one input, one output). The machine requires at least one cell. Configure a port's direction in its UI or with the Reactor Wrench and link your generation source/consumer; verify the terminal shows charge and output. Rework's own guide states charged cells retain their internal energy when moved. This is a useful interim FE buffer—not the gigantic multiblock **Draconic Energy Core**.

## Track 2 — Draconic Evolution energy storage and transfer

1. **Make the Energy Core block** (`draconicevolution:energy_core`) and **four Energy Core Stabilizers** (`draconicevolution:energy_core_stabilizer`). Place the central core at a protected site. The Energy Core is **storage**, not the Draconic Reactor.
2. Open the core's GUI and choose the target **tier**. Use its **Build Guide** and **Assemble Core** controls to see the correct spherical shell of materials, the valid stabilizer placement and the exact requirements for this version. Larger tiers take more material. Begin with a small tier and increase later.
3. Prepare **Energy Pylons** (`draconicevolution:energy_pylon`) for **input and output**, position and link them correctly, then set each I/O direction. Pylon builds may require adjacent glass/crystal formation: check the installed GUI/tooltips rather than assuming old multiblock offsets.
4. Activate the completed bank and connect a power source; verify stored FE rises. Connect a load to the output and verify it decreases. Avoid dismantling an active/charged core until its energy-loss behavior is verified.
5. For distribution, build a **Basic Energy I/O Crystal**, and later choose relay/wireless crystals where appropriate. Draconic Evolution's crystal network is the intended FE transfer path; do not add a generic power-cable mod.

## Track 3 — high-risk Draconic Reactor

**This is not the BiggerReactors generator, and it is not the Energy Core battery.** Reactor construction uses `draconicevolution:reactor_core`, **four** `draconicevolution:reactor_stabilizer` blocks, an energy injector (`draconicevolution:reactor_injector`), fuel and controlled FE transfer. The Reactor Stabilizer is distinct from the Energy Core Stabilizer. Consult the installed JEI recipe for the Reactor Core; some packs/versions require **Chaos Shards**. The quests deliberately award no shards or reactor blocks, and **do not bypass the Chaos Guardian/ProjectE gate**.

1. **Isolate the site**: build far from your home, protected infrastructure and valued chunks. Test initial configurations in a disposable world, not the player's valued survival base.
2. **Place the core and four stabilizers** in the supported symmetric arrangement with clear line of sight. Add the Reactor Energy Injector in the appropriate orientation/position from the installed reactor UI or guide.
3. **Separate the containment supply from power extraction**. Feed the injector continuously from a stable, buffered source. Use Flux Gates and Draconic energy I/O to control flow and keep the output from draining the safety supply. Plan a failure mode before introducing fuel.
4. **Monitor the actual GUI**: temperature, containment field, energy saturation and fuel conversion. Do not copy historic safe-temperature thresholds from older mod versions. Keep sufficient reserve power for containment during transient loads; test shutdown while no valuable world assets are nearby.
5. **Only then activate fuel and power production**. The final quests are **manual checkmarks**, not telemetry sensors. A book claim cannot prove safety, and a failed containment field can destroy substantial terrain.

## Quest and milestone policy

- FTB Quests item objectives count item possession, not an assembled multiblock, so "Assemble and Start a Bigger Reactor", "Operate the Nuclear Battery", "Activate the Draconic Power Bank", and the two Draconic Reactor safety entries are **explicit manual confirmation tasks**, not fake auto-detection.
- All 32 new tech quests award positive XP and a small, fixed useful item (mostly wiring components, redstone, glass, fuel, and monitoring parts). They never grant a subsequent quest's machine block, missing boss token or progression unlock.
- Building one technology does not require every magic school. Turbine/exchanger and some network components are optional branches.
- The power chapter guides the FE infrastructure; *the dedicated Endgame Draconic Evolution chapter* will own full boss and late crafting rewards and must not duplicate technology component objectives.

## Source references and remaining checks

- [BiggerReactors-Rework source and integrated Guide](https://github.com/vindocel/BiggerReactors-Rework), specifically `src/main/resources/assets/biggerreactorsrework/lang/en_us.json` and `client/guide/GuideContent.java`. The guide confirms shell/frame, terminals, fuel columns, Power Taps, battery ports/cells and turbine/heat-exchanger options.
- [Draconic Evolution official source 1.21 branch](https://github.com/Draconic-Inc/Draconic-Evolution/tree/1.21), including generated English block IDs (`energy_core`, `energy_core_stabilizer`, `energy_pylon`, `reactor_core`, `reactor_stabilizer`, `reactor_injector`, `flux_gate`).
- [Draconic Evolution 1.21.1 3.1.4.633 beta release](https://modrinth.com/mod/draconic-evolution/version/3.1.4.633).

**Still unverified in-game:** exact modded item IDs in the pinned binary, recipes, FE behavior, multiblock rendering/dimensions, stable draconic containment, item-claim and cooldown semantics, and real tech-ore scarcity. The server smoke only proves a clean startup; do not claim any reactor actually built or operated.
